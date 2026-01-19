/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.fpe;

import java.math.BigInteger;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.util.RadixConverter;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.BigIntegers;
import org.bouncycastle.util.Bytes;
import org.bouncycastle.util.Integers;
import org.bouncycastle.util.Pack;

class SP80038G {
    static final String FPE_DISABLED = "org.bouncycastle.fpe.disable";
    static final String FF1_DISABLED = "org.bouncycastle.fpe.disable_ff1";
    protected static final int BLOCK_SIZE = 16;
    protected static final double LOG2 = Math.log(2.0);
    protected static final double TWO_TO_96 = Math.pow(2.0, 96.0);

    SP80038G() {
    }

    static byte[] decryptFF1(BlockCipher blockCipher, RadixConverter radixConverter, byte[] byArray, byte[] byArray2, int n2, int n3) {
        SP80038G.checkArgs(blockCipher, true, radixConverter.getRadix(), byArray2, n2, n3);
        int n4 = n3;
        int n5 = n4 / 2;
        int n6 = n4 - n5;
        short[] sArray = SP80038G.toShort(byArray2, n2, n5);
        short[] sArray2 = SP80038G.toShort(byArray2, n2 + n5, n6);
        short[] sArray3 = SP80038G.decFF1(blockCipher, radixConverter, byArray, n4, n5, n6, sArray, sArray2);
        return SP80038G.toByte(sArray3);
    }

    static short[] decryptFF1w(BlockCipher blockCipher, RadixConverter radixConverter, byte[] byArray, short[] sArray, int n2, int n3) {
        SP80038G.checkArgs(blockCipher, true, radixConverter.getRadix(), sArray, n2, n3);
        int n4 = n3;
        int n5 = n4 / 2;
        int n6 = n4 - n5;
        short[] sArray2 = new short[n5];
        short[] sArray3 = new short[n6];
        System.arraycopy(sArray, n2, sArray2, 0, n5);
        System.arraycopy(sArray, n2 + n5, sArray3, 0, n6);
        return SP80038G.decFF1(blockCipher, radixConverter, byArray, n4, n5, n6, sArray2, sArray3);
    }

    static short[] decFF1(BlockCipher blockCipher, RadixConverter radixConverter, byte[] byArray, int n2, int n3, int n4, short[] sArray, short[] sArray2) {
        int n5 = radixConverter.getRadix();
        int n6 = byArray.length;
        int n7 = SP80038G.calculateB_FF1(n5, n4);
        int n8 = n7 + 7 & 0xFFFFFFFC;
        byte[] byArray2 = SP80038G.calculateP_FF1(n5, (byte)n3, n2, n6);
        BigInteger bigInteger = BigInteger.valueOf(n5);
        BigInteger[] bigIntegerArray = SP80038G.calculateModUV(bigInteger, n3, n4);
        int n9 = n3;
        for (int i2 = 9; i2 >= 0; --i2) {
            BigInteger bigInteger2 = SP80038G.calculateY_FF1(blockCipher, byArray, n7, n8, i2, byArray2, sArray, radixConverter);
            n9 = n2 - n9;
            BigInteger bigInteger3 = bigIntegerArray[i2 & 1];
            BigInteger bigInteger4 = radixConverter.fromEncoding(sArray2).subtract(bigInteger2).mod(bigInteger3);
            short[] sArray3 = sArray2;
            sArray2 = sArray;
            sArray = sArray3;
            radixConverter.toEncoding(bigInteger4, n9, sArray3);
        }
        return Arrays.concatenate(sArray, sArray2);
    }

    static byte[] decryptFF3(BlockCipher blockCipher, RadixConverter radixConverter, byte[] byArray, byte[] byArray2, int n2, int n3) {
        SP80038G.checkArgs(blockCipher, false, radixConverter.getRadix(), byArray2, n2, n3);
        if (byArray.length != 8) {
            throw new IllegalArgumentException();
        }
        return SP80038G.implDecryptFF3(blockCipher, radixConverter, byArray, byArray2, n2, n3);
    }

    static byte[] decryptFF3_1(BlockCipher blockCipher, RadixConverter radixConverter, byte[] byArray, byte[] byArray2, int n2, int n3) {
        SP80038G.checkArgs(blockCipher, false, radixConverter.getRadix(), byArray2, n2, n3);
        if (byArray.length != 7) {
            throw new IllegalArgumentException("tweak should be 56 bits");
        }
        byte[] byArray3 = SP80038G.calculateTweak64_FF3_1(byArray);
        return SP80038G.implDecryptFF3(blockCipher, radixConverter, byArray3, byArray2, n2, n3);
    }

