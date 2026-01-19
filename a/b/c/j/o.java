/*
 * Decompiled with CFR 0.152.
 */
package a.b.c.j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.ZipOutputStream;

public class o {
    public static final Map<String, Map<String, Integer>> browserDataCounts;
    public static final o INSTANCE;
    private static String[] b;
    private static final String[] c;
    private static final String[] d;

    public static synchronized void recordDataCount(String string, String string2, int n2) {
        block4: {
            block5: {
                String[] stringArray = o.b();
                try {
                    try {
                        if (stringArray != null) break block4;
                        if (n2 != 0) break block5;
                    }
                    catch (RuntimeException runtimeException) {
                        throw o.a(runtimeException);
                    }
                    return;
                }
                catch (RuntimeException runtimeException) {
                    throw o.a(runtimeException);
                }
            }
            browserDataCounts.putIfAbsent(string, new ConcurrentHashMap());
        }
        Map<String, Integer> map = browserDataCounts.get(string);
        map.put(string2, n2);
    }

    public static synchronized String getCountsSummary() {
        StringBuilder stringBuilder;
        block26: {
            int n2;
            block25: {
                ConcurrentHashMap<String, Integer> concurrentHashMap;
                String[] stringArray;
                block24: {
                    boolean bl;
                    ConcurrentHashMap<String, Integer> concurrentHashMap2;
                    block21: {
                        stringArray = o.b();
                        try {
                            if (browserDataCounts.isEmpty()) {
                                return o.a(-8612, 26685);
                            }
                        }
                        catch (RuntimeException runtimeException) {
                            throw o.a(runtimeException);
                        }
                        stringBuilder = new StringBuilder();
                        stringBuilder.append(o.a(-8622, -8605));
                        concurrentHashMap2 = new ConcurrentHashMap<String, Integer>();
                        n2 = 0;
                        for (Map.Entry<String, Map<String, Integer>> entry : browserDataCounts.entrySet()) {
                            block23: {
                                boolean bl2;
                                Map<String, Integer> map;
                                String string;
                                block22: {
                                    string = entry.getKey();
                                    map = entry.getValue();
                                    try {
                                        try {
                                            bl = map.isEmpty();
                                            if (stringArray != null) break block21;
                                            if (stringArray != null) break block22;
                                        }
                                        catch (RuntimeException runtimeException) {
                                            throw o.a(runtimeException);
                                        }
                                        if (bl) {
                                            continue;
                                        }
                                    }
                                    catch (RuntimeException runtimeException) {
                                        throw o.a(runtimeException);
                                    }
                                    stringBuilder.append(string).append(o.a(-8610, 10824));
                                    bl2 = false;
                                }
                                void var8_10 = bl2;
                                for (Map.Entry<String, Integer> entry2 : map.entrySet()) {
                                    String string2 = entry2.getKey();
                                    int n3 = entry2.getValue();
                                    stringBuilder.append(o.a(-8623, -18338)).append(string2).append(o.a(-8615, -17990)).append(n3).append("\n");
                                    var8_10 += n3;
                                    try {
                                        concurrentHashMap2.put(string2, concurrentHashMap2.getOrDefault(string2, 0) + n3);
                                        if (stringArray == null) {
                                            if (stringArray == null) continue;
                                            break;
                                        }
                                        break block23;
                                    }
                                    catch (RuntimeException runtimeException) {
                                        throw o.a(runtimeException);
                                    }
                                }
                                stringBuilder.append(o.a(-8609, 3276)).append(string).append(o.a(-8621, -10017)).append((int)var8_10).append(o.a(-8613, 22607));
                                n2 += var8_10;
                            }
                            if (stringArray == null) continue;
                        }
                        try {
                            concurrentHashMap = concurrentHashMap2;
                            if (stringArray != null) break block24;
                            bl = concurrentHashMap.isEmpty();
                        }
                        catch (RuntimeException runtimeException) {
                            throw o.a(runtimeException);
                        }
                    }
                    try {
                        if (bl) break block25;
                        stringBuilder.append(o.a(-8614, -25124));
                        concurrentHashMap = concurrentHashMap2;
                    }
                    catch (RuntimeException runtimeException) {
                        throw o.a(runtimeException);
                    }
                }
                for (Map.Entry<String, Map<String, Integer>> entry : concurrentHashMap.entrySet()) {
                    try {
                        stringBuilder.append(o.a(-8624, -20834)).append(entry.getKey()).append(o.a(-8621, -10017)).append(entry.getValue()).append("\n");
                        if (stringArray == null) {
                            if (stringArray == null) continue;
                            break;
                        }
                        break block26;
                    }
                    catch (RuntimeException runtimeException) {
                        throw o.a(runtimeException);
                    }
                }
                stringBuilder.append("\n");
            }
            stringBuilder.append(o.a(-8618, 2016)).append(n2).append(o.a(-8616, 16190));
            stringBuilder.append(o.a(-8611, 5415));
        }
        return stringBuilder.toString();
    }

