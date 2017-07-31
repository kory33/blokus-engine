package com.github.kory33.blokus.game;

import com.github.kory33.blokus.game.color.PlayerColor;
import com.github.kory33.blokus.game.data.BlokusGameData;
import com.github.kory33.blokus.game.data.PlacementHoldings;
import com.github.kory33.blokus.util.IntegerVector;
import com.github.kory33.blokus.util.SetUtil;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/*package-private*/ class BlokusBoardMesh {
    private final BlokusMeshMatrix meshMatrix;
    private final PlayerColor playerColor;

    // for optimization:
    private boolean hasExploredBefore = false;

    private Set<BlokusMeshNode> placementRootCells;
    private Set<BlokusMeshNode> unavailableNodes;
    private HashMap<IntegerVector, HashSet<BlokusPlacement>> placementsContainingCoordinateMap;
    private Set<BlokusPlacement> placementSetCache;

    /*package-private*/ BlokusBoardMesh(@NotNull PlayerColor playerColor) {
        this.placementRootCells = new HashSet<>();
        this.unavailableNodes = new HashSet<>();
        this.placementsContainingCoordinateMap = new HashMap<>();
        this.placementSetCache = new HashSet<>();

        this.playerColor = playerColor;

        // prepare disconnected grid nodes
        this.meshMatrix = new BlokusMeshMatrix();

        // add connections
        this.meshMatrix.getVectorSpace().forEach(vector -> {
            BlokusMeshNode targetCell = this.meshMatrix.getNodeAt(vector);
            assert targetCell != null;

            BlokusMeshNode rightMeshCell = this.meshMatrix.getNodeAt(vector.getX() + 1, vector.getY());
            BlokusMeshNode bottomCell = this.meshMatrix.getNodeAt(vector.getX(), vector.getY() + 1);

            if (rightMeshCell != null) targetCell.addEdgeTo(rightMeshCell);
            if (bottomCell != null) targetCell.addEdgeTo(bottomCell);

            this.placementsContainingCoordinateMap.put(vector, new HashSet<>());
        });

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

    private BlokusBoardMesh(BlokusBoardMesh blokusBoardMesh) {
        this.placementRootCells = new HashSet<>(blokusBoardMesh.placementRootCells);
        this.unavailableNodes = new HashSet<>(blokusBoardMesh.unavailableNodes);
        this.placementSetCache = new HashSet<>(blokusBoardMesh.placementSetCache);

        this.placementsContainingCoordinateMap = new HashMap<>();
        blokusBoardMesh.placementsContainingCoordinateMap.forEach((vector, placementsOnVector) ->
                this.placementsContainingCoordinateMap.put(vector, new HashSet<>(placementsOnVector))
        );

        this.playerColor = blokusBoardMesh.playerColor;

        BlokusMeshMatrix oldMeshMatrix = blokusBoardMesh.meshMatrix;
        this.meshMatrix = new BlokusMeshMatrix();
        this.meshMatrix.getVectorSpace().forEach(vector -> {
            BlokusMeshNode newNode = meshMatrix.getNodeAt(vector);
            BlokusMeshNode correspondingOldNode = oldMeshMatrix.getNodeAt(vector);

            assert newNode != null;
            assert correspondingOldNode != null;

            Set<BlokusMeshNode> nodesConnectedToNewNode =
                    correspondingOldNode.getConnectedNodes().stream()
                        .map(oldMeshMatrix::getCoordinateOf)
                        .map(meshMatrix::getNodeAt)
                        .collect(Collectors.toSet());

            nodesConnectedToNewNode.forEach(newNode::addEdgeTo);
        });
    }

    private void markPlacementAsUnavailable(BlokusPlacement placement) {
        this.placementSetCache.remove(placement);
    }

    private void markCellAsUnavailable(BlokusMeshNode cellNode) {
        this.unavailableNodes.add(cellNode);

        cellNode.disconnectAllEdges();
        this.placementRootCells.remove(cellNode);

        IntegerVector cellCoordinate = this.meshMatrix.getCoordinateOf(cellNode);

        this.placementsContainingCoordinateMap
                .get(cellCoordinate)
                .forEach(this::markPlacementAsUnavailable);
        this.placementsContainingCoordinateMap.put(cellCoordinate, new HashSet<>());
    }

    /**
     * This method computes the changes on the mesh when a given placement is made by the opponent.
     *
     * Internally, placement cells are merely disconnected from the surrounding cells in the mesh graph.
     * @param placement Placement made by the opponent.
     */
    /*package-private*/ void makeOpponentPlacement(BlokusPlacement placement) {
        // disconnect from all surrounding edges
        placement.map(this.meshMatrix::getNodeAt).forEach(this::markCellAsUnavailable);
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
        placementRootCandidate.removeIf(this.unavailableNodes::contains);

        // disconnect all surrounding edges from the cells directly adjacent to one of the placement cells
        Set<BlokusMeshNode> cellsDirectlyAdjacentToPlacementCell = new HashSet<>();
        placementNodes.forEach(placementNode -> {
            cellsDirectlyAdjacentToPlacementCell.addAll(placementNode.getConnectedNodes());
        });

        // update placement root cells
        this.placementRootCells.addAll(placementRootCandidate);

        cellsDirectlyAdjacentToPlacementCell.forEach(this::markCellAsUnavailable);
        placementNodes.forEach(this::markCellAsUnavailable);
    }

    private void registerFoundPlacement(BlokusPlacement placement) {
        this.placementSetCache.add(placement);

        placement.getCellCoordinates().forEach(cellVec -> {
            this.placementsContainingCoordinateMap.get(cellVec).add(placement);
        });
    }

    /**
     * @param placementHoldings Object representing the player's remaining placement holdings.
     * @param visitedNodes nodes that have been visited.
     */
    private void exploreFurtherPlacements(PlacementHoldings placementHoldings,
                                          Set<BlokusMeshNode> visitedNodes) {
        if (placementHoldings.getMaximumPlacementSize() == 0) {
            return;
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
                    this.registerFoundPlacement(constructedPlacement);
                    continue;
                }

                if (placementHoldings.isAvailable(nextExplorationRoot.size())) {
                    if (this.placementSetCache.contains(constructedPlacement)) {
                        continue;
                    }
                    this.registerFoundPlacement(constructedPlacement);
                }

                exploreFurtherPlacements(placementHoldings, nextExplorationRoot);
            }
        }
    }

    /**
     * Obtain a set containing all the next possible placements.
     * @return a set containing all the next possible placements.
     * @param gameData data of the game
     */
    /*package-private*/ Set<BlokusPlacement> getPossiblePlacements(BlokusGameData gameData) {
        PlacementHoldings placementHoldings = gameData.getPlacementHoldingsOf(this.playerColor);

        if (hasExploredBefore) {
            // remove placements whose sizes are no longer place-able.
            this.placementSetCache.removeIf(placement -> !placementHoldings.isAvailable(placement.size()));
        } else {
            hasExploredBefore = true;
        }

        if (this.placementRootCells.isEmpty()) {
            return this.placementSetCache;
        }

        this.placementRootCells.forEach(rootCell -> {
            Set<BlokusMeshNode> visitedNodes = new HashSet<>();
            visitedNodes.add(rootCell);

            exploreFurtherPlacements(placementHoldings, visitedNodes);
        });
        this.placementRootCells = new HashSet<>();

        return new HashSet<>(this.placementSetCache);
    }

    /**
     * Convert the mesh into its string representation.
     * In the string representation, <code>-</code>, <code>|</code>, <code>O</code>, <code>*</code> stands for
     * horizontal edge, vertical edge, normal node and placement root cell respectively.
     */
    @Override
    public String toString() {
        StringBuilder resultString = new StringBuilder();

        for (int row = 1; row <= BlokusConstant.BOARD_SIZE; row++) {
            StringBuilder edgeRowStringBuilder = new StringBuilder();
            for (int column = 1; column <= BlokusConstant.BOARD_SIZE; column++) {
                BlokusMeshNode targetCell = this.meshMatrix.getNodeAt(column, row);
                assert targetCell != null;

                if (this.placementRootCells.contains(targetCell)) {
                    resultString.append("*");
                } else {
                    resultString.append("O");
                }
                if (column != BlokusConstant.BOARD_SIZE) {
                    BlokusMeshNode rightMeshCell = this.meshMatrix.getNodeAt(column + 1, row);
                    assert rightMeshCell != null;

                    if (targetCell.getConnectedNodes().contains(rightMeshCell)) {
                        resultString.append(" - ");
                    } else {
                        resultString.append("   ");
                    }
                }

                if (row != BlokusConstant.BOARD_SIZE) {
                    BlokusMeshNode bottomCell = this.meshMatrix.getNodeAt(column, row + 1);
                    assert bottomCell != null;
                    if (targetCell.getConnectedNodes().contains(bottomCell)) {
                        edgeRowStringBuilder.append("|   ");
                    } else {
                        edgeRowStringBuilder.append("    ");
                    }
                }
            }
            resultString.append("\n").append(edgeRowStringBuilder.toString()).append("\n");
        }
        return resultString.toString();
    }

    public BlokusBoardMesh getCopy() {
        return new BlokusBoardMesh(this);
    }
}
