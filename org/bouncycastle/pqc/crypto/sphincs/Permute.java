/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.sphincs;

import org.bouncycastle.util.Pack;

class Permute {
    private static final int CHACHA_ROUNDS = 12;

    Permute() {
    }

    protected static int rotl(int n2, int n3) {
        return n2 << n3 | n2 >>> -n3;
    }

    public static void permute(int n2, int[] nArray) {
        if (nArray.length != 16) {
            throw new IllegalArgumentException();
        }
        if (n2 % 2 != 0) {
            throw new IllegalArgumentException("Number of rounds must be even");
        }
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
        for (int i2 = n2; i2 > 0; i2 -= 2) {
            n15 = Permute.rotl(n15 ^ (n3 += n7), 16);
            n7 = Permute.rotl(n7 ^ (n11 += n15), 12);
            n15 = Permute.rotl(n15 ^ (n3 += n7), 8);
            n7 = Permute.rotl(n7 ^ (n11 += n15), 7);
            n16 = Permute.rotl(n16 ^ (n4 += n8), 16);
            n8 = Permute.rotl(n8 ^ (n12 += n16), 12);
            n16 = Permute.rotl(n16 ^ (n4 += n8), 8);
            n8 = Permute.rotl(n8 ^ (n12 += n16), 7);
            n17 = Permute.rotl(n17 ^ (n5 += n9), 16);
            n9 = Permute.rotl(n9 ^ (n13 += n17), 12);
            n17 = Permute.rotl(n17 ^ (n5 += n9), 8);
            n9 = Permute.rotl(n9 ^ (n13 += n17), 7);
            n18 = Permute.rotl(n18 ^ (n6 += n10), 16);
            n10 = Permute.rotl(n10 ^ (n14 += n18), 12);
            n18 = Permute.rotl(n18 ^ (n6 += n10), 8);
            n10 = Permute.rotl(n10 ^ (n14 += n18), 7);
            n18 = Permute.rotl(n18 ^ (n3 += n8), 16);
            n8 = Permute.rotl(n8 ^ (n13 += n18), 12);
            n18 = Permute.rotl(n18 ^ (n3 += n8), 8);
            n8 = Permute.rotl(n8 ^ (n13 += n18), 7);
            n15 = Permute.rotl(n15 ^ (n4 += n9), 16);
            n9 = Permute.rotl(n9 ^ (n14 += n15), 12);
            n15 = Permute.rotl(n15 ^ (n4 += n9), 8);
            n9 = Permute.rotl(n9 ^ (n14 += n15), 7);
            n16 = Permute.rotl(n16 ^ (n5 += n10), 16);
            n10 = Permute.rotl(n10 ^ (n11 += n16), 12);
            n16 = Permute.rotl(n16 ^ (n5 += n10), 8);
            n10 = Permute.rotl(n10 ^ (n11 += n16), 7);
            n17 = Permute.rotl(n17 ^ (n6 += n7), 16);
            n7 = Permute.rotl(n7 ^ (n12 += n17), 12);
            n17 = Permute.rotl(n17 ^ (n6 += n7), 8);
            n7 = Permute.rotl(n7 ^ (n12 += n17), 7);
        }
        nArray[0] = n3;
        nArray[1] = n4;
        nArray[2] = n5;
        nArray[3] = n6;
        nArray[4] = n7;
        nArray[5] = n8;
        nArray[6] = n9;
        nArray[7] = n10;
        nArray[8] = n11;
        nArray[9] = n12;
        nArray[10] = n13;
        nArray[11] = n14;
        nArray[12] = n15;
        nArray[13] = n16;
        nArray[14] = n17;
        nArray[15] = n18;
    }

    void chacha_permute(byte[] byArray, byte[] byArray2) {
        int n2;
        int[] nArray = new int[16];
        for (n2 = 0; n2 < 16; ++n2) {
            nArray[n2] = Pack.littleEndianToInt(byArray2, 4 * n2);
        }
        Permute.permute(12, nArray);
        for (n2 = 0; n2 < 16; ++n2) {
            Pack.intToLittleEndian(nArray[n2], byArray, 4 * n2);
        }
    }
}

