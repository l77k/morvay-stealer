/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.jcajce.spec;

import java.security.spec.KeySpec;
import org.bouncycastle.util.Arrays;

public class ScryptKeySpec
implements KeySpec {
    private final char[] password;
    private final byte[] salt;
    private final int costParameter;
    private final int blockSize;
    private final int parallelizationParameter;
    private final int keySize;

    public ScryptKeySpec(char[] cArray, byte[] byArray, int n2, int n3, int n4, int n5) {
        this.password = cArray;
        this.salt = Arrays.clone(byArray);
        this.costParameter = n2;
        this.blockSize = n3;
        this.parallelizationParameter = n4;
        this.keySize = n5;
    }

    public char[] getPassword() {
        return this.password;
    }

    public byte[] getSalt() {
        return Arrays.clone(this.salt);
    }

    public int getCostParameter() {
        return this.costParameter;
    }

    public int getBlockSize() {
        return this.blockSize;
    }

    public int getParallelizationParameter() {
        return this.parallelizationParameter;
    }

    public int getKeyLength() {
        return this.keySize;
    }
}

