/*
 * Decompiled with CFR 0.152.
 */
package okio;

import java.util.concurrent.atomic.AtomicReference;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;
import okio.Segment;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={2, 1, 0}, k=1, xi=48, d1={"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0003\b\u00c0\u0002\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003J\b\u0010\u0011\u001a\u00020\tH\u0007J\u0010\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\tH\u0007J\u0010\u0010\u0015\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\t0\rH\u0002R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u000b\u001a\u0010\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\t0\r0\fX\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u000eR\u0011\u0010\u000f\u001a\u00020\u00058F\u00a2\u0006\u0006\u001a\u0004\b\u0010\u0010\u0007\u00a8\u0006\u0016"}, d2={"Lokio/SegmentPool;", "", "<init>", "()V", "MAX_SIZE", "", "getMAX_SIZE", "()I", "LOCK", "Lokio/Segment;", "HASH_BUCKET_COUNT", "hashBuckets", "", "Ljava/util/concurrent/atomic/AtomicReference;", "[Ljava/util/concurrent/atomic/AtomicReference;", "byteCount", "getByteCount", "take", "recycle", "", "segment", "firstRef", "okio"})
public final class SegmentPool {
    @NotNull
    public static final SegmentPool INSTANCE = new SegmentPool();
    private static final int MAX_SIZE = 65536;
    @NotNull
    private static final Segment LOCK = new Segment(new byte[0], 0, 0, false, false);
    private static final int HASH_BUCKET_COUNT = Integer.highestOneBit(Runtime.getRuntime().availableProcessors() * 2 - 1);
    @NotNull
    private static final AtomicReference<Segment>[] hashBuckets;

    private SegmentPool() {
    }

    public final int getMAX_SIZE() {
        return MAX_SIZE;
    }

    public final int getByteCount() {
        Segment segment = this.firstRef().get();
        if (segment == null) {
            return 0;
        }
        Segment first = segment;
        return first.limit;
    }

    @JvmStatic
    @NotNull
    public static final Segment take() {
        AtomicReference<Segment> firstRef = INSTANCE.firstRef();
        Segment first = firstRef.getAndSet(LOCK);
        if (first == LOCK) {
            return new Segment();
        }
        if (first == null) {
            firstRef.set(null);
            return new Segment();
        }
        firstRef.set(first.next);
        first.next = null;
        first.limit = 0;
        return first;
    }

    @JvmStatic
    public static final void recycle(@NotNull Segment segment) {
        Intrinsics.checkNotNullParameter(segment, "segment");
        if (!(segment.next == null && segment.prev == null)) {
            String string = "Failed requirement.";
            throw new IllegalArgumentException(string.toString());
        }
        if (segment.shared) {
            return;
        }
        AtomicReference<Segment> firstRef = INSTANCE.firstRef();
        Segment first = firstRef.getAndSet(LOCK);
        if (first == LOCK) {
            return;
        }
        Segment segment2 = first;
        int firstLimit = segment2 != null ? segment2.limit : 0;
        if (firstLimit >= MAX_SIZE) {
            firstRef.set(first);
            return;
        }
        segment.next = first;
        segment.pos = 0;
        segment.limit = firstLimit + 8192;
        firstRef.set(segment);
    }

    private final AtomicReference<Segment> firstRef() {
        int hashBucket = (int)(Thread.currentThread().getId() & (long)HASH_BUCKET_COUNT - 1L);
        return hashBuckets[hashBucket];
    }

    static {
        int n2 = 0;
        int n3 = HASH_BUCKET_COUNT;
        AtomicReference[] atomicReferenceArray = new AtomicReference[n3];
        while (n2 < n3) {
            int n4 = n2++;
            atomicReferenceArray[n4] = new AtomicReference();
        }
        hashBuckets = atomicReferenceArray;
    }
}

