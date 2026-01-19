/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.asn1.x500.style;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1String;
import org.bouncycastle.asn1.ASN1UniversalString;
import org.bouncycastle.asn1.x500.AttributeTypeAndValue;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.X500NameStyle;
import org.bouncycastle.asn1.x500.style.X500NameTokenizer;
import org.bouncycastle.util.Strings;
import org.bouncycastle.util.encoders.Hex;

public class IETFUtils {
    private static String unescape(String string) {
        if (string.length() == 0) {
            return string;
        }
        if (string.indexOf(92) < 0 && string.indexOf(34) < 0) {
            return string.trim();
        }
        boolean bl = false;
        boolean bl2 = false;
        StringBuffer stringBuffer = new StringBuffer(string.length());
        int n2 = 0;
        if (string.charAt(0) == '\\' && string.charAt(1) == '#') {
            n2 = 2;
            stringBuffer.append("\\#");
        }
        boolean bl3 = false;
        int n3 = 0;
        char c2 = '\u0000';
        for (int i2 = n2; i2 != string.length(); ++i2) {
            char c3 = string.charAt(i2);
            if (c3 != ' ') {
                bl3 = true;
            }
            if (c3 == '\"') {
                if (!bl) {
                    bl2 = !bl2;
                    continue;
                }
                stringBuffer.append(c3);
                bl = false;
                continue;
            }
            if (c3 == '\\' && !bl && !bl2) {
                bl = true;
                n3 = stringBuffer.length();
                continue;
            }
            if (c3 == ' ' && !bl && !bl3) continue;
            if (bl && IETFUtils.isHexDigit(c3)) {
                if (c2 != '\u0000') {
                    stringBuffer.append((char)(IETFUtils.convertHex(c2) * 16 + IETFUtils.convertHex(c3)));
                    bl = false;
                    c2 = '\u0000';
                    continue;
                }
                c2 = c3;
                continue;
            }
            stringBuffer.append(c3);
            bl = false;
        }
        if (stringBuffer.length() > 0) {
            while (stringBuffer.charAt(stringBuffer.length() - 1) == ' ' && n3 != stringBuffer.length() - 1) {
                stringBuffer.setLength(stringBuffer.length() - 1);
            }
        }
        return stringBuffer.toString();
    }

    private static boolean isHexDigit(char c2) {
        return '0' <= c2 && c2 <= '9' || 'a' <= c2 && c2 <= 'f' || 'A' <= c2 && c2 <= 'F';
    }

    private static int convertHex(char c2) {
        if ('0' <= c2 && c2 <= '9') {
            return c2 - 48;
        }
        if ('a' <= c2 && c2 <= 'f') {
            return c2 - 97 + 10;
        }
        return c2 - 65 + 10;
    }

    public static RDN[] rDNsFromString(String string, X500NameStyle x500NameStyle) {
        X500NameTokenizer x500NameTokenizer = new X500NameTokenizer(string);
        X500NameBuilder x500NameBuilder = new X500NameBuilder(x500NameStyle);
        IETFUtils.addRDNs(x500NameStyle, x500NameBuilder, x500NameTokenizer);
        return x500NameBuilder.build().getRDNs();
    }

    private static void addRDNs(X500NameStyle x500NameStyle, X500NameBuilder x500NameBuilder, X500NameTokenizer x500NameTokenizer) {
        String string;
        while ((string = x500NameTokenizer.nextToken()) != null) {
            if (string.indexOf(43) >= 0) {
                IETFUtils.addMultiValuedRDN(x500NameStyle, x500NameBuilder, new X500NameTokenizer(string, '+'));
                continue;
            }
            IETFUtils.addRDN(x500NameStyle, x500NameBuilder, string);
        }
    }

