package com.github.kory33.blokus.game;

import com.github.kory33.blokus.game.data.PlacementHoldings;
import com.github.kory33.blokus.util.IntegerVector;
import com.github.kory33.blokus.util.SetUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BlokusPlacementSpace {
    private static Set<BlokusPlacement> exploreFurtherPlacements(Set<BlokusMeshNode> visitedNodes,
                                                                 BlokusMeshMatrix meshMatrix) {
        PlacementHoldings initialHoldings = new PlacementHoldings();

        Set<BlokusPlacement> explorationResult = new HashSet<>();
        visitedNodes.forEach(visitedNode -> {
            visitedNode.getConnectedNodes().forEach(nextExplorationNode -> {
                if (visitedNodes.contains(nextExplorationNode)) {
                    return;
                }
                Set<BlokusMeshNode> nextExplorationRoot = new HashSet<>(visitedNodes);
                nextExplorationRoot.add(nextExplorationNode);

                Set<IntegerVector> nextExplorationRootCoords = SetUtil.map(nextExplorationRoot, meshMatrix::getCoordinateOf);
                BlokusPlacement constructedPlacement = new BlokusPlacement(nextExplorationRootCoords);

                if (constructedPlacement.size() == initialHoldings.getMaximumPlacementSize()) {
                    explorationResult.add(constructedPlacement);
                    return;
                }

                if (initialHoldings.isAvailable(constructedPlacement.size())) {
                    explorationResult.add(constructedPlacement);
                }

                explorationResult.addAll(exploreFurtherPlacements(nextExplorationRoot, meshMatrix));
            });
        });
        return explorationResult;
    }

    private static Set<BlokusPlacement> getAllPossiblePlacements(BlokusMeshMatrix meshMatrix) {
        Set<BlokusPlacement> placementSet = new HashSet<>();

        meshMatrix.getVectorSpace().parallelStream()
                .map(meshMatrix::getNodeAt)
                .forEach(node -> {
                    Set<BlokusMeshNode> visitedNodes = new HashSet<>();
                    visitedNodes.add(node);
                    placementSet.addAll(exploreFurtherPlacements(visitedNodes, meshMatrix));
                });

        return placementSet;
    }

    /**
     * List of all the possible placements
     *
     * Order of placements in this list should not be environment specific
     * i.e. it should be ensured that the ColoredBlokusPlacement at index n in range of [1, N]
     * always points to the same placement.
     */
    public static final ArrayList<BlokusPlacement> PLACEMENT_LIST;
    static {
        // prepare disconnected grid nodes
        BlokusMeshMatrix meshMatrix = new BlokusMeshMatrix();
        // add connections
        meshMatrix.getVectorSpace().forEach(vector -> {
            BlokusMeshNode targetCell = meshMatrix.getNodeAt(vector);
            assert targetCell != null;

            BlokusMeshNode rightMeshCell = meshMatrix.getNodeAt(vector.getX() + 1, vector.getY());
            BlokusMeshNode bottomCell = meshMatrix.getNodeAt(vector.getX(), vector.getY() + 1);

            if (rightMeshCell != null) targetCell.addEdgeTo(rightMeshCell);
            if (bottomCell != null) targetCell.addEdgeTo(bottomCell);
        });

        List<BlokusPlacement> orderedPossiblePlacements =
                getAllPossiblePlacements(meshMatrix)
                        .stream()
                        .sorted(BlokusPlacement::compareTo)
                        .collect(Collectors.toList());
        PLACEMENT_LIST = new ArrayList<>(orderedPossiblePlacements);
    }
}
