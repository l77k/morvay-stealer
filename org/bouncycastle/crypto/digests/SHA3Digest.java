/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.digests;

import org.bouncycastle.crypto.CryptoServicePurpose;
import org.bouncycastle.crypto.digests.KeccakDigest;

public class SHA3Digest
extends KeccakDigest {
    private static int checkBitLength(int n2) {
        switch (n2) {
            case 224: 
            case 256: 
            case 384: 
            case 512: {
                return n2;
            }
        }
        throw new IllegalArgumentException("'bitLength' " + n2 + " not supported for SHA-3");
    }

    public SHA3Digest() {
        this(256, CryptoServicePurpose.ANY);
    }

    public SHA3Digest(CryptoServicePurpose cryptoServicePurpose) {
        this(256, cryptoServicePurpose);
    }

    public SHA3Digest(int n2) {
        super(SHA3Digest.checkBitLength(n2), CryptoServicePurpose.ANY);
    }

    public SHA3Digest(int n2, CryptoServicePurpose cryptoServicePurpose) {
        super(SHA3Digest.checkBitLength(n2), cryptoServicePurpose);
    }

    public SHA3Digest(SHA3Digest sHA3Digest) {
        super(sHA3Digest);
    }

    @Override
    public String getAlgorithmName() {
        return "SHA3-" + this.fixedOutputLength;
    }

    @Override
    public int doFinal(byte[] byArray, int n2) {
        this.absorbBits(2, 2);
        return super.doFinal(byArray, n2);
    }

    @Override
    protected int doFinal(byte[] byArray, int n2, byte by, int n3) {
        if (n3 < 0 || n3 > 7) {
            throw new IllegalArgumentException("'partialBits' must be in the range [0,7]");
        }
        int n4 = by & (1 << n3) - 1 | 2 << n3;
        int n5 = n3 + 2;
        if (n5 >= 8) {
            this.absorb((byte)n4);
            n5 -= 8;
            n4 >>>= 8;
        }
        return super.doFinal(byArray, n2, (byte)n4, n5);
    }
}

