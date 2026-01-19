/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.util;

import java.math.BigInteger;
import java.util.NoSuchElementException;
import org.bouncycastle.util.Objects;

public final class Arrays {
    private Arrays() {
    }

    public static boolean areAllZeroes(byte[] byArray, int n2, int n3) {
        int n4 = 0;
        for (int i2 = 0; i2 < n3; ++i2) {
            n4 |= byArray[n2 + i2];
        }
        return n4 == 0;
    }

    public static boolean areEqual(boolean[] blArray, boolean[] blArray2) {
        return java.util.Arrays.equals(blArray, blArray2);
    }

    public static boolean areEqual(byte[] byArray, byte[] byArray2) {
        return java.util.Arrays.equals(byArray, byArray2);
    }

    public static boolean areEqual(byte[] byArray, int n2, int n3, byte[] byArray2, int n4, int n5) {
        int n6 = n3 - n2;
        int n7 = n5 - n4;
        if (n6 != n7) {
            return false;
        }
        for (int i2 = 0; i2 < n6; ++i2) {
            if (byArray[n2 + i2] == byArray2[n4 + i2]) continue;
            return false;
        }
        return true;
    }

    public static boolean areEqual(char[] cArray, char[] cArray2) {
        return java.util.Arrays.equals(cArray, cArray2);
    }

    public static boolean areEqual(int[] nArray, int[] nArray2) {
        return java.util.Arrays.equals(nArray, nArray2);
    }

    public static boolean areEqual(long[] lArray, long[] lArray2) {
        return java.util.Arrays.equals(lArray, lArray2);
    }

    public static boolean areEqual(Object[] objectArray, Object[] objectArray2) {
        return java.util.Arrays.equals(objectArray, objectArray2);
    }

    public static boolean areEqual(short[] sArray, short[] sArray2) {
        return java.util.Arrays.equals(sArray, sArray2);
    }

    public static boolean constantTimeAreEqual(byte[] byArray, byte[] byArray2) {
        int n2;
        if (byArray == null || byArray2 == null) {
            return false;
        }
        if (byArray == byArray2) {
            return true;
        }
        int n3 = byArray.length < byArray2.length ? byArray.length : byArray2.length;
        int n4 = byArray.length ^ byArray2.length;
        for (n2 = 0; n2 != n3; ++n2) {
            n4 |= byArray[n2] ^ byArray2[n2];
        }
        for (n2 = n3; n2 < byArray2.length; ++n2) {
            n4 |= byArray2[n2] ^ ~byArray2[n2];
        }
        return n4 == 0;
    }

    public static boolean constantTimeAreEqual(int n2, byte[] byArray, int n3, byte[] byArray2, int n4) {
        if (null == byArray) {
            throw new NullPointerException("'a' cannot be null");
        }
        if (null == byArray2) {
            throw new NullPointerException("'b' cannot be null");
        }
        if (n2 < 0) {
            throw new IllegalArgumentException("'len' cannot be negative");
        }
        if (n3 > byArray.length - n2) {
            throw new IndexOutOfBoundsException("'aOff' value invalid for specified length");
        }
        if (n4 > byArray2.length - n2) {
            throw new IndexOutOfBoundsException("'bOff' value invalid for specified length");
        }
        int n5 = 0;
        for (int i2 = 0; i2 < n2; ++i2) {
            n5 |= byArray[n3 + i2] ^ byArray2[n4 + i2];
        }
        return 0 == n5;
    }

    public static boolean constantTimeAreEqual(char[] cArray, char[] cArray2) {
        int n2;
        if (cArray == null || cArray2 == null) {
            return false;
        }
        if (cArray == cArray2) {
            return true;
        }
        int n3 = Math.min(cArray.length, cArray2.length);
        int n4 = cArray.length ^ cArray2.length;
        for (n2 = 0; n2 != n3; ++n2) {
            n4 |= cArray[n2] ^ cArray2[n2];
        }
        for (n2 = n3; n2 < cArray2.length; ++n2) {
            n4 |= (byte)cArray2[n2] ^ (byte)(~cArray2[n2]);
        }
        return n4 == 0;
    }

