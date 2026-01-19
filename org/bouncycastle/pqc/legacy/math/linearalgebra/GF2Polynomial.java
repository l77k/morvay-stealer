/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.legacy.math.linearalgebra;

import java.math.BigInteger;
import java.util.Random;
import org.bouncycastle.pqc.legacy.math.linearalgebra.IntUtils;
import org.bouncycastle.pqc.legacy.math.linearalgebra.IntegerFunctions;
import org.bouncycastle.util.Arrays;

public class GF2Polynomial {
    private int len;
    private int blocks;
    private int[] value;
    private static Random rand = new Random();
    private static final boolean[] parity = new boolean[]{false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false};
    private static final short[] squaringTable = new short[]{0, 1, 4, 5, 16, 17, 20, 21, 64, 65, 68, 69, 80, 81, 84, 85, 256, 257, 260, 261, 272, 273, 276, 277, 320, 321, 324, 325, 336, 337, 340, 341, 1024, 1025, 1028, 1029, 1040, 1041, 1044, 1045, 1088, 1089, 1092, 1093, 1104, 1105, 1108, 1109, 1280, 1281, 1284, 1285, 1296, 1297, 1300, 1301, 1344, 1345, 1348, 1349, 1360, 1361, 1364, 1365, 4096, 4097, 4100, 4101, 4112, 4113, 4116, 4117, 4160, 4161, 4164, 4165, 4176, 4177, 4180, 4181, 4352, 4353, 4356, 4357, 4368, 4369, 4372, 4373, 4416, 4417, 4420, 4421, 4432, 4433, 4436, 4437, 5120, 5121, 5124, 5125, 5136, 5137, 5140, 5141, 5184, 5185, 5188, 5189, 5200, 5201, 5204, 5205, 5376, 5377, 5380, 5381, 5392, 5393, 5396, 5397, 5440, 5441, 5444, 5445, 5456, 5457, 5460, 5461, 16384, 16385, 16388, 16389, 16400, 16401, 16404, 16405, 16448, 16449, 16452, 16453, 16464, 16465, 16468, 16469, 16640, 16641, 16644, 16645, 16656, 16657, 16660, 16661, 16704, 16705, 16708, 16709, 16720, 16721, 16724, 16725, 17408, 17409, 17412, 17413, 17424, 17425, 17428, 17429, 17472, 17473, 17476, 17477, 17488, 17489, 17492, 17493, 17664, 17665, 17668, 17669, 17680, 17681, 17684, 17685, 17728, 17729, 17732, 17733, 17744, 17745, 17748, 17749, 20480, 20481, 20484, 20485, 20496, 20497, 20500, 20501, 20544, 20545, 20548, 20549, 20560, 20561, 20564, 20565, 20736, 20737, 20740, 20741, 20752, 20753, 20756, 20757, 20800, 20801, 20804, 20805, 20816, 20817, 20820, 20821, 21504, 21505, 21508, 21509, 21520, 21521, 21524, 21525, 21568, 21569, 21572, 21573, 21584, 21585, 21588, 21589, 21760, 21761, 21764, 21765, 21776, 21777, 21780, 21781, 21824, 21825, 21828, 21829, 21840, 21841, 21844, 21845};
    private static final int[] bitMask = new int[]{1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384, 32768, 65536, 131072, 262144, 524288, 0x100000, 0x200000, 0x400000, 0x800000, 0x1000000, 0x2000000, 0x4000000, 0x8000000, 0x10000000, 0x20000000, 0x40000000, Integer.MIN_VALUE, 0};
    private static final int[] reverseRightMask = new int[]{0, 1, 3, 7, 15, 31, 63, 127, 255, 511, 1023, 2047, 4095, 8191, 16383, Short.MAX_VALUE, 65535, 131071, 262143, 524287, 1048575, 0x1FFFFF, 0x3FFFFF, 0x7FFFFF, 0xFFFFFF, 0x1FFFFFF, 0x3FFFFFF, 0x7FFFFFF, 0xFFFFFFF, 0x1FFFFFFF, 0x3FFFFFFF, Integer.MAX_VALUE, -1};

    public GF2Polynomial(int n2) {
        int n3 = n2;
        if (n3 < 1) {
            n3 = 1;
        }
        this.blocks = (n3 - 1 >> 5) + 1;
        this.value = new int[this.blocks];
        this.len = n3;
    }

    public GF2Polynomial(int n2, Random random) {
        int n3 = n2;
        if (n3 < 1) {
            n3 = 1;
        }
        this.blocks = (n3 - 1 >> 5) + 1;
        this.value = new int[this.blocks];
        this.len = n3;
        this.randomize(random);
    }

    public GF2Polynomial(int n2, String string) {
        int n3 = n2;
        if (n3 < 1) {
            n3 = 1;
        }
        this.blocks = (n3 - 1 >> 5) + 1;
        this.value = new int[this.blocks];
        this.len = n3;
        if (string.equalsIgnoreCase("ZERO")) {
            this.assignZero();
        } else if (string.equalsIgnoreCase("ONE")) {
            this.assignOne();
        } else if (string.equalsIgnoreCase("RANDOM")) {
            this.randomize();
        } else if (string.equalsIgnoreCase("X")) {
            this.assignX();
        } else if (string.equalsIgnoreCase("ALL")) {
            this.assignAll();
        } else {
            throw new IllegalArgumentException("Error: GF2Polynomial was called using " + string + " as value!");
        }
    }

    public GF2Polynomial(int n2, int[] nArray) {
        int n3 = n2;
        if (n3 < 1) {
            n3 = 1;
        }
        this.blocks = (n3 - 1 >> 5) + 1;
        this.value = new int[this.blocks];
        this.len = n3;
        int n4 = Math.min(this.blocks, nArray.length);
        System.arraycopy(nArray, 0, this.value, 0, n4);
        this.zeroUnusedBits();
    }

    public GF2Polynomial(int n2, byte[] byArray) {
        int n3;
        int n4 = n2;
        if (n4 < 1) {
            n4 = 1;
        }
        this.blocks = (n4 - 1 >> 5) + 1;
        this.value = new int[this.blocks];
        this.len = n4;
        int n5 = Math.min((byArray.length - 1 >> 2) + 1, this.blocks);
        int n6 = 0;
        while (n6 < n5 - 1) {
            n3 = byArray.length - (n6 << 2) - 1;
            this.value[n6] = byArray[n3] & 0xFF;
            int n7 = n6;
            this.value[n7] = this.value[n7] | byArray[n3 - 1] << 8 & 0xFF00;
            int n8 = n6;
            this.value[n8] = this.value[n8] | byArray[n3 - 2] << 16 & 0xFF0000;
            int n9 = n6++;
            this.value[n9] = this.value[n9] | byArray[n3 - 3] << 24 & 0xFF000000;
        }
        n6 = n5 - 1;
        n3 = byArray.length - (n6 << 2) - 1;
        this.value[n6] = byArray[n3] & 0xFF;
        if (n3 > 0) {
            int n10 = n6;
            this.value[n10] = this.value[n10] | byArray[n3 - 1] << 8 & 0xFF00;
        }
        if (n3 > 1) {
            int n11 = n6;
            this.value[n11] = this.value[n11] | byArray[n3 - 2] << 16 & 0xFF0000;
        }
        if (n3 > 2) {
            int n12 = n6;
            this.value[n12] = this.value[n12] | byArray[n3 - 3] << 24 & 0xFF000000;
        }
        this.zeroUnusedBits();
        this.reduceN();
    }

