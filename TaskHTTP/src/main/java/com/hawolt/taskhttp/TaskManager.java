package com.hawolt.taskhttp;

import com.hawolt.taskhttp.impl.AbstractTask;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * Created: 13/12/2022 15:36
 * Author: Twitter @hawolt
 **/

public class TaskManager {
    private final Map<Long, AbstractTask<?>> map = new HashMap<>();
    private final Snowflake snowflake = new Snowflake();

    private final ExecutorService service;

    public TaskManager(ExecutorService service) {
        this.service = service;
    }

    public JSONObject enqueue(AbstractTask<?> task) {
        long nextId = snowflake.nextId();
        JSONObject response = new JSONObject();
        response.put("id", nextId);
        map.put(nextId, task);
        response.put("status", TaskState.CREATED);
        service.execute(task);
        return response;
    }

    public JSONObject get(long id) {
        JSONObject response = new JSONObject();
        AbstractTask<?> task = map.get(id);
        response.put("id", id);
        response.put("state", task != null ? task.getState() : "NOT_EXISTING");
        if (task != null) {
            TaskState state = task.getState();
            switch (state) {
                case FAILED:
                    response.put("error", task.getException().getMessage());
                    break;
                case COMPLETED:
                    response.put("result", task.get().toString());
                    break;
            }
            if (state == TaskState.FAILED || state == TaskState.COMPLETED) map.remove(id);
        }
        return response;
    }
}
