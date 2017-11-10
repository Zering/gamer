package com.map;

/**
 * Created by zhanghaojie on 2017/11/10.
 * 地图基本定义信息
 */
public class DictMapDefineVO {

    // 地图ID
    private short mapid;
    // 地图类型
    private byte mapType;
    // 大格子X数
    private short gridX;
    // 大格子Y数
    private short gridY;

    private DictMapDataVO mapData;

    public short getMapid() {
        return mapid;
    }

    public void setMapid(short mapid) {
        this.mapid = mapid;
    }

    public byte getMapType() {
        return mapType;
    }

    public void setMapType(byte mapType) {
        this.mapType = mapType;
    }

    public short getGridX() {
        return gridX;
    }

    public void setGridX(short gridX) {
        this.gridX = gridX;
    }

    public short getGridY() {
        return gridY;
    }

    public void setGridY(short gridY) {
        this.gridY = gridY;
    }

    public DictMapDataVO getMapData() {
        return mapData;
    }

    public void setMapData(DictMapDataVO mapData) {
        this.mapData = mapData;
    }
}
