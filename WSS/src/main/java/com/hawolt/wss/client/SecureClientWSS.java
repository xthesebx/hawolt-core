package com.hawolt.wss.client;

import org.java_websocket.handshake.ServerHandshake;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;

/**
 * Created: 14/12/2022 03:06
 * Author: Twitter @hawolt
 **/

public abstract class SecureClientWSS extends ClientWSS {

    private final String passphrase;

    public SecureClientWSS(String passphrase, URI serverUri, InputStream keyStream, InputStream... certificateStreams) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException, UnrecoverableKeyException, CertificateException, KeyStoreException, KeyManagementException {
        super(serverUri, keyStream, certificateStreams);
        this.passphrase = passphrase;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        send(passphrase);
    }
}
