package com.github.kory33.blokus.util;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

/**
 * Utility class used for manipulating sets.
 */
public class SetUtil {
    public static <T, R> Set<R> map(Set<T> set, Function<? super T, ? extends R> transformer) {
        Set<R> resultSet = new HashSet<>();
        set.forEach(element -> resultSet.add(transformer.apply(element)));
        return resultSet;
    }
}
