/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.bouncycastle.crypto.Mac;

public class MacInputStream
extends FilterInputStream {
    protected Mac mac;

    public MacInputStream(InputStream inputStream2, Mac mac) {
        super(inputStream2);
        this.mac = mac;
    }

    @Override
    public int read() throws IOException {
        int n2 = this.in.read();
        if (n2 >= 0) {
            this.mac.update((byte)n2);
        }
        return n2;
    }

    @Override
    public int read(byte[] byArray, int n2, int n3) throws IOException {
        int n4 = this.in.read(byArray, n2, n3);
        if (n4 >= 0) {
            this.mac.update(byArray, n2, n4);
        }
        return n4;
    }

    public Mac getMac() {
        return this.mac;
    }
}