    public GF2Polynomial(int n2, BigInteger bigInteger) {
        int n3;
        int n4 = n2;
        if (n4 < 1) {
            n4 = 1;
        }
        this.blocks = (n4 - 1 >> 5) + 1;
        this.value = new int[this.blocks];
        this.len = n4;
        byte[] byArray = bigInteger.toByteArray();
        if (byArray[0] == 0) {
            byte[] byArray2 = new byte[byArray.length - 1];
            System.arraycopy(byArray, 1, byArray2, 0, byArray2.length);
            byArray = byArray2;
        }
        int n5 = byArray.length & 3;
        int n6 = (byArray.length - 1 >> 2) + 1;
        for (n3 = 0; n3 < n5; ++n3) {
            int n7 = n6 - 1;
            this.value[n7] = this.value[n7] | (byArray[n3] & 0xFF) << (n5 - 1 - n3 << 3);
        }
        int n8 = 0;
        n3 = 0;
        while (n3 <= byArray.length - 4 >> 2) {
            n8 = byArray.length - 1 - (n3 << 2);
            this.value[n3] = byArray[n8] & 0xFF;
            int n9 = n3;
            this.value[n9] = this.value[n9] | byArray[n8 - 1] << 8 & 0xFF00;
            int n10 = n3;
            this.value[n10] = this.value[n10] | byArray[n8 - 2] << 16 & 0xFF0000;
            int n11 = n3++;
            this.value[n11] = this.value[n11] | byArray[n8 - 3] << 24 & 0xFF000000;
        }
        if ((this.len & 0x1F) != 0) {
            int n12 = this.blocks - 1;
            this.value[n12] = this.value[n12] & reverseRightMask[this.len & 0x1F];
        }
        this.reduceN();
    }

    public GF2Polynomial(GF2Polynomial gF2Polynomial) {
        this.len = gF2Polynomial.len;
        this.blocks = gF2Polynomial.blocks;
        this.value = IntUtils.clone(gF2Polynomial.value);
    }

    public Object clone() {
        return new GF2Polynomial(this);
    }

    public int getLength() {
        return this.len;
    }

    public int[] toIntegerArray() {
        int[] nArray = new int[this.blocks];
        System.arraycopy(this.value, 0, nArray, 0, this.blocks);
        return nArray;
    }

    public String toString(int n2) {
        char[] cArray = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        String[] stringArray = new String[]{"0000", "0001", "0010", "0011", "0100", "0101", "0110", "0111", "1000", "1001", "1010", "1011", "1100", "1101", "1110", "1111"};
        String string = new String();
        if (n2 == 16) {
            for (int i2 = this.blocks - 1; i2 >= 0; --i2) {
                string = string + cArray[this.value[i2] >>> 28 & 0xF];
                string = string + cArray[this.value[i2] >>> 24 & 0xF];
                string = string + cArray[this.value[i2] >>> 20 & 0xF];
                string = string + cArray[this.value[i2] >>> 16 & 0xF];
                string = string + cArray[this.value[i2] >>> 12 & 0xF];
                string = string + cArray[this.value[i2] >>> 8 & 0xF];
                string = string + cArray[this.value[i2] >>> 4 & 0xF];
                string = string + cArray[this.value[i2] & 0xF];
                string = string + " ";
            }
        } else {
            for (int i3 = this.blocks - 1; i3 >= 0; --i3) {
                string = string + stringArray[this.value[i3] >>> 28 & 0xF];
                string = string + stringArray[this.value[i3] >>> 24 & 0xF];
                string = string + stringArray[this.value[i3] >>> 20 & 0xF];
                string = string + stringArray[this.value[i3] >>> 16 & 0xF];
                string = string + stringArray[this.value[i3] >>> 12 & 0xF];
                string = string + stringArray[this.value[i3] >>> 8 & 0xF];
                string = string + stringArray[this.value[i3] >>> 4 & 0xF];
                string = string + stringArray[this.value[i3] & 0xF];
                string = string + " ";
            }
        }
        return string;
    }

    public byte[] toByteArray() {
        int n2;
        int n3;
        int n4 = (this.len - 1 >> 3) + 1;
        int n5 = n4 & 3;
        byte[] byArray = new byte[n4];
        for (n3 = 0; n3 < n4 >> 2; ++n3) {
            n2 = n4 - (n3 << 2) - 1;
            byArray[n2] = (byte)(this.value[n3] & 0xFF);
            byArray[n2 - 1] = (byte)((this.value[n3] & 0xFF00) >>> 8);
            byArray[n2 - 2] = (byte)((this.value[n3] & 0xFF0000) >>> 16);
            byArray[n2 - 3] = (byte)((this.value[n3] & 0xFF000000) >>> 24);
        }
        for (n3 = 0; n3 < n5; ++n3) {
            n2 = n5 - n3 - 1 << 3;
            byArray[n3] = (byte)((this.value[this.blocks - 1] & 255 << n2) >>> n2);
        }
        return byArray;
    }

    public BigInteger toFlexiBigInt() {
        if (this.len == 0 || this.isZero()) {
            return new BigInteger(0, new byte[0]);
        }
        return new BigInteger(1, this.toByteArray());
    }

    public void assignOne() {
        for (int i2 = 1; i2 < this.blocks; ++i2) {
            this.value[i2] = 0;
        }
        this.value[0] = 1;
    }

    public void assignX() {
        for (int i2 = 1; i2 < this.blocks; ++i2) {
            this.value[i2] = 0;
        }
        this.value[0] = 2;
    }

    public void assignAll() {
        for (int i2 = 0; i2 < this.blocks; ++i2) {
            this.value[i2] = -1;
        }
        this.zeroUnusedBits();
    }

    public void assignZero() {
        for (int i2 = 0; i2 < this.blocks; ++i2) {
            this.value[i2] = 0;
        }
    }

    public void randomize() {
        for (int i2 = 0; i2 < this.blocks; ++i2) {
            this.value[i2] = rand.nextInt();
        }
        this.zeroUnusedBits();
    }

    public void randomize(Random random) {
        for (int i2 = 0; i2 < this.blocks; ++i2) {
            this.value[i2] = random.nextInt();
        }
        this.zeroUnusedBits();
    }

    public boolean equals(Object object) {
        if (object == null || !(object instanceof GF2Polynomial)) {
            return false;
        }
        GF2Polynomial gF2Polynomial = (GF2Polynomial)object;
        if (this.len != gF2Polynomial.len) {
            return false;
        }
        for (int i2 = 0; i2 < this.blocks; ++i2) {
            if (this.value[i2] == gF2Polynomial.value[i2]) continue;
            return false;
        }
        return true;
    }

    public int hashCode() {
        return this.len + Arrays.hashCode(this.value);
    }

    public boolean isZero() {
        if (this.len == 0) {
            return true;
        }
        for (int i2 = 0; i2 < this.blocks; ++i2) {
            if (this.value[i2] == 0) continue;
            return false;
        }
        return true;
    }

    public boolean isOne() {
        for (int i2 = 1; i2 < this.blocks; ++i2) {
            if (this.value[i2] == 0) continue;
            return false;
        }
        return this.value[0] == 1;
    }

    public void addToThis(GF2Polynomial gF2Polynomial) {
        this.expandN(gF2Polynomial.len);
        this.xorThisBy(gF2Polynomial);
    }

    public GF2Polynomial add(GF2Polynomial gF2Polynomial) {
        return this.xor(gF2Polynomial);
    }

    public void subtractFromThis(GF2Polynomial gF2Polynomial) {
        this.expandN(gF2Polynomial.len);
        this.xorThisBy(gF2Polynomial);
    }

    public GF2Polynomial subtract(GF2Polynomial gF2Polynomial) {
        return this.xor(gF2Polynomial);
    }

    public void increaseThis() {
        this.xorBit(0);
    }

