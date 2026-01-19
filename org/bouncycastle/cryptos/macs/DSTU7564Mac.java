/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.macs;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.digests.DSTU7564Digest;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.Pack;

public class DSTU7564Mac
implements Mac {
    private static final int BITS_IN_BYTE = 8;
    private DSTU7564Digest engine;
    private int macSize;
    private byte[] paddedKey;
    private byte[] invertedKey;
    private long inputLength;

    public DSTU7564Mac(int n2) {
        this.engine = new DSTU7564Digest(n2);
        this.macSize = n2 / 8;
        this.paddedKey = null;
        this.invertedKey = null;
    }

    @Override
    public void init(CipherParameters cipherParameters) throws IllegalArgumentException {
        this.paddedKey = null;
        this.reset();
        if (cipherParameters instanceof KeyParameter) {
            byte[] byArray = ((KeyParameter)cipherParameters).getKey();
            this.invertedKey = new byte[byArray.length];
            this.paddedKey = this.padKey(byArray);
            for (int i2 = 0; i2 < this.invertedKey.length; ++i2) {
                this.invertedKey[i2] = ~byArray[i2];
            }
        } else {
            throw new IllegalArgumentException("Bad parameter passed");
        }
        this.engine.update(this.paddedKey, 0, this.paddedKey.length);
    }

    @Override
    public String getAlgorithmName() {
        return "DSTU7564Mac";
    }

    @Override
    public int getMacSize() {
        return this.macSize;
    }

    @Override
    public void update(byte by) throws IllegalStateException {
        this.engine.update(by);
        ++this.inputLength;
    }

    @Override
    public void update(byte[] byArray, int n2, int n3) throws DataLengthException, IllegalStateException {
        if (byArray.length - n2 < n3) {
            throw new DataLengthException("Input buffer too short");
        }
        if (this.paddedKey == null) {
            throw new IllegalStateException(this.getAlgorithmName() + " not initialised");
        }
        this.engine.update(byArray, n2, n3);
        this.inputLength += (long)n3;
    }

    @Override
    public int doFinal(byte[] byArray, int n2) throws DataLengthException, IllegalStateException {
        if (this.paddedKey == null) {
            throw new IllegalStateException(this.getAlgorithmName() + " not initialised");
        }
        if (byArray.length - n2 < this.macSize) {
            throw new OutputLengthException("Output buffer too short");
        }
        this.pad();
        this.engine.update(this.invertedKey, 0, this.invertedKey.length);
        this.inputLength = 0L;
        int n3 = this.engine.doFinal(byArray, n2);
        this.reset();
        return n3;
    }

    @Override
    public void reset() {
        this.inputLength = 0L;
        this.engine.reset();
        if (this.paddedKey != null) {
            this.engine.update(this.paddedKey, 0, this.paddedKey.length);
        }
    }

    private void pad() {
        int n2 = this.engine.getByteLength() - (int)(this.inputLength % (long)this.engine.getByteLength());
        if (n2 < 13) {
            n2 += this.engine.getByteLength();
        }
        byte[] byArray = new byte[n2];
        byArray[0] = -128;
        Pack.longToLittleEndian(this.inputLength * 8L, byArray, byArray.length - 12);
        this.engine.update(byArray, 0, byArray.length);
    }

    private byte[] padKey(byte[] byArray) {
        int n2 = (byArray.length + this.engine.getByteLength() - 1) / this.engine.getByteLength() * this.engine.getByteLength();
        int n3 = n2 - byArray.length;
        if (n3 < 13) {
            n2 += this.engine.getByteLength();
        }
        byte[] byArray2 = new byte[n2];
        System.arraycopy(byArray, 0, byArray2, 0, byArray.length);
        byArray2[byArray.length] = -128;
        Pack.intToLittleEndian(byArray.length * 8, byArray2, byArray2.length - 12);
        return byArray2;
    }
}

