package com.hawolt.io;

import java.io.*;
import java.nio.file.Path;

public class Core {

    public static InputStream getFileAsStream(Path path) throws FileNotFoundException {
        return new FileInputStream(path.toFile());
    }

    public static InputStream getResourceAsStream(String name) {
        return Core.class.getClassLoader().getResourceAsStream(name);
    }

    public static ByteArrayOutputStream read(InputStream stream) throws IOException {
        try (ByteArrayOutputStream result = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = stream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            return result;
        }
    }
}
