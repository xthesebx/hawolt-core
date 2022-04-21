package com.hawolt;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created: 18/04/2022 23:08
 * Author: Twitter @hawolt
 **/

public class SafeWrapper<T> {

    private final Future<T> future;

    public SafeWrapper(Future<T> future) {
        this.future = future;
    }

    public Optional<T> get() {
        try {
            return Optional.ofNullable(future.get());
        } catch (InterruptedException | ExecutionException e) {
            return Optional.empty();
        }
    }
}
