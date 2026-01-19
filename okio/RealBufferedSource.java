/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.SourceDebugExtension
 */
package okio;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import kotlin.Metadata;
import kotlin.jvm.JvmField;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.text.CharsKt;
import okio.-SegmentedByteString;
import okio.Buffer;
import okio.BufferedSource;
import okio.ByteString;
import okio.Okio;
import okio.Options;
import okio.PeekSource;
import okio.Sink;
import okio.Source;
import okio.Timeout;
import okio.TypedOptions;
import okio.internal.-Buffer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={2, 1, 0}, k=1, xi=48, d1={"\u0000\u0090\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0010\t\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u0005\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\n\n\u0002\b\u0013\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0000\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0004\b\u0004\u0010\u0005J\b\u0010\n\u001a\u00020\u0007H\u0016J\u0018\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00072\u0006\u0010\u0012\u001a\u00020\u0010H\u0016J\b\u0010\u0013\u001a\u00020\tH\u0016J\u0010\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0012\u001a\u00020\u0010H\u0016J\u0010\u0010\u0016\u001a\u00020\t2\u0006\u0010\u0012\u001a\u00020\u0010H\u0016J\b\u0010\u0017\u001a\u00020\u0018H\u0016J\b\u0010\u0019\u001a\u00020\u001aH\u0016J\u0010\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u0012\u001a\u00020\u0010H\u0016J\u0010\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001eH\u0016J'\u0010\u001b\u001a\u0004\u0018\u0001H\u001f\"\b\b\u0000\u0010\u001f*\u00020 2\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u0002H\u001f0!H\u0016\u00a2\u0006\u0002\u0010\"J\b\u0010#\u001a\u00020$H\u0016J\u0010\u0010#\u001a\u00020$2\u0006\u0010\u0012\u001a\u00020\u0010H\u0016J\u0010\u0010\u000f\u001a\u00020\u001c2\u0006\u0010\u0011\u001a\u00020$H\u0016J\u0010\u0010%\u001a\u00020\u00152\u0006\u0010\u0011\u001a\u00020$H\u0016J \u0010\u000f\u001a\u00020\u001c2\u0006\u0010\u0011\u001a\u00020$2\u0006\u0010&\u001a\u00020\u001c2\u0006\u0010\u0012\u001a\u00020\u001cH\u0016J\u0010\u0010\u000f\u001a\u00020\u001c2\u0006\u0010\u0011\u001a\u00020'H\u0016J\u0018\u0010%\u001a\u00020\u00152\u0006\u0010\u0011\u001a\u00020\u00072\u0006\u0010\u0012\u001a\u00020\u0010H\u0016J\u0010\u0010(\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020)H\u0016J\b\u0010*\u001a\u00020+H\u0016J\u0010\u0010*\u001a\u00020+2\u0006\u0010\u0012\u001a\u00020\u0010H\u0016J\u0010\u0010,\u001a\u00020+2\u0006\u0010-\u001a\u00020.H\u0016J\u0018\u0010,\u001a\u00020+2\u0006\u0010\u0012\u001a\u00020\u00102\u0006\u0010-\u001a\u00020.H\u0016J\n\u0010/\u001a\u0004\u0018\u00010+H\u0016J\b\u00100\u001a\u00020+H\u0016J\u0010\u00100\u001a\u00020+2\u0006\u00101\u001a\u00020\u0010H\u0016J\b\u00102\u001a\u00020\u001cH\u0016J\b\u00103\u001a\u000204H\u0016J\b\u00105\u001a\u000204H\u0016J\b\u00106\u001a\u00020\u001cH\u0016J\b\u00107\u001a\u00020\u001cH\u0016J\b\u00108\u001a\u00020\u0010H\u0016J\b\u00109\u001a\u00020\u0010H\u0016J\b\u0010:\u001a\u00020\u0010H\u0016J\b\u0010;\u001a\u00020\u0010H\u0016J\u0010\u0010<\u001a\u00020\u00152\u0006\u0010\u0012\u001a\u00020\u0010H\u0016J\u0010\u0010=\u001a\u00020\u00102\u0006\u0010>\u001a\u00020\u0018H\u0016J\u0018\u0010=\u001a\u00020\u00102\u0006\u0010>\u001a\u00020\u00182\u0006\u0010?\u001a\u00020\u0010H\u0016J \u0010=\u001a\u00020\u00102\u0006\u0010>\u001a\u00020\u00182\u0006\u0010?\u001a\u00020\u00102\u0006\u0010@\u001a\u00020\u0010H\u0016J\u0010\u0010=\u001a\u00020\u00102\u0006\u0010A\u001a\u00020\u001aH\u0016J\u0018\u0010=\u001a\u00020\u00102\u0006\u0010A\u001a\u00020\u001a2\u0006\u0010?\u001a\u00020\u0010H\u0016J\u0010\u0010B\u001a\u00020\u00102\u0006\u0010C\u001a\u00020\u001aH\u0016J\u0018\u0010B\u001a\u00020\u00102\u0006\u0010C\u001a\u00020\u001a2\u0006\u0010?\u001a\u00020\u0010H\u0016J\u0018\u0010D\u001a\u00020\t2\u0006\u0010&\u001a\u00020\u00102\u0006\u0010A\u001a\u00020\u001aH\u0016J(\u0010D\u001a\u00020\t2\u0006\u0010&\u001a\u00020\u00102\u0006\u0010A\u001a\u00020\u001a2\u0006\u0010E\u001a\u00020\u001c2\u0006\u0010\u0012\u001a\u00020\u001cH\u0016J\b\u0010F\u001a\u00020\u0001H\u0016J\b\u0010G\u001a\u00020HH\u0016J\b\u0010I\u001a\u00020\tH\u0016J\b\u0010J\u001a\u00020\u0015H\u0016J\b\u0010K\u001a\u00020LH\u0016J\b\u0010M\u001a\u00020+H\u0016R\u0010\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0006\u001a\u00020\u00078\u0006X\u0087\u0004\u00a2\u0006\u0002\n\u0000R\u0012\u0010\b\u001a\u00020\t8\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u0010\n\u001a\u00020\u00078\u00d6\u0002X\u0096\u0004\u00a2\u0006\f\u0012\u0004\b\u000b\u0010\f\u001a\u0004\b\r\u0010\u000e\u00a8\u0006N"}, d2={"Lokio/RealBufferedSource;", "Lokio/BufferedSource;", "source", "Lokio/Source;", "<init>", "(Lokio/Source;)V", "bufferField", "Lokio/Buffer;", "closed", "", "buffer", "getBuffer$annotations", "()V", "getBuffer", "()Lokio/Buffer;", "read", "", "sink", "byteCount", "exhausted", "require", "", "request", "readByte", "", "readByteString", "Lokio/ByteString;", "select", "", "options", "Lokio/Options;", "T", "", "Lokio/TypedOptions;", "(Lokio/TypedOptions;)Ljava/lang/Object;", "readByteArray", "", "readFully", "offset", "Ljava/nio/ByteBuffer;", "readAll", "Lokio/Sink;", "readUtf8", "", "readString", "charset", "Ljava/nio/charset/Charset;", "readUtf8Line", "readUtf8LineStrict", "limit", "readUtf8CodePoint", "readShort", "", "readShortLe", "readInt", "readIntLe", "readLong", "readLongLe", "readDecimalLong", "readHexadecimalUnsignedLong", "skip", "indexOf", "b", "fromIndex", "toIndex", "bytes", "indexOfElement", "targetBytes", "rangeEquals", "bytesOffset", "peek", "inputStream", "Ljava/io/InputStream;", "isOpen", "close", "timeout", "Lokio/Timeout;", "toString", "okio"})
@SourceDebugExtension(value={"SMAP\nRealBufferedSource.kt\nKotlin\n*S Kotlin\n*F\n+ 1 RealBufferedSource.kt\nokio/RealBufferedSource\n+ 2 RealBufferedSource.kt\nokio/internal/-RealBufferedSource\n+ 3 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 4 BufferedSource.kt\nokio/internal/-BufferedSource\n+ 5 Util.kt\nokio/-SegmentedByteString\n*L\n1#1,205:1\n63#1:211\n63#1:222\n63#1:229\n63#1:235\n63#1:237\n63#1:241\n63#1:246\n63#1:264\n63#1:268\n63#1:275\n63#1:288\n63#1:297\n63#1:298\n63#1:299\n63#1:305\n63#1:313\n63#1:326\n63#1:330\n63#1:331\n63#1:332\n63#1:333\n63#1:338\n63#1:350\n63#1:366\n63#1:376\n63#1:379\n63#1:382\n63#1:385\n63#1:388\n63#1:391\n63#1:397\n63#1:414\n63#1:434\n63#1:449\n63#1:466\n63#1:479\n63#1:500\n63#1:507\n38#2:206\n39#2,3:208\n42#2,7:212\n52#2:219\n53#2:221\n57#2,2:223\n61#2:225\n62#2,2:227\n64#2,3:230\n70#2,2:233\n75#2:236\n76#2:238\n80#2,2:239\n85#2:242\n87#2,2:244\n89#2,13:247\n108#2:263\n109#2:265\n113#2,2:266\n118#2,6:269\n124#2,9:276\n135#2,3:285\n138#2,6:289\n144#2:296\n148#2,5:300\n153#2,5:306\n160#2,2:311\n162#2,11:314\n176#2:325\n177#2:327\n181#2,2:328\n186#2,4:334\n190#2,6:339\n200#2:345\n201#2,3:347\n204#2,8:351\n212#2,3:360\n219#2,3:363\n222#2,7:367\n232#2,2:374\n237#2,2:377\n242#2,2:380\n247#2,2:383\n252#2,2:386\n257#2,2:389\n262#2,5:392\n267#2,11:398\n281#2,5:409\n286#2,14:415\n303#2,2:429\n305#2,2:432\n307#2,7:435\n316#2,2:442\n318#2,4:445\n322#2,11:450\n336#2,2:461\n339#2,2:464\n341#2,7:467\n352#2,2:474\n355#2,2:477\n357#2,7:480\n373#2:487\n375#2,11:489\n387#2:501\n391#2:502\n395#2,4:503\n399#2:508\n401#2:509\n403#2:510\n1#3:207\n1#3:220\n1#3:226\n1#3:243\n1#3:346\n1#3:431\n1#3:444\n1#3:463\n1#3:476\n1#3:488\n26#4,3:260\n88#5:295\n88#5:359\n*S KotlinDebug\n*F\n+ 1 RealBufferedSource.kt\nokio/RealBufferedSource\n*L\n67#1:211\n68#1:222\n70#1:229\n71#1:235\n72#1:237\n73#1:241\n74#1:246\n76#1:264\n77#1:268\n79#1:275\n81#1:288\n84#1:297\n85#1:298\n89#1:299\n93#1:305\n94#1:313\n95#1:326\n96#1:330\n99#1:331\n100#1:332\n105#1:333\n108#1:338\n110#1:350\n111#1:366\n112#1:376\n113#1:379\n114#1:382\n115#1:385\n116#1:388\n117#1:391\n118#1:397\n119#1:414\n120#1:434\n125#1:449\n129#1:466\n133#1:479\n147#1:500\n201#1:507\n67#1:206\n67#1:208,3\n67#1:212,7\n68#1:219\n68#1:221\n69#1:223,2\n70#1:225\n70#1:227,2\n70#1:230,3\n71#1:233,2\n72#1:236\n72#1:238\n73#1:239,2\n74#1:242\n74#1:244,2\n74#1:247,13\n76#1:263\n76#1:265\n77#1:266,2\n79#1:269,6\n79#1:276,9\n81#1:285,3\n81#1:289,6\n81#1:296\n93#1:300,5\n93#1:306,5\n94#1:311,2\n94#1:314,11\n95#1:325\n95#1:327\n96#1:328,2\n108#1:334,4\n108#1:339,6\n110#1:345\n110#1:347,3\n110#1:351,8\n110#1:360,3\n111#1:363,3\n111#1:367,7\n112#1:374,2\n113#1:377,2\n114#1:380,2\n115#1:383,2\n116#1:386,2\n117#1:389,2\n118#1:392,5\n118#1:398,11\n119#1:409,5\n119#1:415,14\n120#1:429,2\n120#1:432,2\n120#1:435,7\n125#1:442,2\n125#1:445,4\n125#1:450,11\n129#1:461,2\n129#1:464,2\n129#1:467,7\n133#1:474,2\n133#1:477,2\n133#1:480,7\n147#1:487\n147#1:489,11\n147#1:501\n149#1:502\n201#1:503,4\n201#1:508\n202#1:509\n203#1:510\n67#1:207\n68#1:220\n70#1:226\n74#1:243\n110#1:346\n120#1:431\n125#1:444\n129#1:463\n133#1:476\n147#1:488\n75#1:260,3\n81#1:295\n110#1:359\n*E\n"})
public final class RealBufferedSource
implements BufferedSource {
    @JvmField
    @NotNull
    public final Source source;
    @JvmField
    @NotNull
    public final Buffer bufferField;
    @JvmField
    public boolean closed;

    public RealBufferedSource(@NotNull Source source2) {
        Intrinsics.checkNotNullParameter(source2, "source");
        this.source = source2;
        this.bufferField = new Buffer();
    }

    @Override
    @NotNull
    public Buffer getBuffer() {
        boolean $i$f$getBuffer = false;
        return this.bufferField;
    }

    public static /* synthetic */ void getBuffer$annotations() {
    }

    @Override
    @NotNull
    public Buffer buffer() {
        return this.bufferField;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public long read(@NotNull Buffer sink2, long byteCount) {
        boolean $i$f$getBuffer;
        RealBufferedSource this_$iv$iv;
        Intrinsics.checkNotNullParameter(sink2, "sink");
        RealBufferedSource $this$commonRead$iv = this;
        boolean $i$f$commonRead = false;
        if (!(byteCount >= 0L)) {
            boolean $i$a$-require--RealBufferedSource$commonRead$1$iv22 = false;
            String $i$a$-require--RealBufferedSource$commonRead$1$iv22 = "byteCount < 0: " + byteCount;
            throw new IllegalArgumentException($i$a$-require--RealBufferedSource$commonRead$1$iv22.toString());
        }
        if (!(!$this$commonRead$iv.closed)) {
            boolean $i$a$-check--RealBufferedSource$commonRead$2$iv22 = false;
            String $i$a$-check--RealBufferedSource$commonRead$2$iv22 = "closed";
            throw new IllegalStateException($i$a$-check--RealBufferedSource$commonRead$2$iv22.toString());
        }
        RealBufferedSource this_$iv$iv2 = $this$commonRead$iv;
        boolean $i$f$getBuffer2 = false;
        if (this_$iv$iv2.bufferField.size() == 0L) {
            if (byteCount == 0L) {
                return 0L;
            }
            this_$iv$iv = $this$commonRead$iv;
            $i$f$getBuffer = false;
            long read$iv = $this$commonRead$iv.source.read(this_$iv$iv.bufferField, 8192L);
            if (read$iv == -1L) {
                return -1L;
            }
        }
        this_$iv$iv = $this$commonRead$iv;
        $i$f$getBuffer = false;
        long l2 = this_$iv$iv.bufferField.size();
        long toRead$iv = Math.min(byteCount, l2);
        this_$iv$iv = $this$commonRead$iv;
        $i$f$getBuffer = false;
        long l3 = this_$iv$iv.bufferField.read(sink2, toRead$iv);
        return l3;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public boolean exhausted() {
        RealBufferedSource $this$commonExhausted$iv = this;
        boolean $i$f$commonExhausted = false;
        if (!(!$this$commonExhausted$iv.closed)) {
            boolean $i$a$-check--RealBufferedSource$commonExhausted$1$iv22 = false;
            String $i$a$-check--RealBufferedSource$commonExhausted$1$iv22 = "closed";
            throw new IllegalStateException($i$a$-check--RealBufferedSource$commonExhausted$1$iv22.toString());
        }
        RealBufferedSource this_$iv$iv = $this$commonExhausted$iv;
        boolean $i$f$getBuffer = false;
        if (!this_$iv$iv.bufferField.exhausted()) return false;
        this_$iv$iv = $this$commonExhausted$iv;
        $i$f$getBuffer = false;
        if ($this$commonExhausted$iv.source.read(this_$iv$iv.bufferField, 8192L) != -1L) return false;
        return true;
    }

    @Override
    public void require(long byteCount) {
        RealBufferedSource $this$commonRequire$iv = this;
        boolean $i$f$commonRequire = false;
        if (!$this$commonRequire$iv.request(byteCount)) {
            throw new EOFException();
        }
    }

    @Override
    public boolean request(long byteCount) {
        boolean bl;
        block4: {
            block3: {
                RealBufferedSource this_$iv$iv;
                RealBufferedSource $this$commonRequest$iv = this;
                boolean $i$f$commonRequest = false;
                if (!(byteCount >= 0L)) {
                    boolean $i$a$-require--RealBufferedSource$commonRequest$1$iv22 = false;
                    String $i$a$-require--RealBufferedSource$commonRequest$1$iv22 = "byteCount < 0: " + byteCount;
                    throw new IllegalArgumentException($i$a$-require--RealBufferedSource$commonRequest$1$iv22.toString());
                }
                if (!(!$this$commonRequest$iv.closed)) {
                    boolean $i$a$-check--RealBufferedSource$commonRequest$2$iv22 = false;
                    String $i$a$-check--RealBufferedSource$commonRequest$2$iv22 = "closed";
                    throw new IllegalStateException($i$a$-check--RealBufferedSource$commonRequest$2$iv22.toString());
                }
                do {
                    this_$iv$iv = $this$commonRequest$iv;
                    boolean $i$f$getBuffer = false;
                    if (this_$iv$iv.bufferField.size() >= byteCount) break block3;
                    this_$iv$iv = $this$commonRequest$iv;
                    $i$f$getBuffer = false;
                } while ($this$commonRequest$iv.source.read(this_$iv$iv.bufferField, 8192L) != -1L);
                bl = false;
                break block4;
            }
            bl = true;
        }
        return bl;
    }

    @Override
    public byte readByte() {
        RealBufferedSource $this$commonReadByte$iv = this;
        boolean $i$f$commonReadByte = false;
        $this$commonReadByte$iv.require(1L);
        RealBufferedSource this_$iv$iv = $this$commonReadByte$iv;
        boolean $i$f$getBuffer = false;
        return this_$iv$iv.bufferField.readByte();
    }

    @Override
    @NotNull
    public ByteString readByteString() {
        RealBufferedSource $this$commonReadByteString$iv = this;
        boolean $i$f$commonReadByteString = false;
        RealBufferedSource this_$iv$iv = $this$commonReadByteString$iv;
        boolean $i$f$getBuffer = false;
        this_$iv$iv.bufferField.writeAll($this$commonReadByteString$iv.source);
        this_$iv$iv = $this$commonReadByteString$iv;
        $i$f$getBuffer = false;
        return this_$iv$iv.bufferField.readByteString();
    }

    @Override
    @NotNull
    public ByteString readByteString(long byteCount) {
        RealBufferedSource $this$commonReadByteString$iv = this;
        boolean $i$f$commonReadByteString = false;
        $this$commonReadByteString$iv.require(byteCount);
        RealBufferedSource this_$iv$iv = $this$commonReadByteString$iv;
        boolean $i$f$getBuffer = false;
        return this_$iv$iv.bufferField.readByteString(byteCount);
    }

    @Override
    public int select(@NotNull Options options) {
        int n2;
        Intrinsics.checkNotNullParameter(options, "options");
        RealBufferedSource $this$commonSelect$iv = this;
        boolean $i$f$commonSelect = false;
        if (!(!$this$commonSelect$iv.closed)) {
            boolean $i$a$-check--RealBufferedSource$commonSelect$1$iv22 = false;
            String $i$a$-check--RealBufferedSource$commonSelect$1$iv22 = "closed";
            throw new IllegalStateException($i$a$-check--RealBufferedSource$commonSelect$1$iv22.toString());
        }
        block4: while (true) {
            RealBufferedSource this_$iv$iv = $this$commonSelect$iv;
            boolean $i$f$getBuffer = false;
            int index$iv = -Buffer.selectPrefix(this_$iv$iv.bufferField, options, true);
            switch (index$iv) {
                case -1: {
                    n2 = -1;
                    break block4;
                }
                case -2: {
                    RealBufferedSource this_$iv$iv2 = $this$commonSelect$iv;
                    boolean $i$f$getBuffer2 = false;
                    if ($this$commonSelect$iv.source.read(this_$iv$iv2.bufferField, 8192L) != -1L) continue block4;
                    n2 = -1;
                    break block4;
                }
                default: {
                    int selectedSize$iv = options.getByteStrings$okio()[index$iv].size();
                    RealBufferedSource this_$iv$iv3 = $this$commonSelect$iv;
                    boolean $i$f$getBuffer3 = false;
                    this_$iv$iv3.bufferField.skip(selectedSize$iv);
                    n2 = index$iv;
                    break block4;
                }
            }
            break;
        }
        return n2;
    }

    @Override
    @Nullable
    public <T> T select(@NotNull TypedOptions<T> options) {
        Intrinsics.checkNotNullParameter(options, "options");
        BufferedSource $this$commonSelect$iv = this;
        boolean $i$f$commonSelect = false;
        int index$iv = $this$commonSelect$iv.select(options.getOptions$okio());
        return index$iv == -1 ? null : (T)options.get(index$iv);
    }

    @Override
    @NotNull
    public byte[] readByteArray() {
        RealBufferedSource $this$commonReadByteArray$iv = this;
        boolean $i$f$commonReadByteArray = false;
        RealBufferedSource this_$iv$iv = $this$commonReadByteArray$iv;
        boolean $i$f$getBuffer = false;
        this_$iv$iv.bufferField.writeAll($this$commonReadByteArray$iv.source);
        this_$iv$iv = $this$commonReadByteArray$iv;
        $i$f$getBuffer = false;
        return this_$iv$iv.bufferField.readByteArray();
    }

    @Override
    @NotNull
    public byte[] readByteArray(long byteCount) {
        RealBufferedSource $this$commonReadByteArray$iv = this;
        boolean $i$f$commonReadByteArray = false;
        $this$commonReadByteArray$iv.require(byteCount);
        RealBufferedSource this_$iv$iv = $this$commonReadByteArray$iv;
        boolean $i$f$getBuffer = false;
        return this_$iv$iv.bufferField.readByteArray(byteCount);
    }

    @Override
    public int read(@NotNull byte[] sink2) {
        Intrinsics.checkNotNullParameter(sink2, "sink");
        return this.read(sink2, 0, sink2.length);
    }

    @Override
    public void readFully(@NotNull byte[] sink2) {
        Intrinsics.checkNotNullParameter(sink2, "sink");
        RealBufferedSource $this$commonReadFully$iv = this;
        boolean $i$f$commonReadFully = false;
        try {
            $this$commonReadFully$iv.require(sink2.length);
        }
        catch (EOFException e$iv) {
            int offset$iv = 0;
            while (true) {
                RealBufferedSource this_$iv$iv = $this$commonReadFully$iv;
                boolean $i$f$getBuffer = false;
                if (this_$iv$iv.bufferField.size() <= 0L) break;
                RealBufferedSource this_$iv$iv2 = $this$commonReadFully$iv;
                boolean $i$f$getBuffer2 = false;
                this_$iv$iv2 = $this$commonReadFully$iv;
                $i$f$getBuffer2 = false;
                int read$iv = this_$iv$iv2.bufferField.read(sink2, offset$iv, (int)this_$iv$iv2.bufferField.size());
                if (read$iv == -1) {
                    throw new AssertionError();
                }
                offset$iv += read$iv;
            }
            throw e$iv;
        }
        RealBufferedSource this_$iv$iv = $this$commonReadFully$iv;
        boolean $i$f$getBuffer = false;
        this_$iv$iv.bufferField.readFully(sink2);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public int read(@NotNull byte[] sink2, int offset, int byteCount) {
        Intrinsics.checkNotNullParameter(sink2, "sink");
        RealBufferedSource $this$commonRead$iv = this;
        boolean $i$f$commonRead = false;
        -SegmentedByteString.checkOffsetAndCount(sink2.length, offset, byteCount);
        RealBufferedSource this_$iv$iv = $this$commonRead$iv;
        boolean $i$f$getBuffer = false;
        if (this_$iv$iv.bufferField.size() == 0L) {
            if (byteCount == 0) {
                return 0;
            }
            RealBufferedSource this_$iv$iv2 = $this$commonRead$iv;
            boolean $i$f$getBuffer2 = false;
            long read$iv = $this$commonRead$iv.source.read(this_$iv$iv2.bufferField, 8192L);
            if (read$iv == -1L) {
                return -1;
            }
        }
        RealBufferedSource this_$iv$iv3 = $this$commonRead$iv;
        boolean $i$f$getBuffer3 = false;
        long b$iv$iv = this_$iv$iv3.bufferField.size();
        boolean $i$f$minOf = false;
        int toRead$iv = (int)Math.min((long)byteCount, b$iv$iv);
        this_$iv$iv3 = $this$commonRead$iv;
        $i$f$getBuffer3 = false;
        int n2 = this_$iv$iv3.bufferField.read(sink2, offset, toRead$iv);
        return n2;
    }

    @Override
    public int read(@NotNull ByteBuffer sink2) {
        Intrinsics.checkNotNullParameter(sink2, "sink");
        RealBufferedSource this_$iv = this;
        boolean $i$f$getBuffer = false;
        if (this_$iv.bufferField.size() == 0L) {
            RealBufferedSource this_$iv2 = this;
            boolean $i$f$getBuffer2 = false;
            long read = this.source.read(this_$iv2.bufferField, 8192L);
            if (read == -1L) {
                return -1;
            }
        }
        this_$iv = this;
        $i$f$getBuffer = false;
        return this_$iv.bufferField.read(sink2);
    }

    @Override
    public void readFully(@NotNull Buffer sink2, long byteCount) {
        Intrinsics.checkNotNullParameter(sink2, "sink");
        RealBufferedSource $this$commonReadFully$iv = this;
        boolean $i$f$commonReadFully = false;
        try {
            $this$commonReadFully$iv.require(byteCount);
        }
        catch (EOFException e$iv) {
            RealBufferedSource this_$iv$iv = $this$commonReadFully$iv;
            boolean $i$f$getBuffer = false;
            sink2.writeAll(this_$iv$iv.bufferField);
            throw e$iv;
        }
        RealBufferedSource this_$iv$iv = $this$commonReadFully$iv;
        boolean $i$f$getBuffer = false;
        this_$iv$iv.bufferField.readFully(sink2, byteCount);
    }

    @Override
    public long readAll(@NotNull Sink sink2) {
        boolean $i$f$getBuffer;
        RealBufferedSource this_$iv$iv;
        Intrinsics.checkNotNullParameter(sink2, "sink");
        RealBufferedSource $this$commonReadAll$iv = this;
        boolean $i$f$commonReadAll = false;
        long totalBytesWritten$iv = 0L;
        while (true) {
            this_$iv$iv = $this$commonReadAll$iv;
            $i$f$getBuffer = false;
            if ($this$commonReadAll$iv.source.read(this_$iv$iv.bufferField, 8192L) == -1L) break;
            RealBufferedSource this_$iv$iv2 = $this$commonReadAll$iv;
            boolean $i$f$getBuffer2 = false;
            long emitByteCount$iv = this_$iv$iv2.bufferField.completeSegmentByteCount();
            if (emitByteCount$iv <= 0L) continue;
            totalBytesWritten$iv += emitByteCount$iv;
            this_$iv$iv2 = $this$commonReadAll$iv;
            $i$f$getBuffer2 = false;
            sink2.write(this_$iv$iv2.bufferField, emitByteCount$iv);
        }
        this_$iv$iv = $this$commonReadAll$iv;
        $i$f$getBuffer = false;
        if (this_$iv$iv.bufferField.size() > 0L) {
            this_$iv$iv = $this$commonReadAll$iv;
            $i$f$getBuffer = false;
            totalBytesWritten$iv += this_$iv$iv.bufferField.size();
            this_$iv$iv = $this$commonReadAll$iv;
            $i$f$getBuffer = false;
            this_$iv$iv = $this$commonReadAll$iv;
            $i$f$getBuffer = false;
            sink2.write(this_$iv$iv.bufferField, this_$iv$iv.bufferField.size());
        }
        return totalBytesWritten$iv;
    }

    @Override
    @NotNull
    public String readUtf8() {
        RealBufferedSource $this$commonReadUtf8$iv = this;
        boolean $i$f$commonReadUtf8 = false;
        RealBufferedSource this_$iv$iv = $this$commonReadUtf8$iv;
        boolean $i$f$getBuffer = false;
        this_$iv$iv.bufferField.writeAll($this$commonReadUtf8$iv.source);
        this_$iv$iv = $this$commonReadUtf8$iv;
        $i$f$getBuffer = false;
        return this_$iv$iv.bufferField.readUtf8();
    }

    @Override
    @NotNull
    public String readUtf8(long byteCount) {
        RealBufferedSource $this$commonReadUtf8$iv = this;
        boolean $i$f$commonReadUtf8 = false;
        $this$commonReadUtf8$iv.require(byteCount);
        RealBufferedSource this_$iv$iv = $this$commonReadUtf8$iv;
        boolean $i$f$getBuffer = false;
        return this_$iv$iv.bufferField.readUtf8(byteCount);
    }

    @Override
    @NotNull
    public String readString(@NotNull Charset charset) {
        Intrinsics.checkNotNullParameter(charset, "charset");
        RealBufferedSource this_$iv = this;
        boolean $i$f$getBuffer = false;
        this_$iv.bufferField.writeAll(this.source);
        this_$iv = this;
        $i$f$getBuffer = false;
        return this_$iv.bufferField.readString(charset);
    }

    @Override
    @NotNull
    public String readString(long byteCount, @NotNull Charset charset) {
        Intrinsics.checkNotNullParameter(charset, "charset");
        this.require(byteCount);
        RealBufferedSource this_$iv = this;
        boolean $i$f$getBuffer = false;
        return this_$iv.bufferField.readString(byteCount, charset);
    }

    @Override
    @Nullable
    public String readUtf8Line() {
        String string;
        RealBufferedSource $this$commonReadUtf8Line$iv = this;
        boolean $i$f$commonReadUtf8Line = false;
        long newline$iv = $this$commonReadUtf8Line$iv.indexOf((byte)10);
        if (newline$iv == -1L) {
            RealBufferedSource this_$iv$iv = $this$commonReadUtf8Line$iv;
            boolean $i$f$getBuffer = false;
            if (this_$iv$iv.bufferField.size() != 0L) {
                this_$iv$iv = $this$commonReadUtf8Line$iv;
                $i$f$getBuffer = false;
                string = $this$commonReadUtf8Line$iv.readUtf8(this_$iv$iv.bufferField.size());
            } else {
                string = null;
            }
        } else {
            RealBufferedSource this_$iv$iv = $this$commonReadUtf8Line$iv;
            boolean $i$f$getBuffer = false;
            string = -Buffer.readUtf8Line(this_$iv$iv.bufferField, newline$iv);
        }
        return string;
    }

    @Override
    @NotNull
    public String readUtf8LineStrict() {
        return this.readUtf8LineStrict(Long.MAX_VALUE);
    }

    /*
     * WARNING - void declaration
     * Enabled aggressive block sorting
     */
    @Override
    @NotNull
    public String readUtf8LineStrict(long limit) {
        void a$iv$iv;
        String string;
        RealBufferedSource $this$commonReadUtf8LineStrict$iv = this;
        boolean $i$f$commonReadUtf8LineStrict = false;
        if (!(limit >= 0L)) {
            boolean bl = false;
            String string2 = "limit < 0: " + limit;
            throw new IllegalArgumentException(string2.toString());
        }
        long scanLength$iv = limit == Long.MAX_VALUE ? Long.MAX_VALUE : limit + 1L;
        long newline$iv = $this$commonReadUtf8LineStrict$iv.indexOf((byte)10, 0L, scanLength$iv);
        if (newline$iv != -1L) {
            RealBufferedSource this_$iv$iv = $this$commonReadUtf8LineStrict$iv;
            boolean $i$f$getBuffer = false;
            string = -Buffer.readUtf8Line(this_$iv$iv.bufferField, newline$iv);
            return string;
        }
        if (scanLength$iv < Long.MAX_VALUE && $this$commonReadUtf8LineStrict$iv.request(scanLength$iv)) {
            RealBufferedSource this_$iv$iv = $this$commonReadUtf8LineStrict$iv;
            boolean $i$f$getBuffer = false;
            if (this_$iv$iv.bufferField.getByte(scanLength$iv - 1L) == 13 && $this$commonReadUtf8LineStrict$iv.request(scanLength$iv + 1L)) {
                this_$iv$iv = $this$commonReadUtf8LineStrict$iv;
                $i$f$getBuffer = false;
                if (this_$iv$iv.bufferField.getByte(scanLength$iv) == 10) {
                    this_$iv$iv = $this$commonReadUtf8LineStrict$iv;
                    $i$f$getBuffer = false;
                    string = -Buffer.readUtf8Line(this_$iv$iv.bufferField, scanLength$iv);
                    return string;
                }
            }
        }
        Buffer data$iv = new Buffer();
        RealBufferedSource this_$iv$iv22 = $this$commonReadUtf8LineStrict$iv;
        boolean $i$f$getBuffer = false;
        int this_$iv$iv22 = 32;
        RealBufferedSource this_$iv$iv = $this$commonReadUtf8LineStrict$iv;
        boolean $i$f$getBuffer2 = false;
        long b$iv$iv = this_$iv$iv.bufferField.size();
        boolean $i$f$minOf = false;
        this_$iv$iv22.bufferField.copyTo(data$iv, 0L, Math.min((long)a$iv$iv, b$iv$iv));
        RealBufferedSource this_$iv$iv3 = $this$commonReadUtf8LineStrict$iv;
        $i$f$getBuffer = false;
        throw new EOFException("\\n not found: limit=" + Math.min(this_$iv$iv3.bufferField.size(), limit) + " content=" + data$iv.readByteString().hex() + '\u2026');
    }

    @Override
    public int readUtf8CodePoint() {
        RealBufferedSource $this$commonReadUtf8CodePoint$iv = this;
        boolean $i$f$commonReadUtf8CodePoint = false;
        $this$commonReadUtf8CodePoint$iv.require(1L);
        RealBufferedSource this_$iv$iv = $this$commonReadUtf8CodePoint$iv;
        boolean $i$f$getBuffer = false;
        byte b0$iv = this_$iv$iv.bufferField.getByte(0L);
        if ((b0$iv & 0xE0) == 192) {
            $this$commonReadUtf8CodePoint$iv.require(2L);
        } else if ((b0$iv & 0xF0) == 224) {
            $this$commonReadUtf8CodePoint$iv.require(3L);
        } else if ((b0$iv & 0xF8) == 240) {
            $this$commonReadUtf8CodePoint$iv.require(4L);
        }
        this_$iv$iv = $this$commonReadUtf8CodePoint$iv;
        $i$f$getBuffer = false;
        return this_$iv$iv.bufferField.readUtf8CodePoint();
    }

    @Override
    public short readShort() {
        RealBufferedSource $this$commonReadShort$iv = this;
        boolean $i$f$commonReadShort = false;
        $this$commonReadShort$iv.require(2L);
        RealBufferedSource this_$iv$iv = $this$commonReadShort$iv;
        boolean $i$f$getBuffer = false;
        return this_$iv$iv.bufferField.readShort();
    }

    @Override
    public short readShortLe() {
        RealBufferedSource $this$commonReadShortLe$iv = this;
        boolean $i$f$commonReadShortLe = false;
        $this$commonReadShortLe$iv.require(2L);
        RealBufferedSource this_$iv$iv = $this$commonReadShortLe$iv;
        boolean $i$f$getBuffer = false;
        return this_$iv$iv.bufferField.readShortLe();
    }

    @Override
    public int readInt() {
        RealBufferedSource $this$commonReadInt$iv = this;
        boolean $i$f$commonReadInt = false;
        $this$commonReadInt$iv.require(4L);
        RealBufferedSource this_$iv$iv = $this$commonReadInt$iv;
        boolean $i$f$getBuffer = false;
        return this_$iv$iv.bufferField.readInt();
    }

    @Override
    public int readIntLe() {
        RealBufferedSource $this$commonReadIntLe$iv = this;
        boolean $i$f$commonReadIntLe = false;
        $this$commonReadIntLe$iv.require(4L);
        RealBufferedSource this_$iv$iv = $this$commonReadIntLe$iv;
        boolean $i$f$getBuffer = false;
        return this_$iv$iv.bufferField.readIntLe();
    }

    @Override
    public long readLong() {
        RealBufferedSource $this$commonReadLong$iv = this;
        boolean $i$f$commonReadLong = false;
        $this$commonReadLong$iv.require(8L);
        RealBufferedSource this_$iv$iv = $this$commonReadLong$iv;
        boolean $i$f$getBuffer = false;
        return this_$iv$iv.bufferField.readLong();
    }

    @Override
    public long readLongLe() {
        RealBufferedSource $this$commonReadLongLe$iv = this;
        boolean $i$f$commonReadLongLe = false;
        $this$commonReadLongLe$iv.require(8L);
        RealBufferedSource this_$iv$iv = $this$commonReadLongLe$iv;
        boolean $i$f$getBuffer = false;
        return this_$iv$iv.bufferField.readLongLe();
    }

    @Override
    public long readDecimalLong() {
        RealBufferedSource $this$commonReadDecimalLong$iv = this;
        boolean $i$f$commonReadDecimalLong = false;
        $this$commonReadDecimalLong$iv.require(1L);
        long pos$iv = 0L;
        while ($this$commonReadDecimalLong$iv.request(pos$iv + 1L)) {
            RealBufferedSource this_$iv$iv = $this$commonReadDecimalLong$iv;
            boolean $i$f$getBuffer = false;
            byte b$iv = this_$iv$iv.bufferField.getByte(pos$iv);
            if (!(b$iv >= 48 && b$iv <= 57 || pos$iv == 0L && b$iv == 45)) {
                if (pos$iv != 0L) break;
                StringBuilder stringBuilder = new StringBuilder().append("Expected a digit or '-' but was 0x");
                String string = Integer.toString(b$iv, CharsKt.checkRadix(16));
                Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
                throw new NumberFormatException(stringBuilder.append(string).toString());
            }
            ++pos$iv;
        }
        RealBufferedSource this_$iv$iv = $this$commonReadDecimalLong$iv;
        boolean $i$f$getBuffer = false;
        return this_$iv$iv.bufferField.readDecimalLong();
    }

    @Override
    public long readHexadecimalUnsignedLong() {
        RealBufferedSource $this$commonReadHexadecimalUnsignedLong$iv = this;
        boolean $i$f$commonReadHexadecimalUnsignedLong = false;
        $this$commonReadHexadecimalUnsignedLong$iv.require(1L);
        int pos$iv = 0;
        while ($this$commonReadHexadecimalUnsignedLong$iv.request(pos$iv + 1)) {
            RealBufferedSource this_$iv$iv = $this$commonReadHexadecimalUnsignedLong$iv;
            boolean $i$f$getBuffer = false;
            byte b$iv = this_$iv$iv.bufferField.getByte(pos$iv);
            if (!(b$iv >= 48 && b$iv <= 57 || b$iv >= 97 && b$iv <= 102 || b$iv >= 65 && b$iv <= 70)) {
                if (pos$iv != 0) break;
                StringBuilder stringBuilder = new StringBuilder().append("Expected leading [0-9a-fA-F] character but was 0x");
                String string = Integer.toString(b$iv, CharsKt.checkRadix(16));
                Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
                throw new NumberFormatException(stringBuilder.append(string).toString());
            }
            ++pos$iv;
        }
        RealBufferedSource this_$iv$iv = $this$commonReadHexadecimalUnsignedLong$iv;
        boolean $i$f$getBuffer = false;
        return this_$iv$iv.bufferField.readHexadecimalUnsignedLong();
    }

    @Override
    public void skip(long byteCount) {
        long toSkip$iv;
        RealBufferedSource $this$commonSkip$iv = this;
        boolean $i$f$commonSkip = false;
        if (!(!$this$commonSkip$iv.closed)) {
            boolean $i$a$-check--RealBufferedSource$commonSkip$1$iv22 = false;
            String $i$a$-check--RealBufferedSource$commonSkip$1$iv22 = "closed";
            throw new IllegalStateException($i$a$-check--RealBufferedSource$commonSkip$1$iv22.toString());
        }
        for (long byteCount$iv = byteCount; byteCount$iv > 0L; byteCount$iv -= toSkip$iv) {
            RealBufferedSource this_$iv$iv = $this$commonSkip$iv;
            boolean $i$f$getBuffer = false;
            if (this_$iv$iv.bufferField.size() == 0L) {
                this_$iv$iv = $this$commonSkip$iv;
                $i$f$getBuffer = false;
                if ($this$commonSkip$iv.source.read(this_$iv$iv.bufferField, 8192L) == -1L) {
                    throw new EOFException();
                }
            }
            RealBufferedSource this_$iv$iv2 = $this$commonSkip$iv;
            boolean $i$f$getBuffer2 = false;
            long l2 = this_$iv$iv2.bufferField.size();
            toSkip$iv = Math.min(byteCount$iv, l2);
            this_$iv$iv2 = $this$commonSkip$iv;
            $i$f$getBuffer2 = false;
            this_$iv$iv2.bufferField.skip(toSkip$iv);
        }
    }

    @Override
    public long indexOf(byte b2) {
        return this.indexOf(b2, 0L, Long.MAX_VALUE);
    }

    @Override
    public long indexOf(byte b2, long fromIndex) {
        return this.indexOf(b2, fromIndex, Long.MAX_VALUE);
    }

    @Override
    public long indexOf(byte b2, long fromIndex, long toIndex) {
        long l2;
        block6: {
            RealBufferedSource $this$commonIndexOf$iv = this;
            boolean $i$f$commonIndexOf = false;
            long fromIndex$iv = 0L;
            fromIndex$iv = fromIndex;
            if (!(!$this$commonIndexOf$iv.closed)) {
                boolean $i$a$-check--RealBufferedSource$commonIndexOf$1$iv22 = false;
                String $i$a$-check--RealBufferedSource$commonIndexOf$1$iv22 = "closed";
                throw new IllegalStateException($i$a$-check--RealBufferedSource$commonIndexOf$1$iv22.toString());
            }
            if (!(0L <= fromIndex$iv ? fromIndex$iv <= toIndex : false)) {
                boolean bl = false;
                String string = "fromIndex=" + fromIndex$iv + " toIndex=" + toIndex;
                throw new IllegalArgumentException(string.toString());
            }
            while (fromIndex$iv < toIndex) {
                long lastBufferSize$iv;
                block8: {
                    block7: {
                        RealBufferedSource this_$iv$iv = $this$commonIndexOf$iv;
                        boolean $i$f$getBuffer = false;
                        long result$iv = this_$iv$iv.bufferField.indexOf(b2, fromIndex$iv, toIndex);
                        if (result$iv != -1L) {
                            l2 = result$iv;
                            break block6;
                        }
                        RealBufferedSource this_$iv$iv2 = $this$commonIndexOf$iv;
                        boolean $i$f$getBuffer2 = false;
                        lastBufferSize$iv = this_$iv$iv2.bufferField.size();
                        if (lastBufferSize$iv >= toIndex) break block7;
                        this_$iv$iv2 = $this$commonIndexOf$iv;
                        $i$f$getBuffer2 = false;
                        if ($this$commonIndexOf$iv.source.read(this_$iv$iv2.bufferField, 8192L) != -1L) break block8;
                    }
                    l2 = -1L;
                    break block6;
                }
                fromIndex$iv = Math.max(fromIndex$iv, lastBufferSize$iv);
            }
            l2 = -1L;
        }
        return l2;
    }

    @Override
    public long indexOf(@NotNull ByteString bytes) {
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        return this.indexOf(bytes, 0L);
    }

    @Override
    public long indexOf(@NotNull ByteString bytes, long fromIndex) {
        long l2;
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        RealBufferedSource $this$commonIndexOf$iv = this;
        boolean $i$f$commonIndexOf = false;
        long fromIndex$iv = fromIndex;
        if (!(!$this$commonIndexOf$iv.closed)) {
            boolean bl = false;
            String string = "closed";
            throw new IllegalStateException(string.toString());
        }
        while (true) {
            RealBufferedSource this_$iv$iv = $this$commonIndexOf$iv;
            boolean $i$f$getBuffer = false;
            long result$iv = this_$iv$iv.bufferField.indexOf(bytes, fromIndex$iv);
            if (result$iv != -1L) {
                l2 = result$iv;
                break;
            }
            RealBufferedSource this_$iv$iv2 = $this$commonIndexOf$iv;
            boolean $i$f$getBuffer2 = false;
            long lastBufferSize$iv = this_$iv$iv2.bufferField.size();
            this_$iv$iv2 = $this$commonIndexOf$iv;
            $i$f$getBuffer2 = false;
            if ($this$commonIndexOf$iv.source.read(this_$iv$iv2.bufferField, 8192L) == -1L) {
                l2 = -1L;
                break;
            }
            fromIndex$iv = Math.max(fromIndex$iv, lastBufferSize$iv - (long)bytes.size() + 1L);
        }
        return l2;
    }

    @Override
    public long indexOfElement(@NotNull ByteString targetBytes) {
        Intrinsics.checkNotNullParameter(targetBytes, "targetBytes");
        return this.indexOfElement(targetBytes, 0L);
    }

    @Override
    public long indexOfElement(@NotNull ByteString targetBytes, long fromIndex) {
        long l2;
        Intrinsics.checkNotNullParameter(targetBytes, "targetBytes");
        RealBufferedSource $this$commonIndexOfElement$iv = this;
        boolean $i$f$commonIndexOfElement = false;
        long fromIndex$iv = fromIndex;
        if (!(!$this$commonIndexOfElement$iv.closed)) {
            boolean bl = false;
            String string = "closed";
            throw new IllegalStateException(string.toString());
        }
        while (true) {
            RealBufferedSource this_$iv$iv = $this$commonIndexOfElement$iv;
            boolean $i$f$getBuffer = false;
            long result$iv = this_$iv$iv.bufferField.indexOfElement(targetBytes, fromIndex$iv);
            if (result$iv != -1L) {
                l2 = result$iv;
                break;
            }
            RealBufferedSource this_$iv$iv2 = $this$commonIndexOfElement$iv;
            boolean $i$f$getBuffer2 = false;
            long lastBufferSize$iv = this_$iv$iv2.bufferField.size();
            this_$iv$iv2 = $this$commonIndexOfElement$iv;
            $i$f$getBuffer2 = false;
            if ($this$commonIndexOfElement$iv.source.read(this_$iv$iv2.bufferField, 8192L) == -1L) {
                l2 = -1L;
                break;
            }
            fromIndex$iv = Math.max(fromIndex$iv, lastBufferSize$iv);
        }
        return l2;
    }

    @Override
    public boolean rangeEquals(long offset, @NotNull ByteString bytes) {
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        return this.rangeEquals(offset, bytes, 0, bytes.size());
    }

    @Override
    public boolean rangeEquals(long offset, @NotNull ByteString bytes, int bytesOffset, int byteCount) {
        boolean bl;
        block6: {
            Intrinsics.checkNotNullParameter(bytes, "bytes");
            RealBufferedSource $this$commonRangeEquals$iv = this;
            boolean $i$f$commonRangeEquals = false;
            if (!(!$this$commonRangeEquals$iv.closed)) {
                boolean bl2 = false;
                String string = "closed";
                throw new IllegalStateException(string.toString());
            }
            if (offset < 0L || bytesOffset < 0 || byteCount < 0 || bytes.size() - bytesOffset < byteCount) {
                bl = false;
            } else {
                for (int i$iv = 0; i$iv < byteCount; ++i$iv) {
                    long bufferOffset$iv = offset + (long)i$iv;
                    if (!$this$commonRangeEquals$iv.request(bufferOffset$iv + 1L)) {
                        bl = false;
                    } else {
                        RealBufferedSource this_$iv$iv = $this$commonRangeEquals$iv;
                        boolean $i$f$getBuffer = false;
                        if (this_$iv$iv.bufferField.getByte(bufferOffset$iv) == bytes.getByte(bytesOffset + i$iv)) continue;
                        bl = false;
                    }
                    break block6;
                }
                bl = true;
            }
        }
        return bl;
    }

    @Override
    @NotNull
    public BufferedSource peek() {
        RealBufferedSource $this$commonPeek$iv = this;
        boolean $i$f$commonPeek = false;
        return Okio.buffer(new PeekSource($this$commonPeek$iv));
    }

    @Override
    @NotNull
    public InputStream inputStream() {
        return new InputStream(this){
            final /* synthetic */ RealBufferedSource this$0;
            {
                this.this$0 = $receiver;
            }

            /*
             * WARNING - void declaration
             */
            public int read() {
                void $this$and$iv;
                if (this.this$0.closed) {
                    throw new IOException("closed");
                }
                RealBufferedSource this_$iv = this.this$0;
                boolean $i$f$getBuffer = false;
                if (this_$iv.bufferField.size() == 0L) {
                    RealBufferedSource this_$iv2 = this.this$0;
                    boolean $i$f$getBuffer2 = false;
                    long count = this.this$0.source.read(this_$iv2.bufferField, 8192L);
                    if (count == -1L) {
                        return -1;
                    }
                }
                this_$iv = this.this$0;
                $i$f$getBuffer = false;
                byte this_$iv2 = this_$iv.bufferField.readByte();
                int other$iv = 255;
                boolean $i$f$and = false;
                return $this$and$iv & other$iv;
            }

            public int read(byte[] data, int offset, int byteCount) {
                Intrinsics.checkNotNullParameter(data, "data");
                if (this.this$0.closed) {
                    throw new IOException("closed");
                }
                -SegmentedByteString.checkOffsetAndCount(data.length, offset, byteCount);
                RealBufferedSource this_$iv = this.this$0;
                boolean $i$f$getBuffer = false;
                if (this_$iv.bufferField.size() == 0L) {
                    RealBufferedSource this_$iv2 = this.this$0;
                    boolean $i$f$getBuffer2 = false;
                    long count = this.this$0.source.read(this_$iv2.bufferField, 8192L);
                    if (count == -1L) {
                        return -1;
                    }
                }
                this_$iv = this.this$0;
                $i$f$getBuffer = false;
                return this_$iv.bufferField.read(data, offset, byteCount);
            }

            /*
             * WARNING - void declaration
             */
            public int available() {
                void a$iv;
                if (this.this$0.closed) {
                    throw new IOException("closed");
                }
                RealBufferedSource this_$iv22 = this.this$0;
                boolean $i$f$getBuffer = false;
                long this_$iv22 = this_$iv22.bufferField.size();
                int b$iv = Integer.MAX_VALUE;
                boolean $i$f$minOf = false;
                return (int)Math.min((long)a$iv, (long)b$iv);
            }

            public void close() {
                this.this$0.close();
            }

            public String toString() {
                return this.this$0 + ".inputStream()";
            }

            public long transferTo(OutputStream out) {
                Intrinsics.checkNotNullParameter(out, "out");
                if (this.this$0.closed) {
                    throw new IOException("closed");
                }
                long count = 0L;
                while (true) {
                    RealBufferedSource this_$iv = this.this$0;
                    boolean $i$f$getBuffer = false;
                    if (this_$iv.bufferField.size() == 0L) {
                        RealBufferedSource this_$iv2 = this.this$0;
                        boolean $i$f$getBuffer2 = false;
                        long read = this.this$0.source.read(this_$iv2.bufferField, 8192L);
                        if (read == -1L) break;
                    }
                    this_$iv = this.this$0;
                    $i$f$getBuffer = false;
                    count += this_$iv.bufferField.size();
                    this_$iv = this.this$0;
                    $i$f$getBuffer = false;
                    Buffer.writeTo$default(this_$iv.bufferField, out, 0L, 2, null);
                }
                return count;
            }
        };
    }

    @Override
    public boolean isOpen() {
        return !this.closed;
    }

    @Override
    public void close() {
        RealBufferedSource $this$commonClose$iv = this;
        boolean $i$f$commonClose = false;
        if (!$this$commonClose$iv.closed) {
            $this$commonClose$iv.closed = true;
            $this$commonClose$iv.source.close();
            RealBufferedSource this_$iv$iv = $this$commonClose$iv;
            boolean $i$f$getBuffer = false;
            this_$iv$iv.bufferField.clear();
        }
    }

    @Override
    @NotNull
    public Timeout timeout() {
        RealBufferedSource $this$commonTimeout$iv = this;
        boolean $i$f$commonTimeout = false;
        return $this$commonTimeout$iv.source.timeout();
    }

    @NotNull
    public String toString() {
        RealBufferedSource $this$commonToString$iv = this;
        boolean $i$f$commonToString = false;
        return "buffer(" + $this$commonToString$iv.source + ')';
    }
}

