package com.map;

/**
 * Created by zhanghaojie on 2017/11/10.
 * 地图数据信息
 */
public class DictMapDataVO {

    private short height;
    private short width;
    private byte[][] data;

    public boolean isValidPos(int x, int y) {
        return x >= 0 && y >= 0 && x < width && y < height;
    }

    /**
     * 可行走的格子
     *
     * @param xx
     * @param yy
     * @return
     */
    public boolean isWalkable(int xx, int yy) {
        return isValidPos(xx, yy) && data[yy][xx] != 1;
    }

    public short getHeight() {
        return height;
    }

    public void setHeight(short height) {
        this.height = height;
    }

    public short getWidth() {
        return width;
    }

    public void setWidth(short width) {
        this.width = width;
    }

    public byte[][] getData() {
        return data;
    }

    public void setData(byte[][] data) {
        this.data = data;
    }

}
