package com.github.kory33.blokus.game;

import com.github.kory33.blokus.game.cell.BlokusMeshNode;
import com.github.kory33.blokus.game.cell.PlayerColor;
import com.github.kory33.blokus.util.IntegerVector;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

/*package-private*/ class BlokusBoardMesh {
    private final BlokusMeshMatrix meshMatrix;
    private final Set<BlokusMeshNode> placementRootCells = new HashSet<>();

    /*package-private*/ BlokusBoardMesh(@NotNull PlayerColor playerColor) {
        final Integer meshSize = BlokusConstant.BOARD_SIZE;

        // prepare disconnected grid nodes
        this.meshMatrix = new BlokusMeshMatrix(meshSize);

        // add connections
        for (int column = 1; column <= BlokusConstant.BOARD_SIZE; column++) {
            for (int row = 1; row <= BlokusConstant.BOARD_SIZE; row++) {
                BlokusMeshNode targetCell = this.meshMatrix.getNodeAt(column, row);
                assert targetCell != null;

                // add undirected arrow toward the cell on the right
                if (column != BlokusConstant.BOARD_SIZE) {
                    BlokusMeshNode rightMeshCell = this.meshMatrix.getNodeAt(column + 1, row);

                    assert rightMeshCell != null;
                    targetCell.addEdgeTo(rightMeshCell);
                }

                // add undirected arrow toward the cell to the bottom
                if (row != BlokusConstant.BOARD_SIZE) {
                    BlokusMeshNode bottomCell = this.meshMatrix.getNodeAt(column, row + 1);

                    assert bottomCell != null;
                    targetCell.addEdgeTo(bottomCell);
                }
            }
        }

        // define initial placement root
        IntegerVector initialPlacementRoot = null;
        switch(playerColor) {
            case RED:
                initialPlacementRoot = BlokusConstant.RED_BEGIN_COORDINATE;
                break;
            case BLUE:
                initialPlacementRoot = BlokusConstant.BLUE_BEGIN_COORDINATE;
        }
        placementRootCells.add(meshMatrix.getNodeAt(initialPlacementRoot));
    }

    /**
     * This method computes the changes on the mesh when a given placement is made by the opponent.
     *
     * Internally, placement cells are merely disconnected from the surrounding cells in the mesh graph.
     * @param placement Placement made by the opponent.
     */
    /*package-private*/ void makeEnemyOpponent(BlokusPlacement placement) {
        // disconnect all surrounding edges
        placement.forEach(BlokusMeshNode::disconnectAllEdges);
    }

    /**
     * This method computes the changes on the mesh when a given placement is made by the player.
     * <p>
     * Internally, placement cells are merely disconnected from the surrounding cells in the mesh graph.
     * In addition, cells on which the player cannot make a placement will also be disconnected.
     * <p>
     * Cells from which the player can newly begin placement will be marked as new "root cells".
     * </p>
     * @param placement Placement made by the player.
     */
    /*package-private*/ void makePlayerPlacement(BlokusPlacement placement) {
        // mark every nodes diagonally adjacent to any one nodes in the placement as "placement root" candidate
        Set<BlokusMeshNode> placementRootCandidate = new HashSet<>();
        placement.forEach(node -> placementRootCandidate.addAll(this.meshMatrix.getCellsDiagonallyAdjacentTo(node)));

        // disconnect all surrounding edges from the cells directly adjacent to one of the placement cells
        Set<BlokusMeshNode> cellsDirectlyAdjacentToPlacementCell = new HashSet<>();
        placement.forEach(placementNode -> {
            Set<BlokusMeshNode> connectedNodes = placementNode.getConnectedNodes();
            connectedNodes.removeIf(placement::contains);
            cellsDirectlyAdjacentToPlacementCell.addAll(connectedNodes);
        });
        cellsDirectlyAdjacentToPlacementCell.forEach(BlokusMeshNode::disconnectAllEdges);

        // update placement root cells
        this.placementRootCells.addAll(placementRootCandidate);

        // remove adjacent cells from placement root candidates. (no placement can be made from these cells)
        cellsDirectlyAdjacentToPlacementCell.forEach(placementRootCandidate::remove);
    }

    /**
     * Obtain a set containing all the next possible placements.
     * @return a set containing all the next possible placements.
     */
    /*package-private*/ Set<BlokusPlacement> getPossiblePlacement() {
        // TODO
        return new HashSet<>();
    }
}
