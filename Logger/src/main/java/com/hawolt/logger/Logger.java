package com.hawolt.logger;

import com.hawolt.io.Core;

import java.io.*;
import java.nio.file.Files;
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
    private static int ROLLOVER_INTERNAL = 0;
    private static FileWriter WRITER;

    static {
        try (InputStream stream = Core.getFileAsStream(Paths.get("log.properties"))) {
            String[] set = Core.read(stream).toString().split("\n");
            for (String line : set) {
                String[] config = line.trim().split("=", 2);
                if (config.length == 2) {
                    LogSetting setting = LogSetting.find(config[0]);
                    Logger.log(LogLevel.ALL, true, "ALL {}:{}", setting.name(), config[1]);
                    switch (setting) {
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
        } catch (IOException e) {
            Logger.log(LogLevel.INTERNAL, true, "No log.properties present");
        }
    }

    public static String format(String format, Object... objects) {
        StringBuilder builder = new StringBuilder(format);
        int count = 0;
        int indexOf = -1;
        do {
            indexOf = builder.indexOf("{}", indexOf + 1);
            if (indexOf >= 0) {
                builder.replace(indexOf, indexOf + 2, objects[count++].toString());
            }
        } while (indexOf != -1);
        return builder.toString();
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
