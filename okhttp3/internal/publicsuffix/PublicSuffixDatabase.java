/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.publicsuffix;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.IDN;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.io.CloseableKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.SequencesKt;
import kotlin.text.StringsKt;
import okhttp3.internal.Util;
import okio.BufferedSource;
import okio.GzipSource;
import okio.Okio;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0005\u0018\u0000 \u00152\u00020\u0001:\u0001\u0015B\u0005\u00a2\u0006\u0002\u0010\u0002J\u001c\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000b2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\f0\u000bH\u0002J\u0010\u0010\u000e\u001a\u0004\u0018\u00010\f2\u0006\u0010\u000f\u001a\u00020\fJ\b\u0010\u0010\u001a\u00020\u0011H\u0002J\b\u0010\u0012\u001a\u00020\u0011H\u0002J\u0016\u0010\u0013\u001a\u00020\u00112\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u0006J\u0016\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\f0\u000b2\u0006\u0010\u000f\u001a\u00020\fH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0016"}, d2={"Lokhttp3/internal/publicsuffix/PublicSuffixDatabase;", "", "()V", "listRead", "Ljava/util/concurrent/atomic/AtomicBoolean;", "publicSuffixExceptionListBytes", "", "publicSuffixListBytes", "readCompleteLatch", "Ljava/util/concurrent/CountDownLatch;", "findMatchingRule", "", "", "domainLabels", "getEffectiveTldPlusOne", "domain", "readTheList", "", "readTheListUninterruptibly", "setListBytes", "splitDomain", "Companion", "okhttp"})
public final class PublicSuffixDatabase {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private final AtomicBoolean listRead = new AtomicBoolean(false);
    @NotNull
    private final CountDownLatch readCompleteLatch = new CountDownLatch(1);
    private byte[] publicSuffixListBytes;
    private byte[] publicSuffixExceptionListBytes;
    @NotNull
    public static final String PUBLIC_SUFFIX_RESOURCE = "publicsuffixes.gz";
    @NotNull
    private static final byte[] WILDCARD_LABEL;
    @NotNull
    private static final List<String> PREVAILING_RULE;
    private static final char EXCEPTION_MARKER = '!';
    @NotNull
    private static final PublicSuffixDatabase instance;

    @Nullable
    public final String getEffectiveTldPlusOne(@NotNull String domain) {
        Intrinsics.checkNotNullParameter(domain, "domain");
        String unicodeDomain = IDN.toUnicode(domain);
        Intrinsics.checkNotNullExpressionValue(unicodeDomain, "unicodeDomain");
        List<String> domainLabels = this.splitDomain(unicodeDomain);
        List<String> rule = this.findMatchingRule(domainLabels);
        if (domainLabels.size() == rule.size() && rule.get(0).charAt(0) != '!') {
            return null;
        }
        int firstLabelOffset = rule.get(0).charAt(0) == '!' ? domainLabels.size() - rule.size() : domainLabels.size() - (rule.size() + 1);
        return SequencesKt.joinToString$default(SequencesKt.drop(CollectionsKt.asSequence((Iterable)this.splitDomain(domain)), firstLabelOffset), ".", null, null, 0, null, null, 62, null);
    }

    private final List<String> splitDomain(String domain) {
        char[] cArray = new char[]{'.'};
        List domainLabels = StringsKt.split$default((CharSequence)domain, cArray, false, 0, 6, null);
        if (Intrinsics.areEqual(CollectionsKt.last(domainLabels), "")) {
            return CollectionsKt.dropLast(domainLabels, 1);
        }
        return domainLabels;
    }

