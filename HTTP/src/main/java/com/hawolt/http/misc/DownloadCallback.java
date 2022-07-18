package com.hawolt.http.misc;

public interface DownloadCallback {
    void add(int length);

    void notify(int length);
}
