package com.jps;

import java.util.Queue;
import java.util.concurrent.ConcurrentMap;

import com.MapsGenerator;
import com.map.DictMapDefineVO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.common.collect.Maps;

/**
 * https://github.com/qiao/PathFinding.js<br>
 * https://github.com/kevinsheehan/jps
 */
public class JPS {

    private static final Log log = LogFactory.getLog(JPS.class);
    public static final JPSFinder JPS_DIAGONAL_ALWAYS = new JPSFinder(new DiagonalAlways());
    public static final JPSFinder JPS_DIAGONAL_NEVER = new JPSFinder(new DiagonalNever());
    public static final JPSFinder JPS_DIAGONAL_NO_OBSTACLES = new JPSFinder(new DiagonalNoObstacles());
    public static final JPSFinder JPS_DIAGONAL_ONE_OBSTACLES = new JPSFinder(new DiagonalOneObstacles());

    private static ConcurrentMap<Short, Grids> gridsMap = Maps.newConcurrentMap();

    public static void testJPS(short mapId, short fx, short fy, short tx, short ty) {
        testJPS(mapId, fx, fy, tx, ty, JPS_DIAGONAL_ALWAYS, false, false);
    }

    public static void testJPS(short mapId, short fx, short fy, short tx, short ty, JPSFinder finder, boolean adjacentStop, boolean diagonalStop) {
        long nt = System.nanoTime();
        Queue<Grid> path = findPath(mapId, fx, fy, tx, ty, finder, adjacentStop, diagonalStop);
        drawPath(path, fx, fy, tx, ty);
        log.error("com.jps cost " + (System.nanoTime() - nt) / 1000000.0 + " ms");
    }

    public static void testJPS(int mapId, int fx, int fy, int tx, int ty) {
        testJPS((short) mapId, (short) fx, (short) fy, (short) tx, (short) ty);
    }

    public static void drawPath(Queue<Grid> path, short fx, short fy, short tx, short ty) {
        if (path == null) {
            log.error("(" + fx + "," + fy + ")->(" + tx + "," + ty + ") path not found");
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("(" + fx + "," + fy + ")->(" + tx + "," + ty + ") path found\r\n");
            int br = 0;
            for (Grid g : path) {
                if (br > 0) {
                    sb.append("->").append(g);
                } else {
                    sb.append(g);
                }
                br++;
                if (br > 0 && br % 10 == 0) {
                    sb.append("\r\n");
                }
            }
            log.error(sb);
        }
    }

    public static Queue<Grid> jpsDiagonalAlways(short mapId, short fx, short fy, short tx, short ty, boolean adjacentStop, boolean diagonalStop) {
        return findPath(mapId, fx, fy, tx, ty, JPS_DIAGONAL_ALWAYS, adjacentStop, diagonalStop);
    }

    public static Queue<Grid> jpsDiagonalAlways(short mapId, short fx, short fy, short tx, short ty, boolean adjacentStop) {
        return findPath(mapId, fx, fy, tx, ty, JPS_DIAGONAL_ALWAYS, adjacentStop, false);
    }

    public static Queue<Grid> jpsDiagonalAlways(Grids grids, short fx, short fy, short tx, short ty, boolean adjacentStop, boolean diagonalStop) {
        return findPath(grids, fx, fy, tx, ty, JPS_DIAGONAL_ALWAYS, adjacentStop, diagonalStop);
    }

    public static Queue<Grid> jpsDiagonalAlways(Grids grids, short fx, short fy, short tx, short ty, boolean adjacentStop) {
        return findPath(grids, fx, fy, tx, ty, JPS_DIAGONAL_ALWAYS, adjacentStop, false);
    }

    public static Queue<Grid> jpsDiagonalNever(short mapId, short fx, short fy, short tx, short ty, boolean adjacentStop, boolean diagonalStop) {
        return findPath(mapId, fx, fy, tx, ty, JPS_DIAGONAL_NEVER, adjacentStop, diagonalStop);
    }

    public static Queue<Grid> jpsDiagonalNever(short mapId, short fx, short fy, short tx, short ty, boolean adjacentStop) {
        return findPath(mapId, fx, fy, tx, ty, JPS_DIAGONAL_NEVER, adjacentStop, false);
    }

    public static Queue<Grid> jpsDiagonalNever(Grids grids, short fx, short fy, short tx, short ty, boolean adjacentStop, boolean diagonalStop) {
        return findPath(grids, fx, fy, tx, ty, JPS_DIAGONAL_NEVER, adjacentStop, diagonalStop);
    }

