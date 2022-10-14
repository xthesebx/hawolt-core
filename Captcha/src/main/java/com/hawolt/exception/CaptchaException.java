package com.hawolt.exception;

/**
 * Created: 16/08/2022 09:42
 * Author: Twitter @hawolt
 **/

public class CaptchaException extends Exception {
    private final String message;

    public CaptchaException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
