package com.hawolt;


import java.util.concurrent.CompletableFuture;

/**
 * Created: 16/08/2022 09:37
 * Author: Twitter @hawolt
 **/

public interface ICaptchaTask {
    CompletableFuture<ICaptchaResult> solve();
}
