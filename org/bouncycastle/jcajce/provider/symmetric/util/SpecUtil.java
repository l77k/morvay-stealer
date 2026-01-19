/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.jcajce.provider.symmetric.util;

import java.security.AlgorithmParameters;
import java.security.spec.AlgorithmParameterSpec;

class SpecUtil {
    SpecUtil() {
    }

    static AlgorithmParameterSpec extractSpec(AlgorithmParameters algorithmParameters, Class[] classArray) {
        try {
            return algorithmParameters.getParameterSpec(AlgorithmParameterSpec.class);
        }
        catch (Exception exception) {
            for (int i2 = 0; i2 != classArray.length; ++i2) {
                if (classArray[i2] == null) continue;
                try {
                    return algorithmParameters.getParameterSpec(classArray[i2]);
                }
                catch (Exception exception2) {
                    // empty catch block
                }
            }
            return null;
        }
    }
}

