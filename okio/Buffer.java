/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.SourceDebugExtension
 */
package okio;

import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.collections.ArraysKt;
import kotlin.jvm.JvmField;
import kotlin.jvm.JvmName;
import kotlin.jvm.JvmOverloads;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.text.Charsets;
import okio.-SegmentedByteString;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ByteString;
import okio.Okio;
import okio.Options;
import okio.PeekSource;
import okio.Segment;
import okio.SegmentPool;
import okio.SegmentedByteString;
import okio.Sink;
import okio.Source;
import okio.Timeout;
import okio.TypedOptions;
import okio.internal.-Buffer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Metadata(mv={2, 1, 0}, k=1, xi=48, d1={"\u0000\u00b0\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u001a\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u0005\n\u0002\b\u0004\n\u0002\u0010\n\n\u0000\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\b\u001c\n\u0002\u0018\u0002\n\u0002\b\u0013\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u00002\u00020\u00012\u00020\u00022\u00020\u00032\u00020\u0004:\u0002\u0094\u0001B\u0007\u00a2\u0006\u0004\b\u0005\u0010\u0006J\b\u0010\u000f\u001a\u00020\u0000H\u0016J\b\u0010\u0012\u001a\u00020\u0013H\u0016J\b\u0010\u0014\u001a\u00020\u0000H\u0016J\b\u0010\u0015\u001a\u00020\u0000H\u0016J\b\u0010\u0016\u001a\u00020\u0017H\u0016J\u0010\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\nH\u0016J\u0010\u0010\u001b\u001a\u00020\u00172\u0006\u0010\u001a\u001a\u00020\nH\u0016J\b\u0010\u001c\u001a\u00020\u0001H\u0016J\b\u0010\u001d\u001a\u00020\u001eH\u0016J$\u0010\u001f\u001a\u00020\u00002\u0006\u0010 \u001a\u00020\u00132\b\b\u0002\u0010!\u001a\u00020\n2\b\b\u0002\u0010\u001a\u001a\u00020\nH\u0007J \u0010\u001f\u001a\u00020\u00002\u0006\u0010 \u001a\u00020\u00002\b\b\u0002\u0010!\u001a\u00020\n2\u0006\u0010\u001a\u001a\u00020\nJ\u0018\u0010\u001f\u001a\u00020\u00002\u0006\u0010 \u001a\u00020\u00002\b\b\u0002\u0010!\u001a\u00020\nJ\u001a\u0010\"\u001a\u00020\u00002\u0006\u0010 \u001a\u00020\u00132\b\b\u0002\u0010\u001a\u001a\u00020\nH\u0007J\u000e\u0010#\u001a\u00020\u00002\u0006\u0010$\u001a\u00020\u001eJ\u0016\u0010#\u001a\u00020\u00002\u0006\u0010$\u001a\u00020\u001e2\u0006\u0010\u001a\u001a\u00020\nJ \u0010#\u001a\u00020\u00192\u0006\u0010$\u001a\u00020\u001e2\u0006\u0010\u001a\u001a\u00020\n2\u0006\u0010%\u001a\u00020\u0017H\u0002J\u0006\u0010&\u001a\u00020\nJ\b\u0010'\u001a\u00020(H\u0016J\u0016\u0010)\u001a\u00020(2\u0006\u0010*\u001a\u00020\nH\u0087\u0002\u00a2\u0006\u0002\b+J\b\u0010,\u001a\u00020-H\u0016J\b\u0010.\u001a\u00020/H\u0016J\b\u00100\u001a\u00020\nH\u0016J\b\u00101\u001a\u00020-H\u0016J\b\u00102\u001a\u00020/H\u0016J\b\u00103\u001a\u00020\nH\u0016J\b\u00104\u001a\u00020\nH\u0016J\b\u00105\u001a\u00020\nH\u0016J\b\u00106\u001a\u000207H\u0016J\u0010\u00106\u001a\u0002072\u0006\u0010\u001a\u001a\u00020\nH\u0016J\u0010\u00108\u001a\u00020/2\u0006\u00109\u001a\u00020:H\u0016J'\u00108\u001a\u0004\u0018\u0001H;\"\b\b\u0000\u0010;*\u00020<2\f\u00109\u001a\b\u0012\u0004\u0012\u0002H;0=H\u0016\u00a2\u0006\u0002\u0010>J\u0018\u0010?\u001a\u00020\u00192\u0006\u0010@\u001a\u00020\u00002\u0006\u0010\u001a\u001a\u00020\nH\u0016J\u0010\u0010A\u001a\u00020\n2\u0006\u0010@\u001a\u00020BH\u0016J\b\u0010C\u001a\u00020DH\u0016J\u0010\u0010C\u001a\u00020D2\u0006\u0010\u001a\u001a\u00020\nH\u0016J\u0010\u0010E\u001a\u00020D2\u0006\u0010F\u001a\u00020GH\u0016J\u0018\u0010E\u001a\u00020D2\u0006\u0010\u001a\u001a\u00020\n2\u0006\u0010F\u001a\u00020GH\u0016J\n\u0010H\u001a\u0004\u0018\u00010DH\u0016J\b\u0010I\u001a\u00020DH\u0016J\u0010\u0010I\u001a\u00020D2\u0006\u0010J\u001a\u00020\nH\u0016J\b\u0010K\u001a\u00020/H\u0016J\b\u0010L\u001a\u00020MH\u0016J\u0010\u0010L\u001a\u00020M2\u0006\u0010\u001a\u001a\u00020\nH\u0016J\u0010\u0010N\u001a\u00020/2\u0006\u0010@\u001a\u00020MH\u0016J\u0010\u0010?\u001a\u00020\u00192\u0006\u0010@\u001a\u00020MH\u0016J \u0010N\u001a\u00020/2\u0006\u0010@\u001a\u00020M2\u0006\u0010!\u001a\u00020/2\u0006\u0010\u001a\u001a\u00020/H\u0016J\u0010\u0010N\u001a\u00020/2\u0006\u0010@\u001a\u00020OH\u0016J\u0006\u0010P\u001a\u00020\u0019J\u0010\u0010Q\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\nH\u0016J\u0010\u0010R\u001a\u00020\u00002\u0006\u0010S\u001a\u000207H\u0016J \u0010R\u001a\u00020\u00002\u0006\u0010S\u001a\u0002072\u0006\u0010!\u001a\u00020/2\u0006\u0010\u001a\u001a\u00020/H\u0016J\u0010\u0010T\u001a\u00020\u00002\u0006\u0010U\u001a\u00020DH\u0016J \u0010T\u001a\u00020\u00002\u0006\u0010U\u001a\u00020D2\u0006\u0010V\u001a\u00020/2\u0006\u0010W\u001a\u00020/H\u0016J\u0010\u0010X\u001a\u00020\u00002\u0006\u0010Y\u001a\u00020/H\u0016J\u0018\u0010Z\u001a\u00020\u00002\u0006\u0010U\u001a\u00020D2\u0006\u0010F\u001a\u00020GH\u0016J(\u0010Z\u001a\u00020\u00002\u0006\u0010U\u001a\u00020D2\u0006\u0010V\u001a\u00020/2\u0006\u0010W\u001a\u00020/2\u0006\u0010F\u001a\u00020GH\u0016J\u0010\u0010R\u001a\u00020\u00002\u0006\u0010[\u001a\u00020MH\u0016J \u0010R\u001a\u00020\u00002\u0006\u0010[\u001a\u00020M2\u0006\u0010!\u001a\u00020/2\u0006\u0010\u001a\u001a\u00020/H\u0016J\u0010\u0010R\u001a\u00020/2\u0006\u0010[\u001a\u00020OH\u0016J\u0010\u0010\\\u001a\u00020\n2\u0006\u0010[\u001a\u00020]H\u0016J\u0018\u0010R\u001a\u00020\u00002\u0006\u0010[\u001a\u00020]2\u0006\u0010\u001a\u001a\u00020\nH\u0016J\u0010\u0010^\u001a\u00020\u00002\u0006\u0010_\u001a\u00020/H\u0016J\u0010\u0010`\u001a\u00020\u00002\u0006\u0010a\u001a\u00020/H\u0016J\u0010\u0010b\u001a\u00020\u00002\u0006\u0010a\u001a\u00020/H\u0016J\u0010\u0010c\u001a\u00020\u00002\u0006\u0010d\u001a\u00020/H\u0016J\u0010\u0010e\u001a\u00020\u00002\u0006\u0010d\u001a\u00020/H\u0016J\u0010\u0010f\u001a\u00020\u00002\u0006\u0010g\u001a\u00020\nH\u0016J\u0010\u0010h\u001a\u00020\u00002\u0006\u0010g\u001a\u00020\nH\u0016J\u0010\u0010i\u001a\u00020\u00002\u0006\u0010g\u001a\u00020\nH\u0016J\u0010\u0010j\u001a\u00020\u00002\u0006\u0010g\u001a\u00020\nH\u0016J\u0015\u0010k\u001a\u00020\b2\u0006\u0010l\u001a\u00020/H\u0000\u00a2\u0006\u0002\bmJ\u0018\u0010R\u001a\u00020\u00192\u0006\u0010[\u001a\u00020\u00002\u0006\u0010\u001a\u001a\u00020\nH\u0016J\u0018\u0010N\u001a\u00020\n2\u0006\u0010@\u001a\u00020\u00002\u0006\u0010\u001a\u001a\u00020\nH\u0016J\u0010\u0010n\u001a\u00020\n2\u0006\u0010_\u001a\u00020(H\u0016J\u0018\u0010n\u001a\u00020\n2\u0006\u0010_\u001a\u00020(2\u0006\u0010o\u001a\u00020\nH\u0016J \u0010n\u001a\u00020\n2\u0006\u0010_\u001a\u00020(2\u0006\u0010o\u001a\u00020\n2\u0006\u0010p\u001a\u00020\nH\u0016J\u0010\u0010n\u001a\u00020\n2\u0006\u0010q\u001a\u000207H\u0016J\u0018\u0010n\u001a\u00020\n2\u0006\u0010q\u001a\u0002072\u0006\u0010o\u001a\u00020\nH\u0016J\u0010\u0010r\u001a\u00020\n2\u0006\u0010s\u001a\u000207H\u0016J\u0018\u0010r\u001a\u00020\n2\u0006\u0010s\u001a\u0002072\u0006\u0010o\u001a\u00020\nH\u0016J\u0018\u0010t\u001a\u00020\u00172\u0006\u0010!\u001a\u00020\n2\u0006\u0010q\u001a\u000207H\u0016J(\u0010t\u001a\u00020\u00172\u0006\u0010!\u001a\u00020\n2\u0006\u0010q\u001a\u0002072\u0006\u0010u\u001a\u00020/2\u0006\u0010\u001a\u001a\u00020/H\u0016J\b\u0010v\u001a\u00020\u0019H\u0016J\b\u0010w\u001a\u00020\u0017H\u0016J\b\u0010x\u001a\u00020\u0019H\u0016J\b\u0010y\u001a\u00020zH\u0016J\u0006\u0010{\u001a\u000207J\u0006\u0010|\u001a\u000207J\u0006\u0010}\u001a\u000207J\u0006\u0010~\u001a\u000207J\u0011\u0010\u007f\u001a\u0002072\u0007\u0010\u0080\u0001\u001a\u00020DH\u0002J\u0010\u0010\u0081\u0001\u001a\u0002072\u0007\u0010\u0082\u0001\u001a\u000207J\u0010\u0010\u0083\u0001\u001a\u0002072\u0007\u0010\u0082\u0001\u001a\u000207J\u0010\u0010\u0084\u0001\u001a\u0002072\u0007\u0010\u0082\u0001\u001a\u000207J\u001b\u0010\u0085\u0001\u001a\u0002072\u0007\u0010\u0080\u0001\u001a\u00020D2\u0007\u0010\u0082\u0001\u001a\u000207H\u0002J\u0015\u0010\u0086\u0001\u001a\u00020\u00172\t\u0010\u0087\u0001\u001a\u0004\u0018\u00010<H\u0096\u0002J\t\u0010\u0088\u0001\u001a\u00020/H\u0016J\t\u0010\u0089\u0001\u001a\u00020DH\u0016J\u0007\u0010\u008a\u0001\u001a\u00020\u0000J\t\u0010\u008b\u0001\u001a\u00020\u0000H\u0016J\u0007\u0010\u008c\u0001\u001a\u000207J\u000f\u0010\u008c\u0001\u001a\u0002072\u0006\u0010\u001a\u001a\u00020/J\u0016\u0010\u008d\u0001\u001a\u00030\u008e\u00012\n\b\u0002\u0010\u008f\u0001\u001a\u00030\u008e\u0001H\u0007J\u0016\u0010\u0090\u0001\u001a\u00030\u008e\u00012\n\b\u0002\u0010\u008f\u0001\u001a\u00030\u008e\u0001H\u0007J\u0017\u0010+\u001a\u00020(2\u0007\u0010\u0091\u0001\u001a\u00020\nH\u0007\u00a2\u0006\u0003\b\u0092\u0001J\u000e\u0010\u000b\u001a\u00020\nH\u0007\u00a2\u0006\u0003\b\u0093\u0001R\u0014\u0010\u0007\u001a\u0004\u0018\u00010\b8\u0000@\u0000X\u0081\u000e\u00a2\u0006\u0002\n\u0000R&\u0010\u000b\u001a\u00020\n2\u0006\u0010\t\u001a\u00020\n8G@@X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u0014\u0010\u000f\u001a\u00020\u00008VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0010\u0010\u0011\u00a8\u0006\u0095\u0001"}, d2={"Lokio/Buffer;", "Lokio/BufferedSource;", "Lokio/BufferedSink;", "", "Ljava/nio/channels/ByteChannel;", "<init>", "()V", "head", "Lokio/Segment;", "value", "", "size", "()J", "setSize$okio", "(J)V", "buffer", "getBuffer", "()Lokio/Buffer;", "outputStream", "Ljava/io/OutputStream;", "emitCompleteSegments", "emit", "exhausted", "", "require", "", "byteCount", "request", "peek", "inputStream", "Ljava/io/InputStream;", "copyTo", "out", "offset", "writeTo", "readFrom", "input", "forever", "completeSegmentByteCount", "readByte", "", "get", "pos", "getByte", "readShort", "", "readInt", "", "readLong", "readShortLe", "readIntLe", "readLongLe", "readDecimalLong", "readHexadecimalUnsignedLong", "readByteString", "Lokio/ByteString;", "select", "options", "Lokio/Options;", "T", "", "Lokio/TypedOptions;", "(Lokio/TypedOptions;)Ljava/lang/Object;", "readFully", "sink", "readAll", "Lokio/Sink;", "readUtf8", "", "readString", "charset", "Ljava/nio/charset/Charset;", "readUtf8Line", "readUtf8LineStrict", "limit", "readUtf8CodePoint", "readByteArray", "", "read", "Ljava/nio/ByteBuffer;", "clear", "skip", "write", "byteString", "writeUtf8", "string", "beginIndex", "endIndex", "writeUtf8CodePoint", "codePoint", "writeString", "source", "writeAll", "Lokio/Source;", "writeByte", "b", "writeShort", "s", "writeShortLe", "writeInt", "i", "writeIntLe", "writeLong", "v", "writeLongLe", "writeDecimalLong", "writeHexadecimalUnsignedLong", "writableSegment", "minimumCapacity", "writableSegment$okio", "indexOf", "fromIndex", "toIndex", "bytes", "indexOfElement", "targetBytes", "rangeEquals", "bytesOffset", "flush", "isOpen", "close", "timeout", "Lokio/Timeout;", "md5", "sha1", "sha256", "sha512", "digest", "algorithm", "hmacSha1", "key", "hmacSha256", "hmacSha512", "hmac", "equals", "other", "hashCode", "toString", "copy", "clone", "snapshot", "readUnsafe", "Lokio/Buffer$UnsafeCursor;", "unsafeCursor", "readAndWriteUnsafe", "index", "-deprecated_getByte", "-deprecated_size", "UnsafeCursor", "okio"})
@SourceDebugExtension(value={"SMAP\nBuffer.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Buffer.kt\nokio/Buffer\n+ 2 Util.kt\nokio/-SegmentedByteString\n+ 3 Buffer.kt\nokio/internal/-Buffer\n+ 4 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 5 BufferedSource.kt\nokio/internal/-BufferedSource\n*L\n1#1,644:1\n88#2:645\n85#2:678\n85#2:680\n73#2:740\n73#2:766\n82#2:805\n76#2:816\n88#2:1009\n73#2:1024\n85#2:1128\n88#2:1565\n242#3,32:646\n277#3,10:681\n290#3,18:691\n412#3,2:709\n110#3:711\n414#3:712\n112#3,18:713\n311#3,9:731\n320#3,15:741\n338#3,10:756\n348#3,3:767\n346#3,25:770\n374#3,10:795\n384#3:806\n382#3,9:807\n391#3,7:817\n389#3,20:824\n652#3,60:844\n715#3,56:904\n773#3:960\n776#3:961\n777#3,6:963\n787#3,7:969\n797#3,6:979\n805#3,5:985\n837#3,6:990\n847#3:996\n848#3,11:998\n859#3,5:1010\n868#3,9:1015\n878#3,61:1025\n603#3:1086\n606#3:1087\n607#3,5:1089\n614#3:1094\n617#3,7:1095\n626#3,20:1102\n418#3:1122\n421#3,5:1123\n426#3,10:1129\n437#3,7:1139\n442#3,2:1146\n943#3:1148\n944#3,87:1150\n1034#3,48:1237\n573#3:1285\n580#3,21:1286\n1085#3,7:1307\n1095#3,7:1314\n1105#3,4:1321\n1112#3,8:1325\n1123#3,10:1333\n1136#3,14:1343\n447#3,35:1357\n513#3,40:1392\n556#3:1432\n558#3,13:1434\n1153#3:1447\n1204#3:1448\n1205#3,39:1450\n1246#3,2:1489\n1248#3,4:1492\n1255#3,3:1496\n1259#3,4:1500\n110#3:1504\n1263#3,22:1505\n112#3,18:1527\n1289#3,2:1545\n1291#3,3:1548\n110#3:1551\n1294#3,13:1552\n1307#3,13:1566\n112#3,18:1579\n1324#3,2:1597\n1327#3:1600\n110#3:1601\n1328#3,50:1602\n112#3,18:1652\n1387#3,14:1670\n1404#3,32:1684\n1439#3,12:1716\n1454#3,18:1728\n1476#3:1746\n1477#3:1748\n1482#3,34:1749\n1#4:679\n1#4:962\n1#4:997\n1#4:1088\n1#4:1149\n1#4:1433\n1#4:1449\n1#4:1491\n1#4:1499\n1#4:1547\n1#4:1599\n1#4:1747\n26#5,3:976\n*S KotlinDebug\n*F\n+ 1 Buffer.kt\nokio/Buffer\n*L\n167#1:645\n197#1:678\n235#1:680\n261#1:740\n264#1:766\n267#1:805\n267#1:816\n337#1:1009\n340#1:1024\n376#1:1128\n486#1:1565\n181#1:646,32\n252#1:681,10\n255#1:691,18\n258#1:709,2\n258#1:711\n258#1:712\n258#1:713,18\n261#1:731,9\n261#1:741,15\n264#1:756,10\n264#1:767,3\n264#1:770,25\n267#1:795,10\n267#1:806\n267#1:807,9\n267#1:817,7\n267#1:824,20\n279#1:844,60\n282#1:904,56\n284#1:960\n287#1:961\n287#1:963,6\n289#1:969,7\n294#1:979,6\n297#1:985,5\n331#1:990,6\n337#1:996\n337#1:998,11\n337#1:1010,5\n340#1:1015,9\n340#1:1025,61\n342#1:1086\n345#1:1087\n345#1:1089,5\n347#1:1094\n350#1:1095,7\n353#1:1102,20\n373#1:1122\n376#1:1123,5\n376#1:1129,10\n378#1:1139,7\n381#1:1146,2\n386#1:1148\n386#1:1150,87\n389#1:1237,48\n412#1:1285\n418#1:1286,21\n439#1:1307,7\n443#1:1314,7\n445#1:1321,4\n447#1:1325,8\n451#1:1333,10\n455#1:1343,14\n459#1:1357,35\n462#1:1392,40\n465#1:1432\n465#1:1434,13\n467#1:1447\n467#1:1448\n467#1:1450,39\n469#1:1489,2\n469#1:1492,4\n480#1:1496,3\n480#1:1500,4\n480#1:1504\n480#1:1505,22\n480#1:1527,18\n486#1:1545,2\n486#1:1548,3\n486#1:1551\n486#1:1552,13\n486#1:1566,13\n486#1:1579,18\n491#1:1597,2\n491#1:1600\n491#1:1601\n491#1:1602,50\n491#1:1652,18\n501#1:1670,14\n571#1:1684,32\n573#1:1716,12\n581#1:1728,18\n589#1:1746\n589#1:1748\n591#1:1749,34\n287#1:962\n337#1:997\n345#1:1088\n386#1:1149\n465#1:1433\n467#1:1449\n469#1:1491\n480#1:1499\n486#1:1547\n491#1:1599\n589#1:1747\n291#1:976,3\n*E\n"})
public final class Buffer
implements BufferedSource,
BufferedSink,
Cloneable,
ByteChannel {
    @JvmField
    @Nullable
    public Segment head;
    private long size;

    @JvmName(name="size")
    public final long size() {
        return this.size;
    }

    public final void setSize$okio(long l2) {
        this.size = l2;
    }

    @Override
    @NotNull
    public Buffer buffer() {
        return this;
    }

    @Override
    @NotNull
    public Buffer getBuffer() {
        return this;
    }

    @Override
    @NotNull
    public OutputStream outputStream() {
        return new OutputStream(this){
            final /* synthetic */ Buffer this$0;
            {
                this.this$0 = $receiver;
            }

            public void write(int b2) {
                this.this$0.writeByte(b2);
            }

            public void write(byte[] data, int offset, int byteCount) {
                Intrinsics.checkNotNullParameter(data, "data");
                this.this$0.write(data, offset, byteCount);
            }

            public void flush() {
            }

            public void close() {
            }

            public String toString() {
                return this.this$0 + ".outputStream()";
            }
        };
    }

    @Override
    @NotNull
    public Buffer emitCompleteSegments() {
        return this;
    }

    @Override
    @NotNull
    public Buffer emit() {
        return this;
    }

    @Override
    public boolean exhausted() {
        return this.size == 0L;
    }

    @Override
    public void require(long byteCount) throws EOFException {
        if (this.size < byteCount) {
            throw new EOFException();
        }
    }

    @Override
    public boolean request(long byteCount) {
        return this.size >= byteCount;
    }

    @Override
    @NotNull
    public BufferedSource peek() {
        return Okio.buffer(new PeekSource(this));
    }

    @Override
    @NotNull
    public InputStream inputStream() {
        return new InputStream(this){
            final /* synthetic */ Buffer this$0;
            {
                this.this$0 = $receiver;
            }

            /*
             * WARNING - void declaration
             */
            public int read() {
                int n2;
                if (this.this$0.size() > 0L) {
                    void $this$and$iv;
                    byte by = this.this$0.readByte();
                    int other$iv = 255;
                    boolean $i$f$and = false;
                    n2 = $this$and$iv & other$iv;
                } else {
                    n2 = -1;
                }
                return n2;
            }

            public int read(byte[] sink2, int offset, int byteCount) {
                Intrinsics.checkNotNullParameter(sink2, "sink");
                return this.this$0.read(sink2, offset, byteCount);
            }

            /*
             * WARNING - void declaration
             */
            public int available() {
                void a$iv;
                long l2 = this.this$0.size();
                int b$iv = Integer.MAX_VALUE;
                boolean $i$f$minOf = false;
                return (int)Math.min((long)a$iv, (long)b$iv);
            }

            public void close() {
            }

            public String toString() {
                return this.this$0 + ".inputStream()";
            }
        };
    }

    @JvmOverloads
    @NotNull
    public final Buffer copyTo(@NotNull OutputStream out, long offset, long byteCount) throws IOException {
        Intrinsics.checkNotNullParameter(out, "out");
        long offset2 = offset;
        long byteCount2 = byteCount;
        -SegmentedByteString.checkOffsetAndCount(this.size, offset2, byteCount2);
        if (byteCount2 == 0L) {
            return this;
        }
        Segment s2 = this.head;
        while (true) {
            Segment segment = s2;
            Intrinsics.checkNotNull(segment);
            if (offset2 < (long)(segment.limit - s2.pos)) break;
            offset2 -= (long)(s2.limit - s2.pos);
            s2 = s2.next;
        }
        while (byteCount2 > 0L) {
            Segment segment = s2;
            Intrinsics.checkNotNull(segment);
            int pos = (int)((long)segment.pos + offset2);
            int a$iv = s2.limit - pos;
            boolean $i$f$minOf = false;
            int toCopy = (int)Math.min((long)a$iv, byteCount2);
            out.write(s2.data, pos, toCopy);
            byteCount2 -= (long)toCopy;
            offset2 = 0L;
            s2 = s2.next;
        }
        return this;
    }

    public static /* synthetic */ Buffer copyTo$default(Buffer buffer, OutputStream outputStream2, long l2, long l3, int n2, Object object) throws IOException {
        if ((n2 & 2) != 0) {
            l2 = 0L;
        }
        if ((n2 & 4) != 0) {
            l3 = buffer.size - l2;
        }
        return buffer.copyTo(outputStream2, l2, l3);
    }

    @NotNull
    public final Buffer copyTo(@NotNull Buffer out, long offset, long byteCount) {
        Buffer buffer;
        Intrinsics.checkNotNullParameter(out, "out");
        Buffer $this$commonCopyTo$iv = this;
        boolean $i$f$commonCopyTo = false;
        long offset$iv = offset;
        long byteCount$iv = byteCount;
        -SegmentedByteString.checkOffsetAndCount($this$commonCopyTo$iv.size(), offset$iv, byteCount$iv);
        if (byteCount$iv == 0L) {
            buffer = $this$commonCopyTo$iv;
        } else {
            out.setSize$okio(out.size() + byteCount$iv);
            Segment s$iv = $this$commonCopyTo$iv.head;
            while (true) {
                Segment segment = s$iv;
                Intrinsics.checkNotNull(segment);
                if (offset$iv < (long)(segment.limit - s$iv.pos)) break;
                offset$iv -= (long)(s$iv.limit - s$iv.pos);
                s$iv = s$iv.next;
            }
            while (byteCount$iv > 0L) {
                Segment segment = s$iv;
                Intrinsics.checkNotNull(segment);
                Segment copy$iv = segment.sharedCopy();
                copy$iv.pos += (int)offset$iv;
                copy$iv.limit = Math.min(copy$iv.pos + (int)byteCount$iv, copy$iv.limit);
                if (out.head == null) {
                    out.head = copy$iv.next = (copy$iv.prev = copy$iv);
                } else {
                    Segment segment2 = out.head;
                    Intrinsics.checkNotNull(segment2);
                    Segment segment3 = segment2.prev;
                    Intrinsics.checkNotNull(segment3);
                    segment3.push(copy$iv);
                }
                byteCount$iv -= (long)(copy$iv.limit - copy$iv.pos);
                offset$iv = 0L;
                s$iv = s$iv.next;
            }
            buffer = $this$commonCopyTo$iv;
        }
        return buffer;
    }

    public static /* synthetic */ Buffer copyTo$default(Buffer buffer, Buffer buffer2, long l2, long l3, int n2, Object object) {
        if ((n2 & 2) != 0) {
            l2 = 0L;
        }
        return buffer.copyTo(buffer2, l2, l3);
    }

    @NotNull
    public final Buffer copyTo(@NotNull Buffer out, long offset) {
        Intrinsics.checkNotNullParameter(out, "out");
        return this.copyTo(out, offset, this.size - offset);
    }

    public static /* synthetic */ Buffer copyTo$default(Buffer buffer, Buffer buffer2, long l2, int n2, Object object) {
        if ((n2 & 2) != 0) {
            l2 = 0L;
        }
        return buffer.copyTo(buffer2, l2);
    }

    @JvmOverloads
    @NotNull
    public final Buffer writeTo(@NotNull OutputStream out, long byteCount) throws IOException {
        int toCopy;
        long byteCount2;
        Intrinsics.checkNotNullParameter(out, "out");
        -SegmentedByteString.checkOffsetAndCount(this.size, 0L, byteCount2);
        Segment s2 = this.head;
        for (byteCount2 = byteCount; byteCount2 > 0L; byteCount2 -= (long)toCopy) {
            Segment segment = s2;
            Intrinsics.checkNotNull(segment);
            int b$iv22 = segment.limit - s2.pos;
            boolean $i$f$minOf = false;
            toCopy = (int)Math.min(byteCount2, (long)b$iv22);
            out.write(s2.data, s2.pos, toCopy);
            Segment b$iv22 = s2;
            b$iv22.pos += toCopy;
            this.size -= (long)toCopy;
            if (s2.pos != s2.limit) continue;
            Segment toRecycle = s2;
            this.head = s2 = toRecycle.pop();
            SegmentPool.recycle(toRecycle);
        }
        return this;
    }

    public static /* synthetic */ Buffer writeTo$default(Buffer buffer, OutputStream outputStream2, long l2, int n2, Object object) throws IOException {
        if ((n2 & 2) != 0) {
            l2 = buffer.size;
        }
        return buffer.writeTo(outputStream2, l2);
    }

    @NotNull
    public final Buffer readFrom(@NotNull InputStream input) throws IOException {
        Intrinsics.checkNotNullParameter(input, "input");
        this.readFrom(input, Long.MAX_VALUE, true);
        return this;
    }

    @NotNull
    public final Buffer readFrom(@NotNull InputStream input, long byteCount) throws IOException {
        Intrinsics.checkNotNullParameter(input, "input");
        if (!(byteCount >= 0L)) {
            boolean bl = false;
            String string = "byteCount < 0: " + byteCount;
            throw new IllegalArgumentException(string.toString());
        }
        this.readFrom(input, byteCount, false);
        return this;
    }

    private final void readFrom(InputStream input, long byteCount, boolean forever) throws IOException {
        int bytesRead;
        for (long byteCount2 = byteCount; byteCount2 > 0L || forever; byteCount2 -= (long)bytesRead) {
            Segment tail = this.writableSegment$okio(1);
            int b$iv = 8192 - tail.limit;
            boolean $i$f$minOf = false;
            int maxToCopy = (int)Math.min(byteCount2, (long)b$iv);
            bytesRead = input.read(tail.data, tail.limit, maxToCopy);
            if (bytesRead == -1) {
                if (tail.pos == tail.limit) {
                    this.head = tail.pop();
                    SegmentPool.recycle(tail);
                }
                if (forever) {
                    return;
                }
                throw new EOFException();
            }
            tail.limit += bytesRead;
            this.size += (long)bytesRead;
        }
    }

    /*
     * WARNING - void declaration
     */
    public final long completeSegmentByteCount() {
        long l2;
        Buffer $this$commonCompleteSegmentByteCount$iv = this;
        boolean $i$f$commonCompleteSegmentByteCount = false;
        long result$iv = $this$commonCompleteSegmentByteCount$iv.size();
        if (result$iv == 0L) {
            l2 = 0L;
        } else {
            void var3_3;
            Segment segment = $this$commonCompleteSegmentByteCount$iv.head;
            Intrinsics.checkNotNull(segment);
            Segment segment2 = segment.prev;
            Intrinsics.checkNotNull(segment2);
            Segment tail$iv = segment2;
            if (tail$iv.limit < 8192 && tail$iv.owner) {
                result$iv -= (long)(tail$iv.limit - tail$iv.pos);
            }
            l2 = var3_3;
        }
        return l2;
    }

    @Override
    public byte readByte() throws EOFException {
        Buffer $this$commonReadByte$iv = this;
        boolean $i$f$commonReadByte = false;
        if ($this$commonReadByte$iv.size() == 0L) {
            throw new EOFException();
        }
        Segment segment = $this$commonReadByte$iv.head;
        Intrinsics.checkNotNull(segment);
        Segment segment$iv = segment;
        int pos$iv = segment$iv.pos;
        int limit$iv = segment$iv.limit;
        byte[] data$iv = segment$iv.data;
        byte b$iv = data$iv[pos$iv++];
        $this$commonReadByte$iv.setSize$okio($this$commonReadByte$iv.size() - 1L);
        if (pos$iv == limit$iv) {
            $this$commonReadByte$iv.head = segment$iv.pop();
            SegmentPool.recycle(segment$iv);
        } else {
            segment$iv.pos = pos$iv;
        }
        return b$iv;
    }

    /*
     * WARNING - void declaration
     */
    @JvmName(name="getByte")
    public final byte getByte(long pos) {
        byte by;
        Buffer $this$commonGet$iv = this;
        boolean $i$f$commonGet = false;
        -SegmentedByteString.checkOffsetAndCount($this$commonGet$iv.size(), pos, 1L);
        Buffer $this$seek$iv$iv = $this$commonGet$iv;
        boolean $i$f$seek = false;
        Segment segment = $this$seek$iv$iv.head;
        if (segment == null) {
            void offset$iv;
            long l2 = -1L;
            Object s$iv = null;
            boolean bl = false;
            Object v1 = s$iv;
            Intrinsics.checkNotNull(v1);
            by = v1.data[(int)((long)s$iv.pos + pos - offset$iv)];
        } else {
            Segment s$iv$iv = segment;
            if ($this$seek$iv$iv.size() - pos < pos) {
                void offset$iv;
                long offset$iv$iv;
                for (offset$iv$iv = $this$seek$iv$iv.size(); offset$iv$iv > pos; offset$iv$iv -= (long)(s$iv$iv.limit - s$iv$iv.pos)) {
                    Intrinsics.checkNotNull(s$iv$iv.prev);
                }
                long l3 = offset$iv$iv;
                Segment s$iv = s$iv$iv;
                boolean bl = false;
                Segment segment2 = s$iv;
                Intrinsics.checkNotNull(segment2);
                by = segment2.data[(int)((long)s$iv.pos + pos - offset$iv)];
            } else {
                void offset$iv;
                long nextOffset$iv$iv;
                long offset$iv$iv = 0L;
                while ((nextOffset$iv$iv = offset$iv$iv + (long)(s$iv$iv.limit - s$iv$iv.pos)) <= pos) {
                    Intrinsics.checkNotNull(s$iv$iv.next);
                    offset$iv$iv = nextOffset$iv$iv;
                }
                long l4 = offset$iv$iv;
                Segment s$iv = s$iv$iv;
                boolean bl = false;
                Segment segment3 = s$iv;
                Intrinsics.checkNotNull(segment3);
                by = segment3.data[(int)((long)s$iv.pos + pos - offset$iv)];
            }
        }
        return by;
    }

    @Override
    public short readShort() throws EOFException {
        short s2;
        Buffer $this$commonReadShort$iv = this;
        boolean $i$f$commonReadShort = false;
        if ($this$commonReadShort$iv.size() < 2L) {
            throw new EOFException();
        }
        Segment segment = $this$commonReadShort$iv.head;
        Intrinsics.checkNotNull(segment);
        Segment segment$iv = segment;
        int pos$iv = segment$iv.pos;
        int limit$iv = segment$iv.limit;
        if (limit$iv - pos$iv < 2) {
            byte $this$and$iv$iv;
            byte by = $this$commonReadShort$iv.readByte();
            int other$iv$iv = 255;
            boolean $i$f$and = false;
            int n2 = ($this$and$iv$iv & other$iv$iv) << 8;
            $this$and$iv$iv = $this$commonReadShort$iv.readByte();
            other$iv$iv = 255;
            $i$f$and = false;
            int s$iv = n2 | $this$and$iv$iv & other$iv$iv;
            s2 = (short)s$iv;
        } else {
            byte $this$and$iv$iv;
            byte[] data$iv = segment$iv.data;
            byte other$iv$iv = data$iv[pos$iv++];
            int other$iv$iv2 = 255;
            boolean $i$f$and = false;
            int n3 = ($this$and$iv$iv & other$iv$iv2) << 8;
            $this$and$iv$iv = data$iv[pos$iv++];
            other$iv$iv2 = 255;
            $i$f$and = false;
            int s$iv = n3 | $this$and$iv$iv & other$iv$iv2;
            $this$commonReadShort$iv.setSize$okio($this$commonReadShort$iv.size() - 2L);
            if (pos$iv == limit$iv) {
                $this$commonReadShort$iv.head = segment$iv.pop();
                SegmentPool.recycle(segment$iv);
            } else {
                segment$iv.pos = pos$iv;
            }
            s2 = (short)s$iv;
        }
        return s2;
    }

    @Override
    public int readInt() throws EOFException {
        int n2;
        Buffer $this$commonReadInt$iv = this;
        boolean $i$f$commonReadInt = false;
        if ($this$commonReadInt$iv.size() < 4L) {
            throw new EOFException();
        }
        Segment segment = $this$commonReadInt$iv.head;
        Intrinsics.checkNotNull(segment);
        Segment segment$iv = segment;
        int pos$iv = segment$iv.pos;
        int limit$iv = segment$iv.limit;
        if ((long)(limit$iv - pos$iv) < 4L) {
            byte $this$and$iv$iv;
            byte by = $this$commonReadInt$iv.readByte();
            int other$iv$iv = 255;
            boolean $i$f$and = false;
            int n3 = ($this$and$iv$iv & other$iv$iv) << 24;
            $this$and$iv$iv = $this$commonReadInt$iv.readByte();
            other$iv$iv = 255;
            $i$f$and = false;
            int n4 = n3 | ($this$and$iv$iv & other$iv$iv) << 16;
            $this$and$iv$iv = $this$commonReadInt$iv.readByte();
            other$iv$iv = 255;
            $i$f$and = false;
            int n5 = n4 | ($this$and$iv$iv & other$iv$iv) << 8;
            $this$and$iv$iv = $this$commonReadInt$iv.readByte();
            other$iv$iv = 255;
            $i$f$and = false;
            n2 = n5 | $this$and$iv$iv & other$iv$iv;
        } else {
            byte $this$and$iv$iv;
            byte[] data$iv = segment$iv.data;
            byte $i$f$and = data$iv[pos$iv++];
            int other$iv$iv = 255;
            boolean $i$f$and2 = false;
            int n6 = ($this$and$iv$iv & other$iv$iv) << 24;
            $this$and$iv$iv = data$iv[pos$iv++];
            other$iv$iv = 255;
            $i$f$and2 = false;
            int n7 = n6 | ($this$and$iv$iv & other$iv$iv) << 16;
            $this$and$iv$iv = data$iv[pos$iv++];
            other$iv$iv = 255;
            $i$f$and2 = false;
            int n8 = n7 | ($this$and$iv$iv & other$iv$iv) << 8;
            $this$and$iv$iv = data$iv[pos$iv++];
            other$iv$iv = 255;
            $i$f$and2 = false;
            int i$iv = n8 | $this$and$iv$iv & other$iv$iv;
            $this$commonReadInt$iv.setSize$okio($this$commonReadInt$iv.size() - 4L);
            if (pos$iv == limit$iv) {
                $this$commonReadInt$iv.head = segment$iv.pop();
                SegmentPool.recycle(segment$iv);
            } else {
                segment$iv.pos = pos$iv;
            }
            n2 = i$iv;
        }
        return n2;
    }

    @Override
    public long readLong() throws EOFException {
        long l2;
        Buffer $this$commonReadLong$iv = this;
        boolean $i$f$commonReadLong = false;
        if ($this$commonReadLong$iv.size() < 8L) {
            throw new EOFException();
        }
        Segment segment = $this$commonReadLong$iv.head;
        Intrinsics.checkNotNull(segment);
        Segment segment$iv = segment;
        int pos$iv = segment$iv.pos;
        int limit$iv = segment$iv.limit;
        if ((long)(limit$iv - pos$iv) < 8L) {
            int $this$and$iv$iv;
            int n2 = $this$commonReadLong$iv.readInt();
            long other$iv$iv = 0xFFFFFFFFL;
            boolean $i$f$and = false;
            long l3 = ((long)$this$and$iv$iv & other$iv$iv) << 32;
            $this$and$iv$iv = $this$commonReadLong$iv.readInt();
            other$iv$iv = 0xFFFFFFFFL;
            $i$f$and = false;
            l2 = l3 | (long)$this$and$iv$iv & other$iv$iv;
        } else {
            byte $this$and$iv$iv;
            byte[] data$iv = segment$iv.data;
            byte $i$f$and = data$iv[pos$iv++];
            long other$iv$iv = 255L;
            boolean $i$f$and2 = false;
            long l4 = ((long)$this$and$iv$iv & other$iv$iv) << 56;
            $this$and$iv$iv = data$iv[pos$iv++];
            other$iv$iv = 255L;
            $i$f$and2 = false;
            long l5 = l4 | ((long)$this$and$iv$iv & other$iv$iv) << 48;
            $this$and$iv$iv = data$iv[pos$iv++];
            other$iv$iv = 255L;
            $i$f$and2 = false;
            long l6 = l5 | ((long)$this$and$iv$iv & other$iv$iv) << 40;
            $this$and$iv$iv = data$iv[pos$iv++];
            other$iv$iv = 255L;
            $i$f$and2 = false;
            long l7 = l6 | ((long)$this$and$iv$iv & other$iv$iv) << 32;
            $this$and$iv$iv = data$iv[pos$iv++];
            other$iv$iv = 255L;
            $i$f$and2 = false;
            long l8 = l7 | ((long)$this$and$iv$iv & other$iv$iv) << 24;
            $this$and$iv$iv = data$iv[pos$iv++];
            other$iv$iv = 255L;
            $i$f$and2 = false;
            long l9 = l8 | ((long)$this$and$iv$iv & other$iv$iv) << 16;
            $this$and$iv$iv = data$iv[pos$iv++];
            other$iv$iv = 255L;
            $i$f$and2 = false;
            long l10 = l9 | ((long)$this$and$iv$iv & other$iv$iv) << 8;
            $this$and$iv$iv = data$iv[pos$iv++];
            other$iv$iv = 255L;
            $i$f$and2 = false;
            long v$iv = l10 | (long)$this$and$iv$iv & other$iv$iv;
            $this$commonReadLong$iv.setSize$okio($this$commonReadLong$iv.size() - 8L);
            if (pos$iv == limit$iv) {
                $this$commonReadLong$iv.head = segment$iv.pop();
                SegmentPool.recycle(segment$iv);
            } else {
                segment$iv.pos = pos$iv;
            }
            l2 = v$iv;
        }
        return l2;
    }

    @Override
    public short readShortLe() throws EOFException {
        return -SegmentedByteString.reverseBytes(this.readShort());
    }

    @Override
    public int readIntLe() throws EOFException {
        return -SegmentedByteString.reverseBytes(this.readInt());
    }

    @Override
    public long readLongLe() throws EOFException {
        return -SegmentedByteString.reverseBytes(this.readLong());
    }

    @Override
    public long readDecimalLong() throws EOFException {
        int minimumSeen$iv;
        Buffer $this$commonReadDecimalLong$iv = this;
        boolean $i$f$commonReadDecimalLong = false;
        if ($this$commonReadDecimalLong$iv.size() == 0L) {
            throw new EOFException();
        }
        long value$iv = 0L;
        int seen$iv = 0;
        boolean negative$iv = false;
        boolean done$iv = false;
        long overflowDigit$iv = -7L;
        do {
            Segment segment$iv;
            Intrinsics.checkNotNull($this$commonReadDecimalLong$iv.head);
            byte[] data$iv = segment$iv.data;
            int pos$iv = segment$iv.pos;
            int limit$iv = segment$iv.limit;
            while (pos$iv < limit$iv) {
                byte b$iv = data$iv[pos$iv];
                if (b$iv >= 48 && b$iv <= 57) {
                    int digit$iv = 48 - b$iv;
                    if (value$iv < -922337203685477580L || value$iv == -922337203685477580L && (long)digit$iv < overflowDigit$iv) {
                        Buffer buffer$iv = new Buffer().writeDecimalLong(value$iv).writeByte(b$iv);
                        if (!negative$iv) {
                            buffer$iv.readByte();
                        }
                        throw new NumberFormatException("Number too large: " + buffer$iv.readUtf8());
                    }
                    value$iv *= 10L;
                    value$iv += (long)digit$iv;
                } else if (b$iv == 45 && seen$iv == 0) {
                    negative$iv = true;
                    --overflowDigit$iv;
                } else {
                    done$iv = true;
                    break;
                }
                ++pos$iv;
                ++seen$iv;
            }
            if (pos$iv == limit$iv) {
                $this$commonReadDecimalLong$iv.head = segment$iv.pop();
                SegmentPool.recycle(segment$iv);
                continue;
            }
            segment$iv.pos = pos$iv;
        } while (!done$iv && $this$commonReadDecimalLong$iv.head != null);
        $this$commonReadDecimalLong$iv.setSize$okio($this$commonReadDecimalLong$iv.size() - (long)seen$iv);
        int n2 = minimumSeen$iv = negative$iv ? 2 : 1;
        if (seen$iv < minimumSeen$iv) {
            if ($this$commonReadDecimalLong$iv.size() == 0L) {
                throw new EOFException();
            }
            String expected$iv = negative$iv ? "Expected a digit" : "Expected a digit or '-'";
            throw new NumberFormatException(expected$iv + " but was 0x" + -SegmentedByteString.toHexString($this$commonReadDecimalLong$iv.getByte(0L)));
        }
        return negative$iv ? value$iv : -value$iv;
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public long readHexadecimalUnsignedLong() throws EOFException {
        void var3_3;
        Buffer $this$commonReadHexadecimalUnsignedLong$iv = this;
        boolean $i$f$commonReadHexadecimalUnsignedLong = false;
        if ($this$commonReadHexadecimalUnsignedLong$iv.size() == 0L) {
            throw new EOFException();
        }
        long value$iv = 0L;
        int seen$iv = 0;
        boolean done$iv = false;
        do {
            Segment segment$iv;
            Intrinsics.checkNotNull($this$commonReadHexadecimalUnsignedLong$iv.head);
            byte[] data$iv = segment$iv.data;
            int pos$iv = segment$iv.pos;
            int limit$iv = segment$iv.limit;
            while (pos$iv < limit$iv) {
                int digit$iv = 0;
                byte b$iv = data$iv[pos$iv];
                if (b$iv >= 48 && b$iv <= 57) {
                    digit$iv = b$iv - 48;
                } else if (b$iv >= 97 && b$iv <= 102) {
                    digit$iv = b$iv - 97 + 10;
                } else if (b$iv >= 65 && b$iv <= 70) {
                    digit$iv = b$iv - 65 + 10;
                } else {
                    if (seen$iv == 0) {
                        throw new NumberFormatException("Expected leading [0-9a-fA-F] character but was 0x" + -SegmentedByteString.toHexString(b$iv));
                    }
                    done$iv = true;
                    break;
                }
                if ((value$iv & 0xF000000000000000L) != 0L) {
                    Buffer buffer$iv = new Buffer().writeHexadecimalUnsignedLong(value$iv).writeByte(b$iv);
                    throw new NumberFormatException("Number too large: " + buffer$iv.readUtf8());
                }
                value$iv <<= 4;
                value$iv |= (long)digit$iv;
                ++pos$iv;
                ++seen$iv;
            }
            if (pos$iv == limit$iv) {
                $this$commonReadHexadecimalUnsignedLong$iv.head = segment$iv.pop();
                SegmentPool.recycle(segment$iv);
                continue;
            }
            segment$iv.pos = pos$iv;
        } while (!done$iv && $this$commonReadHexadecimalUnsignedLong$iv.head != null);
        $this$commonReadHexadecimalUnsignedLong$iv.setSize$okio($this$commonReadHexadecimalUnsignedLong$iv.size() - (long)seen$iv);
        return (long)var3_3;
    }

    @Override
    @NotNull
    public ByteString readByteString() {
        Buffer $this$commonReadByteString$iv = this;
        boolean $i$f$commonReadByteString = false;
        return $this$commonReadByteString$iv.readByteString($this$commonReadByteString$iv.size());
    }

    @Override
    @NotNull
    public ByteString readByteString(long byteCount) throws EOFException {
        ByteString byteString;
        Buffer $this$commonReadByteString$iv = this;
        boolean $i$f$commonReadByteString = false;
        if (!(byteCount >= 0L && byteCount <= Integer.MAX_VALUE)) {
            boolean $i$a$-require--Buffer$commonReadByteString$1$iv22 = false;
            String $i$a$-require--Buffer$commonReadByteString$1$iv22 = "byteCount: " + byteCount;
            throw new IllegalArgumentException($i$a$-require--Buffer$commonReadByteString$1$iv22.toString());
        }
        if ($this$commonReadByteString$iv.size() < byteCount) {
            throw new EOFException();
        }
        if (byteCount >= 4096L) {
            ByteString byteString2;
            ByteString it$iv = byteString2 = $this$commonReadByteString$iv.snapshot((int)byteCount);
            boolean bl = false;
            $this$commonReadByteString$iv.skip(byteCount);
            byteString = byteString2;
        } else {
            byteString = new ByteString($this$commonReadByteString$iv.readByteArray(byteCount));
        }
        return byteString;
    }

    @Override
    public int select(@NotNull Options options) {
        int n2;
        Intrinsics.checkNotNullParameter(options, "options");
        Buffer $this$commonSelect$iv = this;
        boolean $i$f$commonSelect = false;
        int index$iv = -Buffer.selectPrefix$default($this$commonSelect$iv, options, false, 2, null);
        if (index$iv == -1) {
            n2 = -1;
        } else {
            int selectedSize$iv = options.getByteStrings$okio()[index$iv].size();
            $this$commonSelect$iv.skip(selectedSize$iv);
            n2 = index$iv;
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
    public void readFully(@NotNull Buffer sink2, long byteCount) throws EOFException {
        Intrinsics.checkNotNullParameter(sink2, "sink");
        Buffer $this$commonReadFully$iv = this;
        boolean $i$f$commonReadFully = false;
        if ($this$commonReadFully$iv.size() < byteCount) {
            sink2.write($this$commonReadFully$iv, $this$commonReadFully$iv.size());
            throw new EOFException();
        }
        sink2.write($this$commonReadFully$iv, byteCount);
    }

    @Override
    public long readAll(@NotNull Sink sink2) throws IOException {
        Intrinsics.checkNotNullParameter(sink2, "sink");
        Buffer $this$commonReadAll$iv = this;
        boolean $i$f$commonReadAll = false;
        long byteCount$iv = $this$commonReadAll$iv.size();
        if (byteCount$iv > 0L) {
            sink2.write($this$commonReadAll$iv, byteCount$iv);
        }
        return byteCount$iv;
    }

    @Override
    @NotNull
    public String readUtf8() {
        return this.readString(this.size, Charsets.UTF_8);
    }

    @Override
    @NotNull
    public String readUtf8(long byteCount) throws EOFException {
        return this.readString(byteCount, Charsets.UTF_8);
    }

    @Override
    @NotNull
    public String readString(@NotNull Charset charset) {
        Intrinsics.checkNotNullParameter(charset, "charset");
        return this.readString(this.size, charset);
    }

    @Override
    @NotNull
    public String readString(long byteCount, @NotNull Charset charset) throws EOFException {
        Intrinsics.checkNotNullParameter(charset, "charset");
        if (!(byteCount >= 0L && byteCount <= Integer.MAX_VALUE)) {
            boolean $i$a$-require-Buffer$readString$22 = false;
            String $i$a$-require-Buffer$readString$22 = "byteCount: " + byteCount;
            throw new IllegalArgumentException($i$a$-require-Buffer$readString$22.toString());
        }
        if (this.size < byteCount) {
            throw new EOFException();
        }
        if (byteCount == 0L) {
            return "";
        }
        Segment segment = this.head;
        Intrinsics.checkNotNull(segment);
        Segment s2 = segment;
        if ((long)s2.pos + byteCount > (long)s2.limit) {
            return new String(this.readByteArray(byteCount), charset);
        }
        String result = new String(s2.data, s2.pos, (int)byteCount, charset);
        s2.pos += (int)byteCount;
        this.size -= byteCount;
        if (s2.pos == s2.limit) {
            this.head = s2.pop();
            SegmentPool.recycle(s2);
        }
        return result;
    }

    @Override
    @Nullable
    public String readUtf8Line() throws EOFException {
        Buffer $this$commonReadUtf8Line$iv = this;
        boolean $i$f$commonReadUtf8Line = false;
        long newline$iv = $this$commonReadUtf8Line$iv.indexOf((byte)10);
        return newline$iv != -1L ? -Buffer.readUtf8Line($this$commonReadUtf8Line$iv, newline$iv) : ($this$commonReadUtf8Line$iv.size() != 0L ? $this$commonReadUtf8Line$iv.readUtf8($this$commonReadUtf8Line$iv.size()) : null);
    }

    @Override
    @NotNull
    public String readUtf8LineStrict() throws EOFException {
        return this.readUtf8LineStrict(Long.MAX_VALUE);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    @NotNull
    public String readUtf8LineStrict(long limit) throws EOFException {
        String string;
        Buffer $this$commonReadUtf8LineStrict$iv = this;
        boolean $i$f$commonReadUtf8LineStrict = false;
        if (!(limit >= 0L)) {
            boolean bl = false;
            String string2 = "limit < 0: " + limit;
            throw new IllegalArgumentException(string2.toString());
        }
        long scanLength$iv = limit == Long.MAX_VALUE ? Long.MAX_VALUE : limit + 1L;
        long newline$iv = $this$commonReadUtf8LineStrict$iv.indexOf((byte)10, 0L, scanLength$iv);
        if (newline$iv != -1L) {
            string = -Buffer.readUtf8Line($this$commonReadUtf8LineStrict$iv, newline$iv);
        } else if (scanLength$iv < $this$commonReadUtf8LineStrict$iv.size() && $this$commonReadUtf8LineStrict$iv.getByte(scanLength$iv - 1L) == 13 && $this$commonReadUtf8LineStrict$iv.getByte(scanLength$iv) == 10) {
            string = -Buffer.readUtf8Line($this$commonReadUtf8LineStrict$iv, scanLength$iv);
        } else {
            void a$iv$iv;
            Buffer data$iv = new Buffer();
            int n2 = 32;
            long b$iv$iv = $this$commonReadUtf8LineStrict$iv.size();
            boolean $i$f$minOf = false;
            $this$commonReadUtf8LineStrict$iv.copyTo(data$iv, 0L, Math.min((long)a$iv$iv, b$iv$iv));
            throw new EOFException("\\n not found: limit=" + Math.min($this$commonReadUtf8LineStrict$iv.size(), limit) + " content=" + data$iv.readByteString().hex() + '\u2026');
        }
        return string;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public int readUtf8CodePoint() throws EOFException {
        boolean bl;
        byte $this$and$iv$iv;
        Buffer $this$commonReadUtf8CodePoint$iv = this;
        boolean $i$f$commonReadUtf8CodePoint = false;
        if ($this$commonReadUtf8CodePoint$iv.size() == 0L) {
            throw new EOFException();
        }
        byte b0$iv = $this$commonReadUtf8CodePoint$iv.getByte(0L);
        int codePoint$iv = 0;
        int byteCount$iv = 0;
        int min$iv = 0;
        byte by = b0$iv;
        int other$iv$iv = 128;
        byte $i$f$and = 0;
        if (($this$and$iv$iv & other$iv$iv) == 0) {
            $this$and$iv$iv = b0$iv;
            other$iv$iv = 127;
            $i$f$and = 0;
            codePoint$iv = $this$and$iv$iv & other$iv$iv;
            byteCount$iv = 1;
            min$iv = 0;
        } else {
            $this$and$iv$iv = b0$iv;
            other$iv$iv = 224;
            $i$f$and = 0;
            if (($this$and$iv$iv & other$iv$iv) == 192) {
                $this$and$iv$iv = b0$iv;
                other$iv$iv = 31;
                $i$f$and = 0;
                codePoint$iv = $this$and$iv$iv & other$iv$iv;
                byteCount$iv = 2;
                min$iv = 128;
            } else {
                $this$and$iv$iv = b0$iv;
                other$iv$iv = 240;
                $i$f$and = 0;
                if (($this$and$iv$iv & other$iv$iv) == 224) {
                    $this$and$iv$iv = b0$iv;
                    other$iv$iv = 15;
                    $i$f$and = 0;
                    codePoint$iv = $this$and$iv$iv & other$iv$iv;
                    byteCount$iv = 3;
                    min$iv = 2048;
                } else {
                    $this$and$iv$iv = b0$iv;
                    other$iv$iv = 248;
                    $i$f$and = 0;
                    if (($this$and$iv$iv & other$iv$iv) == 240) {
                        $this$and$iv$iv = b0$iv;
                        other$iv$iv = 7;
                        $i$f$and = 0;
                        codePoint$iv = $this$and$iv$iv & other$iv$iv;
                        byteCount$iv = 4;
                        min$iv = 65536;
                    } else {
                        $this$commonReadUtf8CodePoint$iv.skip(1L);
                        return 65533;
                    }
                }
            }
        }
        if ($this$commonReadUtf8CodePoint$iv.size() < (long)byteCount$iv) {
            throw new EOFException("size < " + byteCount$iv + ": " + $this$commonReadUtf8CodePoint$iv.size() + " (to read code point prefixed 0x" + -SegmentedByteString.toHexString(b0$iv) + ')');
        }
        for (int i$iv = 1; i$iv < byteCount$iv; ++i$iv) {
            byte $this$and$iv$iv2;
            byte b$iv;
            $i$f$and = b$iv = $this$commonReadUtf8CodePoint$iv.getByte(i$iv);
            int other$iv$iv2 = 192;
            boolean $i$f$and2 = false;
            if (($this$and$iv$iv2 & other$iv$iv2) == 128) {
                codePoint$iv <<= 6;
                $this$and$iv$iv2 = b$iv;
                other$iv$iv2 = 63;
                $i$f$and2 = false;
                codePoint$iv |= $this$and$iv$iv2 & other$iv$iv2;
                continue;
            }
            $this$commonReadUtf8CodePoint$iv.skip(i$iv);
            return 65533;
        }
        $this$commonReadUtf8CodePoint$iv.skip(byteCount$iv);
        if (codePoint$iv > 0x10FFFF) {
            return 65533;
        }
        if (55296 <= codePoint$iv) {
            if (codePoint$iv < 57344) {
                return 65533;
            }
            bl = false;
        } else {
            bl = false;
        }
        if (bl) {
            return 65533;
        }
        if (codePoint$iv < min$iv) {
            return 65533;
        }
        int n2 = codePoint$iv;
        return n2;
    }

    @Override
    @NotNull
    public byte[] readByteArray() {
        Buffer $this$commonReadByteArray$iv = this;
        boolean $i$f$commonReadByteArray = false;
        return $this$commonReadByteArray$iv.readByteArray($this$commonReadByteArray$iv.size());
    }

    @Override
    @NotNull
    public byte[] readByteArray(long byteCount) throws EOFException {
        Buffer $this$commonReadByteArray$iv = this;
        boolean $i$f$commonReadByteArray = false;
        if (!(byteCount >= 0L && byteCount <= Integer.MAX_VALUE)) {
            boolean bl = false;
            String string = "byteCount: " + byteCount;
            throw new IllegalArgumentException(string.toString());
        }
        if ($this$commonReadByteArray$iv.size() < byteCount) {
            throw new EOFException();
        }
        byte[] result$iv = new byte[(int)byteCount];
        $this$commonReadByteArray$iv.readFully(result$iv);
        return result$iv;
    }

    @Override
    public int read(@NotNull byte[] sink2) {
        Intrinsics.checkNotNullParameter(sink2, "sink");
        Buffer $this$commonRead$iv = this;
        boolean $i$f$commonRead = false;
        return $this$commonRead$iv.read(sink2, 0, sink2.length);
    }

    @Override
    public void readFully(@NotNull byte[] sink2) throws EOFException {
        int read$iv;
        Intrinsics.checkNotNullParameter(sink2, "sink");
        Buffer $this$commonReadFully$iv = this;
        boolean $i$f$commonReadFully = false;
        for (int offset$iv = 0; offset$iv < sink2.length; offset$iv += read$iv) {
            read$iv = $this$commonReadFully$iv.read(sink2, offset$iv, sink2.length - offset$iv);
            if (read$iv != -1) continue;
            throw new EOFException();
        }
    }

    @Override
    public int read(@NotNull byte[] sink2, int offset, int byteCount) {
        int n2;
        Intrinsics.checkNotNullParameter(sink2, "sink");
        Buffer $this$commonRead$iv = this;
        boolean $i$f$commonRead = false;
        -SegmentedByteString.checkOffsetAndCount(sink2.length, offset, byteCount);
        Segment segment = $this$commonRead$iv.head;
        if (segment == null) {
            n2 = -1;
        } else {
            Segment s$iv = segment;
            int toCopy$iv = Math.min(byteCount, s$iv.limit - s$iv.pos);
            ArraysKt.copyInto(s$iv.data, sink2, offset, s$iv.pos, s$iv.pos + toCopy$iv);
            s$iv.pos += toCopy$iv;
            $this$commonRead$iv.setSize$okio($this$commonRead$iv.size() - (long)toCopy$iv);
            if (s$iv.pos == s$iv.limit) {
                $this$commonRead$iv.head = s$iv.pop();
                SegmentPool.recycle(s$iv);
            }
            n2 = toCopy$iv;
        }
        return n2;
    }

    @Override
    public int read(@NotNull ByteBuffer sink2) throws IOException {
        Intrinsics.checkNotNullParameter(sink2, "sink");
        Segment segment = this.head;
        if (segment == null) {
            return -1;
        }
        Segment s2 = segment;
        int toCopy = Math.min(sink2.remaining(), s2.limit - s2.pos);
        sink2.put(s2.data, s2.pos, toCopy);
        s2.pos += toCopy;
        this.size -= (long)toCopy;
        if (s2.pos == s2.limit) {
            this.head = s2.pop();
            SegmentPool.recycle(s2);
        }
        return toCopy;
    }

    public final void clear() {
        Buffer $this$commonClear$iv = this;
        boolean $i$f$commonClear = false;
        $this$commonClear$iv.skip($this$commonClear$iv.size());
    }

    @Override
    public void skip(long byteCount) throws EOFException {
        Buffer $this$commonSkip$iv = this;
        boolean $i$f$commonSkip = false;
        long byteCount$iv = byteCount;
        while (byteCount$iv > 0L) {
            Segment head$iv;
            if ($this$commonSkip$iv.head == null) {
                throw new EOFException();
            }
            int b$iv$iv = head$iv.limit - head$iv.pos;
            boolean $i$f$minOf = false;
            int toSkip$iv = (int)Math.min(byteCount$iv, (long)b$iv$iv);
            $this$commonSkip$iv.setSize$okio($this$commonSkip$iv.size() - (long)toSkip$iv);
            byteCount$iv -= (long)toSkip$iv;
            head$iv.pos += toSkip$iv;
            if (head$iv.pos != head$iv.limit) continue;
            $this$commonSkip$iv.head = head$iv.pop();
            SegmentPool.recycle(head$iv);
        }
    }

    /*
     * WARNING - void declaration
     */
    @Override
    @NotNull
    public Buffer write(@NotNull ByteString byteString) {
        void var2_2;
        Intrinsics.checkNotNullParameter(byteString, "byteString");
        Buffer $this$commonWrite_u24default$iv = this;
        int offset$iv = 0;
        int byteCount$iv = byteString.size();
        boolean $i$f$commonWrite = false;
        byteString.write$okio($this$commonWrite_u24default$iv, offset$iv, byteCount$iv);
        return var2_2;
    }

    @Override
    @NotNull
    public Buffer write(@NotNull ByteString byteString, int offset, int byteCount) {
        Intrinsics.checkNotNullParameter(byteString, "byteString");
        Buffer $this$commonWrite$iv = this;
        boolean $i$f$commonWrite = false;
        byteString.write$okio($this$commonWrite$iv, offset, byteCount);
        return $this$commonWrite$iv;
    }

    @Override
    @NotNull
    public Buffer writeUtf8(@NotNull String string) {
        Intrinsics.checkNotNullParameter(string, "string");
        return this.writeUtf8(string, 0, string.length());
    }

    @Override
    @NotNull
    public Buffer writeUtf8(@NotNull String string, int beginIndex, int endIndex) {
        Intrinsics.checkNotNullParameter(string, "string");
        Buffer $this$commonWriteUtf8$iv = this;
        boolean $i$f$commonWriteUtf8 = false;
        if (!(beginIndex >= 0)) {
            boolean $i$a$-require--Buffer$commonWriteUtf8$1$iv22 = false;
            String $i$a$-require--Buffer$commonWriteUtf8$1$iv22 = "beginIndex < 0: " + beginIndex;
            throw new IllegalArgumentException($i$a$-require--Buffer$commonWriteUtf8$1$iv22.toString());
        }
        if (!(endIndex >= beginIndex)) {
            boolean $i$a$-require--Buffer$commonWriteUtf8$2$iv22 = false;
            String $i$a$-require--Buffer$commonWriteUtf8$2$iv22 = "endIndex < beginIndex: " + endIndex + " < " + beginIndex;
            throw new IllegalArgumentException($i$a$-require--Buffer$commonWriteUtf8$2$iv22.toString());
        }
        if (!(endIndex <= string.length())) {
            boolean $i$a$-require--Buffer$commonWriteUtf8$3$iv22 = false;
            String $i$a$-require--Buffer$commonWriteUtf8$3$iv22 = "endIndex > string.length: " + endIndex + " > " + string.length();
            throw new IllegalArgumentException($i$a$-require--Buffer$commonWriteUtf8$3$iv22.toString());
        }
        int i$iv = beginIndex;
        while (i$iv < endIndex) {
            char low$iv;
            char c$iv;
            block12: {
                block11: {
                    c$iv = string.charAt(i$iv);
                    if (c$iv < '\u0080') {
                        Segment tail$iv = $this$commonWriteUtf8$iv.writableSegment$okio(1);
                        byte[] data$iv = tail$iv.data;
                        int segmentOffset$iv = tail$iv.limit - i$iv;
                        int runLimit$iv = Math.min(endIndex, 8192 - segmentOffset$iv);
                        data$iv[segmentOffset$iv + i$iv++] = (byte)c$iv;
                        while (i$iv < runLimit$iv && (c$iv = string.charAt(i$iv)) < '\u0080') {
                            data$iv[segmentOffset$iv + i$iv++] = (byte)c$iv;
                        }
                        int runSize$iv = i$iv + segmentOffset$iv - tail$iv.limit;
                        tail$iv.limit += runSize$iv;
                        $this$commonWriteUtf8$iv.setSize$okio($this$commonWriteUtf8$iv.size() + (long)runSize$iv);
                        continue;
                    }
                    if (c$iv < '\u0800') {
                        Segment tail$iv = $this$commonWriteUtf8$iv.writableSegment$okio(2);
                        tail$iv.data[tail$iv.limit] = (byte)(c$iv >> 6 | 0xC0);
                        tail$iv.data[tail$iv.limit + 1] = (byte)(c$iv & 0x3F | 0x80);
                        tail$iv.limit += 2;
                        $this$commonWriteUtf8$iv.setSize$okio($this$commonWriteUtf8$iv.size() + 2L);
                        ++i$iv;
                        continue;
                    }
                    if (c$iv < '\ud800' || c$iv > '\udfff') {
                        Segment tail$iv = $this$commonWriteUtf8$iv.writableSegment$okio(3);
                        tail$iv.data[tail$iv.limit] = (byte)(c$iv >> 12 | 0xE0);
                        tail$iv.data[tail$iv.limit + 1] = (byte)(c$iv >> 6 & 0x3F | 0x80);
                        tail$iv.data[tail$iv.limit + 2] = (byte)(c$iv & 0x3F | 0x80);
                        tail$iv.limit += 3;
                        $this$commonWriteUtf8$iv.setSize$okio($this$commonWriteUtf8$iv.size() + 3L);
                        ++i$iv;
                        continue;
                    }
                    char c2 = low$iv = i$iv + 1 < endIndex ? string.charAt(i$iv + 1) : (char)'\u0000';
                    if (c$iv > '\udbff') break block11;
                    if ('\udc00' <= low$iv ? low$iv < '\ue000' : false) break block12;
                }
                $this$commonWriteUtf8$iv.writeByte(63);
                ++i$iv;
                continue;
            }
            int codePoint$iv = 65536 + ((c$iv & 0x3FF) << 10 | low$iv & 0x3FF);
            Segment tail$iv = $this$commonWriteUtf8$iv.writableSegment$okio(4);
            tail$iv.data[tail$iv.limit] = (byte)(codePoint$iv >> 18 | 0xF0);
            tail$iv.data[tail$iv.limit + 1] = (byte)(codePoint$iv >> 12 & 0x3F | 0x80);
            tail$iv.data[tail$iv.limit + 2] = (byte)(codePoint$iv >> 6 & 0x3F | 0x80);
            tail$iv.data[tail$iv.limit + 3] = (byte)(codePoint$iv & 0x3F | 0x80);
            tail$iv.limit += 4;
            $this$commonWriteUtf8$iv.setSize$okio($this$commonWriteUtf8$iv.size() + 4L);
            i$iv += 2;
        }
        return $this$commonWriteUtf8$iv;
    }

    /*
     * WARNING - void declaration
     */
    @Override
    @NotNull
    public Buffer writeUtf8CodePoint(int codePoint) {
        void var2_2;
        Buffer $this$commonWriteUtf8CodePoint$iv = this;
        boolean $i$f$commonWriteUtf8CodePoint = false;
        if (codePoint < 128) {
            $this$commonWriteUtf8CodePoint$iv.writeByte(codePoint);
        } else if (codePoint < 2048) {
            Segment tail$iv = $this$commonWriteUtf8CodePoint$iv.writableSegment$okio(2);
            tail$iv.data[tail$iv.limit] = (byte)(codePoint >> 6 | 0xC0);
            tail$iv.data[tail$iv.limit + 1] = (byte)(codePoint & 0x3F | 0x80);
            tail$iv.limit += 2;
            $this$commonWriteUtf8CodePoint$iv.setSize$okio($this$commonWriteUtf8CodePoint$iv.size() + 2L);
        } else {
            boolean bl = 55296 <= codePoint ? codePoint < 57344 : false;
            if (bl) {
                $this$commonWriteUtf8CodePoint$iv.writeByte(63);
            } else if (codePoint < 65536) {
                Segment tail$iv = $this$commonWriteUtf8CodePoint$iv.writableSegment$okio(3);
                tail$iv.data[tail$iv.limit] = (byte)(codePoint >> 12 | 0xE0);
                tail$iv.data[tail$iv.limit + 1] = (byte)(codePoint >> 6 & 0x3F | 0x80);
                tail$iv.data[tail$iv.limit + 2] = (byte)(codePoint & 0x3F | 0x80);
                tail$iv.limit += 3;
                $this$commonWriteUtf8CodePoint$iv.setSize$okio($this$commonWriteUtf8CodePoint$iv.size() + 3L);
            } else if (codePoint <= 0x10FFFF) {
                Segment tail$iv = $this$commonWriteUtf8CodePoint$iv.writableSegment$okio(4);
                tail$iv.data[tail$iv.limit] = (byte)(codePoint >> 18 | 0xF0);
                tail$iv.data[tail$iv.limit + 1] = (byte)(codePoint >> 12 & 0x3F | 0x80);
                tail$iv.data[tail$iv.limit + 2] = (byte)(codePoint >> 6 & 0x3F | 0x80);
                tail$iv.data[tail$iv.limit + 3] = (byte)(codePoint & 0x3F | 0x80);
                tail$iv.limit += 4;
                $this$commonWriteUtf8CodePoint$iv.setSize$okio($this$commonWriteUtf8CodePoint$iv.size() + 4L);
            } else {
                throw new IllegalArgumentException("Unexpected code point: 0x" + -SegmentedByteString.toHexString(codePoint));
            }
        }
        return var2_2;
    }

    @Override
    @NotNull
    public Buffer writeString(@NotNull String string, @NotNull Charset charset) {
        Intrinsics.checkNotNullParameter(string, "string");
        Intrinsics.checkNotNullParameter(charset, "charset");
        return this.writeString(string, 0, string.length(), charset);
    }

    @Override
    @NotNull
    public Buffer writeString(@NotNull String string, int beginIndex, int endIndex, @NotNull Charset charset) {
        Intrinsics.checkNotNullParameter(string, "string");
        Intrinsics.checkNotNullParameter(charset, "charset");
        if (!(beginIndex >= 0)) {
            boolean $i$a$-require-Buffer$writeString$42 = false;
            String $i$a$-require-Buffer$writeString$42 = "beginIndex < 0: " + beginIndex;
            throw new IllegalArgumentException($i$a$-require-Buffer$writeString$42.toString());
        }
        if (!(endIndex >= beginIndex)) {
            boolean $i$a$-require-Buffer$writeString$52 = false;
            String $i$a$-require-Buffer$writeString$52 = "endIndex < beginIndex: " + endIndex + " < " + beginIndex;
            throw new IllegalArgumentException($i$a$-require-Buffer$writeString$52.toString());
        }
        if (!(endIndex <= string.length())) {
            boolean bl = false;
            String string2 = "endIndex > string.length: " + endIndex + " > " + string.length();
            throw new IllegalArgumentException(string2.toString());
        }
        if (Intrinsics.areEqual(charset, Charsets.UTF_8)) {
            return this.writeUtf8(string, beginIndex, endIndex);
        }
        String string3 = string.substring(beginIndex, endIndex);
        Intrinsics.checkNotNullExpressionValue(string3, "substring(...)");
        byte[] byArray = string3.getBytes(charset);
        Intrinsics.checkNotNullExpressionValue(byArray, "getBytes(...)");
        byte[] data = byArray;
        return this.write(data, 0, data.length);
    }

    @Override
    @NotNull
    public Buffer write(@NotNull byte[] source2) {
        Intrinsics.checkNotNullParameter(source2, "source");
        Buffer $this$commonWrite$iv = this;
        boolean $i$f$commonWrite = false;
        return $this$commonWrite$iv.write(source2, 0, source2.length);
    }

    @Override
    @NotNull
    public Buffer write(@NotNull byte[] source2, int offset, int byteCount) {
        Intrinsics.checkNotNullParameter(source2, "source");
        Buffer $this$commonWrite$iv = this;
        boolean $i$f$commonWrite = false;
        int offset$iv = offset;
        -SegmentedByteString.checkOffsetAndCount(source2.length, offset$iv, byteCount);
        int limit$iv = offset$iv + byteCount;
        while (offset$iv < limit$iv) {
            Segment tail$iv = $this$commonWrite$iv.writableSegment$okio(1);
            int toCopy$iv = Math.min(limit$iv - offset$iv, 8192 - tail$iv.limit);
            ArraysKt.copyInto(source2, tail$iv.data, tail$iv.limit, offset$iv, offset$iv + toCopy$iv);
            offset$iv += toCopy$iv;
            tail$iv.limit += toCopy$iv;
        }
        $this$commonWrite$iv.setSize$okio($this$commonWrite$iv.size() + (long)byteCount);
        return $this$commonWrite$iv;
    }

    @Override
    public int write(@NotNull ByteBuffer source2) throws IOException {
        int byteCount;
        Intrinsics.checkNotNullParameter(source2, "source");
        int remaining = byteCount = source2.remaining();
        while (remaining > 0) {
            Segment tail = this.writableSegment$okio(1);
            int toCopy = Math.min(remaining, 8192 - tail.limit);
            source2.get(tail.data, tail.limit, toCopy);
            remaining -= toCopy;
            tail.limit += toCopy;
        }
        this.size += (long)byteCount;
        return byteCount;
    }

    @Override
    public long writeAll(@NotNull Source source2) throws IOException {
        long readCount$iv;
        Intrinsics.checkNotNullParameter(source2, "source");
        Buffer $this$commonWriteAll$iv = this;
        boolean $i$f$commonWriteAll = false;
        long totalBytesRead$iv = 0L;
        while ((readCount$iv = source2.read($this$commonWriteAll$iv, 8192L)) != -1L) {
            totalBytesRead$iv += readCount$iv;
        }
        return totalBytesRead$iv;
    }

    @Override
    @NotNull
    public Buffer write(@NotNull Source source2, long byteCount) throws IOException {
        long read$iv;
        Intrinsics.checkNotNullParameter(source2, "source");
        Buffer $this$commonWrite$iv = this;
        boolean $i$f$commonWrite = false;
        for (long byteCount$iv = byteCount; byteCount$iv > 0L; byteCount$iv -= read$iv) {
            read$iv = source2.read($this$commonWrite$iv, byteCount$iv);
            if (read$iv != -1L) continue;
            throw new EOFException();
        }
        return $this$commonWrite$iv;
    }

    /*
     * WARNING - void declaration
     */
    @Override
    @NotNull
    public Buffer writeByte(int b2) {
        void var2_2;
        Buffer $this$commonWriteByte$iv = this;
        boolean $i$f$commonWriteByte = false;
        Segment tail$iv = $this$commonWriteByte$iv.writableSegment$okio(1);
        int n2 = tail$iv.limit;
        tail$iv.limit = n2 + 1;
        tail$iv.data[n2] = (byte)b2;
        $this$commonWriteByte$iv.setSize$okio($this$commonWriteByte$iv.size() + 1L);
        return var2_2;
    }

    /*
     * WARNING - void declaration
     */
    @Override
    @NotNull
    public Buffer writeShort(int s2) {
        void var2_2;
        Buffer $this$commonWriteShort$iv = this;
        boolean $i$f$commonWriteShort = false;
        Segment tail$iv = $this$commonWriteShort$iv.writableSegment$okio(2);
        byte[] data$iv = tail$iv.data;
        int limit$iv = tail$iv.limit;
        data$iv[limit$iv++] = (byte)(s2 >>> 8 & 0xFF);
        data$iv[limit$iv++] = (byte)(s2 & 0xFF);
        tail$iv.limit = limit$iv;
        $this$commonWriteShort$iv.setSize$okio($this$commonWriteShort$iv.size() + 2L);
        return var2_2;
    }

    @Override
    @NotNull
    public Buffer writeShortLe(int s2) {
        return this.writeShort(-SegmentedByteString.reverseBytes((short)s2));
    }

    /*
     * WARNING - void declaration
     */
    @Override
    @NotNull
    public Buffer writeInt(int i2) {
        void var2_2;
        Buffer $this$commonWriteInt$iv = this;
        boolean $i$f$commonWriteInt = false;
        Segment tail$iv = $this$commonWriteInt$iv.writableSegment$okio(4);
        byte[] data$iv = tail$iv.data;
        int limit$iv = tail$iv.limit;
        data$iv[limit$iv++] = (byte)(i2 >>> 24 & 0xFF);
        data$iv[limit$iv++] = (byte)(i2 >>> 16 & 0xFF);
        data$iv[limit$iv++] = (byte)(i2 >>> 8 & 0xFF);
        data$iv[limit$iv++] = (byte)(i2 & 0xFF);
        tail$iv.limit = limit$iv;
        $this$commonWriteInt$iv.setSize$okio($this$commonWriteInt$iv.size() + 4L);
        return var2_2;
    }

    @Override
    @NotNull
    public Buffer writeIntLe(int i2) {
        return this.writeInt(-SegmentedByteString.reverseBytes(i2));
    }

    /*
     * WARNING - void declaration
     */
    @Override
    @NotNull
    public Buffer writeLong(long v2) {
        void var3_2;
        Buffer $this$commonWriteLong$iv = this;
        boolean $i$f$commonWriteLong = false;
        Segment tail$iv = $this$commonWriteLong$iv.writableSegment$okio(8);
        byte[] data$iv = tail$iv.data;
        int limit$iv = tail$iv.limit;
        data$iv[limit$iv++] = (byte)(v2 >>> 56 & 0xFFL);
        data$iv[limit$iv++] = (byte)(v2 >>> 48 & 0xFFL);
        data$iv[limit$iv++] = (byte)(v2 >>> 40 & 0xFFL);
        data$iv[limit$iv++] = (byte)(v2 >>> 32 & 0xFFL);
        data$iv[limit$iv++] = (byte)(v2 >>> 24 & 0xFFL);
        data$iv[limit$iv++] = (byte)(v2 >>> 16 & 0xFFL);
        data$iv[limit$iv++] = (byte)(v2 >>> 8 & 0xFFL);
        data$iv[limit$iv++] = (byte)(v2 & 0xFFL);
        tail$iv.limit = limit$iv;
        $this$commonWriteLong$iv.setSize$okio($this$commonWriteLong$iv.size() + 8L);
        return var3_2;
    }

    @Override
    @NotNull
    public Buffer writeLongLe(long v2) {
        return this.writeLong(-SegmentedByteString.reverseBytes(v2));
    }

    /*
     * WARNING - void declaration
     * Enabled aggressive block sorting
     */
    @Override
    @NotNull
    public Buffer writeDecimalLong(long v2) {
        void var3_2;
        Buffer buffer;
        Buffer $this$commonWriteDecimalLong$iv = this;
        boolean $i$f$commonWriteDecimalLong = false;
        long v$iv = v2;
        if (v$iv == 0L) {
            buffer = $this$commonWriteDecimalLong$iv.writeByte(48);
            return buffer;
        }
        boolean negative$iv = false;
        if (v$iv < 0L) {
            if ((v$iv = -v$iv) < 0L) {
                buffer = $this$commonWriteDecimalLong$iv.writeUtf8("-9223372036854775808");
                return buffer;
            }
            negative$iv = true;
        }
        int width$iv = -Buffer.access$countDigitsIn(v$iv);
        if (negative$iv) {
            ++width$iv;
        }
        Segment tail$iv = $this$commonWriteDecimalLong$iv.writableSegment$okio(width$iv);
        byte[] data$iv = tail$iv.data;
        int pos$iv = tail$iv.limit + width$iv;
        while (v$iv != 0L) {
            int digit$iv = (int)(v$iv % (long)10);
            data$iv[--pos$iv] = -Buffer.getHEX_DIGIT_BYTES()[digit$iv];
            v$iv /= (long)10;
        }
        if (negative$iv) {
            data$iv[--pos$iv] = 45;
        }
        tail$iv.limit += width$iv;
        $this$commonWriteDecimalLong$iv.setSize$okio($this$commonWriteDecimalLong$iv.size() + (long)width$iv);
        buffer = var3_2;
        return buffer;
    }

    /*
     * WARNING - void declaration
     */
    @Override
    @NotNull
    public Buffer writeHexadecimalUnsignedLong(long v2) {
        Buffer buffer;
        Buffer $this$commonWriteHexadecimalUnsignedLong$iv = this;
        boolean $i$f$commonWriteHexadecimalUnsignedLong = false;
        long v$iv = v2;
        if (v$iv == 0L) {
            buffer = $this$commonWriteHexadecimalUnsignedLong$iv.writeByte(48);
        } else {
            void var3_2;
            long x$iv = v$iv;
            x$iv |= x$iv >>> 1;
            x$iv |= x$iv >>> 2;
            x$iv |= x$iv >>> 4;
            x$iv |= x$iv >>> 8;
            x$iv |= x$iv >>> 16;
            x$iv |= x$iv >>> 32;
            x$iv -= x$iv >>> 1 & 0x5555555555555555L;
            x$iv = (x$iv >>> 2 & 0x3333333333333333L) + (x$iv & 0x3333333333333333L);
            x$iv = (x$iv >>> 4) + x$iv & 0xF0F0F0F0F0F0F0FL;
            x$iv += x$iv >>> 8;
            x$iv += x$iv >>> 16;
            x$iv = (x$iv & 0x3FL) + (x$iv >>> 32 & 0x3FL);
            int width$iv = (int)((x$iv + (long)3) / (long)4);
            Segment tail$iv = $this$commonWriteHexadecimalUnsignedLong$iv.writableSegment$okio(width$iv);
            byte[] data$iv = tail$iv.data;
            int start$iv = tail$iv.limit;
            for (int pos$iv = tail$iv.limit + width$iv - 1; pos$iv >= start$iv; --pos$iv) {
                data$iv[pos$iv] = -Buffer.getHEX_DIGIT_BYTES()[(int)(v$iv & 0xFL)];
                v$iv >>>= 4;
            }
            tail$iv.limit += width$iv;
            $this$commonWriteHexadecimalUnsignedLong$iv.setSize$okio($this$commonWriteHexadecimalUnsignedLong$iv.size() + (long)width$iv);
            buffer = var3_2;
        }
        return buffer;
    }

    @NotNull
    public final Segment writableSegment$okio(int minimumCapacity) {
        Segment segment;
        Buffer $this$commonWritableSegment$iv = this;
        boolean $i$f$commonWritableSegment = false;
        if (!(minimumCapacity >= 1 && minimumCapacity <= 8192)) {
            boolean bl = false;
            String string = "unexpected capacity";
            throw new IllegalArgumentException(string.toString());
        }
        if ($this$commonWritableSegment$iv.head == null) {
            Segment result$iv;
            $this$commonWritableSegment$iv.head = result$iv = SegmentPool.take();
            result$iv.prev = result$iv;
            result$iv.next = result$iv;
            segment = result$iv;
        } else {
            Segment tail$iv;
            Segment segment2 = $this$commonWritableSegment$iv.head;
            Intrinsics.checkNotNull(segment2);
            Segment segment3 = tail$iv = segment2.prev;
            Intrinsics.checkNotNull(segment3);
            if (segment3.limit + minimumCapacity > 8192 || !tail$iv.owner) {
                tail$iv = tail$iv.push(SegmentPool.take());
            }
            segment = tail$iv;
        }
        return segment;
    }

    @Override
    public void write(@NotNull Buffer source2, long byteCount) {
        long movedByteCount$iv;
        long byteCount$iv;
        Intrinsics.checkNotNullParameter(source2, "source");
        Buffer $this$commonWrite$iv = this;
        boolean $i$f$commonWrite = false;
        if (!(source2 != $this$commonWrite$iv)) {
            boolean bl = false;
            String string = "source == this";
            throw new IllegalArgumentException(string.toString());
        }
        -SegmentedByteString.checkOffsetAndCount(source2.size(), 0L, byteCount$iv);
        for (byteCount$iv = byteCount; byteCount$iv > 0L; byteCount$iv -= movedByteCount$iv) {
            Segment segmentToMove$iv;
            Segment segment = source2.head;
            Intrinsics.checkNotNull(segment);
            int n2 = segment.limit;
            Segment segment2 = source2.head;
            Intrinsics.checkNotNull(segment2);
            if (byteCount$iv < (long)(n2 - segment2.pos)) {
                Segment tail$iv;
                Segment segment3;
                if ($this$commonWrite$iv.head != null) {
                    Segment segment4 = $this$commonWrite$iv.head;
                    Intrinsics.checkNotNull(segment4);
                    segment3 = segment4.prev;
                } else {
                    segment3 = tail$iv = null;
                }
                if (tail$iv != null && tail$iv.owner && byteCount$iv + (long)tail$iv.limit - (long)(tail$iv.shared ? 0 : tail$iv.pos) <= 8192L) {
                    Segment segment5 = source2.head;
                    Intrinsics.checkNotNull(segment5);
                    segment5.writeTo(tail$iv, (int)byteCount$iv);
                    source2.setSize$okio(source2.size() - byteCount$iv);
                    $this$commonWrite$iv.setSize$okio($this$commonWrite$iv.size() + byteCount$iv);
                    break;
                }
                Segment segment6 = source2.head;
                Intrinsics.checkNotNull(segment6);
                source2.head = segment6.split((int)byteCount$iv);
            }
            Segment segment7 = segmentToMove$iv = source2.head;
            Intrinsics.checkNotNull(segment7);
            movedByteCount$iv = segment7.limit - segmentToMove$iv.pos;
            source2.head = segmentToMove$iv.pop();
            if ($this$commonWrite$iv.head == null) {
                $this$commonWrite$iv.head = segmentToMove$iv;
                segmentToMove$iv.next = segmentToMove$iv.prev = segmentToMove$iv;
            } else {
                Segment tail$iv;
                Segment segment8 = $this$commonWrite$iv.head;
                Intrinsics.checkNotNull(segment8);
                Segment segment9 = tail$iv = segment8.prev;
                Intrinsics.checkNotNull(segment9);
                tail$iv = segment9.push(segmentToMove$iv);
                tail$iv.compact();
            }
            source2.setSize$okio(source2.size() - movedByteCount$iv);
            $this$commonWrite$iv.setSize$okio($this$commonWrite$iv.size() + movedByteCount$iv);
        }
    }

    @Override
    public long read(@NotNull Buffer sink2, long byteCount) {
        long l2;
        Intrinsics.checkNotNullParameter(sink2, "sink");
        Buffer $this$commonRead$iv = this;
        boolean $i$f$commonRead = false;
        long byteCount$iv = 0L;
        byteCount$iv = byteCount;
        if (!(byteCount$iv >= 0L)) {
            boolean bl = false;
            String string = "byteCount < 0: " + byteCount$iv;
            throw new IllegalArgumentException(string.toString());
        }
        if ($this$commonRead$iv.size() == 0L) {
            l2 = -1L;
        } else {
            if (byteCount$iv > $this$commonRead$iv.size()) {
                byteCount$iv = $this$commonRead$iv.size();
            }
            sink2.write($this$commonRead$iv, byteCount$iv);
            l2 = byteCount$iv;
        }
        return l2;
    }

    @Override
    public long indexOf(byte b2) {
        return this.indexOf(b2, 0L, Long.MAX_VALUE);
    }

    @Override
    public long indexOf(byte b2, long fromIndex) {
        return this.indexOf(b2, fromIndex, Long.MAX_VALUE);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public long indexOf(byte b2, long fromIndex, long toIndex) {
        long l2;
        block18: {
            Buffer $this$commonIndexOf$iv = this;
            boolean $i$f$commonIndexOf = false;
            long fromIndex$iv = 0L;
            fromIndex$iv = fromIndex;
            long toIndex$iv = 0L;
            toIndex$iv = toIndex;
            if (!(0L <= fromIndex$iv ? fromIndex$iv <= toIndex$iv : false)) {
                boolean bl = false;
                String string = "size=" + $this$commonIndexOf$iv.size() + " fromIndex=" + fromIndex$iv + " toIndex=" + toIndex$iv;
                throw new IllegalArgumentException(string.toString());
            }
            if (toIndex$iv > $this$commonIndexOf$iv.size()) {
                toIndex$iv = $this$commonIndexOf$iv.size();
            }
            if (fromIndex$iv == toIndex$iv) {
                l2 = -1L;
            } else {
                void $this$seek$iv$iv;
                Buffer buffer = $this$commonIndexOf$iv;
                long fromIndex$iv$iv = fromIndex$iv;
                boolean $i$f$seek = false;
                Segment segment = $this$seek$iv$iv.head;
                if (segment == null) {
                    long l3 = -1L;
                    Object s$iv = null;
                    boolean bl = false;
                    l2 = -1L;
                } else {
                    Segment s$iv$iv = segment;
                    if ($this$seek$iv$iv.size() - fromIndex$iv$iv < fromIndex$iv$iv) {
                        long offset$iv$iv;
                        for (offset$iv$iv = $this$seek$iv$iv.size(); offset$iv$iv > fromIndex$iv$iv; offset$iv$iv -= (long)(s$iv$iv.limit - s$iv$iv.pos)) {
                            Intrinsics.checkNotNull(s$iv$iv.prev);
                        }
                        long offset$iv = offset$iv$iv;
                        Segment s$iv = s$iv$iv;
                        boolean bl = false;
                        Segment segment2 = s$iv;
                        if (segment2 == null) {
                            l2 = -1L;
                        } else {
                            Segment s$iv2 = segment2;
                            long offset$iv2 = offset$iv;
                            while (offset$iv2 < toIndex$iv) {
                                byte[] data$iv = s$iv2.data;
                                int limit$iv = (int)Math.min((long)s$iv2.limit, (long)s$iv2.pos + toIndex$iv - offset$iv2);
                                for (int pos$iv = (int)((long)s$iv2.pos + fromIndex$iv - offset$iv2); pos$iv < limit$iv; ++pos$iv) {
                                    if (data$iv[pos$iv] != b2) continue;
                                    l2 = (long)(pos$iv - s$iv2.pos) + offset$iv2;
                                    break block18;
                                }
                                fromIndex$iv = offset$iv2 += (long)(s$iv2.limit - s$iv2.pos);
                                Intrinsics.checkNotNull(s$iv2.next);
                            }
                            l2 = -1L;
                        }
                    } else {
                        long nextOffset$iv$iv;
                        long offset$iv$iv = 0L;
                        while ((nextOffset$iv$iv = offset$iv$iv + (long)(s$iv$iv.limit - s$iv$iv.pos)) <= fromIndex$iv$iv) {
                            Intrinsics.checkNotNull(s$iv$iv.next);
                            offset$iv$iv = nextOffset$iv$iv;
                        }
                        long l4 = offset$iv$iv;
                        Segment s$iv = s$iv$iv;
                        boolean bl = false;
                        Segment segment3 = s$iv;
                        if (segment3 == null) {
                            l2 = -1L;
                        } else {
                            void offset$iv;
                            Segment s$iv3 = segment3;
                            void offset$iv3 = offset$iv;
                            while (offset$iv3 < toIndex$iv) {
                                byte[] data$iv = s$iv3.data;
                                int limit$iv = (int)Math.min((long)s$iv3.limit, (long)s$iv3.pos + toIndex$iv - offset$iv3);
                                for (int pos$iv = (int)((long)s$iv3.pos + fromIndex$iv - offset$iv3); pos$iv < limit$iv; ++pos$iv) {
                                    if (data$iv[pos$iv] != b2) continue;
                                    l2 = (long)(pos$iv - s$iv3.pos) + offset$iv3;
                                    break block18;
                                }
                                fromIndex$iv = offset$iv3 += (long)(s$iv3.limit - s$iv3.pos);
                                Intrinsics.checkNotNull(s$iv3.next);
                            }
                            l2 = -1L;
                        }
                    }
                }
            }
        }
        return l2;
    }

    @Override
    public long indexOf(@NotNull ByteString bytes) throws IOException {
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        return this.indexOf(bytes, 0L);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public long indexOf(@NotNull ByteString bytes, long fromIndex) throws IOException {
        long l2;
        block16: {
            void $this$seek$iv$iv;
            Intrinsics.checkNotNullParameter(bytes, "bytes");
            Buffer $this$commonIndexOf$iv = this;
            boolean $i$f$commonIndexOf = false;
            long fromIndex$iv = 0L;
            fromIndex$iv = fromIndex;
            if (!(bytes.size() > 0)) {
                boolean $i$a$-require--Buffer$commonIndexOf$3$iv22 = false;
                String $i$a$-require--Buffer$commonIndexOf$3$iv22 = "bytes is empty";
                throw new IllegalArgumentException($i$a$-require--Buffer$commonIndexOf$3$iv22.toString());
            }
            if (!(fromIndex$iv >= 0L)) {
                boolean bl = false;
                String string = "fromIndex < 0: " + fromIndex$iv;
                throw new IllegalArgumentException(string.toString());
            }
            Buffer buffer = $this$commonIndexOf$iv;
            long fromIndex$iv$iv = fromIndex$iv;
            boolean $i$f$seek = false;
            Segment segment = $this$seek$iv$iv.head;
            if (segment == null) {
                long l3 = -1L;
                Object s$iv = null;
                boolean bl = false;
                l2 = -1L;
            } else {
                Segment s$iv$iv = segment;
                if ($this$seek$iv$iv.size() - fromIndex$iv$iv < fromIndex$iv$iv) {
                    long offset$iv$iv;
                    for (offset$iv$iv = $this$seek$iv$iv.size(); offset$iv$iv > fromIndex$iv$iv; offset$iv$iv -= (long)(s$iv$iv.limit - s$iv$iv.pos)) {
                        Intrinsics.checkNotNull(s$iv$iv.prev);
                    }
                    long offset$iv = offset$iv$iv;
                    Segment s$iv = s$iv$iv;
                    boolean bl = false;
                    Segment segment2 = s$iv;
                    if (segment2 == null) {
                        l2 = -1L;
                    } else {
                        Segment s$iv2 = segment2;
                        long offset$iv2 = offset$iv;
                        byte[] targetByteArray$iv = bytes.internalArray$okio();
                        byte b0$iv = targetByteArray$iv[0];
                        int bytesSize$iv = bytes.size();
                        long resultLimit$iv = $this$commonIndexOf$iv.size() - (long)bytesSize$iv + 1L;
                        while (offset$iv2 < resultLimit$iv) {
                            void a$iv$iv;
                            byte[] data$iv = s$iv2.data;
                            int n2 = s$iv2.limit;
                            long b$iv$iv = (long)s$iv2.pos + resultLimit$iv - offset$iv2;
                            boolean $i$f$minOf = false;
                            int segmentLimit$iv = (int)Math.min((long)a$iv$iv, b$iv$iv);
                            for (int pos$iv = (int)((long)s$iv2.pos + fromIndex$iv - offset$iv2); pos$iv < segmentLimit$iv; ++pos$iv) {
                                if (data$iv[pos$iv] != b0$iv || !-Buffer.rangeEquals(s$iv2, pos$iv + 1, targetByteArray$iv, 1, bytesSize$iv)) continue;
                                l2 = (long)(pos$iv - s$iv2.pos) + offset$iv2;
                                break block16;
                            }
                            fromIndex$iv = offset$iv2 += (long)(s$iv2.limit - s$iv2.pos);
                            Intrinsics.checkNotNull(s$iv2.next);
                        }
                        l2 = -1L;
                    }
                } else {
                    long nextOffset$iv$iv;
                    long offset$iv$iv = 0L;
                    while ((nextOffset$iv$iv = offset$iv$iv + (long)(s$iv$iv.limit - s$iv$iv.pos)) <= fromIndex$iv$iv) {
                        Intrinsics.checkNotNull(s$iv$iv.next);
                        offset$iv$iv = nextOffset$iv$iv;
                    }
                    long l4 = offset$iv$iv;
                    Segment s$iv = s$iv$iv;
                    boolean bl = false;
                    Segment segment3 = s$iv;
                    if (segment3 == null) {
                        l2 = -1L;
                    } else {
                        void offset$iv;
                        Segment s$iv3 = segment3;
                        void offset$iv3 = offset$iv;
                        byte[] targetByteArray$iv = bytes.internalArray$okio();
                        byte b0$iv = targetByteArray$iv[0];
                        int bytesSize$iv = bytes.size();
                        long resultLimit$iv = $this$commonIndexOf$iv.size() - (long)bytesSize$iv + 1L;
                        while (offset$iv3 < resultLimit$iv) {
                            void a$iv$iv;
                            byte[] data$iv = s$iv3.data;
                            int n3 = s$iv3.limit;
                            long b$iv$iv = (long)s$iv3.pos + resultLimit$iv - offset$iv3;
                            boolean $i$f$minOf = false;
                            int segmentLimit$iv = (int)Math.min((long)a$iv$iv, b$iv$iv);
                            for (int pos$iv = (int)((long)s$iv3.pos + fromIndex$iv - offset$iv3); pos$iv < segmentLimit$iv; ++pos$iv) {
                                if (data$iv[pos$iv] != b0$iv || !-Buffer.rangeEquals(s$iv3, pos$iv + 1, targetByteArray$iv, 1, bytesSize$iv)) continue;
                                l2 = (long)(pos$iv - s$iv3.pos) + offset$iv3;
                                break block16;
                            }
                            fromIndex$iv = offset$iv3 += (long)(s$iv3.limit - s$iv3.pos);
                            Intrinsics.checkNotNull(s$iv3.next);
                        }
                        l2 = -1L;
                    }
                }
            }
        }
        return l2;
    }

    @Override
    public long indexOfElement(@NotNull ByteString targetBytes) {
        Intrinsics.checkNotNullParameter(targetBytes, "targetBytes");
        return this.indexOfElement(targetBytes, 0L);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public long indexOfElement(@NotNull ByteString targetBytes, long fromIndex) {
        long l2;
        block25: {
            void $this$seek$iv$iv;
            Intrinsics.checkNotNullParameter(targetBytes, "targetBytes");
            Buffer $this$commonIndexOfElement$iv = this;
            boolean $i$f$commonIndexOfElement = false;
            long fromIndex$iv = 0L;
            fromIndex$iv = fromIndex;
            if (!(fromIndex$iv >= 0L)) {
                boolean bl = false;
                String string = "fromIndex < 0: " + fromIndex$iv;
                throw new IllegalArgumentException(string.toString());
            }
            Buffer buffer = $this$commonIndexOfElement$iv;
            long fromIndex$iv$iv = fromIndex$iv;
            boolean $i$f$seek = false;
            Segment segment = $this$seek$iv$iv.head;
            if (segment == null) {
                long l3 = -1L;
                Object s$iv = null;
                boolean bl = false;
                l2 = -1L;
            } else {
                Segment s$iv$iv = segment;
                if ($this$seek$iv$iv.size() - fromIndex$iv$iv < fromIndex$iv$iv) {
                    long offset$iv$iv;
                    for (offset$iv$iv = $this$seek$iv$iv.size(); offset$iv$iv > fromIndex$iv$iv; offset$iv$iv -= (long)(s$iv$iv.limit - s$iv$iv.pos)) {
                        Intrinsics.checkNotNull(s$iv$iv.prev);
                    }
                    long offset$iv = offset$iv$iv;
                    Segment s$iv = s$iv$iv;
                    boolean bl = false;
                    Segment segment2 = s$iv;
                    if (segment2 == null) {
                        l2 = -1L;
                    } else {
                        Segment s$iv2 = segment2;
                        long offset$iv2 = offset$iv;
                        if (targetBytes.size() == 2) {
                            byte b0$iv = targetBytes.getByte(0);
                            byte b1$iv = targetBytes.getByte(1);
                            while (offset$iv2 < $this$commonIndexOfElement$iv.size()) {
                                byte[] data$iv = s$iv2.data;
                                int limit$iv = s$iv2.limit;
                                for (int pos$iv = (int)((long)s$iv2.pos + fromIndex$iv - offset$iv2); pos$iv < limit$iv; ++pos$iv) {
                                    byte b$iv = data$iv[pos$iv];
                                    if (b$iv != b0$iv && b$iv != b1$iv) continue;
                                    l2 = (long)(pos$iv - s$iv2.pos) + offset$iv2;
                                    break block25;
                                }
                                fromIndex$iv = offset$iv2 += (long)(s$iv2.limit - s$iv2.pos);
                                Intrinsics.checkNotNull(s$iv2.next);
                            }
                        } else {
                            byte[] targetByteArray$iv = targetBytes.internalArray$okio();
                            while (offset$iv2 < $this$commonIndexOfElement$iv.size()) {
                                byte[] data$iv = s$iv2.data;
                                int limit$iv = s$iv2.limit;
                                for (int pos$iv = (int)((long)s$iv2.pos + fromIndex$iv - offset$iv2); pos$iv < limit$iv; ++pos$iv) {
                                    byte b$iv = data$iv[pos$iv];
                                    for (byte t$iv : targetByteArray$iv) {
                                        if (b$iv != t$iv) continue;
                                        l2 = (long)(pos$iv - s$iv2.pos) + offset$iv2;
                                        break block25;
                                    }
                                }
                                fromIndex$iv = offset$iv2 += (long)(s$iv2.limit - s$iv2.pos);
                                Intrinsics.checkNotNull(s$iv2.next);
                            }
                        }
                        l2 = -1L;
                    }
                } else {
                    long nextOffset$iv$iv;
                    long offset$iv$iv = 0L;
                    while ((nextOffset$iv$iv = offset$iv$iv + (long)(s$iv$iv.limit - s$iv$iv.pos)) <= fromIndex$iv$iv) {
                        Intrinsics.checkNotNull(s$iv$iv.next);
                        offset$iv$iv = nextOffset$iv$iv;
                    }
                    long l4 = offset$iv$iv;
                    Segment s$iv = s$iv$iv;
                    boolean bl = false;
                    Segment segment3 = s$iv;
                    if (segment3 == null) {
                        l2 = -1L;
                    } else {
                        void offset$iv;
                        Segment s$iv3 = segment3;
                        void offset$iv3 = offset$iv;
                        if (targetBytes.size() == 2) {
                            byte b0$iv = targetBytes.getByte(0);
                            byte b1$iv = targetBytes.getByte(1);
                            while (offset$iv3 < $this$commonIndexOfElement$iv.size()) {
                                byte[] data$iv = s$iv3.data;
                                int limit$iv = s$iv3.limit;
                                for (int pos$iv = (int)((long)s$iv3.pos + fromIndex$iv - offset$iv3); pos$iv < limit$iv; ++pos$iv) {
                                    byte b$iv = data$iv[pos$iv];
                                    if (b$iv != b0$iv && b$iv != b1$iv) continue;
                                    l2 = (long)(pos$iv - s$iv3.pos) + offset$iv3;
                                    break block25;
                                }
                                fromIndex$iv = offset$iv3 += (long)(s$iv3.limit - s$iv3.pos);
                                Intrinsics.checkNotNull(s$iv3.next);
                            }
                        } else {
                            byte[] targetByteArray$iv = targetBytes.internalArray$okio();
                            while (offset$iv3 < $this$commonIndexOfElement$iv.size()) {
                                byte[] data$iv = s$iv3.data;
                                int limit$iv = s$iv3.limit;
                                for (int pos$iv = (int)((long)s$iv3.pos + fromIndex$iv - offset$iv3); pos$iv < limit$iv; ++pos$iv) {
                                    byte b$iv = data$iv[pos$iv];
                                    for (byte t$iv : targetByteArray$iv) {
                                        if (b$iv != t$iv) continue;
                                        l2 = (long)(pos$iv - s$iv3.pos) + offset$iv3;
                                        break block25;
                                    }
                                }
                                fromIndex$iv = offset$iv3 += (long)(s$iv3.limit - s$iv3.pos);
                                Intrinsics.checkNotNull(s$iv3.next);
                            }
                        }
                        l2 = -1L;
                    }
                }
            }
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
        block3: {
            Intrinsics.checkNotNullParameter(bytes, "bytes");
            Buffer $this$commonRangeEquals$iv = this;
            boolean $i$f$commonRangeEquals = false;
            if (offset < 0L || bytesOffset < 0 || byteCount < 0 || $this$commonRangeEquals$iv.size() - offset < (long)byteCount || bytes.size() - bytesOffset < byteCount) {
                bl = false;
            } else {
                for (int i$iv = 0; i$iv < byteCount; ++i$iv) {
                    if ($this$commonRangeEquals$iv.getByte(offset + (long)i$iv) == bytes.getByte(bytesOffset + i$iv)) continue;
                    bl = false;
                    break block3;
                }
                bl = true;
            }
        }
        return bl;
    }

    @Override
    public void flush() {
    }

    @Override
    public boolean isOpen() {
        return true;
    }

    @Override
    public void close() {
    }

    @Override
    @NotNull
    public Timeout timeout() {
        return Timeout.NONE;
    }

    @NotNull
    public final ByteString md5() {
        return this.digest("MD5");
    }

    @NotNull
    public final ByteString sha1() {
        return this.digest("SHA-1");
    }

    @NotNull
    public final ByteString sha256() {
        return this.digest("SHA-256");
    }

    @NotNull
    public final ByteString sha512() {
        return this.digest("SHA-512");
    }

    private final ByteString digest(String algorithm) {
        MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
        Segment segment = this.head;
        if (segment != null) {
            Segment head = segment;
            boolean bl = false;
            messageDigest.update(head.data, head.pos, head.limit - head.pos);
            Segment segment2 = head.next;
            Intrinsics.checkNotNull(segment2);
            Segment s2 = segment2;
            while (s2 != head) {
                messageDigest.update(s2.data, s2.pos, s2.limit - s2.pos);
                Intrinsics.checkNotNull(s2.next);
            }
        }
        byte[] byArray = messageDigest.digest();
        Intrinsics.checkNotNullExpressionValue(byArray, "digest(...)");
        return new ByteString(byArray);
    }

    @NotNull
    public final ByteString hmacSha1(@NotNull ByteString key) {
        Intrinsics.checkNotNullParameter(key, "key");
        return this.hmac("HmacSHA1", key);
    }

    @NotNull
    public final ByteString hmacSha256(@NotNull ByteString key) {
        Intrinsics.checkNotNullParameter(key, "key");
        return this.hmac("HmacSHA256", key);
    }

    @NotNull
    public final ByteString hmacSha512(@NotNull ByteString key) {
        Intrinsics.checkNotNullParameter(key, "key");
        return this.hmac("HmacSHA512", key);
    }

    private final ByteString hmac(String algorithm, ByteString key) {
        try {
            Mac mac = Mac.getInstance(algorithm);
            mac.init(new SecretKeySpec(key.internalArray$okio(), algorithm));
            Segment segment = this.head;
            if (segment != null) {
                Segment head = segment;
                boolean bl = false;
                mac.update(head.data, head.pos, head.limit - head.pos);
                Segment segment2 = head.next;
                Intrinsics.checkNotNull(segment2);
                Segment s2 = segment2;
                while (s2 != head) {
                    mac.update(s2.data, s2.pos, s2.limit - s2.pos);
                    Intrinsics.checkNotNull(s2.next);
                }
            }
            byte[] byArray = mac.doFinal();
            Intrinsics.checkNotNullExpressionValue(byArray, "doFinal(...)");
            return new ByteString(byArray);
        }
        catch (InvalidKeyException e2) {
            throw new IllegalArgumentException(e2);
        }
    }

    public boolean equals(@Nullable Object other) {
        boolean bl;
        block11: {
            Buffer $this$commonEquals$iv = this;
            boolean $i$f$commonEquals = false;
            if ($this$commonEquals$iv == other) {
                bl = true;
            } else if (!(other instanceof Buffer)) {
                bl = false;
            } else if ($this$commonEquals$iv.size() != ((Buffer)other).size()) {
                bl = false;
            } else if ($this$commonEquals$iv.size() == 0L) {
                bl = true;
            } else {
                Segment segment = $this$commonEquals$iv.head;
                Intrinsics.checkNotNull(segment);
                Segment sa$iv = segment;
                Segment segment2 = ((Buffer)other).head;
                Intrinsics.checkNotNull(segment2);
                Segment sb$iv = segment2;
                int posA$iv = sa$iv.pos;
                int posB$iv = sb$iv.pos;
                long count$iv = 0L;
                for (long pos$iv = 0L; pos$iv < $this$commonEquals$iv.size(); pos$iv += count$iv) {
                    count$iv = Math.min(sa$iv.limit - posA$iv, sb$iv.limit - posB$iv);
                    long l2 = count$iv;
                    for (long i$iv = 0L; i$iv < l2; ++i$iv) {
                        if (sa$iv.data[posA$iv++] == sb$iv.data[posB$iv++]) continue;
                        bl = false;
                        break block11;
                    }
                    if (posA$iv == sa$iv.limit) {
                        Intrinsics.checkNotNull(sa$iv.next);
                        posA$iv = sa$iv.pos;
                    }
                    if (posB$iv != sb$iv.limit) continue;
                    Intrinsics.checkNotNull(sb$iv.next);
                    posB$iv = sb$iv.pos;
                }
                bl = true;
            }
        }
        return bl;
    }

    public int hashCode() {
        int n2;
        Buffer $this$commonHashCode$iv = this;
        boolean $i$f$commonHashCode = false;
        Segment segment = $this$commonHashCode$iv.head;
        if (segment == null) {
            n2 = 0;
        } else {
            Segment s$iv = segment;
            int result$iv = 1;
            do {
                int limit$iv = s$iv.limit;
                for (int pos$iv = s$iv.pos; pos$iv < limit$iv; ++pos$iv) {
                    result$iv = 31 * result$iv + s$iv.data[pos$iv];
                }
                Intrinsics.checkNotNull(s$iv.next);
            } while (s$iv != $this$commonHashCode$iv.head);
            n2 = result$iv;
        }
        return n2;
    }

    @NotNull
    public String toString() {
        return this.snapshot().toString();
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public final Buffer copy() {
        Buffer buffer;
        Buffer $this$commonCopy$iv = this;
        boolean $i$f$commonCopy = false;
        Buffer result$iv = new Buffer();
        if ($this$commonCopy$iv.size() == 0L) {
            buffer = result$iv;
        } else {
            void var3_3;
            Segment headCopy$iv;
            Segment segment = $this$commonCopy$iv.head;
            Intrinsics.checkNotNull(segment);
            Segment head$iv = segment;
            headCopy$iv.next = headCopy$iv.prev = (result$iv.head = (headCopy$iv = head$iv.sharedCopy()));
            Segment s$iv = head$iv.next;
            while (s$iv != head$iv) {
                Segment segment2 = headCopy$iv.prev;
                Intrinsics.checkNotNull(segment2);
                Segment segment3 = s$iv;
                Intrinsics.checkNotNull(segment3);
                segment2.push(segment3.sharedCopy());
                s$iv = s$iv.next;
            }
            result$iv.setSize$okio($this$commonCopy$iv.size());
            buffer = var3_3;
        }
        return buffer;
    }

    @NotNull
    public Buffer clone() {
        return this.copy();
    }

    @NotNull
    public final ByteString snapshot() {
        Buffer $this$commonSnapshot$iv = this;
        boolean $i$f$commonSnapshot = false;
        if (!($this$commonSnapshot$iv.size() <= Integer.MAX_VALUE)) {
            boolean bl = false;
            String string = "size > Int.MAX_VALUE: " + $this$commonSnapshot$iv.size();
            throw new IllegalStateException(string.toString());
        }
        return $this$commonSnapshot$iv.snapshot((int)$this$commonSnapshot$iv.size());
    }

    @NotNull
    public final ByteString snapshot(int byteCount) {
        ByteString byteString;
        Buffer $this$commonSnapshot$iv = this;
        boolean $i$f$commonSnapshot = false;
        if (byteCount == 0) {
            byteString = ByteString.EMPTY;
        } else {
            -SegmentedByteString.checkOffsetAndCount($this$commonSnapshot$iv.size(), 0L, byteCount);
            int offset$iv = 0;
            int segmentCount$iv = 0;
            Segment s$iv = $this$commonSnapshot$iv.head;
            while (offset$iv < byteCount) {
                Segment segment = s$iv;
                Intrinsics.checkNotNull(segment);
                if (segment.limit == s$iv.pos) {
                    throw new AssertionError((Object)"s.limit == s.pos");
                }
                offset$iv += s$iv.limit - s$iv.pos;
                ++segmentCount$iv;
                s$iv = s$iv.next;
            }
            byte[][] segments$iv = new byte[segmentCount$iv][];
            int[] directory$iv = new int[segmentCount$iv * 2];
            offset$iv = 0;
            segmentCount$iv = 0;
            s$iv = $this$commonSnapshot$iv.head;
            while (offset$iv < byteCount) {
                Segment segment = s$iv;
                Intrinsics.checkNotNull(segment);
                segments$iv[segmentCount$iv] = segment.data;
                directory$iv[segmentCount$iv] = Math.min(offset$iv += s$iv.limit - s$iv.pos, byteCount);
                directory$iv[segmentCount$iv + ((Object[])segments$iv).length] = s$iv.pos;
                s$iv.shared = true;
                ++segmentCount$iv;
                s$iv = s$iv.next;
            }
            byteString = new SegmentedByteString(segments$iv, directory$iv);
        }
        return byteString;
    }

    @JvmOverloads
    @NotNull
    public final UnsafeCursor readUnsafe(@NotNull UnsafeCursor unsafeCursor) {
        Intrinsics.checkNotNullParameter(unsafeCursor, "unsafeCursor");
        return -Buffer.commonReadUnsafe(this, unsafeCursor);
    }

    public static /* synthetic */ UnsafeCursor readUnsafe$default(Buffer buffer, UnsafeCursor unsafeCursor, int n2, Object object) {
        if ((n2 & 1) != 0) {
            unsafeCursor = -SegmentedByteString.getDEFAULT__new_UnsafeCursor();
        }
        return buffer.readUnsafe(unsafeCursor);
    }

    @JvmOverloads
    @NotNull
    public final UnsafeCursor readAndWriteUnsafe(@NotNull UnsafeCursor unsafeCursor) {
        Intrinsics.checkNotNullParameter(unsafeCursor, "unsafeCursor");
        return -Buffer.commonReadAndWriteUnsafe(this, unsafeCursor);
    }

    public static /* synthetic */ UnsafeCursor readAndWriteUnsafe$default(Buffer buffer, UnsafeCursor unsafeCursor, int n2, Object object) {
        if ((n2 & 1) != 0) {
            unsafeCursor = -SegmentedByteString.getDEFAULT__new_UnsafeCursor();
        }
        return buffer.readAndWriteUnsafe(unsafeCursor);
    }

    @Deprecated(message="moved to operator function", replaceWith=@ReplaceWith(expression="this[index]", imports={}), level=DeprecationLevel.ERROR)
    @JvmName(name="-deprecated_getByte")
    public final byte -deprecated_getByte(long index) {
        return this.getByte(index);
    }

    @Deprecated(message="moved to val", replaceWith=@ReplaceWith(expression="size", imports={}), level=DeprecationLevel.ERROR)
    @JvmName(name="-deprecated_size")
    public final long -deprecated_size() {
        return this.size;
    }

    @JvmOverloads
    @NotNull
    public final Buffer copyTo(@NotNull OutputStream out, long offset) throws IOException {
        Intrinsics.checkNotNullParameter(out, "out");
        return Buffer.copyTo$default(this, out, offset, 0L, 4, null);
    }

    @JvmOverloads
    @NotNull
    public final Buffer copyTo(@NotNull OutputStream out) throws IOException {
        Intrinsics.checkNotNullParameter(out, "out");
        return Buffer.copyTo$default(this, out, 0L, 0L, 6, null);
    }

    @JvmOverloads
    @NotNull
    public final Buffer writeTo(@NotNull OutputStream out) throws IOException {
        Intrinsics.checkNotNullParameter(out, "out");
        return Buffer.writeTo$default(this, out, 0L, 2, null);
    }

    @JvmOverloads
    @NotNull
    public final UnsafeCursor readUnsafe() {
        return Buffer.readUnsafe$default(this, null, 1, null);
    }

    @JvmOverloads
    @NotNull
    public final UnsafeCursor readAndWriteUnsafe() {
        return Buffer.readAndWriteUnsafe$default(this, null, 1, null);
    }

    @Metadata(mv={2, 1, 0}, k=1, xi=48, d1={"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0010\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0007\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u0006\u0010\u0015\u001a\u00020\u0013J\u000e\u0010\u0016\u001a\u00020\u00132\u0006\u0010\u000e\u001a\u00020\u000fJ\u000e\u0010\u0017\u001a\u00020\u000f2\u0006\u0010\u0018\u001a\u00020\u000fJ\u000e\u0010\u0019\u001a\u00020\u000f2\u0006\u0010\u001a\u001a\u00020\u0013J\b\u0010\u001b\u001a\u00020\u001cH\u0016R\u0014\u0010\u0004\u001a\u0004\u0018\u00010\u00058\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0002\n\u0000R\u0012\u0010\u0006\u001a\u00020\u00078\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0002\n\u0000R\u001c\u0010\b\u001a\u0004\u0018\u00010\tX\u0080\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR\u0012\u0010\u000e\u001a\u00020\u000f8\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0010\u001a\u0004\u0018\u00010\u00118\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0002\n\u0000R\u0012\u0010\u0012\u001a\u00020\u00138\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0002\n\u0000R\u0012\u0010\u0014\u001a\u00020\u00138\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001d"}, d2={"Lokio/Buffer$UnsafeCursor;", "Ljava/io/Closeable;", "<init>", "()V", "buffer", "Lokio/Buffer;", "readWrite", "", "segment", "Lokio/Segment;", "getSegment$okio", "()Lokio/Segment;", "setSegment$okio", "(Lokio/Segment;)V", "offset", "", "data", "", "start", "", "end", "next", "seek", "resizeBuffer", "newSize", "expandBuffer", "minByteCount", "close", "", "okio"})
    @SourceDebugExtension(value={"SMAP\nBuffer.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Buffer.kt\nokio/Buffer$UnsafeCursor\n+ 2 Buffer.kt\nokio/internal/-Buffer\n+ 3 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 4 Util.kt\nokio/-SegmentedByteString\n*L\n1#1,644:1\n1537#2:645\n1538#2:647\n1542#2:648\n1543#2,68:650\n1614#2:718\n1615#2,32:720\n1647#2,18:753\n1668#2:771\n1669#2,18:773\n1691#2:791\n1693#2,7:793\n1#3:646\n1#3:649\n1#3:719\n1#3:772\n1#3:792\n85#4:752\n*S KotlinDebug\n*F\n+ 1 Buffer.kt\nokio/Buffer$UnsafeCursor\n*L\n631#1:645\n631#1:647\n633#1:648\n633#1:650,68\n635#1:718\n635#1:720,32\n635#1:753,18\n637#1:771\n637#1:773,18\n640#1:791\n640#1:793,7\n631#1:646\n633#1:649\n635#1:719\n637#1:772\n640#1:792\n635#1:752\n*E\n"})
    public static final class UnsafeCursor
    implements Closeable {
        @JvmField
        @Nullable
        public Buffer buffer;
        @JvmField
        public boolean readWrite;
        @Nullable
        private Segment segment;
        @JvmField
        public long offset = -1L;
        @JvmField
        @Nullable
        public byte[] data;
        @JvmField
        public int start = -1;
        @JvmField
        public int end = -1;

        @Nullable
        public final Segment getSegment$okio() {
            return this.segment;
        }

        public final void setSegment$okio(@Nullable Segment segment) {
            this.segment = segment;
        }

        public final int next() {
            UnsafeCursor $this$commonNext$iv = this;
            boolean $i$f$commonNext = false;
            long l2 = $this$commonNext$iv.offset;
            Buffer buffer = $this$commonNext$iv.buffer;
            Intrinsics.checkNotNull(buffer);
            if (!(l2 != buffer.size())) {
                boolean bl = false;
                String string = "no more bytes";
                throw new IllegalStateException(string.toString());
            }
            return $this$commonNext$iv.offset == -1L ? $this$commonNext$iv.seek(0L) : $this$commonNext$iv.seek($this$commonNext$iv.offset + (long)($this$commonNext$iv.end - $this$commonNext$iv.start));
        }

        public final int seek(long offset) {
            int n2;
            UnsafeCursor $this$commonSeek$iv = this;
            boolean $i$f$commonSeek = false;
            Buffer buffer = $this$commonSeek$iv.buffer;
            if (buffer == null) {
                boolean bl = false;
                String string = "not attached to a buffer";
                throw new IllegalStateException(string.toString());
            }
            Buffer buffer$iv = buffer;
            if (offset < -1L || offset > buffer$iv.size()) {
                throw new ArrayIndexOutOfBoundsException("offset=" + offset + " > size=" + buffer$iv.size());
            }
            if (offset == -1L || offset == buffer$iv.size()) {
                $this$commonSeek$iv.setSegment$okio(null);
                $this$commonSeek$iv.offset = offset;
                $this$commonSeek$iv.data = null;
                $this$commonSeek$iv.start = -1;
                $this$commonSeek$iv.end = -1;
                n2 = -1;
            } else {
                long min$iv = 0L;
                long max$iv = buffer$iv.size();
                Segment head$iv = buffer$iv.head;
                Segment tail$iv = buffer$iv.head;
                if ($this$commonSeek$iv.getSegment$okio() != null) {
                    long l2 = $this$commonSeek$iv.offset;
                    int n3 = $this$commonSeek$iv.start;
                    Segment segment = $this$commonSeek$iv.getSegment$okio();
                    Intrinsics.checkNotNull(segment);
                    long segmentOffset$iv = l2 - (long)(n3 - segment.pos);
                    if (segmentOffset$iv > offset) {
                        max$iv = segmentOffset$iv;
                        tail$iv = $this$commonSeek$iv.getSegment$okio();
                    } else {
                        min$iv = segmentOffset$iv;
                        head$iv = $this$commonSeek$iv.getSegment$okio();
                    }
                }
                Segment next$iv = null;
                long nextOffset$iv = 0L;
                if (max$iv - offset > offset - min$iv) {
                    next$iv = head$iv;
                    nextOffset$iv = min$iv;
                    while (true) {
                        Segment segment = next$iv;
                        Intrinsics.checkNotNull(segment);
                        if (offset >= nextOffset$iv + (long)(segment.limit - next$iv.pos)) {
                            nextOffset$iv += (long)(next$iv.limit - next$iv.pos);
                            next$iv = next$iv.next;
                            continue;
                        }
                        break;
                    }
                } else {
                    Segment segment;
                    next$iv = tail$iv;
                    for (nextOffset$iv = max$iv; nextOffset$iv > offset; nextOffset$iv -= (long)(segment.limit - next$iv.pos)) {
                        Segment segment2 = next$iv;
                        Intrinsics.checkNotNull(segment2);
                        segment = next$iv = segment2.prev;
                        Intrinsics.checkNotNull(segment);
                    }
                }
                if ($this$commonSeek$iv.readWrite) {
                    Segment segment = next$iv;
                    Intrinsics.checkNotNull(segment);
                    if (segment.shared) {
                        Segment unsharedNext$iv = next$iv.unsharedCopy();
                        if (buffer$iv.head == next$iv) {
                            buffer$iv.head = unsharedNext$iv;
                        }
                        next$iv = next$iv.push(unsharedNext$iv);
                        Segment segment3 = next$iv.prev;
                        Intrinsics.checkNotNull(segment3);
                        segment3.pop();
                    }
                }
                $this$commonSeek$iv.setSegment$okio(next$iv);
                $this$commonSeek$iv.offset = offset;
                Segment segment = next$iv;
                Intrinsics.checkNotNull(segment);
                $this$commonSeek$iv.data = segment.data;
                $this$commonSeek$iv.start = next$iv.pos + (int)(offset - nextOffset$iv);
                $this$commonSeek$iv.end = next$iv.limit;
                n2 = $this$commonSeek$iv.end - $this$commonSeek$iv.start;
            }
            return n2;
        }

        public final long resizeBuffer(long newSize) {
            UnsafeCursor $this$commonResizeBuffer$iv = this;
            boolean $i$f$commonResizeBuffer = false;
            Buffer buffer = $this$commonResizeBuffer$iv.buffer;
            if (buffer == null) {
                boolean $i$a$-checkNotNull--Buffer$commonResizeBuffer$buffer$1$iv22 = false;
                String $i$a$-checkNotNull--Buffer$commonResizeBuffer$buffer$1$iv22 = "not attached to a buffer";
                throw new IllegalStateException($i$a$-checkNotNull--Buffer$commonResizeBuffer$buffer$1$iv22.toString());
            }
            Buffer buffer$iv = buffer;
            if (!$this$commonResizeBuffer$iv.readWrite) {
                boolean bl = false;
                String string = "resizeBuffer() only permitted for read/write buffers";
                throw new IllegalStateException(string.toString());
            }
            long oldSize$iv = buffer$iv.size();
            if (newSize <= oldSize$iv) {
                int tailSize$iv;
                if (!(newSize >= 0L)) {
                    boolean bl = false;
                    String string = "newSize < 0: " + newSize;
                    throw new IllegalArgumentException(string.toString());
                }
                for (long bytesToSubtract$iv = oldSize$iv - newSize; bytesToSubtract$iv > 0L; bytesToSubtract$iv -= (long)tailSize$iv) {
                    Segment tail$iv;
                    Segment segment = buffer$iv.head;
                    Intrinsics.checkNotNull(segment);
                    Segment segment2 = tail$iv = segment.prev;
                    Intrinsics.checkNotNull(segment2);
                    tailSize$iv = segment2.limit - tail$iv.pos;
                    if ((long)tailSize$iv <= bytesToSubtract$iv) {
                        buffer$iv.head = tail$iv.pop();
                        SegmentPool.recycle(tail$iv);
                        continue;
                    }
                    tail$iv.limit -= (int)bytesToSubtract$iv;
                    break;
                }
                $this$commonResizeBuffer$iv.setSegment$okio(null);
                $this$commonResizeBuffer$iv.offset = newSize;
                $this$commonResizeBuffer$iv.data = null;
                $this$commonResizeBuffer$iv.start = -1;
                $this$commonResizeBuffer$iv.end = -1;
            } else if (newSize > oldSize$iv) {
                int segmentBytesToAdd$iv;
                boolean needsToSeek$iv = true;
                for (long bytesToAdd$iv = newSize - oldSize$iv; bytesToAdd$iv > 0L; bytesToAdd$iv -= (long)segmentBytesToAdd$iv) {
                    Segment tail$iv = buffer$iv.writableSegment$okio(1);
                    int b$iv$iv = 8192 - tail$iv.limit;
                    boolean $i$f$minOf = false;
                    segmentBytesToAdd$iv = (int)Math.min(bytesToAdd$iv, (long)b$iv$iv);
                    tail$iv.limit += segmentBytesToAdd$iv;
                    if (!needsToSeek$iv) continue;
                    $this$commonResizeBuffer$iv.setSegment$okio(tail$iv);
                    $this$commonResizeBuffer$iv.offset = oldSize$iv;
                    $this$commonResizeBuffer$iv.data = tail$iv.data;
                    $this$commonResizeBuffer$iv.start = tail$iv.limit - segmentBytesToAdd$iv;
                    $this$commonResizeBuffer$iv.end = tail$iv.limit;
                    needsToSeek$iv = false;
                }
            }
            buffer$iv.setSize$okio(newSize);
            return oldSize$iv;
        }

        public final long expandBuffer(int minByteCount) {
            UnsafeCursor $this$commonExpandBuffer$iv = this;
            boolean $i$f$commonExpandBuffer = false;
            if (!(minByteCount > 0)) {
                boolean $i$a$-require--Buffer$commonExpandBuffer$1$iv22 = false;
                String $i$a$-require--Buffer$commonExpandBuffer$1$iv22 = "minByteCount <= 0: " + minByteCount;
                throw new IllegalArgumentException($i$a$-require--Buffer$commonExpandBuffer$1$iv22.toString());
            }
            if (!(minByteCount <= 8192)) {
                boolean bl = false;
                String string = "minByteCount > Segment.SIZE: " + minByteCount;
                throw new IllegalArgumentException(string.toString());
            }
            Buffer buffer = $this$commonExpandBuffer$iv.buffer;
            if (buffer == null) {
                boolean $i$a$-checkNotNull--Buffer$commonExpandBuffer$buffer$1$iv22 = false;
                String $i$a$-checkNotNull--Buffer$commonExpandBuffer$buffer$1$iv22 = "not attached to a buffer";
                throw new IllegalStateException($i$a$-checkNotNull--Buffer$commonExpandBuffer$buffer$1$iv22.toString());
            }
            Buffer buffer$iv = buffer;
            if (!$this$commonExpandBuffer$iv.readWrite) {
                boolean bl = false;
                String string = "expandBuffer() only permitted for read/write buffers";
                throw new IllegalStateException(string.toString());
            }
            long oldSize$iv = buffer$iv.size();
            Segment tail$iv = buffer$iv.writableSegment$okio(minByteCount);
            int result$iv = 8192 - tail$iv.limit;
            tail$iv.limit = 8192;
            buffer$iv.setSize$okio(oldSize$iv + (long)result$iv);
            $this$commonExpandBuffer$iv.setSegment$okio(tail$iv);
            $this$commonExpandBuffer$iv.offset = oldSize$iv;
            $this$commonExpandBuffer$iv.data = tail$iv.data;
            $this$commonExpandBuffer$iv.start = 8192 - result$iv;
            $this$commonExpandBuffer$iv.end = 8192;
            return result$iv;
        }

        @Override
        public void close() {
            UnsafeCursor $this$commonClose$iv = this;
            boolean $i$f$commonClose = false;
            if (!($this$commonClose$iv.buffer != null)) {
                boolean bl = false;
                String string = "not attached to a buffer";
                throw new IllegalStateException(string.toString());
            }
            $this$commonClose$iv.buffer = null;
            $this$commonClose$iv.setSegment$okio(null);
            $this$commonClose$iv.offset = -1L;
            $this$commonClose$iv.data = null;
            $this$commonClose$iv.start = -1;
            $this$commonClose$iv.end = -1;
        }
    }
}

