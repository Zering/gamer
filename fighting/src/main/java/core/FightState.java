package core;

/**
 * Created by zhanghaojie on 2018/1/8.
 * 战斗回合状态
 */
public enum FightState {
    /** 回合初始化（加载战斗单位的基础信息） */
    INIT(false),
    /** 等待初始化 */
    WAITING_INIT_FINISH(false),
    /** 计算战斗过程 */
    FIGHTING(true),
    /** 回合计算结束 */
    ROUND_END(true),
    /** 新回合开始 */
    ROUND_NEW(true),
    ;

    private boolean fighting;

    FightState(boolean fighting) {
        this.fighting = fighting;
    }
}
