package com.ullas.easymetro.metro;

import java.util.HashMap;
import java.util.Map;

public class Station {

    private String name;

    private Station(String name) {
        this.name = name;
    }

    public static Station getStation(String name) {
        return StationFactory.getStation(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Station) {
            return ((Station) obj).getName().equals(this.getName());
        }
        return super.equals(obj);
    }

    private static class StationFactory {

        private static Map<String, Station> mStations = new HashMap<>();

        public static Station getStation(String name) {
            Station station = mStations.get(name);
            if (station == null) {
                station = new Station(name);
                mStations.put(name, station);
            }
            return station;
        }
    }
}