    public static int compareUnsigned(byte[] byArray, byte[] byArray2) {
        if (byArray == byArray2) {
            return 0;
        }
        if (byArray == null) {
            return -1;
        }
        if (byArray2 == null) {
            return 1;
        }
        int n2 = Math.min(byArray.length, byArray2.length);
        for (int i2 = 0; i2 < n2; ++i2) {
            int n3 = byArray[i2] & 0xFF;
            int n4 = byArray2[i2] & 0xFF;
            if (n3 < n4) {
                return -1;
            }
            if (n3 <= n4) continue;
            return 1;
        }
        if (byArray.length < byArray2.length) {
            return -1;
        }
        if (byArray.length > byArray2.length) {
            return 1;
        }
        return 0;
    }

    public static boolean contains(boolean[] blArray, boolean bl) {
        for (int i2 = 0; i2 < blArray.length; ++i2) {
            if (blArray[i2] != bl) continue;
            return true;
        }
        return false;
    }

    public static boolean contains(byte[] byArray, byte by) {
        for (int i2 = 0; i2 < byArray.length; ++i2) {
            if (byArray[i2] != by) continue;
            return true;
        }
        return false;
    }

    public static boolean contains(char[] cArray, char c2) {
        for (int i2 = 0; i2 < cArray.length; ++i2) {
            if (cArray[i2] != c2) continue;
            return true;
        }
        return false;
    }

    public static boolean contains(int[] nArray, int n2) {
        for (int i2 = 0; i2 < nArray.length; ++i2) {
            if (nArray[i2] != n2) continue;
            return true;
        }
        return false;
    }

    public static boolean contains(long[] lArray, long l2) {
        for (int i2 = 0; i2 < lArray.length; ++i2) {
            if (lArray[i2] != l2) continue;
            return true;
        }
        return false;
    }

    public static boolean contains(short[] sArray, short s2) {
        for (int i2 = 0; i2 < sArray.length; ++i2) {
            if (sArray[i2] != s2) continue;
            return true;
        }
        return false;
    }

    public static void fill(boolean[] blArray, boolean bl) {
        java.util.Arrays.fill(blArray, bl);
    }

    public static void fill(boolean[] blArray, int n2, int n3, boolean bl) {
        java.util.Arrays.fill(blArray, n2, n3, bl);
    }

    public static void fill(byte[] byArray, byte by) {
        java.util.Arrays.fill(byArray, by);
    }

    public static void fill(byte[] byArray, int n2, int n3, byte by) {
        java.util.Arrays.fill(byArray, n2, n3, by);
    }

    public static void fill(char[] cArray, char c2) {
        java.util.Arrays.fill(cArray, c2);
    }

    public static void fill(char[] cArray, int n2, int n3, char c2) {
        java.util.Arrays.fill(cArray, n2, n3, c2);
    }

    public static void fill(int[] nArray, int n2) {
        java.util.Arrays.fill(nArray, n2);
    }

    public static void fill(int[] nArray, int n2, int n3, int n4) {
        java.util.Arrays.fill(nArray, n2, n3, n4);
    }

    public static void fill(long[] lArray, long l2) {
        java.util.Arrays.fill(lArray, l2);
    }

    public static void fill(long[] lArray, int n2, int n3, long l2) {
        java.util.Arrays.fill(lArray, n2, n3, l2);
    }

    public static void fill(Object[] objectArray, Object object) {
        java.util.Arrays.fill(objectArray, object);
    }

    public static void fill(Object[] objectArray, int n2, int n3, Object object) {
        java.util.Arrays.fill(objectArray, n2, n3, object);
    }

    public static void fill(short[] sArray, short s2) {
        java.util.Arrays.fill(sArray, s2);
    }

    public static void fill(short[] sArray, int n2, int n3, short s2) {
        java.util.Arrays.fill(sArray, n2, n3, s2);
    }

