/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.math.raw;

import java.util.Random;
import org.bouncycastle.math.raw.Nat;
import org.bouncycastle.util.Integers;

public abstract class Mod {
    private static final int M30 = 0x3FFFFFFF;
    private static final long M32L = 0xFFFFFFFFL;

    public static void checkedModOddInverse(int[] nArray, int[] nArray2, int[] nArray3) {
        if (0 == Mod.modOddInverse(nArray, nArray2, nArray3)) {
            throw new ArithmeticException("Inverse does not exist.");
        }
    }

    public static void checkedModOddInverseVar(int[] nArray, int[] nArray2, int[] nArray3) {
        if (!Mod.modOddInverseVar(nArray, nArray2, nArray3)) {
            throw new ArithmeticException("Inverse does not exist.");
        }
    }

    public static int inverse32(int n2) {
        int n3 = n2;
        n3 *= 2 - n2 * n3;
        n3 *= 2 - n2 * n3;
        n3 *= 2 - n2 * n3;
        n3 *= 2 - n2 * n3;
        return n3;
    }

    public static int modOddInverse(int[] nArray, int[] nArray2, int[] nArray3) {
        int n2;
        int n3 = nArray.length;
        int n4 = (n3 << 5) - Integers.numberOfLeadingZeros(nArray[n3 - 1]);
        int n5 = (n4 + 29) / 30;
        int[] nArray4 = new int[4];
        int[] nArray5 = new int[n5];
        int[] nArray6 = new int[n5];
        int[] nArray7 = new int[n5];
        int[] nArray8 = new int[n5];
        int[] nArray9 = new int[n5];
        nArray6[0] = 1;
        Mod.encode30(n4, nArray2, nArray8);
        Mod.encode30(n4, nArray, nArray9);
        System.arraycopy(nArray9, 0, nArray7, 0, n5);
        int n6 = 0;
        int n7 = Mod.inverse32(nArray9[0]);
        int n8 = Mod.getMaximumHDDivsteps(n4);
        for (n2 = 0; n2 < n8; n2 += 30) {
            n6 = Mod.hddivsteps30(n6, nArray7[0], nArray8[0], nArray4);
            Mod.updateDE30(n5, nArray5, nArray6, nArray4, n7, nArray9);
            Mod.updateFG30(n5, nArray7, nArray8, nArray4);
        }
        n2 = nArray7[n5 - 1] >> 31;
        Mod.cnegate30(n5, n2, nArray7);
        Mod.cnormalize30(n5, n2, nArray5, nArray9);
        Mod.decode30(n4, nArray5, nArray3);
        return Mod.equalTo(n5, nArray7, 1) & Mod.equalTo(n5, nArray8, 0);
    }

    public static boolean modOddInverseVar(int[] nArray, int[] nArray2, int[] nArray3) {
        int n2 = nArray.length;
        int n3 = (n2 << 5) - Integers.numberOfLeadingZeros(nArray[n2 - 1]);
        int n4 = (n3 + 29) / 30;
        int n5 = n3 - Nat.getBitLength(n2, nArray2);
        int[] nArray4 = new int[4];
        int[] nArray5 = new int[n4];
        int[] nArray6 = new int[n4];
        int[] nArray7 = new int[n4];
        int[] nArray8 = new int[n4];
        int[] nArray9 = new int[n4];
        nArray6[0] = 1;
        Mod.encode30(n3, nArray2, nArray8);
        Mod.encode30(n3, nArray, nArray9);
        System.arraycopy(nArray9, 0, nArray7, 0, n4);
        int n6 = -n5;
        int n7 = n4;
        int n8 = n4;
        int n9 = Mod.inverse32(nArray9[0]);
        int n10 = Mod.getMaximumDivsteps(n3);
        int n11 = n5;
        while (!Mod.equalToVar(n8, nArray8, 0)) {
            if (n11 >= n10) {
                return false;
            }
            n11 += 30;
            n6 = Mod.divsteps30Var(n6, nArray7[0], nArray8[0], nArray4);
            Mod.updateDE30(n7, nArray5, nArray6, nArray4, n9, nArray9);
            Mod.updateFG30(n8, nArray7, nArray8, nArray4);
            n8 = Mod.trimFG30(n8, nArray7, nArray8);
        }
        int n12 = nArray7[n8 - 1] >> 31;
        int n13 = nArray5[n7 - 1] >> 31;
        if (n13 < 0) {
            n13 = Mod.add30(n7, nArray5, nArray9);
        }
        if (n12 < 0) {
            n13 = Mod.negate30(n7, nArray5);
            n12 = Mod.negate30(n8, nArray7);
        }
        if (!Mod.equalToVar(n8, nArray7, 1)) {
            return false;
        }
        if (n13 < 0) {
            n13 = Mod.add30(n7, nArray5, nArray9);
        }
        Mod.decode30(n3, nArray5, nArray3);
        return true;
    }

