package com.github.kory33.blokus.game;

import com.github.kory33.blokus.game.color.PlayerColor;
import com.github.kory33.blokus.util.IntegerVector;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
     * This coordinates is defined under the assumptions that the coordinate of top-left color is <code>(1, 1)</code>
     * and the coordinate of bottom-right color is <code>(12, 12)</code>
     * </p>
     */
    public static final IntegerVector RED_BEGIN_COORDINATE = new IntegerVector(3, 3);

    /**
     * Positions from which blue player begins it's first placement.
     * <p>
     * This coordinates is defined under the assumptions that the coordinate of top-left color is <code>(1, 1)</code>
     * and the coordinate of bottom-right color is <code>(12, 12)</code>
     * </p>
     */
    public static final IntegerVector BLUE_BEGIN_COORDINATE = new IntegerVector(10, 10);

    /**
     * Color of the player who plays a placement at the beginning of a game.
     */
    public static final PlayerColor FIRST_PLAYER = PlayerColor.RED;

    /**
     * Map containing a maximum possible numbers of corresponding-sized placement in one game.
     * For example, number mapped from <code>3</code>
     * indicates number of times 3-cells placement can be made in a game.
     */
    public static final Map<Integer, Integer> PLACEMENT_COUNTS;
    static {
        Map<Integer, Integer> placementSizeToCountMap = new HashMap<>();
        placementSizeToCountMap.put(3, 10);
        placementSizeToCountMap.put(4, 5);
        placementSizeToCountMap.put(5, 2);
        PLACEMENT_COUNTS = Collections.unmodifiableMap(placementSizeToCountMap);
    }
}
