/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.math.ec;

import java.math.BigInteger;
import org.bouncycastle.math.ec.ECAlgorithms;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.ECPointMap;
import org.bouncycastle.math.ec.PreCompCallback;
import org.bouncycastle.math.ec.PreCompInfo;
import org.bouncycastle.math.ec.WNafPreCompInfo;

public abstract class WNafUtil {
    public static final String PRECOMP_NAME = "bc_wnaf";
    private static final int[] DEFAULT_WINDOW_SIZE_CUTOFFS = new int[]{13, 41, 121, 337, 897, 2305};
    private static final int MAX_WIDTH = 16;
    private static final byte[] EMPTY_BYTES = new byte[0];
    private static final int[] EMPTY_INTS = new int[0];
    private static final ECPoint[] EMPTY_POINTS = new ECPoint[0];

    public static void configureBasepoint(ECPoint eCPoint) {
        ECCurve eCCurve = eCPoint.getCurve();
        if (null == eCCurve) {
            return;
        }
        BigInteger bigInteger = eCCurve.getOrder();
        int n2 = null == bigInteger ? eCCurve.getFieldSize() + 1 : bigInteger.bitLength();
        final int n3 = Math.min(16, WNafUtil.getWindowSize(n2) + 3);
        eCCurve.precompute(eCPoint, PRECOMP_NAME, new PreCompCallback(){

            @Override
            public PreCompInfo precompute(PreCompInfo preCompInfo) {
                WNafPreCompInfo wNafPreCompInfo;
                WNafPreCompInfo wNafPreCompInfo2 = wNafPreCompInfo = preCompInfo instanceof WNafPreCompInfo ? (WNafPreCompInfo)preCompInfo : null;
                if (null != wNafPreCompInfo && wNafPreCompInfo.getConfWidth() == n3) {
                    wNafPreCompInfo.setPromotionCountdown(0);
                    return wNafPreCompInfo;
                }
                WNafPreCompInfo wNafPreCompInfo3 = new WNafPreCompInfo();
                wNafPreCompInfo3.setPromotionCountdown(0);
                wNafPreCompInfo3.setConfWidth(n3);
                if (null != wNafPreCompInfo) {
                    wNafPreCompInfo3.setPreComp(wNafPreCompInfo.getPreComp());
                    wNafPreCompInfo3.setPreCompNeg(wNafPreCompInfo.getPreCompNeg());
                    wNafPreCompInfo3.setTwice(wNafPreCompInfo.getTwice());
                    wNafPreCompInfo3.setWidth(wNafPreCompInfo.getWidth());
                }
                return wNafPreCompInfo3;
            }
        });
    }

    public static int[] generateCompactNaf(BigInteger bigInteger) {
        if (bigInteger.bitLength() >>> 16 != 0) {
            throw new IllegalArgumentException("'k' must have bitlength < 2^16");
        }
        if (bigInteger.signum() == 0) {
            return EMPTY_INTS;
        }
        BigInteger bigInteger2 = bigInteger.shiftLeft(1).add(bigInteger);
        int n2 = bigInteger2.bitLength();
        int[] nArray = new int[n2 >> 1];
        BigInteger bigInteger3 = bigInteger2.xor(bigInteger);
        int n3 = n2 - 1;
        int n4 = 0;
        int n5 = 0;
        for (int i2 = 1; i2 < n3; ++i2) {
            if (!bigInteger3.testBit(i2)) {
                ++n5;
                continue;
            }
            int n6 = bigInteger.testBit(i2) ? -1 : 1;
            nArray[n4++] = n6 << 16 | n5;
            n5 = 1;
            ++i2;
        }
        nArray[n4++] = 0x10000 | n5;
        if (nArray.length > n4) {
            nArray = WNafUtil.trim(nArray, n4);
        }
        return nArray;
    }

