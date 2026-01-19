/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.generators;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle.crypto.generators.DHParametersHelper;
import org.bouncycastle.crypto.params.ElGamalParameters;

public class ElGamalParametersGenerator {
    private int size;
    private int certainty;
    private SecureRandom random;

    public void init(int n2, int n3, SecureRandom secureRandom) {
        this.size = n2;
        this.certainty = n3;
        this.random = secureRandom;
    }

    public ElGamalParameters generateParameters() {
        BigInteger[] bigIntegerArray = DHParametersHelper.generateSafePrimes(this.size, this.certainty, this.random);
        BigInteger bigInteger = bigIntegerArray[0];
        BigInteger bigInteger2 = bigIntegerArray[1];
        BigInteger bigInteger3 = DHParametersHelper.selectGenerator(bigInteger, bigInteger2, this.random);
        return new ElGamalParameters(bigInteger, bigInteger3);
    }
}

