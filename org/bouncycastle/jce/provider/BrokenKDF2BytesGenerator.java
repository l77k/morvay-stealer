/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.jce.provider;

import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.DerivationFunction;
import org.bouncycastle.crypto.DerivationParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.params.KDFParameters;

public class BrokenKDF2BytesGenerator
implements DerivationFunction {
    private Digest digest;
    private byte[] shared;
    private byte[] iv;

    public BrokenKDF2BytesGenerator(Digest digest) {
        this.digest = digest;
    }

    @Override
    public void init(DerivationParameters derivationParameters) {
        if (!(derivationParameters instanceof KDFParameters)) {
            throw new IllegalArgumentException("KDF parameters required for generator");
        }
        KDFParameters kDFParameters = (KDFParameters)derivationParameters;
        this.shared = kDFParameters.getSharedSecret();
        this.iv = kDFParameters.getIV();
    }

    public Digest getDigest() {
        return this.digest;
    }

    @Override
    public int generateBytes(byte[] byArray, int n2, int n3) throws DataLengthException, IllegalArgumentException {
        if (byArray.length - n3 < n2) {
            throw new OutputLengthException("output buffer too small");
        }
        long l2 = (long)n3 * 8L;
        if (l2 > (long)this.digest.getDigestSize() * 8L * 0x80000000L) {
            throw new IllegalArgumentException("Output length too large");
        }
        int n4 = (int)(l2 / (long)this.digest.getDigestSize());
        byte[] byArray2 = null;
        byArray2 = new byte[this.digest.getDigestSize()];
        for (int i2 = 1; i2 <= n4; ++i2) {
            this.digest.update(this.shared, 0, this.shared.length);
            this.digest.update((byte)(i2 & 0xFF));
            this.digest.update((byte)(i2 >> 8 & 0xFF));
            this.digest.update((byte)(i2 >> 16 & 0xFF));
            this.digest.update((byte)(i2 >> 24 & 0xFF));
            this.digest.update(this.iv, 0, this.iv.length);
            this.digest.doFinal(byArray2, 0);
            if (n3 - n2 > byArray2.length) {
                System.arraycopy(byArray2, 0, byArray, n2, byArray2.length);
                n2 += byArray2.length;
                continue;
            }
            System.arraycopy(byArray2, 0, byArray, n2, n3 - n2);
        }
        this.digest.reset();
        return n3;
    }
}