    public void toOutput(ZipOutputStream zipOutputStream) {
    }

    /*
     * Unable to fully structure code
     */
    static {
        block20: {
            block19: {
                var5 = new String[13];
                var3_1 = 0;
                var2_2 = "8\u00f7\u00f6\u0086$\n\u00bb\u00e3)\u00abI\u000b\u000fs\u0087'e\u0001\u00fc\u00e2u\u00cc\u0096<?\u00ee#)U\u00f1\u00a8\u008bk\f`\u001e\u00c1\u0082\u00d5D?\u00dc\u0095\u0002\u009e\u008b\u0005\u00c2\u00ce\t-\u001b\u0002<)\u0002\u001f\u00c1\f\u00af\u00e3\u00d1\u00e7\u00a5\u0093\u00d3\u00ef\u008a\u00e2Y\u00cb\u001em\u001d^\u0093\u00a8\u00bf\u00af\u00f6~\u00e5\u00f6\u00d1\u0017W>G\u00a9}\u00eaN\u00d2rN\u00f4\u009d)\u0082\u00be>G+\u00ae\u00b0$c\u0014E\u00d6\u00fd\bNtY\u00a7\u007f\u00a1=\u00c5\u00d2(sQ;\u00f8\u0096jBd\u001c\u00d9Q\u00ca_\u00c9\u00c2m\r\u007fP\u009a\u009az\u0007=\u001d\u0010\u0099\u00f3K\u0081\u00b3\u00a4\u00cd\u0012\u0011\u00fd\u00feKk!6\u00aa\b1\u00d9aj\u00e9-\u00f1\u00eb\u00bb)\u0005\u0002-<\u0006\u001f\u00d8\u001d(\u0000\u00e4";
                var4_3 = "8\u00f7\u00f6\u0086$\n\u00bb\u00e3)\u00abI\u000b\u000fs\u0087'e\u0001\u00fc\u00e2u\u00cc\u0096<?\u00ee#)U\u00f1\u00a8\u008bk\f`\u001e\u00c1\u0082\u00d5D?\u00dc\u0095\u0002\u009e\u008b\u0005\u00c2\u00ce\t-\u001b\u0002<)\u0002\u001f\u00c1\f\u00af\u00e3\u00d1\u00e7\u00a5\u0093\u00d3\u00ef\u008a\u00e2Y\u00cb\u001em\u001d^\u0093\u00a8\u00bf\u00af\u00f6~\u00e5\u00f6\u00d1\u0017W>G\u00a9}\u00eaN\u00d2rN\u00f4\u009d)\u0082\u00be>G+\u00ae\u00b0$c\u0014E\u00d6\u00fd\bNtY\u00a7\u007f\u00a1=\u00c5\u00d2(sQ;\u00f8\u0096jBd\u001c\u00d9Q\u00ca_\u00c9\u00c2m\r\u007fP\u009a\u009az\u0007=\u001d\u0010\u0099\u00f3K\u0081\u00b3\u00a4\u00cd\u0012\u0011\u00fd\u00feKk!6\u00aa\b1\u00d9aj\u00e9-\u00f1\u00eb\u00bb)\u0005\u0002-<\u0006\u001f\u00d8\u001d(\u0000\u00e4".length();
                o.b(null);
                var1_4 = 43;
                var0_5 = -1;
lbl8:
                // 2 sources

                while (true) {
                    v0 = 20;
                    v1 = ++var0_5;
                    v2 = var2_2.substring(v1, v1 + var1_4);
                    v3 = -1;
                    break block19;
                    break;
                }
lbl14:
                // 1 sources

                while (true) {
                    var5[var3_1++] = v4.intern();
                    if ((var0_5 += var1_4) < var4_3) {
                        var1_4 = var2_2.charAt(var0_5);
                        ** continue;
                    }
                    var2_2 = "\u0003\u00b5'\u00c9\u001c\u00078\u00ca\u00ael\u008c&\u00c2\u00d6S=F\u00a75\u0084H\u009dY\u00ef\u001e\tv\u00da\u00d4\u00bc\u00c0\u00aeW\u008a;\u00df]/\u00ab\u00edl\u000f";
                    var4_3 = "\u0003\u00b5'\u00c9\u001c\u00078\u00ca\u00ael\u008c&\u00c2\u00d6S=F\u00a75\u0084H\u009dY\u00ef\u001e\tv\u00da\u00d4\u00bc\u00c0\u00aeW\u008a;\u00df]/\u00ab\u00edl\u000f".length();
                    var1_4 = 2;
                    var0_5 = -1;
lbl23:
                    // 2 sources

                    while (true) {
                        v0 = 63;
                        v5 = ++var0_5;
                        v2 = var2_2.substring(v5, v5 + var1_4);
                        v3 = 0;
                        break block19;
                        break;
                    }
                    break;
                }
lbl29:
                // 1 sources

                while (true) {
                    var5[var3_1++] = v4.intern();
                    if ((var0_5 += var1_4) < var4_3) {
                        var1_4 = var2_2.charAt(var0_5);
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
            if (v7 > 1) ** GOTO lbl86
            do {
                v11 = v8;
                v9 = v9;
                v12 = v9;
                v13 = v8;
                v14 = var6_6;
                while (true) {
                    switch (var6_6 % 7) {
                        case 0: {
                            v15 = 44;
                            break;
                        }
                        case 1: {
                            v15 = 32;
                            break;
                        }
                        case 2: {
                            v15 = 71;
                            break;
                        }
                        case 3: {
                            v15 = 66;
                            break;
                        }
                        case 4: {
                            v15 = 75;
                            break;
                        }
                        case 5: {
                            v15 = 82;
                            break;
                        }
                        default: {
                            v15 = 14;
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
lbl86:
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
lbl96:
                // 1 sources

                ** continue;
            }
        }
        o.c = var5;
        o.d = new String[13];
        o.browserDataCounts = new ConcurrentHashMap<String, Map<String, Integer>>();
        o.INSTANCE = new o();
    }

    public static void b(String[] stringArray) {
        b = stringArray;
    }

    public static String[] b() {
        return b;
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }

    private static String a(int n2, int n3) {
        int n4 = (n2 ^ 0xFFFFDE5A) & 0xFFFF;
        if (d[n4] == null) {
            int n5;
            int n6;
            char[] cArray = c[n4].toCharArray();
            switch (cArray[0] & 0xFF) {
                case 0: {
                    n6 = 175;
                    break;
                }
                case 1: {
                    n6 = 16;
                    break;
                }
                case 2: {
                    n6 = 72;
                    break;
                }
                case 3: {
                    n6 = 233;
                    break;
                }
                case 4: {
                    n6 = 124;
                    break;
                }
                case 5: {
                    n6 = 141;
                    break;
                }
                case 6: {
                    n6 = 64;
                    break;
                }
                case 7: {
                    n6 = 111;
                    break;
                }
                case 8: {
                    n6 = 195;
                    break;
                }
                case 9: {
                    n6 = 40;
                    break;
                }
                case 10: {
                    n6 = 38;
                    break;
                }
                case 11: {
                    n6 = 184;
                    break;
                }
                case 12: {
                    n6 = 107;
                    break;
                }
                case 13: {
                    n6 = 15;
                    break;
                }
                case 14: {
                    n6 = 157;
                    break;
                }
                case 15: {
                    n6 = 103;
                    break;
                }
                case 16: {
                    n6 = 46;
                    break;
                }
                case 17: {
                    n6 = 142;
                    break;
                }
                case 18: {
                    n6 = 234;
                    break;
                }
                case 19: {
                    n6 = 172;
                    break;
                }
                case 20: {
                    n6 = 149;
                    break;
                }
                case 21: {
                    n6 = 176;
                    break;
                }
                case 22: {
                    n6 = 117;
                    break;
                }
                case 23: {
                    n6 = 168;
                    break;
                }
                case 24: {
                    n6 = 134;
                    break;
                }
                case 25: {
                    n6 = 143;
                    break;
                }
                case 26: {
                    n6 = 200;
                    break;
                }
                case 27: {
                    n6 = 76;
                    break;
                }
                case 28: {
                    n6 = 89;
                    break;
                }
                case 29: {
                    n6 = 243;
                    break;
                }
                case 30: {
                    n6 = 169;
                    break;
                }
                case 31: {
                    n6 = 98;
                    break;
                }
                case 32: {
                    n6 = 187;
                    break;
                }
                case 33: {
                    n6 = 251;
                    break;
                }
                case 34: {
                    n6 = 189;
                    break;
                }
                case 35: {
                    n6 = 52;
                    break;
                }
                case 36: {
                    n6 = 121;
                    break;
                }
                case 37: {
                    n6 = 223;
                    break;
                }
                case 38: {
                    n6 = 26;
                    break;
                }
                case 39: {
                    n6 = 43;
                    break;
                }
                case 40: {
                    n6 = 94;
                    break;
                }
                case 41: {
                    n6 = 162;
                    break;
                }
                case 42: {
                    n6 = 39;
                    break;
                }
                case 43: {
                    n6 = 203;
                    break;
                }
                case 44: {
                    n6 = 118;
                    break;
                }
                case 45: {
                    n6 = 41;
                    break;
                }
                case 46: {
                    n6 = 60;
                    break;
                }
                case 47: {
                    n6 = 49;
                    break;
                }
                case 48: {
                    n6 = 227;
                    break;
                }
                case 49: {
                    n6 = 248;
                    break;
                }
                case 50: {
                    n6 = 59;
                    break;
                }
                case 51: {
                    n6 = 235;
                    break;
                }
                case 52: {
                    n6 = 206;
                    break;
                }
                case 53: {
                    n6 = 55;
                    break;
                }
                case 54: {
                    n6 = 33;
                    break;
                }
                case 55: {
                    n6 = 97;
                    break;
                }
                case 56: {
                    n6 = 13;
                    break;
                }
                case 57: {
                    n6 = 147;
                    break;
                }
                case 58: {
                    n6 = 148;
                    break;
                }
                case 59: {
                    n6 = 250;
                    break;
                }
                case 60: {
                    n6 = 27;
                    break;
                }
                case 61: {
                    n6 = 61;
                    break;
                }
                case 62: {
                    n6 = 204;
                    break;
                }
                case 63: {
                    n6 = 205;
                    break;
                }
                case 64: {
                    n6 = 140;
                    break;
                }
                case 65: {
                    n6 = 131;
                    break;
                }
                case 66: {
                    n6 = 193;
                    break;
                }
                case 67: {
                    n6 = 102;
                    break;
                }
                case 68: {
                    n6 = 36;
                    break;
                }
                case 69: {
                    n6 = 164;
                    break;
                }
                case 70: {
                    n6 = 246;
                    break;
                }
                case 71: {
                    n6 = 78;
                    break;
                }
                case 72: {
                    n6 = 249;
                    break;
                }
                case 73: {
                    n6 = 3;
                    break;
                }
                case 74: {
                    n6 = 161;
                    break;
                }
                case 75: {
                    n6 = 221;
                    break;
                }
                case 76: {
                    n6 = 77;
                    break;
                }
                case 77: {
                    n6 = 54;
                    break;
                }
                case 78: {
                    n6 = 122;
                    break;
                }
                case 79: {
                    n6 = 196;
                    break;
                }
                case 80: {
                    n6 = 127;
                    break;
                }
                case 81: {
                    n6 = 23;
                    break;
                }
                case 82: {
                    n6 = 88;
                    break;
                }
                case 83: {
                    n6 = 4;
                    break;
                }
                case 84: {
                    n6 = 202;
                    break;
                }
                case 85: {
                    n6 = 34;
                    break;
                }
                case 86: {
                    n6 = 73;
                    break;
                }
                case 87: {
                    n6 = 110;
                    break;
                }
                case 88: {
                    n6 = 119;
                    break;
                }
                case 89: {
                    n6 = 48;
                    break;
                }
                case 90: {
                    n6 = 208;
                    break;
                }
                case 91: {
                    n6 = 133;
                    break;
                }
                case 92: {
                    n6 = 215;
                    break;
                }
                case 93: {
                    n6 = 224;
                    break;
                }
                case 94: {
                    n6 = 125;
                    break;
                }
                case 95: {
                    n6 = 65;
                    break;
                }
                case 96: {
                    n6 = 70;
                    break;
                }
                case 97: {
                    n6 = 216;
                    break;
                }
                case 98: {
                    n6 = 58;
                    break;
                }
                case 99: {
                    n6 = 228;
                    break;
                }
                case 100: {
                    n6 = 182;
                    break;
                }
                case 101: {
                    n6 = 128;
                    break;
                }
                case 102: {
                    n6 = 253;
                    break;
                }
                case 103: {
                    n6 = 71;
                    break;
                }
                case 104: {
                    n6 = 240;
                    break;
                }
                case 105: {
                    n6 = 50;
                    break;
                }
                case 106: {
                    n6 = 211;
                    break;
                }
                case 107: {
                    n6 = 165;
                    break;
                }
                case 108: {
                    n6 = 219;
                    break;
                }
                case 109: {
                    n6 = 7;
                    break;
                }
                case 110: {
                    n6 = 95;
                    break;
                }
                case 111: {
                    n6 = 232;
                    break;
                }
                case 112: {
                    n6 = 156;
                    break;
                }
                case 113: {
                    n6 = 24;
                    break;
                }
                case 114: {
                    n6 = 11;
                    break;
                }
                case 115: {
                    n6 = 132;
                    break;
                }
                case 116: {
                    n6 = 18;
                    break;
                }
                case 117: {
                    n6 = 37;
                    break;
                }
                case 118: {
                    n6 = 14;
                    break;
                }
                case 119: {
                    n6 = 123;
                    break;
                }
                case 120: {
                    n6 = 167;
                    break;
                }
                case 121: {
                    n6 = 81;
                    break;
                }
                case 122: {
                    n6 = 31;
                    break;
                }
                case 123: {
                    n6 = 17;
                    break;
                }
                case 124: {
                    n6 = 166;
                    break;
                }
                case 125: {
                    n6 = 178;
                    break;
                }
                case 126: {
                    n6 = 137;
                    break;
                }
                case 127: {
                    n6 = 159;
                    break;
                }
                case 128: {
                    n6 = 75;
                    break;
                }
                case 129: {
                    n6 = 62;
                    break;
                }
                case 130: {
                    n6 = 53;
                    break;
                }
                case 131: {
                    n6 = 91;
                    break;
                }
                case 132: {
                    n6 = 105;
                    break;
                }
                case 133: {
                    n6 = 222;
                    break;
                }
                case 134: {
                    n6 = 237;
                    break;
                }
                case 135: {
                    n6 = 135;
                    break;
                }
                case 136: {
                    n6 = 116;
                    break;
                }
                case 137: {
                    n6 = 114;
                    break;
                }
                case 138: {
                    n6 = 20;
                    break;
                }
                case 139: {
                    n6 = 126;
                    break;
                }
                case 140: {
                    n6 = 231;
                    break;
                }
                case 141: {
                    n6 = 74;
                    break;
                }
                case 142: {
                    n6 = 244;
                    break;
                }
                case 143: {
                    n6 = 115;
                    break;
                }
                case 144: {
                    n6 = 236;
                    break;
                }
                case 145: {
                    n6 = 69;
                    break;
                }
                case 146: {
                    n6 = 19;
                    break;
                }
                case 147: {
                    n6 = 247;
                    break;
                }
                case 148: {
                    n6 = 217;
                    break;
                }
                case 149: {
                    n6 = 160;
                    break;
                }
                case 150: {
                    n6 = 108;
                    break;
                }
                case 151: {
                    n6 = 21;
                    break;
                }
                case 152: {
                    n6 = 113;
                    break;
                }
                case 153: {
                    n6 = 171;
                    break;
                }
                case 154: {
                    n6 = 44;
                    break;
                }
                case 155: {
                    n6 = 145;
                    break;
                }
                case 156: {
                    n6 = 154;
                    break;
                }
                case 157: {
                    n6 = 66;
                    break;
                }
                case 158: {
                    n6 = 8;
                    break;
                }
                case 159: {
                    n6 = 138;
                    break;
                }
                case 160: {
                    n6 = 9;
                    break;
                }
                case 161: {
                    n6 = 45;
                    break;
                }
                case 162: {
                    n6 = 42;
                    break;
                }
                case 163: {
                    n6 = 5;
                    break;
                }
                case 164: {
                    n6 = 177;
                    break;
                }
                case 165: {
                    n6 = 104;
                    break;
                }
                case 166: {
                    n6 = 163;
                    break;
                }
                case 167: {
                    n6 = 99;
                    break;
                }
                case 168: {
                    n6 = 109;
                    break;
                }
                case 169: {
                    n6 = 218;
                    break;
                }
                case 170: {
                    n6 = 212;
                    break;
                }
                case 171: {
                    n6 = 1;
                    break;
                }
                case 172: {
                    n6 = 209;
                    break;
                }
                case 173: {
                    n6 = 188;
                    break;
                }
                case 174: {
                    n6 = 67;
                    break;
                }
                case 175: {
                    n6 = 22;
                    break;
                }
                case 176: {
                    n6 = 242;
                    break;
                }
                case 177: {
                    n6 = 63;
                    break;
                }
                case 178: {
                    n6 = 153;
                    break;
                }
                case 179: {
                    n6 = 190;
                    break;
                }
                case 180: {
                    n6 = 85;
                    break;
                }
                case 181: {
                    n6 = 120;
                    break;
                }
                case 182: {
                    n6 = 10;
                    break;
                }
                case 183: {
                    n6 = 201;
                    break;
                }
                case 184: {
                    n6 = 68;
                    break;
                }
                case 185: {
                    n6 = 32;
                    break;
                }
                case 186: {
                    n6 = 179;
                    break;
                }
                case 187: {
                    n6 = 29;
                    break;
                }
                case 188: {
                    n6 = 92;
                    break;
                }
                case 189: {
                    n6 = 106;
                    break;
                }
                case 190: {
                    n6 = 252;
                    break;
                }
                case 191: {
                    n6 = 183;
                    break;
                }
                case 192: {
                    n6 = 181;
                    break;
                }
                case 193: {
                    n6 = 101;
                    break;
                }
                case 194: {
                    n6 = 0;
                    break;
                }
                case 195: {
                    n6 = 225;
                    break;
                }
                case 196: {
                    n6 = 146;
                    break;
                }
                case 197: {
                    n6 = 245;
                    break;
                }
                case 198: {
                    n6 = 197;
                    break;
                }
                case 199: {
                    n6 = 12;
                    break;
                }
                case 200: {
                    n6 = 82;
                    break;
                }
                case 201: {
                    n6 = 214;
                    break;
                }
                case 202: {
                    n6 = 180;
                    break;
                }
                case 203: {
                    n6 = 86;
                    break;
                }
                case 204: {
                    n6 = 151;
                    break;
                }
                case 205: {
                    n6 = 93;
                    break;
                }
                case 206: {
                    n6 = 30;
                    break;
                }
                case 207: {
                    n6 = 192;
                    break;
                }
                case 208: {
                    n6 = 213;
                    break;
                }
                case 209: {
                    n6 = 210;
                    break;
                }
                case 210: {
                    n6 = 35;
                    break;
                }
                case 211: {
                    n6 = 96;
                    break;
                }
                case 212: {
                    n6 = 112;
                    break;
                }
                case 213: {
                    n6 = 194;
                    break;
                }
                case 214: {
                    n6 = 129;
                    break;
                }
                case 215: {
                    n6 = 254;
                    break;
                }
                case 216: {
                    n6 = 185;
                    break;
                }
                case 217: {
                    n6 = 155;
                    break;
                }
                case 218: {
                    n6 = 229;
                    break;
                }
                case 219: {
                    n6 = 51;
                    break;
                }
                case 220: {
                    n6 = 220;
                    break;
                }
                case 221: {
                    n6 = 207;
                    break;
                }
                case 222: {
                    n6 = 90;
                    break;
                }
                case 223: {
                    n6 = 241;
                    break;
                }
                case 224: {
                    n6 = 136;
                    break;
                }
                case 225: {
                    n6 = 56;
                    break;
                }
                case 226: {
                    n6 = 186;
                    break;
                }
                case 227: {
                    n6 = 255;
                    break;
                }
                case 228: {
                    n6 = 130;
                    break;
                }
                case 229: {
                    n6 = 191;
                    break;
                }
                case 230: {
                    n6 = 144;
                    break;
                }
                case 231: {
                    n6 = 47;
                    break;
                }
                case 232: {
                    n6 = 174;
                    break;
                }
                case 233: {
                    n6 = 2;
                    break;
                }
                case 234: {
                    n6 = 6;
                    break;
                }
                case 235: {
                    n6 = 80;
                    break;
                }
                case 236: {
                    n6 = 170;
                    break;
                }
                case 237: {
                    n6 = 139;
                    break;
                }
                case 238: {
                    n6 = 152;
                    break;
                }
                case 239: {
                    n6 = 158;
                    break;
                }
                case 240: {
                    n6 = 84;
                    break;
                }
                case 241: {
                    n6 = 199;
                    break;
                }
                case 242: {
                    n6 = 238;
                    break;
                }
                case 243: {
                    n6 = 57;
                    break;
                }
                case 244: {
                    n6 = 25;
                    break;
                }
                case 245: {
                    n6 = 173;
                    break;
                }
                case 246: {
                    n6 = 150;
                    break;
                }
                case 247: {
                    n6 = 230;
                    break;
                }
                case 248: {
                    n6 = 226;
                    break;
                }
                case 249: {
                    n6 = 198;
                    break;
                }
                case 250: {
                    n6 = 100;
                    break;
                }
                case 251: {
                    n6 = 239;
                    break;
                }
                case 252: {
                    n6 = 83;
                    break;
                }
                case 253: {
                    n6 = 79;
                    break;
                }
                case 254: {
                    n6 = 87;
                    break;
                }
                default: {
                    n6 = 28;
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
            o.d[n4] = new String(cArray).intern();
        }
        return d[n4];
    }
}

