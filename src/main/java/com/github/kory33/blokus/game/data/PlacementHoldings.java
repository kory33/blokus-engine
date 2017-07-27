package com.github.kory33.blokus.game.data;

import com.github.kory33.blokus.game.BlokusConstant;
import com.github.kory33.blokus.game.BlokusPlacement;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A data class holding the numbers of placements that a player can make with a given size.
 */
public class PlacementHoldings {
    @NotNull
    private final Map<Integer, Integer> holdings;

    /*package-private*/ PlacementHoldings() {
        this.holdings = new HashMap<>(BlokusConstant.PLACEMENT_COUNTS);
    }

    @Contract(pure = true)
    public boolean isAvailable(Integer placementSize) {
        return this.holdings.containsKey(placementSize);
    }

    @Contract(pure = true)
    @NotNull
    public Integer getMaximumPlacementSize() {
        Set<Integer> availablePlacementSizes = this.holdings.keySet();
        if (availablePlacementSizes.isEmpty()) {
            return 0;
        }

        return Collections.max(this.holdings.keySet());
    }

    @NotNull
    @Contract(pure = true)
    public Map<Integer, Integer> getHoldingsMap() {
        return new HashMap<>(this.holdings);
    }

    /**
     * Update the placement holdings record assuming that the given placement has been made.
     * @param placement Placement which has been made.
     */
    /*package-private*/ void makePlacement(BlokusPlacement placement) {
        int placementSize = placement.size();
        int placementHoldings = holdings.get(placementSize) - 1;
        if (placementHoldings == 0) {
            this.holdings.remove(placementSize);
            return;
        }
        this.holdings.put(placementSize, placementHoldings);
    }
}
