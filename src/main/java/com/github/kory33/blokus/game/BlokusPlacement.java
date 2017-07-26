package com.github.kory33.blokus.game;

import com.github.kory33.blokus.game.cell.PlayerColor;
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

    /*package-private*/ BlokusPlacement(@NotNull Set<IntegerVector> placementSet, @NotNull PlayerColor placementColor) {
        this.placementColor = placementColor;
        this.placementCellCoordinates = placementSet;
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

    public <R> Set<R> map(Function<IntegerVector, R> transformer) {
        return SetUtil.transform(this.placementCellCoordinates, transformer);
    }

    public int size() {
        return placementCellCoordinates.size();
    }

    public boolean contains(BlokusMeshNode o) {
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
        return Objects.hash(placementCellCoordinates);
    }
}
