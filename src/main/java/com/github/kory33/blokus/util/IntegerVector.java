package com.github.kory33.blokus.util;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * A class representing a two-dimensional integer vector.
 */
public class IntegerVector {
    @NotNull private final Integer x;
    @NotNull private final Integer y;

    /**
     * HashCode is cached under the assumption that an IntegerVector object is immutable.
     */
    private int hashCodeCache;

    public IntegerVector(@NotNull Integer x, @NotNull Integer y) {
        this.x = x;
        this.y = y;
        this.hashCodeCache = Objects.hash(x, y);
    }

    public IntegerVector add(IntegerVector vector) {
        return new IntegerVector(this.getX() + vector.getX(), this.getY() + vector.getY());
    }

    public IntegerVector negate() {
        return new IntegerVector(-this.getX(), this.getY());
    }

    public IntegerVector minus(IntegerVector vector) {
        return this.add(vector.negate());
    }

    @NotNull
    public Integer getX() {
        return x;
    }

    @NotNull
    public Integer getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntegerVector that = (IntegerVector) o;
        return Objects.equals(x, that.x) && Objects.equals(y, that.y);
    }

    @Override
    public int hashCode() {
        return this.hashCodeCache;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}