    private static void addMultiValuedRDN(X500NameStyle x500NameStyle, X500NameBuilder x500NameBuilder, X500NameTokenizer x500NameTokenizer) {
        String string = x500NameTokenizer.nextToken();
        if (string == null) {
            throw new IllegalArgumentException("badly formatted directory string");
        }
        if (!x500NameTokenizer.hasMoreTokens()) {
            IETFUtils.addRDN(x500NameStyle, x500NameBuilder, string);
            return;
        }
        Vector vector = new Vector();
        Vector vector2 = new Vector();
        do {
            IETFUtils.collectAttributeTypeAndValue(x500NameStyle, vector, vector2, string);
        } while ((string = x500NameTokenizer.nextToken()) != null);
        x500NameBuilder.addMultiValuedRDN(IETFUtils.toOIDArray(vector), IETFUtils.toValueArray(vector2));
    }

    private static void addRDN(X500NameStyle x500NameStyle, X500NameBuilder x500NameBuilder, String string) {
        X500NameTokenizer x500NameTokenizer = new X500NameTokenizer(string, '=');
        String string2 = IETFUtils.nextToken(x500NameTokenizer, true);
        String string3 = IETFUtils.nextToken(x500NameTokenizer, false);
        ASN1ObjectIdentifier aSN1ObjectIdentifier = x500NameStyle.attrNameToOID(string2.trim());
        String string4 = IETFUtils.unescape(string3);
        x500NameBuilder.addRDN(aSN1ObjectIdentifier, string4);
    }

    private static void collectAttributeTypeAndValue(X500NameStyle x500NameStyle, Vector vector, Vector vector2, String string) {
        X500NameTokenizer x500NameTokenizer = new X500NameTokenizer(string, '=');
        String string2 = IETFUtils.nextToken(x500NameTokenizer, true);
        String string3 = IETFUtils.nextToken(x500NameTokenizer, false);
        ASN1ObjectIdentifier aSN1ObjectIdentifier = x500NameStyle.attrNameToOID(string2.trim());
        String string4 = IETFUtils.unescape(string3);
        vector.addElement(aSN1ObjectIdentifier);
        vector2.addElement(string4);
    }

    private static String nextToken(X500NameTokenizer x500NameTokenizer, boolean bl) {
        String string = x500NameTokenizer.nextToken();
        if (string == null || x500NameTokenizer.hasMoreTokens() != bl) {
            throw new IllegalArgumentException("badly formatted directory string");
        }
        return string;
    }

    private static String[] toValueArray(Vector vector) {
        String[] stringArray = new String[vector.size()];
        for (int i2 = 0; i2 != stringArray.length; ++i2) {
            stringArray[i2] = (String)vector.elementAt(i2);
        }
        return stringArray;
    }

    private static ASN1ObjectIdentifier[] toOIDArray(Vector vector) {
        ASN1ObjectIdentifier[] aSN1ObjectIdentifierArray = new ASN1ObjectIdentifier[vector.size()];
        for (int i2 = 0; i2 != aSN1ObjectIdentifierArray.length; ++i2) {
            aSN1ObjectIdentifierArray[i2] = (ASN1ObjectIdentifier)vector.elementAt(i2);
        }
        return aSN1ObjectIdentifierArray;
    }

    public static String[] findAttrNamesForOID(ASN1ObjectIdentifier aSN1ObjectIdentifier, Hashtable hashtable) {
        int n2 = 0;
        String[] stringArray = hashtable.elements();
        while (stringArray.hasMoreElements()) {
            if (!aSN1ObjectIdentifier.equals(stringArray.nextElement())) continue;
            ++n2;
        }
        stringArray = new String[n2];
        n2 = 0;
        Enumeration enumeration = hashtable.keys();
        while (enumeration.hasMoreElements()) {
            String string = (String)enumeration.nextElement();
            if (!aSN1ObjectIdentifier.equals(hashtable.get(string))) continue;
            stringArray[n2++] = string;
        }
        return stringArray;
    }

