/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.generators;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Vector;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.CryptoServicePurpose;
import org.bouncycastle.crypto.CryptoServicesRegistrar;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.constraints.ConstraintUtils;
import org.bouncycastle.crypto.constraints.DefaultServiceProperties;
import org.bouncycastle.crypto.params.NaccacheSternKeyGenerationParameters;
import org.bouncycastle.crypto.params.NaccacheSternKeyParameters;
import org.bouncycastle.crypto.params.NaccacheSternPrivateKeyParameters;
import org.bouncycastle.util.BigIntegers;

public class NaccacheSternKeyPairGenerator
implements AsymmetricCipherKeyPairGenerator {
    private static int[] smallPrimes = new int[]{3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179, 181, 191, 193, 197, 199, 211, 223, 227, 229, 233, 239, 241, 251, 257, 263, 269, 271, 277, 281, 283, 293, 307, 311, 313, 317, 331, 337, 347, 349, 353, 359, 367, 373, 379, 383, 389, 397, 401, 409, 419, 421, 431, 433, 439, 443, 449, 457, 461, 463, 467, 479, 487, 491, 499, 503, 509, 521, 523, 541, 547, 557};
    private NaccacheSternKeyGenerationParameters param;
    private static final BigInteger ONE = BigInteger.valueOf(1L);

    @Override
    public void init(KeyGenerationParameters keyGenerationParameters) {
        this.param = (NaccacheSternKeyGenerationParameters)keyGenerationParameters;
        CryptoServicesRegistrar.checkConstraints(new DefaultServiceProperties("NaccacheStern KeyGen", ConstraintUtils.bitsOfSecurityForFF(keyGenerationParameters.getStrength()), keyGenerationParameters, CryptoServicePurpose.KEYGEN));
    }

    @Override
    public AsymmetricCipherKeyPair generateKeyPair() {
        BigInteger bigInteger;
        BigInteger bigInteger2;
        BigInteger bigInteger3;
        BigInteger bigInteger4;
        BigInteger bigInteger5;
        BigInteger bigInteger6;
        int n2;
        int n3 = this.param.getStrength();
        SecureRandom secureRandom = this.param.getRandom();
        int n4 = this.param.getCertainty();
        boolean bl = this.param.isDebug();
        if (bl) {
            System.out.println("Fetching first " + this.param.getCntSmallPrimes() + " primes.");
        }
        Vector vector = NaccacheSternKeyPairGenerator.findFirstPrimes(this.param.getCntSmallPrimes());
        vector = NaccacheSternKeyPairGenerator.permuteList(vector, secureRandom);
        BigInteger bigInteger7 = ONE;
        BigInteger bigInteger8 = ONE;
        for (n2 = 0; n2 < vector.size() / 2; ++n2) {
            bigInteger7 = bigInteger7.multiply((BigInteger)vector.elementAt(n2));
        }
        for (n2 = vector.size() / 2; n2 < vector.size(); ++n2) {
            bigInteger8 = bigInteger8.multiply((BigInteger)vector.elementAt(n2));
        }
        BigInteger bigInteger9 = bigInteger7.multiply(bigInteger8);
        int n5 = n3 - bigInteger9.bitLength() - 48;
        BigInteger bigInteger10 = NaccacheSternKeyPairGenerator.generatePrime(n5 / 2 + 1, n4, secureRandom);
        BigInteger bigInteger11 = NaccacheSternKeyPairGenerator.generatePrime(n5 / 2 + 1, n4, secureRandom);
        long l2 = 0L;
        if (bl) {
            System.out.println("generating p and q");
        }
        BigInteger bigInteger12 = bigInteger10.multiply(bigInteger7).shiftLeft(1);
        BigInteger bigInteger13 = bigInteger11.multiply(bigInteger8).shiftLeft(1);
        while (true) {
            ++l2;
            bigInteger6 = NaccacheSternKeyPairGenerator.generatePrime(24, n4, secureRandom);
            bigInteger5 = bigInteger6.multiply(bigInteger12).add(ONE);
            if (!bigInteger5.isProbablePrime(n4)) continue;
            while (bigInteger6.equals(bigInteger4 = NaccacheSternKeyPairGenerator.generatePrime(24, n4, secureRandom)) || !(bigInteger3 = bigInteger4.multiply(bigInteger13).add(ONE)).isProbablePrime(n4)) {
            }
            if (!BigIntegers.modOddIsCoprime(bigInteger6.multiply(bigInteger4), bigInteger9)) continue;
            bigInteger2 = bigInteger5.multiply(bigInteger3);
            if (bigInteger2.bitLength() >= n3) break;
            if (!bl) continue;
            System.out.println("key size too small. Should be " + n3 + " but is actually " + bigInteger5.multiply(bigInteger3).bitLength());
        }
        if (bl) {
            System.out.println("needed " + l2 + " tries to generate p and q.");
        }
        BigInteger bigInteger14 = bigInteger5.subtract(ONE).multiply(bigInteger3.subtract(ONE));
        l2 = 0L;
        if (bl) {
            System.out.println("generating g");
        }
        while (true) {
            int n6;
            Vector<BigInteger> vector2 = new Vector<BigInteger>();
            for (n6 = 0; n6 != vector.size(); ++n6) {
                BigInteger bigInteger15 = (BigInteger)vector.elementAt(n6);
                BigInteger bigInteger16 = bigInteger14.divide(bigInteger15);
                do {
                    ++l2;
                } while ((bigInteger = BigIntegers.createRandomPrime(n3, n4, secureRandom)).modPow(bigInteger16, bigInteger2).equals(ONE));
                vector2.addElement(bigInteger);
            }
            bigInteger = ONE;
            for (n6 = 0; n6 < vector.size(); ++n6) {
                bigInteger = bigInteger.multiply(((BigInteger)vector2.elementAt(n6)).modPow(bigInteger9.divide((BigInteger)vector.elementAt(n6)), bigInteger2)).mod(bigInteger2);
            }
            n6 = 0;
            for (int i2 = 0; i2 < vector.size(); ++i2) {
                if (!bigInteger.modPow(bigInteger14.divide((BigInteger)vector.elementAt(i2)), bigInteger2).equals(ONE)) continue;
                if (bl) {
                    System.out.println("g has order phi(n)/" + vector.elementAt(i2) + "\n g: " + bigInteger);
                }
                n6 = 1;
                break;
            }
            if (n6 != 0) continue;
            if (bigInteger.modPow(bigInteger14.divide(BigInteger.valueOf(4L)), bigInteger2).equals(ONE)) {
                if (!bl) continue;
                System.out.println("g has order phi(n)/4\n g:" + bigInteger);
                continue;
            }
            if (bigInteger.modPow(bigInteger14.divide(bigInteger6), bigInteger2).equals(ONE)) {
                if (!bl) continue;
                System.out.println("g has order phi(n)/p'\n g: " + bigInteger);
                continue;
            }
            if (bigInteger.modPow(bigInteger14.divide(bigInteger4), bigInteger2).equals(ONE)) {
                if (!bl) continue;
                System.out.println("g has order phi(n)/q'\n g: " + bigInteger);
                continue;
            }
            if (bigInteger.modPow(bigInteger14.divide(bigInteger10), bigInteger2).equals(ONE)) {
                if (!bl) continue;
                System.out.println("g has order phi(n)/a\n g: " + bigInteger);
                continue;
            }
            if (!bigInteger.modPow(bigInteger14.divide(bigInteger11), bigInteger2).equals(ONE)) break;
            if (!bl) continue;
            System.out.println("g has order phi(n)/b\n g: " + bigInteger);
        }
        if (bl) {
            System.out.println("needed " + l2 + " tries to generate g");
            System.out.println();
            System.out.println("found new NaccacheStern cipher variables:");
            System.out.println("smallPrimes: " + vector);
            System.out.println("sigma:...... " + bigInteger9 + " (" + bigInteger9.bitLength() + " bits)");
            System.out.println("a:.......... " + bigInteger10);
            System.out.println("b:.......... " + bigInteger11);
            System.out.println("p':......... " + bigInteger6);
            System.out.println("q':......... " + bigInteger4);
            System.out.println("p:.......... " + bigInteger5);
            System.out.println("q:.......... " + bigInteger3);
            System.out.println("n:.......... " + bigInteger2);
            System.out.println("phi(n):..... " + bigInteger14);
            System.out.println("g:.......... " + bigInteger);
            System.out.println();
        }
        return new AsymmetricCipherKeyPair(new NaccacheSternKeyParameters(false, bigInteger, bigInteger2, bigInteger9.bitLength()), new NaccacheSternPrivateKeyParameters(bigInteger, bigInteger2, bigInteger9.bitLength(), vector, bigInteger14));
    }

    private static BigInteger generatePrime(int n2, int n3, SecureRandom secureRandom) {
        BigInteger bigInteger = BigIntegers.createRandomPrime(n2, n3, secureRandom);
        while (bigInteger.bitLength() != n2) {
            bigInteger = BigIntegers.createRandomPrime(n2, n3, secureRandom);
        }
        return bigInteger;
    }

    private static Vector permuteList(Vector vector, SecureRandom secureRandom) {
        Vector vector2 = new Vector();
        Vector vector3 = new Vector();
        for (int i2 = 0; i2 < vector.size(); ++i2) {
            vector3.addElement(vector.elementAt(i2));
        }
        vector2.addElement(vector3.elementAt(0));
        vector3.removeElementAt(0);
        while (vector3.size() != 0) {
            vector2.insertElementAt(vector3.elementAt(0), NaccacheSternKeyPairGenerator.getInt(secureRandom, vector2.size() + 1));
            vector3.removeElementAt(0);
        }
        return vector2;
    }

    private static int getInt(SecureRandom secureRandom, int n2) {
        int n3;
        int n4;
        if ((n2 & -n2) == n2) {
            return (int)((long)n2 * (long)(secureRandom.nextInt() & Integer.MAX_VALUE) >> 31);
        }
        while ((n4 = secureRandom.nextInt() & Integer.MAX_VALUE) - (n3 = n4 % n2) + (n2 - 1) < 0) {
        }
        return n3;
    }

    private static Vector findFirstPrimes(int n2) {
        Vector<BigInteger> vector = new Vector<BigInteger>(n2);
        for (int i2 = 0; i2 != n2; ++i2) {
            vector.addElement(BigInteger.valueOf(smallPrimes[i2]));
        }
        return vector;
    }
}

