package com.github.kory33.blokus.game;

import com.github.kory33.blokus.util.BijectiveHashMap;
import com.github.kory33.blokus.util.IntegerVector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A class representing a matrix of mesh nodes.
 * No connections between nodes are created during the initialization of this object.
 */
/*package-private*/ class BlokusMeshMatrix {
    private static final HashSet<IntegerVector> DIAGONAL_UNIT_VECS;
    static {
        IntegerVector[] vectorArray = {
                new IntegerVector(1, 1),
                new IntegerVector(1, -1),
                new IntegerVector(-1, 1),
                new IntegerVector(-1, -1)
        };
        DIAGONAL_UNIT_VECS = new HashSet<>(Arrays.asList(vectorArray));
    }
    @NotNull
    private final BijectiveHashMap<IntegerVector, BlokusMeshNode> meshCoordinateMap = new BijectiveHashMap<>();

    /*package-private*/ BlokusMeshMatrix(@NotNull Integer matrixSize) {
        for (int column = 1; column <= matrixSize; column++) {
            for (int row = 1; row <= matrixSize; row++) {
                IntegerVector coordinate = new IntegerVector(column, row);
                this.meshCoordinateMap.put(coordinate, new BlokusMeshNode());
            }
        }
    }

    @NotNull
    BlokusMeshNode getNodeAt(@NotNull IntegerVector targetCoordinate) {
        BlokusMeshNode node = this.meshCoordinateMap.getValue(targetCoordinate);

        if (node == null) {
            throw new IllegalArgumentException("Given vector indicates a coordinate outside the mesh matrix.");
        }

        return node;
    }

    @Nullable
    BlokusMeshNode getNodeAt(@NotNull Integer coordinateX, @NotNull Integer coordinateY) {
        return this.getNodeAt(new IntegerVector(coordinateX, coordinateY));
    }

    @Nullable
    public IntegerVector getCoordinateOf(@NotNull BlokusMeshNode cell) {
        return this.meshCoordinateMap.getKey(cell);
    }

    Set<BlokusMeshNode> getCellsDiagonallyAdjacentTo(@NotNull BlokusMeshNode cell) {
        IntegerVector cellCoordinate = this.getCoordinateOf(cell);
        assert cellCoordinate != null;
        return DIAGONAL_UNIT_VECS
                .stream()
                .map(cellCoordinate::add)
                .map(this::getNodeAt)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }
}
