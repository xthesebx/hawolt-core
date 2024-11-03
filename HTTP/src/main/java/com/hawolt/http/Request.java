package com.hawolt.http;


import com.hawolt.http.misc.DownloadCallback;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Request class.</p>
 *
 * @author Hawolt
 * @version 1.1
 */
public class Request {
    private final Map<String, String> headers = new HashMap<>();
    private HttpURLConnection connection;
    private final String endpoint;
    private final boolean output;
    private final Method method;
    private final Proxy proxy;
    private Object body;

    /**
     * <p>Constructor for Request.</p>
     *
     * @param endpoint a {@link java.lang.String} object
     */
    public Request(String endpoint) {
        this(endpoint, null);
    }

    /**
     * <p>Constructor for Request.</p>
     *
     * @param endpoint a {@link java.lang.String} object
     * @param proxy a {@link java.net.Proxy} object
     */
    public Request(String endpoint, Proxy proxy) {
        this(endpoint, proxy, Method.GET, false);
    }

    /**
     * <p>Constructor for Request.</p>
     *
     * @param endpoint a {@link java.lang.String} object
     * @param method a {@link com.hawolt.http.Method} object
     * @param output a boolean
     */
    public Request(String endpoint, Method method, boolean output) {
        this(endpoint, null, method, output);
    }

    /**
     * <p>Constructor for Request.</p>
     *
     * @param endpoint a {@link java.lang.String} object
     * @param proxy a {@link java.net.Proxy} object
     * @param method a {@link com.hawolt.http.Method} object
     * @param output a boolean
     */
    public Request(String endpoint, Proxy proxy, Method method, boolean output) {
        this.endpoint = endpoint;
        this.method = method;
        this.output = output;
        this.proxy = proxy;
    }

    private Request prepare() throws IOException {
        connection = BasicHttp.open(endpoint, proxy);
        connection.setRequestMethod(method.name());
        for (String header : headers.keySet()) {
            connection.addRequestProperty(header, headers.get(header));
        }
        connection.setDoOutput(output);
        if (output) {
            try (OutputStream out = connection.getOutputStream()) {
                out.write(body.toString().getBytes());
                out.flush();
            }
        }
        return this;
    }

    /**
     * <p>addHeader.</p>
     *
     * @param key a {@link java.lang.String} object
     * @param value a {@link java.lang.String} object
     */
    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    /**
     * <p>write.</p>
     *
     * @param output a {@link java.lang.Object} object
     */
    public void write(Object output) {
        this.body = output;
    }

    /**
     * <p>Getter for the field <code>body</code>.</p>
     *
     * @return a {@link java.lang.Object} object
     */
    public Object getBody() {
        return body;
    }

    /**
     * <p>Getter for the field <code>headers</code>.</p>
     *
     * @return a {@link java.util.Map} object
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * <p>Getter for the field <code>endpoint</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getEndpoint() {
        return endpoint;
    }

    /**
     * <p>isOutput.</p>
     *
     * @return a boolean
     */
    public boolean isOutput() {
        return output;
    }

    /**
     * <p>Getter for the field <code>method</code>.</p>
     *
     * @return a {@link com.hawolt.http.Method} object
     */
    public Method getMethod() {
        return method;
    }

    /**
     * <p>Getter for the field <code>proxy</code>.</p>
     *
     * @return a {@link java.net.Proxy} object
     */
    public Proxy getProxy() {
        return proxy;
    }

    /**
     * <p>Getter for the field <code>connection</code>.</p>
     *
     * @return a {@link java.net.HttpURLConnection} object
     */
    public HttpURLConnection getConnection() {
        return connection;
    }

    /**
     * <p>execute.</p>
     *
     * @return a {@link com.hawolt.http.Response} object
     * @throws java.io.IOException if any.
     */
    public Response execute() throws IOException {
        return new Response(prepare());
    }

    /**
     * <p>execute.</p>
     *
     * @param callback a {@link com.hawolt.http.misc.DownloadCallback} object
     * @return a {@link com.hawolt.http.Response} object
     * @throws java.io.IOException if any.
     */
    public Response execute(DownloadCallback callback) throws IOException {
        return new Response(prepare(), callback);
    }
}
