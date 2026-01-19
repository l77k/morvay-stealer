/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.jcajce.spec;

import java.security.spec.AlgorithmParameterSpec;

public class TLSRSAPremasterSecretParameterSpec
implements AlgorithmParameterSpec {
    private final int protocolVersion;

    public TLSRSAPremasterSecretParameterSpec(int n2) {
        this.protocolVersion = n2;
    }

    public int getProtocolVersion() {
        return this.protocolVersion;
    }
}

