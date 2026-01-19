/*
 * Decompiled with CFR 0.152.
 */
package kotlin.collections;

import kotlin.ExperimentalUnsignedTypes;
import kotlin.Metadata;
import kotlin.UByteArray;
import kotlin.UIntArray;
import kotlin.ULongArray;
import kotlin.UShortArray;
import kotlin.UnsignedKt;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Metadata(mv={1, 6, 0}, k=2, xi=48, d1={"\u00000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0010\u001a*\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0006\u0010\u0007\u001a*\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b\t\u0010\n\u001a*\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u000b2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b\f\u0010\r\u001a*\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u000e2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u000f\u0010\u0010\u001a*\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0013\u0010\u0014\u001a*\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0015\u0010\u0016\u001a*\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u000b2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0017\u0010\u0018\u001a*\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u000e2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0019\u0010\u001a\u001a*\u0010\u001b\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u001c\u001a\u00020\u00012\u0006\u0010\u001d\u001a\u00020\u0001H\u0001\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u001e\u0010\u0014\u001a*\u0010\u001b\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\b2\u0006\u0010\u001c\u001a\u00020\u00012\u0006\u0010\u001d\u001a\u00020\u0001H\u0001\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u001f\u0010\u0016\u001a*\u0010\u001b\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u000b2\u0006\u0010\u001c\u001a\u00020\u00012\u0006\u0010\u001d\u001a\u00020\u0001H\u0001\u00f8\u0001\u0000\u00a2\u0006\u0004\b \u0010\u0018\u001a*\u0010\u001b\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u000e2\u0006\u0010\u001c\u001a\u00020\u00012\u0006\u0010\u001d\u001a\u00020\u0001H\u0001\u00f8\u0001\u0000\u00a2\u0006\u0004\b!\u0010\u001a\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\""}, d2={"partition", "", "array", "Lkotlin/UByteArray;", "left", "right", "partition-4UcCI2c", "([BII)I", "Lkotlin/UIntArray;", "partition-oBK06Vg", "([III)I", "Lkotlin/ULongArray;", "partition--nroSd4", "([JII)I", "Lkotlin/UShortArray;", "partition-Aa5vz7o", "([SII)I", "quickSort", "", "quickSort-4UcCI2c", "([BII)V", "quickSort-oBK06Vg", "([III)V", "quickSort--nroSd4", "([JII)V", "quickSort-Aa5vz7o", "([SII)V", "sortArray", "fromIndex", "toIndex", "sortArray-4UcCI2c", "sortArray-oBK06Vg", "sortArray--nroSd4", "sortArray-Aa5vz7o", "kotlin-stdlib"})
public final class UArraySortingKt {
    @ExperimentalUnsignedTypes
    private static final int partition-4UcCI2c(byte[] array, int left, int right) {
        int i2 = left;
        int j2 = right;
        byte pivot = UByteArray.get-w2LRezQ(array, (left + right) / 2);
        while (i2 <= j2) {
            while (Intrinsics.compare(UByteArray.get-w2LRezQ(array, i2) & 0xFF, pivot & 0xFF) < 0) {
                ++i2;
            }
            while (Intrinsics.compare(UByteArray.get-w2LRezQ(array, j2) & 0xFF, pivot & 0xFF) > 0) {
                --j2;
            }
            if (i2 > j2) continue;
            byte tmp = UByteArray.get-w2LRezQ(array, i2);
            UByteArray.set-VurrAj0(array, i2, UByteArray.get-w2LRezQ(array, j2));
            UByteArray.set-VurrAj0(array, j2, tmp);
            ++i2;
            --j2;
        }
        return i2;
    }

    @ExperimentalUnsignedTypes
    private static final void quickSort-4UcCI2c(byte[] array, int left, int right) {
        int index = UArraySortingKt.partition-4UcCI2c(array, left, right);
        if (left < index - 1) {
            UArraySortingKt.quickSort-4UcCI2c(array, left, index - 1);
        }
        if (index < right) {
            UArraySortingKt.quickSort-4UcCI2c(array, index, right);
        }
    }

    @ExperimentalUnsignedTypes
    private static final int partition-Aa5vz7o(short[] array, int left, int right) {
        int i2 = left;
        int j2 = right;
        short pivot = UShortArray.get-Mh2AYeg(array, (left + right) / 2);
        while (i2 <= j2) {
            while (Intrinsics.compare(UShortArray.get-Mh2AYeg(array, i2) & 0xFFFF, pivot & 0xFFFF) < 0) {
                ++i2;
            }
            while (Intrinsics.compare(UShortArray.get-Mh2AYeg(array, j2) & 0xFFFF, pivot & 0xFFFF) > 0) {
                --j2;
            }
            if (i2 > j2) continue;
            short tmp = UShortArray.get-Mh2AYeg(array, i2);
            UShortArray.set-01HTLdE(array, i2, UShortArray.get-Mh2AYeg(array, j2));
            UShortArray.set-01HTLdE(array, j2, tmp);
            ++i2;
            --j2;
        }
        return i2;
    }

