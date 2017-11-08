package com.ullas.easymetro.metro;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ullas.easymetro.constants.Constants;
import com.ullas.easymetro.enums.PathPreferenceTypes;
import com.ullas.easymetro.interfaces.IDataModel;
import com.ullas.easymetro.interfaces.IPathReceiver;
import com.ullas.easymetro.interfaces.IShortestPathAlgorithm;

public class DijkstrasAlgorithm implements IShortestPathAlgorithm {

    private static final String TAG = DijkstrasAlgorithm.class.getSimpleName();
    private final List<Station> mMetroStations;
    private final List<Track> mMetroTracks;
    private Set<Station> mSettledStations;
    private Set<Station> mUnSettledStations;
    private Map<Station, Station> mPreviousStations;
    private Map<Station, Integer> mDurations;
    private Map<Station, Integer> mCost;
    private int mLineSwitchCost = 0;
    private Station mSourceStation;

    public DijkstrasAlgorithm(IDataModel metroDataSource) {
        this.mMetroStations = new ArrayList<>(metroDataSource.getStations());
        this.mMetroTracks = new ArrayList<>(metroDataSource.getTracks());
        mLineSwitchCost = metroDataSource.getLineSwitchCost();
    }

    public void execute(Station source, Station destination, IPathReceiver receiver) {
        initialize();
        mSourceStation = source;
        mDurations.put(source, 0);
        mCost.put(source, 0);
        mUnSettledStations.add(source);
        while (mUnSettledStations.size() > 0) {
            if (Constants.PATH_TYPE == PathPreferenceTypes.MIN_COST) {
                Station node = getMinimumDurationStation(mUnSettledStations);
                mSettledStations.add(node);
                mUnSettledStations.remove(node);
                findMinimalCost(node);
            } else {
                Station node = getMinimumCostStation(mUnSettledStations);
                mSettledStations.add(node);
                mUnSettledStations.remove(node);
                findMinimalDistances(node);
            }
        }
        notifyResult(destination, receiver);
    }

    private void initialize() {
        mSettledStations = new HashSet<>();
        mUnSettledStations = new HashSet<>();
        mDurations = new HashMap<>();
        mCost = new HashMap<>();
        mPreviousStations = new HashMap<>();
    }

    private Station getMinimumDurationStation(Set<Station> stations) {
        Station minimum = null;
        for (Station station : stations) {
            if (minimum == null) {
                minimum = station;
            } else {
                if (getShortestDuration(station) < getShortestDuration(minimum)) {
                    minimum = station;
                }
            }
        }
        return minimum;
    }

    private Station getMinimumCostStation(Set<Station> stations) {
        Station minimum = null;
        for (Station station : stations) {
            if (minimum == null) {
                minimum = station;
            } else {
                if (getMinimumCost(station) < getMinimumCost(minimum)) {
                    minimum = station;
                }
            }
        }
        return minimum;
    }

    private void findMinimalDistances(Station station) {
        List<Station> adjacentNodes = getNeighborStations(station);
        for (Station target : adjacentNodes) {
            if (getShortestDuration(target) > (getShortestDuration(station) + getDuration(station, target))) {
                mDurations.put(target, getShortestDuration(station)
                        + getDuration(station, target));
                mCost.put(target, getMinimumCost(station) +
                        getCost(mPreviousStations.get(station), station, target));
                mPreviousStations.put(target, station);
                mUnSettledStations.add(target);
            }
        }
    }

    private void findMinimalCost(Station station) {
        List<Station> adjacentNodes = getNeighborStations(station);
        for (Station target : adjacentNodes) {
            if (getMinimumCost(target) > getMinimumCost(station)
                    + getCost(mPreviousStations.get(station), station, target)) {
                mDurations.put(target, getShortestDuration(station)
                        + getDuration(station, target));
                mCost.put(target, getMinimumCost(station) +
                        getCost(mPreviousStations.get(station), station, target));
                mPreviousStations.put(target, station);
                mUnSettledStations.add(target);
            }
        }
    }

    private List<Station> getNeighborStations(Station station) {
        List<Station> neighbors = new ArrayList<Station>();
        for (Track track : mMetroTracks) {
            if (track.getFromStation().equals(station)
                    && !isSettled(track.getToStation())) {
                neighbors.add(track.getToStation());
            }
        }
        return neighbors;
    }

    private int getShortestDuration(Station destination) {
        Integer minDuration = mDurations.get(destination);
        return minDuration == null ? Integer.MAX_VALUE : minDuration;
    }

    private int getMinimumCost(Station destination) {
        Integer minCost = mCost.get(destination);
        return minCost == null ? Integer.MAX_VALUE : minCost;
    }

    private int getDuration(Station fromStation, Station target) {
        for (Track edge : mMetroTracks) {
            if (edge.getFromStation().equals(fromStation)
                    && edge.getToStation().equals(target)) {
                return edge.getDuration();
            }
        }
        return 0;
    }

    private int getCost(Station previousStation, Station fromStation, Station target) {
        int cost = 0;
        Track.TrackTypes trackType = null;
        boolean found = false;
        for (Track edge : mMetroTracks) {
            if (edge.getFromStation().equals(fromStation)
                    && edge.getToStation().equals(target)) {
                cost += edge.getCost();
                if (previousStation == null) break;
                if (found) {
                    if (trackType != edge.getTrackType()) {
                        cost += mLineSwitchCost;
                    }
                    break;
                } else {
                    found = true;
                    trackType = edge.getTrackType();
                }
            } else if (previousStation != null && edge.getFromStation().equals(previousStation)
                    && edge.getToStation().equals(fromStation)) {
                if (found) {
                    if (trackType != edge.getTrackType()) {
                        cost += mLineSwitchCost;
                    }
                    break;
                } else {
                    found = true;
                    trackType = edge.getTrackType();
                }
            }
        }
        return cost;
    }

    private boolean isSettled(Station station) {
        return mSettledStations.contains(station);
    }

    private ArrayList<Station> getShortestPath(Station target) {
        ArrayList<Station> path = new ArrayList<>();

            if (mPreviousStations.containsKey(target)) {
                Station station = mPreviousStations.get(target);
                path.add(station);
                while (mPreviousStations.get(station) != null) {
                    station = mPreviousStations.get(station);
                    path.add(station);
                }
                Collections.reverse(path);
            }

        return path;
    }

    private void notifyResult(Station target, IPathReceiver receiver) {
        ArrayList<Station> path = getShortestPath(target);
        if (path == null || path.isEmpty()) {
            receiver.onPathNotFound();
        } else {
            ShortestPath pathResult = new ShortestPath();
            pathResult.setDestinationRoute(path);
            pathResult.setDuration(getShortestDuration(target));
            pathResult.setTotalCost(getMinimumCost(target));
            receiver.onPathFound(pathResult);
        }
    }

   /* private ArrayList<Track> getTracks(ArrayList<Station> path) {
        ArrayList<Track> tracks = new ArrayList<>();
        if (path == null || path.size() <= 0) return null;
        Station start = path.get(0);
        for (int i = 1; i < path.size(); i++) {
            tracks.add(getTrack(start, path.get(i)));
            start = path.get(i);
        }
        return tracks;
    }

    private Track getTrack(Station fromStation, Station target) {
        for (Track track : mMetroTracks) {
            if (track.getFromStation().equals(fromStation)
                    && track.getToStation().equals(target)) {
                return track;
            }
        }
        return null;
    }*/
}
