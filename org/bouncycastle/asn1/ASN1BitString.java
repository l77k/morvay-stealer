/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.asn1;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.bouncycastle.asn1.ASN1BitStringParser;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1ParsingException;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1String;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.ASN1UniversalType;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DLBitString;
import org.bouncycastle.util.Arrays;

public abstract class ASN1BitString
extends ASN1Primitive
implements ASN1String,
ASN1BitStringParser {
    static final ASN1UniversalType TYPE = new ASN1UniversalType(ASN1BitString.class, 3){

        @Override
        ASN1Primitive fromImplicitPrimitive(DEROctetString dEROctetString) {
            return ASN1BitString.createPrimitive(dEROctetString.getOctets());
        }

        @Override
        ASN1Primitive fromImplicitConstructed(ASN1Sequence aSN1Sequence) {
            return aSN1Sequence.toASN1BitString();
        }
    };
    private static final char[] table = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    final byte[] contents;

    public static ASN1BitString getInstance(Object object) {
        if (object == null || object instanceof ASN1BitString) {
            return (ASN1BitString)object;
        }
        if (object instanceof ASN1Encodable) {
            ASN1Primitive aSN1Primitive = ((ASN1Encodable)object).toASN1Primitive();
            if (aSN1Primitive instanceof ASN1BitString) {
                return (ASN1BitString)aSN1Primitive;
            }
        } else if (object instanceof byte[]) {
            try {
                return (ASN1BitString)TYPE.fromByteArray((byte[])object);
            }
            catch (IOException iOException) {
                throw new IllegalArgumentException("failed to construct BIT STRING from byte[]: " + iOException.getMessage());
            }
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + object.getClass().getName());
    }

    public static ASN1BitString getInstance(ASN1TaggedObject aSN1TaggedObject, boolean bl) {
        return (ASN1BitString)TYPE.getContextInstance(aSN1TaggedObject, bl);
    }

    protected static int getPadBits(int n2) {
        int n3;
        int n4 = 0;
        for (n3 = 3; n3 >= 0; --n3) {
            if (n3 != 0) {
                if (n2 >> n3 * 8 == 0) continue;
                n4 = n2 >> n3 * 8 & 0xFF;
                break;
            }
            if (n2 == 0) continue;
            n4 = n2 & 0xFF;
            break;
        }
        if (n4 == 0) {
            return 0;
        }
        n3 = 1;
        while (((n4 <<= 1) & 0xFF) != 0) {
            ++n3;
        }
        return 8 - n3;
    }

    protected static byte[] getBytes(int n2) {
        if (n2 == 0) {
            return new byte[0];
        }
        int n3 = 4;
        for (int i2 = 3; i2 >= 1 && (n2 & 255 << i2 * 8) == 0; --i2) {
            --n3;
        }
        byte[] byArray = new byte[n3];
        for (int i3 = 0; i3 < n3; ++i3) {
            byArray[i3] = (byte)(n2 >> i3 * 8 & 0xFF);
        }
        return byArray;
    }

    ASN1BitString(byte by, int n2) {
        if (n2 > 7 || n2 < 0) {
            throw new IllegalArgumentException("pad bits cannot be greater than 7 or less than 0");
        }
        this.contents = new byte[]{(byte)n2, by};
    }

    ASN1BitString(byte[] byArray, int n2) {
        if (byArray == null) {
            throw new NullPointerException("'data' cannot be null");
        }
        if (byArray.length == 0 && n2 != 0) {
            throw new IllegalArgumentException("zero length data with non-zero pad bits");
        }
        if (n2 > 7 || n2 < 0) {
            throw new IllegalArgumentException("pad bits cannot be greater than 7 or less than 0");
        }
        this.contents = Arrays.prepend(byArray, (byte)n2);
    }

    ASN1BitString(byte[] byArray, boolean bl) {
        if (bl) {
            if (null == byArray) {
                throw new NullPointerException("'contents' cannot be null");
            }
            if (byArray.length < 1) {
                throw new IllegalArgumentException("'contents' cannot be empty");
            }
            int n2 = byArray[0] & 0xFF;
            if (n2 > 0) {
                if (byArray.length < 2) {
                    throw new IllegalArgumentException("zero length data with non-zero pad bits");
                }
                if (n2 > 7) {
                    throw new IllegalArgumentException("pad bits cannot be greater than 7 or less than 0");
                }
            }
        }
        this.contents = byArray;
    }

    @Override
    public InputStream getBitStream() throws IOException {
        return new ByteArrayInputStream(this.contents, 1, this.contents.length - 1);
    }

    @Override
    public InputStream getOctetStream() throws IOException {
        int n2 = this.contents[0] & 0xFF;
        if (0 != n2) {
            throw new IOException("expected octet-aligned bitstring, but found padBits: " + n2);
        }
        return this.getBitStream();
    }

    public ASN1BitStringParser parser() {
        return this;
    }

    @Override
    public String getString() {
        byte[] byArray;
        try {
            byArray = this.getEncoded();
        }
        catch (IOException iOException) {
            throw new ASN1ParsingException("Internal error encoding BitString: " + iOException.getMessage(), iOException);
        }
        StringBuffer stringBuffer = new StringBuffer(1 + byArray.length * 2);
        stringBuffer.append('#');
        for (int i2 = 0; i2 != byArray.length; ++i2) {
            byte by = byArray[i2];
            stringBuffer.append(table[by >>> 4 & 0xF]);
            stringBuffer.append(table[by & 0xF]);
        }
        return stringBuffer.toString();
    }

    public int intValue() {
        int n2;
        int n3 = 0;
        int n4 = Math.min(5, this.contents.length - 1);
        for (n2 = 1; n2 < n4; ++n2) {
            n3 |= (this.contents[n2] & 0xFF) << 8 * (n2 - 1);
        }
        if (1 <= n4 && n4 < 5) {
            n2 = this.contents[0] & 0xFF;
            byte by = (byte)(this.contents[n4] & 255 << n2);
            n3 |= (by & 0xFF) << 8 * (n4 - 1);
        }
        return n3;
    }

    public byte[] getOctets() {
        if (this.contents[0] != 0) {
            throw new IllegalStateException("attempt to get non-octet aligned data from BIT STRING");
        }
        return Arrays.copyOfRange(this.contents, 1, this.contents.length);
    }

    public byte[] getBytes() {
        if (this.contents.length == 1) {
            return ASN1OctetString.EMPTY_OCTETS;
        }
        int n2 = this.contents[0] & 0xFF;
        byte[] byArray = Arrays.copyOfRange(this.contents, 1, this.contents.length);
        int n3 = byArray.length - 1;
        byArray[n3] = (byte)(byArray[n3] & (byte)(255 << n2));
        return byArray;
    }

    @Override
    public int getPadBits() {
        return this.contents[0] & 0xFF;
    }

    public String toString() {
        return this.getString();
    }

    @Override
    public int hashCode() {
        if (this.contents.length < 2) {
            return 1;
        }
        int n2 = this.contents[0] & 0xFF;
        int n3 = this.contents.length - 1;
        byte by = (byte)(this.contents[n3] & 255 << n2);
        int n4 = Arrays.hashCode(this.contents, 0, n3);
        n4 *= 257;
        return n4 ^= by;
    }

    @Override
    boolean asn1Equals(ASN1Primitive aSN1Primitive) {
        int n2;
        if (!(aSN1Primitive instanceof ASN1BitString)) {
            return false;
        }
        ASN1BitString aSN1BitString = (ASN1BitString)aSN1Primitive;
        byte[] byArray = aSN1BitString.contents;
        byte[] byArray2 = this.contents;
        int n3 = byArray2.length;
        if (byArray.length != n3) {
            return false;
        }
        if (n3 == 1) {
            return true;
        }
        int n4 = n3 - 1;
        for (n2 = 0; n2 < n4; ++n2) {
            if (byArray2[n2] == byArray[n2]) continue;
            return false;
        }
        n2 = byArray2[0] & 0xFF;
        byte by = (byte)(byArray2[n4] & 255 << n2);
        byte by2 = (byte)(byArray[n4] & 255 << n2);
        return by == by2;
    }

    @Override
    public ASN1Primitive getLoadedObject() {
        return this.toASN1Primitive();
    }

    @Override
    ASN1Primitive toDERObject() {
        return new DERBitString(this.contents, false);
    }

    @Override
    ASN1Primitive toDLObject() {
        return new DLBitString(this.contents, false);
    }

    static ASN1BitString createPrimitive(byte[] byArray) {
        int n2 = byArray.length;
        if (n2 < 1) {
            throw new IllegalArgumentException("truncated BIT STRING detected");
        }
        int n3 = byArray[0] & 0xFF;
        if (n3 > 0) {
            if (n3 > 7 || n2 < 2) {
                throw new IllegalArgumentException("invalid pad bits detected");
            }
            byte by = byArray[n2 - 1];
            if (by != (byte)(by & 255 << n3)) {
                return new DLBitString(byArray, false);
            }
        }
        return new DERBitString(byArray, false);
    }
}

