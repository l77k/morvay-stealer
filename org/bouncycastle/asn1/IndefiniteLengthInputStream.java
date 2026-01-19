/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.asn1;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import org.bouncycastle.asn1.LimitedInputStream;

class IndefiniteLengthInputStream
extends LimitedInputStream {
    private int _b1;
    private int _b2;
    private boolean _eofReached = false;
    private boolean _eofOn00 = true;

    IndefiniteLengthInputStream(InputStream inputStream2, int n2) throws IOException {
        super(inputStream2, n2);
        this._b1 = inputStream2.read();
        this._b2 = inputStream2.read();
        if (this._b2 < 0) {
            throw new EOFException();
        }
        this.checkForEof();
    }

    void setEofOn00(boolean bl) {
        this._eofOn00 = bl;
        this.checkForEof();
    }

    private boolean checkForEof() {
        if (!this._eofReached && this._eofOn00 && this._b1 == 0 && this._b2 == 0) {
            this._eofReached = true;
            this.setParentEofDetect(true);
        }
        return this._eofReached;
    }

    @Override
    public int read(byte[] byArray, int n2, int n3) throws IOException {
        if (this._eofOn00 || n3 < 3) {
            return super.read(byArray, n2, n3);
        }
        if (this._eofReached) {
            return -1;
        }
        int n4 = this._in.read(byArray, n2 + 2, n3 - 2);
        if (n4 < 0) {
            throw new EOFException();
        }
        byArray[n2] = (byte)this._b1;
        byArray[n2 + 1] = (byte)this._b2;
        this._b1 = this._in.read();
        this._b2 = this._in.read();
        if (this._b2 < 0) {
            throw new EOFException();
        }
        return n4 + 2;
    }

    @Override
    public int read() throws IOException {
        if (this.checkForEof()) {
            return -1;
        }
        int n2 = this._in.read();
        if (n2 < 0) {
            throw new EOFException();
        }
        int n3 = this._b1;
        this._b1 = this._b2;
        this._b2 = n2;
        return n3;
    }
}

