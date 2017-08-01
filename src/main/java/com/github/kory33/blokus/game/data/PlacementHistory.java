package com.github.kory33.blokus.game.data;

import com.github.kory33.blokus.game.ColoredBlokusPlacement;

import java.util.ArrayList;

/**
 * A class representing history of placements
 */
public class PlacementHistory extends ArrayList<ColoredBlokusPlacement> {
    /*package-private*/ PlacementHistory() {
        super();
    }

    private PlacementHistory(PlacementHistory history) {
        super(history);
    }

    public PlacementHistory getCopy() {
        return new PlacementHistory(this);
    }
}
