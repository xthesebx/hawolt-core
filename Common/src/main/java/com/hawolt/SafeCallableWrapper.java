package com.hawolt;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created: 18/04/2022 23:08
 * Author: Twitter @hawolt
 **/

public class SafeCallableWrapper<T> {

    private final Callable<T> callable;

    public SafeCallableWrapper(Callable<T> callable) {
        this.callable = callable;
    }

    public Optional<T> get() {
        try {
            return Optional.ofNullable(callable.call());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
