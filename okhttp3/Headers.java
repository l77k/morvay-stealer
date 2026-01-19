/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
 */
package okhttp3;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.ReplaceWith;
import kotlin.TuplesKt;
import kotlin.collections.CollectionsKt;
import kotlin.internal.ProgressionUtilKt;
import kotlin.jvm.JvmName;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.ArrayIteratorKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.StringCompanionObject;
import kotlin.jvm.internal.markers.KMappedMarker;
import kotlin.text.StringsKt;
import okhttp3.internal.Util;
import okhttp3.internal.http.DatesKt;
import org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000f\n\u0002\u0018\u0002\n\u0002\u0010\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010(\n\u0002\b\u0002\n\u0002\u0010\"\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\u0010 \n\u0002\b\u0006\u0018\u0000 '2\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030\u00020\u0001:\u0002&'B\u0015\b\u0002\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00030\u0005\u00a2\u0006\u0002\u0010\u0006J\u0006\u0010\u000b\u001a\u00020\fJ\u0013\u0010\r\u001a\u00020\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u0096\u0002J\u0013\u0010\u0011\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u0012\u001a\u00020\u0003H\u0086\u0002J\u0010\u0010\u0013\u001a\u0004\u0018\u00010\u00142\u0006\u0010\u0012\u001a\u00020\u0003J\u0012\u0010\u0015\u001a\u0004\u0018\u00010\u00162\u0006\u0010\u0012\u001a\u00020\u0003H\u0007J\b\u0010\u0017\u001a\u00020\tH\u0016J\u001b\u0010\u0018\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030\u00020\u0019H\u0096\u0002J\u000e\u0010\u0012\u001a\u00020\u00032\u0006\u0010\u001a\u001a\u00020\tJ\f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u00030\u001cJ\u0006\u0010\u001d\u001a\u00020\u001eJ\r\u0010\b\u001a\u00020\tH\u0007\u00a2\u0006\u0002\b\u001fJ\u0018\u0010 \u001a\u0014\u0012\u0004\u0012\u00020\u0003\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00030\"0!J\b\u0010#\u001a\u00020\u0003H\u0016J\u000e\u0010$\u001a\u00020\u00032\u0006\u0010\u001a\u001a\u00020\tJ\u0014\u0010%\u001a\b\u0012\u0004\u0012\u00020\u00030\"2\u0006\u0010\u0012\u001a\u00020\u0003R\u0016\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00030\u0005X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0007R\u0011\u0010\b\u001a\u00020\t8G\u00a2\u0006\u0006\u001a\u0004\b\b\u0010\n\u00a8\u0006("}, d2={"Lokhttp3/Headers;", "", "Lkotlin/Pair;", "", "namesAndValues", "", "([Ljava/lang/String;)V", "[Ljava/lang/String;", "size", "", "()I", "byteCount", "", "equals", "", "other", "", "get", "name", "getDate", "Ljava/util/Date;", "getInstant", "Ljava/time/Instant;", "hashCode", "iterator", "", "index", "names", "", "newBuilder", "Lokhttp3/Headers$Builder;", "-deprecated_size", "toMultimap", "", "", "toString", "value", "values", "Builder", "Companion", "okhttp"})
public final class Headers
implements Iterable<Pair<? extends String, ? extends String>>,
KMappedMarker {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private final String[] namesAndValues;

    private Headers(String[] namesAndValues) {
        this.namesAndValues = namesAndValues;
    }

    @Nullable
    public final String get(@NotNull String name) {
        Intrinsics.checkNotNullParameter(name, "name");
        return Headers.Companion.get(this.namesAndValues, name);
    }

    @Nullable
    public final Date getDate(@NotNull String name) {
        Intrinsics.checkNotNullParameter(name, "name");
        String string = this.get(name);
        return string == null ? null : DatesKt.toHttpDateOrNull(string);
    }

    @IgnoreJRERequirement
    @Nullable
    public final Instant getInstant(@NotNull String name) {
        Date value;
        Intrinsics.checkNotNullParameter(name, "name");
        Date date = value = this.getDate(name);
        return date == null ? null : date.toInstant();
    }

    @JvmName(name="size")
    public final int size() {
        return this.namesAndValues.length / 2;
    }

    @Deprecated(message="moved to val", replaceWith=@ReplaceWith(expression="size", imports={}), level=DeprecationLevel.ERROR)
    @JvmName(name="-deprecated_size")
    public final int -deprecated_size() {
        return this.size();
    }

    @NotNull
    public final String name(int index) {
        return this.namesAndValues[index * 2];
    }

    @NotNull
    public final String value(int index) {
        return this.namesAndValues[index * 2 + 1];
    }

    @NotNull
    public final Set<String> names() {
        TreeSet<String> result = new TreeSet<String>(StringsKt.getCASE_INSENSITIVE_ORDER(StringCompanionObject.INSTANCE));
        int n2 = 0;
        int n3 = this.size();
        while (n2 < n3) {
            int i2 = n2++;
            result.add(this.name(i2));
        }
        Set<String> set = Collections.unmodifiableSet((Set)result);
        Intrinsics.checkNotNullExpressionValue(set, "unmodifiableSet(result)");
        return set;
    }

    @NotNull
    public final List<String> values(@NotNull String name) {
        List<String> list;
        Intrinsics.checkNotNullParameter(name, "name");
        List result = null;
        int n2 = 0;
        int n3 = this.size();
        while (n2 < n3) {
            int i2;
            if (!StringsKt.equals(name, this.name(i2 = n2++), true)) continue;
            if (result == null) {
                result = new ArrayList(2);
            }
            result.add(this.value(i2));
        }
        if (result != null) {
            List list2 = Collections.unmodifiableList(result);
            Intrinsics.checkNotNullExpressionValue(list2, "{\n      Collections.unmodifiableList(result)\n    }");
            list = list2;
        } else {
            list = CollectionsKt.emptyList();
        }
        return list;
    }

    public final long byteCount() {
        long result = this.namesAndValues.length * 2;
        int n2 = 0;
        int n3 = this.namesAndValues.length;
        while (n2 < n3) {
            int i2 = n2++;
            result += (long)this.namesAndValues[i2].length();
        }
        return result;
    }

    @Override
    @NotNull
    public Iterator<Pair<String, String>> iterator() {
        int n2 = 0;
        int n3 = this.size();
        Pair[] pairArray = new Pair[n3];
        while (n2 < n3) {
            int n4 = n2++;
            pairArray[n4] = TuplesKt.to(this.name(n4), this.value(n4));
        }
        return ArrayIteratorKt.iterator(pairArray);
    }

    @NotNull
    public final Builder newBuilder() {
        Builder result = new Builder();
        CollectionsKt.addAll((Collection)result.getNamesAndValues$okhttp(), this.namesAndValues);
        return result;
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof Headers && Arrays.equals(this.namesAndValues, ((Headers)other).namesAndValues);
    }

    public int hashCode() {
        return Arrays.hashCode(this.namesAndValues);
    }

    @NotNull
    public String toString() {
        StringBuilder stringBuilder;
        StringBuilder $this$toString_u24lambda_u2d0 = stringBuilder = new StringBuilder();
        boolean bl = false;
        int n2 = 0;
        int n3 = this.size();
        while (n2 < n3) {
            int i2 = n2++;
            String name = this.name(i2);
            String value = this.value(i2);
            $this$toString_u24lambda_u2d0.append(name);
            $this$toString_u24lambda_u2d0.append(": ");
            $this$toString_u24lambda_u2d0.append(Util.isSensitiveHeader(name) ? "\u2588\u2588" : value);
            $this$toString_u24lambda_u2d0.append("\n");
        }
        String string = stringBuilder.toString();
        Intrinsics.checkNotNullExpressionValue(string, "StringBuilder().apply(builderAction).toString()");
        return string;
    }

    @NotNull
    public final Map<String, List<String>> toMultimap() {
        TreeMap result = new TreeMap(StringsKt.getCASE_INSENSITIVE_ORDER(StringCompanionObject.INSTANCE));
        int n2 = 0;
        int n3 = this.size();
        while (n2 < n3) {
            String name;
            int i2 = n2++;
            String string = this.name(i2);
            Locale locale = Locale.US;
            Intrinsics.checkNotNullExpressionValue(locale, "US");
            Intrinsics.checkNotNullExpressionValue(string.toLowerCase(locale), "this as java.lang.String).toLowerCase(locale)");
            List values2 = (List)result.get(name);
            if (values2 == null) {
                values2 = new ArrayList(2);
                ((Map)result).put(name, values2);
            }
            values2.add(this.value(i2));
        }
        return result;
    }

    @JvmStatic
    @JvmName(name="of")
    @NotNull
    public static final Headers of(String ... namesAndValues) {
        return Companion.of(namesAndValues);
    }

    @JvmStatic
    @JvmName(name="of")
    @NotNull
    public static final Headers of(@NotNull Map<String, String> $this$of) {
        return Companion.of($this$of);
    }

    public /* synthetic */ Headers(String[] namesAndValues, DefaultConstructorMarker $constructor_marker) {
        this(namesAndValues);
    }

    @Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\b\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0005J\u0018\u0010\b\u001a\u00020\u00002\u0006\u0010\n\u001a\u00020\u00052\u0006\u0010\u000b\u001a\u00020\fH\u0007J\u0016\u0010\b\u001a\u00020\u00002\u0006\u0010\n\u001a\u00020\u00052\u0006\u0010\u000b\u001a\u00020\rJ\u0016\u0010\b\u001a\u00020\u00002\u0006\u0010\n\u001a\u00020\u00052\u0006\u0010\u000b\u001a\u00020\u0005J\u000e\u0010\u000e\u001a\u00020\u00002\u0006\u0010\u000f\u001a\u00020\u0010J\u0015\u0010\u0011\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0005H\u0000\u00a2\u0006\u0002\b\u0012J\u001d\u0010\u0011\u001a\u00020\u00002\u0006\u0010\n\u001a\u00020\u00052\u0006\u0010\u000b\u001a\u00020\u0005H\u0000\u00a2\u0006\u0002\b\u0012J\u0016\u0010\u0013\u001a\u00020\u00002\u0006\u0010\n\u001a\u00020\u00052\u0006\u0010\u000b\u001a\u00020\u0005J\u0006\u0010\u0014\u001a\u00020\u0010J\u0013\u0010\u0015\u001a\u0004\u0018\u00010\u00052\u0006\u0010\n\u001a\u00020\u0005H\u0086\u0002J\u000e\u0010\u0016\u001a\u00020\u00002\u0006\u0010\n\u001a\u00020\u0005J\u0019\u0010\u0017\u001a\u00020\u00002\u0006\u0010\n\u001a\u00020\u00052\u0006\u0010\u000b\u001a\u00020\fH\u0087\u0002J\u0019\u0010\u0017\u001a\u00020\u00002\u0006\u0010\n\u001a\u00020\u00052\u0006\u0010\u000b\u001a\u00020\rH\u0086\u0002J\u0019\u0010\u0017\u001a\u00020\u00002\u0006\u0010\n\u001a\u00020\u00052\u0006\u0010\u000b\u001a\u00020\u0005H\u0086\u0002R\u001a\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0080\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007\u00a8\u0006\u0018"}, d2={"Lokhttp3/Headers$Builder;", "", "()V", "namesAndValues", "", "", "getNamesAndValues$okhttp", "()Ljava/util/List;", "add", "line", "name", "value", "Ljava/time/Instant;", "Ljava/util/Date;", "addAll", "headers", "Lokhttp3/Headers;", "addLenient", "addLenient$okhttp", "addUnsafeNonAscii", "build", "get", "removeAll", "set", "okhttp"})
    public static final class Builder {
        @NotNull
        private final List<String> namesAndValues = new ArrayList(20);

        @NotNull
        public final List<String> getNamesAndValues$okhttp() {
            return this.namesAndValues;
        }

        @NotNull
        public final Builder addLenient$okhttp(@NotNull String line) {
            Builder builder;
            Intrinsics.checkNotNullParameter(line, "line");
            Builder $this$addLenient_u24lambda_u2d0 = builder = this;
            boolean bl = false;
            int index = StringsKt.indexOf$default((CharSequence)line, ':', 1, false, 4, null);
            if (index != -1) {
                String string = line;
                int n2 = 0;
                String string2 = string.substring(n2, index);
                Intrinsics.checkNotNullExpressionValue(string2, "this as java.lang.String\u2026ing(startIndex, endIndex)");
                string = line;
                n2 = index + 1;
                String string3 = string.substring(n2);
                Intrinsics.checkNotNullExpressionValue(string3, "this as java.lang.String).substring(startIndex)");
                $this$addLenient_u24lambda_u2d0.addLenient$okhttp(string2, string3);
            } else if (line.charAt(0) == ':') {
                String string = line;
                int n3 = 1;
                String string4 = string.substring(n3);
                Intrinsics.checkNotNullExpressionValue(string4, "this as java.lang.String).substring(startIndex)");
                $this$addLenient_u24lambda_u2d0.addLenient$okhttp("", string4);
            } else {
                $this$addLenient_u24lambda_u2d0.addLenient$okhttp("", line);
            }
            return builder;
        }

        @NotNull
        public final Builder add(@NotNull String line) {
            Builder builder;
            Intrinsics.checkNotNullParameter(line, "line");
            Builder $this$add_u24lambda_u2d2 = builder = this;
            boolean bl = false;
            int index = StringsKt.indexOf$default((CharSequence)line, ':', 0, false, 6, null);
            if (!(index != -1)) {
                boolean bl2 = false;
                String string = Intrinsics.stringPlus("Unexpected header: ", line);
                throw new IllegalArgumentException(string.toString());
            }
            String string = line;
            int n2 = 0;
            String string2 = string.substring(n2, index);
            Intrinsics.checkNotNullExpressionValue(string2, "this as java.lang.String\u2026ing(startIndex, endIndex)");
            String string3 = ((Object)StringsKt.trim((CharSequence)string2)).toString();
            string = line;
            n2 = index + 1;
            String string4 = string.substring(n2);
            Intrinsics.checkNotNullExpressionValue(string4, "this as java.lang.String).substring(startIndex)");
            $this$add_u24lambda_u2d2.add(string3, string4);
            return builder;
        }

        @NotNull
        public final Builder add(@NotNull String name, @NotNull String value) {
            Builder builder;
            Intrinsics.checkNotNullParameter(name, "name");
            Intrinsics.checkNotNullParameter(value, "value");
            Builder $this$add_u24lambda_u2d3 = builder = this;
            boolean bl = false;
            Companion.checkName(name);
            Companion.checkValue(value, name);
            $this$add_u24lambda_u2d3.addLenient$okhttp(name, value);
            return builder;
        }

        @NotNull
        public final Builder addUnsafeNonAscii(@NotNull String name, @NotNull String value) {
            Builder builder;
            Intrinsics.checkNotNullParameter(name, "name");
            Intrinsics.checkNotNullParameter(value, "value");
            Builder $this$addUnsafeNonAscii_u24lambda_u2d4 = builder = this;
            boolean bl = false;
            Companion.checkName(name);
            $this$addUnsafeNonAscii_u24lambda_u2d4.addLenient$okhttp(name, value);
            return builder;
        }

        @NotNull
        public final Builder addAll(@NotNull Headers headers) {
            Builder builder;
            Intrinsics.checkNotNullParameter(headers, "headers");
            Builder $this$addAll_u24lambda_u2d5 = builder = this;
            boolean bl = false;
            int n2 = 0;
            int n3 = headers.size();
            while (n2 < n3) {
                int i2 = n2++;
                $this$addAll_u24lambda_u2d5.addLenient$okhttp(headers.name(i2), headers.value(i2));
            }
            return builder;
        }

        @NotNull
        public final Builder add(@NotNull String name, @NotNull Date value) {
            Builder builder;
            Intrinsics.checkNotNullParameter(name, "name");
            Intrinsics.checkNotNullParameter(value, "value");
            Builder $this$add_u24lambda_u2d6 = builder = this;
            boolean bl = false;
            $this$add_u24lambda_u2d6.add(name, DatesKt.toHttpDateString(value));
            return builder;
        }

        @IgnoreJRERequirement
        @NotNull
        public final Builder add(@NotNull String name, @NotNull Instant value) {
            Builder builder;
            Intrinsics.checkNotNullParameter(name, "name");
            Intrinsics.checkNotNullParameter(value, "value");
            Builder $this$add_u24lambda_u2d7 = builder = this;
            boolean bl = false;
            $this$add_u24lambda_u2d7.add(name, new Date(value.toEpochMilli()));
            return builder;
        }

        @NotNull
        public final Builder set(@NotNull String name, @NotNull Date value) {
            Builder builder;
            Intrinsics.checkNotNullParameter(name, "name");
            Intrinsics.checkNotNullParameter(value, "value");
            Builder $this$set_u24lambda_u2d8 = builder = this;
            boolean bl = false;
            $this$set_u24lambda_u2d8.set(name, DatesKt.toHttpDateString(value));
            return builder;
        }

        @IgnoreJRERequirement
        @NotNull
        public final Builder set(@NotNull String name, @NotNull Instant value) {
            Intrinsics.checkNotNullParameter(name, "name");
            Intrinsics.checkNotNullParameter(value, "value");
            Builder $this$set_u24lambda_u2d9 = this;
            boolean bl = false;
            return $this$set_u24lambda_u2d9.set(name, new Date(value.toEpochMilli()));
        }

        @NotNull
        public final Builder addLenient$okhttp(@NotNull String name, @NotNull String value) {
            Builder builder;
            Intrinsics.checkNotNullParameter(name, "name");
            Intrinsics.checkNotNullParameter(value, "value");
            Builder $this$addLenient_u24lambda_u2d10 = builder = this;
            boolean bl = false;
            $this$addLenient_u24lambda_u2d10.getNamesAndValues$okhttp().add(name);
            $this$addLenient_u24lambda_u2d10.getNamesAndValues$okhttp().add(((Object)StringsKt.trim((CharSequence)value)).toString());
            return builder;
        }

        @NotNull
        public final Builder removeAll(@NotNull String name) {
            Builder builder;
            Intrinsics.checkNotNullParameter(name, "name");
            Builder $this$removeAll_u24lambda_u2d11 = builder = this;
            boolean bl = false;
            for (int i2 = 0; i2 < $this$removeAll_u24lambda_u2d11.getNamesAndValues$okhttp().size(); i2 += 2) {
                if (!StringsKt.equals(name, $this$removeAll_u24lambda_u2d11.getNamesAndValues$okhttp().get(i2), true)) continue;
                $this$removeAll_u24lambda_u2d11.getNamesAndValues$okhttp().remove(i2);
                $this$removeAll_u24lambda_u2d11.getNamesAndValues$okhttp().remove(i2);
                i2 -= 2;
            }
            return builder;
        }

        @NotNull
        public final Builder set(@NotNull String name, @NotNull String value) {
            Builder builder;
            Intrinsics.checkNotNullParameter(name, "name");
            Intrinsics.checkNotNullParameter(value, "value");
            Builder $this$set_u24lambda_u2d12 = builder = this;
            boolean bl = false;
            Companion.checkName(name);
            Companion.checkValue(value, name);
            $this$set_u24lambda_u2d12.removeAll(name);
            $this$set_u24lambda_u2d12.addLenient$okhttp(name, value);
            return builder;
        }

        @Nullable
        public final String get(@NotNull String name) {
            int n2;
            Intrinsics.checkNotNullParameter(name, "name");
            int n3 = n2 = this.namesAndValues.size() - 2;
            int n4 = ProgressionUtilKt.getProgressionLastElement(n2, 0, -2);
            if (n4 <= n3) {
                int i2;
                do {
                    i2 = n3;
                    n3 -= 2;
                    if (!StringsKt.equals(name, this.namesAndValues.get(i2), true)) continue;
                    return this.namesAndValues.get(i2 + 1);
                } while (i2 != n4);
            }
            return null;
        }

        @NotNull
        public final Headers build() {
            Collection $this$toTypedArray$iv = this.namesAndValues;
            boolean $i$f$toTypedArray = false;
            Collection thisCollection$iv = $this$toTypedArray$iv;
            String[] stringArray = thisCollection$iv.toArray(new String[0]);
            if (stringArray == null) {
                throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.toTypedArray>");
            }
            return new Headers(stringArray, null);
        }
    }

    /*
     * Illegal identifiers - consider using --renameillegalidents true
     */
    @Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010$\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0002J\u0018\u0010\u0007\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u0006H\u0002J%\u0010\t\u001a\u0004\u0018\u00010\u00062\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00060\u000b2\u0006\u0010\u0005\u001a\u00020\u0006H\u0002\u00a2\u0006\u0002\u0010\fJ#\u0010\r\u001a\u00020\u000e2\u0012\u0010\n\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00060\u000b\"\u00020\u0006H\u0007\u00a2\u0006\u0004\b\u000f\u0010\u0010J#\u0010\u000f\u001a\u00020\u000e2\u0012\u0010\n\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00060\u000b\"\u00020\u0006H\u0007\u00a2\u0006\u0004\b\u0011\u0010\u0010J!\u0010\u000f\u001a\u00020\u000e2\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00060\u0013H\u0007\u00a2\u0006\u0002\b\u0011J\u001d\u0010\u0014\u001a\u00020\u000e*\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00060\u0013H\u0007\u00a2\u0006\u0002\b\u000f\u00a8\u0006\u0015"}, d2={"Lokhttp3/Headers$Companion;", "", "()V", "checkName", "", "name", "", "checkValue", "value", "get", "namesAndValues", "", "([Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;", "headersOf", "Lokhttp3/Headers;", "of", "([Ljava/lang/String;)Lokhttp3/Headers;", "-deprecated_of", "headers", "", "toHeaders", "okhttp"})
    public static final class Companion {
        private Companion() {
        }

        private final String get(String[] namesAndValues, String name) {
            int n2;
            int n3 = n2 = namesAndValues.length - 2;
            int n4 = ProgressionUtilKt.getProgressionLastElement(n2, 0, -2);
            if (n4 <= n3) {
                int i2;
                do {
                    i2 = n3;
                    n3 -= 2;
                    if (!StringsKt.equals(name, namesAndValues[i2], true)) continue;
                    return namesAndValues[i2 + 1];
                } while (i2 != n4);
            }
            return null;
        }

        @JvmStatic
        @JvmName(name="of")
        @NotNull
        public final Headers of(String ... namesAndValues) {
            Intrinsics.checkNotNullParameter(namesAndValues, "namesAndValues");
            if (!(namesAndValues.length % 2 == 0)) {
                boolean bl = false;
                String string = "Expected alternating header names and values";
                throw new IllegalArgumentException(string.toString());
            }
            String[] namesAndValues2 = (String[])namesAndValues.clone();
            int n2 = 0;
            int n3 = namesAndValues2.length;
            while (n2 < n3) {
                int i2;
                if (!(namesAndValues2[i2 = n2++] != null)) {
                    boolean $i$a$-require-Headers$Companion$headersOf$32 = false;
                    String $i$a$-require-Headers$Companion$headersOf$32 = "Headers cannot be null";
                    throw new IllegalArgumentException($i$a$-require-Headers$Companion$headersOf$32.toString());
                }
                namesAndValues2[i2] = ((Object)StringsKt.trim((CharSequence)namesAndValues2[i2])).toString();
            }
            n3 = 0;
            n2 = namesAndValues2.length + -1;
            int n4 = ProgressionUtilKt.getProgressionLastElement(0, n2, 2);
            if (n3 <= n4) {
                int i3;
                do {
                    i3 = n3;
                    n3 += 2;
                    String name = namesAndValues2[i3];
                    String value = namesAndValues2[i3 + 1];
                    this.checkName(name);
                    this.checkValue(value, name);
                } while (i3 != n4);
            }
            return new Headers(namesAndValues2, null);
        }

        @Deprecated(message="function name changed", replaceWith=@ReplaceWith(expression="headersOf(*namesAndValues)", imports={}), level=DeprecationLevel.ERROR)
        @JvmName(name="-deprecated_of")
        @NotNull
        public final Headers -deprecated_of(String ... namesAndValues) {
            Intrinsics.checkNotNullParameter(namesAndValues, "namesAndValues");
            return this.of(Arrays.copyOf(namesAndValues, namesAndValues.length));
        }

        @JvmStatic
        @JvmName(name="of")
        @NotNull
        public final Headers of(@NotNull Map<String, String> $this$toHeaders) {
            Intrinsics.checkNotNullParameter($this$toHeaders, "<this>");
            String[] namesAndValues = new String[$this$toHeaders.size() * 2];
            int i2 = 0;
            for (Map.Entry<String, String> entry : $this$toHeaders.entrySet()) {
                String k2 = entry.getKey();
                String v2 = entry.getValue();
                String name = ((Object)StringsKt.trim((CharSequence)k2)).toString();
                String value = ((Object)StringsKt.trim((CharSequence)v2)).toString();
                this.checkName(name);
                this.checkValue(value, name);
                namesAndValues[i2] = name;
                namesAndValues[i2 + 1] = value;
                i2 += 2;
            }
            return new Headers(namesAndValues, null);
        }

        @Deprecated(message="function moved to extension", replaceWith=@ReplaceWith(expression="headers.toHeaders()", imports={}), level=DeprecationLevel.ERROR)
        @JvmName(name="-deprecated_of")
        @NotNull
        public final Headers -deprecated_of(@NotNull Map<String, String> headers) {
            Intrinsics.checkNotNullParameter(headers, "headers");
            return this.of(headers);
        }

        private final void checkName(String name) {
            if (!(((CharSequence)name).length() > 0)) {
                boolean bl = false;
                String string = "name is empty";
                throw new IllegalArgumentException(string.toString());
            }
            int n2 = 0;
            int n3 = name.length();
            while (n2 < n3) {
                int i2;
                char c2;
                if ('!' <= (c2 = name.charAt(i2 = n2++)) ? c2 < '\u007f' : false) continue;
                boolean bl = false;
                Object[] objectArray = new Object[]{(int)c2, i2, name};
                String string = Util.format("Unexpected char %#04x at %d in header name: %s", objectArray);
                throw new IllegalArgumentException(string.toString());
            }
        }

        /*
         * Enabled aggressive block sorting
         */
        private final void checkValue(String value, String name) {
            int i2;
            char c2;
            boolean bl;
            int n2 = 0;
            int n3 = value.length();
            do {
                block6: {
                    block5: {
                        if (n2 >= n3) {
                            return;
                        }
                        if ((c2 = value.charAt(i2 = n2++)) == '\t') break block5;
                        boolean bl2 = ' ' <= c2 ? c2 < '\u007f' : false;
                        if (!bl2) break block6;
                    }
                    bl = true;
                    continue;
                }
                bl = false;
            } while (bl);
            boolean bl3 = false;
            Object[] objectArray = new Object[]{(int)c2, i2, name};
            String string = Intrinsics.stringPlus(Util.format("Unexpected char %#04x at %d in %s value", objectArray), Util.isSensitiveHeader(name) ? "" : Intrinsics.stringPlus(": ", value));
            throw new IllegalArgumentException(string.toString());
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