    public GF2Polynomial increase() {
        GF2Polynomial gF2Polynomial = new GF2Polynomial(this);
        gF2Polynomial.increaseThis();
        return gF2Polynomial;
    }

    public GF2Polynomial multiplyClassic(GF2Polynomial gF2Polynomial) {
        int n2;
        GF2Polynomial gF2Polynomial2 = new GF2Polynomial(Math.max(this.len, gF2Polynomial.len) << 1);
        GF2Polynomial[] gF2PolynomialArray = new GF2Polynomial[32];
        gF2PolynomialArray[0] = new GF2Polynomial(this);
        for (n2 = 1; n2 <= 31; ++n2) {
            gF2PolynomialArray[n2] = gF2PolynomialArray[n2 - 1].shiftLeft();
        }
        for (n2 = 0; n2 < gF2Polynomial.blocks; ++n2) {
            int n3;
            for (n3 = 0; n3 <= 31; ++n3) {
                if ((gF2Polynomial.value[n2] & bitMask[n3]) == 0) continue;
                gF2Polynomial2.xorThisBy(gF2PolynomialArray[n3]);
            }
            for (n3 = 0; n3 <= 31; ++n3) {
                gF2PolynomialArray[n3].shiftBlocksLeft();
            }
        }
        return gF2Polynomial2;
    }

    public GF2Polynomial multiply(GF2Polynomial gF2Polynomial) {
        int n2 = Math.max(this.len, gF2Polynomial.len);
        this.expandN(n2);
        gF2Polynomial.expandN(n2);
        return this.karaMult(gF2Polynomial);
    }

    private GF2Polynomial karaMult(GF2Polynomial gF2Polynomial) {
        GF2Polynomial gF2Polynomial2 = new GF2Polynomial(this.len << 1);
        if (this.len <= 32) {
            gF2Polynomial2.value = GF2Polynomial.mult32(this.value[0], gF2Polynomial.value[0]);
            return gF2Polynomial2;
        }
        if (this.len <= 64) {
            gF2Polynomial2.value = GF2Polynomial.mult64(this.value, gF2Polynomial.value);
            return gF2Polynomial2;
        }
        if (this.len <= 128) {
            gF2Polynomial2.value = GF2Polynomial.mult128(this.value, gF2Polynomial.value);
            return gF2Polynomial2;
        }
        if (this.len <= 256) {
            gF2Polynomial2.value = GF2Polynomial.mult256(this.value, gF2Polynomial.value);
            return gF2Polynomial2;
        }
        if (this.len <= 512) {
            gF2Polynomial2.value = GF2Polynomial.mult512(this.value, gF2Polynomial.value);
            return gF2Polynomial2;
        }
        int n2 = IntegerFunctions.floorLog(this.len - 1);
        n2 = bitMask[n2];
        GF2Polynomial gF2Polynomial3 = this.lower((n2 - 1 >> 5) + 1);
        GF2Polynomial gF2Polynomial4 = this.upper((n2 - 1 >> 5) + 1);
        GF2Polynomial gF2Polynomial5 = gF2Polynomial.lower((n2 - 1 >> 5) + 1);
        GF2Polynomial gF2Polynomial6 = gF2Polynomial.upper((n2 - 1 >> 5) + 1);
        GF2Polynomial gF2Polynomial7 = gF2Polynomial4.karaMult(gF2Polynomial6);
        GF2Polynomial gF2Polynomial8 = gF2Polynomial3.karaMult(gF2Polynomial5);
        gF2Polynomial3.addToThis(gF2Polynomial4);
        gF2Polynomial5.addToThis(gF2Polynomial6);
        GF2Polynomial gF2Polynomial9 = gF2Polynomial3.karaMult(gF2Polynomial5);
        gF2Polynomial2.shiftLeftAddThis(gF2Polynomial7, n2 << 1);
        gF2Polynomial2.shiftLeftAddThis(gF2Polynomial7, n2);
        gF2Polynomial2.shiftLeftAddThis(gF2Polynomial9, n2);
        gF2Polynomial2.shiftLeftAddThis(gF2Polynomial8, n2);
        gF2Polynomial2.addToThis(gF2Polynomial8);
        return gF2Polynomial2;
    }

    private static int[] mult512(int[] nArray, int[] nArray2) {
        int[] nArray3 = new int[32];
        int[] nArray4 = new int[8];
        System.arraycopy(nArray, 0, nArray4, 0, Math.min(8, nArray.length));
        int[] nArray5 = new int[8];
        if (nArray.length > 8) {
            System.arraycopy(nArray, 8, nArray5, 0, Math.min(8, nArray.length - 8));
        }
        int[] nArray6 = new int[8];
        System.arraycopy(nArray2, 0, nArray6, 0, Math.min(8, nArray2.length));
        int[] nArray7 = new int[8];
        if (nArray2.length > 8) {
            System.arraycopy(nArray2, 8, nArray7, 0, Math.min(8, nArray2.length - 8));
        }
        int[] nArray8 = GF2Polynomial.mult256(nArray5, nArray7);
        nArray3[31] = nArray3[31] ^ nArray8[15];
        nArray3[30] = nArray3[30] ^ nArray8[14];
        nArray3[29] = nArray3[29] ^ nArray8[13];
        nArray3[28] = nArray3[28] ^ nArray8[12];
        nArray3[27] = nArray3[27] ^ nArray8[11];
        nArray3[26] = nArray3[26] ^ nArray8[10];
        nArray3[25] = nArray3[25] ^ nArray8[9];
        nArray3[24] = nArray3[24] ^ nArray8[8];
        nArray3[23] = nArray3[23] ^ (nArray8[7] ^ nArray8[15]);
        nArray3[22] = nArray3[22] ^ (nArray8[6] ^ nArray8[14]);
        nArray3[21] = nArray3[21] ^ (nArray8[5] ^ nArray8[13]);
        nArray3[20] = nArray3[20] ^ (nArray8[4] ^ nArray8[12]);
        nArray3[19] = nArray3[19] ^ (nArray8[3] ^ nArray8[11]);
        nArray3[18] = nArray3[18] ^ (nArray8[2] ^ nArray8[10]);
        nArray3[17] = nArray3[17] ^ (nArray8[1] ^ nArray8[9]);
        nArray3[16] = nArray3[16] ^ (nArray8[0] ^ nArray8[8]);
        nArray3[15] = nArray3[15] ^ nArray8[7];
        nArray3[14] = nArray3[14] ^ nArray8[6];
        nArray3[13] = nArray3[13] ^ nArray8[5];
        nArray3[12] = nArray3[12] ^ nArray8[4];
        nArray3[11] = nArray3[11] ^ nArray8[3];
        nArray3[10] = nArray3[10] ^ nArray8[2];
        nArray3[9] = nArray3[9] ^ nArray8[1];
        nArray3[8] = nArray3[8] ^ nArray8[0];
        nArray5[0] = nArray5[0] ^ nArray4[0];
        nArray5[1] = nArray5[1] ^ nArray4[1];
        nArray5[2] = nArray5[2] ^ nArray4[2];
        nArray5[3] = nArray5[3] ^ nArray4[3];
        nArray5[4] = nArray5[4] ^ nArray4[4];
        nArray5[5] = nArray5[5] ^ nArray4[5];
        nArray5[6] = nArray5[6] ^ nArray4[6];
        nArray5[7] = nArray5[7] ^ nArray4[7];
        nArray7[0] = nArray7[0] ^ nArray6[0];
        nArray7[1] = nArray7[1] ^ nArray6[1];
        nArray7[2] = nArray7[2] ^ nArray6[2];
        nArray7[3] = nArray7[3] ^ nArray6[3];
        nArray7[4] = nArray7[4] ^ nArray6[4];
        nArray7[5] = nArray7[5] ^ nArray6[5];
        nArray7[6] = nArray7[6] ^ nArray6[6];
        nArray7[7] = nArray7[7] ^ nArray6[7];
        int[] nArray9 = GF2Polynomial.mult256(nArray5, nArray7);
        nArray3[23] = nArray3[23] ^ nArray9[15];
        nArray3[22] = nArray3[22] ^ nArray9[14];
        nArray3[21] = nArray3[21] ^ nArray9[13];
        nArray3[20] = nArray3[20] ^ nArray9[12];
        nArray3[19] = nArray3[19] ^ nArray9[11];
        nArray3[18] = nArray3[18] ^ nArray9[10];
        nArray3[17] = nArray3[17] ^ nArray9[9];
        nArray3[16] = nArray3[16] ^ nArray9[8];
        nArray3[15] = nArray3[15] ^ nArray9[7];
        nArray3[14] = nArray3[14] ^ nArray9[6];
        nArray3[13] = nArray3[13] ^ nArray9[5];
        nArray3[12] = nArray3[12] ^ nArray9[4];
        nArray3[11] = nArray3[11] ^ nArray9[3];
        nArray3[10] = nArray3[10] ^ nArray9[2];
        nArray3[9] = nArray3[9] ^ nArray9[1];
        nArray3[8] = nArray3[8] ^ nArray9[0];
        int[] nArray10 = GF2Polynomial.mult256(nArray4, nArray6);
        nArray3[23] = nArray3[23] ^ nArray10[15];
        nArray3[22] = nArray3[22] ^ nArray10[14];
        nArray3[21] = nArray3[21] ^ nArray10[13];
        nArray3[20] = nArray3[20] ^ nArray10[12];
        nArray3[19] = nArray3[19] ^ nArray10[11];
        nArray3[18] = nArray3[18] ^ nArray10[10];
        nArray3[17] = nArray3[17] ^ nArray10[9];
        nArray3[16] = nArray3[16] ^ nArray10[8];
        nArray3[15] = nArray3[15] ^ (nArray10[7] ^ nArray10[15]);
        nArray3[14] = nArray3[14] ^ (nArray10[6] ^ nArray10[14]);
        nArray3[13] = nArray3[13] ^ (nArray10[5] ^ nArray10[13]);
        nArray3[12] = nArray3[12] ^ (nArray10[4] ^ nArray10[12]);
        nArray3[11] = nArray3[11] ^ (nArray10[3] ^ nArray10[11]);
        nArray3[10] = nArray3[10] ^ (nArray10[2] ^ nArray10[10]);
        nArray3[9] = nArray3[9] ^ (nArray10[1] ^ nArray10[9]);
        nArray3[8] = nArray3[8] ^ (nArray10[0] ^ nArray10[8]);
        nArray3[7] = nArray3[7] ^ nArray10[7];
        nArray3[6] = nArray3[6] ^ nArray10[6];
        nArray3[5] = nArray3[5] ^ nArray10[5];
        nArray3[4] = nArray3[4] ^ nArray10[4];
        nArray3[3] = nArray3[3] ^ nArray10[3];
        nArray3[2] = nArray3[2] ^ nArray10[2];
        nArray3[1] = nArray3[1] ^ nArray10[1];
        nArray3[0] = nArray3[0] ^ nArray10[0];
        return nArray3;
    }

