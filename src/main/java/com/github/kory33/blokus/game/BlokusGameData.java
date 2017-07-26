package com.github.kory33.blokus.game;

import com.github.kory33.blokus.game.cell.PlayerColor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A data class which holds statistics and records about a Blokus game session.
 */
public class BlokusGameData {
    @NotNull private final BlokusBoard board;
    @NotNull private final PlacementHoldings redPlacementHoldings;
    @NotNull private final PlacementHoldings bluePlacementHoldings;
    @NotNull private final List<BlokusPlacement> placementHistory;

    /*package-private*/ BlokusGameData() {
        this.board = new BlokusBoard(BlokusConstant.BOARD_SIZE);
        this.redPlacementHoldings = new PlacementHoldings();
        this.bluePlacementHoldings = new PlacementHoldings();
        this.placementHistory = new ArrayList<>();
    }

    /*package-private*/ void updateGameData(BlokusPlacement placement) {
        this.board.updateBoard(placement);
        this.getPlacementHoldingsOf(placement.getPlacementColor()).makePlacement(placement);
        this.placementHistory.add(placement);
    }

    public PlacementHoldings getPlacementHoldingsOf(@NotNull PlayerColor playerColor) {
        if (playerColor == PlayerColor.RED) {
            return this.redPlacementHoldings;
        }
        return this.bluePlacementHoldings;
    }

    @NotNull
    public BlokusBoard getBoard() {
        return board;
    }

    @NotNull
    public List<BlokusPlacement> getPlacementHistory() {
        return placementHistory;
    }
}
