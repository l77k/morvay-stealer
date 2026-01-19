/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.math.raw;

import java.math.BigInteger;
import org.bouncycastle.math.raw.Nat;
import org.bouncycastle.util.Pack;

public abstract class Nat224 {
    private static final long M = 0xFFFFFFFFL;

    public static int add(int[] nArray, int[] nArray2, int[] nArray3) {
        long l2 = 0L;
        nArray3[0] = (int)(l2 += ((long)nArray[0] & 0xFFFFFFFFL) + ((long)nArray2[0] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray3[1] = (int)(l2 += ((long)nArray[1] & 0xFFFFFFFFL) + ((long)nArray2[1] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray3[2] = (int)(l2 += ((long)nArray[2] & 0xFFFFFFFFL) + ((long)nArray2[2] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray3[3] = (int)(l2 += ((long)nArray[3] & 0xFFFFFFFFL) + ((long)nArray2[3] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray3[4] = (int)(l2 += ((long)nArray[4] & 0xFFFFFFFFL) + ((long)nArray2[4] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray3[5] = (int)(l2 += ((long)nArray[5] & 0xFFFFFFFFL) + ((long)nArray2[5] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray3[6] = (int)(l2 += ((long)nArray[6] & 0xFFFFFFFFL) + ((long)nArray2[6] & 0xFFFFFFFFL));
        return (int)(l2 >>>= 32);
    }

    public static int add(int[] nArray, int n2, int[] nArray2, int n3, int[] nArray3, int n4) {
        long l2 = 0L;
        nArray3[n4 + 0] = (int)(l2 += ((long)nArray[n2 + 0] & 0xFFFFFFFFL) + ((long)nArray2[n3 + 0] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray3[n4 + 1] = (int)(l2 += ((long)nArray[n2 + 1] & 0xFFFFFFFFL) + ((long)nArray2[n3 + 1] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray3[n4 + 2] = (int)(l2 += ((long)nArray[n2 + 2] & 0xFFFFFFFFL) + ((long)nArray2[n3 + 2] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray3[n4 + 3] = (int)(l2 += ((long)nArray[n2 + 3] & 0xFFFFFFFFL) + ((long)nArray2[n3 + 3] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray3[n4 + 4] = (int)(l2 += ((long)nArray[n2 + 4] & 0xFFFFFFFFL) + ((long)nArray2[n3 + 4] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray3[n4 + 5] = (int)(l2 += ((long)nArray[n2 + 5] & 0xFFFFFFFFL) + ((long)nArray2[n3 + 5] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray3[n4 + 6] = (int)(l2 += ((long)nArray[n2 + 6] & 0xFFFFFFFFL) + ((long)nArray2[n3 + 6] & 0xFFFFFFFFL));
        return (int)(l2 >>>= 32);
    }

    public static int addBothTo(int[] nArray, int[] nArray2, int[] nArray3) {
        long l2 = 0L;
        nArray3[0] = (int)(l2 += ((long)nArray[0] & 0xFFFFFFFFL) + ((long)nArray2[0] & 0xFFFFFFFFL) + ((long)nArray3[0] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray3[1] = (int)(l2 += ((long)nArray[1] & 0xFFFFFFFFL) + ((long)nArray2[1] & 0xFFFFFFFFL) + ((long)nArray3[1] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray3[2] = (int)(l2 += ((long)nArray[2] & 0xFFFFFFFFL) + ((long)nArray2[2] & 0xFFFFFFFFL) + ((long)nArray3[2] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray3[3] = (int)(l2 += ((long)nArray[3] & 0xFFFFFFFFL) + ((long)nArray2[3] & 0xFFFFFFFFL) + ((long)nArray3[3] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray3[4] = (int)(l2 += ((long)nArray[4] & 0xFFFFFFFFL) + ((long)nArray2[4] & 0xFFFFFFFFL) + ((long)nArray3[4] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray3[5] = (int)(l2 += ((long)nArray[5] & 0xFFFFFFFFL) + ((long)nArray2[5] & 0xFFFFFFFFL) + ((long)nArray3[5] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray3[6] = (int)(l2 += ((long)nArray[6] & 0xFFFFFFFFL) + ((long)nArray2[6] & 0xFFFFFFFFL) + ((long)nArray3[6] & 0xFFFFFFFFL));
        return (int)(l2 >>>= 32);
    }

    public static int addBothTo(int[] nArray, int n2, int[] nArray2, int n3, int[] nArray3, int n4) {
        long l2 = 0L;
        nArray3[n4 + 0] = (int)(l2 += ((long)nArray[n2 + 0] & 0xFFFFFFFFL) + ((long)nArray2[n3 + 0] & 0xFFFFFFFFL) + ((long)nArray3[n4 + 0] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray3[n4 + 1] = (int)(l2 += ((long)nArray[n2 + 1] & 0xFFFFFFFFL) + ((long)nArray2[n3 + 1] & 0xFFFFFFFFL) + ((long)nArray3[n4 + 1] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray3[n4 + 2] = (int)(l2 += ((long)nArray[n2 + 2] & 0xFFFFFFFFL) + ((long)nArray2[n3 + 2] & 0xFFFFFFFFL) + ((long)nArray3[n4 + 2] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray3[n4 + 3] = (int)(l2 += ((long)nArray[n2 + 3] & 0xFFFFFFFFL) + ((long)nArray2[n3 + 3] & 0xFFFFFFFFL) + ((long)nArray3[n4 + 3] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray3[n4 + 4] = (int)(l2 += ((long)nArray[n2 + 4] & 0xFFFFFFFFL) + ((long)nArray2[n3 + 4] & 0xFFFFFFFFL) + ((long)nArray3[n4 + 4] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray3[n4 + 5] = (int)(l2 += ((long)nArray[n2 + 5] & 0xFFFFFFFFL) + ((long)nArray2[n3 + 5] & 0xFFFFFFFFL) + ((long)nArray3[n4 + 5] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray3[n4 + 6] = (int)(l2 += ((long)nArray[n2 + 6] & 0xFFFFFFFFL) + ((long)nArray2[n3 + 6] & 0xFFFFFFFFL) + ((long)nArray3[n4 + 6] & 0xFFFFFFFFL));
        return (int)(l2 >>>= 32);
    }

    public static int addTo(int[] nArray, int[] nArray2) {
        long l2 = 0L;
        nArray2[0] = (int)(l2 += ((long)nArray[0] & 0xFFFFFFFFL) + ((long)nArray2[0] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray2[1] = (int)(l2 += ((long)nArray[1] & 0xFFFFFFFFL) + ((long)nArray2[1] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray2[2] = (int)(l2 += ((long)nArray[2] & 0xFFFFFFFFL) + ((long)nArray2[2] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray2[3] = (int)(l2 += ((long)nArray[3] & 0xFFFFFFFFL) + ((long)nArray2[3] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray2[4] = (int)(l2 += ((long)nArray[4] & 0xFFFFFFFFL) + ((long)nArray2[4] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray2[5] = (int)(l2 += ((long)nArray[5] & 0xFFFFFFFFL) + ((long)nArray2[5] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray2[6] = (int)(l2 += ((long)nArray[6] & 0xFFFFFFFFL) + ((long)nArray2[6] & 0xFFFFFFFFL));
        return (int)(l2 >>>= 32);
    }

    public static int addTo(int[] nArray, int[] nArray2, int n2) {
        long l2 = (long)n2 & 0xFFFFFFFFL;
        nArray2[0] = (int)(l2 += ((long)nArray[0] & 0xFFFFFFFFL) + ((long)nArray2[0] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray2[1] = (int)(l2 += ((long)nArray[1] & 0xFFFFFFFFL) + ((long)nArray2[1] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray2[2] = (int)(l2 += ((long)nArray[2] & 0xFFFFFFFFL) + ((long)nArray2[2] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray2[3] = (int)(l2 += ((long)nArray[3] & 0xFFFFFFFFL) + ((long)nArray2[3] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray2[4] = (int)(l2 += ((long)nArray[4] & 0xFFFFFFFFL) + ((long)nArray2[4] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray2[5] = (int)(l2 += ((long)nArray[5] & 0xFFFFFFFFL) + ((long)nArray2[5] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray2[6] = (int)(l2 += ((long)nArray[6] & 0xFFFFFFFFL) + ((long)nArray2[6] & 0xFFFFFFFFL));
        return (int)(l2 >>>= 32);
    }

    public static int addTo(int[] nArray, int n2, int[] nArray2, int n3, int n4) {
        long l2 = (long)n4 & 0xFFFFFFFFL;
        nArray2[n3 + 0] = (int)(l2 += ((long)nArray[n2 + 0] & 0xFFFFFFFFL) + ((long)nArray2[n3 + 0] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray2[n3 + 1] = (int)(l2 += ((long)nArray[n2 + 1] & 0xFFFFFFFFL) + ((long)nArray2[n3 + 1] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray2[n3 + 2] = (int)(l2 += ((long)nArray[n2 + 2] & 0xFFFFFFFFL) + ((long)nArray2[n3 + 2] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray2[n3 + 3] = (int)(l2 += ((long)nArray[n2 + 3] & 0xFFFFFFFFL) + ((long)nArray2[n3 + 3] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray2[n3 + 4] = (int)(l2 += ((long)nArray[n2 + 4] & 0xFFFFFFFFL) + ((long)nArray2[n3 + 4] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray2[n3 + 5] = (int)(l2 += ((long)nArray[n2 + 5] & 0xFFFFFFFFL) + ((long)nArray2[n3 + 5] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray2[n3 + 6] = (int)(l2 += ((long)nArray[n2 + 6] & 0xFFFFFFFFL) + ((long)nArray2[n3 + 6] & 0xFFFFFFFFL));
        return (int)(l2 >>>= 32);
    }

    public static int addToEachOther(int[] nArray, int n2, int[] nArray2, int n3) {
        long l2 = 0L;
        nArray[n2 + 0] = (int)(l2 += ((long)nArray[n2 + 0] & 0xFFFFFFFFL) + ((long)nArray2[n3 + 0] & 0xFFFFFFFFL));
        nArray2[n3 + 0] = (int)l2;
        l2 >>>= 32;
        nArray[n2 + 1] = (int)(l2 += ((long)nArray[n2 + 1] & 0xFFFFFFFFL) + ((long)nArray2[n3 + 1] & 0xFFFFFFFFL));
        nArray2[n3 + 1] = (int)l2;
        l2 >>>= 32;
        nArray[n2 + 2] = (int)(l2 += ((long)nArray[n2 + 2] & 0xFFFFFFFFL) + ((long)nArray2[n3 + 2] & 0xFFFFFFFFL));
        nArray2[n3 + 2] = (int)l2;
        l2 >>>= 32;
        nArray[n2 + 3] = (int)(l2 += ((long)nArray[n2 + 3] & 0xFFFFFFFFL) + ((long)nArray2[n3 + 3] & 0xFFFFFFFFL));
        nArray2[n3 + 3] = (int)l2;
        l2 >>>= 32;
        nArray[n2 + 4] = (int)(l2 += ((long)nArray[n2 + 4] & 0xFFFFFFFFL) + ((long)nArray2[n3 + 4] & 0xFFFFFFFFL));
        nArray2[n3 + 4] = (int)l2;
        l2 >>>= 32;
        nArray[n2 + 5] = (int)(l2 += ((long)nArray[n2 + 5] & 0xFFFFFFFFL) + ((long)nArray2[n3 + 5] & 0xFFFFFFFFL));
        nArray2[n3 + 5] = (int)l2;
        l2 >>>= 32;
        nArray[n2 + 6] = (int)(l2 += ((long)nArray[n2 + 6] & 0xFFFFFFFFL) + ((long)nArray2[n3 + 6] & 0xFFFFFFFFL));
        nArray2[n3 + 6] = (int)l2;
        return (int)(l2 >>>= 32);
    }

    public static void copy(int[] nArray, int[] nArray2) {
        nArray2[0] = nArray[0];
        nArray2[1] = nArray[1];
        nArray2[2] = nArray[2];
        nArray2[3] = nArray[3];
        nArray2[4] = nArray[4];
        nArray2[5] = nArray[5];
        nArray2[6] = nArray[6];
    }

    public static void copy(int[] nArray, int n2, int[] nArray2, int n3) {
        nArray2[n3 + 0] = nArray[n2 + 0];
        nArray2[n3 + 1] = nArray[n2 + 1];
        nArray2[n3 + 2] = nArray[n2 + 2];
        nArray2[n3 + 3] = nArray[n2 + 3];
        nArray2[n3 + 4] = nArray[n2 + 4];
        nArray2[n3 + 5] = nArray[n2 + 5];
        nArray2[n3 + 6] = nArray[n2 + 6];
    }

    public static int[] create() {
        return new int[7];
    }

    public static int[] createExt() {
        return new int[14];
    }

    public static boolean diff(int[] nArray, int n2, int[] nArray2, int n3, int[] nArray3, int n4) {
        boolean bl = Nat224.gte(nArray, n2, nArray2, n3);
        if (bl) {
            Nat224.sub(nArray, n2, nArray2, n3, nArray3, n4);
        } else {
            Nat224.sub(nArray2, n3, nArray, n2, nArray3, n4);
        }
        return bl;
    }

    public static boolean eq(int[] nArray, int[] nArray2) {
        for (int i2 = 6; i2 >= 0; --i2) {
            if (nArray[i2] == nArray2[i2]) continue;
            return false;
        }
        return true;
    }

    public static int[] fromBigInteger(BigInteger bigInteger) {
        if (bigInteger.signum() < 0 || bigInteger.bitLength() > 224) {
            throw new IllegalArgumentException();
        }
        int[] nArray = Nat224.create();
        for (int i2 = 0; i2 < 7; ++i2) {
            nArray[i2] = bigInteger.intValue();
            bigInteger = bigInteger.shiftRight(32);
        }
        return nArray;
    }

    public static int getBit(int[] nArray, int n2) {
        if (n2 == 0) {
            return nArray[0] & 1;
        }
        int n3 = n2 >> 5;
        if (n3 < 0 || n3 >= 7) {
            return 0;
        }
        int n4 = n2 & 0x1F;
        return nArray[n3] >>> n4 & 1;
    }

    public static boolean gte(int[] nArray, int[] nArray2) {
        for (int i2 = 6; i2 >= 0; --i2) {
            int n2 = nArray[i2] ^ Integer.MIN_VALUE;
            int n3 = nArray2[i2] ^ Integer.MIN_VALUE;
            if (n2 < n3) {
                return false;
            }
            if (n2 <= n3) continue;
            return true;
        }
        return true;
    }

    public static boolean gte(int[] nArray, int n2, int[] nArray2, int n3) {
        for (int i2 = 6; i2 >= 0; --i2) {
            int n4 = nArray[n2 + i2] ^ Integer.MIN_VALUE;
            int n5 = nArray2[n3 + i2] ^ Integer.MIN_VALUE;
            if (n4 < n5) {
                return false;
            }
            if (n4 <= n5) continue;
            return true;
        }
        return true;
    }

    public static boolean isOne(int[] nArray) {
        if (nArray[0] != 1) {
            return false;
        }
        for (int i2 = 1; i2 < 7; ++i2) {
            if (nArray[i2] == 0) continue;
            return false;
        }
        return true;
    }

    public static boolean isZero(int[] nArray) {
        for (int i2 = 0; i2 < 7; ++i2) {
            if (nArray[i2] == 0) continue;
            return false;
        }
        return true;
    }

    public static void mul(int[] nArray, int[] nArray2, int[] nArray3) {
        long l2 = (long)nArray2[0] & 0xFFFFFFFFL;
        long l3 = (long)nArray2[1] & 0xFFFFFFFFL;
        long l4 = (long)nArray2[2] & 0xFFFFFFFFL;
        long l5 = (long)nArray2[3] & 0xFFFFFFFFL;
        long l6 = (long)nArray2[4] & 0xFFFFFFFFL;
        long l7 = (long)nArray2[5] & 0xFFFFFFFFL;
        long l8 = (long)nArray2[6] & 0xFFFFFFFFL;
        long l9 = 0L;
        long l10 = (long)nArray[0] & 0xFFFFFFFFL;
        nArray3[0] = (int)(l9 += l10 * l2);
        l9 >>>= 32;
        nArray3[1] = (int)(l9 += l10 * l3);
        l9 >>>= 32;
        nArray3[2] = (int)(l9 += l10 * l4);
        l9 >>>= 32;
        nArray3[3] = (int)(l9 += l10 * l5);
        l9 >>>= 32;
        nArray3[4] = (int)(l9 += l10 * l6);
        l9 >>>= 32;
        nArray3[5] = (int)(l9 += l10 * l7);
        l9 >>>= 32;
        nArray3[6] = (int)(l9 += l10 * l8);
        nArray3[7] = (int)(l9 >>>= 32);
        for (int i2 = 1; i2 < 7; ++i2) {
            long l11 = 0L;
            long l12 = (long)nArray[i2] & 0xFFFFFFFFL;
            nArray3[i2 + 0] = (int)(l11 += l12 * l2 + ((long)nArray3[i2 + 0] & 0xFFFFFFFFL));
            l11 >>>= 32;
            nArray3[i2 + 1] = (int)(l11 += l12 * l3 + ((long)nArray3[i2 + 1] & 0xFFFFFFFFL));
            l11 >>>= 32;
            nArray3[i2 + 2] = (int)(l11 += l12 * l4 + ((long)nArray3[i2 + 2] & 0xFFFFFFFFL));
            l11 >>>= 32;
            nArray3[i2 + 3] = (int)(l11 += l12 * l5 + ((long)nArray3[i2 + 3] & 0xFFFFFFFFL));
            l11 >>>= 32;
            nArray3[i2 + 4] = (int)(l11 += l12 * l6 + ((long)nArray3[i2 + 4] & 0xFFFFFFFFL));
            l11 >>>= 32;
            nArray3[i2 + 5] = (int)(l11 += l12 * l7 + ((long)nArray3[i2 + 5] & 0xFFFFFFFFL));
            l11 >>>= 32;
            nArray3[i2 + 6] = (int)(l11 += l12 * l8 + ((long)nArray3[i2 + 6] & 0xFFFFFFFFL));
            nArray3[i2 + 7] = (int)(l11 >>>= 32);
        }
    }

    public static void mul(int[] nArray, int n2, int[] nArray2, int n3, int[] nArray3, int n4) {
        long l2 = (long)nArray2[n3 + 0] & 0xFFFFFFFFL;
        long l3 = (long)nArray2[n3 + 1] & 0xFFFFFFFFL;
        long l4 = (long)nArray2[n3 + 2] & 0xFFFFFFFFL;
        long l5 = (long)nArray2[n3 + 3] & 0xFFFFFFFFL;
        long l6 = (long)nArray2[n3 + 4] & 0xFFFFFFFFL;
        long l7 = (long)nArray2[n3 + 5] & 0xFFFFFFFFL;
        long l8 = (long)nArray2[n3 + 6] & 0xFFFFFFFFL;
        long l9 = 0L;
        long l10 = (long)nArray[n2 + 0] & 0xFFFFFFFFL;
        nArray3[n4 + 0] = (int)(l9 += l10 * l2);
        l9 >>>= 32;
        nArray3[n4 + 1] = (int)(l9 += l10 * l3);
        l9 >>>= 32;
        nArray3[n4 + 2] = (int)(l9 += l10 * l4);
        l9 >>>= 32;
        nArray3[n4 + 3] = (int)(l9 += l10 * l5);
        l9 >>>= 32;
        nArray3[n4 + 4] = (int)(l9 += l10 * l6);
        l9 >>>= 32;
        nArray3[n4 + 5] = (int)(l9 += l10 * l7);
        l9 >>>= 32;
        nArray3[n4 + 6] = (int)(l9 += l10 * l8);
        nArray3[n4 + 7] = (int)(l9 >>>= 32);
        for (int i2 = 1; i2 < 7; ++i2) {
            long l11 = 0L;
            long l12 = (long)nArray[n2 + i2] & 0xFFFFFFFFL;
            nArray3[n4 + 0] = (int)(l11 += l12 * l2 + ((long)nArray3[++n4 + 0] & 0xFFFFFFFFL));
            l11 >>>= 32;
            nArray3[n4 + 1] = (int)(l11 += l12 * l3 + ((long)nArray3[n4 + 1] & 0xFFFFFFFFL));
            l11 >>>= 32;
            nArray3[n4 + 2] = (int)(l11 += l12 * l4 + ((long)nArray3[n4 + 2] & 0xFFFFFFFFL));
            l11 >>>= 32;
            nArray3[n4 + 3] = (int)(l11 += l12 * l5 + ((long)nArray3[n4 + 3] & 0xFFFFFFFFL));
            l11 >>>= 32;
            nArray3[n4 + 4] = (int)(l11 += l12 * l6 + ((long)nArray3[n4 + 4] & 0xFFFFFFFFL));
            l11 >>>= 32;
            nArray3[n4 + 5] = (int)(l11 += l12 * l7 + ((long)nArray3[n4 + 5] & 0xFFFFFFFFL));
            l11 >>>= 32;
            nArray3[n4 + 6] = (int)(l11 += l12 * l8 + ((long)nArray3[n4 + 6] & 0xFFFFFFFFL));
            nArray3[n4 + 7] = (int)(l11 >>>= 32);
        }
    }

    public static int mulAddTo(int[] nArray, int[] nArray2, int[] nArray3) {
        long l2 = (long)nArray2[0] & 0xFFFFFFFFL;
        long l3 = (long)nArray2[1] & 0xFFFFFFFFL;
        long l4 = (long)nArray2[2] & 0xFFFFFFFFL;
        long l5 = (long)nArray2[3] & 0xFFFFFFFFL;
        long l6 = (long)nArray2[4] & 0xFFFFFFFFL;
        long l7 = (long)nArray2[5] & 0xFFFFFFFFL;
        long l8 = (long)nArray2[6] & 0xFFFFFFFFL;
        long l9 = 0L;
        for (int i2 = 0; i2 < 7; ++i2) {
            long l10 = 0L;
            long l11 = (long)nArray[i2] & 0xFFFFFFFFL;
            nArray3[i2 + 0] = (int)(l10 += l11 * l2 + ((long)nArray3[i2 + 0] & 0xFFFFFFFFL));
            l10 >>>= 32;
            nArray3[i2 + 1] = (int)(l10 += l11 * l3 + ((long)nArray3[i2 + 1] & 0xFFFFFFFFL));
            l10 >>>= 32;
            nArray3[i2 + 2] = (int)(l10 += l11 * l4 + ((long)nArray3[i2 + 2] & 0xFFFFFFFFL));
            l10 >>>= 32;
            nArray3[i2 + 3] = (int)(l10 += l11 * l5 + ((long)nArray3[i2 + 3] & 0xFFFFFFFFL));
            l10 >>>= 32;
            nArray3[i2 + 4] = (int)(l10 += l11 * l6 + ((long)nArray3[i2 + 4] & 0xFFFFFFFFL));
            l10 >>>= 32;
            nArray3[i2 + 5] = (int)(l10 += l11 * l7 + ((long)nArray3[i2 + 5] & 0xFFFFFFFFL));
            l10 >>>= 32;
            nArray3[i2 + 6] = (int)(l10 += l11 * l8 + ((long)nArray3[i2 + 6] & 0xFFFFFFFFL));
            nArray3[i2 + 7] = (int)(l9 += (l10 >>>= 32) + ((long)nArray3[i2 + 7] & 0xFFFFFFFFL));
            l9 >>>= 32;
        }
        return (int)l9;
    }

    public static int mulAddTo(int[] nArray, int n2, int[] nArray2, int n3, int[] nArray3, int n4) {
        long l2 = (long)nArray2[n3 + 0] & 0xFFFFFFFFL;
        long l3 = (long)nArray2[n3 + 1] & 0xFFFFFFFFL;
        long l4 = (long)nArray2[n3 + 2] & 0xFFFFFFFFL;
        long l5 = (long)nArray2[n3 + 3] & 0xFFFFFFFFL;
        long l6 = (long)nArray2[n3 + 4] & 0xFFFFFFFFL;
        long l7 = (long)nArray2[n3 + 5] & 0xFFFFFFFFL;
        long l8 = (long)nArray2[n3 + 6] & 0xFFFFFFFFL;
        long l9 = 0L;
        for (int i2 = 0; i2 < 7; ++i2) {
            long l10 = 0L;
            long l11 = (long)nArray[n2 + i2] & 0xFFFFFFFFL;
            nArray3[n4 + 0] = (int)(l10 += l11 * l2 + ((long)nArray3[n4 + 0] & 0xFFFFFFFFL));
            l10 >>>= 32;
            nArray3[n4 + 1] = (int)(l10 += l11 * l3 + ((long)nArray3[n4 + 1] & 0xFFFFFFFFL));
            l10 >>>= 32;
            nArray3[n4 + 2] = (int)(l10 += l11 * l4 + ((long)nArray3[n4 + 2] & 0xFFFFFFFFL));
            l10 >>>= 32;
            nArray3[n4 + 3] = (int)(l10 += l11 * l5 + ((long)nArray3[n4 + 3] & 0xFFFFFFFFL));
            l10 >>>= 32;
            nArray3[n4 + 4] = (int)(l10 += l11 * l6 + ((long)nArray3[n4 + 4] & 0xFFFFFFFFL));
            l10 >>>= 32;
            nArray3[n4 + 5] = (int)(l10 += l11 * l7 + ((long)nArray3[n4 + 5] & 0xFFFFFFFFL));
            l10 >>>= 32;
            nArray3[n4 + 6] = (int)(l10 += l11 * l8 + ((long)nArray3[n4 + 6] & 0xFFFFFFFFL));
            nArray3[n4 + 7] = (int)(l9 += (l10 >>>= 32) + ((long)nArray3[n4 + 7] & 0xFFFFFFFFL));
            l9 >>>= 32;
            ++n4;
        }
        return (int)l9;
    }

    public static long mul33Add(int n2, int[] nArray, int n3, int[] nArray2, int n4, int[] nArray3, int n5) {
        long l2 = 0L;
        long l3 = (long)n2 & 0xFFFFFFFFL;
        long l4 = (long)nArray[n3 + 0] & 0xFFFFFFFFL;
        nArray3[n5 + 0] = (int)(l2 += l3 * l4 + ((long)nArray2[n4 + 0] & 0xFFFFFFFFL));
        l2 >>>= 32;
        long l5 = (long)nArray[n3 + 1] & 0xFFFFFFFFL;
        nArray3[n5 + 1] = (int)(l2 += l3 * l5 + l4 + ((long)nArray2[n4 + 1] & 0xFFFFFFFFL));
        l2 >>>= 32;
        long l6 = (long)nArray[n3 + 2] & 0xFFFFFFFFL;
        nArray3[n5 + 2] = (int)(l2 += l3 * l6 + l5 + ((long)nArray2[n4 + 2] & 0xFFFFFFFFL));
        l2 >>>= 32;
        long l7 = (long)nArray[n3 + 3] & 0xFFFFFFFFL;
        nArray3[n5 + 3] = (int)(l2 += l3 * l7 + l6 + ((long)nArray2[n4 + 3] & 0xFFFFFFFFL));
        l2 >>>= 32;
        long l8 = (long)nArray[n3 + 4] & 0xFFFFFFFFL;
        nArray3[n5 + 4] = (int)(l2 += l3 * l8 + l7 + ((long)nArray2[n4 + 4] & 0xFFFFFFFFL));
        l2 >>>= 32;
        long l9 = (long)nArray[n3 + 5] & 0xFFFFFFFFL;
        nArray3[n5 + 5] = (int)(l2 += l3 * l9 + l8 + ((long)nArray2[n4 + 5] & 0xFFFFFFFFL));
        l2 >>>= 32;
        long l10 = (long)nArray[n3 + 6] & 0xFFFFFFFFL;
        nArray3[n5 + 6] = (int)(l2 += l3 * l10 + l9 + ((long)nArray2[n4 + 6] & 0xFFFFFFFFL));
        l2 >>>= 32;
        return l2 += l10;
    }

    public static int mulByWord(int n2, int[] nArray) {
        long l2 = 0L;
        long l3 = (long)n2 & 0xFFFFFFFFL;
        nArray[0] = (int)(l2 += l3 * ((long)nArray[0] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray[1] = (int)(l2 += l3 * ((long)nArray[1] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray[2] = (int)(l2 += l3 * ((long)nArray[2] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray[3] = (int)(l2 += l3 * ((long)nArray[3] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray[4] = (int)(l2 += l3 * ((long)nArray[4] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray[5] = (int)(l2 += l3 * ((long)nArray[5] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray[6] = (int)(l2 += l3 * ((long)nArray[6] & 0xFFFFFFFFL));
        return (int)(l2 >>>= 32);
    }

    public static int mulByWordAddTo(int n2, int[] nArray, int[] nArray2) {
        long l2 = 0L;
        long l3 = (long)n2 & 0xFFFFFFFFL;
        nArray2[0] = (int)(l2 += l3 * ((long)nArray2[0] & 0xFFFFFFFFL) + ((long)nArray[0] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray2[1] = (int)(l2 += l3 * ((long)nArray2[1] & 0xFFFFFFFFL) + ((long)nArray[1] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray2[2] = (int)(l2 += l3 * ((long)nArray2[2] & 0xFFFFFFFFL) + ((long)nArray[2] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray2[3] = (int)(l2 += l3 * ((long)nArray2[3] & 0xFFFFFFFFL) + ((long)nArray[3] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray2[4] = (int)(l2 += l3 * ((long)nArray2[4] & 0xFFFFFFFFL) + ((long)nArray[4] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray2[5] = (int)(l2 += l3 * ((long)nArray2[5] & 0xFFFFFFFFL) + ((long)nArray[5] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray2[6] = (int)(l2 += l3 * ((long)nArray2[6] & 0xFFFFFFFFL) + ((long)nArray[6] & 0xFFFFFFFFL));
        return (int)(l2 >>>= 32);
    }

    public static int mulWordAddTo(int n2, int[] nArray, int n3, int[] nArray2, int n4) {
        long l2 = 0L;
        long l3 = (long)n2 & 0xFFFFFFFFL;
        nArray2[n4 + 0] = (int)(l2 += l3 * ((long)nArray[n3 + 0] & 0xFFFFFFFFL) + ((long)nArray2[n4 + 0] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray2[n4 + 1] = (int)(l2 += l3 * ((long)nArray[n3 + 1] & 0xFFFFFFFFL) + ((long)nArray2[n4 + 1] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray2[n4 + 2] = (int)(l2 += l3 * ((long)nArray[n3 + 2] & 0xFFFFFFFFL) + ((long)nArray2[n4 + 2] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray2[n4 + 3] = (int)(l2 += l3 * ((long)nArray[n3 + 3] & 0xFFFFFFFFL) + ((long)nArray2[n4 + 3] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray2[n4 + 4] = (int)(l2 += l3 * ((long)nArray[n3 + 4] & 0xFFFFFFFFL) + ((long)nArray2[n4 + 4] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray2[n4 + 5] = (int)(l2 += l3 * ((long)nArray[n3 + 5] & 0xFFFFFFFFL) + ((long)nArray2[n4 + 5] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray2[n4 + 6] = (int)(l2 += l3 * ((long)nArray[n3 + 6] & 0xFFFFFFFFL) + ((long)nArray2[n4 + 6] & 0xFFFFFFFFL));
        return (int)(l2 >>>= 32);
    }

    public static int mul33DWordAdd(int n2, long l2, int[] nArray, int n3) {
        long l3 = 0L;
        long l4 = (long)n2 & 0xFFFFFFFFL;
        long l5 = l2 & 0xFFFFFFFFL;
        nArray[n3 + 0] = (int)(l3 += l4 * l5 + ((long)nArray[n3 + 0] & 0xFFFFFFFFL));
        l3 >>>= 32;
        long l6 = l2 >>> 32;
        nArray[n3 + 1] = (int)(l3 += l4 * l6 + l5 + ((long)nArray[n3 + 1] & 0xFFFFFFFFL));
        l3 >>>= 32;
        nArray[n3 + 2] = (int)(l3 += l6 + ((long)nArray[n3 + 2] & 0xFFFFFFFFL));
        l3 >>>= 32;
        nArray[n3 + 3] = (int)(l3 += (long)nArray[n3 + 3] & 0xFFFFFFFFL);
        return (l3 >>>= 32) == 0L ? 0 : Nat.incAt(7, nArray, n3, 4);
    }

    public static int mul33WordAdd(int n2, int n3, int[] nArray, int n4) {
        long l2 = 0L;
        long l3 = (long)n2 & 0xFFFFFFFFL;
        long l4 = (long)n3 & 0xFFFFFFFFL;
        nArray[n4 + 0] = (int)(l2 += l4 * l3 + ((long)nArray[n4 + 0] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray[n4 + 1] = (int)(l2 += l4 + ((long)nArray[n4 + 1] & 0xFFFFFFFFL));
        l2 >>>= 32;
        nArray[n4 + 2] = (int)(l2 += (long)nArray[n4 + 2] & 0xFFFFFFFFL);
        return (l2 >>>= 32) == 0L ? 0 : Nat.incAt(7, nArray, n4, 3);
    }

    public static int mulWordDwordAdd(int n2, long l2, int[] nArray, int n3) {
        long l3 = 0L;
        long l4 = (long)n2 & 0xFFFFFFFFL;
        nArray[n3 + 0] = (int)(l3 += l4 * (l2 & 0xFFFFFFFFL) + ((long)nArray[n3 + 0] & 0xFFFFFFFFL));
        l3 >>>= 32;
        nArray[n3 + 1] = (int)(l3 += l4 * (l2 >>> 32) + ((long)nArray[n3 + 1] & 0xFFFFFFFFL));
        l3 >>>= 32;
        nArray[n3 + 2] = (int)(l3 += (long)nArray[n3 + 2] & 0xFFFFFFFFL);
        return (l3 >>>= 32) == 0L ? 0 : Nat.incAt(7, nArray, n3, 3);
    }

    public static int mulWord(int n2, int[] nArray, int[] nArray2, int n3) {
        long l2 = 0L;
        long l3 = (long)n2 & 0xFFFFFFFFL;
        int n4 = 0;
        do {
            nArray2[n3 + n4] = (int)(l2 += l3 * ((long)nArray[n4] & 0xFFFFFFFFL));
            l2 >>>= 32;
        } while (++n4 < 7);
        return (int)l2;
    }

    public static void square(int[] nArray, int[] nArray2) {
        long l2;
        long l3;
        long l4 = (long)nArray[0] & 0xFFFFFFFFL;
        int n2 = 0;
        int n3 = 6;
        int n4 = 14;
        do {
            l3 = (long)nArray[n3--] & 0xFFFFFFFFL;
            l2 = l3 * l3;
            nArray2[--n4] = n2 << 31 | (int)(l2 >>> 33);
            nArray2[--n4] = (int)(l2 >>> 1);
            n2 = (int)l2;
        } while (n3 > 0);
        l3 = l4 * l4;
        long l5 = (long)(n2 << 31) & 0xFFFFFFFFL | l3 >>> 33;
        nArray2[0] = (int)l3;
        n2 = (int)(l3 >>> 32) & 1;
        long l6 = (long)nArray[1] & 0xFFFFFFFFL;
        l3 = (long)nArray2[2] & 0xFFFFFFFFL;
        int n5 = (int)(l5 += l6 * l4);
        nArray2[1] = n5 << 1 | n2;
        n2 = n5 >>> 31;
        l3 += l5 >>> 32;
        l2 = (long)nArray[2] & 0xFFFFFFFFL;
        long l7 = (long)nArray2[3] & 0xFFFFFFFFL;
        long l8 = (long)nArray2[4] & 0xFFFFFFFFL;
        n5 = (int)(l3 += l2 * l4);
        nArray2[2] = n5 << 1 | n2;
        n2 = n5 >>> 31;
        l7 &= 0xFFFFFFFFL;
        long l9 = (long)nArray[3] & 0xFFFFFFFFL;
        long l10 = ((long)nArray2[5] & 0xFFFFFFFFL) + ((l8 += (l7 += (l3 >>> 32) + l2 * l6) >>> 32) >>> 32);
        l8 &= 0xFFFFFFFFL;
        long l11 = ((long)nArray2[6] & 0xFFFFFFFFL) + (l10 >>> 32);
        l10 &= 0xFFFFFFFFL;
        n5 = (int)(l7 += l9 * l4);
        nArray2[3] = n5 << 1 | n2;
        n2 = n5 >>> 31;
        l8 &= 0xFFFFFFFFL;
        l10 &= 0xFFFFFFFFL;
        long l12 = (long)nArray[4] & 0xFFFFFFFFL;
        long l13 = ((long)nArray2[7] & 0xFFFFFFFFL) + ((l11 += (l10 += ((l8 += (l7 >>> 32) + l9 * l6) >>> 32) + l9 * l2) >>> 32) >>> 32);
        l11 &= 0xFFFFFFFFL;
        long l14 = ((long)nArray2[8] & 0xFFFFFFFFL) + (l13 >>> 32);
        l13 &= 0xFFFFFFFFL;
        n5 = (int)(l8 += l12 * l4);
        nArray2[4] = n5 << 1 | n2;
        n2 = n5 >>> 31;
        l10 &= 0xFFFFFFFFL;
        l11 &= 0xFFFFFFFFL;
        l13 &= 0xFFFFFFFFL;
        long l15 = (long)nArray[5] & 0xFFFFFFFFL;
        long l16 = ((long)nArray2[9] & 0xFFFFFFFFL) + ((l14 += (l13 += ((l11 += ((l10 += (l8 >>> 32) + l12 * l6) >>> 32) + l12 * l2) >>> 32) + l12 * l9) >>> 32) >>> 32);
        l14 &= 0xFFFFFFFFL;
        long l17 = ((long)nArray2[10] & 0xFFFFFFFFL) + (l16 >>> 32);
        l16 &= 0xFFFFFFFFL;
        n5 = (int)(l10 += l15 * l4);
        nArray2[5] = n5 << 1 | n2;
        n2 = n5 >>> 31;
        l11 &= 0xFFFFFFFFL;
        l13 &= 0xFFFFFFFFL;
        l14 &= 0xFFFFFFFFL;
        l16 &= 0xFFFFFFFFL;
        long l18 = (long)nArray[6] & 0xFFFFFFFFL;
        long l19 = ((long)nArray2[11] & 0xFFFFFFFFL) + ((l17 += (l16 += ((l14 += ((l13 += ((l11 += (l10 >>> 32) + l15 * l6) >>> 32) + l15 * l2) >>> 32) + l15 * l9) >>> 32) + l15 * l12) >>> 32) >>> 32);
        l17 &= 0xFFFFFFFFL;
        long l20 = ((long)nArray2[12] & 0xFFFFFFFFL) + (l19 >>> 32);
        l19 &= 0xFFFFFFFFL;
        n5 = (int)(l11 += l18 * l4);
        nArray2[6] = n5 << 1 | n2;
        n2 = n5 >>> 31;
        l20 += (l19 += ((l17 += ((l16 += ((l14 += ((l13 += (l11 >>> 32) + l18 * l6) >>> 32) + l18 * l2) >>> 32) + l18 * l9) >>> 32) + l18 * l12) >>> 32) + l18 * l15) >>> 32;
        n5 = (int)l13;
        nArray2[7] = n5 << 1 | n2;
        n2 = n5 >>> 31;
        n5 = (int)l14;
        nArray2[8] = n5 << 1 | n2;
        n2 = n5 >>> 31;
        n5 = (int)l16;
        nArray2[9] = n5 << 1 | n2;
        n2 = n5 >>> 31;
        n5 = (int)l17;
        nArray2[10] = n5 << 1 | n2;
        n2 = n5 >>> 31;
        n5 = (int)l19;
        nArray2[11] = n5 << 1 | n2;
        n2 = n5 >>> 31;
        n5 = (int)l20;
        nArray2[12] = n5 << 1 | n2;
        n2 = n5 >>> 31;
        n5 = nArray2[13] + (int)(l20 >>> 32);
        nArray2[13] = n5 << 1 | n2;
    }

    public static void square(int[] nArray, int n2, int[] nArray2, int n3) {
        long l2;
        long l3;
        long l4 = (long)nArray[n2 + 0] & 0xFFFFFFFFL;
        int n4 = 0;
        int n5 = 6;
        int n6 = 14;
        do {
            l3 = (long)nArray[n2 + n5--] & 0xFFFFFFFFL;
            l2 = l3 * l3;
            nArray2[n3 + --n6] = n4 << 31 | (int)(l2 >>> 33);
            nArray2[n3 + --n6] = (int)(l2 >>> 1);
            n4 = (int)l2;
        } while (n5 > 0);
        l3 = l4 * l4;
        long l5 = (long)(n4 << 31) & 0xFFFFFFFFL | l3 >>> 33;
        nArray2[n3 + 0] = (int)l3;
        n4 = (int)(l3 >>> 32) & 1;
        long l6 = (long)nArray[n2 + 1] & 0xFFFFFFFFL;
        l3 = (long)nArray2[n3 + 2] & 0xFFFFFFFFL;
        int n7 = (int)(l5 += l6 * l4);
        nArray2[n3 + 1] = n7 << 1 | n4;
        n4 = n7 >>> 31;
        l3 += l5 >>> 32;
        l2 = (long)nArray[n2 + 2] & 0xFFFFFFFFL;
        long l7 = (long)nArray2[n3 + 3] & 0xFFFFFFFFL;
        long l8 = (long)nArray2[n3 + 4] & 0xFFFFFFFFL;
        n7 = (int)(l3 += l2 * l4);
        nArray2[n3 + 2] = n7 << 1 | n4;
        n4 = n7 >>> 31;
        l7 &= 0xFFFFFFFFL;
        long l9 = (long)nArray[n2 + 3] & 0xFFFFFFFFL;
        long l10 = ((long)nArray2[n3 + 5] & 0xFFFFFFFFL) + ((l8 += (l7 += (l3 >>> 32) + l2 * l6) >>> 32) >>> 32);
        l8 &= 0xFFFFFFFFL;
        long l11 = ((long)nArray2[n3 + 6] & 0xFFFFFFFFL) + (l10 >>> 32);
        l10 &= 0xFFFFFFFFL;
        n7 = (int)(l7 += l9 * l4);
        nArray2[n3 + 3] = n7 << 1 | n4;
        n4 = n7 >>> 31;
        l8 &= 0xFFFFFFFFL;
        l10 &= 0xFFFFFFFFL;
        long l12 = (long)nArray[n2 + 4] & 0xFFFFFFFFL;
        long l13 = ((long)nArray2[n3 + 7] & 0xFFFFFFFFL) + ((l11 += (l10 += ((l8 += (l7 >>> 32) + l9 * l6) >>> 32) + l9 * l2) >>> 32) >>> 32);
        l11 &= 0xFFFFFFFFL;
        long l14 = ((long)nArray2[n3 + 8] & 0xFFFFFFFFL) + (l13 >>> 32);
        l13 &= 0xFFFFFFFFL;
        n7 = (int)(l8 += l12 * l4);
        nArray2[n3 + 4] = n7 << 1 | n4;
        n4 = n7 >>> 31;
        l10 &= 0xFFFFFFFFL;
        l11 &= 0xFFFFFFFFL;
        l13 &= 0xFFFFFFFFL;
        long l15 = (long)nArray[n2 + 5] & 0xFFFFFFFFL;
        long l16 = ((long)nArray2[n3 + 9] & 0xFFFFFFFFL) + ((l14 += (l13 += ((l11 += ((l10 += (l8 >>> 32) + l12 * l6) >>> 32) + l12 * l2) >>> 32) + l12 * l9) >>> 32) >>> 32);
        l14 &= 0xFFFFFFFFL;
        long l17 = ((long)nArray2[n3 + 10] & 0xFFFFFFFFL) + (l16 >>> 32);
        l16 &= 0xFFFFFFFFL;
        n7 = (int)(l10 += l15 * l4);
        nArray2[n3 + 5] = n7 << 1 | n4;
        n4 = n7 >>> 31;
        l11 &= 0xFFFFFFFFL;
        l13 &= 0xFFFFFFFFL;
        l14 &= 0xFFFFFFFFL;
        l16 &= 0xFFFFFFFFL;
        long l18 = (long)nArray[n2 + 6] & 0xFFFFFFFFL;
        long l19 = ((long)nArray2[n3 + 11] & 0xFFFFFFFFL) + ((l17 += (l16 += ((l14 += ((l13 += ((l11 += (l10 >>> 32) + l15 * l6) >>> 32) + l15 * l2) >>> 32) + l15 * l9) >>> 32) + l15 * l12) >>> 32) >>> 32);
        l17 &= 0xFFFFFFFFL;
        long l20 = ((long)nArray2[n3 + 12] & 0xFFFFFFFFL) + (l19 >>> 32);
        l19 &= 0xFFFFFFFFL;
        n7 = (int)(l11 += l18 * l4);
        nArray2[n3 + 6] = n7 << 1 | n4;
        n4 = n7 >>> 31;
        l20 += (l19 += ((l17 += ((l16 += ((l14 += ((l13 += (l11 >>> 32) + l18 * l6) >>> 32) + l18 * l2) >>> 32) + l18 * l9) >>> 32) + l18 * l12) >>> 32) + l18 * l15) >>> 32;
        n7 = (int)l13;
        nArray2[n3 + 7] = n7 << 1 | n4;
        n4 = n7 >>> 31;
        n7 = (int)l14;
        nArray2[n3 + 8] = n7 << 1 | n4;
        n4 = n7 >>> 31;
        n7 = (int)l16;
        nArray2[n3 + 9] = n7 << 1 | n4;
        n4 = n7 >>> 31;
        n7 = (int)l17;
        nArray2[n3 + 10] = n7 << 1 | n4;
        n4 = n7 >>> 31;
        n7 = (int)l19;
        nArray2[n3 + 11] = n7 << 1 | n4;
        n4 = n7 >>> 31;
        n7 = (int)l20;
        nArray2[n3 + 12] = n7 << 1 | n4;
        n4 = n7 >>> 31;
        n7 = nArray2[n3 + 13] + (int)(l20 >>> 32);
        nArray2[n3 + 13] = n7 << 1 | n4;
    }

    public static int sub(int[] nArray, int[] nArray2, int[] nArray3) {
        long l2 = 0L;
        nArray3[0] = (int)(l2 += ((long)nArray[0] & 0xFFFFFFFFL) - ((long)nArray2[0] & 0xFFFFFFFFL));
        l2 >>= 32;
        nArray3[1] = (int)(l2 += ((long)nArray[1] & 0xFFFFFFFFL) - ((long)nArray2[1] & 0xFFFFFFFFL));
        l2 >>= 32;
        nArray3[2] = (int)(l2 += ((long)nArray[2] & 0xFFFFFFFFL) - ((long)nArray2[2] & 0xFFFFFFFFL));
        l2 >>= 32;
        nArray3[3] = (int)(l2 += ((long)nArray[3] & 0xFFFFFFFFL) - ((long)nArray2[3] & 0xFFFFFFFFL));
        l2 >>= 32;
        nArray3[4] = (int)(l2 += ((long)nArray[4] & 0xFFFFFFFFL) - ((long)nArray2[4] & 0xFFFFFFFFL));
        l2 >>= 32;
        nArray3[5] = (int)(l2 += ((long)nArray[5] & 0xFFFFFFFFL) - ((long)nArray2[5] & 0xFFFFFFFFL));
        l2 >>= 32;
        nArray3[6] = (int)(l2 += ((long)nArray[6] & 0xFFFFFFFFL) - ((long)nArray2[6] & 0xFFFFFFFFL));
        return (int)(l2 >>= 32);
    }

    public static int sub(int[] nArray, int n2, int[] nArray2, int n3, int[] nArray3, int n4) {
        long l2 = 0L;
        nArray3[n4 + 0] = (int)(l2 += ((long)nArray[n2 + 0] & 0xFFFFFFFFL) - ((long)nArray2[n3 + 0] & 0xFFFFFFFFL));
        l2 >>= 32;
        nArray3[n4 + 1] = (int)(l2 += ((long)nArray[n2 + 1] & 0xFFFFFFFFL) - ((long)nArray2[n3 + 1] & 0xFFFFFFFFL));
        l2 >>= 32;
        nArray3[n4 + 2] = (int)(l2 += ((long)nArray[n2 + 2] & 0xFFFFFFFFL) - ((long)nArray2[n3 + 2] & 0xFFFFFFFFL));
        l2 >>= 32;
        nArray3[n4 + 3] = (int)(l2 += ((long)nArray[n2 + 3] & 0xFFFFFFFFL) - ((long)nArray2[n3 + 3] & 0xFFFFFFFFL));
        l2 >>= 32;
        nArray3[n4 + 4] = (int)(l2 += ((long)nArray[n2 + 4] & 0xFFFFFFFFL) - ((long)nArray2[n3 + 4] & 0xFFFFFFFFL));
        l2 >>= 32;
        nArray3[n4 + 5] = (int)(l2 += ((long)nArray[n2 + 5] & 0xFFFFFFFFL) - ((long)nArray2[n3 + 5] & 0xFFFFFFFFL));
        l2 >>= 32;
        nArray3[n4 + 6] = (int)(l2 += ((long)nArray[n2 + 6] & 0xFFFFFFFFL) - ((long)nArray2[n3 + 6] & 0xFFFFFFFFL));
        return (int)(l2 >>= 32);
    }

    public static int subBothFrom(int[] nArray, int[] nArray2, int[] nArray3) {
        long l2 = 0L;
        nArray3[0] = (int)(l2 += ((long)nArray3[0] & 0xFFFFFFFFL) - ((long)nArray[0] & 0xFFFFFFFFL) - ((long)nArray2[0] & 0xFFFFFFFFL));
        l2 >>= 32;
        nArray3[1] = (int)(l2 += ((long)nArray3[1] & 0xFFFFFFFFL) - ((long)nArray[1] & 0xFFFFFFFFL) - ((long)nArray2[1] & 0xFFFFFFFFL));
        l2 >>= 32;
        nArray3[2] = (int)(l2 += ((long)nArray3[2] & 0xFFFFFFFFL) - ((long)nArray[2] & 0xFFFFFFFFL) - ((long)nArray2[2] & 0xFFFFFFFFL));
        l2 >>= 32;
        nArray3[3] = (int)(l2 += ((long)nArray3[3] & 0xFFFFFFFFL) - ((long)nArray[3] & 0xFFFFFFFFL) - ((long)nArray2[3] & 0xFFFFFFFFL));
        l2 >>= 32;
        nArray3[4] = (int)(l2 += ((long)nArray3[4] & 0xFFFFFFFFL) - ((long)nArray[4] & 0xFFFFFFFFL) - ((long)nArray2[4] & 0xFFFFFFFFL));
        l2 >>= 32;
        nArray3[5] = (int)(l2 += ((long)nArray3[5] & 0xFFFFFFFFL) - ((long)nArray[5] & 0xFFFFFFFFL) - ((long)nArray2[5] & 0xFFFFFFFFL));
        l2 >>= 32;
        nArray3[6] = (int)(l2 += ((long)nArray3[6] & 0xFFFFFFFFL) - ((long)nArray[6] & 0xFFFFFFFFL) - ((long)nArray2[6] & 0xFFFFFFFFL));
        return (int)(l2 >>= 32);
    }

    public static int subFrom(int[] nArray, int[] nArray2) {
        long l2 = 0L;
        nArray2[0] = (int)(l2 += ((long)nArray2[0] & 0xFFFFFFFFL) - ((long)nArray[0] & 0xFFFFFFFFL));
        l2 >>= 32;
        nArray2[1] = (int)(l2 += ((long)nArray2[1] & 0xFFFFFFFFL) - ((long)nArray[1] & 0xFFFFFFFFL));
        l2 >>= 32;
        nArray2[2] = (int)(l2 += ((long)nArray2[2] & 0xFFFFFFFFL) - ((long)nArray[2] & 0xFFFFFFFFL));
        l2 >>= 32;
        nArray2[3] = (int)(l2 += ((long)nArray2[3] & 0xFFFFFFFFL) - ((long)nArray[3] & 0xFFFFFFFFL));
        l2 >>= 32;
        nArray2[4] = (int)(l2 += ((long)nArray2[4] & 0xFFFFFFFFL) - ((long)nArray[4] & 0xFFFFFFFFL));
        l2 >>= 32;
        nArray2[5] = (int)(l2 += ((long)nArray2[5] & 0xFFFFFFFFL) - ((long)nArray[5] & 0xFFFFFFFFL));
        l2 >>= 32;
        nArray2[6] = (int)(l2 += ((long)nArray2[6] & 0xFFFFFFFFL) - ((long)nArray[6] & 0xFFFFFFFFL));
        return (int)(l2 >>= 32);
    }

    public static int subFrom(int[] nArray, int n2, int[] nArray2, int n3) {
        long l2 = 0L;
        nArray2[n3 + 0] = (int)(l2 += ((long)nArray2[n3 + 0] & 0xFFFFFFFFL) - ((long)nArray[n2 + 0] & 0xFFFFFFFFL));
        l2 >>= 32;
        nArray2[n3 + 1] = (int)(l2 += ((long)nArray2[n3 + 1] & 0xFFFFFFFFL) - ((long)nArray[n2 + 1] & 0xFFFFFFFFL));
        l2 >>= 32;
        nArray2[n3 + 2] = (int)(l2 += ((long)nArray2[n3 + 2] & 0xFFFFFFFFL) - ((long)nArray[n2 + 2] & 0xFFFFFFFFL));
        l2 >>= 32;
        nArray2[n3 + 3] = (int)(l2 += ((long)nArray2[n3 + 3] & 0xFFFFFFFFL) - ((long)nArray[n2 + 3] & 0xFFFFFFFFL));
        l2 >>= 32;
        nArray2[n3 + 4] = (int)(l2 += ((long)nArray2[n3 + 4] & 0xFFFFFFFFL) - ((long)nArray[n2 + 4] & 0xFFFFFFFFL));
        l2 >>= 32;
        nArray2[n3 + 5] = (int)(l2 += ((long)nArray2[n3 + 5] & 0xFFFFFFFFL) - ((long)nArray[n2 + 5] & 0xFFFFFFFFL));
        l2 >>= 32;
        nArray2[n3 + 6] = (int)(l2 += ((long)nArray2[n3 + 6] & 0xFFFFFFFFL) - ((long)nArray[n2 + 6] & 0xFFFFFFFFL));
        return (int)(l2 >>= 32);
    }

    public static BigInteger toBigInteger(int[] nArray) {
        byte[] byArray = new byte[28];
        for (int i2 = 0; i2 < 7; ++i2) {
            int n2 = nArray[i2];
            if (n2 == 0) continue;
            Pack.intToBigEndian(n2, byArray, 6 - i2 << 2);
        }
        return new BigInteger(1, byArray);
    }

    public static void zero(int[] nArray) {
        nArray[0] = 0;
        nArray[1] = 0;
        nArray[2] = 0;
        nArray[3] = 0;
        nArray[4] = 0;
        nArray[5] = 0;
        nArray[6] = 0;
    }
}

