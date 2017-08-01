package com.github.kory33.blokus.game.data;

import com.github.kory33.blokus.game.ColoredBlokusPlacement;
import com.github.kory33.blokus.game.color.CellColor;
import com.github.kory33.blokus.game.color.PlayerColor;
import com.github.kory33.blokus.util.IntegerVector;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * A class representing a board in a Blokus game.
 * <p>
 * This class mainly acts as a data class holding the color colors at the given integer coordinate in a game board.
 * </p>
 */
public class BlokusBoard {
    private final Map<IntegerVector, CellColor> boardMatrix;

    /*package-private */BlokusBoard(@NotNull Integer matrixSize) {
        this.boardMatrix = new HashMap<>();
        for (int column = 1; column <= matrixSize; column++) {
            for (int row = 1; row <= matrixSize; row++) {
                IntegerVector cellCoordinate = new IntegerVector(column, row);
                this.boardMatrix.put(cellCoordinate, CellColor.NONE);
            }
        }
    }

    private BlokusBoard(BlokusBoard board) {
        this.boardMatrix = new HashMap<>(board.boardMatrix);
    }

    /*package-private*/ void updateBoard(ColoredBlokusPlacement placement) {
        PlayerColor placementColor = placement.getPlacementColor();
        placement.forEach(placementCoordinate -> {
            this.boardMatrix.put(placementCoordinate, CellColor.fromPlayerColor(placementColor));
        });
    }

    @NotNull
    public CellColor getCellColorAt(IntegerVector vector) {
        CellColor cellColor = this.boardMatrix.get(vector);

        if (cellColor == null) {
            throw new IllegalArgumentException("Given vector indicates a coordinate outside the board matrix.");
        }

        return cellColor;
    }

    public BlokusBoard getCopy() {
        return new BlokusBoard(this);
    }
}
