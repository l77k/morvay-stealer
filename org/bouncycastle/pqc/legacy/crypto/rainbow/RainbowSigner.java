/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.legacy.crypto.rainbow;

import java.security.SecureRandom;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.CryptoServicesRegistrar;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.pqc.crypto.MessageSigner;
import org.bouncycastle.pqc.legacy.crypto.rainbow.Layer;
import org.bouncycastle.pqc.legacy.crypto.rainbow.RainbowKeyParameters;
import org.bouncycastle.pqc.legacy.crypto.rainbow.RainbowPrivateKeyParameters;
import org.bouncycastle.pqc.legacy.crypto.rainbow.RainbowPublicKeyParameters;
import org.bouncycastle.pqc.legacy.crypto.rainbow.util.ComputeInField;
import org.bouncycastle.pqc.legacy.crypto.rainbow.util.GF2Field;

public class RainbowSigner
implements MessageSigner {
    private static final int MAXITS = 65536;
    private SecureRandom random;
    int signableDocumentLength;
    private short[] x;
    private ComputeInField cf = new ComputeInField();
    RainbowKeyParameters key;

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        if (bl) {
            if (cipherParameters instanceof ParametersWithRandom) {
                ParametersWithRandom parametersWithRandom = (ParametersWithRandom)cipherParameters;
                this.random = parametersWithRandom.getRandom();
                this.key = (RainbowPrivateKeyParameters)parametersWithRandom.getParameters();
            } else {
                this.random = CryptoServicesRegistrar.getSecureRandom();
                this.key = (RainbowPrivateKeyParameters)cipherParameters;
            }
        } else {
            this.key = (RainbowPublicKeyParameters)cipherParameters;
        }
        this.signableDocumentLength = this.key.getDocLength();
    }

    private short[] initSign(Layer[] layerArray, short[] sArray) {
        short[] sArray2 = new short[sArray.length];
        sArray2 = this.cf.addVect(((RainbowPrivateKeyParameters)this.key).getB1(), sArray);
        short[] sArray3 = this.cf.multiplyMatrix(((RainbowPrivateKeyParameters)this.key).getInvA1(), sArray2);
        for (int i2 = 0; i2 < layerArray[0].getVi(); ++i2) {
            this.x[i2] = (short)this.random.nextInt();
            this.x[i2] = (short)(this.x[i2] & 0xFF);
        }
        return sArray3;
    }

    @Override
    public byte[] generateSignature(byte[] byArray) {
        boolean bl;
        Layer[] layerArray = ((RainbowPrivateKeyParameters)this.key).getLayers();
        int n2 = layerArray.length;
        this.x = new short[((RainbowPrivateKeyParameters)this.key).getInvA2().length];
        byte[] byArray2 = new byte[layerArray[n2 - 1].getViNext()];
        short[] sArray = this.makeMessageRepresentative(byArray);
        int n3 = 0;
        do {
            bl = true;
            int n4 = 0;
            try {
                int n5;
                short[] sArray2 = this.initSign(layerArray, sArray);
                for (n5 = 0; n5 < n2; ++n5) {
                    int n6;
                    short[] sArray3 = new short[layerArray[n5].getOi()];
                    short[] sArray4 = new short[layerArray[n5].getOi()];
                    for (n6 = 0; n6 < layerArray[n5].getOi(); ++n6) {
                        sArray3[n6] = sArray2[n4];
                        ++n4;
                    }
                    sArray4 = this.cf.solveEquation(layerArray[n5].plugInVinegars(this.x), sArray3);
                    if (sArray4 == null) {
                        throw new Exception("LES is not solveable!");
                    }
                    for (n6 = 0; n6 < sArray4.length; ++n6) {
                        this.x[layerArray[n5].getVi() + n6] = sArray4[n6];
                    }
                }
                short[] sArray5 = this.cf.addVect(((RainbowPrivateKeyParameters)this.key).getB2(), this.x);
                short[] sArray6 = this.cf.multiplyMatrix(((RainbowPrivateKeyParameters)this.key).getInvA2(), sArray5);
                for (n5 = 0; n5 < byArray2.length; ++n5) {
                    byArray2[n5] = (byte)sArray6[n5];
                }
            }
            catch (Exception exception) {
                bl = false;
            }
        } while (!bl && ++n3 < 65536);
        if (n3 == 65536) {
            throw new IllegalStateException("unable to generate signature - LES not solvable");
        }
        return byArray2;
    }

    @Override
    public boolean verifySignature(byte[] byArray, byte[] byArray2) {
        short[] sArray = new short[byArray2.length];
        for (int i2 = 0; i2 < byArray2.length; ++i2) {
            short s2 = byArray2[i2];
            sArray[i2] = s2 = (short)(s2 & 0xFF);
        }
        short[] sArray2 = this.makeMessageRepresentative(byArray);
        short[] sArray3 = this.verifySignatureIntern(sArray);
        boolean bl = true;
        if (sArray2.length != sArray3.length) {
            return false;
        }
        for (int i3 = 0; i3 < sArray2.length; ++i3) {
            bl = bl && sArray2[i3] == sArray3[i3];
        }
        return bl;
    }

    private short[] verifySignatureIntern(short[] sArray) {
        short[][] sArray2 = ((RainbowPublicKeyParameters)this.key).getCoeffQuadratic();
        short[][] sArray3 = ((RainbowPublicKeyParameters)this.key).getCoeffSingular();
        short[] sArray4 = ((RainbowPublicKeyParameters)this.key).getCoeffScalar();
        short[] sArray5 = new short[sArray2.length];
        int n2 = sArray3[0].length;
        int n3 = 0;
        short s2 = 0;
        for (int i2 = 0; i2 < sArray2.length; ++i2) {
            n3 = 0;
            for (int i3 = 0; i3 < n2; ++i3) {
                for (int i4 = i3; i4 < n2; ++i4) {
                    s2 = GF2Field.multElem(sArray2[i2][n3], GF2Field.multElem(sArray[i3], sArray[i4]));
                    sArray5[i2] = GF2Field.addElem(sArray5[i2], s2);
                    ++n3;
                }
                s2 = GF2Field.multElem(sArray3[i2][i3], sArray[i3]);
                sArray5[i2] = GF2Field.addElem(sArray5[i2], s2);
            }
            sArray5[i2] = GF2Field.addElem(sArray5[i2], sArray4[i2]);
        }
        return sArray5;
    }

    private short[] makeMessageRepresentative(byte[] byArray) {
        short[] sArray = new short[this.signableDocumentLength];
        int n2 = 0;
        int n3 = 0;
        while (n3 < byArray.length) {
            sArray[n3] = byArray[n2];
            int n4 = n3++;
            sArray[n4] = (short)(sArray[n4] & 0xFF);
            ++n2;
            if (n3 < sArray.length) continue;
        }
        return sArray;
    }
}

