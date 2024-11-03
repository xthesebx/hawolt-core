package com.hawolt.io.exception;

/**
 * <p>InvalidKeyException class.</p>
 *
 * @author Hawolt
 * @version 1.1
 */
public class InvalidKeyException extends Exception {

    /**
     * <p>Constructor for InvalidKeyException.</p>
     *
     * @param key a {@link java.lang.String} object
     */
    public InvalidKeyException(String key) {
        super("Specified key [" + key + "] is not present");
    }
}
