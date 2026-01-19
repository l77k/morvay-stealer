/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.hqc;

import org.bouncycastle.pqc.crypto.hqc.GF2PolynomialCalculator;
import org.bouncycastle.pqc.crypto.hqc.KeccakRandomGenerator;
import org.bouncycastle.pqc.crypto.hqc.ReedMuller;
import org.bouncycastle.pqc.crypto.hqc.ReedSolomon;
import org.bouncycastle.pqc.crypto.hqc.Utils;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Pack;

class HQCEngine {
    private int n;
    private int n1;
    private int n2;
    private int k;
    private int delta;
    private int w;
    private int wr;
    private int we;
    private int g;
    private int rejectionThreshold;
    private int fft;
    private int mulParam;
    private int SEED_SIZE = 40;
    private byte G_FCT_DOMAIN = (byte)3;
    private byte K_FCT_DOMAIN = (byte)4;
    private int N_BYTE;
    private int n1n2;
    private int N_BYTE_64;
    private int K_BYTE;
    private int K_BYTE_64;
    private int N1_BYTE_64;
    private int N1N2_BYTE_64;
    private int N1N2_BYTE;
    private int N1_BYTE;
    private int GF_POLY_WT = 5;
    private int GF_POLY_M2 = 4;
    private int SALT_SIZE_BYTES = 16;
    private int SALT_SIZE_64 = 2;
    private int[] generatorPoly;
    private int SHA512_BYTES = 64;
    private long RED_MASK;
    private GF2PolynomialCalculator gfCalculator;

    public HQCEngine(int n2, int n3, int n4, int n5, int n6, int n7, int n8, int n9, int n10, int n11, int n12, int[] nArray) {
        this.n = n2;
        this.k = n5;
        this.delta = n7;
        this.w = n8;
        this.wr = n9;
        this.we = n10;
        this.n1 = n3;
        this.n2 = n4;
        this.n1n2 = n3 * n4;
        this.generatorPoly = nArray;
        this.g = n6;
        this.rejectionThreshold = n11;
        this.fft = n12;
        this.mulParam = (int)Math.ceil(n4 / 128);
        this.N_BYTE = Utils.getByteSizeFromBitSize(n2);
        this.K_BYTE = n5;
        this.N_BYTE_64 = Utils.getByte64SizeFromBitSize(n2);
        this.K_BYTE_64 = Utils.getByteSizeFromBitSize(n5);
        this.N1_BYTE_64 = Utils.getByteSizeFromBitSize(n3);
        this.N1N2_BYTE_64 = Utils.getByte64SizeFromBitSize(n3 * n4);
        this.N1N2_BYTE = Utils.getByteSizeFromBitSize(n3 * n4);
        this.N1_BYTE = Utils.getByteSizeFromBitSize(n3);
        this.RED_MASK = (1L << (int)((long)n2 % 64L)) - 1L;
        this.gfCalculator = new GF2PolynomialCalculator(this.N_BYTE_64, n2, this.RED_MASK);
    }

    public void genKeyPair(byte[] byArray, byte[] byArray2, byte[] byArray3) {
        byte[] byArray4 = new byte[this.SEED_SIZE];
        byte[] byArray5 = new byte[this.K_BYTE];
        KeccakRandomGenerator keccakRandomGenerator = new KeccakRandomGenerator(256);
        keccakRandomGenerator.randomGeneratorInit(byArray3, null, byArray3.length, 0);
        keccakRandomGenerator.squeeze(byArray4, 40);
        keccakRandomGenerator.squeeze(byArray5, this.K_BYTE);
        KeccakRandomGenerator keccakRandomGenerator2 = new KeccakRandomGenerator(256);
        keccakRandomGenerator2.seedExpanderInit(byArray4, byArray4.length);
        long[] lArray = new long[this.N_BYTE_64];
        long[] lArray2 = new long[this.N_BYTE_64];
        this.generateRandomFixedWeight(lArray2, keccakRandomGenerator2, this.w);
        this.generateRandomFixedWeight(lArray, keccakRandomGenerator2, this.w);
        byte[] byArray6 = new byte[this.SEED_SIZE];
        keccakRandomGenerator.squeeze(byArray6, 40);
        KeccakRandomGenerator keccakRandomGenerator3 = new KeccakRandomGenerator(256);
        keccakRandomGenerator3.seedExpanderInit(byArray6, byArray6.length);
        long[] lArray3 = new long[this.N_BYTE_64];
        this.generatePublicKeyH(lArray3, keccakRandomGenerator3);
        long[] lArray4 = new long[this.N_BYTE_64];
        this.gfCalculator.multLongs(lArray4, lArray2, lArray3);
        GF2PolynomialCalculator.addLongs(lArray4, lArray4, lArray);
        byte[] byArray7 = new byte[this.N_BYTE];
        Utils.fromLongArrayToByteArray(byArray7, lArray4);
        byte[] byArray8 = Arrays.concatenate(byArray6, byArray7);
        byte[] byArray9 = Arrays.concatenate(byArray4, byArray5, byArray8);
        System.arraycopy(byArray8, 0, byArray, 0, byArray8.length);
        System.arraycopy(byArray9, 0, byArray2, 0, byArray9.length);
    }

