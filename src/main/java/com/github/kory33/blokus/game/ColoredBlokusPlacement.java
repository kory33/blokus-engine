package com.github.kory33.blokus.game;

import com.github.kory33.blokus.game.color.PlayerColor;
import com.github.kory33.blokus.util.IntegerVector;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * This class represents a placement made by one of the players.
 */
public class ColoredBlokusPlacement extends BlokusPlacement {
    @NotNull private final PlayerColor placementColor;

    /*package-private*/ ColoredBlokusPlacement(@NotNull Set<IntegerVector> placementSet, @NotNull PlayerColor placementColor) {
        super(placementSet);
        this.placementColor = placementColor;
    }

    @NotNull
    public PlayerColor getPlacementColor() {
        return placementColor;
    }

}
