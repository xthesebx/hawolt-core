package com.seb.io;

import org.json.JSONObject;

import java.io.*;

/**
 * <p>Reader class.</p>
 *
 * @author xXTheSebXx
 * @version 1.1
 */
public class Reader {

    /**
     * reads Json of a file
     * if file doesnt exist create empty json file
     *
     * @param file the file to read from
     * @return String builder with text from the File
     */
    public static JSONObject readJSON(File file) {
        if (!file.exists()) {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
                writer.write("{}");
                writer.close();
            } catch (IOException ignored) {
            }
        }
        return new JSONObject(readFinal(file));
    }

    /**
     * reads text of a file
     * if file doesnt exist create empty file
     *
     * @param file the file to read from
     * @return String builder with text from the File
     */
    public static String read(File file) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ignored) {
            }
        }
        return readFinal(file);
    }

    private static String readFinal(File file) {
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file.getAbsoluteFile()));
            String temp;
            while ((temp = reader.readLine()) != null) {
                text.append(temp).append("\n");
            }
            reader.close();
        } catch (IOException ignored) {
        }
        if (text.isEmpty()) return null;
        return text.deleteCharAt(text.length() - 1).toString();
    }
}
