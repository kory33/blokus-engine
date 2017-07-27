package com.github.kory33.blokus.game;

import com.github.kory33.blokus.game.color.PlayerColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An enum class representing a status of Blokus game.
 */
public enum BlokusGamePhases {
    RESUME_RED_PLAYER(PlayerColor.RED),
    RESUME_BLUE_PLAYER(PlayerColor.BLUE),
    GAME_FINISH(null);

    private final PlayerColor nextPlayerColor;

    BlokusGamePhases(@Nullable PlayerColor nextPlayerColor) {
        this.nextPlayerColor = nextPlayerColor;
    }

    public boolean isGameFinished() {
        return nextPlayerColor == null;
    }

    @Nullable
    public PlayerColor getNextPlayerColor() {
        return nextPlayerColor;
    }

    @NotNull
    public static BlokusGamePhases getResumeStatus(PlayerColor nextPlayerColor) {
        if (nextPlayerColor == PlayerColor.RED) {
            return RESUME_RED_PLAYER;
        }
        return RESUME_BLUE_PLAYER;
    }
}
