/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.CryptoServicesRegistrar;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.constraints.DefaultServiceProperties;
import org.bouncycastle.crypto.engines.Utils;
import org.bouncycastle.crypto.params.KeyParameter;

public class TEAEngine
implements BlockCipher {
    private static final int rounds = 32;
    private static final int block_size = 8;
    private static final int delta = -1640531527;
    private static final int d_sum = -957401312;
    private int _a;
    private int _b;
    private int _c;
    private int _d;
    private boolean _initialised = false;
    private boolean _forEncryption;

    @Override
    public String getAlgorithmName() {
        return "TEA";
    }

    @Override
    public int getBlockSize() {
        return 8;
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        if (!(cipherParameters instanceof KeyParameter)) {
            throw new IllegalArgumentException("invalid parameter passed to TEA init - " + cipherParameters.getClass().getName());
        }
        this._forEncryption = bl;
        this._initialised = true;
        KeyParameter keyParameter = (KeyParameter)cipherParameters;
        this.setKey(keyParameter.getKey());
        CryptoServicesRegistrar.checkConstraints(new DefaultServiceProperties(this.getAlgorithmName(), 128, cipherParameters, Utils.getPurpose(bl)));
    }

    @Override
    public int processBlock(byte[] byArray, int n2, byte[] byArray2, int n3) {
        if (!this._initialised) {
            throw new IllegalStateException(this.getAlgorithmName() + " not initialised");
        }
        if (n2 + 8 > byArray.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n3 + 8 > byArray2.length) {
            throw new OutputLengthException("output buffer too short");
        }
        return this._forEncryption ? this.encryptBlock(byArray, n2, byArray2, n3) : this.decryptBlock(byArray, n2, byArray2, n3);
    }

    @Override
    public void reset() {
    }

    private void setKey(byte[] byArray) {
        if (byArray.length != 16) {
            throw new IllegalArgumentException("Key size must be 128 bits.");
        }
        this._a = this.bytesToInt(byArray, 0);
        this._b = this.bytesToInt(byArray, 4);
        this._c = this.bytesToInt(byArray, 8);
        this._d = this.bytesToInt(byArray, 12);
    }

    private int encryptBlock(byte[] byArray, int n2, byte[] byArray2, int n3) {
        int n4 = this.bytesToInt(byArray, n2);
        int n5 = this.bytesToInt(byArray, n2 + 4);
        int n6 = 0;
        for (int i2 = 0; i2 != 32; ++i2) {
            n5 += ((n4 += (n5 << 4) + this._a ^ n5 + (n6 -= 1640531527) ^ (n5 >>> 5) + this._b) << 4) + this._c ^ n4 + n6 ^ (n4 >>> 5) + this._d;
        }
        this.unpackInt(n4, byArray2, n3);
        this.unpackInt(n5, byArray2, n3 + 4);
        return 8;
    }

    private int decryptBlock(byte[] byArray, int n2, byte[] byArray2, int n3) {
        int n4 = this.bytesToInt(byArray, n2);
        int n5 = this.bytesToInt(byArray, n2 + 4);
        int n6 = -957401312;
        for (int i2 = 0; i2 != 32; ++i2) {
            n4 -= ((n5 -= (n4 << 4) + this._c ^ n4 + n6 ^ (n4 >>> 5) + this._d) << 4) + this._a ^ n5 + n6 ^ (n5 >>> 5) + this._b;
            n6 += 1640531527;
        }
        this.unpackInt(n4, byArray2, n3);
        this.unpackInt(n5, byArray2, n3 + 4);
        return 8;
    }

    private int bytesToInt(byte[] byArray, int n2) {
        return byArray[n2++] << 24 | (byArray[n2++] & 0xFF) << 16 | (byArray[n2++] & 0xFF) << 8 | byArray[n2] & 0xFF;
    }

    private void unpackInt(int n2, byte[] byArray, int n3) {
        byArray[n3++] = (byte)(n2 >>> 24);
        byArray[n3++] = (byte)(n2 >>> 16);
        byArray[n3++] = (byte)(n2 >>> 8);
        byArray[n3] = (byte)n2;
    }
}

