/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.cmce;

import org.bouncycastle.util.Pack;

class Utils {
    Utils() {
    }

    static void store_gf(byte[] byArray, int n2, short s2) {
        byArray[n2 + 0] = (byte)(s2 & 0xFF);
        byArray[n2 + 1] = (byte)(s2 >> 8);
    }

    static short load_gf(byte[] byArray, int n2, int n3) {
        return (short)(Pack.littleEndianToShort(byArray, n2) & n3);
    }

    static int load4(byte[] byArray, int n2) {
        return Pack.littleEndianToInt(byArray, n2);
    }

    static void store8(byte[] byArray, int n2, long l2) {
        byArray[n2 + 0] = (byte)(l2 >> 0 & 0xFFL);
        byArray[n2 + 1] = (byte)(l2 >> 8 & 0xFFL);
        byArray[n2 + 2] = (byte)(l2 >> 16 & 0xFFL);
        byArray[n2 + 3] = (byte)(l2 >> 24 & 0xFFL);
        byArray[n2 + 4] = (byte)(l2 >> 32 & 0xFFL);
        byArray[n2 + 5] = (byte)(l2 >> 40 & 0xFFL);
        byArray[n2 + 6] = (byte)(l2 >> 48 & 0xFFL);
        byArray[n2 + 7] = (byte)(l2 >> 56 & 0xFFL);
    }

    static long load8(byte[] byArray, int n2) {
        return Pack.littleEndianToLong(byArray, n2);
    }

    static short bitrev(short s2, int n2) {
        s2 = (short)((s2 & 0xFF) << 8 | (s2 & 0xFF00) >> 8);
        s2 = (short)((s2 & 0xF0F) << 4 | (s2 & 0xF0F0) >> 4);
        s2 = (short)((s2 & 0x3333) << 2 | (s2 & 0xCCCC) >> 2);
        s2 = (short)((s2 & 0x5555) << 1 | (s2 & 0xAAAA) >> 1);
        if (n2 == 12) {
            return (short)(s2 >> 4);
        }
        return (short)(s2 >> 3);
    }
}

