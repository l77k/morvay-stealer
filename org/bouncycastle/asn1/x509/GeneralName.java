/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.asn1.x509;

import java.io.IOException;
import java.util.StringTokenizer;
import org.bouncycastle.asn1.ASN1Choice;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1IA5String;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.util.IPAddress;

public class GeneralName
extends ASN1Object
implements ASN1Choice {
    public static final int otherName = 0;
    public static final int rfc822Name = 1;
    public static final int dNSName = 2;
    public static final int x400Address = 3;
    public static final int directoryName = 4;
    public static final int ediPartyName = 5;
    public static final int uniformResourceIdentifier = 6;
    public static final int iPAddress = 7;
    public static final int registeredID = 8;
    private ASN1Encodable obj;
    private int tag;

    public GeneralName(X509Name x509Name) {
        this.obj = X500Name.getInstance(x509Name);
        this.tag = 4;
    }

    public GeneralName(X500Name x500Name) {
        this.obj = x500Name;
        this.tag = 4;
    }

    public GeneralName(int n2, ASN1Encodable aSN1Encodable) {
        this.obj = aSN1Encodable;
        this.tag = n2;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public GeneralName(int n2, String string) {
        this.tag = n2;
        if (n2 == 1 || n2 == 2 || n2 == 6) {
            this.obj = new DERIA5String(string);
            return;
        } else if (n2 == 8) {
            this.obj = new ASN1ObjectIdentifier(string);
            return;
        } else if (n2 == 4) {
            this.obj = new X500Name(string);
            return;
        } else {
            if (n2 != 7) throw new IllegalArgumentException("can't process String for tag: " + n2);
            byte[] byArray = this.toGeneralNameEncoding(string);
            if (byArray == null) throw new IllegalArgumentException("IP Address is invalid");
            this.obj = new DEROctetString(byArray);
        }
    }

    public static GeneralName getInstance(Object object) {
        if (object == null || object instanceof GeneralName) {
            return (GeneralName)object;
        }
        if (object instanceof ASN1TaggedObject) {
            ASN1TaggedObject aSN1TaggedObject = (ASN1TaggedObject)object;
            int n2 = aSN1TaggedObject.getTagNo();
            switch (n2) {
                case 0: 
                case 3: 
                case 5: {
                    return new GeneralName(n2, ASN1Sequence.getInstance(aSN1TaggedObject, false));
                }
                case 1: 
                case 2: 
                case 6: {
                    return new GeneralName(n2, ASN1IA5String.getInstance(aSN1TaggedObject, false));
                }
                case 4: {
                    return new GeneralName(n2, X500Name.getInstance(aSN1TaggedObject, true));
                }
                case 7: {
                    return new GeneralName(n2, ASN1OctetString.getInstance(aSN1TaggedObject, false));
                }
                case 8: {
                    return new GeneralName(n2, ASN1ObjectIdentifier.getInstance(aSN1TaggedObject, false));
                }
            }
            throw new IllegalArgumentException("unknown tag: " + n2);
        }
        if (object instanceof byte[]) {
            try {
                return GeneralName.getInstance(ASN1Primitive.fromByteArray((byte[])object));
            }
            catch (IOException iOException) {
                throw new IllegalArgumentException("unable to parse encoded general name");
            }
        }
        throw new IllegalArgumentException("unknown object in getInstance: " + object.getClass().getName());
    }

    public static GeneralName getInstance(ASN1TaggedObject aSN1TaggedObject, boolean bl) {
        if (!bl) {
            throw new IllegalArgumentException("choice item must be explicitly tagged");
        }
        return GeneralName.getInstance(ASN1TaggedObject.getInstance(aSN1TaggedObject, true));
    }

    public int getTagNo() {
        return this.tag;
    }

    public ASN1Encodable getName() {
        return this.obj;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(this.tag);
        stringBuffer.append(": ");
        switch (this.tag) {
            case 1: 
            case 2: 
            case 6: {
                stringBuffer.append(ASN1IA5String.getInstance(this.obj).getString());
                break;
            }
            case 4: {
                stringBuffer.append(X500Name.getInstance(this.obj).toString());
                break;
            }
            default: {
                stringBuffer.append(this.obj.toString());
            }
        }
        return stringBuffer.toString();
    }

    private byte[] toGeneralNameEncoding(String string) {
        if (IPAddress.isValidIPv6WithNetmask(string) || IPAddress.isValidIPv6(string)) {
            int n2 = string.indexOf(47);
            if (n2 < 0) {
                byte[] byArray = new byte[16];
                int[] nArray = this.parseIPv6(string);
                this.copyInts(nArray, byArray, 0);
                return byArray;
            }
            byte[] byArray = new byte[32];
            int[] nArray = this.parseIPv6(string.substring(0, n2));
            this.copyInts(nArray, byArray, 0);
            String string2 = string.substring(n2 + 1);
            nArray = string2.indexOf(58) > 0 ? this.parseIPv6(string2) : this.parseMask(string2);
            this.copyInts(nArray, byArray, 16);
            return byArray;
        }
        if (IPAddress.isValidIPv4WithNetmask(string) || IPAddress.isValidIPv4(string)) {
            int n3 = string.indexOf(47);
            if (n3 < 0) {
                byte[] byArray = new byte[4];
                this.parseIPv4(string, byArray, 0);
                return byArray;
            }
            byte[] byArray = new byte[8];
            this.parseIPv4(string.substring(0, n3), byArray, 0);
            String string3 = string.substring(n3 + 1);
            if (string3.indexOf(46) > 0) {
                this.parseIPv4(string3, byArray, 4);
            } else {
                this.parseIPv4Mask(string3, byArray, 4);
            }
            return byArray;
        }
        return null;
    }

    private void parseIPv4Mask(String string, byte[] byArray, int n2) {
        int n3 = Integer.parseInt(string);
        for (int i2 = 0; i2 != n3; ++i2) {
            int n4 = i2 / 8 + n2;
            byArray[n4] = (byte)(byArray[n4] | 1 << 7 - i2 % 8);
        }
    }

    private void parseIPv4(String string, byte[] byArray, int n2) {
        StringTokenizer stringTokenizer = new StringTokenizer(string, "./");
        int n3 = 0;
        while (stringTokenizer.hasMoreTokens()) {
            byArray[n2 + n3++] = (byte)Integer.parseInt(stringTokenizer.nextToken());
        }
    }

    private int[] parseMask(String string) {
        int[] nArray = new int[8];
        int n2 = Integer.parseInt(string);
        for (int i2 = 0; i2 != n2; ++i2) {
            int n3 = i2 / 16;
            nArray[n3] = nArray[n3] | 1 << 15 - i2 % 16;
        }
        return nArray;
    }

    private void copyInts(int[] nArray, byte[] byArray, int n2) {
        for (int i2 = 0; i2 != nArray.length; ++i2) {
            byArray[i2 * 2 + n2] = (byte)(nArray[i2] >> 8);
            byArray[i2 * 2 + 1 + n2] = (byte)nArray[i2];
        }
    }

    private int[] parseIPv6(String string) {
        StringTokenizer stringTokenizer = new StringTokenizer(string, ":", true);
        int n2 = 0;
        int[] nArray = new int[8];
        if (string.charAt(0) == ':' && string.charAt(1) == ':') {
            stringTokenizer.nextToken();
        }
        int n3 = -1;
        while (stringTokenizer.hasMoreTokens()) {
            String string2 = stringTokenizer.nextToken();
            if (string2.equals(":")) {
                n3 = n2;
                nArray[n2++] = 0;
                continue;
            }
            if (string2.indexOf(46) < 0) {
                nArray[n2++] = Integer.parseInt(string2, 16);
                if (!stringTokenizer.hasMoreTokens()) continue;
                stringTokenizer.nextToken();
                continue;
            }
            StringTokenizer stringTokenizer2 = new StringTokenizer(string2, ".");
            nArray[n2++] = Integer.parseInt(stringTokenizer2.nextToken()) << 8 | Integer.parseInt(stringTokenizer2.nextToken());
            nArray[n2++] = Integer.parseInt(stringTokenizer2.nextToken()) << 8 | Integer.parseInt(stringTokenizer2.nextToken());
        }
        if (n2 != nArray.length) {
            System.arraycopy(nArray, n3, nArray, nArray.length - (n2 - n3), n2 - n3);
            for (int i2 = n3; i2 != nArray.length - (n2 - n3); ++i2) {
                nArray[i2] = 0;
            }
        }
        return nArray;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        boolean bl = this.tag == 4;
        return new DERTaggedObject(bl, this.tag, this.obj);
    }
}

