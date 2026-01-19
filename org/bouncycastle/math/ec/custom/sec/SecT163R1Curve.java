/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.math.ec.custom.sec;

import java.math.BigInteger;
import org.bouncycastle.math.ec.AbstractECLookupTable;
import org.bouncycastle.math.ec.ECConstants;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.ec.ECLookupTable;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.custom.sec.SecT163FieldElement;
import org.bouncycastle.math.ec.custom.sec.SecT163R1Point;
import org.bouncycastle.math.raw.Nat192;
import org.bouncycastle.util.encoders.Hex;

public class SecT163R1Curve
extends ECCurve.AbstractF2m {
    private static final int SECT163R1_DEFAULT_COORDS = 6;
    private static final ECFieldElement[] SECT163R1_AFFINE_ZS = new ECFieldElement[]{new SecT163FieldElement(ECConstants.ONE)};
    protected SecT163R1Point infinity = new SecT163R1Point(this, null, null);

    public SecT163R1Curve() {
        super(163, 3, 6, 7);
        this.a = this.fromBigInteger(new BigInteger(1, Hex.decodeStrict("07B6882CAAEFA84F9554FF8428BD88E246D2782AE2")));
        this.b = this.fromBigInteger(new BigInteger(1, Hex.decodeStrict("0713612DCDDCB40AAB946BDA29CA91F73AF958AFD9")));
        this.order = new BigInteger(1, Hex.decodeStrict("03FFFFFFFFFFFFFFFFFFFF48AAB689C29CA710279B"));
        this.cofactor = BigInteger.valueOf(2L);
        this.coord = 6;
    }

    @Override
    protected ECCurve cloneCurve() {
        return new SecT163R1Curve();
    }

    @Override
    public boolean supportsCoordinateSystem(int n2) {
        switch (n2) {
            case 6: {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getFieldSize() {
        return 163;
    }

    @Override
    public ECFieldElement fromBigInteger(BigInteger bigInteger) {
        return new SecT163FieldElement(bigInteger);
    }

    @Override
    protected ECPoint createRawPoint(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
        return new SecT163R1Point(this, eCFieldElement, eCFieldElement2);
    }

    @Override
    protected ECPoint createRawPoint(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement[] eCFieldElementArray) {
        return new SecT163R1Point(this, eCFieldElement, eCFieldElement2, eCFieldElementArray);
    }

    @Override
    public ECPoint getInfinity() {
        return this.infinity;
    }

    @Override
    public boolean isKoblitz() {
        return false;
    }

    public int getM() {
        return 163;
    }

    public boolean isTrinomial() {
        return false;
    }

    public int getK1() {
        return 3;
    }

    public int getK2() {
        return 6;
    }

    public int getK3() {
        return 7;
    }

    @Override
    public ECLookupTable createCacheSafeLookupTable(ECPoint[] eCPointArray, int n2, final int n3) {
        final long[] lArray = new long[n3 * 3 * 2];
        int n4 = 0;
        for (int i2 = 0; i2 < n3; ++i2) {
            ECPoint eCPoint = eCPointArray[n2 + i2];
            Nat192.copy64(((SecT163FieldElement)eCPoint.getRawXCoord()).x, 0, lArray, n4);
            Nat192.copy64(((SecT163FieldElement)eCPoint.getRawYCoord()).x, 0, lArray, n4 += 3);
            n4 += 3;
        }
        return new AbstractECLookupTable(){

            @Override
            public int getSize() {
                return n3;
            }

            @Override
            public ECPoint lookup(int n2) {
                long[] lArray3 = Nat192.create64();
                long[] lArray2 = Nat192.create64();
                int n32 = 0;
                for (int i2 = 0; i2 < n3; ++i2) {
                    long l2 = (i2 ^ n2) - 1 >> 31;
                    for (int i3 = 0; i3 < 3; ++i3) {
                        int n4 = i3;
                        lArray3[n4] = lArray3[n4] ^ lArray[n32 + i3] & l2;
                        int n5 = i3;
                        lArray2[n5] = lArray2[n5] ^ lArray[n32 + 3 + i3] & l2;
                    }
                    n32 += 6;
                }
                return this.createPoint(lArray3, lArray2);
            }

            @Override
            public ECPoint lookupVar(int n2) {
                long[] lArray3 = Nat192.create64();
                long[] lArray2 = Nat192.create64();
                int n32 = n2 * 3 * 2;
                for (int i2 = 0; i2 < 3; ++i2) {
                    lArray3[i2] = lArray[n32 + i2];
                    lArray2[i2] = lArray[n32 + 3 + i2];
                }
                return this.createPoint(lArray3, lArray2);
            }

            private ECPoint createPoint(long[] lArray3, long[] lArray2) {
                return SecT163R1Curve.this.createRawPoint(new SecT163FieldElement(lArray3), new SecT163FieldElement(lArray2), SECT163R1_AFFINE_ZS);
            }
        };
    }
}

