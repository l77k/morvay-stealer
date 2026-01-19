/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.asn1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OutputStream;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.ASN1UniversalType;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.OIDTokenizer;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Properties;

public class ASN1RelativeOID
extends ASN1Primitive {
    static final ASN1UniversalType TYPE = new ASN1UniversalType(ASN1RelativeOID.class, 13){

        @Override
        ASN1Primitive fromImplicitPrimitive(DEROctetString dEROctetString) {
            return ASN1RelativeOID.createPrimitive(dEROctetString.getOctets(), false);
        }
    };
    private static final int MAX_CONTENTS_LENGTH = 4096;
    private static final int MAX_IDENTIFIER_LENGTH = 16383;
    private static final long LONG_LIMIT = 0xFFFFFFFFFFFF80L;
    private static final ConcurrentMap<ASN1ObjectIdentifier.OidHandle, ASN1RelativeOID> pool = new ConcurrentHashMap<ASN1ObjectIdentifier.OidHandle, ASN1RelativeOID>();
    private final byte[] contents;
    private String identifier;

    public static ASN1RelativeOID fromContents(byte[] byArray) {
        if (byArray == null) {
            throw new NullPointerException("'contents' cannot be null");
        }
        return ASN1RelativeOID.createPrimitive(byArray, true);
    }

    public static ASN1RelativeOID getInstance(Object object) {
        if (object == null || object instanceof ASN1RelativeOID) {
            return (ASN1RelativeOID)object;
        }
        if (object instanceof ASN1Encodable) {
            ASN1Primitive aSN1Primitive = ((ASN1Encodable)object).toASN1Primitive();
            if (aSN1Primitive instanceof ASN1RelativeOID) {
                return (ASN1RelativeOID)aSN1Primitive;
            }
        } else if (object instanceof byte[]) {
            byte[] byArray = (byte[])object;
            try {
                return (ASN1RelativeOID)TYPE.fromByteArray(byArray);
            }
            catch (IOException iOException) {
                throw new IllegalArgumentException("failed to construct relative OID from byte[]: " + iOException.getMessage());
            }
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + object.getClass().getName());
    }

    public static ASN1RelativeOID getInstance(ASN1TaggedObject aSN1TaggedObject, boolean bl) {
        return (ASN1RelativeOID)TYPE.getContextInstance(aSN1TaggedObject, bl);
    }

    public static ASN1RelativeOID tryFromID(String string) {
        byte[] byArray;
        if (string == null) {
            throw new NullPointerException("'identifier' cannot be null");
        }
        if (string.length() <= 16383 && ASN1RelativeOID.isValidIdentifier(string, 0) && (byArray = ASN1RelativeOID.parseIdentifier(string)).length <= 4096) {
            return new ASN1RelativeOID(byArray, string);
        }
        return null;
    }

    public ASN1RelativeOID(String string) {
        ASN1RelativeOID.checkIdentifier(string);
        byte[] byArray = ASN1RelativeOID.parseIdentifier(string);
        ASN1RelativeOID.checkContentsLength(byArray.length);
        this.contents = byArray;
        this.identifier = string;
    }

    private ASN1RelativeOID(byte[] byArray, String string) {
        this.contents = byArray;
        this.identifier = string;
    }

    public ASN1RelativeOID branch(String string) {
        byte[] byArray;
        ASN1RelativeOID.checkIdentifier(string);
        if (string.length() <= 2) {
            ASN1RelativeOID.checkContentsLength(this.contents.length + 1);
            int n2 = string.charAt(0) - 48;
            if (string.length() == 2) {
                n2 *= 10;
                n2 += string.charAt(1) - 48;
            }
            byArray = Arrays.append(this.contents, (byte)n2);
        } else {
            byte[] byArray2 = ASN1RelativeOID.parseIdentifier(string);
            ASN1RelativeOID.checkContentsLength(this.contents.length + byArray2.length);
            byArray = Arrays.concatenate(this.contents, byArray2);
        }
        String string2 = this.getId();
        String string3 = string2 + "." + string;
        return new ASN1RelativeOID(byArray, string3);
    }

    public synchronized String getId() {
        if (this.identifier == null) {
            this.identifier = ASN1RelativeOID.parseContents(this.contents);
        }
        return this.identifier;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.contents);
    }

    public String toString() {
        return this.getId();
    }

    @Override
    boolean asn1Equals(ASN1Primitive aSN1Primitive) {
        if (this == aSN1Primitive) {
            return true;
        }
        if (!(aSN1Primitive instanceof ASN1RelativeOID)) {
            return false;
        }
        ASN1RelativeOID aSN1RelativeOID = (ASN1RelativeOID)aSN1Primitive;
        return Arrays.areEqual(this.contents, aSN1RelativeOID.contents);
    }

    @Override
    int encodedLength(boolean bl) {
        return ASN1OutputStream.getLengthOfEncodingDL(bl, this.contents.length);
    }

    @Override
    void encode(ASN1OutputStream aSN1OutputStream, boolean bl) throws IOException {
        aSN1OutputStream.writeEncodingDL(bl, 13, this.contents);
    }

    @Override
    boolean encodeConstructed() {
        return false;
    }

    static void checkContentsLength(int n2) {
        if (n2 > 4096) {
            throw new IllegalArgumentException("exceeded relative OID contents length limit");
        }
    }

    static void checkIdentifier(String string) {
        if (string == null) {
            throw new NullPointerException("'identifier' cannot be null");
        }
        if (string.length() > 16383) {
            throw new IllegalArgumentException("exceeded relative OID contents length limit");
        }
        if (!ASN1RelativeOID.isValidIdentifier(string, 0)) {
            throw new IllegalArgumentException("string " + string + " not a valid relative OID");
        }
    }

    static ASN1RelativeOID createPrimitive(byte[] byArray, boolean bl) {
        ASN1RelativeOID.checkContentsLength(byArray.length);
        ASN1ObjectIdentifier.OidHandle oidHandle = new ASN1ObjectIdentifier.OidHandle(byArray);
        ASN1RelativeOID aSN1RelativeOID = (ASN1RelativeOID)pool.get(oidHandle);
        if (aSN1RelativeOID != null) {
            return aSN1RelativeOID;
        }
        if (!ASN1RelativeOID.isValidContents(byArray)) {
            throw new IllegalArgumentException("invalid relative OID contents");
        }
        return new ASN1RelativeOID(bl ? Arrays.clone(byArray) : byArray, null);
    }

    static boolean isValidContents(byte[] byArray) {
        if (Properties.isOverrideSet("org.bouncycastle.asn1.allow_wrong_oid_enc")) {
            return true;
        }
        if (byArray.length < 1) {
            return false;
        }
        boolean bl = true;
        for (int i2 = 0; i2 < byArray.length; ++i2) {
            if (bl && (byArray[i2] & 0xFF) == 128) {
                return false;
            }
            bl = (byArray[i2] & 0x80) == 0;
        }
        return bl;
    }

    static boolean isValidIdentifier(String string, int n2) {
        int n3 = 0;
        int n4 = string.length();
        while (--n4 >= n2) {
            char c2 = string.charAt(n4);
            if (c2 == '.') {
                if (0 == n3 || n3 > 1 && string.charAt(n4 + 1) == '0') {
                    return false;
                }
                n3 = 0;
                continue;
            }
            if ('0' <= c2 && c2 <= '9') {
                ++n3;
                continue;
            }
            return false;
        }
        return 0 != n3 && (n3 <= true || string.charAt(n4 + 1) != '0');
    }

    static String parseContents(byte[] byArray) {
        StringBuilder stringBuilder = new StringBuilder();
        long l2 = 0L;
        BigInteger bigInteger = null;
        boolean bl = true;
        for (int i2 = 0; i2 != byArray.length; ++i2) {
            int n2 = byArray[i2] & 0xFF;
            if (l2 <= 0xFFFFFFFFFFFF80L) {
                l2 += (long)(n2 & 0x7F);
                if ((n2 & 0x80) == 0) {
                    if (bl) {
                        bl = false;
                    } else {
                        stringBuilder.append('.');
                    }
                    stringBuilder.append(l2);
                    l2 = 0L;
                    continue;
                }
                l2 <<= 7;
                continue;
            }
            if (bigInteger == null) {
                bigInteger = BigInteger.valueOf(l2);
            }
            bigInteger = bigInteger.or(BigInteger.valueOf(n2 & 0x7F));
            if ((n2 & 0x80) == 0) {
                if (bl) {
                    bl = false;
                } else {
                    stringBuilder.append('.');
                }
                stringBuilder.append(bigInteger);
                bigInteger = null;
                l2 = 0L;
                continue;
            }
            bigInteger = bigInteger.shiftLeft(7);
        }
        return stringBuilder.toString();
    }

    static byte[] parseIdentifier(String string) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        OIDTokenizer oIDTokenizer = new OIDTokenizer(string);
        while (oIDTokenizer.hasMoreTokens()) {
            String string2 = oIDTokenizer.nextToken();
            if (string2.length() <= 18) {
                ASN1RelativeOID.writeField(byteArrayOutputStream, Long.parseLong(string2));
                continue;
            }
            ASN1RelativeOID.writeField(byteArrayOutputStream, new BigInteger(string2));
        }
        return byteArrayOutputStream.toByteArray();
    }

    static void writeField(ByteArrayOutputStream byteArrayOutputStream, long l2) {
        byte[] byArray = new byte[9];
        int n2 = 8;
        byArray[n2] = (byte)((int)l2 & 0x7F);
        while (l2 >= 128L) {
            byArray[--n2] = (byte)((int)(l2 >>= 7) | 0x80);
        }
        byteArrayOutputStream.write(byArray, n2, 9 - n2);
    }

    static void writeField(ByteArrayOutputStream byteArrayOutputStream, BigInteger bigInteger) {
        int n2 = (bigInteger.bitLength() + 6) / 7;
        if (n2 == 0) {
            byteArrayOutputStream.write(0);
        } else {
            BigInteger bigInteger2 = bigInteger;
            byte[] byArray = new byte[n2];
            for (int i2 = n2 - 1; i2 >= 0; --i2) {
                byArray[i2] = (byte)(bigInteger2.intValue() | 0x80);
                bigInteger2 = bigInteger2.shiftRight(7);
            }
            int n3 = n2 - 1;
            byArray[n3] = (byte)(byArray[n3] & 0x7F);
            byteArrayOutputStream.write(byArray, 0, byArray.length);
        }
    }
}

