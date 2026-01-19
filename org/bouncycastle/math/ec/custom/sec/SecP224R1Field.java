/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.math.ec.custom.sec;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle.math.raw.Mod;
import org.bouncycastle.math.raw.Nat;
import org.bouncycastle.math.raw.Nat224;
import org.bouncycastle.util.Pack;

public class SecP224R1Field {
    private static final long M = 0xFFFFFFFFL;
    static final int[] P = new int[]{1, 0, 0, -1, -1, -1, -1};
    private static final int[] PExt = new int[]{1, 0, 0, -2, -1, -1, 0, 2, 0, 0, -2, -1, -1, -1};
    private static final int[] PExtInv = new int[]{-1, -1, -1, 1, 0, 0, -1, -3, -1, -1, 1};
    private static final int P6 = -1;
    private static final int PExt13 = -1;

    public static void add(int[] nArray, int[] nArray2, int[] nArray3) {
        int n2 = Nat224.add(nArray, nArray2, nArray3);
        if (n2 != 0 || nArray3[6] == -1 && Nat224.gte(nArray3, P)) {
            SecP224R1Field.addPInvTo(nArray3);
        }
    }

    public static void addExt(int[] nArray, int[] nArray2, int[] nArray3) {
        int n2 = Nat.add(14, nArray, nArray2, nArray3);
        if ((n2 != 0 || nArray3[13] == -1 && Nat.gte(14, nArray3, PExt)) && Nat.addTo(PExtInv.length, PExtInv, nArray3) != 0) {
            Nat.incAt(14, nArray3, PExtInv.length);
        }
    }

    public static void addOne(int[] nArray, int[] nArray2) {
        int n2 = Nat.inc(7, nArray, nArray2);
        if (n2 != 0 || nArray2[6] == -1 && Nat224.gte(nArray2, P)) {
            SecP224R1Field.addPInvTo(nArray2);
        }
    }

    public static int[] fromBigInteger(BigInteger bigInteger) {
        int[] nArray = Nat224.fromBigInteger(bigInteger);
        if (nArray[6] == -1 && Nat224.gte(nArray, P)) {
            Nat224.subFrom(P, nArray);
        }
        return nArray;
    }

    public static void half(int[] nArray, int[] nArray2) {
        if ((nArray[0] & 1) == 0) {
            Nat.shiftDownBit(7, nArray, 0, nArray2);
        } else {
            int n2 = Nat224.add(nArray, P, nArray2);
            Nat.shiftDownBit(7, nArray2, n2);
        }
    }

    public static void inv(int[] nArray, int[] nArray2) {
        Mod.checkedModOddInverse(P, nArray, nArray2);
    }

    public static int isZero(int[] nArray) {
        int n2 = 0;
        for (int i2 = 0; i2 < 7; ++i2) {
            n2 |= nArray[i2];
        }
        n2 = n2 >>> 1 | n2 & 1;
        return n2 - 1 >> 31;
    }

    public static void multiply(int[] nArray, int[] nArray2, int[] nArray3) {
        int[] nArray4 = Nat224.createExt();
        Nat224.mul(nArray, nArray2, nArray4);
        SecP224R1Field.reduce(nArray4, nArray3);
    }

    public static void multiplyAddToExt(int[] nArray, int[] nArray2, int[] nArray3) {
        int n2 = Nat224.mulAddTo(nArray, nArray2, nArray3);
        if ((n2 != 0 || nArray3[13] == -1 && Nat.gte(14, nArray3, PExt)) && Nat.addTo(PExtInv.length, PExtInv, nArray3) != 0) {
            Nat.incAt(14, nArray3, PExtInv.length);
        }
    }

    public static void negate(int[] nArray, int[] nArray2) {
        if (0 != SecP224R1Field.isZero(nArray)) {
            Nat224.sub(P, P, nArray2);
        } else {
            Nat224.sub(P, nArray, nArray2);
        }
    }

    public static void random(SecureRandom secureRandom, int[] nArray) {
        byte[] byArray = new byte[28];
        do {
            secureRandom.nextBytes(byArray);
            Pack.littleEndianToInt(byArray, 0, nArray, 0, 7);
        } while (0 == Nat.lessThan(7, nArray, P));
    }

    public static void randomMult(SecureRandom secureRandom, int[] nArray) {
        do {
            SecP224R1Field.random(secureRandom, nArray);
        } while (0 != SecP224R1Field.isZero(nArray));
    }

