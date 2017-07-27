package com.github.kory33.blokus.game;

import com.github.kory33.blokus.game.color.PlayerColor;
import com.github.kory33.blokus.game.data.BlokusGameData;
import com.github.kory33.blokus.game.data.PlacementHoldings;
import com.github.kory33.blokus.util.IntegerVector;
import com.github.kory33.blokus.util.SetUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

/*package-private*/ class BlokusBoardMesh {
    private final BlokusMeshMatrix meshMatrix;
    private final PlayerColor playerColor;
    private final Set<BlokusMeshNode> placementRootCells = new HashSet<>();

    @Nullable
    private Set<BlokusPlacement> placementSetCache;

    /*package-private*/ BlokusBoardMesh(@NotNull PlayerColor playerColor) {
        this.playerColor = playerColor;

        final Integer meshSize = BlokusConstant.BOARD_SIZE;

        // prepare disconnected grid nodes
        this.meshMatrix = new BlokusMeshMatrix(meshSize);

        // add connections
        for (int column = 1; column <= BlokusConstant.BOARD_SIZE; column++) {
            for (int row = 1; row <= BlokusConstant.BOARD_SIZE; row++) {
                BlokusMeshNode targetCell = this.meshMatrix.getNodeAt(column, row);
                assert targetCell != null;

                // add undirected arrow toward the color on the right
                if (column != BlokusConstant.BOARD_SIZE) {
                    BlokusMeshNode rightMeshCell = this.meshMatrix.getNodeAt(column + 1, row);

                    assert rightMeshCell != null;
                    targetCell.addEdgeTo(rightMeshCell);
                }

                // add undirected arrow toward the color to the bottom
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
    /*package-private*/ void makeOpponentPlacement(BlokusPlacement placement) {
        // disconnect from all surrounding edges
        placement.map(this.meshMatrix::getNodeAt).forEach(node -> {
            node.disconnectAllEdges();
            this.placementRootCells.remove(node);
        });

        // clear cache
        this.placementSetCache = null;
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
        Set<BlokusMeshNode> placementNodes = placement.map(this.meshMatrix::getNodeAt);

        // mark every nodes diagonally adjacent to any one nodes in the placement as "placement root" candidate
        Set<BlokusMeshNode> placementRootCandidate = new HashSet<>();
        placementNodes.forEach(node -> {
            placementRootCandidate.addAll(this.meshMatrix.getCellsDiagonallyAdjacentTo(node));
        });

        // disconnect all surrounding edges from the cells directly adjacent to one of the placement cells
        Set<BlokusMeshNode> cellsDirectlyAdjacentToPlacementCell = new HashSet<>();
        placementNodes.forEach(placementNode -> {
            Set<BlokusMeshNode> connectedNodes = placementNode.getConnectedNodes();
            connectedNodes.removeIf(placementNodes::contains);
            cellsDirectlyAdjacentToPlacementCell.addAll(connectedNodes);
        });
        cellsDirectlyAdjacentToPlacementCell.forEach(BlokusMeshNode::disconnectAllEdges);

        // update placement root cells
        this.placementRootCells.addAll(placementRootCandidate);

        // remove adjacent cells from placement root candidates. (no placement can be made from these cells)
        cellsDirectlyAdjacentToPlacementCell.forEach(placementRootCandidate::remove);
        placementNodes.forEach(placementRootCandidate::remove);

        // clear cache
        this.placementSetCache = null;
    }

    /**
     * @param placementHoldings Object representing the player's remaining placement holdings.
     * @param found set of placements that are already found before the exploration session begins.
     * @param visitedNodes nodes that have been visited.
     * @param foundInThisExploration set of placements that are found in this exploration session.
     * @return set of placements that can be derived from the given visited nodes.
     */
    private Set<BlokusPlacement> exploreFurtherPlacements(PlacementHoldings placementHoldings,
                                                          Set<BlokusPlacement> found,
                                                          Set<BlokusMeshNode> visitedNodes,
                                                          Set<BlokusPlacement> foundInThisExploration) {
        if (placementHoldings.getMaximumPlacementSize() == 0) {
            return new HashSet<>();
        }

        for (BlokusMeshNode visitedNode: visitedNodes) {
            for (BlokusMeshNode nextExplorationNode: visitedNode.getConnectedNodes()) {
                if (visitedNodes.contains(nextExplorationNode)) {
                    continue;
                }
                Set<BlokusMeshNode> nextExplorationRoot = new HashSet<>(visitedNodes);
                nextExplorationRoot.add(nextExplorationNode);

                Set<IntegerVector> nextExplorationRootCoords = SetUtil.map(nextExplorationRoot, this.meshMatrix::getCoordinateOf);
                BlokusPlacement constructedPlacement = new BlokusPlacement(nextExplorationRootCoords, this.playerColor);

                if (nextExplorationRoot.size() == placementHoldings.getMaximumPlacementSize()) {
                    foundInThisExploration.add(constructedPlacement);
                    continue;
                }

                if (placementHoldings.isAvailable(nextExplorationRoot.size())) {
                    if (found.contains(constructedPlacement) || foundInThisExploration.contains(constructedPlacement)) {
                        continue;
                    }
                    foundInThisExploration.add(constructedPlacement);
                }

                Set<BlokusPlacement> nextExplorationResult =
                        exploreFurtherPlacements(placementHoldings, found, nextExplorationRoot, foundInThisExploration);
                foundInThisExploration.addAll(nextExplorationResult);
            }
        }

        return foundInThisExploration;
    }

    /**
     * Obtain a set containing all the next possible placements.
     * @return a set containing all the next possible placements.
     * @param gameData data of the game
     */
    /*package-private*/ Set<BlokusPlacement> getPossiblePlacements(BlokusGameData gameData) {
        if (this.placementSetCache != null) {
            return new HashSet<>(this.placementSetCache);
        }

        PlacementHoldings placementHoldings = gameData.getPlacementHoldingsOf(this.playerColor);

        Set<BlokusPlacement> foundPlacements = new HashSet<>();
        Set<BlokusMeshNode> unusablePlacementRoots = new HashSet<>();

        this.placementRootCells.forEach(rootCell -> {
            Set<BlokusMeshNode> visitedNodes = new HashSet<>();
            visitedNodes.add(rootCell);

            Set<BlokusPlacement> explorationResult = exploreFurtherPlacements(placementHoldings, foundPlacements, visitedNodes, new HashSet<>());

            if (explorationResult.size() == 0) {
                unusablePlacementRoots.add(rootCell);
            } else {
                foundPlacements.addAll(explorationResult);
            }
        });

        // update root cells
        unusablePlacementRoots.forEach(this.placementRootCells::remove);

        this.placementSetCache = foundPlacements;
        return foundPlacements;
    }
}
