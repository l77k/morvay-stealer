/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.math.ec.rfc7748;

import org.bouncycastle.math.raw.Mod;

public abstract class X25519Field {
    public static final int SIZE = 10;
    private static final int M24 = 0xFFFFFF;
    private static final int M25 = 0x1FFFFFF;
    private static final int M26 = 0x3FFFFFF;
    private static final int[] P32 = new int[]{-19, -1, -1, -1, -1, -1, -1, Integer.MAX_VALUE};
    private static final int[] ROOT_NEG_ONE = new int[]{-32595792, -7943725, 4688975, 3500415, 6194736, 33281959, -12573105, -1002827, 163343, 5703241};

    protected X25519Field() {
    }

    public static void add(int[] nArray, int[] nArray2, int[] nArray3) {
        for (int i2 = 0; i2 < 10; ++i2) {
            nArray3[i2] = nArray[i2] + nArray2[i2];
        }
    }

    public static void addOne(int[] nArray) {
        nArray[0] = nArray[0] + 1;
    }

    public static void addOne(int[] nArray, int n2) {
        int n3 = n2;
        nArray[n3] = nArray[n3] + 1;
    }

    public static void apm(int[] nArray, int[] nArray2, int[] nArray3, int[] nArray4) {
        for (int i2 = 0; i2 < 10; ++i2) {
            int n2 = nArray[i2];
            int n3 = nArray2[i2];
            nArray3[i2] = n2 + n3;
            nArray4[i2] = n2 - n3;
        }
    }

    public static int areEqual(int[] nArray, int[] nArray2) {
        int n2 = 0;
        for (int i2 = 0; i2 < 10; ++i2) {
            n2 |= nArray[i2] ^ nArray2[i2];
        }
        n2 = n2 >>> 1 | n2 & 1;
        return n2 - 1 >> 31;
    }

    public static boolean areEqualVar(int[] nArray, int[] nArray2) {
        return 0 != X25519Field.areEqual(nArray, nArray2);
    }

    public static void carry(int[] nArray) {
        int n2 = nArray[0];
        int n3 = nArray[1];
        int n4 = nArray[2];
        int n5 = nArray[3];
        int n6 = nArray[4];
        int n7 = nArray[5];
        int n8 = nArray[6];
        int n9 = nArray[7];
        int n10 = nArray[8];
        int n11 = nArray[9];
        n4 += n3 >> 26;
        n3 &= 0x3FFFFFF;
        n6 += n5 >> 26;
        n5 &= 0x3FFFFFF;
        n9 += n8 >> 26;
        n8 &= 0x3FFFFFF;
        n11 += n10 >> 26;
        n10 &= 0x3FFFFFF;
        n5 += n4 >> 25;
        n4 &= 0x1FFFFFF;
        n7 += n6 >> 25;
        n6 &= 0x1FFFFFF;
        n10 += n9 >> 25;
        n9 &= 0x1FFFFFF;
        n2 += (n11 >> 25) * 38;
        n11 &= 0x1FFFFFF;
        n3 += n2 >> 26;
        n2 &= 0x3FFFFFF;
        n8 += n7 >> 26;
        n7 &= 0x3FFFFFF;
        n4 += n3 >> 26;
        n3 &= 0x3FFFFFF;
        n6 += n5 >> 26;
        n5 &= 0x3FFFFFF;
        n9 += n8 >> 26;
        n8 &= 0x3FFFFFF;
        n11 += n10 >> 26;
        n10 &= 0x3FFFFFF;
        nArray[0] = n2;
        nArray[1] = n3;
        nArray[2] = n4;
        nArray[3] = n5;
        nArray[4] = n6;
        nArray[5] = n7;
        nArray[6] = n8;
        nArray[7] = n9;
        nArray[8] = n10;
        nArray[9] = n11;
    }