    public static ASN1ObjectIdentifier decodeAttrName(String string, Hashtable hashtable) {
        if (string.regionMatches(true, 0, "OID.", 0, 4)) {
            return new ASN1ObjectIdentifier(string.substring(4));
        }
        ASN1ObjectIdentifier aSN1ObjectIdentifier = ASN1ObjectIdentifier.tryFromID(string);
        if (aSN1ObjectIdentifier != null) {
            return aSN1ObjectIdentifier;
        }
        aSN1ObjectIdentifier = (ASN1ObjectIdentifier)hashtable.get(Strings.toLowerCase(string));
        if (aSN1ObjectIdentifier != null) {
            return aSN1ObjectIdentifier;
        }
        throw new IllegalArgumentException("Unknown object id - " + string + " - passed to distinguished name");
    }

    public static ASN1Encodable valueFromHexString(String string, int n2) throws IOException {
        byte[] byArray = new byte[(string.length() - n2) / 2];
        for (int i2 = 0; i2 != byArray.length; ++i2) {
            char c2 = string.charAt(i2 * 2 + n2);
            char c3 = string.charAt(i2 * 2 + n2 + 1);
            byArray[i2] = (byte)(IETFUtils.convertHex(c2) << 4 | IETFUtils.convertHex(c3));
        }
        return ASN1Primitive.fromByteArray(byArray);
    }

    public static void appendRDN(StringBuffer stringBuffer, RDN rDN, Hashtable hashtable) {
        if (rDN.isMultiValued()) {
            AttributeTypeAndValue[] attributeTypeAndValueArray = rDN.getTypesAndValues();
            boolean bl = true;
            for (int i2 = 0; i2 != attributeTypeAndValueArray.length; ++i2) {
                if (bl) {
                    bl = false;
                } else {
                    stringBuffer.append('+');
                }
                IETFUtils.appendTypeAndValue(stringBuffer, attributeTypeAndValueArray[i2], hashtable);
            }
        } else if (rDN.getFirst() != null) {
            IETFUtils.appendTypeAndValue(stringBuffer, rDN.getFirst(), hashtable);
        }
    }

    public static void appendTypeAndValue(StringBuffer stringBuffer, AttributeTypeAndValue attributeTypeAndValue, Hashtable hashtable) {
        String string = (String)hashtable.get(attributeTypeAndValue.getType());
        if (string != null) {
            stringBuffer.append(string);
        } else {
            stringBuffer.append(attributeTypeAndValue.getType().getId());
        }
        stringBuffer.append('=');
        stringBuffer.append(IETFUtils.valueToString(attributeTypeAndValue.getValue()));
    }

    public static String valueToString(ASN1Encodable aSN1Encodable) {
        int n2;
        StringBuffer stringBuffer = new StringBuffer();
        if (aSN1Encodable instanceof ASN1String && !(aSN1Encodable instanceof ASN1UniversalString)) {
            String string = ((ASN1String)((Object)aSN1Encodable)).getString();
            if (string.length() > 0 && string.charAt(0) == '#') {
                stringBuffer.append('\\');
            }
            stringBuffer.append(string);
        } else {
            try {
                stringBuffer.append('#');
                stringBuffer.append(Hex.toHexString(aSN1Encodable.toASN1Primitive().getEncoded("DER")));
            }
            catch (IOException iOException) {
                throw new IllegalArgumentException("Other value has no encoded form");
            }
        }
        int n3 = stringBuffer.length();
        int n4 = 0;
        if (stringBuffer.length() >= 2 && stringBuffer.charAt(0) == '\\' && stringBuffer.charAt(1) == '#') {
            n4 += 2;
        }
        block5: while (n4 != n3) {
            switch (stringBuffer.charAt(n4)) {
                case '\"': 
                case '+': 
                case ',': 
                case ';': 
                case '<': 
                case '=': 
                case '>': 
                case '\\': {
                    stringBuffer.insert(n4, "\\");
                    n4 += 2;
                    ++n3;
                    continue block5;
                }
            }
            ++n4;
        }
        if (stringBuffer.length() > 0) {
            for (n2 = 0; stringBuffer.length() > n2 && stringBuffer.charAt(n2) == ' '; n2 += 2) {
                stringBuffer.insert(n2, "\\");
            }
        }
        for (int i2 = stringBuffer.length() - 1; i2 >= n2 && stringBuffer.charAt(i2) == ' '; --i2) {
            stringBuffer.insert(i2, '\\');
        }
        return stringBuffer.toString();
    }

