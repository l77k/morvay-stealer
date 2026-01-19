/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.modes;

import java.io.ByteArrayOutputStream;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.modes.AEADBlockCipher;
import org.bouncycastle.crypto.modes.KCTRBlockCipher;
import org.bouncycastle.crypto.modes.kgcm.KGCMMultiplier;
import org.bouncycastle.crypto.modes.kgcm.Tables16kKGCMMultiplier_512;
import org.bouncycastle.crypto.modes.kgcm.Tables4kKGCMMultiplier_128;
import org.bouncycastle.crypto.modes.kgcm.Tables8kKGCMMultiplier_256;
import org.bouncycastle.crypto.params.AEADParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Pack;

public class KGCMBlockCipher
implements AEADBlockCipher {
    private static final int MIN_MAC_BITS = 64;
    private BlockCipher engine;
    private BufferedBlockCipher ctrEngine;
    private int macSize;
    private boolean forEncryption;
    private byte[] initialAssociatedText;
    private byte[] macBlock;
    private byte[] iv;
    private KGCMMultiplier multiplier;
    private long[] b;
    private final int blockSize;
    private ExposedByteArrayOutputStream associatedText = new ExposedByteArrayOutputStream();
    private ExposedByteArrayOutputStream data = new ExposedByteArrayOutputStream();

    private static KGCMMultiplier createDefaultMultiplier(int n2) {
        switch (n2) {
            case 16: {
                return new Tables4kKGCMMultiplier_128();
            }
            case 32: {
                return new Tables8kKGCMMultiplier_256();
            }
            case 64: {
                return new Tables16kKGCMMultiplier_512();
            }
        }
        throw new IllegalArgumentException("Only 128, 256, and 512 -bit block sizes supported");
    }

    public KGCMBlockCipher(BlockCipher blockCipher) {
        this.engine = blockCipher;
        this.ctrEngine = new BufferedBlockCipher(new KCTRBlockCipher(this.engine));
        this.macSize = -1;
        this.blockSize = this.engine.getBlockSize();
        this.initialAssociatedText = new byte[this.blockSize];
        this.iv = new byte[this.blockSize];
        this.multiplier = KGCMBlockCipher.createDefaultMultiplier(this.blockSize);
        this.b = new long[this.blockSize >>> 3];
        this.macBlock = null;
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) throws IllegalArgumentException {
        KeyParameter keyParameter;
        this.forEncryption = bl;
        if (cipherParameters instanceof AEADParameters) {
            AEADParameters aEADParameters = (AEADParameters)cipherParameters;
            byte[] byArray = aEADParameters.getNonce();
            int n2 = this.iv.length - byArray.length;
            Arrays.fill(this.iv, (byte)0);
            System.arraycopy(byArray, 0, this.iv, n2, byArray.length);
            this.initialAssociatedText = aEADParameters.getAssociatedText();
            int n3 = aEADParameters.getMacSize();
            if (n3 < 64 || n3 > this.blockSize << 3 || (n3 & 7) != 0) {
                throw new IllegalArgumentException("Invalid value for MAC size: " + n3);
            }
            this.macSize = n3 >>> 3;
            keyParameter = aEADParameters.getKey();
            if (this.initialAssociatedText != null) {
                this.processAADBytes(this.initialAssociatedText, 0, this.initialAssociatedText.length);
            }
        } else if (cipherParameters instanceof ParametersWithIV) {
            ParametersWithIV parametersWithIV = (ParametersWithIV)cipherParameters;
            byte[] byArray = parametersWithIV.getIV();
            int n4 = this.iv.length - byArray.length;
            Arrays.fill(this.iv, (byte)0);
            System.arraycopy(byArray, 0, this.iv, n4, byArray.length);
            this.initialAssociatedText = null;
            this.macSize = this.blockSize;
            keyParameter = (KeyParameter)parametersWithIV.getParameters();
        } else {
            throw new IllegalArgumentException("Invalid parameter passed");
        }
        this.macBlock = new byte[this.blockSize];
        this.ctrEngine.init(true, new ParametersWithIV(keyParameter, this.iv));
        this.engine.init(true, keyParameter);
    }

    @Override
    public String getAlgorithmName() {
        return this.engine.getAlgorithmName() + "/KGCM";
    }

    @Override
    public BlockCipher getUnderlyingCipher() {
        return this.engine;
    }

    @Override
    public void processAADByte(byte by) {
        this.associatedText.write(by);
    }

    @Override
    public void processAADBytes(byte[] byArray, int n2, int n3) {
        this.associatedText.write(byArray, n2, n3);
    }

    private void processAAD(byte[] byArray, int n2, int n3) {
        int n4 = n2 + n3;
        for (int i2 = n2; i2 < n4; i2 += this.blockSize) {
            KGCMBlockCipher.xorWithInput(this.b, byArray, i2);
            this.multiplier.multiplyH(this.b);
        }
    }

    @Override
    public int processByte(byte by, byte[] byArray, int n2) throws DataLengthException, IllegalStateException {
        this.data.write(by);
        return 0;
    }

    @Override
    public int processBytes(byte[] byArray, int n2, int n3, byte[] byArray2, int n4) throws DataLengthException, IllegalStateException {
        if (byArray.length < n2 + n3) {
            throw new DataLengthException("input buffer too short");
        }
        this.data.write(byArray, n2, n3);
        return 0;
    }

    @Override
    public int doFinal(byte[] byArray, int n2) throws IllegalStateException, InvalidCipherTextException {
        int n3;
        int n4 = this.data.size();
        if (!this.forEncryption && n4 < this.macSize) {
            throw new InvalidCipherTextException("data too short");
        }
        byte[] byArray2 = new byte[this.blockSize];
        this.engine.processBlock(byArray2, 0, byArray2, 0);
        long[] lArray = new long[this.blockSize >>> 3];
        Pack.littleEndianToLong(byArray2, 0, lArray);
        this.multiplier.init(lArray);
        Arrays.fill(byArray2, (byte)0);
        Arrays.fill(lArray, 0L);
        int n5 = this.associatedText.size();
        if (n5 > 0) {
            this.processAAD(this.associatedText.getBuffer(), 0, n5);
        }
        if (this.forEncryption) {
            if (byArray.length - n2 - this.macSize < n4) {
                throw new OutputLengthException("Output buffer too short");
            }
            n3 = this.ctrEngine.processBytes(this.data.getBuffer(), 0, n4, byArray, n2);
            n3 += this.ctrEngine.doFinal(byArray, n2 + n3);
            this.calculateMac(byArray, n2, n4, n5);
        } else {
            int n6 = n4 - this.macSize;
            if (byArray.length - n2 < n6) {
                throw new OutputLengthException("Output buffer too short");
            }
            this.calculateMac(this.data.getBuffer(), 0, n6, n5);
            n3 = this.ctrEngine.processBytes(this.data.getBuffer(), 0, n6, byArray, n2);
            n3 += this.ctrEngine.doFinal(byArray, n2 + n3);
        }
        if (this.macBlock == null) {
            throw new IllegalStateException("mac is not calculated");
        }
        if (this.forEncryption) {
            System.arraycopy(this.macBlock, 0, byArray, n2 + n3, this.macSize);
            this.reset();
            return n3 + this.macSize;
        }
        byte[] byArray3 = new byte[this.macSize];
        System.arraycopy(this.data.getBuffer(), n4 - this.macSize, byArray3, 0, this.macSize);
        byte[] byArray4 = new byte[this.macSize];
        System.arraycopy(this.macBlock, 0, byArray4, 0, this.macSize);
        if (!Arrays.constantTimeAreEqual(byArray3, byArray4)) {
            throw new InvalidCipherTextException("mac verification failed");
        }
        this.reset();
        return n3;
    }

    @Override
    public byte[] getMac() {
        byte[] byArray = new byte[this.macSize];
        System.arraycopy(this.macBlock, 0, byArray, 0, this.macSize);
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

    @Override
    public void reset() {
        Arrays.fill(this.b, 0L);
        this.engine.reset();
        this.data.reset();
        this.associatedText.reset();
        if (this.initialAssociatedText != null) {
            this.processAADBytes(this.initialAssociatedText, 0, this.initialAssociatedText.length);
        }
    }

    private void calculateMac(byte[] byArray, int n2, int n3, int n4) {
        int n5 = n2 + n3;
        for (int i2 = n2; i2 < n5; i2 += this.blockSize) {
            KGCMBlockCipher.xorWithInput(this.b, byArray, i2);
            this.multiplier.multiplyH(this.b);
        }
        long l2 = ((long)n4 & 0xFFFFFFFFL) << 3;
        long l3 = ((long)n3 & 0xFFFFFFFFL) << 3;
        this.b[0] = this.b[0] ^ l2;
        int n6 = this.blockSize >>> 4;
        this.b[n6] = this.b[n6] ^ l3;
        this.macBlock = Pack.longToLittleEndian(this.b);
        this.engine.processBlock(this.macBlock, 0, this.macBlock, 0);
    }

    private static void xorWithInput(long[] lArray, byte[] byArray, int n2) {
        int n3 = 0;
        while (n3 < lArray.length) {
            int n4 = n3++;
            lArray[n4] = lArray[n4] ^ Pack.littleEndianToLong(byArray, n2);
            n2 += 8;
        }
    }

    private static class ExposedByteArrayOutputStream
    extends ByteArrayOutputStream {
        public byte[] getBuffer() {
            return this.buf;
        }
    }
}

