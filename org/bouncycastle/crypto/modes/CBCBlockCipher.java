/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.modes;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.DefaultMultiBlockCipher;
import org.bouncycastle.crypto.modes.CBCModeCipher;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.util.Arrays;

public class CBCBlockCipher
extends DefaultMultiBlockCipher
implements CBCModeCipher {
    private byte[] IV;
    private byte[] cbcV;
    private byte[] cbcNextV;
    private int blockSize;
    private BlockCipher cipher = null;
    private boolean encrypting;

    public static CBCModeCipher newInstance(BlockCipher blockCipher) {
        return new CBCBlockCipher(blockCipher);
    }

    public CBCBlockCipher(BlockCipher blockCipher) {
        this.cipher = blockCipher;
        this.blockSize = blockCipher.getBlockSize();
        this.IV = new byte[this.blockSize];
        this.cbcV = new byte[this.blockSize];
        this.cbcNextV = new byte[this.blockSize];
    }

    @Override
    public BlockCipher getUnderlyingCipher() {
        return this.cipher;
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) throws IllegalArgumentException {
        boolean bl2 = this.encrypting;
        this.encrypting = bl;
        if (cipherParameters instanceof ParametersWithIV) {
            ParametersWithIV parametersWithIV = (ParametersWithIV)cipherParameters;
            byte[] byArray = parametersWithIV.getIV();
            if (byArray.length != this.blockSize) {
                throw new IllegalArgumentException("initialisation vector must be the same length as block size");
            }
            System.arraycopy(byArray, 0, this.IV, 0, byArray.length);
            cipherParameters = parametersWithIV.getParameters();
        } else {
            Arrays.fill(this.IV, (byte)0);
        }
        this.reset();
        if (cipherParameters != null) {
            this.cipher.init(bl, cipherParameters);
        } else if (bl2 != bl) {
            throw new IllegalArgumentException("cannot change encrypting state without providing key.");
        }
    }

    @Override
    public String getAlgorithmName() {
        return this.cipher.getAlgorithmName() + "/CBC";
    }

    @Override
    public int getBlockSize() {
        return this.cipher.getBlockSize();
    }

    @Override
    public int processBlock(byte[] byArray, int n2, byte[] byArray2, int n3) throws DataLengthException, IllegalStateException {
        return this.encrypting ? this.encryptBlock(byArray, n2, byArray2, n3) : this.decryptBlock(byArray, n2, byArray2, n3);
    }

    @Override
    public void reset() {
        System.arraycopy(this.IV, 0, this.cbcV, 0, this.IV.length);
        Arrays.fill(this.cbcNextV, (byte)0);
        this.cipher.reset();
    }

    private int encryptBlock(byte[] byArray, int n2, byte[] byArray2, int n3) throws DataLengthException, IllegalStateException {
        int n4;
        if (n2 + this.blockSize > byArray.length) {
            throw new DataLengthException("input buffer too short");
        }
        for (n4 = 0; n4 < this.blockSize; ++n4) {
            int n5 = n4;
            this.cbcV[n5] = (byte)(this.cbcV[n5] ^ byArray[n2 + n4]);
        }
        n4 = this.cipher.processBlock(this.cbcV, 0, byArray2, n3);
        System.arraycopy(byArray2, n3, this.cbcV, 0, this.cbcV.length);
        return n4;
    }

    private int decryptBlock(byte[] byArray, int n2, byte[] byArray2, int n3) throws DataLengthException, IllegalStateException {
        if (n2 + this.blockSize > byArray.length) {
            throw new DataLengthException("input buffer too short");
        }
        System.arraycopy(byArray, n2, this.cbcNextV, 0, this.blockSize);
        int n4 = this.cipher.processBlock(byArray, n2, byArray2, n3);
        for (int i2 = 0; i2 < this.blockSize; ++i2) {
            int n5 = n3 + i2;
            byArray2[n5] = (byte)(byArray2[n5] ^ this.cbcV[i2]);
        }
        byte[] byArray3 = this.cbcV;
        this.cbcV = this.cbcNextV;
        this.cbcNextV = byArray3;
        return n4;
    }
}

