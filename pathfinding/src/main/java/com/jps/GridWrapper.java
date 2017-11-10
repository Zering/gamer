package com.jps;

public class GridWrapper {

    private Grid grid;
    private int offsetMS;

    public GridWrapper(Grid g) {
        this.grid = g;
    }

    public int getOffsetMS() {
        return offsetMS;
    }

    public void setOffsetMS(int offsetMS) {
        this.offsetMS = offsetMS;
    }

    public Grid getGrid() {
        return grid;
    }
}
