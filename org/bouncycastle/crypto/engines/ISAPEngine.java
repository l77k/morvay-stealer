/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.engines.AEADBufferBaseEngine;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Pack;

public class ISAPEngine
extends AEADBufferBaseEngine {
    final int ISAP_STATE_SZ = 40;
    private byte[] k;
    private byte[] npub;
    private int ISAP_rH;
    private ISAP_AEAD ISAPAEAD;

    public ISAPEngine(IsapType isapType) {
        this.KEY_SIZE = 16;
        this.IV_SIZE = 16;
        this.MAC_SIZE = 16;
        switch (isapType.ordinal()) {
            case 0: {
                this.ISAPAEAD = new ISAPAEAD_A_128A();
                this.algorithmName = "ISAP-A-128A AEAD";
                break;
            }
            case 1: {
                this.ISAPAEAD = new ISAPAEAD_K_128A();
                this.algorithmName = "ISAP-K-128A AEAD";
                break;
            }
            case 2: {
                this.ISAPAEAD = new ISAPAEAD_A_128();
                this.algorithmName = "ISAP-A-128 AEAD";
                break;
            }
            case 3: {
                this.ISAPAEAD = new ISAPAEAD_K_128();
                this.algorithmName = "ISAP-K-128 AEAD";
            }
        }
        this.AADBufferSize = this.BlockSize;
        this.m_aad = new byte[this.AADBufferSize];
    }

    @Override
    protected void init(byte[] byArray, byte[] byArray2) throws IllegalArgumentException {
        this.npub = byArray2;
        this.k = byArray;
        this.m_buf = new byte[this.BlockSize + (this.forEncryption ? 0 : this.MAC_SIZE)];
        this.ISAPAEAD.init();
        this.initialised = true;
        this.m_state = this.forEncryption ? AEADBufferBaseEngine.State.EncInit : AEADBufferBaseEngine.State.DecInit;
        this.reset();
    }

    @Override
    protected void processBufferAAD(byte[] byArray, int n2) {
        this.ISAPAEAD.absorbMacBlock(byArray, n2);
    }

    @Override
    protected void processFinalAAD() {
        if (!this.aadFinished) {
            this.ISAPAEAD.absorbFinalAADBlock();
            this.ISAPAEAD.swapInternalState();
            this.m_aadPos = 0;
            this.aadFinished = true;
        }
    }

    @Override
    protected void processBuffer(byte[] byArray, int n2, byte[] byArray2, int n3) {
        this.processFinalAAD();
        this.ISAPAEAD.processEncBlock(byArray, n2, byArray2, n3);
        this.ISAPAEAD.swapInternalState();
        if (this.forEncryption) {
            this.ISAPAEAD.absorbMacBlock(byArray2, n3);
        } else {
            this.ISAPAEAD.absorbMacBlock(byArray, n2);
        }
        this.ISAPAEAD.swapInternalState();
    }

    @Override
    protected void processFinalBlock(byte[] byArray, int n2) {
        this.processFinalAAD();
        int n3 = this.m_bufPos;
        this.mac = new byte[this.MAC_SIZE];
        this.ISAPAEAD.processEncFinalBlock(byArray, n2);
        this.ISAPAEAD.swapInternalState();
        if (this.forEncryption) {
            this.ISAPAEAD.processMACFinal(byArray, n2, n3, this.mac);
        } else {
            this.ISAPAEAD.processMACFinal(this.m_buf, 0, n3, this.mac);
        }
    }

    @Override
    protected void reset(boolean bl) {
        if (!this.initialised) {
            throw new IllegalStateException("Need call init function before encryption/decryption");
        }
        Arrays.fill(this.m_buf, (byte)0);
        Arrays.fill(this.m_aad, (byte)0);
        this.ISAPAEAD.reset();
        this.m_bufPos = 0;
        this.m_aadPos = 0;
        this.aadFinished = false;
        super.reset(bl);
    }

    private abstract class ISAPAEAD_A
    implements ISAP_AEAD {
        protected long[] k64;
        protected long[] npub64;
        protected long ISAP_IV1_64;
        protected long ISAP_IV2_64;
        protected long ISAP_IV3_64;
        protected long x0;
        protected long x1;
        protected long x2;
        protected long x3;
        protected long x4;
        protected long t0;
        protected long t1;
        protected long t2;
        protected long t3;
        protected long t4;
        protected long macx0;
        protected long macx1;
        protected long macx2;
        protected long macx3;
        protected long macx4;

        public ISAPAEAD_A() {
            ISAPEngine.this.ISAP_rH = 64;
            ISAPEngine.this.BlockSize = ISAPEngine.this.ISAP_rH + 7 >> 3;
        }

        @Override
        public void init() {
            this.npub64 = new long[this.getLongSize(ISAPEngine.this.npub.length)];
            this.k64 = new long[this.getLongSize(ISAPEngine.this.k.length)];
            Pack.bigEndianToLong(ISAPEngine.this.npub, 0, this.npub64);
            Pack.bigEndianToLong(ISAPEngine.this.k, 0, this.k64);
        }

        protected abstract void PX1();

        protected abstract void PX2();

        @Override
        public void swapInternalState() {
            this.t0 = this.x0;
            this.t1 = this.x1;
            this.t2 = this.x2;
            this.t3 = this.x3;
            this.t4 = this.x4;
            this.x0 = this.macx0;
            this.x1 = this.macx1;
            this.x2 = this.macx2;
            this.x3 = this.macx3;
            this.x4 = this.macx4;
            this.macx0 = this.t0;
            this.macx1 = this.t1;
            this.macx2 = this.t2;
            this.macx3 = this.t3;
            this.macx4 = this.t4;
        }

        @Override
        public void absorbMacBlock(byte[] byArray, int n2) {
            this.x0 ^= Pack.bigEndianToLong(byArray, n2);
            this.P12();
        }

        @Override
        public void absorbFinalAADBlock() {
            if (ISAPEngine.this.m_aadPos == ISAPEngine.this.AADBufferSize) {
                this.absorbMacBlock(ISAPEngine.this.m_aad, 0);
                ISAPEngine.this.m_aadPos = 0;
            } else {
                for (int i2 = 0; i2 < ISAPEngine.this.m_aadPos; ++i2) {
                    this.x0 ^= ((long)ISAPEngine.this.m_aad[i2] & 0xFFL) << (7 - i2 << 3);
                }
            }
            this.x0 ^= 128L << (7 - ISAPEngine.this.m_aadPos << 3);
            this.P12();
            this.x4 ^= 1L;
        }

        @Override
        public void processMACFinal(byte[] byArray, int n2, int n3, byte[] byArray2) {
            if (n3 == ISAPEngine.this.BlockSize) {
                this.absorbMacBlock(byArray, n2);
                n3 = 0;
            } else {
                for (int i2 = 0; i2 < n3; ++i2) {
                    this.x0 ^= ((long)byArray[n2++] & 0xFFL) << (7 - i2 << 3);
                }
            }
            this.x0 ^= 128L << (7 - n3 << 3);
            this.P12();
            Pack.longToBigEndian(this.x0, byArray2, 0);
            Pack.longToBigEndian(this.x1, byArray2, 8);
            long l2 = this.x2;
            long l3 = this.x3;
            long l4 = this.x4;
            this.isap_rk(this.ISAP_IV2_64, byArray2, ISAPEngine.this.KEY_SIZE);
            this.x2 = l2;
            this.x3 = l3;
            this.x4 = l4;
            this.P12();
            Pack.longToBigEndian(this.x0, byArray2, 0);
            Pack.longToBigEndian(this.x1, byArray2, 8);
        }

        public void isap_rk(long l2, byte[] byArray, int n2) {
            this.x0 = this.k64[0];
            this.x1 = this.k64[1];
            this.x2 = l2;
            this.x4 = 0L;
            this.x3 = 0L;
            this.P12();
            for (int i2 = 0; i2 < (n2 << 3) - 1; ++i2) {
                this.x0 ^= ((long)((byArray[i2 >>> 3] >>> 7 - (i2 & 7) & 1) << 7) & 0xFFL) << 56;
                this.PX2();
            }
            this.x0 ^= ((long)byArray[n2 - 1] & 1L) << 7 << 56;
            this.P12();
        }

        @Override
        public void processEncBlock(byte[] byArray, int n2, byte[] byArray2, int n3) {
            long l2 = Pack.littleEndianToLong(byArray, n2);
            long l3 = this.U64BIG(this.x0) ^ l2;
            this.PX1();
            Pack.longToLittleEndian(l3, byArray2, n3);
        }

        @Override
        public void processEncFinalBlock(byte[] byArray, int n2) {
            if (ISAPEngine.this.m_bufPos == ISAPEngine.this.BlockSize) {
                this.processEncBlock(ISAPEngine.this.m_buf, 0, byArray, n2);
            } else {
                byte[] byArray2 = Pack.longToLittleEndian(this.x0);
                int n3 = ISAPEngine.this.m_bufPos;
                while (n3 > 0) {
                    byArray[n2 + n3 - 1] = (byte)(byArray2[ISAPEngine.this.BlockSize - n3] ^ ISAPEngine.this.m_buf[--n3]);
                }
            }
        }

        @Override
        public void reset() {
            this.isap_rk(this.ISAP_IV3_64, ISAPEngine.this.npub, ISAPEngine.this.IV_SIZE);
            this.x3 = this.npub64[0];
            this.x4 = this.npub64[1];
            this.PX1();
            this.swapInternalState();
            this.x0 = this.npub64[0];
            this.x1 = this.npub64[1];
            this.x2 = this.ISAP_IV1_64;
            this.x4 = 0L;
            this.x3 = 0L;
            this.P12();
        }

        private int getLongSize(int n2) {
            return (n2 >>> 3) + ((n2 & 7) != 0 ? 1 : 0);
        }

        private long ROTR(long l2, long l3) {
            return l2 >>> (int)l3 | l2 << (int)(64L - l3);
        }

        protected long U64BIG(long l2) {
            return this.ROTR(l2, 8L) & 0xFF000000FF000000L | this.ROTR(l2, 24L) & 0xFF000000FF0000L | this.ROTR(l2, 40L) & 0xFF000000FF00L | this.ROTR(l2, 56L) & 0xFF000000FFL;
        }

        protected void ROUND(long l2) {
            this.t0 = this.x0 ^ this.x1 ^ this.x2 ^ this.x3 ^ l2 ^ this.x1 & (this.x0 ^ this.x2 ^ this.x4 ^ l2);
            this.t1 = this.x0 ^ this.x2 ^ this.x3 ^ this.x4 ^ l2 ^ (this.x1 ^ this.x2 ^ l2) & (this.x1 ^ this.x3);
            this.t2 = this.x1 ^ this.x2 ^ this.x4 ^ l2 ^ this.x3 & this.x4;
            this.t3 = this.x0 ^ this.x1 ^ this.x2 ^ l2 ^ (this.x0 ^ 0xFFFFFFFFFFFFFFFFL) & (this.x3 ^ this.x4);
            this.t4 = this.x1 ^ this.x3 ^ this.x4 ^ (this.x0 ^ this.x4) & this.x1;
            this.x0 = this.t0 ^ this.ROTR(this.t0, 19L) ^ this.ROTR(this.t0, 28L);
            this.x1 = this.t1 ^ this.ROTR(this.t1, 39L) ^ this.ROTR(this.t1, 61L);
            this.x2 = this.t2 ^ this.ROTR(this.t2, 1L) ^ this.ROTR(this.t2, 6L) ^ 0xFFFFFFFFFFFFFFFFL;
            this.x3 = this.t3 ^ this.ROTR(this.t3, 10L) ^ this.ROTR(this.t3, 17L);
            this.x4 = this.t4 ^ this.ROTR(this.t4, 7L) ^ this.ROTR(this.t4, 41L);
        }

        public void P12() {
            this.ROUND(240L);
            this.ROUND(225L);
            this.ROUND(210L);
            this.ROUND(195L);
            this.ROUND(180L);
            this.ROUND(165L);
            this.P6();
        }

        protected void P6() {
            this.ROUND(150L);
            this.ROUND(135L);
            this.ROUND(120L);
            this.ROUND(105L);
            this.ROUND(90L);
            this.ROUND(75L);
        }
    }

    private class ISAPAEAD_A_128
    extends ISAPAEAD_A {
        public ISAPAEAD_A_128() {
            this.ISAP_IV1_64 = 108156764298152972L;
            this.ISAP_IV2_64 = 180214358336080908L;
            this.ISAP_IV3_64 = 252271952374008844L;
        }

        @Override
        protected void PX1() {
            this.P12();
        }

        @Override
        protected void PX2() {
            this.P12();
        }
    }

    private class ISAPAEAD_A_128A
    extends ISAPAEAD_A {
        public ISAPAEAD_A_128A() {
            this.ISAP_IV1_64 = 108156764297430540L;
            this.ISAP_IV2_64 = 180214358335358476L;
            this.ISAP_IV3_64 = 252271952373286412L;
        }

        @Override
        protected void PX1() {
            this.P6();
        }

        @Override
        protected void PX2() {
            this.ROUND(75L);
        }
    }

    private abstract class ISAPAEAD_K
    implements ISAP_AEAD {
        final int ISAP_STATE_SZ_CRYPTO_NPUBBYTES;
        protected short[] ISAP_IV1_16;
        protected short[] ISAP_IV2_16;
        protected short[] ISAP_IV3_16;
        protected short[] k16;
        protected short[] iv16;
        private final int[] KeccakF400RoundConstants;
        protected short[] SX;
        protected short[] macSX;
        protected short[] E;
        protected short[] C;
        protected short[] macE;
        protected short[] macC;

        public ISAPAEAD_K() {
            this.ISAP_STATE_SZ_CRYPTO_NPUBBYTES = 40 - ISAPEngine.this.IV_SIZE;
            this.KeccakF400RoundConstants = new int[]{1, 32898, 32906, 32768, 32907, 1, 32897, 32777, 138, 136, 32777, 10, 32907, 139, 32905, 32771, 32770, 128, 32778, 10};
            this.SX = new short[25];
            this.macSX = new short[25];
            this.E = new short[25];
            this.C = new short[5];
            this.macE = new short[25];
            this.macC = new short[5];
            ISAPEngine.this.ISAP_rH = 144;
            ISAPEngine.this.BlockSize = ISAPEngine.this.ISAP_rH + 7 >> 3;
        }

        @Override
        public void init() {
            this.k16 = new short[ISAPEngine.this.k.length >> 1];
            this.byteToShort(ISAPEngine.this.k, this.k16, this.k16.length);
            this.iv16 = new short[ISAPEngine.this.npub.length >> 1];
            this.byteToShort(ISAPEngine.this.npub, this.iv16, this.iv16.length);
        }

        @Override
        public void reset() {
            Arrays.fill(this.SX, (short)0);
            this.isap_rk(this.ISAP_IV3_16, ISAPEngine.this.npub, ISAPEngine.this.IV_SIZE, this.SX, this.ISAP_STATE_SZ_CRYPTO_NPUBBYTES, this.C);
            System.arraycopy(this.iv16, 0, this.SX, 17, 8);
            this.PermuteRoundsKX(this.SX, this.E, this.C);
            this.swapInternalState();
            Arrays.fill(this.SX, 12, 25, (short)0);
            System.arraycopy(this.iv16, 0, this.SX, 0, 8);
            System.arraycopy(this.ISAP_IV1_16, 0, this.SX, 8, 4);
            this.PermuteRoundsHX(this.SX, this.E, this.C);
        }

        @Override
        public void swapInternalState() {
            short[] sArray = this.SX;
            this.SX = this.macSX;
            this.macSX = sArray;
            sArray = this.E;
            this.E = this.macE;
            this.macE = sArray;
            sArray = this.C;
            this.C = this.macC;
            this.macC = sArray;
        }

        protected abstract void PermuteRoundsHX(short[] var1, short[] var2, short[] var3);

        protected abstract void PermuteRoundsKX(short[] var1, short[] var2, short[] var3);

        protected abstract void PermuteRoundsBX(short[] var1, short[] var2, short[] var3);

        @Override
        public void absorbMacBlock(byte[] byArray, int n2) {
            this.byteToShortXor(byArray, n2, this.SX, ISAPEngine.this.BlockSize >> 1);
            this.PermuteRoundsHX(this.SX, this.E, this.C);
        }

        @Override
        public void absorbFinalAADBlock() {
            if (ISAPEngine.this.m_aadPos == ISAPEngine.this.AADBufferSize) {
                this.absorbMacBlock(ISAPEngine.this.m_aad, 0);
                ISAPEngine.this.m_aadPos = 0;
            } else {
                for (int i2 = 0; i2 < ISAPEngine.this.m_aadPos; ++i2) {
                    int n2 = i2 >> 1;
                    this.SX[n2] = (short)(this.SX[n2] ^ (ISAPEngine.this.m_aad[i2] & 0xFF) << ((i2 & 1) << 3));
                }
            }
            int n3 = ISAPEngine.this.m_aadPos >> 1;
            this.SX[n3] = (short)(this.SX[n3] ^ 128 << ((ISAPEngine.this.m_aadPos & 1) << 3));
            this.PermuteRoundsHX(this.SX, this.E, this.C);
            this.SX[24] = (short)(this.SX[24] ^ 0x100);
        }

        public void isap_rk(short[] sArray, byte[] byArray, int n2, short[] sArray2, int n3, short[] sArray3) {
            short[] sArray4 = new short[25];
            short[] sArray5 = new short[25];
            System.arraycopy(this.k16, 0, sArray4, 0, 8);
            System.arraycopy(sArray, 0, sArray4, 8, 4);
            this.PermuteRoundsKX(sArray4, sArray5, sArray3);
            for (int i2 = 0; i2 < (n2 << 3) - 1; ++i2) {
                sArray4[0] = (short)(sArray4[0] ^ (byArray[i2 >> 3] >>> 7 - (i2 & 7) & 1) << 7);
                this.PermuteRoundsBX(sArray4, sArray5, sArray3);
            }
            sArray4[0] = (short)(sArray4[0] ^ (byArray[n2 - 1] & 1) << 7);
            this.PermuteRoundsKX(sArray4, sArray5, sArray3);
            System.arraycopy(sArray4, 0, sArray2, 0, n3 == this.ISAP_STATE_SZ_CRYPTO_NPUBBYTES ? 17 : 8);
        }

        @Override
        public void processMACFinal(byte[] byArray, int n2, int n3, byte[] byArray2) {
            if (n3 == ISAPEngine.this.BlockSize) {
                this.absorbMacBlock(byArray, n2);
                n3 = 0;
            } else {
                for (int i2 = 0; i2 < n3; ++i2) {
                    int n4 = i2 >> 1;
                    this.SX[n4] = (short)(this.SX[n4] ^ (byArray[n2++] & 0xFF) << ((i2 & 1) << 3));
                }
            }
            int n5 = n3 >> 1;
            this.SX[n5] = (short)(this.SX[n5] ^ 128 << ((n3 & 1) << 3));
            this.PermuteRoundsHX(this.SX, this.E, this.C);
            this.shortToByte(this.SX, byArray2);
            this.isap_rk(this.ISAP_IV2_16, byArray2, ISAPEngine.this.KEY_SIZE, this.SX, ISAPEngine.this.KEY_SIZE, this.C);
            this.PermuteRoundsHX(this.SX, this.E, this.C);
            this.shortToByte(this.SX, byArray2);
        }

        @Override
        public void processEncBlock(byte[] byArray, int n2, byte[] byArray2, int n3) {
            for (int i2 = 0; i2 < ISAPEngine.this.BlockSize; ++i2) {
                byArray2[n3++] = (byte)(this.SX[i2 >> 1] >>> ((i2 & 1) << 3) ^ byArray[n2++]);
            }
            this.PermuteRoundsKX(this.SX, this.E, this.C);
        }

        @Override
        public void processEncFinalBlock(byte[] byArray, int n2) {
            int n3 = ISAPEngine.this.m_bufPos;
            for (int i2 = 0; i2 < n3; ++i2) {
                byArray[n2++] = (byte)(this.SX[i2 >> 1] >>> ((i2 & 1) << 3) ^ ISAPEngine.this.m_buf[i2]);
            }
        }

        private void byteToShortXor(byte[] byArray, int n2, short[] sArray, int n3) {
            for (int i2 = 0; i2 < n3; ++i2) {
                int n4 = i2;
                sArray[n4] = (short)(sArray[n4] ^ Pack.littleEndianToShort(byArray, n2 + (i2 << 1)));
            }
        }

        private void byteToShort(byte[] byArray, short[] sArray, int n2) {
            for (int i2 = 0; i2 < n2; ++i2) {
                sArray[i2] = Pack.littleEndianToShort(byArray, i2 << 1);
            }
        }

        private void shortToByte(short[] sArray, byte[] byArray) {
            for (int i2 = 0; i2 < 8; ++i2) {
                Pack.shortToLittleEndian(sArray[i2], byArray, i2 << 1);
            }
        }

        protected void rounds12X(short[] sArray, short[] sArray2, short[] sArray3) {
            this.prepareThetaX(sArray, sArray3);
            this.rounds_8_18(sArray, sArray2, sArray3);
        }

        protected void rounds_4_18(short[] sArray, short[] sArray2, short[] sArray3) {
            this.thetaRhoPiChiIotaPrepareTheta(4, sArray, sArray2, sArray3);
            this.thetaRhoPiChiIotaPrepareTheta(5, sArray2, sArray, sArray3);
            this.thetaRhoPiChiIotaPrepareTheta(6, sArray, sArray2, sArray3);
            this.thetaRhoPiChiIotaPrepareTheta(7, sArray2, sArray, sArray3);
            this.rounds_8_18(sArray, sArray2, sArray3);
        }

        protected void rounds_8_18(short[] sArray, short[] sArray2, short[] sArray3) {
            this.thetaRhoPiChiIotaPrepareTheta(8, sArray, sArray2, sArray3);
            this.thetaRhoPiChiIotaPrepareTheta(9, sArray2, sArray, sArray3);
            this.thetaRhoPiChiIotaPrepareTheta(10, sArray, sArray2, sArray3);
            this.thetaRhoPiChiIotaPrepareTheta(11, sArray2, sArray, sArray3);
            this.rounds_12_18(sArray, sArray2, sArray3);
        }

        protected void rounds_12_18(short[] sArray, short[] sArray2, short[] sArray3) {
            this.thetaRhoPiChiIotaPrepareTheta(12, sArray, sArray2, sArray3);
            this.thetaRhoPiChiIotaPrepareTheta(13, sArray2, sArray, sArray3);
            this.thetaRhoPiChiIotaPrepareTheta(14, sArray, sArray2, sArray3);
            this.thetaRhoPiChiIotaPrepareTheta(15, sArray2, sArray, sArray3);
            this.thetaRhoPiChiIotaPrepareTheta(16, sArray, sArray2, sArray3);
            this.thetaRhoPiChiIotaPrepareTheta(17, sArray2, sArray, sArray3);
            this.thetaRhoPiChiIotaPrepareTheta(18, sArray, sArray2, sArray3);
            this.thetaRhoPiChiIota(sArray2, sArray, sArray3);
        }

        protected void prepareThetaX(short[] sArray, short[] sArray2) {
            sArray2[0] = (short)(sArray[0] ^ sArray[5] ^ sArray[10] ^ sArray[15] ^ sArray[20]);
            sArray2[1] = (short)(sArray[1] ^ sArray[6] ^ sArray[11] ^ sArray[16] ^ sArray[21]);
            sArray2[2] = (short)(sArray[2] ^ sArray[7] ^ sArray[12] ^ sArray[17] ^ sArray[22]);
            sArray2[3] = (short)(sArray[3] ^ sArray[8] ^ sArray[13] ^ sArray[18] ^ sArray[23]);
            sArray2[4] = (short)(sArray[4] ^ sArray[9] ^ sArray[14] ^ sArray[19] ^ sArray[24]);
        }

        private short ROL16(short s2, int n2) {
            return (short)((s2 & 0xFFFF) << n2 ^ (s2 & 0xFFFF) >>> 16 - n2);
        }

        protected void thetaRhoPiChiIotaPrepareTheta(int n2, short[] sArray, short[] sArray2, short[] sArray3) {
            short s2 = (short)(sArray3[4] ^ this.ROL16(sArray3[1], 1));
            short s3 = (short)(sArray3[0] ^ this.ROL16(sArray3[2], 1));
            short s4 = (short)(sArray3[1] ^ this.ROL16(sArray3[3], 1));
            short s5 = (short)(sArray3[2] ^ this.ROL16(sArray3[4], 1));
            short s6 = (short)(sArray3[3] ^ this.ROL16(sArray3[0], 1));
            short s7 = sArray[0] = (short)(sArray[0] ^ s2);
            sArray[6] = (short)(sArray[6] ^ s3);
            short s8 = this.ROL16(sArray[6], 12);
            sArray[12] = (short)(sArray[12] ^ s4);
            short s9 = this.ROL16(sArray[12], 11);
            sArray[18] = (short)(sArray[18] ^ s5);
            short s10 = this.ROL16(sArray[18], 5);
            sArray[24] = (short)(sArray[24] ^ s6);
            short s11 = this.ROL16(sArray[24], 14);
            sArray3[0] = sArray2[0] = (short)(s7 ^ ~s8 & s9 ^ this.KeccakF400RoundConstants[n2]);
            sArray3[1] = sArray2[1] = (short)(s8 ^ ~s9 & s10);
            sArray3[2] = sArray2[2] = (short)(s9 ^ ~s10 & s11);
            sArray3[3] = sArray2[3] = (short)(s10 ^ ~s11 & s7);
            sArray3[4] = sArray2[4] = (short)(s11 ^ ~s7 & s8);
            sArray[3] = (short)(sArray[3] ^ s5);
            s7 = this.ROL16(sArray[3], 12);
            sArray[9] = (short)(sArray[9] ^ s6);
            s8 = this.ROL16(sArray[9], 4);
            sArray[10] = (short)(sArray[10] ^ s2);
            s9 = this.ROL16(sArray[10], 3);
            sArray[16] = (short)(sArray[16] ^ s3);
            s10 = this.ROL16(sArray[16], 13);
            sArray[22] = (short)(sArray[22] ^ s4);
            s11 = this.ROL16(sArray[22], 13);
            sArray2[5] = (short)(s7 ^ ~s8 & s9);
            sArray3[0] = (short)(sArray3[0] ^ sArray2[5]);
            sArray2[6] = (short)(s8 ^ ~s9 & s10);
            sArray3[1] = (short)(sArray3[1] ^ sArray2[6]);
            sArray2[7] = (short)(s9 ^ ~s10 & s11);
            sArray3[2] = (short)(sArray3[2] ^ sArray2[7]);
            sArray2[8] = (short)(s10 ^ ~s11 & s7);
            sArray3[3] = (short)(sArray3[3] ^ sArray2[8]);
            sArray2[9] = (short)(s11 ^ ~s7 & s8);
            sArray3[4] = (short)(sArray3[4] ^ sArray2[9]);
            sArray[1] = (short)(sArray[1] ^ s3);
            s7 = this.ROL16(sArray[1], 1);
            sArray[7] = (short)(sArray[7] ^ s4);
            s8 = this.ROL16(sArray[7], 6);
            sArray[13] = (short)(sArray[13] ^ s5);
            s9 = this.ROL16(sArray[13], 9);
            sArray[19] = (short)(sArray[19] ^ s6);
            s10 = this.ROL16(sArray[19], 8);
            sArray[20] = (short)(sArray[20] ^ s2);
            s11 = this.ROL16(sArray[20], 2);
            sArray2[10] = (short)(s7 ^ ~s8 & s9);
            sArray3[0] = (short)(sArray3[0] ^ sArray2[10]);
            sArray2[11] = (short)(s8 ^ ~s9 & s10);
            sArray3[1] = (short)(sArray3[1] ^ sArray2[11]);
            sArray2[12] = (short)(s9 ^ ~s10 & s11);
            sArray3[2] = (short)(sArray3[2] ^ sArray2[12]);
            sArray2[13] = (short)(s10 ^ ~s11 & s7);
            sArray3[3] = (short)(sArray3[3] ^ sArray2[13]);
            sArray2[14] = (short)(s11 ^ ~s7 & s8);
            sArray3[4] = (short)(sArray3[4] ^ sArray2[14]);
            sArray[4] = (short)(sArray[4] ^ s6);
            s7 = this.ROL16(sArray[4], 11);
            sArray[5] = (short)(sArray[5] ^ s2);
            s8 = this.ROL16(sArray[5], 4);
            sArray[11] = (short)(sArray[11] ^ s3);
            s9 = this.ROL16(sArray[11], 10);
            sArray[17] = (short)(sArray[17] ^ s4);
            s10 = this.ROL16(sArray[17], 15);
            sArray[23] = (short)(sArray[23] ^ s5);
            s11 = this.ROL16(sArray[23], 8);
            sArray2[15] = (short)(s7 ^ ~s8 & s9);
            sArray3[0] = (short)(sArray3[0] ^ sArray2[15]);
            sArray2[16] = (short)(s8 ^ ~s9 & s10);
            sArray3[1] = (short)(sArray3[1] ^ sArray2[16]);
            sArray2[17] = (short)(s9 ^ ~s10 & s11);
            sArray3[2] = (short)(sArray3[2] ^ sArray2[17]);
            sArray2[18] = (short)(s10 ^ ~s11 & s7);
            sArray3[3] = (short)(sArray3[3] ^ sArray2[18]);
            sArray2[19] = (short)(s11 ^ ~s7 & s8);
            sArray3[4] = (short)(sArray3[4] ^ sArray2[19]);
            sArray[2] = (short)(sArray[2] ^ s4);
            s7 = this.ROL16(sArray[2], 14);
            sArray[8] = (short)(sArray[8] ^ s5);
            s8 = this.ROL16(sArray[8], 7);
            sArray[14] = (short)(sArray[14] ^ s6);
            s9 = this.ROL16(sArray[14], 7);
            sArray[15] = (short)(sArray[15] ^ s2);
            s10 = this.ROL16(sArray[15], 9);
            sArray[21] = (short)(sArray[21] ^ s3);
            s11 = this.ROL16(sArray[21], 2);
            sArray2[20] = (short)(s7 ^ ~s8 & s9);
            sArray3[0] = (short)(sArray3[0] ^ sArray2[20]);
            sArray2[21] = (short)(s8 ^ ~s9 & s10);
            sArray3[1] = (short)(sArray3[1] ^ sArray2[21]);
            sArray2[22] = (short)(s9 ^ ~s10 & s11);
            sArray3[2] = (short)(sArray3[2] ^ sArray2[22]);
            sArray2[23] = (short)(s10 ^ ~s11 & s7);
            sArray3[3] = (short)(sArray3[3] ^ sArray2[23]);
            sArray2[24] = (short)(s11 ^ ~s7 & s8);
            sArray3[4] = (short)(sArray3[4] ^ sArray2[24]);
        }

        protected void thetaRhoPiChiIota(short[] sArray, short[] sArray2, short[] sArray3) {
            short s2 = (short)(sArray3[4] ^ this.ROL16(sArray3[1], 1));
            short s3 = (short)(sArray3[0] ^ this.ROL16(sArray3[2], 1));
            short s4 = (short)(sArray3[1] ^ this.ROL16(sArray3[3], 1));
            short s5 = (short)(sArray3[2] ^ this.ROL16(sArray3[4], 1));
            short s6 = (short)(sArray3[3] ^ this.ROL16(sArray3[0], 1));
            short s7 = sArray[0] = (short)(sArray[0] ^ s2);
            sArray[6] = (short)(sArray[6] ^ s3);
            short s8 = this.ROL16(sArray[6], 12);
            sArray[12] = (short)(sArray[12] ^ s4);
            short s9 = this.ROL16(sArray[12], 11);
            sArray[18] = (short)(sArray[18] ^ s5);
            short s10 = this.ROL16(sArray[18], 5);
            sArray[24] = (short)(sArray[24] ^ s6);
            short s11 = this.ROL16(sArray[24], 14);
            sArray2[0] = (short)(s7 ^ ~s8 & s9 ^ this.KeccakF400RoundConstants[19]);
            sArray2[1] = (short)(s8 ^ ~s9 & s10);
            sArray2[2] = (short)(s9 ^ ~s10 & s11);
            sArray2[3] = (short)(s10 ^ ~s11 & s7);
            sArray2[4] = (short)(s11 ^ ~s7 & s8);
            sArray[3] = (short)(sArray[3] ^ s5);
            s7 = this.ROL16(sArray[3], 12);
            sArray[9] = (short)(sArray[9] ^ s6);
            s8 = this.ROL16(sArray[9], 4);
            sArray[10] = (short)(sArray[10] ^ s2);
            s9 = this.ROL16(sArray[10], 3);
            sArray[16] = (short)(sArray[16] ^ s3);
            s10 = this.ROL16(sArray[16], 13);
            sArray[22] = (short)(sArray[22] ^ s4);
            s11 = this.ROL16(sArray[22], 13);
            sArray2[5] = (short)(s7 ^ ~s8 & s9);
            sArray2[6] = (short)(s8 ^ ~s9 & s10);
            sArray2[7] = (short)(s9 ^ ~s10 & s11);
            sArray2[8] = (short)(s10 ^ ~s11 & s7);
            sArray2[9] = (short)(s11 ^ ~s7 & s8);
            sArray[1] = (short)(sArray[1] ^ s3);
            s7 = this.ROL16(sArray[1], 1);
            sArray[7] = (short)(sArray[7] ^ s4);
            s8 = this.ROL16(sArray[7], 6);
            sArray[13] = (short)(sArray[13] ^ s5);
            s9 = this.ROL16(sArray[13], 9);
            sArray[19] = (short)(sArray[19] ^ s6);
            s10 = this.ROL16(sArray[19], 8);
            sArray[20] = (short)(sArray[20] ^ s2);
            s11 = this.ROL16(sArray[20], 2);
            sArray2[10] = (short)(s7 ^ ~s8 & s9);
            sArray2[11] = (short)(s8 ^ ~s9 & s10);
            sArray2[12] = (short)(s9 ^ ~s10 & s11);
            sArray2[13] = (short)(s10 ^ ~s11 & s7);
            sArray2[14] = (short)(s11 ^ ~s7 & s8);
            sArray[4] = (short)(sArray[4] ^ s6);
            s7 = this.ROL16(sArray[4], 11);
            sArray[5] = (short)(sArray[5] ^ s2);
            s8 = this.ROL16(sArray[5], 4);
            sArray[11] = (short)(sArray[11] ^ s3);
            s9 = this.ROL16(sArray[11], 10);
            sArray[17] = (short)(sArray[17] ^ s4);
            s10 = this.ROL16(sArray[17], 15);
            sArray[23] = (short)(sArray[23] ^ s5);
            s11 = this.ROL16(sArray[23], 8);
            sArray2[15] = (short)(s7 ^ ~s8 & s9);
            sArray2[16] = (short)(s8 ^ ~s9 & s10);
            sArray2[17] = (short)(s9 ^ ~s10 & s11);
            sArray2[18] = (short)(s10 ^ ~s11 & s7);
            sArray2[19] = (short)(s11 ^ ~s7 & s8);
            sArray[2] = (short)(sArray[2] ^ s4);
            s7 = this.ROL16(sArray[2], 14);
            sArray[8] = (short)(sArray[8] ^ s5);
            s8 = this.ROL16(sArray[8], 7);
            sArray[14] = (short)(sArray[14] ^ s6);
            s9 = this.ROL16(sArray[14], 7);
            sArray[15] = (short)(sArray[15] ^ s2);
            s10 = this.ROL16(sArray[15], 9);
            sArray[21] = (short)(sArray[21] ^ s3);
            s11 = this.ROL16(sArray[21], 2);
            sArray2[20] = (short)(s7 ^ ~s8 & s9);
            sArray2[21] = (short)(s8 ^ ~s9 & s10);
            sArray2[22] = (short)(s9 ^ ~s10 & s11);
            sArray2[23] = (short)(s10 ^ ~s11 & s7);
            sArray2[24] = (short)(s11 ^ ~s7 & s8);
        }
    }

    private class ISAPAEAD_K_128
    extends ISAPAEAD_K {
        public ISAPAEAD_K_128() {
            this.ISAP_IV1_16 = new short[]{-32767, 400, 3092, 3084};
            this.ISAP_IV2_16 = new short[]{-32766, 400, 3092, 3084};
            this.ISAP_IV3_16 = new short[]{-32765, 400, 3092, 3084};
        }

        @Override
        protected void PermuteRoundsHX(short[] sArray, short[] sArray2, short[] sArray3) {
            this.prepareThetaX(sArray, sArray3);
            this.thetaRhoPiChiIotaPrepareTheta(0, sArray, sArray2, sArray3);
            this.thetaRhoPiChiIotaPrepareTheta(1, sArray2, sArray, sArray3);
            this.thetaRhoPiChiIotaPrepareTheta(2, sArray, sArray2, sArray3);
            this.thetaRhoPiChiIotaPrepareTheta(3, sArray2, sArray, sArray3);
            this.rounds_4_18(sArray, sArray2, sArray3);
        }

        @Override
        protected void PermuteRoundsKX(short[] sArray, short[] sArray2, short[] sArray3) {
            this.rounds12X(sArray, sArray2, sArray3);
        }

        @Override
        protected void PermuteRoundsBX(short[] sArray, short[] sArray2, short[] sArray3) {
            this.rounds12X(sArray, sArray2, sArray3);
        }
    }

    private class ISAPAEAD_K_128A
    extends ISAPAEAD_K {
        public ISAPAEAD_K_128A() {
            this.ISAP_IV1_16 = new short[]{-32767, 400, 272, 2056};
            this.ISAP_IV2_16 = new short[]{-32766, 400, 272, 2056};
            this.ISAP_IV3_16 = new short[]{-32765, 400, 272, 2056};
        }

        @Override
        protected void PermuteRoundsHX(short[] sArray, short[] sArray2, short[] sArray3) {
            this.prepareThetaX(sArray, sArray3);
            this.rounds_4_18(sArray, sArray2, sArray3);
        }

        @Override
        protected void PermuteRoundsKX(short[] sArray, short[] sArray2, short[] sArray3) {
            this.prepareThetaX(sArray, sArray3);
            this.rounds_12_18(sArray, sArray2, sArray3);
        }

        @Override
        protected void PermuteRoundsBX(short[] sArray, short[] sArray2, short[] sArray3) {
            this.prepareThetaX(sArray, sArray3);
            this.thetaRhoPiChiIotaPrepareTheta(19, sArray, sArray2, sArray3);
            System.arraycopy(sArray2, 0, sArray, 0, sArray2.length);
        }
    }

    private static interface ISAP_AEAD {
        public void init();

        public void reset();

        public void absorbMacBlock(byte[] var1, int var2);

        public void absorbFinalAADBlock();

        public void swapInternalState();

        public void processEncBlock(byte[] var1, int var2, byte[] var3, int var4);

        public void processEncFinalBlock(byte[] var1, int var2);

        public void processMACFinal(byte[] var1, int var2, int var3, byte[] var4);
    }

    public static enum IsapType {
        ISAP_A_128A,
        ISAP_K_128A,
        ISAP_A_128,
        ISAP_K_128;

    }
}

