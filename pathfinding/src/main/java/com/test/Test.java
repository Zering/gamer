package com.test;

import com.MapsGenerator;
import com.map.GridWrapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhanghaojie on 2017/11/11.
 *
 *  算法测试父类
 */
public abstract class Test {

    protected static final Log log = LogFactory.getLog(JPSTest.class);


    protected static class MoveWorker implements Runnable {

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

    protected static void sleep(long ms) {
        try {
            TimeUnit.MILLISECONDS.sleep(ms);
        } catch (InterruptedException e) {
            log.error("sleep erro", e);
        }
    }

    protected static class MoveUser {

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



    protected static void reloadDictMapData() {
        try {
            // 扫描加载地图信息
            MapsGenerator.scanMaps();
            System.out.println("ok");
        } catch (Exception e) {
            log.error("reloadDictMapData error", e);
        }
    }

}
