package com.astar;

public class AGrid implements Comparable<AGrid> {

    int row;
    int col;
    AGrid parent;
    int f;
    int g;
    int h;
    private double moveTime;
    private double moveDistance;
    private double passMS;

    public AGrid(int row, int col) {
        this.row = row;
        this.col = col;
        this.f = 0;
        this.g = 0;
        this.h = 0;
    }

    public AGrid(AGrid p) {
        this(p.row, p.col);
    }

    public double getMoveDistance(AGrid from) {
        if (from.col == col) {//竖向移动
            return AStarPathFind.TILE_HEIGHT;
        } else if (from.row == row) {//横向移动
            return AStarPathFind.TILE_WIDTH;
        } else {//斜向移动
            return AStarPathFind.TILE_DIAGONAL;
        }
    }

    public int getG() {
        return g;
    }

    @Override
    public int compareTo(AGrid o) {
        if (f == o.f) {
            return g - o.g;
        }
        return f - o.f;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + row;
        result = prime * result + col;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AGrid other = (AGrid) obj;
        if (row != other.row)
            return false;
        if (col != other.col)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "(" + col + "," + row + "," + moveTime + ")";
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public double getMoveTime() {
        return moveTime;
    }

    public void setMoveTime(double moveTime) {
        this.moveTime = moveTime;
    }

    public double getMoveDistance() {
        return moveDistance;
    }

    public void setMoveDistance(double moveDistance) {
        this.moveDistance = moveDistance;
    }

    public double getPassMS() {
        return passMS;
    }

    public void setPassMS(double passMS) {
        this.passMS = passMS;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

}
