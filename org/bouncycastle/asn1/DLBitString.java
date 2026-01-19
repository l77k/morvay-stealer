/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.asn1;

import java.io.IOException;
import org.bouncycastle.asn1.ASN1BitString;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1OutputStream;
import org.bouncycastle.asn1.ASN1Primitive;

public class DLBitString
extends ASN1BitString {
    public DLBitString(byte[] byArray) {
        this(byArray, 0);
    }

    public DLBitString(byte by, int n2) {
        super(by, n2);
    }

    public DLBitString(byte[] byArray, int n2) {
        super(byArray, n2);
    }

    public DLBitString(int n2) {
        super(DLBitString.getBytes(n2), DLBitString.getPadBits(n2));
    }

    public DLBitString(ASN1Encodable aSN1Encodable) throws IOException {
        super(aSN1Encodable.toASN1Primitive().getEncoded("DER"), 0);
    }

    DLBitString(byte[] byArray, boolean bl) {
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
        aSN1OutputStream.writeEncodingDL(bl, 3, this.contents);
    }

    @Override
    ASN1Primitive toDLObject() {
        return this;
    }

    static int encodedLength(boolean bl, int n2) {
        return ASN1OutputStream.getLengthOfEncodingDL(bl, n2);
    }

    static void encode(ASN1OutputStream aSN1OutputStream, boolean bl, byte[] byArray, int n2, int n3) throws IOException {
        aSN1OutputStream.writeEncodingDL(bl, 3, byArray, n2, n3);
    }

    static void encode(ASN1OutputStream aSN1OutputStream, boolean bl, byte by, byte[] byArray, int n2, int n3) throws IOException {
        aSN1OutputStream.writeEncodingDL(bl, 3, by, byArray, n2, n3);
    }
}