    public static int modOddIsCoprime(int[] nArray, int[] nArray2) {
        int n2;
        int n3 = nArray.length;
        int n4 = (n3 << 5) - Integers.numberOfLeadingZeros(nArray[n3 - 1]);
        int n5 = (n4 + 29) / 30;
        int[] nArray3 = new int[4];
        int[] nArray4 = new int[n5];
        int[] nArray5 = new int[n5];
        int[] nArray6 = new int[n5];
        Mod.encode30(n4, nArray2, nArray5);
        Mod.encode30(n4, nArray, nArray6);
        System.arraycopy(nArray6, 0, nArray4, 0, n5);
        int n6 = 0;
        int n7 = Mod.getMaximumHDDivsteps(n4);
        for (n2 = 0; n2 < n7; n2 += 30) {
            n6 = Mod.hddivsteps30(n6, nArray4[0], nArray5[0], nArray3);
            Mod.updateFG30(n5, nArray4, nArray5, nArray3);
        }
        n2 = nArray4[n5 - 1] >> 31;
        Mod.cnegate30(n5, n2, nArray4);
        return Mod.equalTo(n5, nArray4, 1) & Mod.equalTo(n5, nArray5, 0);
    }

    public static boolean modOddIsCoprimeVar(int[] nArray, int[] nArray2) {
        int n2 = nArray.length;
        int n3 = (n2 << 5) - Integers.numberOfLeadingZeros(nArray[n2 - 1]);
        int n4 = (n3 + 29) / 30;
        int n5 = n3 - Nat.getBitLength(n2, nArray2);
        int[] nArray3 = new int[4];
        int[] nArray4 = new int[n4];
        int[] nArray5 = new int[n4];
        int[] nArray6 = new int[n4];
        Mod.encode30(n3, nArray2, nArray5);
        Mod.encode30(n3, nArray, nArray6);
        System.arraycopy(nArray6, 0, nArray4, 0, n4);
        int n6 = -n5;
        int n7 = n4;
        int n8 = Mod.getMaximumDivsteps(n3);
        int n9 = n5;
        while (!Mod.equalToVar(n7, nArray5, 0)) {
            if (n9 >= n8) {
                return false;
            }
            n9 += 30;
            n6 = Mod.divsteps30Var(n6, nArray4[0], nArray5[0], nArray3);
            Mod.updateFG30(n7, nArray4, nArray5, nArray3);
            n7 = Mod.trimFG30(n7, nArray4, nArray5);
        }
        int n10 = nArray4[n7 - 1] >> 31;
        if (n10 < 0) {
            n10 = Mod.negate30(n7, nArray4);
        }
        return Mod.equalToVar(n7, nArray4, 1);
    }

    public static int[] random(int[] nArray) {
        int n2 = nArray.length;
        Random random = new Random();
        int[] nArray2 = Nat.create(n2);
        int n3 = nArray[n2 - 1];
        n3 |= n3 >>> 1;
        n3 |= n3 >>> 2;
        n3 |= n3 >>> 4;
        n3 |= n3 >>> 8;
        n3 |= n3 >>> 16;
        do {
            for (int i2 = 0; i2 != n2; ++i2) {
                nArray2[i2] = random.nextInt();
            }
            int n4 = n2 - 1;
            nArray2[n4] = nArray2[n4] & n3;
        } while (Nat.gte(n2, nArray2, nArray));
        return nArray2;
    }

    private static int add30(int n2, int[] nArray, int[] nArray2) {
        int n3 = 0;
        int n4 = n2 - 1;
        for (int i2 = 0; i2 < n4; ++i2) {
            nArray[i2] = (n3 += nArray[i2] + nArray2[i2]) & 0x3FFFFFFF;
            n3 >>= 30;
        }
        nArray[n4] = n3 += nArray[n4] + nArray2[n4];
        return n3 >>= 30;
    }

    private static void cnegate30(int n2, int n3, int[] nArray) {
        int n4 = 0;
        int n5 = n2 - 1;
        for (int i2 = 0; i2 < n5; ++i2) {
            nArray[i2] = (n4 += (nArray[i2] ^ n3) - n3) & 0x3FFFFFFF;
            n4 >>= 30;
        }
        nArray[n5] = n4 += (nArray[n5] ^ n3) - n3;
    }

