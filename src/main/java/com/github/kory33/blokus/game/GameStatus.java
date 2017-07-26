package com.github.kory33.blokus.game;

import com.github.kory33.blokus.game.cell.PlayerColor;
import org.jetbrains.annotations.Nullable;

/**
 * An enum class representing a status of Blokus game.
 */
public enum GameStatus {
    RESUME_RED_PLAYER(PlayerColor.RED),
    RESUME_BLUE_PLAYER(PlayerColor.BLUE),
    GAME_FINISH(null);

    private final PlayerColor nextPlayerColor;

    GameStatus(@Nullable PlayerColor nextPlayerColor) {
        this.nextPlayerColor = nextPlayerColor;
    }

    public boolean isGameFinished() {
        return nextPlayerColor == null;
    }

    @Nullable
    public PlayerColor getNextPlayerColor() {
        return nextPlayerColor;
    }
}
