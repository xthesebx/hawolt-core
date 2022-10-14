package com.hawolt.impl.anticaptcha;

import com.hawolt.ICaptchaType;

/**
 * Created: 16/08/2022 09:51
 * Author: Twitter @hawolt
 **/

public class AntiCaptchaType {
    public static final ICaptchaType RecaptchaV2TaskProxyless = () -> "RecaptchaV2TaskProxyless";
    public static final ICaptchaType HCaptchaTaskProxyless = () -> "HCaptchaTaskProxyless";
}
