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
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.modes.AEADBlockCipher;
import org.bouncycastle.crypto.modes.gcm.GCMMultiplier;
import org.bouncycastle.crypto.modes.gcm.Tables4kGCMMultiplier;
import org.bouncycastle.crypto.params.AEADParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Pack;

public class GCMSIVBlockCipher
implements AEADBlockCipher {
    private static final int BUFLEN = 16;
    private static final int HALFBUFLEN = 8;
    private static final int NONCELEN = 12;
    private static final int MAX_DATALEN = 0x7FFFFFE7;
    private static final byte MASK = -128;
    private static final byte ADD = -31;
    private static final int INIT = 1;
    private static final int AEAD_COMPLETE = 2;
    private final BlockCipher theCipher;
    private final GCMMultiplier theMultiplier;
    private final byte[] theGHash = new byte[16];
    private final byte[] theReverse = new byte[16];
    private final GCMSIVHasher theAEADHasher;
    private final GCMSIVHasher theDataHasher;
    private GCMSIVCache thePlain;
    private GCMSIVCache theEncData;
    private boolean forEncryption;
    private byte[] theInitialAEAD;
    private byte[] theNonce;
    private int theFlags;
    private byte[] macBlock = new byte[16];

    public GCMSIVBlockCipher() {
        this(AESEngine.newInstance());
    }

    public GCMSIVBlockCipher(BlockCipher blockCipher) {
        this(blockCipher, new Tables4kGCMMultiplier());
    }

    public GCMSIVBlockCipher(BlockCipher blockCipher, GCMMultiplier gCMMultiplier) {
        if (blockCipher.getBlockSize() != 16) {
            throw new IllegalArgumentException("Cipher required with a block size of 16.");
        }
        this.theCipher = blockCipher;
        this.theMultiplier = gCMMultiplier;
        this.theAEADHasher = new GCMSIVHasher();
        this.theDataHasher = new GCMSIVHasher();
    }

    @Override
    public BlockCipher getUnderlyingCipher() {
        return this.theCipher;
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) throws IllegalArgumentException {
        byte[] byArray = null;
        byte[] byArray2 = null;
        KeyParameter keyParameter = null;
        if (cipherParameters instanceof AEADParameters) {
            AEADParameters aEADParameters = (AEADParameters)cipherParameters;
            byArray = aEADParameters.getAssociatedText();
            byArray2 = aEADParameters.getNonce();
            keyParameter = aEADParameters.getKey();
        } else if (cipherParameters instanceof ParametersWithIV) {
            ParametersWithIV parametersWithIV = (ParametersWithIV)cipherParameters;
            byArray2 = parametersWithIV.getIV();
            keyParameter = (KeyParameter)parametersWithIV.getParameters();
        } else {
            throw new IllegalArgumentException("invalid parameters passed to GCM-SIV");
        }
        if (byArray2 == null || byArray2.length != 12) {
            throw new IllegalArgumentException("Invalid nonce");
        }
        if (keyParameter == null || keyParameter.getKeyLength() != 16 && keyParameter.getKeyLength() != 32) {
            throw new IllegalArgumentException("Invalid key");
        }
        this.forEncryption = bl;
        this.theInitialAEAD = byArray;
        this.theNonce = byArray2;
        this.deriveKeys(keyParameter);
        this.resetStreams();
    }

    @Override
    public String getAlgorithmName() {
        return this.theCipher.getAlgorithmName() + "-GCM-SIV";
    }

    private void checkAEADStatus(int n2) {
        if ((this.theFlags & 1) == 0) {
            throw new IllegalStateException("Cipher is not initialised");
        }
        if ((this.theFlags & 2) != 0) {
            throw new IllegalStateException("AEAD data cannot be processed after ordinary data");
        }
        if (this.theAEADHasher.getBytesProcessed() + Long.MIN_VALUE > (long)(0x7FFFFFE7 - n2) + Long.MIN_VALUE) {
            throw new IllegalStateException("AEAD byte count exceeded");
        }
    }

    private void checkStatus(int n2) {
        if ((this.theFlags & 1) == 0) {
            throw new IllegalStateException("Cipher is not initialised");
        }
        if ((this.theFlags & 2) == 0) {
            this.theAEADHasher.completeHash();
            this.theFlags |= 2;
        }
        long l2 = 0x7FFFFFE7L;
        long l3 = this.thePlain.size();
        if (!this.forEncryption) {
            l2 += 16L;
            l3 = this.theEncData.size();
        }
        if (l3 + Long.MIN_VALUE > l2 - (long)n2 + Long.MIN_VALUE) {
            throw new IllegalStateException("byte count exceeded");
        }
    }

    @Override
    public void processAADByte(byte by) {
        this.checkAEADStatus(1);
        this.theAEADHasher.updateHash(by);
    }

    @Override
    public void processAADBytes(byte[] byArray, int n2, int n3) {
        this.checkAEADStatus(n3);
        GCMSIVBlockCipher.checkBuffer(byArray, n2, n3, false);
        this.theAEADHasher.updateHash(byArray, n2, n3);
    }

    @Override
    public int processByte(byte by, byte[] byArray, int n2) throws DataLengthException {
        this.checkStatus(1);
        if (this.forEncryption) {
            this.thePlain.write(by);
            this.theDataHasher.updateHash(by);
        } else {
            this.theEncData.write(by);
        }
        return 0;
    }

    @Override
    public int processBytes(byte[] byArray, int n2, int n3, byte[] byArray2, int n4) throws DataLengthException {
        this.checkStatus(n3);
        GCMSIVBlockCipher.checkBuffer(byArray, n2, n3, false);
        if (this.forEncryption) {
            this.thePlain.write(byArray, n2, n3);
            this.theDataHasher.updateHash(byArray, n2, n3);
        } else {
            this.theEncData.write(byArray, n2, n3);
        }
        return 0;
    }

    @Override
    public int doFinal(byte[] byArray, int n2) throws IllegalStateException, InvalidCipherTextException {
        this.checkStatus(0);
        GCMSIVBlockCipher.checkBuffer(byArray, n2, this.getOutputSize(0), true);
        if (this.forEncryption) {
            byte[] byArray2 = this.calculateTag();
            int n3 = 16 + this.encryptPlain(byArray2, byArray, n2);
            System.arraycopy(byArray2, 0, byArray, n2 + this.thePlain.size(), 16);
            System.arraycopy(byArray2, 0, this.macBlock, 0, this.macBlock.length);
            this.resetStreams();
            return n3;
        }
        this.decryptPlain();
        int n4 = this.thePlain.size();
        byte[] byArray3 = this.thePlain.getBuffer();
        System.arraycopy(byArray3, 0, byArray, n2, n4);
        this.resetStreams();
        return n4;
    }

    @Override
    public byte[] getMac() {
        return Arrays.clone(this.macBlock);
    }

    @Override
    public int getUpdateOutputSize(int n2) {
        return 0;
    }

    @Override
    public int getOutputSize(int n2) {
        if (this.forEncryption) {
            return n2 + this.thePlain.size() + 16;
        }
        int n3 = n2 + this.theEncData.size();
        return n3 > 16 ? n3 - 16 : 0;
    }

    @Override
    public void reset() {
        this.resetStreams();
    }

    private void resetStreams() {
        if (this.thePlain != null) {
            this.thePlain.clearBuffer();
        }
        this.theAEADHasher.reset();
        this.theDataHasher.reset();
        this.thePlain = new GCMSIVCache();
        this.theEncData = this.forEncryption ? null : new GCMSIVCache();
        this.theFlags &= 0xFFFFFFFD;
        Arrays.fill(this.theGHash, (byte)0);
        if (this.theInitialAEAD != null) {
            this.theAEADHasher.updateHash(this.theInitialAEAD, 0, this.theInitialAEAD.length);
        }
    }

    private static int bufLength(byte[] byArray) {
        return byArray == null ? 0 : byArray.length;
    }

    private static void checkBuffer(byte[] byArray, int n2, int n3, boolean bl) {
        boolean bl2;
        int n4 = GCMSIVBlockCipher.bufLength(byArray);
        int n5 = n2 + n3;
        boolean bl3 = bl2 = n3 < 0 || n2 < 0 || n5 < 0;
        if (bl2 || n5 > n4) {
            throw bl ? new OutputLengthException("Output buffer too short.") : new DataLengthException("Input buffer too short.");
        }
    }

    private int encryptPlain(byte[] byArray, byte[] byArray2, int n2) {
        byte[] byArray3 = this.thePlain.getBuffer();
        byte[] byArray4 = Arrays.clone(byArray);
        byArray4[15] = (byte)(byArray4[15] | 0xFFFFFF80);
        byte[] byArray5 = new byte[16];
        int n3 = this.thePlain.size();
        int n4 = 0;
        while (n3 > 0) {
            this.theCipher.processBlock(byArray4, 0, byArray5, 0);
            int n5 = Math.min(16, n3);
            GCMSIVBlockCipher.xorBlock(byArray5, byArray3, n4, n5);
            System.arraycopy(byArray5, 0, byArray2, n2 + n4, n5);
            n3 -= n5;
            n4 += n5;
            GCMSIVBlockCipher.incrementCounter(byArray4);
        }
        return this.thePlain.size();
    }

    private void decryptPlain() throws InvalidCipherTextException {
        byte[] byArray = this.theEncData.getBuffer();
        int n2 = this.theEncData.size() - 16;
        if (n2 < 0) {
            throw new InvalidCipherTextException("Data too short");
        }
        byte[] byArray2 = Arrays.copyOfRange(byArray, n2, n2 + 16);
        byte[] byArray3 = Arrays.clone(byArray2);
        byArray3[15] = (byte)(byArray3[15] | 0xFFFFFF80);
        byte[] byArray4 = new byte[16];
        int n3 = 0;
        while (n2 > 0) {
            this.theCipher.processBlock(byArray3, 0, byArray4, 0);
            int n4 = Math.min(16, n2);
            GCMSIVBlockCipher.xorBlock(byArray4, byArray, n3, n4);
            this.thePlain.write(byArray4, 0, n4);
            this.theDataHasher.updateHash(byArray4, 0, n4);
            n2 -= n4;
            n3 += n4;
            GCMSIVBlockCipher.incrementCounter(byArray3);
        }
        byte[] byArray5 = this.calculateTag();
        if (!Arrays.constantTimeAreEqual(byArray5, byArray2)) {
            this.reset();
            throw new InvalidCipherTextException("mac check failed");
        }
        System.arraycopy(byArray5, 0, this.macBlock, 0, this.macBlock.length);
    }

    private byte[] calculateTag() {
        this.theDataHasher.completeHash();
        byte[] byArray = this.completePolyVal();
        byte[] byArray2 = new byte[16];
        for (int i2 = 0; i2 < 12; ++i2) {
            int n2 = i2;
            byArray[n2] = (byte)(byArray[n2] ^ this.theNonce[i2]);
        }
        byArray[15] = (byte)(byArray[15] & 0xFFFFFF7F);
        this.theCipher.processBlock(byArray, 0, byArray2, 0);
        return byArray2;
    }

    private byte[] completePolyVal() {
        byte[] byArray = new byte[16];
        this.gHashLengths();
        GCMSIVBlockCipher.fillReverse(this.theGHash, 0, 16, byArray);
        return byArray;
    }

    private void gHashLengths() {
        byte[] byArray = new byte[16];
        Pack.longToBigEndian(8L * this.theDataHasher.getBytesProcessed(), byArray, 0);
        Pack.longToBigEndian(8L * this.theAEADHasher.getBytesProcessed(), byArray, 8);
        this.gHASH(byArray);
    }

    private void gHASH(byte[] byArray) {
        GCMSIVBlockCipher.xorBlock(this.theGHash, byArray);
        this.theMultiplier.multiplyH(this.theGHash);
    }

    private static void fillReverse(byte[] byArray, int n2, int n3, byte[] byArray2) {
        int n4 = 0;
        int n5 = 15;
        while (n4 < n3) {
            byArray2[n5] = byArray[n2 + n4];
            ++n4;
            --n5;
        }
    }

    private static void xorBlock(byte[] byArray, byte[] byArray2) {
        for (int i2 = 0; i2 < 16; ++i2) {
            int n2 = i2;
            byArray[n2] = (byte)(byArray[n2] ^ byArray2[i2]);
        }
    }

    private static void xorBlock(byte[] byArray, byte[] byArray2, int n2, int n3) {
        for (int i2 = 0; i2 < n3; ++i2) {
            int n4 = i2;
            byArray[n4] = (byte)(byArray[n4] ^ byArray2[i2 + n2]);
        }
    }

    private static void incrementCounter(byte[] byArray) {
        int n2 = 0;
        while (n2 < 4) {
            int n3 = n2++;
            byArray[n3] = (byte)(byArray[n3] + 1);
            if (byArray[n3] != 0) break;
        }
    }

    private static void mulX(byte[] byArray) {
        int n2 = 0;
        for (int i2 = 0; i2 < 16; ++i2) {
            byte by = byArray[i2];
            byArray[i2] = (byte)(by >> 1 & 0x7F | n2);
            n2 = (by & 1) == 0 ? 0 : -128;
        }
        if (n2 != 0) {
            byArray[0] = (byte)(byArray[0] ^ 0xFFFFFFE1);
        }
    }

    private void deriveKeys(KeyParameter keyParameter) {
        byte[] byArray = new byte[16];
        byte[] byArray2 = new byte[16];
        byte[] byArray3 = new byte[16];
        byte[] byArray4 = new byte[keyParameter.getKeyLength()];
        System.arraycopy(this.theNonce, 0, byArray, 4, 12);
        this.theCipher.init(true, keyParameter);
        int n2 = 0;
        this.theCipher.processBlock(byArray, 0, byArray2, 0);
        System.arraycopy(byArray2, 0, byArray3, n2, 8);
        byArray[0] = (byte)(byArray[0] + 1);
        this.theCipher.processBlock(byArray, 0, byArray2, 0);
        System.arraycopy(byArray2, 0, byArray3, n2 += 8, 8);
        byArray[0] = (byte)(byArray[0] + 1);
        n2 = 0;
        this.theCipher.processBlock(byArray, 0, byArray2, 0);
        System.arraycopy(byArray2, 0, byArray4, n2, 8);
        byArray[0] = (byte)(byArray[0] + 1);
        this.theCipher.processBlock(byArray, 0, byArray2, 0);
        System.arraycopy(byArray2, 0, byArray4, n2 += 8, 8);
        if (byArray4.length == 32) {
            byArray[0] = (byte)(byArray[0] + 1);
            this.theCipher.processBlock(byArray, 0, byArray2, 0);
            System.arraycopy(byArray2, 0, byArray4, n2 += 8, 8);
            byArray[0] = (byte)(byArray[0] + 1);
            this.theCipher.processBlock(byArray, 0, byArray2, 0);
            System.arraycopy(byArray2, 0, byArray4, n2 += 8, 8);
        }
        this.theCipher.init(true, new KeyParameter(byArray4));
        GCMSIVBlockCipher.fillReverse(byArray3, 0, 16, byArray2);
        GCMSIVBlockCipher.mulX(byArray2);
        this.theMultiplier.init(byArray2);
        this.theFlags |= 1;
    }

    private static class GCMSIVCache
    extends ByteArrayOutputStream {
        GCMSIVCache() {
        }

        byte[] getBuffer() {
            return this.buf;
        }

        void clearBuffer() {
            Arrays.fill(this.getBuffer(), (byte)0);
        }
    }

    private class GCMSIVHasher {
        private final byte[] theBuffer = new byte[16];
        private final byte[] theByte = new byte[1];
        private int numActive;
        private long numHashed;

        private GCMSIVHasher() {
        }

        long getBytesProcessed() {
            return this.numHashed;
        }

        void reset() {
            this.numActive = 0;
            this.numHashed = 0L;
        }

        void updateHash(byte by) {
            this.theByte[0] = by;
            this.updateHash(this.theByte, 0, 1);
        }

        void updateHash(byte[] byArray, int n2, int n3) {
            int n4 = 16 - this.numActive;
            int n5 = 0;
            int n6 = n3;
            if (this.numActive > 0 && n3 >= n4) {
                System.arraycopy(byArray, n2, this.theBuffer, this.numActive, n4);
                GCMSIVBlockCipher.fillReverse(this.theBuffer, 0, 16, GCMSIVBlockCipher.this.theReverse);
                GCMSIVBlockCipher.this.gHASH(GCMSIVBlockCipher.this.theReverse);
                n5 += n4;
                n6 -= n4;
                this.numActive = 0;
            }
            while (n6 >= 16) {
                GCMSIVBlockCipher.fillReverse(byArray, n2 + n5, 16, GCMSIVBlockCipher.this.theReverse);
                GCMSIVBlockCipher.this.gHASH(GCMSIVBlockCipher.this.theReverse);
                n5 += 16;
                n6 -= 16;
            }
            if (n6 > 0) {
                System.arraycopy(byArray, n2 + n5, this.theBuffer, this.numActive, n6);
                this.numActive += n6;
            }
            this.numHashed += (long)n3;
        }

        void completeHash() {
            if (this.numActive > 0) {
                Arrays.fill(GCMSIVBlockCipher.this.theReverse, (byte)0);
                GCMSIVBlockCipher.fillReverse(this.theBuffer, 0, this.numActive, GCMSIVBlockCipher.this.theReverse);
                GCMSIVBlockCipher.this.gHASH(GCMSIVBlockCipher.this.theReverse);
            }
        }
    }
}

