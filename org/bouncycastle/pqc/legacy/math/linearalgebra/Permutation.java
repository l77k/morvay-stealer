/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.legacy.math.linearalgebra;

import java.security.SecureRandom;
import org.bouncycastle.pqc.legacy.math.linearalgebra.IntUtils;
import org.bouncycastle.pqc.legacy.math.linearalgebra.IntegerFunctions;
import org.bouncycastle.pqc.legacy.math.linearalgebra.LittleEndianConversions;
import org.bouncycastle.pqc.legacy.math.linearalgebra.RandUtils;
import org.bouncycastle.util.Arrays;

public class Permutation {
    private int[] perm;

    public Permutation(int n2) {
        if (n2 <= 0) {
            throw new IllegalArgumentException("invalid length");
        }
        this.perm = new int[n2];
        for (int i2 = n2 - 1; i2 >= 0; --i2) {
            this.perm[i2] = i2;
        }
    }

    public Permutation(int[] nArray) {
        if (!this.isPermutation(nArray)) {
            throw new IllegalArgumentException("array is not a permutation vector");
        }
        this.perm = IntUtils.clone(nArray);
    }

    public Permutation(byte[] byArray) {
        int n2;
        if (byArray.length <= 4) {
            throw new IllegalArgumentException("invalid encoding");
        }
        int n3 = LittleEndianConversions.OS2IP(byArray, 0);
        if (byArray.length != 4 + n3 * (n2 = IntegerFunctions.ceilLog256(n3 - 1))) {
            throw new IllegalArgumentException("invalid encoding");
        }
        this.perm = new int[n3];
        for (int i2 = 0; i2 < n3; ++i2) {
            this.perm[i2] = LittleEndianConversions.OS2IP(byArray, 4 + i2 * n2, n2);
        }
        if (!this.isPermutation(this.perm)) {
            throw new IllegalArgumentException("invalid encoding");
        }
    }

    public Permutation(int n2, SecureRandom secureRandom) {
        int n3;
        if (n2 <= 0) {
            throw new IllegalArgumentException("invalid length");
        }
        this.perm = new int[n2];
        int[] nArray = new int[n2];
        for (n3 = 0; n3 < n2; ++n3) {
            nArray[n3] = n3;
        }
        n3 = n2;
        for (int i2 = 0; i2 < n2; ++i2) {
            int n4 = RandUtils.nextInt(secureRandom, n3);
            this.perm[i2] = nArray[n4];
            nArray[n4] = nArray[--n3];
        }
    }

    public byte[] getEncoded() {
        int n2 = this.perm.length;
        int n3 = IntegerFunctions.ceilLog256(n2 - 1);
        byte[] byArray = new byte[4 + n2 * n3];
        LittleEndianConversions.I2OSP(n2, byArray, 0);
        for (int i2 = 0; i2 < n2; ++i2) {
            LittleEndianConversions.I2OSP(this.perm[i2], byArray, 4 + i2 * n3, n3);
        }
        return byArray;
    }

    public int[] getVector() {
        return IntUtils.clone(this.perm);
    }

    public Permutation computeInverse() {
        Permutation permutation = new Permutation(this.perm.length);
        for (int i2 = this.perm.length - 1; i2 >= 0; --i2) {
            permutation.perm[this.perm[i2]] = i2;
        }
        return permutation;
    }

    public Permutation rightMultiply(Permutation permutation) {
        if (permutation.perm.length != this.perm.length) {
            throw new IllegalArgumentException("length mismatch");
        }
        Permutation permutation2 = new Permutation(this.perm.length);
        for (int i2 = this.perm.length - 1; i2 >= 0; --i2) {
            permutation2.perm[i2] = this.perm[permutation.perm[i2]];
        }
        return permutation2;
    }

    public boolean equals(Object object) {
        if (!(object instanceof Permutation)) {
            return false;
        }
        Permutation permutation = (Permutation)object;
        return IntUtils.equals(this.perm, permutation.perm);
    }

    public String toString() {
        String string = "[" + this.perm[0];
        for (int i2 = 1; i2 < this.perm.length; ++i2) {
            string = string + ", " + this.perm[i2];
        }
        string = string + "]";
        return string;
    }

    public int hashCode() {
        return Arrays.hashCode(this.perm);
    }

    private boolean isPermutation(int[] nArray) {
        int n2 = nArray.length;
        boolean[] blArray = new boolean[n2];
        for (int i2 = 0; i2 < n2; ++i2) {
            if (nArray[i2] < 0 || nArray[i2] >= n2 || blArray[nArray[i2]]) {
                return false;
            }
            blArray[nArray[i2]] = true;
        }
        return true;
    }
}

