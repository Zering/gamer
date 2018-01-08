package core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Created by zhanghaojie on 2018/1/8.
 * 一场战斗的战斗信息
 */
public class FightContext implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(FightContext.class);

    // 当前时间
    private int nowSec;
    // 战斗开始时间
    private int beginSec;
    // 最后更新时间
    private int lastSec;
    // 回合间隔时间
    private int inteval;
    // 当前战斗状态
    private FightState fs;
    // 当前回合数
    private byte round;

    private boolean roundDone;
    // 上方队伍
    private FightSide uside;
    // 下方队伍 （以下方为战斗发起方）
    private FightSide dside;
    // 所有战斗单位
    private Map<Byte, FightUnit> fuMap;
    // 回合出手顺序
    private List<FightUnit> actSeq;
    // 战斗行为列表
    private List<FightAct> actList;

    @Override
    public void run() {
        try {
            if (!roundDone) {
                return;
            }
            roundDone = false;
            FightState fightState = updateState();
            // 等待中
            if (fightState == null) {
                return;
            }
            switch (fightState) {
                case INIT: {
                    initFightUnits();
                    break;
                }
                case FIGHTING: {
                    roundFight();
                    break;
                }
                case ROUND_END: {
                    roundNew();
                    break;
                }
                default:
                    break;
            }

        } catch (Exception e) {
            // todo 移除本场战斗
            log.error("fightcontext error!", e);
        } finally {
            roundDone = true;
        }
    }

    private void initFightUnits() {

    }

    private void roundFight() {

    }

    private void roundNew() {

    }

    public void updateNow(int nowSec) {
        this.nowSec = nowSec;
    }

    public FightState updateState() {
        if (lastSec == 0 || (nowSec - lastSec) > inteval) {
            return null;
        }
        switch (fs) {
            case INIT: {
                fs = FightState.WAITING_INIT_FINISH;
                lastSec = nowSec;
                round = 1;
                return FightState.INIT;
            }
            case FIGHTING: {
                fs = FightState.ROUND_END;
                lastSec = 0;
                return FightState.FIGHTING;
            }
            case ROUND_NEW: {
                fs = FightState.FIGHTING;
                lastSec = nowSec;
                round++;
                return FightState.ROUND_END;
            }
            default:
                break;

        }

        return null;

    }

}
