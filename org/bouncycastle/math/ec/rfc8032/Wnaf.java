/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.math.ec.rfc8032;

abstract class Wnaf {
    Wnaf() {
    }

    static void getSignedVar(int[] nArray, int n2, byte[] byArray) {
        int n3;
        int[] nArray2 = new int[nArray.length * 2];
        int n4 = nArray[nArray.length - 1] >> 31;
        int n5 = nArray.length;
        int n6 = nArray2.length;
        while (--n5 >= 0) {
            n3 = nArray[n5];
            nArray2[--n6] = n3 >>> 16 | n4 << 16;
            nArray2[--n6] = n4 = n3;
        }
        n4 = 32 - n2;
        n5 = 0;
        n6 = 0;
        n3 = 0;
        while (n3 < nArray2.length) {
            int n7 = nArray2[n3];
            while (n5 < 16) {
                int n8 = n7 >>> n5;
                int n9 = n8 & 1;
                if (n9 == n6) {
                    ++n5;
                    continue;
                }
                int n10 = (n8 | 1) << n4;
                n6 = n10 >>> 31;
                byArray[(n3 << 4) + n5] = (byte)(n10 >> n4);
                n5 += n2;
            }
            ++n3;
            n5 -= 16;
        }
    }
}

