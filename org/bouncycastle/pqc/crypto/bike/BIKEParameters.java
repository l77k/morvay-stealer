/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.bike;

import org.bouncycastle.pqc.crypto.KEMParameters;
import org.bouncycastle.pqc.crypto.bike.BIKEEngine;

public class BIKEParameters
implements KEMParameters {
    public static final BIKEParameters bike128 = new BIKEParameters("bike128", 12323, 142, 134, 256, 5, 3, 128);
    public static final BIKEParameters bike192 = new BIKEParameters("bike192", 24659, 206, 199, 256, 5, 3, 192);
    public static final BIKEParameters bike256 = new BIKEParameters("bike256", 40973, 274, 264, 256, 5, 3, 256);
    private String name;
    private int r;
    private int w;
    private int t;
    private int l;
    private int nbIter;
    private int tau;
    private final int defaultKeySize;
    private BIKEEngine bikeEngine;

    private BIKEParameters(String string, int n2, int n3, int n4, int n5, int n6, int n7, int n8) {
        this.name = string;
        this.r = n2;
        this.w = n3;
        this.t = n4;
        this.l = n5;
        this.nbIter = n6;
        this.tau = n7;
        this.defaultKeySize = n8;
        this.bikeEngine = new BIKEEngine(n2, n3, n4, n5, n6, n7);
    }

    public int getR() {
        return this.r;
    }

    public int getRByte() {
        return (this.r + 7) / 8;
    }

    public int getLByte() {
        return this.l / 8;
    }

    public int getW() {
        return this.w;
    }

    public int getT() {
        return this.t;
    }

    public int getL() {
        return this.l;
    }

    public int getNbIter() {
        return this.nbIter;
    }

    public int getTau() {
        return this.tau;
    }

    public String getName() {
        return this.name;
    }

    public int getSessionKeySize() {
        return this.defaultKeySize;
    }

    BIKEEngine getEngine() {
        return this.bikeEngine;
    }
}

