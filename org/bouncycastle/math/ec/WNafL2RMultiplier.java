/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.math.ec;

import java.math.BigInteger;
import org.bouncycastle.math.ec.AbstractECMultiplier;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.WNafPreCompInfo;
import org.bouncycastle.math.ec.WNafUtil;
import org.bouncycastle.util.Integers;

public class WNafL2RMultiplier
extends AbstractECMultiplier {
    @Override
    protected ECPoint multiplyPositive(ECPoint eCPoint, BigInteger bigInteger) {
        ECPoint[] eCPointArray;
        int n2;
        int n3;
        int n4;
        int n5;
        int n6 = WNafUtil.getWindowSize(bigInteger.bitLength());
        WNafPreCompInfo wNafPreCompInfo = WNafUtil.precompute(eCPoint, n6, true);
        ECPoint[] eCPointArray2 = wNafPreCompInfo.getPreComp();
        ECPoint[] eCPointArray3 = wNafPreCompInfo.getPreCompNeg();
        int n7 = wNafPreCompInfo.getWidth();
        int[] nArray = WNafUtil.generateCompactWindowNaf(n7, bigInteger);
        ECPoint eCPoint2 = eCPoint.getCurve().getInfinity();
        int n8 = nArray.length;
        if (n8 > 1) {
            n5 = nArray[--n8];
            n4 = n5 >> 16;
            n3 = n5 & 0xFFFF;
            n2 = Math.abs(n4);
            ECPoint[] eCPointArray4 = eCPointArray = n4 < 0 ? eCPointArray3 : eCPointArray2;
            if (n2 << 2 < 1 << n7) {
                int n9 = 32 - Integers.numberOfLeadingZeros(n2);
                int n10 = n7 - n9;
                int n11 = n2 ^ 1 << n9 - 1;
                int n12 = (1 << n7 - 1) - 1;
                int n13 = (n11 << n10) + 1;
                eCPoint2 = eCPointArray[n12 >>> 1].add(eCPointArray[n13 >>> 1]);
                n3 -= n10;
            } else {
                eCPoint2 = eCPointArray[n2 >>> 1];
            }
            eCPoint2 = eCPoint2.timesPow2(n3);
        }
        while (n8 > 0) {
            n5 = nArray[--n8];
            n4 = n5 >> 16;
            n3 = n5 & 0xFFFF;
            n2 = Math.abs(n4);
            eCPointArray = n4 < 0 ? eCPointArray3 : eCPointArray2;
            ECPoint eCPoint3 = eCPointArray[n2 >>> 1];
            eCPoint2 = eCPoint2.twicePlus(eCPoint3);
            eCPoint2 = eCPoint2.timesPow2(n3);
        }
        return eCPoint2;
    }
}

