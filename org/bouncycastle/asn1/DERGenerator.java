/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.asn1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.bouncycastle.asn1.ASN1Generator;

public abstract class DERGenerator
extends ASN1Generator {
    private boolean _tagged = false;
    private boolean _isExplicit;
    private int _tagNo;

    protected DERGenerator(OutputStream outputStream2) {
        super(outputStream2);
    }

    public DERGenerator(OutputStream outputStream2, int n2, boolean bl) {
        super(outputStream2);
        this._tagged = true;
        this._isExplicit = bl;
        this._tagNo = n2;
    }

    private void writeLength(OutputStream outputStream2, int n2) throws IOException {
        if (n2 > 127) {
            int n3 = 1;
            int n4 = n2;
            while ((n4 >>>= 8) != 0) {
                ++n3;
            }
            outputStream2.write((byte)(n3 | 0x80));
            for (int i2 = (n3 - 1) * 8; i2 >= 0; i2 -= 8) {
                outputStream2.write((byte)(n2 >> i2));
            }
        } else {
            outputStream2.write((byte)n2);
        }
    }

    void writeDEREncoded(OutputStream outputStream2, int n2, byte[] byArray) throws IOException {
        outputStream2.write(n2);
        this.writeLength(outputStream2, byArray.length);
        outputStream2.write(byArray);
    }

    void writeDEREncoded(int n2, byte[] byArray) throws IOException {
        if (this._tagged) {
            int n3 = this._tagNo | 0x80;
            if (this._isExplicit) {
                int n4 = this._tagNo | 0x20 | 0x80;
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                this.writeDEREncoded(byteArrayOutputStream, n2, byArray);
                this.writeDEREncoded(this._out, n4, byteArrayOutputStream.toByteArray());
            } else if ((n2 & 0x20) != 0) {
                this.writeDEREncoded(this._out, n3 | 0x20, byArray);
            } else {
                this.writeDEREncoded(this._out, n3, byArray);
            }
        } else {
            this.writeDEREncoded(this._out, n2, byArray);
        }
    }
}

