/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.math.ec.rfc8032;

import org.bouncycastle.util.Integers;

abstract class ScalarUtil {
    private static final long M = 0xFFFFFFFFL;

    ScalarUtil() {
    }

    static void addShifted_NP(int n2, int n3, int[] nArray, int[] nArray2, int[] nArray3, int[] nArray4) {
        long l2 = 0L;
        long l3 = 0L;
        if (n3 == 0) {
            for (int i2 = 0; i2 <= n2; ++i2) {
                int n4 = nArray3[i2];
                l3 += (long)nArray[i2] & 0xFFFFFFFFL;
                l3 += (long)n4 & 0xFFFFFFFFL;
                l2 += (long)n4 & 0xFFFFFFFFL;
                n4 = (int)(l2 += (long)nArray2[i2] & 0xFFFFFFFFL);
                l2 >>>= 32;
                nArray3[i2] = n4;
                nArray[i2] = (int)(l3 += (long)n4 & 0xFFFFFFFFL);
                l3 >>>= 32;
            }
        } else if (n3 < 32) {
            int n5 = 0;
            int n6 = 0;
            int n7 = 0;
            for (int i3 = 0; i3 <= n2; ++i3) {
                int n8 = nArray3[i3];
                int n9 = n8 << n3 | n5 >>> -n3;
                n5 = n8;
                l3 += (long)nArray[i3] & 0xFFFFFFFFL;
                l3 += (long)n9 & 0xFFFFFFFFL;
                int n10 = nArray2[i3];
                int n11 = n10 << n3 | n7 >>> -n3;
                n7 = n10;
                l2 += (long)n8 & 0xFFFFFFFFL;
                n8 = (int)(l2 += (long)n11 & 0xFFFFFFFFL);
                l2 >>>= 32;
                nArray3[i3] = n8;
                int n12 = n8 << n3 | n6 >>> -n3;
                n6 = n8;
                nArray[i3] = (int)(l3 += (long)n12 & 0xFFFFFFFFL);
                l3 >>>= 32;
            }
        } else {
            System.arraycopy(nArray3, 0, nArray4, 0, n2);
            int n13 = n3 >>> 5;
            int n14 = n3 & 0x1F;
            if (n14 == 0) {
                for (int i4 = n13; i4 <= n2; ++i4) {
                    l3 += (long)nArray[i4] & 0xFFFFFFFFL;
                    l3 += (long)nArray4[i4 - n13] & 0xFFFFFFFFL;
                    l2 += (long)nArray3[i4] & 0xFFFFFFFFL;
                    nArray3[i4] = (int)(l2 += (long)nArray2[i4 - n13] & 0xFFFFFFFFL);
                    l2 >>>= 32;
                    nArray[i4] = (int)(l3 += (long)nArray3[i4 - n13] & 0xFFFFFFFFL);
                    l3 >>>= 32;
                }
            } else {
                int n15 = 0;
                int n16 = 0;
                int n17 = 0;
                for (int i5 = n13; i5 <= n2; ++i5) {
                    int n18 = nArray4[i5 - n13];
                    int n19 = n18 << n14 | n15 >>> -n14;
                    n15 = n18;
                    l3 += (long)nArray[i5] & 0xFFFFFFFFL;
                    l3 += (long)n19 & 0xFFFFFFFFL;
                    int n20 = nArray2[i5 - n13];
                    int n21 = n20 << n14 | n17 >>> -n14;
                    n17 = n20;
                    l2 += (long)nArray3[i5] & 0xFFFFFFFFL;
                    nArray3[i5] = (int)(l2 += (long)n21 & 0xFFFFFFFFL);
                    l2 >>>= 32;
                    int n22 = nArray3[i5 - n13];
                    int n23 = n22 << n14 | n16 >>> -n14;
                    n16 = n22;
                    nArray[i5] = (int)(l3 += (long)n23 & 0xFFFFFFFFL);
                    l3 >>>= 32;
                }
            }
        }
    }

