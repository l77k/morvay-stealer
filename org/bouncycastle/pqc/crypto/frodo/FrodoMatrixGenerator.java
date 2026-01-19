/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.frodo;

import org.bouncycastle.crypto.digests.SHAKEDigest;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.Pack;

abstract class FrodoMatrixGenerator {
    int n;
    int q;

    public FrodoMatrixGenerator(int n2, int n3) {
        this.n = n2;
        this.q = n3;
    }

    abstract short[] genMatrix(byte[] var1);

    static class Aes128MatrixGenerator
    extends FrodoMatrixGenerator {
        public Aes128MatrixGenerator(int n2, int n3) {
            super(n2, n3);
        }

        @Override
        short[] genMatrix(byte[] byArray) {
            short[] sArray = new short[this.n * this.n];
            byte[] byArray2 = new byte[16];
            byte[] byArray3 = new byte[16];
            AESEngine aESEngine = new AESEngine();
            aESEngine.init(true, new KeyParameter(byArray));
            for (int i2 = 0; i2 < this.n; ++i2) {
                Pack.shortToLittleEndian((short)i2, byArray2, 0);
                for (int i3 = 0; i3 < this.n; i3 += 8) {
                    Pack.shortToLittleEndian((short)i3, byArray2, 2);
                    aESEngine.processBlock(byArray2, 0, byArray3, 0);
                    for (int i4 = 0; i4 < 8; ++i4) {
                        sArray[i2 * this.n + i3 + i4] = (short)(Pack.littleEndianToShort(byArray3, 2 * i4) & this.q - 1);
                    }
                }
            }
            return sArray;
        }
    }

    static class Shake128MatrixGenerator
    extends FrodoMatrixGenerator {
        public Shake128MatrixGenerator(int n2, int n3) {
            super(n2, n3);
        }

        @Override
        short[] genMatrix(byte[] byArray) {
            short[] sArray = new short[this.n * this.n];
            byte[] byArray2 = new byte[16 * this.n / 8];
            byte[] byArray3 = new byte[2 + byArray.length];
            System.arraycopy(byArray, 0, byArray3, 2, byArray.length);
            SHAKEDigest sHAKEDigest = new SHAKEDigest(128);
            for (short s2 = 0; s2 < this.n; s2 = (short)(s2 + 1)) {
                Pack.shortToLittleEndian(s2, byArray3, 0);
                sHAKEDigest.update(byArray3, 0, byArray3.length);
                sHAKEDigest.doFinal(byArray2, 0, byArray2.length);
                for (int n2 = 0; n2 < this.n; n2 = (int)((short)(n2 + 1))) {
                    sArray[s2 * this.n + n2] = (short)(Pack.littleEndianToShort(byArray2, 2 * n2) & this.q - 1);
                }
            }
            return sArray;
        }
    }
}

