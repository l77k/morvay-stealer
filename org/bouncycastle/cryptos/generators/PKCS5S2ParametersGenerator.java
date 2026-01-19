/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.generators;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.PBEParametersGenerator;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.crypto.util.DigestFactory;

public class PKCS5S2ParametersGenerator
extends PBEParametersGenerator {
    private Mac hMac;
    private byte[] state;

    public PKCS5S2ParametersGenerator() {
        this(DigestFactory.createSHA1());
    }

    public PKCS5S2ParametersGenerator(Digest digest) {
        this.hMac = new HMac(digest);
        this.state = new byte[this.hMac.getMacSize()];
    }

    private void F(byte[] byArray, int n2, byte[] byArray2, byte[] byArray3, int n3) {
        if (n2 == 0) {
            throw new IllegalArgumentException("iteration count must be at least 1.");
        }
        if (byArray != null) {
            this.hMac.update(byArray, 0, byArray.length);
        }
        this.hMac.update(byArray2, 0, byArray2.length);
        this.hMac.doFinal(this.state, 0);
        System.arraycopy(this.state, 0, byArray3, n3, this.state.length);
        for (int i2 = 1; i2 < n2; ++i2) {
            this.hMac.update(this.state, 0, this.state.length);
            this.hMac.doFinal(this.state, 0);
            for (int i3 = 0; i3 != this.state.length; ++i3) {
                int n4 = n3 + i3;
                byArray3[n4] = (byte)(byArray3[n4] ^ this.state[i3]);
            }
        }
    }

    private byte[] generateDerivedKey(int n2) {
        int n3 = this.hMac.getMacSize();
        int n4 = (n2 + n3 - 1) / n3;
        byte[] byArray = new byte[4];
        byte[] byArray2 = new byte[n4 * n3];
        int n5 = 0;
        KeyParameter keyParameter = new KeyParameter(this.password);
        this.hMac.init(keyParameter);
        for (int i2 = 1; i2 <= n4; ++i2) {
            int n6;
            int n7 = 3;
            do {
                n6 = n7--;
            } while ((byArray[n6] = (byte)(byArray[n6] + 1)) == 0);
            this.F(this.salt, this.iterationCount, byArray, byArray2, n5);
            n5 += n3;
        }
        return byArray2;
    }

    @Override
    public CipherParameters generateDerivedParameters(int n2) {
        byte[] byArray = this.generateDerivedKey(n2 /= 8);
        return new KeyParameter(byArray, 0, n2);
    }

    @Override
    public CipherParameters generateDerivedParameters(int n2, int n3) {
        byte[] byArray = this.generateDerivedKey((n2 /= 8) + (n3 /= 8));
        return new ParametersWithIV(new KeyParameter(byArray, 0, n2), byArray, n2, n3);
    }

    @Override
    public CipherParameters generateDerivedMacParameters(int n2) {
        return this.generateDerivedParameters(n2);
    }
}

