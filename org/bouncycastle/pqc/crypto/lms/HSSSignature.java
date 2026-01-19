/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.lms;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.bouncycastle.pqc.crypto.lms.Composer;
import org.bouncycastle.pqc.crypto.lms.LMSPublicKeyParameters;
import org.bouncycastle.pqc.crypto.lms.LMSSignature;
import org.bouncycastle.pqc.crypto.lms.LMSSignedPubKey;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Encodable;
import org.bouncycastle.util.Objects;
import org.bouncycastle.util.io.Streams;

class HSSSignature
implements Encodable {
    private final int lMinus1;
    private final LMSSignedPubKey[] signedPubKey;
    private final LMSSignature signature;

    public HSSSignature(int n2, LMSSignedPubKey[] lMSSignedPubKeyArray, LMSSignature lMSSignature) {
        this.lMinus1 = n2;
        this.signedPubKey = lMSSignedPubKeyArray;
        this.signature = lMSSignature;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static HSSSignature getInstance(Object object, int n2) throws IOException {
        if (object instanceof HSSSignature) {
            return (HSSSignature)object;
        }
        if (object instanceof DataInputStream) {
            int n3 = ((DataInputStream)object).readInt();
            if (n3 != n2 - 1) {
                throw new IllegalStateException("nspk exceeded maxNspk");
            }
            LMSSignedPubKey[] lMSSignedPubKeyArray = new LMSSignedPubKey[n3];
            if (n3 != 0) {
                for (int i2 = 0; i2 < lMSSignedPubKeyArray.length; ++i2) {
                    lMSSignedPubKeyArray[i2] = new LMSSignedPubKey(LMSSignature.getInstance(object), LMSPublicKeyParameters.getInstance(object));
                }
            }
            LMSSignature lMSSignature = LMSSignature.getInstance(object);
            return new HSSSignature(n3, lMSSignedPubKeyArray, lMSSignature);
        }
        if (object instanceof byte[]) {
            try (InputStream inputStream2 = null;){
                inputStream2 = new DataInputStream(new ByteArrayInputStream((byte[])object));
                HSSSignature hSSSignature = HSSSignature.getInstance(inputStream2, n2);
                return hSSSignature;
            }
        }
        if (object instanceof InputStream) {
            return HSSSignature.getInstance(Streams.readAll((InputStream)object), n2);
        }
        throw new IllegalArgumentException("cannot parse " + object);
    }

    public int getlMinus1() {
        return this.lMinus1;
    }

    public LMSSignedPubKey[] getSignedPubKey() {
        return this.signedPubKey;
    }

    public LMSSignature getSignature() {
        return this.signature;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        HSSSignature hSSSignature = (HSSSignature)object;
        return this.lMinus1 == hSSSignature.lMinus1 && Arrays.areEqual(this.signedPubKey, hSSSignature.signedPubKey) && Objects.areEqual(this.signature, hSSSignature.signature);
    }

    public int hashCode() {
        int n2 = this.lMinus1;
        n2 = 31 * n2 + Arrays.hashCode(this.signedPubKey);
        n2 = 31 * n2 + Objects.hashCode(this.signature);
        return n2;
    }

    @Override
    public byte[] getEncoded() throws IOException {
        Composer composer = Composer.compose();
        composer.u32str(this.lMinus1);
        if (this.signedPubKey != null) {
            for (LMSSignedPubKey lMSSignedPubKey : this.signedPubKey) {
                composer.bytes(lMSSignedPubKey);
            }
        }
        composer.bytes(this.signature);
        return composer.build();
    }
}

