/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.math.ec;

import org.bouncycastle.math.ec.AbstractECLookupTable;
import org.bouncycastle.math.ec.ECPoint;

public class SimpleLookupTable
extends AbstractECLookupTable {
    private final ECPoint[] points;

    private static ECPoint[] copy(ECPoint[] eCPointArray, int n2, int n3) {
        ECPoint[] eCPointArray2 = new ECPoint[n3];
        for (int i2 = 0; i2 < n3; ++i2) {
            eCPointArray2[i2] = eCPointArray[n2 + i2];
        }
        return eCPointArray2;
    }

    public SimpleLookupTable(ECPoint[] eCPointArray, int n2, int n3) {
        this.points = SimpleLookupTable.copy(eCPointArray, n2, n3);
    }

    @Override
    public int getSize() {
        return this.points.length;
    }

    @Override
    public ECPoint lookup(int n2) {
        throw new UnsupportedOperationException("Constant-time lookup not supported");
    }

    @Override
    public ECPoint lookupVar(int n2) {
        return this.points[n2];
    }
}

