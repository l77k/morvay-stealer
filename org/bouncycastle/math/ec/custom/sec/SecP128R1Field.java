/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.math.ec.custom.sec;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle.math.raw.Mod;
import org.bouncycastle.math.raw.Nat;
import org.bouncycastle.math.raw.Nat128;
import org.bouncycastle.math.raw.Nat256;
import org.bouncycastle.util.Pack;

public class SecP128R1Field {
    private static final long M = 0xFFFFFFFFL;
    static final int[] P = new int[]{-1, -1, -1, -3};
    private static final int[] PExt = new int[]{1, 0, 0, 4, -2, -1, 3, -4};
    private static final int[] PExtInv = new int[]{-1, -1, -1, -5, 1, 0, -4, 3};
    private static final int P3s1 = 0x7FFFFFFE;
    private static final int PExt7s1 = 0x7FFFFFFE;

    public static void add(int[] nArray, int[] nArray2, int[] nArray3) {
        int n2 = Nat128.add(nArray, nArray2, nArray3);
        if (n2 != 0 || nArray3[3] >>> 1 >= 0x7FFFFFFE && Nat128.gte(nArray3, P)) {
            SecP128R1Field.addPInvTo(nArray3);
        }
    }

    public static void addExt(int[] nArray, int[] nArray2, int[] nArray3) {
        int n2 = Nat256.add(nArray, nArray2, nArray3);
        if (n2 != 0 || nArray3[7] >>> 1 >= 0x7FFFFFFE && Nat256.gte(nArray3, PExt)) {
            Nat.addTo(PExtInv.length, PExtInv, nArray3);
        }
    }

    public static void addOne(int[] nArray, int[] nArray2) {
        int n2 = Nat.inc(4, nArray, nArray2);
        if (n2 != 0 || nArray2[3] >>> 1 >= 0x7FFFFFFE && Nat128.gte(nArray2, P)) {
            SecP128R1Field.addPInvTo(nArray2);
        }
    }

    public static int[] fromBigInteger(BigInteger bigInteger) {
        int[] nArray = Nat128.fromBigInteger(bigInteger);
        if (nArray[3] >>> 1 >= 0x7FFFFFFE && Nat128.gte(nArray, P)) {
            Nat128.subFrom(P, nArray);
        }
        return nArray;
    }

    public static void half(int[] nArray, int[] nArray2) {
        if ((nArray[0] & 1) == 0) {
            Nat.shiftDownBit(4, nArray, 0, nArray2);
        } else {
            int n2 = Nat128.add(nArray, P, nArray2);
            Nat.shiftDownBit(4, nArray2, n2);
        }
    }

    public static void inv(int[] nArray, int[] nArray2) {
        Mod.checkedModOddInverse(P, nArray, nArray2);
    }

    public static int isZero(int[] nArray) {
        int n2 = 0;
        for (int i2 = 0; i2 < 4; ++i2) {
            n2 |= nArray[i2];
        }
        n2 = n2 >>> 1 | n2 & 1;
        return n2 - 1 >> 31;
    }

    public static void multiply(int[] nArray, int[] nArray2, int[] nArray3) {
        int[] nArray4 = Nat128.createExt();
        Nat128.mul(nArray, nArray2, nArray4);
        SecP128R1Field.reduce(nArray4, nArray3);
    }

    public static void multiplyAddToExt(int[] nArray, int[] nArray2, int[] nArray3) {
        int n2 = Nat128.mulAddTo(nArray, nArray2, nArray3);
        if (n2 != 0 || nArray3[7] >>> 1 >= 0x7FFFFFFE && Nat256.gte(nArray3, PExt)) {
            Nat.addTo(PExtInv.length, PExtInv, nArray3);
        }
    }

    public static void negate(int[] nArray, int[] nArray2) {
        if (0 != SecP128R1Field.isZero(nArray)) {
            Nat128.sub(P, P, nArray2);
        } else {
            Nat128.sub(P, nArray, nArray2);
        }
    }

    public static void random(SecureRandom secureRandom, int[] nArray) {
        byte[] byArray = new byte[16];
        do {
            secureRandom.nextBytes(byArray);
            Pack.littleEndianToInt(byArray, 0, nArray, 0, 4);
        } while (0 == Nat.lessThan(4, nArray, P));
    }

