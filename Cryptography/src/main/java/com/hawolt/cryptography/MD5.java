package com.hawolt.cryptography;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * <p>MD5 class.</p>
 *
 * @author Hawolt
 * @version 1.1
 */
public class MD5 {
    /**
     * <p>hash.</p>
     *
     * @param plain a {@link java.lang.String} object
     * @return a {@link java.lang.String} object
     */
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
