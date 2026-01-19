/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.modes;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.macs.CMac;
import org.bouncycastle.crypto.modes.AEADBlockCipher;
import org.bouncycastle.crypto.modes.CTRModeCipher;
import org.bouncycastle.crypto.modes.SICBlockCipher;
import org.bouncycastle.crypto.params.AEADParameters;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.util.Arrays;

public class EAXBlockCipher
implements AEADBlockCipher {
    private static final byte nTAG = 0;
    private static final byte hTAG = 1;
    private static final byte cTAG = 2;
    private CTRModeCipher cipher;
    private boolean forEncryption;
    private int blockSize;
    private Mac mac;
    private byte[] nonceMac;
    private byte[] associatedTextMac;
    private byte[] macBlock;
    private int macSize;
    private byte[] bufBlock;
    private int bufOff;
    private boolean cipherInitialized;
    private byte[] initialAssociatedText;

    public EAXBlockCipher(BlockCipher blockCipher) {
        this.blockSize = blockCipher.getBlockSize();
        this.mac = new CMac(blockCipher);
        this.macBlock = new byte[this.blockSize];
        this.associatedTextMac = new byte[this.mac.getMacSize()];
        this.nonceMac = new byte[this.mac.getMacSize()];
        this.cipher = SICBlockCipher.newInstance(blockCipher);
    }

    @Override
    public String getAlgorithmName() {
        return this.cipher.getUnderlyingCipher().getAlgorithmName() + "/EAX";
    }

    @Override
    public BlockCipher getUnderlyingCipher() {
        return this.cipher.getUnderlyingCipher();
    }

    public int getBlockSize() {
        return this.cipher.getBlockSize();
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) throws IllegalArgumentException {
        CipherParameters cipherParameters2;
        byte[] byArray;
        Object object;
        this.forEncryption = bl;
        if (cipherParameters instanceof AEADParameters) {
            object = (AEADParameters)cipherParameters;
            byArray = ((AEADParameters)object).getNonce();
            this.initialAssociatedText = ((AEADParameters)object).getAssociatedText();
            this.macSize = ((AEADParameters)object).getMacSize() / 8;
            cipherParameters2 = ((AEADParameters)object).getKey();
        } else if (cipherParameters instanceof ParametersWithIV) {
            object = (ParametersWithIV)cipherParameters;
            byArray = ((ParametersWithIV)object).getIV();
            this.initialAssociatedText = null;
            this.macSize = this.mac.getMacSize() / 2;
            cipherParameters2 = ((ParametersWithIV)object).getParameters();
        } else {
            throw new IllegalArgumentException("invalid parameters passed to EAX");
        }
        this.bufBlock = new byte[bl ? this.blockSize : this.blockSize + this.macSize];
        object = new byte[this.blockSize];
        this.mac.init(cipherParameters2);
        object[this.blockSize - 1] = false;
        this.mac.update((byte[])object, 0, this.blockSize);
        this.mac.update(byArray, 0, byArray.length);
        this.mac.doFinal(this.nonceMac, 0);
        this.cipher.init(true, new ParametersWithIV(cipherParameters2, this.nonceMac));
        this.reset();
    }

    private void initCipher() {
        if (this.cipherInitialized) {
            return;
        }
        this.cipherInitialized = true;
        this.mac.doFinal(this.associatedTextMac, 0);
        byte[] byArray = new byte[this.blockSize];
        byArray[this.blockSize - 1] = 2;
        this.mac.update(byArray, 0, this.blockSize);
    }

    private void calculateMac() {
        byte[] byArray = new byte[this.blockSize];
        this.mac.doFinal(byArray, 0);
        for (int i2 = 0; i2 < this.macBlock.length; ++i2) {
            this.macBlock[i2] = (byte)(this.nonceMac[i2] ^ this.associatedTextMac[i2] ^ byArray[i2]);
        }
    }

    @Override
    public void reset() {
        this.reset(true);
    }

    private void reset(boolean bl) {
        this.cipher.reset();
        this.mac.reset();
        this.bufOff = 0;
        Arrays.fill(this.bufBlock, (byte)0);
        if (bl) {
            Arrays.fill(this.macBlock, (byte)0);
        }
        byte[] byArray = new byte[this.blockSize];
        byArray[this.blockSize - 1] = 1;
        this.mac.update(byArray, 0, this.blockSize);
        this.cipherInitialized = false;
        if (this.initialAssociatedText != null) {
            this.processAADBytes(this.initialAssociatedText, 0, this.initialAssociatedText.length);
        }
    }

    @Override
    public void processAADByte(byte by) {
        if (this.cipherInitialized) {
            throw new IllegalStateException("AAD data cannot be added after encryption/decryption processing has begun.");
        }
        this.mac.update(by);
    }

    @Override
    public void processAADBytes(byte[] byArray, int n2, int n3) {
        if (this.cipherInitialized) {
            throw new IllegalStateException("AAD data cannot be added after encryption/decryption processing has begun.");
        }
        this.mac.update(byArray, n2, n3);
    }

    @Override
    public int processByte(byte by, byte[] byArray, int n2) throws DataLengthException {
        this.initCipher();
        return this.process(by, byArray, n2);
    }

    @Override
    public int processBytes(byte[] byArray, int n2, int n3, byte[] byArray2, int n4) throws DataLengthException {
        this.initCipher();
        if (byArray.length < n2 + n3) {
            throw new DataLengthException("Input buffer too short");
        }
        int n5 = 0;
        for (int i2 = 0; i2 != n3; ++i2) {
            n5 += this.process(byArray[n2 + i2], byArray2, n4 + n5);
        }
        return n5;
    }

    @Override
    public int doFinal(byte[] byArray, int n2) throws IllegalStateException, InvalidCipherTextException {
        this.initCipher();
        int n3 = this.bufOff;
        byte[] byArray2 = new byte[this.bufBlock.length];
        this.bufOff = 0;
        if (this.forEncryption) {
            if (byArray.length < n2 + n3 + this.macSize) {
                throw new OutputLengthException("Output buffer too short");
            }
            this.cipher.processBlock(this.bufBlock, 0, byArray2, 0);
            System.arraycopy(byArray2, 0, byArray, n2, n3);
            this.mac.update(byArray2, 0, n3);
            this.calculateMac();
            System.arraycopy(this.macBlock, 0, byArray, n2 + n3, this.macSize);
            this.reset(false);
            return n3 + this.macSize;
        }
        if (n3 < this.macSize) {
            throw new InvalidCipherTextException("data too short");
        }
        if (byArray.length < n2 + n3 - this.macSize) {
            throw new OutputLengthException("Output buffer too short");
        }
        if (n3 > this.macSize) {
            this.mac.update(this.bufBlock, 0, n3 - this.macSize);
            this.cipher.processBlock(this.bufBlock, 0, byArray2, 0);
            System.arraycopy(byArray2, 0, byArray, n2, n3 - this.macSize);
        }
        this.calculateMac();
        if (!this.verifyMac(this.bufBlock, n3 - this.macSize)) {
            throw new InvalidCipherTextException("mac check in EAX failed");
        }
        this.reset(false);
        return n3 - this.macSize;
    }

    @Override
    public byte[] getMac() {
        byte[] byArray = new byte[this.macSize];
        System.arraycopy(this.macBlock, 0, byArray, 0, this.macSize);
        return byArray;
    }

    @Override
    public int getUpdateOutputSize(int n2) {
        int n3 = n2 + this.bufOff;
        if (!this.forEncryption) {
            if (n3 < this.macSize) {
                return 0;
            }
            n3 -= this.macSize;
        }
        return n3 - n3 % this.blockSize;
    }

    @Override
    public int getOutputSize(int n2) {
        int n3 = n2 + this.bufOff;
        if (this.forEncryption) {
            return n3 + this.macSize;
        }
        return n3 < this.macSize ? 0 : n3 - this.macSize;
    }

    private int process(byte by, byte[] byArray, int n2) {
        this.bufBlock[this.bufOff++] = by;
        if (this.bufOff == this.bufBlock.length) {
            int n3;
            if (byArray.length < n2 + this.blockSize) {
                throw new OutputLengthException("Output buffer is too short");
            }
            if (this.forEncryption) {
                n3 = this.cipher.processBlock(this.bufBlock, 0, byArray, n2);
                this.mac.update(byArray, n2, this.blockSize);
            } else {
                this.mac.update(this.bufBlock, 0, this.blockSize);
                n3 = this.cipher.processBlock(this.bufBlock, 0, byArray, n2);
            }
            this.bufOff = 0;
            if (!this.forEncryption) {
                System.arraycopy(this.bufBlock, this.blockSize, this.bufBlock, 0, this.macSize);
                this.bufOff = this.macSize;
            }
            return n3;
        }
        return 0;
    }

    private boolean verifyMac(byte[] byArray, int n2) {
        int n3 = 0;
        for (int i2 = 0; i2 < this.macSize; ++i2) {
            n3 |= this.macBlock[i2] ^ byArray[n2 + i2];
        }
        return n3 == 0;
    }
}

