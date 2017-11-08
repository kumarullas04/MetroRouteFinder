package com.ullas.easymetro.metro;

import java.util.ArrayList;

import com.ullas.easymetro.interfaces.IDataModel;

public class MetroMapDataModel implements IDataModel {

    private static final String TAG = MetroMapDataModel.class.getSimpleName();
    private ArrayList<Station> mStationsList = new ArrayList<>();
    private ArrayList<Track> mTrackList = new ArrayList<>();

    private int mTicketCost;
    private int mTimeBetweenStation;
    private int mLineSwitchCost = 0;
    private boolean mIsStatusSuccess;

    public MetroMapDataModel() {
    }

    public int getTicketCost() {
        return mTicketCost;
    }

    public void setTicketCost(int mTicketCost) {
        this.mTicketCost = mTicketCost;
    }

    public int getTimeBetweenStation() {
        return mTimeBetweenStation;
    }

    public void setTimeBetweenStation(int mTimeBetweenStation) {
        this.mTimeBetweenStation = mTimeBetweenStation;
    }

    public void setLineSwitchCost(int mLineSwitchCost) {
        this.mLineSwitchCost = mLineSwitchCost;
    }

    public void setStations(ArrayList<Station> stations) {
        mStationsList = stations;
    }

    public void setTracks(ArrayList<Track> tracks) {
        mTrackList = tracks;
    }

    public void setIsStatusSuccess(boolean isStatusSuccess) {
        this.mIsStatusSuccess = isStatusSuccess;
    }

    @Override
    public boolean isStatusSuccess() {
        return mIsStatusSuccess;
    }

    @Override
    public ArrayList<Station> getStations() {
        return mStationsList;
    }

    @Override
    public ArrayList<Track> getTracks() {
        return mTrackList;
    }

    @Override
    public int getLineSwitchCost() {
        return mLineSwitchCost;
    }
}
