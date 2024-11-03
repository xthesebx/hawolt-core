package com.hawolt.common;

/**
 * Created: 18/04/2022 23:18
 * Author: Twitter @hawolt
 *
 * @author Hawolt
 * @version 1.1
 */
public class Pair<K, V> {
    private final K k;
    private final V v;

    private Pair(K k, V v) {
        this.k = k;
        this.v = v;
    }

    /**
     * <p>Getter for the field <code>k</code>.</p>
     *
     * @return a K object
     */
    public K getK() {
        return k;
    }

    /**
     * <p>Getter for the field <code>v</code>.</p>
     *
     * @return a V object
     */
    public V getV() {
        return v;
    }

    /**
     * <p>of.</p>
     *
     * @param k a K object
     * @param v a V object
     * @param <K> a K class
     * @param <V> a V class
     * @return a {@link com.hawolt.common.Pair} object
     */
    public static <K, V> Pair<K, V> of(K k, V v) {
        return new Pair<>(k, v);
    }
}
