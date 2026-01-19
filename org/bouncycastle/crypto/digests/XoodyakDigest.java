/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.digests;

import java.io.ByteArrayOutputStream;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Integers;
import org.bouncycastle.util.Pack;

public class XoodyakDigest
implements Digest {
    private byte[] state;
    private int phase;
    private MODE mode;
    private int Rabsorb;
    private final int f_bPrime = 48;
    private final int Rhash = 16;
    private final int PhaseDown = 1;
    private final int PhaseUp = 2;
    private final int MAXROUNDS = 12;
    private final int TAGLEN = 16;
    private final int[] RC = new int[]{88, 56, 960, 208, 288, 20, 96, 44, 896, 240, 416, 18};
    private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();

    public XoodyakDigest() {
        this.state = new byte[48];
        this.reset();
    }

    @Override
    public String getAlgorithmName() {
        return "Xoodyak Hash";
    }

    @Override
    public int getDigestSize() {
        return 32;
    }

    @Override
    public void update(byte by) {
        this.buffer.write(by);
    }

    @Override
    public void update(byte[] byArray, int n2, int n3) {
        if (n2 + n3 > byArray.length) {
            throw new DataLengthException("input buffer too short");
        }
        this.buffer.write(byArray, n2, n3);
    }

    @Override
    public int doFinal(byte[] byArray, int n2) {
        int n3;
        if (32 + n2 > byArray.length) {
            throw new OutputLengthException("output buffer is too short");
        }
        byte[] byArray2 = this.buffer.toByteArray();
        int n4 = 0;
        int n5 = this.buffer.size();
        int n6 = 3;
        do {
            if (this.phase != 2) {
                this.Up(null, 0, 0, 0);
            }
            n3 = Math.min(n5, this.Rabsorb);
            this.Down(byArray2, n4, n3, n6);
            n6 = 0;
            n4 += n3;
        } while ((n5 -= n3) != 0);
        this.Up(byArray, n2, 16, 64);
        this.Down(null, 0, 0, 0);
        this.Up(byArray, n2 + 16, 16, 0);
        this.reset();
        return 32;
    }

    @Override
    public void reset() {
        Arrays.fill(this.state, (byte)0);
        this.phase = 2;
        this.mode = MODE.ModeHash;
        this.Rabsorb = 16;
        this.buffer.reset();
    }

    private void Up(byte[] byArray, int n2, int n3, int n4) {
        if (this.mode != MODE.ModeHash) {
            this.state[47] = (byte)(this.state[47] ^ n4);
        }
        int n5 = Pack.littleEndianToInt(this.state, 0);
        int n6 = Pack.littleEndianToInt(this.state, 4);
        int n7 = Pack.littleEndianToInt(this.state, 8);
        int n8 = Pack.littleEndianToInt(this.state, 12);
        int n9 = Pack.littleEndianToInt(this.state, 16);
        int n10 = Pack.littleEndianToInt(this.state, 20);
        int n11 = Pack.littleEndianToInt(this.state, 24);
        int n12 = Pack.littleEndianToInt(this.state, 28);
        int n13 = Pack.littleEndianToInt(this.state, 32);
        int n14 = Pack.littleEndianToInt(this.state, 36);
        int n15 = Pack.littleEndianToInt(this.state, 40);
        int n16 = Pack.littleEndianToInt(this.state, 44);
        for (int i2 = 0; i2 < 12; ++i2) {
            int n17 = n5 ^ n9 ^ n13;
            int n18 = n6 ^ n10 ^ n14;
            int n19 = n7 ^ n11 ^ n15;
            int n20 = n8 ^ n12 ^ n16;
            int n21 = Integers.rotateLeft(n20, 5) ^ Integers.rotateLeft(n20, 14);
            int n22 = Integers.rotateLeft(n17, 5) ^ Integers.rotateLeft(n17, 14);
            int n23 = Integers.rotateLeft(n18, 5) ^ Integers.rotateLeft(n18, 14);
            int n24 = Integers.rotateLeft(n19, 5) ^ Integers.rotateLeft(n19, 14);
            n5 ^= n21;
            n9 ^= n21;
            n13 ^= n21;
            n6 ^= n22;
            n10 ^= n22;
            n14 ^= n22;
            n7 ^= n23;
            n11 ^= n23;
            n15 ^= n23;
            n8 ^= n24;
            n12 ^= n24;
            n16 ^= n24;
            int n25 = n5;
            int n26 = n6;
            int n27 = n7;
            int n28 = n8;
            int n29 = n12;
            int n30 = n9;
            int n31 = n10;
            int n32 = n11;
            int n33 = Integers.rotateLeft(n13, 11);
            int n34 = Integers.rotateLeft(n14, 11);
            int n35 = Integers.rotateLeft(n15, 11);
            int n36 = Integers.rotateLeft(n16, 11);
            n5 = (n25 ^= this.RC[i2]) ^ ~n29 & n33;
            n6 = n26 ^ ~n30 & n34;
            n7 = n27 ^ ~n31 & n35;
            n8 = n28 ^ ~n32 & n36;
            n9 = n29 ^ ~n33 & n25;
            n10 = n30 ^ ~n34 & n26;
            n11 = n31 ^ ~n35 & n27;
            n12 = n32 ^ ~n36 & n28;
            n33 ^= ~n25 & n29;
            n34 ^= ~n26 & n30;
            n35 ^= ~n27 & n31;
            n36 ^= ~n28 & n32;
            n9 = Integers.rotateLeft(n9, 1);
            n10 = Integers.rotateLeft(n10, 1);
            n11 = Integers.rotateLeft(n11, 1);
            n12 = Integers.rotateLeft(n12, 1);
            n13 = Integers.rotateLeft(n35, 8);
            n14 = Integers.rotateLeft(n36, 8);
            n15 = Integers.rotateLeft(n33, 8);
            n16 = Integers.rotateLeft(n34, 8);
        }
        Pack.intToLittleEndian(n5, this.state, 0);
        Pack.intToLittleEndian(n6, this.state, 4);
        Pack.intToLittleEndian(n7, this.state, 8);
        Pack.intToLittleEndian(n8, this.state, 12);
        Pack.intToLittleEndian(n9, this.state, 16);
        Pack.intToLittleEndian(n10, this.state, 20);
        Pack.intToLittleEndian(n11, this.state, 24);
        Pack.intToLittleEndian(n12, this.state, 28);
        Pack.intToLittleEndian(n13, this.state, 32);
        Pack.intToLittleEndian(n14, this.state, 36);
        Pack.intToLittleEndian(n15, this.state, 40);
        Pack.intToLittleEndian(n16, this.state, 44);
        this.phase = 2;
        if (byArray != null) {
            System.arraycopy(this.state, 0, byArray, n2, n3);
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