    public static int hashCode(byte[] byArray) {
        if (byArray == null) {
            return 0;
        }
        int n2 = byArray.length;
        int n3 = n2 + 1;
        while (--n2 >= 0) {
            n3 *= 257;
            n3 ^= byArray[n2];
        }
        return n3;
    }

    public static int hashCode(byte[] byArray, int n2, int n3) {
        if (byArray == null) {
            return 0;
        }
        int n4 = n3;
        int n5 = n4 + 1;
        while (--n4 >= 0) {
            n5 *= 257;
            n5 ^= byArray[n2 + n4];
        }
        return n5;
    }

    public static int hashCode(char[] cArray) {
        if (cArray == null) {
            return 0;
        }
        int n2 = cArray.length;
        int n3 = n2 + 1;
        while (--n2 >= 0) {
            n3 *= 257;
            n3 ^= cArray[n2];
        }
        return n3;
    }

    public static int hashCode(int[][] nArray) {
        int n2 = 0;
        for (int i2 = 0; i2 != nArray.length; ++i2) {
            n2 = n2 * 257 + Arrays.hashCode(nArray[i2]);
        }
        return n2;
    }

    public static int hashCode(int[] nArray) {
        if (nArray == null) {
            return 0;
        }
        int n2 = nArray.length;
        int n3 = n2 + 1;
        while (--n2 >= 0) {
            n3 *= 257;
            n3 ^= nArray[n2];
        }
        return n3;
    }

    public static int hashCode(int[] nArray, int n2, int n3) {
        if (nArray == null) {
            return 0;
        }
        int n4 = n3;
        int n5 = n4 + 1;
        while (--n4 >= 0) {
            n5 *= 257;
            n5 ^= nArray[n2 + n4];
        }
        return n5;
    }

    public static int hashCode(long[] lArray) {
        if (lArray == null) {
            return 0;
        }
        int n2 = lArray.length;
        int n3 = n2 + 1;
        while (--n2 >= 0) {
            long l2 = lArray[n2];
            n3 *= 257;
            n3 ^= (int)l2;
            n3 *= 257;
            n3 ^= (int)(l2 >>> 32);
        }
        return n3;
    }

    public static int hashCode(long[] lArray, int n2, int n3) {
        if (lArray == null) {
            return 0;
        }
        int n4 = n3;
        int n5 = n4 + 1;
        while (--n4 >= 0) {
            long l2 = lArray[n2 + n4];
            n5 *= 257;
            n5 ^= (int)l2;
            n5 *= 257;
            n5 ^= (int)(l2 >>> 32);
        }
        return n5;
    }

    public static int hashCode(short[][][] sArray) {
        int n2 = 0;
        for (int i2 = 0; i2 != sArray.length; ++i2) {
            n2 = n2 * 257 + Arrays.hashCode(sArray[i2]);
        }
        return n2;
    }

    public static int hashCode(short[][] sArray) {
        int n2 = 0;
        for (int i2 = 0; i2 != sArray.length; ++i2) {
            n2 = n2 * 257 + Arrays.hashCode(sArray[i2]);
        }
        return n2;
    }

    public static int hashCode(short[] sArray) {
        if (sArray == null) {
            return 0;
        }
        int n2 = sArray.length;
        int n3 = n2 + 1;
        while (--n2 >= 0) {
            n3 *= 257;
            n3 ^= sArray[n2] & 0xFF;
        }
        return n3;
    }

    public static int hashCode(Object[] objectArray) {
        if (objectArray == null) {
            return 0;
        }
        int n2 = objectArray.length;
        int n3 = n2 + 1;
        while (--n2 >= 0) {
            n3 *= 257;
            n3 ^= Objects.hashCode(objectArray[n2]);
        }
        return n3;
    }

    public static boolean[] clone(boolean[] blArray) {
        return null == blArray ? null : (boolean[])blArray.clone();
    }

    public static byte[] clone(byte[] byArray) {
        return null == byArray ? null : (byte[])byArray.clone();
    }

    public static char[] clone(char[] cArray) {
        return null == cArray ? null : (char[])cArray.clone();
    }

