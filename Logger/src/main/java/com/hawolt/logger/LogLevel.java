package com.hawolt.logger;

public enum LogLevel {
    ALL, INTERNAL, DEBUG, INFO, WARN, ERROR, FATAL;

    private static final LogLevel[] LEVELS = LogLevel.values();

    public static LogLevel find(String name) {
        for (LogLevel level : LEVELS) {
            if (level.name().equalsIgnoreCase(name)) {
                return level;
            }
        }
        return ALL;
    }
}
