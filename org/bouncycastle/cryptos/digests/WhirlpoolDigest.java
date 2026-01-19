/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.digests;

import org.bouncycastle.crypto.CryptoServicePurpose;
import org.bouncycastle.crypto.CryptoServicesRegistrar;
import org.bouncycastle.crypto.ExtendedDigest;
import org.bouncycastle.crypto.digests.Utils;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Memoable;
import org.bouncycastle.util.Pack;

public final class WhirlpoolDigest
implements ExtendedDigest,
Memoable {
    private static final int BITCOUNT_ARRAY_SIZE = 32;
    private static final int BYTE_LENGTH = 64;
    private static final int DIGEST_LENGTH_BYTES = 64;
    private static final int REDUCTION_POLYNOMIAL = 285;
    private static final int ROUNDS = 10;
    private static final int[] SBOX = new int[]{24, 35, 198, 232, 135, 184, 1, 79, 54, 166, 210, 245, 121, 111, 145, 82, 96, 188, 155, 142, 163, 12, 123, 53, 29, 224, 215, 194, 46, 75, 254, 87, 21, 119, 55, 229, 159, 240, 74, 218, 88, 201, 41, 10, 177, 160, 107, 133, 189, 93, 16, 244, 203, 62, 5, 103, 228, 39, 65, 139, 167, 125, 149, 216, 251, 238, 124, 102, 221, 23, 71, 158, 202, 45, 191, 7, 173, 90, 131, 51, 99, 2, 170, 113, 200, 25, 73, 217, 242, 227, 91, 136, 154, 38, 50, 176, 233, 15, 213, 128, 190, 205, 52, 72, 255, 122, 144, 95, 32, 104, 26, 174, 180, 84, 147, 34, 100, 241, 115, 18, 64, 8, 195, 236, 219, 161, 141, 61, 151, 0, 207, 43, 118, 130, 214, 27, 181, 175, 106, 80, 69, 243, 48, 239, 63, 85, 162, 234, 101, 186, 47, 192, 222, 28, 253, 77, 146, 117, 6, 138, 178, 230, 14, 31, 98, 212, 168, 150, 249, 197, 37, 89, 132, 114, 57, 76, 94, 120, 56, 140, 209, 165, 226, 97, 179, 33, 156, 30, 67, 199, 252, 4, 81, 153, 109, 13, 250, 223, 126, 36, 59, 171, 206, 17, 143, 78, 183, 235, 60, 129, 148, 247, 185, 19, 44, 211, 231, 110, 196, 3, 86, 68, 127, 169, 42, 187, 193, 83, 220, 11, 157, 108, 49, 116, 246, 70, 172, 137, 20, 225, 22, 58, 105, 9, 112, 182, 208, 237, 204, 66, 152, 164, 40, 92, 248, 134};
    private static final long[] C0 = new long[256];
    private static final long[] C1 = new long[256];
    private static final long[] C2 = new long[256];
    private static final long[] C3 = new long[256];
    private static final long[] C4 = new long[256];
    private static final long[] C5 = new long[256];
    private static final long[] C6 = new long[256];
    private static final long[] C7 = new long[256];
    private static final short[] EIGHT = new short[32];
    private final long[] _rc = new long[11];
    private final CryptoServicePurpose purpose;
    private byte[] _buffer = new byte[64];
    private int _bufferPos = 0;
    private short[] _bitCount = new short[32];
    private long[] _hash = new long[8];
    private long[] _K = new long[8];
    private long[] _L = new long[8];
    private long[] _block = new long[8];
    private long[] _state = new long[8];

    private static int mulX(int n2) {
        return n2 << 1 ^ -(n2 >>> 7) & 0x11D;
    }

    private static long packIntoLong(int n2, int n3, int n4, int n5, int n6, int n7, int n8, int n9) {
        return (long)n2 << 56 ^ (long)n3 << 48 ^ (long)n4 << 40 ^ (long)n5 << 32 ^ (long)n6 << 24 ^ (long)n7 << 16 ^ (long)n8 << 8 ^ (long)n9;
    }

    public WhirlpoolDigest() {
        this(CryptoServicePurpose.ANY);
    }

    public WhirlpoolDigest(CryptoServicePurpose cryptoServicePurpose) {
        this._rc[0] = 0L;
        for (int i2 = 1; i2 <= 10; ++i2) {
            int n2 = 8 * (i2 - 1);
            this._rc[i2] = C0[n2] & 0xFF00000000000000L ^ C1[n2 + 1] & 0xFF000000000000L ^ C2[n2 + 2] & 0xFF0000000000L ^ C3[n2 + 3] & 0xFF00000000L ^ C4[n2 + 4] & 0xFF000000L ^ C5[n2 + 5] & 0xFF0000L ^ C6[n2 + 6] & 0xFF00L ^ C7[n2 + 7] & 0xFFL;
        }
        this.purpose = cryptoServicePurpose;
        CryptoServicesRegistrar.checkConstraints(Utils.getDefaultProperties(this, this.getDigestSize(), cryptoServicePurpose));
    }

    public WhirlpoolDigest(WhirlpoolDigest whirlpoolDigest) {
        this.purpose = whirlpoolDigest.purpose;
        this.reset(whirlpoolDigest);
        CryptoServicesRegistrar.checkConstraints(Utils.getDefaultProperties(this, this.getDigestSize(), this.purpose));
    }

    @Override
    public String getAlgorithmName() {
        return "Whirlpool";
    }

    @Override
    public int getDigestSize() {
        return 64;
    }

    @Override
    public int doFinal(byte[] byArray, int n2) {
        this.finish();
        Pack.longToBigEndian(this._hash, byArray, n2);
        this.reset();
        return this.getDigestSize();
    }

    @Override
    public void reset() {
        this._bufferPos = 0;
        Arrays.fill(this._bitCount, (short)0);
        Arrays.fill(this._buffer, (byte)0);
        Arrays.fill(this._hash, 0L);
        Arrays.fill(this._K, 0L);
        Arrays.fill(this._L, 0L);
        Arrays.fill(this._block, 0L);
        Arrays.fill(this._state, 0L);
    }

    private void processFilledBuffer(byte[] byArray, int n2) {
        Pack.bigEndianToLong(this._buffer, 0, this._block);
        this.processBlock();
        this._bufferPos = 0;
        Arrays.fill(this._buffer, (byte)0);
    }

    protected void processBlock() {
        int n2;
        for (n2 = 0; n2 < 8; ++n2) {
            this._K[n2] = this._hash[n2];
            this._state[n2] = this._block[n2] ^ this._K[n2];
        }
        for (n2 = 1; n2 <= 10; ++n2) {
            int n3;
            for (n3 = 0; n3 < 8; ++n3) {
                this._L[n3] = 0L;
                int n4 = n3;
                this._L[n4] = this._L[n4] ^ C0[(int)(this._K[n3 - 0 & 7] >>> 56) & 0xFF];
                int n5 = n3;
                this._L[n5] = this._L[n5] ^ C1[(int)(this._K[n3 - 1 & 7] >>> 48) & 0xFF];
                int n6 = n3;
                this._L[n6] = this._L[n6] ^ C2[(int)(this._K[n3 - 2 & 7] >>> 40) & 0xFF];
                int n7 = n3;
                this._L[n7] = this._L[n7] ^ C3[(int)(this._K[n3 - 3 & 7] >>> 32) & 0xFF];
                int n8 = n3;
                this._L[n8] = this._L[n8] ^ C4[(int)(this._K[n3 - 4 & 7] >>> 24) & 0xFF];
                int n9 = n3;
                this._L[n9] = this._L[n9] ^ C5[(int)(this._K[n3 - 5 & 7] >>> 16) & 0xFF];
                int n10 = n3;
                this._L[n10] = this._L[n10] ^ C6[(int)(this._K[n3 - 6 & 7] >>> 8) & 0xFF];
                int n11 = n3;
                this._L[n11] = this._L[n11] ^ C7[(int)this._K[n3 - 7 & 7] & 0xFF];
            }
            System.arraycopy(this._L, 0, this._K, 0, this._K.length);
            this._K[0] = this._K[0] ^ this._rc[n2];
            for (n3 = 0; n3 < 8; ++n3) {
                this._L[n3] = this._K[n3];
                int n12 = n3;
                this._L[n12] = this._L[n12] ^ C0[(int)(this._state[n3 - 0 & 7] >>> 56) & 0xFF];
                int n13 = n3;
                this._L[n13] = this._L[n13] ^ C1[(int)(this._state[n3 - 1 & 7] >>> 48) & 0xFF];
                int n14 = n3;
                this._L[n14] = this._L[n14] ^ C2[(int)(this._state[n3 - 2 & 7] >>> 40) & 0xFF];
                int n15 = n3;
                this._L[n15] = this._L[n15] ^ C3[(int)(this._state[n3 - 3 & 7] >>> 32) & 0xFF];
                int n16 = n3;
                this._L[n16] = this._L[n16] ^ C4[(int)(this._state[n3 - 4 & 7] >>> 24) & 0xFF];
                int n17 = n3;
                this._L[n17] = this._L[n17] ^ C5[(int)(this._state[n3 - 5 & 7] >>> 16) & 0xFF];
                int n18 = n3;
                this._L[n18] = this._L[n18] ^ C6[(int)(this._state[n3 - 6 & 7] >>> 8) & 0xFF];
                int n19 = n3;
                this._L[n19] = this._L[n19] ^ C7[(int)this._state[n3 - 7 & 7] & 0xFF];
            }
            System.arraycopy(this._L, 0, this._state, 0, this._state.length);
        }
        for (n2 = 0; n2 < 8; ++n2) {
            int n20 = n2;
            this._hash[n20] = this._hash[n20] ^ (this._state[n2] ^ this._block[n2]);
        }
    }

    @Override
    public void update(byte by) {
        this._buffer[this._bufferPos] = by;
        if (++this._bufferPos == this._buffer.length) {
            this.processFilledBuffer(this._buffer, 0);
        }
        this.increment();
    }

    private void increment() {
        int n2 = 0;
        for (int i2 = this._bitCount.length - 1; i2 >= 0; --i2) {
            int n3 = (this._bitCount[i2] & 0xFF) + EIGHT[i2] + n2;
            n2 = n3 >>> 8;
            this._bitCount[i2] = (short)(n3 & 0xFF);
        }
    }

    @Override
    public void update(byte[] byArray, int n2, int n3) {
        while (n3 > 0) {
            this.update(byArray[n2]);
            ++n2;
            --n3;
        }
    }

    private void finish() {
        byte[] byArray = this.copyBitLength();
        int n2 = this._bufferPos++;
        this._buffer[n2] = (byte)(this._buffer[n2] | 0x80);
        if (this._bufferPos == this._buffer.length) {
            this.processFilledBuffer(this._buffer, 0);
        }
        if (this._bufferPos > 32) {
            while (this._bufferPos != 0) {
                this.update((byte)0);
            }
        }
        while (this._bufferPos <= 32) {
            this.update((byte)0);
        }
        System.arraycopy(byArray, 0, this._buffer, 32, byArray.length);
        this.processFilledBuffer(this._buffer, 0);
    }

    private byte[] copyBitLength() {
        byte[] byArray = new byte[32];
        for (int i2 = 0; i2 < byArray.length; ++i2) {
            byArray[i2] = (byte)(this._bitCount[i2] & 0xFF);
        }
        return byArray;
    }

    @Override
    public int getByteLength() {
        return 64;
    }

    @Override
    public Memoable copy() {
        return new WhirlpoolDigest(this);
    }

    @Override
    public void reset(Memoable memoable) {
        WhirlpoolDigest whirlpoolDigest = (WhirlpoolDigest)memoable;
        System.arraycopy(whirlpoolDigest._rc, 0, this._rc, 0, this._rc.length);
        System.arraycopy(whirlpoolDigest._buffer, 0, this._buffer, 0, this._buffer.length);
        this._bufferPos = whirlpoolDigest._bufferPos;
        System.arraycopy(whirlpoolDigest._bitCount, 0, this._bitCount, 0, this._bitCount.length);
        System.arraycopy(whirlpoolDigest._hash, 0, this._hash, 0, this._hash.length);
        System.arraycopy(whirlpoolDigest._K, 0, this._K, 0, this._K.length);
        System.arraycopy(whirlpoolDigest._L, 0, this._L, 0, this._L.length);
        System.arraycopy(whirlpoolDigest._block, 0, this._block, 0, this._block.length);
        System.arraycopy(whirlpoolDigest._state, 0, this._state, 0, this._state.length);
    }

    static {
        WhirlpoolDigest.EIGHT[31] = 8;
        for (int i2 = 0; i2 < 256; ++i2) {
            int n2 = SBOX[i2];
            int n3 = WhirlpoolDigest.mulX(n2);
            int n4 = WhirlpoolDigest.mulX(n3);
            int n5 = n4 ^ n2;
            int n6 = WhirlpoolDigest.mulX(n4);
            int n7 = n6 ^ n2;
            WhirlpoolDigest.C0[i2] = WhirlpoolDigest.packIntoLong(n2, n2, n4, n2, n6, n5, n3, n7);
            WhirlpoolDigest.C1[i2] = WhirlpoolDigest.packIntoLong(n7, n2, n2, n4, n2, n6, n5, n3);
            WhirlpoolDigest.C2[i2] = WhirlpoolDigest.packIntoLong(n3, n7, n2, n2, n4, n2, n6, n5);
            WhirlpoolDigest.C3[i2] = WhirlpoolDigest.packIntoLong(n5, n3, n7, n2, n2, n4, n2, n6);
            WhirlpoolDigest.C4[i2] = WhirlpoolDigest.packIntoLong(n6, n5, n3, n7, n2, n2, n4, n2);
            WhirlpoolDigest.C5[i2] = WhirlpoolDigest.packIntoLong(n2, n6, n5, n3, n7, n2, n2, n4);
            WhirlpoolDigest.C6[i2] = WhirlpoolDigest.packIntoLong(n4, n2, n6, n5, n3, n7, n2, n2);
            WhirlpoolDigest.C7[i2] = WhirlpoolDigest.packIntoLong(n2, n4, n2, n6, n5, n3, n7, n2);
        }
    }
}