    public static void reduce(int[] nArray, int[] nArray2) {
        long l2 = (long)nArray[10] & 0xFFFFFFFFL;
        long l3 = (long)nArray[11] & 0xFFFFFFFFL;
        long l4 = (long)nArray[12] & 0xFFFFFFFFL;
        long l5 = (long)nArray[13] & 0xFFFFFFFFL;
        long l6 = ((long)nArray[7] & 0xFFFFFFFFL) + l3 - 1L;
        long l7 = ((long)nArray[8] & 0xFFFFFFFFL) + l4;
        long l8 = ((long)nArray[9] & 0xFFFFFFFFL) + l5;
        long l9 = 0L;
        long l10 = (l9 += ((long)nArray[0] & 0xFFFFFFFFL) - l6) & 0xFFFFFFFFL;
        l9 >>= 32;
        nArray2[1] = (int)(l9 += ((long)nArray[1] & 0xFFFFFFFFL) - l7);
        l9 >>= 32;
        nArray2[2] = (int)(l9 += ((long)nArray[2] & 0xFFFFFFFFL) - l8);
        l9 >>= 32;
        long l11 = (l9 += ((long)nArray[3] & 0xFFFFFFFFL) + l6 - l2) & 0xFFFFFFFFL;
        l9 >>= 32;
        nArray2[4] = (int)(l9 += ((long)nArray[4] & 0xFFFFFFFFL) + l7 - l3);
        l9 >>= 32;
        nArray2[5] = (int)(l9 += ((long)nArray[5] & 0xFFFFFFFFL) + l8 - l4);
        l9 >>= 32;
        nArray2[6] = (int)(l9 += ((long)nArray[6] & 0xFFFFFFFFL) + l2 - l5);
        l9 >>= 32;
        l11 += ++l9;
        nArray2[0] = (int)(l10 -= l9);
        l9 = l10 >> 32;
        if (l9 != 0L) {
            nArray2[1] = (int)(l9 += (long)nArray2[1] & 0xFFFFFFFFL);
            l9 >>= 32;
            nArray2[2] = (int)(l9 += (long)nArray2[2] & 0xFFFFFFFFL);
            l11 += l9 >> 32;
        }
        nArray2[3] = (int)l11;
        l9 = l11 >> 32;
        if (l9 != 0L && Nat.incAt(7, nArray2, 4) != 0 || nArray2[6] == -1 && Nat224.gte(nArray2, P)) {
            SecP224R1Field.addPInvTo(nArray2);
        }
    }

    public static void reduce32(int n2, int[] nArray) {
        long l2 = 0L;
        if (n2 != 0) {
            long l3 = (long)n2 & 0xFFFFFFFFL;
            nArray[0] = (int)(l2 += ((long)nArray[0] & 0xFFFFFFFFL) - l3);
            if ((l2 >>= 32) != 0L) {
                nArray[1] = (int)(l2 += (long)nArray[1] & 0xFFFFFFFFL);
                l2 >>= 32;
                nArray[2] = (int)(l2 += (long)nArray[2] & 0xFFFFFFFFL);
                l2 >>= 32;
            }
            nArray[3] = (int)(l2 += ((long)nArray[3] & 0xFFFFFFFFL) + l3);
            l2 >>= 32;
        }
        if (l2 != 0L && Nat.incAt(7, nArray, 4) != 0 || nArray[6] == -1 && Nat224.gte(nArray, P)) {
            SecP224R1Field.addPInvTo(nArray);
        }
    }

    public static void square(int[] nArray, int[] nArray2) {
        int[] nArray3 = Nat224.createExt();
        Nat224.square(nArray, nArray3);
        SecP224R1Field.reduce(nArray3, nArray2);
    }

    public static void squareN(int[] nArray, int n2, int[] nArray2) {
        int[] nArray3 = Nat224.createExt();
        Nat224.square(nArray, nArray3);
        SecP224R1Field.reduce(nArray3, nArray2);
        while (--n2 > 0) {
            Nat224.square(nArray2, nArray3);
            SecP224R1Field.reduce(nArray3, nArray2);
        }
    }

    public static void subtract(int[] nArray, int[] nArray2, int[] nArray3) {
        int n2 = Nat224.sub(nArray, nArray2, nArray3);
        if (n2 != 0) {
            SecP224R1Field.subPInvFrom(nArray3);
        }
    }

    public static void subtractExt(int[] nArray, int[] nArray2, int[] nArray3) {
        int n2 = Nat.sub(14, nArray, nArray2, nArray3);
        if (n2 != 0 && Nat.subFrom(PExtInv.length, PExtInv, nArray3) != 0) {
            Nat.decAt(14, nArray3, PExtInv.length);
        }
    }

    public static void twice(int[] nArray, int[] nArray2) {
        int n2 = Nat.shiftUpBit(7, nArray, 0, nArray2);
        if (n2 != 0 || nArray2[6] == -1 && Nat224.gte(nArray2, P)) {
            SecP224R1Field.addPInvTo(nArray2);
        }
    }

    private static void addPInvTo(int[] nArray) {
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
            Nat.incAt(7, nArray, 4);
        }
    }

    private static void subPInvFrom(int[] nArray) {
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
            Nat.decAt(7, nArray, 4);
        }
    }
}