    static short[] decryptFF3_1w(BlockCipher blockCipher, RadixConverter radixConverter, byte[] byArray, short[] sArray, int n2, int n3) {
        SP80038G.checkArgs(blockCipher, false, radixConverter.getRadix(), sArray, n2, n3);
        if (byArray.length != 7) {
            throw new IllegalArgumentException("tweak should be 56 bits");
        }
        byte[] byArray2 = SP80038G.calculateTweak64_FF3_1(byArray);
        return SP80038G.implDecryptFF3w(blockCipher, radixConverter, byArray2, sArray, n2, n3);
    }

    static byte[] encryptFF1(BlockCipher blockCipher, RadixConverter radixConverter, byte[] byArray, byte[] byArray2, int n2, int n3) {
        SP80038G.checkArgs(blockCipher, true, radixConverter.getRadix(), byArray2, n2, n3);
        int n4 = n3;
        int n5 = n4 / 2;
        int n6 = n4 - n5;
        short[] sArray = SP80038G.toShort(byArray2, n2, n5);
        short[] sArray2 = SP80038G.toShort(byArray2, n2 + n5, n6);
        return SP80038G.toByte(SP80038G.encFF1(blockCipher, radixConverter, byArray, n4, n5, n6, sArray, sArray2));
    }

    static short[] encryptFF1w(BlockCipher blockCipher, RadixConverter radixConverter, byte[] byArray, short[] sArray, int n2, int n3) {
        SP80038G.checkArgs(blockCipher, true, radixConverter.getRadix(), sArray, n2, n3);
        int n4 = n3;
        int n5 = n4 / 2;
        int n6 = n4 - n5;
        short[] sArray2 = new short[n5];
        short[] sArray3 = new short[n6];
        System.arraycopy(sArray, n2, sArray2, 0, n5);
        System.arraycopy(sArray, n2 + n5, sArray3, 0, n6);
        return SP80038G.encFF1(blockCipher, radixConverter, byArray, n4, n5, n6, sArray2, sArray3);
    }

    private static short[] encFF1(BlockCipher blockCipher, RadixConverter radixConverter, byte[] byArray, int n2, int n3, int n4, short[] sArray, short[] sArray2) {
        int n5 = radixConverter.getRadix();
        int n6 = byArray.length;
        int n7 = SP80038G.calculateB_FF1(n5, n4);
        int n8 = n7 + 7 & 0xFFFFFFFC;
        byte[] byArray2 = SP80038G.calculateP_FF1(n5, (byte)n3, n2, n6);
        BigInteger bigInteger = BigInteger.valueOf(n5);
        BigInteger[] bigIntegerArray = SP80038G.calculateModUV(bigInteger, n3, n4);
        int n9 = n4;
        for (int i2 = 0; i2 < 10; ++i2) {
            BigInteger bigInteger2 = SP80038G.calculateY_FF1(blockCipher, byArray, n7, n8, i2, byArray2, sArray2, radixConverter);
            n9 = n2 - n9;
            BigInteger bigInteger3 = bigIntegerArray[i2 & 1];
            BigInteger bigInteger4 = radixConverter.fromEncoding(sArray);
            BigInteger bigInteger5 = bigInteger4.add(bigInteger2).mod(bigInteger3);
            short[] sArray3 = sArray;
            sArray = sArray2;
            sArray2 = sArray3;
            radixConverter.toEncoding(bigInteger5, n9, sArray3);
        }
        return Arrays.concatenate(sArray, sArray2);
    }

    static byte[] encryptFF3(BlockCipher blockCipher, RadixConverter radixConverter, byte[] byArray, byte[] byArray2, int n2, int n3) {
        SP80038G.checkArgs(blockCipher, false, radixConverter.getRadix(), byArray2, n2, n3);
        if (byArray.length != 8) {
            throw new IllegalArgumentException();
        }
        return SP80038G.implEncryptFF3(blockCipher, radixConverter, byArray, byArray2, n2, n3);
    }

    static short[] encryptFF3w(BlockCipher blockCipher, RadixConverter radixConverter, byte[] byArray, short[] sArray, int n2, int n3) {
        SP80038G.checkArgs(blockCipher, false, radixConverter.getRadix(), sArray, n2, n3);
        if (byArray.length != 8) {
            throw new IllegalArgumentException();
        }
        return SP80038G.implEncryptFF3w(blockCipher, radixConverter, byArray, sArray, n2, n3);
    }

