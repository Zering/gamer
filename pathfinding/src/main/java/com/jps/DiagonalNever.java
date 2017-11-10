package com.jps;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 不可以走对角线
 * 
 * @author liweijie
 */
public class DiagonalNever extends Diagonal {

    @Override
    public Set<Grid> findNeighbors(Grids grids, Grid g, Map<Grid, Grid> parentMap) {
        Set<Grid> neighbors = new HashSet<Grid>();

        Grid parent = parentMap.get(g);

        // directed pruning: can ignore most neighbors, unless forced.
        if (parent != null) {
            final int x = g.x;
            final int y = g.y;
            // get normalized direction of travel
            final int dx = (x - parent.x) / Math.max(Math.abs(x - parent.x), 1);
            final int dy = (y - parent.y) / Math.max(Math.abs(y - parent.y), 1);

            // search horizontally/vertically
            if (dx != 0) {
                grids.addReachableNeighbor(x + dx, y, neighbors);
                grids.addReachableNeighbor(x, y + 1, neighbors);
                grids.addReachableNeighbor(x, y - 1, neighbors);
            } else if (dy != 0) {
                grids.addReachableNeighbor(x, y + dy, neighbors);
                grids.addReachableNeighbor(x + 1, y, neighbors);
                grids.addReachableNeighbor(x - 1, y, neighbors);
            }
        } else {
            // no parent, return all neighbors
            neighbors.addAll(grids.getNeighborsOf(g, Grids.Diagonal.NEVER));
        }

        return neighbors;
    }

    @Override
    public Grid jump(Grids grids, Grid neighbor, Grid current, Set<Grid> goals) {
        if (neighbor == null || !neighbor.isWalkable()) {
            return null;
        }
        if (goals.contains(neighbor)) {
            return neighbor;
        }

        int dx = neighbor.x - current.x;
        int dy = neighbor.y - current.y;

        // check for forced neighbors
        // check horizontally/vertically
        if (dx != 0) {
            if ((grids.isWalkable(neighbor.x, neighbor.y + 1) && !grids.isWalkable(neighbor.x - dx, neighbor.y + 1))
                    || (grids.isWalkable(neighbor.x, neighbor.y - 1) && !grids.isWalkable(neighbor.x - dx, neighbor.y - 1))) {
                return neighbor;
            }
        } else if (dy != 0) {
            if ((grids.isWalkable(neighbor.x + 1, neighbor.y) && !grids.isWalkable(neighbor.x + 1, neighbor.y - dy))
                    || (grids.isWalkable(neighbor.x - 1, neighbor.y) && !grids.isWalkable(neighbor.x - 1, neighbor.y - dy))) {
                return neighbor;
            }
            // when moving vertically check for horizontal jump points
            if (jump(grids, grids.getGrid(neighbor.x + 1, neighbor.y), neighbor, goals) != null
                    || jump(grids, grids.getGrid(neighbor.x - 1, neighbor.y), neighbor, goals) != null) {
                return neighbor;
            }
        } else {
            return null;
        }

        return jump(grids, grids.getGrid(neighbor.x + dx, neighbor.y + dy), neighbor, goals);
    }

}
