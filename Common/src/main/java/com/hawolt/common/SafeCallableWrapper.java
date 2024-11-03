package com.hawolt.common;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created: 18/04/2022 23:08
 * Author: Twitter @hawolt
 *
 * @author Hawolt
 * @version 1.1
 */
public class SafeCallableWrapper<T> {

    private final Callable<T> callable;

    /**
     * <p>Constructor for SafeCallableWrapper.</p>
     *
     * @param callable a {@link java.util.concurrent.Callable} object
     */
    public SafeCallableWrapper(Callable<T> callable) {
        this.callable = callable;
    }

    /**
     * <p>get.</p>
     *
     * @return a {@link java.util.Optional} object
     */
    public Optional<T> get() {
        try {
            return Optional.ofNullable(callable.call());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
