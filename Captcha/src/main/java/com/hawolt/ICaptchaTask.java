package com.hawolt;


import com.hawolt.exception.CaptchaException;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * Created: 16/08/2022 09:37
 * Author: Twitter @hawolt
 **/

public interface ICaptchaTask {
    CompletableFuture<ICaptchaResult> solve();

    ICaptchaResult get() throws IOException, CaptchaException;
}
