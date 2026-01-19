/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.rainbow;

import java.security.SecureRandom;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.CryptoServicesRegistrar;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.pqc.crypto.MessageSigner;
import org.bouncycastle.pqc.crypto.rainbow.ComputeInField;
import org.bouncycastle.pqc.crypto.rainbow.GF2Field;
import org.bouncycastle.pqc.crypto.rainbow.RainbowDRBG;
import org.bouncycastle.pqc.crypto.rainbow.RainbowKeyParameters;
import org.bouncycastle.pqc.crypto.rainbow.RainbowPrivateKeyParameters;
import org.bouncycastle.pqc.crypto.rainbow.RainbowPublicKeyParameters;
import org.bouncycastle.pqc.crypto.rainbow.RainbowPublicMap;
import org.bouncycastle.pqc.crypto.rainbow.RainbowUtil;
import org.bouncycastle.pqc.crypto.rainbow.Version;
import org.bouncycastle.util.Arrays;

public class RainbowSigner
implements MessageSigner {
    private static final int MAXITS = 65536;
    private SecureRandom random;
    int signableDocumentLength;
    private ComputeInField cf = new ComputeInField();
    private RainbowKeyParameters key;
    private Digest hashAlgo;
    private Version version;

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        if (bl) {
            RainbowKeyParameters rainbowKeyParameters;
            if (cipherParameters instanceof ParametersWithRandom) {
                ParametersWithRandom parametersWithRandom = (ParametersWithRandom)cipherParameters;
                this.random = parametersWithRandom.getRandom();
                rainbowKeyParameters = (RainbowKeyParameters)parametersWithRandom.getParameters();
            } else {
                rainbowKeyParameters = (RainbowKeyParameters)cipherParameters;
                SecureRandom secureRandom = CryptoServicesRegistrar.getSecureRandom();
                byte[] byArray = new byte[rainbowKeyParameters.getParameters().getLen_skseed()];
                secureRandom.nextBytes(byArray);
                this.random = new RainbowDRBG(byArray, rainbowKeyParameters.getParameters().getHash_algo());
            }
            this.version = rainbowKeyParameters.getParameters().getVersion();
            this.key = rainbowKeyParameters;
        } else {
            this.key = (RainbowKeyParameters)cipherParameters;
            this.version = this.key.getParameters().getVersion();
        }
        this.signableDocumentLength = this.key.getDocLength();
        this.hashAlgo = this.key.getParameters().getHash_algo();
    }

    private byte[] genSignature(byte[] byArray) {
        short[] sArray;
        short s2;
        int n2;
        int n3;
        int n4;
        int n5;
        byte[] byArray2 = new byte[this.hashAlgo.getDigestSize()];
        this.hashAlgo.update(byArray, 0, byArray.length);
        this.hashAlgo.doFinal(byArray2, 0);
        int n6 = this.key.getParameters().getV1();
        int n7 = this.key.getParameters().getO1();
        int n8 = this.key.getParameters().getO2();
        int n9 = this.key.getParameters().getM();
        int n10 = this.key.getParameters().getN();
        RainbowPrivateKeyParameters rainbowPrivateKeyParameters = (RainbowPrivateKeyParameters)this.key;
        byte[] byArray3 = RainbowUtil.hash(this.hashAlgo, rainbowPrivateKeyParameters.sk_seed, byArray2, new byte[this.hashAlgo.getDigestSize()]);
        this.random = new RainbowDRBG(byArray3, rainbowPrivateKeyParameters.getParameters().getHash_algo());
        short[] sArray2 = new short[n6];
        short[][] sArray3 = null;
        short[] sArray4 = new short[n7];
        short[] sArray5 = new short[n8];
        short[] sArray6 = new short[n8];
        short[][] sArray7 = new short[n8][n7];
        short[][] sArray8 = new short[n8][n8];
        byte[] byArray4 = new byte[rainbowPrivateKeyParameters.getParameters().getLen_salt()];
        short[] sArray9 = new short[n9];
        short[] sArray10 = new short[n7];
        short[] sArray11 = null;
        for (n5 = 0; sArray3 == null && n5 < 65536; ++n5) {
            byte[] byArray5 = new byte[n6];
            this.random.nextBytes(byArray5);
            for (n4 = 0; n4 < n6; ++n4) {
                sArray2[n4] = (short)(byArray5[n4] & 0xFF);
            }
            sArray3 = new short[n7][n7];
            for (n4 = 0; n4 < n6; ++n4) {
                for (n3 = 0; n3 < n7; ++n3) {
                    for (n2 = 0; n2 < n7; ++n2) {
                        s2 = GF2Field.multElem(rainbowPrivateKeyParameters.l1_F2[n3][n4][n2], sArray2[n4]);
                        sArray3[n3][n2] = GF2Field.addElem(sArray3[n3][n2], s2);
                    }
                }
            }
            sArray3 = this.cf.inverse(sArray3);
        }
        for (n4 = 0; n4 < n7; ++n4) {
            sArray4[n4] = this.cf.multiplyMatrix_quad(rainbowPrivateKeyParameters.l1_F1[n4], sArray2);
        }
        for (n4 = 0; n4 < n6; ++n4) {
            for (n3 = 0; n3 < n8; ++n3) {
                sArray5[n3] = this.cf.multiplyMatrix_quad(rainbowPrivateKeyParameters.l2_F1[n3], sArray2);
                for (n2 = 0; n2 < n7; ++n2) {
                    s2 = GF2Field.multElem(rainbowPrivateKeyParameters.l2_F2[n3][n4][n2], sArray2[n4]);
                    sArray7[n3][n2] = GF2Field.addElem(sArray7[n3][n2], s2);
                }
                for (n2 = 0; n2 < n8; ++n2) {
                    s2 = GF2Field.multElem(rainbowPrivateKeyParameters.l2_F3[n3][n4][n2], sArray2[n4]);
                    sArray8[n3][n2] = GF2Field.addElem(sArray8[n3][n2], s2);
                }
            }
        }
        byte[] byArray6 = new byte[n9];
        while (sArray11 == null && n5 < 65536) {
            short[][] sArray12 = new short[n8][n8];
            this.random.nextBytes(byArray4);
            byte[] byArray7 = RainbowUtil.hash(this.hashAlgo, byArray2, byArray4, byArray6);
            short[] sArray13 = this.makeMessageRepresentative(byArray7);
            sArray = this.cf.multiplyMatrix(rainbowPrivateKeyParameters.s1, Arrays.copyOfRange(sArray13, n7, n9));
            sArray = this.cf.addVect(Arrays.copyOf(sArray13, n7), sArray);
            System.arraycopy(sArray, 0, sArray9, 0, n7);
            System.arraycopy(sArray13, n7, sArray9, n7, n8);
            sArray = this.cf.addVect(sArray4, Arrays.copyOf(sArray9, n7));
            sArray10 = this.cf.multiplyMatrix(sArray3, sArray);
            sArray = this.cf.multiplyMatrix(sArray7, sArray10);
            for (n3 = 0; n3 < n8; ++n3) {
                sArray6[n3] = this.cf.multiplyMatrix_quad(rainbowPrivateKeyParameters.l2_F5[n3], sArray10);
            }
            sArray = this.cf.addVect(sArray, sArray6);
            sArray = this.cf.addVect(sArray, sArray5);
            sArray = this.cf.addVect(sArray, Arrays.copyOfRange(sArray9, n7, n9));
            for (n3 = 0; n3 < n7; ++n3) {
                for (n2 = 0; n2 < n8; ++n2) {
                    for (int i2 = 0; i2 < n8; ++i2) {
                        s2 = GF2Field.multElem(rainbowPrivateKeyParameters.l2_F6[n2][n3][i2], sArray10[n3]);
                        sArray12[n2][i2] = GF2Field.addElem(sArray12[n2][i2], s2);
                    }
                }
            }
            sArray12 = this.cf.addMatrix(sArray12, sArray8);
            sArray11 = this.cf.solveEquation(sArray12, sArray);
            ++n5;
        }
        sArray11 = sArray11 == null ? new short[n8] : sArray11;
        sArray = this.cf.multiplyMatrix(rainbowPrivateKeyParameters.t1, sArray10);
        short[] sArray14 = this.cf.addVect(sArray2, sArray);
        sArray = this.cf.multiplyMatrix(rainbowPrivateKeyParameters.t4, sArray11);
        sArray14 = this.cf.addVect(sArray14, sArray);
        sArray = this.cf.multiplyMatrix(rainbowPrivateKeyParameters.t3, sArray11);
        sArray = this.cf.addVect(sArray10, sArray);
        sArray14 = Arrays.copyOf(sArray14, n10);
        System.arraycopy(sArray, 0, sArray14, n6, n7);
        System.arraycopy(sArray11, 0, sArray14, n7 + n6, n8);
        if (n5 == 65536) {
            throw new IllegalStateException("unable to generate signature - LES not solvable");
        }
        byte[] byArray8 = RainbowUtil.convertArray(sArray14);
        return Arrays.concatenate(byArray8, byArray4);
    }

    @Override
    public byte[] generateSignature(byte[] byArray) {
        return this.genSignature(byArray);
    }

    @Override
    public boolean verifySignature(byte[] byArray, byte[] byArray2) {
        short[] sArray;
        byte[] byArray3 = new byte[this.hashAlgo.getDigestSize()];
        this.hashAlgo.update(byArray, 0, byArray.length);
        this.hashAlgo.doFinal(byArray3, 0);
        int n2 = this.key.getParameters().getM();
        int n3 = this.key.getParameters().getN();
        RainbowPublicMap rainbowPublicMap = new RainbowPublicMap(this.key.getParameters());
        byte[] byArray4 = Arrays.copyOfRange(byArray2, n3, byArray2.length);
        byte[] byArray5 = RainbowUtil.hash(this.hashAlgo, byArray3, byArray4, new byte[n2]);
        short[] sArray2 = this.makeMessageRepresentative(byArray5);
        byte[] byArray6 = Arrays.copyOfRange(byArray2, 0, n3);
        short[] sArray3 = RainbowUtil.convertArray(byArray6);
        switch (this.version) {
            case CLASSIC: {
                RainbowPublicKeyParameters rainbowPublicKeyParameters = (RainbowPublicKeyParameters)this.key;
                sArray = rainbowPublicMap.publicMap(rainbowPublicKeyParameters, sArray3);
                break;
            }
            case CIRCUMZENITHAL: 
            case COMPRESSED: {
                RainbowPublicKeyParameters rainbowPublicKeyParameters = (RainbowPublicKeyParameters)this.key;
                sArray = rainbowPublicMap.publicMap_cyclic(rainbowPublicKeyParameters, sArray3);
                break;
            }
            default: {
                throw new IllegalArgumentException("No valid version. Please choose one of the following: classic, circumzenithal, compressed");
            }
        }
        return RainbowUtil.equals(sArray2, sArray);
    }

    private short[] makeMessageRepresentative(byte[] byArray) {
        short[] sArray = new short[this.signableDocumentLength];
        int n2 = 0;
        int n3 = 0;
        while (n3 < byArray.length) {
            sArray[n3] = (short)(byArray[n2] & 0xFF);
            ++n2;
            if (++n3 < sArray.length) continue;
        }
        return sArray;
    }
}