    public static String canonicalize(String string) {
        int n2;
        int n3;
        int n4;
        ASN1Primitive aSN1Primitive;
        if (string.length() > 0 && string.charAt(0) == '#' && (aSN1Primitive = IETFUtils.decodeObject(string)) instanceof ASN1String) {
            string = ((ASN1String)((Object)aSN1Primitive)).getString();
        }
        if ((n4 = (string = Strings.toLowerCase(string)).length()) < 2) {
            return string;
        }
        int n5 = n4 - 1;
        for (n3 = 0; n3 < n5 && string.charAt(n3) == '\\' && string.charAt(n3 + 1) == ' '; n3 += 2) {
        }
        int n6 = n3 + 1;
        for (n2 = n5; n2 > n6 && string.charAt(n2 - 1) == '\\' && string.charAt(n2) == ' '; n2 -= 2) {
        }
        if (n3 > 0 || n2 < n5) {
            string = string.substring(n3, n2 + 1);
        }
        return IETFUtils.stripInternalSpaces(string);
    }

    public static String canonicalString(ASN1Encodable aSN1Encodable) {
        return IETFUtils.canonicalize(IETFUtils.valueToString(aSN1Encodable));
    }

    private static ASN1Primitive decodeObject(String string) {
        try {
            return ASN1Primitive.fromByteArray(Hex.decodeStrict(string, 1, string.length() - 1));
        }
        catch (IOException iOException) {
            throw new IllegalStateException("unknown encoding in name: " + iOException);
        }
    }

    public static String stripInternalSpaces(String string) {
        if (string.indexOf("  ") < 0) {
            return string;
        }
        StringBuffer stringBuffer = new StringBuffer();
        char c2 = string.charAt(0);
        stringBuffer.append(c2);
        for (int i2 = 1; i2 < string.length(); ++i2) {
            char c3 = string.charAt(i2);
            if (c2 == ' ' && c3 == ' ') continue;
            stringBuffer.append(c3);
            c2 = c3;
        }
        return stringBuffer.toString();
    }

    public static boolean rDNAreEqual(RDN rDN, RDN rDN2) {
        AttributeTypeAndValue[] attributeTypeAndValueArray;
        if (rDN.size() != rDN2.size()) {
            return false;
        }
        AttributeTypeAndValue[] attributeTypeAndValueArray2 = rDN.getTypesAndValues();
        if (attributeTypeAndValueArray2.length != (attributeTypeAndValueArray = rDN2.getTypesAndValues()).length) {
            return false;
        }
        for (int i2 = 0; i2 != attributeTypeAndValueArray2.length; ++i2) {
            if (IETFUtils.atvAreEqual(attributeTypeAndValueArray2[i2], attributeTypeAndValueArray[i2])) continue;
            return false;
        }
        return true;
    }

    private static boolean atvAreEqual(AttributeTypeAndValue attributeTypeAndValue, AttributeTypeAndValue attributeTypeAndValue2) {
        String string;
        ASN1ObjectIdentifier aSN1ObjectIdentifier;
        if (attributeTypeAndValue == attributeTypeAndValue2) {
            return true;
        }
        if (null == attributeTypeAndValue || null == attributeTypeAndValue2) {
            return false;
        }
        ASN1ObjectIdentifier aSN1ObjectIdentifier2 = attributeTypeAndValue.getType();
        if (!aSN1ObjectIdentifier2.equals(aSN1ObjectIdentifier = attributeTypeAndValue2.getType())) {
            return false;
        }
        String string2 = IETFUtils.canonicalString(attributeTypeAndValue.getValue());
        return string2.equals(string = IETFUtils.canonicalString(attributeTypeAndValue2.getValue()));
    }
}

