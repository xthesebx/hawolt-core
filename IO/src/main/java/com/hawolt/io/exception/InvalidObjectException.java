package com.hawolt.io.exception;

public class InvalidObjectException extends RuntimeException {

    public InvalidObjectException(Object o) {
        super("Invalid Object: " + o.toString());
    }
}