    private static int[] mult256(int[] nArray, int[] nArray2) {
        int[] nArray3;
        int[] nArray4 = new int[16];
        int[] nArray5 = new int[4];
        System.arraycopy(nArray, 0, nArray5, 0, Math.min(4, nArray.length));
        int[] nArray6 = new int[4];
        if (nArray.length > 4) {
            System.arraycopy(nArray, 4, nArray6, 0, Math.min(4, nArray.length - 4));
        }
        int[] nArray7 = new int[4];
        System.arraycopy(nArray2, 0, nArray7, 0, Math.min(4, nArray2.length));
        int[] nArray8 = new int[4];
        if (nArray2.length > 4) {
            System.arraycopy(nArray2, 4, nArray8, 0, Math.min(4, nArray2.length - 4));
        }
        if (nArray6[3] == 0 && nArray6[2] == 0 && nArray8[3] == 0 && nArray8[2] == 0) {
            if (nArray6[1] == 0 && nArray8[1] == 0) {
                if (nArray6[0] != 0 || nArray8[0] != 0) {
                    nArray3 = GF2Polynomial.mult32(nArray6[0], nArray8[0]);
                    nArray4[9] = nArray4[9] ^ nArray3[1];
                    nArray4[8] = nArray4[8] ^ nArray3[0];
                    nArray4[5] = nArray4[5] ^ nArray3[1];
                    nArray4[4] = nArray4[4] ^ nArray3[0];
                }
            } else {
                nArray3 = GF2Polynomial.mult64(nArray6, nArray8);
                nArray4[11] = nArray4[11] ^ nArray3[3];
                nArray4[10] = nArray4[10] ^ nArray3[2];
                nArray4[9] = nArray4[9] ^ nArray3[1];
                nArray4[8] = nArray4[8] ^ nArray3[0];
                nArray4[7] = nArray4[7] ^ nArray3[3];
                nArray4[6] = nArray4[6] ^ nArray3[2];
                nArray4[5] = nArray4[5] ^ nArray3[1];
                nArray4[4] = nArray4[4] ^ nArray3[0];
            }
        } else {
            nArray3 = GF2Polynomial.mult128(nArray6, nArray8);
            nArray4[15] = nArray4[15] ^ nArray3[7];
            nArray4[14] = nArray4[14] ^ nArray3[6];
            nArray4[13] = nArray4[13] ^ nArray3[5];
            nArray4[12] = nArray4[12] ^ nArray3[4];
            nArray4[11] = nArray4[11] ^ (nArray3[3] ^ nArray3[7]);
            nArray4[10] = nArray4[10] ^ (nArray3[2] ^ nArray3[6]);
            nArray4[9] = nArray4[9] ^ (nArray3[1] ^ nArray3[5]);
            nArray4[8] = nArray4[8] ^ (nArray3[0] ^ nArray3[4]);
            nArray4[7] = nArray4[7] ^ nArray3[3];
            nArray4[6] = nArray4[6] ^ nArray3[2];
            nArray4[5] = nArray4[5] ^ nArray3[1];
            nArray4[4] = nArray4[4] ^ nArray3[0];
        }
        nArray6[0] = nArray6[0] ^ nArray5[0];
        nArray6[1] = nArray6[1] ^ nArray5[1];
        nArray6[2] = nArray6[2] ^ nArray5[2];
        nArray6[3] = nArray6[3] ^ nArray5[3];
        nArray8[0] = nArray8[0] ^ nArray7[0];
        nArray8[1] = nArray8[1] ^ nArray7[1];
        nArray8[2] = nArray8[2] ^ nArray7[2];
        nArray8[3] = nArray8[3] ^ nArray7[3];
        nArray3 = GF2Polynomial.mult128(nArray6, nArray8);
        nArray4[11] = nArray4[11] ^ nArray3[7];
        nArray4[10] = nArray4[10] ^ nArray3[6];
        nArray4[9] = nArray4[9] ^ nArray3[5];
        nArray4[8] = nArray4[8] ^ nArray3[4];
        nArray4[7] = nArray4[7] ^ nArray3[3];
        nArray4[6] = nArray4[6] ^ nArray3[2];
        nArray4[5] = nArray4[5] ^ nArray3[1];
        nArray4[4] = nArray4[4] ^ nArray3[0];
        int[] nArray9 = GF2Polynomial.mult128(nArray5, nArray7);
        nArray4[11] = nArray4[11] ^ nArray9[7];
        nArray4[10] = nArray4[10] ^ nArray9[6];
        nArray4[9] = nArray4[9] ^ nArray9[5];
        nArray4[8] = nArray4[8] ^ nArray9[4];
        nArray4[7] = nArray4[7] ^ (nArray9[3] ^ nArray9[7]);
        nArray4[6] = nArray4[6] ^ (nArray9[2] ^ nArray9[6]);
        nArray4[5] = nArray4[5] ^ (nArray9[1] ^ nArray9[5]);
        nArray4[4] = nArray4[4] ^ (nArray9[0] ^ nArray9[4]);
        nArray4[3] = nArray4[3] ^ nArray9[3];
        nArray4[2] = nArray4[2] ^ nArray9[2];
        nArray4[1] = nArray4[1] ^ nArray9[1];
        nArray4[0] = nArray4[0] ^ nArray9[0];
        return nArray4;
    }

