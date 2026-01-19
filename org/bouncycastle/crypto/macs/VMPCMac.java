/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.macs;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

public class VMPCMac
implements Mac {
    private byte g;
    private byte n = 0;
    private byte[] P = null;
    private byte s = 0;
    private byte[] T;
    private byte[] workingIV;
    private byte[] workingKey;
    private byte x1;
    private byte x2;
    private byte x3;
    private byte x4;

    @Override
    public int doFinal(byte[] byArray, int n2) throws DataLengthException, IllegalStateException {
        int n3;
        int n4;
        for (n4 = 1; n4 < 25; ++n4) {
            this.s = this.P[this.s + this.P[this.n & 0xFF] & 0xFF];
            this.x4 = this.P[this.x4 + this.x3 + n4 & 0xFF];
            this.x3 = this.P[this.x3 + this.x2 + n4 & 0xFF];
            this.x2 = this.P[this.x2 + this.x1 + n4 & 0xFF];
            this.x1 = this.P[this.x1 + this.s + n4 & 0xFF];
            this.T[this.g & 0x1F] = (byte)(this.T[this.g & 0x1F] ^ this.x1);
            this.T[this.g + 1 & 0x1F] = (byte)(this.T[this.g + 1 & 0x1F] ^ this.x2);
            this.T[this.g + 2 & 0x1F] = (byte)(this.T[this.g + 2 & 0x1F] ^ this.x3);
            this.T[this.g + 3 & 0x1F] = (byte)(this.T[this.g + 3 & 0x1F] ^ this.x4);
            this.g = (byte)(this.g + 4 & 0x1F);
            n3 = this.P[this.n & 0xFF];
            this.P[this.n & 0xFF] = this.P[this.s & 0xFF];
            this.P[this.s & 0xFF] = n3;
            this.n = (byte)(this.n + 1 & 0xFF);
        }
        for (n4 = 0; n4 < 768; ++n4) {
            this.s = this.P[this.s + this.P[n4 & 0xFF] + this.T[n4 & 0x1F] & 0xFF];
            n3 = this.P[n4 & 0xFF];
            this.P[n4 & 0xFF] = this.P[this.s & 0xFF];
            this.P[this.s & 0xFF] = n3;
        }
        byte[] byArray2 = new byte[20];
        for (n3 = 0; n3 < 20; ++n3) {
            this.s = this.P[this.s + this.P[n3 & 0xFF] & 0xFF];
            byArray2[n3] = this.P[this.P[this.P[this.s & 0xFF] & 0xFF] + 1 & 0xFF];
            byte by = this.P[n3 & 0xFF];
            this.P[n3 & 0xFF] = this.P[this.s & 0xFF];
            this.P[this.s & 0xFF] = by;
        }
        System.arraycopy(byArray2, 0, byArray, n2, byArray2.length);
        this.reset();
        return byArray2.length;
    }

    @Override
    public String getAlgorithmName() {
        return "VMPC-MAC";
    }

    @Override
    public int getMacSize() {
        return 20;
    }

    @Override
    public void init(CipherParameters cipherParameters) throws IllegalArgumentException {
        if (!(cipherParameters instanceof ParametersWithIV)) {
            throw new IllegalArgumentException("VMPC-MAC Init parameters must include an IV");
        }
        ParametersWithIV parametersWithIV = (ParametersWithIV)cipherParameters;
        KeyParameter keyParameter = (KeyParameter)parametersWithIV.getParameters();
        if (!(parametersWithIV.getParameters() instanceof KeyParameter)) {
            throw new IllegalArgumentException("VMPC-MAC Init parameters must include a key");
        }
        this.workingIV = parametersWithIV.getIV();
        if (this.workingIV == null || this.workingIV.length < 1 || this.workingIV.length > 768) {
            throw new IllegalArgumentException("VMPC-MAC requires 1 to 768 bytes of IV");
        }
        this.workingKey = keyParameter.getKey();
        this.reset();
    }

    private void initKey(byte[] byArray, byte[] byArray2) {
        byte by;
        int n2;
        this.s = 0;
        this.P = new byte[256];
        for (n2 = 0; n2 < 256; ++n2) {
            this.P[n2] = (byte)n2;
        }
        for (n2 = 0; n2 < 768; ++n2) {
            this.s = this.P[this.s + this.P[n2 & 0xFF] + byArray[n2 % byArray.length] & 0xFF];
            by = this.P[n2 & 0xFF];
            this.P[n2 & 0xFF] = this.P[this.s & 0xFF];
            this.P[this.s & 0xFF] = by;
        }
        for (n2 = 0; n2 < 768; ++n2) {
            this.s = this.P[this.s + this.P[n2 & 0xFF] + byArray2[n2 % byArray2.length] & 0xFF];
            by = this.P[n2 & 0xFF];
            this.P[n2 & 0xFF] = this.P[this.s & 0xFF];
            this.P[this.s & 0xFF] = by;
        }
        this.n = 0;
    }

    @Override
    public void reset() {
        this.initKey(this.workingKey, this.workingIV);
        this.n = 0;
        this.x4 = 0;
        this.x3 = 0;
        this.x2 = 0;
        this.x1 = 0;
        this.g = 0;
        this.T = new byte[32];
        for (int i2 = 0; i2 < 32; ++i2) {
            this.T[i2] = 0;
        }
    }

    @Override
    public void update(byte by) throws IllegalStateException {
        this.s = this.P[this.s + this.P[this.n & 0xFF] & 0xFF];
        byte by2 = (byte)(by ^ this.P[this.P[this.P[this.s & 0xFF] & 0xFF] + 1 & 0xFF]);
        this.x4 = this.P[this.x4 + this.x3 & 0xFF];
        this.x3 = this.P[this.x3 + this.x2 & 0xFF];
        this.x2 = this.P[this.x2 + this.x1 & 0xFF];
        this.x1 = this.P[this.x1 + this.s + by2 & 0xFF];
        this.T[this.g & 0x1F] = (byte)(this.T[this.g & 0x1F] ^ this.x1);
        this.T[this.g + 1 & 0x1F] = (byte)(this.T[this.g + 1 & 0x1F] ^ this.x2);
        this.T[this.g + 2 & 0x1F] = (byte)(this.T[this.g + 2 & 0x1F] ^ this.x3);
        this.T[this.g + 3 & 0x1F] = (byte)(this.T[this.g + 3 & 0x1F] ^ this.x4);
        this.g = (byte)(this.g + 4 & 0x1F);
        byte by3 = this.P[this.n & 0xFF];
        this.P[this.n & 0xFF] = this.P[this.s & 0xFF];
        this.P[this.s & 0xFF] = by3;
        this.n = (byte)(this.n + 1 & 0xFF);
    }

    @Override
    public void update(byte[] byArray, int n2, int n3) throws DataLengthException, IllegalStateException {
        if (n2 + n3 > byArray.length) {
            throw new DataLengthException("input buffer too short");
        }
        for (int i2 = 0; i2 < n3; ++i2) {
            this.update(byArray[n2 + i2]);
        }
    }
}