    public static Queue<Grid> jpsDiagonalNever(Grids grids, short fx, short fy, short tx, short ty, boolean adjacentStop) {
        return findPath(grids, fx, fy, tx, ty, JPS_DIAGONAL_NEVER, adjacentStop, false);
    }

    public static Queue<Grid> jpsDiagonalNoObstacles(short mapId, short fx, short fy, short tx, short ty, boolean adjacentStop,
            boolean diagonalStop) {
        return findPath(mapId, fx, fy, tx, ty, JPS_DIAGONAL_NO_OBSTACLES, adjacentStop, diagonalStop);
    }

    public static Queue<Grid> jpsDiagonalNoObstacles(short mapId, short fx, short fy, short tx, short ty, boolean adjacentStop) {
        return findPath(mapId, fx, fy, tx, ty, JPS_DIAGONAL_NO_OBSTACLES, adjacentStop, false);
    }

    public static Queue<Grid> jpsDiagonalNoObstacles(Grids grids, short fx, short fy, short tx, short ty, boolean adjacentStop,
            boolean diagonalStop) {
        return findPath(grids, fx, fy, tx, ty, JPS_DIAGONAL_NO_OBSTACLES, adjacentStop, diagonalStop);
    }

    public static Queue<Grid> jpsDiagonalNoObstacles(Grids grids, short fx, short fy, short tx, short ty, boolean adjacentStop) {
        return findPath(grids, fx, fy, tx, ty, JPS_DIAGONAL_NO_OBSTACLES, adjacentStop, false);
    }

    public static Queue<Grid> jpsDiagonalOneObstacles(short mapId, short fx, short fy, short tx, short ty, boolean adjacentStop,
            boolean diagonalStop) {
        return findPath(mapId, fx, fy, tx, ty, JPS_DIAGONAL_ONE_OBSTACLES, adjacentStop, diagonalStop);
    }

    public static Queue<Grid> jpsDiagonalOneObstacles(short mapId, short fx, short fy, short tx, short ty, boolean adjacentStop) {
        return findPath(mapId, fx, fy, tx, ty, JPS_DIAGONAL_ONE_OBSTACLES, adjacentStop, false);
    }

    public static Queue<Grid> jpsDiagonalOneObstacles(Grids grids, short fx, short fy, short tx, short ty, boolean adjacentStop,
            boolean diagonalStop) {
        return findPath(grids, fx, fy, tx, ty, JPS_DIAGONAL_ONE_OBSTACLES, adjacentStop, diagonalStop);
    }

    public static Queue<Grid> jpsDiagonalOneObstacles(Grids grids, short fx, short fy, short tx, short ty, boolean adjacentStop) {
        return findPath(grids, fx, fy, tx, ty, JPS_DIAGONAL_ONE_OBSTACLES, adjacentStop, false);
    }

    public static Queue<Grid> findPath(short mapId, short fx, short fy, short tx, short ty, JPSFinder finder, boolean adjacentStop,
            boolean diagonalStop) {
        Grids grids = getGrids(mapId);
        return findPath(grids, fx, fy, tx, ty, finder, adjacentStop, diagonalStop);
    }

    /**
     * JPS寻路
     * 
     * @param grids 地图数据
     * @param fx 起点X坐标
     * @param fy 起点Y坐标
     * @param tx 终点X坐标
     * @param ty 终点Y坐标
     * @param finder
     * @param adjacentStop 相邻障碍停止
     * @param diagonalStop 对角障碍停止
     * @return
     */
    public static Queue<Grid> findPath(Grids grids, short fx, short fy, short tx, short ty, JPSFinder finder, boolean adjacentStop,
            boolean diagonalStop) {
        Grid start = grids.getGrid(fx, fy);
        if (start == null) {
            return null;
        }
        Grid goal = grids.getGrid(tx, ty);
        if (goal == null || !goal.isWalkable()) {
            return null;
        }
        return finder.findPath(grids, start, goal, adjacentStop, diagonalStop);
    }

    public static Queue<Grid> findPath(Grids grids, Grid start, Grid goal, JPSFinder finder, boolean adjacentStop, boolean diagonalStop) {
        return finder.findPath(grids, start, goal, adjacentStop, diagonalStop);
    }

    private static Grids getGrids(short mapId) {
        Grids grids = gridsMap.get(mapId);
        if (grids == null) {
            DictMapDefineVO mdDVO = MapsGenerator.getMapInfo(mapId);
            grids = new Grids(mdDVO.getMapData());
            Grids old = gridsMap.putIfAbsent(mapId, grids);
            if (old != null) {
                grids = old;
            }
        }
        return grids;
    }

    public static void clearGrids() {
        gridsMap.clear();
    }
}
