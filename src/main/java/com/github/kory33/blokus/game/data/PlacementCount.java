package com.github.kory33.blokus.game.data;

import com.github.kory33.blokus.game.BlokusPlacement;
import com.github.kory33.blokus.game.cell.PlayerColor;

import java.util.HashMap;
import java.util.Map;

/**
 * Class representing total size of placements made by red/blue players.
 */
public class PlacementCount {
    private final Map<PlayerColor, Integer> placementCounts;

    public PlacementCount() {
        this.placementCounts = new HashMap<>();
        this.placementCounts.put(PlayerColor.RED, 0);
        this.placementCounts.put(PlayerColor.BLUE, 0);
    }

    public void addPlacementCount(BlokusPlacement placement) {
        PlayerColor placementColor = placement.getPlacementColor();
        int currentPlacementCount = this.placementCounts.get(placementColor);
        this.placementCounts.put(placementColor, currentPlacementCount + placement.size());
    }
}