    public static void cmov(int n2, int[] nArray, int n3, int[] nArray2, int n4) {
        for (int i2 = 0; i2 < 10; ++i2) {
            int n5 = nArray2[n4 + i2];
            int n6 = n5 ^ nArray[n3 + i2];
            nArray2[n4 + i2] = n5 ^= n6 & n2;
        }
    }

    public static void cnegate(int n2, int[] nArray) {
        int n3 = 0 - n2;
        for (int i2 = 0; i2 < 10; ++i2) {
            nArray[i2] = (nArray[i2] ^ n3) - n3;
        }
    }

    public static void copy(int[] nArray, int n2, int[] nArray2, int n3) {
        for (int i2 = 0; i2 < 10; ++i2) {
            nArray2[n3 + i2] = nArray[n2 + i2];
        }
    }

    public static int[] create() {
        return new int[10];
    }

    public static int[] createTable(int n2) {
        return new int[10 * n2];
    }

    public static void cswap(int n2, int[] nArray, int[] nArray2) {
        int n3 = 0 - n2;
        for (int i2 = 0; i2 < 10; ++i2) {
            int n4 = nArray[i2];
            int n5 = nArray2[i2];
            int n6 = n3 & (n4 ^ n5);
            nArray[i2] = n4 ^ n6;
            nArray2[i2] = n5 ^ n6;
        }
    }

    public static void decode(int[] nArray, int n2, int[] nArray2) {
        X25519Field.decode128(nArray, n2, nArray2, 0);
        X25519Field.decode128(nArray, n2 + 4, nArray2, 5);
        nArray2[9] = nArray2[9] & 0xFFFFFF;
    }

    public static void decode(byte[] byArray, int[] nArray) {
        X25519Field.decode128(byArray, 0, nArray, 0);
        X25519Field.decode128(byArray, 16, nArray, 5);
        nArray[9] = nArray[9] & 0xFFFFFF;
    }

    public static void decode(byte[] byArray, int n2, int[] nArray) {
        X25519Field.decode128(byArray, n2, nArray, 0);
        X25519Field.decode128(byArray, n2 + 16, nArray, 5);
        nArray[9] = nArray[9] & 0xFFFFFF;
    }

    public static void decode(byte[] byArray, int n2, int[] nArray, int n3) {
        X25519Field.decode128(byArray, n2, nArray, n3);
        X25519Field.decode128(byArray, n2 + 16, nArray, n3 + 5);
        int n4 = n3 + 9;
        nArray[n4] = nArray[n4] & 0xFFFFFF;
    }

    private static void decode128(int[] nArray, int n2, int[] nArray2, int n3) {
        int n4 = nArray[n2 + 0];
        int n5 = nArray[n2 + 1];
        int n6 = nArray[n2 + 2];
        int n7 = nArray[n2 + 3];
        nArray2[n3 + 0] = n4 & 0x3FFFFFF;
        nArray2[n3 + 1] = (n5 << 6 | n4 >>> 26) & 0x3FFFFFF;
        nArray2[n3 + 2] = (n6 << 12 | n5 >>> 20) & 0x1FFFFFF;
        nArray2[n3 + 3] = (n7 << 19 | n6 >>> 13) & 0x3FFFFFF;
        nArray2[n3 + 4] = n7 >>> 7;
    }

    private static void decode128(byte[] byArray, int n2, int[] nArray, int n3) {
        int n4 = X25519Field.decode32(byArray, n2 + 0);
        int n5 = X25519Field.decode32(byArray, n2 + 4);
        int n6 = X25519Field.decode32(byArray, n2 + 8);
        int n7 = X25519Field.decode32(byArray, n2 + 12);
        nArray[n3 + 0] = n4 & 0x3FFFFFF;
        nArray[n3 + 1] = (n5 << 6 | n4 >>> 26) & 0x3FFFFFF;
        nArray[n3 + 2] = (n6 << 12 | n5 >>> 20) & 0x1FFFFFF;
        nArray[n3 + 3] = (n7 << 19 | n6 >>> 13) & 0x3FFFFFF;
        nArray[n3 + 4] = n7 >>> 7;
    }

