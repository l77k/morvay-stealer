/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.tls;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle.crypto.CryptoServicePurpose;
import org.bouncycastle.crypto.CryptoServicesRegistrar;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.constraints.ConstraintUtils;
import org.bouncycastle.crypto.constraints.DefaultServiceProperties;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.params.RSAPrivateCrtKeyParameters;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.BigIntegers;
import org.bouncycastle.util.Pack;

public abstract class TlsRsaKeyExchange {
    public static final int PRE_MASTER_SECRET_LENGTH = 48;
    private static final BigInteger ONE = BigInteger.valueOf(1L);

    private TlsRsaKeyExchange() {
    }

    public static byte[] decryptPreMasterSecret(byte[] byArray, int n2, int n3, RSAKeyParameters rSAKeyParameters, int n4, SecureRandom secureRandom) {
        if (byArray == null || n3 < 1 || n3 > TlsRsaKeyExchange.getInputLimit(rSAKeyParameters) || n2 < 0 || n2 > byArray.length - n3) {
            throw new IllegalArgumentException("input not a valid EncryptedPreMasterSecret");
        }
        if (!rSAKeyParameters.isPrivate()) {
            throw new IllegalArgumentException("'privateKey' must be an RSA private key");
        }
        BigInteger bigInteger = rSAKeyParameters.getModulus();
        int n5 = bigInteger.bitLength();
        if (n5 < 512) {
            throw new IllegalArgumentException("'privateKey' must be at least 512 bits");
        }
        int n6 = ConstraintUtils.bitsOfSecurityFor(bigInteger);
        CryptoServicesRegistrar.checkConstraints(new DefaultServiceProperties("RSA", n6, rSAKeyParameters, CryptoServicePurpose.DECRYPTION));
        if ((n4 & 0xFFFF) != n4) {
            throw new IllegalArgumentException("'protocolVersion' must be a 16 bit value");
        }
        secureRandom = CryptoServicesRegistrar.getSecureRandom(secureRandom);
        byte[] byArray2 = new byte[48];
        secureRandom.nextBytes(byArray2);
        try {
            BigInteger bigInteger2 = TlsRsaKeyExchange.convertInput(bigInteger, byArray, n2, n3);
            byte[] byArray3 = TlsRsaKeyExchange.rsaBlinded(rSAKeyParameters, bigInteger2, secureRandom);
            int n7 = (n5 - 1) / 8;
            int n8 = byArray3.length - 48;
            int n9 = TlsRsaKeyExchange.checkPkcs1Encoding2(byArray3, n7, 48);
            int n10 = -((Pack.bigEndianToShort(byArray3, n8) ^ n4) & 0xFFFF) >> 31;
            int n11 = n9 | n10;
            for (int i2 = 0; i2 < 48; ++i2) {
                byArray2[i2] = (byte)(byArray2[i2] & n11 | byArray3[n8 + i2] & ~n11);
            }
            Arrays.fill(byArray3, (byte)0);
        }
        catch (Exception exception) {
            // empty catch block
        }
        return byArray2;
    }

    public static int getInputLimit(RSAKeyParameters rSAKeyParameters) {
        return (rSAKeyParameters.getModulus().bitLength() + 7) / 8;
    }

    private static int caddTo(int n2, int n3, byte[] byArray, byte[] byArray2) {
        int n4 = n3 & 0xFF;
        int n5 = 0;
        for (int i2 = n2 - 1; i2 >= 0; --i2) {
            byArray2[i2] = (byte)(n5 += (byArray2[i2] & 0xFF) + (byArray[i2] & n4));
            n5 >>>= 8;
        }
        return n5;
    }

    private static int checkPkcs1Encoding2(byte[] byArray, int n2, int n3) {
        int n4;
        int n5 = n2 - n3 - 10;
        int n6 = byArray.length - n2;
        int n7 = byArray.length - 1 - n3;
        for (n4 = 0; n4 < n6; ++n4) {
            n5 |= -(byArray[n4] & 0xFF);
        }
        n5 |= -(byArray[n6] & 0xFF ^ 2);
        for (n4 = n6 + 1; n4 < n7; ++n4) {
            n5 |= (byArray[n4] & 0xFF) - 1;
        }
        return (n5 |= -(byArray[n7] & 0xFF)) >> 31;
    }

    private static BigInteger convertInput(BigInteger bigInteger, byte[] byArray, int n2, int n3) {
        BigInteger bigInteger2 = BigIntegers.fromUnsignedByteArray(byArray, n2, n3);
        if (bigInteger2.compareTo(bigInteger) < 0) {
            return bigInteger2;
        }
        throw new DataLengthException("input too large for RSA cipher.");
    }

