/*
 * Decompiled with CFR 0.152.
 */
package a.b.c.g;

import a.b.c.g.h;
import com.sun.jna.Native;
import com.sun.jna.win32.W32APIOptions;

class i {
    private static final h a;
    private static final long[] b;
    private static final Integer[] c;

    private i() {
    }

    private static h a() {
        try {
            char[] cArray = new char[i.a(9388, 8183162023122319483L)];
            cArray[0] = i.a(14418, 4988772850608350359L);
            cArray[1] = i.a(13635, 872397786991503771L);
            cArray[2] = i.a(14884, 631411180611839712L);
            cArray[3] = i.a(8932, 1518919397469470256L);
            cArray[4] = i.a(10338, 2237353166477669541L);
            cArray[5] = i.a(23045, 5991342194929321690L);
            char[] cArray2 = cArray;
            String string = new String(cArray2);
            h h2 = Native.load(string, h.class, W32APIOptions.UNICODE_OPTIONS);
            return h2;
        }
        catch (UnsatisfiedLinkError unsatisfiedLinkError) {
            try {
                char[] cArray = new char[i.a(8412, 3766084893581491212L)];
                cArray[0] = i.a(12707, 2972861349556825442L);
                cArray[1] = i.a(13183, 7820913343361888173L);
                cArray[2] = i.a(31520, 3200573668936771580L);
                cArray[3] = i.a(11476, 2837875555882938377L);
                cArray[4] = i.a(25994, 7339361474935748944L);
                cArray[5] = i.a(14744, 9018761534581548353L);
                cArray[i.a((int)15994, (long)6112121082090595004L)] = i.a(12021, 5300452638211093028L);
                cArray[i.a((int)4741, (long)5973446144680824414L)] = i.a(3706, 9189103325305606831L);
                cArray[i.a((int)10893, (long)2865990867690665563L)] = i.a(25801, 4201701065749821466L);
                cArray[i.a((int)10037, (long)8635004648790965237L)] = i.a(30238, 8856614574233610944L);
                char[] cArray3 = cArray;
                String string = new String(cArray3);
                return Native.load(string, h.class);
            }
            catch (Exception exception) {
                throw new RuntimeException();
            }
        }
    }

    static h b() {
        return a;
    }

