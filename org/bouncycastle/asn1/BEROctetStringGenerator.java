/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.asn1;

import java.io.IOException;
import java.io.OutputStream;
import org.bouncycastle.asn1.BERGenerator;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DEROutputStream;

public class BEROctetStringGenerator
extends BERGenerator {
    public BEROctetStringGenerator(OutputStream outputStream2) throws IOException {
        super(outputStream2);
        this.writeBERHeader(36);
    }

    public BEROctetStringGenerator(OutputStream outputStream2, int n2, boolean bl) throws IOException {
        super(outputStream2, n2, bl);
        this.writeBERHeader(36);
    }

    public OutputStream getOctetOutputStream() {
        return this.getOctetOutputStream(new byte[1000]);
    }

    public OutputStream getOctetOutputStream(byte[] byArray) {
        return new BufferedBEROctetStream(byArray);
    }

    private class BufferedBEROctetStream
    extends OutputStream {
        private byte[] _buf;
        private int _off;
        private DEROutputStream _derOut;

        BufferedBEROctetStream(byte[] byArray) {
            this._buf = byArray;
            this._off = 0;
            this._derOut = new DEROutputStream(BEROctetStringGenerator.this._out);
        }

        @Override
        public void write(int n2) throws IOException {
            this._buf[this._off++] = (byte)n2;
            if (this._off == this._buf.length) {
                DEROctetString.encode(this._derOut, true, this._buf, 0, this._buf.length);
                this._off = 0;
            }
        }

        @Override
        public void write(byte[] byArray, int n2, int n3) throws IOException {
            int n4;
            int n5 = this._buf.length;
            int n6 = n5 - this._off;
            if (n3 < n6) {
                System.arraycopy(byArray, n2, this._buf, this._off, n3);
                this._off += n3;
                return;
            }
            int n7 = 0;
            if (this._off > 0) {
                System.arraycopy(byArray, n2, this._buf, this._off, n6);
                n7 += n6;
                DEROctetString.encode(this._derOut, true, this._buf, 0, n5);
            }
            while ((n4 = n3 - n7) >= n5) {
                DEROctetString.encode(this._derOut, true, byArray, n2 + n7, n5);
                n7 += n5;
            }
            System.arraycopy(byArray, n2 + n7, this._buf, 0, n4);
            this._off = n4;
        }

        @Override
        public void close() throws IOException {
            if (this._off != 0) {
                DEROctetString.encode(this._derOut, true, this._buf, 0, this._off);
            }
            this._derOut.flushInternal();
            BEROctetStringGenerator.this.writeBEREnd();
        }
    }
}

