/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package org.jsoup.helper;

import java.lang.reflect.Constructor;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import org.jsoup.helper.RequestAuthenticator;
import org.jspecify.annotations.Nullable;

class AuthenticationHandler
extends Authenticator {
    static final int MaxAttempts = 3;
    static AuthShim handler;
    @Nullable RequestAuthenticator auth;
    int attemptCount = 0;

    AuthenticationHandler() {
    }

    AuthenticationHandler(RequestAuthenticator auth) {
        this.auth = auth;
    }

    @Override
    public final @Nullable PasswordAuthentication getPasswordAuthentication() {
        AuthenticationHandler delegate = handler.get(this);
        if (delegate == null) {
            return null;
        }
        ++delegate.attemptCount;
        if (delegate.attemptCount > 3) {
            return null;
        }
        if (delegate.auth == null) {
            return null;
        }
        RequestAuthenticator.Context ctx = new RequestAuthenticator.Context(this.getRequestingURL(), this.getRequestorType(), this.getRequestingPrompt());
        return delegate.auth.authenticate(ctx);
    }

    static {
        try {
            Class<?> perRequestClass = Class.forName("org.jsoup.helper.RequestAuthHandler");
            Constructor<?> constructor = perRequestClass.getConstructor(new Class[0]);
            handler = (AuthShim)constructor.newInstance(new Object[0]);
        }
        catch (ClassNotFoundException e2) {
            handler = new GlobalHandler();
        }
        catch (Exception e3) {
            throw new IllegalStateException(e3);
        }
    }

    static interface AuthShim {
        public void enable(RequestAuthenticator var1, Object var2);

        public void remove();

        public @Nullable AuthenticationHandler get(AuthenticationHandler var1);
    }

    static class GlobalHandler
    implements AuthShim {
        static ThreadLocal<AuthenticationHandler> authenticators = new ThreadLocal();

        GlobalHandler() {
        }

        @Override
        public void enable(RequestAuthenticator auth, Object ignored) {
            authenticators.set(new AuthenticationHandler(auth));
        }

        @Override
        public void remove() {
            authenticators.remove();
        }

        @Override
        public AuthenticationHandler get(AuthenticationHandler helper) {
            return authenticators.get();
        }

        static {
            Authenticator.setDefault(new AuthenticationHandler());
        }
    }
}

