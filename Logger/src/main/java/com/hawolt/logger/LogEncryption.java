package com.hawolt.logger;

public interface LogEncryption {
    String onBeforeWrite(String line);
}
