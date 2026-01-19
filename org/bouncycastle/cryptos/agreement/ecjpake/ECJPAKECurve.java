/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.agreement.ecjpake;

import java.math.BigInteger;
import org.bouncycastle.crypto.agreement.ecjpake.ECJPAKEUtil;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;

public class ECJPAKECurve {
    private final ECCurve.Fp curve;
    private final ECPoint g;

    public ECJPAKECurve(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, BigInteger bigInteger4, BigInteger bigInteger5, BigInteger bigInteger6, BigInteger bigInteger7) {
        ECJPAKEUtil.validateNotNull(bigInteger2, "a");
        ECJPAKEUtil.validateNotNull(bigInteger3, "b");
        ECJPAKEUtil.validateNotNull(bigInteger, "q");
        ECJPAKEUtil.validateNotNull(bigInteger4, "n");
        ECJPAKEUtil.validateNotNull(bigInteger5, "h");
        ECJPAKEUtil.validateNotNull(bigInteger6, "g_x");
        ECJPAKEUtil.validateNotNull(bigInteger7, "g_y");
        if (!bigInteger.isProbablePrime(20)) {
            throw new IllegalArgumentException("Field size q must be prime");
        }
        if (bigInteger2.compareTo(BigInteger.ZERO) < 0 || bigInteger2.compareTo(bigInteger) >= 0) {
            throw new IllegalArgumentException("The parameter 'a' is not in the field [0, q-1]");
        }
        if (bigInteger3.compareTo(BigInteger.ZERO) < 0 || bigInteger3.compareTo(bigInteger) >= 0) {
            throw new IllegalArgumentException("The parameter 'b' is not in the field [0, q-1]");
        }
        BigInteger bigInteger8 = ECJPAKECurve.calculateDeterminant(bigInteger, bigInteger2, bigInteger3);
        if (bigInteger8.equals(BigInteger.ZERO)) {
            throw new IllegalArgumentException("The curve is singular, i.e the discriminant is equal to 0 mod q.");
        }
        if (!bigInteger4.isProbablePrime(20)) {
            throw new IllegalArgumentException("The order n must be prime");
        }
        ECCurve.Fp fp = new ECCurve.Fp(bigInteger, bigInteger2, bigInteger3, bigInteger4, bigInteger5);
        ECPoint eCPoint = fp.createPoint(bigInteger6, bigInteger7);
        if (!eCPoint.isValid()) {
            throw new IllegalArgumentException("The base point G does not lie on the curve.");
        }
        this.curve = fp;
        this.g = eCPoint;
    }

    ECJPAKECurve(ECCurve.Fp fp, ECPoint eCPoint) {
        ECJPAKEUtil.validateNotNull(fp, "curve");
        ECJPAKEUtil.validateNotNull(eCPoint, "g");
        ECJPAKEUtil.validateNotNull(fp.getOrder(), "n");
        ECJPAKEUtil.validateNotNull(fp.getCofactor(), "h");
        this.curve = fp;
        this.g = eCPoint;
    }

    public ECCurve.Fp getCurve() {
        return this.curve;
    }

    public ECPoint getG() {
        return this.g;
    }

    public BigInteger getA() {
        return this.curve.getA().toBigInteger();
    }

    public BigInteger getB() {
        return this.curve.getB().toBigInteger();
    }

    public BigInteger getN() {
        return this.curve.getOrder();
    }

    public BigInteger getH() {
        return this.curve.getCofactor();
    }

    public BigInteger getQ() {
        return this.curve.getQ();
    }

    private static BigInteger calculateDeterminant(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3) {
        BigInteger bigInteger4 = bigInteger2.multiply(bigInteger2).mod(bigInteger).multiply(bigInteger2).mod(bigInteger).shiftLeft(2);
        BigInteger bigInteger5 = bigInteger3.multiply(bigInteger3).mod(bigInteger).multiply(BigInteger.valueOf(27L));
        return bigInteger4.add(bigInteger5).mod(bigInteger);
    }
}

