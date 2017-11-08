package com.ullas.easymetro.metro;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.ullas.easymetro.constants.Constants;
import com.ullas.easymetro.constants.JsonConstants;
import com.ullas.easymetro.interfaces.IDataModel;
import com.ullas.easymetro.interfaces.IParser;

public class MetroMapParser implements IParser {

    private int mTicketCost = Constants.DEFAULT_COST;
    private int mTimeBetweenStation = Constants.DEFAULT_DURATION;

    @Override
    public IDataModel parse(JSONObject json) {
        MetroMapDataModel model = new MetroMapDataModel();
        if (json != null) {
            try {
                if (json.has(JsonConstants.DEFAULT_TICKET_COST_PER_STATION)) {
                    mTicketCost = json.getInt(JsonConstants.DEFAULT_TICKET_COST_PER_STATION);
                    model.setTicketCost(mTicketCost);
                }
                if (json.has(JsonConstants.LINE_SWITCH_COST)) {
                    model.setLineSwitchCost(json.getInt(JsonConstants.LINE_SWITCH_COST));
                }
                if (json.has(JsonConstants.DEFAULT_TIME_BETWEEN_STATION)) {
                    mTimeBetweenStation = json.getInt(JsonConstants.DEFAULT_TIME_BETWEEN_STATION);
                    model.setTimeBetweenStation(mTimeBetweenStation);
                }
                if (json.has(JsonConstants.STATIONS)) {
                    model.setStations(parseStations(json.getJSONArray(JsonConstants.STATIONS)));
                }
                if (json.has(JsonConstants.TRACKS)) {
                    model.setTracks(parseTracks(json.getJSONArray(JsonConstants.TRACKS)));
                }
                model.setIsStatusSuccess(true);
            } catch (JSONException e) {
                e.printStackTrace();
                model.setIsStatusSuccess(false);
            }
        } else {
            model.setIsStatusSuccess(false);
        }
        return model;
    }

    private ArrayList<Track> parseTracks(JSONArray jsonArray) throws JSONException {
        ArrayList<Track> trackList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject json = (JSONObject) jsonArray.get(i);
            if (!json.has(JsonConstants.IS_ACTIVE) ||
                    (json.has(JsonConstants.IS_ACTIVE) && json.getBoolean(JsonConstants.IS_ACTIVE))) {

                Station station1 = json.has(JsonConstants.STATION1) ?
                        Station.getStation(json.getString(JsonConstants.STATION1)) : null;
                Station station2 = json.has(JsonConstants.STATION2) ?
                        Station.getStation(json.getString(JsonConstants.STATION2)) : null;
                Track.TrackTypes trackType = Track.TrackTypes.fromString(json.optString(JsonConstants.TRACK_TYPE));
                Track track = new Track(trackType, station1, station2);

                if (json.has(JsonConstants.TICKET_COST)) {
                    track.setCost(json.getInt(JsonConstants.TICKET_COST));
                } else {
                    track.setCost(mTicketCost);
                }

                if (json.has(JsonConstants.TIME_DURATION)) {
                    track.setDuration(json.getInt(JsonConstants.TIME_DURATION));
                } else {
                    track.setDuration(mTimeBetweenStation);
                }
                trackList.add(track);
                trackList.add(getReverseTrack(track));
            }
        }
        return trackList;
    }

    private Track getReverseTrack(Track track) {
        Track reverseTrack = new Track(track);
        Station temp = reverseTrack.getFromStation();
        reverseTrack.setFromStation(reverseTrack.getToStation());
        reverseTrack.setToStation(temp);
        return reverseTrack;
    }

    private ArrayList<Station> parseStations(JSONArray jsonArray) throws JSONException {
        ArrayList<Station> stationList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject json = (JSONObject) jsonArray.get(i);
            if (json.has(JsonConstants.NAME)) {
                if (!json.has(JsonConstants.IS_ACTIVE) ||
                        (json.has(JsonConstants.IS_ACTIVE) && json.getBoolean(JsonConstants.IS_ACTIVE))) {
                    Station station = Station.getStation(json.getString(JsonConstants.NAME));
                    stationList.add(station);
                }
            }
        }
        return stationList;
    }
}
