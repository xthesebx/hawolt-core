package com.hawolt.impl.anticaptcha;

import com.hawolt.ICaptchaResult;
import com.hawolt.ICaptchaTask;
import com.hawolt.exception.CaptchaException;
import com.hawolt.exception.CaptchaLogicException;
import com.hawolt.http.Method;
import com.hawolt.http.Request;
import com.hawolt.http.Response;
import com.hawolt.logger.Logger;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.*;

/**
 * Created: 16/08/2022 09:49
 * Author: Twitter @hawolt
 **/

public class AntiCaptchaTask implements ICaptchaTask {
    private final long taskId, timeout;
    private final String client;

    public AntiCaptchaTask(String client, long taskId) {
        this(client, taskId, 240);
    }

    public AntiCaptchaTask(String client, long taskId, long timeout) {
        if (TimeUnit.SECONDS.toMillis(timeout) > 240000) {
            throw new CaptchaLogicException("Timeout exceeds AntiCaptcha base timeout");
        }
        this.timeout = timeout;
        this.taskId = taskId;
        this.client = client;
    }

    @Override
    public AntiCaptchaResult get() throws IOException, CaptchaException {
        Request request = new Request("https://api.anti-captcha.com/getTaskResult", Method.POST, true);
        JSONObject payload = new JSONObject();
        payload.put("clientKey", client);
        payload.put("taskId", taskId);
        request.write(payload);
        Response response = request.execute();
        JSONObject tmp = new JSONObject(response.getBodyAsString());
        AntiCaptchaError type = AntiCaptchaError.find(tmp);
        if (type == AntiCaptchaError.NO_ERROR) {
            if (tmp.getString("status").equals("processing")) {
                return null;
            } else {
                return new AntiCaptchaResult(tmp);
            }
        } else throw new CaptchaException(type.getDescription());
    }

    private ScheduledFuture<?> future;

    @Override
    public CompletableFuture<ICaptchaResult> solve() {
        final CompletableFuture<ICaptchaResult> future = new CompletableFuture<>();
        final long start = System.currentTimeMillis();
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        this.future = service.scheduleAtFixedRate(() -> {
            if ((System.currentTimeMillis() - start) >= TimeUnit.SECONDS.toMillis(timeout)) {
                future.complete(AntiCaptchaResult.FAILURE);
            } else {
                try {
                    future.complete(get());
                } catch (CaptchaException e) {
                    future.completeExceptionally(e);
                } catch (IOException e) {
                    Logger.error(e);
                }
            }
            if (future.isCompletedExceptionally() && future.isDone()) return;
            this.future.cancel(true);
            service.shutdown();
        }, TimeUnit.SECONDS.toMillis(10), TimeUnit.SECONDS.toMillis(5), TimeUnit.MILLISECONDS);
        return future;
    }
}
