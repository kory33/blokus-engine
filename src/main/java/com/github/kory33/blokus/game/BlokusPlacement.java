package com.github.kory33.blokus.game;

import com.github.kory33.blokus.util.IntegerVector;
import com.github.kory33.blokus.util.SetUtil;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Represents abstracted placement on a blokus game.
 *
 * This placement representation does not include its color.
 * To ensure the color presence, use {@link ColoredBlokusPlacement}
 */
public class BlokusPlacement {
    @NotNull private final Set<IntegerVector> placementCellCoordinates;

    /**
     * HashCode is cached under the assumption that a ColoredBlokusPlacement object is immutable.
     */
    private int hashCodeCache;

    public BlokusPlacement(@NotNull Set<IntegerVector> placementSet) {
        this.placementCellCoordinates = placementSet;
    }

    public Set<IntegerVector> getCellCoordinates() {
        return new HashSet<>(placementCellCoordinates);
    }

    public void forEach(Consumer<? super IntegerVector> action) {
        placementCellCoordinates.forEach(action);
    }

    public <R> Set<R> map(Function<? super IntegerVector, ? extends R> transformer) {
        return SetUtil.map(this.placementCellCoordinates, transformer);
    }

    public int size() {
        return placementCellCoordinates.size();
    }

    public boolean contains(IntegerVector o) {
        return placementCellCoordinates.contains(o);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlokusPlacement that = (BlokusPlacement) o;
        return Objects.equals(placementCellCoordinates, that.placementCellCoordinates);
    }

    @Override
    public int hashCode() {
        return this.hashCodeCache;
    }

}
