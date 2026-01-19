/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.params;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.util.Arrays;

public class KeyParameter
implements CipherParameters {
    private byte[] key;

    public KeyParameter(byte[] byArray) {
        this(byArray, 0, byArray.length);
    }

    public KeyParameter(byte[] byArray, int n2, int n3) {
        this(n3);
        System.arraycopy(byArray, n2, this.key, 0, n3);
    }

    private KeyParameter(int n2) {
        this.key = new byte[n2];
    }

    public void copyTo(byte[] byArray, int n2, int n3) {
        if (this.key.length != n3) {
            throw new IllegalArgumentException("len");
        }
        System.arraycopy(this.key, 0, byArray, n2, n3);
    }

    public byte[] getKey() {
        return this.key;
    }

    public int getKeyLength() {
        return this.key.length;
    }

    public KeyParameter reverse() {
        KeyParameter keyParameter = new KeyParameter(this.key.length);
        Arrays.reverse(this.key, keyParameter.key);
        return keyParameter;
    }
}