    @ExperimentalUnsignedTypes
    private static final void quickSort-Aa5vz7o(short[] array, int left, int right) {
        int index = UArraySortingKt.partition-Aa5vz7o(array, left, right);
        if (left < index - 1) {
            UArraySortingKt.quickSort-Aa5vz7o(array, left, index - 1);
        }
        if (index < right) {
            UArraySortingKt.quickSort-Aa5vz7o(array, index, right);
        }
    }

    @ExperimentalUnsignedTypes
    private static final int partition-oBK06Vg(int[] array, int left, int right) {
        int i2 = left;
        int j2 = right;
        int pivot = UIntArray.get-pVg5ArA(array, (left + right) / 2);
        while (i2 <= j2) {
            while (UnsignedKt.uintCompare(UIntArray.get-pVg5ArA(array, i2), pivot) < 0) {
                ++i2;
            }
            while (UnsignedKt.uintCompare(UIntArray.get-pVg5ArA(array, j2), pivot) > 0) {
                --j2;
            }
            if (i2 > j2) continue;
            int tmp = UIntArray.get-pVg5ArA(array, i2);
            UIntArray.set-VXSXFK8(array, i2, UIntArray.get-pVg5ArA(array, j2));
            UIntArray.set-VXSXFK8(array, j2, tmp);
            ++i2;
            --j2;
        }
        return i2;
    }

    @ExperimentalUnsignedTypes
    private static final void quickSort-oBK06Vg(int[] array, int left, int right) {
        int index = UArraySortingKt.partition-oBK06Vg(array, left, right);
        if (left < index - 1) {
            UArraySortingKt.quickSort-oBK06Vg(array, left, index - 1);
        }
        if (index < right) {
            UArraySortingKt.quickSort-oBK06Vg(array, index, right);
        }
    }

    @ExperimentalUnsignedTypes
    private static final int partition--nroSd4(long[] array, int left, int right) {
        int i2 = left;
        int j2 = right;
        long pivot = ULongArray.get-s-VKNKU(array, (left + right) / 2);
        while (i2 <= j2) {
            while (UnsignedKt.ulongCompare(ULongArray.get-s-VKNKU(array, i2), pivot) < 0) {
                ++i2;
            }
            while (UnsignedKt.ulongCompare(ULongArray.get-s-VKNKU(array, j2), pivot) > 0) {
                --j2;
            }
            if (i2 > j2) continue;
            long tmp = ULongArray.get-s-VKNKU(array, i2);
            ULongArray.set-k8EXiF4(array, i2, ULongArray.get-s-VKNKU(array, j2));
            ULongArray.set-k8EXiF4(array, j2, tmp);
            ++i2;
            --j2;
        }
        return i2;
    }

    @ExperimentalUnsignedTypes
    private static final void quickSort--nroSd4(long[] array, int left, int right) {
        int index = UArraySortingKt.partition--nroSd4(array, left, right);
        if (left < index - 1) {
            UArraySortingKt.quickSort--nroSd4(array, left, index - 1);
        }
        if (index < right) {
            UArraySortingKt.quickSort--nroSd4(array, index, right);
        }
    }

    @ExperimentalUnsignedTypes
    public static final void sortArray-4UcCI2c(@NotNull byte[] array, int fromIndex, int toIndex) {
        Intrinsics.checkNotNullParameter(array, "array");
        UArraySortingKt.quickSort-4UcCI2c(array, fromIndex, toIndex - 1);
    }

    @ExperimentalUnsignedTypes
    public static final void sortArray-Aa5vz7o(@NotNull short[] array, int fromIndex, int toIndex) {
        Intrinsics.checkNotNullParameter(array, "array");
        UArraySortingKt.quickSort-Aa5vz7o(array, fromIndex, toIndex - 1);
    }

    @ExperimentalUnsignedTypes
    public static final void sortArray-oBK06Vg(@NotNull int[] array, int fromIndex, int toIndex) {
        Intrinsics.checkNotNullParameter(array, "array");
        UArraySortingKt.quickSort-oBK06Vg(array, fromIndex, toIndex - 1);
    }

    @ExperimentalUnsignedTypes
    public static final void sortArray--nroSd4(@NotNull long[] array, int fromIndex, int toIndex) {
        Intrinsics.checkNotNullParameter(array, "array");
        UArraySortingKt.quickSort--nroSd4(array, fromIndex, toIndex - 1);
    }
}

