package com.github.kory33.blokus.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface IImmutableBijectiveHashMap<K, V> {
    @NotNull
    BijectiveHashMap<V, K> getInverse();

    @NotNull
    Map<K, V> getAsMap();

    @Nullable
    V getValue(K key);

    @Nullable
    K getKey(V value);
}
