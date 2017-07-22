package com.github.kory33.blokus.game;

import com.github.kory33.blokus.game.cell.PlayerColor;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * An interface representing a player in Blokus game.
 */
public interface IBlokusPlayer {
    @NotNull
    PlayerColor getPlayerColor();

    @NotNull
    BlokusPlacement chooseBestPlacementFrom(Set<BlokusPlacement> placementSet);
}
