/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.agreement.kdf;

import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.DerivationFunction;
import org.bouncycastle.crypto.DerivationParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.params.KDFParameters;

public class ConcatenationKDFGenerator
implements DerivationFunction {
    private Digest digest;
    private byte[] shared;
    private byte[] otherInfo;
    private int hLen;

    public ConcatenationKDFGenerator(Digest digest) {
        this.digest = digest;
        this.hLen = digest.getDigestSize();
    }

    @Override
    public void init(DerivationParameters derivationParameters) {
        if (!(derivationParameters instanceof KDFParameters)) {
            throw new IllegalArgumentException("KDF parameters required for generator");
        }
        KDFParameters kDFParameters = (KDFParameters)derivationParameters;
        this.shared = kDFParameters.getSharedSecret();
        this.otherInfo = kDFParameters.getIV();
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
        if (n3 <= 0) {
            throw new IllegalArgumentException("len must be > 0");
        }
        if (byArray.length - n3 < n2) {
            throw new OutputLengthException("output buffer too small");
        }
        byte[] byArray2 = new byte[this.hLen];
        byte[] byArray3 = new byte[4];
        int n4 = 1;
        int n5 = 0;
        this.digest.reset();
        if (n3 > this.hLen) {
            do {
                this.ItoOSP(n4, byArray3);
                this.digest.update(byArray3, 0, byArray3.length);
                this.digest.update(this.shared, 0, this.shared.length);
                this.digest.update(this.otherInfo, 0, this.otherInfo.length);
                this.digest.doFinal(byArray2, 0);
                System.arraycopy(byArray2, 0, byArray, n2 + n5, this.hLen);
                n5 += this.hLen;
            } while (n4++ < n3 / this.hLen);
        }
        if (n5 < n3) {
            this.ItoOSP(n4, byArray3);
            this.digest.update(byArray3, 0, byArray3.length);
            this.digest.update(this.shared, 0, this.shared.length);
            this.digest.update(this.otherInfo, 0, this.otherInfo.length);
            this.digest.doFinal(byArray2, 0);
            System.arraycopy(byArray2, 0, byArray, n2 + n5, n3 - n5);
        }
        return n3;
    }
}

