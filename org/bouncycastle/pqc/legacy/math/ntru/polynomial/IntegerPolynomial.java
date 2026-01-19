/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.legacy.math.ntru.polynomial;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import org.bouncycastle.pqc.legacy.math.ntru.euclid.BigIntEuclidean;
import org.bouncycastle.pqc.legacy.math.ntru.polynomial.BigIntPolynomial;
import org.bouncycastle.pqc.legacy.math.ntru.polynomial.Constants;
import org.bouncycastle.pqc.legacy.math.ntru.polynomial.LongPolynomial2;
import org.bouncycastle.pqc.legacy.math.ntru.polynomial.ModularResultant;
import org.bouncycastle.pqc.legacy.math.ntru.polynomial.Polynomial;
import org.bouncycastle.pqc.legacy.math.ntru.polynomial.Resultant;
import org.bouncycastle.pqc.legacy.math.ntru.util.ArrayEncoder;
import org.bouncycastle.pqc.legacy.math.ntru.util.Util;
import org.bouncycastle.util.Arrays;

public class IntegerPolynomial
implements Polynomial {
    private static final int NUM_EQUAL_RESULTANTS = 3;
    private static final int[] PRIMES = new int[]{4507, 4513, 4517, 4519, 4523, 4547, 4549, 4561, 4567, 4583, 4591, 4597, 4603, 4621, 4637, 4639, 4643, 4649, 4651, 4657, 4663, 4673, 4679, 4691, 4703, 4721, 4723, 4729, 4733, 4751, 4759, 4783, 4787, 4789, 4793, 4799, 4801, 4813, 4817, 4831, 4861, 4871, 4877, 4889, 4903, 4909, 4919, 4931, 4933, 4937, 4943, 4951, 4957, 4967, 4969, 4973, 4987, 4993, 4999, 5003, 5009, 5011, 5021, 5023, 5039, 5051, 5059, 5077, 5081, 5087, 5099, 5101, 5107, 5113, 5119, 5147, 5153, 5167, 5171, 5179, 5189, 5197, 5209, 5227, 5231, 5233, 5237, 5261, 5273, 5279, 5281, 5297, 5303, 5309, 5323, 5333, 5347, 5351, 5381, 5387, 5393, 5399, 5407, 5413, 5417, 5419, 5431, 5437, 5441, 5443, 5449, 5471, 5477, 5479, 5483, 5501, 5503, 5507, 5519, 5521, 5527, 5531, 5557, 5563, 5569, 5573, 5581, 5591, 5623, 5639, 5641, 5647, 5651, 5653, 5657, 5659, 5669, 5683, 5689, 5693, 5701, 5711, 5717, 5737, 5741, 5743, 5749, 5779, 5783, 5791, 5801, 5807, 5813, 5821, 5827, 5839, 5843, 5849, 5851, 5857, 5861, 5867, 5869, 5879, 5881, 5897, 5903, 5923, 5927, 5939, 5953, 5981, 5987, 6007, 6011, 6029, 6037, 6043, 6047, 6053, 6067, 6073, 6079, 6089, 6091, 6101, 6113, 6121, 6131, 6133, 6143, 6151, 6163, 6173, 6197, 6199, 6203, 6211, 6217, 6221, 6229, 6247, 6257, 6263, 6269, 6271, 6277, 6287, 6299, 6301, 6311, 6317, 6323, 6329, 6337, 6343, 6353, 6359, 6361, 6367, 6373, 6379, 6389, 6397, 6421, 6427, 6449, 6451, 6469, 6473, 6481, 6491, 6521, 6529, 6547, 6551, 6553, 6563, 6569, 6571, 6577, 6581, 6599, 6607, 6619, 6637, 6653, 6659, 6661, 6673, 6679, 6689, 6691, 6701, 6703, 6709, 6719, 6733, 6737, 6761, 6763, 6779, 6781, 6791, 6793, 6803, 6823, 6827, 6829, 6833, 6841, 6857, 6863, 6869, 6871, 6883, 6899, 6907, 6911, 6917, 6947, 6949, 6959, 6961, 6967, 6971, 6977, 6983, 6991, 6997, 7001, 7013, 7019, 7027, 7039, 7043, 7057, 7069, 7079, 7103, 7109, 7121, 7127, 7129, 7151, 7159, 7177, 7187, 7193, 7207, 7211, 7213, 7219, 7229, 7237, 7243, 7247, 7253, 7283, 7297, 7307, 7309, 7321, 7331, 7333, 7349, 7351, 7369, 7393, 7411, 7417, 7433, 7451, 7457, 7459, 7477, 7481, 7487, 7489, 7499, 7507, 7517, 7523, 7529, 7537, 7541, 7547, 7549, 7559, 7561, 7573, 7577, 7583, 7589, 7591, 7603, 7607, 7621, 7639, 7643, 7649, 7669, 7673, 7681, 7687, 7691, 7699, 7703, 7717, 7723, 7727, 7741, 7753, 7757, 7759, 7789, 7793, 7817, 7823, 7829, 7841, 7853, 7867, 7873, 7877, 7879, 7883, 7901, 7907, 7919, 7927, 7933, 7937, 7949, 7951, 7963, 7993, 8009, 8011, 8017, 8039, 8053, 8059, 8069, 8081, 8087, 8089, 8093, 8101, 8111, 8117, 8123, 8147, 8161, 8167, 8171, 8179, 8191, 8209, 8219, 8221, 8231, 8233, 8237, 8243, 8263, 8269, 8273, 8287, 8291, 8293, 8297, 8311, 8317, 8329, 8353, 8363, 8369, 8377, 8387, 8389, 8419, 8423, 8429, 8431, 8443, 8447, 8461, 8467, 8501, 8513, 8521, 8527, 8537, 8539, 8543, 8563, 8573, 8581, 8597, 8599, 8609, 8623, 8627, 8629, 8641, 8647, 8663, 8669, 8677, 8681, 8689, 8693, 8699, 8707, 8713, 8719, 8731, 8737, 8741, 8747, 8753, 8761, 8779, 8783, 8803, 8807, 8819, 8821, 8831, 8837, 8839, 8849, 8861, 8863, 8867, 8887, 8893, 8923, 8929, 8933, 8941, 8951, 8963, 8969, 8971, 8999, 9001, 9007, 9011, 9013, 9029, 9041, 9043, 9049, 9059, 9067, 9091, 9103, 9109, 9127, 9133, 9137, 9151, 9157, 9161, 9173, 9181, 9187, 9199, 9203, 9209, 9221, 9227, 9239, 9241, 9257, 9277, 9281, 9283, 9293, 9311, 9319, 9323, 9337, 9341, 9343, 9349, 9371, 9377, 9391, 9397, 9403, 9413, 9419, 9421, 9431, 9433, 9437, 9439, 9461, 9463, 9467, 9473, 9479, 9491, 9497, 9511, 9521, 9533, 9539, 9547, 9551, 9587, 9601, 9613, 9619, 9623, 9629, 9631, 9643, 9649, 9661, 9677, 9679, 9689, 9697, 9719, 9721, 9733, 9739, 9743, 9749, 9767, 9769, 9781, 9787, 9791, 9803, 9811, 9817, 9829, 9833, 9839, 9851, 9857, 9859, 9871, 9883, 9887, 9901, 9907, 9923, 9929, 9931, 9941, 9949, 9967, 9973};
    private static final List BIGINT_PRIMES = new ArrayList();
    public int[] coeffs;

    public IntegerPolynomial(int n2) {
        this.coeffs = new int[n2];
    }

    public IntegerPolynomial(int[] nArray) {
        this.coeffs = nArray;
    }

    public IntegerPolynomial(BigIntPolynomial bigIntPolynomial) {
        this.coeffs = new int[bigIntPolynomial.coeffs.length];
        for (int i2 = 0; i2 < bigIntPolynomial.coeffs.length; ++i2) {
            this.coeffs[i2] = bigIntPolynomial.coeffs[i2].intValue();
        }
    }

    public static IntegerPolynomial fromBinary3Sves(byte[] byArray, int n2) {
        return new IntegerPolynomial(ArrayEncoder.decodeMod3Sves(byArray, n2));
    }

    public static IntegerPolynomial fromBinary3Tight(byte[] byArray, int n2) {
        return new IntegerPolynomial(ArrayEncoder.decodeMod3Tight(byArray, n2));
    }

    public static IntegerPolynomial fromBinary3Tight(InputStream inputStream2, int n2) throws IOException {
        return new IntegerPolynomial(ArrayEncoder.decodeMod3Tight(inputStream2, n2));
    }

    public static IntegerPolynomial fromBinary(byte[] byArray, int n2, int n3) {
        return new IntegerPolynomial(ArrayEncoder.decodeModQ(byArray, n2, n3));
    }

    public static IntegerPolynomial fromBinary(InputStream inputStream2, int n2, int n3) throws IOException {
        return new IntegerPolynomial(ArrayEncoder.decodeModQ(inputStream2, n2, n3));
    }

    public byte[] toBinary3Sves() {
        return ArrayEncoder.encodeMod3Sves(this.coeffs);
    }

    public byte[] toBinary3Tight() {
        int n2;
        BigInteger bigInteger = Constants.BIGINT_ZERO;
        for (n2 = this.coeffs.length - 1; n2 >= 0; --n2) {
            bigInteger = bigInteger.multiply(BigInteger.valueOf(3L));
            bigInteger = bigInteger.add(BigInteger.valueOf(this.coeffs[n2] + 1));
        }
        n2 = (BigInteger.valueOf(3L).pow(this.coeffs.length).bitLength() + 7) / 8;
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

    public byte[] toBinary(int n2) {
        return ArrayEncoder.encodeModQ(this.coeffs, n2);
    }

    @Override
    public IntegerPolynomial mult(IntegerPolynomial integerPolynomial, int n2) {
        IntegerPolynomial integerPolynomial2 = this.mult(integerPolynomial);
        integerPolynomial2.mod(n2);
        return integerPolynomial2;
    }

    @Override
    public IntegerPolynomial mult(IntegerPolynomial integerPolynomial) {
        int n2 = this.coeffs.length;
        if (integerPolynomial.coeffs.length != n2) {
            throw new IllegalArgumentException("Number of coefficients must be the same");
        }
        IntegerPolynomial integerPolynomial2 = this.multRecursive(integerPolynomial);
        if (integerPolynomial2.coeffs.length > n2) {
            for (int i2 = n2; i2 < integerPolynomial2.coeffs.length; ++i2) {
                int n3 = i2 - n2;
                integerPolynomial2.coeffs[n3] = integerPolynomial2.coeffs[n3] + integerPolynomial2.coeffs[i2];
            }
            integerPolynomial2.coeffs = Arrays.copyOf(integerPolynomial2.coeffs, n2);
        }
        return integerPolynomial2;
    }

    @Override
    public BigIntPolynomial mult(BigIntPolynomial bigIntPolynomial) {
        return new BigIntPolynomial(this).mult(bigIntPolynomial);
    }

    private IntegerPolynomial multRecursive(IntegerPolynomial integerPolynomial) {
        int n2;
        int[] nArray = this.coeffs;
        int[] nArray2 = integerPolynomial.coeffs;
        int n3 = integerPolynomial.coeffs.length;
        if (n3 <= 32) {
            int n4 = 2 * n3 - 1;
            IntegerPolynomial integerPolynomial2 = new IntegerPolynomial(new int[n4]);
            for (int i2 = 0; i2 < n4; ++i2) {
                for (int i3 = Math.max(0, i2 - n3 + 1); i3 <= Math.min(i2, n3 - 1); ++i3) {
                    int n5 = i2;
                    integerPolynomial2.coeffs[n5] = integerPolynomial2.coeffs[n5] + nArray2[i3] * nArray[i2 - i3];
                }
            }
            return integerPolynomial2;
        }
        int n6 = n3 / 2;
        IntegerPolynomial integerPolynomial3 = new IntegerPolynomial(Arrays.copyOf(nArray, n6));
        IntegerPolynomial integerPolynomial4 = new IntegerPolynomial(Arrays.copyOfRange(nArray, n6, n3));
        IntegerPolynomial integerPolynomial5 = new IntegerPolynomial(Arrays.copyOf(nArray2, n6));
        IntegerPolynomial integerPolynomial6 = new IntegerPolynomial(Arrays.copyOfRange(nArray2, n6, n3));
        IntegerPolynomial integerPolynomial7 = (IntegerPolynomial)integerPolynomial3.clone();
        integerPolynomial7.add(integerPolynomial4);
        IntegerPolynomial integerPolynomial8 = (IntegerPolynomial)integerPolynomial5.clone();
        integerPolynomial8.add(integerPolynomial6);
        IntegerPolynomial integerPolynomial9 = integerPolynomial3.multRecursive(integerPolynomial5);
        IntegerPolynomial integerPolynomial10 = integerPolynomial4.multRecursive(integerPolynomial6);
        IntegerPolynomial integerPolynomial11 = integerPolynomial7.multRecursive(integerPolynomial8);
        integerPolynomial11.sub(integerPolynomial9);
        integerPolynomial11.sub(integerPolynomial10);
        IntegerPolynomial integerPolynomial12 = new IntegerPolynomial(2 * n3 - 1);
        for (n2 = 0; n2 < integerPolynomial9.coeffs.length; ++n2) {
            integerPolynomial12.coeffs[n2] = integerPolynomial9.coeffs[n2];
        }
        for (n2 = 0; n2 < integerPolynomial11.coeffs.length; ++n2) {
            int n7 = n6 + n2;
            integerPolynomial12.coeffs[n7] = integerPolynomial12.coeffs[n7] + integerPolynomial11.coeffs[n2];
        }
        for (n2 = 0; n2 < integerPolynomial10.coeffs.length; ++n2) {
            int n8 = 2 * n6 + n2;
            integerPolynomial12.coeffs[n8] = integerPolynomial12.coeffs[n8] + integerPolynomial10.coeffs[n2];
        }
        return integerPolynomial12;
    }

    public IntegerPolynomial invertFq(int n2) {
        int n3 = this.coeffs.length;
        int n4 = 0;
        IntegerPolynomial integerPolynomial = new IntegerPolynomial(n3 + 1);
        integerPolynomial.coeffs[0] = 1;
        IntegerPolynomial integerPolynomial2 = new IntegerPolynomial(n3 + 1);
        IntegerPolynomial integerPolynomial3 = new IntegerPolynomial(n3 + 1);
        integerPolynomial3.coeffs = Arrays.copyOf(this.coeffs, n3 + 1);
        integerPolynomial3.modPositive(2);
        IntegerPolynomial integerPolynomial4 = new IntegerPolynomial(n3 + 1);
        integerPolynomial4.coeffs[0] = 1;
        integerPolynomial4.coeffs[n3] = 1;
        while (true) {
            if (integerPolynomial3.coeffs[0] == 0) {
                for (int i2 = 1; i2 <= n3; ++i2) {
                    integerPolynomial3.coeffs[i2 - 1] = integerPolynomial3.coeffs[i2];
                    integerPolynomial2.coeffs[n3 + 1 - i2] = integerPolynomial2.coeffs[n3 - i2];
                }
                integerPolynomial3.coeffs[n3] = 0;
                integerPolynomial2.coeffs[0] = 0;
                ++n4;
                if (!integerPolynomial3.equalsZero()) continue;
                return null;
            }
            if (integerPolynomial3.equalsOne()) break;
            if (integerPolynomial3.degree() < integerPolynomial4.degree()) {
                IntegerPolynomial integerPolynomial5 = integerPolynomial3;
                integerPolynomial3 = integerPolynomial4;
                integerPolynomial4 = integerPolynomial5;
                integerPolynomial5 = integerPolynomial;
                integerPolynomial = integerPolynomial2;
                integerPolynomial2 = integerPolynomial5;
            }
            integerPolynomial3.add(integerPolynomial4, 2);
            integerPolynomial.add(integerPolynomial2, 2);
        }
        if (integerPolynomial.coeffs[n3] != 0) {
            return null;
        }
        IntegerPolynomial integerPolynomial6 = new IntegerPolynomial(n3);
        int n5 = 0;
        n4 %= n3;
        for (int i3 = n3 - 1; i3 >= 0; --i3) {
            n5 = i3 - n4;
            if (n5 < 0) {
                n5 += n3;
            }
            integerPolynomial6.coeffs[n5] = integerPolynomial.coeffs[i3];
        }
        return this.mod2ToModq(integerPolynomial6, n2);
    }

    private IntegerPolynomial mod2ToModq(IntegerPolynomial integerPolynomial, int n2) {
        if (Util.is64BitJVM() && n2 == 2048) {
            LongPolynomial2 longPolynomial2 = new LongPolynomial2(this);
            LongPolynomial2 longPolynomial22 = new LongPolynomial2(integerPolynomial);
            int n3 = 2;
            while (n3 < n2) {
                LongPolynomial2 longPolynomial23 = (LongPolynomial2)longPolynomial22.clone();
                longPolynomial23.mult2And((n3 *= 2) - 1);
                longPolynomial22 = longPolynomial2.mult(longPolynomial22).mult(longPolynomial22);
                longPolynomial23.subAnd(longPolynomial22, n3 - 1);
                longPolynomial22 = longPolynomial23;
            }
            return longPolynomial22.toIntegerPolynomial();
        }
        int n4 = 2;
        while (n4 < n2) {
            IntegerPolynomial integerPolynomial2 = new IntegerPolynomial(Arrays.copyOf(integerPolynomial.coeffs, integerPolynomial.coeffs.length));
            integerPolynomial2.mult2(n4 *= 2);
            integerPolynomial = this.mult(integerPolynomial, n4).mult(integerPolynomial, n4);
            integerPolynomial2.sub(integerPolynomial, n4);
            integerPolynomial = integerPolynomial2;
        }
        return integerPolynomial;
    }

    public IntegerPolynomial invertF3() {
        int n2 = this.coeffs.length;
        int n3 = 0;
        IntegerPolynomial integerPolynomial = new IntegerPolynomial(n2 + 1);
        integerPolynomial.coeffs[0] = 1;
        IntegerPolynomial integerPolynomial2 = new IntegerPolynomial(n2 + 1);
        IntegerPolynomial integerPolynomial3 = new IntegerPolynomial(n2 + 1);
        integerPolynomial3.coeffs = Arrays.copyOf(this.coeffs, n2 + 1);
        integerPolynomial3.modPositive(3);
        IntegerPolynomial integerPolynomial4 = new IntegerPolynomial(n2 + 1);
        integerPolynomial4.coeffs[0] = -1;
        integerPolynomial4.coeffs[n2] = 1;
        while (true) {
            if (integerPolynomial3.coeffs[0] == 0) {
                for (int i2 = 1; i2 <= n2; ++i2) {
                    integerPolynomial3.coeffs[i2 - 1] = integerPolynomial3.coeffs[i2];
                    integerPolynomial2.coeffs[n2 + 1 - i2] = integerPolynomial2.coeffs[n2 - i2];
                }
                integerPolynomial3.coeffs[n2] = 0;
                integerPolynomial2.coeffs[0] = 0;
                ++n3;
                if (!integerPolynomial3.equalsZero()) continue;
                return null;
            }
            if (integerPolynomial3.equalsAbsOne()) break;
            if (integerPolynomial3.degree() < integerPolynomial4.degree()) {
                IntegerPolynomial integerPolynomial5 = integerPolynomial3;
                integerPolynomial3 = integerPolynomial4;
                integerPolynomial4 = integerPolynomial5;
                integerPolynomial5 = integerPolynomial;
                integerPolynomial = integerPolynomial2;
                integerPolynomial2 = integerPolynomial5;
            }
            if (integerPolynomial3.coeffs[0] == integerPolynomial4.coeffs[0]) {
                integerPolynomial3.sub(integerPolynomial4, 3);
                integerPolynomial.sub(integerPolynomial2, 3);
                continue;
            }
            integerPolynomial3.add(integerPolynomial4, 3);
            integerPolynomial.add(integerPolynomial2, 3);
        }
        if (integerPolynomial.coeffs[n2] != 0) {
            return null;
        }
        IntegerPolynomial integerPolynomial6 = new IntegerPolynomial(n2);
        int n4 = 0;
        n3 %= n2;
        for (int i3 = n2 - 1; i3 >= 0; --i3) {
            n4 = i3 - n3;
            if (n4 < 0) {
                n4 += n2;
            }
            integerPolynomial6.coeffs[n4] = integerPolynomial3.coeffs[0] * integerPolynomial.coeffs[i3];
        }
        integerPolynomial6.ensurePositive(3);
        return integerPolynomial6;
    }

    public Resultant resultant() {
        BigInteger bigInteger;
        Object object;
        Object object2;
        Object object3;
        int n2 = this.coeffs.length;
        LinkedList<Object> linkedList = new LinkedList<Object>();
        BigInteger bigInteger2 = Constants.BIGINT_ONE;
        BigInteger bigInteger3 = Constants.BIGINT_ONE;
        int n3 = 1;
        PrimeGenerator primeGenerator = new PrimeGenerator();
        while (true) {
            object3 = primeGenerator.nextPrime();
            object2 = this.resultant(((BigInteger)object3).intValue());
            linkedList.add(object2);
            object = bigInteger2.multiply((BigInteger)object3);
            BigIntEuclidean bigIntEuclidean = BigIntEuclidean.calculate((BigInteger)object3, bigInteger2);
            bigInteger = bigInteger3;
            bigInteger3 = bigInteger3.multiply(bigIntEuclidean.x.multiply((BigInteger)object3));
            BigInteger bigInteger4 = ((ModularResultant)object2).res.multiply(bigIntEuclidean.y.multiply(bigInteger2));
            bigInteger3 = bigInteger3.add(bigInteger4).mod((BigInteger)object);
            bigInteger2 = object;
            BigInteger bigInteger5 = bigInteger2.divide(BigInteger.valueOf(2L));
            BigInteger bigInteger6 = bigInteger5.negate();
            if (bigInteger3.compareTo(bigInteger5) > 0) {
                bigInteger3 = bigInteger3.subtract(bigInteger2);
            } else if (bigInteger3.compareTo(bigInteger6) < 0) {
                bigInteger3 = bigInteger3.add(bigInteger2);
            }
            if (bigInteger3.equals(bigInteger)) {
                if (++n3 < 3) continue;
                break;
            }
            n3 = 1;
        }
        while (linkedList.size() > 1) {
            object3 = (ModularResultant)linkedList.removeFirst();
            object2 = (ModularResultant)linkedList.removeFirst();
            object = ModularResultant.combineRho((ModularResultant)object3, (ModularResultant)object2);
            linkedList.addLast(object);
        }
        object3 = ((ModularResultant)linkedList.getFirst()).rho;
        object2 = bigInteger2.divide(BigInteger.valueOf(2L));
        object = ((BigInteger)object2).negate();
        if (bigInteger3.compareTo((BigInteger)object2) > 0) {
            bigInteger3 = bigInteger3.subtract(bigInteger2);
        }
        if (bigInteger3.compareTo((BigInteger)object) < 0) {
            bigInteger3 = bigInteger3.add(bigInteger2);
        }
        for (int i2 = 0; i2 < n2; ++i2) {
            bigInteger = ((BigIntPolynomial)object3).coeffs[i2];
            if (bigInteger.compareTo((BigInteger)object2) > 0) {
                ((BigIntPolynomial)object3).coeffs[i2] = bigInteger.subtract(bigInteger2);
            }
            if (bigInteger.compareTo((BigInteger)object) >= 0) continue;
            ((BigIntPolynomial)object3).coeffs[i2] = bigInteger.add(bigInteger2);
        }
        return new Resultant((BigIntPolynomial)object3, bigInteger3);
    }

    public Resultant resultantMultiThread() {
        Object object;
        Object object2;
        Object object3;
        Object object4;
        int n2 = this.coeffs.length;
        BigInteger bigInteger = this.squareSum().pow((n2 + 1) / 2);
        bigInteger = bigInteger.multiply(BigInteger.valueOf(2L).pow((this.degree() + 1) / 2));
        BigInteger bigInteger2 = bigInteger.multiply(BigInteger.valueOf(2L));
        BigInteger bigInteger3 = BigInteger.valueOf(10000L);
        BigInteger bigInteger4 = Constants.BIGINT_ONE;
        LinkedBlockingQueue<Object> linkedBlockingQueue = new LinkedBlockingQueue<Object>();
        Iterator iterator2 = BIGINT_PRIMES.iterator();
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        while (bigInteger4.compareTo(bigInteger2) < 0) {
            bigInteger3 = iterator2.hasNext() ? (BigInteger)iterator2.next() : bigInteger3.nextProbablePrime();
            object4 = executorService.submit(new ModResultantTask(bigInteger3.intValue()));
            linkedBlockingQueue.add(object4);
            bigInteger4 = bigInteger4.multiply(bigInteger3);
        }
        object4 = null;
        while (!linkedBlockingQueue.isEmpty()) {
            try {
                object3 = (Future)linkedBlockingQueue.take();
                object2 = (Future)linkedBlockingQueue.poll();
                if (object2 == null) {
                    object4 = (ModularResultant)object3.get();
                    break;
                }
                object = executorService.submit(new CombineTask((ModularResultant)object3.get(), (ModularResultant)object2.get()));
                linkedBlockingQueue.add(object);
            }
            catch (Exception exception) {
                throw new IllegalStateException(exception.toString());
            }
        }
        executorService.shutdown();
        object3 = ((ModularResultant)object4).res;
        object2 = ((ModularResultant)object4).rho;
        object = bigInteger4.divide(BigInteger.valueOf(2L));
        BigInteger bigInteger5 = ((BigInteger)object).negate();
        if (((BigInteger)object3).compareTo((BigInteger)object) > 0) {
            object3 = ((BigInteger)object3).subtract(bigInteger4);
        }
        if (((BigInteger)object3).compareTo(bigInteger5) < 0) {
            object3 = ((BigInteger)object3).add(bigInteger4);
        }
        for (int i2 = 0; i2 < n2; ++i2) {
            BigInteger bigInteger6 = ((BigIntPolynomial)object2).coeffs[i2];
            if (bigInteger6.compareTo((BigInteger)object) > 0) {
                ((BigIntPolynomial)object2).coeffs[i2] = bigInteger6.subtract(bigInteger4);
            }
            if (bigInteger6.compareTo(bigInteger5) >= 0) continue;
            ((BigIntPolynomial)object2).coeffs[i2] = bigInteger6.add(bigInteger4);
        }
        return new Resultant((BigIntPolynomial)object2, (BigInteger)object3);
    }

    public ModularResultant resultant(int n2) {
        int[] nArray = Arrays.copyOf(this.coeffs, this.coeffs.length + 1);
        IntegerPolynomial integerPolynomial = new IntegerPolynomial(nArray);
        int n3 = nArray.length;
        IntegerPolynomial integerPolynomial2 = new IntegerPolynomial(n3);
        integerPolynomial2.coeffs[0] = -1;
        integerPolynomial2.coeffs[n3 - 1] = 1;
        IntegerPolynomial integerPolynomial3 = new IntegerPolynomial(integerPolynomial.coeffs);
        IntegerPolynomial integerPolynomial4 = new IntegerPolynomial(n3);
        IntegerPolynomial integerPolynomial5 = new IntegerPolynomial(n3);
        integerPolynomial5.coeffs[0] = 1;
        int n4 = n3 - 1;
        int n5 = integerPolynomial3.degree();
        int n6 = n4;
        int n7 = 0;
        int n8 = 1;
        while (n5 > 0) {
            n7 = Util.invert(integerPolynomial3.coeffs[n5], n2);
            n7 = n7 * integerPolynomial2.coeffs[n4] % n2;
            integerPolynomial2.multShiftSub(integerPolynomial3, n7, n4 - n5, n2);
            integerPolynomial4.multShiftSub(integerPolynomial5, n7, n4 - n5, n2);
            n4 = integerPolynomial2.degree();
            if (n4 >= n5) continue;
            n8 *= Util.pow(integerPolynomial3.coeffs[n5], n6 - n4, n2);
            n8 %= n2;
            if (n6 % 2 == 1 && n5 % 2 == 1) {
                n8 = -n8 % n2;
            }
            IntegerPolynomial integerPolynomial6 = integerPolynomial2;
            integerPolynomial2 = integerPolynomial3;
            integerPolynomial3 = integerPolynomial6;
            int n9 = n4;
            n4 = n5;
            integerPolynomial6 = integerPolynomial4;
            integerPolynomial4 = integerPolynomial5;
            integerPolynomial5 = integerPolynomial6;
            n6 = n5;
            n5 = n9;
        }
        n8 *= Util.pow(integerPolynomial3.coeffs[0], n4, n2);
        n7 = Util.invert(integerPolynomial3.coeffs[0], n2);
        integerPolynomial5.mult(n7);
        integerPolynomial5.mod(n2);
        integerPolynomial5.mult(n8 %= n2);
        integerPolynomial5.mod(n2);
        integerPolynomial5.coeffs = Arrays.copyOf(integerPolynomial5.coeffs, integerPolynomial5.coeffs.length - 1);
        return new ModularResultant(new BigIntPolynomial(integerPolynomial5), BigInteger.valueOf(n8), BigInteger.valueOf(n2));
    }

    private void multShiftSub(IntegerPolynomial integerPolynomial, int n2, int n3, int n4) {
        int n5 = this.coeffs.length;
        for (int i2 = n3; i2 < n5; ++i2) {
            this.coeffs[i2] = (this.coeffs[i2] - integerPolynomial.coeffs[i2 - n3] * n2) % n4;
        }
    }

    private BigInteger squareSum() {
        BigInteger bigInteger = Constants.BIGINT_ZERO;
        for (int i2 = 0; i2 < this.coeffs.length; ++i2) {
            bigInteger = bigInteger.add(BigInteger.valueOf(this.coeffs[i2] * this.coeffs[i2]));
        }
        return bigInteger;
    }

    int degree() {
        int n2;
        for (n2 = this.coeffs.length - 1; n2 > 0 && this.coeffs[n2] == 0; --n2) {
        }
        return n2;
    }

    public void add(IntegerPolynomial integerPolynomial, int n2) {
        this.add(integerPolynomial);
        this.mod(n2);
    }

    public void add(IntegerPolynomial integerPolynomial) {
        if (integerPolynomial.coeffs.length > this.coeffs.length) {
            this.coeffs = Arrays.copyOf(this.coeffs, integerPolynomial.coeffs.length);
        }
        for (int i2 = 0; i2 < integerPolynomial.coeffs.length; ++i2) {
            int n2 = i2;
            this.coeffs[n2] = this.coeffs[n2] + integerPolynomial.coeffs[i2];
        }
    }

    public void sub(IntegerPolynomial integerPolynomial, int n2) {
        this.sub(integerPolynomial);
        this.mod(n2);
    }

    public void sub(IntegerPolynomial integerPolynomial) {
        if (integerPolynomial.coeffs.length > this.coeffs.length) {
            this.coeffs = Arrays.copyOf(this.coeffs, integerPolynomial.coeffs.length);
        }
        for (int i2 = 0; i2 < integerPolynomial.coeffs.length; ++i2) {
            int n2 = i2;
            this.coeffs[n2] = this.coeffs[n2] - integerPolynomial.coeffs[i2];
        }
    }

    void sub(int n2) {
        int n3 = 0;
        while (n3 < this.coeffs.length) {
            int n4 = n3++;
            this.coeffs[n4] = this.coeffs[n4] - n2;
        }
    }

    public void mult(int n2) {
        int n3 = 0;
        while (n3 < this.coeffs.length) {
            int n4 = n3++;
            this.coeffs[n4] = this.coeffs[n4] * n2;
        }
    }

    private void mult2(int n2) {
        int n3 = 0;
        while (n3 < this.coeffs.length) {
            int n4 = n3;
            this.coeffs[n4] = this.coeffs[n4] * 2;
            int n5 = n3++;
            this.coeffs[n5] = this.coeffs[n5] % n2;
        }
    }

    public void mult3(int n2) {
        int n3 = 0;
        while (n3 < this.coeffs.length) {
            int n4 = n3;
            this.coeffs[n4] = this.coeffs[n4] * 3;
            int n5 = n3++;
            this.coeffs[n5] = this.coeffs[n5] % n2;
        }
    }

    public void div(int n2) {
        int n3 = (n2 + 1) / 2;
        int n4 = 0;
        while (n4 < this.coeffs.length) {
            int n5 = n4;
            this.coeffs[n5] = this.coeffs[n5] + (this.coeffs[n4] > 0 ? n3 : -n3);
            int n6 = n4++;
            this.coeffs[n6] = this.coeffs[n6] / n2;
        }
    }

    public void mod3() {
        for (int i2 = 0; i2 < this.coeffs.length; ++i2) {
            int n2 = i2;
            this.coeffs[n2] = this.coeffs[n2] % 3;
            if (this.coeffs[i2] > 1) {
                int n3 = i2;
                this.coeffs[n3] = this.coeffs[n3] - 3;
            }
            if (this.coeffs[i2] >= -1) continue;
            int n4 = i2;
            this.coeffs[n4] = this.coeffs[n4] + 3;
        }
    }

    public void modPositive(int n2) {
        this.mod(n2);
        this.ensurePositive(n2);
    }

    void modCenter(int n2) {
        this.mod(n2);
        for (int i2 = 0; i2 < this.coeffs.length; ++i2) {
            while (this.coeffs[i2] < n2 / 2) {
                int n3 = i2;
                this.coeffs[n3] = this.coeffs[n3] + n2;
            }
            while (this.coeffs[i2] >= n2 / 2) {
                int n4 = i2;
                this.coeffs[n4] = this.coeffs[n4] - n2;
            }
        }
    }

    public void mod(int n2) {
        int n3 = 0;
        while (n3 < this.coeffs.length) {
            int n4 = n3++;
            this.coeffs[n4] = this.coeffs[n4] % n2;
        }
    }

    public void ensurePositive(int n2) {
        for (int i2 = 0; i2 < this.coeffs.length; ++i2) {
            while (this.coeffs[i2] < 0) {
                int n3 = i2;
                this.coeffs[n3] = this.coeffs[n3] + n2;
            }
        }
    }

    public long centeredNormSq(int n2) {
        int n3 = this.coeffs.length;
        IntegerPolynomial integerPolynomial = (IntegerPolynomial)this.clone();
        integerPolynomial.shiftGap(n2);
        long l2 = 0L;
        long l3 = 0L;
        for (int i2 = 0; i2 != integerPolynomial.coeffs.length; ++i2) {
            int n4 = integerPolynomial.coeffs[i2];
            l2 += (long)n4;
            l3 += (long)(n4 * n4);
        }
        long l4 = l3 - l2 * l2 / (long)n3;
        return l4;
    }

    void shiftGap(int n2) {
        int n3;
        int n4;
        this.modCenter(n2);
        int[] nArray = Arrays.clone(this.coeffs);
        this.sort(nArray);
        int n5 = 0;
        int n6 = 0;
        for (n4 = 0; n4 < nArray.length - 1; ++n4) {
            n3 = nArray[n4 + 1] - nArray[n4];
            if (n3 <= n5) continue;
            n5 = n3;
            n6 = nArray[n4];
        }
        n3 = nArray[nArray.length - 1];
        n4 = nArray[0];
        int n7 = n2 - n3 + n4;
        int n8 = n7 > n5 ? (n3 + n4) / 2 : n6 + n5 / 2 + n2 / 2;
        this.sub(n8);
    }

    private void sort(int[] nArray) {
        boolean bl = true;
        while (bl) {
            bl = false;
            for (int i2 = 0; i2 != nArray.length - 1; ++i2) {
                if (nArray[i2] <= nArray[i2 + 1]) continue;
                int n2 = nArray[i2];
                nArray[i2] = nArray[i2 + 1];
                nArray[i2 + 1] = n2;
                bl = true;
            }
        }
    }

    public void center0(int n2) {
        for (int i2 = 0; i2 < this.coeffs.length; ++i2) {
            while (this.coeffs[i2] < -n2 / 2) {
                int n3 = i2;
                this.coeffs[n3] = this.coeffs[n3] + n2;
            }
            while (this.coeffs[i2] > n2 / 2) {
                int n4 = i2;
                this.coeffs[n4] = this.coeffs[n4] - n2;
            }
        }
    }

    public int sumCoeffs() {
        int n2 = 0;
        for (int i2 = 0; i2 < this.coeffs.length; ++i2) {
            n2 += this.coeffs[i2];
        }
        return n2;
    }

    private boolean equalsZero() {
        for (int i2 = 0; i2 < this.coeffs.length; ++i2) {
            if (this.coeffs[i2] == 0) continue;
            return false;
        }
        return true;
    }

    public boolean equalsOne() {
        for (int i2 = 1; i2 < this.coeffs.length; ++i2) {
            if (this.coeffs[i2] == 0) continue;
            return false;
        }
        return this.coeffs[0] == 1;
    }

    private boolean equalsAbsOne() {
        for (int i2 = 1; i2 < this.coeffs.length; ++i2) {
            if (this.coeffs[i2] == 0) continue;
            return false;
        }
        return Math.abs(this.coeffs[0]) == 1;
    }

    public int count(int n2) {
        int n3 = 0;
        for (int i2 = 0; i2 != this.coeffs.length; ++i2) {
            if (this.coeffs[i2] != n2) continue;
            ++n3;
        }
        return n3;
    }

    public void rotate1() {
        int n2 = this.coeffs[this.coeffs.length - 1];
        for (int i2 = this.coeffs.length - 1; i2 > 0; --i2) {
            this.coeffs[i2] = this.coeffs[i2 - 1];
        }
        this.coeffs[0] = n2;
    }

    public void clear() {
        for (int i2 = 0; i2 < this.coeffs.length; ++i2) {
            this.coeffs[i2] = 0;
        }
    }

    @Override
    public IntegerPolynomial toIntegerPolynomial() {
        return (IntegerPolynomial)this.clone();
    }

    public Object clone() {
        return new IntegerPolynomial((int[])this.coeffs.clone());
    }

    public boolean equals(Object object) {
        if (object instanceof IntegerPolynomial) {
            return Arrays.areEqual(this.coeffs, ((IntegerPolynomial)object).coeffs);
        }
        return false;
    }

    static {
        for (int i2 = 0; i2 != PRIMES.length; ++i2) {
            BIGINT_PRIMES.add(BigInteger.valueOf(PRIMES[i2]));
        }
    }

    private static class CombineTask
    implements Callable<ModularResultant> {
        private ModularResultant modRes1;
        private ModularResultant modRes2;

        private CombineTask(ModularResultant modularResultant, ModularResultant modularResultant2) {
            this.modRes1 = modularResultant;
            this.modRes2 = modularResultant2;
        }

        @Override
        public ModularResultant call() {
            return ModularResultant.combineRho(this.modRes1, this.modRes2);
        }
    }

    private class ModResultantTask
    implements Callable<ModularResultant> {
        private int modulus;

        private ModResultantTask(int n2) {
            this.modulus = n2;
        }

        @Override
        public ModularResultant call() {
            return IntegerPolynomial.this.resultant(this.modulus);
        }
    }

    private static class PrimeGenerator {
        private int index = 0;
        private BigInteger prime;

        private PrimeGenerator() {
        }

        public BigInteger nextPrime() {
            this.prime = this.index < BIGINT_PRIMES.size() ? (BigInteger)BIGINT_PRIMES.get(this.index++) : this.prime.nextProbablePrime();
            return this.prime;
        }
    }
}

