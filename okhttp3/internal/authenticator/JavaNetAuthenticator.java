/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.authenticator;

import java.io.IOException;
import java.net.Authenticator;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.SocketAddress;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import okhttp3.Address;
import okhttp3.Authenticator;
import okhttp3.Challenge;
import okhttp3.Credentials;
import okhttp3.Dns;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u000f\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u001c\u0010\u0005\u001a\u0004\u0018\u00010\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\b2\u0006\u0010\t\u001a\u00020\nH\u0016J\u001c\u0010\u000b\u001a\u00020\f*\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0003H\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0011"}, d2={"Lokhttp3/internal/authenticator/JavaNetAuthenticator;", "Lokhttp3/Authenticator;", "defaultDns", "Lokhttp3/Dns;", "(Lokhttp3/Dns;)V", "authenticate", "Lokhttp3/Request;", "route", "Lokhttp3/Route;", "response", "Lokhttp3/Response;", "connectToInetAddress", "Ljava/net/InetAddress;", "Ljava/net/Proxy;", "url", "Lokhttp3/HttpUrl;", "dns", "okhttp"})
public final class JavaNetAuthenticator
implements Authenticator {
    @NotNull
    private final Dns defaultDns;

    public JavaNetAuthenticator(@NotNull Dns defaultDns) {
        Intrinsics.checkNotNullParameter(defaultDns, "defaultDns");
        this.defaultDns = defaultDns;
    }

    public /* synthetic */ JavaNetAuthenticator(Dns dns, int n2, DefaultConstructorMarker defaultConstructorMarker) {
        if ((n2 & 1) != 0) {
            dns = Dns.SYSTEM;
        }
        this(dns);
    }

    @Override
    @Nullable
    public Request authenticate(@Nullable Route route, @NotNull Response response) throws IOException {
        Intrinsics.checkNotNullParameter(response, "response");
        List<Challenge> challenges = response.challenges();
        Request request = response.request();
        HttpUrl url = request.url();
        boolean proxyAuthorization = response.code() == 407;
        Route route2 = route;
        Proxy proxy = route2 == null ? null : route2.proxy();
        if (proxy == null) {
            proxy = Proxy.NO_PROXY;
        }
        Proxy proxy2 = proxy;
        for (Challenge challenge : challenges) {
            PasswordAuthentication auth;
            PasswordAuthentication passwordAuthentication;
            Dns dns;
            Dns dns2;
            Dns dns3;
            if (!StringsKt.equals("Basic", challenge.scheme(), true)) continue;
            Route route3 = route;
            if (route3 == null) {
                dns3 = null;
            } else {
                Address address = route3.address();
                dns3 = dns2 = address == null ? null : address.dns();
            }
            if (dns3 == null) {
                dns2 = dns = this.defaultDns;
            }
            if (proxyAuthorization) {
                SocketAddress socketAddress = proxy2.address();
                if (socketAddress == null) {
                    throw new NullPointerException("null cannot be cast to non-null type java.net.InetSocketAddress");
                }
                InetSocketAddress proxyAddress = (InetSocketAddress)socketAddress;
                String string = proxyAddress.getHostName();
                Intrinsics.checkNotNullExpressionValue(proxy2, "proxy");
                passwordAuthentication = java.net.Authenticator.requestPasswordAuthentication(string, this.connectToInetAddress(proxy2, url, dns), proxyAddress.getPort(), url.scheme(), challenge.realm(), challenge.scheme(), url.url(), Authenticator.RequestorType.PROXY);
            } else {
                String string = url.host();
                Intrinsics.checkNotNullExpressionValue(proxy2, "proxy");
                passwordAuthentication = java.net.Authenticator.requestPasswordAuthentication(string, this.connectToInetAddress(proxy2, url, dns), url.port(), url.scheme(), challenge.realm(), challenge.scheme(), url.url(), Authenticator.RequestorType.SERVER);
            }
            if ((auth = passwordAuthentication) == null) continue;
            String credentialHeader = proxyAuthorization ? "Proxy-Authorization" : "Authorization";
            Object object = auth.getUserName();
            Intrinsics.checkNotNullExpressionValue(object, "auth.userName");
            String string = object;
            object = auth.getPassword();
            Intrinsics.checkNotNullExpressionValue(object, "auth.password");
            String credential = Credentials.basic(string, new String((char[])object), challenge.charset());
            return request.newBuilder().header(credentialHeader, credential).build();
        }
        return null;
    }

    private final InetAddress connectToInetAddress(Proxy $this$connectToInetAddress, HttpUrl url, Dns dns) throws IOException {
        InetAddress inetAddress;
        Proxy.Type type = $this$connectToInetAddress.type();
        if ((type == null ? -1 : WhenMappings.$EnumSwitchMapping$0[type.ordinal()]) == 1) {
            inetAddress = CollectionsKt.first(dns.lookup(url.host()));
        } else {
            SocketAddress socketAddress = $this$connectToInetAddress.address();
            if (socketAddress == null) {
                throw new NullPointerException("null cannot be cast to non-null type java.net.InetSocketAddress");
            }
            InetAddress inetAddress2 = ((InetSocketAddress)socketAddress).getAddress();
            Intrinsics.checkNotNullExpressionValue(inetAddress2, "address() as InetSocketAddress).address");
            inetAddress = inetAddress2;
        }
        return inetAddress;
    }

    public JavaNetAuthenticator() {
        this(null, 1, null);
    }

    @Metadata(mv={1, 6, 0}, k=3, xi=48)
    public final class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] nArray = new int[Proxy.Type.values().length];
            nArray[Proxy.Type.DIRECT.ordinal()] = 1;
            $EnumSwitchMapping$0 = nArray;
        }
    }
}

