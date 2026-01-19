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

public class RC6Engine
implements BlockCipher {
    private static final int wordSize = 32;
    private static final int bytesPerWord = 4;
    private static final int _noRounds = 20;
    private int[] _S = null;
    private static final int P32 = -1209970333;
    private static final int Q32 = -1640531527;
    private static final int LGW = 5;
    private boolean forEncryption;

    @Override
    public String getAlgorithmName() {
        return "RC6";
    }

    @Override
    public int getBlockSize() {
        return 16;
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        if (!(cipherParameters instanceof KeyParameter)) {
            throw new IllegalArgumentException("invalid parameter passed to RC6 init - " + cipherParameters.getClass().getName());
        }
        KeyParameter keyParameter = (KeyParameter)cipherParameters;
        this.forEncryption = bl;
        byte[] byArray = keyParameter.getKey();
        this.setKey(byArray);
        CryptoServicesRegistrar.checkConstraints(new DefaultServiceProperties(this.getAlgorithmName(), byArray.length * 8, cipherParameters, Utils.getPurpose(bl)));
    }

    @Override
    public int processBlock(byte[] byArray, int n2, byte[] byArray2, int n3) {
        int n4 = this.getBlockSize();
        if (this._S == null) {
            throw new IllegalStateException("RC6 engine not initialised");
        }
        if (n2 + n4 > byArray.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n3 + n4 > byArray2.length) {
            throw new OutputLengthException("output buffer too short");
        }
        return this.forEncryption ? this.encryptBlock(byArray, n2, byArray2, n3) : this.decryptBlock(byArray, n2, byArray2, n3);
    }

    @Override
    public void reset() {
    }

    private void setKey(byte[] byArray) {
        int n2;
        int n3 = (byArray.length + 3) / 4;
        if (n3 == 0) {
            n3 = 1;
        }
        int[] nArray = new int[(byArray.length + 4 - 1) / 4];
        for (n2 = byArray.length - 1; n2 >= 0; --n2) {
            nArray[n2 / 4] = (nArray[n2 / 4] << 8) + (byArray[n2] & 0xFF);
        }
        this._S = new int[44];
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
        int n4 = this.bytesToWord(byArray, n2);
        int n5 = this.bytesToWord(byArray, n2 + 4);
        int n6 = this.bytesToWord(byArray, n2 + 8);
        int n7 = this.bytesToWord(byArray, n2 + 12);
        n5 += this._S[0];
        n7 += this._S[1];
        for (int i2 = 1; i2 <= 20; ++i2) {
            int n8 = 0;
            int n9 = 0;
            n8 = n5 * (2 * n5 + 1);
            n8 = this.rotateLeft(n8, 5);
            n9 = n7 * (2 * n7 + 1);
            n9 = this.rotateLeft(n9, 5);
            n4 ^= n8;
            n4 = this.rotateLeft(n4, n9);
            n6 ^= n9;
            n6 = this.rotateLeft(n6, n8);
            int n10 = n4 += this._S[2 * i2];
            n4 = n5;
            n5 = n6 += this._S[2 * i2 + 1];
            n6 = n7;
            n7 = n10;
        }
        this.wordToBytes(n4 += this._S[42], byArray2, n3);
        this.wordToBytes(n5, byArray2, n3 + 4);
        this.wordToBytes(n6 += this._S[43], byArray2, n3 + 8);
        this.wordToBytes(n7, byArray2, n3 + 12);
        return 16;
    }

    private int decryptBlock(byte[] byArray, int n2, byte[] byArray2, int n3) {
        int n4 = this.bytesToWord(byArray, n2);
        int n5 = this.bytesToWord(byArray, n2 + 4);
        int n6 = this.bytesToWord(byArray, n2 + 8);
        int n7 = this.bytesToWord(byArray, n2 + 12);
        n6 -= this._S[43];
        n4 -= this._S[42];
        for (int i2 = 20; i2 >= 1; --i2) {
            int n8 = 0;
            int n9 = 0;
            int n10 = n7;
            n7 = n6;
            n6 = n5;
            n5 = n4;
            n4 = n10;
            n8 = n5 * (2 * n5 + 1);
            n8 = this.rotateLeft(n8, 5);
            n9 = n7 * (2 * n7 + 1);
            n9 = this.rotateLeft(n9, 5);
            n6 -= this._S[2 * i2 + 1];
            n6 = this.rotateRight(n6, n8);
            n6 ^= n9;
            n4 -= this._S[2 * i2];
            n4 = this.rotateRight(n4, n9);
            n4 ^= n8;
        }
        this.wordToBytes(n4, byArray2, n3);
        this.wordToBytes(n5 -= this._S[0], byArray2, n3 + 4);
        this.wordToBytes(n6, byArray2, n3 + 8);
        this.wordToBytes(n7 -= this._S[1], byArray2, n3 + 12);
        return 16;
    }

    private int rotateLeft(int n2, int n3) {
        return n2 << n3 | n2 >>> -n3;
    }

    private int rotateRight(int n2, int n3) {
        return n2 >>> n3 | n2 << -n3;
    }

    private int bytesToWord(byte[] byArray, int n2) {
        int n3 = 0;
        for (int i2 = 3; i2 >= 0; --i2) {
            n3 = (n3 << 8) + (byArray[i2 + n2] & 0xFF);
        }
        return n3;
    }

    private void wordToBytes(int n2, byte[] byArray, int n3) {
        for (int i2 = 0; i2 < 4; ++i2) {
            byArray[i2 + n3] = (byte)n2;
            n2 >>>= 8;
        }
    }
}

