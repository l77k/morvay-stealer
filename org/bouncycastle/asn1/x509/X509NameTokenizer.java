/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.asn1.x509;

public class X509NameTokenizer {
    private String value;
    private int index;
    private char separator;
    private StringBuffer buf = new StringBuffer();

    public X509NameTokenizer(String string) {
        this(string, ',');
    }

    public X509NameTokenizer(String string, char c2) {
        this.value = string;
        this.index = -1;
        this.separator = c2;
    }

    public boolean hasMoreTokens() {
        return this.index != this.value.length();
    }

    public String nextToken() {
        int n2;
        if (this.index == this.value.length()) {
            return null;
        }
        boolean bl = false;
        boolean bl2 = false;
        this.buf.setLength(0);
        for (n2 = this.index + 1; n2 != this.value.length(); ++n2) {
            char c2 = this.value.charAt(n2);
            if (c2 == '\"') {
                if (!bl2) {
                    bl = !bl;
                }
                this.buf.append(c2);
                bl2 = false;
                continue;
            }
            if (bl2 || bl) {
                this.buf.append(c2);
                bl2 = false;
                continue;
            }
            if (c2 == '\\') {
                this.buf.append(c2);
                bl2 = true;
                continue;
            }
            if (c2 == this.separator) break;
            this.buf.append(c2);
        }
        this.index = n2;
        return this.buf.toString();
    }
}

