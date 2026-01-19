/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.pqc.legacy.crypto.qtesla;

public class QTESLASecurityCategory {
    public static final int PROVABLY_SECURE_I = 5;
    public static final int PROVABLY_SECURE_III = 6;

    private QTESLASecurityCategory() {
    }

    static void validate(int n2) {
        switch (n2) {
            case 5: 
            case 6: {
                break;
            }
            default: {
                throw new IllegalArgumentException("unknown security category: " + n2);
            }
        }
    }

    static int getPrivateSize(int n2) {
        switch (n2) {
            case 5: {
                return 5224;
            }
            case 6: {
                return 12392;
            }
        }
        throw new IllegalArgumentException("unknown security category: " + n2);
    }

    static int getPublicSize(int n2) {
        switch (n2) {
            case 5: {
                return 14880;
            }
            case 6: {
                return 38432;
            }
        }
        throw new IllegalArgumentException("unknown security category: " + n2);
    }

    static int getSignatureSize(int n2) {
        switch (n2) {
            case 5: {
                return 2592;
            }
            case 6: {
                return 5664;
            }
        }
        throw new IllegalArgumentException("unknown security category: " + n2);
    }

    public static String getName(int n2) {
        switch (n2) {
            case 5: {
                return "qTESLA-p-I";
            }
            case 6: {
                return "qTESLA-p-III";
            }
        }
        throw new IllegalArgumentException("unknown security category: " + n2);
    }
}

