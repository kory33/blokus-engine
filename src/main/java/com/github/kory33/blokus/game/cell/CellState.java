package com.github.kory33.blokus.game.cell;

/**
 * Enum representing state of an individual cell.
 */
public enum CellState{
    RED3(CellColor.RED, 3),
    RED4(CellColor.RED, 4),
    RED5(CellColor.RED, 5),

    BLUE3(CellColor.BLUE, 3),
    BLUE4(CellColor.BLUE, 4),
    BLUE5(CellColor.BLUE, 5),

    NONE(CellColor.NONE, 0);

    public final CellColor color;
    public final int blockSize;

    CellState(CellColor color, int blockSize) {
        this.color = color;
        this.blockSize = blockSize;
    }
}
