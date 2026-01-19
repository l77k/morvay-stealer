/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.digests;

import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.ExtendedDigest;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.engines.SparkleEngine;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Integers;
import org.bouncycastle.util.Pack;

public class SparkleDigest
implements ExtendedDigest {
    private static final int RATE_BYTES = 16;
    private static final int RATE_WORDS = 4;
    private String algorithmName;
    private final int[] state;
    private final byte[] m_buf = new byte[16];
    private final int DIGEST_BYTES;
    private final int SPARKLE_STEPS_SLIM;
    private final int SPARKLE_STEPS_BIG;
    private final int STATE_WORDS;
    private int m_bufPos = 0;

    public SparkleDigest(SparkleParameters sparkleParameters) {
        switch (sparkleParameters.ordinal()) {
            case 0: {
                this.algorithmName = "ESCH-256";
                this.DIGEST_BYTES = 32;
                this.SPARKLE_STEPS_SLIM = 7;
                this.SPARKLE_STEPS_BIG = 11;
                this.STATE_WORDS = 12;
                break;
            }
            case 1: {
                this.algorithmName = "ESCH-384";
                this.DIGEST_BYTES = 48;
                this.SPARKLE_STEPS_SLIM = 8;
                this.SPARKLE_STEPS_BIG = 12;
                this.STATE_WORDS = 16;
                break;
            }
            default: {
                throw new IllegalArgumentException("Invalid definition of SCHWAEMM instance");
            }
        }
        this.state = new int[this.STATE_WORDS];
    }

    @Override
    public String getAlgorithmName() {
        return this.algorithmName;
    }

    @Override
    public int getDigestSize() {
        return this.DIGEST_BYTES;
    }

    @Override
    public int getByteLength() {
        return 16;
    }

    @Override
    public void update(byte by) {
        if (this.m_bufPos == 16) {
            this.processBlock(this.m_buf, 0, this.SPARKLE_STEPS_SLIM);
            this.m_bufPos = 0;
        }
        this.m_buf[this.m_bufPos++] = by;
    }

    @Override
    public void update(byte[] byArray, int n2, int n3) {
        int n4;
        if (n2 > byArray.length - n3) {
            throw new DataLengthException(this.algorithmName + " input buffer too short");
        }
        if (n3 < 1) {
            return;
        }
        int n5 = 16 - this.m_bufPos;
        if (n3 <= n5) {
            System.arraycopy(byArray, n2, this.m_buf, this.m_bufPos, n3);
            this.m_bufPos += n3;
            return;
        }
        int n6 = 0;
        if (this.m_bufPos > 0) {
            System.arraycopy(byArray, n2, this.m_buf, this.m_bufPos, n5);
            this.processBlock(this.m_buf, 0, this.SPARKLE_STEPS_SLIM);
            n6 += n5;
        }
        while ((n4 = n3 - n6) > 16) {
            this.processBlock(byArray, n2 + n6, this.SPARKLE_STEPS_SLIM);
            n6 += 16;
        }
        System.arraycopy(byArray, n2 + n6, this.m_buf, 0, n4);
        this.m_bufPos = n4;
    }

    @Override
    public int doFinal(byte[] byArray, int n2) {
        if (n2 > byArray.length - this.DIGEST_BYTES) {
            throw new OutputLengthException(this.algorithmName + " input buffer too short");
        }
        if (this.m_bufPos < 16) {
            int n3 = (this.STATE_WORDS >> 1) - 1;
            this.state[n3] = this.state[n3] ^ 0x1000000;
            this.m_buf[this.m_bufPos] = -128;
            while (++this.m_bufPos < 16) {
                this.m_buf[this.m_bufPos] = 0;
            }
        } else {
            int n4 = (this.STATE_WORDS >> 1) - 1;
            this.state[n4] = this.state[n4] ^ 0x2000000;
        }
        this.processBlock(this.m_buf, 0, this.SPARKLE_STEPS_BIG);
        Pack.intToLittleEndian(this.state, 0, 4, byArray, n2);
        if (this.STATE_WORDS == 16) {
            SparkleEngine.sparkle_opt16(Friend.INSTANCE, this.state, this.SPARKLE_STEPS_SLIM);
            Pack.intToLittleEndian(this.state, 0, 4, byArray, n2 + 16);
            SparkleEngine.sparkle_opt16(Friend.INSTANCE, this.state, this.SPARKLE_STEPS_SLIM);
            Pack.intToLittleEndian(this.state, 0, 4, byArray, n2 + 32);
        } else {
            SparkleEngine.sparkle_opt12(Friend.INSTANCE, this.state, this.SPARKLE_STEPS_SLIM);
            Pack.intToLittleEndian(this.state, 0, 4, byArray, n2 + 16);
        }
        this.reset();
        return this.DIGEST_BYTES;
    }

    @Override
    public void reset() {
        Arrays.fill(this.state, 0);
        Arrays.fill(this.m_buf, (byte)0);
        this.m_bufPos = 0;
    }

    private void processBlock(byte[] byArray, int n2, int n3) {
        int n4 = Pack.littleEndianToInt(byArray, n2);
        int n5 = Pack.littleEndianToInt(byArray, n2 + 4);
        int n6 = Pack.littleEndianToInt(byArray, n2 + 8);
        int n7 = Pack.littleEndianToInt(byArray, n2 + 12);
        int n8 = SparkleDigest.ELL(n4 ^ n6);
        int n9 = SparkleDigest.ELL(n5 ^ n7);
        this.state[0] = this.state[0] ^ (n4 ^ n9);
        this.state[1] = this.state[1] ^ (n5 ^ n8);
        this.state[2] = this.state[2] ^ (n6 ^ n9);
        this.state[3] = this.state[3] ^ (n7 ^ n8);
        this.state[4] = this.state[4] ^ n9;
        this.state[5] = this.state[5] ^ n8;
        if (this.STATE_WORDS == 16) {
            this.state[6] = this.state[6] ^ n9;
            this.state[7] = this.state[7] ^ n8;
            SparkleEngine.sparkle_opt16(Friend.INSTANCE, this.state, n3);
        } else {
            SparkleEngine.sparkle_opt12(Friend.INSTANCE, this.state, n3);
        }
    }

    private static int ELL(int n2) {
        return Integers.rotateRight(n2, 16) ^ n2 & 0xFFFF;
    }

    public static class Friend {
        private static final Friend INSTANCE = new Friend();

        private Friend() {
        }
    }

    public static enum SparkleParameters {
        ESCH256,
        ESCH384;

    }
}

