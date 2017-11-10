package com.jps;

import java.util.Map;
import java.util.Set;

/**
 * 处理对角线行走逻辑
 */
public abstract class Diagonal {

    /**
     * Find all neighbors for a given node. If node has a parent then prune neighbors based on JPS algorithm, otherwise
     * 
     * @param grids
     * @param g
     * @param parentMap
     * @return
     */
    public abstract Set<Grid> findNeighbors(Grids grids, Grid g, Map<Grid, Grid> parentMap);

    /**
     * Search towards the child from the parent, returning when a jump point is found.
     * 
     * @param grids
     * @param neighbor
     * @param current
     * @param goals
     * @return
     */
    public abstract Grid jump(Grids grids, Grid neighbor, Grid current, Set<Grid> goals);
}
