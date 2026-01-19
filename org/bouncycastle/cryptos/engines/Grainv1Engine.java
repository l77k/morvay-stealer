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
import org.bouncycastle.crypto.params.ParametersWithIV;

public class Grainv1Engine
implements StreamCipher {
    private static final int STATE_SIZE = 5;
    private byte[] workingKey;
    private byte[] workingIV;
    private byte[] out;
    private int[] lfsr;
    private int[] nfsr;
    private int output;
    private int index = 2;
    private boolean initialised = false;

    @Override
    public String getAlgorithmName() {
        return "Grain v1";
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) throws IllegalArgumentException {
        if (!(cipherParameters instanceof ParametersWithIV)) {
            throw new IllegalArgumentException("Grain v1 init parameters must include an IV");
        }
        ParametersWithIV parametersWithIV = (ParametersWithIV)cipherParameters;
        byte[] byArray = parametersWithIV.getIV();
        if (byArray == null || byArray.length != 8) {
            throw new IllegalArgumentException("Grain v1 requires exactly 8 bytes of IV");
        }
        if (!(parametersWithIV.getParameters() instanceof KeyParameter)) {
            throw new IllegalArgumentException("Grain v1 init parameters must include a key");
        }
        KeyParameter keyParameter = (KeyParameter)parametersWithIV.getParameters();
        byte[] byArray2 = keyParameter.getKey();
        if (byArray2.length != 10) {
            throw new IllegalArgumentException("Grain v1 key must be 80 bits long");
        }
        this.workingIV = new byte[byArray2.length];
        this.workingKey = new byte[byArray2.length];
        this.lfsr = new int[5];
        this.nfsr = new int[5];
        this.out = new byte[2];
        System.arraycopy(byArray, 0, this.workingIV, 0, byArray.length);
        System.arraycopy(byArray2, 0, this.workingKey, 0, byArray2.length);
        CryptoServicesRegistrar.checkConstraints(new DefaultServiceProperties(this.getAlgorithmName(), 80, cipherParameters, Utils.getPurpose(bl)));
        this.reset();
    }

    private void initGrain() {
        for (int i2 = 0; i2 < 10; ++i2) {
            this.output = this.getOutput();
            this.nfsr = this.shift(this.nfsr, this.getOutputNFSR() ^ this.lfsr[0] ^ this.output);
            this.lfsr = this.shift(this.lfsr, this.getOutputLFSR() ^ this.output);
        }
        this.initialised = true;
    }

    private int getOutputNFSR() {
        int n2 = this.nfsr[0];
        int n3 = this.nfsr[0] >>> 9 | this.nfsr[1] << 7;
        int n4 = this.nfsr[0] >>> 14 | this.nfsr[1] << 2;
        int n5 = this.nfsr[0] >>> 15 | this.nfsr[1] << 1;
        int n6 = this.nfsr[1] >>> 5 | this.nfsr[2] << 11;
        int n7 = this.nfsr[1] >>> 12 | this.nfsr[2] << 4;
        int n8 = this.nfsr[2] >>> 1 | this.nfsr[3] << 15;
        int n9 = this.nfsr[2] >>> 5 | this.nfsr[3] << 11;
        int n10 = this.nfsr[2] >>> 13 | this.nfsr[3] << 3;
        int n11 = this.nfsr[3] >>> 4 | this.nfsr[4] << 12;
        int n12 = this.nfsr[3] >>> 12 | this.nfsr[4] << 4;
        int n13 = this.nfsr[3] >>> 14 | this.nfsr[4] << 2;
        int n14 = this.nfsr[3] >>> 15 | this.nfsr[4] << 1;
        return (n13 ^ n12 ^ n11 ^ n10 ^ n9 ^ n8 ^ n7 ^ n6 ^ n4 ^ n3 ^ n2 ^ n14 & n12 ^ n9 & n8 ^ n5 & n3 ^ n12 & n11 & n10 ^ n8 & n7 & n6 ^ n14 & n10 & n7 & n3 ^ n12 & n11 & n9 & n8 ^ n14 & n12 & n6 & n5 ^ n14 & n12 & n11 & n10 & n9 ^ n8 & n7 & n6 & n5 & n3 ^ n11 & n10 & n9 & n8 & n7 & n6) & 0xFFFF;
    }

    private int getOutputLFSR() {
        int n2 = this.lfsr[0];
        int n3 = this.lfsr[0] >>> 13 | this.lfsr[1] << 3;
        int n4 = this.lfsr[1] >>> 7 | this.lfsr[2] << 9;
        int n5 = this.lfsr[2] >>> 6 | this.lfsr[3] << 10;
        int n6 = this.lfsr[3] >>> 3 | this.lfsr[4] << 13;
        int n7 = this.lfsr[3] >>> 14 | this.lfsr[4] << 2;
        return (n2 ^ n3 ^ n4 ^ n5 ^ n6 ^ n7) & 0xFFFF;
    }

    private int getOutput() {
        int n2 = this.nfsr[0] >>> 1 | this.nfsr[1] << 15;
        int n3 = this.nfsr[0] >>> 2 | this.nfsr[1] << 14;
        int n4 = this.nfsr[0] >>> 4 | this.nfsr[1] << 12;
        int n5 = this.nfsr[0] >>> 10 | this.nfsr[1] << 6;
        int n6 = this.nfsr[1] >>> 15 | this.nfsr[2] << 1;
        int n7 = this.nfsr[2] >>> 11 | this.nfsr[3] << 5;
        int n8 = this.nfsr[3] >>> 8 | this.nfsr[4] << 8;
        int n9 = this.nfsr[3] >>> 15 | this.nfsr[4] << 1;
        int n10 = this.lfsr[0] >>> 3 | this.lfsr[1] << 13;
        int n11 = this.lfsr[1] >>> 9 | this.lfsr[2] << 7;
        int n12 = this.lfsr[2] >>> 14 | this.lfsr[3] << 2;
        int n13 = this.lfsr[4];
        return (n11 ^ n9 ^ n10 & n13 ^ n12 & n13 ^ n13 & n9 ^ n10 & n11 & n12 ^ n10 & n12 & n13 ^ n10 & n12 & n9 ^ n11 & n12 & n9 ^ n12 & n13 & n9 ^ n2 ^ n3 ^ n4 ^ n5 ^ n6 ^ n7 ^ n8) & 0xFFFF;
    }

    private int[] shift(int[] nArray, int n2) {
        nArray[0] = nArray[1];
        nArray[1] = nArray[2];
        nArray[2] = nArray[3];
        nArray[3] = nArray[4];
        nArray[4] = n2;
        return nArray;
    }

    private void setKey(byte[] byArray, byte[] byArray2) {
        byArray2[8] = -1;
        byArray2[9] = -1;
        this.workingKey = byArray;
        this.workingIV = byArray2;
        int n2 = 0;
        for (int i2 = 0; i2 < this.nfsr.length; ++i2) {
            this.nfsr[i2] = (this.workingKey[n2 + 1] << 8 | this.workingKey[n2] & 0xFF) & 0xFFFF;
            this.lfsr[i2] = (this.workingIV[n2 + 1] << 8 | this.workingIV[n2] & 0xFF) & 0xFFFF;
            n2 += 2;
        }
    }

    @Override
    public int processBytes(byte[] byArray, int n2, int n3, byte[] byArray2, int n4) throws DataLengthException {
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
            byArray2[n4 + i2] = (byte)(byArray[n2 + i2] ^ this.getKeyStream());
        }
        return n3;
    }

    @Override
    public void reset() {
        this.index = 2;
        this.setKey(this.workingKey, this.workingIV);
        this.initGrain();
    }

    private void oneRound() {
        this.output = this.getOutput();
        this.out[0] = (byte)this.output;
        this.out[1] = (byte)(this.output >> 8);
        this.nfsr = this.shift(this.nfsr, this.getOutputNFSR() ^ this.lfsr[0]);
        this.lfsr = this.shift(this.lfsr, this.getOutputLFSR());
    }

    @Override
    public byte returnByte(byte by) {
        if (!this.initialised) {
            throw new IllegalStateException(this.getAlgorithmName() + " not initialised");
        }
        return (byte)(by ^ this.getKeyStream());
    }

    private byte getKeyStream() {
        if (this.index > 1) {
            this.oneRound();
            this.index = 0;
        }
        return this.out[this.index++];
    }
}

