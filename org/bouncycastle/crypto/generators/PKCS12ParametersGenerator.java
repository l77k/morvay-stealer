/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.generators;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.ExtendedDigest;
import org.bouncycastle.crypto.PBEParametersGenerator;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

public class PKCS12ParametersGenerator
extends PBEParametersGenerator {
    public static final int KEY_MATERIAL = 1;
    public static final int IV_MATERIAL = 2;
    public static final int MAC_MATERIAL = 3;
    private Digest digest;
    private int u;
    private int v;

    public PKCS12ParametersGenerator(Digest digest) {
        this.digest = digest;
        if (!(digest instanceof ExtendedDigest)) {
            throw new IllegalArgumentException("Digest " + digest.getAlgorithmName() + " unsupported");
        }
        this.u = digest.getDigestSize();
        this.v = ((ExtendedDigest)digest).getByteLength();
    }

    private void adjust(byte[] byArray, int n2, byte[] byArray2) {
        int n3 = (byArray2[byArray2.length - 1] & 0xFF) + (byArray[n2 + byArray2.length - 1] & 0xFF) + 1;
        byArray[n2 + byArray2.length - 1] = (byte)n3;
        n3 >>>= 8;
        for (int i2 = byArray2.length - 2; i2 >= 0; --i2) {
            byArray[n2 + i2] = (byte)(n3 += (byArray2[i2] & 0xFF) + (byArray[n2 + i2] & 0xFF));
            n3 >>>= 8;
        }
    }

    private byte[] generateDerivedKey(int n2, int n3) {
        byte[] byArray;
        byte[] byArray2;
        byte[] byArray3 = new byte[this.v];
        byte[] byArray4 = new byte[n3];
        for (int i2 = 0; i2 != byArray3.length; ++i2) {
            byArray3[i2] = (byte)n2;
        }
        if (this.salt != null && this.salt.length != 0) {
            byArray2 = new byte[this.v * ((this.salt.length + this.v - 1) / this.v)];
            for (int i3 = 0; i3 != byArray2.length; ++i3) {
                byArray2[i3] = this.salt[i3 % this.salt.length];
            }
        } else {
            byArray2 = new byte[]{};
        }
        if (this.password != null && this.password.length != 0) {
            byArray = new byte[this.v * ((this.password.length + this.v - 1) / this.v)];
            for (int i4 = 0; i4 != byArray.length; ++i4) {
                byArray[i4] = this.password[i4 % this.password.length];
            }
        } else {
            byArray = new byte[]{};
        }
        byte[] byArray5 = new byte[byArray2.length + byArray.length];
        System.arraycopy(byArray2, 0, byArray5, 0, byArray2.length);
        System.arraycopy(byArray, 0, byArray5, byArray2.length, byArray.length);
        byte[] byArray6 = new byte[this.v];
        int n4 = (n3 + this.u - 1) / this.u;
        byte[] byArray7 = new byte[this.u];
        for (int i5 = 1; i5 <= n4; ++i5) {
            int n5;
            this.digest.update(byArray3, 0, byArray3.length);
            this.digest.update(byArray5, 0, byArray5.length);
            this.digest.doFinal(byArray7, 0);
            for (n5 = 1; n5 < this.iterationCount; ++n5) {
                this.digest.update(byArray7, 0, byArray7.length);
                this.digest.doFinal(byArray7, 0);
            }
            for (n5 = 0; n5 != byArray6.length; ++n5) {
                byArray6[n5] = byArray7[n5 % byArray7.length];
            }
            for (n5 = 0; n5 != byArray5.length / this.v; ++n5) {
                this.adjust(byArray5, n5 * this.v, byArray6);
            }
            if (i5 == n4) {
                System.arraycopy(byArray7, 0, byArray4, (i5 - 1) * this.u, byArray4.length - (i5 - 1) * this.u);
                continue;
            }
            System.arraycopy(byArray7, 0, byArray4, (i5 - 1) * this.u, byArray7.length);
        }
        return byArray4;
    }

    @Override
    public CipherParameters generateDerivedParameters(int n2) {
        byte[] byArray = this.generateDerivedKey(1, n2 /= 8);
        return new KeyParameter(byArray, 0, n2);
    }

    @Override
    public CipherParameters generateDerivedParameters(int n2, int n3) {
        byte[] byArray = this.generateDerivedKey(1, n2 /= 8);
        byte[] byArray2 = this.generateDerivedKey(2, n3 /= 8);
        return new ParametersWithIV(new KeyParameter(byArray, 0, n2), byArray2, 0, n3);
    }

    @Override
    public CipherParameters generateDerivedMacParameters(int n2) {
        byte[] byArray = this.generateDerivedKey(3, n2 /= 8);
        return new KeyParameter(byArray, 0, n2);
    }
}

