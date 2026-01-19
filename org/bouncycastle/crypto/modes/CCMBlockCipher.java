/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.modes;

import java.io.ByteArrayOutputStream;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.macs.CBCBlockCipherMac;
import org.bouncycastle.crypto.modes.CCMModeCipher;
import org.bouncycastle.crypto.modes.CTRModeCipher;
import org.bouncycastle.crypto.modes.SICBlockCipher;
import org.bouncycastle.crypto.params.AEADParameters;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.util.Arrays;

public class CCMBlockCipher
implements CCMModeCipher {
    private BlockCipher cipher;
    private int blockSize;
    private boolean forEncryption;
    private byte[] nonce;
    private byte[] initialAssociatedText;
    private int macSize;
    private CipherParameters keyParam;
    private byte[] macBlock;
    private ExposedByteArrayOutputStream associatedText = new ExposedByteArrayOutputStream();
    private ExposedByteArrayOutputStream data = new ExposedByteArrayOutputStream();

    public static CCMModeCipher newInstance(BlockCipher blockCipher) {
        return new CCMBlockCipher(blockCipher);
    }

    public CCMBlockCipher(BlockCipher blockCipher) {
        this.cipher = blockCipher;
        this.blockSize = blockCipher.getBlockSize();
        this.macBlock = new byte[this.blockSize];
        if (this.blockSize != 16) {
            throw new IllegalArgumentException("cipher required with a block size of 16.");
        }
    }

    @Override
    public BlockCipher getUnderlyingCipher() {
        return this.cipher;
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) throws IllegalArgumentException {
        CipherParameters cipherParameters2;
        this.forEncryption = bl;
        if (cipherParameters instanceof AEADParameters) {
            AEADParameters aEADParameters = (AEADParameters)cipherParameters;
            this.nonce = aEADParameters.getNonce();
            this.initialAssociatedText = aEADParameters.getAssociatedText();
            this.macSize = this.getMacSize(bl, aEADParameters.getMacSize());
            cipherParameters2 = aEADParameters.getKey();
        } else if (cipherParameters instanceof ParametersWithIV) {
            ParametersWithIV parametersWithIV = (ParametersWithIV)cipherParameters;
            this.nonce = parametersWithIV.getIV();
            this.initialAssociatedText = null;
            this.macSize = this.getMacSize(bl, 64);
            cipherParameters2 = parametersWithIV.getParameters();
        } else {
            throw new IllegalArgumentException("invalid parameters passed to CCM: " + cipherParameters.getClass().getName());
        }
        if (cipherParameters2 != null) {
            this.keyParam = cipherParameters2;
        }
        if (this.nonce == null || this.nonce.length < 7 || this.nonce.length > 13) {
            throw new IllegalArgumentException("nonce must have length from 7 to 13 octets");
        }
        this.reset();
    }

    @Override
    public String getAlgorithmName() {
        return this.cipher.getAlgorithmName() + "/CCM";
    }

    @Override
    public void processAADByte(byte by) {
        this.associatedText.write(by);
    }

    @Override
    public void processAADBytes(byte[] byArray, int n2, int n3) {
        this.associatedText.write(byArray, n2, n3);
    }

    @Override
    public int processByte(byte by, byte[] byArray, int n2) throws DataLengthException, IllegalStateException {
        this.data.write(by);
        return 0;
    }

    @Override
    public int processBytes(byte[] byArray, int n2, int n3, byte[] byArray2, int n4) throws DataLengthException, IllegalStateException {
        if (byArray.length < n2 + n3) {
            throw new DataLengthException("Input buffer too short");
        }
        this.data.write(byArray, n2, n3);
        return 0;
    }

    @Override
    public int doFinal(byte[] byArray, int n2) throws IllegalStateException, InvalidCipherTextException {
        int n3 = this.processPacket(this.data.getBuffer(), 0, this.data.size(), byArray, n2);
        this.reset();
        return n3;
    }

    @Override
    public void reset() {
        this.cipher.reset();
        this.associatedText.reset();
        this.data.reset();
    }

    @Override
    public byte[] getMac() {
        byte[] byArray = new byte[this.macSize];
        System.arraycopy(this.macBlock, 0, byArray, 0, byArray.length);
        return byArray;
    }

    @Override
    public int getUpdateOutputSize(int n2) {
        return 0;
    }

    @Override
    public int getOutputSize(int n2) {
        int n3 = n2 + this.data.size();
        if (this.forEncryption) {
            return n3 + this.macSize;
        }
        return n3 < this.macSize ? 0 : n3 - this.macSize;
    }

    public byte[] processPacket(byte[] byArray, int n2, int n3) throws IllegalStateException, InvalidCipherTextException {
        byte[] byArray2;
        if (this.forEncryption) {
            byArray2 = new byte[n3 + this.macSize];
        } else {
            if (n3 < this.macSize) {
                throw new InvalidCipherTextException("data too short");
            }
            byArray2 = new byte[n3 - this.macSize];
        }
        this.processPacket(byArray, n2, n3, byArray2, 0);
        return byArray2;
    }

    public int processPacket(byte[] byArray, int n2, int n3, byte[] byArray2, int n4) throws IllegalStateException, InvalidCipherTextException, DataLengthException {
        int n5;
        int n6;
        if (this.keyParam == null) {
            throw new IllegalStateException("CCM cipher unitialized.");
        }
        int n7 = this.nonce.length;
        int n8 = 15 - n7;
        if (n8 < 4) {
            int n9 = 1 << 8 * n8;
            int n10 = 0;
            if (!this.forEncryption) {
                n10 = 16;
            }
            if (n3 - n10 >= n9) {
                throw new IllegalStateException("CCM packet too large for choice of q");
            }
        }
        byte[] byArray3 = new byte[this.blockSize];
        byArray3[0] = (byte)(n8 - 1 & 7);
        System.arraycopy(this.nonce, 0, byArray3, 1, this.nonce.length);
        CTRModeCipher cTRModeCipher = SICBlockCipher.newInstance(this.cipher);
        cTRModeCipher.init(this.forEncryption, new ParametersWithIV(this.keyParam, byArray3));
        int n11 = n4;
        if (this.forEncryption) {
            n6 = n3 + this.macSize;
            if (byArray2.length < n6 + n4) {
                throw new OutputLengthException("Output buffer too short.");
            }
            this.calculateMac(byArray, n2, n3, this.macBlock);
            byte[] byArray4 = new byte[this.blockSize];
            cTRModeCipher.processBlock(this.macBlock, 0, byArray4, 0);
            for (n5 = n2; n5 < n2 + n3 - this.blockSize; n5 += this.blockSize) {
                cTRModeCipher.processBlock(byArray, n5, byArray2, n11);
                n11 += this.blockSize;
            }
            byte[] byArray5 = new byte[this.blockSize];
            System.arraycopy(byArray, n5, byArray5, 0, n3 + n2 - n5);
            cTRModeCipher.processBlock(byArray5, 0, byArray5, 0);
            System.arraycopy(byArray5, 0, byArray2, n11, n3 + n2 - n5);
            System.arraycopy(byArray4, 0, byArray2, n4 + n3, this.macSize);
        } else {
            if (n3 < this.macSize) {
                throw new InvalidCipherTextException("data too short");
            }
            n6 = n3 - this.macSize;
            if (byArray2.length < n6 + n4) {
                throw new OutputLengthException("Output buffer too short.");
            }
            System.arraycopy(byArray, n2 + n6, this.macBlock, 0, this.macSize);
            cTRModeCipher.processBlock(this.macBlock, 0, this.macBlock, 0);
            for (int i2 = this.macSize; i2 != this.macBlock.length; ++i2) {
                this.macBlock[i2] = 0;
            }
            while (n5 < n2 + n6 - this.blockSize) {
                cTRModeCipher.processBlock(byArray, n5, byArray2, n11);
                n11 += this.blockSize;
                n5 += this.blockSize;
            }
            byte[] byArray6 = new byte[this.blockSize];
            System.arraycopy(byArray, n5, byArray6, 0, n6 - (n5 - n2));
            cTRModeCipher.processBlock(byArray6, 0, byArray6, 0);
            System.arraycopy(byArray6, 0, byArray2, n11, n6 - (n5 - n2));
            byte[] byArray7 = new byte[this.blockSize];
            this.calculateMac(byArray2, n4, n6, byArray7);
            if (!Arrays.constantTimeAreEqual(this.macBlock, byArray7)) {
                throw new InvalidCipherTextException("mac check in CCM failed");
            }
        }
        return n6;
    }

    private int calculateMac(byte[] byArray, int n2, int n3, byte[] byArray2) {
        CBCBlockCipherMac cBCBlockCipherMac = new CBCBlockCipherMac(this.cipher, this.macSize * 8);
        cBCBlockCipherMac.init(this.keyParam);
        byte[] byArray3 = new byte[16];
        if (this.hasAssociatedText()) {
            byArray3[0] = (byte)(byArray3[0] | 0x40);
        }
        byArray3[0] = (byte)(byArray3[0] | ((cBCBlockCipherMac.getMacSize() - 2) / 2 & 7) << 3);
        byArray3[0] = (byte)(byArray3[0] | 15 - this.nonce.length - 1 & 7);
        System.arraycopy(this.nonce, 0, byArray3, 1, this.nonce.length);
        int n4 = n3;
        int n5 = 1;
        while (n4 > 0) {
            byArray3[byArray3.length - n5] = (byte)(n4 & 0xFF);
            n4 >>>= 8;
            ++n5;
        }
        cBCBlockCipherMac.update(byArray3, 0, byArray3.length);
        if (this.hasAssociatedText()) {
            int n6;
            int n7 = this.getAssociatedTextLength();
            if (n7 < 65280) {
                cBCBlockCipherMac.update((byte)(n7 >> 8));
                cBCBlockCipherMac.update((byte)n7);
                n6 = 2;
            } else {
                cBCBlockCipherMac.update((byte)-1);
                cBCBlockCipherMac.update((byte)-2);
                cBCBlockCipherMac.update((byte)(n7 >> 24));
                cBCBlockCipherMac.update((byte)(n7 >> 16));
                cBCBlockCipherMac.update((byte)(n7 >> 8));
                cBCBlockCipherMac.update((byte)n7);
                n6 = 6;
            }
            if (this.initialAssociatedText != null) {
                cBCBlockCipherMac.update(this.initialAssociatedText, 0, this.initialAssociatedText.length);
            }
            if (this.associatedText.size() > 0) {
                cBCBlockCipherMac.update(this.associatedText.getBuffer(), 0, this.associatedText.size());
            }
            if ((n6 = (n6 + n7) % 16) != 0) {
                for (int i2 = n6; i2 != 16; ++i2) {
                    cBCBlockCipherMac.update((byte)0);
                }
            }
        }
        cBCBlockCipherMac.update(byArray, n2, n3);
        return cBCBlockCipherMac.doFinal(byArray2, 0);
    }

    private int getMacSize(boolean bl, int n2) {
        if (bl && (n2 < 32 || n2 > 128 || 0 != (n2 & 0xF))) {
            throw new IllegalArgumentException("tag length in octets must be one of {4,6,8,10,12,14,16}");
        }
        return n2 >>> 3;
    }

    private int getAssociatedTextLength() {
        return this.associatedText.size() + (this.initialAssociatedText == null ? 0 : this.initialAssociatedText.length);
    }

    private boolean hasAssociatedText() {
        return this.getAssociatedTextLength() > 0;
    }

    private static class ExposedByteArrayOutputStream
    extends ByteArrayOutputStream {
        public byte[] getBuffer() {
            return this.buf;
        }
    }
}

