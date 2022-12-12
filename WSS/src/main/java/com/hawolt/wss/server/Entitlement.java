package com.hawolt.wss.server;

import java.util.concurrent.CompletableFuture;

/**
 * Created: 07/12/2022 12:56
 * Author: Twitter @hawolt
 **/

public class Entitlement {
    private final long createdAt = System.currentTimeMillis();
    private final CompletableFuture<Boolean> future;

    public Entitlement(CompletableFuture<Boolean> future) {
        this.future = future;
    }

    public CompletableFuture<Boolean> getFuture() {
        return future;
    }

    private boolean isVerified;

    public long getCreatedAt() {
        return createdAt;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }
}