    private final List<String> findMatchingRule(List<String> domainLabels) {
        List list;
        List list2;
        String rule;
        String rule22;
        int i22;
        if (!this.listRead.get() && this.listRead.compareAndSet(false, true)) {
            this.readTheListUninterruptibly();
        } else {
            try {
                this.readCompleteLatch.await();
            }
            catch (InterruptedException _) {
                Thread.currentThread().interrupt();
            }
        }
        if (!(this.publicSuffixListBytes != null)) {
            boolean $i$a$-check-PublicSuffixDatabase$findMatchingRule$32 = false;
            String $i$a$-check-PublicSuffixDatabase$findMatchingRule$32 = "Unable to load publicsuffixes.gz resource from the classpath.";
            throw new IllegalStateException($i$a$-check-PublicSuffixDatabase$findMatchingRule$32.toString());
        }
        int $i$a$-check-PublicSuffixDatabase$findMatchingRule$32 = 0;
        int n2 = domainLabels.size();
        byte[][] byArrayArray = new byte[n2][];
        while ($i$a$-check-PublicSuffixDatabase$findMatchingRule$32 < n2) {
            int n3 = $i$a$-check-PublicSuffixDatabase$findMatchingRule$32++;
            Charset charset = StandardCharsets.UTF_8;
            Intrinsics.checkNotNullExpressionValue(charset, "UTF_8");
            Intrinsics.checkNotNullExpressionValue(domainLabels.get(n3).getBytes(charset), "this as java.lang.String).getBytes(charset)");
        }
        byte[][] domainLabelsUtf8Bytes = byArrayArray;
        String exactMatch = null;
        n2 = 0;
        int n4 = ((Object[])domainLabelsUtf8Bytes).length;
        while (n2 < n4) {
            i22 = n2++;
            byte[] byArray = this.publicSuffixListBytes;
            if (this.publicSuffixListBytes == null) {
                Intrinsics.throwUninitializedPropertyAccessException("publicSuffixListBytes");
                byArray = null;
            }
            if ((rule22 = PublicSuffixDatabase.Companion.binarySearch(byArray, domainLabelsUtf8Bytes, i22)) == null) continue;
            exactMatch = rule22;
            break;
        }
        String wildcardMatch = null;
        if (((Object[])domainLabelsUtf8Bytes).length > 1) {
            byte[][] labelsWithWildcard = (byte[][])((Object[])domainLabelsUtf8Bytes).clone();
            i22 = 0;
            int rule22 = ((Object[])labelsWithWildcard).length - 1;
            while (i22 < rule22) {
                int labelIndex = i22++;
                labelsWithWildcard[labelIndex] = WILDCARD_LABEL;
                byte[] byArray = this.publicSuffixListBytes;
                if (this.publicSuffixListBytes == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("publicSuffixListBytes");
                    byArray = null;
                }
                if ((rule = PublicSuffixDatabase.Companion.binarySearch(byArray, labelsWithWildcard, labelIndex)) == null) continue;
                wildcardMatch = rule;
                break;
            }
        }
        String exception = null;
        if (wildcardMatch != null) {
            i22 = 0;
            int rule22 = ((Object[])domainLabelsUtf8Bytes).length - 1;
            while (i22 < rule22) {
                int labelIndex = i22++;
                byte[] byArray = this.publicSuffixExceptionListBytes;
                if (this.publicSuffixExceptionListBytes == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("publicSuffixExceptionListBytes");
                    byArray = null;
                }
                if ((rule = PublicSuffixDatabase.Companion.binarySearch(byArray, domainLabelsUtf8Bytes, labelIndex)) == null) continue;
                exception = rule;
                break;
            }
        }
        if (exception != null) {
            exception = Intrinsics.stringPlus("!", exception);
            char[] i22 = new char[]{'.'};
            return StringsKt.split$default((CharSequence)exception, i22, false, 0, 6, null);
        }
        if (exactMatch == null && wildcardMatch == null) {
            return PREVAILING_RULE;
        }
        String string = exactMatch;
        if (string == null) {
            list2 = null;
        } else {
            char[] cArray = new char[]{'.'};
            list2 = StringsKt.split$default((CharSequence)string, cArray, false, 0, 6, null);
        }
        rule22 = list2;
        List<String> exactRuleLabels = rule22 == null ? CollectionsKt.emptyList() : rule22;
        String string2 = wildcardMatch;
        if (string2 == null) {
            list = null;
        } else {
            char[] cArray = new char[]{'.'};
            list = StringsKt.split$default((CharSequence)string2, cArray, false, 0, 6, null);
        }
        List list3 = list;
        List<String> wildcardRuleLabels = list3 == null ? CollectionsKt.emptyList() : list3;
        return exactRuleLabels.size() > wildcardRuleLabels.size() ? exactRuleLabels : wildcardRuleLabels;
    }

    /*
     * Exception decompiling
     */
    private final void readTheListUninterruptibly() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [3[CATCHBLOCK]], but top level block is 2[TRYBLOCK]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private final void readTheList() throws IOException {
        byte[] publicSuffixListBytes = null;
        byte[] publicSuffixExceptionListBytes = null;
        InputStream inputStream2 = PublicSuffixDatabase.class.getResourceAsStream(PUBLIC_SUFFIX_RESOURCE);
        if (inputStream2 == null) {
            return;
        }
        InputStream resource = inputStream2;
        Object object = Okio.buffer(new GzipSource(Okio.source(resource)));
        Throwable throwable = null;
        try {
            BufferedSource bufferedSource = (BufferedSource)object;
            boolean bl = false;
            int totalBytes = bufferedSource.readInt();
            publicSuffixListBytes = bufferedSource.readByteArray(totalBytes);
            int totalExceptionBytes = bufferedSource.readInt();
            publicSuffixExceptionListBytes = bufferedSource.readByteArray(totalExceptionBytes);
            Unit unit = Unit.INSTANCE;
        }
        catch (Throwable throwable2) {
            throwable = throwable2;
            throw throwable2;
        }
        finally {
            CloseableKt.closeFinally((Closeable)object, throwable);
        }
        object = this;
        synchronized (object) {
            boolean bl = false;
            Intrinsics.checkNotNull(publicSuffixListBytes);
            this.publicSuffixListBytes = publicSuffixListBytes;
            Intrinsics.checkNotNull(publicSuffixExceptionListBytes);
            this.publicSuffixExceptionListBytes = publicSuffixExceptionListBytes;
            Unit unit = Unit.INSTANCE;
        }
        this.readCompleteLatch.countDown();
    }

