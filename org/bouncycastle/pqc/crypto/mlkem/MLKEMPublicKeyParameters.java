/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.mlkem;

import org.bouncycastle.pqc.crypto.mlkem.MLKEMKeyParameters;
import org.bouncycastle.pqc.crypto.mlkem.MLKEMParameters;
import org.bouncycastle.util.Arrays;

public class MLKEMPublicKeyParameters
extends MLKEMKeyParameters {
    final byte[] t;
    final byte[] rho;

    static byte[] getEncoded(byte[] byArray, byte[] byArray2) {
        return Arrays.concatenate(byArray, byArray2);
    }

    public MLKEMPublicKeyParameters(MLKEMParameters mLKEMParameters, byte[] byArray, byte[] byArray2) {
        super(false, mLKEMParameters);
        this.t = Arrays.clone(byArray);
        this.rho = Arrays.clone(byArray2);
    }

    public MLKEMPublicKeyParameters(MLKEMParameters mLKEMParameters, byte[] byArray) {
        super(false, mLKEMParameters);
        this.t = Arrays.copyOfRange(byArray, 0, byArray.length - 32);
        this.rho = Arrays.copyOfRange(byArray, byArray.length - 32, byArray.length);
    }

    public byte[] getEncoded() {
        return MLKEMPublicKeyParameters.getEncoded(this.t, this.rho);
    }

    public byte[] getRho() {
        return Arrays.clone(this.rho);
    }

    public byte[] getT() {
        return Arrays.clone(this.t);
    }
}

