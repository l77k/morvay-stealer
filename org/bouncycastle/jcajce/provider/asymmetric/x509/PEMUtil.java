/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.jcajce.provider.asymmetric.x509;

import java.io.IOException;
import java.io.InputStream;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.util.encoders.Base64;

class PEMUtil {
    private final Boundaries[] _supportedBoundaries;

    PEMUtil(String string) {
        this._supportedBoundaries = new Boundaries[]{new Boundaries(string), new Boundaries("X509 " + string), new Boundaries("PKCS7")};
    }

    private String readLine(InputStream inputStream2) throws IOException {
        int n2;
        StringBuffer stringBuffer = new StringBuffer();
        while (true) {
            if ((n2 = inputStream2.read()) != 13 && n2 != 10 && n2 >= 0) {
                stringBuffer.append((char)n2);
                continue;
            }
            if (n2 < 0 || stringBuffer.length() != 0) break;
        }
        if (n2 < 0) {
            if (stringBuffer.length() == 0) {
                return null;
            }
            return stringBuffer.toString();
        }
        if (n2 == 13) {
            inputStream2.mark(1);
            n2 = inputStream2.read();
            if (n2 == 10) {
                inputStream2.mark(1);
            }
            if (n2 > 0) {
                inputStream2.reset();
            }
        }
        return stringBuffer.toString();
    }

    private Boundaries getBoundaries(String string) {
        for (int i2 = 0; i2 != this._supportedBoundaries.length; ++i2) {
            Boundaries boundaries = this._supportedBoundaries[i2];
            if (!boundaries.isTheExpectedHeader(string) && !boundaries.isTheExpectedFooter(string)) continue;
            return boundaries;
        }
        return null;
    }

    ASN1Sequence readPEMObject(InputStream inputStream2, boolean bl) throws IOException {
        String string;
        StringBuffer stringBuffer = new StringBuffer();
        Boundaries boundaries = null;
        while (boundaries == null && (string = this.readLine(inputStream2)) != null) {
            boundaries = this.getBoundaries(string);
            if (boundaries == null || boundaries.isTheExpectedHeader(string)) continue;
            throw new IOException("malformed PEM data: found footer where header was expected");
        }
        if (boundaries == null) {
            if (!bl) {
                return null;
            }
            throw new IOException("malformed PEM data: no header found");
        }
        Boundaries boundaries2 = null;
        while (boundaries2 == null && (string = this.readLine(inputStream2)) != null) {
            boundaries2 = this.getBoundaries(string);
            if (boundaries2 != null) {
                if (boundaries.isTheExpectedFooter(string)) continue;
                throw new IOException("malformed PEM data: header/footer mismatch");
            }
            stringBuffer.append(string);
        }
        if (boundaries2 == null) {
            throw new IOException("malformed PEM data: no footer found");
        }
        if (stringBuffer.length() != 0) {
            try {
                return ASN1Sequence.getInstance(Base64.decode(stringBuffer.toString()));
            }
            catch (Exception exception) {
                throw new IOException("malformed PEM data encountered");
            }
        }
        return null;
    }

    private static class Boundaries {
        private final String _header;
        private final String _footer;

        private Boundaries(String string) {
            this._header = "-----BEGIN " + string + "-----";
            this._footer = "-----END " + string + "-----";
        }

        public boolean isTheExpectedHeader(String string) {
            return string.startsWith(this._header);
        }

        public boolean isTheExpectedFooter(String string) {
            return string.startsWith(this._footer);
        }
    }
}

