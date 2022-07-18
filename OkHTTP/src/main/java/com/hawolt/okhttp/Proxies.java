package com.hawolt.okhttp;

import java.net.Proxy;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created: 18/07/2022 08:57
 * Author: Twitter @hawolt
 **/

public class Proxies {
    private final static List<ProxyWrapper> list = new LinkedList<>();

    private static final Object lock = new Object();

    public static int available() {
        return list.size();
    }

    public static ProxyWrapper getIfAvailable() {
        synchronized (lock) {
            if (list.isEmpty()) return null;
            else return list.remove(0);
        }
    }

    public static void restore(Proxy proxy, Authentication authentication) {
        synchronized (lock) {
            list.add(new ProxyWrapper(proxy, authentication));
        }
    }

    public static void submit(Proxy proxy, Authentication authentication) {
        synchronized (lock) {
            list.add(new ProxyWrapper(proxy, authentication));
        }
    }
}
