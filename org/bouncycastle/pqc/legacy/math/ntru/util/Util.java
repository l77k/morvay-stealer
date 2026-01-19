/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.legacy.math.ntru.util;

import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import org.bouncycastle.pqc.legacy.math.ntru.euclid.IntEuclidean;
import org.bouncycastle.pqc.legacy.math.ntru.polynomial.DenseTernaryPolynomial;
import org.bouncycastle.pqc.legacy.math.ntru.polynomial.SparseTernaryPolynomial;
import org.bouncycastle.pqc.legacy.math.ntru.polynomial.TernaryPolynomial;
import org.bouncycastle.util.Integers;

public class Util {
    private static volatile boolean IS_64_BITNESS_KNOWN;
    private static volatile boolean IS_64_BIT_JVM;

    public static int invert(int n2, int n3) {
        if ((n2 %= n3) < 0) {
            n2 += n3;
        }
        return IntEuclidean.calculate((int)n2, (int)n3).x;
    }

    public static int pow(int n2, int n3, int n4) {
        int n5 = 1;
        for (int i2 = 0; i2 < n3; ++i2) {
            n5 = n5 * n2 % n4;
        }
        return n5;
    }

    public static long pow(long l2, int n2, long l3) {
        long l4 = 1L;
        for (int i2 = 0; i2 < n2; ++i2) {
            l4 = l4 * l2 % l3;
        }
        return l4;
    }

    public static TernaryPolynomial generateRandomTernary(int n2, int n3, int n4, boolean bl, SecureRandom secureRandom) {
        if (bl) {
            return SparseTernaryPolynomial.generateRandom(n2, n3, n4, secureRandom);
        }
        return DenseTernaryPolynomial.generateRandom(n2, n3, n4, secureRandom);
    }

    public static int[] generateRandomTernary(int n2, int n3, int n4, SecureRandom secureRandom) {
        int n5;
        Integer n6 = Integers.valueOf(1);
        Integer n7 = Integers.valueOf(-1);
        Integer n8 = Integers.valueOf(0);
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        for (n5 = 0; n5 < n3; ++n5) {
            arrayList.add(n6);
        }
        for (n5 = 0; n5 < n4; ++n5) {
            arrayList.add(n7);
        }
        while (arrayList.size() < n2) {
            arrayList.add(n8);
        }
        Collections.shuffle(arrayList, secureRandom);
        int[] nArray = new int[n2];
        for (int i2 = 0; i2 < n2; ++i2) {
            nArray[i2] = (Integer)arrayList.get(i2);
        }
        return nArray;
    }

    public static boolean is64BitJVM() {
        if (!IS_64_BITNESS_KNOWN) {
            String string = System.getProperty("os.arch");
            String string2 = System.getProperty("sun.arch.data.model");
            IS_64_BIT_JVM = "amd64".equals(string) || "x86_64".equals(string) || "ppc64".equals(string) || "64".equals(string2);
            IS_64_BITNESS_KNOWN = true;
        }
        return IS_64_BIT_JVM;
    }

    public static byte[] readFullLength(InputStream inputStream2, int n2) throws IOException {
        byte[] byArray = new byte[n2];
        if (inputStream2.read(byArray) != byArray.length) {
            throw new IOException("Not enough bytes to read.");
        }
        return byArray;
    }
}

