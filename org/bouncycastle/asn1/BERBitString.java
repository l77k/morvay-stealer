/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.asn1;

import java.io.IOException;
import org.bouncycastle.asn1.ASN1BitString;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1OutputStream;
import org.bouncycastle.asn1.DLBitString;

public class BERBitString
extends ASN1BitString {
    private static final int DEFAULT_SEGMENT_LIMIT = 1000;
    private final int segmentLimit;
    private final ASN1BitString[] elements;

    static byte[] flattenBitStrings(ASN1BitString[] aSN1BitStringArray) {
        int n2 = aSN1BitStringArray.length;
        switch (n2) {
            case 0: {
                return new byte[]{0};
            }
            case 1: {
                return aSN1BitStringArray[0].contents;
            }
        }
        int n3 = n2 - 1;
        int n4 = 0;
        for (int i2 = 0; i2 < n3; ++i2) {
            byte[] byArray = aSN1BitStringArray[i2].contents;
            if (byArray[0] != 0) {
                throw new IllegalArgumentException("only the last nested bitstring can have padding");
            }
            n4 += byArray.length - 1;
        }
        byte[] byArray = aSN1BitStringArray[n3].contents;
        byte by = byArray[0];
        byte[] byArray2 = new byte[n4 += byArray.length];
        byArray2[0] = by;
        int n5 = 1;
        for (int i3 = 0; i3 < n2; ++i3) {
            byte[] byArray3 = aSN1BitStringArray[i3].contents;
            int n6 = byArray3.length - 1;
            System.arraycopy(byArray3, 1, byArray2, n5, n6);
            n5 += n6;
        }
        return byArray2;
    }

    public BERBitString(byte[] byArray) {
        this(byArray, 0);
    }

    public BERBitString(byte by, int n2) {
        super(by, n2);
        this.elements = null;
        this.segmentLimit = 1000;
    }

    public BERBitString(byte[] byArray, int n2) {
        this(byArray, n2, 1000);
    }

    public BERBitString(byte[] byArray, int n2, int n3) {
        super(byArray, n2);
        this.elements = null;
        this.segmentLimit = n3;
    }

    public BERBitString(ASN1Encodable aSN1Encodable) throws IOException {
        this(aSN1Encodable.toASN1Primitive().getEncoded("DER"), 0);
    }

    public BERBitString(ASN1BitString[] aSN1BitStringArray) {
        this(aSN1BitStringArray, 1000);
    }

    public BERBitString(ASN1BitString[] aSN1BitStringArray, int n2) {
        super(BERBitString.flattenBitStrings(aSN1BitStringArray), false);
        this.elements = aSN1BitStringArray;
        this.segmentLimit = n2;
    }

    BERBitString(byte[] byArray, boolean bl) {
        super(byArray, bl);
        this.elements = null;
        this.segmentLimit = 1000;
    }

    @Override
    boolean encodeConstructed() {
        return null != this.elements || this.contents.length > this.segmentLimit;
    }

    @Override
    int encodedLength(boolean bl) throws IOException {
        int n2;
        if (!this.encodeConstructed()) {
            return DLBitString.encodedLength(bl, this.contents.length);
        }
        int n3 = n2 = bl ? 4 : 3;
        if (null != this.elements) {
            for (int i2 = 0; i2 < this.elements.length; ++i2) {
                n2 += this.elements[i2].encodedLength(true);
            }
        } else if (this.contents.length >= 2) {
            int n4 = (this.contents.length - 2) / (this.segmentLimit - 1);
            n2 += n4 * DLBitString.encodedLength(true, this.segmentLimit);
            int n5 = this.contents.length - n4 * (this.segmentLimit - 1);
            n2 += DLBitString.encodedLength(true, n5);
        }
        return n2;
    }

    @Override
    void encode(ASN1OutputStream aSN1OutputStream, boolean bl) throws IOException {
        if (!this.encodeConstructed()) {
            DLBitString.encode(aSN1OutputStream, bl, this.contents, 0, this.contents.length);
            return;
        }
        aSN1OutputStream.writeIdentifier(bl, 35);
        aSN1OutputStream.write(128);
        if (null != this.elements) {
            aSN1OutputStream.writePrimitives(this.elements);
        } else if (this.contents.length >= 2) {
            int n2;
            byte by = this.contents[0];
            int n3 = this.contents.length;
            int n4 = this.segmentLimit - 1;
            for (n2 = n3 - 1; n2 > n4; n2 -= n4) {
                DLBitString.encode(aSN1OutputStream, true, (byte)0, this.contents, n3 - n2, n4);
            }
            DLBitString.encode(aSN1OutputStream, true, by, this.contents, n3 - n2, n2);
        }
        aSN1OutputStream.write(0);
        aSN1OutputStream.write(0);
    }
}

