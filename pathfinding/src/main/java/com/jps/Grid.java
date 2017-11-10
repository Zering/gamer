package com.jps;

public class Grid {

    short x;
    short y;
    private byte type;//类型=1时不可行走
    private boolean walkable;

    public Grid(short x, short y, byte type) {
        super();
        this.x = x;
        this.y = y;
        this.type = type;
        this.walkable = type != 1;
    }

    /**
     * 是否可行走
     * 
     * @return
     */
    public boolean isWalkable() {
        return walkable;
    }

    public short getX() {
        return x;
    }

    public void setX(short x) {
        this.x = x;
    }

    public short getY() {
        return y;
    }

    public void setY(short y) {
        this.y = y;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + x;
        result = prime * result + y;
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
        Grid other = (Grid) obj;
        if (x != other.x)
            return false;
        if (y != other.y)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
