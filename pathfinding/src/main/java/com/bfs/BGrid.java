package com.bfs;

/**
 * Created by zhanghaojie on 2017/11/14.
 * 节点
 */
public class BGrid {

    int row;
    int col;
    BGrid parent;

    public BGrid(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public BGrid(BGrid p) {
        this.row = p.row;
        this.col = p.col;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        BGrid bGrid = (BGrid) o;

        return row == bGrid.row && col == bGrid.col;
    }

    @Override
    public int hashCode() {
        int result = row;
        result = 31 * result + col;
        return result;
    }

    @Override
    public String toString() {
        return "BGrid{" + "row=" + row + ", col=" + col + '}';
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public BGrid getParent() {
        return parent;
    }

    public void setParent(BGrid parent) {
        this.parent = parent;
    }
}
