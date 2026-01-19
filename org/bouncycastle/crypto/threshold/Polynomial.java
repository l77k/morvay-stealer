/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.threshold;

import org.bouncycastle.crypto.threshold.PolynomialNative;
import org.bouncycastle.crypto.threshold.PolynomialTable;
import org.bouncycastle.crypto.threshold.ShamirSecretSplitter;

abstract class Polynomial {
    Polynomial() {
    }

    public static Polynomial newInstance(ShamirSecretSplitter.Algorithm algorithm, ShamirSecretSplitter.Mode mode) {
        if (mode == ShamirSecretSplitter.Mode.Native) {
            return new PolynomialNative(algorithm);
        }
        return new PolynomialTable(algorithm);
    }

    protected abstract byte gfMul(int var1, int var2);

    protected abstract byte gfDiv(int var1, int var2);

    protected byte gfPow(int n2, byte by) {
        int n3 = 1;
        for (int i2 = 0; i2 < 8; ++i2) {
            if ((by & 1 << i2) != 0) {
                n3 = this.gfMul(n3 & 0xFF, n2 & 0xFF);
            }
            n2 = this.gfMul(n2 & 0xFF, n2 & 0xFF);
        }
        return (byte)n3;
    }

    public byte[] gfVecMul(byte[] byArray, byte[][] byArray2) {
        byte[] byArray3 = new byte[byArray2[0].length];
        for (int i2 = 0; i2 < byArray2[0].length; ++i2) {
            int n2 = 0;
            for (int i3 = 0; i3 < byArray.length; ++i3) {
                n2 ^= this.gfMul(byArray[i3] & 0xFF, byArray2[i3][i2] & 0xFF);
            }
            byArray3[i2] = (byte)n2;
        }
        return byArray3;
    }
}