    public static int[] clone(int[] nArray) {
        return null == nArray ? null : (int[])nArray.clone();
    }

    public static long[] clone(long[] lArray) {
        return null == lArray ? null : (long[])lArray.clone();
    }

    public static short[] clone(short[] sArray) {
        return null == sArray ? null : (short[])sArray.clone();
    }

    public static BigInteger[] clone(BigInteger[] bigIntegerArray) {
        return null == bigIntegerArray ? null : (BigInteger[])bigIntegerArray.clone();
    }

    public static byte[] clone(byte[] byArray, byte[] byArray2) {
        if (byArray == null) {
            return null;
        }
        if (byArray2 == null || byArray2.length != byArray.length) {
            return Arrays.clone(byArray);
        }
        System.arraycopy(byArray, 0, byArray2, 0, byArray2.length);
        return byArray2;
    }

    public static long[] clone(long[] lArray, long[] lArray2) {
        if (lArray == null) {
            return null;
        }
        if (lArray2 == null || lArray2.length != lArray.length) {
            return Arrays.clone(lArray);
        }
        System.arraycopy(lArray, 0, lArray2, 0, lArray2.length);
        return lArray2;
    }

    public static byte[][] clone(byte[][] byArray) {
        if (byArray == null) {
            return null;
        }
        byte[][] byArrayArray = new byte[byArray.length][];
        for (int i2 = 0; i2 != byArrayArray.length; ++i2) {
            byArrayArray[i2] = Arrays.clone(byArray[i2]);
        }
        return byArrayArray;
    }

    public static byte[][][] clone(byte[][][] byArray) {
        if (byArray == null) {
            return null;
        }
        byte[][][] byArrayArray = new byte[byArray.length][][];
        for (int i2 = 0; i2 != byArrayArray.length; ++i2) {
            byArrayArray[i2] = Arrays.clone(byArray[i2]);
        }
        return byArrayArray;
    }

    public static boolean[] copyOf(boolean[] blArray, int n2) {
        boolean[] blArray2 = new boolean[n2];
        System.arraycopy(blArray, 0, blArray2, 0, Math.min(blArray.length, n2));
        return blArray2;
    }

    public static byte[] copyOf(byte[] byArray, int n2) {
        byte[] byArray2 = new byte[n2];
        System.arraycopy(byArray, 0, byArray2, 0, Math.min(byArray.length, n2));
        return byArray2;
    }

    public static char[] copyOf(char[] cArray, int n2) {
        char[] cArray2 = new char[n2];
        System.arraycopy(cArray, 0, cArray2, 0, Math.min(cArray.length, n2));
        return cArray2;
    }

    public static int[] copyOf(int[] nArray, int n2) {
        int[] nArray2 = new int[n2];
        System.arraycopy(nArray, 0, nArray2, 0, Math.min(nArray.length, n2));
        return nArray2;
    }

    public static long[] copyOf(long[] lArray, int n2) {
        long[] lArray2 = new long[n2];
        System.arraycopy(lArray, 0, lArray2, 0, Math.min(lArray.length, n2));
        return lArray2;
    }

    public static short[] copyOf(short[] sArray, int n2) {
        short[] sArray2 = new short[n2];
        System.arraycopy(sArray, 0, sArray2, 0, Math.min(sArray.length, n2));
        return sArray2;
    }

    public static BigInteger[] copyOf(BigInteger[] bigIntegerArray, int n2) {
        BigInteger[] bigIntegerArray2 = new BigInteger[n2];
        System.arraycopy(bigIntegerArray, 0, bigIntegerArray2, 0, Math.min(bigIntegerArray.length, n2));
        return bigIntegerArray2;
    }

    public static boolean[] copyOfRange(boolean[] blArray, int n2, int n3) {
        int n4 = Arrays.getLength(n2, n3);
        boolean[] blArray2 = new boolean[n4];
        System.arraycopy(blArray, n2, blArray2, 0, Math.min(blArray.length - n2, n4));
        return blArray2;
    }

