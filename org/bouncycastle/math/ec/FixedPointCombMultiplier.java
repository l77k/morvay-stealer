/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.math.ec;

import java.math.BigInteger;
import org.bouncycastle.math.ec.AbstractECMultiplier;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECLookupTable;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.FixedPointPreCompInfo;
import org.bouncycastle.math.ec.FixedPointUtil;
import org.bouncycastle.math.raw.Nat;

public class FixedPointCombMultiplier
extends AbstractECMultiplier {
    @Override
    protected ECPoint multiplyPositive(ECPoint eCPoint, BigInteger bigInteger) {
        ECCurve eCCurve = eCPoint.getCurve();
        int n2 = FixedPointUtil.getCombSize(eCCurve);
        if (bigInteger.bitLength() > n2) {
            throw new IllegalStateException("fixed-point comb doesn't support scalars larger than the curve order");
        }
        FixedPointPreCompInfo fixedPointPreCompInfo = FixedPointUtil.precompute(eCPoint);
        ECLookupTable eCLookupTable = fixedPointPreCompInfo.getLookupTable();
        int n3 = fixedPointPreCompInfo.getWidth();
        int n4 = (n2 + n3 - 1) / n3;
        ECPoint eCPoint2 = eCCurve.getInfinity();
        int n5 = n4 * n3;
        int[] nArray = Nat.fromBigInteger(n5, bigInteger);
        int n6 = n5 - 1;
        for (int i2 = 0; i2 < n4; ++i2) {
            int n7 = 0;
            for (int i3 = n6 - i2; i3 >= 0; i3 -= n4) {
                int n8 = nArray[i3 >>> 5] >>> (i3 & 0x1F);
                n7 ^= n8 >>> 1;
                n7 <<= 1;
                n7 ^= n8;
            }
            ECPoint eCPoint3 = eCLookupTable.lookup(n7);
            eCPoint2 = eCPoint2.twicePlus(eCPoint3);
        }
        return eCPoint2.add(fixedPointPreCompInfo.getOffset());
    }
}

