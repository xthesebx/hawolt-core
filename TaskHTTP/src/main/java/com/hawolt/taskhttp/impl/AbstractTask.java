package com.hawolt.taskhttp.impl;

import com.hawolt.taskhttp.Task;
import com.hawolt.taskhttp.TaskState;

/**
 * Created: 13/12/2022 15:47
 * Author: Twitter @hawolt
 **/

public abstract class AbstractTask<T> implements Runnable, Task<T> {

    protected TaskState state = TaskState.CREATED;

    private Exception e;
    private T t;

    @Override
    public TaskState getState() {
        return state;
    }

    @Override
    public void run() {
        this.state = TaskState.PENDING;
        try {
            this.t = compute();
            this.state = TaskState.COMPLETED;
        } catch (Exception e) {
            this.state = TaskState.FAILED;
            this.e = e;
            this.exceptionally();
        }
    }

    public Exception getException() {
        return e;
    }

    @Override
    public T get() {
        return t;
    }

    protected abstract T compute() throws Exception;

    protected abstract void exceptionally();
}
