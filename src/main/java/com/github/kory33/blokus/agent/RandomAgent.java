package com.github.kory33.blokus.agent;

import com.github.kory33.blokus.game.ColoredBlokusPlacement;
import com.github.kory33.blokus.game.IBlokusPlayer;
import com.github.kory33.blokus.game.color.PlayerColor;
import com.github.kory33.blokus.game.data.BlokusGameData;
import com.github.kory33.blokus.util.SetUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Represents an agent which chooses its action randomly.
 */
public class RandomAgent implements IBlokusPlayer {
    @NotNull
    @Override
    public ColoredBlokusPlacement chooseBestPlacementFrom(@NotNull Set<ColoredBlokusPlacement> placementSet, @NotNull BlokusGameData gameData) {
        return SetUtil.chooseRandomlyFrom(placementSet);
    }

    @Override
    public void assignColor(@NotNull PlayerColor color) {
        // do nothing.
    }
}
