package com.astar;

public class AGrid implements Comparable<AGrid> {

    // 横坐标
    int row;
    // 纵坐标
    int col;
    // 连接该节点的父节点
    AGrid parent;
    // 从起点到该节点已经走过的距离
    int f;
    // 从该节点到终点的曼哈顿距离
    int g;
    // f + g
    int h;

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
        return "(" + col + "," + row + ")";
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

}
