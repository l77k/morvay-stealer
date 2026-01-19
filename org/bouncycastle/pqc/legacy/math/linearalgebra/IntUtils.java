/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.legacy.math.linearalgebra;

import org.bouncycastle.pqc.legacy.math.linearalgebra.BigEndianConversions;
import org.bouncycastle.pqc.legacy.math.linearalgebra.ByteUtils;

public final class IntUtils {
    private IntUtils() {
    }

    public static boolean equals(int[] nArray, int[] nArray2) {
        if (nArray.length != nArray2.length) {
            return false;
        }
        boolean bl = true;
        for (int i2 = nArray.length - 1; i2 >= 0; --i2) {
            bl &= nArray[i2] == nArray2[i2];
        }
        return bl;
    }

    public static int[] clone(int[] nArray) {
        int[] nArray2 = new int[nArray.length];
        System.arraycopy(nArray, 0, nArray2, 0, nArray.length);
        return nArray2;
    }

    public static void fill(int[] nArray, int n2) {
        for (int i2 = nArray.length - 1; i2 >= 0; --i2) {
            nArray[i2] = n2;
        }
    }

    public static void quicksort(int[] nArray) {
        IntUtils.quicksort(nArray, 0, nArray.length - 1);
    }

    public static void quicksort(int[] nArray, int n2, int n3) {
        if (n3 > n2) {
            int n4 = IntUtils.partition(nArray, n2, n3, n3);
            IntUtils.quicksort(nArray, n2, n4 - 1);
            IntUtils.quicksort(nArray, n4 + 1, n3);
        }
    }

    private static int partition(int[] nArray, int n2, int n3, int n4) {
        int n5;
        int n6 = nArray[n4];
        nArray[n4] = nArray[n3];
        nArray[n3] = n6;
        int n7 = n2;
        for (n5 = n2; n5 < n3; ++n5) {
            if (nArray[n5] > n6) continue;
            int n8 = nArray[n7];
            nArray[n7] = nArray[n5];
            nArray[n5] = n8;
            ++n7;
        }
        n5 = nArray[n7];
        nArray[n7] = nArray[n3];
        nArray[n3] = n5;
        return n7;
    }

    public static int[] subArray(int[] nArray, int n2, int n3) {
        int[] nArray2 = new int[n3 - n2];
        System.arraycopy(nArray, n2, nArray2, 0, n3 - n2);
        return nArray2;
    }

    public static String toString(int[] nArray) {
        String string = "";
        for (int i2 = 0; i2 < nArray.length; ++i2) {
            string = string + nArray[i2] + " ";
        }
        return string;
    }

    public static String toHexString(int[] nArray) {
        return ByteUtils.toHexString(BigEndianConversions.toByteArray(nArray));
    }
}

