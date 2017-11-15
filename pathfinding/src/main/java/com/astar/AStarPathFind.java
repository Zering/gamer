package com.astar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.map.DictMapDataVO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A*寻路算法
 */
public class AStarPathFind {

    private static final Log log = LogFactory.getLog(AStarPathFind.class);
    private static final byte[] offset_col = { 0, -1, 0, 1, -1, -1, 1, 1 };
    private static final byte[] offset_row = { -1, 0, 1, 0, 1, -1, -1, 1 };
    private static final byte[] offset_g = { 10, 10, 10, 10, 14, 14, 14, 14 };
    private static final int SPEED = 200;
    private static final byte PATH_DIRS = 8;
    static final double TILE_HEIGHT = 32.0D;//px
    static final double TILE_WIDTH = 48.0D;//px
    static final double TILE_DIAGONAL = Math.sqrt(Math.pow(TILE_HEIGHT, 2) + Math.pow(TILE_WIDTH, 2));//px
    // static final double DEFAULT_SPEED = 250.0D;

    public static List<AGrid> astar(AGrid from, AGrid to, DictMapDataVO mdDVO) {
        byte[][] map = mdDVO.getData();
        int rows = map.length;
        int cols = map[0].length;
        AGrid cur = new AGrid(from);
        ArrayList<AGrid> open = new ArrayList<>();
        ArrayList<AGrid> close = new ArrayList<>();
        boolean notFound = true;
        AGrid tmp = null;
        while (notFound) {
            for (int i = 0; i < PATH_DIRS; i++) {
                int ccol = cur.col + offset_col[i];
                int crow = cur.row + offset_row[i];
                if (isBlock(crow, ccol, rows, cols, map)) {
                    continue;
                }
                tmp = new AGrid(crow, ccol);
                if (to.equals(tmp)) {
                    notFound = false;
                    to.parent = cur;
                    break;
                }
                tmp.g = cur.g + offset_g[i];
                tmp.h = getDistance(tmp, to);
                tmp.f = tmp.g + tmp.h;
                if (open.contains(tmp)) {
                    int pos = open.indexOf(tmp);
                    AGrid p = open.get(pos);
                    if (p.f > tmp.f) {
                        open.remove(p);
                        open.add(tmp);
                        tmp.parent = cur;
                    }
                } else if (close.contains(tmp)) {
                    int pos = close.indexOf(tmp);
                    AGrid p = close.get(pos);
                    if (p.f > tmp.f) {
                        close.remove(pos);
                        open.add(tmp);
                        tmp.parent = cur;
                    }
                } else {
                    open.add(tmp);
                    tmp.parent = cur;
                }
            }
            if (!notFound) {
                break;
            }
            open.remove(cur);
            if (open.isEmpty()) {
                return null;
            }
            close.add(cur);
            Collections.sort(open);
            cur = open.get(0);
        }
        List<AGrid> path = new ArrayList<>();
        AGrid p = to;
        while (p.parent != null) {
            path.add(p);
            p = p.parent;
        }
        return path;
    }

    public static void showPath(List<AGrid> path, int frow, int fcol, int trow, int tcol) {
        if (path == null) {
            log.error("(" + fcol + "," + frow + ") -> (" + tcol + "," + trow + ") no path found");
        } else {
            StringBuilder sb = new StringBuilder();
            int step = 1;
            for (int i = path.size() - 1; i >= 0; i--) {
                sb.append(" -> ").append(path.get(i));
                if (step++ % 10 == 0) {
                    sb.append("\r\n");
                }
            }
            log.error("\r\n(" + fcol + "," + frow + ")" + sb);
        }
    }

    //3328
    private static final double xpx = Math.sqrt(Math.pow(48, 2) + Math.pow(32, 2));

    public static int calPath(List<AGrid> path, int frow, int fcol, int trow, int tcol) {
        if (path == null || path.isEmpty()) {
            log.error("(" + fcol + "," + frow + ") -> (" + tcol + "," + trow + ") no path found");
            return 0;
        } else {
            StringBuilder sb = new StringBuilder();
            int step = 1;
            int x = fcol, y = frow;
            int speed = SPEED;
            int ms = 0;
            for (int i = path.size() - 1; i >= 0; i--) {
                AGrid g = path.get(i);
                double px = 0;
                if (x == g.col) {//横着走 48px
                    px = 32;
                    g.g = 192;
                } else if (y == g.row) {//竖着走 32px
                    px = 48;
                    g.g = 128;
                } else {//斜着走 
                    px = xpx;
                    g.g = 230;
                }
                int t = (int) (px / speed * 1000);
                ms += t;
                x = g.col;
                y = g.row;
                sb.append(" -> ").append(path.get(i));
                if (step++ % 10 == 0) {
                    sb.append("\r\n");
                }
            }
            //log.error("\r\n(" + fcol + "," + frow + ")" + sb + "\r\nmove ms=" + ms);
            return ms;
        }
    }

    public static int calPath(List<AGrid> path, AGrid star, AGrid goal) {
        return calPath(path, star.getRow(), star.getCol(), goal.getRow(), goal.getCol());
    }

    public static List<AGrid> astar(int fx, int fy, int tx, int ty, DictMapDataVO mdDVO) {
        return astar(new AGrid(fy, fx), new AGrid(ty, tx), mdDVO);
    }

    private static boolean isBlock(int row, int col, int rows, int cols, byte[][] map) {
        return row < 0 || col < 0 || row >= rows || col >= cols || map[row][col] == 1;
    }

    private static int getDistance(AGrid from, AGrid to) {
        return (Math.abs(from.row - to.row) + Math.abs(from.col - to.col)) * 10;
    }

}
