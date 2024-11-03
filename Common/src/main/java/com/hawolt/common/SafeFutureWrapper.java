package com.hawolt.common;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created: 18/04/2022 23:08
 * Author: Twitter @hawolt
 *
 * @author Hawolt
 * @version 1.1
 */
public class SafeFutureWrapper<T> {

    private final Future<T> future;

    /**
     * <p>Constructor for SafeFutureWrapper.</p>
     *
     * @param future a {@link java.util.concurrent.Future} object
     */
    public SafeFutureWrapper(Future<T> future) {
        this.future = future;
    }

    /**
     * <p>get.</p>
     *
     * @return a {@link java.util.Optional} object
     */
    public Optional<T> get() {
        try {
            return Optional.ofNullable(future.get());
        } catch (InterruptedException | ExecutionException e) {
            return Optional.empty();
        }
    }
}
