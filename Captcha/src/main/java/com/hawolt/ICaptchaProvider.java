package com.hawolt;

import java.util.Map;

/**
 * Created: 16/08/2022 11:21
 * Author: Twitter @hawolt
 **/

public interface ICaptchaProvider {
    Map<String, Object> get();
}
