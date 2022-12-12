package com.hawolt;

import com.hawolt.io.Core;
import org.java_websocket.server.DefaultSSLWebSocketServerFactory;
import org.java_websocket.server.WebSocketServer;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;

/**
 * Created: 11/12/2022 23:19
 * Author: Twitter @hawolt
 **/

public abstract class ServerWSS extends WebSocketServer {

    // We require the generated "privkey.pem" and "cert.pem" file from LetsEncrypt

    public ServerWSS(InetSocketAddress address, InputStream keyStream, InputStream certificateStream) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException, UnrecoverableKeyException, CertificateException, KeyStoreException, KeyManagementException {
        super(address);
        byte[] keyBytes = Core.read(keyStream).toByteArray();
        RSAPrivateKey privateKey = CertificateUtilities.parsePrivateKey(keyBytes);
        byte[] certificateBytes = Core.read(certificateStream).toByteArray();
        CertificateUtilities.close(keyStream, certificateStream);
        KeyManager[] keyManagers = CertificateUtilities.buildKeyManagers(privateKey, certificateBytes);
        SSLContext sslContext = CertificateUtilities.getContext(keyManagers);
        setWebSocketFactory(new DefaultSSLWebSocketServerFactory(sslContext));
        setConnectionLostTimeout(30);
        start();
    }
}
