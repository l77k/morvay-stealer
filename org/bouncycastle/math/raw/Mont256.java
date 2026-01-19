/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.math.raw;

import org.bouncycastle.math.raw.Nat256;

public abstract class Mont256 {
    private static final long M = 0xFFFFFFFFL;

    public static int inverse32(int n2) {
        int n3 = n2;
        n3 *= 2 - n2 * n3;
        n3 *= 2 - n2 * n3;
        n3 *= 2 - n2 * n3;
        n3 *= 2 - n2 * n3;
        return n3;
    }

    public static void multAdd(int[] nArray, int[] nArray2, int[] nArray3, int[] nArray4, int n2) {
        int n3 = 0;
        long l2 = (long)nArray2[0] & 0xFFFFFFFFL;
        for (int i2 = 0; i2 < 8; ++i2) {
            long l3 = (long)nArray3[0] & 0xFFFFFFFFL;
            long l4 = (long)nArray[i2] & 0xFFFFFFFFL;
            long l5 = l4 * l2;
            long l6 = (l5 & 0xFFFFFFFFL) + l3;
            long l7 = (long)((int)l6 * n2) & 0xFFFFFFFFL;
            long l8 = l7 * ((long)nArray4[0] & 0xFFFFFFFFL);
            l6 += l8 & 0xFFFFFFFFL;
            l6 = (l6 >>> 32) + (l5 >>> 32) + (l8 >>> 32);
            for (int i3 = 1; i3 < 8; ++i3) {
                l5 = l4 * ((long)nArray2[i3] & 0xFFFFFFFFL);
                l8 = l7 * ((long)nArray4[i3] & 0xFFFFFFFFL);
                nArray3[i3 - 1] = (int)(l6 += (l5 & 0xFFFFFFFFL) + (l8 & 0xFFFFFFFFL) + ((long)nArray3[i3] & 0xFFFFFFFFL));
                l6 = (l6 >>> 32) + (l5 >>> 32) + (l8 >>> 32);
            }
            nArray3[7] = (int)(l6 += (long)n3 & 0xFFFFFFFFL);
            n3 = (int)(l6 >>> 32);
        }
        if (n3 != 0 || Nat256.gte(nArray3, nArray4)) {
            Nat256.sub(nArray3, nArray4, nArray3);
        }
    }

    public static void multAddXF(int[] nArray, int[] nArray2, int[] nArray3, int[] nArray4) {
        int n2 = 0;
        long l2 = (long)nArray2[0] & 0xFFFFFFFFL;
        for (int i2 = 0; i2 < 8; ++i2) {
            long l3 = (long)nArray[i2] & 0xFFFFFFFFL;
            long l4 = l3 * l2 + ((long)nArray3[0] & 0xFFFFFFFFL);
            long l5 = l4 & 0xFFFFFFFFL;
            l4 = (l4 >>> 32) + l5;
            for (int i3 = 1; i3 < 8; ++i3) {
                long l6 = l3 * ((long)nArray2[i3] & 0xFFFFFFFFL);
                long l7 = l5 * ((long)nArray4[i3] & 0xFFFFFFFFL);
                nArray3[i3 - 1] = (int)(l4 += (l6 & 0xFFFFFFFFL) + (l7 & 0xFFFFFFFFL) + ((long)nArray3[i3] & 0xFFFFFFFFL));
                l4 = (l4 >>> 32) + (l6 >>> 32) + (l7 >>> 32);
            }
            nArray3[7] = (int)(l4 += (long)n2 & 0xFFFFFFFFL);
            n2 = (int)(l4 >>> 32);
        }
        if (n2 != 0 || Nat256.gte(nArray3, nArray4)) {
            Nat256.sub(nArray3, nArray4, nArray3);
        }
    }

    public static void reduce(int[] nArray, int[] nArray2, int n2) {
        for (int i2 = 0; i2 < 8; ++i2) {
            int n3 = nArray[0];
            long l2 = (long)(n3 * n2) & 0xFFFFFFFFL;
            long l3 = l2 * ((long)nArray2[0] & 0xFFFFFFFFL) + ((long)n3 & 0xFFFFFFFFL);
            l3 >>>= 32;
            for (int i3 = 1; i3 < 8; ++i3) {
                nArray[i3 - 1] = (int)(l3 += l2 * ((long)nArray2[i3] & 0xFFFFFFFFL) + ((long)nArray[i3] & 0xFFFFFFFFL));
                l3 >>>= 32;
            }
            nArray[7] = (int)l3;
        }
        if (Nat256.gte(nArray, nArray2)) {
            Nat256.sub(nArray, nArray2, nArray);
        }
    }

    public static void reduceXF(int[] nArray, int[] nArray2) {
        for (int i2 = 0; i2 < 8; ++i2) {
            long l2;
            int n2 = nArray[0];
            long l3 = l2 = (long)n2 & 0xFFFFFFFFL;
            for (int i3 = 1; i3 < 8; ++i3) {
                nArray[i3 - 1] = (int)(l3 += l2 * ((long)nArray2[i3] & 0xFFFFFFFFL) + ((long)nArray[i3] & 0xFFFFFFFFL));
                l3 >>>= 32;
            }
            nArray[7] = (int)l3;
        }
        if (Nat256.gte(nArray, nArray2)) {
            Nat256.sub(nArray, nArray2, nArray);
        }
    }
}