    public static int[] generateCompactWindowNaf(int n2, BigInteger bigInteger) {
        if (n2 == 2) {
            return WNafUtil.generateCompactNaf(bigInteger);
        }
        if (n2 < 2 || n2 > 16) {
            throw new IllegalArgumentException("'width' must be in the range [2, 16]");
        }
        if (bigInteger.bitLength() >>> 16 != 0) {
            throw new IllegalArgumentException("'k' must have bitlength < 2^16");
        }
        if (bigInteger.signum() == 0) {
            return EMPTY_INTS;
        }
        int[] nArray = new int[bigInteger.bitLength() / n2 + 1];
        int n3 = 1 << n2;
        int n4 = n3 - 1;
        int n5 = n3 >>> 1;
        boolean bl = false;
        int n6 = 0;
        int n7 = 0;
        while (n7 <= bigInteger.bitLength()) {
            if (bigInteger.testBit(n7) == bl) {
                ++n7;
                continue;
            }
            bigInteger = bigInteger.shiftRight(n7);
            int n8 = bigInteger.intValue() & n4;
            if (bl) {
                ++n8;
            }
            boolean bl2 = bl = (n8 & n5) != 0;
            if (bl) {
                n8 -= n3;
            }
            int n9 = n6 > 0 ? n7 - 1 : n7;
            nArray[n6++] = n8 << 16 | n9;
            n7 = n2;
        }
        if (nArray.length > n6) {
            nArray = WNafUtil.trim(nArray, n6);
        }
        return nArray;
    }

    public static byte[] generateJSF(BigInteger bigInteger, BigInteger bigInteger2) {
        int n2 = Math.max(bigInteger.bitLength(), bigInteger2.bitLength()) + 1;
        byte[] byArray = new byte[n2];
        BigInteger bigInteger3 = bigInteger;
        BigInteger bigInteger4 = bigInteger2;
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        while (n4 | n5 || bigInteger3.bitLength() > n6 || bigInteger4.bitLength() > n6) {
            int n7;
            int n8 = (bigInteger3.intValue() >>> n6) + n4 & 7;
            int n9 = (bigInteger4.intValue() >>> n6) + n5 & 7;
            int n10 = n8 & 1;
            if (n10 != 0 && n8 + (n10 -= n8 & 2) == 4 && (n9 & 3) == 2) {
                n10 = -n10;
            }
            if ((n7 = n9 & 1) != 0 && n9 + (n7 -= n9 & 2) == 4 && (n8 & 3) == 2) {
                n7 = -n7;
            }
            if (n4 << 1 == 1 + n10) {
                n4 ^= 1;
            }
            if (n5 << 1 == 1 + n7) {
                n5 ^= 1;
            }
            if (++n6 == 30) {
                n6 = 0;
                bigInteger3 = bigInteger3.shiftRight(30);
                bigInteger4 = bigInteger4.shiftRight(30);
            }
            byArray[n3++] = (byte)(n10 << 4 | n7 & 0xF);
        }
        if (byArray.length > n3) {
            byArray = WNafUtil.trim(byArray, n3);
        }
        return byArray;
    }

    public static byte[] generateNaf(BigInteger bigInteger) {
        if (bigInteger.signum() == 0) {
            return EMPTY_BYTES;
        }
        BigInteger bigInteger2 = bigInteger.shiftLeft(1).add(bigInteger);
        int n2 = bigInteger2.bitLength() - 1;
        byte[] byArray = new byte[n2];
        BigInteger bigInteger3 = bigInteger2.xor(bigInteger);
        for (int i2 = 1; i2 < n2; ++i2) {
            if (!bigInteger3.testBit(i2)) continue;
            byArray[i2 - 1] = (byte)(bigInteger.testBit(i2) ? -1 : 1);
            ++i2;
        }
        byArray[n2 - 1] = 1;
        return byArray;
    }

