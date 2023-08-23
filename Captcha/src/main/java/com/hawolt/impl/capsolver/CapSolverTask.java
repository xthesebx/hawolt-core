package com.hawolt.impl.capsolver;

import com.hawolt.ICaptchaResult;
import com.hawolt.ICaptchaTask;
import com.hawolt.exception.CaptchaException;
import com.hawolt.exception.CaptchaLogicException;
import com.hawolt.http.Method;
import com.hawolt.http.Request;
import com.hawolt.http.Response;
import com.hawolt.impl.anticaptcha.AntiCaptchaResult;
import com.hawolt.logger.Logger;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.*;

/**
 * Created: 16/08/2022 09:49
 * Author: Twitter @hawolt
 **/

public class CapSolverTask implements ICaptchaTask {
    private final String client, taskId;
    private final long timeout;

    public CapSolverTask(String client, String taskId) {
        this(client, taskId, 300);
    }

    public CapSolverTask(String client, String taskId, long timeout) {
        if (TimeUnit.SECONDS.toMillis(timeout) > 300000) {
            throw new CaptchaLogicException("Timeout exceeds CapSolver base timeout");
        }
        this.timeout = timeout;
        this.taskId = taskId;
        this.client = client;
    }

    @Override
    public CapSolverResult get() throws IOException, CaptchaException {
        Request request = new Request("https://api.capsolver.com/getTaskResult", Method.POST, true);
        JSONObject payload = new JSONObject();
        payload.put("clientKey", client);
        payload.put("taskId", taskId);
        request.write(payload);
        Response response = request.execute();
        JSONObject tmp = new JSONObject(response.getBodyAsString());
        CapSolverError type = CapSolverError.find(tmp);
        if (type == CapSolverError.NO_ERROR) {
            String status = tmp.getString("status");
            if ("processing".equals(status) || "idle".equals(status)) {
                return null;
            } else {
                return new CapSolverResult(tmp);
            }
        } else throw new CapSolverException(type.getDescription(), tmp);
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
        }, TimeUnit.SECONDS.toMillis(10), TimeUnit.SECONDS.toMillis(3), TimeUnit.MILLISECONDS);
        return future;
    }
}
