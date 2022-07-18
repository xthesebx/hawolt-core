package com.hawolt.cryptography;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256 {
    public static String hash(String plain) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            return plain;
        }
        byte[] hash = digest.digest(plain.getBytes(StandardCharsets.UTF_8));
        return EncryptionUtil.byteToHex(hash);
    }


}
