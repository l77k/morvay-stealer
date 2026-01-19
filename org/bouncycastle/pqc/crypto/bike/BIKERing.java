/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.bike;

import java.util.HashMap;
import java.util.Map;
import org.bouncycastle.math.raw.Interleave;
import org.bouncycastle.math.raw.Mod;
import org.bouncycastle.math.raw.Nat;
import org.bouncycastle.util.Integers;
import org.bouncycastle.util.Pack;

class BIKERing {
    private static final int PERMUTATION_CUTOFF = 64;
    private final int bits;
    private final int size;
    private final int sizeExt;
    private final Map<Integer, Integer> halfPowers = new HashMap<Integer, Integer>();

    BIKERing(int n2) {
        if ((n2 & 0xFFFF0001) != 1) {
            throw new IllegalArgumentException();
        }
        this.bits = n2;
        this.size = n2 + 63 >>> 6;
        this.sizeExt = this.size * 2;
        BIKERing.generateHalfPowersInv(this.halfPowers, n2);
    }

    void add(long[] lArray, long[] lArray2, long[] lArray3) {
        for (int i2 = 0; i2 < this.size; ++i2) {
            lArray3[i2] = lArray[i2] ^ lArray2[i2];
        }
    }

    void addTo(long[] lArray, long[] lArray2) {
        for (int i2 = 0; i2 < this.size; ++i2) {
            int n2 = i2;
            lArray2[n2] = lArray2[n2] ^ lArray[i2];
        }
    }

    void copy(long[] lArray, long[] lArray2) {
        for (int i2 = 0; i2 < this.size; ++i2) {
            lArray2[i2] = lArray[i2];
        }
    }

    long[] create() {
        return new long[this.size];
    }

    long[] createExt() {
        return new long[this.sizeExt];
    }

    void decodeBytes(byte[] byArray, long[] lArray) {
        int n2 = this.bits & 0x3F;
        Pack.littleEndianToLong(byArray, 0, lArray, 0, this.size - 1);
        byte[] byArray2 = new byte[8];
        System.arraycopy(byArray, this.size - 1 << 3, byArray2, 0, n2 + 7 >>> 3);
        lArray[this.size - 1] = Pack.littleEndianToLong(byArray2, 0);
    }

    byte[] encodeBitsTransposed(long[] lArray) {
        byte[] byArray = new byte[this.bits];
        byArray[0] = (byte)(lArray[0] & 1L);
        for (int i2 = 1; i2 < this.bits; ++i2) {
            byArray[this.bits - i2] = (byte)(lArray[i2 >>> 6] >>> (i2 & 0x3F) & 1L);
        }
        return byArray;
    }

    void encodeBytes(long[] lArray, byte[] byArray) {
        int n2 = this.bits & 0x3F;
        Pack.longToLittleEndian(lArray, 0, this.size - 1, byArray, 0);
        byte[] byArray2 = new byte[8];
        Pack.longToLittleEndian(lArray[this.size - 1], byArray2, 0);
        System.arraycopy(byArray2, 0, byArray, this.size - 1 << 3, n2 + 7 >>> 3);
    }

    void inv(long[] lArray, long[] lArray2) {
        long[] lArray3 = this.create();
        long[] lArray4 = this.create();
        long[] lArray5 = this.create();
        this.copy(lArray, lArray3);
        this.copy(lArray, lArray5);
        int n2 = this.bits - 2;
        int n3 = 32 - Integers.numberOfLeadingZeros(n2);
        for (int i2 = 1; i2 < n3; ++i2) {
            this.squareN(lArray3, 1 << i2 - 1, lArray4);
            this.multiply(lArray3, lArray4, lArray3);
            if ((n2 & 1 << i2) == 0) continue;
            int n4 = n2 & (1 << i2) - 1;
            this.squareN(lArray3, n4, lArray4);
            this.multiply(lArray5, lArray4, lArray5);
        }
        this.square(lArray5, lArray2);
    }

    void multiply(long[] lArray, long[] lArray2, long[] lArray3) {
        long[] lArray4 = this.createExt();
        this.implMultiplyAcc(lArray, lArray2, lArray4);
        this.reduce(lArray4, lArray3);
    }

