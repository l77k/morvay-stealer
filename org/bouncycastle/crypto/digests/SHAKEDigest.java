/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.digests;

import org.bouncycastle.crypto.CryptoServiceProperties;
import org.bouncycastle.crypto.CryptoServicePurpose;
import org.bouncycastle.crypto.Xof;
import org.bouncycastle.crypto.digests.KeccakDigest;
import org.bouncycastle.crypto.digests.Utils;

public class SHAKEDigest
extends KeccakDigest
implements Xof {
    private static int checkBitLength(int n2) {
        switch (n2) {
            case 128: 
            case 256: {
                return n2;
            }
        }
        throw new IllegalArgumentException("'bitStrength' " + n2 + " not supported for SHAKE");
    }

    public SHAKEDigest() {
        this(128);
    }

    public SHAKEDigest(CryptoServicePurpose cryptoServicePurpose) {
        this(128, cryptoServicePurpose);
    }

    public SHAKEDigest(int n2) {
        super(SHAKEDigest.checkBitLength(n2), CryptoServicePurpose.ANY);
    }

    public SHAKEDigest(int n2, CryptoServicePurpose cryptoServicePurpose) {
        super(SHAKEDigest.checkBitLength(n2), cryptoServicePurpose);
    }

    public SHAKEDigest(SHAKEDigest sHAKEDigest) {
        super(sHAKEDigest);
    }

    @Override
    public String getAlgorithmName() {
        return "SHAKE" + this.fixedOutputLength;
    }

    @Override
    public int getDigestSize() {
        return this.fixedOutputLength / 4;
    }

    @Override
    public int doFinal(byte[] byArray, int n2) {
        return this.doFinal(byArray, n2, this.getDigestSize());
    }

    @Override
    public int doFinal(byte[] byArray, int n2, int n3) {
        int n4 = this.doOutput(byArray, n2, n3);
        this.reset();
        return n4;
    }

    @Override
    public int doOutput(byte[] byArray, int n2, int n3) {
        if (!this.squeezing) {
            this.absorbBits(15, 4);
        }
        this.squeeze(byArray, n2, (long)n3 * 8L);
        return n3;
    }

    @Override
    protected int doFinal(byte[] byArray, int n2, byte by, int n3) {
        return this.doFinal(byArray, n2, this.getDigestSize(), by, n3);
    }

    protected int doFinal(byte[] byArray, int n2, int n3, byte by, int n4) {
        if (n4 < 0 || n4 > 7) {
            throw new IllegalArgumentException("'partialBits' must be in the range [0,7]");
        }
        int n5 = by & (1 << n4) - 1 | 15 << n4;
        int n6 = n4 + 4;
        if (n6 >= 8) {
            this.absorb((byte)n5);
            n6 -= 8;
            n5 >>>= 8;
        }
        if (n6 > 0) {
            this.absorbBits(n5, n6);
        }
        this.squeeze(byArray, n2, (long)n3 * 8L);
        this.reset();
        return n3;
    }

    @Override
    protected CryptoServiceProperties cryptoServiceProperties() {
        return Utils.getDefaultProperties(this, this.purpose);
    }
}

