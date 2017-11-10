package com.jps;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 对角最多有一个障碍物时可以走对角线
 */
public class DiagonalOneObstacles extends Diagonal {

    @Override
    public Set<Grid> findNeighbors(Grids grids, Grid g, Map<Grid, Grid> parentMap) {
        Set<Grid> neighbors = new HashSet<Grid>();

        Grid parent = parentMap.get(g);
        if (parent != null) {
            final int x = g.x;
            final int y = g.y;
            // get normalized direction of travel
            final int dx = (x - parent.x) / Math.max(Math.abs(x - parent.x), 1);
            final int dy = (y - parent.y) / Math.max(Math.abs(y - parent.y), 1);

            // search diagonally
            if (dx != 0 && dy != 0) {
                boolean w1 = grids.addReachableNeighbor(x, y + dy, neighbors);
                boolean w2 = grids.addReachableNeighbor(x + dx, y, neighbors);
                if ((w1 || w2) && grids.isWalkable(x + dx, y + dy)) {
                    Grid neighbor = grids.getGrid(x + dx, y + dy);
                    if (neighbor != null && neighbor.isWalkable()) {
                        neighbors.add(neighbor);
                    }
                }
                Grid n1 = grids.getGrid(x - dx, y);
                Grid n2 = grids.getGrid(x, y + dy);
                Grid n3 = grids.getGrid(x - dx, y + dy);
                if ((!n1.isWalkable() && n2.isWalkable()) && n3.isWalkable()) {
                    neighbors.add(n3);
                }
                n1 = grids.getGrid(x, y - dy);
                n2 = grids.getGrid(x + dx, y);
                n3 = grids.getGrid(x + dx, y - dy);
                if ((!n1.isWalkable() && n2.isWalkable()) && n3.isWalkable()) {
                    neighbors.add(n3);
                }
            } else { // search horizontally/vertically
                if (dx == 0) {
                    if (grids.addReachableNeighbor(x, y + dy, neighbors)) {
                        grids.addUnreachableNeighbor(x + 1, y, x + 1, y + dy, neighbors);
                        grids.addUnreachableNeighbor(x - 1, y, x - 1, y + dy, neighbors);
                    }
                } else {
                    if (grids.addReachableNeighbor(x + dx, y, neighbors)) {
                        grids.addUnreachableNeighbor(x, y + 1, x + dx, y + 1, neighbors);
                        grids.addUnreachableNeighbor(x, y - 1, x + dx, y - 1, neighbors);
                    }
                }
            }
        } else {
            // no parent, return all neighbors
            neighbors.addAll(grids.getNeighborsOf(g, Grids.Diagonal.ONE_OBSTACLE));
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
        System.out.println(current + "->" + neighbor + ",dx=" + dx + ",dy=" + dy);
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

        // moving diagonally, must make sure one of the vertical/horizontal
        // neighbors is open to allow the path
        if (grids.isWalkable(neighbor.x + dx, neighbor.y) || grids.isWalkable(neighbor.x, neighbor.y + dy)) {
            return jump(grids, grids.getGrid(neighbor.x + dx, neighbor.y + dy), neighbor, goals);
        } else {
            return null;
        }
    }

}
