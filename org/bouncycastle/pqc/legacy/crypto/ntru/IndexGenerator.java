/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.legacy.crypto.ntru;

import org.bouncycastle.crypto.Digest;
import org.bouncycastle.pqc.legacy.crypto.ntru.NTRUEncryptionParameters;
import org.bouncycastle.util.Arrays;

public class IndexGenerator {
    private byte[] seed;
    private int N;
    private int c;
    private int minCallsR;
    private int totLen;
    private int remLen;
    private BitString buf;
    private int counter;
    private boolean initialized;
    private Digest hashAlg;
    private int hLen;

    IndexGenerator(byte[] byArray, NTRUEncryptionParameters nTRUEncryptionParameters) {
        this.seed = byArray;
        this.N = nTRUEncryptionParameters.N;
        this.c = nTRUEncryptionParameters.c;
        this.minCallsR = nTRUEncryptionParameters.minCallsR;
        this.totLen = 0;
        this.remLen = 0;
        this.counter = 0;
        this.hashAlg = nTRUEncryptionParameters.hashAlg;
        this.hLen = this.hashAlg.getDigestSize();
        this.initialized = false;
    }

    int nextIndex() {
        int n2;
        Object object;
        if (!this.initialized) {
            this.buf = new BitString();
            object = new byte[this.hashAlg.getDigestSize()];
            while (this.counter < this.minCallsR) {
                this.appendHash(this.buf, (byte[])object);
                ++this.counter;
            }
            this.remLen = this.totLen = this.minCallsR * 8 * this.hLen;
            this.initialized = true;
        }
        do {
            this.totLen += this.c;
            object = this.buf.getTrailing(this.remLen);
            if (this.remLen < this.c) {
                n2 = this.c - this.remLen;
                int n3 = this.counter + (n2 + this.hLen - 1) / this.hLen;
                byte[] byArray = new byte[this.hashAlg.getDigestSize()];
                while (this.counter < n3) {
                    this.appendHash((BitString)object, byArray);
                    ++this.counter;
                    if (n2 <= 8 * this.hLen) continue;
                    n2 -= 8 * this.hLen;
                }
                this.remLen = 8 * this.hLen - n2;
                this.buf = new BitString();
                this.buf.appendBits(byArray);
                continue;
            }
            this.remLen -= this.c;
        } while ((n2 = ((BitString)object).getLeadingAsInt(this.c)) >= (1 << this.c) - (1 << this.c) % this.N);
        return n2 % this.N;
    }

    private void appendHash(BitString bitString, byte[] byArray) {
        this.hashAlg.update(this.seed, 0, this.seed.length);
        this.putInt(this.hashAlg, this.counter);
        this.hashAlg.doFinal(byArray, 0);
        bitString.appendBits(byArray);
    }

    private void putInt(Digest digest, int n2) {
        digest.update((byte)(n2 >> 24));
        digest.update((byte)(n2 >> 16));
        digest.update((byte)(n2 >> 8));
        digest.update((byte)n2);
    }

    private static byte[] copyOf(byte[] byArray, int n2) {
        byte[] byArray2 = new byte[n2];
        System.arraycopy(byArray, 0, byArray2, 0, n2 < byArray.length ? n2 : byArray.length);
        return byArray2;
    }

    public static class BitString {
        byte[] bytes = new byte[4];
        int numBytes;
        int lastByteBits;

        void appendBits(byte[] byArray) {
            for (int i2 = 0; i2 != byArray.length; ++i2) {
                this.appendBits(byArray[i2]);
            }
        }

        public void appendBits(byte by) {
            if (this.numBytes == this.bytes.length) {
                this.bytes = IndexGenerator.copyOf(this.bytes, 2 * this.bytes.length);
            }
            if (this.numBytes == 0) {
                this.numBytes = 1;
                this.bytes[0] = by;
                this.lastByteBits = 8;
            } else if (this.lastByteBits == 8) {
                this.bytes[this.numBytes++] = by;
            } else {
                int n2 = 8 - this.lastByteBits;
                int n3 = this.numBytes - 1;
                this.bytes[n3] = (byte)(this.bytes[n3] | (by & 0xFF) << this.lastByteBits);
                this.bytes[this.numBytes++] = (byte)((by & 0xFF) >> n2);
            }
        }

        public BitString getTrailing(int n2) {
            int n3;
            BitString bitString = new BitString();
            bitString.numBytes = (n2 + 7) / 8;
            bitString.bytes = new byte[bitString.numBytes];
            for (n3 = 0; n3 < bitString.numBytes; ++n3) {
                bitString.bytes[n3] = this.bytes[n3];
            }
            bitString.lastByteBits = n2 % 8;
            if (bitString.lastByteBits == 0) {
                bitString.lastByteBits = 8;
            } else {
                n3 = 32 - bitString.lastByteBits;
                bitString.bytes[bitString.numBytes - 1] = (byte)(bitString.bytes[bitString.numBytes - 1] << n3 >>> n3);
            }
            return bitString;
        }

        public int getLeadingAsInt(int n2) {
            int n3 = (this.numBytes - 1) * 8 + this.lastByteBits - n2;
            int n4 = n3 / 8;
            int n5 = n3 % 8;
            int n6 = (this.bytes[n4] & 0xFF) >>> n5;
            int n7 = 8 - n5;
            for (int i2 = n4 + 1; i2 < this.numBytes; ++i2) {
                n6 |= (this.bytes[i2] & 0xFF) << n7;
                n7 += 8;
            }
            return n6;
        }

        public byte[] getBytes() {
            return Arrays.clone(this.bytes);
        }
    }
}

