/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.asn1;

import java.io.IOException;
import org.bouncycastle.asn1.ASN1BitString;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1OutputStream;
import org.bouncycastle.asn1.ASN1Primitive;

public class DERBitString
extends ASN1BitString {
    public static DERBitString convert(ASN1BitString aSN1BitString) {
        return (DERBitString)aSN1BitString.toDERObject();
    }

    public DERBitString(byte[] byArray) {
        this(byArray, 0);
    }

    public DERBitString(byte by, int n2) {
        super(by, n2);
    }

    public DERBitString(byte[] byArray, int n2) {
        super(byArray, n2);
    }

    public DERBitString(int n2) {
        super(DERBitString.getBytes(n2), DERBitString.getPadBits(n2));
    }

    public DERBitString(ASN1Encodable aSN1Encodable) throws IOException {
        super(aSN1Encodable.toASN1Primitive().getEncoded("DER"), 0);
    }

    DERBitString(byte[] byArray, boolean bl) {
        super(byArray, bl);
    }

    @Override
    boolean encodeConstructed() {
        return false;
    }

    @Override
    int encodedLength(boolean bl) {
        return ASN1OutputStream.getLengthOfEncodingDL(bl, this.contents.length);
    }

    @Override
    void encode(ASN1OutputStream aSN1OutputStream, boolean bl) throws IOException {
        int n2 = this.contents.length;
        int n3 = n2 - 1;
        byte by = this.contents[n3];
        int n4 = this.contents[0] & 0xFF;
        byte by2 = (byte)(this.contents[n3] & 255 << n4);
        if (by == by2) {
            aSN1OutputStream.writeEncodingDL(bl, 3, this.contents);
        } else {
            aSN1OutputStream.writeEncodingDL(bl, 3, this.contents, 0, n3, by2);
        }
    }

    @Override
    ASN1Primitive toDERObject() {
        return this;
    }

    @Override
    ASN1Primitive toDLObject() {
        return this;
    }

    static DERBitString fromOctetString(ASN1OctetString aSN1OctetString) {
        return new DERBitString(aSN1OctetString.getOctets(), true);
    }
}