    void reduce(long[] lArray, long[] lArray2) {
        int n2 = this.bits & 0x3F;
        int n3 = 64 - n2;
        long l2 = -1L >>> n3;
        Nat.shiftUpBits64(this.size, lArray, this.size, n3, lArray[this.size - 1], lArray2, 0);
        this.addTo(lArray, lArray2);
        int n4 = this.size - 1;
        lArray2[n4] = lArray2[n4] & l2;
    }

    int getSize() {
        return this.size;
    }

    int getSizeExt() {
        return this.sizeExt;
    }

    void square(long[] lArray, long[] lArray2) {
        long[] lArray3 = this.createExt();
        this.implSquare(lArray, lArray3);
        this.reduce(lArray3, lArray2);
    }

    void squareN(long[] lArray, int n2, long[] lArray2) {
        if (n2 >= 64) {
            this.implPermute(lArray, n2, lArray2);
            return;
        }
        long[] lArray3 = this.createExt();
        this.implSquare(lArray, lArray3);
        this.reduce(lArray3, lArray2);
        while (--n2 > 0) {
            this.implSquare(lArray2, lArray3);
            this.reduce(lArray3, lArray2);
        }
    }

    private static int implModAdd(int n2, int n3, int n4) {
        int n5 = n3 + n4 - n2;
        return n5 + (n5 >> 31 & n2);
    }

    protected void implMultiplyAcc(long[] lArray, long[] lArray2, long[] lArray3) {
        int n2;
        long[] lArray4 = new long[16];
        for (int i2 = 0; i2 < this.size; ++i2) {
            BIKERing.implMulwAcc(lArray4, lArray[i2], lArray2[i2], lArray3, i2 << 1);
        }
        long l2 = lArray3[0];
        long l3 = lArray3[1];
        for (int i3 = 1; i3 < this.size; ++i3) {
            lArray3[i3] = (l2 ^= lArray3[i3 << 1]) ^ l3;
            l3 ^= lArray3[(i3 << 1) + 1];
        }
        long l4 = l2 ^ l3;
        for (n2 = 0; n2 < this.size; ++n2) {
            lArray3[this.size + n2] = lArray3[n2] ^ l4;
        }
        n2 = this.size - 1;
        for (int i4 = 1; i4 < n2 * 2; ++i4) {
            int n3 = Math.min(n2, i4);
            for (int i5 = i4 - n3; i5 < n3; ++i5, --n3) {
                BIKERing.implMulwAcc(lArray4, lArray[i5] ^ lArray[n3], lArray2[i5] ^ lArray2[n3], lArray3, i4);
            }
        }
    }

    private void implPermute(long[] lArray, int n2, long[] lArray2) {
        int n3 = this.bits;
        int n4 = this.halfPowers.get(Integers.valueOf(n2));
        int n5 = BIKERing.implModAdd(n3, n4, n4);
        int n6 = BIKERing.implModAdd(n3, n5, n5);
        int n7 = BIKERing.implModAdd(n3, n6, n6);
        int n8 = n3 - n7;
        int n9 = BIKERing.implModAdd(n3, n8, n4);
        int n10 = BIKERing.implModAdd(n3, n8, n5);
        int n11 = BIKERing.implModAdd(n3, n9, n5);
        int n12 = BIKERing.implModAdd(n3, n8, n6);
        int n13 = BIKERing.implModAdd(n3, n9, n6);
        int n14 = BIKERing.implModAdd(n3, n10, n6);
        int n15 = BIKERing.implModAdd(n3, n11, n6);
        for (int i2 = 0; i2 < this.size; ++i2) {
            long l2 = 0L;
            for (int i3 = 0; i3 < 64; i3 += 8) {
                n8 = BIKERing.implModAdd(n3, n8, n7);
                n9 = BIKERing.implModAdd(n3, n9, n7);
                n10 = BIKERing.implModAdd(n3, n10, n7);
                n11 = BIKERing.implModAdd(n3, n11, n7);
                n12 = BIKERing.implModAdd(n3, n12, n7);
                n13 = BIKERing.implModAdd(n3, n13, n7);
                n14 = BIKERing.implModAdd(n3, n14, n7);
                n15 = BIKERing.implModAdd(n3, n15, n7);
                l2 |= (lArray[n8 >>> 6] >>> n8 & 1L) << i3 + 0;
                l2 |= (lArray[n9 >>> 6] >>> n9 & 1L) << i3 + 1;
                l2 |= (lArray[n10 >>> 6] >>> n10 & 1L) << i3 + 2;
                l2 |= (lArray[n11 >>> 6] >>> n11 & 1L) << i3 + 3;
                l2 |= (lArray[n12 >>> 6] >>> n12 & 1L) << i3 + 4;
                l2 |= (lArray[n13 >>> 6] >>> n13 & 1L) << i3 + 5;
                l2 |= (lArray[n14 >>> 6] >>> n14 & 1L) << i3 + 6;
                l2 |= (lArray[n15 >>> 6] >>> n15 & 1L) << i3 + 7;
            }
            lArray2[i2] = l2;
        }
        int n16 = this.size - 1;
        lArray2[n16] = lArray2[n16] & -1L >>> -n3;
    }