    private static int[] mult128(int[] nArray, int[] nArray2) {
        int[] nArray3;
        int[] nArray4 = new int[8];
        int[] nArray5 = new int[2];
        System.arraycopy(nArray, 0, nArray5, 0, Math.min(2, nArray.length));
        int[] nArray6 = new int[2];
        if (nArray.length > 2) {
            System.arraycopy(nArray, 2, nArray6, 0, Math.min(2, nArray.length - 2));
        }
        int[] nArray7 = new int[2];
        System.arraycopy(nArray2, 0, nArray7, 0, Math.min(2, nArray2.length));
        int[] nArray8 = new int[2];
        if (nArray2.length > 2) {
            System.arraycopy(nArray2, 2, nArray8, 0, Math.min(2, nArray2.length - 2));
        }
        if (nArray6[1] == 0 && nArray8[1] == 0) {
            if (nArray6[0] != 0 || nArray8[0] != 0) {
                nArray3 = GF2Polynomial.mult32(nArray6[0], nArray8[0]);
                nArray4[5] = nArray4[5] ^ nArray3[1];
                nArray4[4] = nArray4[4] ^ nArray3[0];
                nArray4[3] = nArray4[3] ^ nArray3[1];
                nArray4[2] = nArray4[2] ^ nArray3[0];
            }
        } else {
            nArray3 = GF2Polynomial.mult64(nArray6, nArray8);
            nArray4[7] = nArray4[7] ^ nArray3[3];
            nArray4[6] = nArray4[6] ^ nArray3[2];
            nArray4[5] = nArray4[5] ^ (nArray3[1] ^ nArray3[3]);
            nArray4[4] = nArray4[4] ^ (nArray3[0] ^ nArray3[2]);
            nArray4[3] = nArray4[3] ^ nArray3[1];
            nArray4[2] = nArray4[2] ^ nArray3[0];
        }
        nArray6[0] = nArray6[0] ^ nArray5[0];
        nArray6[1] = nArray6[1] ^ nArray5[1];
        nArray8[0] = nArray8[0] ^ nArray7[0];
        nArray8[1] = nArray8[1] ^ nArray7[1];
        if (nArray6[1] == 0 && nArray8[1] == 0) {
            nArray3 = GF2Polynomial.mult32(nArray6[0], nArray8[0]);
            nArray4[3] = nArray4[3] ^ nArray3[1];
            nArray4[2] = nArray4[2] ^ nArray3[0];
        } else {
            nArray3 = GF2Polynomial.mult64(nArray6, nArray8);
            nArray4[5] = nArray4[5] ^ nArray3[3];
            nArray4[4] = nArray4[4] ^ nArray3[2];
            nArray4[3] = nArray4[3] ^ nArray3[1];
            nArray4[2] = nArray4[2] ^ nArray3[0];
        }
        if (nArray5[1] == 0 && nArray7[1] == 0) {
            nArray3 = GF2Polynomial.mult32(nArray5[0], nArray7[0]);
            nArray4[3] = nArray4[3] ^ nArray3[1];
            nArray4[2] = nArray4[2] ^ nArray3[0];
            nArray4[1] = nArray4[1] ^ nArray3[1];
            nArray4[0] = nArray4[0] ^ nArray3[0];
        } else {
            nArray3 = GF2Polynomial.mult64(nArray5, nArray7);
            nArray4[5] = nArray4[5] ^ nArray3[3];
            nArray4[4] = nArray4[4] ^ nArray3[2];
            nArray4[3] = nArray4[3] ^ (nArray3[1] ^ nArray3[3]);
            nArray4[2] = nArray4[2] ^ (nArray3[0] ^ nArray3[2]);
            nArray4[1] = nArray4[1] ^ nArray3[1];
            nArray4[0] = nArray4[0] ^ nArray3[0];
        }
        return nArray4;
    }

    private static int[] mult64(int[] nArray, int[] nArray2) {
        int[] nArray3;
        int[] nArray4 = new int[4];
        int n2 = nArray[0];
        int n3 = 0;
        if (nArray.length > 1) {
            n3 = nArray[1];
        }
        int n4 = nArray2[0];
        int n5 = 0;
        if (nArray2.length > 1) {
            n5 = nArray2[1];
        }
        if (n3 != 0 || n5 != 0) {
            nArray3 = GF2Polynomial.mult32(n3, n5);
            nArray4[3] = nArray4[3] ^ nArray3[1];
            nArray4[2] = nArray4[2] ^ (nArray3[0] ^ nArray3[1]);
            nArray4[1] = nArray4[1] ^ nArray3[0];
        }
        nArray3 = GF2Polynomial.mult32(n2 ^ n3, n4 ^ n5);
        nArray4[2] = nArray4[2] ^ nArray3[1];
        nArray4[1] = nArray4[1] ^ nArray3[0];
        int[] nArray5 = GF2Polynomial.mult32(n2, n4);
        nArray4[2] = nArray4[2] ^ nArray5[1];
        nArray4[1] = nArray4[1] ^ (nArray5[0] ^ nArray5[1]);
        nArray4[0] = nArray4[0] ^ nArray5[0];
        return nArray4;
    }

    private static int[] mult32(int n2, int n3) {
        int[] nArray = new int[2];
        if (n2 == 0 || n3 == 0) {
            return nArray;
        }
        long l2 = n3;
        l2 &= 0xFFFFFFFFL;
        long l3 = 0L;
        for (int i2 = 1; i2 <= 32; ++i2) {
            if ((n2 & bitMask[i2 - 1]) != 0) {
                l3 ^= l2;
            }
            l2 <<= 1;
        }
        nArray[1] = (int)(l3 >>> 32);
        nArray[0] = (int)(l3 & 0xFFFFFFFFL);
        return nArray;
    }

    private GF2Polynomial upper(int n2) {
        int n3 = Math.min(n2, this.blocks - n2);
        GF2Polynomial gF2Polynomial = new GF2Polynomial(n3 << 5);
        if (this.blocks >= n2) {
            System.arraycopy(this.value, n2, gF2Polynomial.value, 0, n3);
        }
        return gF2Polynomial;
    }

    private GF2Polynomial lower(int n2) {
        GF2Polynomial gF2Polynomial = new GF2Polynomial(n2 << 5);
        System.arraycopy(this.value, 0, gF2Polynomial.value, 0, Math.min(n2, this.blocks));
        return gF2Polynomial;
    }

