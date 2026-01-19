/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.generators;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.PBEParametersGenerator;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

public class PKCS5S1ParametersGenerator
extends PBEParametersGenerator {
    private Digest digest;

    public PKCS5S1ParametersGenerator(Digest digest) {
        this.digest = digest;
    }

    private byte[] generateDerivedKey() {
        byte[] byArray = new byte[this.digest.getDigestSize()];
        this.digest.update(this.password, 0, this.password.length);
        this.digest.update(this.salt, 0, this.salt.length);
        this.digest.doFinal(byArray, 0);
        for (int i2 = 1; i2 < this.iterationCount; ++i2) {
            this.digest.update(byArray, 0, byArray.length);
            this.digest.doFinal(byArray, 0);
        }
        return byArray;
    }

    @Override
    public CipherParameters generateDerivedParameters(int n2) {
        if ((n2 /= 8) > this.digest.getDigestSize()) {
            throw new IllegalArgumentException("Can't generate a derived key " + n2 + " bytes long.");
        }
        byte[] byArray = this.generateDerivedKey();
        return new KeyParameter(byArray, 0, n2);
    }

    @Override
    public CipherParameters generateDerivedParameters(int n2, int n3) {
        if ((n2 /= 8) + (n3 /= 8) > this.digest.getDigestSize()) {
            throw new IllegalArgumentException("Can't generate a derived key " + (n2 + n3) + " bytes long.");
        }
        byte[] byArray = this.generateDerivedKey();
        return new ParametersWithIV(new KeyParameter(byArray, 0, n2), byArray, n2, n3);
    }

    @Override
    public CipherParameters generateDerivedMacParameters(int n2) {
        return this.generateDerivedParameters(n2);
    }
}

