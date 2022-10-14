package com.hawolt.exception;

/**
 * Created: 16/08/2022 09:42
 * Author: Twitter @hawolt
 **/

public class CaptchaLogicException extends RuntimeException {
    private final String message;

    public CaptchaLogicException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