    public static void randomMult(SecureRandom secureRandom, int[] nArray) {
        do {
            SecP128R1Field.random(secureRandom, nArray);
        } while (0 != SecP128R1Field.isZero(nArray));
    }

    public static void reduce(int[] nArray, int[] nArray2) {
        long l2 = (long)nArray[0] & 0xFFFFFFFFL;
        long l3 = (long)nArray[1] & 0xFFFFFFFFL;
        long l4 = (long)nArray[2] & 0xFFFFFFFFL;
        long l5 = (long)nArray[3] & 0xFFFFFFFFL;
        long l6 = (long)nArray[4] & 0xFFFFFFFFL;
        long l7 = (long)nArray[5] & 0xFFFFFFFFL;
        long l8 = (long)nArray[6] & 0xFFFFFFFFL;
        long l9 = (long)nArray[7] & 0xFFFFFFFFL;
        l5 += l9;
        l4 += (l8 += l9 << 1);
        l3 += (l7 += l8 << 1);
        l5 += l6 << 1;
        nArray2[0] = (int)(l2 += (l6 += l7 << 1));
        nArray2[1] = (int)(l3 += l2 >>> 32);
        nArray2[2] = (int)(l4 += l3 >>> 32);
        nArray2[3] = (int)(l5 += l4 >>> 32);
        SecP128R1Field.reduce32((int)(l5 >>> 32), nArray2);
    }

    public static void reduce32(int n2, int[] nArray) {
        while (n2 != 0) {
            long l2 = (long)n2 & 0xFFFFFFFFL;
            long l3 = ((long)nArray[0] & 0xFFFFFFFFL) + l2;
            nArray[0] = (int)l3;
            if ((l3 >>= 32) != 0L) {
                nArray[1] = (int)(l3 += (long)nArray[1] & 0xFFFFFFFFL);
                l3 >>= 32;
                nArray[2] = (int)(l3 += (long)nArray[2] & 0xFFFFFFFFL);
                l3 >>= 32;
            }
            nArray[3] = (int)(l3 += ((long)nArray[3] & 0xFFFFFFFFL) + (l2 << 1));
            n2 = (int)(l3 >>= 32);
        }
        if (nArray[3] >>> 1 >= 0x7FFFFFFE && Nat128.gte(nArray, P)) {
            SecP128R1Field.addPInvTo(nArray);
        }
    }

    public static void square(int[] nArray, int[] nArray2) {
        int[] nArray3 = Nat128.createExt();
        Nat128.square(nArray, nArray3);
        SecP128R1Field.reduce(nArray3, nArray2);
    }

    public static void squareN(int[] nArray, int n2, int[] nArray2) {
        int[] nArray3 = Nat128.createExt();
        Nat128.square(nArray, nArray3);
        SecP128R1Field.reduce(nArray3, nArray2);
        while (--n2 > 0) {
            Nat128.square(nArray2, nArray3);
            SecP128R1Field.reduce(nArray3, nArray2);
        }
    }

    public static void subtract(int[] nArray, int[] nArray2, int[] nArray3) {
        int n2 = Nat128.sub(nArray, nArray2, nArray3);
        if (n2 != 0) {
            SecP128R1Field.subPInvFrom(nArray3);
        }
    }

    public static void subtractExt(int[] nArray, int[] nArray2, int[] nArray3) {
        int n2 = Nat.sub(10, nArray, nArray2, nArray3);
        if (n2 != 0) {
            Nat.subFrom(PExtInv.length, PExtInv, nArray3);
        }
    }

    public static void twice(int[] nArray, int[] nArray2) {
        int n2 = Nat.shiftUpBit(4, nArray, 0, nArray2);
        if (n2 != 0 || nArray2[3] >>> 1 >= 0x7FFFFFFE && Nat128.gte(nArray2, P)) {
            SecP128R1Field.addPInvTo(nArray2);
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
        nArray[3] = (int)(l2 += ((long)nArray[3] & 0xFFFFFFFFL) + 2L);
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
        nArray[3] = (int)(l2 += ((long)nArray[3] & 0xFFFFFFFFL) - 2L);
    }
}

