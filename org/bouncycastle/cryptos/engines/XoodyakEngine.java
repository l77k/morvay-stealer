/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.engines.AEADBufferBaseEngine;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Integers;
import org.bouncycastle.util.Pack;

public class XoodyakEngine
extends AEADBufferBaseEngine {
    private byte[] state;
    private int phase;
    private MODE mode;
    private final int f_bPrime_1 = 47;
    private byte[] K;
    private byte[] iv;
    private final int PhaseUp = 2;
    private final int[] RC = new int[]{88, 56, 960, 208, 288, 20, 96, 44, 896, 240, 416, 18};
    private boolean encrypted;
    private byte aadcd;

    public XoodyakEngine() {
        this.algorithmName = "Xoodyak AEAD";
        this.KEY_SIZE = 16;
        this.IV_SIZE = 16;
        this.MAC_SIZE = 16;
        this.BlockSize = 24;
        this.AADBufferSize = 44;
        this.m_aad = new byte[this.AADBufferSize];
    }

    @Override
    public void init(byte[] byArray, byte[] byArray2) throws IllegalArgumentException {
        this.K = byArray;
        this.iv = byArray2;
        this.state = new byte[48];
        this.mac = new byte[this.MAC_SIZE];
        this.m_buf = new byte[this.BlockSize + (this.forEncryption ? 0 : this.MAC_SIZE)];
        this.initialised = true;
        this.m_state = this.forEncryption ? AEADBufferBaseEngine.State.EncInit : AEADBufferBaseEngine.State.DecInit;
        this.reset();
    }

    @Override
    protected void processBufferAAD(byte[] byArray, int n2) {
        this.AbsorbAny(byArray, n2, this.AADBufferSize, this.aadcd);
        this.aadcd = 0;
    }

    @Override
    protected void processFinalAAD() {
        if (this.mode != MODE.ModeKeyed) {
            throw new IllegalArgumentException("Xoodyak has not been initialised");
        }
        if (!this.aadFinished) {
            this.AbsorbAny(this.m_aad, 0, this.m_aadPos, this.aadcd);
            this.aadFinished = true;
            this.m_aadPos = 0;
        }
    }

    @Override
    protected void processBuffer(byte[] byArray, int n2, byte[] byArray2, int n3) {
        this.processFinalAAD();
        this.encrypt(byArray, n2, this.BlockSize, byArray2, n3);
    }

    private void encrypt(byte[] byArray, int n2, int n3, byte[] byArray2, int n4) {
        int n5;
        int n6;
        byte[] byArray3 = new byte[this.BlockSize];
        int n7 = n6 = this.encrypted ? 0 : 128;
        for (int i2 = n3; i2 != 0 || !this.encrypted; i2 -= n5) {
            n5 = Math.min(i2, this.BlockSize);
            if (this.forEncryption) {
                System.arraycopy(byArray, n2, byArray3, 0, n5);
            }
            this.Up(null, 0, n6);
            for (int i3 = 0; i3 < n5; ++i3) {
                byArray2[n4 + i3] = (byte)(byArray[n2++] ^ this.state[i3]);
            }
            if (this.forEncryption) {
                this.Down(byArray3, 0, n5, 0);
            } else {
                this.Down(byArray2, n4, n5, 0);
            }
            n6 = 0;
            n4 += n5;
            this.encrypted = true;
        }
    }

    @Override
    protected void processFinalBlock(byte[] byArray, int n2) {
        this.processFinalAAD();
        if (this.forEncryption) {
            Arrays.fill(this.m_buf, this.m_bufPos, this.BlockSize, (byte)0);
        }
        this.encrypt(this.m_buf, 0, this.m_bufPos, byArray, n2);
        this.mac = new byte[this.MAC_SIZE];
        this.Up(this.mac, this.MAC_SIZE, 64);
    }

    @Override
    protected void reset(boolean bl) {
        if (!this.initialised) {
            throw new IllegalArgumentException("Need call init function before encryption/decryption");
        }
        Arrays.fill(this.state, (byte)0);
        this.aadFinished = false;
        this.encrypted = false;
        this.phase = 2;
        Arrays.fill(this.m_buf, (byte)0);
        Arrays.fill(this.m_aad, (byte)0);
        this.m_bufPos = 0;
        this.m_aadPos = 0;
        this.aadcd = (byte)3;
        int n2 = this.K.length;
        int n3 = this.iv.length;
        byte[] byArray = new byte[this.AADBufferSize];
        this.mode = MODE.ModeKeyed;
        System.arraycopy(this.K, 0, byArray, 0, n2);
        System.arraycopy(this.iv, 0, byArray, n2, n3);
        byArray[n2 + n3] = (byte)n3;
        this.AbsorbAny(byArray, 0, n2 + n3 + 1, 2);
        super.reset(bl);
    }

    private void AbsorbAny(byte[] byArray, int n2, int n3, int n4) {
        int n5;
        do {
            if (this.phase != 2) {
                this.Up(null, 0, 0);
            }
            n5 = Math.min(n3, this.AADBufferSize);
            this.Down(byArray, n2, n5, n4);
            n4 = 0;
            n2 += n5;
        } while ((n3 -= n5) != 0);
    }

    private void Up(byte[] byArray, int n2, int n3) {
        if (this.mode != MODE.ModeHash) {
            this.state[47] = (byte)(this.state[47] ^ n3);
        }
        int n4 = Pack.littleEndianToInt(this.state, 0);
        int n5 = Pack.littleEndianToInt(this.state, 4);
        int n6 = Pack.littleEndianToInt(this.state, 8);
        int n7 = Pack.littleEndianToInt(this.state, 12);
        int n8 = Pack.littleEndianToInt(this.state, 16);
        int n9 = Pack.littleEndianToInt(this.state, 20);
        int n10 = Pack.littleEndianToInt(this.state, 24);
        int n11 = Pack.littleEndianToInt(this.state, 28);
        int n12 = Pack.littleEndianToInt(this.state, 32);
        int n13 = Pack.littleEndianToInt(this.state, 36);
        int n14 = Pack.littleEndianToInt(this.state, 40);
        int n15 = Pack.littleEndianToInt(this.state, 44);
        for (int i2 = 0; i2 < 12; ++i2) {
            int n16 = n4 ^ n8 ^ n12;
            int n17 = n5 ^ n9 ^ n13;
            int n18 = n6 ^ n10 ^ n14;
            int n19 = n7 ^ n11 ^ n15;
            int n20 = Integers.rotateLeft(n19, 5) ^ Integers.rotateLeft(n19, 14);
            int n21 = Integers.rotateLeft(n16, 5) ^ Integers.rotateLeft(n16, 14);
            int n22 = Integers.rotateLeft(n17, 5) ^ Integers.rotateLeft(n17, 14);
            int n23 = Integers.rotateLeft(n18, 5) ^ Integers.rotateLeft(n18, 14);
            n4 ^= n20;
            n8 ^= n20;
            n12 ^= n20;
            n5 ^= n21;
            n9 ^= n21;
            n13 ^= n21;
            n6 ^= n22;
            n10 ^= n22;
            n14 ^= n22;
            n7 ^= n23;
            n11 ^= n23;
            n15 ^= n23;
            int n24 = n4;
            int n25 = n5;
            int n26 = n6;
            int n27 = n7;
            int n28 = n11;
            int n29 = n8;
            int n30 = n9;
            int n31 = n10;
            int n32 = Integers.rotateLeft(n12, 11);
            int n33 = Integers.rotateLeft(n13, 11);
            int n34 = Integers.rotateLeft(n14, 11);
            int n35 = Integers.rotateLeft(n15, 11);
            n4 = (n24 ^= this.RC[i2]) ^ ~n28 & n32;
            n5 = n25 ^ ~n29 & n33;
            n6 = n26 ^ ~n30 & n34;
            n7 = n27 ^ ~n31 & n35;
            n8 = n28 ^ ~n32 & n24;
            n9 = n29 ^ ~n33 & n25;
            n10 = n30 ^ ~n34 & n26;
            n11 = n31 ^ ~n35 & n27;
            n32 ^= ~n24 & n28;
            n33 ^= ~n25 & n29;
            n34 ^= ~n26 & n30;
            n35 ^= ~n27 & n31;
            n8 = Integers.rotateLeft(n8, 1);
            n9 = Integers.rotateLeft(n9, 1);
            n10 = Integers.rotateLeft(n10, 1);
            n11 = Integers.rotateLeft(n11, 1);
            n12 = Integers.rotateLeft(n34, 8);
            n13 = Integers.rotateLeft(n35, 8);
            n14 = Integers.rotateLeft(n32, 8);
            n15 = Integers.rotateLeft(n33, 8);
        }
        Pack.intToLittleEndian(n4, this.state, 0);
        Pack.intToLittleEndian(n5, this.state, 4);
        Pack.intToLittleEndian(n6, this.state, 8);
        Pack.intToLittleEndian(n7, this.state, 12);
        Pack.intToLittleEndian(n8, this.state, 16);
        Pack.intToLittleEndian(n9, this.state, 20);
        Pack.intToLittleEndian(n10, this.state, 24);
        Pack.intToLittleEndian(n11, this.state, 28);
        Pack.intToLittleEndian(n12, this.state, 32);
        Pack.intToLittleEndian(n13, this.state, 36);
        Pack.intToLittleEndian(n14, this.state, 40);
        Pack.intToLittleEndian(n15, this.state, 44);
        this.phase = 2;
        if (byArray != null) {
            System.arraycopy(this.state, 0, byArray, 0, n2);
        }
    }

    void Down(byte[] byArray, int n2, int n3, int n4) {
        int n5 = 0;
        while (n5 < n3) {
            int n6 = n5++;
            this.state[n6] = (byte)(this.state[n6] ^ byArray[n2++]);
        }
        int n7 = n3;
        this.state[n7] = (byte)(this.state[n7] ^ 1);
        this.state[47] = (byte)(this.state[47] ^ (this.mode == MODE.ModeHash ? n4 & 1 : n4));
        this.phase = 1;
    }

    static enum MODE {
        ModeHash,
        ModeKeyed;

    }
}

