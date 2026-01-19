/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.i18n.filter;

import org.bouncycastle.i18n.filter.Filter;

public class HTMLFilter
implements Filter {
    @Override
    public String doFilter(String string) {
        StringBuffer stringBuffer = new StringBuffer(string);
        block14: for (int i2 = 0; i2 < stringBuffer.length(); i2 += 4) {
            char c2 = stringBuffer.charAt(i2);
            switch (c2) {
                case '<': {
                    stringBuffer.replace(i2, i2 + 1, "&#60");
                    continue block14;
                }
                case '>': {
                    stringBuffer.replace(i2, i2 + 1, "&#62");
                    continue block14;
                }
                case '(': {
                    stringBuffer.replace(i2, i2 + 1, "&#40");
                    continue block14;
                }
                case ')': {
                    stringBuffer.replace(i2, i2 + 1, "&#41");
                    continue block14;
                }
                case '#': {
                    stringBuffer.replace(i2, i2 + 1, "&#35");
                    continue block14;
                }
                case '&': {
                    stringBuffer.replace(i2, i2 + 1, "&#38");
                    continue block14;
                }
                case '\"': {
                    stringBuffer.replace(i2, i2 + 1, "&#34");
                    continue block14;
                }
                case '\'': {
                    stringBuffer.replace(i2, i2 + 1, "&#39");
                    continue block14;
                }
                case '%': {
                    stringBuffer.replace(i2, i2 + 1, "&#37");
                    continue block14;
                }
                case ';': {
                    stringBuffer.replace(i2, i2 + 1, "&#59");
                    continue block14;
                }
                case '+': {
                    stringBuffer.replace(i2, i2 + 1, "&#43");
                    continue block14;
                }
                case '-': {
                    stringBuffer.replace(i2, i2 + 1, "&#45");
                    continue block14;
                }
                default: {
                    i2 -= 3;
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

