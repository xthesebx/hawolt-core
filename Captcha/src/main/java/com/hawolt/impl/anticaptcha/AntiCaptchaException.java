package com.hawolt.impl.anticaptcha;

import com.hawolt.exception.CaptchaException;
import org.json.JSONObject;

/**
 * Created: 14/05/2023 18:34
 * Author: Twitter @hawolt
 **/

public class AntiCaptchaException extends CaptchaException {
    private final JSONObject source;

    public AntiCaptchaException(String message, JSONObject source) {
        super(message);
        this.source = source;
    }

    public JSONObject getSource() {
        return source;
    }
}
