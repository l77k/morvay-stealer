/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.DefaultMultiBlockCipher;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.StreamCipher;

public abstract class StreamBlockCipher
extends DefaultMultiBlockCipher
implements StreamCipher {
    private final BlockCipher cipher;

    protected StreamBlockCipher(BlockCipher blockCipher) {
        this.cipher = blockCipher;
    }

    public BlockCipher getUnderlyingCipher() {
        return this.cipher;
    }

    @Override
    public final byte returnByte(byte by) {
        return this.calculateByte(by);
    }

    @Override
    public int processBytes(byte[] byArray, int n2, int n3, byte[] byArray2, int n4) throws DataLengthException {
        if (n2 + n3 > byArray.length) {
            throw new DataLengthException("input buffer too small");
        }
        if (n4 + n3 > byArray2.length) {
            throw new OutputLengthException("output buffer too short");
        }
        int n5 = n2;
        int n6 = n2 + n3;
        int n7 = n4;
        while (n5 < n6) {
            byArray2[n7++] = this.calculateByte(byArray[n5++]);
        }
        return n3;
    }

    protected abstract byte calculateByte(byte var1);
}

