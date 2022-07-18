package com.hawolt.okhttp;

import java.net.Proxy;

/**
 * Created: 18/07/2022 08:57
 * Author: Twitter @hawolt
 **/

public class ProxyWrapper {
    private final Authentication authentication;
    private final Proxy proxy;
    private long time;

    public ProxyWrapper(Proxy proxy, Authentication authentication) {
        this.authentication = authentication;
        this.proxy = proxy;
    }

    public Proxy getProxy() {
        return proxy;
    }

    public Authentication getAuthentication() {
        return authentication;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Proxy)) return false;
        return proxy.equals(o);
    }
}
