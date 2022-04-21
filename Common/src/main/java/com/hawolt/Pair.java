package com.hawolt;

/**
 * Created: 18/04/2022 23:18
 * Author: Twitter @hawolt
 **/

public class Pair<K, V> {
    private final K k;
    private final V v;

    private Pair(K k, V v) {
        this.k = k;
        this.v = v;
    }

    public K getK() {
        return k;
    }

    public V getV() {
        return v;
    }

    public static <K, V> Pair<K, V> of(K k, V v) {
        return new Pair<>(k, v);
    }
}
