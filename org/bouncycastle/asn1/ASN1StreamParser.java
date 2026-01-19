/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.asn1;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Exception;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.ASN1TaggedObjectParser;
import org.bouncycastle.asn1.BERBitStringParser;
import org.bouncycastle.asn1.BEROctetStringParser;
import org.bouncycastle.asn1.BERSequenceParser;
import org.bouncycastle.asn1.BERSetParser;
import org.bouncycastle.asn1.BERTaggedObjectParser;
import org.bouncycastle.asn1.DERExternalParser;
import org.bouncycastle.asn1.DEROctetStringParser;
import org.bouncycastle.asn1.DLBitStringParser;
import org.bouncycastle.asn1.DLSequenceParser;
import org.bouncycastle.asn1.DLSetParser;
import org.bouncycastle.asn1.DLTaggedObjectParser;
import org.bouncycastle.asn1.DefiniteLengthInputStream;
import org.bouncycastle.asn1.InMemoryRepresentable;
import org.bouncycastle.asn1.IndefiniteLengthInputStream;
import org.bouncycastle.asn1.StreamUtil;

public class ASN1StreamParser {
    private final InputStream _in;
    private final int _limit;
    private final byte[][] tmpBuffers;

    public ASN1StreamParser(InputStream inputStream2) {
        this(inputStream2, StreamUtil.findLimit(inputStream2));
    }

    public ASN1StreamParser(byte[] byArray) {
        this(new ByteArrayInputStream(byArray), byArray.length);
    }

    public ASN1StreamParser(InputStream inputStream2, int n2) {
        this(inputStream2, n2, new byte[11][]);
    }

    ASN1StreamParser(InputStream inputStream2, int n2, byte[][] byArray) {
        this._in = inputStream2;
        this._limit = n2;
        this.tmpBuffers = byArray;
    }

    public ASN1Encodable readObject() throws IOException {
        int n2 = this._in.read();
        if (n2 < 0) {
            return null;
        }
        return this.implParseObject(n2);
    }

    ASN1Encodable implParseObject(int n2) throws IOException {
        this.set00Check(false);
        int n3 = ASN1InputStream.readTagNumber(this._in, n2);
        int n4 = ASN1InputStream.readLength(this._in, this._limit, n3 == 3 || n3 == 4 || n3 == 16 || n3 == 17 || n3 == 8);
        if (n4 < 0) {
            if (0 == (n2 & 0x20)) {
                throw new IOException("indefinite-length primitive encoding encountered");
            }
            IndefiniteLengthInputStream indefiniteLengthInputStream = new IndefiniteLengthInputStream(this._in, this._limit);
            ASN1StreamParser aSN1StreamParser = new ASN1StreamParser(indefiniteLengthInputStream, this._limit, this.tmpBuffers);
            int n5 = n2 & 0xC0;
            if (0 != n5) {
                return new BERTaggedObjectParser(n5, n3, aSN1StreamParser);
            }
            return aSN1StreamParser.parseImplicitConstructedIL(n3);
        }
        DefiniteLengthInputStream definiteLengthInputStream = new DefiniteLengthInputStream(this._in, n4, this._limit);
        if (0 == (n2 & 0xE0)) {
            return this.parseImplicitPrimitive(n3, definiteLengthInputStream);
        }
        ASN1StreamParser aSN1StreamParser = new ASN1StreamParser(definiteLengthInputStream, definiteLengthInputStream.getLimit(), this.tmpBuffers);
        int n6 = n2 & 0xC0;
        if (0 != n6) {
            boolean bl = (n2 & 0x20) != 0;
            return new DLTaggedObjectParser(n6, n3, bl, aSN1StreamParser);
        }
        return aSN1StreamParser.parseImplicitConstructedDL(n3);
    }

    ASN1Primitive loadTaggedDL(int n2, int n3, boolean bl) throws IOException {
        if (!bl) {
            byte[] byArray = ((DefiniteLengthInputStream)this._in).toByteArray();
            return ASN1TaggedObject.createPrimitive(n2, n3, byArray);
        }
        ASN1EncodableVector aSN1EncodableVector = this.readVector();
        return ASN1TaggedObject.createConstructedDL(n2, n3, aSN1EncodableVector);
    }

