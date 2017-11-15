package com.bfs;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.map.DictMapDataVO;

/**
 * Created by zhanghaojie on 2017/11/14.
 * Breadth-First-Search广度优先算法
 */
public class BFSFinder {

    private static final byte[] offset_col = { 0, -1, 0, 1, -1, -1, 1, 1 };
    private static final byte[] offset_row = { -1, 0, 1, 0, 1, -1, -1, 1 };

    public static List<BGrid> find(BGrid from, BGrid to, DictMapDataVO mdDVO) {
        byte[][] map = mdDVO.getData();
        int rows = map.length;
        int cols = map[0].length;

        Queue<BGrid> open = new LinkedList<>();
        ArrayList<BGrid> close = new ArrayList<>();
        BGrid cur = new BGrid(from);
        BGrid tmp;
        open.add(cur);
        while (!open.isEmpty()) {
            open.poll();
            close.add(cur);
            for (int i = 0; i < 8; i++) { // 遍历附近的点
                int ccol = cur.col + offset_col[i];
                int crow = cur.row + offset_row[i];
                if (isBlock(crow, ccol, rows, cols, map)) {
                    continue;
                }
                tmp = new BGrid(crow, ccol);
                if (to.equals(tmp)) {
                    to.parent = cur;
                    break;
                }

                if (open.contains(tmp) || close.contains(tmp)) {
                    continue;
                }

                open.add(tmp);
                tmp.parent = cur;
            }
            cur = open.peek();
        }

        List<BGrid> path = new ArrayList<>();
        BGrid p = to;
        while (p.parent != null) {
            path.add(p);
            p = p.parent;
        }
        return path;
    }

    private static boolean isBlock(int row, int col, int rows, int cols, byte[][] map) {
        return row < 0 || col < 0 || row >= rows || col >= cols || map[row][col] == 1;
    }

}