    public static byte[] generateWindowNaf(int n2, BigInteger bigInteger) {
        if (n2 == 2) {
            return WNafUtil.generateNaf(bigInteger);
        }
        if (n2 < 2 || n2 > 8) {
            throw new IllegalArgumentException("'width' must be in the range [2, 8]");
        }
        if (bigInteger.signum() == 0) {
            return EMPTY_BYTES;
        }
        byte[] byArray = new byte[bigInteger.bitLength() + 1];
        int n3 = 1 << n2;
        int n4 = n3 - 1;
        int n5 = n3 >>> 1;
        boolean bl = false;
        int n6 = 0;
        int n7 = 0;
        while (n7 <= bigInteger.bitLength()) {
            if (bigInteger.testBit(n7) == bl) {
                ++n7;
                continue;
            }
            bigInteger = bigInteger.shiftRight(n7);
            int n8 = bigInteger.intValue() & n4;
            if (bl) {
                ++n8;
            }
            boolean bl2 = bl = (n8 & n5) != 0;
            if (bl) {
                n8 -= n3;
            }
            n6 += n6 > 0 ? n7 - 1 : n7;
            byArray[n6++] = (byte)n8;
            n7 = n2;
        }
        if (byArray.length > n6) {
            byArray = WNafUtil.trim(byArray, n6);
        }
        return byArray;
    }

    public static int getNafWeight(BigInteger bigInteger) {
        if (bigInteger.signum() == 0) {
            return 0;
        }
        BigInteger bigInteger2 = bigInteger.shiftLeft(1).add(bigInteger);
        BigInteger bigInteger3 = bigInteger2.xor(bigInteger);
        return bigInteger3.bitCount();
    }

    public static WNafPreCompInfo getWNafPreCompInfo(ECPoint eCPoint) {
        return WNafUtil.getWNafPreCompInfo(eCPoint.getCurve().getPreCompInfo(eCPoint, PRECOMP_NAME));
    }

    public static WNafPreCompInfo getWNafPreCompInfo(PreCompInfo preCompInfo) {
        return preCompInfo instanceof WNafPreCompInfo ? (WNafPreCompInfo)preCompInfo : null;
    }

    public static int getWindowSize(int n2) {
        return WNafUtil.getWindowSize(n2, DEFAULT_WINDOW_SIZE_CUTOFFS, 16);
    }

    public static int getWindowSize(int n2, int n3) {
        return WNafUtil.getWindowSize(n2, DEFAULT_WINDOW_SIZE_CUTOFFS, n3);
    }

    public static int getWindowSize(int n2, int[] nArray) {
        return WNafUtil.getWindowSize(n2, nArray, 16);
    }

    public static int getWindowSize(int n2, int[] nArray, int n3) {
        int n4;
        for (n4 = 0; n4 < nArray.length && n2 >= nArray[n4]; ++n4) {
        }
        return Math.max(2, Math.min(n3, n4 + 2));
    }

