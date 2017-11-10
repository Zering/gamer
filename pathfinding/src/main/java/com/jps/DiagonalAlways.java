package com.jps;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.Sets;

/**
 * 可以走对角线
 */
public class DiagonalAlways extends Diagonal {

    @Override
    public Set<Grid> findNeighbors(Grids grids, Grid g, Map<Grid, Grid> parentMap) {
        Set<Grid> neighbors = Sets.newHashSet();
        Grid parent = parentMap.get(g);

        // directed pruning: can ignore most neighbors, unless forced.
        if (parent != null) {
            final int x = g.x;
            final int y = g.y;
            // get normalized direction of travel
            final int dx = (x - parent.x) / Math.max(Math.abs(x - parent.x), 1);
            final int dy = (y - parent.y) / Math.max(Math.abs(y - parent.y), 1);

            // search diagonally
            if (dx != 0 && dy != 0) {
                grids.addReachableNeighbor(x, y + dy, neighbors);
                grids.addReachableNeighbor(x + dx, y, neighbors);
                grids.addReachableNeighbor(x + dx, y + dy, neighbors);
                grids.addUnreachableNeighbor(x - dx, y, x - dx, y + dy, neighbors);
                grids.addUnreachableNeighbor(x, y - dy, x + dx, y - dy, neighbors);
            } else { // search horizontally/vertically
                if (dx == 0) {
                    grids.addReachableNeighbor(x, y + dy, neighbors);
                    grids.addUnreachableNeighbor(x + 1, y, x + 1, y + dy, neighbors);
                    grids.addUnreachableNeighbor(x - 1, y, x - 1, y + dy, neighbors);
                } else {
                    grids.addReachableNeighbor(x + dx, y, neighbors);
                    grids.addUnreachableNeighbor(x, y + 1, x + dx, y + 1, neighbors);
                    grids.addUnreachableNeighbor(x, y - 1, x + dx, y - 1, neighbors);
                }
            }
        } else {
            neighbors.addAll(grids.getNeighborsOf(g, Grids.Diagonal.ALWAYS));
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
        // check along diagonal
        if (dx != 0 && dy != 0) {
            if ((grids.isWalkable(neighbor.x - dx, neighbor.y + dy) && !grids.isWalkable(neighbor.x - dx, neighbor.y))
                    || (grids.isWalkable(neighbor.x + dx, neighbor.y - dy) && !grids.isWalkable(neighbor.x, neighbor.y - dy))) {
                return neighbor;
            }
            // when moving diagonally, must check for vertical/horizontal jump points
            if (jump(grids, grids.getGrid(neighbor.x + dx, neighbor.y), neighbor, goals) != null
                    || jump(grids, grids.getGrid(neighbor.x, neighbor.y + dy), neighbor, goals) != null) {
                return neighbor;
            }
        } else { // check horizontally/vertically
            if (dx != 0) {
                if ((grids.isWalkable(neighbor.x + dx, neighbor.y + 1) && !grids.isWalkable(neighbor.x, neighbor.y + 1))
                        || (grids.isWalkable(neighbor.x + dx, neighbor.y - 1) && !grids.isWalkable(neighbor.x, neighbor.y - 1))) {
                    return neighbor;
                }
            } else {
                if ((grids.isWalkable(neighbor.x + 1, neighbor.y + dy) && !grids.isWalkable(neighbor.x + 1, neighbor.y))
                        || (grids.isWalkable(neighbor.x - 1, neighbor.y + dy) && !grids.isWalkable(neighbor.x - 1, neighbor.y))) {
                    return neighbor;
                }
            }
        }

        // jump diagonally towards our goal
        return jump(grids, grids.getGrid(neighbor.x + dx, neighbor.y + dy), neighbor, goals);
    }

}
