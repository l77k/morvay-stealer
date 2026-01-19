/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.rainbow;

import org.bouncycastle.pqc.crypto.rainbow.RainbowKeyParameters;
import org.bouncycastle.pqc.crypto.rainbow.RainbowParameters;
import org.bouncycastle.pqc.crypto.rainbow.RainbowUtil;
import org.bouncycastle.pqc.crypto.rainbow.Version;
import org.bouncycastle.util.Arrays;

public class RainbowPublicKeyParameters
extends RainbowKeyParameters {
    short[][][] pk;
    byte[] pk_seed;
    short[][][] l1_Q3;
    short[][][] l1_Q5;
    short[][][] l1_Q6;
    short[][][] l1_Q9;
    short[][][] l2_Q9;

    RainbowPublicKeyParameters(RainbowParameters rainbowParameters, short[][][] sArray, short[][][] sArray2, short[][][] sArray3, short[][][] sArray4, short[][][] sArray5, short[][][] sArray6, short[][][] sArray7, short[][][] sArray8, short[][][] sArray9, short[][][] sArray10, short[][][] sArray11, short[][][] sArray12) {
        super(false, rainbowParameters);
        int n2;
        int n3;
        int n4 = rainbowParameters.getV1();
        int n5 = rainbowParameters.getO1();
        int n6 = rainbowParameters.getO2();
        this.pk = new short[rainbowParameters.getM()][rainbowParameters.getN()][rainbowParameters.getN()];
        for (n3 = 0; n3 < n5; ++n3) {
            for (n2 = 0; n2 < n4; ++n2) {
                System.arraycopy(sArray[n3][n2], 0, this.pk[n3][n2], 0, n4);
                System.arraycopy(sArray2[n3][n2], 0, this.pk[n3][n2], n4, n5);
                System.arraycopy(sArray3[n3][n2], 0, this.pk[n3][n2], n4 + n5, n6);
            }
            for (n2 = 0; n2 < n5; ++n2) {
                System.arraycopy(sArray4[n3][n2], 0, this.pk[n3][n2 + n4], n4, n5);
                System.arraycopy(sArray5[n3][n2], 0, this.pk[n3][n2 + n4], n4 + n5, n6);
            }
            for (n2 = 0; n2 < n6; ++n2) {
                System.arraycopy(sArray6[n3][n2], 0, this.pk[n3][n2 + n4 + n5], n4 + n5, n6);
            }
        }
        for (n3 = 0; n3 < n6; ++n3) {
            for (n2 = 0; n2 < n4; ++n2) {
                System.arraycopy(sArray7[n3][n2], 0, this.pk[n3 + n5][n2], 0, n4);
                System.arraycopy(sArray8[n3][n2], 0, this.pk[n3 + n5][n2], n4, n5);
                System.arraycopy(sArray9[n3][n2], 0, this.pk[n3 + n5][n2], n4 + n5, n6);
            }
            for (n2 = 0; n2 < n5; ++n2) {
                System.arraycopy(sArray10[n3][n2], 0, this.pk[n3 + n5][n2 + n4], n4, n5);
                System.arraycopy(sArray11[n3][n2], 0, this.pk[n3 + n5][n2 + n4], n4 + n5, n6);
            }
            for (n2 = 0; n2 < n6; ++n2) {
                System.arraycopy(sArray12[n3][n2], 0, this.pk[n3 + n5][n2 + n4 + n5], n4 + n5, n6);
            }
        }
    }

    RainbowPublicKeyParameters(RainbowParameters rainbowParameters, byte[] byArray, short[][][] sArray, short[][][] sArray2, short[][][] sArray3, short[][][] sArray4, short[][][] sArray5) {
        super(false, rainbowParameters);
        this.pk_seed = (byte[])byArray.clone();
        this.l1_Q3 = RainbowUtil.cloneArray(sArray);
        this.l1_Q5 = RainbowUtil.cloneArray(sArray2);
        this.l1_Q6 = RainbowUtil.cloneArray(sArray3);
        this.l1_Q9 = RainbowUtil.cloneArray(sArray4);
        this.l2_Q9 = RainbowUtil.cloneArray(sArray5);
    }

    public RainbowPublicKeyParameters(RainbowParameters rainbowParameters, byte[] byArray) {
        super(false, rainbowParameters);
        int n2 = rainbowParameters.getM();
        int n3 = rainbowParameters.getN();
        if (this.getParameters().getVersion() == Version.CLASSIC) {
            this.pk = new short[n2][n3][n3];
            int n4 = 0;
            for (int i2 = 0; i2 < n3; ++i2) {
                for (int i3 = 0; i3 < n3; ++i3) {
                    for (int i4 = 0; i4 < n2; ++i4) {
                        if (i2 > i3) {
                            this.pk[i4][i2][i3] = 0;
                            continue;
                        }
                        this.pk[i4][i2][i3] = (short)(byArray[n4] & 0xFF);
                        ++n4;
                    }
                }
            }
        } else {
            this.pk_seed = Arrays.copyOfRange(byArray, 0, rainbowParameters.getLen_pkseed());
            this.l1_Q3 = new short[rainbowParameters.getO1()][rainbowParameters.getV1()][rainbowParameters.getO2()];
            this.l1_Q5 = new short[rainbowParameters.getO1()][rainbowParameters.getO1()][rainbowParameters.getO1()];
            this.l1_Q6 = new short[rainbowParameters.getO1()][rainbowParameters.getO1()][rainbowParameters.getO2()];
            this.l1_Q9 = new short[rainbowParameters.getO1()][rainbowParameters.getO2()][rainbowParameters.getO2()];
            this.l2_Q9 = new short[rainbowParameters.getO2()][rainbowParameters.getO2()][rainbowParameters.getO2()];
            int n5 = rainbowParameters.getLen_pkseed();
            n5 += RainbowUtil.loadEncoded(this.l1_Q3, byArray, n5, false);
            n5 += RainbowUtil.loadEncoded(this.l1_Q5, byArray, n5, true);
            n5 += RainbowUtil.loadEncoded(this.l1_Q6, byArray, n5, false);
            n5 += RainbowUtil.loadEncoded(this.l1_Q9, byArray, n5, true);
            if ((n5 += RainbowUtil.loadEncoded(this.l2_Q9, byArray, n5, true)) != byArray.length) {
                throw new IllegalArgumentException("unparsed data in key encoding");
            }
        }
    }

    public short[][][] getPk() {
        return RainbowUtil.cloneArray(this.pk);
    }

    public byte[] getEncoded() {
        if (this.getParameters().getVersion() != Version.CLASSIC) {
            byte[] byArray = this.pk_seed;
            byArray = Arrays.concatenate(byArray, RainbowUtil.getEncoded(this.l1_Q3, false));
            byArray = Arrays.concatenate(byArray, RainbowUtil.getEncoded(this.l1_Q5, true));
            byArray = Arrays.concatenate(byArray, RainbowUtil.getEncoded(this.l1_Q6, false));
            byArray = Arrays.concatenate(byArray, RainbowUtil.getEncoded(this.l1_Q9, true));
            byArray = Arrays.concatenate(byArray, RainbowUtil.getEncoded(this.l2_Q9, true));
            return byArray;
        }
        return RainbowUtil.getEncoded(this.pk, true);
    }
}