    public static WNafPreCompInfo precompute(final ECPoint eCPoint, final int n2, final boolean bl) {
        final ECCurve eCCurve = eCPoint.getCurve();
        return (WNafPreCompInfo)eCCurve.precompute(eCPoint, PRECOMP_NAME, new PreCompCallback(){

            @Override
            public PreCompInfo precompute(PreCompInfo preCompInfo) {
                int n22;
                int n3;
                int n4;
                int n5;
                WNafPreCompInfo wNafPreCompInfo = preCompInfo instanceof WNafPreCompInfo ? (WNafPreCompInfo)preCompInfo : null;
                if (this.checkExisting(wNafPreCompInfo, n5 = Math.max(2, Math.min(16, n2)), n4 = 1 << n5 - 2, bl)) {
                    wNafPreCompInfo.decrementPromotionCountdown();
                    return wNafPreCompInfo;
                }
                WNafPreCompInfo wNafPreCompInfo2 = new WNafPreCompInfo();
                ECPoint[] eCPointArray = null;
                ECPoint[] eCPointArray2 = null;
                ECPoint eCPoint4 = null;
                if (null != wNafPreCompInfo) {
                    n3 = wNafPreCompInfo.decrementPromotionCountdown();
                    wNafPreCompInfo2.setPromotionCountdown(n3);
                    n22 = wNafPreCompInfo.getConfWidth();
                    wNafPreCompInfo2.setConfWidth(n22);
                    eCPointArray = wNafPreCompInfo.getPreComp();
                    eCPointArray2 = wNafPreCompInfo.getPreCompNeg();
                    eCPoint4 = wNafPreCompInfo.getTwice();
                }
                n5 = Math.min(16, Math.max(wNafPreCompInfo2.getConfWidth(), n5));
                n4 = 1 << n5 - 2;
                n3 = 0;
                if (null == eCPointArray) {
                    eCPointArray = EMPTY_POINTS;
                } else {
                    n3 = eCPointArray.length;
                }
                if (n3 < n4) {
                    eCPointArray = WNafUtil.resizeTable(eCPointArray, n4);
                    if (n4 == 1) {
                        eCPointArray[0] = eCPoint.normalize();
                    } else {
                        n22 = n3;
                        if (n22 == 0) {
                            eCPointArray[0] = eCPoint;
                            n22 = 1;
                        }
                        ECFieldElement eCFieldElement = null;
                        if (n4 == 2) {
                            eCPointArray[1] = eCPoint.threeTimes();
                        } else {
                            ECPoint eCPoint2 = eCPoint4;
                            ECPoint eCPoint3 = eCPointArray[n22 - 1];
                            if (null == eCPoint2 && !(eCPoint4 = (eCPoint2 = eCPointArray[0].twice())).isInfinity() && ECAlgorithms.isFpCurve(eCCurve) && eCCurve.getFieldSize() >= 64) {
                                switch (eCCurve.getCoordinateSystem()) {
                                    case 2: 
                                    case 3: 
                                    case 4: {
                                        eCFieldElement = eCPoint4.getZCoord(0);
                                        eCPoint2 = eCCurve.createPoint(eCPoint4.getXCoord().toBigInteger(), eCPoint4.getYCoord().toBigInteger());
                                        ECFieldElement eCFieldElement2 = eCFieldElement.square();
                                        ECFieldElement eCFieldElement3 = eCFieldElement2.multiply(eCFieldElement);
                                        eCPoint3 = eCPoint3.scaleX(eCFieldElement2).scaleY(eCFieldElement3);
                                        if (n3 != 0) break;
                                        eCPointArray[0] = eCPoint3;
                                        break;
                                    }
                                }
                            }
                            while (n22 < n4) {
                                eCPointArray[n22++] = eCPoint3 = eCPoint3.add(eCPoint2);
                            }
                        }
                        eCCurve.normalizeAll(eCPointArray, n3, n4 - n3, eCFieldElement);
                    }
                }
                if (bl) {
                    if (null == eCPointArray2) {
                        n22 = 0;
                        eCPointArray2 = new ECPoint[n4];
                    } else {
                        n22 = eCPointArray2.length;
                        if (n22 < n4) {
                            eCPointArray2 = WNafUtil.resizeTable(eCPointArray2, n4);
                        }
                    }
                    while (n22 < n4) {
                        eCPointArray2[n22] = eCPointArray[n22].negate();
                        ++n22;
                    }
                }
                wNafPreCompInfo2.setPreComp(eCPointArray);
                wNafPreCompInfo2.setPreCompNeg(eCPointArray2);
                wNafPreCompInfo2.setTwice(eCPoint4);
                wNafPreCompInfo2.setWidth(n5);
                return wNafPreCompInfo2;
            }

            private boolean checkExisting(WNafPreCompInfo wNafPreCompInfo, int n22, int n3, boolean bl2) {
                return null != wNafPreCompInfo && wNafPreCompInfo.getWidth() >= Math.max(wNafPreCompInfo.getConfWidth(), n22) && this.checkTable(wNafPreCompInfo.getPreComp(), n3) && (!bl2 || this.checkTable(wNafPreCompInfo.getPreCompNeg(), n3));
            }

            private boolean checkTable(ECPoint[] eCPointArray, int n22) {
                return null != eCPointArray && eCPointArray.length >= n22;
            }
        });
    }

