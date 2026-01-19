/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.hqc;

import org.bouncycastle.util.Pack;

class Utils {
    Utils() {
    }

    static void resizeArray(long[] lArray, int n2, long[] lArray2, int n3, int n4, int n5) {
        long l2 = Long.MAX_VALUE;
        int n6 = 0;
        if (n2 < n3) {
            if (n2 % 64 != 0) {
                n6 = 64 - n2 % 64;
            }
            System.arraycopy(lArray2, 0, lArray, 0, n4);
            for (int i2 = 0; i2 < n6; ++i2) {
                int n7 = n5 - 1;
                lArray[n7] = lArray[n7] & l2 >> i2;
            }
        } else {
            System.arraycopy(lArray2, 0, lArray, 0, (n3 + 7) / 8);
        }
    }

    static void fromByte16ArrayToLongArray(long[] lArray, int[] nArray) {
        for (int i2 = 0; i2 != nArray.length; i2 += 4) {
            lArray[i2 / 4] = (long)nArray[i2] & 0xFFFFL;
            int n2 = i2 / 4;
            lArray[n2] = lArray[n2] | (long)nArray[i2 + 1] << 16;
            int n3 = i2 / 4;
            lArray[n3] = lArray[n3] | (long)nArray[i2 + 2] << 32;
            int n4 = i2 / 4;
            lArray[n4] = lArray[n4] | (long)nArray[i2 + 3] << 48;
        }
    }

    static void fromByteArrayToByte16Array(int[] nArray, byte[] byArray) {
        byte[] byArray2 = byArray;
        if (byArray.length % 2 != 0) {
            byArray2 = new byte[(byArray.length + 1) / 2 * 2];
            System.arraycopy(byArray, 0, byArray2, 0, byArray.length);
        }
        int n2 = 0;
        for (int i2 = 0; i2 < nArray.length; ++i2) {
            nArray[i2] = Pack.littleEndianToShort(byArray2, n2) & 0xFFFF;
            n2 += 2;
        }
    }

    static void fromLongArrayToByteArray(byte[] byArray, long[] lArray) {
        int n2;
        int n3 = byArray.length / 8;
        for (n2 = 0; n2 != n3; ++n2) {
            Pack.longToLittleEndian(lArray[n2], byArray, n2 * 8);
        }
        if (byArray.length % 8 != 0) {
            n2 = n3 * 8;
            int n4 = 0;
            while (n2 < byArray.length) {
                byArray[n2++] = (byte)(lArray[n3] >>> n4++ * 8);
            }
        }
    }

    static long bitMask(long l2, long l3) {
        return (1L << (int)(l2 % l3)) - 1L;
    }

    static void fromByteArrayToLongArray(long[] lArray, byte[] byArray) {
        byte[] byArray2 = byArray;
        if (byArray.length % 8 != 0) {
            byArray2 = new byte[(byArray.length + 7) / 8 * 8];
            System.arraycopy(byArray, 0, byArray2, 0, byArray.length);
        }
        int n2 = 0;
        for (int i2 = 0; i2 < lArray.length; ++i2) {
            lArray[i2] = Pack.littleEndianToLong(byArray2, n2);
            n2 += 8;
        }
    }

    static void fromByte32ArrayToLongArray(long[] lArray, int[] nArray) {
        for (int i2 = 0; i2 != nArray.length; i2 += 2) {
            lArray[i2 / 2] = (long)nArray[i2] & 0xFFFFFFFFL;
            int n2 = i2 / 2;
            lArray[n2] = lArray[n2] | (long)nArray[i2 + 1] << 32;
        }
    }

    static void fromLongArrayToByte32Array(int[] nArray, long[] lArray) {
        for (int i2 = 0; i2 != lArray.length; ++i2) {
            nArray[2 * i2] = (int)lArray[i2];
            nArray[2 * i2 + 1] = (int)(lArray[i2] >> 32);
        }
    }

    static void copyBytes(int[] nArray, int n2, int[] nArray2, int n3, int n4) {
        System.arraycopy(nArray, n2, nArray2, n3, n4 / 2);
    }

    static int getByteSizeFromBitSize(int n2) {
        return (n2 + 7) / 8;
    }

    static int getByte64SizeFromBitSize(int n2) {
        return (n2 + 63) / 64;
    }

    static int toUnsigned8bits(int n2) {
        return n2 & 0xFF;
    }

    static int toUnsigned16Bits(int n2) {
        return n2 & 0xFFFF;
    }

    static void xorLongToByte16Array(int[] nArray, long l2, int n2) {
        int n3 = n2 + 0;
        nArray[n3] = nArray[n3] ^ (int)l2 & 0xFFFF;
        int n4 = n2 + 1;
        nArray[n4] = nArray[n4] ^ (int)(l2 >>> 16) & 0xFFFF;
        int n5 = n2 + 2;
        nArray[n5] = nArray[n5] ^ (int)(l2 >>> 32) & 0xFFFF;
        int n6 = n2 + 3;
        nArray[n6] = nArray[n6] ^ (int)(l2 >>> 48) & 0xFFFF;
    }
}

