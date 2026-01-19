/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.legacy.crypto.rainbow;

import java.security.SecureRandom;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.CryptoServicesRegistrar;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.pqc.legacy.crypto.rainbow.Layer;
import org.bouncycastle.pqc.legacy.crypto.rainbow.RainbowKeyGenerationParameters;
import org.bouncycastle.pqc.legacy.crypto.rainbow.RainbowParameters;
import org.bouncycastle.pqc.legacy.crypto.rainbow.RainbowPrivateKeyParameters;
import org.bouncycastle.pqc.legacy.crypto.rainbow.RainbowPublicKeyParameters;
import org.bouncycastle.pqc.legacy.crypto.rainbow.util.ComputeInField;
import org.bouncycastle.pqc.legacy.crypto.rainbow.util.GF2Field;

public class RainbowKeyPairGenerator
implements AsymmetricCipherKeyPairGenerator {
    private boolean initialized = false;
    private SecureRandom sr;
    private RainbowKeyGenerationParameters rainbowParams;
    private short[][] A1;
    private short[][] A1inv;
    private short[] b1;
    private short[][] A2;
    private short[][] A2inv;
    private short[] b2;
    private int numOfLayers;
    private Layer[] layers;
    private int[] vi;
    private short[][] pub_quadratic;
    private short[][] pub_singular;
    private short[] pub_scalar;

    public AsymmetricCipherKeyPair genKeyPair() {
        if (!this.initialized) {
            this.initializeDefault();
        }
        this.keygen();
        RainbowPrivateKeyParameters rainbowPrivateKeyParameters = new RainbowPrivateKeyParameters(this.A1inv, this.b1, this.A2inv, this.b2, this.vi, this.layers);
        RainbowPublicKeyParameters rainbowPublicKeyParameters = new RainbowPublicKeyParameters(this.vi[this.vi.length - 1] - this.vi[0], this.pub_quadratic, this.pub_singular, this.pub_scalar);
        return new AsymmetricCipherKeyPair(rainbowPublicKeyParameters, rainbowPrivateKeyParameters);
    }

    public void initialize(KeyGenerationParameters keyGenerationParameters) {
        this.rainbowParams = (RainbowKeyGenerationParameters)keyGenerationParameters;
        this.sr = this.rainbowParams.getRandom();
        this.vi = this.rainbowParams.getParameters().getVi();
        this.numOfLayers = this.rainbowParams.getParameters().getNumOfLayers();
        this.initialized = true;
    }

    private void initializeDefault() {
        RainbowKeyGenerationParameters rainbowKeyGenerationParameters = new RainbowKeyGenerationParameters(CryptoServicesRegistrar.getSecureRandom(), new RainbowParameters());
        this.initialize(rainbowKeyGenerationParameters);
    }

    private void keygen() {
        this.generateL1();
        this.generateL2();
        this.generateF();
        this.computePublicKey();
    }

    private void generateL1() {
        int n2;
        int n3 = this.vi[this.vi.length - 1] - this.vi[0];
        this.A1 = new short[n3][n3];
        this.A1inv = null;
        ComputeInField computeInField = new ComputeInField();
        while (this.A1inv == null) {
            for (n2 = 0; n2 < n3; ++n2) {
                for (int i2 = 0; i2 < n3; ++i2) {
                    this.A1[n2][i2] = (short)(this.sr.nextInt() & 0xFF);
                }
            }
            this.A1inv = computeInField.inverse(this.A1);
        }
        this.b1 = new short[n3];
        for (n2 = 0; n2 < n3; ++n2) {
            this.b1[n2] = (short)(this.sr.nextInt() & 0xFF);
        }
    }

    private void generateL2() {
        int n2;
        int n3 = this.vi[this.vi.length - 1];
        this.A2 = new short[n3][n3];
        this.A2inv = null;
        ComputeInField computeInField = new ComputeInField();
        while (this.A2inv == null) {
            for (n2 = 0; n2 < n3; ++n2) {
                for (int i2 = 0; i2 < n3; ++i2) {
                    this.A2[n2][i2] = (short)(this.sr.nextInt() & 0xFF);
                }
            }
            this.A2inv = computeInField.inverse(this.A2);
        }
        this.b2 = new short[n3];
        for (n2 = 0; n2 < n3; ++n2) {
            this.b2[n2] = (short)(this.sr.nextInt() & 0xFF);
        }
    }

    private void generateF() {
        this.layers = new Layer[this.numOfLayers];
        for (int i2 = 0; i2 < this.numOfLayers; ++i2) {
            this.layers[i2] = new Layer(this.vi[i2], this.vi[i2 + 1], this.sr);
        }
    }

    private void computePublicKey() {
        int n2;
        ComputeInField computeInField = new ComputeInField();
        int n3 = this.vi[this.vi.length - 1] - this.vi[0];
        int n4 = this.vi[this.vi.length - 1];
        short[][][] sArray = new short[n3][n4][n4];
        this.pub_singular = new short[n3][n4];
        this.pub_scalar = new short[n3];
        int n5 = 0;
        int n6 = 0;
        int n7 = 0;
        short[] sArray2 = new short[n4];
        short s2 = 0;
        for (int i2 = 0; i2 < this.layers.length; ++i2) {
            short[][][] sArray3 = this.layers[i2].getCoeffAlpha();
            short[][][] sArray4 = this.layers[i2].getCoeffBeta();
            short[][] sArray5 = this.layers[i2].getCoeffGamma();
            short[] sArray6 = this.layers[i2].getCoeffEta();
            n5 = sArray3[0].length;
            n6 = sArray4[0].length;
            for (int i3 = 0; i3 < n5; ++i3) {
                int n8;
                for (n8 = 0; n8 < n5; ++n8) {
                    for (n2 = 0; n2 < n6; ++n2) {
                        sArray2 = computeInField.multVect(sArray3[i3][n8][n2], this.A2[n8 + n6]);
                        sArray[n7 + i3] = computeInField.addSquareMatrix(sArray[n7 + i3], computeInField.multVects(sArray2, this.A2[n2]));
                        sArray2 = computeInField.multVect(this.b2[n2], sArray2);
                        this.pub_singular[n7 + i3] = computeInField.addVect(sArray2, this.pub_singular[n7 + i3]);
                        sArray2 = computeInField.multVect(sArray3[i3][n8][n2], this.A2[n2]);
                        sArray2 = computeInField.multVect(this.b2[n8 + n6], sArray2);
                        this.pub_singular[n7 + i3] = computeInField.addVect(sArray2, this.pub_singular[n7 + i3]);
                        s2 = GF2Field.multElem(sArray3[i3][n8][n2], this.b2[n8 + n6]);
                        this.pub_scalar[n7 + i3] = GF2Field.addElem(this.pub_scalar[n7 + i3], GF2Field.multElem(s2, this.b2[n2]));
                    }
                }
                for (n8 = 0; n8 < n6; ++n8) {
                    for (n2 = 0; n2 < n6; ++n2) {
                        sArray2 = computeInField.multVect(sArray4[i3][n8][n2], this.A2[n8]);
                        sArray[n7 + i3] = computeInField.addSquareMatrix(sArray[n7 + i3], computeInField.multVects(sArray2, this.A2[n2]));
                        sArray2 = computeInField.multVect(this.b2[n2], sArray2);
                        this.pub_singular[n7 + i3] = computeInField.addVect(sArray2, this.pub_singular[n7 + i3]);
                        sArray2 = computeInField.multVect(sArray4[i3][n8][n2], this.A2[n2]);
                        sArray2 = computeInField.multVect(this.b2[n8], sArray2);
                        this.pub_singular[n7 + i3] = computeInField.addVect(sArray2, this.pub_singular[n7 + i3]);
                        s2 = GF2Field.multElem(sArray4[i3][n8][n2], this.b2[n8]);
                        this.pub_scalar[n7 + i3] = GF2Field.addElem(this.pub_scalar[n7 + i3], GF2Field.multElem(s2, this.b2[n2]));
                    }
                }
                for (n8 = 0; n8 < n6 + n5; ++n8) {
                    sArray2 = computeInField.multVect(sArray5[i3][n8], this.A2[n8]);
                    this.pub_singular[n7 + i3] = computeInField.addVect(sArray2, this.pub_singular[n7 + i3]);
                    this.pub_scalar[n7 + i3] = GF2Field.addElem(this.pub_scalar[n7 + i3], GF2Field.multElem(sArray5[i3][n8], this.b2[n8]));
                }
                this.pub_scalar[n7 + i3] = GF2Field.addElem(this.pub_scalar[n7 + i3], sArray6[i3]);
            }
            n7 += n5;
        }
        short[][][] sArray7 = new short[n3][n4][n4];
        short[][] sArray8 = new short[n3][n4];
        short[] sArray9 = new short[n3];
        for (n2 = 0; n2 < n3; ++n2) {
            for (int i4 = 0; i4 < this.A1.length; ++i4) {
                sArray7[n2] = computeInField.addSquareMatrix(sArray7[n2], computeInField.multMatrix(this.A1[n2][i4], sArray[i4]));
                sArray8[n2] = computeInField.addVect(sArray8[n2], computeInField.multVect(this.A1[n2][i4], this.pub_singular[i4]));
                sArray9[n2] = GF2Field.addElem(sArray9[n2], GF2Field.multElem(this.A1[n2][i4], this.pub_scalar[i4]));
            }
            sArray9[n2] = GF2Field.addElem(sArray9[n2], this.b1[n2]);
        }
        sArray = sArray7;
        this.pub_singular = sArray8;
        this.pub_scalar = sArray9;
        this.compactPublicKey(sArray);
    }

    private void compactPublicKey(short[][][] sArray) {
        int n2 = sArray.length;
        int n3 = sArray[0].length;
        int n4 = n3 * (n3 + 1) / 2;
        this.pub_quadratic = new short[n2][n4];
        int n5 = 0;
        for (int i2 = 0; i2 < n2; ++i2) {
            n5 = 0;
            for (int i3 = 0; i3 < n3; ++i3) {
                for (int i4 = i3; i4 < n3; ++i4) {
                    this.pub_quadratic[i2][n5] = i4 == i3 ? sArray[i2][i3][i4] : GF2Field.addElem(sArray[i2][i3][i4], sArray[i2][i4][i3]);
                    ++n5;
                }
            }
        }
    }

    @Override
    public void init(KeyGenerationParameters keyGenerationParameters) {
        this.initialize(keyGenerationParameters);
    }

    @Override
    public AsymmetricCipherKeyPair generateKeyPair() {
        return this.genKeyPair();
    }
}

