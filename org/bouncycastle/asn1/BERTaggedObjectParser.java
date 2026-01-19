/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.asn1;

import java.io.IOException;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ParsingException;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1StreamParser;
import org.bouncycastle.asn1.ASN1TaggedObjectParser;

class BERTaggedObjectParser
implements ASN1TaggedObjectParser {
    final int _tagClass;
    final int _tagNo;
    final ASN1StreamParser _parser;

    BERTaggedObjectParser(int n2, int n3, ASN1StreamParser aSN1StreamParser) {
        this._tagClass = n2;
        this._tagNo = n3;
        this._parser = aSN1StreamParser;
    }

    @Override
    public int getTagClass() {
        return this._tagClass;
    }

    @Override
    public int getTagNo() {
        return this._tagNo;
    }

    @Override
    public boolean hasContextTag() {
        return this._tagClass == 128;
    }

    @Override
    public boolean hasContextTag(int n2) {
        return this._tagClass == 128 && this._tagNo == n2;
    }

    @Override
    public boolean hasTag(int n2, int n3) {
        return this._tagClass == n2 && this._tagNo == n3;
    }

    @Override
    public boolean hasTagClass(int n2) {
        return this._tagClass == n2;
    }

    @Override
    public ASN1Primitive getLoadedObject() throws IOException {
        return this._parser.loadTaggedIL(this._tagClass, this._tagNo);
    }

    @Override
    public ASN1Encodable parseBaseUniversal(boolean bl, int n2) throws IOException {
        if (bl) {
            return this._parser.parseObject(n2);
        }
        return this._parser.parseImplicitConstructedIL(n2);
    }

    @Override
    public ASN1Encodable parseExplicitBaseObject() throws IOException {
        return this._parser.readObject();
    }

    @Override
    public ASN1TaggedObjectParser parseExplicitBaseTagged() throws IOException {
        return this._parser.parseTaggedObject();
    }

    @Override
    public ASN1TaggedObjectParser parseImplicitBaseTagged(int n2, int n3) throws IOException {
        return new BERTaggedObjectParser(n2, n3, this._parser);
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        try {
            return this.getLoadedObject();
        }
        catch (IOException iOException) {
            throw new ASN1ParsingException(iOException.getMessage());
        }
    }
}

