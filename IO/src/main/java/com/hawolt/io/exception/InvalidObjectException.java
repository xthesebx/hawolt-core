package com.hawolt.io.exception;

/**
 * <p>InvalidObjectException class.</p>
 *
 * @author Hawolt
 * @version 1.1
 */
public class InvalidObjectException extends RuntimeException {

    /**
     * <p>Constructor for InvalidObjectException.</p>
     *
     * @param o a {@link java.lang.Object} object
     */
    public InvalidObjectException(Object o) {
        super("Invalid Object: " + o.toString());
    }
}
