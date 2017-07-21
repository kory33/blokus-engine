package com.github.kory33.blokus.game;

/**
 * Created by Kory on 7/21/2017.
 */
public class BlokusConstant {
    /**
     * Size(width and height) of a board.
     */
    public static final Integer boardSize = 12;

    /**
     * Positions from which red player begins it's first placement.
     * <p>
     * This coordinates is defined under the assumptions that the coordinate of top-left cell is <code>(1, 1)</code>
     * and the coordinate of bottom-right cell is <code>(12, 12)</code>
     * </p>
     */
    public static final BlokusCoordinate RED_BEGIN_COORDINATE = new BlokusCoordinate(3, 3);

    /**
     * Positions from which blue player begins it's first placement.
     * <p>
     * This coordinates is defined under the assumptions that the coordinate of top-left cell is <code>(1, 1)</code>
     * and the coordinate of bottom-right cell is <code>(12, 12)</code>
     * </p>
     */
    public static final BlokusCoordinate BLUE_BEGIN_COORDINATE = new BlokusCoordinate(10, 10);
}
