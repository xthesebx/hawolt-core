package com.hawolt.wss;

import com.hawolt.cryptography.MD5;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

/**
 * Created: 11/12/2022 23:21
 * Author: Twitter @hawolt
 **/

public class CertificateUtilities {
    private final static String BEGIN = "-----BEGIN %s-----";
    private final static String END = "-----END %s-----";
    private final static String PASSWORD = "";

    public static SSLContext getContext(KeyManager[] keyManagers, TrustManager[]... trustManagers) throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(keyManagers, trustManagers.length == 0 ? null : trustManagers[0], null);
        return context;
    }

    public static KeyManager[] buildKeyManagers(RSAPrivateKey rsa, byte[]... certificates) throws NoSuchAlgorithmException, CertificateException, KeyStoreException, UnrecoverableKeyException, IOException {
        KeyStore keystore = KeyStore.getInstance("JKS");
        keystore.load(null);
        for (int i = 0; i < certificates.length; i++) {
            byte[] b = certificates[i];
            String plain = parseKeyData(b, FileType.CERTIFICATE);
            String keyEntry = MD5.hash(plain + FileType.PRIVATE_KEY + i);
            String certificateEntry = MD5.hash(plain + FileType.CERTIFICATE + i);
            X509Certificate certificate = parseX509Certificate(plain);
            keystore.setCertificateEntry(certificateEntry, certificate);
            keystore.setKeyEntry(keyEntry, rsa, PASSWORD.toCharArray(), new Certificate[]{certificate});
        }
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(keystore, PASSWORD.toCharArray());
        return kmf.getKeyManagers();
    }

    public static X509Certificate parseX509Certificate(String plain) throws CertificateException {
        byte[] converted = DatatypeConverter.parseBase64Binary(plain);
        return generateCertificateFromDER(converted);
    }

    public static RSAPrivateKey parsePrivateKey(byte[] b) throws InvalidKeySpecException, NoSuchAlgorithmException {
        byte[] privateKeyBytes = parseDERFromPEM(b);
        return generatePrivateKeyFromDER(privateKeyBytes);
    }

    public static String parseKeyData(byte[] b, FileType type) {
        String data = new String(b);
        String[] tokens = data.split(String.format(BEGIN, type));
        tokens = tokens[1].split(String.format(END, type));
        return tokens[0];
    }

    public static byte[] parseDERFromPEM(byte[] b) {
        return DatatypeConverter.parseBase64Binary(parseKeyData(b, FileType.PRIVATE_KEY));
    }

    public static RSAPrivateKey generatePrivateKeyFromDER(byte[] b) throws InvalidKeySpecException, NoSuchAlgorithmException {
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(b);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        return (RSAPrivateKey) factory.generatePrivate(spec);
    }

    public static X509Certificate generateCertificateFromDER(byte[] b) throws CertificateException {
        CertificateFactory factory = CertificateFactory.getInstance("X.509");
        return (X509Certificate) factory.generateCertificate(new ByteArrayInputStream(b));
    }

    public static void close(InputStream stream, InputStream... streams) throws IOException {
        stream.close();
        for (InputStream i : streams) {
            i.close();
        }
    }
}
