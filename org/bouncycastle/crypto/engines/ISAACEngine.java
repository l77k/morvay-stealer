/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.CryptoServicesRegistrar;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.StreamCipher;
import org.bouncycastle.crypto.constraints.DefaultServiceProperties;
import org.bouncycastle.crypto.engines.Utils;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.Pack;

public class ISAACEngine
implements StreamCipher {
    private final int sizeL = 8;
    private final int stateArraySize = 256;
    private int[] engineState = null;
    private int[] results = null;
    private int a = 0;
    private int b = 0;
    private int c = 0;
    private int index = 0;
    private byte[] keyStream = new byte[1024];
    private byte[] workingKey = null;
    private boolean initialised = false;

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        if (!(cipherParameters instanceof KeyParameter)) {
            throw new IllegalArgumentException("invalid parameter passed to ISAAC init - " + cipherParameters.getClass().getName());
        }
        KeyParameter keyParameter = (KeyParameter)cipherParameters;
        byte[] byArray = keyParameter.getKey();
        this.setKey(byArray);
        CryptoServicesRegistrar.checkConstraints(new DefaultServiceProperties(this.getAlgorithmName(), byArray.length < 32 ? byArray.length * 8 : 256, cipherParameters, Utils.getPurpose(bl)));
    }

    @Override
    public byte returnByte(byte by) {
        if (this.index == 0) {
            this.isaac();
            this.keyStream = Pack.intToBigEndian(this.results);
        }
        byte by2 = (byte)(this.keyStream[this.index] ^ by);
        this.index = this.index + 1 & 0x3FF;
        return by2;
    }

    @Override
    public int processBytes(byte[] byArray, int n2, int n3, byte[] byArray2, int n4) {
        if (!this.initialised) {
            throw new IllegalStateException(this.getAlgorithmName() + " not initialised");
        }
        if (n2 + n3 > byArray.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n4 + n3 > byArray2.length) {
            throw new OutputLengthException("output buffer too short");
        }
        for (int i2 = 0; i2 < n3; ++i2) {
            if (this.index == 0) {
                this.isaac();
                this.keyStream = Pack.intToBigEndian(this.results);
            }
            byArray2[i2 + n4] = (byte)(this.keyStream[this.index] ^ byArray[i2 + n2]);
            this.index = this.index + 1 & 0x3FF;
        }
        return n3;
    }

    @Override
    public String getAlgorithmName() {
        return "ISAAC";
    }

    @Override
    public void reset() {
        this.setKey(this.workingKey);
    }

    private void setKey(byte[] byArray) {
        int n2;
        this.workingKey = byArray;
        if (this.engineState == null) {
            this.engineState = new int[256];
        }
        if (this.results == null) {
            this.results = new int[256];
        }
        for (n2 = 0; n2 < 256; ++n2) {
            this.results[n2] = 0;
            this.engineState[n2] = 0;
        }
        this.c = 0;
        this.b = 0;
        this.a = 0;
        this.index = 0;
        byte[] byArray2 = new byte[byArray.length + (byArray.length & 3)];
        System.arraycopy(byArray, 0, byArray2, 0, byArray.length);
        for (n2 = 0; n2 < byArray2.length; n2 += 4) {
            this.results[n2 >>> 2] = Pack.littleEndianToInt(byArray2, n2);
        }
        int[] nArray = new int[8];
        for (n2 = 0; n2 < 8; ++n2) {
            nArray[n2] = -1640531527;
        }
        for (n2 = 0; n2 < 4; ++n2) {
            this.mix(nArray);
        }
        for (n2 = 0; n2 < 2; ++n2) {
            for (int i2 = 0; i2 < 256; i2 += 8) {
                int n3;
                for (n3 = 0; n3 < 8; ++n3) {
                    int n4 = n3;
                    nArray[n4] = nArray[n4] + (n2 < 1 ? this.results[i2 + n3] : this.engineState[i2 + n3]);
                }
                this.mix(nArray);
                for (n3 = 0; n3 < 8; ++n3) {
                    this.engineState[i2 + n3] = nArray[n3];
                }
            }
        }
        this.isaac();
        this.initialised = true;
    }

    private void isaac() {
        this.b += ++this.c;
        for (int i2 = 0; i2 < 256; ++i2) {
            int n2;
            int n3 = this.engineState[i2];
            switch (i2 & 3) {
                case 0: {
                    this.a ^= this.a << 13;
                    break;
                }
                case 1: {
                    this.a ^= this.a >>> 6;
                    break;
                }
                case 2: {
                    this.a ^= this.a << 2;
                    break;
                }
                case 3: {
                    this.a ^= this.a >>> 16;
                }
            }
            this.a += this.engineState[i2 + 128 & 0xFF];
            this.engineState[i2] = n2 = this.engineState[n3 >>> 2 & 0xFF] + this.a + this.b;
            this.results[i2] = this.b = this.engineState[n2 >>> 10 & 0xFF] + n3;
        }
    }

    private void mix(int[] nArray) {
        nArray[0] = nArray[0] ^ nArray[1] << 11;
        nArray[3] = nArray[3] + nArray[0];
        nArray[1] = nArray[1] + nArray[2];
        nArray[1] = nArray[1] ^ nArray[2] >>> 2;
        nArray[4] = nArray[4] + nArray[1];
        nArray[2] = nArray[2] + nArray[3];
        nArray[2] = nArray[2] ^ nArray[3] << 8;
        nArray[5] = nArray[5] + nArray[2];
        nArray[3] = nArray[3] + nArray[4];
        nArray[3] = nArray[3] ^ nArray[4] >>> 16;
        nArray[6] = nArray[6] + nArray[3];
        nArray[4] = nArray[4] + nArray[5];
        nArray[4] = nArray[4] ^ nArray[5] << 10;
        nArray[7] = nArray[7] + nArray[4];
        nArray[5] = nArray[5] + nArray[6];
        nArray[5] = nArray[5] ^ nArray[6] >>> 4;
        nArray[0] = nArray[0] + nArray[5];
        nArray[6] = nArray[6] + nArray[7];
        nArray[6] = nArray[6] ^ nArray[7] << 8;
        nArray[1] = nArray[1] + nArray[6];
        nArray[7] = nArray[7] + nArray[0];
        nArray[7] = nArray[7] ^ nArray[0] >>> 9;
        nArray[2] = nArray[2] + nArray[7];
        nArray[0] = nArray[0] + nArray[1];
    }
}