    private static void cnormalize30(int n2, int n3, int[] nArray, int[] nArray2) {
        int n4;
        int n5;
        int n6 = n2 - 1;
        int n7 = 0;
        int n8 = nArray[n6] >> 31;
        for (n5 = 0; n5 < n6; ++n5) {
            n4 = nArray[n5] + (nArray2[n5] & n8);
            n4 = (n4 ^ n3) - n3;
            nArray[n5] = (n7 += n4) & 0x3FFFFFFF;
            n7 >>= 30;
        }
        n5 = nArray[n6] + (nArray2[n6] & n8);
        n5 = (n5 ^ n3) - n3;
        nArray[n6] = n7 += n5;
        n7 = 0;
        n8 = nArray[n6] >> 31;
        for (n5 = 0; n5 < n6; ++n5) {
            n4 = nArray[n5] + (nArray2[n5] & n8);
            nArray[n5] = (n7 += n4) & 0x3FFFFFFF;
            n7 >>= 30;
        }
        n5 = nArray[n6] + (nArray2[n6] & n8);
        nArray[n6] = n7 += n5;
    }

    private static void decode30(int n2, int[] nArray, int[] nArray2) {
        int n3 = 0;
        long l2 = 0L;
        int n4 = 0;
        int n5 = 0;
        while (n2 > 0) {
            while (n3 < Math.min(32, n2)) {
                l2 |= (long)nArray[n4++] << n3;
                n3 += 30;
            }
            nArray2[n5++] = (int)l2;
            l2 >>>= 32;
            n3 -= 32;
            n2 -= 32;
        }
    }

    private static int divsteps30Var(int n2, int n3, int n4, int[] nArray) {
        int n5 = 1;
        int n6 = 0;
        int n7 = 0;
        int n8 = 1;
        int n9 = n3;
        int n10 = n4;
        int n11 = 30;
        while (true) {
            int n12;
            int n13;
            int n14;
            int n15 = Integers.numberOfTrailingZeros(n10 | -1 << n11);
            n10 >>= n15;
            n5 <<= n15;
            n6 <<= n15;
            n2 -= n15;
            if ((n11 -= n15) <= 0) break;
            if (n2 <= 0) {
                n2 = 2 - n2;
                int n16 = n9;
                n9 = n10;
                n10 = -n16;
                int n17 = n5;
                n5 = n7;
                n7 = -n17;
                int n18 = n6;
                n6 = n8;
                n8 = -n18;
                n14 = n2 > n11 ? n11 : n2;
                n13 = -1 >>> 32 - n14 & 0x3F;
                n12 = n9 * n10 * (n9 * n9 - 2) & n13;
            } else {
                n14 = n2 > n11 ? n11 : n2;
                n13 = -1 >>> 32 - n14 & 0xF;
                n12 = n9 + ((n9 + 1 & 4) << 1);
                n12 = n12 * -n10 & n13;
            }
            n10 += n9 * n12;
            n7 += n5 * n12;
            n8 += n6 * n12;
        }
        nArray[0] = n5;
        nArray[1] = n6;
        nArray[2] = n7;
        nArray[3] = n8;
        return n2;
    }

    private static void encode30(int n2, int[] nArray, int[] nArray2) {
        int n3 = 0;
        long l2 = 0L;
        int n4 = 0;
        int n5 = 0;
        while (n2 > 0) {
            if (n3 < Math.min(30, n2)) {
                l2 |= ((long)nArray[n4++] & 0xFFFFFFFFL) << n3;
                n3 += 32;
            }
            nArray2[n5++] = (int)l2 & 0x3FFFFFFF;
            l2 >>>= 30;
            n3 -= 30;
            n2 -= 30;
        }
    }

    private static int equalTo(int n2, int[] nArray, int n3) {
        int n4 = nArray[0] ^ n3;
        for (int i2 = 1; i2 < n2; ++i2) {
            n4 |= nArray[i2];
        }
        n4 = n4 >>> 1 | n4 & 1;
        return n4 - 1 >> 31;
    }

    private static boolean equalToVar(int n2, int[] nArray, int n3) {
        int n4 = nArray[0] ^ n3;
        if (n4 != 0) {
            return false;
        }
        for (int i2 = 1; i2 < n2; ++i2) {
            n4 |= nArray[i2];
        }
        return n4 == 0;
    }

    private static int getMaximumDivsteps(int n2) {
        return (int)(188898L * (long)n2 + (long)(n2 < 46 ? 308405 : 181188) >>> 16);
    }

    private static int getMaximumHDDivsteps(int n2) {
        return (int)(150964L * (long)n2 + 99243L >>> 16);
    }

