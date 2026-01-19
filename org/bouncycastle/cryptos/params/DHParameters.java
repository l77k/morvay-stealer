/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.params;

import java.math.BigInteger;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.params.DHValidationParameters;
import org.bouncycastle.util.Properties;

public class DHParameters
implements CipherParameters {
    private static final int DEFAULT_MINIMUM_LENGTH = 160;
    private BigInteger g;
    private BigInteger p;
    private BigInteger q;
    private BigInteger j;
    private int m;
    private int l;
    private DHValidationParameters validation;

    private static int getDefaultMParam(int n2) {
        if (n2 == 0) {
            return 160;
        }
        return n2 < 160 ? n2 : 160;
    }

    public DHParameters(BigInteger bigInteger, BigInteger bigInteger2) {
        this(bigInteger, bigInteger2, null, 0);
    }

    public DHParameters(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3) {
        this(bigInteger, bigInteger2, bigInteger3, 0);
    }

    public DHParameters(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, int n2) {
        this(bigInteger, bigInteger2, bigInteger3, DHParameters.getDefaultMParam(n2), n2, null, null);
    }

    public DHParameters(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, int n2, int n3) {
        this(bigInteger, bigInteger2, bigInteger3, n2, n3, null, null);
    }

    public DHParameters(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, BigInteger bigInteger4, DHValidationParameters dHValidationParameters) {
        this(bigInteger, bigInteger2, bigInteger3, 160, 0, bigInteger4, dHValidationParameters);
    }

    public DHParameters(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, int n2, int n3, BigInteger bigInteger4, DHValidationParameters dHValidationParameters) {
        if (n3 != 0) {
            if (n3 > bigInteger.bitLength()) {
                throw new IllegalArgumentException("when l value specified, it must satisfy 2^(l-1) <= p");
            }
            if (n3 < n2) {
                throw new IllegalArgumentException("when l value specified, it may not be less than m value");
            }
        }
        if (n2 > bigInteger.bitLength() && !Properties.isOverrideSet("org.bouncycastle.dh.allow_unsafe_p_value")) {
            throw new IllegalArgumentException("unsafe p value so small specific l required");
        }
        this.g = bigInteger2;
        this.p = bigInteger;
        this.q = bigInteger3;
        this.m = n2;
        this.l = n3;
        this.j = bigInteger4;
        this.validation = dHValidationParameters;
    }

    public BigInteger getP() {
        return this.p;
    }

    public BigInteger getG() {
        return this.g;
    }

    public BigInteger getQ() {
        return this.q;
    }

    public BigInteger getJ() {
        return this.j;
    }

    public int getM() {
        return this.m;
    }

    public int getL() {
        return this.l;
    }

    public DHValidationParameters getValidationParameters() {
        return this.validation;
    }

    public boolean equals(Object object) {
        if (!(object instanceof DHParameters)) {
            return false;
        }
        DHParameters dHParameters = (DHParameters)object;
        if (this.getQ() != null ? !this.getQ().equals(dHParameters.getQ()) : dHParameters.getQ() != null) {
            return false;
        }
        return dHParameters.getP().equals(this.p) && dHParameters.getG().equals(this.g);
    }

    public int hashCode() {
        return this.getP().hashCode() ^ this.getG().hashCode() ^ (this.getQ() != null ? this.getQ().hashCode() : 0);
    }
}

