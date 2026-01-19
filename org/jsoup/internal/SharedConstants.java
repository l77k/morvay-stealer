/*
 * Decompiled with CFR 0.152.
 */
package org.jsoup.internal;

public final class SharedConstants {
    public static final String UserDataKey = "/jsoup.userdata";
    public static final String AttrRangeKey = "jsoup.attrs";
    public static final String RangeKey = "jsoup.start";
    public static final String EndRangeKey = "jsoup.end";
    public static final int DefaultBufferSize = 8192;
    public static final String[] FormSubmitTags = new String[]{"input", "keygen", "object", "select", "textarea"};
    public static final String DummyUri = "https://dummy.example/";
    public static final String UseHttpClient = "jsoup.useHttpClient";

    private SharedConstants() {
    }
}

