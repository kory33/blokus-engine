package com.github.kory33.blokus.game;

import com.github.kory33.blokus.game.color.PlayerColor;
import com.github.kory33.blokus.util.IntegerVector;
import com.github.kory33.blokus.util.SetUtil;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * This class represents a placement made by one of the players.
 */
public class BlokusPlacement {
    @NotNull private final PlayerColor placementColor;
    @NotNull private final Set<IntegerVector> placementCellCoordinates;

    /**
     * HashCode is cached under the assumption that a BlokusPlacement object is immutable.
     */
    private int hashCodeCache;

    /*package-private*/ BlokusPlacement(@NotNull Set<IntegerVector> placementSet, @NotNull PlayerColor placementColor) {
        this.placementColor = placementColor;
        this.placementCellCoordinates = placementSet;
        this.hashCodeCache = Objects.hash(placementCellCoordinates);
    }

    public Set<IntegerVector> getCellCoordinates() {
        return new HashSet<>(placementCellCoordinates);
    }

    @NotNull
    public PlayerColor getPlacementColor() {
        return placementColor;
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
        if (this.hashCode() != that.hashCode()) return false;
        return Objects.equals(placementCellCoordinates, that.placementCellCoordinates);
    }

    @Override
    public int hashCode() {
        return this.hashCodeCache;
    }

    @Override
    public String toString() {
        return "BlokusPlacement{" +
                "placementColor=" + placementColor +
                ", placementCellCoordinates=" + placementCellCoordinates +
                '}';
    }
}
