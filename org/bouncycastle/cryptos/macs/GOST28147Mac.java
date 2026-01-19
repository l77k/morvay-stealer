/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.macs;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.CryptoServicePurpose;
import org.bouncycastle.crypto.CryptoServicesRegistrar;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.constraints.DefaultServiceProperties;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.crypto.params.ParametersWithSBox;
import org.bouncycastle.util.Pack;

public class GOST28147Mac
implements Mac {
    private final CryptoServicePurpose purpose;
    private static final int BLOCK_SIZE = 8;
    private static final int MAC_SIZE = 4;
    private int bufOff;
    private byte[] buf;
    private byte[] mac;
    private boolean firstStep = true;
    private int[] workingKey = null;
    private byte[] macIV = null;
    private byte[] S = new byte[]{9, 6, 3, 2, 8, 11, 1, 7, 10, 4, 14, 15, 12, 0, 13, 5, 3, 7, 14, 9, 8, 10, 15, 0, 5, 2, 6, 12, 11, 4, 13, 1, 14, 4, 6, 2, 11, 3, 13, 8, 12, 15, 5, 10, 0, 7, 1, 9, 14, 7, 10, 12, 13, 1, 3, 9, 0, 2, 11, 4, 15, 8, 5, 6, 11, 5, 1, 9, 8, 13, 15, 0, 14, 4, 2, 3, 12, 7, 10, 6, 3, 10, 13, 12, 1, 2, 0, 11, 7, 5, 9, 4, 8, 15, 14, 6, 1, 13, 2, 9, 7, 10, 6, 0, 8, 12, 4, 5, 15, 3, 11, 14, 11, 10, 15, 5, 0, 12, 14, 8, 6, 2, 3, 9, 1, 7, 13, 4};

    public GOST28147Mac() {
        this(CryptoServicePurpose.AUTHENTICATION);
    }

    public GOST28147Mac(CryptoServicePurpose cryptoServicePurpose) {
        this.purpose = cryptoServicePurpose;
        this.mac = new byte[8];
        this.buf = new byte[8];
        this.bufOff = 0;
    }

    private int[] generateWorkingKey(byte[] byArray) {
        if (byArray.length != 32) {
            throw new IllegalArgumentException("Key length invalid. Key needs to be 32 byte - 256 bit!!!");
        }
        int[] nArray = new int[8];
        for (int i2 = 0; i2 != 8; ++i2) {
            nArray[i2] = Pack.littleEndianToInt(byArray, i2 * 4);
        }
        return nArray;
    }

    @Override
    public void init(CipherParameters cipherParameters) throws IllegalArgumentException {
        this.reset();
        this.buf = new byte[8];
        this.macIV = null;
        this.recursiveInit(cipherParameters);
        CryptoServicesRegistrar.checkConstraints(new DefaultServiceProperties(this.getAlgorithmName(), 178, cipherParameters, this.purpose));
    }

    private void recursiveInit(CipherParameters cipherParameters) throws IllegalArgumentException {
        if (cipherParameters == null) {
            return;
        }
        CipherParameters cipherParameters2 = null;
        if (cipherParameters instanceof ParametersWithSBox) {
            ParametersWithSBox parametersWithSBox = (ParametersWithSBox)cipherParameters;
            System.arraycopy(parametersWithSBox.getSBox(), 0, this.S, 0, parametersWithSBox.getSBox().length);
            cipherParameters2 = parametersWithSBox.getParameters();
        } else if (cipherParameters instanceof KeyParameter) {
            this.workingKey = this.generateWorkingKey(((KeyParameter)cipherParameters).getKey());
        } else if (cipherParameters instanceof ParametersWithIV) {
            ParametersWithIV parametersWithIV = (ParametersWithIV)cipherParameters;
            System.arraycopy(parametersWithIV.getIV(), 0, this.mac, 0, this.mac.length);
            this.macIV = parametersWithIV.getIV();
            cipherParameters2 = parametersWithIV.getParameters();
        } else {
            throw new IllegalArgumentException("invalid parameter passed to GOST28147 init - " + cipherParameters.getClass().getName());
        }
        this.recursiveInit(cipherParameters2);
    }

    @Override
    public String getAlgorithmName() {
        return "GOST28147Mac";
    }

    @Override
    public int getMacSize() {
        return 4;
    }

    private int gost28147_mainStep(int n2, int n3) {
        int n4 = n3 + n2;
        int n5 = this.S[0 + (n4 >> 0 & 0xF)] << 0;
        n5 += this.S[16 + (n4 >> 4 & 0xF)] << 4;
        n5 += this.S[32 + (n4 >> 8 & 0xF)] << 8;
        n5 += this.S[48 + (n4 >> 12 & 0xF)] << 12;
        n5 += this.S[64 + (n4 >> 16 & 0xF)] << 16;
        n5 += this.S[80 + (n4 >> 20 & 0xF)] << 20;
        n5 += this.S[96 + (n4 >> 24 & 0xF)] << 24;
        return (n5 += this.S[112 + (n4 >> 28 & 0xF)] << 28) << 11 | n5 >>> 21;
    }

    private void gost28147MacFunc(int[] nArray, byte[] byArray, int n2, byte[] byArray2, int n3) {
        int n4 = Pack.littleEndianToInt(byArray, n2);
        int n5 = Pack.littleEndianToInt(byArray, n2 + 4);
        for (int i2 = 0; i2 < 2; ++i2) {
            for (int i3 = 0; i3 < 8; ++i3) {
                int n6 = n4;
                n4 = n5 ^ this.gost28147_mainStep(n4, nArray[i3]);
                n5 = n6;
            }
        }
        Pack.intToLittleEndian(n4, byArray2, n3);
        Pack.intToLittleEndian(n5, byArray2, n3 + 4);
    }

    @Override
    public void update(byte by) throws IllegalStateException {
        if (this.bufOff == this.buf.length) {
            byte[] byArray = new byte[this.buf.length];
            if (this.firstStep) {
                this.firstStep = false;
                if (this.macIV != null) {
                    GOST28147Mac.CM5func(this.buf, 0, this.macIV, byArray);
                } else {
                    System.arraycopy(this.buf, 0, byArray, 0, this.mac.length);
                }
            } else {
                GOST28147Mac.CM5func(this.buf, 0, this.mac, byArray);
            }
            this.gost28147MacFunc(this.workingKey, byArray, 0, this.mac, 0);
            this.bufOff = 0;
        }
        this.buf[this.bufOff++] = by;
    }

    @Override
    public void update(byte[] byArray, int n2, int n3) throws DataLengthException, IllegalStateException {
        if (n3 < 0) {
            throw new IllegalArgumentException("Can't have a negative input length!");
        }
        int n4 = 8 - this.bufOff;
        if (n3 > n4) {
            System.arraycopy(byArray, n2, this.buf, this.bufOff, n4);
            byte[] byArray2 = new byte[this.buf.length];
            if (this.firstStep) {
                this.firstStep = false;
                if (this.macIV != null) {
                    GOST28147Mac.CM5func(this.buf, 0, this.macIV, byArray2);
                } else {
                    System.arraycopy(this.buf, 0, byArray2, 0, this.mac.length);
                }
            } else {
                GOST28147Mac.CM5func(this.buf, 0, this.mac, byArray2);
            }
            this.gost28147MacFunc(this.workingKey, byArray2, 0, this.mac, 0);
            this.bufOff = 0;
            n3 -= n4;
            n2 += n4;
            while (n3 > 8) {
                GOST28147Mac.CM5func(byArray, n2, this.mac, byArray2);
                this.gost28147MacFunc(this.workingKey, byArray2, 0, this.mac, 0);
                n3 -= 8;
                n2 += 8;
            }
        }
        System.arraycopy(byArray, n2, this.buf, this.bufOff, n3);
        this.bufOff += n3;
    }

    @Override
    public int doFinal(byte[] byArray, int n2) throws DataLengthException, IllegalStateException {
        while (this.bufOff < 8) {
            this.buf[this.bufOff] = 0;
            ++this.bufOff;
        }
        byte[] byArray2 = new byte[this.buf.length];
        if (this.firstStep) {
            this.firstStep = false;
            System.arraycopy(this.buf, 0, byArray2, 0, this.mac.length);
        } else {
            GOST28147Mac.CM5func(this.buf, 0, this.mac, byArray2);
        }
        this.gost28147MacFunc(this.workingKey, byArray2, 0, this.mac, 0);
        System.arraycopy(this.mac, this.mac.length / 2 - 4, byArray, n2, 4);
        this.reset();
        return 4;
    }

    @Override
    public void reset() {
        for (int i2 = 0; i2 < this.buf.length; ++i2) {
            this.buf[i2] = 0;
        }
        this.bufOff = 0;
        this.firstStep = true;
    }

    private static void CM5func(byte[] byArray, int n2, byte[] byArray2, byte[] byArray3) {
        for (int i2 = 0; i2 < 8; ++i2) {
            byArray3[i2] = (byte)(byArray[n2 + i2] ^ byArray2[i2]);
        }
    }
}

