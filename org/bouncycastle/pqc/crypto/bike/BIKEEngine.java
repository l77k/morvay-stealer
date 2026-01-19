/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.bike;

import java.security.SecureRandom;
import org.bouncycastle.crypto.digests.SHA3Digest;
import org.bouncycastle.crypto.digests.SHAKEDigest;
import org.bouncycastle.pqc.crypto.bike.BIKERing;
import org.bouncycastle.pqc.crypto.bike.BIKEUtils;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Bytes;

class BIKEEngine {
    private int r;
    private int w;
    private int hw;
    private int t;
    private int nbIter;
    private int tau;
    private final BIKERing bikeRing;
    private int L_BYTE;
    private int R_BYTE;
    private int R2_BYTE;

    public BIKEEngine(int n2, int n3, int n4, int n5, int n6, int n7) {
        this.r = n2;
        this.w = n3;
        this.t = n4;
        this.nbIter = n6;
        this.tau = n7;
        this.hw = this.w / 2;
        this.L_BYTE = n5 / 8;
        this.R_BYTE = n2 + 7 >>> 3;
        this.R2_BYTE = 2 * n2 + 7 >>> 3;
        this.bikeRing = new BIKERing(n2);
    }

    public int getSessionKeySize() {
        return this.L_BYTE;
    }

    private byte[] functionH(byte[] byArray) {
        byte[] byArray2 = new byte[2 * this.R_BYTE];
        SHAKEDigest sHAKEDigest = new SHAKEDigest(256);
        sHAKEDigest.update(byArray, 0, byArray.length);
        BIKEUtils.generateRandomByteArray(byArray2, 2 * this.r, this.t, sHAKEDigest);
        return byArray2;
    }

    private void functionL(byte[] byArray, byte[] byArray2, byte[] byArray3) {
        byte[] byArray4 = new byte[48];
        SHA3Digest sHA3Digest = new SHA3Digest(384);
        sHA3Digest.update(byArray, 0, byArray.length);
        sHA3Digest.update(byArray2, 0, byArray2.length);
        sHA3Digest.doFinal(byArray4, 0);
        System.arraycopy(byArray4, 0, byArray3, 0, this.L_BYTE);
    }

    private void functionK(byte[] byArray, byte[] byArray2, byte[] byArray3, byte[] byArray4) {
        byte[] byArray5 = new byte[48];
        SHA3Digest sHA3Digest = new SHA3Digest(384);
        sHA3Digest.update(byArray, 0, byArray.length);
        sHA3Digest.update(byArray2, 0, byArray2.length);
        sHA3Digest.update(byArray3, 0, byArray3.length);
        sHA3Digest.doFinal(byArray5, 0);
        System.arraycopy(byArray5, 0, byArray4, 0, this.L_BYTE);
    }

    public void genKeyPair(byte[] byArray, byte[] byArray2, byte[] byArray3, byte[] byArray4, SecureRandom secureRandom) {
        byte[] byArray5 = new byte[64];
        secureRandom.nextBytes(byArray5);
        SHAKEDigest sHAKEDigest = new SHAKEDigest(256);
        sHAKEDigest.update(byArray5, 0, this.L_BYTE);
        BIKEUtils.generateRandomByteArray(byArray, this.r, this.hw, sHAKEDigest);
        BIKEUtils.generateRandomByteArray(byArray2, this.r, this.hw, sHAKEDigest);
        long[] lArray = this.bikeRing.create();
        long[] lArray2 = this.bikeRing.create();
        this.bikeRing.decodeBytes(byArray, lArray);
        this.bikeRing.decodeBytes(byArray2, lArray2);
        long[] lArray3 = this.bikeRing.create();
        this.bikeRing.inv(lArray, lArray3);
        this.bikeRing.multiply(lArray3, lArray2, lArray3);
        this.bikeRing.encodeBytes(lArray3, byArray4);
        System.arraycopy(byArray5, this.L_BYTE, byArray3, 0, byArray3.length);
    }

