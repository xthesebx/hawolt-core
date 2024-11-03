package com.hawolt.http;

import com.hawolt.http.misc.DownloadCallback;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * <p>Response class.</p>
 *
 * @author Hawolt
 * @version 1.1
 */
public class Response {

    private final int code;
    private final byte[] body;
    private final Request request;
    private final DownloadCallback callback;
    private final Map<String, List<String>> headers;

    /**
     * <p>Constructor for Response.</p>
     *
     * @param request a {@link com.hawolt.http.Request} object
     * @throws java.io.IOException if any.
     */
    public Response(Request request) throws IOException {
        this(request, null);
    }

    /**
     * <p>Constructor for Response.</p>
     *
     * @param request a {@link com.hawolt.http.Request} object
     * @param callback a {@link com.hawolt.http.misc.DownloadCallback} object
     * @throws java.io.IOException if any.
     */
    public Response(Request request, DownloadCallback callback) throws IOException {
        this.request = request;
        this.callback = callback;
        HttpURLConnection connection = request.getConnection();
        headers = connection.getHeaderFields();
        if (callback != null && headers != null && headers.containsKey("Content-Length")) {
            List<String> list = headers.get("Content-Length");
            if (!list.isEmpty()) {
                String length = list.get(0);
                if (length.matches("[0-9]+")) {
                    int content = Integer.parseInt(length);
                    callback.notify(content);
                }
            }
        }
        this.code = connection.getResponseCode();
        this.body = read(connection);
        connection.disconnect();
    }

    /**
     * <p>read.</p>
     *
     * @param connection a {@link java.net.HttpURLConnection} object
     * @return an array of {@link byte} objects
     * @throws java.io.IOException if any.
     */
    public byte[] read(HttpURLConnection connection) throws IOException {
        try (InputStream stream = connection.getInputStream()) {
            return BasicHttp.get(stream, callback);
        } catch (IOException e1) {
            try (InputStream stream = connection.getErrorStream()) {
                return stream == null ? null : BasicHttp.get(stream, callback);
            } catch (IOException e2) {
                throw e2;
            }
        }
    }

    /**
     * <p>Getter for the field <code>code</code>.</p>
     *
     * @return a int
     */
    public int getCode() {
        return code;
    }

    /**
     * <p>Getter for the field <code>body</code>.</p>
     *
     * @return an array of {@link byte} objects
     */
    public byte[] getBody() {
        return body;
    }

    /**
     * <p>getBodyAsString.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getBodyAsString() {
        return new String(body, StandardCharsets.UTF_8);
    }

    /**
     * <p>getBodyAsString.</p>
     *
     * @param charset a {@link java.lang.String} object
     * @return a {@link java.lang.String} object
     * @throws java.io.UnsupportedEncodingException if any.
     */
    public String getBodyAsString(String charset) throws UnsupportedEncodingException {
        return new String(body, charset);
    }

    /**
     * <p>Getter for the field <code>headers</code>.</p>
     *
     * @return a {@link java.util.Map} object
     */
    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    /**
     * <p>getOriginRequest.</p>
     *
     * @return a {@link com.hawolt.http.Request} object
     */
    public Request getOriginRequest() {
        return request;
    }
}
