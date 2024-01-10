package com.hawolt.http;

import com.hawolt.http.misc.DownloadCallback;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class Response {

    private final int code;
    private final byte[] body;
    private final Request request;
    private final DownloadCallback callback;
    private final Map<String, List<String>> headers;

    public Response(Request request) throws IOException {
        this(request, null);
    }

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

    public int getCode() {
        return code;
    }

    public byte[] getBody() {
        return body;
    }

    public String getBodyAsString() {
        return new String(body, StandardCharsets.UTF_8);
    }

    public String getBodyAsString(String charset) throws UnsupportedEncodingException {
        return new String(body, charset);
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public Request getOriginRequest() {
        return request;
    }
}
