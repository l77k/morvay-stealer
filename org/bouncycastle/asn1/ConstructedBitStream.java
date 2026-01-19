/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.asn1;

import java.io.IOException;
import java.io.InputStream;
import org.bouncycastle.asn1.ASN1BitStringParser;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1StreamParser;

class ConstructedBitStream
extends InputStream {
    private final ASN1StreamParser _parser;
    private final boolean _octetAligned;
    private boolean _first = true;
    private int _padBits = 0;
    private ASN1BitStringParser _currentParser;
    private InputStream _currentStream;

    ConstructedBitStream(ASN1StreamParser aSN1StreamParser, boolean bl) {
        this._parser = aSN1StreamParser;
        this._octetAligned = bl;
    }

    int getPadBits() {
        return this._padBits;
    }

    @Override
    public int read(byte[] byArray, int n2, int n3) throws IOException {
        if (this._currentStream == null) {
            if (!this._first) {
                return -1;
            }
            this._currentParser = this.getNextParser();
            if (this._currentParser == null) {
                return -1;
            }
            this._first = false;
            this._currentStream = this._currentParser.getBitStream();
        }
        int n4 = 0;
        while (true) {
            int n5;
            if ((n5 = this._currentStream.read(byArray, n2 + n4, n3 - n4)) >= 0) {
                if ((n4 += n5) != n3) continue;
                return n4;
            }
            this._padBits = this._currentParser.getPadBits();
            this._currentParser = this.getNextParser();
            if (this._currentParser == null) {
                this._currentStream = null;
                return n4 < 1 ? -1 : n4;
            }
            this._currentStream = this._currentParser.getBitStream();
        }
    }

    @Override
    public int read() throws IOException {
        if (this._currentStream == null) {
            if (!this._first) {
                return -1;
            }
            this._currentParser = this.getNextParser();
            if (this._currentParser == null) {
                return -1;
            }
            this._first = false;
            this._currentStream = this._currentParser.getBitStream();
        }
        int n2;
        while ((n2 = this._currentStream.read()) < 0) {
            this._padBits = this._currentParser.getPadBits();
            this._currentParser = this.getNextParser();
            if (this._currentParser == null) {
                this._currentStream = null;
                return -1;
            }
            this._currentStream = this._currentParser.getBitStream();
        }
        return n2;
    }

    private ASN1BitStringParser getNextParser() throws IOException {
        ASN1Encodable aSN1Encodable = this._parser.readObject();
        if (aSN1Encodable == null) {
            if (this._octetAligned && this._padBits != 0) {
                throw new IOException("expected octet-aligned bitstring, but found padBits: " + this._padBits);
            }
            return null;
        }
        if (aSN1Encodable instanceof ASN1BitStringParser) {
            if (this._padBits != 0) {
                throw new IOException("only the last nested bitstring can have padding");
            }
            return (ASN1BitStringParser)aSN1Encodable;
        }
        throw new IOException("unknown object encountered: " + aSN1Encodable.getClass());
    }
}

