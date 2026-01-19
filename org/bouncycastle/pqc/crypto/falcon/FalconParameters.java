/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.falcon;

import org.bouncycastle.crypto.CipherParameters;

public class FalconParameters
implements CipherParameters {
    public static final FalconParameters falcon_512 = new FalconParameters("falcon-512", 9, 40);
    public static final FalconParameters falcon_1024 = new FalconParameters("falcon-1024", 10, 40);
    private final String name;
    private final int logn;
    private final int nonce_length;

    private FalconParameters(String string, int n2, int n3) {
        if (n2 < 1 || n2 > 10) {
            throw new IllegalArgumentException("Log N degree should be between 1 and 10");
        }
        this.name = string;
        this.logn = n2;
        this.nonce_length = n3;
    }

    public int getLogN() {
        return this.logn;
    }

    int getNonceLength() {
        return this.nonce_length;
    }

    public String getName() {
        return this.name;
    }
}

