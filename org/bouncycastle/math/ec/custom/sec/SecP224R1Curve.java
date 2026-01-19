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
import org.bouncycastle.math.ec.custom.sec.SecP224R1Field;
import org.bouncycastle.math.ec.custom.sec.SecP224R1FieldElement;
import org.bouncycastle.math.ec.custom.sec.SecP224R1Point;
import org.bouncycastle.math.raw.Nat224;
import org.bouncycastle.util.encoders.Hex;

public class SecP224R1Curve
extends ECCurve.AbstractFp {
    public static final BigInteger q = SecP224R1FieldElement.Q;
    private static final int SECP224R1_DEFAULT_COORDS = 2;
    private static final ECFieldElement[] SECP224R1_AFFINE_ZS = new ECFieldElement[]{new SecP224R1FieldElement(ECConstants.ONE)};
    protected SecP224R1Point infinity = new SecP224R1Point(this, null, null);

    public SecP224R1Curve() {
        super(q);
        this.a = this.fromBigInteger(new BigInteger(1, Hex.decodeStrict("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFE")));
        this.b = this.fromBigInteger(new BigInteger(1, Hex.decodeStrict("B4050A850C04B3ABF54132565044B0B7D7BFD8BA270B39432355FFB4")));
        this.order = new BigInteger(1, Hex.decodeStrict("FFFFFFFFFFFFFFFFFFFFFFFFFFFF16A2E0B8F03E13DD29455C5C2A3D"));
        this.cofactor = BigInteger.valueOf(1L);
        this.coord = 2;
    }

    @Override
    protected ECCurve cloneCurve() {
        return new SecP224R1Curve();
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
        return new SecP224R1FieldElement(bigInteger);
    }

    @Override
    protected ECPoint createRawPoint(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
        return new SecP224R1Point(this, eCFieldElement, eCFieldElement2);
    }

    @Override
    protected ECPoint createRawPoint(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement[] eCFieldElementArray) {
        return new SecP224R1Point(this, eCFieldElement, eCFieldElement2, eCFieldElementArray);
    }

    @Override
    public ECPoint getInfinity() {
        return this.infinity;
    }

    @Override
    public ECLookupTable createCacheSafeLookupTable(ECPoint[] eCPointArray, int n2, final int n3) {
        final int[] nArray = new int[n3 * 7 * 2];
        int n4 = 0;
        for (int i2 = 0; i2 < n3; ++i2) {
            ECPoint eCPoint = eCPointArray[n2 + i2];
            Nat224.copy(((SecP224R1FieldElement)eCPoint.getRawXCoord()).x, 0, nArray, n4);
            Nat224.copy(((SecP224R1FieldElement)eCPoint.getRawYCoord()).x, 0, nArray, n4 += 7);
            n4 += 7;
        }
        return new AbstractECLookupTable(){

            @Override
            public int getSize() {
                return n3;
            }

            @Override
            public ECPoint lookup(int n2) {
                int[] nArray3 = Nat224.create();
                int[] nArray2 = Nat224.create();
                int n32 = 0;
                for (int i2 = 0; i2 < n3; ++i2) {
                    int n4 = (i2 ^ n2) - 1 >> 31;
                    for (int i3 = 0; i3 < 7; ++i3) {
                        int n5 = i3;
                        nArray3[n5] = nArray3[n5] ^ nArray[n32 + i3] & n4;
                        int n6 = i3;
                        nArray2[n6] = nArray2[n6] ^ nArray[n32 + 7 + i3] & n4;
                    }
                    n32 += 14;
                }
                return this.createPoint(nArray3, nArray2);
            }

            @Override
            public ECPoint lookupVar(int n2) {
                int[] nArray3 = Nat224.create();
                int[] nArray2 = Nat224.create();
                int n32 = n2 * 7 * 2;
                for (int i2 = 0; i2 < 7; ++i2) {
                    nArray3[i2] = nArray[n32 + i2];
                    nArray2[i2] = nArray[n32 + 7 + i2];
                }
                return this.createPoint(nArray3, nArray2);
            }

            private ECPoint createPoint(int[] nArray3, int[] nArray2) {
                return SecP224R1Curve.this.createRawPoint(new SecP224R1FieldElement(nArray3), new SecP224R1FieldElement(nArray2), SECP224R1_AFFINE_ZS);
            }
        };
    }

    @Override
    public ECFieldElement randomFieldElement(SecureRandom secureRandom) {
        int[] nArray = Nat224.create();
        SecP224R1Field.random(secureRandom, nArray);
        return new SecP224R1FieldElement(nArray);
    }

    @Override
    public ECFieldElement randomFieldElementMult(SecureRandom secureRandom) {
        int[] nArray = Nat224.create();
        SecP224R1Field.randomMult(secureRandom, nArray);
        return new SecP224R1FieldElement(nArray);
    }
}

