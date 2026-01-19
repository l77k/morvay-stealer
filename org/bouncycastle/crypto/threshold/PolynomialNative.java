/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.threshold;

import org.bouncycastle.crypto.threshold.Polynomial;
import org.bouncycastle.crypto.threshold.ShamirSecretSplitter;

class PolynomialNative
extends Polynomial {
    private final int IRREDUCIBLE;

    public PolynomialNative(ShamirSecretSplitter.Algorithm algorithm) {
        switch (algorithm) {
            case AES: {
                this.IRREDUCIBLE = 283;
                break;
            }
            case RSA: {
                this.IRREDUCIBLE = 285;
                break;
            }
            default: {
                throw new IllegalArgumentException("The algorithm is not correct");
            }
        }
    }

    @Override
    protected byte gfMul(int n2, int n3) {
        int n4 = 0;
        while (n3 > 0) {
            if ((n3 & 1) != 0) {
                n4 ^= n2;
            }
            if (((n2 <<= 1) & 0x100) != 0) {
                n2 ^= this.IRREDUCIBLE;
            }
            n3 >>= 1;
        }
        while (n4 >= 256) {
            if ((n4 & 0x100) != 0) {
                n4 ^= this.IRREDUCIBLE;
            }
            n4 <<= 1;
        }
        return (byte)(n4 & 0xFF);
    }

    @Override
    protected byte gfDiv(int n2, int n3) {
        return this.gfMul(n2, this.gfPow((byte)n3, (byte)-2) & 0xFF);
    }
}

