package com.test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.MapsGenerator;
import com.astar.AGrid;
import com.astar.AStarPathFind;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.map.DictMapDefineVO;
import com.map.GridWrapper;

/**
 * Created by zhanghaojie on 2017/11/11.
 * A*算法测试
 */
public class AStarTest extends Test{

    public static void main(String[] args) {
        MapsGenerator.scanMaps();
        short mapId = 1001;
        // 起点
        short cx = 21;
        short cy = 53;
        // 终点
        short tx = 39;
        short ty = 34;

        AGrid start = new AGrid(cy, cx);
        AGrid goal = new AGrid(ty, tx);
        DictMapDefineVO mapInfo = MapsGenerator.getMapInfo(mapId);

        final Map<Integer, MoveUser> userMap = Maps.newConcurrentMap();
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        executor.scheduleAtFixedRate(new MoveWorker(userMap), 10000, 10, TimeUnit.MILLISECONDS);
        for (int i = 0; i < 2000; i++) {
            List<AGrid> path = AStarPathFind.astar(cx, cy, tx, ty, mapInfo.getMapData());
            if (path != null) {
                ArrayList<AGrid> copy = Lists.newArrayList(path);
                AStarPathFind.showPath(path, cx, cy, tx, ty);
                LinkedList<GridWrapper> pathList = Lists.newLinkedList();
                AGrid f = copy.remove(0);
                int total = 0;
                MoveUser mu = new MoveUser();
                mu.id = i;
                mu.cur = new GridWrapper<>(f);
                for (AGrid grid : path) {
                    int ms = AStarPathFind.calPath(path, f, grid);
                    log.error(i +" : " + f + "->" + grid + "=" + ms + "ms");
                    total += ms;
                    GridWrapper gw = new GridWrapper<>(grid);
                    gw.setOffsetMS(ms);
                    pathList.add(gw);
                    f = grid;
                }
                log.error(i +" : " + start + " -> " + goal + "=" + total + "ms");

                mu.pathList = pathList;
                mu.moving = true;
                userMap.put(i, mu);
            }
        }
        sleep(Long.MAX_VALUE);
    }

}
