/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.asn1;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import org.bouncycastle.asn1.LimitedInputStream;
import org.bouncycastle.util.io.Streams;

class DefiniteLengthInputStream
extends LimitedInputStream {
    private static final byte[] EMPTY_BYTES = new byte[0];
    private final int _originalLength;
    private int _remaining;

    DefiniteLengthInputStream(InputStream inputStream2, int n2, int n3) {
        super(inputStream2, n3);
        if (n2 <= 0) {
            if (n2 < 0) {
                throw new IllegalArgumentException("negative lengths not allowed");
            }
            this.setParentEofDetect(true);
        }
        this._originalLength = n2;
        this._remaining = n2;
    }

    int getRemaining() {
        return this._remaining;
    }

    @Override
    public int read() throws IOException {
        if (this._remaining == 0) {
            return -1;
        }
        int n2 = this._in.read();
        if (n2 < 0) {
            throw new EOFException("DEF length " + this._originalLength + " object truncated by " + this._remaining);
        }
        if (--this._remaining == 0) {
            this.setParentEofDetect(true);
        }
        return n2;
    }

    @Override
    public int read(byte[] byArray, int n2, int n3) throws IOException {
        if (this._remaining == 0) {
            return -1;
        }
        int n4 = Math.min(n3, this._remaining);
        int n5 = this._in.read(byArray, n2, n4);
        if (n5 < 0) {
            throw new EOFException("DEF length " + this._originalLength + " object truncated by " + this._remaining);
        }
        if ((this._remaining -= n5) == 0) {
            this.setParentEofDetect(true);
        }
        return n5;
    }

    void readAllIntoByteArray(byte[] byArray) throws IOException {
        if (this._remaining != byArray.length) {
            throw new IllegalArgumentException("buffer length not right for data");
        }
        if (this._remaining == 0) {
            return;
        }
        int n2 = this.getLimit();
        if (this._remaining >= n2) {
            throw new IOException("corrupted stream - out of bounds length found: " + this._remaining + " >= " + n2);
        }
        if ((this._remaining -= Streams.readFully(this._in, byArray, 0, byArray.length)) != 0) {
            throw new EOFException("DEF length " + this._originalLength + " object truncated by " + this._remaining);
        }
        this.setParentEofDetect(true);
    }

    byte[] toByteArray() throws IOException {
        if (this._remaining == 0) {
            return EMPTY_BYTES;
        }
        int n2 = this.getLimit();
        if (this._remaining >= n2) {
            throw new IOException("corrupted stream - out of bounds length found: " + this._remaining + " >= " + n2);
        }
        byte[] byArray = new byte[this._remaining];
        if ((this._remaining -= Streams.readFully(this._in, byArray, 0, byArray.length)) != 0) {
            throw new EOFException("DEF length " + this._originalLength + " object truncated by " + this._remaining);
        }
        this.setParentEofDetect(true);
        return byArray;
    }
}

