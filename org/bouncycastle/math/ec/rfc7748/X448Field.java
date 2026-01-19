/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.math.ec.rfc7748;

import org.bouncycastle.math.raw.Mod;

public abstract class X448Field {
    public static final int SIZE = 16;
    private static final int M28 = 0xFFFFFFF;
    private static final long U32 = 0xFFFFFFFFL;
    private static final int[] P32 = new int[]{-1, -1, -1, -1, -1, -1, -1, -2, -1, -1, -1, -1, -1, -1};

    protected X448Field() {
    }

    public static void add(int[] nArray, int[] nArray2, int[] nArray3) {
        for (int i2 = 0; i2 < 16; ++i2) {
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

    public static int areEqual(int[] nArray, int[] nArray2) {
        int n2 = 0;
        for (int i2 = 0; i2 < 16; ++i2) {
            n2 |= nArray[i2] ^ nArray2[i2];
        }
        n2 = n2 >>> 1 | n2 & 1;
        return n2 - 1 >> 31;
    }

    public static boolean areEqualVar(int[] nArray, int[] nArray2) {
        return 0 != X448Field.areEqual(nArray, nArray2);
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
        int n12 = nArray[10];
        int n13 = nArray[11];
        int n14 = nArray[12];
        int n15 = nArray[13];
        int n16 = nArray[14];
        int n17 = nArray[15];
        n3 += n2 >>> 28;
        n2 &= 0xFFFFFFF;
        n7 += n6 >>> 28;
        n6 &= 0xFFFFFFF;
        n11 += n10 >>> 28;
        n10 &= 0xFFFFFFF;
        n15 += n14 >>> 28;
        n14 &= 0xFFFFFFF;
        n4 += n3 >>> 28;
        n3 &= 0xFFFFFFF;
        n8 += n7 >>> 28;
        n7 &= 0xFFFFFFF;
        n12 += n11 >>> 28;
        n11 &= 0xFFFFFFF;
        n16 += n15 >>> 28;
        n15 &= 0xFFFFFFF;
        n5 += n4 >>> 28;
        n4 &= 0xFFFFFFF;
        n9 += n8 >>> 28;
        n8 &= 0xFFFFFFF;
        n13 += n12 >>> 28;
        n12 &= 0xFFFFFFF;
        n17 += n16 >>> 28;
        n16 &= 0xFFFFFFF;
        int n18 = n17 >>> 28;
        n17 &= 0xFFFFFFF;
        n2 += n18;
        n10 += n18;
        n6 += n5 >>> 28;
        n5 &= 0xFFFFFFF;
        n10 += n9 >>> 28;
        n9 &= 0xFFFFFFF;
        n14 += n13 >>> 28;
        n13 &= 0xFFFFFFF;
        n3 += n2 >>> 28;
        n2 &= 0xFFFFFFF;
        n7 += n6 >>> 28;
        n6 &= 0xFFFFFFF;
        n11 += n10 >>> 28;
        n10 &= 0xFFFFFFF;
        n15 += n14 >>> 28;
        n14 &= 0xFFFFFFF;
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
        nArray[10] = n12;
        nArray[11] = n13;
        nArray[12] = n14;
        nArray[13] = n15;
        nArray[14] = n16;
        nArray[15] = n17;
    }

    public static void cmov(int n2, int[] nArray, int n3, int[] nArray2, int n4) {
        for (int i2 = 0; i2 < 16; ++i2) {
            int n5 = nArray2[n4 + i2];
            int n6 = n5 ^ nArray[n3 + i2];
            nArray2[n4 + i2] = n5 ^= n6 & n2;
        }
    }

    public static void cnegate(int n2, int[] nArray) {
        int[] nArray2 = X448Field.create();
        X448Field.sub(nArray2, nArray, nArray2);
        X448Field.cmov(-n2, nArray2, 0, nArray, 0);
    }

    public static void copy(int[] nArray, int n2, int[] nArray2, int n3) {
        for (int i2 = 0; i2 < 16; ++i2) {
            nArray2[n3 + i2] = nArray[n2 + i2];
        }
    }

    public static int[] create() {
        return new int[16];
    }

    public static int[] createTable(int n2) {
        return new int[16 * n2];
    }

    public static void cswap(int n2, int[] nArray, int[] nArray2) {
        int n3 = 0 - n2;
        for (int i2 = 0; i2 < 16; ++i2) {
            int n4 = nArray[i2];
            int n5 = nArray2[i2];
            int n6 = n3 & (n4 ^ n5);
            nArray[i2] = n4 ^ n6;
            nArray2[i2] = n5 ^ n6;
        }
    }

    public static void decode(int[] nArray, int n2, int[] nArray2) {
        X448Field.decode224(nArray, n2, nArray2, 0);
        X448Field.decode224(nArray, n2 + 7, nArray2, 8);
    }

    public static void decode(byte[] byArray, int[] nArray) {
        X448Field.decode56(byArray, 0, nArray, 0);
        X448Field.decode56(byArray, 7, nArray, 2);
        X448Field.decode56(byArray, 14, nArray, 4);
        X448Field.decode56(byArray, 21, nArray, 6);
        X448Field.decode56(byArray, 28, nArray, 8);
        X448Field.decode56(byArray, 35, nArray, 10);
        X448Field.decode56(byArray, 42, nArray, 12);
        X448Field.decode56(byArray, 49, nArray, 14);
    }

    public static void decode(byte[] byArray, int n2, int[] nArray) {
        X448Field.decode56(byArray, n2, nArray, 0);
        X448Field.decode56(byArray, n2 + 7, nArray, 2);
        X448Field.decode56(byArray, n2 + 14, nArray, 4);
        X448Field.decode56(byArray, n2 + 21, nArray, 6);
        X448Field.decode56(byArray, n2 + 28, nArray, 8);
        X448Field.decode56(byArray, n2 + 35, nArray, 10);
        X448Field.decode56(byArray, n2 + 42, nArray, 12);
        X448Field.decode56(byArray, n2 + 49, nArray, 14);
    }

    public static void decode(byte[] byArray, int n2, int[] nArray, int n3) {
        X448Field.decode56(byArray, n2, nArray, n3);
        X448Field.decode56(byArray, n2 + 7, nArray, n3 + 2);
        X448Field.decode56(byArray, n2 + 14, nArray, n3 + 4);
        X448Field.decode56(byArray, n2 + 21, nArray, n3 + 6);
        X448Field.decode56(byArray, n2 + 28, nArray, n3 + 8);
        X448Field.decode56(byArray, n2 + 35, nArray, n3 + 10);
        X448Field.decode56(byArray, n2 + 42, nArray, n3 + 12);
        X448Field.decode56(byArray, n2 + 49, nArray, n3 + 14);
    }

    private static void decode224(int[] nArray, int n2, int[] nArray2, int n3) {
        int n4 = nArray[n2 + 0];
        int n5 = nArray[n2 + 1];
        int n6 = nArray[n2 + 2];
        int n7 = nArray[n2 + 3];
        int n8 = nArray[n2 + 4];
        int n9 = nArray[n2 + 5];
        int n10 = nArray[n2 + 6];
        nArray2[n3 + 0] = n4 & 0xFFFFFFF;
        nArray2[n3 + 1] = (n4 >>> 28 | n5 << 4) & 0xFFFFFFF;
        nArray2[n3 + 2] = (n5 >>> 24 | n6 << 8) & 0xFFFFFFF;
        nArray2[n3 + 3] = (n6 >>> 20 | n7 << 12) & 0xFFFFFFF;
        nArray2[n3 + 4] = (n7 >>> 16 | n8 << 16) & 0xFFFFFFF;
        nArray2[n3 + 5] = (n8 >>> 12 | n9 << 20) & 0xFFFFFFF;
        nArray2[n3 + 6] = (n9 >>> 8 | n10 << 24) & 0xFFFFFFF;
        nArray2[n3 + 7] = n10 >>> 4;
    }

    private static int decode24(byte[] byArray, int n2) {
        int n3 = byArray[n2] & 0xFF;
        n3 |= (byArray[++n2] & 0xFF) << 8;
        return n3 |= (byArray[++n2] & 0xFF) << 16;
    }

    private static int decode32(byte[] byArray, int n2) {
        int n3 = byArray[n2] & 0xFF;
        n3 |= (byArray[++n2] & 0xFF) << 8;
        n3 |= (byArray[++n2] & 0xFF) << 16;
        return n3 |= byArray[++n2] << 24;
    }

    private static void decode56(byte[] byArray, int n2, int[] nArray, int n3) {
        int n4 = X448Field.decode32(byArray, n2);
        int n5 = X448Field.decode24(byArray, n2 + 4);
        nArray[n3] = n4 & 0xFFFFFFF;
        nArray[n3 + 1] = n4 >>> 28 | n5 << 4;
    }

    public static void encode(int[] nArray, int[] nArray2, int n2) {
        X448Field.encode224(nArray, 0, nArray2, n2);
        X448Field.encode224(nArray, 8, nArray2, n2 + 7);
    }

    public static void encode(int[] nArray, byte[] byArray) {
        X448Field.encode56(nArray, 0, byArray, 0);
        X448Field.encode56(nArray, 2, byArray, 7);
        X448Field.encode56(nArray, 4, byArray, 14);
        X448Field.encode56(nArray, 6, byArray, 21);
        X448Field.encode56(nArray, 8, byArray, 28);
        X448Field.encode56(nArray, 10, byArray, 35);
        X448Field.encode56(nArray, 12, byArray, 42);
        X448Field.encode56(nArray, 14, byArray, 49);
    }

    public static void encode(int[] nArray, byte[] byArray, int n2) {
        X448Field.encode56(nArray, 0, byArray, n2);
        X448Field.encode56(nArray, 2, byArray, n2 + 7);
        X448Field.encode56(nArray, 4, byArray, n2 + 14);
        X448Field.encode56(nArray, 6, byArray, n2 + 21);
        X448Field.encode56(nArray, 8, byArray, n2 + 28);
        X448Field.encode56(nArray, 10, byArray, n2 + 35);
        X448Field.encode56(nArray, 12, byArray, n2 + 42);
        X448Field.encode56(nArray, 14, byArray, n2 + 49);
    }

    public static void encode(int[] nArray, int n2, byte[] byArray, int n3) {
        X448Field.encode56(nArray, n2, byArray, n3);
        X448Field.encode56(nArray, n2 + 2, byArray, n3 + 7);
        X448Field.encode56(nArray, n2 + 4, byArray, n3 + 14);
        X448Field.encode56(nArray, n2 + 6, byArray, n3 + 21);
        X448Field.encode56(nArray, n2 + 8, byArray, n3 + 28);
        X448Field.encode56(nArray, n2 + 10, byArray, n3 + 35);
        X448Field.encode56(nArray, n2 + 12, byArray, n3 + 42);
        X448Field.encode56(nArray, n2 + 14, byArray, n3 + 49);
    }

    private static void encode224(int[] nArray, int n2, int[] nArray2, int n3) {
        int n4 = nArray[n2 + 0];
        int n5 = nArray[n2 + 1];
        int n6 = nArray[n2 + 2];
        int n7 = nArray[n2 + 3];
        int n8 = nArray[n2 + 4];
        int n9 = nArray[n2 + 5];
        int n10 = nArray[n2 + 6];
        int n11 = nArray[n2 + 7];
        nArray2[n3 + 0] = n4 | n5 << 28;
        nArray2[n3 + 1] = n5 >>> 4 | n6 << 24;
        nArray2[n3 + 2] = n6 >>> 8 | n7 << 20;
        nArray2[n3 + 3] = n7 >>> 12 | n8 << 16;
        nArray2[n3 + 4] = n8 >>> 16 | n9 << 12;
        nArray2[n3 + 5] = n9 >>> 20 | n10 << 8;
        nArray2[n3 + 6] = n10 >>> 24 | n11 << 4;
    }

    private static void encode24(int n2, byte[] byArray, int n3) {
        byArray[n3] = (byte)n2;
        byArray[++n3] = (byte)(n2 >>> 8);
        byArray[++n3] = (byte)(n2 >>> 16);
    }

    private static void encode32(int n2, byte[] byArray, int n3) {
        byArray[n3] = (byte)n2;
        byArray[++n3] = (byte)(n2 >>> 8);
        byArray[++n3] = (byte)(n2 >>> 16);
        byArray[++n3] = (byte)(n2 >>> 24);
    }

    private static void encode56(int[] nArray, int n2, byte[] byArray, int n3) {
        int n4 = nArray[n2];
        int n5 = nArray[n2 + 1];
        X448Field.encode32(n4 | n5 << 28, byArray, n3);
        X448Field.encode24(n5 >>> 4, byArray, n3 + 4);
    }

    public static void inv(int[] nArray, int[] nArray2) {
        int[] nArray3 = X448Field.create();
        int[] nArray4 = new int[14];
        X448Field.copy(nArray, 0, nArray3, 0);
        X448Field.normalize(nArray3);
        X448Field.encode(nArray3, nArray4, 0);
        Mod.modOddInverse(P32, nArray4, nArray4);
        X448Field.decode(nArray4, 0, nArray2);
    }

    public static void invVar(int[] nArray, int[] nArray2) {
        int[] nArray3 = X448Field.create();
        int[] nArray4 = new int[14];
        X448Field.copy(nArray, 0, nArray3, 0);
        X448Field.normalize(nArray3);
        X448Field.encode(nArray3, nArray4, 0);
        Mod.modOddInverseVar(P32, nArray4, nArray4);
        X448Field.decode(nArray4, 0, nArray2);
    }

    public static int isOne(int[] nArray) {
        int n2 = nArray[0] ^ 1;
        for (int i2 = 1; i2 < 16; ++i2) {
            n2 |= nArray[i2];
        }
        n2 = n2 >>> 1 | n2 & 1;
        return n2 - 1 >> 31;
    }

    public static boolean isOneVar(int[] nArray) {
        return 0 != X448Field.isOne(nArray);
    }

    public static int isZero(int[] nArray) {
        int n2 = 0;
        for (int i2 = 0; i2 < 16; ++i2) {
            n2 |= nArray[i2];
        }
        n2 = n2 >>> 1 | n2 & 1;
        return n2 - 1 >> 31;
    }

    public static boolean isZeroVar(int[] nArray) {
        return 0 != X448Field.isZero(nArray);
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
        int n13 = nArray[10];
        int n14 = nArray[11];
        int n15 = nArray[12];
        int n16 = nArray[13];
        int n17 = nArray[14];
        int n18 = nArray[15];
        long l2 = (long)n4 * (long)n2;
        int n19 = (int)l2 & 0xFFFFFFF;
        l2 >>>= 28;
        long l3 = (long)n8 * (long)n2;
        int n20 = (int)l3 & 0xFFFFFFF;
        l3 >>>= 28;
        long l4 = (long)n12 * (long)n2;
        int n21 = (int)l4 & 0xFFFFFFF;
        l4 >>>= 28;
        long l5 = (long)n16 * (long)n2;
        int n22 = (int)l5 & 0xFFFFFFF;
        l5 >>>= 28;
        nArray2[2] = (int)(l2 += (long)n5 * (long)n2) & 0xFFFFFFF;
        l2 >>>= 28;
        nArray2[6] = (int)(l3 += (long)n9 * (long)n2) & 0xFFFFFFF;
        l3 >>>= 28;
        nArray2[10] = (int)(l4 += (long)n13 * (long)n2) & 0xFFFFFFF;
        l4 >>>= 28;
        nArray2[14] = (int)(l5 += (long)n17 * (long)n2) & 0xFFFFFFF;
        l5 >>>= 28;
        nArray2[3] = (int)(l2 += (long)n6 * (long)n2) & 0xFFFFFFF;
        l2 >>>= 28;
        nArray2[7] = (int)(l3 += (long)n10 * (long)n2) & 0xFFFFFFF;
        l3 >>>= 28;
        nArray2[11] = (int)(l4 += (long)n14 * (long)n2) & 0xFFFFFFF;
        l4 >>>= 28;
        nArray2[15] = (int)(l5 += (long)n18 * (long)n2) & 0xFFFFFFF;
        l3 += (l5 >>>= 28);
        nArray2[4] = (int)(l2 += (long)n7 * (long)n2) & 0xFFFFFFF;
        l2 >>>= 28;
        nArray2[8] = (int)(l3 += (long)n11 * (long)n2) & 0xFFFFFFF;
        nArray2[12] = (int)(l4 += (long)n15 * (long)n2) & 0xFFFFFFF;
        nArray2[0] = (int)(l5 += (long)n3 * (long)n2) & 0xFFFFFFF;
        nArray2[1] = n19 + (int)(l5 >>>= 28);
        nArray2[5] = n20 + (int)l2;
        nArray2[9] = n21 + (int)(l3 >>>= 28);
        nArray2[13] = n22 + (int)(l4 >>>= 28);
    }

    public static void mul(int[] nArray, int[] nArray2, int[] nArray3) {
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
        int n12 = nArray[10];
        int n13 = nArray[11];
        int n14 = nArray[12];
        int n15 = nArray[13];
        int n16 = nArray[14];
        int n17 = nArray[15];
        int n18 = nArray2[0];
        int n19 = nArray2[1];
        int n20 = nArray2[2];
        int n21 = nArray2[3];
        int n22 = nArray2[4];
        int n23 = nArray2[5];
        int n24 = nArray2[6];
        int n25 = nArray2[7];
        int n26 = nArray2[8];
        int n27 = nArray2[9];
        int n28 = nArray2[10];
        int n29 = nArray2[11];
        int n30 = nArray2[12];
        int n31 = nArray2[13];
        int n32 = nArray2[14];
        int n33 = nArray2[15];
        int n34 = n2 + n10;
        int n35 = n3 + n11;
        int n36 = n4 + n12;
        int n37 = n5 + n13;
        int n38 = n6 + n14;
        int n39 = n7 + n15;
        int n40 = n8 + n16;
        int n41 = n9 + n17;
        int n42 = n18 + n26;
        int n43 = n19 + n27;
        int n44 = n20 + n28;
        int n45 = n21 + n29;
        int n46 = n22 + n30;
        int n47 = n23 + n31;
        int n48 = n24 + n32;
        int n49 = n25 + n33;
        long l2 = (long)n2 * (long)n18;
        long l3 = (long)n9 * (long)n19 + (long)n8 * (long)n20 + (long)n7 * (long)n21 + (long)n6 * (long)n22 + (long)n5 * (long)n23 + (long)n4 * (long)n24 + (long)n3 * (long)n25;
        long l4 = (long)n10 * (long)n26;
        long l5 = (long)n17 * (long)n27 + (long)n16 * (long)n28 + (long)n15 * (long)n29 + (long)n14 * (long)n30 + (long)n13 * (long)n31 + (long)n12 * (long)n32 + (long)n11 * (long)n33;
        long l6 = (long)n34 * (long)n42;
        long l7 = (long)n41 * (long)n43 + (long)n40 * (long)n44 + (long)n39 * (long)n45 + (long)n38 * (long)n46 + (long)n37 * (long)n47 + (long)n36 * (long)n48 + (long)n35 * (long)n49;
        long l8 = l2 + l4 + l7 - l3;
        int n50 = (int)l8 & 0xFFFFFFF;
        l8 >>>= 28;
        long l9 = l5 + l6 - l2 + l7;
        int n51 = (int)l9 & 0xFFFFFFF;
        l9 >>>= 28;
        long l10 = (long)n3 * (long)n18 + (long)n2 * (long)n19;
        long l11 = (long)n9 * (long)n20 + (long)n8 * (long)n21 + (long)n7 * (long)n22 + (long)n6 * (long)n23 + (long)n5 * (long)n24 + (long)n4 * (long)n25;
        long l12 = (long)n11 * (long)n26 + (long)n10 * (long)n27;
        long l13 = (long)n17 * (long)n28 + (long)n16 * (long)n29 + (long)n15 * (long)n30 + (long)n14 * (long)n31 + (long)n13 * (long)n32 + (long)n12 * (long)n33;
        long l14 = (long)n35 * (long)n42 + (long)n34 * (long)n43;
        long l15 = (long)n41 * (long)n44 + (long)n40 * (long)n45 + (long)n39 * (long)n46 + (long)n38 * (long)n47 + (long)n37 * (long)n48 + (long)n36 * (long)n49;
        int n52 = (int)(l8 += l10 + l12 + l15 - l11) & 0xFFFFFFF;
        l8 >>>= 28;
        int n53 = (int)(l9 += l13 + l14 - l10 + l15) & 0xFFFFFFF;
        l9 >>>= 28;
        long l16 = (long)n4 * (long)n18 + (long)n3 * (long)n19 + (long)n2 * (long)n20;
        long l17 = (long)n9 * (long)n21 + (long)n8 * (long)n22 + (long)n7 * (long)n23 + (long)n6 * (long)n24 + (long)n5 * (long)n25;
        long l18 = (long)n12 * (long)n26 + (long)n11 * (long)n27 + (long)n10 * (long)n28;
        long l19 = (long)n17 * (long)n29 + (long)n16 * (long)n30 + (long)n15 * (long)n31 + (long)n14 * (long)n32 + (long)n13 * (long)n33;
        long l20 = (long)n36 * (long)n42 + (long)n35 * (long)n43 + (long)n34 * (long)n44;
        long l21 = (long)n41 * (long)n45 + (long)n40 * (long)n46 + (long)n39 * (long)n47 + (long)n38 * (long)n48 + (long)n37 * (long)n49;
        int n54 = (int)(l8 += l16 + l18 + l21 - l17) & 0xFFFFFFF;
        l8 >>>= 28;
        int n55 = (int)(l9 += l19 + l20 - l16 + l21) & 0xFFFFFFF;
        l9 >>>= 28;
        long l22 = (long)n5 * (long)n18 + (long)n4 * (long)n19 + (long)n3 * (long)n20 + (long)n2 * (long)n21;
        long l23 = (long)n9 * (long)n22 + (long)n8 * (long)n23 + (long)n7 * (long)n24 + (long)n6 * (long)n25;
        long l24 = (long)n13 * (long)n26 + (long)n12 * (long)n27 + (long)n11 * (long)n28 + (long)n10 * (long)n29;
        long l25 = (long)n17 * (long)n30 + (long)n16 * (long)n31 + (long)n15 * (long)n32 + (long)n14 * (long)n33;
        long l26 = (long)n37 * (long)n42 + (long)n36 * (long)n43 + (long)n35 * (long)n44 + (long)n34 * (long)n45;
        long l27 = (long)n41 * (long)n46 + (long)n40 * (long)n47 + (long)n39 * (long)n48 + (long)n38 * (long)n49;
        int n56 = (int)(l8 += l22 + l24 + l27 - l23) & 0xFFFFFFF;
        l8 >>>= 28;
        int n57 = (int)(l9 += l25 + l26 - l22 + l27) & 0xFFFFFFF;
        l9 >>>= 28;
        long l28 = (long)n6 * (long)n18 + (long)n5 * (long)n19 + (long)n4 * (long)n20 + (long)n3 * (long)n21 + (long)n2 * (long)n22;
        long l29 = (long)n9 * (long)n23 + (long)n8 * (long)n24 + (long)n7 * (long)n25;
        long l30 = (long)n14 * (long)n26 + (long)n13 * (long)n27 + (long)n12 * (long)n28 + (long)n11 * (long)n29 + (long)n10 * (long)n30;
        long l31 = (long)n17 * (long)n31 + (long)n16 * (long)n32 + (long)n15 * (long)n33;
        long l32 = (long)n38 * (long)n42 + (long)n37 * (long)n43 + (long)n36 * (long)n44 + (long)n35 * (long)n45 + (long)n34 * (long)n46;
        long l33 = (long)n41 * (long)n47 + (long)n40 * (long)n48 + (long)n39 * (long)n49;
        int n58 = (int)(l8 += l28 + l30 + l33 - l29) & 0xFFFFFFF;
        l8 >>>= 28;
        int n59 = (int)(l9 += l31 + l32 - l28 + l33) & 0xFFFFFFF;
        l9 >>>= 28;
        long l34 = (long)n7 * (long)n18 + (long)n6 * (long)n19 + (long)n5 * (long)n20 + (long)n4 * (long)n21 + (long)n3 * (long)n22 + (long)n2 * (long)n23;
        long l35 = (long)n9 * (long)n24 + (long)n8 * (long)n25;
        long l36 = (long)n15 * (long)n26 + (long)n14 * (long)n27 + (long)n13 * (long)n28 + (long)n12 * (long)n29 + (long)n11 * (long)n30 + (long)n10 * (long)n31;
        long l37 = (long)n17 * (long)n32 + (long)n16 * (long)n33;
        long l38 = (long)n39 * (long)n42 + (long)n38 * (long)n43 + (long)n37 * (long)n44 + (long)n36 * (long)n45 + (long)n35 * (long)n46 + (long)n34 * (long)n47;
        long l39 = (long)n41 * (long)n48 + (long)n40 * (long)n49;
        int n60 = (int)(l8 += l34 + l36 + l39 - l35) & 0xFFFFFFF;
        l8 >>>= 28;
        int n61 = (int)(l9 += l37 + l38 - l34 + l39) & 0xFFFFFFF;
        l9 >>>= 28;
        long l40 = (long)n8 * (long)n18 + (long)n7 * (long)n19 + (long)n6 * (long)n20 + (long)n5 * (long)n21 + (long)n4 * (long)n22 + (long)n3 * (long)n23 + (long)n2 * (long)n24;
        long l41 = (long)n9 * (long)n25;
        long l42 = (long)n16 * (long)n26 + (long)n15 * (long)n27 + (long)n14 * (long)n28 + (long)n13 * (long)n29 + (long)n12 * (long)n30 + (long)n11 * (long)n31 + (long)n10 * (long)n32;
        long l43 = (long)n17 * (long)n33;
        long l44 = (long)n40 * (long)n42 + (long)n39 * (long)n43 + (long)n38 * (long)n44 + (long)n37 * (long)n45 + (long)n36 * (long)n46 + (long)n35 * (long)n47 + (long)n34 * (long)n48;
        long l45 = (long)n41 * (long)n49;
        int n62 = (int)(l8 += l40 + l42 + l45 - l41) & 0xFFFFFFF;
        l8 >>>= 28;
        int n63 = (int)(l9 += l43 + l44 - l40 + l45) & 0xFFFFFFF;
        l9 >>>= 28;
        long l46 = (long)n9 * (long)n18 + (long)n8 * (long)n19 + (long)n7 * (long)n20 + (long)n6 * (long)n21 + (long)n5 * (long)n22 + (long)n4 * (long)n23 + (long)n3 * (long)n24 + (long)n2 * (long)n25;
        long l47 = (long)n17 * (long)n26 + (long)n16 * (long)n27 + (long)n15 * (long)n28 + (long)n14 * (long)n29 + (long)n13 * (long)n30 + (long)n12 * (long)n31 + (long)n11 * (long)n32 + (long)n10 * (long)n33;
        long l48 = (long)n41 * (long)n42 + (long)n40 * (long)n43 + (long)n39 * (long)n44 + (long)n38 * (long)n45 + (long)n37 * (long)n46 + (long)n36 * (long)n47 + (long)n35 * (long)n48 + (long)n34 * (long)n49;
        int n64 = (int)(l8 += l46 + l47) & 0xFFFFFFF;
        l8 >>>= 28;
        int n65 = (int)(l9 += l48 - l46) & 0xFFFFFFF;
        l8 += (l9 >>>= 28);
        l8 += (long)n51;
        n51 = (int)l8 & 0xFFFFFFF;
        l9 += (long)n50;
        n50 = (int)l9 & 0xFFFFFFF;
        n53 += (int)(l8 >>>= 28);
        nArray3[0] = n50;
        nArray3[1] = n52 += (int)(l9 >>>= 28);
        nArray3[2] = n54;
        nArray3[3] = n56;
        nArray3[4] = n58;
        nArray3[5] = n60;
        nArray3[6] = n62;
        nArray3[7] = n64;
        nArray3[8] = n51;
        nArray3[9] = n53;
        nArray3[10] = n55;
        nArray3[11] = n57;
        nArray3[12] = n59;
        nArray3[13] = n61;
        nArray3[14] = n63;
        nArray3[15] = n65;
    }

    public static void negate(int[] nArray, int[] nArray2) {
        int[] nArray3 = X448Field.create();
        X448Field.sub(nArray3, nArray, nArray2);
    }

    public static void normalize(int[] nArray) {
        X448Field.reduce(nArray, 1);
        X448Field.reduce(nArray, -1);
    }

    public static void one(int[] nArray) {
        nArray[0] = 1;
        for (int i2 = 1; i2 < 16; ++i2) {
            nArray[i2] = 0;
        }
    }

    private static void powPm3d4(int[] nArray, int[] nArray2) {
        int[] nArray3 = X448Field.create();
        X448Field.sqr(nArray, nArray3);
        X448Field.mul(nArray, nArray3, nArray3);
        int[] nArray4 = X448Field.create();
        X448Field.sqr(nArray3, nArray4);
        X448Field.mul(nArray, nArray4, nArray4);
        int[] nArray5 = X448Field.create();
        X448Field.sqr(nArray4, 3, nArray5);
        X448Field.mul(nArray4, nArray5, nArray5);
        int[] nArray6 = X448Field.create();
        X448Field.sqr(nArray5, 3, nArray6);
        X448Field.mul(nArray4, nArray6, nArray6);
        int[] nArray7 = X448Field.create();
        X448Field.sqr(nArray6, 9, nArray7);
        X448Field.mul(nArray6, nArray7, nArray7);
        int[] nArray8 = X448Field.create();
        X448Field.sqr(nArray7, nArray8);
        X448Field.mul(nArray, nArray8, nArray8);
        int[] nArray9 = X448Field.create();
        X448Field.sqr(nArray8, 18, nArray9);
        X448Field.mul(nArray7, nArray9, nArray9);
        int[] nArray10 = X448Field.create();
        X448Field.sqr(nArray9, 37, nArray10);
        X448Field.mul(nArray9, nArray10, nArray10);
        int[] nArray11 = X448Field.create();
        X448Field.sqr(nArray10, 37, nArray11);
        X448Field.mul(nArray9, nArray11, nArray11);
        int[] nArray12 = X448Field.create();
        X448Field.sqr(nArray11, 111, nArray12);
        X448Field.mul(nArray11, nArray12, nArray12);
        int[] nArray13 = X448Field.create();
        X448Field.sqr(nArray12, nArray13);
        X448Field.mul(nArray, nArray13, nArray13);
        int[] nArray14 = X448Field.create();
        X448Field.sqr(nArray13, 223, nArray14);
        X448Field.mul(nArray14, nArray12, nArray2);
    }

    private static void reduce(int[] nArray, int n2) {
        int n3;
        int n4 = nArray[15];
        int n5 = n4 & 0xFFFFFFF;
        n4 = (n4 >>> 28) + n2;
        long l2 = n4;
        for (n3 = 0; n3 < 8; ++n3) {
            nArray[n3] = (int)(l2 += (long)nArray[n3] & 0xFFFFFFFFL) & 0xFFFFFFF;
            l2 >>= 28;
        }
        l2 += (long)n4;
        for (n3 = 8; n3 < 15; ++n3) {
            nArray[n3] = (int)(l2 += (long)nArray[n3] & 0xFFFFFFFFL) & 0xFFFFFFF;
            l2 >>= 28;
        }
        nArray[15] = n5 + (int)l2;
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
        int n12 = nArray[10];
        int n13 = nArray[11];
        int n14 = nArray[12];
        int n15 = nArray[13];
        int n16 = nArray[14];
        int n17 = nArray[15];
        int n18 = n2 * 2;
        int n19 = n3 * 2;
        int n20 = n4 * 2;
        int n21 = n5 * 2;
        int n22 = n6 * 2;
        int n23 = n7 * 2;
        int n24 = n8 * 2;
        int n25 = n10 * 2;
        int n26 = n11 * 2;
        int n27 = n12 * 2;
        int n28 = n13 * 2;
        int n29 = n14 * 2;
        int n30 = n15 * 2;
        int n31 = n16 * 2;
        int n32 = n2 + n10;
        int n33 = n3 + n11;
        int n34 = n4 + n12;
        int n35 = n5 + n13;
        int n36 = n6 + n14;
        int n37 = n7 + n15;
        int n38 = n8 + n16;
        int n39 = n9 + n17;
        int n40 = n32 * 2;
        int n41 = n33 * 2;
        int n42 = n34 * 2;
        int n43 = n35 * 2;
        int n44 = n36 * 2;
        int n45 = n37 * 2;
        int n46 = n38 * 2;
        long l2 = (long)n2 * (long)n2;
        long l3 = (long)n9 * (long)n19 + (long)n8 * (long)n20 + (long)n7 * (long)n21 + (long)n6 * (long)n6;
        long l4 = (long)n10 * (long)n10;
        long l5 = (long)n17 * (long)n26 + (long)n16 * (long)n27 + (long)n15 * (long)n28 + (long)n14 * (long)n14;
        long l6 = (long)n32 * (long)n32;
        long l7 = (long)n39 * ((long)n41 & 0xFFFFFFFFL) + (long)n38 * ((long)n42 & 0xFFFFFFFFL) + (long)n37 * ((long)n43 & 0xFFFFFFFFL) + (long)n36 * (long)n36;
        long l8 = l2 + l4 + l7 - l3;
        int n47 = (int)l8 & 0xFFFFFFF;
        l8 >>>= 28;
        long l9 = l5 + l6 - l2 + l7;
        int n48 = (int)l9 & 0xFFFFFFF;
        l9 >>>= 28;
        long l10 = (long)n3 * (long)n18;
        long l11 = (long)n9 * (long)n20 + (long)n8 * (long)n21 + (long)n7 * (long)n22;
        long l12 = (long)n11 * (long)n25;
        long l13 = (long)n17 * (long)n27 + (long)n16 * (long)n28 + (long)n15 * (long)n29;
        long l14 = (long)n33 * ((long)n40 & 0xFFFFFFFFL);
        long l15 = (long)n39 * ((long)n42 & 0xFFFFFFFFL) + (long)n38 * ((long)n43 & 0xFFFFFFFFL) + (long)n37 * ((long)n44 & 0xFFFFFFFFL);
        int n49 = (int)(l8 += l10 + l12 + l15 - l11) & 0xFFFFFFF;
        l8 >>>= 28;
        int n50 = (int)(l9 += l13 + l14 - l10 + l15) & 0xFFFFFFF;
        l9 >>>= 28;
        long l16 = (long)n4 * (long)n18 + (long)n3 * (long)n3;
        long l17 = (long)n9 * (long)n21 + (long)n8 * (long)n22 + (long)n7 * (long)n7;
        long l18 = (long)n12 * (long)n25 + (long)n11 * (long)n11;
        long l19 = (long)n17 * (long)n28 + (long)n16 * (long)n29 + (long)n15 * (long)n15;
        long l20 = (long)n34 * ((long)n40 & 0xFFFFFFFFL) + (long)n33 * (long)n33;
        long l21 = (long)n39 * ((long)n43 & 0xFFFFFFFFL) + (long)n38 * ((long)n44 & 0xFFFFFFFFL) + (long)n37 * (long)n37;
        int n51 = (int)(l8 += l16 + l18 + l21 - l17) & 0xFFFFFFF;
        l8 >>>= 28;
        int n52 = (int)(l9 += l19 + l20 - l16 + l21) & 0xFFFFFFF;
        l9 >>>= 28;
        long l22 = (long)n5 * (long)n18 + (long)n4 * (long)n19;
        long l23 = (long)n9 * (long)n22 + (long)n8 * (long)n23;
        long l24 = (long)n13 * (long)n25 + (long)n12 * (long)n26;
        long l25 = (long)n17 * (long)n29 + (long)n16 * (long)n30;
        long l26 = (long)n35 * ((long)n40 & 0xFFFFFFFFL) + (long)n34 * ((long)n41 & 0xFFFFFFFFL);
        long l27 = (long)n39 * ((long)n44 & 0xFFFFFFFFL) + (long)n38 * ((long)n45 & 0xFFFFFFFFL);
        int n53 = (int)(l8 += l22 + l24 + l27 - l23) & 0xFFFFFFF;
        l8 >>>= 28;
        int n54 = (int)(l9 += l25 + l26 - l22 + l27) & 0xFFFFFFF;
        l9 >>>= 28;
        long l28 = (long)n6 * (long)n18 + (long)n5 * (long)n19 + (long)n4 * (long)n4;
        long l29 = (long)n9 * (long)n23 + (long)n8 * (long)n8;
        long l30 = (long)n14 * (long)n25 + (long)n13 * (long)n26 + (long)n12 * (long)n12;
        long l31 = (long)n17 * (long)n30 + (long)n16 * (long)n16;
        long l32 = (long)n36 * ((long)n40 & 0xFFFFFFFFL) + (long)n35 * ((long)n41 & 0xFFFFFFFFL) + (long)n34 * (long)n34;
        long l33 = (long)n39 * ((long)n45 & 0xFFFFFFFFL) + (long)n38 * (long)n38;
        int n55 = (int)(l8 += l28 + l30 + l33 - l29) & 0xFFFFFFF;
        l8 >>>= 28;
        int n56 = (int)(l9 += l31 + l32 - l28 + l33) & 0xFFFFFFF;
        l9 >>>= 28;
        long l34 = (long)n7 * (long)n18 + (long)n6 * (long)n19 + (long)n5 * (long)n20;
        long l35 = (long)n9 * (long)n24;
        long l36 = (long)n15 * (long)n25 + (long)n14 * (long)n26 + (long)n13 * (long)n27;
        long l37 = (long)n17 * (long)n31;
        long l38 = (long)n37 * ((long)n40 & 0xFFFFFFFFL) + (long)n36 * ((long)n41 & 0xFFFFFFFFL) + (long)n35 * ((long)n42 & 0xFFFFFFFFL);
        long l39 = (long)n39 * ((long)n46 & 0xFFFFFFFFL);
        int n57 = (int)(l8 += l34 + l36 + l39 - l35) & 0xFFFFFFF;
        l8 >>>= 28;
        int n58 = (int)(l9 += l37 + l38 - l34 + l39) & 0xFFFFFFF;
        l9 >>>= 28;
        long l40 = (long)n8 * (long)n18 + (long)n7 * (long)n19 + (long)n6 * (long)n20 + (long)n5 * (long)n5;
        long l41 = (long)n9 * (long)n9;
        long l42 = (long)n16 * (long)n25 + (long)n15 * (long)n26 + (long)n14 * (long)n27 + (long)n13 * (long)n13;
        long l43 = (long)n17 * (long)n17;
        long l44 = (long)n38 * ((long)n40 & 0xFFFFFFFFL) + (long)n37 * ((long)n41 & 0xFFFFFFFFL) + (long)n36 * ((long)n42 & 0xFFFFFFFFL) + (long)n35 * (long)n35;
        long l45 = (long)n39 * (long)n39;
        int n59 = (int)(l8 += l40 + l42 + l45 - l41) & 0xFFFFFFF;
        l8 >>>= 28;
        int n60 = (int)(l9 += l43 + l44 - l40 + l45) & 0xFFFFFFF;
        l9 >>>= 28;
        long l46 = (long)n9 * (long)n18 + (long)n8 * (long)n19 + (long)n7 * (long)n20 + (long)n6 * (long)n21;
        long l47 = (long)n17 * (long)n25 + (long)n16 * (long)n26 + (long)n15 * (long)n27 + (long)n14 * (long)n28;
        long l48 = (long)n39 * ((long)n40 & 0xFFFFFFFFL) + (long)n38 * ((long)n41 & 0xFFFFFFFFL) + (long)n37 * ((long)n42 & 0xFFFFFFFFL) + (long)n36 * ((long)n43 & 0xFFFFFFFFL);
        int n61 = (int)(l8 += l46 + l47) & 0xFFFFFFF;
        l8 >>>= 28;
        int n62 = (int)(l9 += l48 - l46) & 0xFFFFFFF;
        l8 += (l9 >>>= 28);
        l8 += (long)n48;
        n48 = (int)l8 & 0xFFFFFFF;
        l9 += (long)n47;
        n47 = (int)l9 & 0xFFFFFFF;
        n50 += (int)(l8 >>>= 28);
        nArray2[0] = n47;
        nArray2[1] = n49 += (int)(l9 >>>= 28);
        nArray2[2] = n51;
        nArray2[3] = n53;
        nArray2[4] = n55;
        nArray2[5] = n57;
        nArray2[6] = n59;
        nArray2[7] = n61;
        nArray2[8] = n48;
        nArray2[9] = n50;
        nArray2[10] = n52;
        nArray2[11] = n54;
        nArray2[12] = n56;
        nArray2[13] = n58;
        nArray2[14] = n60;
        nArray2[15] = n62;
    }

    public static void sqr(int[] nArray, int n2, int[] nArray2) {
        X448Field.sqr(nArray, nArray2);
        while (--n2 > 0) {
            X448Field.sqr(nArray2, nArray2);
        }
    }

    public static boolean sqrtRatioVar(int[] nArray, int[] nArray2, int[] nArray3) {
        int[] nArray4 = X448Field.create();
        int[] nArray5 = X448Field.create();
        X448Field.sqr(nArray, nArray4);
        X448Field.mul(nArray4, nArray2, nArray4);
        X448Field.sqr(nArray4, nArray5);
        X448Field.mul(nArray4, nArray, nArray4);
        X448Field.mul(nArray5, nArray, nArray5);
        X448Field.mul(nArray5, nArray2, nArray5);
        int[] nArray6 = X448Field.create();
        X448Field.powPm3d4(nArray5, nArray6);
        X448Field.mul(nArray6, nArray4, nArray6);
        int[] nArray7 = X448Field.create();
        X448Field.sqr(nArray6, nArray7);
        X448Field.mul(nArray7, nArray2, nArray7);
        X448Field.sub(nArray, nArray7, nArray7);
        X448Field.normalize(nArray7);
        if (X448Field.isZeroVar(nArray7)) {
            X448Field.copy(nArray6, 0, nArray3, 0);
            return true;
        }
        return false;
    }

    public static void sub(int[] nArray, int[] nArray2, int[] nArray3) {
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
        int n12 = nArray[10];
        int n13 = nArray[11];
        int n14 = nArray[12];
        int n15 = nArray[13];
        int n16 = nArray[14];
        int n17 = nArray[15];
        int n18 = nArray2[0];
        int n19 = nArray2[1];
        int n20 = nArray2[2];
        int n21 = nArray2[3];
        int n22 = nArray2[4];
        int n23 = nArray2[5];
        int n24 = nArray2[6];
        int n25 = nArray2[7];
        int n26 = nArray2[8];
        int n27 = nArray2[9];
        int n28 = nArray2[10];
        int n29 = nArray2[11];
        int n30 = nArray2[12];
        int n31 = nArray2[13];
        int n32 = nArray2[14];
        int n33 = nArray2[15];
        int n34 = n2 + 0x1FFFFFFE - n18;
        int n35 = n3 + 0x1FFFFFFE - n19;
        int n36 = n4 + 0x1FFFFFFE - n20;
        int n37 = n5 + 0x1FFFFFFE - n21;
        int n38 = n6 + 0x1FFFFFFE - n22;
        int n39 = n7 + 0x1FFFFFFE - n23;
        int n40 = n8 + 0x1FFFFFFE - n24;
        int n41 = n9 + 0x1FFFFFFE - n25;
        int n42 = n10 + 0x1FFFFFFC - n26;
        int n43 = n11 + 0x1FFFFFFE - n27;
        int n44 = n12 + 0x1FFFFFFE - n28;
        int n45 = n13 + 0x1FFFFFFE - n29;
        int n46 = n14 + 0x1FFFFFFE - n30;
        int n47 = n15 + 0x1FFFFFFE - n31;
        int n48 = n16 + 0x1FFFFFFE - n32;
        int n49 = n17 + 0x1FFFFFFE - n33;
        n36 += n35 >>> 28;
        n35 &= 0xFFFFFFF;
        n40 += n39 >>> 28;
        n39 &= 0xFFFFFFF;
        n44 += n43 >>> 28;
        n43 &= 0xFFFFFFF;
        n48 += n47 >>> 28;
        n47 &= 0xFFFFFFF;
        n37 += n36 >>> 28;
        n36 &= 0xFFFFFFF;
        n41 += n40 >>> 28;
        n40 &= 0xFFFFFFF;
        n45 += n44 >>> 28;
        n44 &= 0xFFFFFFF;
        n49 += n48 >>> 28;
        n48 &= 0xFFFFFFF;
        int n50 = n49 >>> 28;
        n49 &= 0xFFFFFFF;
        n34 += n50;
        n42 += n50;
        n38 += n37 >>> 28;
        n37 &= 0xFFFFFFF;
        n42 += n41 >>> 28;
        n41 &= 0xFFFFFFF;
        n46 += n45 >>> 28;
        n45 &= 0xFFFFFFF;
        n35 += n34 >>> 28;
        n34 &= 0xFFFFFFF;
        n39 += n38 >>> 28;
        n38 &= 0xFFFFFFF;
        n43 += n42 >>> 28;
        n42 &= 0xFFFFFFF;
        n47 += n46 >>> 28;
        n46 &= 0xFFFFFFF;
        nArray3[0] = n34;
        nArray3[1] = n35;
        nArray3[2] = n36;
        nArray3[3] = n37;
        nArray3[4] = n38;
        nArray3[5] = n39;
        nArray3[6] = n40;
        nArray3[7] = n41;
        nArray3[8] = n42;
        nArray3[9] = n43;
        nArray3[10] = n44;
        nArray3[11] = n45;
        nArray3[12] = n46;
        nArray3[13] = n47;
        nArray3[14] = n48;
        nArray3[15] = n49;
    }

    public static void subOne(int[] nArray) {
        int[] nArray2 = X448Field.create();
        nArray2[0] = 1;
        X448Field.sub(nArray, nArray2, nArray);
    }

    public static void zero(int[] nArray) {
        for (int i2 = 0; i2 < 16; ++i2) {
            nArray[i2] = 0;
        }
    }
}