    private static int decode32(byte[] byArray, int n2) {
        int n3 = byArray[n2] & 0xFF;
        n3 |= (byArray[++n2] & 0xFF) << 8;
        n3 |= (byArray[++n2] & 0xFF) << 16;
        return n3 |= byArray[++n2] << 24;
    }

    public static void encode(int[] nArray, int[] nArray2, int n2) {
        X25519Field.encode128(nArray, 0, nArray2, n2);
        X25519Field.encode128(nArray, 5, nArray2, n2 + 4);
    }

    public static void encode(int[] nArray, byte[] byArray) {
        X25519Field.encode128(nArray, 0, byArray, 0);
        X25519Field.encode128(nArray, 5, byArray, 16);
    }

    public static void encode(int[] nArray, byte[] byArray, int n2) {
        X25519Field.encode128(nArray, 0, byArray, n2);
        X25519Field.encode128(nArray, 5, byArray, n2 + 16);
    }

    public static void encode(int[] nArray, int n2, byte[] byArray, int n3) {
        X25519Field.encode128(nArray, n2, byArray, n3);
        X25519Field.encode128(nArray, n2 + 5, byArray, n3 + 16);
    }

    private static void encode128(int[] nArray, int n2, int[] nArray2, int n3) {
        int n4 = nArray[n2 + 0];
        int n5 = nArray[n2 + 1];
        int n6 = nArray[n2 + 2];
        int n7 = nArray[n2 + 3];
        int n8 = nArray[n2 + 4];
        nArray2[n3 + 0] = n4 | n5 << 26;
        nArray2[n3 + 1] = n5 >>> 6 | n6 << 20;
        nArray2[n3 + 2] = n6 >>> 12 | n7 << 13;
        nArray2[n3 + 3] = n7 >>> 19 | n8 << 7;
    }

    private static void encode128(int[] nArray, int n2, byte[] byArray, int n3) {
        int n4 = nArray[n2 + 0];
        int n5 = nArray[n2 + 1];
        int n6 = nArray[n2 + 2];
        int n7 = nArray[n2 + 3];
        int n8 = nArray[n2 + 4];
        int n9 = n4 | n5 << 26;
        X25519Field.encode32(n9, byArray, n3 + 0);
        int n10 = n5 >>> 6 | n6 << 20;
        X25519Field.encode32(n10, byArray, n3 + 4);
        int n11 = n6 >>> 12 | n7 << 13;
        X25519Field.encode32(n11, byArray, n3 + 8);
        int n12 = n7 >>> 19 | n8 << 7;
        X25519Field.encode32(n12, byArray, n3 + 12);
    }

    private static void encode32(int n2, byte[] byArray, int n3) {
        byArray[n3] = (byte)n2;
        byArray[++n3] = (byte)(n2 >>> 8);
        byArray[++n3] = (byte)(n2 >>> 16);
        byArray[++n3] = (byte)(n2 >>> 24);
    }

    public static void inv(int[] nArray, int[] nArray2) {
        int[] nArray3 = X25519Field.create();
        int[] nArray4 = new int[8];
        X25519Field.copy(nArray, 0, nArray3, 0);
        X25519Field.normalize(nArray3);
        X25519Field.encode(nArray3, nArray4, 0);
        Mod.modOddInverse(P32, nArray4, nArray4);
        X25519Field.decode(nArray4, 0, nArray2);
    }

    public static void invVar(int[] nArray, int[] nArray2) {
        int[] nArray3 = X25519Field.create();
        int[] nArray4 = new int[8];
        X25519Field.copy(nArray, 0, nArray3, 0);
        X25519Field.normalize(nArray3);
        X25519Field.encode(nArray3, nArray4, 0);
        Mod.modOddInverseVar(P32, nArray4, nArray4);
        X25519Field.decode(nArray4, 0, nArray2);
    }