    static short[] encryptFF3_1w(BlockCipher blockCipher, RadixConverter radixConverter, byte[] byArray, short[] sArray, int n2, int n3) {
        SP80038G.checkArgs(blockCipher, false, radixConverter.getRadix(), sArray, n2, n3);
        if (byArray.length != 7) {
            throw new IllegalArgumentException("tweak should be 56 bits");
        }
        byte[] byArray2 = SP80038G.calculateTweak64_FF3_1(byArray);
        return SP80038G.encryptFF3w(blockCipher, radixConverter, byArray2, sArray, n2, n3);
    }

    static byte[] encryptFF3_1(BlockCipher blockCipher, RadixConverter radixConverter, byte[] byArray, byte[] byArray2, int n2, int n3) {
        SP80038G.checkArgs(blockCipher, false, radixConverter.getRadix(), byArray2, n2, n3);
        if (byArray.length != 7) {
            throw new IllegalArgumentException("tweak should be 56 bits");
        }
        byte[] byArray3 = SP80038G.calculateTweak64_FF3_1(byArray);
        return SP80038G.encryptFF3(blockCipher, radixConverter, byArray3, byArray2, n2, n3);
    }

    protected static int calculateB_FF1(int n2, int n3) {
        int n4 = Integers.numberOfTrailingZeros(n2);
        int n5 = n4 * n3;
        int n6 = n2 >>> n4;
        if (n6 != 1) {
            n5 += BigInteger.valueOf(n6).pow(n3).bitLength();
        }
        return (n5 + 7) / 8;
    }

    protected static BigInteger[] calculateModUV(BigInteger bigInteger, int n2, int n3) {
        BigInteger[] bigIntegerArray;
        bigIntegerArray = new BigInteger[]{bigInteger.pow(n2), bigIntegerArray[0]};
        if (n3 != n2) {
            bigIntegerArray[1] = bigIntegerArray[1].multiply(bigInteger);
        }
        return bigIntegerArray;
    }

    protected static byte[] calculateP_FF1(int n2, byte by, int n3, int n4) {
        byte[] byArray = new byte[16];
        byArray[0] = 1;
        byArray[1] = 2;
        byArray[2] = 1;
        byArray[3] = 0;
        byArray[4] = (byte)(n2 >> 8);
        byArray[5] = (byte)n2;
        byArray[6] = 10;
        byArray[7] = by;
        Pack.intToBigEndian(n3, byArray, 8);
        Pack.intToBigEndian(n4, byArray, 12);
        return byArray;
    }

    protected static byte[] calculateTweak64_FF3_1(byte[] byArray) {
        byte[] byArray2 = new byte[]{byArray[0], byArray[1], byArray[2], (byte)(byArray[3] & 0xF0), byArray[4], byArray[5], byArray[6], (byte)(byArray[3] << 4)};
        return byArray2;
    }

    protected static BigInteger calculateY_FF1(BlockCipher blockCipher, byte[] byArray, int n2, int n3, int n4, byte[] byArray2, short[] sArray, RadixConverter radixConverter) {
        byte[] byArray3;
        int n5 = byArray.length;
        BigInteger bigInteger = radixConverter.fromEncoding(sArray);
        byte[] byArray4 = BigIntegers.asUnsignedByteArray(bigInteger);
        int n6 = -(n5 + n2 + 1) & 0xF;
        byte[] byArray5 = new byte[n5 + n6 + 1 + n2];
        System.arraycopy(byArray, 0, byArray5, 0, n5);
        byArray5[n5 + n6] = (byte)n4;
        System.arraycopy(byArray4, 0, byArray5, byArray5.length - byArray4.length, byArray4.length);
        byte[] byArray6 = byArray3 = SP80038G.prf(blockCipher, Arrays.concatenate(byArray2, byArray5));
        if (n3 > 16) {
            int n7 = (n3 + 16 - 1) / 16;
            byArray6 = new byte[n7 * 16];
            int n8 = Pack.bigEndianToInt(byArray3, 12);
            System.arraycopy(byArray3, 0, byArray6, 0, 16);
            for (int i2 = 1; i2 < n7; ++i2) {
                int n9 = i2 * 16;
                System.arraycopy(byArray3, 0, byArray6, n9, 12);
                Pack.intToBigEndian(n8 ^ i2, byArray6, n9 + 16 - 4);
                blockCipher.processBlock(byArray6, n9, byArray6, n9);
            }
        }
        return SP80038G.num(byArray6, 0, n3);
    }

