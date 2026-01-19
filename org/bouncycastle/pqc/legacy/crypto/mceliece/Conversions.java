/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.legacy.crypto.mceliece;

import java.math.BigInteger;
import org.bouncycastle.pqc.legacy.math.linearalgebra.BigIntUtils;
import org.bouncycastle.pqc.legacy.math.linearalgebra.GF2Vector;
import org.bouncycastle.pqc.legacy.math.linearalgebra.IntegerFunctions;

final class Conversions {
    private static final BigInteger ZERO = BigInteger.valueOf(0L);
    private static final BigInteger ONE = BigInteger.valueOf(1L);

    private Conversions() {
    }

    public static GF2Vector encode(int n2, int n3, byte[] byArray) {
        if (n2 < n3) {
            throw new IllegalArgumentException("n < t");
        }
        BigInteger bigInteger = new BigInteger(1, byArray);
        BigInteger bigInteger2 = IntegerFunctions.binomial(n2, n3);
        if (bigInteger.compareTo(bigInteger2) >= 0) {
            throw new IllegalArgumentException("Encoded number too large.");
        }
        GF2Vector gF2Vector = new GF2Vector(n2);
        int n4 = n2;
        int n5 = n3;
        for (int i2 = 0; i2 < n2; ++i2) {
            bigInteger2 = bigInteger2.multiply(BigInteger.valueOf(n4 - n5)).divide(BigInteger.valueOf(n4));
            --n4;
            if (bigInteger2.compareTo(bigInteger) > 0) continue;
            gF2Vector.setBit(i2);
            bigInteger = bigInteger.subtract(bigInteger2);
            bigInteger2 = n4 == --n5 ? ONE : bigInteger2.multiply(BigInteger.valueOf(n5 + 1)).divide(BigInteger.valueOf(n4 - n5));
        }
        return gF2Vector;
    }

    public static byte[] decode(int n2, int n3, GF2Vector gF2Vector) {
        if (gF2Vector.getLength() != n2 || gF2Vector.getHammingWeight() != n3) {
            throw new IllegalArgumentException("vector has wrong length or hamming weight");
        }
        int[] nArray = gF2Vector.getVecArray();
        BigInteger bigInteger = IntegerFunctions.binomial(n2, n3);
        BigInteger bigInteger2 = ZERO;
        int n4 = n2;
        int n5 = n3;
        for (int i2 = 0; i2 < n2; ++i2) {
            bigInteger = bigInteger.multiply(BigInteger.valueOf(n4 - n5)).divide(BigInteger.valueOf(n4));
            --n4;
            int n6 = i2 >> 5;
            int n7 = nArray[n6] & 1 << (i2 & 0x1F);
            if (n7 == 0) continue;
            bigInteger2 = bigInteger2.add(bigInteger);
            bigInteger = n4 == --n5 ? ONE : bigInteger.multiply(BigInteger.valueOf(n5 + 1)).divide(BigInteger.valueOf(n4 - n5));
        }
        return BigIntUtils.toMinimalByteArray(bigInteger2);
    }

    public static byte[] signConversion(int n2, int n3, byte[] byArray) {
        int n4;
        int n5;
        byte[] byArray2;
        if (n2 < n3) {
            throw new IllegalArgumentException("n < t");
        }
        BigInteger bigInteger = IntegerFunctions.binomial(n2, n3);
        int n6 = bigInteger.bitLength() - 1;
        int n7 = n6 >> 3;
        int n8 = n6 & 7;
        if (n8 == 0) {
            --n7;
            n8 = 8;
        }
        int n9 = n2 >> 3;
        int n10 = n2 & 7;
        if (n10 == 0) {
            --n9;
            n10 = 8;
        }
        if (byArray.length < (byArray2 = new byte[n9 + 1]).length) {
            System.arraycopy(byArray, 0, byArray2, 0, byArray.length);
            for (n5 = byArray.length; n5 < byArray2.length; ++n5) {
                byArray2[n5] = 0;
            }
        } else {
            System.arraycopy(byArray, 0, byArray2, 0, n9);
            n5 = (1 << n10) - 1;
            byArray2[n9] = (byte)(n5 & byArray[n9]);
        }
        BigInteger bigInteger2 = ZERO;
        int n11 = n2;
        int n12 = n3;
        for (int i2 = 0; i2 < n2; ++i2) {
            bigInteger = bigInteger.multiply(new BigInteger(Integer.toString(n11 - n12))).divide(new BigInteger(Integer.toString(n11)));
            --n11;
            int n13 = i2 >>> 3;
            n4 = i2 & 7;
            byte by = (byte)((n4 = 1 << n4) & byArray2[n13]);
            if (by == 0) continue;
            bigInteger2 = bigInteger2.add(bigInteger);
            bigInteger = n11 == --n12 ? ONE : bigInteger.multiply(new BigInteger(Integer.toString(n12 + 1))).divide(new BigInteger(Integer.toString(n11 - n12)));
        }
        byte[] byArray3 = new byte[n7 + 1];
        byte[] byArray4 = bigInteger2.toByteArray();
        if (byArray4.length < byArray3.length) {
            System.arraycopy(byArray4, 0, byArray3, 0, byArray4.length);
            for (n4 = byArray4.length; n4 < byArray3.length; ++n4) {
                byArray3[n4] = 0;
            }
        } else {
            System.arraycopy(byArray4, 0, byArray3, 0, n7);
            byArray3[n7] = (byte)((1 << n8) - 1 & byArray4[n7]);
        }
        return byArray3;
    }
}

