/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.jcajce.spec;

import java.security.spec.AlgorithmParameterSpec;
import org.bouncycastle.crypto.util.RadixConverter;
import org.bouncycastle.util.Arrays;

public class FPEParameterSpec
implements AlgorithmParameterSpec {
    private final RadixConverter radixConverter;
    private final byte[] tweak;
    private final boolean useInverse;

    public FPEParameterSpec(int n2, byte[] byArray) {
        this(n2, byArray, false);
    }

    public FPEParameterSpec(int n2, byte[] byArray, boolean bl) {
        this(new RadixConverter(n2), byArray, bl);
    }

    public FPEParameterSpec(RadixConverter radixConverter, byte[] byArray, boolean bl) {
        this.radixConverter = radixConverter;
        this.tweak = Arrays.clone(byArray);
        this.useInverse = bl;
    }

    public int getRadix() {
        return this.radixConverter.getRadix();
    }

    public RadixConverter getRadixConverter() {
        return this.radixConverter;
    }

    public byte[] getTweak() {
        return Arrays.clone(this.tweak);
    }

    public boolean isUsingInverseFunction() {
        return this.useInverse;
    }
}

