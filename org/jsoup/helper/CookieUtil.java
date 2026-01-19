/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package org.jsoup.helper;

import java.io.IOException;
import java.net.CookieManager;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import org.jsoup.Connection;
import org.jsoup.helper.HttpConnection;
import org.jsoup.internal.StringUtil;
import org.jsoup.parser.TokenQueue;
import org.jspecify.annotations.Nullable;

class CookieUtil {
    private static final Map<String, List<String>> EmptyRequestHeaders = Collections.unmodifiableMap(new HashMap());
    private static final String Sep = "; ";
    private static final String CookieName = "Cookie";
    private static final String Cookie2Name = "Cookie2";

    CookieUtil() {
    }

    static void applyCookiesToRequest(HttpConnection.Request req, BiConsumer<String, String> setter) throws IOException {
        LinkedHashSet<String> cookieSet = CookieUtil.requestCookieSet(req);
        HashSet cookies2 = null;
        Map<String, List<String>> storedCookies = req.cookieManager().get(CookieUtil.asUri(req.url), EmptyRequestHeaders);
        for (Map.Entry<String, List<String>> entry : storedCookies.entrySet()) {
            HashSet set;
            List<String> cookies = entry.getValue();
            if (cookies == null || cookies.size() == 0) continue;
            String key = entry.getKey();
            if (CookieName.equals(key)) {
                set = cookieSet;
            } else {
                if (!Cookie2Name.equals(key)) continue;
                cookies2 = set = new HashSet();
            }
            set.addAll(cookies);
        }
        if (cookieSet.size() > 0) {
            setter.accept(CookieName, StringUtil.join(cookieSet, Sep));
        }
        if (cookies2 != null && cookies2.size() > 0) {
            setter.accept(Cookie2Name, StringUtil.join(cookies2, Sep));
        }
    }

    private static LinkedHashSet<String> requestCookieSet(Connection.Request req) {
        LinkedHashSet<String> set = new LinkedHashSet<String>();
        for (Map.Entry<String, String> cookie : req.cookies().entrySet()) {
            set.add(cookie.getKey() + "=" + cookie.getValue());
        }
        return set;
    }

    static URI asUri(URL url) throws IOException {
        try {
            return url.toURI();
        }
        catch (URISyntaxException e2) {
            MalformedURLException ue = new MalformedURLException(e2.getMessage());
            ue.initCause(e2);
            throw ue;
        }
    }

    static void storeCookies(HttpConnection.Request req, HttpConnection.Response res, URL url, Map<String, List<String>> resHeaders) throws IOException {
        CookieManager manager = req.cookieManager();
        URI uri = CookieUtil.asUri(url);
        manager.put(uri, resHeaders);
        for (Map.Entry<String, List<String>> entry : resHeaders.entrySet()) {
            String name = entry.getKey();
            List<String> values2 = entry.getValue();
            if (!name.equalsIgnoreCase("Set-Cookie")) continue;
            for (String value : values2) {
                CookieUtil.parseCookie(value, res);
            }
        }
    }

    static void parseCookie(@Nullable String value, HttpConnection.Response res) {
        if (value == null) {
            return;
        }
        TokenQueue cd = new TokenQueue(value);
        String cookieName = cd.chompTo("=").trim();
        String cookieVal = cd.consumeTo(";").trim();
        if (!cookieName.isEmpty()) {
            res.cookie(cookieName, cookieVal);
        }
    }
}