    public static int isOne(int[] nArray) {
        int n2 = nArray[0] ^ 1;
        for (int i2 = 1; i2 < 10; ++i2) {
            n2 |= nArray[i2];
        }
        n2 = n2 >>> 1 | n2 & 1;
        return n2 - 1 >> 31;
    }

    public static boolean isOneVar(int[] nArray) {
        return 0 != X25519Field.isOne(nArray);
    }

    public static int isZero(int[] nArray) {
        int n2 = 0;
        for (int i2 = 0; i2 < 10; ++i2) {
            n2 |= nArray[i2];
        }
        n2 = n2 >>> 1 | n2 & 1;
        return n2 - 1 >> 31;
    }

    public static boolean isZeroVar(int[] nArray) {
        return 0 != X25519Field.isZero(nArray);
    }

    public static void mul(int[] nArray, int n2, int[] nArray2) {
        int n3 = nArray[0];
        int n4 = nArray[1];
        int n5 = nArray[2];
        int n6 = nArray[3];
        int n7 = nArray[4];
        int n8 = nArray[5];
        int n9 = nArray[6];
        int n10 = nArray[7];
        int n11 = nArray[8];
        int n12 = nArray[9];
        long l2 = (long)n5 * (long)n2;
        n5 = (int)l2 & 0x1FFFFFF;
        l2 >>= 25;
        long l3 = (long)n7 * (long)n2;
        n7 = (int)l3 & 0x1FFFFFF;
        l3 >>= 25;
        long l4 = (long)n10 * (long)n2;
        n10 = (int)l4 & 0x1FFFFFF;
        l4 >>= 25;
        long l5 = (long)n12 * (long)n2;
        n12 = (int)l5 & 0x1FFFFFF;
        l5 >>= 25;
        l5 *= 38L;
        nArray2[0] = (int)(l5 += (long)n3 * (long)n2) & 0x3FFFFFF;
        l5 >>= 26;
        nArray2[5] = (int)(l3 += (long)n8 * (long)n2) & 0x3FFFFFF;
        l3 >>= 26;
        nArray2[1] = (int)(l5 += (long)n4 * (long)n2) & 0x3FFFFFF;
        nArray2[3] = (int)(l2 += (long)n6 * (long)n2) & 0x3FFFFFF;
        nArray2[6] = (int)(l3 += (long)n9 * (long)n2) & 0x3FFFFFF;
        nArray2[8] = (int)(l4 += (long)n11 * (long)n2) & 0x3FFFFFF;
        nArray2[2] = n5 + (int)(l5 >>= 26);
        nArray2[4] = n7 + (int)(l2 >>= 26);
        nArray2[7] = n10 + (int)(l3 >>= 26);
        nArray2[9] = n12 + (int)(l4 >>= 26);
    }

