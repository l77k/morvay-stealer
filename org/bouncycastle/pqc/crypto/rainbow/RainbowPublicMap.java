/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.rainbow;

import org.bouncycastle.pqc.crypto.rainbow.ComputeInField;
import org.bouncycastle.pqc.crypto.rainbow.GF2Field;
import org.bouncycastle.pqc.crypto.rainbow.RainbowDRBG;
import org.bouncycastle.pqc.crypto.rainbow.RainbowParameters;
import org.bouncycastle.pqc.crypto.rainbow.RainbowPublicKeyParameters;
import org.bouncycastle.pqc.crypto.rainbow.RainbowUtil;
import org.bouncycastle.util.Arrays;

class RainbowPublicMap {
    private ComputeInField cf = new ComputeInField();
    private RainbowParameters params;
    private final int num_gf_elements = 256;

    public RainbowPublicMap(RainbowParameters rainbowParameters) {
        this.params = rainbowParameters;
    }

    private short[][] compute_accumulator(short[] sArray, short[] sArray2, short[][][] sArray3, int n2) {
        short[][] sArray4 = new short[256][n2];
        if (sArray2.length != sArray3[0].length || sArray.length != sArray3[0][0].length || sArray3.length != n2) {
            throw new RuntimeException("Accumulator calculation not possible!");
        }
        for (int i2 = 0; i2 < sArray2.length; ++i2) {
            short[] sArray5 = this.cf.multVect(sArray2[i2], sArray);
            for (int i3 = 0; i3 < sArray.length; ++i3) {
                for (int i4 = 0; i4 < sArray3.length; ++i4) {
                    short s2 = sArray5[i3];
                    if (s2 == 0) continue;
                    sArray4[s2][i4] = GF2Field.addElem(sArray4[s2][i4], sArray3[i4][i2][i3]);
                }
            }
        }
        return sArray4;
    }

    private short[] add_and_reduce(short[][] sArray) {
        int n2 = this.params.getM();
        short[] sArray2 = new short[n2];
        for (int i2 = 0; i2 < 8; ++i2) {
            int n3 = (int)Math.pow(2.0, i2);
            short[] sArray3 = new short[n2];
            for (int i3 = n3; i3 < 256; i3 += n3 * 2) {
                for (int i4 = 0; i4 < n3; ++i4) {
                    sArray3 = this.cf.addVect(sArray3, sArray[i3 + i4]);
                }
            }
            sArray2 = this.cf.addVect(sArray2, this.cf.multVect((short)n3, sArray3));
        }
        return sArray2;
    }

    public short[] publicMap(RainbowPublicKeyParameters rainbowPublicKeyParameters, short[] sArray) {
        short[][] sArray2 = this.compute_accumulator(sArray, sArray, rainbowPublicKeyParameters.pk, this.params.getM());
        return this.add_and_reduce(sArray2);
    }

    public short[] publicMap_cyclic(RainbowPublicKeyParameters rainbowPublicKeyParameters, short[] sArray) {
        int n2 = this.params.getV1();
        int n3 = this.params.getO1();
        int n4 = this.params.getO2();
        short[][] sArray2 = new short[256][n3 + n4];
        short[] sArray3 = Arrays.copyOfRange(sArray, 0, n2);
        short[] sArray4 = Arrays.copyOfRange(sArray, n2, n2 + n3);
        short[] sArray5 = Arrays.copyOfRange(sArray, n2 + n3, sArray.length);
        RainbowDRBG rainbowDRBG = new RainbowDRBG(rainbowPublicKeyParameters.pk_seed, rainbowPublicKeyParameters.getParameters().getHash_algo());
        short[][][] sArray6 = RainbowUtil.generate_random(rainbowDRBG, n3, n2, n2, true);
        short[][] sArray7 = this.compute_accumulator(sArray3, sArray3, sArray6, n3);
        sArray6 = RainbowUtil.generate_random(rainbowDRBG, n3, n2, n3, false);
        sArray7 = this.cf.addMatrix(sArray7, this.compute_accumulator(sArray4, sArray3, sArray6, n3));
        sArray7 = this.cf.addMatrix(sArray7, this.compute_accumulator(sArray5, sArray3, rainbowPublicKeyParameters.l1_Q3, n3));
        sArray7 = this.cf.addMatrix(sArray7, this.compute_accumulator(sArray4, sArray4, rainbowPublicKeyParameters.l1_Q5, n3));
        sArray7 = this.cf.addMatrix(sArray7, this.compute_accumulator(sArray5, sArray4, rainbowPublicKeyParameters.l1_Q6, n3));
        sArray7 = this.cf.addMatrix(sArray7, this.compute_accumulator(sArray5, sArray5, rainbowPublicKeyParameters.l1_Q9, n3));
        sArray6 = RainbowUtil.generate_random(rainbowDRBG, n4, n2, n2, true);
        short[][] sArray8 = this.compute_accumulator(sArray3, sArray3, sArray6, n4);
        sArray6 = RainbowUtil.generate_random(rainbowDRBG, n4, n2, n3, false);
        sArray8 = this.cf.addMatrix(sArray8, this.compute_accumulator(sArray4, sArray3, sArray6, n4));
        sArray6 = RainbowUtil.generate_random(rainbowDRBG, n4, n2, n4, false);
        sArray8 = this.cf.addMatrix(sArray8, this.compute_accumulator(sArray5, sArray3, sArray6, n4));
        sArray6 = RainbowUtil.generate_random(rainbowDRBG, n4, n3, n3, true);
        sArray8 = this.cf.addMatrix(sArray8, this.compute_accumulator(sArray4, sArray4, sArray6, n4));
        sArray6 = RainbowUtil.generate_random(rainbowDRBG, n4, n3, n4, false);
        sArray8 = this.cf.addMatrix(sArray8, this.compute_accumulator(sArray5, sArray4, sArray6, n4));
        sArray8 = this.cf.addMatrix(sArray8, this.compute_accumulator(sArray5, sArray5, rainbowPublicKeyParameters.l2_Q9, n4));
        for (int i2 = 0; i2 < 256; ++i2) {
            sArray2[i2] = Arrays.concatenate(sArray7[i2], sArray8[i2]);
        }
        return this.add_and_reduce(sArray2);
    }
}

