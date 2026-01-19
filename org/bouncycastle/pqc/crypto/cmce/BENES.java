/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.cmce;

abstract class BENES {
    private static final long[] TRANSPOSE_MASKS = new long[]{0x5555555555555555L, 0x3333333333333333L, 0xF0F0F0F0F0F0F0FL, 0xFF00FF00FF00FFL, 0xFFFF0000FFFFL, 0xFFFFFFFFL};
    protected final int SYS_N;
    protected final int SYS_T;
    protected final int GFBITS;

    public BENES(int n2, int n3, int n4) {
        this.SYS_N = n2;
        this.SYS_T = n3;
        this.GFBITS = n4;
    }

    static void transpose_64x64(long[] lArray, long[] lArray2) {
        BENES.transpose_64x64(lArray, lArray2, 0);
    }

    static void transpose_64x64(long[] lArray, long[] lArray2, int n2) {
        long l2;
        long l3;
        long l4;
        int n3;
        int n4;
        int n5;
        long l5;
        System.arraycopy(lArray2, n2, lArray, n2, 64);
        int n6 = 5;
        do {
            l5 = TRANSPOSE_MASKS[n6];
            n5 = 1 << n6;
            for (n4 = n2; n4 < n2 + 64; n4 += n5 * 2) {
                for (n3 = n4; n3 < n4 + n5; n3 += 4) {
                    l4 = lArray[n3 + 0];
                    l3 = lArray[n3 + 1];
                    l2 = lArray[n3 + 2];
                    long l6 = lArray[n3 + 3];
                    long l7 = lArray[n3 + n5 + 0];
                    long l8 = lArray[n3 + n5 + 1];
                    long l9 = lArray[n3 + n5 + 2];
                    long l10 = lArray[n3 + n5 + 3];
                    long l11 = (l4 >>> n5 ^ l7) & l5;
                    long l12 = (l3 >>> n5 ^ l8) & l5;
                    long l13 = (l2 >>> n5 ^ l9) & l5;
                    long l14 = (l6 >>> n5 ^ l10) & l5;
                    lArray[n3 + 0] = l4 ^ l11 << n5;
                    lArray[n3 + 1] = l3 ^ l12 << n5;
                    lArray[n3 + 2] = l2 ^ l13 << n5;
                    lArray[n3 + 3] = l6 ^ l14 << n5;
                    lArray[n3 + n5 + 0] = l7 ^ l11;
                    lArray[n3 + n5 + 1] = l8 ^ l12;
                    lArray[n3 + n5 + 2] = l9 ^ l13;
                    lArray[n3 + n5 + 3] = l10 ^ l14;
                }
            }
        } while (--n6 >= 2);
        do {
            l5 = TRANSPOSE_MASKS[n6];
            n5 = 1 << n6;
            for (n4 = n2; n4 < n2 + 64; n4 += n5 * 2) {
                for (n3 = n4; n3 < n4 + n5; ++n3) {
                    l4 = lArray[n3 + 0];
                    l3 = lArray[n3 + n5];
                    l2 = (l4 >>> n5 ^ l3) & l5;
                    lArray[n3 + 0] = l4 ^ l2 << n5;
                    lArray[n3 + n5] = l3 ^ l2;
                }
            }
        } while (--n6 >= 0);
    }

    protected abstract void support_gen(short[] var1, byte[] var2);
}

