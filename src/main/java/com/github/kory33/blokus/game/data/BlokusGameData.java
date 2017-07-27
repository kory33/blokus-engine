package com.github.kory33.blokus.game.data;

import com.github.kory33.blokus.game.BlokusConstant;
import com.github.kory33.blokus.game.BlokusPlacement;
import com.github.kory33.blokus.game.cell.PlayerColor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * A data class which holds statistics and records about a Blokus game session.
 */
public class BlokusGameData {
    @NotNull private final BlokusBoard board;
    @NotNull private final PlacementHoldings redPlacementHoldings;
    @NotNull private final PlacementHoldings bluePlacementHoldings;
    @NotNull private final PlacementHistory placementHistory;
    @NotNull private final PlacementCounts placementCounts;

    public BlokusGameData() {
        board = new BlokusBoard(BlokusConstant.BOARD_SIZE);
        redPlacementHoldings = new PlacementHoldings();
        bluePlacementHoldings = new PlacementHoldings();
        placementHistory = new PlacementHistory();
        placementCounts = new PlacementCounts();
    }

    private BlokusGameData(BlokusGameData data){
        board = data.getBoard();
        redPlacementHoldings = data.getPlacementHoldingsOf(PlayerColor.RED);
        bluePlacementHoldings = data.getPlacementHoldingsOf(PlayerColor.BLUE);
        placementHistory = data.getPlacementHistory();
        placementCounts = data.getPlacementCounts();
    }

    public void updateGameData(BlokusPlacement placement) {
        this.board.updateBoard(placement);
        this.getPlacementHoldingsOf(placement.getPlacementColor()).makePlacement(placement);
        this.placementHistory.add(placement);
        this.placementCounts.addPlacementCount(placement);
    }

    @Contract(pure = true)
    public PlacementHoldings getPlacementHoldingsOf(@NotNull PlayerColor playerColor) {
        if (playerColor == PlayerColor.RED) {
            return this.redPlacementHoldings.getCopy();
        }
        return this.bluePlacementHoldings.getCopy();
    }

    @NotNull
    @Contract(pure = true)
    public BlokusBoard getBoard() {
        return board.getCopy();
    }

    @NotNull
    @Contract(pure = true)
    public PlacementHistory getPlacementHistory() {
        return placementHistory.getCopy();
    }

    @NotNull
    @Contract(pure = true)
    public PlacementCounts getPlacementCounts() {
        return this.placementCounts.getCopy();
    }

    @NotNull
    @Contract(pure = true)
    public BlokusGameData getCopy() {
        return new BlokusGameData(this);
    }
}
