/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.jce.spec;

import java.security.spec.AlgorithmParameterSpec;
import org.bouncycastle.util.Arrays;

public class IESParameterSpec
implements AlgorithmParameterSpec {
    private byte[] derivation;
    private byte[] encoding;
    private int macKeySize;
    private int cipherKeySize;
    private byte[] nonce;
    private boolean usePointCompression;

    public IESParameterSpec(byte[] byArray, byte[] byArray2, int n2) {
        this(byArray, byArray2, n2, -1, null, false);
    }

    public IESParameterSpec(byte[] byArray, byte[] byArray2, int n2, int n3, byte[] byArray3) {
        this(byArray, byArray2, n2, n3, byArray3, false);
    }

    public IESParameterSpec(byte[] byArray, byte[] byArray2, int n2, int n3, byte[] byArray3, boolean bl) {
        if (byArray != null) {
            this.derivation = new byte[byArray.length];
            System.arraycopy(byArray, 0, this.derivation, 0, byArray.length);
        } else {
            this.derivation = null;
        }
        if (byArray2 != null) {
            this.encoding = new byte[byArray2.length];
            System.arraycopy(byArray2, 0, this.encoding, 0, byArray2.length);
        } else {
            this.encoding = null;
        }
        this.macKeySize = n2;
        this.cipherKeySize = n3;
        this.nonce = Arrays.clone(byArray3);
        this.usePointCompression = bl;
    }

    public byte[] getDerivationV() {
        return Arrays.clone(this.derivation);
    }

    public byte[] getEncodingV() {
        return Arrays.clone(this.encoding);
    }

    public int getMacKeySize() {
        return this.macKeySize;
    }

    public int getCipherKeySize() {
        return this.cipherKeySize;
    }

    public byte[] getNonce() {
        return Arrays.clone(this.nonce);
    }

    public void setPointCompression(boolean bl) {
        this.usePointCompression = bl;
    }

    public boolean getPointCompression() {
        return this.usePointCompression;
    }
}

