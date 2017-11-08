package com.ullas.easymetro.interfaces;

import com.ullas.easymetro.metro.Station;

public interface IShortestPathAlgorithm {

    public void execute(Station source, Station destination, IPathReceiver receiver);

}
