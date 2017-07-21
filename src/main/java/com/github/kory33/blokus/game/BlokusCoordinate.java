package com.github.kory33.blokus.game;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * A
 */
public class BlokusCoordinate {
    @NotNull private final Integer x;
    @NotNull private final Integer y;

    public BlokusCoordinate(@NotNull Integer x, @NotNull Integer y) {
        this.x = x;
        this.y = y;
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
        BlokusCoordinate that = (BlokusCoordinate) o;
        return Objects.equals(x, that.x) &&
                Objects.equals(y, that.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}