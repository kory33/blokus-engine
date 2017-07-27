package com.github.kory33.blokus.game.data;

import com.github.kory33.blokus.game.BlokusConstant;
import com.github.kory33.blokus.game.BlokusPlacement;
import com.github.kory33.blokus.game.cell.PlayerColor;

import java.util.HashMap;
import java.util.Map;

/**
 * Class representing total size of placements made by red/blue players.
 */
public class PlacementCounts {
    private final Map<PlayerColor, Integer> placementCounts;

    public PlacementCounts() {
        this.placementCounts = new HashMap<>();
        this.placementCounts.put(PlayerColor.RED, 0);
        this.placementCounts.put(PlayerColor.BLUE, 0);
    }

    private PlacementCounts(PlacementCounts placementCounts) {
        this.placementCounts = new HashMap<>(placementCounts.placementCounts);
    }

    public void addPlacementCount(BlokusPlacement placement) {
        PlayerColor placementColor = placement.getPlacementColor();
        int currentPlacementCount = this.placementCounts.get(placementColor);
        this.placementCounts.put(placementColor, currentPlacementCount + placement.size());
    }

    public PlacementCounts getCopy() {
        return new PlacementCounts(this);
    }

    public PlayerColor getWinningColor() {
        Integer redPlacementCounts = this.placementCounts.get(PlayerColor.RED);
        Integer bluePlacementCounts = this.placementCounts.get(PlayerColor.BLUE);

        if (redPlacementCounts > bluePlacementCounts) {
            return PlayerColor.RED;
        } else if (bluePlacementCounts > redPlacementCounts) {
            return PlayerColor.BLUE;
        }

        return BlokusConstant.FIRST_PLAYER.getOpponentColor();
    }
}
