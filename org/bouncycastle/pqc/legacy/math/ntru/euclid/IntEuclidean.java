/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.legacy.math.ntru.euclid;

public class IntEuclidean {
    public int x;
    public int y;
    public int gcd;

    private IntEuclidean() {
    }

    public static IntEuclidean calculate(int n2, int n3) {
        int n4 = 0;
        int n5 = 1;
        int n6 = 1;
        int n7 = 0;
        while (n3 != 0) {
            int n8 = n2 / n3;
            int n9 = n2;
            n2 = n3;
            n3 = n9 % n3;
            n9 = n4;
            n4 = n5 - n8 * n4;
            n5 = n9;
            n9 = n6;
            n6 = n7 - n8 * n6;
            n7 = n9;
        }
        IntEuclidean intEuclidean = new IntEuclidean();
        intEuclidean.x = n5;
        intEuclidean.y = n7;
        intEuclidean.gcd = n2;
        return intEuclidean;
    }
}

