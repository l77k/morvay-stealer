/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.util;

import java.math.BigInteger;
import org.bouncycastle.util.BigIntegers;

public class RadixConverter {
    private static final double LOG_LONG_MAX_VALUE = Math.log(9.223372036854776E18);
    private static final int DEFAULT_POWERS_TO_CACHE = 10;
    private final int digitsGroupLength;
    private final BigInteger digitsGroupSpaceSize;
    private final int radix;
    private final BigInteger[] digitsGroupSpacePowers;

    public RadixConverter(int n2, int n3) {
        this.radix = n2;
        this.digitsGroupLength = (int)Math.floor(LOG_LONG_MAX_VALUE / Math.log(n2));
        this.digitsGroupSpaceSize = BigInteger.valueOf(n2).pow(this.digitsGroupLength);
        this.digitsGroupSpacePowers = this.precomputeDigitsGroupPowers(n3, this.digitsGroupSpaceSize);
    }

    public RadixConverter(int n2) {
        this(n2, 10);
    }

    public int getRadix() {
        return this.radix;
    }

    public void toEncoding(BigInteger bigInteger, int n2, short[] sArray) {
        if (bigInteger.signum() < 0) {
            throw new IllegalArgumentException();
        }
        int n3 = n2 - 1;
        do {
            if (bigInteger.equals(BigInteger.ZERO)) {
                sArray[n3--] = 0;
                continue;
            }
            BigInteger[] bigIntegerArray = bigInteger.divideAndRemainder(this.digitsGroupSpaceSize);
            bigInteger = bigIntegerArray[0];
            n3 = this.toEncoding(bigIntegerArray[1].longValue(), n3, sArray);
        } while (n3 >= 0);
        if (bigInteger.signum() != 0) {
            throw new IllegalArgumentException();
        }
    }

    private int toEncoding(long l2, int n2, short[] sArray) {
        for (int i2 = 0; i2 < this.digitsGroupLength && n2 >= 0; ++i2) {
            if (l2 == 0L) {
                sArray[n2--] = 0;
                continue;
            }
            sArray[n2--] = (short)(l2 % (long)this.radix);
            l2 /= (long)this.radix;
        }
        if (l2 != 0L) {
            throw new IllegalStateException("Failed to convert decimal number");
        }
        return n2;
    }

    public BigInteger fromEncoding(short[] sArray) {
        BigInteger bigInteger = BigIntegers.ONE;
        BigInteger bigInteger2 = null;
        int n2 = 0;
        int n3 = sArray.length;
        for (int i2 = n3 - this.digitsGroupLength; i2 > -this.digitsGroupLength; i2 -= this.digitsGroupLength) {
            int n4 = this.digitsGroupLength;
            if (i2 < 0) {
                n4 = this.digitsGroupLength + i2;
                i2 = 0;
            }
            int n5 = Math.min(i2 + n4, n3);
            long l2 = this.fromEncoding(i2, n5, sArray);
            BigInteger bigInteger3 = BigInteger.valueOf(l2);
            if (n2 == 0) {
                bigInteger2 = bigInteger3;
            } else {
                bigInteger = n2 <= this.digitsGroupSpacePowers.length ? this.digitsGroupSpacePowers[n2 - 1] : bigInteger.multiply(this.digitsGroupSpaceSize);
                bigInteger2 = bigInteger2.add(bigInteger3.multiply(bigInteger));
            }
            ++n2;
        }
        return bigInteger2;
    }

    public int getDigitsGroupLength() {
        return this.digitsGroupLength;
    }

    private long fromEncoding(int n2, int n3, short[] sArray) {
        long l2 = 0L;
        for (int i2 = n2; i2 < n3; ++i2) {
            l2 = l2 * (long)this.radix + (long)(sArray[i2] & 0xFFFF);
        }
        return l2;
    }

    private BigInteger[] precomputeDigitsGroupPowers(int n2, BigInteger bigInteger) {
        BigInteger[] bigIntegerArray = new BigInteger[n2];
        BigInteger bigInteger2 = bigInteger;
        for (int i2 = 0; i2 < n2; ++i2) {
            bigIntegerArray[i2] = bigInteger2;
            bigInteger2 = bigInteger2.multiply(bigInteger);
        }
        return bigIntegerArray;
    }
}

