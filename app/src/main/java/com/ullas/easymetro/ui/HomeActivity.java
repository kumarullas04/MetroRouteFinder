package com.ullas.easymetro.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.ullas.easymetro.R;
import com.ullas.easymetro.constants.Constants;
import com.ullas.easymetro.interfaces.IPathReceiver;
import com.ullas.easymetro.metro.DijkstrasAlgorithm;
import com.ullas.easymetro.metro.MetroMapParser;
import com.ullas.easymetro.metro.ShortestPath;
import com.ullas.easymetro.metro.Station;
import com.ullas.easymetro.interfaces.IDataModel;
import com.ullas.easymetro.interfaces.IShortestPathAlgorithm;

public class HomeActivity extends AppCompatActivity implements IPathReceiver {

    private static final String TAG = HomeActivity.class.getSimpleName();
    IDataModel mDataSource;
    private TextView mResultTextView;
    private TextView mPathTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        MetroMapParser parser = new MetroMapParser();
        mDataSource = parser.parse(loadJsonFileFromAsset());
        initViews();
        if (mDataSource.isStatusSuccess()) {
            initViews();
        } else {
            Toast.makeText(this, R.string.json_load_failed, Toast.LENGTH_SHORT).show();
        }
    }

    private void initViews() {
        mResultTextView = (TextView) findViewById(R.id.result_text_view);
        mPathTextView = (TextView) findViewById(R.id.path_text_view);
        final Spinner sourceSpinner = (Spinner) findViewById(R.id.source_spinner);
        sourceSpinner.setAdapter(new SpinnerAdapter(this, R.layout.spinner_item, mDataSource.getStations()));
        final Spinner destinationSpinner = (Spinner) findViewById(R.id.destination_spinner);
        destinationSpinner.setAdapter(new SpinnerAdapter(this, R.layout.spinner_item, mDataSource.getStations()));

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Station sourceStation = (Station) sourceSpinner.getSelectedItem();
                Station destinatinStation = (Station) destinationSpinner.getSelectedItem();
                findShortestPath(sourceStation, destinatinStation);
            }
        });
    }

    public JSONObject loadJsonFileFromAsset() {
        JSONObject jsonObject = null;
        try {
            String json = null;
            InputStream is = getAssets().open(Constants.METRO_JSON_FILE_NAME);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            jsonObject = new JSONObject(json);
        } catch (IOException e) {
           //ignore
            e.printStackTrace();
        } catch (JSONException e) {
            //ignore
            e.printStackTrace();
        }
        return jsonObject;
    }

    private void findShortestPath(Station source, Station destination) {
        IShortestPathAlgorithm algorithm = new DijkstrasAlgorithm(mDataSource);
        algorithm.execute(source, destination, this);
    }

    @Override
    public void onPathFound(ShortestPath shortestPath) {
        String result = getString(R.string.the_minimum_cost) + shortestPath.getTotalCost() + getString(R.string.currency_symbol);
        result += "\n\n" + getString(R.string.time_it_would_take) + shortestPath.getDuration() + " " + getString(R.string.time_unit);
        ArrayList<Station> list = shortestPath.getDestinationPath();
        String path = "";
        if (list != null && !list.isEmpty()) {

            for (Station station : list) {
                if (!TextUtils.isEmpty(path)) {
                    path += " -> ";
                }
                path += station.getName();
            }
        } else {
            mResultTextView.setText(R.string.no_path_found);
            mPathTextView.setText("");
        }

        mResultTextView.setText(result);
        mPathTextView.setText(path);
    }

    @Override
    public void onPathNotFound() {
        mResultTextView.setText(R.string.no_path_found);
        mPathTextView.setText("");
    }
}
