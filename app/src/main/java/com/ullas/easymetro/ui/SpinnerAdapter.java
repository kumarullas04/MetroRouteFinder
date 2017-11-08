package com.ullas.easymetro.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import com.ullas.easymetro.R;
import com.ullas.easymetro.metro.Station;


public class SpinnerAdapter extends ArrayAdapter {

    private ArrayList<Station> mStationsList;

    @Override
    public int getCount() {
        return mStationsList.size();
    }

    @Nullable
    @Override
    public Station getItem(int position) {
        return mStationsList.get(position);
    }

    public SpinnerAdapter(Context context, int textViewId, ArrayList<Station> stationsList) {
        super(context, textViewId);
        mStationsList = stationsList;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View view = super.getView(position, convertView, parent);
        TextView textView = (TextView) view.findViewById(R.id.text_view);
        textView.setText(mStationsList.get(position).getName());
        return view;
    }


    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = super.getDropDownView(position, convertView, parent);
        TextView textView = (TextView) view.findViewById(R.id.text_view);
        textView.setText(mStationsList.get(position).getName());
        return view;
    }
}
