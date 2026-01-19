/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.lms;

import org.bouncycastle.crypto.Digest;
import org.bouncycastle.pqc.crypto.lms.LMSParameters;
import org.bouncycastle.pqc.crypto.lms.LMSigParameters;

class LmsUtils {
    LmsUtils() {
    }

    static void u32str(int n2, Digest digest) {
        digest.update((byte)(n2 >>> 24));
        digest.update((byte)(n2 >>> 16));
        digest.update((byte)(n2 >>> 8));
        digest.update((byte)n2);
    }

    static void u16str(short s2, Digest digest) {
        digest.update((byte)(s2 >>> 8));
        digest.update((byte)s2);
    }

    static void byteArray(byte[] byArray, Digest digest) {
        digest.update(byArray, 0, byArray.length);
    }

    static void byteArray(byte[] byArray, int n2, int n3, Digest digest) {
        digest.update(byArray, n2, n3);
    }

    static int calculateStrength(LMSParameters lMSParameters) {
        if (lMSParameters == null) {
            throw new NullPointerException("lmsParameters cannot be null");
        }
        LMSigParameters lMSigParameters = lMSParameters.getLMSigParam();
        return (1 << lMSigParameters.getH()) * lMSigParameters.getM();
    }
}

