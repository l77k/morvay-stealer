/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.legacy.crypto.mceliece;

import org.bouncycastle.pqc.legacy.crypto.mceliece.McElieceParameters;

public class McElieceCCA2Parameters
extends McElieceParameters {
    private final String digest;

    public McElieceCCA2Parameters() {
        this(11, 50, "SHA-256");
    }

    public McElieceCCA2Parameters(String string) {
        this(11, 50, string);
    }

    public McElieceCCA2Parameters(int n2) {
        this(n2, "SHA-256");
    }

    public McElieceCCA2Parameters(int n2, String string) {
        super(n2);
        this.digest = string;
    }

    public McElieceCCA2Parameters(int n2, int n3) {
        this(n2, n3, "SHA-256");
    }

    public McElieceCCA2Parameters(int n2, int n3, String string) {
        super(n2, n3);
        this.digest = string;
    }

    public McElieceCCA2Parameters(int n2, int n3, int n4) {
        this(n2, n3, n4, "SHA-256");
    }

    public McElieceCCA2Parameters(int n2, int n3, int n4, String string) {
        super(n2, n3, n4);
        this.digest = string;
    }

    public String getDigest() {
        return this.digest;
    }
}