    static void addShifted_UV(int n2, int n3, int[] nArray, int[] nArray2, int[] nArray3, int[] nArray4) {
        int n4 = n3 >>> 5;
        int n5 = n3 & 0x1F;
        long l2 = 0L;
        long l3 = 0L;
        if (n5 == 0) {
            for (int i2 = n4; i2 <= n2; ++i2) {
                l2 += (long)nArray[i2] & 0xFFFFFFFFL;
                l3 += (long)nArray2[i2] & 0xFFFFFFFFL;
                nArray[i2] = (int)(l2 += (long)nArray3[i2 - n4] & 0xFFFFFFFFL);
                l2 >>>= 32;
                nArray2[i2] = (int)(l3 += (long)nArray4[i2 - n4] & 0xFFFFFFFFL);
                l3 >>>= 32;
            }
        } else {
            int n6 = 0;
            int n7 = 0;
            for (int i3 = n4; i3 <= n2; ++i3) {
                int n8 = nArray3[i3 - n4];
                int n9 = nArray4[i3 - n4];
                int n10 = n8 << n5 | n6 >>> -n5;
                int n11 = n9 << n5 | n7 >>> -n5;
                n6 = n8;
                n7 = n9;
                l2 += (long)nArray[i3] & 0xFFFFFFFFL;
                l3 += (long)nArray2[i3] & 0xFFFFFFFFL;
                nArray[i3] = (int)(l2 += (long)n10 & 0xFFFFFFFFL);
                l2 >>>= 32;
                nArray2[i3] = (int)(l3 += (long)n11 & 0xFFFFFFFFL);
                l3 >>>= 32;
            }
        }
    }

    static int getBitLength(int n2, int[] nArray) {
        int n3;
        int n4 = nArray[n3] >> 31;
        for (n3 = n2; n3 > 0 && nArray[n3] == n4; --n3) {
        }
        return n3 * 32 + 32 - Integers.numberOfLeadingZeros(nArray[n3] ^ n4);
    }

    static int getBitLengthPositive(int n2, int[] nArray) {
        int n3;
        for (n3 = n2; n3 > 0 && nArray[n3] == 0; --n3) {
        }
        return n3 * 32 + 32 - Integers.numberOfLeadingZeros(nArray[n3]);
    }

    static boolean lessThan(int n2, int[] nArray, int[] nArray2) {
        int n3 = n2;
        do {
            int n4;
            int n5;
            if ((n5 = nArray[n3] + Integer.MIN_VALUE) < (n4 = nArray2[n3] + Integer.MIN_VALUE)) {
                return true;
            }
            if (n5 <= n4) continue;
            return false;
        } while (--n3 >= 0);
        return false;
    }

