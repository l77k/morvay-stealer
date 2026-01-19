/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.newhope;

import org.bouncycastle.crypto.digests.SHAKEDigest;
import org.bouncycastle.pqc.crypto.newhope.ChaCha20;
import org.bouncycastle.pqc.crypto.newhope.NTT;
import org.bouncycastle.pqc.crypto.newhope.Precomp;
import org.bouncycastle.pqc.crypto.newhope.Reduce;
import org.bouncycastle.util.Pack;

class Poly {
    Poly() {
    }

    static void add(short[] sArray, short[] sArray2, short[] sArray3) {
        for (int i2 = 0; i2 < 1024; ++i2) {
            sArray3[i2] = Reduce.barrett((short)(sArray[i2] + sArray2[i2]));
        }
    }

    static void fromBytes(short[] sArray, byte[] byArray) {
        for (int i2 = 0; i2 < 256; ++i2) {
            int n2 = 7 * i2;
            int n3 = byArray[n2 + 0] & 0xFF;
            int n4 = byArray[n2 + 1] & 0xFF;
            int n5 = byArray[n2 + 2] & 0xFF;
            int n6 = byArray[n2 + 3] & 0xFF;
            int n7 = byArray[n2 + 4] & 0xFF;
            int n8 = byArray[n2 + 5] & 0xFF;
            int n9 = byArray[n2 + 6] & 0xFF;
            int n10 = 4 * i2;
            sArray[n10 + 0] = (short)(n3 | (n4 & 0x3F) << 8);
            sArray[n10 + 1] = (short)(n4 >>> 6 | n5 << 2 | (n6 & 0xF) << 10);
            sArray[n10 + 2] = (short)(n6 >>> 4 | n7 << 4 | (n8 & 3) << 12);
            sArray[n10 + 3] = (short)(n8 >>> 2 | n9 << 6);
        }
    }

    static void fromNTT(short[] sArray) {
        NTT.bitReverse(sArray);
        NTT.core(sArray, Precomp.OMEGAS_INV_MONTGOMERY);
        NTT.mulCoefficients(sArray, Precomp.PSIS_INV_MONTGOMERY);
    }

    static void getNoise(short[] sArray, byte[] byArray, byte by) {
        byte[] byArray2 = new byte[8];
        byArray2[0] = by;
        byte[] byArray3 = new byte[4096];
        ChaCha20.process(byArray, byArray2, byArray3, 0, byArray3.length);
        for (int i2 = 0; i2 < 1024; ++i2) {
            int n2;
            int n3 = Pack.bigEndianToInt(byArray3, i2 * 4);
            int n4 = 0;
            for (n2 = 0; n2 < 8; ++n2) {
                n4 += n3 >> n2 & 0x1010101;
            }
            n2 = (n4 >>> 24) + (n4 >>> 0) & 0xFF;
            int n5 = (n4 >>> 16) + (n4 >>> 8) & 0xFF;
            sArray[i2] = (short)(n2 + 12289 - n5);
        }
    }

    static void pointWise(short[] sArray, short[] sArray2, short[] sArray3) {
        for (int i2 = 0; i2 < 1024; ++i2) {
            int n2 = sArray[i2] & 0xFFFF;
            int n3 = sArray2[i2] & 0xFFFF;
            short s2 = Reduce.montgomery(3186 * n3);
            sArray3[i2] = Reduce.montgomery(n2 * (s2 & 0xFFFF));
        }
    }

    static void toBytes(byte[] byArray, short[] sArray) {
        for (int i2 = 0; i2 < 256; ++i2) {
            int n2 = 4 * i2;
            short s2 = Poly.normalize(sArray[n2 + 0]);
            short s3 = Poly.normalize(sArray[n2 + 1]);
            short s4 = Poly.normalize(sArray[n2 + 2]);
            short s5 = Poly.normalize(sArray[n2 + 3]);
            int n3 = 7 * i2;
            byArray[n3 + 0] = (byte)s2;
            byArray[n3 + 1] = (byte)(s2 >> 8 | s3 << 6);
            byArray[n3 + 2] = (byte)(s3 >> 2);
            byArray[n3 + 3] = (byte)(s3 >> 10 | s4 << 4);
            byArray[n3 + 4] = (byte)(s4 >> 4);
            byArray[n3 + 5] = (byte)(s4 >> 12 | s5 << 2);
            byArray[n3 + 6] = (byte)(s5 >> 6);
        }
    }

    static void toNTT(short[] sArray) {
        NTT.mulCoefficients(sArray, Precomp.PSIS_BITREV_MONTGOMERY);
        NTT.core(sArray, Precomp.OMEGAS_MONTGOMERY);
    }

    static void uniform(short[] sArray, byte[] byArray) {
        SHAKEDigest sHAKEDigest = new SHAKEDigest(128);
        sHAKEDigest.update(byArray, 0, byArray.length);
        int n2 = 0;
        block0: while (true) {
            byte[] byArray2 = new byte[256];
            sHAKEDigest.doOutput(byArray2, 0, byArray2.length);
            int n3 = 0;
            while (true) {
                if (n3 >= byArray2.length) continue block0;
                int n4 = byArray2[n3] & 0xFF | (byArray2[n3 + 1] & 0xFF) << 8;
                if (n4 < 61445) {
                    sArray[n2++] = (short)n4;
                    if (n2 == 1024) {
                        return;
                    }
                }
                n3 += 2;
            }
            break;
        }
    }

    private static short normalize(short s2) {
        int n2 = Reduce.barrett(s2);
        int n3 = n2 - 12289;
        int n4 = n3 >> 31;
        n2 = n3 ^ (n2 ^ n3) & n4;
        return (short)n2;
    }
}

