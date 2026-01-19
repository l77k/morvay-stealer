/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.CryptoServicesRegistrar;
import org.bouncycastle.crypto.constraints.DefaultServiceProperties;
import org.bouncycastle.crypto.engines.Utils;
import org.bouncycastle.crypto.params.RC5Parameters;

public class RC564Engine
implements BlockCipher {
    private static final int wordSize = 64;
    private static final int bytesPerWord = 8;
    private int _noRounds = 12;
    private long[] _S = null;
    private static final long P64 = -5196783011329398165L;
    private static final long Q64 = -7046029254386353131L;
    private boolean forEncryption;

    @Override
    public String getAlgorithmName() {
        return "RC5-64";
    }

    @Override
    public int getBlockSize() {
        return 16;
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        if (!(cipherParameters instanceof RC5Parameters)) {
            throw new IllegalArgumentException("invalid parameter passed to RC564 init - " + cipherParameters.getClass().getName());
        }
        RC5Parameters rC5Parameters = (RC5Parameters)cipherParameters;
        this.forEncryption = bl;
        this._noRounds = rC5Parameters.getRounds();
        byte[] byArray = rC5Parameters.getKey();
        this.setKey(byArray);
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
        long[] lArray = new long[(byArray.length + 7) / 8];
        for (n2 = 0; n2 != byArray.length; ++n2) {
            int n3 = n2 / 8;
            lArray[n3] = lArray[n3] + ((long)(byArray[n2] & 0xFF) << 8 * (n2 % 8));
        }
        this._S = new long[2 * (this._noRounds + 1)];
        this._S[0] = -5196783011329398165L;
        for (n2 = 1; n2 < this._S.length; ++n2) {
            this._S[n2] = this._S[n2 - 1] + -7046029254386353131L;
        }
        n2 = lArray.length > this._S.length ? 3 * lArray.length : 3 * this._S.length;
        long l2 = 0L;
        long l3 = 0L;
        int n4 = 0;
        int n5 = 0;
        for (int i2 = 0; i2 < n2; ++i2) {
            l2 = this._S[n4] = this.rotateLeft(this._S[n4] + l2 + l3, 3L);
            l3 = lArray[n5] = this.rotateLeft(lArray[n5] + l2 + l3, l2 + l3);
            n4 = (n4 + 1) % this._S.length;
            n5 = (n5 + 1) % lArray.length;
        }
    }

    private int encryptBlock(byte[] byArray, int n2, byte[] byArray2, int n3) {
        long l2 = this.bytesToWord(byArray, n2) + this._S[0];
        long l3 = this.bytesToWord(byArray, n2 + 8) + this._S[1];
        for (int i2 = 1; i2 <= this._noRounds; ++i2) {
            l2 = this.rotateLeft(l2 ^ l3, l3) + this._S[2 * i2];
            l3 = this.rotateLeft(l3 ^ l2, l2) + this._S[2 * i2 + 1];
        }
        this.wordToBytes(l2, byArray2, n3);
        this.wordToBytes(l3, byArray2, n3 + 8);
        return 16;
    }

    private int decryptBlock(byte[] byArray, int n2, byte[] byArray2, int n3) {
        long l2 = this.bytesToWord(byArray, n2);
        long l3 = this.bytesToWord(byArray, n2 + 8);
        for (int i2 = this._noRounds; i2 >= 1; --i2) {
            l3 = this.rotateRight(l3 - this._S[2 * i2 + 1], l2) ^ l2;
            l2 = this.rotateRight(l2 - this._S[2 * i2], l3) ^ l3;
        }
        this.wordToBytes(l2 - this._S[0], byArray2, n3);
        this.wordToBytes(l3 - this._S[1], byArray2, n3 + 8);
        return 16;
    }

    private long rotateLeft(long l2, long l3) {
        return l2 << (int)(l3 & 0x3FL) | l2 >>> (int)(64L - (l3 & 0x3FL));
    }

    private long rotateRight(long l2, long l3) {
        return l2 >>> (int)(l3 & 0x3FL) | l2 << (int)(64L - (l3 & 0x3FL));
    }

    private long bytesToWord(byte[] byArray, int n2) {
        long l2 = 0L;
        for (int i2 = 7; i2 >= 0; --i2) {
            l2 = (l2 << 8) + (long)(byArray[i2 + n2] & 0xFF);
        }
        return l2;
    }

    private void wordToBytes(long l2, byte[] byArray, int n2) {
        for (int i2 = 0; i2 < 8; ++i2) {
            byArray[i2 + n2] = (byte)l2;
            l2 >>>= 8;
        }
    }
}

