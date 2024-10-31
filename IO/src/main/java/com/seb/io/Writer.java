package com.seb.io;

import java.io.*;

public class Writer {
    /**
     * write text to a file
     *
     * @param text text to write
     * @param file file to write the text to
     */
    public static void write(String text, File file) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
            writer.write(text);
            writer.close();
        } catch (FileNotFoundException e) {
            file.getParentFile().mkdirs();
            write(text, file);
        } catch (IOException ignored) {}
    }
}
