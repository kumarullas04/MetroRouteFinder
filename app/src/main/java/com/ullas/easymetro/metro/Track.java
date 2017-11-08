package com.ullas.easymetro.metro;

public class Track {

    private TrackTypes mTrackType;
    private Station mFromStation;
    private Station mToStation;
    private int mCost;
    private int mDuration;

    public Track(TrackTypes trackType, Station fromStation, Station toStation) {
        this.mTrackType = trackType;
        this.mFromStation = fromStation;
        this.mToStation = toStation;
    }

    public Track(Track track) {
        mTrackType = track.getTrackType();
        mFromStation = track.getFromStation();
        mToStation = track.getToStation();
        mCost = track.getCost();
        mDuration = track.getDuration();
    }

    public TrackTypes getTrackType() {
        return mTrackType;
    }

    public void setTrackType(TrackTypes mTrackType) {
        this.mTrackType = mTrackType;
    }

    public int getCost() {
        return mCost;
    }

    public void setCost(int cost) {
        this.mCost = cost;
    }

    public int getDuration() {
        return mDuration;
    }

    public void setDuration(int mDuration) {
        this.mDuration = mDuration;
    }

    public Station getFromStation() {
        return mFromStation;
    }

    public Station getToStation() {
        return mToStation;
    }

    public void setFromStation(Station station) {
        mFromStation = station;
    }

    public void setToStation(Station station) {
        mToStation = station;
    }

    public enum TrackTypes {

        RED("RED"),
        GREEN("GREEN"),
        BLUE("BLUE"),
        YELLOW("YELLOW"),
        BLACK("BLACK");

        private String mTrackType;

        TrackTypes(String trackType) {
            mTrackType = trackType;
        }

        @Override
        public String toString() {
            return mTrackType;
        }

        public static TrackTypes fromString(String trackType) {
            for (TrackTypes type : values()) {
                if (type.toString().equalsIgnoreCase(trackType)) {
                    return type;
                }
            }
            return null;
        }
    }
}
