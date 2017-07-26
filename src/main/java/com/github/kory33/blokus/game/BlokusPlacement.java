package com.github.kory33.blokus.game;

import com.github.kory33.blokus.game.cell.BlokusMeshNode;
import com.github.kory33.blokus.game.cell.PlayerColor;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

/**
 * This class represents a placement made by one of the players.
 */
public class BlokusPlacement {
    private final PlayerColor placementColor;

    private final Set<BlokusMeshNode> placementCells;

    /*package-private*/ BlokusPlacement(@NotNull Set<BlokusMeshNode> placementSet, @NotNull PlayerColor placementColor) {
        this.placementColor = placementColor;
        this.placementCells = placementSet;
    }

    public Set<BlokusMeshNode> getCells() {
        return new HashSet<>(placementCells);
    }

    public PlayerColor getPlacementColor() {
        return placementColor;
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

    public void forEach(Consumer<? super BlokusMeshNode> action) {
        placementCells.forEach(action);
    }

    public int size() {
        return placementCells.size();
    }

    public boolean contains(BlokusMeshNode o) {
        return placementCells.contains(o);
    }

}
