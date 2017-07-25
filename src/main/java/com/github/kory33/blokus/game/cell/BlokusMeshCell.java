package com.github.kory33.blokus.game.cell;

import com.github.kory33.blokus.util.GraphNode;

/**
 * This class represents a node in a mesh-representation of a Blokus board.
 */
public class BlokusMeshCell extends GraphNode<Void> {
    /**
     * Create empty mesh node
     */
    public BlokusMeshCell() {
        //noinspection ConstantConditions
        super(null);
    }
}
