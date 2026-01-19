/*
 * Decompiled with CFR 0.152.
 */
package okio.internal;

import java.util.GregorianCalendar;
import kotlin.Metadata;

@Metadata(mv={2, 1, 0}, k=2, xi=48, d1={"\u0000\u0018\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0007\u001a8\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u00012\u0006\u0010\t\u001a\u00020\u00012\u0006\u0010\n\u001a\u00020\u00012\u0006\u0010\u000b\u001a\u00020\u00012\u0006\u0010\f\u001a\u00020\u00012\u0006\u0010\r\u001a\u00020\u0001H\u0000\"\u0014\u0010\u0000\u001a\u00020\u0001X\u0080D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\u0003*\f\b\u0000\u0010\u0004\"\u00020\u00052\u00020\u0005\u00a8\u0006\u000e"}, d2={"DEFAULT_COMPRESSION", "", "getDEFAULT_COMPRESSION", "()I", "CRC32", "Ljava/util/zip/CRC32;", "datePartsToEpochMillis", "", "year", "month", "day", "hour", "minute", "second", "okio"})
public final class _ZlibJvmKt {
    private static final int DEFAULT_COMPRESSION = -1;

    public static final int getDEFAULT_COMPRESSION() {
        return DEFAULT_COMPRESSION;
    }

    public static final long datePartsToEpochMillis(int year, int month, int day, int hour, int minute, int second) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(14, 0);
        calendar.set(year, month - 1, day, hour, minute, second);
        return calendar.getTime().getTime();
    }
}

