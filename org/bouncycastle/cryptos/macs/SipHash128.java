/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.macs;

import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.macs.SipHash;
import org.bouncycastle.util.Pack;

public class SipHash128
extends SipHash {
    public SipHash128() {
    }

    public SipHash128(int n2, int n3) {
        super(n2, n3);
    }

    @Override
    public String getAlgorithmName() {
        return "SipHash128-" + this.c + "-" + this.d;
    }

    @Override
    public int getMacSize() {
        return 16;
    }

    @Override
    public long doFinal() throws DataLengthException, IllegalStateException {
        throw new UnsupportedOperationException("doFinal() is not supported");
    }

    @Override
    public int doFinal(byte[] byArray, int n2) throws DataLengthException, IllegalStateException {
        this.m >>>= 7 - this.wordPos << 3;
        this.m >>>= 8;
        this.m |= ((long)((this.wordCount << 3) + this.wordPos) & 0xFFL) << 56;
        this.processMessageWord();
        this.v2 ^= 0xEEL;
        this.applySipRounds(this.d);
        long l2 = this.v0 ^ this.v1 ^ this.v2 ^ this.v3;
        this.v1 ^= 0xDDL;
        this.applySipRounds(this.d);
        long l3 = this.v0 ^ this.v1 ^ this.v2 ^ this.v3;
        this.reset();
        Pack.longToLittleEndian(l2, byArray, n2);
        Pack.longToLittleEndian(l3, byArray, n2 + 8);
        return 16;
    }

    @Override
    public void reset() {
        super.reset();
        this.v1 ^= 0xEEL;
    }
}

