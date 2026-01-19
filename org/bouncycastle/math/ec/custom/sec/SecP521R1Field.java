/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.math.ec.custom.sec;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle.math.raw.Mod;
import org.bouncycastle.math.raw.Nat;
import org.bouncycastle.math.raw.Nat512;
import org.bouncycastle.util.Pack;

public class SecP521R1Field {
    static final int[] P = new int[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 511};
    private static final int P16 = 511;

    public static void add(int[] nArray, int[] nArray2, int[] nArray3) {
        int n2 = Nat.add(16, nArray, nArray2, nArray3) + nArray[16] + nArray2[16];
        if (n2 > 511 || n2 == 511 && Nat.eq(16, nArray3, P)) {
            n2 += Nat.inc(16, nArray3);
            n2 &= 0x1FF;
        }
        nArray3[16] = n2;
    }

    public static void addOne(int[] nArray, int[] nArray2) {
        int n2 = Nat.inc(16, nArray, nArray2) + nArray[16];
        if (n2 > 511 || n2 == 511 && Nat.eq(16, nArray2, P)) {
            n2 += Nat.inc(16, nArray2);
            n2 &= 0x1FF;
        }
        nArray2[16] = n2;
    }

    public static int[] fromBigInteger(BigInteger bigInteger) {
        int[] nArray = Nat.fromBigInteger(521, bigInteger);
        if (Nat.eq(17, nArray, P)) {
            Nat.zero(17, nArray);
        }
        return nArray;
    }

    public static void half(int[] nArray, int[] nArray2) {
        int n2 = nArray[16];
        int n3 = Nat.shiftDownBit(16, nArray, n2, nArray2);
        nArray2[16] = n2 >>> 1 | n3 >>> 23;
    }

    public static void inv(int[] nArray, int[] nArray2) {
        Mod.checkedModOddInverse(P, nArray, nArray2);
    }

    public static int isZero(int[] nArray) {
        int n2 = 0;
        for (int i2 = 0; i2 < 17; ++i2) {
            n2 |= nArray[i2];
        }
        n2 = n2 >>> 1 | n2 & 1;
        return n2 - 1 >> 31;
    }

    public static void multiply(int[] nArray, int[] nArray2, int[] nArray3) {
        int[] nArray4 = Nat.create(33);
        SecP521R1Field.implMultiply(nArray, nArray2, nArray4);
        SecP521R1Field.reduce(nArray4, nArray3);
    }

    public static void multiply(int[] nArray, int[] nArray2, int[] nArray3, int[] nArray4) {
        SecP521R1Field.implMultiply(nArray, nArray2, nArray4);
        SecP521R1Field.reduce(nArray4, nArray3);
    }

    public static void negate(int[] nArray, int[] nArray2) {
        if (0 != SecP521R1Field.isZero(nArray)) {
            Nat.sub(17, P, P, nArray2);
        } else {
            Nat.sub(17, P, nArray, nArray2);
        }
    }

    public static void random(SecureRandom secureRandom, int[] nArray) {
        byte[] byArray = new byte[68];
        do {
            secureRandom.nextBytes(byArray);
            Pack.littleEndianToInt(byArray, 0, nArray, 0, 17);
            nArray[16] = nArray[16] & 0x1FF;
        } while (0 == Nat.lessThan(17, nArray, P));
    }

    public static void randomMult(SecureRandom secureRandom, int[] nArray) {
        do {
            SecP521R1Field.random(secureRandom, nArray);
        } while (0 != SecP521R1Field.isZero(nArray));
    }

    public static void reduce(int[] nArray, int[] nArray2) {
        int n2 = nArray[32];
        int n3 = Nat.shiftDownBits(16, nArray, 16, 9, n2, nArray2, 0) >>> 23;
        n3 += n2 >>> 9;
        if ((n3 += Nat.addTo(16, nArray, nArray2)) > 511 || n3 == 511 && Nat.eq(16, nArray2, P)) {
            n3 += Nat.inc(16, nArray2);
            n3 &= 0x1FF;
        }
        nArray2[16] = n3;
    }

    public static void reduce23(int[] nArray) {
        int n2 = nArray[16];
        int n3 = Nat.addWordTo(16, n2 >>> 9, nArray) + (n2 & 0x1FF);
        if (n3 > 511 || n3 == 511 && Nat.eq(16, nArray, P)) {
            n3 += Nat.inc(16, nArray);
            n3 &= 0x1FF;
        }
        nArray[16] = n3;
    }

    public static void square(int[] nArray, int[] nArray2) {
        int[] nArray3 = Nat.create(33);
        SecP521R1Field.implSquare(nArray, nArray3);
        SecP521R1Field.reduce(nArray3, nArray2);
    }

    public static void square(int[] nArray, int[] nArray2, int[] nArray3) {
        SecP521R1Field.implSquare(nArray, nArray3);
        SecP521R1Field.reduce(nArray3, nArray2);
    }

    public static void squareN(int[] nArray, int n2, int[] nArray2) {
        int[] nArray3 = Nat.create(33);
        SecP521R1Field.implSquare(nArray, nArray3);
        SecP521R1Field.reduce(nArray3, nArray2);
        while (--n2 > 0) {
            SecP521R1Field.implSquare(nArray2, nArray3);
            SecP521R1Field.reduce(nArray3, nArray2);
        }
    }

    public static void squareN(int[] nArray, int n2, int[] nArray2, int[] nArray3) {
        SecP521R1Field.implSquare(nArray, nArray3);
        SecP521R1Field.reduce(nArray3, nArray2);
        while (--n2 > 0) {
            SecP521R1Field.implSquare(nArray2, nArray3);
            SecP521R1Field.reduce(nArray3, nArray2);
        }
    }

    public static void subtract(int[] nArray, int[] nArray2, int[] nArray3) {
        int n2 = Nat.sub(16, nArray, nArray2, nArray3) + nArray[16] - nArray2[16];
        if (n2 < 0) {
            n2 += Nat.dec(16, nArray3);
            n2 &= 0x1FF;
        }
        nArray3[16] = n2;
    }

    public static void twice(int[] nArray, int[] nArray2) {
        int n2 = nArray[16];
        int n3 = Nat.shiftUpBit(16, nArray, n2 << 23, nArray2) | n2 << 1;
        nArray2[16] = n3 & 0x1FF;
    }

    protected static void implMultiply(int[] nArray, int[] nArray2, int[] nArray3) {
        Nat512.mul(nArray, nArray2, nArray3);
        int n2 = nArray[16];
        int n3 = nArray2[16];
        nArray3[32] = Nat.mul31BothAdd(16, n2, nArray2, n3, nArray, nArray3, 16) + n2 * n3;
    }

    protected static void implSquare(int[] nArray, int[] nArray2) {
        Nat512.square(nArray, nArray2);
        int n2 = nArray[16];
        nArray2[32] = Nat.mulWordAddTo(16, n2 << 1, nArray, 0, nArray2, 16) + n2 * n2;
    }
}

