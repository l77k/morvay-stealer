/*
 * Decompiled with CFR 0.152.
 */
package a.b.c.g;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.ptr.ByteByReference;

interface ab
extends Library {
    public static final ab INSTANCE;

    public int RtlAdjustPrivilege(int var1, boolean var2, boolean var3, ByteByReference var4);

    /*
     * Handled impossible loop by duplicating code
     * Enabled aggressive block sorting
     */
    static {
        char[] cArray;
        block12: {
            int n2;
            char[] cArray2;
            int n3;
            int n4;
            block11: {
                char[] cArray3 = "\u0011Ys'f".toCharArray();
                int n5 = cArray3.length;
                n4 = 0;
                n3 = 79;
                cArray2 = cArray3;
                n2 = n5;
                if (n5 <= 1) break block11;
                cArray = cArray2;
                n2 = n2;
                if (n2 <= n4) break block12;
            }
            do {
                int n6 = n3;
                cArray2 = cArray2;
                char[] cArray4 = cArray2;
                int n7 = n3;
                int n8 = n4;
                while (true) {
                    int n9;
                    switch (n4 % 7) {
                        case 0: {
                            n9 = 16;
                            break;
                        }
                        case 1: {
                            n9 = 98;
                            break;
                        }
                        case 2: {
                            n9 = 120;
                            break;
                        }
                        case 3: {
                            n9 = 4;
                            break;
                        }
                        case 4: {
                            n9 = 69;
                            break;
                        }
                        case 5: {
                            n9 = 57;
                            break;
                        }
                        default: {
                            n9 = 103;
                        }
                    }
                    cArray4[n8] = (char)(cArray4[n8] ^ (n7 ^ n9));
                    ++n4;
                    n3 = n6;
                    if (n6 != 0) break;
                    n6 = n3;
                    cArray2 = cArray2;
                    n8 = n3;
                    cArray4 = cArray2;
                    n7 = n3;
                }
                cArray = cArray2;
                n2 = n2;
            } while (n2 > n4);
        }
        String string = new String(cArray).intern();
        INSTANCE = Native.load(string, ab.class);
    }
}

