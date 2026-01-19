/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.ntruprime;

import org.bouncycastle.crypto.EncapsulatedSecretExtractor;
import org.bouncycastle.pqc.crypto.ntruprime.SNTRUPrimeParameters;
import org.bouncycastle.pqc.crypto.ntruprime.SNTRUPrimePrivateKeyParameters;
import org.bouncycastle.pqc.crypto.ntruprime.Utils;
import org.bouncycastle.util.Arrays;

public class SNTRUPrimeKEMExtractor
implements EncapsulatedSecretExtractor {
    private final SNTRUPrimePrivateKeyParameters privateKey;

    public SNTRUPrimeKEMExtractor(SNTRUPrimePrivateKeyParameters sNTRUPrimePrivateKeyParameters) {
        this.privateKey = sNTRUPrimePrivateKeyParameters;
    }

    @Override
    public byte[] extractSecret(byte[] byArray) {
        SNTRUPrimeParameters sNTRUPrimeParameters = this.privateKey.getParameters();
        int n2 = sNTRUPrimeParameters.getP();
        int n3 = sNTRUPrimeParameters.getQ();
        int n4 = sNTRUPrimeParameters.getW();
        int n5 = sNTRUPrimeParameters.getRoundedPolynomialBytes();
        byte[] byArray2 = new byte[n2];
        Utils.getDecodedSmallPolynomial(byArray2, this.privateKey.getF(), n2);
        byte[] byArray3 = new byte[n2];
        Utils.getDecodedSmallPolynomial(byArray3, this.privateKey.getGinv(), n2);
        short[] sArray = new short[n2];
        Utils.getRoundedDecodedPolynomial(sArray, byArray, n2, n3);
        short[] sArray2 = new short[n2];
        Utils.multiplicationInRQ(sArray2, sArray, byArray2, n2, n3);
        short[] sArray3 = new short[n2];
        Utils.scalarMultiplicationInRQ(sArray3, sArray2, 3, n3);
        byte[] byArray4 = new byte[n2];
        Utils.transformRQToR3(byArray4, sArray3);
        byte[] byArray5 = new byte[n2];
        Utils.multiplicationInR3(byArray5, byArray4, byArray3, n2);
        byte[] byArray6 = new byte[n2];
        Utils.checkForSmallPolynomial(byArray6, byArray5, n2, n4);
        byte[] byArray7 = new byte[(n2 + 3) / 4];
        Utils.getEncodedSmallPolynomial(byArray7, byArray6, n2);
        short[] sArray4 = new short[n2];
        Utils.getDecodedPolynomial(sArray4, this.privateKey.getPk(), n2, n3);
        short[] sArray5 = new short[n2];
        Utils.multiplicationInRQ(sArray5, sArray4, byArray6, n2, n3);
        short[] sArray6 = new short[n2];
        Utils.roundPolynomial(sArray6, sArray5);
        byte[] byArray8 = new byte[n5];
        Utils.getRoundedEncodedPolynomial(byArray8, sArray6, n2, n3);
        byte[] byArray9 = new byte[]{3};
        byte[] byArray10 = Utils.getHashWithPrefix(byArray9, byArray7);
        byte[] byArray11 = new byte[byArray10.length / 2 + this.privateKey.getHash().length];
        System.arraycopy(byArray10, 0, byArray11, 0, byArray10.length / 2);
        System.arraycopy(this.privateKey.getHash(), 0, byArray11, byArray10.length / 2, this.privateKey.getHash().length);
        byte[] byArray12 = new byte[]{2};
        byte[] byArray13 = Utils.getHashWithPrefix(byArray12, byArray11);
        byte[] byArray14 = new byte[byArray8.length + byArray13.length / 2];
        System.arraycopy(byArray8, 0, byArray14, 0, byArray8.length);
        System.arraycopy(byArray13, 0, byArray14, byArray8.length, byArray13.length / 2);
        int n6 = Arrays.areEqual(byArray, byArray14) ? 0 : -1;
        Utils.updateDiffMask(byArray7, this.privateKey.getRho(), n6);
        byte[] byArray15 = new byte[]{3};
        byte[] byArray16 = Utils.getHashWithPrefix(byArray15, byArray7);
        byte[] byArray17 = new byte[byArray16.length / 2 + byArray14.length];
        System.arraycopy(byArray16, 0, byArray17, 0, byArray16.length / 2);
        System.arraycopy(byArray14, 0, byArray17, byArray16.length / 2, byArray14.length);
        byte[] byArray18 = new byte[]{(byte)(n6 + 1)};
        byte[] byArray19 = Utils.getHashWithPrefix(byArray18, byArray17);
        return Arrays.copyOfRange(byArray19, 0, sNTRUPrimeParameters.getSessionKeySize() / 8);
    }

    @Override
    public int getEncapsulationLength() {
        return this.privateKey.getParameters().getRoundedPolynomialBytes() + 32;
    }
}

