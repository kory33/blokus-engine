package com.github.kory33.blokus.util;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;

/**
 * Utility class used for manipulating sets.
 * @author Kory
 */
public class SetUtil {
    private static final Random randGenerator = new Random();

    public static <T, R> Set<R> map(Set<? extends T> set, Function<? super T, ? extends R> transformer) {
        Set<R> resultSet = new HashSet<>();
        set.forEach(element -> resultSet.add(transformer.apply(element)));
        return resultSet;
    }

    /**
     * Get a element randomly from the given set.
     * <p>
     * Note that this method chooses an element using {@link Random} class, hence
     * the result is not cryptographically secure.
     * </p>
     * @param set set from which an element is to be chosen.
     * @return an element randomly chosen from the set.
     */
    @NotNull
    public static <E> E chooseRandomlyFrom(@NotNull Set<E> set) {
        int setSize = set.size();

        if (setSize == 0) {
            throw new NoSuchElementException("No element to choose from the given set.");
        }
        int targetIndex = randGenerator.nextInt(setSize);

        Iterator<E> iterator = set.iterator();
        for (int i = 0; i < targetIndex; i++) {
            iterator.next();
        }
        return iterator.next();
    }
}