    public void encaps(byte[] byArray, byte[] byArray2, byte[] byArray3, byte[] byArray4, byte[] byArray5, byte[] byArray6) {
        byte[] byArray7 = new byte[this.K_BYTE];
        byte[] byArray8 = new byte[this.SEED_SIZE];
        KeccakRandomGenerator keccakRandomGenerator = new KeccakRandomGenerator(256);
        keccakRandomGenerator.randomGeneratorInit(byArray5, null, byArray5.length, 0);
        keccakRandomGenerator.squeeze(byArray8, 40);
        byte[] byArray9 = new byte[this.K_BYTE];
        keccakRandomGenerator.squeeze(byArray9, this.K_BYTE);
        byte[] byArray10 = new byte[this.SEED_SIZE];
        keccakRandomGenerator.squeeze(byArray10, 40);
        keccakRandomGenerator.squeeze(byArray7, this.K_BYTE);
        byte[] byArray11 = new byte[this.SHA512_BYTES];
        byte[] byArray12 = new byte[this.K_BYTE + this.SALT_SIZE_BYTES * 2 + this.SALT_SIZE_BYTES];
        keccakRandomGenerator.squeeze(byArray6, this.SALT_SIZE_BYTES);
        System.arraycopy(byArray7, 0, byArray12, 0, byArray7.length);
        System.arraycopy(byArray4, 0, byArray12, this.K_BYTE, this.SALT_SIZE_BYTES * 2);
        System.arraycopy(byArray6, 0, byArray12, this.K_BYTE + this.SALT_SIZE_BYTES * 2, this.SALT_SIZE_BYTES);
        KeccakRandomGenerator keccakRandomGenerator2 = new KeccakRandomGenerator(256);
        keccakRandomGenerator2.SHAKE256_512_ds(byArray11, byArray12, byArray12.length, new byte[]{this.G_FCT_DOMAIN});
        long[] lArray = new long[this.N_BYTE_64];
        byte[] byArray13 = new byte[this.N_BYTE];
        this.extractPublicKeys(lArray, byArray13, byArray4);
        long[] lArray2 = new long[this.N1N2_BYTE_64];
        this.encrypt(byArray, lArray2, lArray, byArray13, byArray7, byArray11);
        Utils.fromLongArrayToByteArray(byArray2, lArray2);
        byte[] byArray14 = Arrays.concatenate(byArray7, byArray, byArray2);
        keccakRandomGenerator2.SHAKE256_512_ds(byArray3, byArray14, byArray14.length, new byte[]{this.K_FCT_DOMAIN});
    }

    public int decaps(byte[] byArray, byte[] byArray2, byte[] byArray3) {
        long[] lArray = new long[this.N_BYTE_64];
        byte[] byArray4 = new byte[40 + this.N_BYTE];
        byte[] byArray5 = new byte[this.K_BYTE];
        this.extractKeysFromSecretKeys(lArray, byArray5, byArray4, byArray3);
        byte[] byArray6 = new byte[this.N_BYTE];
        byte[] byArray7 = new byte[this.N1N2_BYTE];
        byte[] byArray8 = new byte[this.SALT_SIZE_BYTES];
        this.extractCiphertexts(byArray6, byArray7, byArray8, byArray2);
        byte[] byArray9 = new byte[this.k];
        int n2 = this.decrypt(byArray9, byArray9, byArray5, byArray6, byArray7, lArray);
        byte[] byArray10 = new byte[this.SHA512_BYTES];
        byte[] byArray11 = new byte[this.K_BYTE + this.SALT_SIZE_BYTES * 2 + this.SALT_SIZE_BYTES];
        System.arraycopy(byArray9, 0, byArray11, 0, byArray9.length);
        System.arraycopy(byArray4, 0, byArray11, this.K_BYTE, this.SALT_SIZE_BYTES * 2);
        System.arraycopy(byArray8, 0, byArray11, this.K_BYTE + this.SALT_SIZE_BYTES * 2, this.SALT_SIZE_BYTES);
        KeccakRandomGenerator keccakRandomGenerator = new KeccakRandomGenerator(256);
        keccakRandomGenerator.SHAKE256_512_ds(byArray10, byArray11, byArray11.length, new byte[]{this.G_FCT_DOMAIN});
        long[] lArray2 = new long[this.N_BYTE_64];
        byte[] byArray12 = new byte[this.N_BYTE];
        this.extractPublicKeys(lArray2, byArray12, byArray4);
        byte[] byArray13 = new byte[this.N_BYTE];
        byte[] byArray14 = new byte[this.N1N2_BYTE];
        long[] lArray3 = new long[this.N1N2_BYTE_64];
        this.encrypt(byArray13, lArray3, lArray2, byArray12, byArray9, byArray10);
        Utils.fromLongArrayToByteArray(byArray14, lArray3);
        byte[] byArray15 = new byte[this.K_BYTE + this.N_BYTE + this.N1N2_BYTE];
        if (!Arrays.constantTimeAreEqual(byArray6, byArray13)) {
            n2 = 1;
        }
        if (!Arrays.constantTimeAreEqual(byArray7, byArray14)) {
            n2 = 1;
        }
        --n2;
        for (int i2 = 0; i2 < this.K_BYTE; ++i2) {
            byArray15[i2] = (byte)((byArray9[i2] & n2 ^ byArray5[i2] & ~n2) & 0xFF);
        }
        System.arraycopy(byArray6, 0, byArray15, this.K_BYTE, this.N_BYTE);
        System.arraycopy(byArray7, 0, byArray15, this.K_BYTE + this.N_BYTE, this.N1N2_BYTE);
        keccakRandomGenerator.SHAKE256_512_ds(byArray, byArray15, byArray15.length, new byte[]{this.K_FCT_DOMAIN});
        return -n2;
    }