    public void encaps(byte[] byArray, byte[] byArray2, byte[] byArray3, byte[] byArray4, SecureRandom secureRandom) {
        byte[] byArray5 = new byte[this.L_BYTE];
        secureRandom.nextBytes(byArray5);
        byte[] byArray6 = this.functionH(byArray5);
        byte[] byArray7 = new byte[this.R_BYTE];
        byte[] byArray8 = new byte[this.R_BYTE];
        this.splitEBytes(byArray6, byArray7, byArray8);
        long[] lArray = this.bikeRing.create();
        long[] lArray2 = this.bikeRing.create();
        this.bikeRing.decodeBytes(byArray7, lArray);
        this.bikeRing.decodeBytes(byArray8, lArray2);
        long[] lArray3 = this.bikeRing.create();
        this.bikeRing.decodeBytes(byArray4, lArray3);
        this.bikeRing.multiply(lArray3, lArray2, lArray3);
        this.bikeRing.add(lArray3, lArray, lArray3);
        this.bikeRing.encodeBytes(lArray3, byArray);
        this.functionL(byArray7, byArray8, byArray2);
        Bytes.xorTo(this.L_BYTE, byArray5, byArray2);
        this.functionK(byArray5, byArray, byArray2, byArray3);
    }

    public void decaps(byte[] byArray, byte[] byArray2, byte[] byArray3, byte[] byArray4, byte[] byArray5, byte[] byArray6) {
        int[] nArray = new int[this.hw];
        int[] nArray2 = new int[this.hw];
        this.convertToCompact(nArray, byArray2);
        this.convertToCompact(nArray2, byArray3);
        byte[] byArray7 = this.computeSyndrome(byArray5, byArray2);
        byte[] byArray8 = this.BGFDecoder(byArray7, nArray, nArray2);
        byte[] byArray9 = new byte[2 * this.R_BYTE];
        BIKEUtils.fromBitArrayToByteArray(byArray9, byArray8, 0, 2 * this.r);
        byte[] byArray10 = new byte[this.R_BYTE];
        byte[] byArray11 = new byte[this.R_BYTE];
        this.splitEBytes(byArray9, byArray10, byArray11);
        byte[] byArray12 = new byte[this.L_BYTE];
        this.functionL(byArray10, byArray11, byArray12);
        Bytes.xorTo(this.L_BYTE, byArray6, byArray12);
        byte[] byArray13 = this.functionH(byArray12);
        if (Arrays.areEqual(byArray9, 0, this.R2_BYTE, byArray13, 0, this.R2_BYTE)) {
            this.functionK(byArray12, byArray5, byArray6, byArray);
        } else {
            this.functionK(byArray4, byArray5, byArray6, byArray);
        }
    }

    private byte[] computeSyndrome(byte[] byArray, byte[] byArray2) {
        long[] lArray = this.bikeRing.create();
        long[] lArray2 = this.bikeRing.create();
        this.bikeRing.decodeBytes(byArray, lArray);
        this.bikeRing.decodeBytes(byArray2, lArray2);
        this.bikeRing.multiply(lArray, lArray2, lArray);
        return this.bikeRing.encodeBitsTransposed(lArray);
    }

    private byte[] BGFDecoder(byte[] byArray, int[] nArray, int[] nArray2) {
        byte[] byArray2 = new byte[2 * this.r];
        int[] nArray3 = this.getColumnFromCompactVersion(nArray);
        int[] nArray4 = this.getColumnFromCompactVersion(nArray2);
        byte[] byArray3 = new byte[2 * this.r];
        byte[] byArray4 = new byte[this.r];
        byte[] byArray5 = new byte[2 * this.r];
        int n2 = this.threshold(BIKEUtils.getHammingWeight(byArray), this.r);
        this.BFIter(byArray, byArray2, n2, nArray, nArray2, nArray3, nArray4, byArray3, byArray5, byArray4);
        this.BFMaskedIter(byArray, byArray2, byArray3, (this.hw + 1) / 2 + 1, nArray, nArray2, nArray3, nArray4);
        this.BFMaskedIter(byArray, byArray2, byArray5, (this.hw + 1) / 2 + 1, nArray, nArray2, nArray3, nArray4);
        for (int i2 = 1; i2 < this.nbIter; ++i2) {
            Arrays.fill(byArray3, (byte)0);
            n2 = this.threshold(BIKEUtils.getHammingWeight(byArray), this.r);
            this.BFIter2(byArray, byArray2, n2, nArray, nArray2, nArray3, nArray4, byArray4);
        }
        if (BIKEUtils.getHammingWeight(byArray) == 0) {
            return byArray2;
        }
        return null;
    }

