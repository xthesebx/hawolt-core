package com.hawolt.http;

import com.hawolt.http.misc.DownloadCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * <p>BasicHttp class.</p>
 *
 * @author Hawolt
 * @version 1.1
 */
public class BasicHttp {

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36";

    /**
     * <p>open.</p>
     *
     * @param endpoint a {@link java.lang.String} object
     * @param proxy a {@link java.net.Proxy} object
     * @return a {@link java.net.HttpURLConnection} object
     * @throws java.io.IOException if any.
     */
    public static HttpURLConnection open(String endpoint, Proxy proxy) throws IOException {
        HttpURLConnection connection;
        if (proxy != null) {
            connection = (HttpURLConnection) new URL(endpoint).openConnection(proxy);
        } else {
            connection = (HttpURLConnection) new URL(endpoint).openConnection();
        }
        connection.setRequestProperty("user-agent", USER_AGENT);
        return connection;
    }

    /**
     * <p>read.</p>
     *
     * @param stream a {@link java.io.InputStream} object
     * @return a {@link java.lang.String} object
     * @throws java.io.IOException if any.
     */
    public static String read(InputStream stream) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = stream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString(StandardCharsets.UTF_8.name());
    }

    /**
     * <p>get.</p>
     *
     * @param stream a {@link java.io.InputStream} object
     * @return an array of {@link byte} objects
     * @throws java.io.IOException if any.
     */
    public static byte[] get(InputStream stream) throws IOException {
        return get(stream, null);
    }

    /**
     * <p>get.</p>
     *
     * @param stream a {@link java.io.InputStream} object
     * @param callback a {@link com.hawolt.http.misc.DownloadCallback} object
     * @return an array of {@link byte} objects
     * @throws java.io.IOException if any.
     */
    public static byte[] get(InputStream stream, DownloadCallback callback) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = stream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
            if (callback != null) callback.add(length);
        }
        return result.toByteArray();
    }
}
