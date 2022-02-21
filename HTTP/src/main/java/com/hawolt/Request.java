package com.hawolt;


import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;

public class Request {
    private final HttpURLConnection connection;

    public Request(String endpoint) throws IOException {
        this(endpoint, null);
    }

    public Request(String endpoint, Proxy proxy) throws IOException {
        this(endpoint, proxy, Method.GET, false);
    }

    public Request(String endpoint, Method method, boolean output) throws IOException {
        this(endpoint, null, method, output);
    }

    public Request(String endpoint, Proxy proxy, Method method, boolean output) throws IOException {
        connection = BasicHttp.open(endpoint, proxy);
        connection.setRequestMethod(method.name());
        connection.setDoOutput(output);
    }

    public void addHeader(String key, String value) {
        connection.addRequestProperty(key, value);
    }

    public void write(Object output) throws IOException {
        try (OutputStream out = connection.getOutputStream()) {
            out.write(output.toString().getBytes());
            out.flush();
        }
    }

    public HttpURLConnection getConnection() {
        return connection;
    }


    public Response execute() throws IOException {
        return new Response(this);
    }
}