    private void BFIter(byte[] byArray, byte[] byArray2, int n2, int[] nArray, int[] nArray2, int[] nArray3, int[] nArray4, byte[] byArray3, byte[] byArray4, byte[] byArray5) {
        int n3;
        this.ctrAll(nArray3, byArray, byArray5);
        int n4 = byArray5[0] & 0xFF;
        int n5 = (n4 - n2 >> 31) + 1;
        int n6 = (n4 - (n2 - this.tau) >> 31) + 1;
        byArray2[0] = (byte)(byArray2[0] ^ (byte)n5);
        byArray3[0] = (byte)n5;
        byArray4[0] = (byte)n6;
        for (n4 = 1; n4 < this.r; ++n4) {
            n5 = byArray5[n4] & 0xFF;
            n6 = (n5 - n2 >> 31) + 1;
            n3 = (n5 - (n2 - this.tau) >> 31) + 1;
            int n7 = this.r - n4;
            byArray2[n7] = (byte)(byArray2[n7] ^ (byte)n6);
            byArray3[n4] = (byte)n6;
            byArray4[n4] = (byte)n3;
        }
        this.ctrAll(nArray4, byArray, byArray5);
        n4 = byArray5[0] & 0xFF;
        n5 = (n4 - n2 >> 31) + 1;
        n6 = (n4 - (n2 - this.tau) >> 31) + 1;
        int n8 = this.r;
        byArray2[n8] = (byte)(byArray2[n8] ^ (byte)n5);
        byArray3[this.r] = (byte)n5;
        byArray4[this.r] = (byte)n6;
        for (n4 = 1; n4 < this.r; ++n4) {
            n5 = byArray5[n4] & 0xFF;
            n6 = (n5 - n2 >> 31) + 1;
            n3 = (n5 - (n2 - this.tau) >> 31) + 1;
            int n9 = this.r + this.r - n4;
            byArray2[n9] = (byte)(byArray2[n9] ^ (byte)n6);
            byArray3[this.r + n4] = (byte)n6;
            byArray4[this.r + n4] = (byte)n3;
        }
        for (n4 = 0; n4 < 2 * this.r; ++n4) {
            this.recomputeSyndrome(byArray, n4, nArray, nArray2, byArray3[n4] != 0);
        }
    }

    private void BFIter2(byte[] byArray, byte[] byArray2, int n2, int[] nArray, int[] nArray2, int[] nArray3, int[] nArray4, byte[] byArray3) {
        int n3;
        int[] nArray5 = new int[2 * this.r];
        this.ctrAll(nArray3, byArray, byArray3);
        int n4 = byArray3[0] & 0xFF;
        int n5 = (n4 - n2 >> 31) + 1;
        byArray2[0] = (byte)(byArray2[0] ^ (byte)n5);
        nArray5[0] = n5;
        for (n4 = 1; n4 < this.r; ++n4) {
            n5 = byArray3[n4] & 0xFF;
            n3 = (n5 - n2 >> 31) + 1;
            int n6 = this.r - n4;
            byArray2[n6] = (byte)(byArray2[n6] ^ (byte)n3);
            nArray5[n4] = n3;
        }
        this.ctrAll(nArray4, byArray, byArray3);
        n4 = byArray3[0] & 0xFF;
        n5 = (n4 - n2 >> 31) + 1;
        int n7 = this.r;
        byArray2[n7] = (byte)(byArray2[n7] ^ (byte)n5);
        nArray5[this.r] = n5;
        for (n4 = 1; n4 < this.r; ++n4) {
            n5 = byArray3[n4] & 0xFF;
            n3 = (n5 - n2 >> 31) + 1;
            int n8 = this.r + this.r - n4;
            byArray2[n8] = (byte)(byArray2[n8] ^ (byte)n3);
            nArray5[this.r + n4] = n3;
        }
        for (n4 = 0; n4 < 2 * this.r; ++n4) {
            this.recomputeSyndrome(byArray, n4, nArray, nArray2, nArray5[n4] == 1);
        }
    }

