/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.asn1;

import java.io.IOException;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1OutputStream;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DLSequence;

public class DLTaggedObject
extends ASN1TaggedObject {
    public DLTaggedObject(int n2, ASN1Encodable aSN1Encodable) {
        super(true, n2, aSN1Encodable);
    }

    public DLTaggedObject(int n2, int n3, ASN1Encodable aSN1Encodable) {
        super(true, n2, n3, aSN1Encodable);
    }

    public DLTaggedObject(boolean bl, int n2, ASN1Encodable aSN1Encodable) {
        super(bl, n2, aSN1Encodable);
    }

    public DLTaggedObject(boolean bl, int n2, int n3, ASN1Encodable aSN1Encodable) {
        super(bl, n2, n3, aSN1Encodable);
    }

    DLTaggedObject(int n2, int n3, int n4, ASN1Encodable aSN1Encodable) {
        super(n2, n3, n4, aSN1Encodable);
    }

    @Override
    boolean encodeConstructed() {
        return this.isExplicit() || this.obj.toASN1Primitive().toDLObject().encodeConstructed();
    }

    @Override
    int encodedLength(boolean bl) throws IOException {
        ASN1Primitive aSN1Primitive = this.obj.toASN1Primitive().toDLObject();
        boolean bl2 = this.isExplicit();
        int n2 = aSN1Primitive.encodedLength(bl2);
        if (bl2) {
            n2 += ASN1OutputStream.getLengthOfDL(n2);
        }
        return n2 += bl ? ASN1OutputStream.getLengthOfIdentifier(this.tagNo) : 0;
    }

    @Override
    void encode(ASN1OutputStream aSN1OutputStream, boolean bl) throws IOException {
        ASN1Primitive aSN1Primitive = this.obj.toASN1Primitive().toDLObject();
        boolean bl2 = this.isExplicit();
        if (bl) {
            int n2 = this.tagClass;
            if (bl2 || aSN1Primitive.encodeConstructed()) {
                n2 |= 0x20;
            }
            aSN1OutputStream.writeIdentifier(true, n2, this.tagNo);
        }
        if (bl2) {
            aSN1OutputStream.writeDL(aSN1Primitive.encodedLength(true));
        }
        aSN1Primitive.encode(aSN1OutputStream.getDLSubStream(), bl2);
    }

    @Override
    ASN1Sequence rebuildConstructed(ASN1Primitive aSN1Primitive) {
        return new DLSequence(aSN1Primitive);
    }

    @Override
    ASN1TaggedObject replaceTag(int n2, int n3) {
        return new DLTaggedObject(this.explicitness, n2, n3, this.obj);
    }

    @Override
    ASN1Primitive toDLObject() {
        return this;
    }
}

