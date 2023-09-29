package com.hawolt.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created: 31/07/2022 00:09
 * Author: Twitter @hawolt
 **/

public enum RunLevel {
    JAR, FILE, UNKNOWN;

    private static final RunLevel _LEVEL;

    static {
        URL url = RunLevel.class.getResource("RunLevel.class");
        if (url == null) {
            _LEVEL = RunLevel.UNKNOWN;
        } else {
            String plain = url.toString();
            _LEVEL = plain.startsWith("file") ? FILE : plain.startsWith("jar") ? JAR : UNKNOWN;
        }
    }

    public static RunLevel getLevel(Class<?> klass) {
        URL url = klass.getResource(String.join(".", klass.getSimpleName(), "class"));
        if (url == null) {
            return RunLevel.UNKNOWN;
        } else {
            String plain = url.toString();
            return plain.startsWith("file") ? FILE : plain.startsWith("jar") ? JAR : UNKNOWN;
        }
    }

    public static RunLevel getLevel() {
        return _LEVEL;
    }

    public static InputStream get(String file) throws IOException {
        return get(file, _LEVEL);
    }

    public static InputStream get(String file, RunLevel level) throws IOException {
        switch (level) {
            case JAR:
                InputStream stream = RunLevel.class.getResourceAsStream("/" + file);
                if (stream == null) return get(file, RunLevel.FILE);
                return stream;
            case FILE:
                Path base = Paths.get(System.getProperty("user.dir"));
                Path root = base.resolve(file);
                Path resource = base.resolve("src").resolve("main").resolve("resources").resolve(file);
                if (root.toFile().exists()) {
                    return Files.newInputStream(root.toFile().toPath());
                } else if (resource.toFile().exists()) {
                    return Files.newInputStream(resource.toFile().toPath());
                } else {
                    throw new NoSuchFileException(file);
                }
        }
        return new ByteArrayInputStream(new byte[0]);
    }
}
