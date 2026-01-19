/*
 * Decompiled with CFR 0.152.
 */
package okio;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import okio.AsyncTimeout;
import okio.Okio;
import okio.Okio__JvmOkioKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={2, 1, 0}, k=1, xi=48, d1={"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0004\b\u0004\u0010\u0005J\u0012\u0010\u0006\u001a\u00020\u00072\b\u0010\b\u001a\u0004\u0018\u00010\u0007H\u0014J\b\u0010\t\u001a\u00020\nH\u0014R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000b"}, d2={"Lokio/SocketAsyncTimeout;", "Lokio/AsyncTimeout;", "socket", "Ljava/net/Socket;", "<init>", "(Ljava/net/Socket;)V", "newTimeoutException", "Ljava/io/IOException;", "cause", "timedOut", "", "okio"})
final class SocketAsyncTimeout
extends AsyncTimeout {
    @NotNull
    private final Socket socket;

    public SocketAsyncTimeout(@NotNull Socket socket) {
        Intrinsics.checkNotNullParameter(socket, "socket");
        this.socket = socket;
    }

    @Override
    @NotNull
    protected IOException newTimeoutException(@Nullable IOException cause) {
        SocketTimeoutException ioe = new SocketTimeoutException("timeout");
        if (cause != null) {
            ioe.initCause(cause);
        }
        return ioe;
    }

    @Override
    protected void timedOut() {
        try {
            this.socket.close();
        }
        catch (Exception e2) {
            Okio__JvmOkioKt.access$getLogger$p().log(Level.WARNING, "Failed to close timed out socket " + this.socket, e2);
        }
        catch (AssertionError e3) {
            if (Okio.isAndroidGetsocknameError(e3)) {
                Okio__JvmOkioKt.access$getLogger$p().log(Level.WARNING, "Failed to close timed out socket " + this.socket, (Throwable)((Object)e3));
            }
            throw e3;
        }
    }
}

