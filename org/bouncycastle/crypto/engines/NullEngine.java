/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.OutputLengthException;

public class NullEngine
implements BlockCipher {
    private boolean initialised;
    protected static final int DEFAULT_BLOCK_SIZE = 1;
    private final int blockSize;

    public NullEngine() {
        this(1);
    }

    public NullEngine(int n2) {
        this.blockSize = n2;
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) throws IllegalArgumentException {
        this.initialised = true;
    }

    @Override
    public String getAlgorithmName() {
        return "Null";
    }

    @Override
    public int getBlockSize() {
        return this.blockSize;
    }

    @Override
    public int processBlock(byte[] byArray, int n2, byte[] byArray2, int n3) throws DataLengthException, IllegalStateException {
        if (!this.initialised) {
            throw new IllegalStateException("Null engine not initialised");
        }
        if (n2 + this.blockSize > byArray.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n3 + this.blockSize > byArray2.length) {
            throw new OutputLengthException("output buffer too short");
        }
        for (int i2 = 0; i2 < this.blockSize; ++i2) {
            byArray2[n3 + i2] = byArray[n2 + i2];
        }
        return this.blockSize;
    }

    @Override
    public void reset() {
    }
}

