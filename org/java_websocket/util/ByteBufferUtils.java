/*
 * Decompiled with CFR 0.152.
 */
package org.java_websocket.util;

import java.nio.ByteBuffer;

public class ByteBufferUtils {
    private ByteBufferUtils() {
    }

    public static int transferByteBuffer(ByteBuffer source2, ByteBuffer dest) {
        int toremain;
        if (source2 == null || dest == null) {
            throw new IllegalArgumentException();
        }
        int fremain = source2.remaining();
        if (fremain > (toremain = dest.remaining())) {
            int limit = Math.min(fremain, toremain);
            source2.limit(limit);
            dest.put(source2);
            return limit;
        }
        dest.put(source2);
        return fremain;
    }

    public static ByteBuffer getEmptyByteBuffer() {
        return ByteBuffer.allocate(0);
    }
}

