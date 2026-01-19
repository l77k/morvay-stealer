/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.SourceDebugExtension
 */
package okio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.RandomAccess;
import kotlin.Metadata;
import kotlin.collections.AbstractList;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import okio.Buffer;
import okio.ByteString;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={2, 1, 0}, k=1, xi=48, d1={"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\u0015\n\u0002\b\b\n\u0002\u0010\b\n\u0002\b\u0006\u0018\u0000 \u00162\b\u0012\u0004\u0012\u00020\u00020\u00012\u00060\u0003j\u0002`\u0004:\u0001\u0016B!\b\u0002\u0012\u000e\u0010\u0005\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u00a2\u0006\u0004\b\t\u0010\nJ\u0011\u0010\u0014\u001a\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u0011H\u0096\u0002R\u001e\u0010\u0005\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00020\u0006X\u0080\u0004\u00a2\u0006\n\n\u0002\u0010\r\u001a\u0004\b\u000b\u0010\fR\u0014\u0010\u0007\u001a\u00020\bX\u0080\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0014\u0010\u0010\u001a\u00020\u00118VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0012\u0010\u0013\u00a8\u0006\u0017"}, d2={"Lokio/Options;", "Lkotlin/collections/AbstractList;", "Lokio/ByteString;", "Ljava/util/RandomAccess;", "Lkotlin/collections/RandomAccess;", "byteStrings", "", "trie", "", "<init>", "([Lokio/ByteString;[I)V", "getByteStrings$okio", "()[Lokio/ByteString;", "[Lokio/ByteString;", "getTrie$okio", "()[I", "size", "", "getSize", "()I", "get", "index", "Companion", "okio"})
public final class Options
extends AbstractList<ByteString>
implements RandomAccess {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private final ByteString[] byteStrings;
    @NotNull
    private final int[] trie;

    private Options(ByteString[] byteStrings, int[] trie) {
        this.byteStrings = byteStrings;
        this.trie = trie;
    }

    @NotNull
    public final ByteString[] getByteStrings$okio() {
        return this.byteStrings;
    }

    @NotNull
    public final int[] getTrie$okio() {
        return this.trie;
    }

    @Override
    public int getSize() {
        return this.byteStrings.length;
    }

    @Override
    @NotNull
    public ByteString get(int index) {
        return this.byteStrings[index];
    }

    @JvmStatic
    @NotNull
    public static final Options of(ByteString ... byteStrings) {
        return Companion.of(byteStrings);
    }

    public /* synthetic */ Options(ByteString[] byteStrings, int[] trie, DefaultConstructorMarker $constructor_marker) {
        this(byteStrings, trie);
    }

    @Metadata(mv={2, 1, 0}, k=1, xi=48, d1={"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\u0010 \n\u0002\b\u0007\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003J!\u0010\u0004\u001a\u00020\u00052\u0012\u0010\u0006\u001a\n\u0012\u0006\b\u0001\u0012\u00020\b0\u0007\"\u00020\bH\u0007\u00a2\u0006\u0002\u0010\tJT\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\b\b\u0002\u0010\u0010\u001a\u00020\u00112\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u00122\b\b\u0002\u0010\u0013\u001a\u00020\u00112\b\b\u0002\u0010\u0014\u001a\u00020\u00112\f\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00110\u0012H\u0002R\u0018\u0010\u0016\u001a\u00020\r*\u00020\u000f8BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0017\u0010\u0018\u00a8\u0006\u0019"}, d2={"Lokio/Options$Companion;", "", "<init>", "()V", "of", "Lokio/Options;", "byteStrings", "", "Lokio/ByteString;", "([Lokio/ByteString;)Lokio/Options;", "buildTrieRecursive", "", "nodeOffset", "", "node", "Lokio/Buffer;", "byteStringOffset", "", "", "fromIndex", "toIndex", "indexes", "intCount", "getIntCount", "(Lokio/Buffer;)J", "okio"})
    @SourceDebugExtension(value={"SMAP\nOptions.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Options.kt\nokio/Options$Companion\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 3 _Arrays.kt\nkotlin/collections/ArraysKt___ArraysKt\n+ 4 Util.kt\nokio/-SegmentedByteString\n*L\n1#1,238:1\n1#2:239\n13467#3,3:240\n73#4:243\n73#4:244\n*S KotlinDebug\n*F\n+ 1 Options.kt\nokio/Options$Companion\n*L\n48#1:240,3\n153#1:243\n210#1:244\n*E\n"})
    public static final class Companion {
        private Companion() {
        }

        /*
         * WARNING - void declaration
         */
        @JvmStatic
        @NotNull
        public final Options of(ByteString ... byteStrings) {
            int n2;
            Intrinsics.checkNotNullParameter(byteStrings, "byteStrings");
            if (byteStrings.length == 0) {
                int[] nArray = new int[]{0, -1};
                return new Options(new ByteString[0], nArray, null);
            }
            List<ByteString> list = ArraysKt.toMutableList(byteStrings);
            CollectionsKt.sort(list);
            int n3 = list.size();
            ArrayList<Integer> arrayList = new ArrayList<Integer>(n3);
            int n4 = 0;
            while (n4 < n3) {
                int n5;
                n2 = n5 = n4++;
                ArrayList<Integer> arrayList2 = arrayList;
                boolean bl = false;
                arrayList2.add(-1);
            }
            List indexes = arrayList;
            ByteString[] $this$forEachIndexed$iv = byteStrings;
            boolean $i$f$forEachIndexed = false;
            int index$iv = 0;
            for (ByteString item$iv : $this$forEachIndexed$iv) {
                void byteString;
                int n6 = index$iv++;
                ByteString byteString2 = item$iv;
                int callerIndex = n6;
                boolean bl = false;
                int sortedIndex = CollectionsKt.binarySearch$default(list, (Comparable)byteString, 0, 0, 6, null);
                indexes.set(sortedIndex, callerIndex);
            }
            if (!(list.get(0).size() > 0)) {
                boolean $i$a$-require-Options$Companion$of$42 = false;
                String $i$a$-require-Options$Companion$of$42 = "the empty byte string is not a supported option";
                throw new IllegalArgumentException($i$a$-require-Options$Companion$of$42.toString());
            }
            for (int a2 = 0; a2 < list.size(); ++a2) {
                ByteString byteString;
                ByteString prefix = list.get(a2);
                int b2 = a2 + 1;
                while (b2 < list.size() && (byteString = list.get(b2)).startsWith(prefix)) {
                    if (!(byteString.size() != prefix.size())) {
                        boolean bl = false;
                        String string = "duplicate option: " + byteString;
                        throw new IllegalArgumentException(string.toString());
                    }
                    if (((Number)indexes.get(b2)).intValue() > ((Number)indexes.get(a2)).intValue()) {
                        list.remove(b2);
                        ((Number)indexes.remove(b2)).intValue();
                        continue;
                    }
                    ++b2;
                }
            }
            Buffer trieBytes = new Buffer();
            okio.Options$Companion.buildTrieRecursive$default(this, 0L, trieBytes, 0, list, 0, 0, indexes, 53, null);
            int n7 = 0;
            n2 = (int)this.getIntCount(trieBytes);
            int[] nArray = new int[n2];
            while (n7 < n2) {
                int n8 = n7++;
                nArray[n8] = trieBytes.readInt();
            }
            int[] trie = nArray;
            ByteString[] byteStringArray = Arrays.copyOf(byteStrings, byteStrings.length);
            Intrinsics.checkNotNullExpressionValue(byteStringArray, "copyOf(...)");
            return new Options(byteStringArray, trie, null);
        }

        /*
         * WARNING - void declaration
         */
        private final void buildTrieRecursive(long nodeOffset, Buffer node, int byteStringOffset, List<? extends ByteString> byteStrings, int fromIndex, int toIndex, List<Integer> indexes) {
            Object object;
            if (!(fromIndex < toIndex)) {
                String string = "Failed requirement.";
                throw new IllegalArgumentException(string.toString());
            }
            for (int i2 = fromIndex; i2 < toIndex; ++i2) {
                if (byteStrings.get(i2).size() >= byteStringOffset) continue;
                String string = "Failed requirement.";
                throw new IllegalArgumentException(string.toString());
            }
            int fromIndex2 = fromIndex;
            ByteString from = byteStrings.get(fromIndex2);
            ByteString to = byteStrings.get(toIndex - 1);
            int prefixIndex = -1;
            if (byteStringOffset == from.size()) {
                prefixIndex = ((Number)indexes.get(fromIndex2)).intValue();
                from = byteStrings.get(++fromIndex2);
            }
            if (from.getByte(byteStringOffset) != to.getByte(byteStringOffset)) {
                int selectChoiceCount = 1;
                for (int i3 = fromIndex2 + 1; i3 < toIndex; ++i3) {
                    if (byteStrings.get(i3 - 1).getByte(byteStringOffset) == byteStrings.get(i3).getByte(byteStringOffset)) continue;
                    ++selectChoiceCount;
                }
                long childNodesOffset = nodeOffset + this.getIntCount(node) + (long)2 + (long)(selectChoiceCount * 2);
                node.writeInt(selectChoiceCount);
                node.writeInt(prefixIndex);
                for (int i4 = fromIndex2; i4 < toIndex; ++i4) {
                    void $this$and$iv;
                    byte rangeByte = byteStrings.get(i4).getByte(byteStringOffset);
                    if (i4 != fromIndex2 && rangeByte == byteStrings.get(i4 - 1).getByte(byteStringOffset)) continue;
                    byte by = rangeByte;
                    int other$iv = 255;
                    boolean $i$f$and = false;
                    node.writeInt($this$and$iv & other$iv);
                }
                Buffer childNodes = new Buffer();
                int rangeStart = fromIndex2;
                while (rangeStart < toIndex) {
                    byte rangeByte = byteStrings.get(rangeStart).getByte(byteStringOffset);
                    int rangeEnd = toIndex;
                    for (int i5 = rangeStart + 1; i5 < toIndex; ++i5) {
                        if (rangeByte == byteStrings.get(i5).getByte(byteStringOffset)) continue;
                        rangeEnd = i5;
                        break;
                    }
                    if (rangeStart + 1 == rangeEnd && byteStringOffset + 1 == byteStrings.get(rangeStart).size()) {
                        node.writeInt(((Number)indexes.get(rangeStart)).intValue());
                    } else {
                        node.writeInt(-1 * (int)(childNodesOffset + this.getIntCount(childNodes)));
                        this.buildTrieRecursive(childNodesOffset, childNodes, byteStringOffset + 1, byteStrings, rangeStart, rangeEnd, indexes);
                    }
                    rangeStart = rangeEnd;
                }
                object = node.writeAll(childNodes);
            } else {
                int scanByteCount = 0;
                int n2 = Math.min(from.size(), to.size());
                for (int i6 = byteStringOffset; i6 < n2 && from.getByte(i6) == to.getByte(i6); ++i6) {
                    ++scanByteCount;
                }
                long childNodesOffset = nodeOffset + this.getIntCount(node) + (long)2 + (long)scanByteCount + 1L;
                node.writeInt(-scanByteCount);
                node.writeInt(prefixIndex);
                int n3 = byteStringOffset + scanByteCount;
                for (int i7 = byteStringOffset; i7 < n3; ++i7) {
                    void $this$and$iv;
                    byte rangeByte = from.getByte(i7);
                    int other$iv = 255;
                    boolean $i$f$and = false;
                    node.writeInt($this$and$iv & other$iv);
                }
                if (fromIndex2 + 1 == toIndex) {
                    if (!(byteStringOffset + scanByteCount == byteStrings.get(fromIndex2).size())) {
                        throw new IllegalStateException("Check failed.");
                    }
                    object = node.writeInt(((Number)indexes.get(fromIndex2)).intValue());
                } else {
                    Buffer childNodes = new Buffer();
                    node.writeInt(-1 * (int)(childNodesOffset + this.getIntCount(childNodes)));
                    this.buildTrieRecursive(childNodesOffset, childNodes, byteStringOffset + scanByteCount, byteStrings, fromIndex2, toIndex, indexes);
                    object = node.writeAll(childNodes);
                }
            }
        }

        static /* synthetic */ void buildTrieRecursive$default(Companion companion, long l2, Buffer buffer, int n2, List list, int n3, int n4, List list2, int n5, Object object) {
            if ((n5 & 1) != 0) {
                l2 = 0L;
            }
            if ((n5 & 4) != 0) {
                n2 = 0;
            }
            if ((n5 & 0x10) != 0) {
                n3 = 0;
            }
            if ((n5 & 0x20) != 0) {
                n4 = list.size();
            }
            companion.buildTrieRecursive(l2, buffer, n2, list, n3, n4, list2);
        }

        private final long getIntCount(Buffer $this$intCount) {
            return $this$intCount.size() / (long)4;
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

