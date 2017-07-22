package com.github.kory33.blokus.game.cell;

import org.jetbrains.annotations.Nullable;

/**
 * Enum representing colors that are assignable to a cell.
 */
public enum CellColor {
    NONE(null), RED(PlayerColor.RED), BLUE(PlayerColor.BLUE);

    @Nullable
    private final PlayerColor placedBy;

    CellColor(@Nullable PlayerColor placedBy) {
        this.placedBy = placedBy;
    }

    @Nullable
    public PlayerColor getPlayerColor() {
        return placedBy;
    }
}
