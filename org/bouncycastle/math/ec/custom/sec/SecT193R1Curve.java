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
import org.bouncycastle.math.ec.custom.sec.SecT193FieldElement;
import org.bouncycastle.math.ec.custom.sec.SecT193R1Point;
import org.bouncycastle.math.raw.Nat256;
import org.bouncycastle.util.encoders.Hex;

public class SecT193R1Curve
extends ECCurve.AbstractF2m {
    private static final int SECT193R1_DEFAULT_COORDS = 6;
    private static final ECFieldElement[] SECT193R1_AFFINE_ZS = new ECFieldElement[]{new SecT193FieldElement(ECConstants.ONE)};
    protected SecT193R1Point infinity = new SecT193R1Point(this, null, null);

    public SecT193R1Curve() {
        super(193, 15, 0, 0);
        this.a = this.fromBigInteger(new BigInteger(1, Hex.decodeStrict("0017858FEB7A98975169E171F77B4087DE098AC8A911DF7B01")));
        this.b = this.fromBigInteger(new BigInteger(1, Hex.decodeStrict("00FDFB49BFE6C3A89FACADAA7A1E5BBC7CC1C2E5D831478814")));
        this.order = new BigInteger(1, Hex.decodeStrict("01000000000000000000000000C7F34A778F443ACC920EBA49"));
        this.cofactor = BigInteger.valueOf(2L);
        this.coord = 6;
    }

    @Override
    protected ECCurve cloneCurve() {
        return new SecT193R1Curve();
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
        return 193;
    }

    @Override
    public ECFieldElement fromBigInteger(BigInteger bigInteger) {
        return new SecT193FieldElement(bigInteger);
    }

    @Override
    protected ECPoint createRawPoint(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
        return new SecT193R1Point(this, eCFieldElement, eCFieldElement2);
    }

    @Override
    protected ECPoint createRawPoint(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement[] eCFieldElementArray) {
        return new SecT193R1Point(this, eCFieldElement, eCFieldElement2, eCFieldElementArray);
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
        return 193;
    }

    public boolean isTrinomial() {
        return true;
    }

    public int getK1() {
        return 15;
    }

    public int getK2() {
        return 0;
    }

    public int getK3() {
        return 0;
    }

    @Override
    public ECLookupTable createCacheSafeLookupTable(ECPoint[] eCPointArray, int n2, final int n3) {
        final long[] lArray = new long[n3 * 4 * 2];
        int n4 = 0;
        for (int i2 = 0; i2 < n3; ++i2) {
            ECPoint eCPoint = eCPointArray[n2 + i2];
            Nat256.copy64(((SecT193FieldElement)eCPoint.getRawXCoord()).x, 0, lArray, n4);
            Nat256.copy64(((SecT193FieldElement)eCPoint.getRawYCoord()).x, 0, lArray, n4 += 4);
            n4 += 4;
        }
        return new AbstractECLookupTable(){

            @Override
            public int getSize() {
                return n3;
            }

            @Override
            public ECPoint lookup(int n2) {
                long[] lArray3 = Nat256.create64();
                long[] lArray2 = Nat256.create64();
                int n32 = 0;
                for (int i2 = 0; i2 < n3; ++i2) {
                    long l2 = (i2 ^ n2) - 1 >> 31;
                    for (int i3 = 0; i3 < 4; ++i3) {
                        int n4 = i3;
                        lArray3[n4] = lArray3[n4] ^ lArray[n32 + i3] & l2;
                        int n5 = i3;
                        lArray2[n5] = lArray2[n5] ^ lArray[n32 + 4 + i3] & l2;
                    }
                    n32 += 8;
                }
                return this.createPoint(lArray3, lArray2);
            }

            @Override
            public ECPoint lookupVar(int n2) {
                long[] lArray3 = Nat256.create64();
                long[] lArray2 = Nat256.create64();
                int n32 = n2 * 4 * 2;
                for (int i2 = 0; i2 < 4; ++i2) {
                    lArray3[i2] = lArray[n32 + i2];
                    lArray2[i2] = lArray[n32 + 4 + i2];
                }
                return this.createPoint(lArray3, lArray2);
            }

            private ECPoint createPoint(long[] lArray3, long[] lArray2) {
                return SecT193R1Curve.this.createRawPoint(new SecT193FieldElement(lArray3), new SecT193FieldElement(lArray2), SECT193R1_AFFINE_ZS);
            }
        };
    }
}

