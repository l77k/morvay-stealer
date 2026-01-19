/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.jcajce.provider.asymmetric.dstu;

import java.io.IOException;
import java.security.SignatureException;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.jcajce.provider.asymmetric.dstu.SignatureSpi;

public class SignatureSpiLe
extends SignatureSpi {
    void reverseBytes(byte[] byArray) {
        for (int i2 = 0; i2 < byArray.length / 2; ++i2) {
            byte by = byArray[i2];
            byArray[i2] = byArray[byArray.length - 1 - i2];
            byArray[byArray.length - 1 - i2] = by;
        }
    }

    @Override
    protected byte[] engineSign() throws SignatureException {
        byte[] byArray = ASN1OctetString.getInstance(super.engineSign()).getOctets();
        this.reverseBytes(byArray);
        try {
            return new DEROctetString(byArray).getEncoded();
        }
        catch (Exception exception) {
            throw new SignatureException(exception.toString());
        }
    }

    @Override
    protected boolean engineVerify(byte[] byArray) throws SignatureException {
        byte[] byArray2 = null;
        try {
            byArray2 = ((ASN1OctetString)ASN1OctetString.fromByteArray(byArray)).getOctets();
        }
        catch (IOException iOException) {
            throw new SignatureException("error decoding signature bytes.");
        }
        this.reverseBytes(byArray2);
        try {
            return super.engineVerify(new DEROctetString(byArray2).getEncoded());
        }
        catch (SignatureException signatureException) {
            throw signatureException;
        }
        catch (Exception exception) {
            throw new SignatureException(exception.toString());
        }
    }
}

