package com.map;

public class GridWrapper<T> {

    private T grid;
    private int offsetMS;

    public GridWrapper(T g) {
        this.grid = g;
    }

    public int getOffsetMS() {
        return offsetMS;
    }

    public void setOffsetMS(int offsetMS) {
        this.offsetMS = offsetMS;
    }

    public T getGrid() {
        return grid;
    }
}
