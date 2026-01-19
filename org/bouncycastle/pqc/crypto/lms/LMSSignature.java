/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.crypto.lms;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import org.bouncycastle.pqc.crypto.lms.Composer;
import org.bouncycastle.pqc.crypto.lms.LMOtsSignature;
import org.bouncycastle.pqc.crypto.lms.LMSigParameters;
import org.bouncycastle.util.Encodable;
import org.bouncycastle.util.io.Streams;

class LMSSignature
implements Encodable {
    private final int q;
    private final LMOtsSignature otsSignature;
    private final LMSigParameters parameter;
    private final byte[][] y;

    public LMSSignature(int n2, LMOtsSignature lMOtsSignature, LMSigParameters lMSigParameters, byte[][] byArray) {
        this.q = n2;
        this.otsSignature = lMOtsSignature;
        this.parameter = lMSigParameters;
        this.y = byArray;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static LMSSignature getInstance(Object object) throws IOException {
        if (object instanceof LMSSignature) {
            return (LMSSignature)object;
        }
        if (object instanceof DataInputStream) {
            int n2 = ((DataInputStream)object).readInt();
            LMOtsSignature lMOtsSignature = LMOtsSignature.getInstance(object);
            LMSigParameters lMSigParameters = LMSigParameters.getParametersForType(((DataInputStream)object).readInt());
            byte[][] byArrayArray = new byte[lMSigParameters.getH()][];
            for (int i2 = 0; i2 < byArrayArray.length; ++i2) {
                byArrayArray[i2] = new byte[lMSigParameters.getM()];
                ((DataInputStream)object).readFully(byArrayArray[i2]);
            }
            return new LMSSignature(n2, lMOtsSignature, lMSigParameters, byArrayArray);
        }
        if (object instanceof byte[]) {
            try (InputStream inputStream2 = null;){
                inputStream2 = new DataInputStream(new ByteArrayInputStream((byte[])object));
                LMSSignature lMSSignature = LMSSignature.getInstance(inputStream2);
                return lMSSignature;
            }
        }
        if (object instanceof InputStream) {
            return LMSSignature.getInstance(Streams.readAll((InputStream)object));
        }
        throw new IllegalArgumentException("cannot parse " + object);
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        LMSSignature lMSSignature = (LMSSignature)object;
        if (this.q != lMSSignature.q) {
            return false;
        }
        if (this.otsSignature != null ? !this.otsSignature.equals(lMSSignature.otsSignature) : lMSSignature.otsSignature != null) {
            return false;
        }
        if (this.parameter != null ? !this.parameter.equals(lMSSignature.parameter) : lMSSignature.parameter != null) {
            return false;
        }
        return Arrays.deepEquals((Object[])this.y, (Object[])lMSSignature.y);
    }

    public int hashCode() {
        int n2 = this.q;
        n2 = 31 * n2 + (this.otsSignature != null ? this.otsSignature.hashCode() : 0);
        n2 = 31 * n2 + (this.parameter != null ? this.parameter.hashCode() : 0);
        n2 = 31 * n2 + Arrays.deepHashCode((Object[])this.y);
        return n2;
    }

    @Override
    public byte[] getEncoded() throws IOException {
        return Composer.compose().u32str(this.q).bytes(this.otsSignature.getEncoded()).u32str(this.parameter.getType()).bytes(this.y).build();
    }

    public int getQ() {
        return this.q;
    }

    public LMOtsSignature getOtsSignature() {
        return this.otsSignature;
    }

    public LMSigParameters getParameter() {
        return this.parameter;
    }

    public byte[][] getY() {
        return this.y;
    }
}

