/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.CryptoServicesRegistrar;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.constraints.DefaultServiceProperties;
import org.bouncycastle.crypto.engines.Utils;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.TweakableBlockCipherParameters;
import org.bouncycastle.util.Pack;

public class ThreefishEngine
implements BlockCipher {
    public static final int BLOCKSIZE_256 = 256;
    public static final int BLOCKSIZE_512 = 512;
    public static final int BLOCKSIZE_1024 = 1024;
    private static final int TWEAK_SIZE_BYTES = 16;
    private static final int TWEAK_SIZE_WORDS = 2;
    private static final int ROUNDS_256 = 72;
    private static final int ROUNDS_512 = 72;
    private static final int ROUNDS_1024 = 80;
    private static final int MAX_ROUNDS = 80;
    private static final long C_240 = 2004413935125273122L;
    private static int[] MOD9 = new int[80];
    private static int[] MOD17 = new int[MOD9.length];
    private static int[] MOD5 = new int[MOD9.length];
    private static int[] MOD3 = new int[MOD9.length];
    private int blocksizeBytes;
    private int blocksizeWords;
    private long[] currentBlock;
    private long[] t = new long[5];
    private long[] kw;
    private ThreefishCipher cipher;
    private boolean forEncryption;

    public ThreefishEngine(int n2) {
        this.blocksizeBytes = n2 / 8;
        this.blocksizeWords = this.blocksizeBytes / 8;
        this.currentBlock = new long[this.blocksizeWords];
        this.kw = new long[2 * this.blocksizeWords + 1];
        switch (n2) {
            case 256: {
                this.cipher = new Threefish256Cipher(this.kw, this.t);
                break;
            }
            case 512: {
                this.cipher = new Threefish512Cipher(this.kw, this.t);
                break;
            }
            case 1024: {
                this.cipher = new Threefish1024Cipher(this.kw, this.t);
                break;
            }
            default: {
                throw new IllegalArgumentException("Invalid blocksize - Threefish is defined with block size of 256, 512, or 1024 bits");
            }
        }
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) throws IllegalArgumentException {
        byte[] byArray;
        byte[] byArray2;
        Object object;
        if (cipherParameters instanceof TweakableBlockCipherParameters) {
            object = (TweakableBlockCipherParameters)cipherParameters;
            byArray2 = ((TweakableBlockCipherParameters)object).getKey().getKey();
            byArray = ((TweakableBlockCipherParameters)object).getTweak();
        } else if (cipherParameters instanceof KeyParameter) {
            byArray2 = ((KeyParameter)cipherParameters).getKey();
            byArray = null;
        } else {
            throw new IllegalArgumentException("Invalid parameter passed to Threefish init - " + cipherParameters.getClass().getName());
        }
        object = null;
        long[] lArray = null;
        if (byArray2 != null) {
            if (byArray2.length != this.blocksizeBytes) {
                throw new IllegalArgumentException("Threefish key must be same size as block (" + this.blocksizeBytes + " bytes)");
            }
            object = new long[this.blocksizeWords];
            Pack.littleEndianToLong(byArray2, 0, (long[])object);
        }
        if (byArray != null) {
            if (byArray.length != 16) {
                throw new IllegalArgumentException("Threefish tweak must be 16 bytes");
            }
            lArray = new long[2];
            Pack.littleEndianToLong(byArray, 0, lArray);
        }
        this.init(bl, (long[])object, lArray);
        CryptoServicesRegistrar.checkConstraints(new DefaultServiceProperties(this.getAlgorithmName(), 256, cipherParameters, Utils.getPurpose(bl)));
    }

    public void init(boolean bl, long[] lArray, long[] lArray2) {
        this.forEncryption = bl;
        if (lArray != null) {
            this.setKey(lArray);
        }
        if (lArray2 != null) {
            this.setTweak(lArray2);
        }
    }

    private void setKey(long[] lArray) {
        if (lArray.length != this.blocksizeWords) {
            throw new IllegalArgumentException("Threefish key must be same size as block (" + this.blocksizeWords + " words)");
        }
        long l2 = 2004413935125273122L;
        for (int i2 = 0; i2 < this.blocksizeWords; ++i2) {
            this.kw[i2] = lArray[i2];
            l2 ^= this.kw[i2];
        }
        this.kw[this.blocksizeWords] = l2;
        System.arraycopy(this.kw, 0, this.kw, this.blocksizeWords + 1, this.blocksizeWords);
    }

    private void setTweak(long[] lArray) {
        if (lArray.length != 2) {
            throw new IllegalArgumentException("Tweak must be 2 words.");
        }
        this.t[0] = lArray[0];
        this.t[1] = lArray[1];
        this.t[2] = this.t[0] ^ this.t[1];
        this.t[3] = this.t[0];
        this.t[4] = this.t[1];
    }

    @Override
    public String getAlgorithmName() {
        return "Threefish-" + this.blocksizeBytes * 8;
    }

    @Override
    public int getBlockSize() {
        return this.blocksizeBytes;
    }

    @Override
    public void reset() {
    }

    @Override
    public int processBlock(byte[] byArray, int n2, byte[] byArray2, int n3) throws DataLengthException, IllegalStateException {
        if (n2 + this.blocksizeBytes > byArray.length) {
            throw new DataLengthException("Input buffer too short");
        }
        if (n3 + this.blocksizeBytes > byArray2.length) {
            throw new OutputLengthException("Output buffer too short");
        }
        Pack.littleEndianToLong(byArray, n2, this.currentBlock);
        this.processBlock(this.currentBlock, this.currentBlock);
        Pack.longToLittleEndian(this.currentBlock, byArray2, n3);
        return this.blocksizeBytes;
    }

    public int processBlock(long[] lArray, long[] lArray2) throws DataLengthException, IllegalStateException {
        if (this.kw[this.blocksizeWords] == 0L) {
            throw new IllegalStateException("Threefish engine not initialised");
        }
        if (lArray.length != this.blocksizeWords) {
            throw new DataLengthException("Input buffer too short");
        }
        if (lArray2.length != this.blocksizeWords) {
            throw new OutputLengthException("Output buffer too short");
        }
        if (this.forEncryption) {
            this.cipher.encryptBlock(lArray, lArray2);
        } else {
            this.cipher.decryptBlock(lArray, lArray2);
        }
        return this.blocksizeWords;
    }

    public static long bytesToWord(byte[] byArray, int n2) {
        return Pack.littleEndianToLong(byArray, n2);
    }

    public static void wordToBytes(long l2, byte[] byArray, int n2) {
        Pack.longToLittleEndian(l2, byArray, n2);
    }

    static long rotlXor(long l2, int n2, long l3) {
        return (l2 << n2 | l2 >>> -n2) ^ l3;
    }

    static long xorRotr(long l2, int n2, long l3) {
        long l4 = l2 ^ l3;
        return l4 >>> n2 | l4 << -n2;
    }

    static {
        for (int i2 = 0; i2 < MOD9.length; ++i2) {
            ThreefishEngine.MOD17[i2] = i2 % 17;
            ThreefishEngine.MOD9[i2] = i2 % 9;
            ThreefishEngine.MOD5[i2] = i2 % 5;
            ThreefishEngine.MOD3[i2] = i2 % 3;
        }
    }

    private static final class Threefish1024Cipher
    extends ThreefishCipher {
        private static final int ROTATION_0_0 = 24;
        private static final int ROTATION_0_1 = 13;
        private static final int ROTATION_0_2 = 8;
        private static final int ROTATION_0_3 = 47;
        private static final int ROTATION_0_4 = 8;
        private static final int ROTATION_0_5 = 17;
        private static final int ROTATION_0_6 = 22;
        private static final int ROTATION_0_7 = 37;
        private static final int ROTATION_1_0 = 38;
        private static final int ROTATION_1_1 = 19;
        private static final int ROTATION_1_2 = 10;
        private static final int ROTATION_1_3 = 55;
        private static final int ROTATION_1_4 = 49;
        private static final int ROTATION_1_5 = 18;
        private static final int ROTATION_1_6 = 23;
        private static final int ROTATION_1_7 = 52;
        private static final int ROTATION_2_0 = 33;
        private static final int ROTATION_2_1 = 4;
        private static final int ROTATION_2_2 = 51;
        private static final int ROTATION_2_3 = 13;
        private static final int ROTATION_2_4 = 34;
        private static final int ROTATION_2_5 = 41;
        private static final int ROTATION_2_6 = 59;
        private static final int ROTATION_2_7 = 17;
        private static final int ROTATION_3_0 = 5;
        private static final int ROTATION_3_1 = 20;
        private static final int ROTATION_3_2 = 48;
        private static final int ROTATION_3_3 = 41;
        private static final int ROTATION_3_4 = 47;
        private static final int ROTATION_3_5 = 28;
        private static final int ROTATION_3_6 = 16;
        private static final int ROTATION_3_7 = 25;
        private static final int ROTATION_4_0 = 41;
        private static final int ROTATION_4_1 = 9;
        private static final int ROTATION_4_2 = 37;
        private static final int ROTATION_4_3 = 31;
        private static final int ROTATION_4_4 = 12;
        private static final int ROTATION_4_5 = 47;
        private static final int ROTATION_4_6 = 44;
        private static final int ROTATION_4_7 = 30;
        private static final int ROTATION_5_0 = 16;
        private static final int ROTATION_5_1 = 34;
        private static final int ROTATION_5_2 = 56;
        private static final int ROTATION_5_3 = 51;
        private static final int ROTATION_5_4 = 4;
        private static final int ROTATION_5_5 = 53;
        private static final int ROTATION_5_6 = 42;
        private static final int ROTATION_5_7 = 41;
        private static final int ROTATION_6_0 = 31;
        private static final int ROTATION_6_1 = 44;
        private static final int ROTATION_6_2 = 47;
        private static final int ROTATION_6_3 = 46;
        private static final int ROTATION_6_4 = 19;
        private static final int ROTATION_6_5 = 42;
        private static final int ROTATION_6_6 = 44;
        private static final int ROTATION_6_7 = 25;
        private static final int ROTATION_7_0 = 9;
        private static final int ROTATION_7_1 = 48;
        private static final int ROTATION_7_2 = 35;
        private static final int ROTATION_7_3 = 52;
        private static final int ROTATION_7_4 = 23;
        private static final int ROTATION_7_5 = 31;
        private static final int ROTATION_7_6 = 37;
        private static final int ROTATION_7_7 = 20;

        public Threefish1024Cipher(long[] lArray, long[] lArray2) {
            super(lArray, lArray2);
        }

        @Override
        void encryptBlock(long[] lArray, long[] lArray2) {
            long[] lArray3 = this.kw;
            long[] lArray4 = this.t;
            int[] nArray = MOD17;
            int[] nArray2 = MOD3;
            if (lArray3.length != 33) {
                throw new IllegalArgumentException();
            }
            if (lArray4.length != 5) {
                throw new IllegalArgumentException();
            }
            long l2 = lArray[0];
            long l3 = lArray[1];
            long l4 = lArray[2];
            long l5 = lArray[3];
            long l6 = lArray[4];
            long l7 = lArray[5];
            long l8 = lArray[6];
            long l9 = lArray[7];
            long l10 = lArray[8];
            long l11 = lArray[9];
            long l12 = lArray[10];
            long l13 = lArray[11];
            long l14 = lArray[12];
            long l15 = lArray[13];
            long l16 = lArray[14];
            long l17 = lArray[15];
            l2 += lArray3[0];
            l3 += lArray3[1];
            l4 += lArray3[2];
            l5 += lArray3[3];
            l6 += lArray3[4];
            l7 += lArray3[5];
            l8 += lArray3[6];
            l9 += lArray3[7];
            l10 += lArray3[8];
            l11 += lArray3[9];
            l12 += lArray3[10];
            l13 += lArray3[11];
            l14 += lArray3[12];
            l15 += lArray3[13] + lArray4[0];
            l16 += lArray3[14] + lArray4[1];
            l17 += lArray3[15];
            for (int i2 = 1; i2 < 20; i2 += 2) {
                int n2 = nArray[i2];
                int n3 = nArray2[i2];
                l2 += l3;
                l3 = ThreefishEngine.rotlXor(l3, 24, l2);
                l4 += l5;
                l5 = ThreefishEngine.rotlXor(l5, 13, l4);
                l6 += l7;
                l7 = ThreefishEngine.rotlXor(l7, 8, l6);
                l8 += l9;
                l9 = ThreefishEngine.rotlXor(l9, 47, l8);
                l10 += l11;
                l11 = ThreefishEngine.rotlXor(l11, 8, l10);
                l12 += l13;
                l13 = ThreefishEngine.rotlXor(l13, 17, l12);
                l14 += l15;
                l15 = ThreefishEngine.rotlXor(l15, 22, l14);
                l16 += l17;
                l17 = ThreefishEngine.rotlXor(l17, 37, l16);
                l2 += l11;
                l11 = ThreefishEngine.rotlXor(l11, 38, l2);
                l4 += l15;
                l15 = ThreefishEngine.rotlXor(l15, 19, l4);
                l8 += l13;
                l13 = ThreefishEngine.rotlXor(l13, 10, l8);
                l6 += l17;
                l17 = ThreefishEngine.rotlXor(l17, 55, l6);
                l12 += l9;
                l9 = ThreefishEngine.rotlXor(l9, 49, l12);
                l14 += l5;
                l5 = ThreefishEngine.rotlXor(l5, 18, l14);
                l16 += l7;
                l7 = ThreefishEngine.rotlXor(l7, 23, l16);
                l10 += l3;
                l3 = ThreefishEngine.rotlXor(l3, 52, l10);
                l2 += l9;
                l9 = ThreefishEngine.rotlXor(l9, 33, l2);
                l4 += l7;
                l7 = ThreefishEngine.rotlXor(l7, 4, l4);
                l6 += l5;
                l5 = ThreefishEngine.rotlXor(l5, 51, l6);
                l8 += l3;
                l3 = ThreefishEngine.rotlXor(l3, 13, l8);
                l14 += l17;
                l17 = ThreefishEngine.rotlXor(l17, 34, l14);
                l16 += l15;
                l15 = ThreefishEngine.rotlXor(l15, 41, l16);
                l10 += l13;
                l13 = ThreefishEngine.rotlXor(l13, 59, l10);
                l12 += l11;
                l11 = ThreefishEngine.rotlXor(l11, 17, l12);
                l2 += l17;
                l17 = ThreefishEngine.rotlXor(l17, 5, l2);
                l4 += l13;
                l13 = ThreefishEngine.rotlXor(l13, 20, l4);
                l8 += l15;
                l15 = ThreefishEngine.rotlXor(l15, 48, l8);
                l6 += l11;
                l11 = ThreefishEngine.rotlXor(l11, 41, l6);
                l16 += l3;
                l3 = ThreefishEngine.rotlXor(l3, 47, l16);
                l10 += l7;
                l7 = ThreefishEngine.rotlXor(l7, 28, l10);
                l12 += l5;
                l5 = ThreefishEngine.rotlXor(l5, 16, l12);
                l14 += l9;
                l9 = ThreefishEngine.rotlXor(l9, 25, l14);
                l2 += lArray3[n2];
                l3 += lArray3[n2 + 1];
                l4 += lArray3[n2 + 2];
                l5 += lArray3[n2 + 3];
                l6 += lArray3[n2 + 4];
                l7 += lArray3[n2 + 5];
                l8 += lArray3[n2 + 6];
                l9 += lArray3[n2 + 7];
                l10 += lArray3[n2 + 8];
                l11 += lArray3[n2 + 9];
                l12 += lArray3[n2 + 10];
                l13 += lArray3[n2 + 11];
                l14 += lArray3[n2 + 12];
                l15 += lArray3[n2 + 13] + lArray4[n3];
                l16 += lArray3[n2 + 14] + lArray4[n3 + 1];
                l17 += lArray3[n2 + 15] + (long)i2;
                l2 += l3;
                l3 = ThreefishEngine.rotlXor(l3, 41, l2);
                l4 += l5;
                l5 = ThreefishEngine.rotlXor(l5, 9, l4);
                l6 += l7;
                l7 = ThreefishEngine.rotlXor(l7, 37, l6);
                l8 += l9;
                l9 = ThreefishEngine.rotlXor(l9, 31, l8);
                l10 += l11;
                l11 = ThreefishEngine.rotlXor(l11, 12, l10);
                l12 += l13;
                l13 = ThreefishEngine.rotlXor(l13, 47, l12);
                l14 += l15;
                l15 = ThreefishEngine.rotlXor(l15, 44, l14);
                l16 += l17;
                l17 = ThreefishEngine.rotlXor(l17, 30, l16);
                l2 += l11;
                l11 = ThreefishEngine.rotlXor(l11, 16, l2);
                l4 += l15;
                l15 = ThreefishEngine.rotlXor(l15, 34, l4);
                l8 += l13;
                l13 = ThreefishEngine.rotlXor(l13, 56, l8);
                l6 += l17;
                l17 = ThreefishEngine.rotlXor(l17, 51, l6);
                l12 += l9;
                l9 = ThreefishEngine.rotlXor(l9, 4, l12);
                l14 += l5;
                l5 = ThreefishEngine.rotlXor(l5, 53, l14);
                l16 += l7;
                l7 = ThreefishEngine.rotlXor(l7, 42, l16);
                l10 += l3;
                l3 = ThreefishEngine.rotlXor(l3, 41, l10);
                l2 += l9;
                l9 = ThreefishEngine.rotlXor(l9, 31, l2);
                l4 += l7;
                l7 = ThreefishEngine.rotlXor(l7, 44, l4);
                l6 += l5;
                l5 = ThreefishEngine.rotlXor(l5, 47, l6);
                l8 += l3;
                l3 = ThreefishEngine.rotlXor(l3, 46, l8);
                l14 += l17;
                l17 = ThreefishEngine.rotlXor(l17, 19, l14);
                l16 += l15;
                l15 = ThreefishEngine.rotlXor(l15, 42, l16);
                l10 += l13;
                l13 = ThreefishEngine.rotlXor(l13, 44, l10);
                l12 += l11;
                l11 = ThreefishEngine.rotlXor(l11, 25, l12);
                l2 += l17;
                l17 = ThreefishEngine.rotlXor(l17, 9, l2);
                l4 += l13;
                l13 = ThreefishEngine.rotlXor(l13, 48, l4);
                l8 += l15;
                l15 = ThreefishEngine.rotlXor(l15, 35, l8);
                l6 += l11;
                l11 = ThreefishEngine.rotlXor(l11, 52, l6);
                l16 += l3;
                l3 = ThreefishEngine.rotlXor(l3, 23, l16);
                l10 += l7;
                l7 = ThreefishEngine.rotlXor(l7, 31, l10);
                l12 += l5;
                l5 = ThreefishEngine.rotlXor(l5, 37, l12);
                l14 += l9;
                l9 = ThreefishEngine.rotlXor(l9, 20, l14);
                l2 += lArray3[n2 + 1];
                l3 += lArray3[n2 + 2];
                l4 += lArray3[n2 + 3];
                l5 += lArray3[n2 + 4];
                l6 += lArray3[n2 + 5];
                l7 += lArray3[n2 + 6];
                l8 += lArray3[n2 + 7];
                l9 += lArray3[n2 + 8];
                l10 += lArray3[n2 + 9];
                l11 += lArray3[n2 + 10];
                l12 += lArray3[n2 + 11];
                l13 += lArray3[n2 + 12];
                l14 += lArray3[n2 + 13];
                l15 += lArray3[n2 + 14] + lArray4[n3 + 1];
                l16 += lArray3[n2 + 15] + lArray4[n3 + 2];
                l17 += lArray3[n2 + 16] + (long)i2 + 1L;
            }
            lArray2[0] = l2;
            lArray2[1] = l3;
            lArray2[2] = l4;
            lArray2[3] = l5;
            lArray2[4] = l6;
            lArray2[5] = l7;
            lArray2[6] = l8;
            lArray2[7] = l9;
            lArray2[8] = l10;
            lArray2[9] = l11;
            lArray2[10] = l12;
            lArray2[11] = l13;
            lArray2[12] = l14;
            lArray2[13] = l15;
            lArray2[14] = l16;
            lArray2[15] = l17;
        }

        @Override
        void decryptBlock(long[] lArray, long[] lArray2) {
            long[] lArray3 = this.kw;
            long[] lArray4 = this.t;
            int[] nArray = MOD17;
            int[] nArray2 = MOD3;
            if (lArray3.length != 33) {
                throw new IllegalArgumentException();
            }
            if (lArray4.length != 5) {
                throw new IllegalArgumentException();
            }
            long l2 = lArray[0];
            long l3 = lArray[1];
            long l4 = lArray[2];
            long l5 = lArray[3];
            long l6 = lArray[4];
            long l7 = lArray[5];
            long l8 = lArray[6];
            long l9 = lArray[7];
            long l10 = lArray[8];
            long l11 = lArray[9];
            long l12 = lArray[10];
            long l13 = lArray[11];
            long l14 = lArray[12];
            long l15 = lArray[13];
            long l16 = lArray[14];
            long l17 = lArray[15];
            for (int i2 = 19; i2 >= 1; i2 -= 2) {
                int n2 = nArray[i2];
                int n3 = nArray2[i2];
                l2 -= lArray3[n2 + 1];
                l3 -= lArray3[n2 + 2];
                l4 -= lArray3[n2 + 3];
                l5 -= lArray3[n2 + 4];
                l6 -= lArray3[n2 + 5];
                l7 -= lArray3[n2 + 6];
                l8 -= lArray3[n2 + 7];
                l9 -= lArray3[n2 + 8];
                l10 -= lArray3[n2 + 9];
                l11 -= lArray3[n2 + 10];
                l12 -= lArray3[n2 + 11];
                l13 -= lArray3[n2 + 12];
                l14 -= lArray3[n2 + 13];
                l15 -= lArray3[n2 + 14] + lArray4[n3 + 1];
                l16 -= lArray3[n2 + 15] + lArray4[n3 + 2];
                l17 -= lArray3[n2 + 16] + (long)i2 + 1L;
                l17 = ThreefishEngine.xorRotr(l17, 9, l2);
                l2 -= l17;
                l13 = ThreefishEngine.xorRotr(l13, 48, l4);
                l4 -= l13;
                l15 = ThreefishEngine.xorRotr(l15, 35, l8);
                l8 -= l15;
                l11 = ThreefishEngine.xorRotr(l11, 52, l6);
                l6 -= l11;
                l3 = ThreefishEngine.xorRotr(l3, 23, l16);
                l16 -= l3;
                l7 = ThreefishEngine.xorRotr(l7, 31, l10);
                l10 -= l7;
                l5 = ThreefishEngine.xorRotr(l5, 37, l12);
                l12 -= l5;
                l9 = ThreefishEngine.xorRotr(l9, 20, l14);
                l14 -= l9;
                l9 = ThreefishEngine.xorRotr(l9, 31, l2);
                l2 -= l9;
                l7 = ThreefishEngine.xorRotr(l7, 44, l4);
                l4 -= l7;
                l5 = ThreefishEngine.xorRotr(l5, 47, l6);
                l6 -= l5;
                l3 = ThreefishEngine.xorRotr(l3, 46, l8);
                l8 -= l3;
                l17 = ThreefishEngine.xorRotr(l17, 19, l14);
                l14 -= l17;
                l15 = ThreefishEngine.xorRotr(l15, 42, l16);
                l16 -= l15;
                l13 = ThreefishEngine.xorRotr(l13, 44, l10);
                l10 -= l13;
                l11 = ThreefishEngine.xorRotr(l11, 25, l12);
                l12 -= l11;
                l11 = ThreefishEngine.xorRotr(l11, 16, l2);
                l2 -= l11;
                l15 = ThreefishEngine.xorRotr(l15, 34, l4);
                l4 -= l15;
                l13 = ThreefishEngine.xorRotr(l13, 56, l8);
                l8 -= l13;
                l17 = ThreefishEngine.xorRotr(l17, 51, l6);
                l6 -= l17;
                l9 = ThreefishEngine.xorRotr(l9, 4, l12);
                l12 -= l9;
                l5 = ThreefishEngine.xorRotr(l5, 53, l14);
                l14 -= l5;
                l7 = ThreefishEngine.xorRotr(l7, 42, l16);
                l16 -= l7;
                l3 = ThreefishEngine.xorRotr(l3, 41, l10);
                l10 -= l3;
                l3 = ThreefishEngine.xorRotr(l3, 41, l2);
                l2 -= l3;
                l5 = ThreefishEngine.xorRotr(l5, 9, l4);
                l4 -= l5;
                l7 = ThreefishEngine.xorRotr(l7, 37, l6);
                l6 -= l7;
                l9 = ThreefishEngine.xorRotr(l9, 31, l8);
                l8 -= l9;
                l11 = ThreefishEngine.xorRotr(l11, 12, l10);
                l10 -= l11;
                l13 = ThreefishEngine.xorRotr(l13, 47, l12);
                l12 -= l13;
                l15 = ThreefishEngine.xorRotr(l15, 44, l14);
                l14 -= l15;
                l17 = ThreefishEngine.xorRotr(l17, 30, l16);
                l16 -= l17;
                l2 -= lArray3[n2];
                l3 -= lArray3[n2 + 1];
                l4 -= lArray3[n2 + 2];
                l5 -= lArray3[n2 + 3];
                l6 -= lArray3[n2 + 4];
                l7 -= lArray3[n2 + 5];
                l8 -= lArray3[n2 + 6];
                l9 -= lArray3[n2 + 7];
                l10 -= lArray3[n2 + 8];
                l11 -= lArray3[n2 + 9];
                l12 -= lArray3[n2 + 10];
                l13 -= lArray3[n2 + 11];
                l14 -= lArray3[n2 + 12];
                l15 -= lArray3[n2 + 13] + lArray4[n3];
                l16 -= lArray3[n2 + 14] + lArray4[n3 + 1];
                l17 -= lArray3[n2 + 15] + (long)i2;
                l17 = ThreefishEngine.xorRotr(l17, 5, l2);
                l2 -= l17;
                l13 = ThreefishEngine.xorRotr(l13, 20, l4);
                l4 -= l13;
                l15 = ThreefishEngine.xorRotr(l15, 48, l8);
                l8 -= l15;
                l11 = ThreefishEngine.xorRotr(l11, 41, l6);
                l6 -= l11;
                l3 = ThreefishEngine.xorRotr(l3, 47, l16);
                l16 -= l3;
                l7 = ThreefishEngine.xorRotr(l7, 28, l10);
                l10 -= l7;
                l5 = ThreefishEngine.xorRotr(l5, 16, l12);
                l12 -= l5;
                l9 = ThreefishEngine.xorRotr(l9, 25, l14);
                l14 -= l9;
                l9 = ThreefishEngine.xorRotr(l9, 33, l2);
                l2 -= l9;
                l7 = ThreefishEngine.xorRotr(l7, 4, l4);
                l4 -= l7;
                l5 = ThreefishEngine.xorRotr(l5, 51, l6);
                l6 -= l5;
                l3 = ThreefishEngine.xorRotr(l3, 13, l8);
                l8 -= l3;
                l17 = ThreefishEngine.xorRotr(l17, 34, l14);
                l14 -= l17;
                l15 = ThreefishEngine.xorRotr(l15, 41, l16);
                l16 -= l15;
                l13 = ThreefishEngine.xorRotr(l13, 59, l10);
                l10 -= l13;
                l11 = ThreefishEngine.xorRotr(l11, 17, l12);
                l12 -= l11;
                l11 = ThreefishEngine.xorRotr(l11, 38, l2);
                l2 -= l11;
                l15 = ThreefishEngine.xorRotr(l15, 19, l4);
                l4 -= l15;
                l13 = ThreefishEngine.xorRotr(l13, 10, l8);
                l8 -= l13;
                l17 = ThreefishEngine.xorRotr(l17, 55, l6);
                l6 -= l17;
                l9 = ThreefishEngine.xorRotr(l9, 49, l12);
                l12 -= l9;
                l5 = ThreefishEngine.xorRotr(l5, 18, l14);
                l14 -= l5;
                l7 = ThreefishEngine.xorRotr(l7, 23, l16);
                l16 -= l7;
                l3 = ThreefishEngine.xorRotr(l3, 52, l10);
                l10 -= l3;
                l3 = ThreefishEngine.xorRotr(l3, 24, l2);
                l2 -= l3;
                l5 = ThreefishEngine.xorRotr(l5, 13, l4);
                l4 -= l5;
                l7 = ThreefishEngine.xorRotr(l7, 8, l6);
                l6 -= l7;
                l9 = ThreefishEngine.xorRotr(l9, 47, l8);
                l8 -= l9;
                l11 = ThreefishEngine.xorRotr(l11, 8, l10);
                l10 -= l11;
                l13 = ThreefishEngine.xorRotr(l13, 17, l12);
                l12 -= l13;
                l15 = ThreefishEngine.xorRotr(l15, 22, l14);
                l14 -= l15;
                l17 = ThreefishEngine.xorRotr(l17, 37, l16);
                l16 -= l17;
            }
            l2 -= lArray3[0];
            l3 -= lArray3[1];
            l4 -= lArray3[2];
            l5 -= lArray3[3];
            l6 -= lArray3[4];
            l7 -= lArray3[5];
            l8 -= lArray3[6];
            l9 -= lArray3[7];
            l10 -= lArray3[8];
            l11 -= lArray3[9];
            l12 -= lArray3[10];
            l13 -= lArray3[11];
            l14 -= lArray3[12];
            l15 -= lArray3[13] + lArray4[0];
            l16 -= lArray3[14] + lArray4[1];
            l17 -= lArray3[15];
            lArray2[0] = l2;
            lArray2[1] = l3;
            lArray2[2] = l4;
            lArray2[3] = l5;
            lArray2[4] = l6;
            lArray2[5] = l7;
            lArray2[6] = l8;
            lArray2[7] = l9;
            lArray2[8] = l10;
            lArray2[9] = l11;
            lArray2[10] = l12;
            lArray2[11] = l13;
            lArray2[12] = l14;
            lArray2[13] = l15;
            lArray2[14] = l16;
            lArray2[15] = l17;
        }
    }

    private static final class Threefish256Cipher
    extends ThreefishCipher {
        private static final int ROTATION_0_0 = 14;
        private static final int ROTATION_0_1 = 16;
        private static final int ROTATION_1_0 = 52;
        private static final int ROTATION_1_1 = 57;
        private static final int ROTATION_2_0 = 23;
        private static final int ROTATION_2_1 = 40;
        private static final int ROTATION_3_0 = 5;
        private static final int ROTATION_3_1 = 37;
        private static final int ROTATION_4_0 = 25;
        private static final int ROTATION_4_1 = 33;
        private static final int ROTATION_5_0 = 46;
        private static final int ROTATION_5_1 = 12;
        private static final int ROTATION_6_0 = 58;
        private static final int ROTATION_6_1 = 22;
        private static final int ROTATION_7_0 = 32;
        private static final int ROTATION_7_1 = 32;

        public Threefish256Cipher(long[] lArray, long[] lArray2) {
            super(lArray, lArray2);
        }

        @Override
        void encryptBlock(long[] lArray, long[] lArray2) {
            long[] lArray3 = this.kw;
            long[] lArray4 = this.t;
            int[] nArray = MOD5;
            int[] nArray2 = MOD3;
            if (lArray3.length != 9) {
                throw new IllegalArgumentException();
            }
            if (lArray4.length != 5) {
                throw new IllegalArgumentException();
            }
            long l2 = lArray[0];
            long l3 = lArray[1];
            long l4 = lArray[2];
            long l5 = lArray[3];
            l2 += lArray3[0];
            l3 += lArray3[1] + lArray4[0];
            l4 += lArray3[2] + lArray4[1];
            l5 += lArray3[3];
            for (int i2 = 1; i2 < 18; i2 += 2) {
                int n2 = nArray[i2];
                int n3 = nArray2[i2];
                l2 += l3;
                l3 = ThreefishEngine.rotlXor(l3, 14, l2);
                l4 += l5;
                l5 = ThreefishEngine.rotlXor(l5, 16, l4);
                l2 += l5;
                l5 = ThreefishEngine.rotlXor(l5, 52, l2);
                l4 += l3;
                l3 = ThreefishEngine.rotlXor(l3, 57, l4);
                l2 += l3;
                l3 = ThreefishEngine.rotlXor(l3, 23, l2);
                l4 += l5;
                l5 = ThreefishEngine.rotlXor(l5, 40, l4);
                l2 += l5;
                l5 = ThreefishEngine.rotlXor(l5, 5, l2);
                l4 += l3;
                l3 = ThreefishEngine.rotlXor(l3, 37, l4);
                l2 += lArray3[n2];
                l4 += lArray3[n2 + 2] + lArray4[n3 + 1];
                l3 = ThreefishEngine.rotlXor(l3, 25, l2 += (l3 += lArray3[n2 + 1] + lArray4[n3]));
                l5 = ThreefishEngine.rotlXor(l5, 33, l4 += (l5 += lArray3[n2 + 3] + (long)i2));
                l2 += l5;
                l5 = ThreefishEngine.rotlXor(l5, 46, l2);
                l4 += l3;
                l3 = ThreefishEngine.rotlXor(l3, 12, l4);
                l2 += l3;
                l3 = ThreefishEngine.rotlXor(l3, 58, l2);
                l4 += l5;
                l5 = ThreefishEngine.rotlXor(l5, 22, l4);
                l2 += l5;
                l5 = ThreefishEngine.rotlXor(l5, 32, l2);
                l4 += l3;
                l3 = ThreefishEngine.rotlXor(l3, 32, l4);
                l2 += lArray3[n2 + 1];
                l3 += lArray3[n2 + 2] + lArray4[n3 + 1];
                l4 += lArray3[n2 + 3] + lArray4[n3 + 2];
                l5 += lArray3[n2 + 4] + (long)i2 + 1L;
            }
            lArray2[0] = l2;
            lArray2[1] = l3;
            lArray2[2] = l4;
            lArray2[3] = l5;
        }

        @Override
        void decryptBlock(long[] lArray, long[] lArray2) {
            long[] lArray3 = this.kw;
            long[] lArray4 = this.t;
            int[] nArray = MOD5;
            int[] nArray2 = MOD3;
            if (lArray3.length != 9) {
                throw new IllegalArgumentException();
            }
            if (lArray4.length != 5) {
                throw new IllegalArgumentException();
            }
            long l2 = lArray[0];
            long l3 = lArray[1];
            long l4 = lArray[2];
            long l5 = lArray[3];
            for (int i2 = 17; i2 >= 1; i2 -= 2) {
                int n2 = nArray[i2];
                int n3 = nArray2[i2];
                l3 -= lArray3[n2 + 2] + lArray4[n3 + 1];
                l5 -= lArray3[n2 + 4] + (long)i2 + 1L;
                l5 = ThreefishEngine.xorRotr(l5, 32, l2 -= lArray3[n2 + 1]);
                l3 = ThreefishEngine.xorRotr(l3, 32, l4 -= lArray3[n2 + 3] + lArray4[n3 + 2]);
                l4 -= l3;
                l3 = ThreefishEngine.xorRotr(l3, 58, l2 -= l5);
                l5 = ThreefishEngine.xorRotr(l5, 22, l4);
                l4 -= l5;
                l5 = ThreefishEngine.xorRotr(l5, 46, l2 -= l3);
                l3 = ThreefishEngine.xorRotr(l3, 12, l4);
                l4 -= l3;
                l3 = ThreefishEngine.xorRotr(l3, 25, l2 -= l5);
                l2 -= l3;
                l5 = ThreefishEngine.xorRotr(l5, 33, l4);
                l4 -= l5;
                l3 -= lArray3[n2 + 1] + lArray4[n3];
                l5 -= lArray3[n2 + 3] + (long)i2;
                l5 = ThreefishEngine.xorRotr(l5, 5, l2 -= lArray3[n2]);
                l3 = ThreefishEngine.xorRotr(l3, 37, l4 -= lArray3[n2 + 2] + lArray4[n3 + 1]);
                l4 -= l3;
                l3 = ThreefishEngine.xorRotr(l3, 23, l2 -= l5);
                l5 = ThreefishEngine.xorRotr(l5, 40, l4);
                l4 -= l5;
                l5 = ThreefishEngine.xorRotr(l5, 52, l2 -= l3);
                l3 = ThreefishEngine.xorRotr(l3, 57, l4);
                l4 -= l3;
                l3 = ThreefishEngine.xorRotr(l3, 14, l2 -= l5);
                l2 -= l3;
                l5 = ThreefishEngine.xorRotr(l5, 16, l4);
                l4 -= l5;
            }
            lArray2[0] = l2 -= lArray3[0];
            lArray2[1] = l3 -= lArray3[1] + lArray4[0];
            lArray2[2] = l4 -= lArray3[2] + lArray4[1];
            lArray2[3] = l5 -= lArray3[3];
        }
    }

    private static final class Threefish512Cipher
    extends ThreefishCipher {
        private static final int ROTATION_0_0 = 46;
        private static final int ROTATION_0_1 = 36;
        private static final int ROTATION_0_2 = 19;
        private static final int ROTATION_0_3 = 37;
        private static final int ROTATION_1_0 = 33;
        private static final int ROTATION_1_1 = 27;
        private static final int ROTATION_1_2 = 14;
        private static final int ROTATION_1_3 = 42;
        private static final int ROTATION_2_0 = 17;
        private static final int ROTATION_2_1 = 49;
        private static final int ROTATION_2_2 = 36;
        private static final int ROTATION_2_3 = 39;
        private static final int ROTATION_3_0 = 44;
        private static final int ROTATION_3_1 = 9;
        private static final int ROTATION_3_2 = 54;
        private static final int ROTATION_3_3 = 56;
        private static final int ROTATION_4_0 = 39;
        private static final int ROTATION_4_1 = 30;
        private static final int ROTATION_4_2 = 34;
        private static final int ROTATION_4_3 = 24;
        private static final int ROTATION_5_0 = 13;
        private static final int ROTATION_5_1 = 50;
        private static final int ROTATION_5_2 = 10;
        private static final int ROTATION_5_3 = 17;
        private static final int ROTATION_6_0 = 25;
        private static final int ROTATION_6_1 = 29;
        private static final int ROTATION_6_2 = 39;
        private static final int ROTATION_6_3 = 43;
        private static final int ROTATION_7_0 = 8;
        private static final int ROTATION_7_1 = 35;
        private static final int ROTATION_7_2 = 56;
        private static final int ROTATION_7_3 = 22;

        protected Threefish512Cipher(long[] lArray, long[] lArray2) {
            super(lArray, lArray2);
        }

        @Override
        public void encryptBlock(long[] lArray, long[] lArray2) {
            long[] lArray3 = this.kw;
            long[] lArray4 = this.t;
            int[] nArray = MOD9;
            int[] nArray2 = MOD3;
            if (lArray3.length != 17) {
                throw new IllegalArgumentException();
            }
            if (lArray4.length != 5) {
                throw new IllegalArgumentException();
            }
            long l2 = lArray[0];
            long l3 = lArray[1];
            long l4 = lArray[2];
            long l5 = lArray[3];
            long l6 = lArray[4];
            long l7 = lArray[5];
            long l8 = lArray[6];
            long l9 = lArray[7];
            l2 += lArray3[0];
            l3 += lArray3[1];
            l4 += lArray3[2];
            l5 += lArray3[3];
            l6 += lArray3[4];
            l7 += lArray3[5] + lArray4[0];
            l8 += lArray3[6] + lArray4[1];
            l9 += lArray3[7];
            for (int i2 = 1; i2 < 18; i2 += 2) {
                int n2 = nArray[i2];
                int n3 = nArray2[i2];
                l2 += l3;
                l3 = ThreefishEngine.rotlXor(l3, 46, l2);
                l4 += l5;
                l5 = ThreefishEngine.rotlXor(l5, 36, l4);
                l6 += l7;
                l7 = ThreefishEngine.rotlXor(l7, 19, l6);
                l8 += l9;
                l9 = ThreefishEngine.rotlXor(l9, 37, l8);
                l4 += l3;
                l3 = ThreefishEngine.rotlXor(l3, 33, l4);
                l6 += l9;
                l9 = ThreefishEngine.rotlXor(l9, 27, l6);
                l8 += l7;
                l7 = ThreefishEngine.rotlXor(l7, 14, l8);
                l2 += l5;
                l5 = ThreefishEngine.rotlXor(l5, 42, l2);
                l6 += l3;
                l3 = ThreefishEngine.rotlXor(l3, 17, l6);
                l8 += l5;
                l5 = ThreefishEngine.rotlXor(l5, 49, l8);
                l2 += l7;
                l7 = ThreefishEngine.rotlXor(l7, 36, l2);
                l4 += l9;
                l9 = ThreefishEngine.rotlXor(l9, 39, l4);
                l8 += l3;
                l3 = ThreefishEngine.rotlXor(l3, 44, l8);
                l2 += l9;
                l9 = ThreefishEngine.rotlXor(l9, 9, l2);
                l4 += l7;
                l7 = ThreefishEngine.rotlXor(l7, 54, l4);
                l6 += l5;
                l5 = ThreefishEngine.rotlXor(l5, 56, l6);
                l2 += lArray3[n2];
                l3 += lArray3[n2 + 1];
                l4 += lArray3[n2 + 2];
                l5 += lArray3[n2 + 3];
                l6 += lArray3[n2 + 4];
                l7 += lArray3[n2 + 5] + lArray4[n3];
                l8 += lArray3[n2 + 6] + lArray4[n3 + 1];
                l9 += lArray3[n2 + 7] + (long)i2;
                l2 += l3;
                l3 = ThreefishEngine.rotlXor(l3, 39, l2);
                l4 += l5;
                l5 = ThreefishEngine.rotlXor(l5, 30, l4);
                l6 += l7;
                l7 = ThreefishEngine.rotlXor(l7, 34, l6);
                l8 += l9;
                l9 = ThreefishEngine.rotlXor(l9, 24, l8);
                l4 += l3;
                l3 = ThreefishEngine.rotlXor(l3, 13, l4);
                l6 += l9;
                l9 = ThreefishEngine.rotlXor(l9, 50, l6);
                l8 += l7;
                l7 = ThreefishEngine.rotlXor(l7, 10, l8);
                l2 += l5;
                l5 = ThreefishEngine.rotlXor(l5, 17, l2);
                l6 += l3;
                l3 = ThreefishEngine.rotlXor(l3, 25, l6);
                l8 += l5;
                l5 = ThreefishEngine.rotlXor(l5, 29, l8);
                l2 += l7;
                l7 = ThreefishEngine.rotlXor(l7, 39, l2);
                l4 += l9;
                l9 = ThreefishEngine.rotlXor(l9, 43, l4);
                l8 += l3;
                l3 = ThreefishEngine.rotlXor(l3, 8, l8);
                l2 += l9;
                l9 = ThreefishEngine.rotlXor(l9, 35, l2);
                l4 += l7;
                l7 = ThreefishEngine.rotlXor(l7, 56, l4);
                l6 += l5;
                l5 = ThreefishEngine.rotlXor(l5, 22, l6);
                l2 += lArray3[n2 + 1];
                l3 += lArray3[n2 + 2];
                l4 += lArray3[n2 + 3];
                l5 += lArray3[n2 + 4];
                l6 += lArray3[n2 + 5];
                l7 += lArray3[n2 + 6] + lArray4[n3 + 1];
                l8 += lArray3[n2 + 7] + lArray4[n3 + 2];
                l9 += lArray3[n2 + 8] + (long)i2 + 1L;
            }
            lArray2[0] = l2;
            lArray2[1] = l3;
            lArray2[2] = l4;
            lArray2[3] = l5;
            lArray2[4] = l6;
            lArray2[5] = l7;
            lArray2[6] = l8;
            lArray2[7] = l9;
        }

        @Override
        public void decryptBlock(long[] lArray, long[] lArray2) {
            long[] lArray3 = this.kw;
            long[] lArray4 = this.t;
            int[] nArray = MOD9;
            int[] nArray2 = MOD3;
            if (lArray3.length != 17) {
                throw new IllegalArgumentException();
            }
            if (lArray4.length != 5) {
                throw new IllegalArgumentException();
            }
            long l2 = lArray[0];
            long l3 = lArray[1];
            long l4 = lArray[2];
            long l5 = lArray[3];
            long l6 = lArray[4];
            long l7 = lArray[5];
            long l8 = lArray[6];
            long l9 = lArray[7];
            for (int i2 = 17; i2 >= 1; i2 -= 2) {
                int n2 = nArray[i2];
                int n3 = nArray2[i2];
                l2 -= lArray3[n2 + 1];
                l3 -= lArray3[n2 + 2];
                l4 -= lArray3[n2 + 3];
                l5 -= lArray3[n2 + 4];
                l6 -= lArray3[n2 + 5];
                l7 -= lArray3[n2 + 6] + lArray4[n3 + 1];
                l9 -= lArray3[n2 + 8] + (long)i2 + 1L;
                l3 = ThreefishEngine.xorRotr(l3, 8, l8 -= lArray3[n2 + 7] + lArray4[n3 + 2]);
                l8 -= l3;
                l9 = ThreefishEngine.xorRotr(l9, 35, l2);
                l2 -= l9;
                l7 = ThreefishEngine.xorRotr(l7, 56, l4);
                l4 -= l7;
                l5 = ThreefishEngine.xorRotr(l5, 22, l6);
                l3 = ThreefishEngine.xorRotr(l3, 25, l6 -= l5);
                l6 -= l3;
                l5 = ThreefishEngine.xorRotr(l5, 29, l8);
                l8 -= l5;
                l7 = ThreefishEngine.xorRotr(l7, 39, l2);
                l2 -= l7;
                l9 = ThreefishEngine.xorRotr(l9, 43, l4);
                l3 = ThreefishEngine.xorRotr(l3, 13, l4 -= l9);
                l4 -= l3;
                l9 = ThreefishEngine.xorRotr(l9, 50, l6);
                l6 -= l9;
                l7 = ThreefishEngine.xorRotr(l7, 10, l8);
                l8 -= l7;
                l5 = ThreefishEngine.xorRotr(l5, 17, l2);
                l3 = ThreefishEngine.xorRotr(l3, 39, l2 -= l5);
                l2 -= l3;
                l5 = ThreefishEngine.xorRotr(l5, 30, l4);
                l4 -= l5;
                l7 = ThreefishEngine.xorRotr(l7, 34, l6);
                l6 -= l7;
                l9 = ThreefishEngine.xorRotr(l9, 24, l8);
                l8 -= l9;
                l2 -= lArray3[n2];
                l3 -= lArray3[n2 + 1];
                l4 -= lArray3[n2 + 2];
                l5 -= lArray3[n2 + 3];
                l6 -= lArray3[n2 + 4];
                l7 -= lArray3[n2 + 5] + lArray4[n3];
                l9 -= lArray3[n2 + 7] + (long)i2;
                l3 = ThreefishEngine.xorRotr(l3, 44, l8 -= lArray3[n2 + 6] + lArray4[n3 + 1]);
                l8 -= l3;
                l9 = ThreefishEngine.xorRotr(l9, 9, l2);
                l2 -= l9;
                l7 = ThreefishEngine.xorRotr(l7, 54, l4);
                l4 -= l7;
                l5 = ThreefishEngine.xorRotr(l5, 56, l6);
                l3 = ThreefishEngine.xorRotr(l3, 17, l6 -= l5);
                l6 -= l3;
                l5 = ThreefishEngine.xorRotr(l5, 49, l8);
                l8 -= l5;
                l7 = ThreefishEngine.xorRotr(l7, 36, l2);
                l2 -= l7;
                l9 = ThreefishEngine.xorRotr(l9, 39, l4);
                l3 = ThreefishEngine.xorRotr(l3, 33, l4 -= l9);
                l4 -= l3;
                l9 = ThreefishEngine.xorRotr(l9, 27, l6);
                l6 -= l9;
                l7 = ThreefishEngine.xorRotr(l7, 14, l8);
                l8 -= l7;
                l5 = ThreefishEngine.xorRotr(l5, 42, l2);
                l3 = ThreefishEngine.xorRotr(l3, 46, l2 -= l5);
                l2 -= l3;
                l5 = ThreefishEngine.xorRotr(l5, 36, l4);
                l4 -= l5;
                l7 = ThreefishEngine.xorRotr(l7, 19, l6);
                l6 -= l7;
                l9 = ThreefishEngine.xorRotr(l9, 37, l8);
                l8 -= l9;
            }
            l2 -= lArray3[0];
            l3 -= lArray3[1];
            l4 -= lArray3[2];
            l5 -= lArray3[3];
            l6 -= lArray3[4];
            l7 -= lArray3[5] + lArray4[0];
            l8 -= lArray3[6] + lArray4[1];
            l9 -= lArray3[7];
            lArray2[0] = l2;
            lArray2[1] = l3;
            lArray2[2] = l4;
            lArray2[3] = l5;
            lArray2[4] = l6;
            lArray2[5] = l7;
            lArray2[6] = l8;
            lArray2[7] = l9;
        }
    }

    private static abstract class ThreefishCipher {
        protected final long[] t;
        protected final long[] kw;

        protected ThreefishCipher(long[] lArray, long[] lArray2) {
            this.kw = lArray;
            this.t = lArray2;
        }

        abstract void encryptBlock(long[] var1, long[] var2);

        abstract void decryptBlock(long[] var1, long[] var2);
    }
}