    private void BFMaskedIter(byte[] byArray, byte[] byArray2, byte[] byArray3, int n2, int[] nArray, int[] nArray2, int[] nArray3, int[] nArray4) {
        boolean bl;
        int n3;
        int[] nArray5 = new int[2 * this.r];
        for (n3 = 0; n3 < this.r; ++n3) {
            if (byArray3[n3] != 1) continue;
            bl = this.ctr(nArray3, byArray, n3) >= n2;
            this.updateNewErrorIndex(byArray2, n3, bl);
            nArray5[n3] = bl ? 1 : 0;
        }
        for (n3 = 0; n3 < this.r; ++n3) {
            if (byArray3[this.r + n3] != 1) continue;
            bl = this.ctr(nArray4, byArray, n3) >= n2;
            this.updateNewErrorIndex(byArray2, this.r + n3, bl);
            nArray5[this.r + n3] = bl ? 1 : 0;
        }
        for (n3 = 0; n3 < 2 * this.r; ++n3) {
            this.recomputeSyndrome(byArray, n3, nArray, nArray2, nArray5[n3] == 1);
        }
    }

    private int threshold(int n2, int n3) {
        switch (n3) {
            case 12323: {
                return BIKEEngine.thresholdFromParameters(n2, 0.0069722, 13.53, 36);
            }
            case 24659: {
                return BIKEEngine.thresholdFromParameters(n2, 0.005265, 15.2588, 52);
            }
            case 40973: {
                return BIKEEngine.thresholdFromParameters(n2, 0.00402312, 17.8785, 69);
            }
        }
        throw new IllegalArgumentException();
    }

    private static int thresholdFromParameters(int n2, double d2, double d3, int n3) {
        return Math.max(n3, (int)Math.floor(d2 * (double)n2 + d3));
    }

    private int ctr(int[] nArray, byte[] byArray, int n2) {
        int n3;
        int n4;
        int n5 = 0;
        int n6 = this.hw - 4;
        for (n4 = 0; n4 <= n6; n4 += 4) {
            n3 = nArray[n4 + 0] + n2 - this.r;
            int n7 = nArray[n4 + 1] + n2 - this.r;
            int n8 = nArray[n4 + 2] + n2 - this.r;
            int n9 = nArray[n4 + 3] + n2 - this.r;
            n3 += n3 >> 31 & this.r;
            n7 += n7 >> 31 & this.r;
            n8 += n8 >> 31 & this.r;
            n9 += n9 >> 31 & this.r;
            n5 += byArray[n3] & 0xFF;
            n5 += byArray[n7] & 0xFF;
            n5 += byArray[n8] & 0xFF;
            n5 += byArray[n9] & 0xFF;
        }
        while (n4 < this.hw) {
            n3 = nArray[n4] + n2 - this.r;
            n3 += n3 >> 31 & this.r;
            n5 += byArray[n3] & 0xFF;
            ++n4;
        }
        return n5;
    }

    private void ctrAll(int[] nArray, byte[] byArray, byte[] byArray2) {
        int n2 = nArray[0];
        int n3 = this.r - n2;
        System.arraycopy(byArray, n2, byArray2, 0, n3);
        System.arraycopy(byArray, 0, byArray2, n3, n2);
        for (n2 = 1; n2 < this.hw; ++n2) {
            int n4;
            n3 = nArray[n2];
            int n5 = this.r - n3;
            int n6 = n5 - 4;
            for (n4 = 0; n4 <= n6; n4 += 4) {
                int n7 = n4 + 0;
                byArray2[n7] = (byte)(byArray2[n7] + (byArray[n3 + n4 + 0] & 0xFF));
                int n8 = n4 + 1;
                byArray2[n8] = (byte)(byArray2[n8] + (byArray[n3 + n4 + 1] & 0xFF));
                int n9 = n4 + 2;
                byArray2[n9] = (byte)(byArray2[n9] + (byArray[n3 + n4 + 2] & 0xFF));
                int n10 = n4 + 3;
                byArray2[n10] = (byte)(byArray2[n10] + (byArray[n3 + n4 + 3] & 0xFF));
            }
            while (n4 < n5) {
                int n11 = n4;
                byArray2[n11] = (byte)(byArray2[n11] + (byArray[n3 + n4] & 0xFF));
                ++n4;
            }
            int n12 = this.r - 4;
            for (n6 = n5; n6 <= n12; n6 += 4) {
                int n13 = n6 + 0;
                byArray2[n13] = (byte)(byArray2[n13] + (byArray[n6 + 0 - n5] & 0xFF));
                int n14 = n6 + 1;
                byArray2[n14] = (byte)(byArray2[n14] + (byArray[n6 + 1 - n5] & 0xFF));
                int n15 = n6 + 2;
                byArray2[n15] = (byte)(byArray2[n15] + (byArray[n6 + 2 - n5] & 0xFF));
                int n16 = n6 + 3;
                byArray2[n16] = (byte)(byArray2[n16] + (byArray[n6 + 3 - n5] & 0xFF));
            }
            while (n6 < this.r) {
                int n17 = n6;
                byArray2[n17] = (byte)(byArray2[n17] + (byArray[n6 - n5] & 0xFF));
                ++n6;
            }
        }
    }

