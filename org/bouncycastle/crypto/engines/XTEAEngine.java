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

public class XTEAEngine
implements BlockCipher {
    private static final int rounds = 32;
    private static final int block_size = 8;
    private static final int delta = -1640531527;
    private int[] _S = new int[4];
    private int[] _sum0 = new int[32];
    private int[] _sum1 = new int[32];
    private boolean _initialised = false;
    private boolean _forEncryption;

    @Override
    public String getAlgorithmName() {
        return "XTEA";
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
        int n2 = 0;
        int n3 = 0;
        while (n3 < 4) {
            this._S[n3] = this.bytesToInt(byArray, n2);
            ++n3;
            n2 += 4;
        }
        n2 = 0;
        for (n3 = 0; n3 < 32; ++n3) {
            this._sum0[n3] = n2 + this._S[n2 & 3];
            this._sum1[n3] = (n2 -= 1640531527) + this._S[n2 >>> 11 & 3];
        }
    }

    private int encryptBlock(byte[] byArray, int n2, byte[] byArray2, int n3) {
        int n4 = this.bytesToInt(byArray, n2);
        int n5 = this.bytesToInt(byArray, n2 + 4);
        for (int i2 = 0; i2 < 32; ++i2) {
            n5 += ((n4 += (n5 << 4 ^ n5 >>> 5) + n5 ^ this._sum0[i2]) << 4 ^ n4 >>> 5) + n4 ^ this._sum1[i2];
        }
        this.unpackInt(n4, byArray2, n3);
        this.unpackInt(n5, byArray2, n3 + 4);
        return 8;
    }

    private int decryptBlock(byte[] byArray, int n2, byte[] byArray2, int n3) {
        int n4 = this.bytesToInt(byArray, n2);
        int n5 = this.bytesToInt(byArray, n2 + 4);
        for (int i2 = 31; i2 >= 0; --i2) {
            n4 -= ((n5 -= (n4 << 4 ^ n4 >>> 5) + n4 ^ this._sum1[i2]) << 4 ^ n5 >>> 5) + n5 ^ this._sum0[i2];
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

