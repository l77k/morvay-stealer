/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.jcajce.spec;

import java.security.spec.EncodedKeySpec;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Strings;

public class OpenSSHPublicKeySpec
extends EncodedKeySpec {
    private static final String[] allowedTypes = new String[]{"ssh-rsa", "ssh-ed25519", "ssh-dss"};
    private final String type;

    public OpenSSHPublicKeySpec(byte[] byArray) {
        super(byArray);
        int n2 = 0;
        int n3 = (byArray[n2++] & 0xFF) << 24;
        n3 |= (byArray[n2++] & 0xFF) << 16;
        n3 |= (byArray[n2++] & 0xFF) << 8;
        if (n2 + (n3 |= byArray[n2++] & 0xFF) >= byArray.length) {
            throw new IllegalArgumentException("invalid public key blob: type field longer than blob");
        }
        this.type = Strings.fromByteArray(Arrays.copyOfRange(byArray, n2, n2 + n3));
        if (this.type.startsWith("ecdsa")) {
            return;
        }
        for (int i2 = 0; i2 < allowedTypes.length; ++i2) {
            if (!allowedTypes[i2].equals(this.type)) continue;
            return;
        }
        throw new IllegalArgumentException("unrecognised public key type " + this.type);
    }

    @Override
    public String getFormat() {
        return "OpenSSH";
    }

    public String getType() {
        return this.type;
    }
}

