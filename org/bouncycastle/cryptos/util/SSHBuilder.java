/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import org.bouncycastle.util.Strings;

class SSHBuilder {
    private final ByteArrayOutputStream bos = new ByteArrayOutputStream();

    SSHBuilder() {
    }

    public void u32(int n2) {
        this.bos.write(n2 >>> 24 & 0xFF);
        this.bos.write(n2 >>> 16 & 0xFF);
        this.bos.write(n2 >>> 8 & 0xFF);
        this.bos.write(n2 & 0xFF);
    }

    public void writeBigNum(BigInteger bigInteger) {
        this.writeBlock(bigInteger.toByteArray());
    }

    public void writeBlock(byte[] byArray) {
        this.u32(byArray.length);
        try {
            this.bos.write(byArray);
        }
        catch (IOException iOException) {
            throw new IllegalStateException(iOException.getMessage(), iOException);
        }
    }

    public void writeBytes(byte[] byArray) {
        try {
            this.bos.write(byArray);
        }
        catch (IOException iOException) {
            throw new IllegalStateException(iOException.getMessage(), iOException);
        }
    }

    public void writeString(String string) {
        this.writeBlock(Strings.toByteArray(string));
    }

    public byte[] getBytes() {
        return this.bos.toByteArray();
    }

    public byte[] getPaddedBytes() {
        return this.getPaddedBytes(8);
    }

    public byte[] getPaddedBytes(int n2) {
        int n3 = this.bos.size() % n2;
        if (0 != n3) {
            int n4 = n2 - n3;
            for (int i2 = 1; i2 <= n4; ++i2) {
                this.bos.write(i2);
            }
        }
        return this.bos.toByteArray();
    }
}

