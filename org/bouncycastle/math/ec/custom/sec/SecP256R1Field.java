/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.math.ec.custom.sec;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle.math.raw.Mod;
import org.bouncycastle.math.raw.Nat;
import org.bouncycastle.math.raw.Nat256;
import org.bouncycastle.util.Pack;

public class SecP256R1Field {
    private static final long M = 0xFFFFFFFFL;
    static final int[] P = new int[]{-1, -1, -1, 0, 0, 0, 1, -1};
    private static final int[] PExt = new int[]{1, 0, 0, -2, -1, -1, -2, 1, -2, 1, -2, 1, 1, -2, 2, -2};
    private static final int P7 = -1;
    private static final int PExt15s1 = Integer.MAX_VALUE;

    public static void add(int[] nArray, int[] nArray2, int[] nArray3) {
        int n2 = Nat256.add(nArray, nArray2, nArray3);
        if (n2 != 0 || nArray3[7] == -1 && Nat256.gte(nArray3, P)) {
            SecP256R1Field.addPInvTo(nArray3);
        }
    }

    public static void addExt(int[] nArray, int[] nArray2, int[] nArray3) {
        int n2 = Nat.add(16, nArray, nArray2, nArray3);
        if (n2 != 0 || nArray3[15] >>> 1 >= Integer.MAX_VALUE && Nat.gte(16, nArray3, PExt)) {
            Nat.subFrom(16, PExt, nArray3);
        }
    }

    public static void addOne(int[] nArray, int[] nArray2) {
        int n2 = Nat.inc(8, nArray, nArray2);
        if (n2 != 0 || nArray2[7] == -1 && Nat256.gte(nArray2, P)) {
            SecP256R1Field.addPInvTo(nArray2);
        }
    }

    public static int[] fromBigInteger(BigInteger bigInteger) {
        int[] nArray = Nat256.fromBigInteger(bigInteger);
        if (nArray[7] == -1 && Nat256.gte(nArray, P)) {
            Nat256.subFrom(P, nArray);
        }
        return nArray;
    }

    public static void half(int[] nArray, int[] nArray2) {
        if ((nArray[0] & 1) == 0) {
            Nat.shiftDownBit(8, nArray, 0, nArray2);
        } else {
            int n2 = Nat256.add(nArray, P, nArray2);
            Nat.shiftDownBit(8, nArray2, n2);
        }
    }

    public static void inv(int[] nArray, int[] nArray2) {
        Mod.checkedModOddInverse(P, nArray, nArray2);
    }

    public static int isZero(int[] nArray) {
        int n2 = 0;
        for (int i2 = 0; i2 < 8; ++i2) {
            n2 |= nArray[i2];
        }
        n2 = n2 >>> 1 | n2 & 1;
        return n2 - 1 >> 31;
    }

    public static void multiply(int[] nArray, int[] nArray2, int[] nArray3) {
        int[] nArray4 = Nat256.createExt();
        Nat256.mul(nArray, nArray2, nArray4);
        SecP256R1Field.reduce(nArray4, nArray3);
    }

    public static void multiply(int[] nArray, int[] nArray2, int[] nArray3, int[] nArray4) {
        Nat256.mul(nArray, nArray2, nArray4);
        SecP256R1Field.reduce(nArray4, nArray3);
    }

    public static void multiplyAddToExt(int[] nArray, int[] nArray2, int[] nArray3) {
        int n2 = Nat256.mulAddTo(nArray, nArray2, nArray3);
        if (n2 != 0 || nArray3[15] >>> 1 >= Integer.MAX_VALUE && Nat.gte(16, nArray3, PExt)) {
            Nat.subFrom(16, PExt, nArray3);
        }
    }

    public static void negate(int[] nArray, int[] nArray2) {
        if (0 != SecP256R1Field.isZero(nArray)) {
            Nat256.sub(P, P, nArray2);
        } else {
            Nat256.sub(P, nArray, nArray2);
        }
    }

    public static void random(SecureRandom secureRandom, int[] nArray) {
        byte[] byArray = new byte[32];
        do {
            secureRandom.nextBytes(byArray);
            Pack.littleEndianToInt(byArray, 0, nArray, 0, 8);
        } while (0 == Nat.lessThan(8, nArray, P));
    }

