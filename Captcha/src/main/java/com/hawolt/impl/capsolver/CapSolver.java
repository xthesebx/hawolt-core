package com.hawolt.impl.capsolver;

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
 * Created: 23/08/2023 08:35
 * Author: Twitter @hawolt
 **/

public class CapSolver implements ICaptchaService, ICaptchaSolver {

    private final String client;

    public CapSolver(String client) {
        this.client = client;
    }

    @Override
    public String getClient() {
        return client;
    }

    @Override
    public String getName() {
        return "CapSolver";
    }

    @Override
    public double getBalance() throws IOException, CaptchaException {
        Request request = new Request("https://api.capsolver.com/getBalance", Method.POST, true);
        JSONObject payload = new JSONObject();
        payload.put("clientKey", client);
        request.write(payload);
        Response response = request.execute();
        JSONObject tmp = new JSONObject(response.getBodyAsString());
        CapSolverError type = CapSolverError.find(tmp);
        if (type == CapSolverError.NO_ERROR) return tmp.getDouble("balance");
        else throw new CaptchaException(type.getDescription());
    }

    @Override
    public ICaptchaSolver getSolver() {
        return this;
    }

    @Override
    public ICaptchaTask create(Map<String, Object> map) throws IOException, CaptchaException {
        Request request = new Request("https://api.capsolver.com/createTask", Method.POST, true);
        JSONObject task = build(((ICaptchaType) map.get("type")).getName(), map);
        JSONObject payload = new JSONObject();
        payload.put("appId", "B29452B3-3CEE-401D-BE6D-BF3B5F3DBAEE");
        payload.put("clientKey", client);
        payload.put("task", task);
        System.out.println(payload);
        request.write(payload);
        Response response = request.execute();
        JSONObject tmp = new JSONObject(response.getBodyAsString());
        CapSolverError type = CapSolverError.find(tmp);
        if (type == CapSolverError.NO_ERROR) return new CapSolverTask(client, tmp.getString("taskId"));
        else throw new CapSolverException(type.getDescription(), tmp);
    }

    private JSONObject build(String name, Map<String, Object> map) throws CaptchaException {
        JSONObject task = new JSONObject();
        task.put("type", ((ICaptchaType) map.get("type")).getName());
        switch (name) {
            case "HCaptchaTaskProxyless":
                if (map.containsKey("enterprisePayload")) task.put("enterprisePayload", map.get("enterprisePayload"));
                if (map.containsKey("userAgent")) task.put("userAgent", map.get("userAgent"));
                task.put("isEnterprise", map.get("isEnterprise"));
                task.put("isInvisible", map.get("isInvisible"));
                task.put("websiteKey", map.get("websiteKey"));
                task.put("websiteURL", map.get("websiteURL"));
                return task;
            default:
                throw new CaptchaException("Unsupported: " + name);
        }
    }
}