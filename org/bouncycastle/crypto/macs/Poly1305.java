/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.macs;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.util.Pack;

public class Poly1305
implements Mac {
    private static final int BLOCK_SIZE = 16;
    private final BlockCipher cipher;
    private final byte[] singleByte = new byte[1];
    private int r0;
    private int r1;
    private int r2;
    private int r3;
    private int r4;
    private int s1;
    private int s2;
    private int s3;
    private int s4;
    private int k0;
    private int k1;
    private int k2;
    private int k3;
    private final byte[] currentBlock = new byte[16];
    private int currentBlockOffset = 0;
    private int h0;
    private int h1;
    private int h2;
    private int h3;
    private int h4;

    public Poly1305() {
        this.cipher = null;
    }

    public Poly1305(BlockCipher blockCipher) {
        if (blockCipher.getBlockSize() != 16) {
            throw new IllegalArgumentException("Poly1305 requires a 128 bit block cipher.");
        }
        this.cipher = blockCipher;
    }

    @Override
    public void init(CipherParameters cipherParameters) throws IllegalArgumentException {
        CipherParameters cipherParameters2;
        byte[] byArray = null;
        if (this.cipher != null) {
            if (!(cipherParameters instanceof ParametersWithIV)) {
                throw new IllegalArgumentException("Poly1305 requires an IV when used with a block cipher.");
            }
            cipherParameters2 = (ParametersWithIV)cipherParameters;
            byArray = ((ParametersWithIV)cipherParameters2).getIV();
            cipherParameters = ((ParametersWithIV)cipherParameters2).getParameters();
        }
        if (!(cipherParameters instanceof KeyParameter)) {
            throw new IllegalArgumentException("Poly1305 requires a key.");
        }
        cipherParameters2 = (KeyParameter)cipherParameters;
        this.setKey(((KeyParameter)cipherParameters2).getKey(), byArray);
        this.reset();
    }

    private void setKey(byte[] byArray, byte[] byArray2) {
        int n2;
        byte[] byArray3;
        if (byArray.length != 32) {
            throw new IllegalArgumentException("Poly1305 key must be 256 bits.");
        }
        if (this.cipher != null && (byArray2 == null || byArray2.length != 16)) {
            throw new IllegalArgumentException("Poly1305 requires a 128 bit IV.");
        }
        int n3 = Pack.littleEndianToInt(byArray, 0);
        int n4 = Pack.littleEndianToInt(byArray, 4);
        int n5 = Pack.littleEndianToInt(byArray, 8);
        int n6 = Pack.littleEndianToInt(byArray, 12);
        this.r0 = n3 & 0x3FFFFFF;
        this.r1 = (n3 >>> 26 | n4 << 6) & 0x3FFFF03;
        this.r2 = (n4 >>> 20 | n5 << 12) & 0x3FFC0FF;
        this.r3 = (n5 >>> 14 | n6 << 18) & 0x3F03FFF;
        this.r4 = n6 >>> 8 & 0xFFFFF;
        this.s1 = this.r1 * 5;
        this.s2 = this.r2 * 5;
        this.s3 = this.r3 * 5;
        this.s4 = this.r4 * 5;
        if (this.cipher == null) {
            byArray3 = byArray;
            n2 = 16;
        } else {
            byArray3 = new byte[16];
            n2 = 0;
            this.cipher.init(true, new KeyParameter(byArray, 16, 16));
            this.cipher.processBlock(byArray2, 0, byArray3, 0);
        }
        this.k0 = Pack.littleEndianToInt(byArray3, n2 + 0);
        this.k1 = Pack.littleEndianToInt(byArray3, n2 + 4);
        this.k2 = Pack.littleEndianToInt(byArray3, n2 + 8);
        this.k3 = Pack.littleEndianToInt(byArray3, n2 + 12);
    }

    @Override
    public String getAlgorithmName() {
        return this.cipher == null ? "Poly1305" : "Poly1305-" + this.cipher.getAlgorithmName();
    }

    @Override
    public int getMacSize() {
        return 16;
    }

    @Override
    public void update(byte by) throws IllegalStateException {
        this.singleByte[0] = by;
        this.update(this.singleByte, 0, 1);
    }

    @Override
    public void update(byte[] byArray, int n2, int n3) throws DataLengthException, IllegalStateException {
        int n4 = 0;
        while (n3 > n4) {
            if (this.currentBlockOffset == 16) {
                this.processBlock();
                this.currentBlockOffset = 0;
            }
            int n5 = Math.min(n3 - n4, 16 - this.currentBlockOffset);
            System.arraycopy(byArray, n4 + n2, this.currentBlock, this.currentBlockOffset, n5);
            n4 += n5;
            this.currentBlockOffset += n5;
        }
    }

    private void processBlock() {
        if (this.currentBlockOffset < 16) {
            this.currentBlock[this.currentBlockOffset] = 1;
            for (int i2 = this.currentBlockOffset + 1; i2 < 16; ++i2) {
                this.currentBlock[i2] = 0;
            }
        }
        long l2 = 0xFFFFFFFFL & (long)Pack.littleEndianToInt(this.currentBlock, 0);
        long l3 = 0xFFFFFFFFL & (long)Pack.littleEndianToInt(this.currentBlock, 4);
        long l4 = 0xFFFFFFFFL & (long)Pack.littleEndianToInt(this.currentBlock, 8);
        long l5 = 0xFFFFFFFFL & (long)Pack.littleEndianToInt(this.currentBlock, 12);
        this.h0 = (int)((long)this.h0 + (l2 & 0x3FFFFFFL));
        this.h1 = (int)((long)this.h1 + ((l3 << 32 | l2) >>> 26 & 0x3FFFFFFL));
        this.h2 = (int)((long)this.h2 + ((l4 << 32 | l3) >>> 20 & 0x3FFFFFFL));
        this.h3 = (int)((long)this.h3 + ((l5 << 32 | l4) >>> 14 & 0x3FFFFFFL));
        this.h4 = (int)((long)this.h4 + (l5 >>> 8));
        if (this.currentBlockOffset == 16) {
            this.h4 += 0x1000000;
        }
        long l6 = Poly1305.mul32x32_64(this.h0, this.r0) + Poly1305.mul32x32_64(this.h1, this.s4) + Poly1305.mul32x32_64(this.h2, this.s3) + Poly1305.mul32x32_64(this.h3, this.s2) + Poly1305.mul32x32_64(this.h4, this.s1);
        long l7 = Poly1305.mul32x32_64(this.h0, this.r1) + Poly1305.mul32x32_64(this.h1, this.r0) + Poly1305.mul32x32_64(this.h2, this.s4) + Poly1305.mul32x32_64(this.h3, this.s3) + Poly1305.mul32x32_64(this.h4, this.s2);
        long l8 = Poly1305.mul32x32_64(this.h0, this.r2) + Poly1305.mul32x32_64(this.h1, this.r1) + Poly1305.mul32x32_64(this.h2, this.r0) + Poly1305.mul32x32_64(this.h3, this.s4) + Poly1305.mul32x32_64(this.h4, this.s3);
        long l9 = Poly1305.mul32x32_64(this.h0, this.r3) + Poly1305.mul32x32_64(this.h1, this.r2) + Poly1305.mul32x32_64(this.h2, this.r1) + Poly1305.mul32x32_64(this.h3, this.r0) + Poly1305.mul32x32_64(this.h4, this.s4);
        long l10 = Poly1305.mul32x32_64(this.h0, this.r4) + Poly1305.mul32x32_64(this.h1, this.r3) + Poly1305.mul32x32_64(this.h2, this.r2) + Poly1305.mul32x32_64(this.h3, this.r1) + Poly1305.mul32x32_64(this.h4, this.r0);
        this.h0 = (int)l6 & 0x3FFFFFF;
        this.h1 = (int)(l7 += l6 >>> 26) & 0x3FFFFFF;
        this.h2 = (int)(l8 += l7 >>> 26) & 0x3FFFFFF;
        this.h3 = (int)(l9 += l8 >>> 26) & 0x3FFFFFF;
        this.h4 = (int)(l10 += l9 >>> 26) & 0x3FFFFFF;
        this.h0 += (int)(l10 >>> 26) * 5;
        this.h1 += this.h0 >>> 26;
        this.h0 &= 0x3FFFFFF;
    }

    @Override
    public int doFinal(byte[] byArray, int n2) throws DataLengthException, IllegalStateException {
        if (n2 + 16 > byArray.length) {
            throw new OutputLengthException("Output buffer is too short.");
        }
        if (this.currentBlockOffset > 0) {
            this.processBlock();
        }
        this.h1 += this.h0 >>> 26;
        this.h0 &= 0x3FFFFFF;
        this.h2 += this.h1 >>> 26;
        this.h1 &= 0x3FFFFFF;
        this.h3 += this.h2 >>> 26;
        this.h2 &= 0x3FFFFFF;
        this.h4 += this.h3 >>> 26;
        this.h3 &= 0x3FFFFFF;
        this.h0 += (this.h4 >>> 26) * 5;
        this.h4 &= 0x3FFFFFF;
        this.h1 += this.h0 >>> 26;
        this.h0 &= 0x3FFFFFF;
        int n3 = this.h0 + 5;
        int n4 = n3 >>> 26;
        n3 &= 0x3FFFFFF;
        int n5 = this.h1 + n4;
        n4 = n5 >>> 26;
        n5 &= 0x3FFFFFF;
        int n6 = this.h2 + n4;
        n4 = n6 >>> 26;
        n6 &= 0x3FFFFFF;
        int n7 = this.h3 + n4;
        n4 = n7 >>> 26;
        n7 &= 0x3FFFFFF;
        int n8 = this.h4 + n4 - 0x4000000;
        n4 = (n8 >>> 31) - 1;
        int n9 = ~n4;
        this.h0 = this.h0 & n9 | n3 & n4;
        this.h1 = this.h1 & n9 | n5 & n4;
        this.h2 = this.h2 & n9 | n6 & n4;
        this.h3 = this.h3 & n9 | n7 & n4;
        this.h4 = this.h4 & n9 | n8 & n4;
        long l2 = ((long)(this.h0 | this.h1 << 26) & 0xFFFFFFFFL) + (0xFFFFFFFFL & (long)this.k0);
        long l3 = ((long)(this.h1 >>> 6 | this.h2 << 20) & 0xFFFFFFFFL) + (0xFFFFFFFFL & (long)this.k1);
        long l4 = ((long)(this.h2 >>> 12 | this.h3 << 14) & 0xFFFFFFFFL) + (0xFFFFFFFFL & (long)this.k2);
        long l5 = ((long)(this.h3 >>> 18 | this.h4 << 8) & 0xFFFFFFFFL) + (0xFFFFFFFFL & (long)this.k3);
        Pack.intToLittleEndian((int)l2, byArray, n2);
        Pack.intToLittleEndian((int)(l3 += l2 >>> 32), byArray, n2 + 4);
        Pack.intToLittleEndian((int)(l4 += l3 >>> 32), byArray, n2 + 8);
        Pack.intToLittleEndian((int)(l5 += l4 >>> 32), byArray, n2 + 12);
        this.reset();
        return 16;
    }

    @Override
    public void reset() {
        this.currentBlockOffset = 0;
        this.h4 = 0;
        this.h3 = 0;
        this.h2 = 0;
        this.h1 = 0;
        this.h0 = 0;
    }

    private static final long mul32x32_64(int n2, int n3) {
        return ((long)n2 & 0xFFFFFFFFL) * (long)n3;
    }
}

