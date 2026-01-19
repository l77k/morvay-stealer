/*
 * Decompiled with CFR 0.152.
 */
package a.b;

import a.b.c.d;

public class Main {
    private static int b;

    public static void main(String[] stringArray) {
        d.run();
    }

    public static void b(int n2) {
        b = n2;
    }

    public static int b() {
        return b;
    }

    public static int c() {
        int n2 = Main.b();
        try {
            if (n2 == 0) {
                return 87;
            }
        }
        catch (RuntimeException runtimeException) {
            throw Main.a(runtimeException);
        }
        return 0;
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }

    static {
        if (Main.b() != 0) {
            Main.b(66);
        }
    }
}

