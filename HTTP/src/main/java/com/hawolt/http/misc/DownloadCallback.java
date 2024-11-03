package com.hawolt.http.misc;

/**
 * <p>DownloadCallback interface.</p>
 *
 * @author Hawolt
 * @version 1.1
 */
public interface DownloadCallback {
    /**
     * <p>add.</p>
     *
     * @param length a int
     */
    void add(int length);

    /**
     * <p>notify.</p>
     *
     * @param length a int
     */
    void notify(int length);
}
