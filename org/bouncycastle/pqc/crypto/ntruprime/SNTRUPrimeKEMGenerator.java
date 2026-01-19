/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.ntruprime;

import java.security.SecureRandom;
import org.bouncycastle.crypto.EncapsulatedSecretGenerator;
import org.bouncycastle.crypto.SecretWithEncapsulation;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.pqc.crypto.ntruprime.SNTRUPrimeParameters;
import org.bouncycastle.pqc.crypto.ntruprime.SNTRUPrimePublicKeyParameters;
import org.bouncycastle.pqc.crypto.ntruprime.Utils;
import org.bouncycastle.pqc.crypto.util.SecretWithEncapsulationImpl;
import org.bouncycastle.util.Arrays;

public class SNTRUPrimeKEMGenerator
implements EncapsulatedSecretGenerator {
    private final SecureRandom random;

    public SNTRUPrimeKEMGenerator(SecureRandom secureRandom) {
        this.random = secureRandom;
    }

    @Override
    public SecretWithEncapsulation generateEncapsulated(AsymmetricKeyParameter asymmetricKeyParameter) {
        SNTRUPrimePublicKeyParameters sNTRUPrimePublicKeyParameters = (SNTRUPrimePublicKeyParameters)asymmetricKeyParameter;
        SNTRUPrimeParameters sNTRUPrimeParameters = sNTRUPrimePublicKeyParameters.getParameters();
        int n2 = sNTRUPrimeParameters.getP();
        int n3 = sNTRUPrimeParameters.getQ();
        int n4 = sNTRUPrimeParameters.getW();
        int n5 = sNTRUPrimeParameters.getRoundedPolynomialBytes();
        byte[] byArray = new byte[]{4};
        byte[] byArray2 = Utils.getHashWithPrefix(byArray, sNTRUPrimePublicKeyParameters.getEncoded());
        byte[] byArray3 = new byte[n2];
        Utils.getRandomShortPolynomial(this.random, byArray3, n2, n4);
        byte[] byArray4 = new byte[(n2 + 3) / 4];
        Utils.getEncodedSmallPolynomial(byArray4, byArray3, n2);
        short[] sArray = new short[n2];
        Utils.getDecodedPolynomial(sArray, sNTRUPrimePublicKeyParameters.getEncH(), n2, n3);
        short[] sArray2 = new short[n2];
        Utils.multiplicationInRQ(sArray2, sArray, byArray3, n2, n3);
        short[] sArray3 = new short[n2];
        Utils.roundPolynomial(sArray3, sArray2);
        byte[] byArray5 = new byte[n5];
        Utils.getRoundedEncodedPolynomial(byArray5, sArray3, n2, n3);
        byte[] byArray6 = new byte[]{3};
        byte[] byArray7 = Utils.getHashWithPrefix(byArray6, byArray4);
        byte[] byArray8 = new byte[byArray7.length / 2 + byArray2.length / 2];
        System.arraycopy(byArray7, 0, byArray8, 0, byArray7.length / 2);
        System.arraycopy(byArray2, 0, byArray8, byArray7.length / 2, byArray2.length / 2);
        byte[] byArray9 = new byte[]{2};
        byte[] byArray10 = Utils.getHashWithPrefix(byArray9, byArray8);
        byte[] byArray11 = new byte[byArray5.length + byArray10.length / 2];
        System.arraycopy(byArray5, 0, byArray11, 0, byArray5.length);
        System.arraycopy(byArray10, 0, byArray11, byArray5.length, byArray10.length / 2);
        byte[] byArray12 = new byte[]{3};
        byte[] byArray13 = Utils.getHashWithPrefix(byArray12, byArray4);
        byte[] byArray14 = new byte[byArray13.length / 2 + byArray11.length];
        System.arraycopy(byArray13, 0, byArray14, 0, byArray13.length / 2);
        System.arraycopy(byArray11, 0, byArray14, byArray13.length / 2, byArray11.length);
        byte[] byArray15 = new byte[]{1};
        byte[] byArray16 = Utils.getHashWithPrefix(byArray15, byArray14);
        byte[] byArray17 = Arrays.copyOfRange(byArray16, 0, sNTRUPrimeParameters.getSessionKeySize() / 8);
        return new SecretWithEncapsulationImpl(byArray17, byArray11);
    }
}

