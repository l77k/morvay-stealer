/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto;

import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.MultiBlockCipher;

public abstract class DefaultMultiBlockCipher
implements MultiBlockCipher {
    protected DefaultMultiBlockCipher() {
    }

    @Override
    public int getMultiBlockSize() {
        return this.getBlockSize();
    }

    @Override
    public int processBlocks(byte[] byArray, int n2, int n3, byte[] byArray2, int n4) throws DataLengthException, IllegalStateException {
        int n5 = 0;
        int n6 = this.getMultiBlockSize();
        for (int i2 = 0; i2 != n3; ++i2) {
            n5 += this.processBlock(byArray, n2, byArray2, n4 + n5);
            n2 += n6;
        }
        return n5;
    }
}

