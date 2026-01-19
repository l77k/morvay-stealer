/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.util.encoders;

import org.bouncycastle.util.encoders.Translator;

public class BufferedEncoder {
    protected byte[] buf;
    protected int bufOff;
    protected Translator translator;

    public BufferedEncoder(Translator translator, int n2) {
        this.translator = translator;
        if (n2 % translator.getEncodedBlockSize() != 0) {
            throw new IllegalArgumentException("buffer size not multiple of input block size");
        }
        this.buf = new byte[n2];
        this.bufOff = 0;
    }

    public int processByte(byte by, byte[] byArray, int n2) {
        int n3 = 0;
        this.buf[this.bufOff++] = by;
        if (this.bufOff == this.buf.length) {
            n3 = this.translator.encode(this.buf, 0, this.buf.length, byArray, n2);
            this.bufOff = 0;
        }
        return n3;
    }

    public int processBytes(byte[] byArray, int n2, int n3, byte[] byArray2, int n4) {
        if (n3 < 0) {
            throw new IllegalArgumentException("Can't have a negative input length!");
        }
        int n5 = 0;
        int n6 = this.buf.length - this.bufOff;
        if (n3 > n6) {
            System.arraycopy(byArray, n2, this.buf, this.bufOff, n6);
            n5 += this.translator.encode(this.buf, 0, this.buf.length, byArray2, n4);
            this.bufOff = 0;
            n4 += n5;
            int n7 = (n3 -= n6) - n3 % this.buf.length;
            n5 += this.translator.encode(byArray, n2 += n6, n7, byArray2, n4);
            n3 -= n7;
            n2 += n7;
        }
        if (n3 != 0) {
            System.arraycopy(byArray, n2, this.buf, this.bufOff, n3);
            this.bufOff += n3;
        }
        return n5;
    }
}

