/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Vector;
import org.bouncycastle.util.StringList;
import org.bouncycastle.util.encoders.UTF8;

public final class Strings {
    private static String LINE_SEPARATOR;

    public static String fromUTF8ByteArray(byte[] byArray) {
        char[] cArray = new char[byArray.length];
        int n2 = UTF8.transcodeToUTF16(byArray, cArray);
        if (n2 < 0) {
            throw new IllegalArgumentException("Invalid UTF-8 input");
        }
        return new String(cArray, 0, n2);
    }

    public static String fromUTF8ByteArray(byte[] byArray, int n2, int n3) {
        char[] cArray = new char[n3];
        int n4 = UTF8.transcodeToUTF16(byArray, n2, n3, cArray);
        if (n4 < 0) {
            throw new IllegalArgumentException("Invalid UTF-8 input");
        }
        return new String(cArray, 0, n4);
    }

    public static byte[] toUTF8ByteArray(String string) {
        return Strings.toUTF8ByteArray(string.toCharArray());
    }

    public static byte[] toUTF8ByteArray(char[] cArray) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            Strings.toUTF8ByteArray(cArray, byteArrayOutputStream);
        }
        catch (IOException iOException) {
            throw new IllegalStateException("cannot encode string to byte array!");
        }
        return byteArrayOutputStream.toByteArray();
    }

    public static void toUTF8ByteArray(char[] cArray, OutputStream outputStream2) throws IOException {
        char[] cArray2 = cArray;
        for (int i2 = 0; i2 < cArray2.length; ++i2) {
            char c2 = cArray2[i2];
            if (c2 < '\u0080') {
                outputStream2.write(c2);
                continue;
            }
            if (c2 < '\u0800') {
                outputStream2.write(0xC0 | c2 >> 6);
                outputStream2.write(0x80 | c2 & 0x3F);
                continue;
            }
            if (c2 >= '\ud800' && c2 <= '\udfff') {
                if (i2 + 1 >= cArray2.length) {
                    throw new IllegalStateException("invalid UTF-16 codepoint");
                }
                char c3 = c2;
                char c4 = c2 = cArray2[++i2];
                if (c3 > '\udbff') {
                    throw new IllegalStateException("invalid UTF-16 codepoint");
                }
                int n2 = ((c3 & 0x3FF) << 10 | c4 & 0x3FF) + 65536;
                outputStream2.write(0xF0 | n2 >> 18);
                outputStream2.write(0x80 | n2 >> 12 & 0x3F);
                outputStream2.write(0x80 | n2 >> 6 & 0x3F);
                outputStream2.write(0x80 | n2 & 0x3F);
                continue;
            }
            outputStream2.write(0xE0 | c2 >> 12);
            outputStream2.write(0x80 | c2 >> 6 & 0x3F);
            outputStream2.write(0x80 | c2 & 0x3F);
        }
    }

    public static String toUpperCase(String string) {
        boolean bl = false;
        char[] cArray = string.toCharArray();
        for (int i2 = 0; i2 != cArray.length; ++i2) {
            char c2 = cArray[i2];
            if ('a' > c2 || 'z' < c2) continue;
            bl = true;
            cArray[i2] = (char)(c2 - 97 + 65);
        }
        if (bl) {
            return new String(cArray);
        }
        return string;
    }

    public static String toLowerCase(String string) {
        boolean bl = false;
        char[] cArray = string.toCharArray();
        for (int i2 = 0; i2 != cArray.length; ++i2) {
            char c2 = cArray[i2];
            if ('A' > c2 || 'Z' < c2) continue;
            bl = true;
            cArray[i2] = (char)(c2 - 65 + 97);
        }
        if (bl) {
            return new String(cArray);
        }
        return string;
    }

    public static byte[] toByteArray(char[] cArray) {
        byte[] byArray = new byte[cArray.length];
        for (int i2 = 0; i2 != byArray.length; ++i2) {
            byArray[i2] = (byte)cArray[i2];
        }
        return byArray;
    }

    public static byte[] toByteArray(String string) {
        byte[] byArray = new byte[string.length()];
        for (int i2 = 0; i2 != byArray.length; ++i2) {
            char c2 = string.charAt(i2);
            byArray[i2] = (byte)c2;
        }
        return byArray;
    }

    public static int toByteArray(String string, byte[] byArray, int n2) {
        int n3 = string.length();
        for (int i2 = 0; i2 < n3; ++i2) {
            char c2 = string.charAt(i2);
            byArray[n2 + i2] = (byte)c2;
        }
        return n3;
    }

    public static boolean constantTimeAreEqual(String string, String string2) {
        boolean bl = string.length() == string2.length();
        int n2 = string.length();
        if (bl) {
            for (int i2 = 0; i2 != n2; ++i2) {
                bl &= string.charAt(i2) == string2.charAt(i2);
            }
        } else {
            for (int i3 = 0; i3 != n2; ++i3) {
                bl &= string.charAt(i3) == ' ';
            }
        }
        return bl;
    }

    public static String fromByteArray(byte[] byArray) {
        return new String(Strings.asCharArray(byArray));
    }

    public static char[] asCharArray(byte[] byArray) {
        char[] cArray = new char[byArray.length];
        for (int i2 = 0; i2 != cArray.length; ++i2) {
            cArray[i2] = (char)(byArray[i2] & 0xFF);
        }
        return cArray;
    }

    public static String[] split(String string, char c2) {
        Vector<String> vector = new Vector<String>();
        boolean bl = true;
        while (bl) {
            int n2 = string.indexOf(c2);
            if (n2 > 0) {
                String string2 = string.substring(0, n2);
                vector.addElement(string2);
                string = string.substring(n2 + 1);
                continue;
            }
            bl = false;
            vector.addElement(string);
        }
        String[] stringArray = new String[vector.size()];
        for (int i2 = 0; i2 != stringArray.length; ++i2) {
            stringArray[i2] = (String)vector.elementAt(i2);
        }
        return stringArray;
    }

    public static StringList newList() {
        return new StringListImpl();
    }

    public static String lineSeparator() {
        return LINE_SEPARATOR;
    }

    static {
        try {
            LINE_SEPARATOR = AccessController.doPrivileged(new PrivilegedAction<String>(){

                @Override
                public String run() {
                    return System.getProperty("line.separator");
                }
            });
        }
        catch (Exception exception) {
            try {
                LINE_SEPARATOR = String.format("%n", new Object[0]);
            }
            catch (Exception exception2) {
                LINE_SEPARATOR = "\n";
            }
        }
    }

    private static class StringListImpl
    extends ArrayList<String>
    implements StringList {
        private StringListImpl() {
        }

        @Override
        public boolean add(String string) {
            return super.add(string);
        }

        @Override
        public String set(int n2, String string) {
            return super.set(n2, string);
        }

        @Override
        public void add(int n2, String string) {
            super.add(n2, string);
        }

        @Override
        public String[] toStringArray() {
            String[] stringArray = new String[this.size()];
            for (int i2 = 0; i2 != stringArray.length; ++i2) {
                stringArray[i2] = (String)this.get(i2);
            }
            return stringArray;
        }

        @Override
        public String[] toStringArray(int n2, int n3) {
            String[] stringArray = new String[n3 - n2];
            for (int i2 = n2; i2 != this.size() && i2 != n3; ++i2) {
                stringArray[i2 - n2] = (String)this.get(i2);
            }
            return stringArray;
        }
    }
}

