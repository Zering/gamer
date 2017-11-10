package com.jps;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 对角没有障碍物时可以走对角线
 */
public class DiagonalNoObstacles extends Diagonal {

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
            if ((dx & dy) != 0) {
                boolean w1 = grids.addReachableNeighbor(x, y + dy, neighbors);
                boolean w2 = grids.addReachableNeighbor(x + dx, y, neighbors);
                if (w1 && w2) {
                    neighbors.add(grids.getGrid(x + dx, y + dy));
                }
            } else { // search horizontally/vertically
                if (dx != 0) {
                    Grid next = grids.getGrid(x + dx, y);
                    Grid up = grids.getGrid(x, y + 1);
                    Grid down = grids.getGrid(x, y - 1);

                    if (next != null && next.isWalkable()) {
                        neighbors.add(next);
                        if (up != null && up.isWalkable()) {
                            neighbors.add(grids.getGrid(x + dx, y + 1));
                        }
                        if (down != null && down.isWalkable()) {
                            neighbors.add(grids.getGrid(x + dx, y - 1));
                        }
                    }
                    if (up != null && up.isWalkable()) {
                        neighbors.add(up);
                    }
                    if (down != null && down.isWalkable()) {
                        neighbors.add(down);
                    }
                } else if (dy != 0) {
                    Grid next = grids.getGrid(x, y + dy);
                    Grid right = grids.getGrid(x + 1, y);
                    Grid left = grids.getGrid(x - 1, y);
                    if (next != null && next.isWalkable()) {
                        neighbors.add(next);
                        if (right != null && right.isWalkable()) {
                            neighbors.add(grids.getGrid(x + 1, y + dy));
                        }
                        if (left != null && left.isWalkable()) {
                            neighbors.add(grids.getGrid(x - 1, y + dy));
                        }
                    }
                    if (right != null && right.isWalkable()) {
                        neighbors.add(right);
                    }
                    if (left != null && left.isWalkable()) {
                        neighbors.add(left);
                    }
                }
            }
        } else {
            // return all neighbors
            neighbors.addAll(grids.getNeighborsOf(g, Grids.Diagonal.NO_OBSTACLES));
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

        // check for forced neighbors (eliminate symmetrical paths)
        // check along diagonal
        if ((dx & dy) != 0) {
            // when moving diagonally, must check for vertical/horizontal jump points
            if (jump(grids, grids.getGrid(neighbor.x + dx, neighbor.y), neighbor, goals) != null
                    || jump(grids, grids.getGrid(neighbor.x, neighbor.y + dy), neighbor, goals) != null) {
                return neighbor;
            }
        } else { // check along horizontal/vertical
            if (dx != 0) {
                if ((grids.isWalkable(neighbor.x, neighbor.y - 1) && !grids.isWalkable(neighbor.x - dx, neighbor.y - 1))
                        || (grids.isWalkable(neighbor.x, neighbor.y + 1) && !grids.isWalkable(neighbor.x - dx, neighbor.y + 1))) {
                    return neighbor;
                }
            } else if (dy != 0) {
                if ((grids.isWalkable(neighbor.x - 1, neighbor.y) && !grids.isWalkable(neighbor.x - 1, neighbor.y - dy))
                        || (grids.isWalkable(neighbor.x + 1, neighbor.y) && !grids.isWalkable(neighbor.x + 1, neighbor.y - dy))) {
                    return neighbor;
                }
            }
        }

        // moving diagonally must make sure both of the vertical/horizontal neighbors is open to allow the path
        if (grids.isWalkable(neighbor.x + dx, neighbor.y) && grids.isWalkable(neighbor.x, neighbor.y + dy)) {
            return jump(grids, grids.getGrid(neighbor.x + dx, neighbor.y + dy), neighbor, goals);
        } else {
            return null;
        }
    }

}
