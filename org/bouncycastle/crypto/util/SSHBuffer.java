/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.util;

import java.math.BigInteger;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Strings;

class SSHBuffer {
    private final byte[] buffer;
    private int pos = 0;

    public SSHBuffer(byte[] byArray, byte[] byArray2) {
        this.buffer = byArray2;
        for (int i2 = 0; i2 != byArray.length; ++i2) {
            if (byArray[i2] == byArray2[i2]) continue;
            throw new IllegalArgumentException("magic-number incorrect");
        }
        this.pos += byArray.length;
    }

    public SSHBuffer(byte[] byArray) {
        this.buffer = byArray;
    }

    public int readU32() {
        if (this.pos > this.buffer.length - 4) {
            throw new IllegalArgumentException("4 bytes for U32 exceeds buffer.");
        }
        int n2 = (this.buffer[this.pos++] & 0xFF) << 24;
        n2 |= (this.buffer[this.pos++] & 0xFF) << 16;
        n2 |= (this.buffer[this.pos++] & 0xFF) << 8;
        return n2 |= this.buffer[this.pos++] & 0xFF;
    }

    public String readString() {
        return Strings.fromByteArray(this.readBlock());
    }

    public byte[] readBlock() {
        int n2 = this.readU32();
        if (n2 == 0) {
            return new byte[0];
        }
        if (this.pos > this.buffer.length - n2) {
            throw new IllegalArgumentException("not enough data for block");
        }
        int n3 = this.pos;
        this.pos += n2;
        return Arrays.copyOfRange(this.buffer, n3, this.pos);
    }

    public void skipBlock() {
        int n2 = this.readU32();
        if (this.pos > this.buffer.length - n2) {
            throw new IllegalArgumentException("not enough data for block");
        }
        this.pos += n2;
    }

    public byte[] readPaddedBlock() {
        return this.readPaddedBlock(8);
    }

    public byte[] readPaddedBlock(int n2) {
        int n3;
        int n4 = this.readU32();
        if (n4 == 0) {
            return new byte[0];
        }
        if (this.pos > this.buffer.length - n4) {
            throw new IllegalArgumentException("not enough data for block");
        }
        int n5 = n4 % n2;
        if (0 != n5) {
            throw new IllegalArgumentException("missing padding");
        }
        int n6 = this.pos;
        this.pos += n4;
        int n7 = this.pos;
        if (n4 > 0 && 0 < (n3 = this.buffer[this.pos - 1] & 0xFF) && n3 < n2) {
            int n8 = n3;
            int n9 = 1;
            int n10 = n7 -= n8;
            while (n9 <= n8) {
                if (n9 != (this.buffer[n10] & 0xFF)) {
                    throw new IllegalArgumentException("incorrect padding");
                }
                ++n9;
                ++n10;
            }
        }
        return Arrays.copyOfRange(this.buffer, n6, n7);
    }

    public BigInteger readBigNumPositive() {
        int n2 = this.readU32();
        if (this.pos + n2 > this.buffer.length) {
            throw new IllegalArgumentException("not enough data for big num");
        }
        int n3 = this.pos;
        this.pos += n2;
        byte[] byArray = Arrays.copyOfRange(this.buffer, n3, this.pos);
        return new BigInteger(1, byArray);
    }

    public byte[] getBuffer() {
        return Arrays.clone(this.buffer);
    }

    public boolean hasRemaining() {
        return this.pos < this.buffer.length;
    }
}