    static void subShifted_NP(int n2, int n3, int[] nArray, int[] nArray2, int[] nArray3, int[] nArray4) {
        long l2 = 0L;
        long l3 = 0L;
        if (n3 == 0) {
            for (int i2 = 0; i2 <= n2; ++i2) {
                int n4 = nArray3[i2];
                l3 += (long)nArray[i2] & 0xFFFFFFFFL;
                l3 -= (long)n4 & 0xFFFFFFFFL;
                l2 += (long)n4 & 0xFFFFFFFFL;
                n4 = (int)(l2 -= (long)nArray2[i2] & 0xFFFFFFFFL);
                l2 >>= 32;
                nArray3[i2] = n4;
                nArray[i2] = (int)(l3 -= (long)n4 & 0xFFFFFFFFL);
                l3 >>= 32;
            }
        } else if (n3 < 32) {
            int n5 = 0;
            int n6 = 0;
            int n7 = 0;
            for (int i3 = 0; i3 <= n2; ++i3) {
                int n8 = nArray3[i3];
                int n9 = n8 << n3 | n5 >>> -n3;
                n5 = n8;
                l3 += (long)nArray[i3] & 0xFFFFFFFFL;
                l3 -= (long)n9 & 0xFFFFFFFFL;
                int n10 = nArray2[i3];
                int n11 = n10 << n3 | n7 >>> -n3;
                n7 = n10;
                l2 += (long)n8 & 0xFFFFFFFFL;
                n8 = (int)(l2 -= (long)n11 & 0xFFFFFFFFL);
                l2 >>= 32;
                nArray3[i3] = n8;
                int n12 = n8 << n3 | n6 >>> -n3;
                n6 = n8;
                nArray[i3] = (int)(l3 -= (long)n12 & 0xFFFFFFFFL);
                l3 >>= 32;
            }
        } else {
            System.arraycopy(nArray3, 0, nArray4, 0, n2);
            int n13 = n3 >>> 5;
            int n14 = n3 & 0x1F;
            if (n14 == 0) {
                for (int i4 = n13; i4 <= n2; ++i4) {
                    l3 += (long)nArray[i4] & 0xFFFFFFFFL;
                    l3 -= (long)nArray4[i4 - n13] & 0xFFFFFFFFL;
                    l2 += (long)nArray3[i4] & 0xFFFFFFFFL;
                    nArray3[i4] = (int)(l2 -= (long)nArray2[i4 - n13] & 0xFFFFFFFFL);
                    l2 >>= 32;
                    nArray[i4] = (int)(l3 -= (long)nArray3[i4 - n13] & 0xFFFFFFFFL);
                    l3 >>= 32;
                }
            } else {
                int n15 = 0;
                int n16 = 0;
                int n17 = 0;
                for (int i5 = n13; i5 <= n2; ++i5) {
                    int n18 = nArray4[i5 - n13];
                    int n19 = n18 << n14 | n15 >>> -n14;
                    n15 = n18;
                    l3 += (long)nArray[i5] & 0xFFFFFFFFL;
                    l3 -= (long)n19 & 0xFFFFFFFFL;
                    int n20 = nArray2[i5 - n13];
                    int n21 = n20 << n14 | n17 >>> -n14;
                    n17 = n20;
                    l2 += (long)nArray3[i5] & 0xFFFFFFFFL;
                    nArray3[i5] = (int)(l2 -= (long)n21 & 0xFFFFFFFFL);
                    l2 >>= 32;
                    int n22 = nArray3[i5 - n13];
                    int n23 = n22 << n14 | n16 >>> -n14;
                    n16 = n22;
                    nArray[i5] = (int)(l3 -= (long)n23 & 0xFFFFFFFFL);
                    l3 >>= 32;
                }
            }
        }
    }

    static void subShifted_UV(int n2, int n3, int[] nArray, int[] nArray2, int[] nArray3, int[] nArray4) {
        int n4 = n3 >>> 5;
        int n5 = n3 & 0x1F;
        long l2 = 0L;
        long l3 = 0L;
        if (n5 == 0) {
            for (int i2 = n4; i2 <= n2; ++i2) {
                l2 += (long)nArray[i2] & 0xFFFFFFFFL;
                l3 += (long)nArray2[i2] & 0xFFFFFFFFL;
                nArray[i2] = (int)(l2 -= (long)nArray3[i2 - n4] & 0xFFFFFFFFL);
                l2 >>= 32;
                nArray2[i2] = (int)(l3 -= (long)nArray4[i2 - n4] & 0xFFFFFFFFL);
                l3 >>= 32;
            }
        } else {
            int n6 = 0;
            int n7 = 0;
            for (int i3 = n4; i3 <= n2; ++i3) {
                int n8 = nArray3[i3 - n4];
                int n9 = nArray4[i3 - n4];
                int n10 = n8 << n5 | n6 >>> -n5;
                int n11 = n9 << n5 | n7 >>> -n5;
                n6 = n8;
                n7 = n9;
                l2 += (long)nArray[i3] & 0xFFFFFFFFL;
                l3 += (long)nArray2[i3] & 0xFFFFFFFFL;
                nArray[i3] = (int)(l2 -= (long)n10 & 0xFFFFFFFFL);
                l2 >>= 32;
                nArray2[i3] = (int)(l3 -= (long)n11 & 0xFFFFFFFFL);
                l3 >>= 32;
            }
        }
    }
}

