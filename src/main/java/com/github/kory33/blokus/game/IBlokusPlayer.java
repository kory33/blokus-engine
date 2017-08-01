package com.github.kory33.blokus.game;

import com.github.kory33.blokus.game.color.PlayerColor;
import com.github.kory33.blokus.game.data.BlokusGameData;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * An interface representing a player in Blokus game.
 */
public interface IBlokusPlayer {
    @NotNull
    ColoredBlokusPlacement chooseBestPlacementFrom(@NotNull Set<ColoredBlokusPlacement> placementSet, @NotNull BlokusGameData gameData);

    void assignColor(@NotNull PlayerColor color);
}