    public static WNafPreCompInfo precomputeWithPointMap(ECPoint eCPoint, final ECPointMap eCPointMap, final WNafPreCompInfo wNafPreCompInfo, final boolean bl) {
        ECCurve eCCurve = eCPoint.getCurve();
        return (WNafPreCompInfo)eCCurve.precompute(eCPoint, PRECOMP_NAME, new PreCompCallback(){

            @Override
            public PreCompInfo precompute(PreCompInfo preCompInfo) {
                Object object;
                int n2;
                int n3;
                WNafPreCompInfo wNafPreCompInfo3 = preCompInfo instanceof WNafPreCompInfo ? (WNafPreCompInfo)preCompInfo : null;
                if (this.checkExisting(wNafPreCompInfo3, n3 = wNafPreCompInfo.getWidth(), n2 = wNafPreCompInfo.getPreComp().length, bl)) {
                    wNafPreCompInfo3.decrementPromotionCountdown();
                    return wNafPreCompInfo3;
                }
                WNafPreCompInfo wNafPreCompInfo2 = new WNafPreCompInfo();
                wNafPreCompInfo2.setPromotionCountdown(wNafPreCompInfo.getPromotionCountdown());
                ECPoint eCPoint = wNafPreCompInfo.getTwice();
                if (null != eCPoint) {
                    object = eCPointMap.map(eCPoint);
                    wNafPreCompInfo2.setTwice((ECPoint)object);
                }
                object = wNafPreCompInfo.getPreComp();
                ECPoint[] eCPointArray = new ECPoint[((ECPoint[])object).length];
                for (int i2 = 0; i2 < ((ECPoint[])object).length; ++i2) {
                    eCPointArray[i2] = eCPointMap.map(object[i2]);
                }
                wNafPreCompInfo2.setPreComp(eCPointArray);
                wNafPreCompInfo2.setWidth(n3);
                if (bl) {
                    ECPoint[] eCPointArray2 = new ECPoint[eCPointArray.length];
                    for (int i3 = 0; i3 < eCPointArray2.length; ++i3) {
                        eCPointArray2[i3] = eCPointArray[i3].negate();
                    }
                    wNafPreCompInfo2.setPreCompNeg(eCPointArray2);
                }
                return wNafPreCompInfo2;
            }

            private boolean checkExisting(WNafPreCompInfo wNafPreCompInfo2, int n2, int n3, boolean bl2) {
                return null != wNafPreCompInfo2 && wNafPreCompInfo2.getWidth() >= n2 && this.checkTable(wNafPreCompInfo2.getPreComp(), n3) && (!bl2 || this.checkTable(wNafPreCompInfo2.getPreCompNeg(), n3));
            }

            private boolean checkTable(ECPoint[] eCPointArray, int n2) {
                return null != eCPointArray && eCPointArray.length >= n2;
            }
        });
    }

    private static byte[] trim(byte[] byArray, int n2) {
        byte[] byArray2 = new byte[n2];
        System.arraycopy(byArray, 0, byArray2, 0, byArray2.length);
        return byArray2;
    }

    private static int[] trim(int[] nArray, int n2) {
        int[] nArray2 = new int[n2];
        System.arraycopy(nArray, 0, nArray2, 0, nArray2.length);
        return nArray2;
    }

    private static ECPoint[] resizeTable(ECPoint[] eCPointArray, int n2) {
        ECPoint[] eCPointArray2 = new ECPoint[n2];
        System.arraycopy(eCPointArray, 0, eCPointArray2, 0, eCPointArray.length);
        return eCPointArray2;
    }
}

