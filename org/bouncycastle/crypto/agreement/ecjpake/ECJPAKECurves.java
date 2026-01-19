/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.agreement.ecjpake;

import org.bouncycastle.asn1.nist.NISTNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.agreement.ecjpake.ECJPAKECurve;
import org.bouncycastle.math.ec.ECCurve;

public class ECJPAKECurves {
    public static final ECJPAKECurve NIST_P256 = ECJPAKECurves.fromX9ECParameters(NISTNamedCurves.getByName("P-256"));
    public static final ECJPAKECurve NIST_P384 = ECJPAKECurves.fromX9ECParameters(NISTNamedCurves.getByName("P-384"));
    public static final ECJPAKECurve NIST_P521 = ECJPAKECurves.fromX9ECParameters(NISTNamedCurves.getByName("P-521"));

    private static ECJPAKECurve fromX9ECParameters(X9ECParameters x9ECParameters) {
        return new ECJPAKECurve((ECCurve.Fp)x9ECParameters.getCurve(), x9ECParameters.getG());
    }
}

