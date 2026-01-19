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
import org.bouncycastle.math.ec.custom.sec.SecT113FieldElement;
import org.bouncycastle.math.ec.custom.sec.SecT113R1Point;
import org.bouncycastle.math.raw.Nat128;
import org.bouncycastle.util.encoders.Hex;

public class SecT113R1Curve
extends ECCurve.AbstractF2m {
    private static final int SECT113R1_DEFAULT_COORDS = 6;
    private static final ECFieldElement[] SECT113R1_AFFINE_ZS = new ECFieldElement[]{new SecT113FieldElement(ECConstants.ONE)};
    protected SecT113R1Point infinity = new SecT113R1Point(this, null, null);

    public SecT113R1Curve() {
        super(113, 9, 0, 0);
        this.a = this.fromBigInteger(new BigInteger(1, Hex.decodeStrict("003088250CA6E7C7FE649CE85820F7")));
        this.b = this.fromBigInteger(new BigInteger(1, Hex.decodeStrict("00E8BEE4D3E2260744188BE0E9C723")));
        this.order = new BigInteger(1, Hex.decodeStrict("0100000000000000D9CCEC8A39E56F"));
        this.cofactor = BigInteger.valueOf(2L);
        this.coord = 6;
    }

    @Override
    protected ECCurve cloneCurve() {
        return new SecT113R1Curve();
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
        return 113;
    }

    @Override
    public ECFieldElement fromBigInteger(BigInteger bigInteger) {
        return new SecT113FieldElement(bigInteger);
    }

    @Override
    protected ECPoint createRawPoint(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
        return new SecT113R1Point(this, eCFieldElement, eCFieldElement2);
    }

    @Override
    protected ECPoint createRawPoint(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement[] eCFieldElementArray) {
        return new SecT113R1Point(this, eCFieldElement, eCFieldElement2, eCFieldElementArray);
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
        return 113;
    }

    public boolean isTrinomial() {
        return true;
    }

    public int getK1() {
        return 9;
    }

    public int getK2() {
        return 0;
    }

    public int getK3() {
        return 0;
    }

    @Override
    public ECLookupTable createCacheSafeLookupTable(ECPoint[] eCPointArray, int n2, final int n3) {
        final long[] lArray = new long[n3 * 2 * 2];
        int n4 = 0;
        for (int i2 = 0; i2 < n3; ++i2) {
            ECPoint eCPoint = eCPointArray[n2 + i2];
            Nat128.copy64(((SecT113FieldElement)eCPoint.getRawXCoord()).x, 0, lArray, n4);
            Nat128.copy64(((SecT113FieldElement)eCPoint.getRawYCoord()).x, 0, lArray, n4 += 2);
            n4 += 2;
        }
        return new AbstractECLookupTable(){

            @Override
            public int getSize() {
                return n3;
            }

            @Override
            public ECPoint lookup(int n2) {
                long[] lArray3 = Nat128.create64();
                long[] lArray2 = Nat128.create64();
                int n32 = 0;
                for (int i2 = 0; i2 < n3; ++i2) {
                    long l2 = (i2 ^ n2) - 1 >> 31;
                    for (int i3 = 0; i3 < 2; ++i3) {
                        int n4 = i3;
                        lArray3[n4] = lArray3[n4] ^ lArray[n32 + i3] & l2;
                        int n5 = i3;
                        lArray2[n5] = lArray2[n5] ^ lArray[n32 + 2 + i3] & l2;
                    }
                    n32 += 4;
                }
                return this.createPoint(lArray3, lArray2);
            }

            @Override
            public ECPoint lookupVar(int n2) {
                long[] lArray3 = Nat128.create64();
                long[] lArray2 = Nat128.create64();
                int n32 = n2 * 2 * 2;
                for (int i2 = 0; i2 < 2; ++i2) {
                    lArray3[i2] = lArray[n32 + i2];
                    lArray2[i2] = lArray[n32 + 2 + i2];
                }
                return this.createPoint(lArray3, lArray2);
            }

            private ECPoint createPoint(long[] lArray3, long[] lArray2) {
                return SecT113R1Curve.this.createRawPoint(new SecT113FieldElement(lArray3), new SecT113FieldElement(lArray2), SECT113R1_AFFINE_ZS);
            }
        };
    }
}

