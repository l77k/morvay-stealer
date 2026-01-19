/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.digests;

import org.bouncycastle.crypto.CryptoServicePurpose;
import org.bouncycastle.crypto.digests.SHAKEDigest;
import org.bouncycastle.crypto.digests.XofUtils;
import org.bouncycastle.util.Arrays;

public class CSHAKEDigest
extends SHAKEDigest {
    private static final byte[] padding = new byte[100];
    private final byte[] diff;

    public CSHAKEDigest(int n2, byte[] byArray, byte[] byArray2) {
        this(n2, CryptoServicePurpose.ANY, byArray, byArray2);
    }

    public CSHAKEDigest(int n2, CryptoServicePurpose cryptoServicePurpose, byte[] byArray, byte[] byArray2) {
        super(n2, cryptoServicePurpose);
        if (!(byArray != null && byArray.length != 0 || byArray2 != null && byArray2.length != 0)) {
            this.diff = null;
        } else {
            this.diff = Arrays.concatenate(XofUtils.leftEncode(this.rate / 8), this.encodeString(byArray), this.encodeString(byArray2));
            this.diffPadAndAbsorb();
        }
    }

    public CSHAKEDigest(CSHAKEDigest cSHAKEDigest) {
        super(cSHAKEDigest);
        this.diff = Arrays.clone(cSHAKEDigest.diff);
    }

    private void diffPadAndAbsorb() {
        int n2 = this.rate / 8;
        this.absorb(this.diff, 0, this.diff.length);
        int n3 = this.diff.length % n2;
        if (n3 != 0) {
            int n4;
            for (n4 = n2 - n3; n4 > padding.length; n4 -= padding.length) {
                this.absorb(padding, 0, padding.length);
            }
            this.absorb(padding, 0, n4);
        }
    }

    private byte[] encodeString(byte[] byArray) {
        if (byArray == null || byArray.length == 0) {
            return XofUtils.leftEncode(0L);
        }
        return Arrays.concatenate(XofUtils.leftEncode((long)byArray.length * 8L), byArray);
    }

    @Override
    public String getAlgorithmName() {
        return "CSHAKE" + this.fixedOutputLength;
    }

    @Override
    public int doOutput(byte[] byArray, int n2, int n3) {
        if (this.diff != null) {
            if (!this.squeezing) {
                this.absorbBits(0, 2);
            }
            this.squeeze(byArray, n2, (long)n3 * 8L);
            return n3;
        }
        return super.doOutput(byArray, n2, n3);
    }

    @Override
    public void reset() {
        super.reset();
        if (this.diff != null) {
            this.diffPadAndAbsorb();
        }
    }
}

