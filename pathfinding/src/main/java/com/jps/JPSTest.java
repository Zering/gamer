package com.jps;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.MapsGenerator;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class JPSTest {

    private static final Log log = LogFactory.getLog(JPSTest.class);

    public static void main(String[] args) {
        reloadDictMapData();

        short mapid = 1001;
        // 起点
        short cx = 21;
        short cy = 53;
        // 终点
        short tx = 39;
        short ty = 34;

        Grids grids = MapsGenerator.getGridsInfo(mapid);
        Grid start = grids.getGrid(cx, cy);
        Grid goal = grids.getGrid(tx, ty);
        final Map<Integer, MoveUser> userMap = Maps.newConcurrentMap();
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        executor.scheduleAtFixedRate(new MoveWorker(userMap), 10000, 10, TimeUnit.MILLISECONDS);
        for (int i = 0; i < 2000; i++) {
            Queue<Grid> path = JPS.findPath(grids, start, goal, JPS.JPS_DIAGONAL_ALWAYS, false, false);
            if (path != null) {
                Grid f = path.poll();//起点
                LinkedList<GridWrapper> pathList = Lists.newLinkedList();
                int total = 0;
                MoveUser mu = new MoveUser();
                mu.id = i;
                mu.cur = new GridWrapper(f);
                for (Grid grid : path) {
                    int ms = calcMoveMS(f, grid);
                    log.error(i +" : " + f + "->" + grid + "=" + ms + "ms");
                    total += ms;
                    GridWrapper gw = new GridWrapper(grid);
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

    private static class MoveWorker implements Runnable {

        private Map<Integer, MoveUser> userMap;

        public MoveWorker(Map<Integer, MoveUser> userMap) {
            this.userMap = userMap;
        }

        @Override
        public void run() {
            if (userMap.isEmpty()) {
                sleep(100);
                return;
            }
            long curMS = System.currentTimeMillis();
            for (MoveUser user : userMap.values()) {
                if (!user.moving) {
                    continue;
                }
                if (user.lastMoveMS == 0 || user.lastMoveMS - curMS <= 0) {
                    user.move(curMS);
                }
            }
        }

    }

    private static void sleep(long ms) {
        try {
            TimeUnit.MILLISECONDS.sleep(ms);
        } catch (InterruptedException e) {
            log.error("sleep erro", e);
        }
    }

    private static class MoveUser {

        int id;
        LinkedList<GridWrapper> pathList;
        GridWrapper cur;
        long lastMoveMS;
        volatile boolean moving;

        public void move(long curMS) {
            if (pathList.isEmpty()) {
                log.error(id + " move stop");
                moving = false;
                return;
            }
            GridWrapper first = pathList.removeFirst();
            if (id == 0) {
                log.error("moving..." + cur.getGrid() + "->" + first.getGrid());
            }
            cur = first;
            lastMoveMS = curMS + first.getOffsetMS();
        }
    }

    private static int calcMoveMS(Grid from, Grid to) {
        double base = 200;//
        if (from.getX() == to.getX() || from.getY() == to.getY()) {//↑ → ↓ ←
            base *= 1.41;
        } else {
            base *= 1.58;
        }
        //base *= 2;
        return (int) Math.ceil(base);
    }

    private static void reloadDictMapData() {
        try {
            // 扫描加载地图信息
            MapsGenerator.scanMaps();
            System.out.println("ok");
        } catch (Exception e) {
            log.error("reloadDictMapData error", e);
        }
    }
}
