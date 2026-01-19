/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.asn1;

import java.io.IOException;
import java.io.OutputStream;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1OutputStream;
import org.bouncycastle.asn1.ASN1Primitive;

class DLOutputStream
extends ASN1OutputStream {
    DLOutputStream(OutputStream outputStream2) {
        super(outputStream2);
    }

    @Override
    DLOutputStream getDLSubStream() {
        return this;
    }

    @Override
    void writeElements(ASN1Encodable[] aSN1EncodableArray) throws IOException {
        int n2 = aSN1EncodableArray.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            aSN1EncodableArray[i2].toASN1Primitive().toDLObject().encode(this, true);
        }
    }

    @Override
    void writePrimitive(ASN1Primitive aSN1Primitive, boolean bl) throws IOException {
        aSN1Primitive.toDLObject().encode(this, bl);
    }

    @Override
    void writePrimitives(ASN1Primitive[] aSN1PrimitiveArray) throws IOException {
        int n2 = aSN1PrimitiveArray.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            aSN1PrimitiveArray[i2].toDLObject().encode(this, true);
        }
    }
}

