/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.jcajce.io;

import java.io.IOException;
import java.io.OutputStream;
import javax.crypto.Mac;

public final class MacOutputStream
extends OutputStream {
    private Mac mac;

    public MacOutputStream(Mac mac) {
        this.mac = mac;
    }

    @Override
    public void write(int n2) throws IOException {
        this.mac.update((byte)n2);
    }

    @Override
    public void write(byte[] byArray, int n2, int n3) throws IOException {
        this.mac.update(byArray, n2, n3);
    }

    public byte[] getMac() {
        return this.mac.doFinal();
    }
}

