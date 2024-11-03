package com.hawolt.io;

import java.io.*;
import java.nio.file.Path;

/**
 * <p>Core class.</p>
 *
 * @author Hawolt
 * @version 1.1
 */
public class Core {

    /**
     * <p>getFileAsStream.</p>
     *
     * @param path a {@link java.nio.file.Path} object
     * @return a {@link java.io.InputStream} object
     * @throws java.io.FileNotFoundException if any.
     */
    public static InputStream getFileAsStream(Path path) throws FileNotFoundException {
        return new FileInputStream(path.toFile());
    }

    /**
     * <p>getResourceAsStream.</p>
     *
     * @param name a {@link java.lang.String} object
     * @return a {@link java.io.InputStream} object
     */
    public static InputStream getResourceAsStream(String name) {
        return Core.class.getClassLoader().getResourceAsStream(name);
    }

    /**
     * <p>read.</p>
     *
     * @param stream a {@link java.io.InputStream} object
     * @return a {@link java.io.ByteArrayOutputStream} object
     * @throws java.io.IOException if any.
     */
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
