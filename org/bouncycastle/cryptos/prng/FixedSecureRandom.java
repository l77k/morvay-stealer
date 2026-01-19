/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.prng;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.SecureRandom;

public class FixedSecureRandom
extends SecureRandom {
    private byte[] _data;
    private int _index;
    private int _intPad;

    public FixedSecureRandom(byte[] byArray) {
        this(false, new byte[][]{byArray});
    }

    public FixedSecureRandom(byte[][] byArray) {
        this(false, byArray);
    }

    public FixedSecureRandom(boolean bl, byte[] byArray) {
        this(bl, new byte[][]{byArray});
    }

    public FixedSecureRandom(boolean bl, byte[][] byArray) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        for (int i2 = 0; i2 != byArray.length; ++i2) {
            try {
                byteArrayOutputStream.write(byArray[i2]);
                continue;
            }
            catch (IOException iOException) {
                throw new IllegalArgumentException("can't save value array.");
            }
        }
        this._data = byteArrayOutputStream.toByteArray();
        if (bl) {
            this._intPad = this._data.length % 4;
        }
    }

    @Override
    public void nextBytes(byte[] byArray) {
        System.arraycopy(this._data, this._index, byArray, 0, byArray.length);
        this._index += byArray.length;
    }

    @Override
    public byte[] generateSeed(int n2) {
        byte[] byArray = new byte[n2];
        this.nextBytes(byArray);
        return byArray;
    }

    @Override
    public int nextInt() {
        int n2 = 0;
        n2 |= this.nextValue() << 24;
        n2 |= this.nextValue() << 16;
        if (this._intPad == 2) {
            --this._intPad;
        } else {
            n2 |= this.nextValue() << 8;
        }
        if (this._intPad == 1) {
            --this._intPad;
        } else {
            n2 |= this.nextValue();
        }
        return n2;
    }

    @Override
    public long nextLong() {
        long l2 = 0L;
        l2 |= (long)this.nextValue() << 56;
        l2 |= (long)this.nextValue() << 48;
        l2 |= (long)this.nextValue() << 40;
        l2 |= (long)this.nextValue() << 32;
        l2 |= (long)this.nextValue() << 24;
        l2 |= (long)this.nextValue() << 16;
        l2 |= (long)this.nextValue() << 8;
        return l2 |= (long)this.nextValue();
    }

    public boolean isExhausted() {
        return this._index == this._data.length;
    }

    private int nextValue() {
        return this._data[this._index++] & 0xFF;
    }
}

