package com.hawolt.impl.anticaptcha;

import com.hawolt.ICaptchaResult;
import org.json.JSONObject;

/**
 * Created: 16/08/2022 10:12
 * Author: Twitter @hawolt
 **/

public class AntiCaptchaResult implements ICaptchaResult {
    public static final AntiCaptchaResult FAILURE = new AntiCaptchaResult();
    private JSONObject source;
    private double cost;
    private String solution, userAgent;
    private long createdAt, solvedAt;
    private int solveCount, errorId;
    private String ip, status;
    private boolean isFailure;

    public AntiCaptchaResult() {
        isFailure = true;
    }

    public AntiCaptchaResult(JSONObject tmp) {
        JSONObject solution = tmp.getJSONObject("solution");
        this.solution = solution.getString("gRecaptchaResponse");
        this.userAgent = solution.getString("userAgent");
        this.createdAt = tmp.getLong("createTime");
        this.solveCount = tmp.getInt("solveCount");
        this.solvedAt = tmp.getLong("endTime");
        this.status = tmp.getString("status");
        this.errorId = tmp.getInt("errorId");
        this.cost = tmp.getDouble("cost");
        this.ip = tmp.getString("ip");
        this.source = tmp;
    }

    public JSONObject getSource() {
        return source;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public double getCost() {
        return cost;
    }

    public String getSolution() {
        return solution;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getSolvedAt() {
        return solvedAt;
    }

    public int getSolveCount() {
        return solveCount;
    }

    public int getErrorId() {
        return errorId;
    }

    public String getIp() {
        return ip;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public boolean isFailure() {
        return isFailure;
    }

    @Override
    public String getResult() {
        return solution;
    }
}
