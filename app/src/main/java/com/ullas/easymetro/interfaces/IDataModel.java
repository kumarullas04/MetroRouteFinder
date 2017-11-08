package com.ullas.easymetro.interfaces;

import java.util.ArrayList;

import com.ullas.easymetro.metro.Station;
import com.ullas.easymetro.metro.Track;

public interface IDataModel {

    public boolean isStatusSuccess();

    public ArrayList<Station> getStations();

    public ArrayList<Track> getTracks();

    public int getLineSwitchCost();

}