    protected static BigInteger calculateY_FF3(BlockCipher blockCipher, byte[] byArray, int n2, int n3, short[] sArray, RadixConverter radixConverter) {
        byte[] byArray2 = new byte[16];
        Pack.intToBigEndian(Pack.bigEndianToInt(byArray, n2) ^ n3, byArray2, 0);
        BigInteger bigInteger = radixConverter.fromEncoding(sArray);
        BigIntegers.asUnsignedByteArray(bigInteger, byArray2, 4, 12);
        Arrays.reverseInPlace(byArray2);
        blockCipher.processBlock(byArray2, 0, byArray2, 0);
        Arrays.reverseInPlace(byArray2);
        byte[] byArray3 = byArray2;
        return SP80038G.num(byArray3, 0, byArray3.length);
    }

    protected static void checkArgs(BlockCipher blockCipher, boolean bl, int n2, short[] sArray, int n3, int n4) {
        SP80038G.checkCipher(blockCipher);
        if (n2 < 2 || n2 > 65536) {
            throw new IllegalArgumentException();
        }
        SP80038G.checkData(bl, n2, sArray, n3, n4);
    }

    protected static void checkArgs(BlockCipher blockCipher, boolean bl, int n2, byte[] byArray, int n3, int n4) {
        SP80038G.checkCipher(blockCipher);
        if (n2 < 2 || n2 > 256) {
            throw new IllegalArgumentException();
        }
        SP80038G.checkData(bl, n2, byArray, n3, n4);
    }

    protected static void checkCipher(BlockCipher blockCipher) {
        if (16 != blockCipher.getBlockSize()) {
            throw new IllegalArgumentException();
        }
    }

    protected static void checkData(boolean bl, int n2, short[] sArray, int n3, int n4) {
        SP80038G.checkLength(bl, n2, n4);
        for (int i2 = 0; i2 < n4; ++i2) {
            int n5 = sArray[n3 + i2] & 0xFFFF;
            if (n5 < n2) continue;
            throw new IllegalArgumentException("input data outside of radix");
        }
    }

    protected static void checkData(boolean bl, int n2, byte[] byArray, int n3, int n4) {
        SP80038G.checkLength(bl, n2, n4);
        for (int i2 = 0; i2 < n4; ++i2) {
            int n5 = byArray[n3 + i2] & 0xFF;
            if (n5 < n2) continue;
            throw new IllegalArgumentException("input data outside of radix");
        }
    }

    private static void checkLength(boolean bl, int n2, int n3) {
        int n4;
        if (n3 < 2 || Math.pow(n2, n3) < 1000000.0) {
            throw new IllegalArgumentException("input too short");
        }
        if (!bl && n3 > (n4 = 2 * (int)Math.floor(Math.log(TWO_TO_96) / Math.log(n2)))) {
            throw new IllegalArgumentException("maximum input length is " + n4);
        }
    }

    protected static byte[] implDecryptFF3(BlockCipher blockCipher, RadixConverter radixConverter, byte[] byArray, byte[] byArray2, int n2, int n3) {
        byte[] byArray3 = byArray;
        int n4 = n3;
        int n5 = n4 / 2;
        int n6 = n4 - n5;
        short[] sArray = SP80038G.toShort(byArray2, n2, n6);
        short[] sArray2 = SP80038G.toShort(byArray2, n2 + n6, n5);
        short[] sArray3 = SP80038G.decFF3_1(blockCipher, radixConverter, byArray3, n4, n5, n6, sArray, sArray2);
        return SP80038G.toByte(sArray3);
    }

    protected static short[] implDecryptFF3w(BlockCipher blockCipher, RadixConverter radixConverter, byte[] byArray, short[] sArray, int n2, int n3) {
        byte[] byArray2 = byArray;
        int n4 = n3;
        int n5 = n4 / 2;
        int n6 = n4 - n5;
        short[] sArray2 = new short[n6];
        short[] sArray3 = new short[n5];
        System.arraycopy(sArray, n2, sArray2, 0, n6);
        System.arraycopy(sArray, n2 + n6, sArray3, 0, n5);
        return SP80038G.decFF3_1(blockCipher, radixConverter, byArray2, n4, n5, n6, sArray2, sArray3);
    }

    private static short[] decFF3_1(BlockCipher blockCipher, RadixConverter radixConverter, byte[] byArray, int n2, int n3, int n4, short[] sArray, short[] sArray2) {
        BigInteger bigInteger = BigInteger.valueOf(radixConverter.getRadix());
        BigInteger[] bigIntegerArray = SP80038G.calculateModUV(bigInteger, n3, n4);
        int n5 = n4;
        Arrays.reverseInPlace(sArray);
        Arrays.reverseInPlace(sArray2);
        for (int i2 = 7; i2 >= 0; --i2) {
            n5 = n2 - n5;
            BigInteger bigInteger2 = bigIntegerArray[1 - (i2 & 1)];
            int n6 = 4 - (i2 & 1) * 4;
            BigInteger bigInteger3 = SP80038G.calculateY_FF3(blockCipher, byArray, n6, i2, sArray, radixConverter);
            BigInteger bigInteger4 = radixConverter.fromEncoding(sArray2).subtract(bigInteger3).mod(bigInteger2);
            short[] sArray3 = sArray2;
            sArray2 = sArray;
            sArray = sArray3;
            radixConverter.toEncoding(bigInteger4, n5, sArray3);
        }
        Arrays.reverseInPlace(sArray);
        Arrays.reverseInPlace(sArray2);
        return Arrays.concatenate(sArray, sArray2);
    }