    public static void mul(int[] nArray, int[] nArray2, int[] nArray3) {
        int n2 = nArray[0];
        int n3 = nArray2[0];
        int n4 = nArray[1];
        int n5 = nArray2[1];
        int n6 = nArray[2];
        int n7 = nArray2[2];
        int n8 = nArray[3];
        int n9 = nArray2[3];
        int n10 = nArray[4];
        int n11 = nArray2[4];
        int n12 = nArray[5];
        int n13 = nArray2[5];
        int n14 = nArray[6];
        int n15 = nArray2[6];
        int n16 = nArray[7];
        int n17 = nArray2[7];
        int n18 = nArray[8];
        int n19 = nArray2[8];
        int n20 = nArray[9];
        int n21 = nArray2[9];
        long l2 = (long)n2 * (long)n3;
        long l3 = (long)n2 * (long)n5 + (long)n4 * (long)n3;
        long l4 = (long)n2 * (long)n7 + (long)n4 * (long)n5 + (long)n6 * (long)n3;
        long l5 = (long)n4 * (long)n7 + (long)n6 * (long)n5;
        l5 <<= 1;
        l5 += (long)n2 * (long)n9 + (long)n8 * (long)n3;
        long l6 = (long)n6 * (long)n7;
        l6 <<= 1;
        l6 += (long)n2 * (long)n11 + (long)n4 * (long)n9 + (long)n8 * (long)n5 + (long)n10 * (long)n3;
        long l7 = (long)n4 * (long)n11 + (long)n6 * (long)n9 + (long)n8 * (long)n7 + (long)n10 * (long)n5;
        l7 <<= 1;
        long l8 = (long)n6 * (long)n11 + (long)n10 * (long)n7;
        l8 <<= 1;
        l8 += (long)n8 * (long)n9;
        long l9 = (long)n8 * (long)n11 + (long)n10 * (long)n9;
        long l10 = (long)n10 * (long)n11;
        l10 <<= 1;
        long l11 = (long)n12 * (long)n13;
        long l12 = (long)n12 * (long)n15 + (long)n14 * (long)n13;
        long l13 = (long)n12 * (long)n17 + (long)n14 * (long)n15 + (long)n16 * (long)n13;
        long l14 = (long)n14 * (long)n17 + (long)n16 * (long)n15;
        l14 <<= 1;
        l14 += (long)n12 * (long)n19 + (long)n18 * (long)n13;
        long l15 = (long)n16 * (long)n17;
        l15 <<= 1;
        l15 += (long)n12 * (long)n21 + (long)n14 * (long)n19 + (long)n18 * (long)n15 + (long)n20 * (long)n13;
        long l16 = (long)n14 * (long)n21 + (long)n16 * (long)n19 + (long)n18 * (long)n17 + (long)n20 * (long)n15;
        long l17 = (long)n16 * (long)n21 + (long)n20 * (long)n17;
        l17 <<= 1;
        long l18 = (long)n18 * (long)n21 + (long)n20 * (long)n19;
        long l19 = (long)n20 * (long)n21;
        l2 -= l16 * 76L;
        l3 -= (l17 += (long)n18 * (long)n19) * 38L;
        l4 -= l18 * 38L;
        l5 -= l19 * 76L;
        l7 -= l11;
        l8 -= l12;
        l9 -= l13;
        l10 -= l14;
        n2 += n12;
        n3 += n13;
        n4 += n14;
        n5 += n15;
        n6 += n16;
        n7 += n17;
        n8 += n18;
        n9 += n19;
        n10 += n20;
        n11 += n21;
        long l20 = (long)n2 * (long)n3;
        long l21 = (long)n2 * (long)n5 + (long)n4 * (long)n3;
        long l22 = (long)n2 * (long)n7 + (long)n4 * (long)n5 + (long)n6 * (long)n3;
        long l23 = (long)n4 * (long)n7 + (long)n6 * (long)n5;
        l23 <<= 1;
        l23 += (long)n2 * (long)n9 + (long)n8 * (long)n3;
        long l24 = (long)n6 * (long)n7;
        l24 <<= 1;
        l24 += (long)n2 * (long)n11 + (long)n4 * (long)n9 + (long)n8 * (long)n5 + (long)n10 * (long)n3;
        long l25 = (long)n4 * (long)n11 + (long)n6 * (long)n9 + (long)n8 * (long)n7 + (long)n10 * (long)n5;
        l25 <<= 1;
        long l26 = (long)n6 * (long)n11 + (long)n10 * (long)n7;
        l26 <<= 1;
        l26 += (long)n8 * (long)n9;
        long l27 = (long)n8 * (long)n11 + (long)n10 * (long)n9;
        long l28 = (long)n10 * (long)n11;
        l28 <<= 1;
        long l29 = l10 + (l23 - l5);
        int n22 = (int)l29 & 0x3FFFFFF;
        l29 >>= 26;
        int n23 = (int)(l29 += l24 - l6 - l15) & 0x1FFFFFF;
        l29 >>= 25;
        l29 = l2 + (l29 + l25 - l7) * 38L;
        nArray3[0] = (int)l29 & 0x3FFFFFF;
        l29 >>= 26;
        nArray3[1] = (int)(l29 += l3 + (l26 - l8) * 38L) & 0x3FFFFFF;
        l29 >>= 26;
        nArray3[2] = (int)(l29 += l4 + (l27 - l9) * 38L) & 0x1FFFFFF;
        l29 >>= 25;
        nArray3[3] = (int)(l29 += l5 + (l28 - l10) * 38L) & 0x3FFFFFF;
        l29 >>= 26;
        nArray3[4] = (int)(l29 += l6 + l15 * 38L) & 0x1FFFFFF;
        l29 >>= 25;
        nArray3[5] = (int)(l29 += l7 + (l20 - l2)) & 0x3FFFFFF;
        l29 >>= 26;
        nArray3[6] = (int)(l29 += l8 + (l21 - l3)) & 0x3FFFFFF;
        l29 >>= 26;
        nArray3[7] = (int)(l29 += l9 + (l22 - l4)) & 0x1FFFFFF;
        l29 >>= 25;
        nArray3[8] = (int)(l29 += (long)n22) & 0x3FFFFFF;
        nArray3[9] = n23 + (int)(l29 >>= 26);
    }

