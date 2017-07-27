package com.github.kory33.blokus.game.data;

import com.github.kory33.blokus.game.BlokusConstant;
import com.github.kory33.blokus.game.BlokusPlacement;
import com.github.kory33.blokus.game.cell.PlayerColor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A data class which holds statistics and records about a Blokus game session.
 */
public class BlokusGameData {
    @NotNull private final BlokusBoard board = new BlokusBoard(BlokusConstant.BOARD_SIZE);
    @NotNull private final PlacementHoldings redPlacementHoldings = new PlacementHoldings();
    @NotNull private final PlacementHoldings bluePlacementHoldings = new PlacementHoldings();
    @NotNull private final List<BlokusPlacement> placementHistory = new ArrayList<>();

    @NotNull private final PlacementCount placementCounts = new PlacementCount();

    public BlokusGameData() {}

    public void updateGameData(BlokusPlacement placement) {
        this.board.updateBoard(placement);
        this.getPlacementHoldingsOf(placement.getPlacementColor()).makePlacement(placement);
        this.placementHistory.add(placement);
        this.placementCounts.addPlacementCount(placement);
    }

    @Contract(pure = true)
    public PlacementHoldings getPlacementHoldingsOf(@NotNull PlayerColor playerColor) {
        if (playerColor == PlayerColor.RED) {
            return this.redPlacementHoldings;
        }
        return this.bluePlacementHoldings;
    }

    @NotNull
    @Contract(pure = true)
    public BlokusBoard getBoard() {
        return board;
    }

    @NotNull
    @Contract(pure = true)
    public List<BlokusPlacement> getPlacementHistory() {
        return placementHistory;
    }

    @NotNull
    @Contract(pure = true)
    public BlokusGameData getCopy() {
        return null;
    }
}
