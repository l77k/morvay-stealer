/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package org.jsoup.helper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;
import org.jsoup.Connection;
import org.jsoup.helper.AuthenticationHandler;
import org.jsoup.helper.CookieUtil;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.RequestExecutor;
import org.jsoup.internal.Functions;
import org.jspecify.annotations.Nullable;

class UrlConnectionExecutor
extends RequestExecutor {
    @Nullable HttpURLConnection conn;

    UrlConnectionExecutor(HttpConnection.Request req, @Nullable HttpConnection.Response prevRes) {
        super(req, prevRes);
    }

    @Override
    HttpConnection.Response execute() throws IOException {
        try {
            this.conn = UrlConnectionExecutor.createConnection(this.req);
            this.conn.connect();
            if (this.conn.getDoOutput()) {
                try (OutputStream out = this.conn.getOutputStream();){
                    HttpConnection.Response.writePost(this.req, out);
                }
                catch (IOException e2) {
                    this.conn.disconnect();
                    throw e2;
                }
            }
            HttpConnection.Response res = new HttpConnection.Response(this.req);
            res.executor = this;
            res.method = Connection.Method.valueOf(this.conn.getRequestMethod());
            res.url = this.conn.getURL();
            res.statusCode = this.conn.getResponseCode();
            res.statusMessage = this.conn.getResponseMessage();
            res.contentType = this.conn.getContentType();
            res.contentLength = this.conn.getContentLength();
            LinkedHashMap<String, List<String>> resHeaders = UrlConnectionExecutor.createHeaderMap(this.conn);
            res.prepareResponse(resHeaders, this.prevRes);
            return res;
        }
        catch (IOException e3) {
            this.safeClose();
            throw e3;
        }
    }

    @Override
    InputStream responseBody() throws IOException {
        if (this.conn == null) {
            throw new IllegalStateException("Not yet executed");
        }
        return this.conn.getErrorStream() != null ? this.conn.getErrorStream() : this.conn.getInputStream();
    }

    @Override
    void safeClose() {
        if (this.conn != null) {
            this.conn.disconnect();
            this.conn = null;
        }
    }

    private static HttpURLConnection createConnection(HttpConnection.Request req) throws IOException {
        Proxy proxy = req.proxy();
        HttpURLConnection conn = (HttpURLConnection)(proxy == null ? req.url().openConnection() : req.url().openConnection(proxy));
        conn.setRequestMethod(req.method().name());
        conn.setInstanceFollowRedirects(false);
        conn.setConnectTimeout(req.timeout());
        conn.setReadTimeout(req.timeout() / 2);
        if (req.sslSocketFactory() != null && conn instanceof HttpsURLConnection) {
            ((HttpsURLConnection)conn).setSSLSocketFactory(req.sslSocketFactory());
        }
        if (req.authenticator != null) {
            AuthenticationHandler.handler.enable(req.authenticator, conn);
        }
        if (req.method().hasBody()) {
            conn.setDoOutput(true);
        }
        CookieUtil.applyCookiesToRequest(req, conn::addRequestProperty);
        for (Map.Entry header : req.multiHeaders().entrySet()) {
            for (String value : (List)header.getValue()) {
                conn.addRequestProperty((String)header.getKey(), value);
            }
        }
        return conn;
    }

    private static LinkedHashMap<String, List<String>> createHeaderMap(HttpURLConnection conn) {
        LinkedHashMap<String, List<String>> headers = new LinkedHashMap<String, List<String>>();
        int i2 = 0;
        while (true) {
            String key = conn.getHeaderFieldKey(i2);
            String val = conn.getHeaderField(i2);
            if (key == null && val == null) break;
            ++i2;
            if (key == null || val == null) continue;
            List vals = headers.computeIfAbsent(key, Functions.listFunction());
            vals.add(val);
        }
        return headers;
    }
}

