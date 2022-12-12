package com.hawolt.wss.client;

import com.hawolt.io.Core;
import com.hawolt.wss.CertificateUtilities;
import com.hawolt.wss.SelfSignedTrustManager;
import org.java_websocket.client.WebSocketClient;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;

/**
 * Created: 12/12/2022 10:16
 * Author: Twitter @hawolt
 **/

public abstract class ClientWSS extends WebSocketClient {

    // We require the generated "privkey.pem", "cert.pem", "chain.pem", "fullchain.pem" file from LetsEncrypt

    public ClientWSS(URI serverUri, InputStream keyStream, InputStream... certificateStreams) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException, UnrecoverableKeyException, CertificateException, KeyStoreException, KeyManagementException {
        super(serverUri);
        byte[] keyBytes = Core.read(keyStream).toByteArray();
        RSAPrivateKey privateKey = CertificateUtilities.parsePrivateKey(keyBytes);
        byte[][] certificateBytes = new byte[certificateStreams.length][];
        for (int i = 0; i < certificateStreams.length; i++) {
            certificateBytes[i] = Core.read(certificateStreams[i]).toByteArray();
        }
        CertificateUtilities.close(keyStream, certificateStreams);
        KeyManager[] keyManagers = CertificateUtilities.buildKeyManagers(privateKey, certificateBytes);
        TrustManager[] trustManagers = new TrustManager[]{new SelfSignedTrustManager()};
        SSLContext sslContext = CertificateUtilities.getContext(keyManagers, trustManagers);
        SSLSocketFactory factory = sslContext.getSocketFactory();
        setSocketFactory(factory);
    }
}
