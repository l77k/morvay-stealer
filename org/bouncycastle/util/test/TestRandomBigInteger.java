/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.util.test;

import java.math.BigInteger;
import org.bouncycastle.util.BigIntegers;
import org.bouncycastle.util.test.FixedSecureRandom;

public class TestRandomBigInteger
extends FixedSecureRandom {
    public TestRandomBigInteger(String string) {
        this(string, 10);
    }

    public TestRandomBigInteger(String string, int n2) {
        super(new FixedSecureRandom.Source[]{new FixedSecureRandom.BigInteger(BigIntegers.asUnsignedByteArray(new BigInteger(string, n2)))});
    }

    public TestRandomBigInteger(byte[] byArray) {
        super(new FixedSecureRandom.Source[]{new FixedSecureRandom.BigInteger(byArray)});
    }

    public TestRandomBigInteger(int n2, byte[] byArray) {
        super(new FixedSecureRandom.Source[]{new FixedSecureRandom.BigInteger(n2, byArray)});
    }
}