    private static BigInteger rsa(RSAKeyParameters rSAKeyParameters, BigInteger bigInteger) {
        return bigInteger.modPow(rSAKeyParameters.getExponent(), rSAKeyParameters.getModulus());
    }

    private static byte[] rsaBlinded(RSAKeyParameters rSAKeyParameters, BigInteger bigInteger, SecureRandom secureRandom) {
        RSAPrivateCrtKeyParameters rSAPrivateCrtKeyParameters;
        BigInteger bigInteger2;
        BigInteger bigInteger3 = rSAKeyParameters.getModulus();
        int n2 = bigInteger3.bitLength() / 8 + 1;
        if (rSAKeyParameters instanceof RSAPrivateCrtKeyParameters && (bigInteger2 = (rSAPrivateCrtKeyParameters = (RSAPrivateCrtKeyParameters)rSAKeyParameters).getPublicExponent()) != null) {
            BigInteger bigInteger4 = BigIntegers.createRandomInRange(ONE, bigInteger3.subtract(ONE), secureRandom);
            BigInteger bigInteger5 = bigInteger4.modPow(bigInteger2, bigInteger3);
            BigInteger bigInteger6 = BigIntegers.modOddInverse(bigInteger3, bigInteger4);
            BigInteger bigInteger7 = bigInteger5.multiply(bigInteger).mod(bigInteger3);
            BigInteger bigInteger8 = TlsRsaKeyExchange.rsaCrt(rSAPrivateCrtKeyParameters, bigInteger7);
            BigInteger bigInteger9 = bigInteger6.add(ONE).multiply(bigInteger8).mod(bigInteger3);
            byte[] byArray = TlsRsaKeyExchange.toBytes(bigInteger8, n2);
            byte[] byArray2 = TlsRsaKeyExchange.toBytes(bigInteger3, n2);
            byte[] byArray3 = TlsRsaKeyExchange.toBytes(bigInteger9, n2);
            int n3 = TlsRsaKeyExchange.subFrom(n2, byArray, byArray3);
            TlsRsaKeyExchange.caddTo(n2, n3, byArray2, byArray3);
            return byArray3;
        }
        return TlsRsaKeyExchange.toBytes(TlsRsaKeyExchange.rsa(rSAKeyParameters, bigInteger), n2);
    }

    private static BigInteger rsaCrt(RSAPrivateCrtKeyParameters rSAPrivateCrtKeyParameters, BigInteger bigInteger) {
        BigInteger bigInteger2 = rSAPrivateCrtKeyParameters.getPublicExponent();
        BigInteger bigInteger3 = rSAPrivateCrtKeyParameters.getP();
        BigInteger bigInteger4 = rSAPrivateCrtKeyParameters.getQ();
        BigInteger bigInteger5 = rSAPrivateCrtKeyParameters.getDP();
        BigInteger bigInteger6 = rSAPrivateCrtKeyParameters.getDQ();
        BigInteger bigInteger7 = rSAPrivateCrtKeyParameters.getQInv();
        BigInteger bigInteger8 = bigInteger.remainder(bigInteger3).modPow(bigInteger5, bigInteger3);
        BigInteger bigInteger9 = bigInteger.remainder(bigInteger4).modPow(bigInteger6, bigInteger4);
        BigInteger bigInteger10 = bigInteger8.subtract(bigInteger9);
        bigInteger10 = bigInteger10.multiply(bigInteger7);
        BigInteger bigInteger11 = (bigInteger10 = bigInteger10.mod(bigInteger3)).multiply(bigInteger4).add(bigInteger9);
        BigInteger bigInteger12 = bigInteger11.modPow(bigInteger2, rSAPrivateCrtKeyParameters.getModulus());
        if (!bigInteger12.equals(bigInteger)) {
            throw new IllegalStateException("RSA engine faulty decryption/signing detected");
        }
        return bigInteger11;
    }

    private static int subFrom(int n2, byte[] byArray, byte[] byArray2) {
        int n3 = 0;
        for (int i2 = n2 - 1; i2 >= 0; --i2) {
            byArray2[i2] = (byte)(n3 += (byArray2[i2] & 0xFF) - (byArray[i2] & 0xFF));
            n3 >>= 8;
        }
        return n3;
    }

    private static byte[] toBytes(BigInteger bigInteger, int n2) {
        byte[] byArray = bigInteger.toByteArray();
        byte[] byArray2 = new byte[n2];
        System.arraycopy(byArray, 0, byArray2, byArray2.length - byArray.length, byArray.length);
        return byArray2;
    }
}

