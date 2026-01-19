/*
 * Decompiled with CFR 0.152.
 */
package a.b.c.g;

import java.util.List;

class aj {
    List<String> a;
    int b;
    String c;
    List<aj> d;

    aj(List<String> list, int n2) {
        this.a = list;
        this.b = n2;
    }

    aj(List<String> list, int n2, String string) {
        this(list, n2);
        this.c = string;
    }

    public static aj multi(List<aj> list, int n2) {
        aj aj2 = new aj(null, n2);
        aj2.d = list;
        return aj2;
    }
}

