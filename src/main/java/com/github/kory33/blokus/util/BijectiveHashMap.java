package com.github.kory33.blokus.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class BijectiveHashMap<K, V> implements IImmutableBijectiveHashMap<K, V> {
    private final HashMap<K, V> map;
    private final HashMap<V, K> inverse;

    public BijectiveHashMap() {
        this.map = new HashMap<>();
        this.inverse = new HashMap<>();
    }

    private BijectiveHashMap(HashMap<K, V> map, HashMap<V, K> inverse) {
        this.map = new HashMap<>(map);
        this.inverse = new HashMap<>(inverse);
    }

    /**
     * Make a copy of a {@link BijectiveHashMap} object
     * @param bMap map to be cloned
     */
    public BijectiveHashMap(BijectiveHashMap<K, V> bMap) {
        this(bMap.map, bMap.inverse);
    }

    /**
     * Make a new mapping from key to value.
     * If this map already contains key, the old mapping will be removed.
     * However, if there already exists a mapping to the given value, {@link IllegalArgumentException} is thrown.
     * @param key from which the value is mapped
     * @param value to which the key is mapped
     * @throws IllegalArgumentException when a mapping to the given value already exists
     */
    public void put(K key, V value) throws IllegalArgumentException {
        if(this.inverse.containsKey(value)) {
            throw new IllegalArgumentException("There already exists a mapping to the given value.");
        }

        if(this.map.containsKey(key)) {
            this.inverse.remove(this.map.get(key));
        }

        this.inverse.put(value, key);
        this.map.put(key, value);
    }

    /**
     * Remove the mapping pair which has the given key.
     * @param key key to be removed
     * @return value which was mapped from removed key. Null if the key was not in the key set.
     */
    @Nullable
    public V removeKey(K key) {
        if (!this.map.containsKey(key)) {
            return null;
        }

        V removed = this.map.remove(key);
        this.inverse.remove(removed);
        return removed;
    }

    /**
     * Remove the mapping pair which has the given value.
     * @param value value to be removed from the map
     * @return key which was mapped to removed value. Null if the value was not in the value set.
     */
    @Nullable
    public K removeValue(V value) {
        K removed = this.inverse.remove(value);

        if (removed != null) {
            this.map.remove(removed);
        }

        return removed;
    }

    /**
     * Get an inverse of the map.
     * @return inverse map
     */
    @Override
    @NotNull
    public BijectiveHashMap<V, K> getInverse() {
        return new BijectiveHashMap<>(this.inverse, this.map);
    }

    /**
     * Get raw map
     * @return map
     */
    @Override
    @NotNull
    public Map<K, V> getAsMap() {
        return new HashMap<>(this.map);
    }

    /**
     * Get the value mapped from the given key
     * @param key Key of the mapping
     * @return value mapped from the key
     */
    @Override
    @Nullable
    public V getValue(K key) {
        return this.map.get(key);
    }

    /**
     * Get the key mapped to the given value
     * @param value Value of the mapping
     * @return key mapped to the value
     */
    @Override
    @Nullable
    public K getKey(V value) {
        return this.inverse.get(value);
    }
}
