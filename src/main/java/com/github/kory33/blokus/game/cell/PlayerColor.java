package com.github.kory33.blokus.game.cell;

/**
 * Class representing a color of a player.
 */
public enum PlayerColor {
    RED, BLUE;

    public PlayerColor getOpponentColor() {
        if (this == RED) {
            return BLUE;
        }
        return RED;
    }
}
