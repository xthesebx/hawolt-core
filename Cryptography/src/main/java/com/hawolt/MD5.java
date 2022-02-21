package com.hawolt;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
    public static String hash(String plain) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return plain;
        }
        byte[] hash = digest.digest(plain.getBytes(StandardCharsets.UTF_8));
        return EncryptionUtil.byteToHex(hash);
    }


}
