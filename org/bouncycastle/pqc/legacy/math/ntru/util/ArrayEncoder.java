/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.legacy.math.ntru.util;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import org.bouncycastle.pqc.legacy.math.ntru.util.Util;
import org.bouncycastle.util.Arrays;

public class ArrayEncoder {
    private static final int[] COEFF1_TABLE = new int[]{0, 0, 0, 1, 1, 1, -1, -1};
    private static final int[] COEFF2_TABLE = new int[]{0, 1, -1, 0, 1, -1, 0, 1};
    private static final int[] BIT1_TABLE = new int[]{1, 1, 1, 0, 0, 0, 1, 0, 1};
    private static final int[] BIT2_TABLE = new int[]{1, 1, 1, 1, 0, 0, 0, 1, 0};
    private static final int[] BIT3_TABLE = new int[]{1, 0, 1, 0, 0, 1, 1, 1, 0};

    public static byte[] encodeModQ(int[] nArray, int n2) {
        int n3 = 31 - Integer.numberOfLeadingZeros(n2);
        int n4 = nArray.length * n3;
        int n5 = (n4 + 7) / 8;
        byte[] byArray = new byte[n5];
        int n6 = 0;
        int n7 = 0;
        for (int i2 = 0; i2 < nArray.length; ++i2) {
            for (int i3 = 0; i3 < n3; ++i3) {
                int n8 = nArray[i2] >> i3 & 1;
                int n9 = n7++;
                byArray[n9] = (byte)(byArray[n9] | n8 << n6);
                if (n6 == 7) {
                    n6 = 0;
                    continue;
                }
                ++n6;
            }
        }
        return byArray;
    }

    public static int[] decodeModQ(byte[] byArray, int n2, int n3) {
        int[] nArray = new int[n2];
        int n4 = 31 - Integer.numberOfLeadingZeros(n3);
        int n5 = n2 * n4;
        int n6 = 0;
        for (int i2 = 0; i2 < n5; ++i2) {
            if (i2 <= 0 || i2 % n4 == 0) {
                // empty if block
            }
            int n7 = ArrayEncoder.getBit(byArray, i2);
            int n8 = ++n6;
            nArray[n8] = nArray[n8] + (n7 << i2 % n4);
        }
        return nArray;
    }

    public static int[] decodeModQ(InputStream inputStream2, int n2, int n3) throws IOException {
        int n4 = 31 - Integer.numberOfLeadingZeros(n3);
        int n5 = (n2 * n4 + 7) / 8;
        byte[] byArray = Util.readFullLength(inputStream2, n5);
        return ArrayEncoder.decodeModQ(byArray, n2, n3);
    }

    public static int[] decodeMod3Sves(byte[] byArray, int n2) {
        int[] nArray = new int[n2];
        int n3 = 0;
        int n4 = 0;
        while (n4 < byArray.length * 8) {
            int n5 = ArrayEncoder.getBit(byArray, n4++);
            int n6 = ArrayEncoder.getBit(byArray, n4++);
            int n7 = ArrayEncoder.getBit(byArray, n4++);
            int n8 = n5 * 4 + n6 * 2 + n7;
            nArray[n3++] = COEFF1_TABLE[n8];
            nArray[n3++] = COEFF2_TABLE[n8];
            if (n3 <= n2 - 2) continue;
            break;
        }
        return nArray;
    }

    public static byte[] encodeMod3Sves(int[] nArray) {
        int n2 = (nArray.length * 3 + 1) / 2;
        int n3 = (n2 + 7) / 8;
        byte[] byArray = new byte[n3];
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        while (n6 < nArray.length / 2 * 2) {
            int n7 = nArray[n6++] + 1;
            int n8 = nArray[n6++] + 1;
            if (n7 == 0 && n8 == 0) {
                throw new IllegalStateException("Illegal encoding!");
            }
            int n9 = n7 * 3 + n8;
            int[] nArray2 = new int[]{BIT1_TABLE[n9], BIT2_TABLE[n9], BIT3_TABLE[n9]};
            for (int i2 = 0; i2 < 3; ++i2) {
                int n10 = n5++;
                byArray[n10] = (byte)(byArray[n10] | nArray2[i2] << n4);
                if (n4 == 7) {
                    n4 = 0;
                    continue;
                }
                ++n4;
            }
        }
        return byArray;
    }

    public static byte[] encodeMod3Tight(int[] nArray) {
        int n2;
        BigInteger bigInteger = BigInteger.ZERO;
        for (n2 = nArray.length - 1; n2 >= 0; --n2) {
            bigInteger = bigInteger.multiply(BigInteger.valueOf(3L));
            bigInteger = bigInteger.add(BigInteger.valueOf(nArray[n2] + 1));
        }
        n2 = (BigInteger.valueOf(3L).pow(nArray.length).bitLength() + 7) / 8;
        byte[] byArray = bigInteger.toByteArray();
        if (byArray.length < n2) {
            byte[] byArray2 = new byte[n2];
            System.arraycopy(byArray, 0, byArray2, n2 - byArray.length, byArray.length);
            return byArray2;
        }
        if (byArray.length > n2) {
            byArray = Arrays.copyOfRange(byArray, 1, byArray.length);
        }
        return byArray;
    }

    public static int[] decodeMod3Tight(byte[] byArray, int n2) {
        BigInteger bigInteger = new BigInteger(1, byArray);
        int[] nArray = new int[n2];
        for (int i2 = 0; i2 < n2; ++i2) {
            nArray[i2] = bigInteger.mod(BigInteger.valueOf(3L)).intValue() - 1;
            if (nArray[i2] > 1) {
                int n3 = i2;
                nArray[n3] = nArray[n3] - 3;
            }
            bigInteger = bigInteger.divide(BigInteger.valueOf(3L));
        }
        return nArray;
    }

    public static int[] decodeMod3Tight(InputStream inputStream2, int n2) throws IOException {
        int n3 = (int)Math.ceil((double)n2 * Math.log(3.0) / Math.log(2.0) / 8.0);
        byte[] byArray = Util.readFullLength(inputStream2, n3);
        return ArrayEncoder.decodeMod3Tight(byArray, n2);
    }

    private static int getBit(byte[] byArray, int n2) {
        int n3 = n2 / 8;
        int n4 = byArray[n3] & 0xFF;
        return n4 >> n2 % 8 & 1;
    }
}

