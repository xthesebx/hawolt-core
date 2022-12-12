package com.hawolt.wss;

import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

/**
 * Created: 12/12/2022 10:25
 * Author: Twitter @hawolt
 **/

public class SelfSignedTrustManager implements X509TrustManager {
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }

    public void checkClientTrusted(X509Certificate[] certs, String authType) {
    }

    public void checkServerTrusted(X509Certificate[] certs, String authType) {
    }
}
