/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.Log
 */
package okhttp3.internal.platform.android;

import android.util.Log;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.Level;
import java.util.logging.Logger;
import kotlin.Metadata;
import kotlin.collections.MapsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import okhttp3.OkHttpClient;
import okhttp3.internal.SuppressSignatureCheck;
import okhttp3.internal.concurrent.TaskRunner;
import okhttp3.internal.http2.Http2;
import okhttp3.internal.platform.android.AndroidLogHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u0003\n\u0002\b\u0007\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J/\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\n2\u0006\u0010\u000e\u001a\u00020\u00042\u0006\u0010\u000f\u001a\u00020\n2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0011H\u0000\u00a2\u0006\u0002\b\u0012J\u0006\u0010\u0013\u001a\u00020\fJ\u0018\u0010\u0014\u001a\u00020\f2\u0006\u0010\u0015\u001a\u00020\n2\u0006\u0010\u0016\u001a\u00020\nH\u0002J\u0010\u0010\u0017\u001a\u00020\n2\u0006\u0010\r\u001a\u00020\nH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\n0\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0018"}, d2={"Lokhttp3/internal/platform/android/AndroidLog;", "", "()V", "MAX_LOG_LENGTH", "", "configuredLoggers", "Ljava/util/concurrent/CopyOnWriteArraySet;", "Ljava/util/logging/Logger;", "knownLoggers", "", "", "androidLog", "", "loggerName", "logLevel", "message", "t", "", "androidLog$okhttp", "enable", "enableLogging", "logger", "tag", "loggerTag", "okhttp"})
@SuppressSignatureCheck
public final class AndroidLog {
    @NotNull
    public static final AndroidLog INSTANCE;
    private static final int MAX_LOG_LENGTH = 4000;
    @NotNull
    private static final CopyOnWriteArraySet<Logger> configuredLoggers;
    @NotNull
    private static final Map<String, String> knownLoggers;

    private AndroidLog() {
    }

    public final void androidLog$okhttp(@NotNull String loggerName, int logLevel, @NotNull String message, @Nullable Throwable t2) {
        Intrinsics.checkNotNullParameter(loggerName, "loggerName");
        Intrinsics.checkNotNullParameter(message, "message");
        String tag = this.loggerTag(loggerName);
        if (Log.isLoggable((String)tag, (int)logLevel)) {
            String logMessage = message;
            if (t2 != null) {
                logMessage = logMessage + '\n' + Log.getStackTraceString((Throwable)t2);
            }
            int i2 = 0;
            int length = logMessage.length();
            while (i2 < length) {
                int end;
                int newline = StringsKt.indexOf$default((CharSequence)logMessage, '\n', i2, false, 4, null);
                newline = newline != -1 ? newline : length;
                do {
                    int n2 = i2 + 4000;
                    end = Math.min(newline, n2);
                    String string = logMessage.substring(i2, end);
                    Intrinsics.checkNotNullExpressionValue(string, "this as java.lang.String\u2026ing(startIndex, endIndex)");
                    Log.println((int)logLevel, (String)tag, (String)string);
                } while ((i2 = end) < newline);
                int n3 = i2;
                i2 = n3 + 1;
            }
        }
    }

    private final String loggerTag(String loggerName) {
        String string = knownLoggers.get(loggerName);
        if (string == null) {
            string = StringsKt.take(loggerName, 23);
        }
        return string;
    }

    public final void enable() {
        for (Map.Entry<String, String> entry : knownLoggers.entrySet()) {
            String logger = entry.getKey();
            String tag = entry.getValue();
            this.enableLogging(logger, tag);
        }
    }

    private final void enableLogging(String logger, String tag) {
        Logger logger2 = Logger.getLogger(logger);
        if (configuredLoggers.add(logger2)) {
            logger2.setUseParentHandlers(false);
            logger2.setLevel(Log.isLoggable((String)tag, (int)3) ? Level.FINE : (Log.isLoggable((String)tag, (int)4) ? Level.INFO : Level.WARNING));
            logger2.addHandler(AndroidLogHandler.INSTANCE);
        }
    }

    static {
        String string;
        Map map;
        String packageName;
        LinkedHashMap linkedHashMap;
        INSTANCE = new AndroidLog();
        configuredLoggers = new CopyOnWriteArraySet();
        LinkedHashMap $this$knownLoggers_u24lambda_u2d0 = linkedHashMap = new LinkedHashMap();
        boolean bl = false;
        Package package_ = OkHttpClient.class.getPackage();
        String string2 = packageName = package_ == null ? null : package_.getName();
        if (packageName != null) {
            map = $this$knownLoggers_u24lambda_u2d0;
            string = "OkHttp";
            map.put(packageName, string);
        }
        map = $this$knownLoggers_u24lambda_u2d0;
        string = OkHttpClient.class.getName();
        Intrinsics.checkNotNullExpressionValue(string, "OkHttpClient::class.java.name");
        String string3 = "okhttp.OkHttpClient";
        map.put(string, string3);
        map = $this$knownLoggers_u24lambda_u2d0;
        string = Http2.class.getName();
        Intrinsics.checkNotNullExpressionValue(string, "Http2::class.java.name");
        string3 = "okhttp.Http2";
        map.put(string, string3);
        map = $this$knownLoggers_u24lambda_u2d0;
        string = TaskRunner.class.getName();
        Intrinsics.checkNotNullExpressionValue(string, "TaskRunner::class.java.name");
        string3 = "okhttp.TaskRunner";
        map.put(string, string3);
        map = $this$knownLoggers_u24lambda_u2d0;
        string = "okhttp3.mockwebserver.MockWebServer";
        string3 = "okhttp.MockWebServer";
        map.put(string, string3);
        knownLoggers = MapsKt.toMap(linkedHashMap);
    }
}

