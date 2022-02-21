package com.hawolt.misc;

public interface DownloadCallback {
    void add(int length);

    void notify(int length);
}