    public GF2Polynomial remainder(GF2Polynomial gF2Polynomial) throws RuntimeException {
        GF2Polynomial gF2Polynomial2 = new GF2Polynomial(this);
        GF2Polynomial gF2Polynomial3 = new GF2Polynomial(gF2Polynomial);
        if (gF2Polynomial3.isZero()) {
            throw new RuntimeException();
        }
        gF2Polynomial2.reduceN();
        gF2Polynomial3.reduceN();
        if (gF2Polynomial2.len < gF2Polynomial3.len) {
            return gF2Polynomial2;
        }
        int n2 = gF2Polynomial2.len - gF2Polynomial3.len;
        while (n2 >= 0) {
            GF2Polynomial gF2Polynomial4 = gF2Polynomial3.shiftLeft(n2);
            gF2Polynomial2.subtractFromThis(gF2Polynomial4);
            gF2Polynomial2.reduceN();
            n2 = gF2Polynomial2.len - gF2Polynomial3.len;
        }
        return gF2Polynomial2;
    }

    public GF2Polynomial quotient(GF2Polynomial gF2Polynomial) throws RuntimeException {
        GF2Polynomial gF2Polynomial2 = new GF2Polynomial(this.len);
        GF2Polynomial gF2Polynomial3 = new GF2Polynomial(this);
        GF2Polynomial gF2Polynomial4 = new GF2Polynomial(gF2Polynomial);
        if (gF2Polynomial4.isZero()) {
            throw new RuntimeException();
        }
        gF2Polynomial3.reduceN();
        gF2Polynomial4.reduceN();
        if (gF2Polynomial3.len < gF2Polynomial4.len) {
            return new GF2Polynomial(0);
        }
        int n2 = gF2Polynomial3.len - gF2Polynomial4.len;
        gF2Polynomial2.expandN(n2 + 1);
        while (n2 >= 0) {
            GF2Polynomial gF2Polynomial5 = gF2Polynomial4.shiftLeft(n2);
            gF2Polynomial3.subtractFromThis(gF2Polynomial5);
            gF2Polynomial3.reduceN();
            gF2Polynomial2.xorBit(n2);
            n2 = gF2Polynomial3.len - gF2Polynomial4.len;
        }
        return gF2Polynomial2;
    }

    public GF2Polynomial[] divide(GF2Polynomial gF2Polynomial) throws RuntimeException {
        GF2Polynomial[] gF2PolynomialArray = new GF2Polynomial[2];
        GF2Polynomial gF2Polynomial2 = new GF2Polynomial(this.len);
        GF2Polynomial gF2Polynomial3 = new GF2Polynomial(this);
        GF2Polynomial gF2Polynomial4 = new GF2Polynomial(gF2Polynomial);
        if (gF2Polynomial4.isZero()) {
            throw new RuntimeException();
        }
        gF2Polynomial3.reduceN();
        gF2Polynomial4.reduceN();
        if (gF2Polynomial3.len < gF2Polynomial4.len) {
            gF2PolynomialArray[0] = new GF2Polynomial(0);
            gF2PolynomialArray[1] = gF2Polynomial3;
            return gF2PolynomialArray;
        }
        int n2 = gF2Polynomial3.len - gF2Polynomial4.len;
        gF2Polynomial2.expandN(n2 + 1);
        while (n2 >= 0) {
            GF2Polynomial gF2Polynomial5 = gF2Polynomial4.shiftLeft(n2);
            gF2Polynomial3.subtractFromThis(gF2Polynomial5);
            gF2Polynomial3.reduceN();
            gF2Polynomial2.xorBit(n2);
            n2 = gF2Polynomial3.len - gF2Polynomial4.len;
        }
        gF2PolynomialArray[0] = gF2Polynomial2;
        gF2PolynomialArray[1] = gF2Polynomial3;
        return gF2PolynomialArray;
    }

    public GF2Polynomial gcd(GF2Polynomial gF2Polynomial) throws RuntimeException {
        if (this.isZero() && gF2Polynomial.isZero()) {
            throw new ArithmeticException("Both operands of gcd equal zero.");
        }
        if (this.isZero()) {
            return new GF2Polynomial(gF2Polynomial);
        }
        if (gF2Polynomial.isZero()) {
            return new GF2Polynomial(this);
        }
        GF2Polynomial gF2Polynomial2 = new GF2Polynomial(this);
        GF2Polynomial gF2Polynomial3 = new GF2Polynomial(gF2Polynomial);
        while (!gF2Polynomial3.isZero()) {
            GF2Polynomial gF2Polynomial4 = gF2Polynomial2.remainder(gF2Polynomial3);
            gF2Polynomial2 = gF2Polynomial3;
            gF2Polynomial3 = gF2Polynomial4;
        }
        return gF2Polynomial2;
    }

    public boolean isIrreducible() {
        if (this.isZero()) {
            return false;
        }
        GF2Polynomial gF2Polynomial = new GF2Polynomial(this);
        gF2Polynomial.reduceN();
        int n2 = gF2Polynomial.len - 1;
        GF2Polynomial gF2Polynomial2 = new GF2Polynomial(gF2Polynomial.len, "X");
        for (int i2 = 1; i2 <= n2 >> 1; ++i2) {
            gF2Polynomial2.squareThisPreCalc();
            gF2Polynomial2 = gF2Polynomial2.remainder(gF2Polynomial);
            GF2Polynomial gF2Polynomial3 = gF2Polynomial2.add(new GF2Polynomial(32, "X"));
            if (!gF2Polynomial3.isZero()) {
                GF2Polynomial gF2Polynomial4 = gF2Polynomial.gcd(gF2Polynomial3);
                if (gF2Polynomial4.isOne()) continue;
                return false;
            }
            return false;
        }
        return true;
    }

    void reduceTrinomial(int n2, int n3) {
        long l2;
        int n4 = n2 >>> 5;
        int n5 = 32 - (n2 & 0x1F);
        int n6 = n2 - n3 >>> 5;
        int n7 = 32 - (n2 - n3 & 0x1F);
        int n8 = (n2 << 1) - 2 >>> 5;
        int n9 = n4;
        for (int i2 = n8; i2 > n9; --i2) {
            l2 = (long)this.value[i2] & 0xFFFFFFFFL;
            int n10 = i2 - n4 - 1;
            this.value[n10] = this.value[n10] ^ (int)(l2 << n5);
            int n11 = i2 - n4;
            this.value[n11] = (int)((long)this.value[n11] ^ l2 >>> 32 - n5);
            int n12 = i2 - n6 - 1;
            this.value[n12] = this.value[n12] ^ (int)(l2 << n7);
            int n13 = i2 - n6;
            this.value[n13] = (int)((long)this.value[n13] ^ l2 >>> 32 - n7);
            this.value[i2] = 0;
        }
        l2 = (long)this.value[n9] & 0xFFFFFFFFL & 0xFFFFFFFFL << (n2 & 0x1F);
        this.value[0] = (int)((long)this.value[0] ^ l2 >>> 32 - n5);
        if (n9 - n6 - 1 >= 0) {
            int n14 = n9 - n6 - 1;
            this.value[n14] = this.value[n14] ^ (int)(l2 << n7);
        }
        int n15 = n9 - n6;
        this.value[n15] = (int)((long)this.value[n15] ^ l2 >>> 32 - n7);
        int n16 = n9;
        this.value[n16] = this.value[n16] & reverseRightMask[n2 & 0x1F];
        this.blocks = (n2 - 1 >>> 5) + 1;
        this.len = n2;
    }

