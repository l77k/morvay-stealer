/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.math.ec;

import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.PreCompInfo;

public class WNafPreCompInfo
implements PreCompInfo {
    volatile int promotionCountdown = 4;
    protected int confWidth = -1;
    protected ECPoint[] preComp = null;
    protected ECPoint[] preCompNeg = null;
    protected ECPoint twice = null;
    protected int width = -1;

    int decrementPromotionCountdown() {
        int n2 = this.promotionCountdown;
        if (n2 > 0) {
            this.promotionCountdown = --n2;
        }
        return n2;
    }

    int getPromotionCountdown() {
        return this.promotionCountdown;
    }

    void setPromotionCountdown(int n2) {
        this.promotionCountdown = n2;
    }

    public boolean isPromoted() {
        return this.promotionCountdown <= 0;
    }

    public int getConfWidth() {
        return this.confWidth;
    }

    public void setConfWidth(int n2) {
        this.confWidth = n2;
    }

    public ECPoint[] getPreComp() {
        return this.preComp;
    }

    public void setPreComp(ECPoint[] eCPointArray) {
        this.preComp = eCPointArray;
    }

    public ECPoint[] getPreCompNeg() {
        return this.preCompNeg;
    }

    public void setPreCompNeg(ECPoint[] eCPointArray) {
        this.preCompNeg = eCPointArray;
    }

    public ECPoint getTwice() {
        return this.twice;
    }

    public void setTwice(ECPoint eCPoint) {
        this.twice = eCPoint;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int n2) {
        this.width = n2;
    }
}

