package com.github.kory33.blokus.game.color;

import org.jetbrains.annotations.NotNull;

/**
 * Class representing a color of a player.
 */
public enum PlayerColor {
    RED("red"), BLUE("blue");

    @NotNull
    private final String colorName;

    PlayerColor(@NotNull String colorName) {
        this.colorName = colorName;
    }

    @NotNull
    public PlayerColor getOpponentColor() {
        if (this == RED) {
            return BLUE;
        }
        return RED;
    }

    @NotNull
    public String getColorName() {
        return this.colorName;
    }
}