    /*
     * Unable to fully structure code
     */
    static {
        block8: {
            block7: {
                var0 = 3827530933991732254L;
                var6_1 = new long[22];
                var3_2 = 0;
                var4_3 = "\u00b7\u00e2\t\u008f\"V\u00e6\u00d5D\u00ba\u00b5\u0007\u000f\u00fbJW\u00ce\u00d5|\u00c5\u0000\u00c9Lc`j\u00f3\u00bc\u0099RBMR\u00f6\u008c\u00ee\u009e?\u00c6\u00146\u00d1\u00b0\u009b$\u0086\u00c8\u0018\u008a\u008b}\u00a8c\u0000\u008ch\u00cb=\u0098\u00e8EW\u00db\u00d0q?\u0084m\u00beO\u00c4n\u001e\u008d):\u0091\u0014\u0093\u0090\u00d34\u001f\u00a3o\u00f72\u00b0s\u00fc'\u00c1\u001e\u00d8\u009e\u00b2\u00be\u00cd\u00df\u00ef\u00f9\u00d5\u00d1+\u00bb\u00fd\bf\u00b3\u0012]\u00e6?\u001fp\bE\u00c5\u00faG=wy\u00fe\u00c0\u00de\r>\u008e\u00f5\u00d7=.\u00fa\u00d0\u00e7Y\u0092[\u00d2`\u00f1R\u008c\u00c5\u00c5\u00bd[#\u009a@\u00cb\u00ee\u00fb\u0087\u0011UJ\u00d6\u00a4";
                var5_4 = "\u00b7\u00e2\t\u008f\"V\u00e6\u00d5D\u00ba\u00b5\u0007\u000f\u00fbJW\u00ce\u00d5|\u00c5\u0000\u00c9Lc`j\u00f3\u00bc\u0099RBMR\u00f6\u008c\u00ee\u009e?\u00c6\u00146\u00d1\u00b0\u009b$\u0086\u00c8\u0018\u008a\u008b}\u00a8c\u0000\u008ch\u00cb=\u0098\u00e8EW\u00db\u00d0q?\u0084m\u00beO\u00c4n\u001e\u008d):\u0091\u0014\u0093\u0090\u00d34\u001f\u00a3o\u00f72\u00b0s\u00fc'\u00c1\u001e\u00d8\u009e\u00b2\u00be\u00cd\u00df\u00ef\u00f9\u00d5\u00d1+\u00bb\u00fd\bf\u00b3\u0012]\u00e6?\u001fp\bE\u00c5\u00faG=wy\u00fe\u00c0\u00de\r>\u008e\u00f5\u00d7=.\u00fa\u00d0\u00e7Y\u0092[\u00d2`\u00f1R\u008c\u00c5\u00c5\u00bd[#\u009a@\u00cb\u00ee\u00fb\u0087\u0011UJ\u00d6\u00a4".length();
                var2_5 = 0;
                while (true) {
                    var7_6 = var4_3.substring(var2_5, var2_5 += 8).getBytes("ISO-8859-1");
                    v0 = var6_1;
                    v1 = var3_2++;
                    v2 = ((long)var7_6[0] & 255L) << 56 | ((long)var7_6[1] & 255L) << 48 | ((long)var7_6[2] & 255L) << 40 | ((long)var7_6[3] & 255L) << 32 | ((long)var7_6[4] & 255L) << 24 | ((long)var7_6[5] & 255L) << 16 | ((long)var7_6[6] & 255L) << 8 | (long)var7_6[7] & 255L;
                    v3 = -1;
                    break block7;
                    break;
                }
lbl14:
                // 1 sources

                while (true) {
                    v0[v1] = v4;
                    if (var2_5 < var5_4) ** continue;
                    var4_3 = "\u00e6\u0018]Bu\u00b7\u00d9\u0012\u0013\u00a7\u00c4d\u00a2mO\u00e2";
                    var5_4 = "\u00e6\u0018]Bu\u00b7\u00d9\u0012\u0013\u00a7\u00c4d\u00a2mO\u00e2".length();
                    var2_5 = 0;
                    while (true) {
                        var7_6 = var4_3.substring(var2_5, var2_5 += 8).getBytes("ISO-8859-1");
                        v0 = var6_1;
                        v1 = var3_2++;
                        v2 = ((long)var7_6[0] & 255L) << 56 | ((long)var7_6[1] & 255L) << 48 | ((long)var7_6[2] & 255L) << 40 | ((long)var7_6[3] & 255L) << 32 | ((long)var7_6[4] & 255L) << 24 | ((long)var7_6[5] & 255L) << 16 | ((long)var7_6[6] & 255L) << 8 | (long)var7_6[7] & 255L;
                        v3 = 0;
                        break block7;
                        break;
                    }
                    break;
                }
lbl27:
                // 1 sources

                while (true) {
                    v0[v1] = v4;
                    if (var2_5 < var5_4) ** continue;
                    break block8;
                    break;
                }
            }
            v4 = v2 ^ var0;
            switch (v3) {
                default: {
                    ** continue;
                }
                ** case 0:
lbl38:
                // 1 sources

                ** continue;
            }
        }
        i.b = var6_1;
        i.c = new Integer[22];
        i.a = i.a();
    }

    private static int a(int n2, long l2) {
        int n3 = n2 ^ (int)(l2 & 0x7FFFL) ^ 0x1CD5;
        if (c[n3] == null) {
            i.c[n3] = (int)(b[n3] ^ l2);
        }
        return c[n3];
    }
}

