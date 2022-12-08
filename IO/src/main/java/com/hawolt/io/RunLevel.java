package com.hawolt.io;

import java.io.*;
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

    final static RunLevel _LEVEL;

    static {
        URL url = RunLevel.class.getResource("RunLevel.class");
        if (url == null) {
            _LEVEL = RunLevel.UNKNOWN;
        } else {
            String plain = url.toString();
            _LEVEL = plain.startsWith("file") ? FILE : plain.startsWith("jar") ? JAR : UNKNOWN;
        }
    }

    public static InputStream get(Class<?> o, String file) throws IOException {
        switch (_LEVEL) {
            case JAR:
                return o.getResourceAsStream("/" + file);
            case FILE:
                Path path = Paths.get(System.getProperty("user.dir")).resolve("src").resolve("main").resolve("resources").resolve(file);
                return Files.newInputStream(path.toFile().toPath());
        }
        return new ByteArrayInputStream(new byte[0]);
    }
}
