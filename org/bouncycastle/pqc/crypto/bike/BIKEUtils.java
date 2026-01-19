/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.bike;

import org.bouncycastle.crypto.Xof;
import org.bouncycastle.util.Pack;

class BIKEUtils {
    BIKEUtils() {
    }

    static int getHammingWeight(byte[] byArray) {
        int n2 = 0;
        for (int i2 = 0; i2 < byArray.length; ++i2) {
            n2 += byArray[i2];
        }
        return n2;
    }

    static void fromBitArrayToByteArray(byte[] byArray, byte[] byArray2, int n2, int n3) {
        int n4 = 0;
        int n5 = 0;
        long l2 = n3;
        while ((long)n4 < l2) {
            int n6;
            int n7;
            if (n4 + 8 >= n3) {
                n7 = byArray2[n2 + n4];
                for (n6 = n3 - n4 - 1; n6 >= 1; --n6) {
                    n7 |= byArray2[n2 + n4 + n6] << n6;
                }
                byArray[n5] = (byte)n7;
            } else {
                n7 = byArray2[n2 + n4];
                for (n6 = 7; n6 >= 1; --n6) {
                    n7 |= byArray2[n2 + n4 + n6] << n6;
                }
                byArray[n5] = (byte)n7;
            }
            n4 += 8;
            ++n5;
        }
    }

    static void generateRandomByteArray(byte[] byArray, int n2, int n3, Xof xof) {
        byte[] byArray2 = new byte[4];
        for (int i2 = n3 - 1; i2 >= 0; --i2) {
            xof.doOutput(byArray2, 0, 4);
            long l2 = (long)Pack.littleEndianToInt(byArray2, 0) & 0xFFFFFFFFL;
            l2 = l2 * (long)(n2 - i2) >> 32;
            int n4 = (int)l2;
            if (BIKEUtils.CHECK_BIT(byArray, n4 += i2) != 0) {
                n4 = i2;
            }
            BIKEUtils.SET_BIT(byArray, n4);
        }
    }

    protected static int CHECK_BIT(byte[] byArray, int n2) {
        int n3 = n2 / 8;
        int n4 = n2 % 8;
        return byArray[n3] >>> n4 & 1;
    }

    protected static void SET_BIT(byte[] byArray, int n2) {
        int n3 = n2 / 8;
        int n4 = n2 % 8;
        int n5 = n3;
        byArray[n5] = (byte)((long)byArray[n5] | 1L << (int)((long)n4));
    }
}

