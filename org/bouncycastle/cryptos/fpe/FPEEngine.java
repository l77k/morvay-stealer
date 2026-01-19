/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.fpe;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.params.FPEParameters;
import org.bouncycastle.util.Pack;

public abstract class FPEEngine {
    protected final BlockCipher baseCipher;
    protected boolean forEncryption;
    protected FPEParameters fpeParameters;

    protected FPEEngine(BlockCipher blockCipher) {
        this.baseCipher = blockCipher;
    }

    public int processBlock(byte[] byArray, int n2, int n3, byte[] byArray2, int n4) {
        if (this.fpeParameters == null) {
            throw new IllegalStateException("FPE engine not initialized");
        }
        if (n3 < 0) {
            throw new IllegalArgumentException("input length cannot be negative");
        }
        if (byArray == null || byArray2 == null) {
            throw new NullPointerException("buffer value is null");
        }
        if (byArray.length < n2 + n3) {
            throw new DataLengthException("input buffer too short");
        }
        if (byArray2.length < n4 + n3) {
            throw new OutputLengthException("output buffer too short");
        }
        if (this.forEncryption) {
            return this.encryptBlock(byArray, n2, n3, byArray2, n4);
        }
        return this.decryptBlock(byArray, n2, n3, byArray2, n4);
    }

    protected static short[] toShortArray(byte[] byArray) {
        if ((byArray.length & 1) != 0) {
            throw new IllegalArgumentException("data must be an even number of bytes for a wide radix");
        }
        short[] sArray = new short[byArray.length / 2];
        for (int i2 = 0; i2 != sArray.length; ++i2) {
            sArray[i2] = Pack.bigEndianToShort(byArray, i2 * 2);
        }
        return sArray;
    }

    protected static byte[] toByteArray(short[] sArray) {
        byte[] byArray = new byte[sArray.length * 2];
        for (int i2 = 0; i2 != sArray.length; ++i2) {
            Pack.shortToBigEndian(sArray[i2], byArray, i2 * 2);
        }
        return byArray;
    }

    public abstract void init(boolean var1, CipherParameters var2);

    public abstract String getAlgorithmName();

    protected abstract int encryptBlock(byte[] var1, int var2, int var3, byte[] var4, int var5);

    protected abstract int decryptBlock(byte[] var1, int var2, int var3, byte[] var4, int var5);
}

