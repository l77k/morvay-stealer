/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.math.ec;

import java.math.BigInteger;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECLookupTable;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.FixedPointPreCompInfo;
import org.bouncycastle.math.ec.PreCompCallback;
import org.bouncycastle.math.ec.PreCompInfo;

public class FixedPointUtil {
    public static final String PRECOMP_NAME = "bc_fixed_point";

    public static int getCombSize(ECCurve eCCurve) {
        BigInteger bigInteger = eCCurve.getOrder();
        return bigInteger == null ? eCCurve.getFieldSize() + 1 : bigInteger.bitLength();
    }

    public static FixedPointPreCompInfo getFixedPointPreCompInfo(PreCompInfo preCompInfo) {
        return preCompInfo instanceof FixedPointPreCompInfo ? (FixedPointPreCompInfo)preCompInfo : null;
    }

    public static FixedPointPreCompInfo precompute(final ECPoint eCPoint) {
        final ECCurve eCCurve = eCPoint.getCurve();
        return (FixedPointPreCompInfo)eCCurve.precompute(eCPoint, PRECOMP_NAME, new PreCompCallback(){

            @Override
            public PreCompInfo precompute(PreCompInfo preCompInfo) {
                int n2;
                int n3;
                int n4;
                FixedPointPreCompInfo fixedPointPreCompInfo = preCompInfo instanceof FixedPointPreCompInfo ? (FixedPointPreCompInfo)preCompInfo : null;
                if (this.checkExisting(fixedPointPreCompInfo, n4 = 1 << (n3 = (n2 = FixedPointUtil.getCombSize(eCCurve)) > 250 ? 6 : 5))) {
                    return fixedPointPreCompInfo;
                }
                int n5 = (n2 + n3 - 1) / n3;
                ECPoint[] eCPointArray = new ECPoint[n3 + 1];
                eCPointArray[0] = eCPoint;
                for (int i2 = 1; i2 < n3; ++i2) {
                    eCPointArray[i2] = eCPointArray[i2 - 1].timesPow2(n5);
                }
                eCPointArray[n3] = eCPointArray[0].subtract(eCPointArray[1]);
                eCCurve.normalizeAll(eCPointArray);
                ECPoint[] eCPointArray2 = new ECPoint[n4];
                eCPointArray2[0] = eCPointArray[0];
                for (int i3 = n3 - 1; i3 >= 0; --i3) {
                    int n6;
                    ECPoint eCPoint2 = eCPointArray[i3];
                    for (int i4 = n6 = 1 << i3; i4 < n4; i4 += n6 << 1) {
                        eCPointArray2[i4] = eCPointArray2[i4 - n6].add(eCPoint2);
                    }
                }
                eCCurve.normalizeAll(eCPointArray2);
                FixedPointPreCompInfo fixedPointPreCompInfo2 = new FixedPointPreCompInfo();
                fixedPointPreCompInfo2.setLookupTable(eCCurve.createCacheSafeLookupTable(eCPointArray2, 0, eCPointArray2.length));
                fixedPointPreCompInfo2.setOffset(eCPointArray[n3]);
                fixedPointPreCompInfo2.setWidth(n3);
                return fixedPointPreCompInfo2;
            }

            private boolean checkExisting(FixedPointPreCompInfo fixedPointPreCompInfo, int n2) {
                return fixedPointPreCompInfo != null && this.checkTable(fixedPointPreCompInfo.getLookupTable(), n2);
            }

            private boolean checkTable(ECLookupTable eCLookupTable, int n2) {
                return eCLookupTable != null && eCLookupTable.getSize() >= n2;
            }
        });
    }
}

