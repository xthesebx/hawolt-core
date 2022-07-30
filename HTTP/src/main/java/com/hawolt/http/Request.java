package com.hawolt.http;


import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.Proxy;

public class Request {
    private HttpURLConnection connection;
    private final String endpoint;
    private final boolean output;
    private final Method method;
    private final Proxy proxy;

    public Request(String endpoint) {
        this(endpoint, null);
    }

    public Request(String endpoint, Proxy proxy) {
        this(endpoint, proxy, Method.GET, false);
    }

    public Request(String endpoint, Method method, boolean output) {
        this(endpoint, null, method, output);
    }

    public Request(String endpoint, Proxy proxy, Method method, boolean output) {
        this.endpoint = endpoint;
        this.method = method;
        this.output = output;
        this.proxy = proxy;
    }

    private Request prepare() throws IOException {
        connection = BasicHttp.open(endpoint, proxy);
        connection.setRequestMethod(method.name());
        connection.setDoOutput(output);
        return this;
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

    public String getEndpoint() {
        return endpoint;
    }

    public boolean isOutput() {
        return output;
    }

    public Method getMethod() {
        return method;
    }

    public Proxy getProxy() {
        return proxy;
    }

    public HttpURLConnection getConnection() {
        return connection;
    }


    public Response execute() throws IOException {
        return new Response(prepare());
    }
}