    private void convertToCompact(int[] nArray, byte[] byArray) {
        int n2 = 0;
        for (int i2 = 0; i2 < this.R_BYTE; ++i2) {
            for (int i3 = 0; i3 < 8 && i2 * 8 + i3 != this.r; ++i3) {
                int n3 = byArray[i2] >> i3 & 1;
                nArray[n2] = i2 * 8 + i3 & -n3 | nArray[n2] & ~(-n3);
                n2 = (n2 + n3) % this.hw;
            }
        }
    }

    private int[] getColumnFromCompactVersion(int[] nArray) {
        int[] nArray2 = new int[this.hw];
        if (nArray[0] == 0) {
            nArray2[0] = 0;
            for (int i2 = 1; i2 < this.hw; ++i2) {
                nArray2[i2] = this.r - nArray[this.hw - i2];
            }
        } else {
            for (int i3 = 0; i3 < this.hw; ++i3) {
                nArray2[i3] = this.r - nArray[this.hw - 1 - i3];
            }
        }
        return nArray2;
    }

    private void recomputeSyndrome(byte[] byArray, int n2, int[] nArray, int[] nArray2, boolean bl) {
        byte by;
        byte by2 = by = bl ? (byte)1 : 0;
        if (n2 < this.r) {
            for (int i2 = 0; i2 < this.hw; ++i2) {
                if (nArray[i2] <= n2) {
                    int n3 = n2 - nArray[i2];
                    byArray[n3] = (byte)(byArray[n3] ^ by);
                    continue;
                }
                int n4 = this.r + n2 - nArray[i2];
                byArray[n4] = (byte)(byArray[n4] ^ by);
            }
        } else {
            for (int i3 = 0; i3 < this.hw; ++i3) {
                if (nArray2[i3] <= n2 - this.r) {
                    int n5 = n2 - this.r - nArray2[i3];
                    byArray[n5] = (byte)(byArray[n5] ^ by);
                    continue;
                }
                int n6 = this.r - nArray2[i3] + (n2 - this.r);
                byArray[n6] = (byte)(byArray[n6] ^ by);
            }
        }
    }

    private void splitEBytes(byte[] byArray, byte[] byArray2, byte[] byArray3) {
        int n2 = this.r & 7;
        System.arraycopy(byArray, 0, byArray2, 0, this.R_BYTE - 1);
        byte by = byArray[this.R_BYTE - 1];
        byte by2 = (byte)(-1 << n2);
        byArray2[this.R_BYTE - 1] = (byte)(by & ~by2);
        byte by3 = (byte)(by & by2);
        for (int i2 = 0; i2 < this.R_BYTE; ++i2) {
            byte by4 = byArray[this.R_BYTE + i2];
            byArray3[i2] = (byte)(by4 << 8 - n2 | (by3 & 0xFF) >>> n2);
            by3 = by4;
        }
    }

    private void updateNewErrorIndex(byte[] byArray, int n2, boolean bl) {
        int n3 = n2;
        if (n2 != 0 && n2 != this.r) {
            n3 = n2 > this.r ? 2 * this.r - n2 + this.r : this.r - n2;
        }
        int n4 = n3;
        byArray[n4] = (byte)(byArray[n4] ^ (bl ? (byte)1 : 0));
    }
}

