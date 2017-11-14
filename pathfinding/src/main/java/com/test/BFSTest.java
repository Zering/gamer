package com.test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.astar.AGrid;
import com.astar.AStarPathFind;
import com.bfs.BFSFinder;
import com.bfs.BGrid;
import com.map.DictMapDefineVO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.MapsGenerator;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jps.Grid;
import com.jps.Grids;
import com.jps.JPS;
import com.map.GridWrapper;

public class BFSTest extends Test {

    public static void main(String[] args) {
        MapsGenerator.scanMaps();
        short mapId = 1001;
        // 起点
        short cx = 21; // rol
        short cy = 53; // row
        // 终点
        short tx = 39;
        short ty = 34;

        BGrid start = new BGrid(cy, cx);
        BGrid goal = new BGrid(ty, tx);
        DictMapDefineVO mapInfo = MapsGenerator.getMapInfo(mapId);

        final Map<Integer, MoveUser> userMap = Maps.newConcurrentMap();
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        executor.scheduleAtFixedRate(new MoveWorker(userMap), 10000, 10, TimeUnit.MILLISECONDS);
        for (int i = 0; i < 2000; i++) {
            List<BGrid> path = BFSFinder.find(start, goal, mapInfo.getMapData());
            if (path != null) {
                ArrayList<BGrid> copy = Lists.newArrayList(path);
                LinkedList<GridWrapper> pathList = Lists.newLinkedList();
                BGrid f = copy.remove(0);
                int total = 0;
                MoveUser mu = new MoveUser();
                mu.id = i;
                mu.cur = new GridWrapper<>(f);
                for (BGrid grid : path) {
                    int ms = calcMoveMS(f, grid);
                    log.error(i + " : " + f + "->" + grid + "=" + ms + "ms");
                    total += ms;
                    GridWrapper gw = new GridWrapper<>(grid);
                    gw.setOffsetMS(ms);
                    pathList.add(gw);
                    f = grid;
                }
                log.error(i + " : " + start + " -> " + goal + "=" + total + "ms");

                mu.pathList = pathList;
                mu.moving = true;
                userMap.put(i, mu);
            }
        }
        sleep(Long.MAX_VALUE);
    }

    private static int calcMoveMS(BGrid from, BGrid to) {
        double base = 200;//
        if (from.getRow() == to.getRow() || from.getCol() == to.getCol()) {//↑ → ↓ ←
            base *= 1.41;
        } else {
            base *= 1.58;
        }
        //base *= 2;
        return (int) Math.ceil(base);
    }

}
