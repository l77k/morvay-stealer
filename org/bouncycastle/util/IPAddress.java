/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.util;

public class IPAddress {
    public static boolean isValid(String string) {
        return IPAddress.isValidIPv4(string) || IPAddress.isValidIPv6(string);
    }

    public static boolean isValidWithNetMask(String string) {
        return IPAddress.isValidIPv4WithNetmask(string) || IPAddress.isValidIPv6WithNetmask(string);
    }

    public static boolean isValidIPv4(String string) {
        int n2 = string.length();
        if (n2 < 7 || n2 > 15) {
            return false;
        }
        int n3 = 0;
        for (int i2 = 0; i2 < 3; ++i2) {
            int n4 = string.indexOf(46, n3);
            if (!IPAddress.isParseableIPv4Octet(string, n3, n4)) {
                return false;
            }
            n3 = n4 + 1;
        }
        return IPAddress.isParseableIPv4Octet(string, n3, n2);
    }

    public static boolean isValidIPv4WithNetmask(String string) {
        int n2 = string.indexOf("/");
        if (n2 < 1) {
            return false;
        }
        String string2 = string.substring(0, n2);
        String string3 = string.substring(n2 + 1);
        return IPAddress.isValidIPv4(string2) && (IPAddress.isValidIPv4(string3) || IPAddress.isParseableIPv4Mask(string3));
    }

    public static boolean isValidIPv6(String string) {
        int n2;
        if (string.length() == 0) {
            return false;
        }
        char c2 = string.charAt(0);
        if (c2 != ':' && Character.digit(c2, 16) < 0) {
            return false;
        }
        int n3 = 0;
        String string2 = string + ":";
        boolean bl = false;
        int n4 = 0;
        while (n4 < string2.length() && (n2 = string2.indexOf(58, n4)) >= n4) {
            if (n3 == 8) {
                return false;
            }
            if (n4 != n2) {
                String string3 = string2.substring(n4, n2);
                if (n2 == string2.length() - 1 && string3.indexOf(46) > 0) {
                    if (++n3 == 8) {
                        return false;
                    }
                    if (!IPAddress.isValidIPv4(string3)) {
                        return false;
                    }
                } else if (!IPAddress.isParseableIPv6Segment(string2, n4, n2)) {
                    return false;
                }
            } else {
                if (n2 != 1 && n2 != string2.length() - 1 && bl) {
                    return false;
                }
                bl = true;
            }
            n4 = n2 + 1;
            ++n3;
        }
        return n3 == 8 || bl;
    }

    public static boolean isValidIPv6WithNetmask(String string) {
        int n2 = string.indexOf("/");
        if (n2 < 1) {
            return false;
        }
        String string2 = string.substring(0, n2);
        String string3 = string.substring(n2 + 1);
        return IPAddress.isValidIPv6(string2) && (IPAddress.isValidIPv6(string3) || IPAddress.isParseableIPv6Mask(string3));
    }

    private static boolean isParseableIPv4Mask(String string) {
        return IPAddress.isParseable(string, 0, string.length(), 10, 2, false, 0, 32);
    }

    private static boolean isParseableIPv4Octet(String string, int n2, int n3) {
        return IPAddress.isParseable(string, n2, n3, 10, 3, true, 0, 255);
    }

    private static boolean isParseableIPv6Mask(String string) {
        return IPAddress.isParseable(string, 0, string.length(), 10, 3, false, 1, 128);
    }

    private static boolean isParseableIPv6Segment(String string, int n2, int n3) {
        return IPAddress.isParseable(string, n2, n3, 16, 4, true, 0, 65535);
    }

    private static boolean isParseable(String string, int n2, int n3, int n4, int n5, boolean bl, int n6, int n7) {
        int n8 = n3 - n2;
        if (n8 < 1 | n8 > n5) {
            return false;
        }
        boolean bl2 = n8 > 1 & !bl;
        if (bl2 && Character.digit(string.charAt(n2), n4) <= 0) {
            return false;
        }
        int n9 = 0;
        while (n2 < n3) {
            char c2;
            int n10;
            if ((n10 = Character.digit(c2 = string.charAt(n2++), n4)) < 0) {
                return false;
            }
            n9 *= n4;
            n9 += n10;
        }
        return n9 >= n6 & n9 <= n7;
    }
}

