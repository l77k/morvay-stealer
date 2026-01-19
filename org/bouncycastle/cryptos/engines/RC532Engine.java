/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.CryptoServicesRegistrar;
import org.bouncycastle.crypto.constraints.DefaultServiceProperties;
import org.bouncycastle.crypto.engines.Utils;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.RC5Parameters;

public class RC532Engine
implements BlockCipher {
    private int _noRounds = 12;
    private int[] _S = null;
    private static final int P32 = -1209970333;
    private static final int Q32 = -1640531527;
    private boolean forEncryption;

    @Override
    public String getAlgorithmName() {
        return "RC5-32";
    }

    @Override
    public int getBlockSize() {
        return 8;
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        byte[] byArray;
        if (cipherParameters instanceof RC5Parameters) {
            RC5Parameters rC5Parameters = (RC5Parameters)cipherParameters;
            this._noRounds = rC5Parameters.getRounds();
            byArray = rC5Parameters.getKey();
            this.setKey(byArray);
        } else if (cipherParameters instanceof KeyParameter) {
            KeyParameter keyParameter = (KeyParameter)cipherParameters;
            byArray = keyParameter.getKey();
            this.setKey(byArray);
        } else {
            throw new IllegalArgumentException("invalid parameter passed to RC532 init - " + cipherParameters.getClass().getName());
        }
        this.forEncryption = bl;
        CryptoServicesRegistrar.checkConstraints(new DefaultServiceProperties(this.getAlgorithmName(), byArray.length * 8, cipherParameters, Utils.getPurpose(bl)));
    }

    @Override
    public int processBlock(byte[] byArray, int n2, byte[] byArray2, int n3) {
        return this.forEncryption ? this.encryptBlock(byArray, n2, byArray2, n3) : this.decryptBlock(byArray, n2, byArray2, n3);
    }

    @Override
    public void reset() {
    }

    private void setKey(byte[] byArray) {
        int n2;
        int[] nArray = new int[(byArray.length + 3) / 4];
        for (n2 = 0; n2 != byArray.length; ++n2) {
            int n3 = n2 / 4;
            nArray[n3] = nArray[n3] + ((byArray[n2] & 0xFF) << 8 * (n2 % 4));
        }
        this._S = new int[2 * (this._noRounds + 1)];
        this._S[0] = -1209970333;
        for (n2 = 1; n2 < this._S.length; ++n2) {
            this._S[n2] = this._S[n2 - 1] + -1640531527;
        }
        n2 = nArray.length > this._S.length ? 3 * nArray.length : 3 * this._S.length;
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        int n7 = 0;
        for (int i2 = 0; i2 < n2; ++i2) {
            n4 = this._S[n6] = this.rotateLeft(this._S[n6] + n4 + n5, 3);
            n5 = nArray[n7] = this.rotateLeft(nArray[n7] + n4 + n5, n4 + n5);
            n6 = (n6 + 1) % this._S.length;
            n7 = (n7 + 1) % nArray.length;
        }
    }

    private int encryptBlock(byte[] byArray, int n2, byte[] byArray2, int n3) {
        int n4 = this.bytesToWord(byArray, n2) + this._S[0];
        int n5 = this.bytesToWord(byArray, n2 + 4) + this._S[1];
        for (int i2 = 1; i2 <= this._noRounds; ++i2) {
            n4 = this.rotateLeft(n4 ^ n5, n5) + this._S[2 * i2];
            n5 = this.rotateLeft(n5 ^ n4, n4) + this._S[2 * i2 + 1];
        }
        this.wordToBytes(n4, byArray2, n3);
        this.wordToBytes(n5, byArray2, n3 + 4);
        return 8;
    }

    private int decryptBlock(byte[] byArray, int n2, byte[] byArray2, int n3) {
        int n4 = this.bytesToWord(byArray, n2);
        int n5 = this.bytesToWord(byArray, n2 + 4);
        for (int i2 = this._noRounds; i2 >= 1; --i2) {
            n5 = this.rotateRight(n5 - this._S[2 * i2 + 1], n4) ^ n4;
            n4 = this.rotateRight(n4 - this._S[2 * i2], n5) ^ n5;
        }
        this.wordToBytes(n4 - this._S[0], byArray2, n3);
        this.wordToBytes(n5 - this._S[1], byArray2, n3 + 4);
        return 8;
    }

    private int rotateLeft(int n2, int n3) {
        return n2 << (n3 & 0x1F) | n2 >>> 32 - (n3 & 0x1F);
    }

    private int rotateRight(int n2, int n3) {
        return n2 >>> (n3 & 0x1F) | n2 << 32 - (n3 & 0x1F);
    }

    private int bytesToWord(byte[] byArray, int n2) {
        return byArray[n2] & 0xFF | (byArray[n2 + 1] & 0xFF) << 8 | (byArray[n2 + 2] & 0xFF) << 16 | (byArray[n2 + 3] & 0xFF) << 24;
    }

    private void wordToBytes(int n2, byte[] byArray, int n3) {
        byArray[n3] = (byte)n2;
        byArray[n3 + 1] = (byte)(n2 >> 8);
        byArray[n3 + 2] = (byte)(n2 >> 16);
        byArray[n3 + 3] = (byte)(n2 >> 24);
    }
}

