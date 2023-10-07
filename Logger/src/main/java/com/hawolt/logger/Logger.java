package com.hawolt.logger;

import com.hawolt.io.Core;
import com.hawolt.io.RunLevel;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Logger {
    private final static Object SYNCHRONIZED_LOCK = new Object();
    private static SimpleDateFormat LOG_DATE_FORMAT = new SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.US);
    private static SimpleDateFormat LOG_FILE_FORMAT = new SimpleDateFormat("dd-MMM-yyyy_HH-mm-ss", Locale.US);
    private static Path TARGET_DIRECTORY = Paths.get(System.getProperty("user.dir"));
    private static boolean LOG_TO_FILE, LOG_TO_CONSOLE = true;
    private static LogLevel MIN_LOG_LEVEL = LogLevel.ALL;
    private static String BASE_STRUCTURE = "";
    private static int ROLLOVER_INTERNAL = 0;
    private static FileWriter WRITER;

    static {
        try {
            RunLevel level = RunLevel.getLevel(Logger.class);
            InputStream dynamic = RunLevel.get("log.properties");
            Logger.log(LogLevel.INTERNAL, true, "Logger Runlevel {}", level);
            Logger.log(LogLevel.INTERNAL, true, "Properties available for {}:{}", level, dynamic != null);
            if (dynamic == null) {
                Logger.log(LogLevel.INTERNAL, true, "Attempting to locate log.properties in directory");
            }
            try (InputStream stream = dynamic != null ? dynamic : Core.getFileAsStream(Paths.get("log.properties"))) {
                load(stream);
            } catch (IOException e) {
                Logger.log(LogLevel.INTERNAL, true, e.getMessage());
                Logger.log(LogLevel.INTERNAL, true, "Unable to locate log.properties in default directory");
            }
        } catch (IOException e) {
            if (e instanceof NoSuchFileException) {
                Logger.warn("Unable to locate log.properties");
            } else {
                Logger.error(e);
            }
        }
    }

    public static void load(InputStream stream) {
        try {
            String[] set = Core.read(stream).toString().split("\n");
            for (String line : set) {
                String[] config = line.trim().split("=", 2);
                if (config.length == 2) {
                    LogSetting setting = LogSetting.find(config[0]);
                    switch (setting) {
                        case BASE_STRUCTURE:
                            BASE_STRUCTURE = config[1];
                            log(LogLevel.INTERNAL, true, "{}:{}", setting.name(), config[1]);
                            break;
                        case FORMAT_DATE:
                            LOG_DATE_FORMAT = new SimpleDateFormat(config[1], Locale.US);
                            log(LogLevel.INTERNAL, true, "{}:{}", setting.name(), config[1]);
                            break;
                        case FORMAT_FILE:
                            LOG_FILE_FORMAT = new SimpleDateFormat(config[1], Locale.US);
                            log(LogLevel.INTERNAL, true, "{}:{}", setting.name(), config[1]);
                            break;
                        case DEST_CONSOLE:
                            LOG_TO_CONSOLE = Boolean.parseBoolean(config[1]);
                            log(LogLevel.INTERNAL, true, "{}:{}", setting.name(), config[1]);
                            break;
                        case DEST_FILE:
                            LOG_TO_FILE = Boolean.parseBoolean(config[1]);
                            log(LogLevel.INTERNAL, true, "{}:{}", setting.name(), config[1]);
                            break;
                        case LOG_LEVEL:
                            MIN_LOG_LEVEL = LogLevel.find(config[1]);
                            log(LogLevel.INTERNAL, true, "{}:{}", setting.name(), config[1]);
                            break;
                        case LOG_ROLLOVER:
                            ROLLOVER_INTERNAL = Integer.parseInt(config[1]);
                            log(LogLevel.INTERNAL, true, "{}:{}", setting.name(), config[1]);
                            break;
                        case LOG_DIR:
                            TARGET_DIRECTORY = Paths.get(config[1]);
                            log(LogLevel.INTERNAL, true, "{}:{}", setting.name(), config[1]);
                            break;
                        default:
                            log(LogLevel.ALL, true, "DEFAULT {}:{}", setting.name(), config[1]);
                            break;

                    }
                }
            }
            if (LOG_TO_FILE) {
                Files.createDirectories(TARGET_DIRECTORY);
                Logger.WRITER = new FileWriter(TARGET_DIRECTORY.resolve(LOG_FILE_FORMAT.format(new Date())).toFile());
                if (ROLLOVER_INTERNAL > 0) {
                    long nextRollOver = Instant.now().atZone(ZoneOffset.UTC).plusDays(1).truncatedTo(ChronoUnit.DAYS).toInstant().toEpochMilli();
                    long delayUntil = nextRollOver - System.currentTimeMillis();
                    Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
                        try {
                            synchronized (SYNCHRONIZED_LOCK) {
                                Logger.WRITER = new FileWriter(TARGET_DIRECTORY.resolve(LOG_FILE_FORMAT.format(new Date())).toFile());
                            }
                        } catch (IOException e) {
                            Logger.error(e);
                        }
                    }, delayUntil, TimeUnit.DAYS.toMillis(ROLLOVER_INTERNAL), TimeUnit.MILLISECONDS);
                }
            }
        } catch (Exception e) {
            Logger.log(LogLevel.INTERNAL, true, "Error parsing provided property file");
        }
    }

    public static String format(String format, Object... objects) {
        String[] base = BASE_STRUCTURE.split(",", 2);
        Object[] values = form(base, objects);
        StringBuilder builder = new StringBuilder(String.join(" ", base[0], format).trim());
        int count = 0;
        int indexOf = -1;
        do {
            indexOf = builder.indexOf("{}", indexOf + 1);
            if (indexOf >= 0) {
                String replacement = getPlausibleCalling(values[count++].toString());
                builder.replace(indexOf, indexOf + 2, replacement);
                indexOf += replacement.length() - 1;
            }
        } while (indexOf != -1);
        return builder.toString();
    }

    private static Object[] form(String[] base, Object[] objects) {
        if (base.length != 2) return objects;
        Object[] leading = base[1].split(",");
        Object[] replacement = new Object[leading.length + objects.length];
        System.arraycopy(leading, 0, replacement, 0, leading.length);
        System.arraycopy(objects, 0, replacement, leading.length, objects.length);
        return replacement;
    }

    private static String getPlausibleCalling(String replacement) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (replacement.contains("$METHOD"))
            replacement = replacement.replace("$METHOD", getCallingMethodName(stackTrace));
        if (replacement.contains("$CLASS"))
            replacement = replacement.replace("$CLASS", getCallingClassName(stackTrace));
        if (replacement.contains("$ORIGIN")) replacement = replacement.replace("$ORIGIN", getCallingOrigin(stackTrace));
        return replacement;
    }

    private static StackTraceElement getFirstPlausible(StackTraceElement[] elements) {
        for (int i = 1; i < elements.length; i++) {
            StackTraceElement element = elements[i];
            if (element.getClassName().startsWith("com.hawolt.logger.Logger")) continue;
            return element;
        }
        return elements[elements.length - 1];
    }

    private static String getCallingOrigin(StackTraceElement[] stackTrace) {
        StackTraceElement element = getFirstPlausible(stackTrace);
        return String.join("::", element.getClassName(), element.getMethodName());
    }


    private static String getCallingClassName(StackTraceElement[] stackTrace) {
        StackTraceElement element = getFirstPlausible(stackTrace);
        return element.getClassName();
    }

    private static String getCallingMethodName(StackTraceElement[] stackTrace) {
        StackTraceElement element = getFirstPlausible(stackTrace);
        return element.getMethodName();
    }

    private static void writeToOutputStream(LogLevel level, String line, boolean linebreak) {
        PrintStream stream = level == LogLevel.ERROR ? System.err : System.out;
        try {
            stream.write(line.getBytes());
            if (linebreak) stream.write(System.lineSeparator().getBytes());
            stream.flush();
        } catch (IOException e) {

        }
    }

    private static void writeToFile(String line, boolean linebreak) {
        if (Logger.WRITER == null) return;
        try {
            Logger.WRITER.write(line);
            if (linebreak) Logger.WRITER.write(System.lineSeparator());
            Logger.WRITER.flush();
        } catch (IOException e) {

        }
    }

    private static void write(LogLevel level, String line, boolean linebreak) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        Runnable runnable = () -> {
            synchronized (SYNCHRONIZED_LOCK) {
                if (LOG_TO_FILE) writeToFile(line, linebreak);
                if (LOG_TO_CONSOLE) writeToOutputStream(level, line, linebreak);
            }
        };
        service.execute(runnable);
        service.shutdown();
    }

    public static void log(LogLevel level, boolean linebreak, String format, Object... objects) {
        if (level.ordinal() >= MIN_LOG_LEVEL.ordinal()) {
            String line = "[" + LOG_DATE_FORMAT.format(new Date()) + "] [" + level.name() + "] " + format(format, objects);
            write(level, line, linebreak);
        }
    }

    public static void error(Throwable throwable) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        throwable.printStackTrace(new PrintStream(out));
        log(LogLevel.ERROR, false, "{}", out.toString());
    }

    public static void fatal(String format, Object... objects) {
        log(LogLevel.FATAL, true, format, objects);
    }

    public static void warn(String format, Object... objects) {
        log(LogLevel.WARN, true, format, objects);
    }

    public static void error(String format, Object... objects) {
        log(LogLevel.ERROR, true, format, objects);
    }

    public static void info(String format, Object... objects) {
        log(LogLevel.INFO, true, format, objects);
    }

    public static void debug(String format, Object... objects) {
        log(LogLevel.DEBUG, true, format, objects);
    }

    public static void fatal(Object object) {
        log(LogLevel.FATAL, true, "{}", object);
    }

    public static void warn(Object object) {
        log(LogLevel.WARN, true, "{}", object);
    }

    public static void error(Object object) {
        log(LogLevel.ERROR, true, "{}", object);
    }

    public static void info(Object object) {
        log(LogLevel.INFO, true, "{}", object);
    }

    public static void debug(Object object) {
        log(LogLevel.DEBUG, true, "{}", object);
    }
}
