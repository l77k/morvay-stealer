/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.asn1;

import java.io.IOException;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1OutputStream;
import org.bouncycastle.asn1.DEROctetString;

public class BEROctetString
extends ASN1OctetString {
    private static final int DEFAULT_SEGMENT_LIMIT = 1000;
    private final int segmentLimit;
    private final ASN1OctetString[] elements;

    static byte[] flattenOctetStrings(ASN1OctetString[] aSN1OctetStringArray) {
        int n2 = aSN1OctetStringArray.length;
        switch (n2) {
            case 0: {
                return EMPTY_OCTETS;
            }
            case 1: {
                return aSN1OctetStringArray[0].string;
            }
        }
        int n3 = 0;
        for (int i2 = 0; i2 < n2; ++i2) {
            n3 += aSN1OctetStringArray[i2].string.length;
        }
        byte[] byArray = new byte[n3];
        int n4 = 0;
        for (int i3 = 0; i3 < n2; ++i3) {
            byte[] byArray2 = aSN1OctetStringArray[i3].string;
            System.arraycopy(byArray2, 0, byArray, n4, byArray2.length);
            n4 += byArray2.length;
        }
        return byArray;
    }

    public BEROctetString(byte[] byArray) {
        this(byArray, 1000);
    }

    public BEROctetString(ASN1OctetString[] aSN1OctetStringArray) {
        this(aSN1OctetStringArray, 1000);
    }

    public BEROctetString(byte[] byArray, int n2) {
        this(byArray, null, n2);
    }

    public BEROctetString(ASN1OctetString[] aSN1OctetStringArray, int n2) {
        this(BEROctetString.flattenOctetStrings(aSN1OctetStringArray), aSN1OctetStringArray, n2);
    }

    private BEROctetString(byte[] byArray, ASN1OctetString[] aSN1OctetStringArray, int n2) {
        super(byArray);
        this.elements = aSN1OctetStringArray;
        this.segmentLimit = n2;
    }

    @Override
    boolean encodeConstructed() {
        return true;
    }

    @Override
    int encodedLength(boolean bl) throws IOException {
        int n2;
        int n3 = n2 = bl ? 4 : 3;
        if (null != this.elements) {
            for (int i2 = 0; i2 < this.elements.length; ++i2) {
                n2 += this.elements[i2].encodedLength(true);
            }
        } else {
            int n4 = this.string.length / this.segmentLimit;
            n2 += n4 * DEROctetString.encodedLength(true, this.segmentLimit);
            int n5 = this.string.length - n4 * this.segmentLimit;
            if (n5 > 0) {
                n2 += DEROctetString.encodedLength(true, n5);
            }
        }
        return n2;
    }

    @Override
    void encode(ASN1OutputStream aSN1OutputStream, boolean bl) throws IOException {
        aSN1OutputStream.writeIdentifier(bl, 36);
        aSN1OutputStream.write(128);
        if (null != this.elements) {
            aSN1OutputStream.writePrimitives(this.elements);
        } else {
            int n2;
            for (int i2 = 0; i2 < this.string.length; i2 += n2) {
                n2 = Math.min(this.string.length - i2, this.segmentLimit);
                DEROctetString.encode(aSN1OutputStream, true, this.string, i2, n2);
            }
        }
        aSN1OutputStream.write(0);
        aSN1OutputStream.write(0);
    }
}

