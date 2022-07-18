package com.hawolt.logger;

public enum LogSetting {
    UNKNOWN,
    FORMAT_DATE,
    FORMAT_FILE,
    DEST_CONSOLE,
    DEST_FILE,
    LOG_LEVEL,
    LOG_ROLLOVER,
    LOG_DIR;

    private static final LogSetting[] LOG_SETTINGS = LogSetting.values();

    public static LogSetting find(String in) {
        for (LogSetting setting : LOG_SETTINGS) {
            if (in.equals(setting.toString())) {
                return setting;
            }
        }
        return LogSetting.UNKNOWN;
    }

    @Override
    public String toString() {
        return name().toLowerCase().replaceAll("_", ".");
    }
}
