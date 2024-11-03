package com.hawolt.logger;

/**
 * <p>LogLevel class.</p>
 *
 * @author Hawolt
 * @version 1.1
 */
public enum LogLevel {
    ALL, INTERNAL, DEBUG, INFO, WARN, ERROR, FATAL;

    /** Constant <code>LEVELS</code> */
    private static final LogLevel[] LEVELS = LogLevel.values();

    /**
     * <p>find.</p>
     *
     * @param name a {@link java.lang.String} object
     * @return a {@link com.hawolt.logger.LogLevel} object
     */
    public static LogLevel find(String name) {
        for (LogLevel level : LEVELS) {
            if (level.name().equalsIgnoreCase(name)) {
                return level;
            }
        }
        return ALL;
    }
}
