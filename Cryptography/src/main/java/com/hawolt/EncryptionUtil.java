package com.hawolt;

public class EncryptionUtil {
    public static String hexToString(String in) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < in.length(); i += 2) {
            String str = in.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }
        return output.toString();
    }

    public static String byteToHex(byte[] hash) {
        StringBuilder builder = new StringBuilder();
        for (byte character : hash) {
            String hex = Integer.toHexString(0xff & character);
            if (hex.length() == 1)
                builder.append('0');
            builder.append(hex);
        }
        return builder.toString();
    }

    public static String salt(String format, String in) {
        return String.format(format, in);
    }
}