    int getSessionKeySize() {
        return this.SHA512_BYTES;
    }

    private void encrypt(byte[] byArray, long[] lArray, long[] lArray2, byte[] byArray2, byte[] byArray3, byte[] byArray4) {
        KeccakRandomGenerator keccakRandomGenerator = new KeccakRandomGenerator(256);
        keccakRandomGenerator.seedExpanderInit(byArray4, this.SEED_SIZE);
        long[] lArray3 = new long[this.N_BYTE_64];
        long[] lArray4 = new long[this.N_BYTE_64];
        long[] lArray5 = new long[this.N_BYTE_64];
        this.generateRandomFixedWeight(lArray5, keccakRandomGenerator, this.wr);
        this.generateRandomFixedWeight(lArray3, keccakRandomGenerator, this.we);
        this.generateRandomFixedWeight(lArray4, keccakRandomGenerator, this.wr);
        long[] lArray6 = new long[this.N_BYTE_64];
        this.gfCalculator.multLongs(lArray6, lArray5, lArray2);
        GF2PolynomialCalculator.addLongs(lArray6, lArray6, lArray4);
        Utils.fromLongArrayToByteArray(byArray, lArray6);
        byte[] byArray5 = new byte[this.n1];
        long[] lArray7 = new long[this.N1N2_BYTE_64];
        long[] lArray8 = new long[this.N_BYTE_64];
        ReedSolomon.encode(byArray5, byArray3, this.K_BYTE * 8, this.n1, this.k, this.g, this.generatorPoly);
        ReedMuller.encode(lArray7, byArray5, this.n1, this.mulParam);
        System.arraycopy(lArray7, 0, lArray8, 0, lArray7.length);
        long[] lArray9 = new long[this.N_BYTE_64];
        Utils.fromByteArrayToLongArray(lArray9, byArray2);
        long[] lArray10 = new long[this.N_BYTE_64];
        this.gfCalculator.multLongs(lArray10, lArray5, lArray9);
        GF2PolynomialCalculator.addLongs(lArray10, lArray10, lArray8);
        GF2PolynomialCalculator.addLongs(lArray10, lArray10, lArray3);
        Utils.resizeArray(lArray, this.n1n2, lArray10, this.n, this.N1N2_BYTE_64, this.N1N2_BYTE_64);
    }

    private int decrypt(byte[] byArray, byte[] byArray2, byte[] byArray3, byte[] byArray4, byte[] byArray5, long[] lArray) {
        long[] lArray2 = new long[this.N_BYTE_64];
        Utils.fromByteArrayToLongArray(lArray2, byArray4);
        long[] lArray3 = new long[this.N1N2_BYTE_64];
        Utils.fromByteArrayToLongArray(lArray3, byArray5);
        long[] lArray4 = new long[this.N_BYTE_64];
        System.arraycopy(lArray3, 0, lArray4, 0, lArray3.length);
        long[] lArray5 = new long[this.N_BYTE_64];
        this.gfCalculator.multLongs(lArray5, lArray, lArray2);
        GF2PolynomialCalculator.addLongs(lArray5, lArray5, lArray4);
        byte[] byArray6 = new byte[this.n1];
        ReedMuller.decode(byArray6, lArray5, this.n1, this.mulParam);
        ReedSolomon.decode(byArray2, byArray6, this.n1, this.fft, this.delta, this.k, this.g);
        System.arraycopy(byArray2, 0, byArray, 0, byArray.length);
        return 0;
    }

