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

public class RC4Engine
implements StreamCipher {
    private static final int STATE_LENGTH = 256;
    private byte[] engineState = null;
    private int x = 0;
    private int y = 0;
    private byte[] workingKey = null;
    private boolean forEncryption;

    public RC4Engine() {
        CryptoServicesRegistrar.checkConstraints(new DefaultServiceProperties(this.getAlgorithmName(), 20));
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        if (cipherParameters instanceof KeyParameter) {
            this.workingKey = ((KeyParameter)cipherParameters).getKey();
            this.forEncryption = bl;
            this.setKey(this.workingKey);
            CryptoServicesRegistrar.checkConstraints(new DefaultServiceProperties(this.getAlgorithmName(), 20, cipherParameters, Utils.getPurpose(bl)));
            return;
        }
        throw new IllegalArgumentException("invalid parameter passed to RC4 init - " + cipherParameters.getClass().getName());
    }

    @Override
    public String getAlgorithmName() {
        return "RC4";
    }

    @Override
    public byte returnByte(byte by) {
        this.x = this.x + 1 & 0xFF;
        this.y = this.engineState[this.x] + this.y & 0xFF;
        byte by2 = this.engineState[this.x];
        this.engineState[this.x] = this.engineState[this.y];
        this.engineState[this.y] = by2;
        return (byte)(by ^ this.engineState[this.engineState[this.x] + this.engineState[this.y] & 0xFF]);
    }

    @Override
    public int processBytes(byte[] byArray, int n2, int n3, byte[] byArray2, int n4) {
        if (n2 + n3 > byArray.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n4 + n3 > byArray2.length) {
            throw new OutputLengthException("output buffer too short");
        }
        for (int i2 = 0; i2 < n3; ++i2) {
            this.x = this.x + 1 & 0xFF;
            this.y = this.engineState[this.x] + this.y & 0xFF;
            byte by = this.engineState[this.x];
            this.engineState[this.x] = this.engineState[this.y];
            this.engineState[this.y] = by;
            byArray2[i2 + n4] = (byte)(byArray[i2 + n2] ^ this.engineState[this.engineState[this.x] + this.engineState[this.y] & 0xFF]);
        }
        return n3;
    }

    @Override
    public void reset() {
        this.setKey(this.workingKey);
    }

    private void setKey(byte[] byArray) {
        int n2;
        this.workingKey = byArray;
        this.x = 0;
        this.y = 0;
        if (this.engineState == null) {
            this.engineState = new byte[256];
        }
        for (n2 = 0; n2 < 256; ++n2) {
            this.engineState[n2] = (byte)n2;
        }
        n2 = 0;
        int n3 = 0;
        for (int i2 = 0; i2 < 256; ++i2) {
            n3 = (byArray[n2] & 0xFF) + this.engineState[i2] + n3 & 0xFF;
            byte by = this.engineState[i2];
            this.engineState[i2] = this.engineState[n3];
            this.engineState[n3] = by;
            n2 = (n2 + 1) % byArray.length;
        }
    }
}

