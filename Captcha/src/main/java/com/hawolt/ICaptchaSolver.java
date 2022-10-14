package com.hawolt;

import com.hawolt.exception.CaptchaException;

import java.io.IOException;
import java.util.Map;

/**
 * Created: 16/08/2022 09:33
 * Author: Twitter @hawolt
 **/

public interface ICaptchaSolver {
    ICaptchaTask create(Map<String, Object> map) throws CaptchaException, IOException;
}
