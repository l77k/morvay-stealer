/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.picnic;

import org.bouncycastle.util.Integers;

class Utils {
    Utils() {
    }

    protected static int numBytes(int n2) {
        return n2 == 0 ? 0 : (n2 - 1) / 8 + 1;
    }

    protected static int ceil_log2(int n2) {
        if (n2 == 0) {
            return 0;
        }
        return 32 - Utils.nlz(n2 - 1);
    }

    private static int nlz(int n2) {
        if (n2 == 0) {
            return 32;
        }
        int n3 = 1;
        if (n2 >>> 16 == 0) {
            n3 += 16;
            n2 <<= 16;
        }
        if (n2 >>> 24 == 0) {
            n3 += 8;
            n2 <<= 8;
        }
        if (n2 >>> 28 == 0) {
            n3 += 4;
            n2 <<= 4;
        }
        if (n2 >>> 30 == 0) {
            n3 += 2;
            n2 <<= 2;
        }
        return n3 -= n2 >>> 31;
    }

    protected static int parity(byte[] byArray, int n2) {
        byte by = byArray[0];
        for (int i2 = 1; i2 < n2; ++i2) {
            by = (byte)(by ^ byArray[i2]);
        }
        return Integers.bitCount(by & 0xFF) & 1;
    }

    protected static int parity16(int n2) {
        return Integers.bitCount(n2 & 0xFFFF) & 1;
    }

    protected static int parity32(int n2) {
        return Integers.bitCount(n2) & 1;
    }

    protected static void setBitInWordArray(int[] nArray, int n2, int n3) {
        Utils.setBit(nArray, n2, n3);
    }

    protected static int getBitFromWordArray(int[] nArray, int n2) {
        return Utils.getBit(nArray, n2);
    }

    protected static byte getBit(byte[] byArray, int n2) {
        int n3 = n2 >>> 3;
        int n4 = n2 & 7 ^ 7;
        return (byte)(byArray[n3] >>> n4 & 1);
    }

    protected static byte getCrumbAligned(byte[] byArray, int n2) {
        int n3 = n2 >>> 2;
        int n4 = n2 << 1 & 6 ^ 6;
        int n5 = byArray[n3] >>> n4;
        return (byte)((n5 & 1) << 1 | (n5 & 2) >> 1);
    }

    protected static int getBit(int n2, int n3) {
        int n4 = n3 ^ 7;
        return n2 >>> n4 & 1;
    }

    protected static int getBit(int[] nArray, int n2) {
        int n3 = n2 >>> 5;
        int n4 = n2 & 0x1F ^ 7;
        return nArray[n3] >>> n4 & 1;
    }

    protected static void setBit(byte[] byArray, int n2, byte by) {
        int n3 = n2 >>> 3;
        int n4 = n2 & 7 ^ 7;
        int n5 = byArray[n3];
        n5 &= ~(1 << n4);
        byArray[n3] = (byte)(n5 |= by << n4);
    }

    protected static int setBit(int n2, int n3, int n4) {
        int n5 = n3 ^ 7;
        n2 &= ~(1 << n5);
        return n2 |= n4 << n5;
    }

    protected static void setBit(int[] nArray, int n2, int n3) {
        int n4 = n2 >>> 5;
        int n5 = n2 & 0x1F ^ 7;
        int n6 = nArray[n4];
        n6 &= ~(1 << n5);
        nArray[n4] = n6 |= n3 << n5;
    }

    protected static void zeroTrailingBits(int[] nArray, int n2) {
        int n3 = n2 & 0x1F;
        if (n3 != 0) {
            int n4 = n2 >>> 5;
            nArray[n4] = nArray[n4] & Utils.getTrailingBitsMask(n2);
        }
    }

    protected static int getTrailingBitsMask(int n2) {
        int n3 = n2 & 0xFFFFFFF8;
        int n4 = ~(-1 << n3);
        int n5 = n2 & 7;
        if (n5 != 0) {
            n4 ^= (65280 >>> n5 & 0xFF) << n3;
        }
        return n4;
    }
}

