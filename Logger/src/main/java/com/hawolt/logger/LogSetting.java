package com.hawolt.logger;

import java.util.Locale;

/**
 * <p>LogSetting class.</p>
 *
 * @author Hawolt
 * @version 1.1
 */
public enum LogSetting {
    UNKNOWN,
    FORMAT_DATE,
    FORMAT_FILE,
    DEST_CONSOLE,
    DEST_FILE,
    LOG_LEVEL,
    LOG_ROLLOVER,
    LOG_DIR,
    BASE_STRUCTURE;

    /** Constant <code>LOG_SETTINGS</code> */
    private static final LogSetting[] LOG_SETTINGS = LogSetting.values();

    /**
     * <p>find.</p>
     *
     * @param in a {@link java.lang.String} object
     * @return a {@link com.hawolt.logger.LogSetting} object
     */
    public static LogSetting find(String in) {
        for (LogSetting setting : LOG_SETTINGS) {
            if (in.equals(setting.toString())) {
                return setting;
            }
        }
        return LogSetting.UNKNOWN;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return name().toLowerCase(Locale.ENGLISH).replaceAll("_", ".");
    }
}
