/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.asn1;

import java.io.IOException;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1OutputStream;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.DLOutputStream;

public class DLSet
extends ASN1Set {
    private int contentsLength = -1;

    public DLSet() {
    }

    public DLSet(ASN1Encodable aSN1Encodable) {
        super(aSN1Encodable);
    }

    public DLSet(ASN1EncodableVector aSN1EncodableVector) {
        super(aSN1EncodableVector, false);
    }

    public DLSet(ASN1Encodable[] aSN1EncodableArray) {
        super(aSN1EncodableArray, false);
    }

    DLSet(boolean bl, ASN1Encodable[] aSN1EncodableArray) {
        super(bl, aSN1EncodableArray);
    }

    DLSet(ASN1Encodable[] aSN1EncodableArray, ASN1Encodable[] aSN1EncodableArray2) {
        super(aSN1EncodableArray, aSN1EncodableArray2);
    }

    private int getContentsLength() throws IOException {
        if (this.contentsLength < 0) {
            int n2 = this.elements.length;
            int n3 = 0;
            for (int i2 = 0; i2 < n2; ++i2) {
                ASN1Primitive aSN1Primitive = this.elements[i2].toASN1Primitive().toDLObject();
                n3 += aSN1Primitive.encodedLength(true);
            }
            this.contentsLength = n3;
        }
        return this.contentsLength;
    }

    @Override
    int encodedLength(boolean bl) throws IOException {
        return ASN1OutputStream.getLengthOfEncodingDL(bl, this.getContentsLength());
    }

    @Override
    void encode(ASN1OutputStream aSN1OutputStream, boolean bl) throws IOException {
        aSN1OutputStream.writeIdentifier(bl, 49);
        DLOutputStream dLOutputStream = aSN1OutputStream.getDLSubStream();
        int n2 = this.elements.length;
        if (this.contentsLength >= 0 || n2 > 16) {
            aSN1OutputStream.writeDL(this.getContentsLength());
            for (int i2 = 0; i2 < n2; ++i2) {
                ((ASN1OutputStream)dLOutputStream).writePrimitive(this.elements[i2].toASN1Primitive(), true);
            }
        } else {
            int n3;
            int n4 = 0;
            ASN1Primitive[] aSN1PrimitiveArray = new ASN1Primitive[n2];
            for (n3 = 0; n3 < n2; ++n3) {
                ASN1Primitive aSN1Primitive;
                aSN1PrimitiveArray[n3] = aSN1Primitive = this.elements[n3].toASN1Primitive().toDLObject();
                n4 += aSN1Primitive.encodedLength(true);
            }
            this.contentsLength = n4;
            aSN1OutputStream.writeDL(n4);
            for (n3 = 0; n3 < n2; ++n3) {
                ((ASN1OutputStream)dLOutputStream).writePrimitive(aSN1PrimitiveArray[n3], true);
            }
        }
    }

    @Override
    ASN1Primitive toDLObject() {
        return this;
    }
}

