package com.hawolt.logger;

/**
 * <p>LogEncryption interface.</p>
 *
 * @author Hawolt
 * @version 1.1
 */
public interface LogEncryption {
    /**
     * <p>onBeforeWrite.</p>
     *
     * @param line a {@link java.lang.String} object
     * @return a {@link java.lang.String} object
     */
    String onBeforeWrite(String line);
}