    public static void negate(int[] nArray, int[] nArray2) {
        for (int i2 = 0; i2 < 10; ++i2) {
            nArray2[i2] = -nArray[i2];
        }
    }

    public static void normalize(int[] nArray) {
        int n2 = nArray[9] >>> 23 & 1;
        X25519Field.reduce(nArray, n2);
        X25519Field.reduce(nArray, -n2);
    }

    public static void one(int[] nArray) {
        nArray[0] = 1;
        for (int i2 = 1; i2 < 10; ++i2) {
            nArray[i2] = 0;
        }
    }

    private static void powPm5d8(int[] nArray, int[] nArray2, int[] nArray3) {
        int[] nArray4 = nArray2;
        X25519Field.sqr(nArray, nArray4);
        X25519Field.mul(nArray, nArray4, nArray4);
        int[] nArray5 = X25519Field.create();
        X25519Field.sqr(nArray4, nArray5);
        X25519Field.mul(nArray, nArray5, nArray5);
        int[] nArray6 = nArray5;
        X25519Field.sqr(nArray5, 2, nArray6);
        X25519Field.mul(nArray4, nArray6, nArray6);
        int[] nArray7 = X25519Field.create();
        X25519Field.sqr(nArray6, 5, nArray7);
        X25519Field.mul(nArray6, nArray7, nArray7);
        int[] nArray8 = X25519Field.create();
        X25519Field.sqr(nArray7, 5, nArray8);
        X25519Field.mul(nArray6, nArray8, nArray8);
        int[] nArray9 = nArray6;
        X25519Field.sqr(nArray8, 10, nArray9);
        X25519Field.mul(nArray7, nArray9, nArray9);
        int[] nArray10 = nArray7;
        X25519Field.sqr(nArray9, 25, nArray10);
        X25519Field.mul(nArray9, nArray10, nArray10);
        int[] nArray11 = nArray8;
        X25519Field.sqr(nArray10, 25, nArray11);
        X25519Field.mul(nArray9, nArray11, nArray11);
        int[] nArray12 = nArray9;
        X25519Field.sqr(nArray11, 50, nArray12);
        X25519Field.mul(nArray10, nArray12, nArray12);
        int[] nArray13 = nArray10;
        X25519Field.sqr(nArray12, 125, nArray13);
        X25519Field.mul(nArray12, nArray13, nArray13);
        int[] nArray14 = nArray12;
        X25519Field.sqr(nArray13, 2, nArray14);
        X25519Field.mul(nArray14, nArray, nArray3);
    }

