/*
 * Decompiled with CFR 0.152.
 */
package a.b.c.g;

import a.b.c.g.i;
import com.sun.jna.Library;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.ptr.PointerByReference;

interface h
extends Library {
    public static final h INSTANCE = h.getInstance();

    public static h getInstance() {
        return i.b();
    }

    public int NCryptOpenStorageProvider(PointerByReference var1, String var2, int var3);

    public int NCryptOpenKey(Pointer var1, PointerByReference var2, String var3, int var4, int var5);

    public int NCryptDecrypt(Pointer var1, byte[] var2, int var3, Pointer var4, byte[] var5, int var6, WinDef.DWORDByReference var7, int var8);

    public int NCryptFreeObject(Pointer var1);
}

