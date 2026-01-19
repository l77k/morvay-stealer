/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.crypto.parsers;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import org.bouncycastle.crypto.KeyParser;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.util.io.Streams;

public class ECIESPublicKeyParser
implements KeyParser {
    private ECDomainParameters ecParams;

    public ECIESPublicKeyParser(ECDomainParameters eCDomainParameters) {
        this.ecParams = eCDomainParameters;
    }

    @Override
    public AsymmetricKeyParameter readKey(InputStream inputStream2) throws IOException {
        boolean bl;
        int n2 = inputStream2.read();
        if (n2 < 0) {
            throw new EOFException();
        }
        switch (n2) {
            case 0: {
                throw new IOException("Sender's public key invalid.");
            }
            case 2: 
            case 3: {
                bl = true;
                break;
            }
            case 4: 
            case 6: 
            case 7: {
                bl = false;
                break;
            }
            default: {
                throw new IOException("Sender's public key has invalid point encoding 0x" + Integer.toString(n2, 16));
            }
        }
        ECCurve eCCurve = this.ecParams.getCurve();
        int n3 = eCCurve.getAffinePointEncodingLength(bl);
        byte[] byArray = new byte[n3];
        byArray[0] = (byte)n2;
        int n4 = n3 - 1;
        if (Streams.readFully(inputStream2, byArray, 1, n4) != n4) {
            throw new EOFException();
        }
        return new ECPublicKeyParameters(eCCurve.decodePoint(byArray), this.ecParams);
    }
}

