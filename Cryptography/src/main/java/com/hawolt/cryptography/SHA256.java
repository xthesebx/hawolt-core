package com.hawolt.cryptography;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * <p>SHA256 class.</p>
 *
 * @author Hawolt
 * @version 1.1
 */
public class SHA256 {
    /**
     * <p>hash.</p>
     *
     * @param plain a {@link java.lang.String} object
     * @return a {@link java.lang.String} object
     */
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
