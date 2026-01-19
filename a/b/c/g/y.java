/*
 * Decompiled with CFR 0.152.
 */
package a.b.c.g;

import a.b.c.g.w;
import com.sun.jna.Native;
import com.sun.jna.win32.W32APIOptions;

class y {
    private static final w a;
    private static final long[] b;
    private static final Integer[] c;

    private y() {
    }

    private static w a() {
        try {
            char[] cArray = new char[y.a(24449, 2603203218010514989L)];
            cArray[0] = y.a(12879, 5637981529584061413L);
            cArray[1] = y.a(26370, 740866763360804512L);
            cArray[2] = y.a(8813, 6099892798240873422L);
            cArray[3] = y.a(11264, 1933070249125795232L);
            cArray[4] = y.a(19101, 4321051796957246268L);
            cArray[5] = y.a(19886, 8607456710727971862L);
            char[] cArray2 = cArray;
            String string = new String(cArray2);
            w w2 = Native.load(string, w.class, W32APIOptions.UNICODE_OPTIONS);
            return w2;
        }
        catch (UnsatisfiedLinkError unsatisfiedLinkError) {
            try {
                char[] cArray = new char[y.a(4883, 1966614901369203382L)];
                cArray[0] = y.a(24679, 1836858095285508556L);
                cArray[1] = y.a(21515, 1432212753650888111L);
                cArray[2] = y.a(27696, 3160353522066658697L);
                cArray[3] = y.a(23772, 3285701464485340530L);
                cArray[4] = y.a(1452, 5598783214139145233L);
                cArray[5] = y.a(1000, 8802535456149240391L);
                cArray[y.a((int)10871, (long)1749769538961888218L)] = y.a(15291, 5356843922077663772L);
                cArray[y.a((int)21475, (long)4352494174172490315L)] = y.a(21696, 2371501482414157161L);
                cArray[y.a((int)1979, (long)6458002298374192640L)] = y.a(7048, 8362182060083208750L);
                cArray[y.a((int)878, (long)4678990507213191892L)] = y.a(16261, 7492159057058317881L);
                char[] cArray3 = cArray;
                String string = new String(cArray3);
                return Native.load(string, w.class);
            }
            catch (Exception exception) {
                throw new RuntimeException();
            }
        }
    }

    static w b() {
        return a;
    }

    /*
     * Unable to fully structure code
     */
    static {
        block8: {
            block7: {
                var0 = 8307912899605181705L;
                var6_1 = new long[22];
                var3_2 = 0;
                var4_3 = "\u00d6\u00d4\u00b1\u0086\u00c7X\u001fEz\u00dd\u0098rFA\u0098\u0004q\u00e3Re\u00a4\u00c8~\u0082\u00d77/\u00c9u\u0091,\u00abk\u00ba\n\u0096\u001aq\u0093\"\u00efB\u00a0\u0097\u00d5Ef\u00d5tli\u00ee\u0001{\u0010\u0002\u00abp\u00e2y\u00dfTO:\u00e4(\u0097\u008f\u0080\u00de\u00e0\u00d0&\u00dd\u0096\u00be\u00f0i\u0006E\u00ed\u00f6\u0004\u00b3\u00b5\u00d9\u00ab\u00ca\u00c7\u0091\u0005\u00df\u00ba\u00c6n\u00b5\u00fa\u00d5\n.\u009d\u00ff\u0018\u00c5\u008e\u00cd\u00c7\u008e\u009b\u00c3\u00df\u00b5&\u00bb\u0098\u00f0\u00b3\u00bdWK>5\u009bR\u0081|w;\u00a7\u000f}a\u00fb\u00a4\u0001k\u00e1\u0016\u00c7]b\u00aa \u00f2\u0014{Z\u00f1\u00a4\u0007\u00cf\u00d4\u000e\u00c4yz\f\u00e5\u00cb\u0001";
                var5_4 = "\u00d6\u00d4\u00b1\u0086\u00c7X\u001fEz\u00dd\u0098rFA\u0098\u0004q\u00e3Re\u00a4\u00c8~\u0082\u00d77/\u00c9u\u0091,\u00abk\u00ba\n\u0096\u001aq\u0093\"\u00efB\u00a0\u0097\u00d5Ef\u00d5tli\u00ee\u0001{\u0010\u0002\u00abp\u00e2y\u00dfTO:\u00e4(\u0097\u008f\u0080\u00de\u00e0\u00d0&\u00dd\u0096\u00be\u00f0i\u0006E\u00ed\u00f6\u0004\u00b3\u00b5\u00d9\u00ab\u00ca\u00c7\u0091\u0005\u00df\u00ba\u00c6n\u00b5\u00fa\u00d5\n.\u009d\u00ff\u0018\u00c5\u008e\u00cd\u00c7\u008e\u009b\u00c3\u00df\u00b5&\u00bb\u0098\u00f0\u00b3\u00bdWK>5\u009bR\u0081|w;\u00a7\u000f}a\u00fb\u00a4\u0001k\u00e1\u0016\u00c7]b\u00aa \u00f2\u0014{Z\u00f1\u00a4\u0007\u00cf\u00d4\u000e\u00c4yz\f\u00e5\u00cb\u0001".length();
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
                    var4_3 = "C\u00b4\u00c0\u0012Iu\u00f3\\pJ\u00f0\u0019\u00b8\u00f4\u00c9h";
                    var5_4 = "C\u00b4\u00c0\u0012Iu\u00f3\\pJ\u00f0\u0019\u00b8\u00f4\u00c9h".length();
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
        y.b = var6_1;
        y.c = new Integer[22];
        y.a = y.a();
    }

    private static int a(int n2, long l2) {
        int n3 = n2 ^ (int)(l2 & 0x7FFFL) ^ 0x79A8;
        if (c[n3] == null) {
            y.c[n3] = (int)(b[n3] ^ l2);
        }
        return c[n3];
    }
}

