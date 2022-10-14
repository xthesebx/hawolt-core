package com.hawolt;

import com.hawolt.exception.CaptchaException;

import java.io.IOException;

/**
 * Created: 16/08/2022 09:32
 * Author: Twitter @hawolt
 **/

public interface ICaptchaService {
    String getClient();

    String getName();

    double getBalance() throws CaptchaException, IOException;

    ICaptchaSolver getSolver();
}
