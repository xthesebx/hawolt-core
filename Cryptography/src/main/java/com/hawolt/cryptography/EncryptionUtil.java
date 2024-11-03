package com.hawolt.cryptography;

/**
 * <p>EncryptionUtil class.</p>
 *
 * @author Hawolt
 * @version 1.1
 */
public class EncryptionUtil {
    /**
     * <p>hexToString.</p>
     *
     * @param in a {@link java.lang.String} object
     * @return a {@link java.lang.String} object
     */
    public static String hexToString(String in) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < in.length(); i += 2) {
            String str = in.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }
        return output.toString();
    }

    /**
     * <p>byteToHex.</p>
     *
     * @param hash an array of {@link byte} objects
     * @return a {@link java.lang.String} object
     */
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

    /**
     * <p>salt.</p>
     *
     * @param format a {@link java.lang.String} object
     * @param in a {@link java.lang.String} object
     * @return a {@link java.lang.String} object
     */
    public static String salt(String format, String in) {
        return String.format(format, in);
    }
}
