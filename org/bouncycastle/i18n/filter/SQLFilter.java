/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.i18n.filter;

import org.bouncycastle.i18n.filter.Filter;

public class SQLFilter
implements Filter {
    @Override
    public String doFilter(String string) {
        StringBuffer stringBuffer = new StringBuffer(string);
        block11: for (int i2 = 0; i2 < stringBuffer.length(); ++i2) {
            char c2 = stringBuffer.charAt(i2);
            switch (c2) {
                case '\'': {
                    stringBuffer.replace(i2, i2 + 1, "\\'");
                    ++i2;
                    continue block11;
                }
                case '\"': {
                    stringBuffer.replace(i2, i2 + 1, "\\\"");
                    ++i2;
                    continue block11;
                }
                case '=': {
                    stringBuffer.replace(i2, i2 + 1, "\\=");
                    ++i2;
                    continue block11;
                }
                case '-': {
                    stringBuffer.replace(i2, i2 + 1, "\\-");
                    ++i2;
                    continue block11;
                }
                case '/': {
                    stringBuffer.replace(i2, i2 + 1, "\\/");
                    ++i2;
                    continue block11;
                }
                case '\\': {
                    stringBuffer.replace(i2, i2 + 1, "\\\\");
                    ++i2;
                    continue block11;
                }
                case ';': {
                    stringBuffer.replace(i2, i2 + 1, "\\;");
                    ++i2;
                    continue block11;
                }
                case '\r': {
                    stringBuffer.replace(i2, i2 + 1, "\\r");
                    ++i2;
                    continue block11;
                }
                case '\n': {
                    stringBuffer.replace(i2, i2 + 1, "\\n");
                    ++i2;
                    continue block11;
                }
            }
        }
        return stringBuffer.toString();
    }

    @Override
    public String doFilterUrl(String string) {
        return this.doFilter(string);
    }
}

