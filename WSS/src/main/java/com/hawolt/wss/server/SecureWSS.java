package com.hawolt.wss.server;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created: 12/12/2022 13:35
 * Author: Twitter @hawolt
 **/

public abstract class SecureWSS extends ServerWSS {
    private final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
    private final static Map<String, Entitlement> entitlements = new HashMap<>();
    private final String passphrase;

    public SecureWSS(String passphrase, InetSocketAddress address, InputStream keyStream, InputStream certificateStream) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException, UnrecoverableKeyException, CertificateException, KeyStoreException, KeyManagementException {
        super(address, keyStream, certificateStream);
        this.passphrase = passphrase;
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        webSocket.send("> 001 : You are in a maze of twisty passages, all alike.");
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        Entitlement entitlement = new Entitlement(future);
        entitlements.put(webSocket.getRemoteSocketAddress().getHostString(), entitlement);
        service.schedule(() -> future.complete(false), 3000L, TimeUnit.MILLISECONDS);
        future.whenComplete((verified, throwable) -> {
            entitlement.setVerified(verified);
            if (!verified) {
                webSocket.send("> 007 : You got lost.");
                webSocket.close();
            }
        });
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        entitlements.remove(webSocket.getRemoteSocketAddress().getHostString());
    }

    @Override
    public void onMessage(WebSocket webSocket, String s) {
        Entitlement entitlement = entitlements.get(webSocket.getRemoteSocketAddress().getHostString());
        if (passphrase.equals(s)) {
            webSocket.send("> 003 : You have found your passage.");
            entitlement.getFuture().complete(true);
        }
        if (!entitlement.isVerified()) return;
        onVerifiedMessage(webSocket, s);
    }

    abstract void onVerifiedMessage(WebSocket webSocket, String message);

    @Override
    public void onStart() {
        setConnectionLostTimeout(100);
    }

    public void forward(String message) {
        List<WebSocket> list;
        synchronized (getConnections()) {
            list = new ArrayList<>(getConnections());
        }
        for (WebSocket webSocket : list) {
            Entitlement entitlement = entitlements.get(webSocket.getRemoteSocketAddress().getHostString());
            if (!entitlement.isVerified()) continue;
            webSocket.send(message);
        }
    }
}
