/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.signers;

import java.math.BigInteger;
import org.bouncycastle.crypto.signers.DSAEncoding;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.BigIntegers;

public class PlainDSAEncoding
implements DSAEncoding {
    public static final PlainDSAEncoding INSTANCE = new PlainDSAEncoding();

    @Override
    public byte[] encode(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3) {
        int n2 = BigIntegers.getUnsignedByteLength(bigInteger);
        byte[] byArray = new byte[n2 * 2];
        this.encodeValue(bigInteger, bigInteger2, byArray, 0, n2);
        this.encodeValue(bigInteger, bigInteger3, byArray, n2, n2);
        return byArray;
    }

    @Override
    public BigInteger[] decode(BigInteger bigInteger, byte[] byArray) {
        int n2 = BigIntegers.getUnsignedByteLength(bigInteger);
        if (byArray.length != n2 * 2) {
            throw new IllegalArgumentException("Encoding has incorrect length");
        }
        return new BigInteger[]{this.decodeValue(bigInteger, byArray, 0, n2), this.decodeValue(bigInteger, byArray, n2, n2)};
    }

    protected BigInteger checkValue(BigInteger bigInteger, BigInteger bigInteger2) {
        if (bigInteger2.signum() < 0 || bigInteger2.compareTo(bigInteger) >= 0) {
            throw new IllegalArgumentException("Value out of range");
        }
        return bigInteger2;
    }

    protected BigInteger decodeValue(BigInteger bigInteger, byte[] byArray, int n2, int n3) {
        byte[] byArray2 = Arrays.copyOfRange(byArray, n2, n2 + n3);
        return this.checkValue(bigInteger, new BigInteger(1, byArray2));
    }

    private void encodeValue(BigInteger bigInteger, BigInteger bigInteger2, byte[] byArray, int n2, int n3) {
        byte[] byArray2 = this.checkValue(bigInteger, bigInteger2).toByteArray();
        int n4 = Math.max(0, byArray2.length - n3);
        int n5 = byArray2.length - n4;
        int n6 = n3 - n5;
        Arrays.fill(byArray, n2, n2 + n6, (byte)0);
        System.arraycopy(byArray2, n4, byArray, n2 + n6, n5);
    }
}

