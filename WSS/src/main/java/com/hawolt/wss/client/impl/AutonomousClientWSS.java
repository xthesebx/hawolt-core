package com.hawolt.wss.client.impl;

import com.hawolt.wss.client.SecureClientWSS;
import com.hawolt.wss.common.Command;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created: 14/12/2022 03:10
 * Author: Twitter @hawolt
 **/

public abstract class AutonomousClientWSS extends SecureClientWSS {

    private final Map<String, Command> map = new HashMap<>();

    public AutonomousClientWSS(String passphrase, URI serverUri, InputStream keyStream, InputStream... certificateStreams) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException, UnrecoverableKeyException, CertificateException, KeyStoreException, KeyManagementException {
        super(passphrase, serverUri, keyStream, certificateStreams);
    }

    public void addCommand(Command command) {
        map.put(command.getName(), command);
    }

    @Override
    public void onMessage(String message) {
        String[] data = message.split(" ");
        if (!map.containsKey(data[0])) return;
        Command command = map.get(data[0]);
        String response = command.perform(message);
        if (command.isResponseRequired()) send(response);
    }
}
