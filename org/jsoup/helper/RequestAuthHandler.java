/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jsoup.helper.RequestAuthHandler
 */
package org.jsoup.helper;

import java.net.HttpURLConnection;
import java.net.http.HttpClient;
import org.jsoup.helper.AuthenticationHandler;
import org.jsoup.helper.RequestAuthenticator;

class RequestAuthHandler
implements AuthenticationHandler.AuthShim {
    @Override
    public void enable(RequestAuthenticator auth, Object connOrHttp) {
        AuthenticationHandler authenticator = new AuthenticationHandler(auth);
        if (connOrHttp instanceof HttpURLConnection) {
            HttpURLConnection conn = (HttpURLConnection)connOrHttp;
            conn.setAuthenticator(authenticator);
        } else if (connOrHttp instanceof HttpClient.Builder) {
            HttpClient.Builder builder = (HttpClient.Builder)connOrHttp;
            builder.authenticator(authenticator);
        } else {
            throw new IllegalArgumentException("Unsupported executor: " + connOrHttp.getClass().getName());
        }
    }

    @Override
    public void remove() {
    }

    @Override
    public AuthenticationHandler get(AuthenticationHandler helper) {
        return helper;
    }
}

