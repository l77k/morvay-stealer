/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.legacy.math.linearalgebra;

public final class PolynomialRingGF2 {
    private PolynomialRingGF2() {
    }

    public static int add(int n2, int n3) {
        return n2 ^ n3;
    }

    public static long multiply(int n2, int n3) {
        long l2 = 0L;
        if (n3 != 0) {
            long l3 = (long)n3 & 0xFFFFFFFFL;
            while (n2 != 0) {
                byte by = (byte)(n2 & 1);
                if (by == 1) {
                    l2 ^= l3;
                }
                n2 >>>= 1;
                l3 <<= 1;
            }
        }
        return l2;
    }

    public static int modMultiply(int n2, int n3, int n4) {
        int n5 = 0;
        int n6 = PolynomialRingGF2.remainder(n2, n4);
        int n7 = PolynomialRingGF2.remainder(n3, n4);
        if (n7 != 0) {
            int n8 = 1 << PolynomialRingGF2.degree(n4);
            while (n6 != 0) {
                byte by = (byte)(n6 & 1);
                if (by == 1) {
                    n5 ^= n7;
                }
                n6 >>>= 1;
                if ((n7 <<= 1) < n8) continue;
                n7 ^= n4;
            }
        }
        return n5;
    }

    public static int degree(int n2) {
        int n3 = -1;
        while (n2 != 0) {
            ++n3;
            n2 >>>= 1;
        }
        return n3;
    }

    public static int degree(long l2) {
        int n2 = 0;
        while (l2 != 0L) {
            ++n2;
            l2 >>>= 1;
        }
        return n2 - 1;
    }

    public static int remainder(int n2, int n3) {
        int n4 = n2;
        if (n3 == 0) {
            System.err.println("Error: to be divided by 0");
            return 0;
        }
        while (PolynomialRingGF2.degree(n4) >= PolynomialRingGF2.degree(n3)) {
            n4 ^= n3 << PolynomialRingGF2.degree(n4) - PolynomialRingGF2.degree(n3);
        }
        return n4;
    }

    public static int rest(long l2, int n2) {
        long l3 = l2;
        if (n2 == 0) {
            System.err.println("Error: to be divided by 0");
            return 0;
        }
        long l4 = (long)n2 & 0xFFFFFFFFL;
        while (l3 >>> 32 != 0L) {
            l3 ^= l4 << PolynomialRingGF2.degree(l3) - PolynomialRingGF2.degree(l4);
        }
        int n3 = (int)(l3 & 0xFFFFFFFFFFFFFFFFL);
        while (PolynomialRingGF2.degree(n3) >= PolynomialRingGF2.degree(n2)) {
            n3 ^= n2 << PolynomialRingGF2.degree(n3) - PolynomialRingGF2.degree(n2);
        }
        return n3;
    }

    public static int gcd(int n2, int n3) {
        int n4 = n2;
        int n5 = n3;
        while (n5 != 0) {
            int n6 = PolynomialRingGF2.remainder(n4, n5);
            n4 = n5;
            n5 = n6;
        }
        return n4;
    }

    public static boolean isIrreducible(int n2) {
        if (n2 == 0) {
            return false;
        }
        int n3 = PolynomialRingGF2.degree(n2) >>> 1;
        int n4 = 2;
        for (int i2 = 0; i2 < n3; ++i2) {
            if (PolynomialRingGF2.gcd((n4 = PolynomialRingGF2.modMultiply(n4, n4, n2)) ^ 2, n2) == 1) continue;
            return false;
        }
        return true;
    }

    public static int getIrreduciblePolynomial(int n2) {
        if (n2 < 0) {
            System.err.println("The Degree is negative");
            return 0;
        }
        if (n2 > 31) {
            System.err.println("The Degree is more then 31");
            return 0;
        }
        if (n2 == 0) {
            return 1;
        }
        int n3 = 1 << n2;
        int n4 = 1 << n2 + 1;
        for (int i2 = ++n3; i2 < n4; i2 += 2) {
            if (!PolynomialRingGF2.isIrreducible(i2)) continue;
            return i2;
        }
        return 0;
    }
}