    public final void setListBytes(@NotNull byte[] publicSuffixListBytes, @NotNull byte[] publicSuffixExceptionListBytes) {
        Intrinsics.checkNotNullParameter(publicSuffixListBytes, "publicSuffixListBytes");
        Intrinsics.checkNotNullParameter(publicSuffixExceptionListBytes, "publicSuffixExceptionListBytes");
        this.publicSuffixListBytes = publicSuffixListBytes;
        this.publicSuffixExceptionListBytes = publicSuffixExceptionListBytes;
        this.listRead.set(true);
        this.readCompleteLatch.countDown();
    }

    static {
        byte[] byArray = new byte[]{42};
        WILDCARD_LABEL = byArray;
        PREVAILING_RULE = CollectionsKt.listOf("*");
        instance = new PublicSuffixDatabase();
    }

    @Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\f\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\r\u001a\u00020\fJ)\u0010\u000e\u001a\u0004\u0018\u00010\u0007*\u00020\n2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\n0\u00102\u0006\u0010\u0011\u001a\u00020\u0012H\u0002\u00a2\u0006\u0002\u0010\u0013R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0007X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0014"}, d2={"Lokhttp3/internal/publicsuffix/PublicSuffixDatabase$Companion;", "", "()V", "EXCEPTION_MARKER", "", "PREVAILING_RULE", "", "", "PUBLIC_SUFFIX_RESOURCE", "WILDCARD_LABEL", "", "instance", "Lokhttp3/internal/publicsuffix/PublicSuffixDatabase;", "get", "binarySearch", "labels", "", "labelIndex", "", "([B[[BI)Ljava/lang/String;", "okhttp"})
    public static final class Companion {
        private Companion() {
        }

        @NotNull
        public final PublicSuffixDatabase get() {
            return instance;
        }

        private final String binarySearch(byte[] $this$binarySearch, byte[][] labels, int labelIndex) {
            int low = 0;
            int high = $this$binarySearch.length;
            String match = null;
            while (low < high) {
                int n2;
                int n3;
                int mid = (low + high) / 2;
                while (mid > -1 && $this$binarySearch[mid] != 10) {
                    n3 = mid;
                    mid = n3 + -1;
                }
                n3 = mid;
                mid = n3 + 1;
                int end = 1;
                while ($this$binarySearch[mid + end] != 10) {
                    int n4 = end;
                    end = n4 + 1;
                }
                int publicSuffixLength = mid + end - mid;
                int compareResult = 0;
                int currentLabelIndex = labelIndex;
                int currentLabelByteIndex = 0;
                int publicSuffixByteIndex = 0;
                boolean expectDot = false;
                while (true) {
                    int byte0 = 0;
                    if (expectDot) {
                        byte0 = 46;
                        expectDot = false;
                    } else {
                        byte0 = Util.and(labels[currentLabelIndex][currentLabelByteIndex], 255);
                    }
                    int byte1 = Util.and($this$binarySearch[mid + publicSuffixByteIndex], 255);
                    compareResult = byte0 - byte1;
                    if (compareResult != 0) break;
                    n2 = publicSuffixByteIndex;
                    publicSuffixByteIndex = n2 + 1;
                    n2 = currentLabelByteIndex;
                    currentLabelByteIndex = n2 + 1;
                    if (publicSuffixByteIndex == publicSuffixLength) break;
                    if (labels[currentLabelIndex].length != currentLabelByteIndex) continue;
                    if (currentLabelIndex == ((Object[])labels).length - 1) break;
                    n2 = currentLabelIndex;
                    currentLabelIndex = n2 + 1;
                    currentLabelByteIndex = -1;
                    expectDot = true;
                }
                if (compareResult < 0) {
                    high = mid - 1;
                    continue;
                }
                if (compareResult > 0) {
                    low = mid + end + 1;
                    continue;
                }
                int publicSuffixBytesLeft = publicSuffixLength - publicSuffixByteIndex;
                int labelBytesLeft = labels[currentLabelIndex].length - currentLabelByteIndex;
                n2 = currentLabelIndex + 1;
                int n5 = ((Object[])labels).length;
                while (n2 < n5) {
                    int i2 = n2++;
                    labelBytesLeft += labels[i2].length;
                }
                if (labelBytesLeft < publicSuffixBytesLeft) {
                    high = mid - 1;
                    continue;
                }
                if (labelBytesLeft > publicSuffixBytesLeft) {
                    low = mid + end + 1;
                    continue;
                }
                Charset charset = StandardCharsets.UTF_8;
                Intrinsics.checkNotNullExpressionValue(charset, "UTF_8");
                match = new String($this$binarySearch, mid, publicSuffixLength, charset);
                break;
            }
            return match;
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

