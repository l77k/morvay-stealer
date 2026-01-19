/*
 * Decompiled with CFR 0.152.
 */
package a.b.c.g;

import a.b.c.g.g;

class c {
    final byte[] a;
    final boolean b;

    c(byte[] byArray, boolean bl) {
        this.a = byArray;
        this.b = bl;
    }

    boolean a() {
        int n2;
        block13: {
            block11: {
                boolean bl;
                block12: {
                    byte[] byArray;
                    block10: {
                        bl = g.i();
                        try {
                            try {
                                byArray = this.a;
                                if (!bl) break block10;
                                if (byArray == null) break block11;
                            }
                            catch (RuntimeException runtimeException) {
                                throw c.a(runtimeException);
                            }
                            byArray = this.a;
                        }
                        catch (RuntimeException runtimeException) {
                            throw c.a(runtimeException);
                        }
                    }
                    try {
                        try {
                            n2 = byArray.length;
                            if (!bl) break block12;
                            if (n2 <= 0) break block11;
                        }
                        catch (RuntimeException runtimeException) {
                            throw c.a(runtimeException);
                        }
                        n2 = this.b ? 1 : 0;
                    }
                    catch (RuntimeException runtimeException) {
                        throw c.a(runtimeException);
                    }
                }
                try {
                    if (!bl) break block13;
                    if (n2 != 0) break block11;
                }
                catch (RuntimeException runtimeException) {
                    throw c.a(runtimeException);
                }
                n2 = 1;
                break block13;
            }
            n2 = 0;
        }
        return n2 != 0;
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

