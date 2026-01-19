/*
 * Decompiled with CFR 0.152.
 */
package a.b.c.g;

import a.b.c.g.g;
import a.b.c.g.v;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import java.util.concurrent.CountDownLatch;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

class an
extends WebSocketListener {
    final JsonArray[] a;
    final String[] b;
    final CountDownLatch c;
    final v d;
    private static final String[] e;
    private static final String[] f;
    private static final long g;

    an(v v2, JsonArray[] jsonArrayArray, String[] stringArray, CountDownLatch countDownLatch) {
        this.d = v2;
        this.a = jsonArrayArray;
        this.b = stringArray;
        this.c = countDownLatch;
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        webSocket.send(an.a(24190, 26494));
        webSocket.send(an.a(24178, -17916));
    }

    /*
     * Unable to fully structure code
     */
    @Override
    public void onMessage(WebSocket var1_1, String var2_2) {
        block25: {
            var3_3 = a.b.c.g.g.i();
            try {
                block28: {
                    block27: {
                        block33: {
                            block32: {
                                block31: {
                                    block26: {
                                        block24: {
                                            var4_4 = JsonParser.parseString(var2_2).getAsJsonObject();
                                            v0 = var4_4.has(an.a(24176, 17652));
                                            if (!var3_3) break block24;
                                            try {
                                                block29: {
                                                    if (v0 == 0) break block25;
                                                    break block29;
                                                    catch (Exception v1) {
                                                        throw an.a(v1);
                                                    }
                                                }
                                                v0 = var4_4.get(an.a(24185, 12175)).getAsInt();
                                            }
                                            catch (Exception v2) {
                                                throw an.a(v2);
                                            }
                                        }
                                        if (!var3_3) break block26;
                                        try {
                                            block30: {
                                                if (v0 != 1) break block25;
                                                break block30;
                                                catch (Exception v3) {
                                                    throw an.a(v3);
                                                }
                                            }
                                            v0 = (int)var4_4.has(an.a(24191, 10168));
                                        }
                                        catch (Exception v4) {
                                            throw an.a(v4);
                                        }
                                    }
                                    if (!var3_3) break block27;
                                    if (v0 == 0) ** GOTO lbl58
                                    break block31;
                                    catch (Exception v5) {
                                        throw an.a(v5);
                                    }
                                }
                                v0 = (int)var4_4.getAsJsonObject(an.a(24181, 30759)).has(an.a(24166, -26935));
                                if (!var3_3) break block27;
                                break block32;
                                catch (Exception v6) {
                                    throw an.a(v6);
                                }
                            }
                            if (v0 == 0) ** GOTO lbl58
                            break block33;
                            catch (Exception v7) {
                                throw an.a(v7);
                            }
                        }
                        try {
                            block34: {
                                this.a[0] = var4_4.getAsJsonObject(an.a(24181, 30759)).getAsJsonArray(an.a(24188, -27586));
                                if (var3_3) break block28;
                                break block34;
                                catch (Exception v8) {
                                    throw an.a(v8);
                                }
                            }
                            v0 = (int)var4_4.has(an.a(24189, 19526));
                        }
                        catch (Exception v9) {
                            throw an.a(v9);
                        }
                    }
                    if (v0 == 0) ** GOTO lbl72
                    try {
                        block35: {
                            this.b[0] = an.a(24182, 16132) + var4_4.getAsJsonObject(an.a(24179, 7359)).get(an.a(24184, 15133)).getAsString();
                            if (var3_3) break block28;
                            break block35;
                            catch (Exception v10) {
                                throw an.a(v10);
                            }
                        }
                        this.b[0] = an.a(24186, -8531);
                    }
                    catch (Exception v11) {
                        throw an.a(v11);
                    }
                }
                this.c.countDown();
            }
            catch (Exception var4_5) {
                this.b[0] = an.a(24183, 5912) + var4_5.getMessage();
                this.c.countDown();
            }
        }
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable throwable, Response response) {
        String string;
        StringBuilder stringBuilder;
        block4: {
            block5: {
                boolean bl = a.b.c.g.g.i();
                try {
                    try {
                        String[] stringArray = this.b;
                        int n2 = 0;
                        stringBuilder = new StringBuilder().append(an.a(24180, -8607));
                        string = throwable.getMessage();
                        if (!bl) break block4;
                        stringBuilder = stringBuilder.append(string);
                        if (response == null) break block5;
                    }
                    catch (RuntimeException runtimeException) {
                        throw an.a(runtimeException);
                    }
                    string = an.a(24177, -14434) + response.code() + ")";
                    break block4;
                }
                catch (RuntimeException runtimeException) {
                    throw an.a(runtimeException);
                }
            }
            string = "";
        }
        stringArray[n2] = stringBuilder.append(string).toString();
        this.c.countDown();
    }

    @Override
    public void onClosing(WebSocket webSocket, int n2, String string) {
        webSocket.close((int)g, an.a(24167, -11959));
    }

    @Override
    public void onClosed(WebSocket webSocket, int n2, String string) {
        block9: {
            an an2;
            block10: {
                block11: {
                    boolean bl;
                    block8: {
                        bl = a.b.c.g.g.i();
                        try {
                            try {
                                an2 = this;
                                if (!bl) break block8;
                                if (an2.c.getCount() <= 0L) break block9;
                            }
                            catch (RuntimeException runtimeException) {
                                throw an.a(runtimeException);
                            }
                            an2 = this;
                        }
                        catch (RuntimeException runtimeException) {
                            throw an.a(runtimeException);
                        }
                    }
                    try {
                        try {
                            if (!bl) break block10;
                            if (an2.b[0] != null) break block11;
                        }
                        catch (RuntimeException runtimeException) {
                            throw an.a(runtimeException);
                        }
                        this.b[0] = an.a(24187, -28397) + n2 + ")";
                    }
                    catch (RuntimeException runtimeException) {
                        throw an.a(runtimeException);
                    }
                }
                an2 = this;
            }
            an2.c.countDown();
        }
    }

    private static Exception a(Exception exception) {
        return exception;
    }

    /*
     * Unable to fully structure code
     */
    static {
        block22: {
            block21: {
                block20: {
                    var7 = new String[18];
                    var5_1 = 0;
                    var4_2 = "l\u001f\u00ebj9\u00e5\u00c2Rb\u00e24\u00d5\u00ec~\u00c9\f\u008b\u0083\u00d9!\u00c5c\u00cf\u0003&\u00c8@\u00e2YH\u00b1\u00e4\u00cf\u00ab!\u00db\u00ee\u001dT\u00e7\"\u000f\u00b9\u0012\u00f0\u00cdg\u0090\u00d0v\u00a4\u00e8\u00d4V\u00ca\u009c\u008b\u001ea\u008egh\u00e9\u00c9\u00b8\u00e8|\u00fe\u0013.\u009a\u00e3\u001372\u00b0\u0094\u009b\u00825\u001fo}d\u00c3\u00cb\u00a4\u009a\u0006T\u00f3<\u0018\u00fc\u00a6)Q\u00d3\u00ae\u00a2\u00b9\u008c\u00dfx12\u00ff\u00f8T53\u00f9M\u00a1\u0090=^\u00a1\u00a1&\u0007\u00dc\u008b\u00aa6\"\u00a9\u009b\u009d\u00f7H\u00af\u001doN\f\u00a3\u0005\u001e\u00ab\u0081\u00fct\u0002&\u00da\f\u0000P\u008f\u0098\u0090\u00a0\u00df\u00bc\u00f5\u00a8C\u00f6\"\"\u0011\u00c0\u00fat\u0087g\u0019\u0007\u001e9}\u008c\u0085(\u00ef.c\u00fce\u00d3\u00aa\u0010G1\u00f0O$\u00f9\u00ba\u00e7\u00c65f\u0006U\u00d2\u001c<\u00f8\"\u0007\u00ed\u009a$`ee$\u0005C/*l\u0001,\u0005\u000e\u00c3\u00c0\u00cb\u0000\u00db\u0005\u00ee\u00c7\u00eeh\u0096\u00fd\u00d32\u0093m\u00be\u009a\u00c7\u00a0\u00df\u0002|\u00fe\u00e8\u00fc\u00ee\u00b8\u0086\u0096\u009b'D\u00car\u0002\u001a\u000e\u00a0\u0095\u00cfz0\u00fdL\u0099\u00c9xiY\u00ff\u00a6\u00ef\u0097\u00bf\u0098\u00daL\u00dd\u0099\u00ebx?\u0016\u0087\u00da\u0015+\u00e3\u000f*\u00b2\u00d9\u0098\bvJ.]\\\u00edpk\u00ed\u0012b\u00ff\u00fa\u00c2\u0087\u00bb\u0007\u00f9\u00deu\u00bb\u00d8\u0007\u008c\u0002\u00f5Y";
                    var6_3 = "l\u001f\u00ebj9\u00e5\u00c2Rb\u00e24\u00d5\u00ec~\u00c9\f\u008b\u0083\u00d9!\u00c5c\u00cf\u0003&\u00c8@\u00e2YH\u00b1\u00e4\u00cf\u00ab!\u00db\u00ee\u001dT\u00e7\"\u000f\u00b9\u0012\u00f0\u00cdg\u0090\u00d0v\u00a4\u00e8\u00d4V\u00ca\u009c\u008b\u001ea\u008egh\u00e9\u00c9\u00b8\u00e8|\u00fe\u0013.\u009a\u00e3\u001372\u00b0\u0094\u009b\u00825\u001fo}d\u00c3\u00cb\u00a4\u009a\u0006T\u00f3<\u0018\u00fc\u00a6)Q\u00d3\u00ae\u00a2\u00b9\u008c\u00dfx12\u00ff\u00f8T53\u00f9M\u00a1\u0090=^\u00a1\u00a1&\u0007\u00dc\u008b\u00aa6\"\u00a9\u009b\u009d\u00f7H\u00af\u001doN\f\u00a3\u0005\u001e\u00ab\u0081\u00fct\u0002&\u00da\f\u0000P\u008f\u0098\u0090\u00a0\u00df\u00bc\u00f5\u00a8C\u00f6\"\"\u0011\u00c0\u00fat\u0087g\u0019\u0007\u001e9}\u008c\u0085(\u00ef.c\u00fce\u00d3\u00aa\u0010G1\u00f0O$\u00f9\u00ba\u00e7\u00c65f\u0006U\u00d2\u001c<\u00f8\"\u0007\u00ed\u009a$`ee$\u0005C/*l\u0001,\u0005\u000e\u00c3\u00c0\u00cb\u0000\u00db\u0005\u00ee\u00c7\u00eeh\u0096\u00fd\u00d32\u0093m\u00be\u009a\u00c7\u00a0\u00df\u0002|\u00fe\u00e8\u00fc\u00ee\u00b8\u0086\u0096\u009b'D\u00car\u0002\u001a\u000e\u00a0\u0095\u00cfz0\u00fdL\u0099\u00c9xiY\u00ff\u00a6\u00ef\u0097\u00bf\u0098\u00daL\u00dd\u0099\u00ebx?\u0016\u0087\u00da\u0015+\u00e3\u000f*\u00b2\u00d9\u0098\bvJ.]\\\u00edpk\u00ed\u0012b\u00ff\u00fa\u00c2\u0087\u00bb\u0007\u00f9\u00deu\u00bb\u00d8\u0007\u008c\u0002\u00f5Y".length();
                    var3_4 = 34;
                    var2_5 = -1;
lbl7:
                    // 2 sources

                    while (true) {
                        v0 = 62;
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
                        var4_2 = "}-S\u00f3\u00eer0\u0016\u0087!.\u001f\u00801Is\u00f2\u00bf]\u00a2l3(bpx)\u0000m:";
                        var6_3 = "}-S\u00f3\u00eer0\u0016\u0087!.\u001f\u00801Is\u00f2\u00bf]\u00a2l3(bpx)\u0000m:".length();
                        var3_4 = 7;
                        var2_5 = -1;
lbl22:
                        // 2 sources

                        while (true) {
                            v0 = 86;
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
                                v15 = 34;
                                break;
                            }
                            case 1: {
                                v15 = 35;
                                break;
                            }
                            case 2: {
                                v15 = 68;
                                break;
                            }
                            case 3: {
                                v15 = 71;
                                break;
                            }
                            case 4: {
                                v15 = 123;
                                break;
                            }
                            case 5: {
                                v15 = 27;
                                break;
                            }
                            default: {
                                v15 = 41;
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
            an.e = var7;
            an.f = new String[18];
            break block22;
lbl100:
            // 1 sources

            while (true) {
                continue;
                break;
            }
        }
        var0_7 = 4809114267268256437L;
        ** while (true)
        an.g = 9152836193766445405L ^ var0_7;
    }

    private static String a(int n2, int n3) {
        int n4 = (n2 ^ 0x5E76) & 0xFFFF;
        if (f[n4] == null) {
            int n5;
            int n6;
            char[] cArray = e[n4].toCharArray();
            switch (cArray[0] & 0xFF) {
                case 0: {
                    n6 = 120;
                    break;
                }
                case 1: {
                    n6 = 231;
                    break;
                }
                case 2: {
                    n6 = 88;
                    break;
                }
                case 3: {
                    n6 = 145;
                    break;
                }
                case 4: {
                    n6 = 16;
                    break;
                }
                case 5: {
                    n6 = 100;
                    break;
                }
                case 6: {
                    n6 = 242;
                    break;
                }
                case 7: {
                    n6 = 173;
                    break;
                }
                case 8: {
                    n6 = 219;
                    break;
                }
                case 9: {
                    n6 = 95;
                    break;
                }
                case 10: {
                    n6 = 77;
                    break;
                }
                case 11: {
                    n6 = 106;
                    break;
                }
                case 12: {
                    n6 = 160;
                    break;
                }
                case 13: {
                    n6 = 146;
                    break;
                }
                case 14: {
                    n6 = 213;
                    break;
                }
                case 15: {
                    n6 = 111;
                    break;
                }
                case 16: {
                    n6 = 216;
                    break;
                }
                case 17: {
                    n6 = 162;
                    break;
                }
                case 18: {
                    n6 = 109;
                    break;
                }
                case 19: {
                    n6 = 86;
                    break;
                }
                case 20: {
                    n6 = 69;
                    break;
                }
                case 21: {
                    n6 = 117;
                    break;
                }
                case 22: {
                    n6 = 157;
                    break;
                }
                case 23: {
                    n6 = 187;
                    break;
                }
                case 24: {
                    n6 = 138;
                    break;
                }
                case 25: {
                    n6 = 97;
                    break;
                }
                case 26: {
                    n6 = 194;
                    break;
                }
                case 27: {
                    n6 = 251;
                    break;
                }
                case 28: {
                    n6 = 98;
                    break;
                }
                case 29: {
                    n6 = 94;
                    break;
                }
                case 30: {
                    n6 = 36;
                    break;
                }
                case 31: {
                    n6 = 45;
                    break;
                }
                case 32: {
                    n6 = 181;
                    break;
                }
                case 33: {
                    n6 = 238;
                    break;
                }
                case 34: {
                    n6 = 158;
                    break;
                }
                case 35: {
                    n6 = 18;
                    break;
                }
                case 36: {
                    n6 = 208;
                    break;
                }
                case 37: {
                    n6 = 247;
                    break;
                }
                case 38: {
                    n6 = 11;
                    break;
                }
                case 39: {
                    n6 = 13;
                    break;
                }
                case 40: {
                    n6 = 141;
                    break;
                }
                case 41: {
                    n6 = 234;
                    break;
                }
                case 42: {
                    n6 = 154;
                    break;
                }
                case 43: {
                    n6 = 186;
                    break;
                }
                case 44: {
                    n6 = 92;
                    break;
                }
                case 45: {
                    n6 = 183;
                    break;
                }
                case 46: {
                    n6 = 250;
                    break;
                }
                case 47: {
                    n6 = 2;
                    break;
                }
                case 48: {
                    n6 = 8;
                    break;
                }
                case 49: {
                    n6 = 48;
                    break;
                }
                case 50: {
                    n6 = 252;
                    break;
                }
                case 51: {
                    n6 = 226;
                    break;
                }
                case 52: {
                    n6 = 218;
                    break;
                }
                case 53: {
                    n6 = 74;
                    break;
                }
                case 54: {
                    n6 = 28;
                    break;
                }
                case 55: {
                    n6 = 81;
                    break;
                }
                case 56: {
                    n6 = 152;
                    break;
                }
                case 57: {
                    n6 = 243;
                    break;
                }
                case 58: {
                    n6 = 161;
                    break;
                }
                case 59: {
                    n6 = 227;
                    break;
                }
                case 60: {
                    n6 = 76;
                    break;
                }
                case 61: {
                    n6 = 87;
                    break;
                }
                case 62: {
                    n6 = 57;
                    break;
                }
                case 63: {
                    n6 = 177;
                    break;
                }
                case 64: {
                    n6 = 110;
                    break;
                }
                case 65: {
                    n6 = 113;
                    break;
                }
                case 66: {
                    n6 = 239;
                    break;
                }
                case 67: {
                    n6 = 190;
                    break;
                }
                case 68: {
                    n6 = 14;
                    break;
                }
                case 69: {
                    n6 = 91;
                    break;
                }
                case 70: {
                    n6 = 114;
                    break;
                }
                case 71: {
                    n6 = 151;
                    break;
                }
                case 72: {
                    n6 = 237;
                    break;
                }
                case 73: {
                    n6 = 125;
                    break;
                }
                case 74: {
                    n6 = 164;
                    break;
                }
                case 75: {
                    n6 = 147;
                    break;
                }
                case 76: {
                    n6 = 217;
                    break;
                }
                case 77: {
                    n6 = 206;
                    break;
                }
                case 78: {
                    n6 = 175;
                    break;
                }
                case 79: {
                    n6 = 40;
                    break;
                }
                case 80: {
                    n6 = 72;
                    break;
                }
                case 81: {
                    n6 = 63;
                    break;
                }
                case 82: {
                    n6 = 60;
                    break;
                }
                case 83: {
                    n6 = 107;
                    break;
                }
                case 84: {
                    n6 = 104;
                    break;
                }
                case 85: {
                    n6 = 174;
                    break;
                }
                case 86: {
                    n6 = 182;
                    break;
                }
                case 87: {
                    n6 = 119;
                    break;
                }
                case 88: {
                    n6 = 224;
                    break;
                }
                case 89: {
                    n6 = 59;
                    break;
                }
                case 90: {
                    n6 = 203;
                    break;
                }
                case 91: {
                    n6 = 32;
                    break;
                }
                case 92: {
                    n6 = 201;
                    break;
                }
                case 93: {
                    n6 = 200;
                    break;
                }
                case 94: {
                    n6 = 155;
                    break;
                }
                case 95: {
                    n6 = 12;
                    break;
                }
                case 96: {
                    n6 = 144;
                    break;
                }
                case 97: {
                    n6 = 185;
                    break;
                }
                case 98: {
                    n6 = 29;
                    break;
                }
                case 99: {
                    n6 = 136;
                    break;
                }
                case 100: {
                    n6 = 73;
                    break;
                }
                case 101: {
                    n6 = 39;
                    break;
                }
                case 102: {
                    n6 = 108;
                    break;
                }
                case 103: {
                    n6 = 255;
                    break;
                }
                case 104: {
                    n6 = 143;
                    break;
                }
                case 105: {
                    n6 = 254;
                    break;
                }
                case 106: {
                    n6 = 233;
                    break;
                }
                case 107: {
                    n6 = 220;
                    break;
                }
                case 108: {
                    n6 = 53;
                    break;
                }
                case 109: {
                    n6 = 47;
                    break;
                }
                case 110: {
                    n6 = 65;
                    break;
                }
                case 111: {
                    n6 = 210;
                    break;
                }
                case 112: {
                    n6 = 207;
                    break;
                }
                case 113: {
                    n6 = 198;
                    break;
                }
                case 114: {
                    n6 = 82;
                    break;
                }
                case 115: {
                    n6 = 232;
                    break;
                }
                case 116: {
                    n6 = 90;
                    break;
                }
                case 117: {
                    n6 = 166;
                    break;
                }
                case 118: {
                    n6 = 43;
                    break;
                }
                case 119: {
                    n6 = 240;
                    break;
                }
                case 120: {
                    n6 = 134;
                    break;
                }
                case 121: {
                    n6 = 22;
                    break;
                }
                case 122: {
                    n6 = 26;
                    break;
                }
                case 123: {
                    n6 = 37;
                    break;
                }
                case 124: {
                    n6 = 165;
                    break;
                }
                case 125: {
                    n6 = 248;
                    break;
                }
                case 126: {
                    n6 = 129;
                    break;
                }
                case 127: {
                    n6 = 140;
                    break;
                }
                case 128: {
                    n6 = 84;
                    break;
                }
                case 129: {
                    n6 = 197;
                    break;
                }
                case 130: {
                    n6 = 96;
                    break;
                }
                case 131: {
                    n6 = 102;
                    break;
                }
                case 132: {
                    n6 = 68;
                    break;
                }
                case 133: {
                    n6 = 85;
                    break;
                }
                case 134: {
                    n6 = 170;
                    break;
                }
                case 135: {
                    n6 = 44;
                    break;
                }
                case 136: {
                    n6 = 79;
                    break;
                }
                case 137: {
                    n6 = 214;
                    break;
                }
                case 138: {
                    n6 = 62;
                    break;
                }
                case 139: {
                    n6 = 99;
                    break;
                }
                case 140: {
                    n6 = 49;
                    break;
                }
                case 141: {
                    n6 = 103;
                    break;
                }
                case 142: {
                    n6 = 171;
                    break;
                }
                case 143: {
                    n6 = 193;
                    break;
                }
                case 144: {
                    n6 = 75;
                    break;
                }
                case 145: {
                    n6 = 188;
                    break;
                }
                case 146: {
                    n6 = 245;
                    break;
                }
                case 147: {
                    n6 = 41;
                    break;
                }
                case 148: {
                    n6 = 61;
                    break;
                }
                case 149: {
                    n6 = 130;
                    break;
                }
                case 150: {
                    n6 = 178;
                    break;
                }
                case 151: {
                    n6 = 78;
                    break;
                }
                case 152: {
                    n6 = 80;
                    break;
                }
                case 153: {
                    n6 = 116;
                    break;
                }
                case 154: {
                    n6 = 101;
                    break;
                }
                case 155: {
                    n6 = 192;
                    break;
                }
                case 156: {
                    n6 = 215;
                    break;
                }
                case 157: {
                    n6 = 52;
                    break;
                }
                case 158: {
                    n6 = 55;
                    break;
                }
                case 159: {
                    n6 = 70;
                    break;
                }
                case 160: {
                    n6 = 137;
                    break;
                }
                case 161: {
                    n6 = 204;
                    break;
                }
                case 162: {
                    n6 = 212;
                    break;
                }
                case 163: {
                    n6 = 225;
                    break;
                }
                case 164: {
                    n6 = 222;
                    break;
                }
                case 165: {
                    n6 = 223;
                    break;
                }
                case 166: {
                    n6 = 105;
                    break;
                }
                case 167: {
                    n6 = 42;
                    break;
                }
                case 168: {
                    n6 = 83;
                    break;
                }
                case 169: {
                    n6 = 123;
                    break;
                }
                case 170: {
                    n6 = 163;
                    break;
                }
                case 171: {
                    n6 = 191;
                    break;
                }
                case 172: {
                    n6 = 51;
                    break;
                }
                case 173: {
                    n6 = 142;
                    break;
                }
                case 174: {
                    n6 = 56;
                    break;
                }
                case 175: {
                    n6 = 211;
                    break;
                }
                case 176: {
                    n6 = 184;
                    break;
                }
                case 177: {
                    n6 = 209;
                    break;
                }
                case 178: {
                    n6 = 180;
                    break;
                }
                case 179: {
                    n6 = 228;
                    break;
                }
                case 180: {
                    n6 = 249;
                    break;
                }
                case 181: {
                    n6 = 128;
                    break;
                }
                case 182: {
                    n6 = 24;
                    break;
                }
                case 183: {
                    n6 = 199;
                    break;
                }
                case 184: {
                    n6 = 23;
                    break;
                }
                case 185: {
                    n6 = 64;
                    break;
                }
                case 186: {
                    n6 = 246;
                    break;
                }
                case 187: {
                    n6 = 66;
                    break;
                }
                case 188: {
                    n6 = 221;
                    break;
                }
                case 189: {
                    n6 = 6;
                    break;
                }
                case 190: {
                    n6 = 3;
                    break;
                }
                case 191: {
                    n6 = 126;
                    break;
                }
                case 192: {
                    n6 = 50;
                    break;
                }
                case 193: {
                    n6 = 230;
                    break;
                }
                case 194: {
                    n6 = 5;
                    break;
                }
                case 195: {
                    n6 = 89;
                    break;
                }
                case 196: {
                    n6 = 33;
                    break;
                }
                case 197: {
                    n6 = 205;
                    break;
                }
                case 198: {
                    n6 = 156;
                    break;
                }
                case 199: {
                    n6 = 150;
                    break;
                }
                case 200: {
                    n6 = 195;
                    break;
                }
                case 201: {
                    n6 = 132;
                    break;
                }
                case 202: {
                    n6 = 21;
                    break;
                }
                case 203: {
                    n6 = 0;
                    break;
                }
                case 204: {
                    n6 = 7;
                    break;
                }
                case 205: {
                    n6 = 253;
                    break;
                }
                case 206: {
                    n6 = 169;
                    break;
                }
                case 207: {
                    n6 = 135;
                    break;
                }
                case 208: {
                    n6 = 4;
                    break;
                }
                case 209: {
                    n6 = 19;
                    break;
                }
                case 210: {
                    n6 = 1;
                    break;
                }
                case 211: {
                    n6 = 38;
                    break;
                }
                case 212: {
                    n6 = 67;
                    break;
                }
                case 213: {
                    n6 = 241;
                    break;
                }
                case 214: {
                    n6 = 189;
                    break;
                }
                case 215: {
                    n6 = 176;
                    break;
                }
                case 216: {
                    n6 = 58;
                    break;
                }
                case 217: {
                    n6 = 168;
                    break;
                }
                case 218: {
                    n6 = 244;
                    break;
                }
                case 219: {
                    n6 = 118;
                    break;
                }
                case 220: {
                    n6 = 20;
                    break;
                }
                case 221: {
                    n6 = 46;
                    break;
                }
                case 222: {
                    n6 = 35;
                    break;
                }
                case 223: {
                    n6 = 115;
                    break;
                }
                case 224: {
                    n6 = 133;
                    break;
                }
                case 225: {
                    n6 = 93;
                    break;
                }
                case 226: {
                    n6 = 167;
                    break;
                }
                case 227: {
                    n6 = 139;
                    break;
                }
                case 228: {
                    n6 = 25;
                    break;
                }
                case 229: {
                    n6 = 149;
                    break;
                }
                case 230: {
                    n6 = 179;
                    break;
                }
                case 231: {
                    n6 = 124;
                    break;
                }
                case 232: {
                    n6 = 202;
                    break;
                }
                case 233: {
                    n6 = 15;
                    break;
                }
                case 234: {
                    n6 = 9;
                    break;
                }
                case 235: {
                    n6 = 71;
                    break;
                }
                case 236: {
                    n6 = 236;
                    break;
                }
                case 237: {
                    n6 = 122;
                    break;
                }
                case 238: {
                    n6 = 235;
                    break;
                }
                case 239: {
                    n6 = 196;
                    break;
                }
                case 240: {
                    n6 = 131;
                    break;
                }
                case 241: {
                    n6 = 172;
                    break;
                }
                case 242: {
                    n6 = 159;
                    break;
                }
                case 243: {
                    n6 = 153;
                    break;
                }
                case 244: {
                    n6 = 148;
                    break;
                }
                case 245: {
                    n6 = 27;
                    break;
                }
                case 246: {
                    n6 = 229;
                    break;
                }
                case 247: {
                    n6 = 34;
                    break;
                }
                case 248: {
                    n6 = 121;
                    break;
                }
                case 249: {
                    n6 = 10;
                    break;
                }
                case 250: {
                    n6 = 30;
                    break;
                }
                case 251: {
                    n6 = 112;
                    break;
                }
                case 252: {
                    n6 = 17;
                    break;
                }
                case 253: {
                    n6 = 54;
                    break;
                }
                case 254: {
                    n6 = 127;
                    break;
                }
                default: {
                    n6 = 31;
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
            an.f[n4] = new String(cArray).intern();
        }
        return f[n4];
    }
}