    public static byte[] copyOfRange(byte[] byArray, int n2, int n3) {
        int n4 = Arrays.getLength(n2, n3);
        byte[] byArray2 = new byte[n4];
        System.arraycopy(byArray, n2, byArray2, 0, Math.min(byArray.length - n2, n4));
        return byArray2;
    }

    public static char[] copyOfRange(char[] cArray, int n2, int n3) {
        int n4 = Arrays.getLength(n2, n3);
        char[] cArray2 = new char[n4];
        System.arraycopy(cArray, n2, cArray2, 0, Math.min(cArray.length - n2, n4));
        return cArray2;
    }

    public static int[] copyOfRange(int[] nArray, int n2, int n3) {
        int n4 = Arrays.getLength(n2, n3);
        int[] nArray2 = new int[n4];
        System.arraycopy(nArray, n2, nArray2, 0, Math.min(nArray.length - n2, n4));
        return nArray2;
    }

    public static long[] copyOfRange(long[] lArray, int n2, int n3) {
        int n4 = Arrays.getLength(n2, n3);
        long[] lArray2 = new long[n4];
        System.arraycopy(lArray, n2, lArray2, 0, Math.min(lArray.length - n2, n4));
        return lArray2;
    }

    public static short[] copyOfRange(short[] sArray, int n2, int n3) {
        int n4 = Arrays.getLength(n2, n3);
        short[] sArray2 = new short[n4];
        System.arraycopy(sArray, n2, sArray2, 0, Math.min(sArray.length - n2, n4));
        return sArray2;
    }

    public static BigInteger[] copyOfRange(BigInteger[] bigIntegerArray, int n2, int n3) {
        int n4 = Arrays.getLength(n2, n3);
        BigInteger[] bigIntegerArray2 = new BigInteger[n4];
        System.arraycopy(bigIntegerArray, n2, bigIntegerArray2, 0, Math.min(bigIntegerArray.length - n2, n4));
        return bigIntegerArray2;
    }

    private static int getLength(int n2, int n3) {
        int n4 = n3 - n2;
        if (n4 < 0) {
            throw new IllegalArgumentException(n2 + " > " + n3);
        }
        return n4;
    }

    public static byte[] append(byte[] byArray, byte by) {
        if (byArray == null) {
            return new byte[]{by};
        }
        int n2 = byArray.length;
        byte[] byArray2 = new byte[n2 + 1];
        System.arraycopy(byArray, 0, byArray2, 0, n2);
        byArray2[n2] = by;
        return byArray2;
    }

    public static short[] append(short[] sArray, short s2) {
        if (sArray == null) {
            return new short[]{s2};
        }
        int n2 = sArray.length;
        short[] sArray2 = new short[n2 + 1];
        System.arraycopy(sArray, 0, sArray2, 0, n2);
        sArray2[n2] = s2;
        return sArray2;
    }

    public static int[] append(int[] nArray, int n2) {
        if (nArray == null) {
            return new int[]{n2};
        }
        int n3 = nArray.length;
        int[] nArray2 = new int[n3 + 1];
        System.arraycopy(nArray, 0, nArray2, 0, n3);
        nArray2[n3] = n2;
        return nArray2;
    }

    public static String[] append(String[] stringArray, String string) {
        if (stringArray == null) {
            return new String[]{string};
        }
        int n2 = stringArray.length;
        String[] stringArray2 = new String[n2 + 1];
        System.arraycopy(stringArray, 0, stringArray2, 0, n2);
        stringArray2[n2] = string;
        return stringArray2;
    }

    public static byte[] concatenate(byte[] byArray, byte[] byArray2) {
        if (null == byArray) {
            return Arrays.clone(byArray2);
        }
        if (null == byArray2) {
            return Arrays.clone(byArray);
        }
        byte[] byArray3 = new byte[byArray.length + byArray2.length];
        System.arraycopy(byArray, 0, byArray3, 0, byArray.length);
        System.arraycopy(byArray2, 0, byArray3, byArray.length, byArray2.length);
        return byArray3;
    }