    private static void reduce(int[] nArray, int n2) {
        int n3 = nArray[9];
        int n4 = n3 & 0xFFFFFF;
        n3 = (n3 >> 24) + n2;
        long l2 = n3 * 19;
        nArray[0] = (int)(l2 += (long)nArray[0]) & 0x3FFFFFF;
        l2 >>= 26;
        nArray[1] = (int)(l2 += (long)nArray[1]) & 0x3FFFFFF;
        l2 >>= 26;
        nArray[2] = (int)(l2 += (long)nArray[2]) & 0x1FFFFFF;
        l2 >>= 25;
        nArray[3] = (int)(l2 += (long)nArray[3]) & 0x3FFFFFF;
        l2 >>= 26;
        nArray[4] = (int)(l2 += (long)nArray[4]) & 0x1FFFFFF;
        l2 >>= 25;
        nArray[5] = (int)(l2 += (long)nArray[5]) & 0x3FFFFFF;
        l2 >>= 26;
        nArray[6] = (int)(l2 += (long)nArray[6]) & 0x3FFFFFF;
        l2 >>= 26;
        nArray[7] = (int)(l2 += (long)nArray[7]) & 0x1FFFFFF;
        l2 >>= 25;
        nArray[8] = (int)(l2 += (long)nArray[8]) & 0x3FFFFFF;
        nArray[9] = n4 + (int)(l2 >>= 26);
    }

    public static void sqr(int[] nArray, int[] nArray2) {
        int n2 = nArray[0];
        int n3 = nArray[1];
        int n4 = nArray[2];
        int n5 = nArray[3];
        int n6 = nArray[4];
        int n7 = nArray[5];
        int n8 = nArray[6];
        int n9 = nArray[7];
        int n10 = nArray[8];
        int n11 = nArray[9];
        int n12 = n3 * 2;
        int n13 = n4 * 2;
        int n14 = n5 * 2;
        int n15 = n6 * 2;
        long l2 = (long)n2 * (long)n2;
        long l3 = (long)n2 * (long)n12;
        long l4 = (long)n2 * (long)n13 + (long)n3 * (long)n3;
        long l5 = (long)n12 * (long)n13 + (long)n2 * (long)n14;
        long l6 = (long)n4 * (long)n13 + (long)n2 * (long)n15 + (long)n3 * (long)n14;
        long l7 = (long)n12 * (long)n15 + (long)n13 * (long)n14;
        long l8 = (long)n13 * (long)n15 + (long)n5 * (long)n5;
        long l9 = (long)n5 * (long)n15;
        long l10 = (long)n6 * (long)n15;
        int n16 = n8 * 2;
        int n17 = n9 * 2;
        int n18 = n10 * 2;
        int n19 = n11 * 2;
        long l11 = (long)n7 * (long)n7;
        long l12 = (long)n7 * (long)n16;
        long l13 = (long)n7 * (long)n17 + (long)n8 * (long)n8;
        long l14 = (long)n16 * (long)n17 + (long)n7 * (long)n18;
        long l15 = (long)n9 * (long)n17 + (long)n7 * (long)n19 + (long)n8 * (long)n18;
        long l16 = (long)n16 * (long)n19 + (long)n17 * (long)n18;
        long l17 = (long)n17 * (long)n19 + (long)n10 * (long)n10;
        long l18 = (long)n10 * (long)n19;
        long l19 = (long)n11 * (long)n19;
        l2 -= l16 * 38L;
        l3 -= l17 * 38L;
        l4 -= l18 * 38L;
        l5 -= l19 * 38L;
        l7 -= l11;
        l8 -= l12;
        l9 -= l13;
        l10 -= l14;
        n12 = (n3 += n8) * 2;
        n13 = (n4 += n9) * 2;
        n14 = (n5 += n10) * 2;
        n15 = (n6 += n11) * 2;
        long l20 = (long)(n2 += n7) * (long)n2;
        long l21 = (long)n2 * (long)n12;
        long l22 = (long)n2 * (long)n13 + (long)n3 * (long)n3;
        long l23 = (long)n12 * (long)n13 + (long)n2 * (long)n14;
        long l24 = (long)n4 * (long)n13 + (long)n2 * (long)n15 + (long)n3 * (long)n14;
        long l25 = (long)n12 * (long)n15 + (long)n13 * (long)n14;
        long l26 = (long)n13 * (long)n15 + (long)n5 * (long)n5;
        long l27 = (long)n5 * (long)n15;
        long l28 = (long)n6 * (long)n15;
        long l29 = l10 + (l23 - l5);
        int n20 = (int)l29 & 0x3FFFFFF;
        l29 >>= 26;
        int n21 = (int)(l29 += l24 - l6 - l15) & 0x1FFFFFF;
        l29 >>= 25;
        l29 = l2 + (l29 + l25 - l7) * 38L;
        nArray2[0] = (int)l29 & 0x3FFFFFF;
        l29 >>= 26;
        nArray2[1] = (int)(l29 += l3 + (l26 - l8) * 38L) & 0x3FFFFFF;
        l29 >>= 26;
        nArray2[2] = (int)(l29 += l4 + (l27 - l9) * 38L) & 0x1FFFFFF;
        l29 >>= 25;
        nArray2[3] = (int)(l29 += l5 + (l28 - l10) * 38L) & 0x3FFFFFF;
        l29 >>= 26;
        nArray2[4] = (int)(l29 += l6 + l15 * 38L) & 0x1FFFFFF;
        l29 >>= 25;
        nArray2[5] = (int)(l29 += l7 + (l20 - l2)) & 0x3FFFFFF;
        l29 >>= 26;
        nArray2[6] = (int)(l29 += l8 + (l21 - l3)) & 0x3FFFFFF;
        l29 >>= 26;
        nArray2[7] = (int)(l29 += l9 + (l22 - l4)) & 0x1FFFFFF;
        l29 >>= 25;
        nArray2[8] = (int)(l29 += (long)n20) & 0x3FFFFFF;
        nArray2[9] = n21 + (int)(l29 >>= 26);
    }