    void reducePentanomial(int n2, int[] nArray) {
        long l2;
        int n3 = n2 >>> 5;
        int n4 = 32 - (n2 & 0x1F);
        int n5 = n2 - nArray[0] >>> 5;
        int n6 = 32 - (n2 - nArray[0] & 0x1F);
        int n7 = n2 - nArray[1] >>> 5;
        int n8 = 32 - (n2 - nArray[1] & 0x1F);
        int n9 = n2 - nArray[2] >>> 5;
        int n10 = 32 - (n2 - nArray[2] & 0x1F);
        int n11 = (n2 << 1) - 2 >>> 5;
        int n12 = n3;
        for (int i2 = n11; i2 > n12; --i2) {
            l2 = (long)this.value[i2] & 0xFFFFFFFFL;
            int n13 = i2 - n3 - 1;
            this.value[n13] = this.value[n13] ^ (int)(l2 << n4);
            int n14 = i2 - n3;
            this.value[n14] = (int)((long)this.value[n14] ^ l2 >>> 32 - n4);
            int n15 = i2 - n5 - 1;
            this.value[n15] = this.value[n15] ^ (int)(l2 << n6);
            int n16 = i2 - n5;
            this.value[n16] = (int)((long)this.value[n16] ^ l2 >>> 32 - n6);
            int n17 = i2 - n7 - 1;
            this.value[n17] = this.value[n17] ^ (int)(l2 << n8);
            int n18 = i2 - n7;
            this.value[n18] = (int)((long)this.value[n18] ^ l2 >>> 32 - n8);
            int n19 = i2 - n9 - 1;
            this.value[n19] = this.value[n19] ^ (int)(l2 << n10);
            int n20 = i2 - n9;
            this.value[n20] = (int)((long)this.value[n20] ^ l2 >>> 32 - n10);
            this.value[i2] = 0;
        }
        l2 = (long)this.value[n12] & 0xFFFFFFFFL & 0xFFFFFFFFL << (n2 & 0x1F);
        this.value[0] = (int)((long)this.value[0] ^ l2 >>> 32 - n4);
        if (n12 - n5 - 1 >= 0) {
            int n21 = n12 - n5 - 1;
            this.value[n21] = this.value[n21] ^ (int)(l2 << n6);
        }
        int n22 = n12 - n5;
        this.value[n22] = (int)((long)this.value[n22] ^ l2 >>> 32 - n6);
        if (n12 - n7 - 1 >= 0) {
            int n23 = n12 - n7 - 1;
            this.value[n23] = this.value[n23] ^ (int)(l2 << n8);
        }
        int n24 = n12 - n7;
        this.value[n24] = (int)((long)this.value[n24] ^ l2 >>> 32 - n8);
        if (n12 - n9 - 1 >= 0) {
            int n25 = n12 - n9 - 1;
            this.value[n25] = this.value[n25] ^ (int)(l2 << n10);
        }
        int n26 = n12 - n9;
        this.value[n26] = (int)((long)this.value[n26] ^ l2 >>> 32 - n10);
        int n27 = n12;
        this.value[n27] = this.value[n27] & reverseRightMask[n2 & 0x1F];
        this.blocks = (n2 - 1 >>> 5) + 1;
        this.len = n2;
    }

    public void reduceN() {
        int n2;
        for (n2 = this.blocks - 1; this.value[n2] == 0 && n2 > 0; --n2) {
        }
        int n3 = this.value[n2];
        int n4 = 0;
        while (n3 != 0) {
            n3 >>>= 1;
            ++n4;
        }
        this.len = (n2 << 5) + n4;
        this.blocks = n2 + 1;
    }

    public void expandN(int n2) {
        if (this.len >= n2) {
            return;
        }
        this.len = n2;
        int n3 = (n2 - 1 >>> 5) + 1;
        if (this.blocks >= n3) {
            return;
        }
        if (this.value.length >= n3) {
            for (int i2 = this.blocks; i2 < n3; ++i2) {
                this.value[i2] = 0;
            }
            this.blocks = n3;
            return;
        }
        int[] nArray = new int[n3];
        System.arraycopy(this.value, 0, nArray, 0, this.blocks);
        this.blocks = n3;
        this.value = null;
        this.value = nArray;
    }

    public void squareThisBitwise() {
        if (this.isZero()) {
            return;
        }
        int[] nArray = new int[this.blocks << 1];
        for (int i2 = this.blocks - 1; i2 >= 0; --i2) {
            int n2 = this.value[i2];
            int n3 = 1;
            for (int i3 = 0; i3 < 16; ++i3) {
                if ((n2 & 1) != 0) {
                    int n4 = i2 << 1;
                    nArray[n4] = nArray[n4] | n3;
                }
                if ((n2 & 0x10000) != 0) {
                    int n5 = (i2 << 1) + 1;
                    nArray[n5] = nArray[n5] | n3;
                }
                n3 <<= 2;
                n2 >>>= 1;
            }
        }
        this.value = null;
        this.value = nArray;
        this.blocks = nArray.length;
        this.len = (this.len << 1) - 1;
    }

    public void squareThisPreCalc() {
        if (this.isZero()) {
            return;
        }
        if (this.value.length >= this.blocks << 1) {
            for (int i2 = this.blocks - 1; i2 >= 0; --i2) {
                this.value[(i2 << 1) + 1] = squaringTable[(this.value[i2] & 0xFF0000) >>> 16] | squaringTable[(this.value[i2] & 0xFF000000) >>> 24] << 16;
                this.value[i2 << 1] = squaringTable[this.value[i2] & 0xFF] | squaringTable[(this.value[i2] & 0xFF00) >>> 8] << 16;
            }
            this.blocks <<= 1;
            this.len = (this.len << 1) - 1;
        } else {
            int[] nArray = new int[this.blocks << 1];
            for (int i3 = 0; i3 < this.blocks; ++i3) {
                nArray[i3 << 1] = squaringTable[this.value[i3] & 0xFF] | squaringTable[(this.value[i3] & 0xFF00) >>> 8] << 16;
                nArray[(i3 << 1) + 1] = squaringTable[(this.value[i3] & 0xFF0000) >>> 16] | squaringTable[(this.value[i3] & 0xFF000000) >>> 24] << 16;
            }
            this.value = null;
            this.value = nArray;
            this.blocks <<= 1;
            this.len = (this.len << 1) - 1;
        }
    }

    public boolean vectorMult(GF2Polynomial gF2Polynomial) throws RuntimeException {
        boolean bl = false;
        if (this.len != gF2Polynomial.len) {
            throw new RuntimeException();
        }
        for (int i2 = 0; i2 < this.blocks; ++i2) {
            int n2 = this.value[i2] & gF2Polynomial.value[i2];
            bl ^= parity[n2 & 0xFF];
            bl ^= parity[n2 >>> 8 & 0xFF];
            bl ^= parity[n2 >>> 16 & 0xFF];
            bl ^= parity[n2 >>> 24 & 0xFF];
        }
        return bl;
    }

    public GF2Polynomial xor(GF2Polynomial gF2Polynomial) {
        GF2Polynomial gF2Polynomial2;
        int n2 = Math.min(this.blocks, gF2Polynomial.blocks);
        if (this.len >= gF2Polynomial.len) {
            gF2Polynomial2 = new GF2Polynomial(this);
            for (int i2 = 0; i2 < n2; ++i2) {
                int n3 = i2;
                gF2Polynomial2.value[n3] = gF2Polynomial2.value[n3] ^ gF2Polynomial.value[i2];
            }
        } else {
            gF2Polynomial2 = new GF2Polynomial(gF2Polynomial);
            for (int i3 = 0; i3 < n2; ++i3) {
                int n4 = i3;
                gF2Polynomial2.value[n4] = gF2Polynomial2.value[n4] ^ this.value[i3];
            }
        }
        gF2Polynomial2.zeroUnusedBits();
        return gF2Polynomial2;
    }

    public void xorThisBy(GF2Polynomial gF2Polynomial) {
        for (int i2 = 0; i2 < Math.min(this.blocks, gF2Polynomial.blocks); ++i2) {
            int n2 = i2;
            this.value[n2] = this.value[n2] ^ gF2Polynomial.value[i2];
        }
        this.zeroUnusedBits();
    }