    private void generateRandomFixedWeight(long[] lArray, KeccakRandomGenerator keccakRandomGenerator, int n2) {
        int n3;
        int n4;
        int n5;
        int[] nArray = new int[this.wr];
        byte[] byArray = new byte[this.wr * 4];
        int[] nArray2 = new int[this.wr];
        int[] nArray3 = new int[this.wr];
        long[] lArray2 = new long[this.wr];
        keccakRandomGenerator.expandSeed(byArray, 4 * n2);
        Pack.littleEndianToInt(byArray, 0, nArray, 0, nArray.length);
        for (n5 = 0; n5 < n2; ++n5) {
            nArray2[n5] = (int)((long)n5 + ((long)nArray[n5] & 0xFFFFFFFFL) % (long)(this.n - n5));
        }
        for (n5 = n2 - 1; n5 >= 0; --n5) {
            n4 = 0;
            for (n3 = n5 + 1; n3 < n2; ++n3) {
                if (nArray2[n3] != nArray2[n5]) continue;
                n4 |= 1;
            }
            n3 = -n4;
            nArray2[n5] = n3 & n5 ^ ~n3 & nArray2[n5];
        }
        for (n5 = 0; n5 < n2; ++n5) {
            nArray3[n5] = nArray2[n5] >>> 6;
            n4 = nArray2[n5] & 0x3F;
            lArray2[n5] = 1L << n4;
        }
        long l2 = 0L;
        n3 = 0;
        while (n3 < this.N_BYTE_64) {
            l2 = 0L;
            for (int i2 = 0; i2 < n2; ++i2) {
                int n6 = n3 - nArray3[i2];
                int n7 = 1 ^ (n6 | -n6) >>> 31;
                long l3 = -n7;
                l2 |= lArray2[i2] & l3;
            }
            int n8 = n3++;
            lArray[n8] = lArray[n8] | l2;
        }
    }

    void generatePublicKeyH(long[] lArray, KeccakRandomGenerator keccakRandomGenerator) {
        byte[] byArray = new byte[this.N_BYTE];
        keccakRandomGenerator.expandSeed(byArray, this.N_BYTE);
        long[] lArray2 = new long[this.N_BYTE_64];
        Utils.fromByteArrayToLongArray(lArray2, byArray);
        int n2 = this.N_BYTE_64 - 1;
        lArray2[n2] = lArray2[n2] & Utils.bitMask(this.n, 64L);
        System.arraycopy(lArray2, 0, lArray, 0, lArray.length);
    }

    private void extractPublicKeys(long[] lArray, byte[] byArray, byte[] byArray2) {
        byte[] byArray3 = new byte[this.SEED_SIZE];
        System.arraycopy(byArray2, 0, byArray3, 0, byArray3.length);
        KeccakRandomGenerator keccakRandomGenerator = new KeccakRandomGenerator(256);
        keccakRandomGenerator.seedExpanderInit(byArray3, byArray3.length);
        long[] lArray2 = new long[this.N_BYTE_64];
        this.generatePublicKeyH(lArray2, keccakRandomGenerator);
        System.arraycopy(lArray2, 0, lArray, 0, lArray.length);
        System.arraycopy(byArray2, 40, byArray, 0, byArray.length);
    }

    private void extractKeysFromSecretKeys(long[] lArray, byte[] byArray, byte[] byArray2, byte[] byArray3) {
        byte[] byArray4 = new byte[this.SEED_SIZE];
        System.arraycopy(byArray3, 0, byArray4, 0, byArray4.length);
        System.arraycopy(byArray3, this.SEED_SIZE, byArray, 0, this.K_BYTE);
        KeccakRandomGenerator keccakRandomGenerator = new KeccakRandomGenerator(256);
        keccakRandomGenerator.seedExpanderInit(byArray4, byArray4.length);
        this.generateRandomFixedWeight(lArray, keccakRandomGenerator, this.w);
        System.arraycopy(byArray3, this.SEED_SIZE + this.K_BYTE, byArray2, 0, byArray2.length);
    }

    private void extractCiphertexts(byte[] byArray, byte[] byArray2, byte[] byArray3, byte[] byArray4) {
        System.arraycopy(byArray4, 0, byArray, 0, byArray.length);
        System.arraycopy(byArray4, byArray.length, byArray2, 0, byArray2.length);
        System.arraycopy(byArray4, byArray.length + byArray2.length, byArray3, 0, byArray3.length);
    }
}