    public static void sqr(int[] nArray, int n2, int[] nArray2) {
        X25519Field.sqr(nArray, nArray2);
        while (--n2 > 0) {
            X25519Field.sqr(nArray2, nArray2);
        }
    }

    public static boolean sqrtRatioVar(int[] nArray, int[] nArray2, int[] nArray3) {
        int[] nArray4 = X25519Field.create();
        int[] nArray5 = X25519Field.create();
        X25519Field.mul(nArray, nArray2, nArray4);
        X25519Field.sqr(nArray2, nArray5);
        X25519Field.mul(nArray4, nArray5, nArray4);
        X25519Field.sqr(nArray5, nArray5);
        X25519Field.mul(nArray5, nArray4, nArray5);
        int[] nArray6 = X25519Field.create();
        int[] nArray7 = X25519Field.create();
        X25519Field.powPm5d8(nArray5, nArray6, nArray7);
        X25519Field.mul(nArray7, nArray4, nArray7);
        int[] nArray8 = X25519Field.create();
        X25519Field.sqr(nArray7, nArray8);
        X25519Field.mul(nArray8, nArray2, nArray8);
        X25519Field.sub(nArray8, nArray, nArray6);
        X25519Field.normalize(nArray6);
        if (X25519Field.isZeroVar(nArray6)) {
            X25519Field.copy(nArray7, 0, nArray3, 0);
            return true;
        }
        X25519Field.add(nArray8, nArray, nArray6);
        X25519Field.normalize(nArray6);
        if (X25519Field.isZeroVar(nArray6)) {
            X25519Field.mul(nArray7, ROOT_NEG_ONE, nArray3);
            return true;
        }
        return false;
    }

    public static void sub(int[] nArray, int[] nArray2, int[] nArray3) {
        for (int i2 = 0; i2 < 10; ++i2) {
            nArray3[i2] = nArray[i2] - nArray2[i2];
        }
    }

    public static void subOne(int[] nArray) {
        nArray[0] = nArray[0] - 1;
    }

    public static void zero(int[] nArray) {
        for (int i2 = 0; i2 < 10; ++i2) {
            nArray[i2] = 0;
        }
    }
}