    private void zeroUnusedBits() {
        if ((this.len & 0x1F) != 0) {
            int n2 = this.blocks - 1;
            this.value[n2] = this.value[n2] & reverseRightMask[this.len & 0x1F];
        }
    }

    public void setBit(int n2) throws RuntimeException {
        if (n2 < 0 || n2 > this.len - 1) {
            throw new RuntimeException();
        }
        int n3 = n2 >>> 5;
        this.value[n3] = this.value[n3] | bitMask[n2 & 0x1F];
    }

    public int getBit(int n2) {
        if (n2 < 0) {
            throw new RuntimeException();
        }
        if (n2 > this.len - 1) {
            return 0;
        }
        return (this.value[n2 >>> 5] & bitMask[n2 & 0x1F]) != 0 ? 1 : 0;
    }

    public void resetBit(int n2) throws RuntimeException {
        if (n2 < 0) {
            throw new RuntimeException();
        }
        if (n2 > this.len - 1) {
            return;
        }
        int n3 = n2 >>> 5;
        this.value[n3] = this.value[n3] & ~bitMask[n2 & 0x1F];
    }

    public void xorBit(int n2) throws RuntimeException {
        if (n2 < 0 || n2 > this.len - 1) {
            throw new RuntimeException();
        }
        int n3 = n2 >>> 5;
        this.value[n3] = this.value[n3] ^ bitMask[n2 & 0x1F];
    }

    public boolean testBit(int n2) {
        if (n2 < 0) {
            throw new RuntimeException();
        }
        if (n2 > this.len - 1) {
            return false;
        }
        return (this.value[n2 >>> 5] & bitMask[n2 & 0x1F]) != 0;
    }

    public GF2Polynomial shiftLeft() {
        GF2Polynomial gF2Polynomial = new GF2Polynomial(this.len + 1, this.value);
        for (int i2 = gF2Polynomial.blocks - 1; i2 >= 1; --i2) {
            int n2 = i2;
            gF2Polynomial.value[n2] = gF2Polynomial.value[n2] << 1;
            int n3 = i2;
            gF2Polynomial.value[n3] = gF2Polynomial.value[n3] | gF2Polynomial.value[i2 - 1] >>> 31;
        }
        gF2Polynomial.value[0] = gF2Polynomial.value[0] << 1;
        return gF2Polynomial;
    }

    public void shiftLeftThis() {
        if ((this.len & 0x1F) == 0) {
            ++this.len;
            ++this.blocks;
            if (this.blocks > this.value.length) {
                int[] nArray = new int[this.blocks];
                System.arraycopy(this.value, 0, nArray, 0, this.value.length);
                this.value = null;
                this.value = nArray;
            }
            for (int i2 = this.blocks - 1; i2 >= 1; --i2) {
                int n2 = i2;
                this.value[n2] = this.value[n2] | this.value[i2 - 1] >>> 31;
                int n3 = i2 - 1;
                this.value[n3] = this.value[n3] << 1;
            }
        } else {
            ++this.len;
            for (int i3 = this.blocks - 1; i3 >= 1; --i3) {
                int n4 = i3;
                this.value[n4] = this.value[n4] << 1;
                int n5 = i3;
                this.value[n5] = this.value[n5] | this.value[i3 - 1] >>> 31;
            }
            this.value[0] = this.value[0] << 1;
        }
    }

    public GF2Polynomial shiftLeft(int n2) {
        int n3;
        GF2Polynomial gF2Polynomial = new GF2Polynomial(this.len + n2, this.value);
        if (n2 >= 32) {
            gF2Polynomial.doShiftBlocksLeft(n2 >>> 5);
        }
        if ((n3 = n2 & 0x1F) != 0) {
            for (int i2 = gF2Polynomial.blocks - 1; i2 >= 1; --i2) {
                int n4 = i2;
                gF2Polynomial.value[n4] = gF2Polynomial.value[n4] << n3;
                int n5 = i2;
                gF2Polynomial.value[n5] = gF2Polynomial.value[n5] | gF2Polynomial.value[i2 - 1] >>> 32 - n3;
            }
            gF2Polynomial.value[0] = gF2Polynomial.value[0] << n3;
        }
        return gF2Polynomial;
    }

    public void shiftLeftAddThis(GF2Polynomial gF2Polynomial, int n2) {
        if (n2 == 0) {
            this.addToThis(gF2Polynomial);
            return;
        }
        this.expandN(gF2Polynomial.len + n2);
        int n3 = n2 >>> 5;
        for (int i2 = gF2Polynomial.blocks - 1; i2 >= 0; --i2) {
            if (i2 + n3 + 1 < this.blocks && (n2 & 0x1F) != 0) {
                int n4 = i2 + n3 + 1;
                this.value[n4] = this.value[n4] ^ gF2Polynomial.value[i2] >>> 32 - (n2 & 0x1F);
            }
            int n5 = i2 + n3;
            this.value[n5] = this.value[n5] ^ gF2Polynomial.value[i2] << (n2 & 0x1F);
        }
    }

    void shiftBlocksLeft() {
        ++this.blocks;
        this.len += 32;
        if (this.blocks <= this.value.length) {
            for (int i2 = this.blocks - 1; i2 >= 1; --i2) {
                this.value[i2] = this.value[i2 - 1];
            }
            this.value[0] = 0;
        } else {
            int[] nArray = new int[this.blocks];
            System.arraycopy(this.value, 0, nArray, 1, this.blocks - 1);
            this.value = null;
            this.value = nArray;
        }
    }

    private void doShiftBlocksLeft(int n2) {
        if (this.blocks <= this.value.length) {
            int n3;
            for (n3 = this.blocks - 1; n3 >= n2; --n3) {
                this.value[n3] = this.value[n3 - n2];
            }
            for (n3 = 0; n3 < n2; ++n3) {
                this.value[n3] = 0;
            }
        } else {
            int[] nArray = new int[this.blocks];
            System.arraycopy(this.value, 0, nArray, n2, this.blocks - n2);
            this.value = null;
            this.value = nArray;
        }
    }

    public GF2Polynomial shiftRight() {
        GF2Polynomial gF2Polynomial = new GF2Polynomial(this.len - 1);
        System.arraycopy(this.value, 0, gF2Polynomial.value, 0, gF2Polynomial.blocks);
        for (int i2 = 0; i2 <= gF2Polynomial.blocks - 2; ++i2) {
            int n2 = i2;
            gF2Polynomial.value[n2] = gF2Polynomial.value[n2] >>> 1;
            int n3 = i2;
            gF2Polynomial.value[n3] = gF2Polynomial.value[n3] | gF2Polynomial.value[i2 + 1] << 31;
        }
        int n4 = gF2Polynomial.blocks - 1;
        gF2Polynomial.value[n4] = gF2Polynomial.value[n4] >>> 1;
        if (gF2Polynomial.blocks < this.blocks) {
            int n5 = gF2Polynomial.blocks - 1;
            gF2Polynomial.value[n5] = gF2Polynomial.value[n5] | this.value[gF2Polynomial.blocks] << 31;
        }
        return gF2Polynomial;
    }

    public void shiftRightThis() {
        --this.len;
        this.blocks = (this.len - 1 >>> 5) + 1;
        for (int i2 = 0; i2 <= this.blocks - 2; ++i2) {
            int n2 = i2;
            this.value[n2] = this.value[n2] >>> 1;
            int n3 = i2;
            this.value[n3] = this.value[n3] | this.value[i2 + 1] << 31;
        }
        int n4 = this.blocks - 1;
        this.value[n4] = this.value[n4] >>> 1;
        if ((this.len & 0x1F) == 0) {
            int n5 = this.blocks - 1;
            this.value[n5] = this.value[n5] | this.value[this.blocks] << 31;
        }
    }
}

