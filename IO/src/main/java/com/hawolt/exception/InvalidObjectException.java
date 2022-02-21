package com.hawolt.exception;

public class InvalidObjectException extends RuntimeException {

    public InvalidObjectException(Object o) {
        super("Invalid Object: " + o.toString());
    }
}
