/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.threshold;

import java.io.IOException;
import java.security.SecureRandom;
import org.bouncycastle.crypto.threshold.Polynomial;
import org.bouncycastle.crypto.threshold.SecretShare;
import org.bouncycastle.crypto.threshold.SecretSplitter;
import org.bouncycastle.crypto.threshold.ShamirSplitSecret;
import org.bouncycastle.crypto.threshold.ShamirSplitSecretShare;
import org.bouncycastle.util.Arrays;

public class ShamirSecretSplitter
implements SecretSplitter {
    private final Polynomial poly;
    protected int l;
    protected SecureRandom random;

    public ShamirSecretSplitter(Algorithm algorithm, Mode mode, int n2, SecureRandom secureRandom) {
        if (n2 < 0 || n2 > 65534) {
            throw new IllegalArgumentException("Invalid input: l ranges from 0 to 65534 (2^16-2) bytes.");
        }
        this.poly = Polynomial.newInstance(algorithm, mode);
        this.l = n2;
        this.random = secureRandom;
    }

    @Override
    public ShamirSplitSecret split(int n2, int n3) {
        int n4;
        byte[][] byArray = this.initP(n2, n3);
        byte[][] byArray2 = new byte[n2][this.l];
        ShamirSplitSecretShare[] shamirSplitSecretShareArray = new ShamirSplitSecretShare[this.l];
        for (n4 = 0; n4 < n2; ++n4) {
            this.random.nextBytes(byArray2[n4]);
        }
        for (n4 = 0; n4 < byArray.length; ++n4) {
            shamirSplitSecretShareArray[n4] = new ShamirSplitSecretShare(this.poly.gfVecMul(byArray[n4], byArray2), n4 + 1);
        }
        return new ShamirSplitSecret(this.poly, shamirSplitSecretShareArray);
    }

    @Override
    public ShamirSplitSecret splitAround(SecretShare secretShare, int n2, int n3) throws IOException {
        int n4;
        byte[][] byArray = this.initP(n2, n3);
        byte[][] byArray2 = new byte[n2][this.l];
        ShamirSplitSecretShare[] shamirSplitSecretShareArray = new ShamirSplitSecretShare[this.l];
        byte[] byArray3 = secretShare.getEncoded();
        shamirSplitSecretShareArray[0] = new ShamirSplitSecretShare(byArray3, 1);
        for (n4 = 0; n4 < n2; ++n4) {
            this.random.nextBytes(byArray2[n4]);
        }
        for (n4 = 0; n4 < this.l; ++n4) {
            byte by = byArray2[1][n4];
            for (int i2 = 2; i2 < n2; ++i2) {
                by = (byte)(by ^ byArray2[i2][n4]);
            }
            byArray2[0][n4] = (byte)(by ^ byArray3[n4]);
        }
        for (n4 = 1; n4 < byArray.length; ++n4) {
            shamirSplitSecretShareArray[n4] = new ShamirSplitSecretShare(this.poly.gfVecMul(byArray[n4], byArray2), n4 + 1);
        }
        return new ShamirSplitSecret(this.poly, shamirSplitSecretShareArray);
    }

    @Override
    public ShamirSplitSecret resplit(byte[] byArray, int n2, int n3) {
        int n4;
        byte[][] byArray2 = this.initP(n2, n3);
        byte[][] byArray3 = new byte[n2][this.l];
        ShamirSplitSecretShare[] shamirSplitSecretShareArray = new ShamirSplitSecretShare[this.l];
        byArray3[0] = Arrays.clone(byArray);
        for (n4 = 1; n4 < n2; ++n4) {
            this.random.nextBytes(byArray3[n4]);
        }
        for (n4 = 0; n4 < byArray2.length; ++n4) {
            shamirSplitSecretShareArray[n4] = new ShamirSplitSecretShare(this.poly.gfVecMul(byArray2[n4], byArray3), n4 + 1);
        }
        return new ShamirSplitSecret(this.poly, shamirSplitSecretShareArray);
    }

    private byte[][] initP(int n2, int n3) {
        if (n2 < 1 || n2 > 255) {
            throw new IllegalArgumentException("Invalid input: m must be less than 256 and positive.");
        }
        if (n3 < n2 || n3 > 255) {
            throw new IllegalArgumentException("Invalid input: n must be less than 256 and greater than or equal to n.");
        }
        byte[][] byArray = new byte[n3][n2];
        for (int i2 = 0; i2 < n3; ++i2) {
            for (int i3 = 0; i3 < n2; ++i3) {
                byArray[i2][i3] = this.poly.gfPow((byte)(i2 + 1), (byte)i3);
            }
        }
        return byArray;
    }

    public static enum Algorithm {
        AES,
        RSA;

    }

    public static enum Mode {
        Native,
        Table;

    }
}

