/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jsoup.helper.HttpClientExecutor
 *  org.jsoup.helper.HttpClientExecutor$ProxyWrap
 *  org.jspecify.annotations.Nullable
 */
package org.jsoup.helper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import org.jsoup.Connection;
import org.jsoup.helper.AuthenticationHandler;
import org.jsoup.helper.CookieUtil;
import org.jsoup.helper.HttpClientExecutor;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.RequestAuthenticator;
import org.jsoup.helper.RequestExecutor;
import org.jspecify.annotations.Nullable;

/*
 * Exception performing whole class analysis ignored.
 */
class HttpClientExecutor
extends RequestExecutor {
    static ThreadLocal<Proxy> perRequestProxy = new ThreadLocal();
    @Nullable HttpResponse<InputStream> hRes;

    public HttpClientExecutor(HttpConnection.Request request,  @Nullable HttpConnection.Response previousResponse) {
        super(request, previousResponse);
    }

    HttpClient client() {
        RequestAuthenticator prevAuth = this.req.connection.lastAuth;
        this.req.connection.lastAuth = this.req.authenticator;
        if (this.req.connection.client != null && prevAuth == this.req.authenticator) {
            return (HttpClient)this.req.connection.client;
        }
        HttpClient.Builder builder = HttpClient.newBuilder();
        builder.followRedirects(HttpClient.Redirect.NEVER);
        builder.proxy((ProxySelector)new ProxyWrap());
        if (this.req.authenticator != null) {
            builder.authenticator(new AuthenticationHandler(this.req.authenticator));
        }
        HttpClient client = builder.build();
        this.req.connection.client = client;
        return client;
    }

    @Override
    HttpConnection.Response execute() throws IOException {
        try {
            HttpRequest.Builder reqBuilder = HttpRequest.newBuilder(this.req.url.toURI()).method(this.req.method.name(), HttpClientExecutor.requestBody((HttpConnection.Request)this.req));
            if (this.req.timeout() > 0) {
                reqBuilder.timeout(Duration.ofMillis(this.req.timeout()));
            }
            CookieUtil.applyCookiesToRequest(this.req, reqBuilder::header);
            this.req.multiHeaders().forEach((key, values2) -> values2.forEach(value -> reqBuilder.header((String)key, (String)value)));
            if (this.req.proxy() != null) {
                perRequestProxy.set(this.req.proxy());
            }
            HttpRequest hReq = reqBuilder.build();
            HttpClient client = this.client();
            this.hRes = client.send(hReq, HttpResponse.BodyHandlers.ofInputStream());
            HttpHeaders headers = this.hRes.headers();
            HttpConnection.Response res = new HttpConnection.Response(this.req);
            res.executor = this;
            res.method = Connection.Method.valueOf(this.hRes.request().method());
            res.url = this.hRes.uri().toURL();
            res.statusCode = this.hRes.statusCode();
            res.contentType = headers.firstValue("content-type").orElse("");
            long length = headers.firstValueAsLong("content-length").orElse(-1L);
            res.contentLength = length < Integer.MAX_VALUE ? (int)length : -1;
            res.prepareResponse(headers.map(), this.prevRes);
            HttpConnection.Response response = res;
            return response;
        }
        catch (IOException e2) {
            this.safeClose();
            throw e2;
        }
        catch (InterruptedException e3) {
            this.safeClose();
            Thread.currentThread().interrupt();
            throw new IOException(e3);
        }
        catch (URISyntaxException e4) {
            throw new IllegalArgumentException("Malformed URL: " + String.valueOf(this.req.url), e4);
        }
        finally {
            perRequestProxy.remove();
        }
    }

    @Override
    InputStream responseBody() throws IOException {
        if (this.hRes == null) {
            throw new IllegalStateException("Not yet executed");
        }
        return (InputStream)this.hRes.body();
    }

    @Override
    void safeClose() {
        if (this.hRes != null) {
            InputStream body = (InputStream)this.hRes.body();
            if (body != null) {
                try {
                    body.close();
                }
                catch (IOException iOException) {
                    // empty catch block
                }
            }
            this.hRes = null;
        }
    }

    static HttpRequest.BodyPublisher requestBody(HttpConnection.Request req) throws IOException {
        if (req.method.hasBody()) {
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            HttpConnection.Response.writePost(req, buf);
            return HttpRequest.BodyPublishers.ofByteArray(buf.toByteArray());
        }
        return HttpRequest.BodyPublishers.noBody();
    }
}

