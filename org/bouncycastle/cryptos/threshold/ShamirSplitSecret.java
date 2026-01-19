/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.threshold;

import java.io.IOException;
import org.bouncycastle.crypto.threshold.Polynomial;
import org.bouncycastle.crypto.threshold.ShamirSecretSplitter;
import org.bouncycastle.crypto.threshold.ShamirSplitSecretShare;
import org.bouncycastle.crypto.threshold.SplitSecret;

public class ShamirSplitSecret
implements SplitSecret {
    private final ShamirSplitSecretShare[] secretShares;
    private final Polynomial poly;

    public ShamirSplitSecret(ShamirSecretSplitter.Algorithm algorithm, ShamirSecretSplitter.Mode mode, ShamirSplitSecretShare[] shamirSplitSecretShareArray) {
        this.secretShares = shamirSplitSecretShareArray;
        this.poly = Polynomial.newInstance(algorithm, mode);
    }

    ShamirSplitSecret(Polynomial polynomial, ShamirSplitSecretShare[] shamirSplitSecretShareArray) {
        this.secretShares = shamirSplitSecretShareArray;
        this.poly = polynomial;
    }

    public ShamirSplitSecretShare[] getSecretShares() {
        return this.secretShares;
    }

    public ShamirSplitSecret multiple(int n2) throws IOException {
        for (int i2 = 0; i2 < this.secretShares.length; ++i2) {
            byte[] byArray = this.secretShares[i2].getEncoded();
            for (int i3 = 0; i3 < byArray.length; ++i3) {
                byArray[i3] = this.poly.gfMul(byArray[i3] & 0xFF, n2);
            }
            this.secretShares[i2] = new ShamirSplitSecretShare(byArray, i2 + 1);
        }
        return this;
    }

    public ShamirSplitSecret divide(int n2) throws IOException {
        for (int i2 = 0; i2 < this.secretShares.length; ++i2) {
            byte[] byArray = this.secretShares[i2].getEncoded();
            for (int i3 = 0; i3 < byArray.length; ++i3) {
                byArray[i3] = this.poly.gfDiv(byArray[i3] & 0xFF, n2);
            }
            this.secretShares[i2] = new ShamirSplitSecretShare(byArray, i2 + 1);
        }
        return this;
    }

    @Override
    public byte[] getSecret() throws IOException {
        int n2 = this.secretShares.length;
        byte[] byArray = new byte[n2];
        byte[] byArray2 = new byte[n2 - 1];
        byte[][] byArray3 = new byte[n2][this.secretShares[0].getEncoded().length];
        for (int i2 = 0; i2 < n2; ++i2) {
            int n3;
            byArray3[i2] = this.secretShares[i2].getEncoded();
            int n4 = 0;
            for (n3 = 0; n3 < n2; ++n3) {
                if (n3 == i2) continue;
                int n5 = n4;
                n4 = (byte)(n4 + 1);
                byArray2[n5] = this.poly.gfDiv(this.secretShares[n3].r, this.secretShares[i2].r ^ this.secretShares[n3].r);
            }
            n4 = 1;
            for (n3 = 0; n3 != byArray2.length; ++n3) {
                n4 = this.poly.gfMul(n4 & 0xFF, byArray2[n3] & 0xFF);
            }
            byArray[i2] = n4;
        }
        return this.poly.gfVecMul(byArray, byArray3);
    }
}

