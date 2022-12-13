package com.hawolt.taskhttp;

/**
 * Created: 13/12/2022 15:44
 * Author: Twitter @hawolt
 **/

public interface Task<T> {
    T get();

    TaskState getState();
}
