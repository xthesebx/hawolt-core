package com.hawolt.impl.anticaptcha;

import com.hawolt.ICaptchaService;
import com.hawolt.ICaptchaSolver;
import com.hawolt.ICaptchaTask;
import com.hawolt.ICaptchaType;
import com.hawolt.exception.CaptchaException;
import com.hawolt.http.Method;
import com.hawolt.http.Request;
import com.hawolt.http.Response;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;


/**
 * Created: 16/08/2022 09:35
 * Author: Twitter @hawolt
 **/

public class AntiCaptcha implements ICaptchaService, ICaptchaSolver {

    private final String client;

    public AntiCaptcha(String client) {
        this.client = client;
    }

    @Override
    public String getClient() {
        return client;
    }

    @Override
    public String getName() {
        return "AntiCaptcha";
    }

    @Override
    public double getBalance() throws IOException, CaptchaException {
        Request request = new Request(" https://api.anti-captcha.com/getBalance", Method.POST, true);
        JSONObject payload = new JSONObject();
        payload.put("clientKey", client);
        request.write(payload);
        Response response = request.execute();
        JSONObject tmp = new JSONObject(response.getBodyAsString());
        AntiCaptchaError type = AntiCaptchaError.find(tmp);
        if (type == AntiCaptchaError.NO_ERROR) return tmp.getDouble("balance");
        else throw new CaptchaException(type.getDescription());
    }

    @Override
    public ICaptchaSolver getSolver() {
        return this;
    }

    @Override
    public ICaptchaTask create(Map<String, Object> map) throws IOException, CaptchaException {
        Request request = new Request("https://api.anti-captcha.com/createTask", Method.POST, true);
        JSONObject task = build(((ICaptchaType) map.get("type")).getName(), map);
        JSONObject payload = new JSONObject();
        payload.put("clientKey", client);
        payload.put("task", task);
        request.write(payload);
        Response response = request.execute();
        JSONObject tmp = new JSONObject(response.getBodyAsString());
        AntiCaptchaError type = AntiCaptchaError.find(tmp);
        if (type == AntiCaptchaError.NO_ERROR) return new AntiCaptchaTask(client, tmp.getLong("taskId"));
        else throw new AntiCaptchaException(type.getDescription(), tmp);
    }

    private JSONObject build(String name, Map<String, Object> map) {
        JSONObject task = new JSONObject();
        task.put("type", ((ICaptchaType) map.get("type")).getName());
        switch (name) {
            case "HCaptchaTaskProxyless":
                if (map.containsKey("enterprisePayload")) task.put("enterprisePayload", map.get("enterprisePayload"));
                task.put("isInvisible", map.get("isInvisible"));
                task.put("websiteKey", map.get("websiteKey"));
                task.put("websiteURL", map.get("websiteURL"));
                return task;
            case "RecaptchaV2TaskProxyless":
                task.put("websiteKey", map.get("websiteKey"));
                task.put("websiteURL", map.get("websiteURL"));
                return task;
            default:
                return task;
        }
    }
}