    public static short[] concatenate(short[] sArray, short[] sArray2) {
        if (null == sArray) {
            return Arrays.clone(sArray2);
        }
        if (null == sArray2) {
            return Arrays.clone(sArray);
        }
        short[] sArray3 = new short[sArray.length + sArray2.length];
        System.arraycopy(sArray, 0, sArray3, 0, sArray.length);
        System.arraycopy(sArray2, 0, sArray3, sArray.length, sArray2.length);
        return sArray3;
    }

    public static byte[] concatenate(byte[] byArray, byte[] byArray2, byte[] byArray3) {
        if (null == byArray) {
            return Arrays.concatenate(byArray2, byArray3);
        }
        if (null == byArray2) {
            return Arrays.concatenate(byArray, byArray3);
        }
        if (null == byArray3) {
            return Arrays.concatenate(byArray, byArray2);
        }
        byte[] byArray4 = new byte[byArray.length + byArray2.length + byArray3.length];
        int n2 = 0;
        System.arraycopy(byArray, 0, byArray4, n2, byArray.length);
        System.arraycopy(byArray2, 0, byArray4, n2 += byArray.length, byArray2.length);
        System.arraycopy(byArray3, 0, byArray4, n2 += byArray2.length, byArray3.length);
        return byArray4;
    }

    public static byte[] concatenate(byte[] byArray, byte[] byArray2, byte[] byArray3, byte[] byArray4) {
        if (null == byArray) {
            return Arrays.concatenate(byArray2, byArray3, byArray4);
        }
        if (null == byArray2) {
            return Arrays.concatenate(byArray, byArray3, byArray4);
        }
        if (null == byArray3) {
            return Arrays.concatenate(byArray, byArray2, byArray4);
        }
        if (null == byArray4) {
            return Arrays.concatenate(byArray, byArray2, byArray3);
        }
        byte[] byArray5 = new byte[byArray.length + byArray2.length + byArray3.length + byArray4.length];
        int n2 = 0;
        System.arraycopy(byArray, 0, byArray5, n2, byArray.length);
        System.arraycopy(byArray2, 0, byArray5, n2 += byArray.length, byArray2.length);
        System.arraycopy(byArray3, 0, byArray5, n2 += byArray2.length, byArray3.length);
        System.arraycopy(byArray4, 0, byArray5, n2 += byArray3.length, byArray4.length);
        return byArray5;
    }

    public static byte[] concatenate(byte[][] byArray) {
        int n2 = 0;
        for (int i2 = 0; i2 != byArray.length; ++i2) {
            n2 += byArray[i2].length;
        }
        byte[] byArray2 = new byte[n2];
        int n3 = 0;
        for (int i3 = 0; i3 != byArray.length; ++i3) {
            System.arraycopy(byArray[i3], 0, byArray2, n3, byArray[i3].length);
            n3 += byArray[i3].length;
        }
        return byArray2;
    }

    public static int[] concatenate(int[] nArray, int[] nArray2) {
        if (null == nArray) {
            return Arrays.clone(nArray2);
        }
        if (null == nArray2) {
            return Arrays.clone(nArray);
        }
        int[] nArray3 = new int[nArray.length + nArray2.length];
        System.arraycopy(nArray, 0, nArray3, 0, nArray.length);
        System.arraycopy(nArray2, 0, nArray3, nArray.length, nArray2.length);
        return nArray3;
    }

    public static byte[] prepend(byte[] byArray, byte by) {
        if (byArray == null) {
            return new byte[]{by};
        }
        int n2 = byArray.length;
        byte[] byArray2 = new byte[n2 + 1];
        System.arraycopy(byArray, 0, byArray2, 1, n2);
        byArray2[0] = by;
        return byArray2;
    }

    public static short[] prepend(short[] sArray, short s2) {
        if (sArray == null) {
            return new short[]{s2};
        }
        int n2 = sArray.length;
        short[] sArray2 = new short[n2 + 1];
        System.arraycopy(sArray, 0, sArray2, 1, n2);
        sArray2[0] = s2;
        return sArray2;
    }

