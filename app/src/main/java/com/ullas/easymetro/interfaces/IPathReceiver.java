package com.ullas.easymetro.interfaces;


import com.ullas.easymetro.metro.ShortestPath;

public interface IPathReceiver {

    public void onPathFound(ShortestPath shortestPath);

    public void onPathNotFound();

}
