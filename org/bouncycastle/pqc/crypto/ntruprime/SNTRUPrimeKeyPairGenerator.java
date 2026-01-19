/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.ntruprime;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.pqc.crypto.ntruprime.SNTRUPrimeKeyGenerationParameters;
import org.bouncycastle.pqc.crypto.ntruprime.SNTRUPrimePrivateKeyParameters;
import org.bouncycastle.pqc.crypto.ntruprime.SNTRUPrimePublicKeyParameters;
import org.bouncycastle.pqc.crypto.ntruprime.Utils;
import org.bouncycastle.util.Arrays;

public class SNTRUPrimeKeyPairGenerator
implements AsymmetricCipherKeyPairGenerator {
    private SNTRUPrimeKeyGenerationParameters params;

    public SNTRUPrimeKeyGenerationParameters getParams() {
        return this.params;
    }

    @Override
    public void init(KeyGenerationParameters keyGenerationParameters) {
        this.params = (SNTRUPrimeKeyGenerationParameters)keyGenerationParameters;
    }

    @Override
    public AsymmetricCipherKeyPair generateKeyPair() {
        int n2 = this.params.getSntrupParams().getP();
        int n3 = this.params.getSntrupParams().getQ();
        int n4 = this.params.getSntrupParams().getW();
        byte[] byArray = new byte[n2];
        byte[] byArray2 = new byte[n2];
        do {
            Utils.getRandomSmallPolynomial(this.params.getRandom(), byArray);
        } while (!Utils.isInvertiblePolynomialInR3(byArray, byArray2, n2));
        byte[] byArray3 = new byte[n2];
        Utils.getRandomShortPolynomial(this.params.getRandom(), byArray3, n2, n4);
        short[] sArray = new short[n2];
        Utils.getOneThirdInverseInRQ(sArray, byArray3, n2, n3);
        short[] sArray2 = new short[n2];
        Utils.multiplicationInRQ(sArray2, sArray, byArray, n2, n3);
        byte[] byArray4 = new byte[this.params.getSntrupParams().getPublicKeyBytes()];
        Utils.getEncodedPolynomial(byArray4, sArray2, n2, n3);
        SNTRUPrimePublicKeyParameters sNTRUPrimePublicKeyParameters = new SNTRUPrimePublicKeyParameters(this.params.getSntrupParams(), byArray4);
        byte[] byArray5 = new byte[(n2 + 3) / 4];
        Utils.getEncodedSmallPolynomial(byArray5, byArray3, n2);
        byte[] byArray6 = new byte[(n2 + 3) / 4];
        Utils.getEncodedSmallPolynomial(byArray6, byArray2, n2);
        byte[] byArray7 = new byte[(n2 + 3) / 4];
        this.params.getRandom().nextBytes(byArray7);
        byte[] byArray8 = new byte[]{4};
        byte[] byArray9 = Utils.getHashWithPrefix(byArray8, byArray4);
        SNTRUPrimePrivateKeyParameters sNTRUPrimePrivateKeyParameters = new SNTRUPrimePrivateKeyParameters(this.params.getSntrupParams(), byArray5, byArray6, byArray4, byArray7, Arrays.copyOfRange(byArray9, 0, byArray9.length / 2));
        return new AsymmetricCipherKeyPair(sNTRUPrimePublicKeyParameters, sNTRUPrimePrivateKeyParameters);
    }
}

