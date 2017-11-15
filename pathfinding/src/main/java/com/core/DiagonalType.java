package com.core;

/**
 * Created by zhanghaojie on 2017/11/12.
 * 对角线行走类型
 */
public enum DiagonalType {

    // 总是考虑对角线
    ALWAYS,
    // 不考虑对角线
    NEVER,
    // 对角线没有障碍时考虑走对角线
    NO_OBSTACLE,
    // 对角最多有一个障碍时考虑对角线
    ONE_OBSTACLE,
}