    private static int generateHalfPower(int n2, int n3, int n4) {
        int n5;
        int n6;
        int n7 = 1;
        for (n6 = n4; n6 >= 32; n6 -= 32) {
            n5 = n3 * n7;
            long l2 = ((long)n5 & 0xFFFFFFFFL) * (long)n2;
            long l3 = l2 + (long)n7;
            n7 = (int)(l3 >>> 32);
        }
        if (n6 > 0) {
            n5 = -1 >>> -n6;
            int n8 = n3 * n7 & n5;
            long l4 = ((long)n8 & 0xFFFFFFFFL) * (long)n2;
            long l5 = l4 + (long)n7;
            n7 = (int)(l5 >>> n6);
        }
        return n7;
    }

    private static void generateHalfPowersInv(Map<Integer, Integer> map, int n2) {
        int n3 = n2 - 2;
        int n4 = 32 - Integers.numberOfLeadingZeros(n3);
        int n5 = Mod.inverse32(-n2);
        for (int i2 = 1; i2 < n4; ++i2) {
            int n6;
            int n7 = 1 << i2 - 1;
            if (n7 >= 64 && !map.containsKey(Integers.valueOf(n7))) {
                map.put(Integers.valueOf(n7), Integers.valueOf(BIKERing.generateHalfPower(n2, n5, n7)));
            }
            if ((n3 & 1 << i2) == 0 || (n6 = n3 & (1 << i2) - 1) < 64 || map.containsKey(Integers.valueOf(n6))) continue;
            map.put(Integers.valueOf(n6), Integers.valueOf(BIKERing.generateHalfPower(n2, n5, n6)));
        }
    }

    private static void implMulwAcc(long[] lArray, long l2, long l3, long[] lArray2, int n2) {
        int n3;
        lArray[1] = l3;
        for (n3 = 2; n3 < 16; n3 += 2) {
            lArray[n3] = lArray[n3 >>> 1] << 1;
            lArray[n3 + 1] = lArray[n3] ^ l3;
        }
        n3 = (int)l2;
        long l4 = 0L;
        long l5 = lArray[n3 & 0xF] ^ lArray[n3 >>> 4 & 0xF] << 4;
        int n4 = 56;
        do {
            n3 = (int)(l2 >>> n4);
            long l6 = lArray[n3 & 0xF] ^ lArray[n3 >>> 4 & 0xF] << 4;
            l5 ^= l6 << n4;
            l4 ^= l6 >>> -n4;
        } while ((n4 -= 8) > 0);
        for (int i2 = 0; i2 < 7; ++i2) {
            l2 = (l2 & 0xFEFEFEFEFEFEFEFEL) >>> 1;
            l4 ^= l2 & l3 << i2 >> 63;
        }
        int n5 = n2;
        lArray2[n5] = lArray2[n5] ^ l5;
        int n6 = n2 + 1;
        lArray2[n6] = lArray2[n6] ^ l4;
    }

    private void implSquare(long[] lArray, long[] lArray2) {
        Interleave.expand64To128(lArray, 0, this.size, lArray2, 0);
    }
}

