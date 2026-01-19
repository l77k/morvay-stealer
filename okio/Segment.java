/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.SourceDebugExtension
 */
package okio;

import java.util.Arrays;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.jvm.JvmField;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import okio.SegmentPool;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={2, 1, 0}, k=1, xi=48, d1={"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\f\n\u0002\u0010\u0002\n\u0002\b\u0004\b\u0000\u0018\u0000 \u001a2\u00020\u0001:\u0001\u001aB\t\b\u0016\u00a2\u0006\u0004\b\u0002\u0010\u0003B1\b\u0016\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\u0007\u0012\u0006\u0010\t\u001a\u00020\n\u0012\u0006\u0010\u000b\u001a\u00020\n\u00a2\u0006\u0004\b\u0002\u0010\fJ\u0006\u0010\u000f\u001a\u00020\u0000J\u0006\u0010\u0010\u001a\u00020\u0000J\b\u0010\u0011\u001a\u0004\u0018\u00010\u0000J\u000e\u0010\u0012\u001a\u00020\u00002\u0006\u0010\u0013\u001a\u00020\u0000J\u000e\u0010\u0014\u001a\u00020\u00002\u0006\u0010\u0015\u001a\u00020\u0007J\u0006\u0010\u0016\u001a\u00020\u0017J\u0016\u0010\u0018\u001a\u00020\u00172\u0006\u0010\u0019\u001a\u00020\u00002\u0006\u0010\u0015\u001a\u00020\u0007R\u0010\u0010\u0004\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\u0002\n\u0000R\u0012\u0010\u0006\u001a\u00020\u00078\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0002\n\u0000R\u0012\u0010\b\u001a\u00020\u00078\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0002\n\u0000R\u0012\u0010\t\u001a\u00020\n8\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0002\n\u0000R\u0012\u0010\u000b\u001a\u00020\n8\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\r\u001a\u0004\u0018\u00010\u00008\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000e\u001a\u0004\u0018\u00010\u00008\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001b"}, d2={"Lokio/Segment;", "", "<init>", "()V", "data", "", "pos", "", "limit", "shared", "", "owner", "([BIIZZ)V", "next", "prev", "sharedCopy", "unsharedCopy", "pop", "push", "segment", "split", "byteCount", "compact", "", "writeTo", "sink", "Companion", "okio"})
@SourceDebugExtension(value={"SMAP\nSegment.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Segment.kt\nokio/Segment\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,187:1\n1#2:188\n*E\n"})
public final class Segment {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @JvmField
    @NotNull
    public final byte[] data;
    @JvmField
    public int pos;
    @JvmField
    public int limit;
    @JvmField
    public boolean shared;
    @JvmField
    public boolean owner;
    @JvmField
    @Nullable
    public Segment next;
    @JvmField
    @Nullable
    public Segment prev;
    public static final int SIZE = 8192;
    public static final int SHARE_MINIMUM = 1024;

    public Segment() {
        this.data = new byte[8192];
        this.owner = true;
        this.shared = false;
    }

    public Segment(@NotNull byte[] data, int pos, int limit, boolean shared, boolean owner) {
        Intrinsics.checkNotNullParameter(data, "data");
        this.data = data;
        this.pos = pos;
        this.limit = limit;
        this.shared = shared;
        this.owner = owner;
    }

    @NotNull
    public final Segment sharedCopy() {
        this.shared = true;
        return new Segment(this.data, this.pos, this.limit, true, false);
    }

    @NotNull
    public final Segment unsharedCopy() {
        byte[] byArray = Arrays.copyOf(this.data, this.data.length);
        Intrinsics.checkNotNullExpressionValue(byArray, "copyOf(...)");
        return new Segment(byArray, this.pos, this.limit, false, true);
    }

    @Nullable
    public final Segment pop() {
        Segment result = this.next != this ? this.next : null;
        Intrinsics.checkNotNull(this.prev);
        this.prev.next = this.next;
        Intrinsics.checkNotNull(this.next);
        this.next.prev = this.prev;
        this.next = null;
        this.prev = null;
        return result;
    }

    @NotNull
    public final Segment push(@NotNull Segment segment) {
        Intrinsics.checkNotNullParameter(segment, "segment");
        segment.prev = this;
        segment.next = this.next;
        Intrinsics.checkNotNull(this.next);
        this.next.prev = segment;
        this.next = segment;
        return segment;
    }

    @NotNull
    public final Segment split(int byteCount) {
        if (!(byteCount > 0 && byteCount <= this.limit - this.pos)) {
            boolean bl = false;
            String string = "byteCount out of range";
            throw new IllegalArgumentException(string.toString());
        }
        Segment prefix = null;
        if (byteCount >= 1024) {
            prefix = this.sharedCopy();
        } else {
            prefix = SegmentPool.take();
            ArraysKt.copyInto$default(this.data, prefix.data, 0, this.pos, this.pos + byteCount, 2, null);
        }
        prefix.limit = prefix.pos + byteCount;
        this.pos += byteCount;
        Segment segment = this.prev;
        Intrinsics.checkNotNull(segment);
        segment.push(prefix);
        return prefix;
    }

    public final void compact() {
        int n2;
        if (!(this.prev != this)) {
            boolean $i$a$-check-Segment$compact$22 = false;
            String $i$a$-check-Segment$compact$22 = "cannot compact";
            throw new IllegalStateException($i$a$-check-Segment$compact$22.toString());
        }
        Segment segment = this.prev;
        Intrinsics.checkNotNull(segment);
        if (!segment.owner) {
            return;
        }
        int byteCount = this.limit - this.pos;
        Segment segment2 = this.prev;
        Intrinsics.checkNotNull(segment2);
        int n3 = 8192 - segment2.limit;
        Segment segment3 = this.prev;
        Intrinsics.checkNotNull(segment3);
        if (segment3.shared) {
            n2 = 0;
        } else {
            Segment segment4 = this.prev;
            Intrinsics.checkNotNull(segment4);
            n2 = segment4.pos;
        }
        int availableByteCount = n3 + n2;
        if (byteCount > availableByteCount) {
            return;
        }
        Segment segment5 = this.prev;
        Intrinsics.checkNotNull(segment5);
        this.writeTo(segment5, byteCount);
        this.pop();
        SegmentPool.recycle(this);
    }

    public final void writeTo(@NotNull Segment sink2, int byteCount) {
        Intrinsics.checkNotNullParameter(sink2, "sink");
        if (!sink2.owner) {
            boolean bl = false;
            String string = "only owner can write";
            throw new IllegalStateException(string.toString());
        }
        if (sink2.limit + byteCount > 8192) {
            if (sink2.shared) {
                throw new IllegalArgumentException();
            }
            if (sink2.limit + byteCount - sink2.pos > 8192) {
                throw new IllegalArgumentException();
            }
            ArraysKt.copyInto$default(sink2.data, sink2.data, 0, sink2.pos, sink2.limit, 2, null);
            sink2.limit -= sink2.pos;
            sink2.pos = 0;
        }
        ArraysKt.copyInto(this.data, sink2.data, sink2.limit, this.pos, this.pos + byteCount);
        sink2.limit += byteCount;
        this.pos += byteCount;
    }

    @Metadata(mv={2, 1, 0}, k=1, xi=48, d1={"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0007"}, d2={"Lokio/Segment$Companion;", "", "<init>", "()V", "SIZE", "", "SHARE_MINIMUM", "okio"})
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

