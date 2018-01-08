package core;

import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import java.util.Calendar;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zhanghaojie on 2018/1/8.
 * 战斗管理类, 回合制战斗 1s检测一次战斗状态
 */
public final class FightManager {

    private static final Logger log = LoggerFactory.getLogger(FightManager.class);

    // 战斗序列
    private AtomicInteger fightseq;

    // 正在进行中的战斗
    private ConcurrentMap<Integer, FightContext> fcMap;

    // 定时检测当前战斗状态
    private ScheduledExecutorService fightStateSchedule;

    // 战斗任务线程池
    private ThreadPoolExecutor fightTaskPool;

    private static FightManager instance;

    public FightManager() {
        fightseq = new AtomicInteger(0);
        fcMap = Maps.newConcurrentMap();
        fightStateSchedule = Executors.newSingleThreadScheduledExecutor();
        // fixme 常量优化
        BlockingQueue<Runnable> queue = Queues.newLinkedBlockingQueue(100);
        fightTaskPool = new ThreadPoolExecutor(50, 100, 300, TimeUnit.SECONDS, queue);
        FightManager.instance = this;
    }

    public void init() {
        fightStateSchedule.scheduleAtFixedRate(() -> execute((int) (Calendar.getInstance().getTimeInMillis() / 1000L)), 1, 1, TimeUnit.SECONDS);
    }

    private void execute(int nowSec) {
        try {
            for (FightContext fc : fcMap.values()) {
                fc.updateNow(nowSec);
                fightTaskPool.execute(fc);
            }
        } catch (Exception e) {
            log.error("fight execute error!", e);
        }
    }

    public static FightManager getInstance() {
        return instance;
    }
}