    public static int[] prepend(int[] nArray, int n2) {
        if (nArray == null) {
            return new int[]{n2};
        }
        int n3 = nArray.length;
        int[] nArray2 = new int[n3 + 1];
        System.arraycopy(nArray, 0, nArray2, 1, n3);
        nArray2[0] = n2;
        return nArray2;
    }

    public static byte[] reverse(byte[] byArray) {
        if (byArray == null) {
            return null;
        }
        int n2 = 0;
        int n3 = byArray.length;
        byte[] byArray2 = new byte[n3];
        while (--n3 >= 0) {
            byArray2[n3] = byArray[n2++];
        }
        return byArray2;
    }

    public static int[] reverse(int[] nArray) {
        if (nArray == null) {
            return null;
        }
        int n2 = 0;
        int n3 = nArray.length;
        int[] nArray2 = new int[n3];
        while (--n3 >= 0) {
            nArray2[n3] = nArray[n2++];
        }
        return nArray2;
    }

    public static void reverse(byte[] byArray, byte[] byArray2) {
        int n2 = byArray.length - 1;
        for (int i2 = 0; i2 <= n2; ++i2) {
            byArray2[i2] = byArray[n2 - i2];
        }
    }

    public static byte[] reverseInPlace(byte[] byArray) {
        if (null == byArray) {
            return null;
        }
        int n2 = 0;
        int n3 = byArray.length - 1;
        while (n2 < n3) {
            byte by = byArray[n2];
            byte by2 = byArray[n3];
            byArray[n2++] = by2;
            byArray[n3--] = by;
        }
        return byArray;
    }

    public static void reverseInPlace(byte[] byArray, int n2, int n3) {
        int n4 = n2;
        int n5 = n2 + n3 - 1;
        while (n4 < n5) {
            byte by = byArray[n4];
            byte by2 = byArray[n5];
            byArray[n4++] = by2;
            byArray[n5--] = by;
        }
    }

    public static short[] reverseInPlace(short[] sArray) {
        if (null == sArray) {
            return null;
        }
        int n2 = 0;
        int n3 = sArray.length - 1;
        while (n2 < n3) {
            short s2 = sArray[n2];
            short s3 = sArray[n3];
            sArray[n2++] = s3;
            sArray[n3--] = s2;
        }
        return sArray;
    }

    public static int[] reverseInPlace(int[] nArray) {
        if (null == nArray) {
            return null;
        }
        int n2 = 0;
        int n3 = nArray.length - 1;
        while (n2 < n3) {
            int n4 = nArray[n2];
            int n5 = nArray[n3];
            nArray[n2++] = n5;
            nArray[n3--] = n4;
        }
        return nArray;
    }

    public static void clear(byte[] byArray) {
        if (null != byArray) {
            java.util.Arrays.fill(byArray, (byte)0);
        }
    }

    public static void clear(int[] nArray) {
        if (null != nArray) {
            java.util.Arrays.fill(nArray, 0);
        }
    }

    public static boolean isNullOrContainsNull(Object[] objectArray) {
        if (null == objectArray) {
            return true;
        }
        int n2 = objectArray.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            if (null != objectArray[i2]) continue;
            return true;
        }
        return false;
    }

    public static boolean isNullOrEmpty(byte[] byArray) {
        return null == byArray || byArray.length < 1;
    }

    public static boolean isNullOrEmpty(int[] nArray) {
        return null == nArray || nArray.length < 1;
    }

    public static boolean isNullOrEmpty(Object[] objectArray) {
        return null == objectArray || objectArray.length < 1;
    }

    public static class Iterator<T>
    implements java.util.Iterator<T> {
        private final T[] dataArray;
        private int position = 0;

        public Iterator(T[] TArray) {
            this.dataArray = TArray;
        }

        @Override
        public boolean hasNext() {
            return this.position < this.dataArray.length;
        }

        @Override
        public T next() {
            if (this.position == this.dataArray.length) {
                throw new NoSuchElementException("Out of elements: " + this.position);
            }
            return this.dataArray[this.position++];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Cannot remove element from an Array.");
        }
    }
}

