/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package org.jsoup.helper;

import java.io.IOException;
import java.io.InputStream;
import org.jsoup.helper.HttpConnection;
import org.jspecify.annotations.Nullable;

abstract class RequestExecutor {
    final HttpConnection.Request req;
    final @Nullable HttpConnection.Response prevRes;

    RequestExecutor(HttpConnection.Request request, @Nullable HttpConnection.Response previousResponse) {
        this.req = request;
        this.prevRes = previousResponse;
    }

    abstract HttpConnection.Response execute() throws IOException;

    abstract InputStream responseBody() throws IOException;

    abstract void safeClose();
}

