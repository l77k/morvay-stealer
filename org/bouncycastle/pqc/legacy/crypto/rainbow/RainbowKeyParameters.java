/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.legacy.crypto.rainbow;

import org.bouncycastle.crypto.params.AsymmetricKeyParameter;

public class RainbowKeyParameters
extends AsymmetricKeyParameter {
    private int docLength;

    public RainbowKeyParameters(boolean bl, int n2) {
        super(bl);
        this.docLength = n2;
    }

    public int getDocLength() {
        return this.docLength;
    }
}

