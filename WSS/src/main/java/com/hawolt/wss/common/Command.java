package com.hawolt.wss.common;

/**
 * Created: 13/12/2022 23:34
 * Author: Twitter @hawolt
 **/

public interface Command {
    String perform(String in);

    String getName();

    boolean isResponseRequired();
}
