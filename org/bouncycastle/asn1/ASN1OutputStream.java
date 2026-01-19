/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.asn1;

import java.io.IOException;
import java.io.OutputStream;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DEROutputStream;
import org.bouncycastle.asn1.DLOutputStream;

public class ASN1OutputStream {
    private OutputStream os;

    public static ASN1OutputStream create(OutputStream outputStream2) {
        return new ASN1OutputStream(outputStream2);
    }

    public static ASN1OutputStream create(OutputStream outputStream2, String string) {
        if (string.equals("DER")) {
            return new DEROutputStream(outputStream2);
        }
        if (string.equals("DL")) {
            return new DLOutputStream(outputStream2);
        }
        return new ASN1OutputStream(outputStream2);
    }

    ASN1OutputStream(OutputStream outputStream2) {
        this.os = outputStream2;
    }

    public void close() throws IOException {
        this.os.close();
    }

    public void flush() throws IOException {
        this.os.flush();
    }

    public final void writeObject(ASN1Encodable aSN1Encodable) throws IOException {
        if (null == aSN1Encodable) {
            throw new IOException("null object detected");
        }
        this.writePrimitive(aSN1Encodable.toASN1Primitive(), true);
        this.flushInternal();
    }

    public final void writeObject(ASN1Primitive aSN1Primitive) throws IOException {
        if (null == aSN1Primitive) {
            throw new IOException("null object detected");
        }
        this.writePrimitive(aSN1Primitive, true);
        this.flushInternal();
    }

    void flushInternal() throws IOException {
    }

    DEROutputStream getDERSubStream() {
        return new DEROutputStream(this.os);
    }

    DLOutputStream getDLSubStream() {
        return new DLOutputStream(this.os);
    }

    final void writeDL(int n2) throws IOException {
        if (n2 < 128) {
            this.write(n2);
        } else {
            byte[] byArray = new byte[5];
            int n3 = byArray.length;
            do {
                byArray[--n3] = (byte)n2;
            } while ((n2 >>>= 8) != 0);
            int n4 = byArray.length - n3;
            byArray[--n3] = (byte)(0x80 | n4);
            this.write(byArray, n3, n4 + 1);
        }
    }

    final void write(int n2) throws IOException {
        this.os.write(n2);
    }

    final void write(byte[] byArray, int n2, int n3) throws IOException {
        this.os.write(byArray, n2, n3);
    }

    void writeElements(ASN1Encodable[] aSN1EncodableArray) throws IOException {
        int n2 = aSN1EncodableArray.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            aSN1EncodableArray[i2].toASN1Primitive().encode(this, true);
        }
    }

    final void writeEncodingDL(boolean bl, int n2, byte by) throws IOException {
        this.writeIdentifier(bl, n2);
        this.writeDL(1);
        this.write(by);
    }

    final void writeEncodingDL(boolean bl, int n2, byte[] byArray) throws IOException {
        this.writeIdentifier(bl, n2);
        this.writeDL(byArray.length);
        this.write(byArray, 0, byArray.length);
    }

    final void writeEncodingDL(boolean bl, int n2, byte[] byArray, int n3, int n4) throws IOException {
        this.writeIdentifier(bl, n2);
        this.writeDL(n4);
        this.write(byArray, n3, n4);
    }

    final void writeEncodingDL(boolean bl, int n2, byte by, byte[] byArray, int n3, int n4) throws IOException {
        this.writeIdentifier(bl, n2);
        this.writeDL(1 + n4);
        this.write(by);
        this.write(byArray, n3, n4);
    }

    final void writeEncodingDL(boolean bl, int n2, byte[] byArray, int n3, int n4, byte by) throws IOException {
        this.writeIdentifier(bl, n2);
        this.writeDL(n4 + 1);
        this.write(byArray, n3, n4);
        this.write(by);
    }

    final void writeEncodingDL(boolean bl, int n2, int n3, byte[] byArray) throws IOException {
        this.writeIdentifier(bl, n2, n3);
        this.writeDL(byArray.length);
        this.write(byArray, 0, byArray.length);
    }

    final void writeEncodingIL(boolean bl, int n2, ASN1Encodable[] aSN1EncodableArray) throws IOException {
        this.writeIdentifier(bl, n2);
        this.write(128);
        this.writeElements(aSN1EncodableArray);
        this.write(0);
        this.write(0);
    }

    final void writeIdentifier(boolean bl, int n2) throws IOException {
        if (bl) {
            this.write(n2);
        }
    }

    final void writeIdentifier(boolean bl, int n2, int n3) throws IOException {
        if (bl) {
            if (n3 < 31) {
                this.write(n2 | n3);
            } else {
                byte[] byArray = new byte[6];
                int n4 = byArray.length;
                byArray[--n4] = (byte)(n3 & 0x7F);
                while (n3 > 127) {
                    byArray[--n4] = (byte)((n3 >>>= 7) & 0x7F | 0x80);
                }
                byArray[--n4] = (byte)(n2 | 0x1F);
                this.write(byArray, n4, byArray.length - n4);
            }
        }
    }

    void writePrimitive(ASN1Primitive aSN1Primitive, boolean bl) throws IOException {
        aSN1Primitive.encode(this, bl);
    }

    void writePrimitives(ASN1Primitive[] aSN1PrimitiveArray) throws IOException {
        int n2 = aSN1PrimitiveArray.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            aSN1PrimitiveArray[i2].encode(this, true);
        }
    }

    static int getLengthOfDL(int n2) {
        if (n2 < 128) {
            return 1;
        }
        int n3 = 2;
        while ((n2 >>>= 8) != 0) {
            ++n3;
        }
        return n3;
    }

    static int getLengthOfEncodingDL(boolean bl, int n2) {
        return (bl ? 1 : 0) + ASN1OutputStream.getLengthOfDL(n2) + n2;
    }

    static int getLengthOfIdentifier(int n2) {
        if (n2 < 31) {
            return 1;
        }
        int n3 = 2;
        while ((n2 >>>= 7) != 0) {
            ++n3;
        }
        return n3;
    }
}

