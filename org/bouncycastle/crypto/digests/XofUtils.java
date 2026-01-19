/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.digests;

import org.bouncycastle.util.Arrays;

public class XofUtils {
    public static byte[] leftEncode(long l2) {
        int n2 = 1;
        long l3 = l2;
        while ((l3 >>= 8) != 0L) {
            n2 = (byte)(n2 + 1);
        }
        byte[] byArray = new byte[n2 + 1];
        byArray[0] = n2;
        for (int i2 = 1; i2 <= n2; ++i2) {
            byArray[i2] = (byte)(l2 >> 8 * (n2 - i2));
        }
        return byArray;
    }

    public static byte[] rightEncode(long l2) {
        int n2 = 1;
        long l3 = l2;
        while ((l3 >>= 8) != 0L) {
            n2 = (byte)(n2 + 1);
        }
        byte[] byArray = new byte[n2 + 1];
        byArray[n2] = n2;
        for (int i2 = 0; i2 < n2; ++i2) {
            byArray[i2] = (byte)(l2 >> 8 * (n2 - i2 - 1));
        }
        return byArray;
    }

    static byte[] encode(byte by) {
        return Arrays.concatenate(XofUtils.leftEncode(8L), new byte[]{by});
    }

    static byte[] encode(byte[] byArray, int n2, int n3) {
        if (byArray.length == n3) {
            return Arrays.concatenate(XofUtils.leftEncode(n3 * 8), byArray);
        }
        return Arrays.concatenate(XofUtils.leftEncode(n3 * 8), Arrays.copyOfRange(byArray, n2, n2 + n3));
    }
}

