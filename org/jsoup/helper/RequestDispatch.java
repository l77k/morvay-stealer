/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package org.jsoup.helper;

import java.lang.reflect.Constructor;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.RequestExecutor;
import org.jsoup.helper.UrlConnectionExecutor;
import org.jspecify.annotations.Nullable;

class RequestDispatch {
    static @Nullable Constructor<RequestExecutor> clientConstructor;

    RequestDispatch() {
    }

    static RequestExecutor get(HttpConnection.Request request, @Nullable HttpConnection.Response previousResponse) {
        if (Boolean.getBoolean("jsoup.useHttpClient") && clientConstructor != null) {
            try {
                return clientConstructor.newInstance(request, previousResponse);
            }
            catch (Exception e2) {
                return new UrlConnectionExecutor(request, previousResponse);
            }
        }
        return new UrlConnectionExecutor(request, previousResponse);
    }

    static {
        try {
            Class<?> httpClass = Class.forName("org.jsoup.helper.HttpClientExecutor");
            clientConstructor = httpClass.getConstructor(HttpConnection.Request.class, HttpConnection.Response.class);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }
}