    public static void randomMult(SecureRandom secureRandom, int[] nArray) {
        do {
            SecP256R1Field.random(secureRandom, nArray);
        } while (0 != SecP256R1Field.isZero(nArray));
    }

    public static void reduce(int[] nArray, int[] nArray2) {
        long l2 = (long)nArray[8] & 0xFFFFFFFFL;
        long l3 = (long)nArray[9] & 0xFFFFFFFFL;
        long l4 = (long)nArray[10] & 0xFFFFFFFFL;
        long l5 = (long)nArray[11] & 0xFFFFFFFFL;
        long l6 = (long)nArray[12] & 0xFFFFFFFFL;
        long l7 = (long)nArray[13] & 0xFFFFFFFFL;
        long l8 = (long)nArray[14] & 0xFFFFFFFFL;
        long l9 = (long)nArray[15] & 0xFFFFFFFFL;
        long l10 = (l2 -= 6L) + l3;
        long l11 = l3 + l4;
        long l12 = l4 + l5 - l9;
        long l13 = l5 + l6;
        long l14 = l6 + l7;
        long l15 = l7 + l8;
        long l16 = l8 + l9;
        long l17 = l15 - l10;
        long l18 = 0L;
        nArray2[0] = (int)(l18 += ((long)nArray[0] & 0xFFFFFFFFL) - l13 - l17);
        l18 >>= 32;
        nArray2[1] = (int)(l18 += ((long)nArray[1] & 0xFFFFFFFFL) + l11 - l14 - l16);
        l18 >>= 32;
        nArray2[2] = (int)(l18 += ((long)nArray[2] & 0xFFFFFFFFL) + l12 - l15);
        l18 >>= 32;
        nArray2[3] = (int)(l18 += ((long)nArray[3] & 0xFFFFFFFFL) + (l13 << 1) + l17 - l16);
        l18 >>= 32;
        nArray2[4] = (int)(l18 += ((long)nArray[4] & 0xFFFFFFFFL) + (l14 << 1) + l8 - l11);
        l18 >>= 32;
        nArray2[5] = (int)(l18 += ((long)nArray[5] & 0xFFFFFFFFL) + (l15 << 1) - l12);
        l18 >>= 32;
        nArray2[6] = (int)(l18 += ((long)nArray[6] & 0xFFFFFFFFL) + (l16 << 1) + l17);
        l18 >>= 32;
        nArray2[7] = (int)(l18 += ((long)nArray[7] & 0xFFFFFFFFL) + (l9 << 1) + l2 - l12 - l14);
        l18 >>= 32;
        SecP256R1Field.reduce32((int)(l18 += 6L), nArray2);
    }

    public static void reduce32(int n2, int[] nArray) {
        long l2 = 0L;
        if (n2 != 0) {
            long l3 = (long)n2 & 0xFFFFFFFFL;
            nArray[0] = (int)(l2 += ((long)nArray[0] & 0xFFFFFFFFL) + l3);
            if ((l2 >>= 32) != 0L) {
                nArray[1] = (int)(l2 += (long)nArray[1] & 0xFFFFFFFFL);
                l2 >>= 32;
                nArray[2] = (int)(l2 += (long)nArray[2] & 0xFFFFFFFFL);
                l2 >>= 32;
            }
            nArray[3] = (int)(l2 += ((long)nArray[3] & 0xFFFFFFFFL) - l3);
            if ((l2 >>= 32) != 0L) {
                nArray[4] = (int)(l2 += (long)nArray[4] & 0xFFFFFFFFL);
                l2 >>= 32;
                nArray[5] = (int)(l2 += (long)nArray[5] & 0xFFFFFFFFL);
                l2 >>= 32;
            }
            nArray[6] = (int)(l2 += ((long)nArray[6] & 0xFFFFFFFFL) - l3);
            l2 >>= 32;
            nArray[7] = (int)(l2 += ((long)nArray[7] & 0xFFFFFFFFL) + l3);
            l2 >>= 32;
        }
        if (l2 != 0L || nArray[7] == -1 && Nat256.gte(nArray, P)) {
            SecP256R1Field.addPInvTo(nArray);
        }
    }

    public static void square(int[] nArray, int[] nArray2) {
        int[] nArray3 = Nat256.createExt();
        Nat256.square(nArray, nArray3);
        SecP256R1Field.reduce(nArray3, nArray2);
    }

