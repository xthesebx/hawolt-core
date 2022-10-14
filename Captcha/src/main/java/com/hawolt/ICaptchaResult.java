package com.hawolt;

/**
 * Created: 16/08/2022 09:37
 * Author: Twitter @hawolt
 **/

public interface ICaptchaResult {
    String getResult();

    boolean isFailure();
}
