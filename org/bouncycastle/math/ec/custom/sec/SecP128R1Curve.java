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
import org.bouncycastle.math.ec.custom.sec.SecP128R1Field;
import org.bouncycastle.math.ec.custom.sec.SecP128R1FieldElement;
import org.bouncycastle.math.ec.custom.sec.SecP128R1Point;
import org.bouncycastle.math.raw.Nat128;
import org.bouncycastle.util.encoders.Hex;

public class SecP128R1Curve
extends ECCurve.AbstractFp {
    public static final BigInteger q = SecP128R1FieldElement.Q;
    private static final int SECP128R1_DEFAULT_COORDS = 2;
    private static final ECFieldElement[] SECP128R1_AFFINE_ZS = new ECFieldElement[]{new SecP128R1FieldElement(ECConstants.ONE)};
    protected SecP128R1Point infinity = new SecP128R1Point(this, null, null);

    public SecP128R1Curve() {
        super(q);
        this.a = this.fromBigInteger(new BigInteger(1, Hex.decodeStrict("FFFFFFFDFFFFFFFFFFFFFFFFFFFFFFFC")));
        this.b = this.fromBigInteger(new BigInteger(1, Hex.decodeStrict("E87579C11079F43DD824993C2CEE5ED3")));
        this.order = new BigInteger(1, Hex.decodeStrict("FFFFFFFE0000000075A30D1B9038A115"));
        this.cofactor = BigInteger.valueOf(1L);
        this.coord = 2;
    }

    @Override
    protected ECCurve cloneCurve() {
        return new SecP128R1Curve();
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
        return new SecP128R1FieldElement(bigInteger);
    }

    @Override
    protected ECPoint createRawPoint(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
        return new SecP128R1Point(this, eCFieldElement, eCFieldElement2);
    }

    @Override
    protected ECPoint createRawPoint(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement[] eCFieldElementArray) {
        return new SecP128R1Point(this, eCFieldElement, eCFieldElement2, eCFieldElementArray);
    }

    @Override
    public ECPoint getInfinity() {
        return this.infinity;
    }

    @Override
    public ECLookupTable createCacheSafeLookupTable(ECPoint[] eCPointArray, int n2, final int n3) {
        final int[] nArray = new int[n3 * 4 * 2];
        int n4 = 0;
        for (int i2 = 0; i2 < n3; ++i2) {
            ECPoint eCPoint = eCPointArray[n2 + i2];
            Nat128.copy(((SecP128R1FieldElement)eCPoint.getRawXCoord()).x, 0, nArray, n4);
            Nat128.copy(((SecP128R1FieldElement)eCPoint.getRawYCoord()).x, 0, nArray, n4 += 4);
            n4 += 4;
        }
        return new AbstractECLookupTable(){

            @Override
            public int getSize() {
                return n3;
            }

            @Override
            public ECPoint lookup(int n2) {
                int[] nArray3 = Nat128.create();
                int[] nArray2 = Nat128.create();
                int n32 = 0;
                for (int i2 = 0; i2 < n3; ++i2) {
                    int n4 = (i2 ^ n2) - 1 >> 31;
                    for (int i3 = 0; i3 < 4; ++i3) {
                        int n5 = i3;
                        nArray3[n5] = nArray3[n5] ^ nArray[n32 + i3] & n4;
                        int n6 = i3;
                        nArray2[n6] = nArray2[n6] ^ nArray[n32 + 4 + i3] & n4;
                    }
                    n32 += 8;
                }
                return this.createPoint(nArray3, nArray2);
            }

            @Override
            public ECPoint lookupVar(int n2) {
                int[] nArray3 = Nat128.create();
                int[] nArray2 = Nat128.create();
                int n32 = n2 * 4 * 2;
                for (int i2 = 0; i2 < 4; ++i2) {
                    nArray3[i2] = nArray[n32 + i2];
                    nArray2[i2] = nArray[n32 + 4 + i2];
                }
                return this.createPoint(nArray3, nArray2);
            }

            private ECPoint createPoint(int[] nArray3, int[] nArray2) {
                return SecP128R1Curve.this.createRawPoint(new SecP128R1FieldElement(nArray3), new SecP128R1FieldElement(nArray2), SECP128R1_AFFINE_ZS);
            }
        };
    }

    @Override
    public ECFieldElement randomFieldElement(SecureRandom secureRandom) {
        int[] nArray = Nat128.create();
        SecP128R1Field.random(secureRandom, nArray);
        return new SecP128R1FieldElement(nArray);
    }

    @Override
    public ECFieldElement randomFieldElementMult(SecureRandom secureRandom) {
        int[] nArray = Nat128.create();
        SecP128R1Field.randomMult(secureRandom, nArray);
        return new SecP128R1FieldElement(nArray);
    }
}

