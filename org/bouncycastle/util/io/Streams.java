/*
 * Decompiled with CFR 0.152.
 */
package org.bouncycastle.util.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.bouncycastle.util.io.StreamOverflowException;

public final class Streams {
    private static int BUFFER_SIZE = 4096;

    public static void drain(InputStream inputStream2) throws IOException {
        byte[] byArray = new byte[BUFFER_SIZE];
        while (inputStream2.read(byArray, 0, byArray.length) >= 0) {
        }
    }

    public static void pipeAll(InputStream inputStream2, OutputStream outputStream2) throws IOException {
        Streams.pipeAll(inputStream2, outputStream2, BUFFER_SIZE);
    }

    public static void pipeAll(InputStream inputStream2, OutputStream outputStream2, int n2) throws IOException {
        int n3;
        byte[] byArray = new byte[n2];
        while ((n3 = inputStream2.read(byArray, 0, byArray.length)) >= 0) {
            outputStream2.write(byArray, 0, n3);
        }
    }

    public static long pipeAllLimited(InputStream inputStream2, long l2, OutputStream outputStream2) throws IOException {
        int n2;
        long l3 = 0L;
        byte[] byArray = new byte[BUFFER_SIZE];
        while ((n2 = inputStream2.read(byArray, 0, byArray.length)) >= 0) {
            if (l2 - l3 < (long)n2) {
                throw new StreamOverflowException("Data Overflow");
            }
            l3 += (long)n2;
            outputStream2.write(byArray, 0, n2);
        }
        return l3;
    }

    public static byte[] readAll(InputStream inputStream2) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Streams.pipeAll(inputStream2, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static byte[] readAllLimited(InputStream inputStream2, int n2) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Streams.pipeAllLimited(inputStream2, n2, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static int readFully(InputStream inputStream2, byte[] byArray) throws IOException {
        return Streams.readFully(inputStream2, byArray, 0, byArray.length);
    }

    public static int readFully(InputStream inputStream2, byte[] byArray, int n2, int n3) throws IOException {
        int n4;
        int n5;
        for (n4 = 0; n4 < n3 && (n5 = inputStream2.read(byArray, n2 + n4, n3 - n4)) >= 0; n4 += n5) {
        }
        return n4;
    }

    public static void validateBufferArguments(byte[] byArray, int n2, int n3) {
        if (byArray == null) {
            throw new NullPointerException();
        }
        int n4 = byArray.length - n2;
        int n5 = n4 - n3;
        if ((n2 | n3 | n4 | n5) < 0) {
            throw new IndexOutOfBoundsException();
        }
    }

    public static void writeBufTo(ByteArrayOutputStream byteArrayOutputStream, OutputStream outputStream2) throws IOException {
        byteArrayOutputStream.writeTo(outputStream2);
    }
}

