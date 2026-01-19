/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.constraints;

import java.math.BigInteger;
import org.bouncycastle.math.ec.ECCurve;

public class ConstraintUtils {
    public static int bitsOfSecurityFor(BigInteger bigInteger) {
        return ConstraintUtils.bitsOfSecurityForFF(bigInteger.bitLength());
    }

    public static int bitsOfSecurityFor(ECCurve eCCurve) {
        int n2 = (eCCurve.getFieldSize() + 1) / 2;
        return n2 > 256 ? 256 : n2;
    }

    public static int bitsOfSecurityForFF(int n2) {
        if (n2 >= 2048) {
            return n2 >= 3072 ? (n2 >= 7680 ? (n2 >= 15360 ? 256 : 192) : 128) : 112;
        }
        return n2 >= 1024 ? 80 : 20;
    }
}

