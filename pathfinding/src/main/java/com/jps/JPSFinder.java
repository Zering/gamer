package com.jps;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * Jump Point Search 跳点寻路算法
 */
public class JPSFinder {

    protected static final Log log = LogFactory.getLog(JPSFinder.class);
    private Diagonal diagonal;

    public JPSFinder(Diagonal diagonal) {
        this.diagonal = diagonal;
    }

    public Queue<Grid> findPath(Grids grids, Grid start, Grid goal, boolean adjacentStop, boolean diagonalStop) {
        //long nt = System.nanoTime();
        final Map<Grid, Double> fMap = Maps.newHashMap();//distance to start + estimate to end
        Map<Grid, Double> gMap = Maps.newHashMap();//distance to start (parent's g + distance from parent)
        Map<Grid, Double> hMap = Maps.newHashMap();//estimate to end
        Queue<Grid> opened = new PriorityQueue<>(11, Comparator.comparingDouble(a -> getOrDefault(fMap, a, 0d)));
        Set<Grid> closed = Sets.newHashSet();
        Map<Grid, Grid> parentMap = Maps.newHashMap();
        Set<Grid> goals = Sets.newHashSet();
        try {
            if (adjacentStop) {
                if (!diagonalStop) {
                    goals = grids.getNeighborsOf(goal, Grids.Diagonal.NEVER);
                } else {
                    goals = diagonal.findNeighbors(grids, goal, parentMap);
                }
            }
            if (goal.isWalkable()) {
                goals.add(goal);
            }
            if (goals.isEmpty()) {
                return null;
            }
            opened.add(start);// push the start node into the open list
            while (!opened.isEmpty()) {// while the open list is not empty
                Grid g = opened.poll();// pop the position of node which has the minimum `f` value.
                closed.add(g);// mark the current node as checked
                if (goals.contains(g)) {
                    return backTrace(grids, g, parentMap);
                }
                // add all possible next steps from the current node
                identifySuccessors(grids, g, goal, goals, opened, closed, parentMap, fMap, gMap, hMap);
            }
            return null;// failed to find a path
        } finally {
            fMap.clear();
            gMap.clear();
            hMap.clear();
            opened.clear();
            closed.clear();
            parentMap.clear();
            goals.clear();
            gMap = null;
            hMap = null;
            opened = null;
            closed = null;
            parentMap = null;
            goals = null;
            //log.error(start + "->" + goal + "=" + (System.nanoTime() - nt) / 1000000.0 + " ms");
        }
    }

    /**
     * Identify successors for the given node. Runs a JPS in direction of each available neighbor, adding any open nodes
     * found to the open list.
     * return All the nodes we have found jumpable from the current node
     */
    private void identifySuccessors(Grids grids, Grid g, Grid goal, Set<Grid> goals, Queue<Grid> opened, Set<Grid> closed, Map<Grid, Grid> parentMap,
            Map<Grid, Double> fMap, Map<Grid, Double> gMap, Map<Grid, Double> hMap) {
        // get all neighbors to the current node
        Collection<Grid> neighbors = diagonal.findNeighbors(grids, g, parentMap);
        double d;
        double ng;
        for (Grid neighbor : neighbors) {
            // jump in the direction of our neighbor
            Grid jump = diagonal.jump(grids, neighbor, g, goals);
            // don't add a node we have already gotten to quicker
            if (jump == null || closed.contains(jump)) {
                continue;
            }
            // determine the jumpNode's distance from the start along the current path
            d = grids.getDistance(jump, g);
            ng = getOrDefault(gMap, g, 0d) + d;

            // if the node has already been opened and this is a shorter path, update it
            // if it hasn't been opened, mark as open and update it
            if (!opened.contains(jump) || ng < getOrDefault(gMap, jump, 0d)) {
                gMap.put(jump, ng);
                hMap.put(jump, grids.getHeuristicDistance(jump, goal));
                fMap.put(jump, ng + getOrDefault(hMap, jump, 0d));
                //System.out.println("jumpNode: " + jumpNode.x + "," + jumpNode.y + " f: " + fMap.get(jumpNode));
                parentMap.put(jump, g);

                if (!opened.contains(jump)) {
                    opened.offer(jump);
                }
            }
        }
    }

    /**
     * Returns a path of the parent nodes from a given node.
     */
    private Queue<Grid> backTrace(Grids grids, Grid g, Map<Grid, Grid> parentMap) {
        LinkedList<Grid> path = new LinkedList<>();
        path.add(g);
        int prevX, prevY, currX, currY;
        int dx, dy;
        int steps;
        Grid temp;
        while (true) {
            Grid prev = parentMap.get(g);
            if (prev == null) {
                break;
            }
            prevX = prev.x;
            prevY = prev.y;
            currX = g.x;
            currY = g.y;
            steps = Math.max(Math.abs(prevX - currX), Math.abs(prevY - currY));
            dx = Integer.compare(prevX, currX);
            dy = Integer.compare(prevY, currY);

            temp = g;
            for (int i = 0; i < steps; i++) {
                temp = grids.getGrid(temp.x + dx, temp.y + dy);
                path.addFirst(temp);
            }
            g = parentMap.get(g);
            if (g == null) {
                break;
            }
        }
        return path;
    }

    private Double getOrDefault(Map<Grid, Double> fMap, Grid k, Double dft) {
        Double d0 = fMap.get(k);
        return d0 != null ? d0 : dft;
    }
}
