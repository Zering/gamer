package com.core;

/**
 * Created by zhanghaojie on 2017/11/12.
 *
 * 节点
 *
 */
public class Node {

  int x;
  int y;

  private byte type; // 1为不可走
  private boolean walkable;

  public Node(int x, int y, byte type) {
    this.x = x;
    this.y = y;
    this.type = type;
    this.walkable = type == 1;
  }

  public int getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }

  public byte getType() {
    return type;
  }

  public void setType(byte type) {
    this.type = type;
  }

  public boolean isWalkable() {
    return walkable;
  }

  public void setWalkable(boolean walkable) {
    this.walkable = walkable;
  }
}
