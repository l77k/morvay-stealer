/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.falcon;

import java.security.SecureRandom;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.pqc.crypto.falcon.FalconKeyGenerationParameters;
import org.bouncycastle.pqc.crypto.falcon.FalconNIST;
import org.bouncycastle.pqc.crypto.falcon.FalconParameters;
import org.bouncycastle.pqc.crypto.falcon.FalconPrivateKeyParameters;
import org.bouncycastle.pqc.crypto.falcon.FalconPublicKeyParameters;

public class FalconKeyPairGenerator
implements AsymmetricCipherKeyPairGenerator {
    private FalconKeyGenerationParameters params;
    private SecureRandom random;
    private FalconNIST nist;
    private int logn;
    private int noncelen;
    private int pk_size;
    private int sk_size;

    @Override
    public void init(KeyGenerationParameters keyGenerationParameters) {
        this.params = (FalconKeyGenerationParameters)keyGenerationParameters;
        this.random = keyGenerationParameters.getRandom();
        this.logn = ((FalconKeyGenerationParameters)keyGenerationParameters).getParameters().getLogN();
        this.noncelen = ((FalconKeyGenerationParameters)keyGenerationParameters).getParameters().getNonceLength();
        this.nist = new FalconNIST(this.logn, this.noncelen, this.random);
        int n2 = 1 << this.logn;
        int n3 = 8;
        if (n2 == 1024) {
            n3 = 5;
        } else if (n2 == 256 || n2 == 512) {
            n3 = 6;
        } else if (n2 == 64 || n2 == 128) {
            n3 = 7;
        }
        this.pk_size = 1 + 14 * n2 / 8;
        this.sk_size = 1 + 2 * n3 * n2 / 8 + n2;
    }

    @Override
    public AsymmetricCipherKeyPair generateKeyPair() {
        byte[] byArray = new byte[this.pk_size];
        byte[] byArray2 = new byte[this.sk_size];
        byte[][] byArray3 = this.nist.crypto_sign_keypair(byArray, 0, byArray2, 0);
        FalconParameters falconParameters = this.params.getParameters();
        FalconPrivateKeyParameters falconPrivateKeyParameters = new FalconPrivateKeyParameters(falconParameters, byArray3[1], byArray3[2], byArray3[3], byArray3[0]);
        FalconPublicKeyParameters falconPublicKeyParameters = new FalconPublicKeyParameters(falconParameters, byArray3[0]);
        return new AsymmetricCipherKeyPair(falconPublicKeyParameters, falconPrivateKeyParameters);
    }
}

