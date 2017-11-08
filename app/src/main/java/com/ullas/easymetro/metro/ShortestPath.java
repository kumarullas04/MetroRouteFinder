package com.ullas.easymetro.metro;

import java.util.ArrayList;

public class ShortestPath {
    ArrayList<Station> mDestinationPath;
    private int mTotalCost;
    private int mDuration;

    public ArrayList<Station> getDestinationPath() {
        return mDestinationPath;
    }

    public void setDestinationRoute(ArrayList<Station> destinationRoute) {
        this.mDestinationPath = destinationRoute;
    }

    public int getTotalCost() {
        return mTotalCost;
    }

    public void setTotalCost(int totalCost) {
        this.mTotalCost = totalCost;
    }

    public int getDuration() {
        return mDuration;
    }

    public void setDuration(int duration) {
        this.mDuration = duration;
    }
}
