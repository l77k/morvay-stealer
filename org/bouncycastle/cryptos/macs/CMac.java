/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.macs;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.ISO7816d4Padding;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.Pack;

public class CMac
implements Mac {
    private byte[] poly;
    private byte[] ZEROES;
    private byte[] mac;
    private byte[] buf;
    private int bufOff;
    private BlockCipher cipher;
    private int macSize;
    private byte[] Lu;
    private byte[] Lu2;

    public CMac(BlockCipher blockCipher) {
        this(blockCipher, blockCipher.getBlockSize() * 8);
    }

    public CMac(BlockCipher blockCipher, int n2) {
        if (n2 % 8 != 0) {
            throw new IllegalArgumentException("MAC size must be multiple of 8");
        }
        if (n2 > blockCipher.getBlockSize() * 8) {
            throw new IllegalArgumentException("MAC size must be less or equal to " + blockCipher.getBlockSize() * 8);
        }
        this.cipher = CBCBlockCipher.newInstance(blockCipher);
        this.macSize = n2 / 8;
        this.poly = CMac.lookupPoly(blockCipher.getBlockSize());
        this.mac = new byte[blockCipher.getBlockSize()];
        this.buf = new byte[blockCipher.getBlockSize()];
        this.ZEROES = new byte[blockCipher.getBlockSize()];
        this.bufOff = 0;
    }

    @Override
    public String getAlgorithmName() {
        return this.cipher.getAlgorithmName();
    }

    private static int shiftLeft(byte[] byArray, byte[] byArray2) {
        int n2 = byArray.length;
        int n3 = 0;
        while (--n2 >= 0) {
            int n4 = byArray[n2] & 0xFF;
            byArray2[n2] = (byte)(n4 << 1 | n3);
            n3 = n4 >>> 7 & 1;
        }
        return n3;
    }

    private byte[] doubleLu(byte[] byArray) {
        byte[] byArray2 = new byte[byArray.length];
        int n2 = CMac.shiftLeft(byArray, byArray2);
        int n3 = -n2 & 0xFF;
        int n4 = byArray.length - 3;
        byArray2[n4] = (byte)(byArray2[n4] ^ this.poly[1] & n3);
        int n5 = byArray.length - 2;
        byArray2[n5] = (byte)(byArray2[n5] ^ this.poly[2] & n3);
        int n6 = byArray.length - 1;
        byArray2[n6] = (byte)(byArray2[n6] ^ this.poly[3] & n3);
        return byArray2;
    }

    private static byte[] lookupPoly(int n2) {
        int n3;
        switch (n2 * 8) {
            case 64: {
                n3 = 27;
                break;
            }
            case 128: {
                n3 = 135;
                break;
            }
            case 160: {
                n3 = 45;
                break;
            }
            case 192: {
                n3 = 135;
                break;
            }
            case 224: {
                n3 = 777;
                break;
            }
            case 256: {
                n3 = 1061;
                break;
            }
            case 320: {
                n3 = 27;
                break;
            }
            case 384: {
                n3 = 4109;
                break;
            }
            case 448: {
                n3 = 2129;
                break;
            }
            case 512: {
                n3 = 293;
                break;
            }
            case 768: {
                n3 = 655377;
                break;
            }
            case 1024: {
                n3 = 524355;
                break;
            }
            case 2048: {
                n3 = 548865;
                break;
            }
            default: {
                throw new IllegalArgumentException("Unknown block size for CMAC: " + n2 * 8);
            }
        }
        return Pack.intToBigEndian(n3);
    }

    @Override
    public void init(CipherParameters cipherParameters) {
        this.validate(cipherParameters);
        this.cipher.init(true, cipherParameters);
        byte[] byArray = new byte[this.ZEROES.length];
        this.cipher.processBlock(this.ZEROES, 0, byArray, 0);
        this.Lu = this.doubleLu(byArray);
        this.Lu2 = this.doubleLu(this.Lu);
        this.reset();
    }

    void validate(CipherParameters cipherParameters) {
        if (cipherParameters != null && !(cipherParameters instanceof KeyParameter)) {
            throw new IllegalArgumentException("CMac mode only permits key to be set.");
        }
    }

    @Override
    public int getMacSize() {
        return this.macSize;
    }

    @Override
    public void update(byte by) {
        if (this.bufOff == this.buf.length) {
            this.cipher.processBlock(this.buf, 0, this.mac, 0);
            this.bufOff = 0;
        }
        this.buf[this.bufOff++] = by;
    }

    @Override
    public void update(byte[] byArray, int n2, int n3) {
        if (n3 < 0) {
            throw new IllegalArgumentException("Can't have a negative input length!");
        }
        int n4 = this.cipher.getBlockSize();
        int n5 = n4 - this.bufOff;
        if (n3 > n5) {
            System.arraycopy(byArray, n2, this.buf, this.bufOff, n5);
            this.cipher.processBlock(this.buf, 0, this.mac, 0);
            this.bufOff = 0;
            n3 -= n5;
            n2 += n5;
            while (n3 > n4) {
                this.cipher.processBlock(byArray, n2, this.mac, 0);
                n3 -= n4;
                n2 += n4;
            }
        }
        System.arraycopy(byArray, n2, this.buf, this.bufOff, n3);
        this.bufOff += n3;
    }

    @Override
    public int doFinal(byte[] byArray, int n2) {
        byte[] byArray2;
        int n3 = this.cipher.getBlockSize();
        if (this.bufOff == n3) {
            byArray2 = this.Lu;
        } else {
            new ISO7816d4Padding().addPadding(this.buf, this.bufOff);
            byArray2 = this.Lu2;
        }
        for (int i2 = 0; i2 < this.mac.length; ++i2) {
            int n4 = i2;
            this.buf[n4] = (byte)(this.buf[n4] ^ byArray2[i2]);
        }
        this.cipher.processBlock(this.buf, 0, this.mac, 0);
        System.arraycopy(this.mac, 0, byArray, n2, this.macSize);
        this.reset();
        return this.macSize;
    }

    @Override
    public void reset() {
        for (int i2 = 0; i2 < this.buf.length; ++i2) {
            this.buf[i2] = 0;
        }
        this.bufOff = 0;
        this.cipher.reset();
    }
}

