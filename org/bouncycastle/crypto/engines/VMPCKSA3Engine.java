/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.engines.VMPCEngine;

public class VMPCKSA3Engine
extends VMPCEngine {
    @Override
    public String getAlgorithmName() {
        return "VMPC-KSA3";
    }

    @Override
    protected void initKey(byte[] byArray, byte[] byArray2) {
        byte by;
        int n2;
        this.s = 0;
        this.P = new byte[256];
        for (n2 = 0; n2 < 256; ++n2) {
            this.P[n2] = (byte)n2;
        }
        for (n2 = 0; n2 < 768; ++n2) {
            this.s = this.P[this.s + this.P[n2 & 0xFF] + byArray[n2 % byArray.length] & 0xFF];
            by = this.P[n2 & 0xFF];
            this.P[n2 & 0xFF] = this.P[this.s & 0xFF];
            this.P[this.s & 0xFF] = by;
        }
        for (n2 = 0; n2 < 768; ++n2) {
            this.s = this.P[this.s + this.P[n2 & 0xFF] + byArray2[n2 % byArray2.length] & 0xFF];
            by = this.P[n2 & 0xFF];
            this.P[n2 & 0xFF] = this.P[this.s & 0xFF];
            this.P[this.s & 0xFF] = by;
        }
        for (n2 = 0; n2 < 768; ++n2) {
            this.s = this.P[this.s + this.P[n2 & 0xFF] + byArray[n2 % byArray.length] & 0xFF];
            by = this.P[n2 & 0xFF];
            this.P[n2 & 0xFF] = this.P[this.s & 0xFF];
            this.P[this.s & 0xFF] = by;
        }
        this.n = 0;
    }
}

