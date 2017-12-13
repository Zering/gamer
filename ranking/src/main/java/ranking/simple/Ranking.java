package ranking.simple;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

/**
 * Created by zhanghaojie on 2017/12/9.
 * 排行榜信息
 * 要预留出多余排行榜所需的数据，防止数据删除
 * 比如 要求实时排行前200名,一般实时维护400名的数据
 */
public class Ranking<DATA extends RankData<DATA>> {

    // 排行榜数据量
    protected final short limit;
    // 排行榜列表
    protected final List<DATA> rankList;
    // uniqueid, rank
    protected final Map<Integer, Short> rankMap;

    private final ReentrantReadWriteLock rwl;

    protected final ReadLock readLock;
    protected final WriteLock writeLock;

    public Ranking(short limit) {
        this.limit = limit;
        this.rankList = Lists.newArrayList();
        this.rankMap = Maps.newHashMap();
        this.rwl = new ReentrantReadWriteLock();
        this.readLock = this.rwl.readLock();
        this.writeLock = this.rwl.writeLock();
    }

    /**
     * 初始化缓存信息
     *
     * @param list
     */
    public void initRanking(List<DATA> list) {
        rankMap.clear();
        rankList.clear();
        Collections.sort(list);
        short rank = 1;
        for (DATA one : list) {
            one.setRank(rank);
            rankList.add(one);
            rankMap.put(one.getUniqueId(), rank);
            rank++;
        }
    }

    /**
     * 新增数据或列表更新
     *
     * @param data
     */
    public void doUpdate(DATA data) {
        boolean sort = false;
        // 新增排行榜数据
        if (!rankMap.containsKey(data.getUniqueId())) {
            rankList.add(data);
            sort = true;
        } else { // 排行榜中的数据有更新
            // 比较前一名和后一名的数据信息
            short rank = data.getRank();
            int preIndex = rank - 2;
            if (preIndex >= 0 && preIndex < rankList.size()) {
                DATA pre = rankList.get(preIndex);
                if (data.compareTo(pre) == -1) { // 排行数据比前一名高了
                    sort = true;
                }
            }
            if (!sort) {
                int nextIndex = rank;
                if (nextIndex < rankList.size()) {
                    DATA next = rankList.get(nextIndex);
                    if (data.compareTo(next) == 1) { // 排行数据句比后一名低了
                        sort = true;
                    }
                }
            }
        }
        // 重排排行榜信息
        if (sort) {
            Collections.sort(rankList);
            int limit0 = rankList.size();
            while (limit0 > limit) {
                rankList.remove(--limit0);
            }
            rankMap.clear();
            short rank = 1;
            for (int index = 0; index < rankList.size(); index++) {
                DATA one = rankList.get(index);
                one.setRank(rank);
                rankMap.put(one.getUniqueId(), rank);
                rank++;
            }
        }
    }

    public void removeData(int uid) {
        try {
            writeLock.lock();
            Short remove = rankMap.remove(uid);
            if (remove == null) {
                return;
            }
            int removeIndex = remove - 1;
            DATA one = rankList.remove(removeIndex);
            int size = rankList.size();
            if (size > 0) {
                short rank = one.getRank();
                for (; removeIndex < size; removeIndex++) {
                    DATA next = rankList.get(removeIndex);
                    next.setRank(rank);
                    rankMap.put(next.getUniqueId(), rank);
                    rank++;
                }
            } else {
                // reload
            }

        } finally {
            writeLock.unlock();
        }
    }

    /**
     * 返回前topN条排行榜记录
     *
     * @param topN
     * @return
     */
    public List<DATA> getRankingList(int topN) {
        try {
            readLock.lock();
            topN = Math.min(topN, rankList.size());
            if (topN == 0) {
                return Collections.emptyList();
            }
            List<DATA> list = Lists.newArrayListWithCapacity(topN);
            for (int i = 0; i < topN; i++) {
                list.add(rankList.get(i));
            }
            return list;
        } finally {
            readLock.unlock();
        }
    }

    public DATA getRankData(int uniqueId) {
        try {
            readLock.lock();
            Short rank = rankMap.get(uniqueId);
            if (rank == null) {
                return null;
            }
            return rankList.get(rank - 1);
        } finally {
            readLock.unlock();
        }
    }

}