    ASN1Primitive loadTaggedIL(int n2, int n3) throws IOException {
        ASN1EncodableVector aSN1EncodableVector = this.readVector();
        return ASN1TaggedObject.createConstructedIL(n2, n3, aSN1EncodableVector);
    }

    ASN1Encodable parseImplicitConstructedDL(int n2) throws IOException {
        switch (n2) {
            case 3: {
                return new BERBitStringParser(this);
            }
            case 8: {
                return new DERExternalParser(this);
            }
            case 4: {
                return new BEROctetStringParser(this);
            }
            case 17: {
                return new DLSetParser(this);
            }
            case 16: {
                return new DLSequenceParser(this);
            }
        }
        throw new ASN1Exception("unknown DL object encountered: 0x" + Integer.toHexString(n2));
    }

    ASN1Encodable parseImplicitConstructedIL(int n2) throws IOException {
        switch (n2) {
            case 3: {
                return new BERBitStringParser(this);
            }
            case 4: {
                return new BEROctetStringParser(this);
            }
            case 8: {
                return new DERExternalParser(this);
            }
            case 16: {
                return new BERSequenceParser(this);
            }
            case 17: {
                return new BERSetParser(this);
            }
        }
        throw new ASN1Exception("unknown BER object encountered: 0x" + Integer.toHexString(n2));
    }

    ASN1Encodable parseImplicitPrimitive(int n2) throws IOException {
        return this.parseImplicitPrimitive(n2, (DefiniteLengthInputStream)this._in);
    }

    ASN1Encodable parseImplicitPrimitive(int n2, DefiniteLengthInputStream definiteLengthInputStream) throws IOException {
        switch (n2) {
            case 3: {
                return new DLBitStringParser(definiteLengthInputStream);
            }
            case 8: {
                throw new ASN1Exception("externals must use constructed encoding (see X.690 8.18)");
            }
            case 4: {
                return new DEROctetStringParser(definiteLengthInputStream);
            }
            case 17: {
                throw new ASN1Exception("sequences must use constructed encoding (see X.690 8.9.1/8.10.1)");
            }
            case 16: {
                throw new ASN1Exception("sets must use constructed encoding (see X.690 8.11.1/8.12.1)");
            }
        }
        try {
            return ASN1InputStream.createPrimitiveDERObject(n2, definiteLengthInputStream, this.tmpBuffers);
        }
        catch (IllegalArgumentException illegalArgumentException) {
            throw new ASN1Exception("corrupted stream detected", illegalArgumentException);
        }
    }

    ASN1Encodable parseObject(int n2) throws IOException {
        if (n2 < 0 || n2 > 30) {
            throw new IllegalArgumentException("invalid universal tag number: " + n2);
        }
        int n3 = this._in.read();
        if (n3 < 0) {
            return null;
        }
        if ((n3 & 0xFFFFFFDF) != n2) {
            throw new IOException("unexpected identifier encountered: " + n3);
        }
        return this.implParseObject(n3);
    }

    ASN1TaggedObjectParser parseTaggedObject() throws IOException {
        int n2 = this._in.read();
        if (n2 < 0) {
            return null;
        }
        int n3 = n2 & 0xC0;
        if (0 == n3) {
            throw new ASN1Exception("no tagged object found");
        }
        return (ASN1TaggedObjectParser)this.implParseObject(n2);
    }

    ASN1EncodableVector readVector() throws IOException {
        int n2 = this._in.read();
        if (n2 < 0) {
            return new ASN1EncodableVector(0);
        }
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        do {
            ASN1Encodable aSN1Encodable;
            if ((aSN1Encodable = this.implParseObject(n2)) instanceof InMemoryRepresentable) {
                aSN1EncodableVector.add(((InMemoryRepresentable)((Object)aSN1Encodable)).getLoadedObject());
                continue;
            }
            aSN1EncodableVector.add(aSN1Encodable.toASN1Primitive());
        } while ((n2 = this._in.read()) >= 0);
        return aSN1EncodableVector;
    }

    private void set00Check(boolean bl) {
        if (this._in instanceof IndefiniteLengthInputStream) {
            ((IndefiniteLengthInputStream)this._in).setEofOn00(bl);
        }
    }
}

