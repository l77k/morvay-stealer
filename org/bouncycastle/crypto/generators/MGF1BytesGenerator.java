/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.generators;

import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.DerivationFunction;
import org.bouncycastle.crypto.DerivationParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.params.MGFParameters;

public class MGF1BytesGenerator
implements DerivationFunction {
    private Digest digest;
    private byte[] seed;
    private int hLen;

    public MGF1BytesGenerator(Digest digest) {
        this.digest = digest;
        this.hLen = digest.getDigestSize();
    }

    @Override
    public void init(DerivationParameters derivationParameters) {
        if (!(derivationParameters instanceof MGFParameters)) {
            throw new IllegalArgumentException("MGF parameters required for MGF1Generator");
        }
        MGFParameters mGFParameters = (MGFParameters)derivationParameters;
        this.seed = mGFParameters.getSeed();
    }

    public Digest getDigest() {
        return this.digest;
    }

    private void ItoOSP(int n2, byte[] byArray) {
        byArray[0] = (byte)(n2 >>> 24);
        byArray[1] = (byte)(n2 >>> 16);
        byArray[2] = (byte)(n2 >>> 8);
        byArray[3] = (byte)(n2 >>> 0);
    }

    @Override
    public int generateBytes(byte[] byArray, int n2, int n3) throws DataLengthException, IllegalArgumentException {
        if (byArray.length - n3 < n2) {
            throw new OutputLengthException("output buffer too small");
        }
        byte[] byArray2 = new byte[this.hLen];
        byte[] byArray3 = new byte[4];
        int n4 = 0;
        this.digest.reset();
        if (n3 > this.hLen) {
            do {
                this.ItoOSP(n4, byArray3);
                this.digest.update(this.seed, 0, this.seed.length);
                this.digest.update(byArray3, 0, byArray3.length);
                this.digest.doFinal(byArray2, 0);
                System.arraycopy(byArray2, 0, byArray, n2 + n4 * this.hLen, this.hLen);
            } while (++n4 < n3 / this.hLen);
        }
        if (n4 * this.hLen < n3) {
            this.ItoOSP(n4, byArray3);
            this.digest.update(this.seed, 0, this.seed.length);
            this.digest.update(byArray3, 0, byArray3.length);
            this.digest.doFinal(byArray2, 0);
            System.arraycopy(byArray2, 0, byArray, n2 + n4 * this.hLen, n3 - n4 * this.hLen);
        }
        return n3;
    }
}

