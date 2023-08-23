package com.hawolt.impl.capsolver;

import com.hawolt.ICaptchaResult;
import org.json.JSONObject;

/**
 * Created: 16/08/2022 10:12
 * Author: Twitter @hawolt
 **/

public class CapSolverResult implements ICaptchaResult {
    public static final CapSolverResult FAILURE = new CapSolverResult();
    private String taskId, status, userAgent, gRecaptchaResponse, captchaKey;
    private JSONObject source;
    private boolean isFailure;
    private long errorId, expireTime, timestamp;

    public CapSolverResult() {
        isFailure = true;
    }

    public CapSolverResult(JSONObject tmp) {
        JSONObject solution = tmp.getJSONObject("solution");
        this.gRecaptchaResponse = solution.getString("gRecaptchaResponse");
        this.captchaKey = solution.getString("captchaKey");
        this.expireTime = solution.getLong("expireTime");
        this.userAgent = solution.getString("userAgent");
        this.timestamp = solution.getLong("timestamp");
        this.errorId = tmp.getLong("errorId");
        this.taskId = tmp.getString("taskId");
        this.status = tmp.getString("status");
        this.source = tmp;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getStatus() {
        return status;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public String getgRecaptchaResponse() {
        return gRecaptchaResponse;
    }

    public String getCaptchaKey() {
        return captchaKey;
    }

    public JSONObject getSource() {
        return source;
    }

    public long getErrorId() {
        return errorId;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean isFailure() {
        return isFailure;
    }

    @Override
    public String getResult() {
        return gRecaptchaResponse;
    }
}
