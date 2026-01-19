/*
 * Decompiled with CFR 0.152.
 */
package a.b.c.g;

final class a
extends Enum<a> {
    public static final /* enum */ a COOKIES;
    public static final /* enum */ a PASSWORDS;
    public static final /* enum */ a AUTOFILL;
    public static final /* enum */ a HISTORY;
    private static final a[] a;

    public static a[] values() {
        return (a[])a.clone();
    }

    public static a valueOf(String string) {
        return Enum.valueOf(a.class, string);
    }

    /*
     * Unable to fully structure code
     */
    static {
        block20: {
            block19: {
                var0 = new String[4];
                var4_1 = 0;
                var3_2 = "GgJ:z*@\u0007LaV%|=J";
                var5_3 = "GgJ:z*@\u0007LaV%|=J".length();
                var2_4 = 7;
                var1_5 = -1;
lbl7:
                // 2 sources

                while (true) {
                    v0 = 97;
                    v1 = ++var1_5;
                    v2 = var3_2.substring(v1, v1 + var2_4);
                    v3 = -1;
                    break block19;
                    break;
                }
lbl13:
                // 1 sources

                while (true) {
                    var0[var4_1++] = v4.intern();
                    if ((var1_5 += var2_4) < var5_3) {
                        var2_4 = var3_2.charAt(var1_5);
                        ** continue;
                    }
                    var3_2 = "\u0015%\u0000w(}\u0001\u00017\b\u00041\u0007k9{\u001f\t";
                    var5_3 = "\u0015%\u0000w(}\u0001\u00017\b\u00041\u0007k9{\u001f\t".length();
                    var2_4 = 9;
                    var1_5 = -1;
lbl22:
                    // 2 sources

                    while (true) {
                        v0 = 43;
                        v5 = ++var1_5;
                        v2 = var3_2.substring(v5, v5 + var2_4);
                        v3 = 0;
                        break block19;
                        break;
                    }
                    break;
                }
lbl28:
                // 1 sources

                while (true) {
                    var0[var4_1++] = v4.intern();
                    if ((var1_5 += var2_4) < var5_3) {
                        var2_4 = var3_2.charAt(var1_5);
                        ** continue;
                    }
                    break block20;
                    break;
                }
            }
            v6 = v2.toCharArray();
            v7 = v6.length;
            var6_6 = 0;
            v8 = v0;
            v9 = v6;
            v10 = v7;
            if (v7 > 1) ** GOTO lbl85
            do {
                v11 = v8;
                v9 = v9;
                v12 = v9;
                v13 = v8;
                v14 = var6_6;
                while (true) {
                    switch (var6_6 % 7) {
                        case 0: {
                            v15 = 110;
                            break;
                        }
                        case 1: {
                            v15 = 79;
                            break;
                        }
                        case 2: {
                            v15 = 120;
                            break;
                        }
                        case 3: {
                            v15 = 15;
                            break;
                        }
                        case 4: {
                            v15 = 84;
                            break;
                        }
                        case 5: {
                            v15 = 25;
                            break;
                        }
                        default: {
                            v15 = 120;
                        }
                    }
                    v12[v14] = (char)(v12[v14] ^ (v13 ^ v15));
                    ++var6_6;
                    v8 = v11;
                    if (v11 != 0) break;
                    v11 = v8;
                    v9 = v9;
                    v14 = v8;
                    v12 = v9;
                    v13 = v8;
                }
lbl85:
                // 2 sources

                v16 = v9;
                v10 = v10;
            } while (v10 > var6_6);
            v4 = new String(v16);
            switch (v3) {
                default: {
                    ** continue;
                }
                ** case 0:
lbl95:
                // 1 sources

                ** continue;
            }
        }
        a.b.c.g.a.COOKIES = new a();
        a.b.c.g.a.PASSWORDS = new a();
        a.b.c.g.a.AUTOFILL = new a();
        a.b.c.g.a.HISTORY = new a();
        a.b.c.g.a.a = new a[]{a.b.c.g.a.COOKIES, a.b.c.g.a.PASSWORDS, a.b.c.g.a.AUTOFILL, a.b.c.g.a.HISTORY};
    }
}

