package com.github.kory33.blokus.game;

import com.github.kory33.blokus.util.IntegerVector;
import com.github.kory33.blokus.util.SetUtil;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Represents abstracted placement on a blokus game.
 *
 * This placement representation does not include its color.
 * To ensure the color presence, use {@link ColoredBlokusPlacement}
 */
public class BlokusPlacement implements Comparable<BlokusPlacement> {
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
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BlokusPlacement)) return false;
        BlokusPlacement that = (BlokusPlacement) o;
        return Objects.equals(placementCellCoordinates, that.placementCellCoordinates);
    }

    @Override
    public final int hashCode() {
        return this.hashCodeCache;
    }

    @NotNull
    private Set<Integer> toVectorIdSet() {
        return SetUtil.map(
                this.placementCellCoordinates,
                vector -> vector.getX() + BlokusConstant.BOARD_SIZE * vector.getY()
        );
    }

    @Override
    public int compareTo(@NotNull BlokusPlacement o) {
        if (this.equals(o)) {
            return 0;
        }

        Set<Integer> thisVectorIdSet = this.toVectorIdSet();
        Set<Integer> thatVectorIdSet = o.toVectorIdSet();
        if (thisVectorIdSet.size() != thatVectorIdSet.size()) {
            return Integer.compare(thisVectorIdSet.size(), thatVectorIdSet.size());
        }

        // Compare the sorted vector id list.
        // Return the compare value of the first pair of elements
        // whose indices are equal but values are different.
        List<Integer> thisVectorIdList = thisVectorIdSet.stream().sorted().collect(Collectors.toList());
        List<Integer> thatVectorIdList = thatVectorIdSet.stream().sorted().collect(Collectors.toList());
        for (int index = 0; index < thisVectorIdList.size(); index++) {
            int thisVectorIdAtIndex = thisVectorIdList.get(index);
            int thatVectorIdAtIndex = thatVectorIdList.get(index);
            if (thisVectorIdAtIndex != thatVectorIdAtIndex) {
                return Integer.compare(thisVectorIdAtIndex, thatVectorIdAtIndex);
            }
        }

        throw new IllegalStateException("Two objects are equal and yet equals() returned false!");
    }
}