    protected static byte[] implEncryptFF3(BlockCipher blockCipher, RadixConverter radixConverter, byte[] byArray, byte[] byArray2, int n2, int n3) {
        byte[] byArray3 = byArray;
        int n4 = n3;
        int n5 = n4 / 2;
        int n6 = n4 - n5;
        short[] sArray = SP80038G.toShort(byArray2, n2, n6);
        short[] sArray2 = SP80038G.toShort(byArray2, n2 + n6, n5);
        short[] sArray3 = SP80038G.encFF3_1(blockCipher, radixConverter, byArray3, n4, n5, n6, sArray, sArray2);
        return SP80038G.toByte(sArray3);
    }

    protected static short[] implEncryptFF3w(BlockCipher blockCipher, RadixConverter radixConverter, byte[] byArray, short[] sArray, int n2, int n3) {
        byte[] byArray2 = byArray;
        int n4 = n3;
        int n5 = n4 / 2;
        int n6 = n4 - n5;
        short[] sArray2 = new short[n6];
        short[] sArray3 = new short[n5];
        System.arraycopy(sArray, n2, sArray2, 0, n6);
        System.arraycopy(sArray, n2 + n6, sArray3, 0, n5);
        return SP80038G.encFF3_1(blockCipher, radixConverter, byArray2, n4, n5, n6, sArray2, sArray3);
    }

    private static short[] encFF3_1(BlockCipher blockCipher, RadixConverter radixConverter, byte[] byArray, int n2, int n3, int n4, short[] sArray, short[] sArray2) {
        BigInteger bigInteger = BigInteger.valueOf(radixConverter.getRadix());
        BigInteger[] bigIntegerArray = SP80038G.calculateModUV(bigInteger, n3, n4);
        int n5 = n3;
        Arrays.reverseInPlace(sArray);
        Arrays.reverseInPlace(sArray2);
        for (int i2 = 0; i2 < 8; ++i2) {
            n5 = n2 - n5;
            BigInteger bigInteger2 = bigIntegerArray[1 - (i2 & 1)];
            int n6 = 4 - (i2 & 1) * 4;
            BigInteger bigInteger3 = SP80038G.calculateY_FF3(blockCipher, byArray, n6, i2, sArray2, radixConverter);
            BigInteger bigInteger4 = radixConverter.fromEncoding(sArray).add(bigInteger3).mod(bigInteger2);
            short[] sArray3 = sArray;
            sArray = sArray2;
            sArray2 = sArray3;
            radixConverter.toEncoding(bigInteger4, n5, sArray3);
        }
        Arrays.reverseInPlace(sArray);
        Arrays.reverseInPlace(sArray2);
        return Arrays.concatenate(sArray, sArray2);
    }

    protected static BigInteger num(byte[] byArray, int n2, int n3) {
        return new BigInteger(1, Arrays.copyOfRange(byArray, n2, n2 + n3));
    }

    protected static byte[] prf(BlockCipher blockCipher, byte[] byArray) {
        if (byArray.length % 16 != 0) {
            throw new IllegalArgumentException();
        }
        int n2 = byArray.length / 16;
        byte[] byArray2 = new byte[16];
        for (int i2 = 0; i2 < n2; ++i2) {
            Bytes.xorTo(16, byArray, i2 * 16, byArray2, 0);
            blockCipher.processBlock(byArray2, 0, byArray2, 0);
        }
        return byArray2;
    }

    private static byte[] toByte(short[] sArray) {
        byte[] byArray = new byte[sArray.length];
        for (int i2 = 0; i2 != byArray.length; ++i2) {
            byArray[i2] = (byte)sArray[i2];
        }
        return byArray;
    }

    private static short[] toShort(byte[] byArray, int n2, int n3) {
        short[] sArray = new short[n3];
        for (int i2 = 0; i2 != sArray.length; ++i2) {
            sArray[i2] = (short)(byArray[n2 + i2] & 0xFF);
        }
        return sArray;
    }
}

