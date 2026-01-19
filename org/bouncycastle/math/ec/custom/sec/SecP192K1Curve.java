/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.math.ec.custom.sec;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle.math.ec.AbstractECLookupTable;
import org.bouncycastle.math.ec.ECConstants;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.ec.ECLookupTable;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.custom.sec.SecP192K1Field;
import org.bouncycastle.math.ec.custom.sec.SecP192K1FieldElement;
import org.bouncycastle.math.ec.custom.sec.SecP192K1Point;
import org.bouncycastle.math.raw.Nat192;
import org.bouncycastle.util.encoders.Hex;

public class SecP192K1Curve
extends ECCurve.AbstractFp {
    public static final BigInteger q = SecP192K1FieldElement.Q;
    private static final int SECP192K1_DEFAULT_COORDS = 2;
    private static final ECFieldElement[] SECP192K1_AFFINE_ZS = new ECFieldElement[]{new SecP192K1FieldElement(ECConstants.ONE)};
    protected SecP192K1Point infinity = new SecP192K1Point(this, null, null);

    public SecP192K1Curve() {
        super(q);
        this.a = this.fromBigInteger(ECConstants.ZERO);
        this.b = this.fromBigInteger(BigInteger.valueOf(3L));
        this.order = new BigInteger(1, Hex.decodeStrict("FFFFFFFFFFFFFFFFFFFFFFFE26F2FC170F69466A74DEFD8D"));
        this.cofactor = BigInteger.valueOf(1L);
        this.coord = 2;
    }

    @Override
    protected ECCurve cloneCurve() {
        return new SecP192K1Curve();
    }

    @Override
    public boolean supportsCoordinateSystem(int n2) {
        switch (n2) {
            case 2: {
                return true;
            }
        }
        return false;
    }

    public BigInteger getQ() {
        return q;
    }

    @Override
    public int getFieldSize() {
        return q.bitLength();
    }

    @Override
    public ECFieldElement fromBigInteger(BigInteger bigInteger) {
        return new SecP192K1FieldElement(bigInteger);
    }

    @Override
    protected ECPoint createRawPoint(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
        return new SecP192K1Point(this, eCFieldElement, eCFieldElement2);
    }

    @Override
    protected ECPoint createRawPoint(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement[] eCFieldElementArray) {
        return new SecP192K1Point(this, eCFieldElement, eCFieldElement2, eCFieldElementArray);
    }

    @Override
    public ECPoint getInfinity() {
        return this.infinity;
    }

    @Override
    public ECLookupTable createCacheSafeLookupTable(ECPoint[] eCPointArray, int n2, final int n3) {
        final int[] nArray = new int[n3 * 6 * 2];
        int n4 = 0;
        for (int i2 = 0; i2 < n3; ++i2) {
            ECPoint eCPoint = eCPointArray[n2 + i2];
            Nat192.copy(((SecP192K1FieldElement)eCPoint.getRawXCoord()).x, 0, nArray, n4);
            Nat192.copy(((SecP192K1FieldElement)eCPoint.getRawYCoord()).x, 0, nArray, n4 += 6);
            n4 += 6;
        }
        return new AbstractECLookupTable(){

            @Override
            public int getSize() {
                return n3;
            }

            @Override
            public ECPoint lookup(int n2) {
                int[] nArray3 = Nat192.create();
                int[] nArray2 = Nat192.create();
                int n32 = 0;
                for (int i2 = 0; i2 < n3; ++i2) {
                    int n4 = (i2 ^ n2) - 1 >> 31;
                    for (int i3 = 0; i3 < 6; ++i3) {
                        int n5 = i3;
                        nArray3[n5] = nArray3[n5] ^ nArray[n32 + i3] & n4;
                        int n6 = i3;
                        nArray2[n6] = nArray2[n6] ^ nArray[n32 + 6 + i3] & n4;
                    }
                    n32 += 12;
                }
                return this.createPoint(nArray3, nArray2);
            }

            @Override
            public ECPoint lookupVar(int n2) {
                int[] nArray3 = Nat192.create();
                int[] nArray2 = Nat192.create();
                int n32 = n2 * 6 * 2;
                for (int i2 = 0; i2 < 6; ++i2) {
                    nArray3[i2] = nArray[n32 + i2];
                    nArray2[i2] = nArray[n32 + 6 + i2];
                }
                return this.createPoint(nArray3, nArray2);
            }

            private ECPoint createPoint(int[] nArray3, int[] nArray2) {
                return SecP192K1Curve.this.createRawPoint(new SecP192K1FieldElement(nArray3), new SecP192K1FieldElement(nArray2), SECP192K1_AFFINE_ZS);
            }
        };
    }

    @Override
    public ECFieldElement randomFieldElement(SecureRandom secureRandom) {
        int[] nArray = Nat192.create();
        SecP192K1Field.random(secureRandom, nArray);
        return new SecP192K1FieldElement(nArray);
    }

    @Override
    public ECFieldElement randomFieldElementMult(SecureRandom secureRandom) {
        int[] nArray = Nat192.create();
        SecP192K1Field.randomMult(secureRandom, nArray);
        return new SecP192K1FieldElement(nArray);
    }
}

