package com.hawolt.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created: 31/07/2022 00:09
 * Author: Twitter @hawolt
 **/

public enum RunLevel {
    JAR, FILE, UNKNOWN;

    private final static RunLevel _LEVEL;

    static {
        URL url = RunLevel.class.getResource("RunLevel.class");
        if (url == null) {
            _LEVEL = RunLevel.UNKNOWN;
        } else {
            String plain = url.toString();
            _LEVEL = plain.startsWith("file") ? FILE : plain.startsWith("jar") ? JAR : UNKNOWN;
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
                Path path = Paths.get(System.getProperty("user.dir")).resolve("src").resolve("main").resolve("resources").resolve(file);
                return Files.newInputStream(path.toFile().toPath());
        }
        return new ByteArrayInputStream(new byte[0]);
    }
}
