package com.hawolt.cryptography;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

/**
 * <p>AES256 class.</p>
 *
 * @author Hawolt
 * @version 1.1
 */
public class AES256 {
    private final String secret, salt;
    private final byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private final IvParameterSpec ivspec = new IvParameterSpec(iv);
    KeySpec spec;
    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
    SecretKeySpec secretKey;
    Cipher cipher;

    private AES256(String secret, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException {
        this.secret = secret;
        this.salt = salt;
        spec = new PBEKeySpec(secret.toCharArray(), salt.getBytes(), 65536, 256);
        SecretKey tmp = factory.generateSecret(spec);
        secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
        cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
    }

    /**
     * <p>init.</p>
     *
     * @param secret a {@link java.lang.String} object
     * @param salt a {@link java.lang.String} object
     * @return a {@link com.hawolt.cryptography.AES256} object
     * @throws java.security.NoSuchAlgorithmException if any.
     * @throws javax.crypto.NoSuchPaddingException if any.
     * @throws java.security.spec.InvalidKeySpecException if any.
     */
    public static AES256 init(String secret, String salt) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException {
        return new AES256(secret, salt);
    }

    /**
     * <p>encrypt.</p>
     *
     * @param s a {@link java.lang.String} object
     * @return a {@link java.lang.String} object
     * @throws java.security.InvalidAlgorithmParameterException if any.
     * @throws java.security.InvalidKeyException if any.
     * @throws java.io.UnsupportedEncodingException if any.
     * @throws javax.crypto.BadPaddingException if any.
     * @throws javax.crypto.IllegalBlockSizeException if any.
     */
    public String encrypt(String s) throws InvalidAlgorithmParameterException, InvalidKeyException, UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException {
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
        return Base64.getEncoder().encodeToString(cipher.doFinal(s.getBytes("UTF-8")));
    }

    /**
     * <p>decrypt.</p>
     *
     * @param s a {@link java.lang.String} object
     * @return a {@link java.lang.String} object
     * @throws javax.crypto.BadPaddingException if any.
     * @throws javax.crypto.IllegalBlockSizeException if any.
     * @throws java.security.InvalidAlgorithmParameterException if any.
     * @throws java.security.InvalidKeyException if any.
     */
    public String decrypt(String s) throws BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException, InvalidKeyException {
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
        return new String(cipher.doFinal(Base64.getDecoder().decode(s)));
    }
}
