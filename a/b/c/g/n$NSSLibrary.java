/*
 * Decompiled with CFR 0.152.
 */
package a.b.c.g;

import a.b.c.g.n$SECItem;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public interface n$NSSLibrary
extends Library {
    public static final n$NSSLibrary INSTANCE;
    public static final String[] a;
    public static final String[] b;

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static n$NSSLibrary loadNSS() {
        String string;
        String[] stringArray;
        ArrayList<String> arrayList;
        ArrayList<String> arrayList2;
        String string2 = null;
        String string3 = System.getenv(n$NSSLibrary.a(31225, -6975));
        String string4 = System.getenv(n$NSSLibrary.a(31231, 8374));
        String string5 = System.getenv(n$NSSLibrary.a(31229, -7291));
        String[] stringArray2 = new String[]{n$NSSLibrary.a(31230, -9112), n$NSSLibrary.a(31227, 26598), n$NSSLibrary.a(31228, -25936)};
        try {
            ArrayList<String> arrayList3;
            arrayList2 = arrayList3;
            arrayList = arrayList3;
            String[] stringArray3 = new String[3];
            stringArray3[0] = string3;
            stringArray3[1] = string4;
            String[] stringArray4 = stringArray3;
            stringArray = stringArray3;
            int n2 = 2;
            string = string5 != null ? Paths.get(string5, n$NSSLibrary.a(31226, -20327)).toString() : null;
        }
        catch (UnsatisfiedLinkError unsatisfiedLinkError) {
            throw n$NSSLibrary.a(unsatisfiedLinkError);
        }
        stringArray4[n2] = string;
        arrayList2(Arrays.asList(stringArray));
        ArrayList<String> arrayList4 = arrayList;
        arrayList4.removeIf(Objects::isNull);
        for (String string6 : arrayList4) {
            for (String string7 : stringArray2) {
                Path path = Paths.get(string6, string7);
                if (!Files.exists(path, new LinkOption[0])) continue;
                string2 = path.toString();
                break;
            }
            try {
                if (string2 == null) continue;
                break;
            }
            catch (UnsatisfiedLinkError unsatisfiedLinkError) {
                throw n$NSSLibrary.a(unsatisfiedLinkError);
            }
        }
        try {
            if (string2 == null) {
                try {
                    return Native.load(n$NSSLibrary.a(31224, 31223), n$NSSLibrary.class);
                }
                catch (UnsatisfiedLinkError unsatisfiedLinkError) {
                    // empty catch block
                }
            }
        }
        catch (RuntimeException runtimeException) {
            throw n$NSSLibrary.a(runtimeException);
        }
        try {
            if (string2 == null) return null;
            try {
                return Native.load(string2, n$NSSLibrary.class);
            }
            catch (UnsatisfiedLinkError unsatisfiedLinkError) {
                return null;
            }
        }
        catch (RuntimeException runtimeException) {
            throw n$NSSLibrary.a(runtimeException);
        }
    }

    public int NSS_Init(String var1);

    public int NSS_Shutdown();

    public int PK11SDR_Decrypt(n.SECItem var1, n.SECItem var2, Pointer var3);

    public void SECITEM_FreeItem(n.SECItem var1, int var2);

    /*
     * Unable to fully structure code
     */
    static {
        block20: {
            block19: {
                var5 = new String[8];
                var3_1 = 0;
                var2_2 = "\u00dc|\u00c8\u00e9\f\u00f0\u0080\u00a8\u0003\u008c@9`\u009dm\u00daV\b\u001a.\u00f5\u00d6'\u00faL\u0002\"\u00bf.\u0088\u00ac\"\u00f9o\u00fe\u00cd\u001dKfw\u0093\u001c\u0086\fq\u0091\u00a4Ok\u00d7{\u0098\u00d8\u0014F1\u00f5\u0096\u00c7<\u00b6\u0018\u00ec\u00c6\u00e2\u00b1oZ\u00c6\u008a\u00f2\u009f\u00f6\u00b6\u00eb&\u009dj\b\u00a1\u00b8\u00d0\u00ddH/\u000e\f\u0082PU\u00a5\u00a4V\u00f8\u0090\u0012\u008a\u0092\u0010";
                var4_3 = "\u00dc|\u00c8\u00e9\f\u00f0\u0080\u00a8\u0003\u008c@9`\u009dm\u00daV\b\u001a.\u00f5\u00d6'\u00faL\u0002\"\u00bf.\u0088\u00ac\"\u00f9o\u00fe\u00cd\u001dKfw\u0093\u001c\u0086\fq\u0091\u00a4Ok\u00d7{\u0098\u00d8\u0014F1\u00f5\u0096\u00c7<\u00b6\u0018\u00ec\u00c6\u00e2\u00b1oZ\u00c6\u008a\u00f2\u009f\u00f6\u00b6\u00eb&\u009dj\b\u00a1\u00b8\u00d0\u00ddH/\u000e\f\u0082PU\u00a5\u00a4V\u00f8\u0090\u0012\u008a\u0092\u0010".length();
                var1_4 = 4;
                var0_5 = -1;
lbl7:
                // 2 sources

                while (true) {
                    v0 = 36;
                    v1 = ++var0_5;
                    v2 = var2_2.substring(v1, v1 + var1_4);
                    v3 = -1;
                    break block19;
                    break;
                }
lbl13:
                // 1 sources

                while (true) {
                    var5[var3_1++] = v4.intern();
                    if ((var0_5 += var1_4) < var4_3) {
                        var1_4 = var2_2.charAt(var0_5);
                        ** continue;
                    }
                    var2_2 = "%D\u009e\f\u00c4D\u00c6\r\u00e7HofZ\u00b4\u00dd|\u0006D^K&\u001cw\u00a3\u0011C\u00c8\u00f9-\u0081\u00a2\u00bf\u001bj%\u0003xl\u00b4\u00dc\u00e3)";
                    var4_3 = "%D\u009e\f\u00c4D\u00c6\r\u00e7HofZ\u00b4\u00dd|\u0006D^K&\u001cw\u00a3\u0011C\u00c8\u00f9-\u0081\u00a2\u00bf\u001bj%\u0003xl\u00b4\u00dc\u00e3)".length();
                    var1_4 = 24;
                    var0_5 = -1;
lbl22:
                    // 2 sources

                    while (true) {
                        v0 = 78;
                        v5 = ++var0_5;
                        v2 = var2_2.substring(v5, v5 + var1_4);
                        v3 = 0;
                        break block19;
                        break;
                    }
                    break;
                }
lbl28:
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
                            v15 = 35;
                            break;
                        }
                        case 1: {
                            v15 = 28;
                            break;
                        }
                        case 2: {
                            v15 = 71;
                            break;
                        }
                        case 3: {
                            v15 = 107;
                            break;
                        }
                        case 4: {
                            v15 = 33;
                            break;
                        }
                        case 5: {
                            v15 = 7;
                            break;
                        }
                        default: {
                            v15 = 125;
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
        n$NSSLibrary.a = var5;
        n$NSSLibrary.b = new String[8];
        n$NSSLibrary.INSTANCE = n$NSSLibrary.loadNSS();
    }

    private static Throwable a(Throwable throwable) {
        return throwable;
    }

    private static String a(int n2, int n3) {
        int n4 = (n2 ^ 0x79F8) & 0xFFFF;
        if (b[n4] == null) {
            int n5;
            int n6;
            char[] cArray = a[n4].toCharArray();
            switch (cArray[0] & 0xFF) {
                case 0: {
                    n6 = 74;
                    break;
                }
                case 1: {
                    n6 = 81;
                    break;
                }
                case 2: {
                    n6 = 249;
                    break;
                }
                case 3: {
                    n6 = 143;
                    break;
                }
                case 4: {
                    n6 = 101;
                    break;
                }
                case 5: {
                    n6 = 100;
                    break;
                }
                case 6: {
                    n6 = 199;
                    break;
                }
                case 7: {
                    n6 = 150;
                    break;
                }
                case 8: {
                    n6 = 61;
                    break;
                }
                case 9: {
                    n6 = 189;
                    break;
                }
                case 10: {
                    n6 = 163;
                    break;
                }
                case 11: {
                    n6 = 86;
                    break;
                }
                case 12: {
                    n6 = 123;
                    break;
                }
                case 13: {
                    n6 = 181;
                    break;
                }
                case 14: {
                    n6 = 34;
                    break;
                }
                case 15: {
                    n6 = 126;
                    break;
                }
                case 16: {
                    n6 = 50;
                    break;
                }
                case 17: {
                    n6 = 138;
                    break;
                }
                case 18: {
                    n6 = 255;
                    break;
                }
                case 19: {
                    n6 = 35;
                    break;
                }
                case 20: {
                    n6 = 161;
                    break;
                }
                case 21: {
                    n6 = 168;
                    break;
                }
                case 22: {
                    n6 = 40;
                    break;
                }
                case 23: {
                    n6 = 153;
                    break;
                }
                case 24: {
                    n6 = 221;
                    break;
                }
                case 25: {
                    n6 = 231;
                    break;
                }
                case 26: {
                    n6 = 97;
                    break;
                }
                case 27: {
                    n6 = 25;
                    break;
                }
                case 28: {
                    n6 = 63;
                    break;
                }
                case 29: {
                    n6 = 76;
                    break;
                }
                case 30: {
                    n6 = 225;
                    break;
                }
                case 31: {
                    n6 = 216;
                    break;
                }
                case 32: {
                    n6 = 59;
                    break;
                }
                case 33: {
                    n6 = 38;
                    break;
                }
                case 34: {
                    n6 = 58;
                    break;
                }
                case 35: {
                    n6 = 202;
                    break;
                }
                case 36: {
                    n6 = 77;
                    break;
                }
                case 37: {
                    n6 = 23;
                    break;
                }
                case 38: {
                    n6 = 220;
                    break;
                }
                case 39: {
                    n6 = 172;
                    break;
                }
                case 40: {
                    n6 = 67;
                    break;
                }
                case 41: {
                    n6 = 230;
                    break;
                }
                case 42: {
                    n6 = 69;
                    break;
                }
                case 43: {
                    n6 = 131;
                    break;
                }
                case 44: {
                    n6 = 145;
                    break;
                }
                case 45: {
                    n6 = 224;
                    break;
                }
                case 46: {
                    n6 = 56;
                    break;
                }
                case 47: {
                    n6 = 52;
                    break;
                }
                case 48: {
                    n6 = 22;
                    break;
                }
                case 49: {
                    n6 = 28;
                    break;
                }
                case 50: {
                    n6 = 219;
                    break;
                }
                case 51: {
                    n6 = 242;
                    break;
                }
                case 52: {
                    n6 = 176;
                    break;
                }
                case 53: {
                    n6 = 175;
                    break;
                }
                case 54: {
                    n6 = 154;
                    break;
                }
                case 55: {
                    n6 = 158;
                    break;
                }
                case 56: {
                    n6 = 237;
                    break;
                }
                case 57: {
                    n6 = 57;
                    break;
                }
                case 58: {
                    n6 = 201;
                    break;
                }
                case 59: {
                    n6 = 48;
                    break;
                }
                case 60: {
                    n6 = 27;
                    break;
                }
                case 61: {
                    n6 = 240;
                    break;
                }
                case 62: {
                    n6 = 136;
                    break;
                }
                case 63: {
                    n6 = 37;
                    break;
                }
                case 64: {
                    n6 = 195;
                    break;
                }
                case 65: {
                    n6 = 214;
                    break;
                }
                case 66: {
                    n6 = 179;
                    break;
                }
                case 67: {
                    n6 = 108;
                    break;
                }
                case 68: {
                    n6 = 0;
                    break;
                }
                case 69: {
                    n6 = 60;
                    break;
                }
                case 70: {
                    n6 = 98;
                    break;
                }
                case 71: {
                    n6 = 251;
                    break;
                }
                case 72: {
                    n6 = 99;
                    break;
                }
                case 73: {
                    n6 = 17;
                    break;
                }
                case 74: {
                    n6 = 197;
                    break;
                }
                case 75: {
                    n6 = 144;
                    break;
                }
                case 76: {
                    n6 = 103;
                    break;
                }
                case 77: {
                    n6 = 226;
                    break;
                }
                case 78: {
                    n6 = 132;
                    break;
                }
                case 79: {
                    n6 = 113;
                    break;
                }
                case 80: {
                    n6 = 95;
                    break;
                }
                case 81: {
                    n6 = 49;
                    break;
                }
                case 82: {
                    n6 = 141;
                    break;
                }
                case 83: {
                    n6 = 119;
                    break;
                }
                case 84: {
                    n6 = 162;
                    break;
                }
                case 85: {
                    n6 = 208;
                    break;
                }
                case 86: {
                    n6 = 68;
                    break;
                }
                case 87: {
                    n6 = 65;
                    break;
                }
                case 88: {
                    n6 = 115;
                    break;
                }
                case 89: {
                    n6 = 244;
                    break;
                }
                case 90: {
                    n6 = 64;
                    break;
                }
                case 91: {
                    n6 = 252;
                    break;
                }
                case 92: {
                    n6 = 11;
                    break;
                }
                case 93: {
                    n6 = 116;
                    break;
                }
                case 94: {
                    n6 = 80;
                    break;
                }
                case 95: {
                    n6 = 243;
                    break;
                }
                case 96: {
                    n6 = 169;
                    break;
                }
                case 97: {
                    n6 = 209;
                    break;
                }
                case 98: {
                    n6 = 160;
                    break;
                }
                case 99: {
                    n6 = 9;
                    break;
                }
                case 100: {
                    n6 = 182;
                    break;
                }
                case 101: {
                    n6 = 207;
                    break;
                }
                case 102: {
                    n6 = 213;
                    break;
                }
                case 103: {
                    n6 = 46;
                    break;
                }
                case 104: {
                    n6 = 85;
                    break;
                }
                case 105: {
                    n6 = 120;
                    break;
                }
                case 106: {
                    n6 = 122;
                    break;
                }
                case 107: {
                    n6 = 222;
                    break;
                }
                case 108: {
                    n6 = 31;
                    break;
                }
                case 109: {
                    n6 = 210;
                    break;
                }
                case 110: {
                    n6 = 20;
                    break;
                }
                case 111: {
                    n6 = 73;
                    break;
                }
                case 112: {
                    n6 = 236;
                    break;
                }
                case 113: {
                    n6 = 193;
                    break;
                }
                case 114: {
                    n6 = 51;
                    break;
                }
                case 115: {
                    n6 = 12;
                    break;
                }
                case 116: {
                    n6 = 167;
                    break;
                }
                case 117: {
                    n6 = 102;
                    break;
                }
                case 118: {
                    n6 = 223;
                    break;
                }
                case 119: {
                    n6 = 134;
                    break;
                }
                case 120: {
                    n6 = 79;
                    break;
                }
                case 121: {
                    n6 = 4;
                    break;
                }
                case 122: {
                    n6 = 127;
                    break;
                }
                case 123: {
                    n6 = 112;
                    break;
                }
                case 124: {
                    n6 = 84;
                    break;
                }
                case 125: {
                    n6 = 44;
                    break;
                }
                case 126: {
                    n6 = 42;
                    break;
                }
                case 127: {
                    n6 = 107;
                    break;
                }
                case 128: {
                    n6 = 91;
                    break;
                }
                case 129: {
                    n6 = 16;
                    break;
                }
                case 130: {
                    n6 = 41;
                    break;
                }
                case 131: {
                    n6 = 194;
                    break;
                }
                case 132: {
                    n6 = 2;
                    break;
                }
                case 133: {
                    n6 = 188;
                    break;
                }
                case 134: {
                    n6 = 72;
                    break;
                }
                case 135: {
                    n6 = 45;
                    break;
                }
                case 136: {
                    n6 = 29;
                    break;
                }
                case 137: {
                    n6 = 148;
                    break;
                }
                case 138: {
                    n6 = 10;
                    break;
                }
                case 139: {
                    n6 = 106;
                    break;
                }
                case 140: {
                    n6 = 192;
                    break;
                }
                case 141: {
                    n6 = 129;
                    break;
                }
                case 142: {
                    n6 = 186;
                    break;
                }
                case 143: {
                    n6 = 151;
                    break;
                }
                case 144: {
                    n6 = 121;
                    break;
                }
                case 145: {
                    n6 = 198;
                    break;
                }
                case 146: {
                    n6 = 191;
                    break;
                }
                case 147: {
                    n6 = 117;
                    break;
                }
                case 148: {
                    n6 = 140;
                    break;
                }
                case 149: {
                    n6 = 7;
                    break;
                }
                case 150: {
                    n6 = 13;
                    break;
                }
                case 151: {
                    n6 = 183;
                    break;
                }
                case 152: {
                    n6 = 152;
                    break;
                }
                case 153: {
                    n6 = 184;
                    break;
                }
                case 154: {
                    n6 = 55;
                    break;
                }
                case 155: {
                    n6 = 87;
                    break;
                }
                case 156: {
                    n6 = 104;
                    break;
                }
                case 157: {
                    n6 = 218;
                    break;
                }
                case 158: {
                    n6 = 233;
                    break;
                }
                case 159: {
                    n6 = 78;
                    break;
                }
                case 160: {
                    n6 = 228;
                    break;
                }
                case 161: {
                    n6 = 229;
                    break;
                }
                case 162: {
                    n6 = 114;
                    break;
                }
                case 163: {
                    n6 = 187;
                    break;
                }
                case 164: {
                    n6 = 235;
                    break;
                }
                case 165: {
                    n6 = 111;
                    break;
                }
                case 166: {
                    n6 = 250;
                    break;
                }
                case 167: {
                    n6 = 171;
                    break;
                }
                case 168: {
                    n6 = 254;
                    break;
                }
                case 169: {
                    n6 = 109;
                    break;
                }
                case 170: {
                    n6 = 174;
                    break;
                }
                case 171: {
                    n6 = 178;
                    break;
                }
                case 172: {
                    n6 = 19;
                    break;
                }
                case 173: {
                    n6 = 82;
                    break;
                }
                case 174: {
                    n6 = 149;
                    break;
                }
                case 175: {
                    n6 = 190;
                    break;
                }
                case 176: {
                    n6 = 234;
                    break;
                }
                case 177: {
                    n6 = 118;
                    break;
                }
                case 178: {
                    n6 = 30;
                    break;
                }
                case 179: {
                    n6 = 157;
                    break;
                }
                case 180: {
                    n6 = 211;
                    break;
                }
                case 181: {
                    n6 = 71;
                    break;
                }
                case 182: {
                    n6 = 185;
                    break;
                }
                case 183: {
                    n6 = 146;
                    break;
                }
                case 184: {
                    n6 = 232;
                    break;
                }
                case 185: {
                    n6 = 142;
                    break;
                }
                case 186: {
                    n6 = 53;
                    break;
                }
                case 187: {
                    n6 = 39;
                    break;
                }
                case 188: {
                    n6 = 159;
                    break;
                }
                case 189: {
                    n6 = 94;
                    break;
                }
                case 190: {
                    n6 = 14;
                    break;
                }
                case 191: {
                    n6 = 93;
                    break;
                }
                case 192: {
                    n6 = 36;
                    break;
                }
                case 193: {
                    n6 = 47;
                    break;
                }
                case 194: {
                    n6 = 253;
                    break;
                }
                case 195: {
                    n6 = 133;
                    break;
                }
                case 196: {
                    n6 = 137;
                    break;
                }
                case 197: {
                    n6 = 75;
                    break;
                }
                case 198: {
                    n6 = 89;
                    break;
                }
                case 199: {
                    n6 = 8;
                    break;
                }
                case 200: {
                    n6 = 217;
                    break;
                }
                case 201: {
                    n6 = 125;
                    break;
                }
                case 202: {
                    n6 = 33;
                    break;
                }
                case 203: {
                    n6 = 173;
                    break;
                }
                case 204: {
                    n6 = 180;
                    break;
                }
                case 205: {
                    n6 = 5;
                    break;
                }
                case 206: {
                    n6 = 24;
                    break;
                }
                case 207: {
                    n6 = 62;
                    break;
                }
                case 208: {
                    n6 = 54;
                    break;
                }
                case 209: {
                    n6 = 206;
                    break;
                }
                case 210: {
                    n6 = 139;
                    break;
                }
                case 211: {
                    n6 = 18;
                    break;
                }
                case 212: {
                    n6 = 170;
                    break;
                }
                case 213: {
                    n6 = 245;
                    break;
                }
                case 214: {
                    n6 = 247;
                    break;
                }
                case 215: {
                    n6 = 1;
                    break;
                }
                case 216: {
                    n6 = 90;
                    break;
                }
                case 217: {
                    n6 = 124;
                    break;
                }
                case 218: {
                    n6 = 110;
                    break;
                }
                case 219: {
                    n6 = 66;
                    break;
                }
                case 220: {
                    n6 = 70;
                    break;
                }
                case 221: {
                    n6 = 21;
                    break;
                }
                case 222: {
                    n6 = 130;
                    break;
                }
                case 223: {
                    n6 = 203;
                    break;
                }
                case 224: {
                    n6 = 227;
                    break;
                }
                case 225: {
                    n6 = 83;
                    break;
                }
                case 226: {
                    n6 = 205;
                    break;
                }
                case 227: {
                    n6 = 248;
                    break;
                }
                case 228: {
                    n6 = 164;
                    break;
                }
                case 229: {
                    n6 = 43;
                    break;
                }
                case 230: {
                    n6 = 32;
                    break;
                }
                case 231: {
                    n6 = 6;
                    break;
                }
                case 232: {
                    n6 = 177;
                    break;
                }
                case 233: {
                    n6 = 212;
                    break;
                }
                case 234: {
                    n6 = 239;
                    break;
                }
                case 235: {
                    n6 = 3;
                    break;
                }
                case 236: {
                    n6 = 92;
                    break;
                }
                case 237: {
                    n6 = 15;
                    break;
                }
                case 238: {
                    n6 = 200;
                    break;
                }
                case 239: {
                    n6 = 128;
                    break;
                }
                case 240: {
                    n6 = 238;
                    break;
                }
                case 241: {
                    n6 = 147;
                    break;
                }
                case 242: {
                    n6 = 155;
                    break;
                }
                case 243: {
                    n6 = 246;
                    break;
                }
                case 244: {
                    n6 = 135;
                    break;
                }
                case 245: {
                    n6 = 204;
                    break;
                }
                case 246: {
                    n6 = 88;
                    break;
                }
                case 247: {
                    n6 = 26;
                    break;
                }
                case 248: {
                    n6 = 215;
                    break;
                }
                case 249: {
                    n6 = 196;
                    break;
                }
                case 250: {
                    n6 = 166;
                    break;
                }
                case 251: {
                    n6 = 165;
                    break;
                }
                case 252: {
                    n6 = 96;
                    break;
                }
                case 253: {
                    n6 = 156;
                    break;
                }
                case 254: {
                    n6 = 241;
                    break;
                }
                default: {
                    n6 = 105;
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
            n$NSSLibrary.b[n4] = new String(cArray).intern();
        }
        return b[n4];
    }
}

