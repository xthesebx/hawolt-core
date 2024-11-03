package com.hawolt.cryptography;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Created: 06/12/2022 18:04
 * Author: Twitter @hawolt
 *
 * @author Hawolt
 * @version 1.1
 */
public class HmacSHA256 {

    /**
     * <p>generate.</p>
     *
     * @param message a {@link java.lang.String} object
     * @param key a {@link java.lang.String} object
     * @return a {@link java.lang.String} object
     * @throws java.security.InvalidKeyException if any.
     * @throws java.security.NoSuchAlgorithmException if any.
     */
    public static String generate(String message, String key) throws InvalidKeyException, NoSuchAlgorithmException {
        byte[] bytes = hmac(key.getBytes(), message.getBytes());
        return bytesToHex(bytes);
    }

    private static byte[] hmac(byte[] key, byte[] message) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(key, "HmacSHA256"));
        return mac.doFinal(message);
    }

    private static String bytesToHex(byte[] bytes) {
        final char[] hexArray = "0123456789abcdef".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0, v; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
