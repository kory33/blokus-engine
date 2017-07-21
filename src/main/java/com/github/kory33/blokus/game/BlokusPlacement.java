package com.github.kory33.blokus.game;

import com.github.kory33.blokus.game.cell.BlokusMeshCell;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * This class represents a placement made by one of the players.
 */
public class BlokusPlacement {
    private final Set<BlokusMeshCell> placementCells;

    BlokusPlacement(@NotNull Set<BlokusMeshCell> placementSet) {
        this.placementCells = placementSet;
    }

    public Set<BlokusMeshCell> getCells() {
        return new HashSet<>(placementCells);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlokusPlacement that = (BlokusPlacement) o;
        return Objects.equals(placementCells, that.placementCells);
    }

    @Override
    public int hashCode() {
        return Objects.hash(placementCells);
    }
}
