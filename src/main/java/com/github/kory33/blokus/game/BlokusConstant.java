package com.github.kory33.blokus.game;

import com.github.kory33.blokus.util.IntegerVector;

/**
 * Constants used in game of Blokus
 */
public class BlokusConstant {
    /**
     * Size(width and height) of a board.
     */
    public static final Integer BOARD_SIZE = 12;

    /**
     * Positions from which red player begins it's first placement.
     * <p>
     * This coordinates is defined under the assumptions that the coordinate of top-left cell is <code>(1, 1)</code>
     * and the coordinate of bottom-right cell is <code>(12, 12)</code>
     * </p>
     */
    public static final IntegerVector RED_BEGIN_COORDINATE = new IntegerVector(3, 3);

    /**
     * Positions from which blue player begins it's first placement.
     * <p>
     * This coordinates is defined under the assumptions that the coordinate of top-left cell is <code>(1, 1)</code>
     * and the coordinate of bottom-right cell is <code>(12, 12)</code>
     * </p>
     */
    public static final IntegerVector BLUE_BEGIN_COORDINATE = new IntegerVector(10, 10);

    /**
     * Maximum size of a placement.
     */
    public static final Integer MIN_PLACEMENT_SIZE = 3;

    /**
     * Minimum size of a placement.
     */
    public static final Integer MAX_PLACEMENT_SIZE = 5;

    /**
     * Array containing a maximum possible numbers of corresponding-sized placement in one game.
     * For example, number in an index of <code>3</code>
     * indicates number of times 3-cells placement can be made in a game.
     */
    public static final Integer[] PLACEMENT_COUNTS = {0, 0, 0, 10, 5, 2};
}