    private static int hddivsteps30(int n2, int n3, int n4, int[] nArray) {
        int n5 = 0x40000000;
        int n6 = 0;
        int n7 = 0;
        int n8 = 0x40000000;
        int n9 = n3;
        int n10 = n4;
        for (int i2 = 0; i2 < 30; ++i2) {
            int n11 = n2 >> 31;
            int n12 = -(n10 & 1);
            int n13 = n9 ^ n11;
            int n14 = n5 ^ n11;
            int n15 = n6 ^ n11;
            int n16 = n12 & ~n11;
            n2 = (n2 ^ n16) + 1;
            n9 += (n10 -= n13 & n12) & n16;
            n5 += (n7 -= n14 & n12) & n16;
            n6 += (n8 -= n15 & n12) & n16;
            n10 >>= 1;
            n7 >>= 1;
            n8 >>= 1;
        }
        nArray[0] = n5;
        nArray[1] = n6;
        nArray[2] = n7;
        nArray[3] = n8;
        return n2;
    }

    private static int negate30(int n2, int[] nArray) {
        int n3 = 0;
        int n4 = n2 - 1;
        for (int i2 = 0; i2 < n4; ++i2) {
            nArray[i2] = (n3 -= nArray[i2]) & 0x3FFFFFFF;
            n3 >>= 30;
        }
        nArray[n4] = n3 -= nArray[n4];
        return n3 >>= 30;
    }

    private static int trimFG30(int n2, int[] nArray, int[] nArray2) {
        int n3 = nArray[n2 - 1];
        int n4 = nArray2[n2 - 1];
        int n5 = n2 - 2 >> 31;
        n5 |= n3 ^ n3 >> 31;
        if ((n5 |= n4 ^ n4 >> 31) == 0) {
            int n6 = n2 - 2;
            nArray[n6] = nArray[n6] | n3 << 30;
            int n7 = n2 - 2;
            nArray2[n7] = nArray2[n7] | n4 << 30;
            --n2;
        }
        return n2;
    }

    private static void updateDE30(int n2, int[] nArray, int[] nArray2, int[] nArray3, int n3, int[] nArray4) {
        int n4 = nArray3[0];
        int n5 = nArray3[1];
        int n6 = nArray3[2];
        int n7 = nArray3[3];
        int n8 = nArray[n2 - 1] >> 31;
        int n9 = nArray2[n2 - 1] >> 31;
        int n10 = (n4 & n8) + (n5 & n9);
        int n11 = (n6 & n8) + (n7 & n9);
        int n12 = nArray4[0];
        int n13 = nArray[0];
        int n14 = nArray2[0];
        long l2 = (long)n4 * (long)n13 + (long)n5 * (long)n14;
        long l3 = (long)n6 * (long)n13 + (long)n7 * (long)n14;
        n10 -= n3 * (int)l2 + n10 & 0x3FFFFFFF;
        n11 -= n3 * (int)l3 + n11 & 0x3FFFFFFF;
        l2 += (long)n12 * (long)n10;
        l3 += (long)n12 * (long)n11;
        l2 >>= 30;
        l3 >>= 30;
        for (int i2 = 1; i2 < n2; ++i2) {
            n12 = nArray4[i2];
            n13 = nArray[i2];
            n14 = nArray2[i2];
            nArray[i2 - 1] = (int)(l2 += (long)n4 * (long)n13 + (long)n5 * (long)n14 + (long)n12 * (long)n10) & 0x3FFFFFFF;
            l2 >>= 30;
            nArray2[i2 - 1] = (int)(l3 += (long)n6 * (long)n13 + (long)n7 * (long)n14 + (long)n12 * (long)n11) & 0x3FFFFFFF;
            l3 >>= 30;
        }
        nArray[n2 - 1] = (int)l2;
        nArray2[n2 - 1] = (int)l3;
    }

    private static void updateFG30(int n2, int[] nArray, int[] nArray2, int[] nArray3) {
        int n3 = nArray3[0];
        int n4 = nArray3[1];
        int n5 = nArray3[2];
        int n6 = nArray3[3];
        int n7 = nArray[0];
        int n8 = nArray2[0];
        long l2 = (long)n3 * (long)n7 + (long)n4 * (long)n8;
        long l3 = (long)n5 * (long)n7 + (long)n6 * (long)n8;
        l2 >>= 30;
        l3 >>= 30;
        for (int i2 = 1; i2 < n2; ++i2) {
            n7 = nArray[i2];
            n8 = nArray2[i2];
            nArray[i2 - 1] = (int)(l2 += (long)n3 * (long)n7 + (long)n4 * (long)n8) & 0x3FFFFFFF;
            l2 >>= 30;
            nArray2[i2 - 1] = (int)(l3 += (long)n5 * (long)n7 + (long)n6 * (long)n8) & 0x3FFFFFFF;
            l3 >>= 30;
        }
        nArray[n2 - 1] = (int)l2;
        nArray2[n2 - 1] = (int)l3;
    }
}

