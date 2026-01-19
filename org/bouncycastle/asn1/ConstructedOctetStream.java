/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.asn1;

import java.io.IOException;
import java.io.InputStream;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1OctetStringParser;
import org.bouncycastle.asn1.ASN1StreamParser;

class ConstructedOctetStream
extends InputStream {
    private final ASN1StreamParser _parser;
    private boolean _first = true;
    private InputStream _currentStream;

    ConstructedOctetStream(ASN1StreamParser aSN1StreamParser) {
        this._parser = aSN1StreamParser;
    }

    @Override
    public int read(byte[] byArray, int n2, int n3) throws IOException {
        if (this._currentStream == null) {
            if (!this._first) {
                return -1;
            }
            ASN1OctetStringParser aSN1OctetStringParser = this.getNextParser();
            if (aSN1OctetStringParser == null) {
                return -1;
            }
            this._first = false;
            this._currentStream = aSN1OctetStringParser.getOctetStream();
        }
        int n4 = 0;
        while (true) {
            int n5;
            if ((n5 = this._currentStream.read(byArray, n2 + n4, n3 - n4)) >= 0) {
                if ((n4 += n5) != n3) continue;
                return n4;
            }
            ASN1OctetStringParser aSN1OctetStringParser = this.getNextParser();
            if (aSN1OctetStringParser == null) {
                this._currentStream = null;
                return n4 < 1 ? -1 : n4;
            }
            this._currentStream = aSN1OctetStringParser.getOctetStream();
        }
    }

    @Override
    public int read() throws IOException {
        if (this._currentStream == null) {
            if (!this._first) {
                return -1;
            }
            ASN1OctetStringParser aSN1OctetStringParser = this.getNextParser();
            if (aSN1OctetStringParser == null) {
                return -1;
            }
            this._first = false;
            this._currentStream = aSN1OctetStringParser.getOctetStream();
        }
        int n2;
        while ((n2 = this._currentStream.read()) < 0) {
            ASN1OctetStringParser aSN1OctetStringParser = this.getNextParser();
            if (aSN1OctetStringParser == null) {
                this._currentStream = null;
                return -1;
            }
            this._currentStream = aSN1OctetStringParser.getOctetStream();
        }
        return n2;
    }

    private ASN1OctetStringParser getNextParser() throws IOException {
        ASN1Encodable aSN1Encodable = this._parser.readObject();
        if (aSN1Encodable == null) {
            return null;
        }
        if (aSN1Encodable instanceof ASN1OctetStringParser) {
            return (ASN1OctetStringParser)aSN1Encodable;
        }
        throw new IOException("unknown object encountered: " + aSN1Encodable.getClass());
    }
}

