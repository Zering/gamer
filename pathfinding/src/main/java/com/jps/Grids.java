package com.jps;

import com.google.common.collect.Lists;
import com.map.DictMapDataVO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Grids {

    //[↑ → ↓ ← ↖ ↗ ↘ ↙]
    private static final byte[] NEIGHBOR_OFFSET = new byte[] { 0, -1, 1, 0, 0, 1, -1, 0, -1, -1, 1, -1, 1, 1, -1, 1 };
    private List<Grid> grids;
    private int width;
    private int height;
    private DistanceAlgo distance;
    private DistanceAlgo heuristic;

    public Grids(List<List<Grid>> data, DistanceAlgo distance, DistanceAlgo heuristic) {
        width = data.get(0).size();
        height = data.size();
        grids = new ArrayList<>(height * width);
        for (List<Grid> list : data) {
            grids.addAll(list);
        }
        this.distance = distance;
        this.heuristic = heuristic;
    }

    public Grids(Grid[][] data, DistanceAlgo distance, DistanceAlgo heuristic) {
        width = data[0].length;
        height = data.length;
        grids = new ArrayList<>(height * width);
        for (Grid[] row : data) {
            Collections.addAll(grids, row);
        }
        this.distance = distance;
        this.heuristic = heuristic;
    }

    public Grids(DictMapDataVO mdDVO, DistanceAlgo distance, DistanceAlgo heuristic) {
        this.width = mdDVO.getWidth();
        this.height = mdDVO.getHeight();
        this.grids = new ArrayList<>(height * width);
        for (short y = 0; y < height; y++) {
            List<Grid> row = Lists.newArrayListWithCapacity(width);
            for (short x = 0; x < width; x++) {
                row.add(new Grid(x, y, mdDVO.getData()[y][x]));
                this.grids.add(new Grid(x, y, mdDVO.getData()[y][x]));
            }
        }
        this.distance = distance;
        this.heuristic = heuristic;
    }

    public Grids(List<List<Grid>> data) {
        this(data, DistanceAlgo.EUCLIDEAN, DistanceAlgo.CHEBYSHEV);
    }

    public Grids(Grid[][] data) {
        this(data, DistanceAlgo.EUCLIDEAN, DistanceAlgo.CHEBYSHEV);
    }

    public Grids(DictMapDataVO mdDVO) {
        this(mdDVO, DistanceAlgo.EUCLIDEAN, DistanceAlgo.CHEBYSHEV);
    }

    public Collection<Grid> getGrids() {
        return grids;
    }

    public Grid getGrid(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return null;
        }
        return grids.get(x + y * width);
    }

    public double getDistance(Grid a, Grid b) {
        return distance.algo(a, b);
    }

    public double getHeuristicDistance(Grid a, Grid b) {
        return heuristic.algo(a, b);
    }

    public Collection<Grid> getNeighborsOf(Grid Grid) {
        return getNeighborsOf(Grid, Diagonal.NO_OBSTACLES);
    }

    public Set<Grid> getNeighborsOf(Grid g, Diagonal diagonal) {
        Set<Grid> neighbors = new HashSet<>();
        boolean[] flag = new boolean[8];
        for (byte dir = 0; dir < 8; dir += 2) {
            int x = g.x + NEIGHBOR_OFFSET[dir];
            int y = g.y + NEIGHBOR_OFFSET[dir + 1];
            if (addReachableNeighbor(x, y, neighbors)) {
                flag[dir / 2] = true;
            }
        }
        switch (diagonal) {
            case NEVER://不考虑斜线
                return neighbors;
            case NO_OBSTACLES://没有障碍时才考虑斜线
                flag[4] = flag[0] && flag[3];//↖
                flag[5] = flag[0] && flag[1];//↗
                flag[6] = flag[2] && flag[1];//↘
                flag[7] = flag[2] && flag[3];//↙
                break;
            case ONE_OBSTACLE://只有一侧有障碍时才考虑斜线
                flag[4] = flag[0] || flag[3];
                flag[5] = flag[0] || flag[1];
                flag[6] = flag[2] || flag[1];
                flag[7] = flag[2] || flag[3];
                break;
            case ALWAYS://总是考虑斜线
                flag[4] = flag[5] = flag[6] = flag[7] = true;
        }
        for (byte dir = 8; dir < 16; dir += 2) {
            int x = g.x + NEIGHBOR_OFFSET[dir];
            int y = g.y + NEIGHBOR_OFFSET[dir + 1];
            if (flag[dir / 2]) {
                addReachableNeighbor(x, y, neighbors);
            }
        }
        return neighbors;
    }

    public boolean addReachableNeighbor(int x, int y, Collection<Grid> neighbors) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            Grid g = grids.get(x + y * width);
            if (g.isWalkable()) {
                neighbors.add(g);
                return true;
            }
        }
        return false;
    }

    public boolean addUnreachableNeighbor(int x, int y, int xn, int yn, Collection<Grid> neighbors) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            Grid g = grids.get(x + y * width);
            if (!g.isWalkable()) {
                g = getGrid(xn, yn);
                if (g != null) {
                    neighbors.add(g);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isWalkable(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height && getGrid(x, y).isWalkable();
    }

    public enum Diagonal {
        ALWAYS,
        NO_OBSTACLES,
        ONE_OBSTACLE,
        NEVER
    }

    public enum DistanceAlgo {
        MANHATTAN {

            @Override
            public double algo(Grid a, Grid b) {
                return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
            }
        },
        EUCLIDEAN {

            @Override
            public double algo(Grid a, Grid b) {
                return Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
            }
        },
        OCTILE {

            @Override
            public double algo(Grid a, Grid b) {
                double f = Math.sqrt(2) - 1;
                double dx = Math.abs(a.x - b.x);
                double dy = Math.abs(a.y - b.y);
                return (dx < dy) ? f * dx + dy : f * dy + dx;
            }
        },
        CHEBYSHEV {

            @Override
            public double algo(Grid a, Grid b) {
                return Math.max(Math.abs(a.x - b.x), Math.abs(a.y - b.y));
            }
        };

        public abstract double algo(Grid a, Grid b);
    }

}
