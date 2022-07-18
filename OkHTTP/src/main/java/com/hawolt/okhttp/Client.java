package com.hawolt.okhttp;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.IOException;
import java.net.Proxy;
import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created: 18/07/2022 08:58
 * Author: Twitter @hawolt
 **/

public class Client {

    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36";
    private static final OkHttpClient NO_PROXY = new OkHttpClient();

    private static OkHttpClient getClient(ProxyWrapper wrapper) {
        if (wrapper == null) return NO_PROXY;
        return getClient(wrapper.getProxy(), wrapper.getAuthentication());
    }

    private static OkHttpClient getClient(Proxy proxy, Authentication authentication) {
        if (proxy == null) return NO_PROXY;
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder()
                .connectTimeout(15L, TimeUnit.SECONDS)
                .writeTimeout(15L, TimeUnit.SECONDS)
                .readTimeout(15L, TimeUnit.SECONDS)
                .callTimeout(15L, TimeUnit.SECONDS)
                .proxy(proxy);
        return authentication == null ? builder.build() : builder.proxyAuthenticator(authentication).build();
    }

    public static Call perform(Request request) throws IOException {
        return perform(request, null);
    }

    public static Call perform(Request request, ProxyWrapper wrapper) {
        return getClient(wrapper).newCall(request);
    }
}