    public static void square(int[] nArray, int[] nArray2, int[] nArray3) {
        Nat256.square(nArray, nArray3);
        SecP256R1Field.reduce(nArray3, nArray2);
    }

    public static void squareN(int[] nArray, int n2, int[] nArray2) {
        int[] nArray3 = Nat256.createExt();
        Nat256.square(nArray, nArray3);
        SecP256R1Field.reduce(nArray3, nArray2);
        while (--n2 > 0) {
            Nat256.square(nArray2, nArray3);
            SecP256R1Field.reduce(nArray3, nArray2);
        }
    }

    public static void squareN(int[] nArray, int n2, int[] nArray2, int[] nArray3) {
        Nat256.square(nArray, nArray3);
        SecP256R1Field.reduce(nArray3, nArray2);
        while (--n2 > 0) {
            Nat256.square(nArray2, nArray3);
            SecP256R1Field.reduce(nArray3, nArray2);
        }
    }

    public static void subtract(int[] nArray, int[] nArray2, int[] nArray3) {
        int n2 = Nat256.sub(nArray, nArray2, nArray3);
        if (n2 != 0) {
            SecP256R1Field.subPInvFrom(nArray3);
        }
    }

    public static void subtractExt(int[] nArray, int[] nArray2, int[] nArray3) {
        int n2 = Nat.sub(16, nArray, nArray2, nArray3);
        if (n2 != 0) {
            Nat.addTo(16, PExt, nArray3);
        }
    }

    public static void twice(int[] nArray, int[] nArray2) {
        int n2 = Nat.shiftUpBit(8, nArray, 0, nArray2);
        if (n2 != 0 || nArray2[7] == -1 && Nat256.gte(nArray2, P)) {
            SecP256R1Field.addPInvTo(nArray2);
        }
    }

    private static void addPInvTo(int[] nArray) {
        long l2 = ((long)nArray[0] & 0xFFFFFFFFL) + 1L;
        nArray[0] = (int)l2;
        if ((l2 >>= 32) != 0L) {
            nArray[1] = (int)(l2 += (long)nArray[1] & 0xFFFFFFFFL);
            l2 >>= 32;
            nArray[2] = (int)(l2 += (long)nArray[2] & 0xFFFFFFFFL);
            l2 >>= 32;
        }
        nArray[3] = (int)(l2 += ((long)nArray[3] & 0xFFFFFFFFL) - 1L);
        if ((l2 >>= 32) != 0L) {
            nArray[4] = (int)(l2 += (long)nArray[4] & 0xFFFFFFFFL);
            l2 >>= 32;
            nArray[5] = (int)(l2 += (long)nArray[5] & 0xFFFFFFFFL);
            l2 >>= 32;
        }
        nArray[6] = (int)(l2 += ((long)nArray[6] & 0xFFFFFFFFL) - 1L);
        l2 >>= 32;
        nArray[7] = (int)(l2 += ((long)nArray[7] & 0xFFFFFFFFL) + 1L);
    }

    private static void subPInvFrom(int[] nArray) {
        long l2 = ((long)nArray[0] & 0xFFFFFFFFL) - 1L;
        nArray[0] = (int)l2;
        if ((l2 >>= 32) != 0L) {
            nArray[1] = (int)(l2 += (long)nArray[1] & 0xFFFFFFFFL);
            l2 >>= 32;
            nArray[2] = (int)(l2 += (long)nArray[2] & 0xFFFFFFFFL);
            l2 >>= 32;
        }
        nArray[3] = (int)(l2 += ((long)nArray[3] & 0xFFFFFFFFL) + 1L);
        if ((l2 >>= 32) != 0L) {
            nArray[4] = (int)(l2 += (long)nArray[4] & 0xFFFFFFFFL);
            l2 >>= 32;
            nArray[5] = (int)(l2 += (long)nArray[5] & 0xFFFFFFFFL);
            l2 >>= 32;
        }
        nArray[6] = (int)(l2 += ((long)nArray[6] & 0xFFFFFFFFL) + 1L);
        l2 >>= 32;
        nArray[7] = (int)(l2 += ((long)nArray[7] & 0xFFFFFFFFL) - 1L);
    }
}

