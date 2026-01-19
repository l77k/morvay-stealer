/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.util;

import org.bouncycastle.crypto.util.PBKDFConfig;
import org.bouncycastle.internal.asn1.misc.MiscObjectIdentifiers;

public class ScryptConfig
extends PBKDFConfig {
    private final int costParameter;
    private final int blockSize;
    private final int parallelizationParameter;
    private final int saltLength;

    private ScryptConfig(Builder builder) {
        super(MiscObjectIdentifiers.id_scrypt);
        this.costParameter = builder.costParameter;
        this.blockSize = builder.blockSize;
        this.parallelizationParameter = builder.parallelizationParameter;
        this.saltLength = builder.saltLength;
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

    public int getSaltLength() {
        return this.saltLength;
    }

    public static class Builder {
        private final int costParameter;
        private final int blockSize;
        private final int parallelizationParameter;
        private int saltLength = 16;

        public Builder(int n2, int n3, int n4) {
            if (n2 <= 1 || !Builder.isPowerOf2(n2)) {
                throw new IllegalArgumentException("Cost parameter N must be > 1 and a power of 2");
            }
            this.costParameter = n2;
            this.blockSize = n3;
            this.parallelizationParameter = n4;
        }

        public Builder withSaltLength(int n2) {
            this.saltLength = n2;
            return this;
        }

        public ScryptConfig build() {
            return new ScryptConfig(this);
        }

        private static boolean isPowerOf2(int n2) {
            return (n2 & n2 - 1) == 0;
        }
    }
}

