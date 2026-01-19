/*
 * Decompiled with CFR 0.152.
 */
package org.jsoup;

@FunctionalInterface
public interface Progress<ProgressContext> {
    public void onProgress(int var1, int var2, float var3, ProgressContext var4);
}

