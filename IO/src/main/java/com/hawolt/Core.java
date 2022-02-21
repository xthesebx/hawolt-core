package com.hawolt;

import java.io.*;
import java.nio.file.Path;

public class Core {
    static {
        System.out.print("\033[0;32m");
        try {
            System.out.println(read(getResourceAsStream("lion")).toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print("\033[0m");
    }

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
