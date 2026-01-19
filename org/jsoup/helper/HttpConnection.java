/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package org.jsoup.helper;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UncheckedIOException;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
import javax.net.ssl.SSLSocketFactory;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Progress;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.helper.AuthenticationHandler;
import org.jsoup.helper.CookieUtil;
import org.jsoup.helper.DataUtil;
import org.jsoup.helper.RequestAuthenticator;
import org.jsoup.helper.RequestDispatch;
import org.jsoup.helper.RequestExecutor;
import org.jsoup.helper.UrlBuilder;
import org.jsoup.helper.Validate;
import org.jsoup.internal.ControllableInputStream;
import org.jsoup.internal.Normalizer;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.parser.StreamParser;
import org.jspecify.annotations.Nullable;

public class HttpConnection
implements Connection {
    public static final String CONTENT_ENCODING = "Content-Encoding";
    public static final String DEFAULT_UA = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36";
    private static final String USER_AGENT = "User-Agent";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String MULTIPART_FORM_DATA = "multipart/form-data";
    public static final String FORM_URL_ENCODED = "application/x-www-form-urlencoded";
    private static final int HTTP_TEMP_REDIR = 307;
    static final String DefaultUploadType = "application/octet-stream";
    private static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");
    private Request req;
    private @Nullable Connection.Response res;
    @Nullable Object client;
    @Nullable RequestAuthenticator lastAuth;

    public static Connection connect(String url) {
        HttpConnection con = new HttpConnection();
        con.url(url);
        return con;
    }

    public static Connection connect(URL url) {
        HttpConnection con = new HttpConnection();
        con.url(url);
        return con;
    }

    public HttpConnection() {
        this.req = new Request();
        this.req.connection = this;
    }

    HttpConnection(Request copy) {
        this.req = new Request(copy);
    }

    static String encodeMimeName(String val) {
        return val.replace("\"", "%22");
    }

    @Override
    public Connection newRequest() {
        return new HttpConnection(this.req);
    }

    private HttpConnection(Request req, Response res) {
        this.req = req;
        this.res = res;
    }

    @Override
    public Connection url(URL url) {
        this.req.url(url);
        return this;
    }

    @Override
    public Connection url(String url) {
        Validate.notEmptyParam(url, "url");
        try {
            this.req.url(new URL(url));
        }
        catch (MalformedURLException e2) {
            throw new IllegalArgumentException(String.format("The supplied URL, '%s', is malformed. Make sure it is an absolute URL, and starts with 'http://' or 'https://'. See https://jsoup.org/cookbook/extracting-data/working-with-urls", url), e2);
        }
        return this;
    }

    @Override
    public Connection proxy(@Nullable Proxy proxy) {
        this.req.proxy(proxy);
        return this;
    }

    @Override
    public Connection proxy(String host, int port) {
        this.req.proxy(host, port);
        return this;
    }

    @Override
    public Connection userAgent(String userAgent) {
        Validate.notNullParam(userAgent, "userAgent");
        this.req.header(USER_AGENT, userAgent);
        return this;
    }

    @Override
    public Connection timeout(int millis) {
        this.req.timeout(millis);
        return this;
    }

    @Override
    public Connection maxBodySize(int bytes) {
        this.req.maxBodySize(bytes);
        return this;
    }

    @Override
    public Connection followRedirects(boolean followRedirects) {
        this.req.followRedirects(followRedirects);
        return this;
    }

    @Override
    public Connection referrer(String referrer) {
        Validate.notNullParam(referrer, "referrer");
        this.req.header("Referer", referrer);
        return this;
    }

    @Override
    public Connection method(Connection.Method method) {
        this.req.method(method);
        return this;
    }

    @Override
    public Connection ignoreHttpErrors(boolean ignoreHttpErrors) {
        this.req.ignoreHttpErrors(ignoreHttpErrors);
        return this;
    }

    @Override
    public Connection ignoreContentType(boolean ignoreContentType) {
        this.req.ignoreContentType(ignoreContentType);
        return this;
    }

    @Override
    public Connection data(String key, String value) {
        this.req.data(KeyVal.create(key, value));
        return this;
    }

    @Override
    public Connection sslSocketFactory(SSLSocketFactory sslSocketFactory) {
        this.req.sslSocketFactory(sslSocketFactory);
        return this;
    }

    @Override
    public Connection data(String key, String filename, InputStream inputStream2) {
        this.req.data(KeyVal.create(key, filename, inputStream2));
        return this;
    }

    @Override
    public Connection data(String key, String filename, InputStream inputStream2, String contentType) {
        this.req.data(KeyVal.create(key, filename, inputStream2).contentType(contentType));
        return this;
    }

    @Override
    public Connection data(Map<String, String> data) {
        Validate.notNullParam(data, "data");
        for (Map.Entry<String, String> entry : data.entrySet()) {
            this.req.data(KeyVal.create(entry.getKey(), entry.getValue()));
        }
        return this;
    }

    @Override
    public Connection data(String ... keyvals) {
        Validate.notNullParam(keyvals, "keyvals");
        Validate.isTrue(keyvals.length % 2 == 0, "Must supply an even number of key value pairs");
        for (int i2 = 0; i2 < keyvals.length; i2 += 2) {
            String key = keyvals[i2];
            String value = keyvals[i2 + 1];
            Validate.notEmpty(key, "Data key must not be empty");
            Validate.notNull(value, "Data value must not be null");
            this.req.data(KeyVal.create(key, value));
        }
        return this;
    }

    @Override
    public Connection data(Collection<Connection.KeyVal> data) {
        Validate.notNullParam(data, "data");
        for (Connection.KeyVal entry : data) {
            this.req.data(entry);
        }
        return this;
    }

    @Override
    public @Nullable Connection.KeyVal data(String key) {
        Validate.notEmptyParam(key, "key");
        for (Connection.KeyVal keyVal : this.request().data()) {
            if (!keyVal.key().equals(key)) continue;
            return keyVal;
        }
        return null;
    }

    @Override
    public Connection requestBody(String body) {
        this.req.requestBody(body);
        return this;
    }

    @Override
    public Connection header(String name, String value) {
        this.req.header(name, value);
        return this;
    }

    @Override
    public Connection headers(Map<String, String> headers) {
        Validate.notNullParam(headers, "headers");
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            this.req.header(entry.getKey(), entry.getValue());
        }
        return this;
    }

    @Override
    public Connection cookie(String name, String value) {
        this.req.cookie(name, value);
        return this;
    }

    @Override
    public Connection cookies(Map<String, String> cookies) {
        Validate.notNullParam(cookies, "cookies");
        for (Map.Entry<String, String> entry : cookies.entrySet()) {
            this.req.cookie(entry.getKey(), entry.getValue());
        }
        return this;
    }

    @Override
    public Connection cookieStore(CookieStore cookieStore) {
        this.req.cookieManager = new CookieManager(cookieStore, null);
        return this;
    }

    @Override
    public CookieStore cookieStore() {
        return this.req.cookieManager.getCookieStore();
    }

    @Override
    public Connection parser(Parser parser) {
        this.req.parser(parser);
        return this;
    }

    @Override
    public Document get() throws IOException {
        this.req.method(Connection.Method.GET);
        this.execute();
        Validate.notNull(this.res);
        return this.res.parse();
    }

    @Override
    public Document post() throws IOException {
        this.req.method(Connection.Method.POST);
        this.execute();
        Validate.notNull(this.res);
        return this.res.parse();
    }

    @Override
    public Connection.Response execute() throws IOException {
        this.res = Response.execute(this.req);
        return this.res;
    }

    @Override
    public Connection.Request request() {
        return this.req;
    }

    @Override
    public Connection request(Connection.Request request) {
        this.req = (Request)request;
        return this;
    }

    @Override
    public Connection.Response response() {
        if (this.res == null) {
            throw new IllegalArgumentException("You must execute the request before getting a response.");
        }
        return this.res;
    }

    @Override
    public Connection response(Connection.Response response) {
        this.res = response;
        return this;
    }

    @Override
    public Connection postDataCharset(String charset) {
        this.req.postDataCharset(charset);
        return this;
    }

    @Override
    public Connection auth(@Nullable RequestAuthenticator authenticator) {
        this.req.auth(authenticator);
        return this;
    }

    @Override
    public Connection onResponseProgress(Progress<Connection.Response> handler) {
        this.req.responseProgress = handler;
        return this;
    }

    private static boolean needsMultipart(Connection.Request req) {
        for (Connection.KeyVal keyVal : req.data()) {
            if (!keyVal.hasInputStream()) continue;
            return true;
        }
        return false;
    }

    public static class Request
    extends Base<Connection.Request>
    implements Connection.Request {
        HttpConnection connection;
        private @Nullable Proxy proxy;
        private int timeoutMilliseconds;
        private int maxBodySizeBytes;
        private boolean followRedirects;
        private final Collection<Connection.KeyVal> data;
        private @Nullable String body = null;
        @Nullable String mimeBoundary;
        private boolean ignoreHttpErrors = false;
        private boolean ignoreContentType = false;
        private Parser parser;
        private boolean parserDefined = false;
        private String postDataCharset = DataUtil.defaultCharsetName;
        private @Nullable SSLSocketFactory sslSocketFactory;
        private CookieManager cookieManager;
        @Nullable RequestAuthenticator authenticator;
        private @Nullable Progress<Connection.Response> responseProgress;
        private volatile boolean executing = false;

        Request() {
            this.timeoutMilliseconds = 30000;
            this.maxBodySizeBytes = 0x200000;
            this.followRedirects = true;
            this.data = new ArrayList<Connection.KeyVal>();
            this.method = Connection.Method.GET;
            this.addHeader("Accept-Encoding", "gzip");
            this.addHeader(HttpConnection.USER_AGENT, HttpConnection.DEFAULT_UA);
            this.parser = Parser.htmlParser();
            this.cookieManager = new CookieManager();
        }

        Request(Request copy) {
            super(copy);
            this.connection = copy.connection;
            this.proxy = copy.proxy;
            this.postDataCharset = copy.postDataCharset;
            this.timeoutMilliseconds = copy.timeoutMilliseconds;
            this.maxBodySizeBytes = copy.maxBodySizeBytes;
            this.followRedirects = copy.followRedirects;
            this.data = new ArrayList<Connection.KeyVal>();
            this.ignoreHttpErrors = copy.ignoreHttpErrors;
            this.ignoreContentType = copy.ignoreContentType;
            this.parser = copy.parser.newInstance();
            this.parserDefined = copy.parserDefined;
            this.sslSocketFactory = copy.sslSocketFactory;
            this.cookieManager = copy.cookieManager;
            this.authenticator = copy.authenticator;
            this.responseProgress = copy.responseProgress;
            this.executing = false;
        }

        @Override
        public @Nullable Proxy proxy() {
            return this.proxy;
        }

        @Override
        public Request proxy(@Nullable Proxy proxy) {
            this.proxy = proxy;
            return this;
        }

        @Override
        public Request proxy(String host, int port) {
            this.proxy = new Proxy(Proxy.Type.HTTP, InetSocketAddress.createUnresolved(host, port));
            return this;
        }

        @Override
        public int timeout() {
            return this.timeoutMilliseconds;
        }

        @Override
        public Request timeout(int millis) {
            Validate.isTrue(millis >= 0, "Timeout milliseconds must be 0 (infinite) or greater");
            this.timeoutMilliseconds = millis;
            return this;
        }

        @Override
        public int maxBodySize() {
            return this.maxBodySizeBytes;
        }

        @Override
        public Connection.Request maxBodySize(int bytes) {
            Validate.isTrue(bytes >= 0, "maxSize must be 0 (unlimited) or larger");
            this.maxBodySizeBytes = bytes;
            return this;
        }

        @Override
        public boolean followRedirects() {
            return this.followRedirects;
        }

        @Override
        public Connection.Request followRedirects(boolean followRedirects) {
            this.followRedirects = followRedirects;
            return this;
        }

        @Override
        public boolean ignoreHttpErrors() {
            return this.ignoreHttpErrors;
        }

        @Override
        public @Nullable SSLSocketFactory sslSocketFactory() {
            return this.sslSocketFactory;
        }

        @Override
        public void sslSocketFactory(SSLSocketFactory sslSocketFactory) {
            this.sslSocketFactory = sslSocketFactory;
        }

        @Override
        public Connection.Request ignoreHttpErrors(boolean ignoreHttpErrors) {
            this.ignoreHttpErrors = ignoreHttpErrors;
            return this;
        }

        @Override
        public boolean ignoreContentType() {
            return this.ignoreContentType;
        }

        @Override
        public Connection.Request ignoreContentType(boolean ignoreContentType) {
            this.ignoreContentType = ignoreContentType;
            return this;
        }

        @Override
        public Request data(Connection.KeyVal keyval) {
            Validate.notNullParam(keyval, "keyval");
            this.data.add(keyval);
            return this;
        }

        @Override
        public Collection<Connection.KeyVal> data() {
            return this.data;
        }

        @Override
        public Connection.Request requestBody(@Nullable String body) {
            this.body = body;
            return this;
        }

        @Override
        public @Nullable String requestBody() {
            return this.body;
        }

        @Override
        public Request parser(Parser parser) {
            this.parser = parser;
            this.parserDefined = true;
            return this;
        }

        @Override
        public Parser parser() {
            return this.parser;
        }

        @Override
        public Connection.Request postDataCharset(String charset) {
            Validate.notNullParam(charset, "charset");
            if (!Charset.isSupported(charset)) {
                throw new IllegalCharsetNameException(charset);
            }
            this.postDataCharset = charset;
            return this;
        }

        @Override
        public String postDataCharset() {
            return this.postDataCharset;
        }

        CookieManager cookieManager() {
            return this.cookieManager;
        }

        @Override
        public Connection.Request auth(@Nullable RequestAuthenticator authenticator) {
            this.authenticator = authenticator;
            return this;
        }

        @Override
        public @Nullable RequestAuthenticator auth() {
            return this.authenticator;
        }

        static {
            System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
        }
    }

    public static class Response
    extends Base<Connection.Response>
    implements Connection.Response {
        private static final int MAX_REDIRECTS = 20;
        private static final String LOCATION = "Location";
        int statusCode;
        @Nullable String statusMessage;
        private @Nullable ByteBuffer byteData;
        private @Nullable ControllableInputStream bodyStream;
        @Nullable RequestExecutor executor;
        private @Nullable String charset;
        @Nullable String contentType;
        int contentLength;
        private boolean executed = false;
        private boolean inputStreamRead = false;
        private int numRedirects = 0;
        private final Request req;
        private static final Pattern xmlContentTypeRxp = Pattern.compile("(\\w+)/\\w*\\+?xml.*");

        Response() {
            this.statusCode = 400;
            this.statusMessage = "Request not made";
            this.req = new Request();
            this.contentType = null;
        }

        static Response execute(Request req) throws IOException {
            return Response.execute(req, null);
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        static Response execute(Request req, @Nullable Response prevRes) throws IOException {
            boolean hasBody;
            Request request = req;
            synchronized (request) {
                Validate.isFalse(req.executing, "Multiple threads were detected trying to execute the same request concurrently. Make sure to use Connection#newRequest() and do not share an executing request between threads.");
                req.executing = true;
            }
            Validate.notNullParam(req, "req");
            URL url = req.url();
            Validate.notNull(url, "URL must be specified to connect");
            String protocol = url.getProtocol();
            if (!protocol.equals("http") && !protocol.equals("https")) {
                throw new MalformedURLException("Only http & https protocols supported");
            }
            boolean supportsBody = req.method().hasBody();
            boolean bl = hasBody = req.requestBody() != null;
            if (!supportsBody) {
                Validate.isFalse(hasBody, "Cannot set a request body for HTTP method " + (Object)((Object)req.method()));
            }
            if (!(req.data().isEmpty() || supportsBody && !hasBody)) {
                Response.serialiseRequestUrl(req);
            } else if (supportsBody) {
                Response.setOutputContentType(req);
            }
            long startTime = System.nanoTime();
            RequestExecutor executor = RequestDispatch.get(req, prevRes);
            Response res = null;
            try {
                res = executor.execute();
                if (res.hasHeader(LOCATION) && req.followRedirects()) {
                    if (res.statusCode != 307) {
                        req.method(Connection.Method.GET);
                        req.data().clear();
                        req.requestBody(null);
                        req.removeHeader(HttpConnection.CONTENT_TYPE);
                    }
                    String location = res.header(LOCATION);
                    Validate.notNull(location);
                    if (location.startsWith("http:/") && location.charAt(6) != '/') {
                        location = location.substring(6);
                    }
                    URL redir = StringUtil.resolve(req.url(), location);
                    req.url(redir);
                    req.executing = false;
                    Response response = Response.execute(req, res);
                    return response;
                }
                if (!(res.statusCode >= 200 && res.statusCode < 400 || req.ignoreHttpErrors())) {
                    throw new HttpStatusException("HTTP error fetching URL", res.statusCode, req.url().toString());
                }
                String contentType = res.contentType();
                if (!(contentType == null || req.ignoreContentType() || contentType.startsWith("text/") || xmlContentTypeRxp.matcher(contentType).matches())) {
                    throw new UnsupportedMimeTypeException("Unhandled content type. Must be text/*, */xml, or */*+xml", contentType, req.url().toString());
                }
                if (contentType != null && xmlContentTypeRxp.matcher(contentType).matches() && !req.parserDefined) {
                    req.parser(Parser.xmlParser());
                }
                res.charset = DataUtil.getCharsetFromContentType(res.contentType);
                if (res.contentLength != 0 && req.method() != Connection.Method.HEAD) {
                    InputStream stream = executor.responseBody();
                    if (res.hasHeaderWithValue(HttpConnection.CONTENT_ENCODING, "gzip")) {
                        stream = new GZIPInputStream(stream);
                    } else if (res.hasHeaderWithValue(HttpConnection.CONTENT_ENCODING, "deflate")) {
                        stream = new InflaterInputStream(stream, new Inflater(true));
                    }
                    res.bodyStream = ControllableInputStream.wrap(stream, 8192, req.maxBodySize()).timeout(startTime, req.timeout());
                    if (req.responseProgress != null) {
                        res.bodyStream.onProgress(res.contentLength, req.responseProgress, res);
                    }
                } else {
                    res.byteData = DataUtil.emptyByteBuffer();
                }
            }
            catch (IOException e2) {
                if (res != null) {
                    res.safeClose();
                }
                throw e2;
            }
            finally {
                req.executing = false;
                if (req.authenticator != null) {
                    AuthenticationHandler.handler.remove();
                }
            }
            res.executed = true;
            return res;
        }

        @Override
        public int statusCode() {
            return this.statusCode;
        }

        @Override
        public String statusMessage() {
            return this.statusMessage;
        }

        @Override
        public @Nullable String charset() {
            return this.charset;
        }

        @Override
        public Response charset(String charset) {
            this.charset = charset;
            return this;
        }

        @Override
        public @Nullable String contentType() {
            return this.contentType;
        }

        private ControllableInputStream prepareParse() {
            Validate.isTrue(this.executed, "Request must be executed (with .execute(), .get(), or .post() before parsing response");
            ControllableInputStream stream = this.bodyStream;
            if (this.byteData != null) {
                ByteArrayInputStream bytes = new ByteArrayInputStream(this.byteData.array(), 0, this.byteData.limit());
                stream = ControllableInputStream.wrap(bytes, 0);
                this.inputStreamRead = false;
            }
            Validate.isFalse(this.inputStreamRead, "Input stream already read and parsed, cannot re-read.");
            Validate.notNull(stream);
            this.inputStreamRead = true;
            return stream;
        }

        @Override
        public Document parse() throws IOException {
            ControllableInputStream stream = this.prepareParse();
            Document doc = DataUtil.parseInputStream(stream, this.charset, this.url.toExternalForm(), this.req.parser());
            doc.connection(new HttpConnection(this.req, this));
            this.charset = doc.outputSettings().charset().name();
            this.safeClose();
            return doc;
        }

        @Override
        public StreamParser streamParser() throws IOException {
            ControllableInputStream stream = this.prepareParse();
            String baseUri = this.url.toExternalForm();
            DataUtil.CharsetDoc charsetDoc = DataUtil.detectCharset(stream, this.charset, baseUri, this.req.parser());
            StreamParser streamer = new StreamParser(this.req.parser());
            BufferedReader reader = new BufferedReader(new InputStreamReader((InputStream)stream, charsetDoc.charset));
            streamer.parse(reader, baseUri);
            streamer.document().connection(new HttpConnection(this.req, this));
            this.charset = charsetDoc.charset.name();
            return streamer;
        }

        private void prepareByteData() {
            Validate.isTrue(this.executed, "Request must be executed (with .execute(), .get(), or .post() before getting response body");
            if (this.bodyStream != null && this.byteData == null) {
                Validate.isFalse(this.inputStreamRead, "Request has already been read (with .parse())");
                try {
                    this.byteData = DataUtil.readToByteBuffer(this.bodyStream, this.req.maxBodySize());
                }
                catch (IOException e2) {
                    throw new UncheckedIOException(e2);
                }
                finally {
                    this.inputStreamRead = true;
                    this.safeClose();
                }
            }
        }

        @Override
        public String body() {
            this.prepareByteData();
            Validate.notNull(this.byteData);
            String body = (this.charset == null ? DataUtil.UTF_8 : Charset.forName(this.charset)).decode(this.byteData).toString();
            ((Buffer)this.byteData).rewind();
            return body;
        }

        @Override
        public byte[] bodyAsBytes() {
            this.prepareByteData();
            Validate.notNull(this.byteData);
            Validate.isTrue(this.byteData.hasArray());
            byte[] array = this.byteData.array();
            int offset = this.byteData.arrayOffset();
            int length = this.byteData.limit();
            if (offset == 0 && length == array.length) {
                return array;
            }
            byte[] exactArray = new byte[length];
            System.arraycopy(array, offset, exactArray, 0, length);
            return exactArray;
        }

        @Override
        public Connection.Response bufferUp() {
            this.prepareByteData();
            return this;
        }

        @Override
        public BufferedInputStream bodyStream() {
            Validate.isTrue(this.executed, "Request must be executed (with .execute(), .get(), or .post() before getting response body");
            if (this.byteData != null) {
                return new BufferedInputStream(new ByteArrayInputStream(this.byteData.array(), 0, this.byteData.limit()), 8192);
            }
            Validate.isFalse(this.inputStreamRead, "Request has already been read");
            Validate.notNull(this.bodyStream);
            this.inputStreamRead = true;
            return this.bodyStream.inputStream();
        }

        private void safeClose() {
            if (this.bodyStream != null) {
                try {
                    this.bodyStream.close();
                }
                catch (IOException iOException) {
                }
                finally {
                    this.bodyStream = null;
                }
            }
            if (this.executor != null) {
                this.executor.safeClose();
            }
        }

        Response(Request request) {
            this.req = request;
        }

        void prepareResponse(Map<String, List<String>> resHeaders, @Nullable Response previousResponse) throws IOException {
            this.processResponseHeaders(resHeaders);
            CookieUtil.storeCookies(this.req, this, this.url, resHeaders);
            if (previousResponse != null) {
                for (Map.Entry prevCookie : previousResponse.cookies().entrySet()) {
                    if (this.hasCookie((String)prevCookie.getKey())) continue;
                    this.cookie((String)prevCookie.getKey(), (String)prevCookie.getValue());
                }
                previousResponse.safeClose();
                this.numRedirects = previousResponse.numRedirects + 1;
                if (this.numRedirects >= 20) {
                    throw new IOException(String.format("Too many redirects occurred trying to load URL %s", previousResponse.url()));
                }
            }
        }

        void processResponseHeaders(Map<String, List<String>> resHeaders) {
            for (Map.Entry<String, List<String>> entry : resHeaders.entrySet()) {
                String name = entry.getKey();
                if (name == null) continue;
                List<String> values2 = entry.getValue();
                for (String value : values2) {
                    this.addHeader(name, Response.fixHeaderEncoding(value));
                }
            }
        }

        private static @Nullable String fixHeaderEncoding(@Nullable String val) {
            if (val == null) {
                return val;
            }
            byte[] bytes = val.getBytes(ISO_8859_1);
            if (Response.looksLikeUtf8(bytes)) {
                return new String(bytes, DataUtil.UTF_8);
            }
            return val;
        }

        private static boolean looksLikeUtf8(byte[] input) {
            int i2 = 0;
            if (input.length >= 3 && (input[0] & 0xFF) == 239 && (input[1] & 0xFF) == 187 && (input[2] & 0xFF) == 191) {
                i2 = 3;
            }
            boolean foundNonAscii = false;
            int j2 = input.length;
            while (i2 < j2) {
                byte o2 = input[i2];
                if ((o2 & 0x80) != 0) {
                    int end;
                    foundNonAscii = true;
                    if ((o2 & 0xE0) == 192) {
                        end = i2 + 1;
                    } else if ((o2 & 0xF0) == 224) {
                        end = i2 + 2;
                    } else if ((o2 & 0xF8) == 240) {
                        end = i2 + 3;
                    } else {
                        return false;
                    }
                    if (end >= input.length) {
                        return false;
                    }
                    while (i2 < end) {
                        if (((o2 = input[++i2]) & 0xC0) == 128) continue;
                        return false;
                    }
                }
                ++i2;
            }
            return foundNonAscii;
        }

        private static void setOutputContentType(Request req) {
            String contentType = req.header(HttpConnection.CONTENT_TYPE);
            String bound = null;
            if (contentType != null) {
                if (contentType.contains(HttpConnection.MULTIPART_FORM_DATA) && !contentType.contains("boundary")) {
                    bound = DataUtil.mimeBoundary();
                    req.header(HttpConnection.CONTENT_TYPE, "multipart/form-data; boundary=" + bound);
                }
            } else if (HttpConnection.needsMultipart(req)) {
                bound = DataUtil.mimeBoundary();
                req.header(HttpConnection.CONTENT_TYPE, "multipart/form-data; boundary=" + bound);
            } else {
                req.header(HttpConnection.CONTENT_TYPE, "application/x-www-form-urlencoded; charset=" + req.postDataCharset());
            }
            req.mimeBoundary = bound;
        }

        static void writePost(Request req, OutputStream outputStream2) throws IOException {
            Collection<Connection.KeyVal> data = req.data();
            BufferedWriter w2 = new BufferedWriter(new OutputStreamWriter(outputStream2, Charset.forName(req.postDataCharset())));
            String boundary = req.mimeBoundary;
            if (boundary != null) {
                for (Connection.KeyVal keyVal : data) {
                    w2.write("--");
                    w2.write(boundary);
                    w2.write("\r\n");
                    w2.write("Content-Disposition: form-data; name=\"");
                    w2.write(HttpConnection.encodeMimeName(keyVal.key()));
                    w2.write("\"");
                    InputStream input = keyVal.inputStream();
                    if (input != null) {
                        w2.write("; filename=\"");
                        w2.write(HttpConnection.encodeMimeName(keyVal.value()));
                        w2.write("\"\r\nContent-Type: ");
                        String contentType = keyVal.contentType();
                        w2.write(contentType != null ? contentType : HttpConnection.DefaultUploadType);
                        w2.write("\r\n\r\n");
                        w2.flush();
                        DataUtil.crossStreams(input, outputStream2);
                        outputStream2.flush();
                    } else {
                        w2.write("\r\n\r\n");
                        w2.write(keyVal.value());
                    }
                    w2.write("\r\n");
                }
                w2.write("--");
                w2.write(boundary);
                w2.write("--");
            } else {
                String body = req.requestBody();
                if (body != null) {
                    w2.write(body);
                } else {
                    boolean first = true;
                    for (Connection.KeyVal keyVal : data) {
                        if (!first) {
                            w2.append('&');
                        } else {
                            first = false;
                        }
                        w2.write(URLEncoder.encode(keyVal.key(), req.postDataCharset()));
                        w2.write(61);
                        w2.write(URLEncoder.encode(keyVal.value(), req.postDataCharset()));
                    }
                }
            }
            w2.close();
        }

        private static void serialiseRequestUrl(Connection.Request req) throws IOException {
            UrlBuilder in = new UrlBuilder(req.url());
            for (Connection.KeyVal keyVal : req.data()) {
                Validate.isFalse(keyVal.hasInputStream(), "InputStream data not supported in URL query string.");
                in.appendKeyVal(keyVal);
            }
            req.url(in.build());
            req.data().clear();
        }
    }

    public static class KeyVal
    implements Connection.KeyVal {
        private String key;
        private String value;
        private @Nullable InputStream stream;
        private @Nullable String contentType;

        public static KeyVal create(String key, String value) {
            return new KeyVal(key, value);
        }

        public static KeyVal create(String key, String filename, InputStream stream) {
            return new KeyVal(key, filename).inputStream(stream);
        }

        private KeyVal(String key, String value) {
            Validate.notEmptyParam(key, "key");
            Validate.notNullParam(value, "value");
            this.key = key;
            this.value = value;
        }

        @Override
        public KeyVal key(String key) {
            Validate.notEmptyParam(key, "key");
            this.key = key;
            return this;
        }

        @Override
        public String key() {
            return this.key;
        }

        @Override
        public KeyVal value(String value) {
            Validate.notNullParam(value, "value");
            this.value = value;
            return this;
        }

        @Override
        public String value() {
            return this.value;
        }

        @Override
        public KeyVal inputStream(InputStream inputStream2) {
            Validate.notNullParam(this.value, "inputStream");
            this.stream = inputStream2;
            return this;
        }

        @Override
        public @Nullable InputStream inputStream() {
            return this.stream;
        }

        @Override
        public boolean hasInputStream() {
            return this.stream != null;
        }

        @Override
        public Connection.KeyVal contentType(String contentType) {
            Validate.notEmpty(contentType);
            this.contentType = contentType;
            return this;
        }

        @Override
        public @Nullable String contentType() {
            return this.contentType;
        }

        public String toString() {
            return this.key + "=" + this.value;
        }
    }

    private static abstract class Base<T extends Connection.Base<T>>
    implements Connection.Base<T> {
        private static final URL UnsetUrl;
        URL url = UnsetUrl;
        Connection.Method method = Connection.Method.GET;
        Map<String, List<String>> headers;
        Map<String, String> cookies;

        private Base() {
            this.headers = new LinkedHashMap<String, List<String>>();
            this.cookies = new LinkedHashMap<String, String>();
        }

        private Base(Base<T> copy) {
            this.url = copy.url;
            this.method = copy.method;
            this.headers = new LinkedHashMap<String, List<String>>();
            for (Map.Entry<String, List<String>> entry : copy.headers.entrySet()) {
                this.headers.put(entry.getKey(), new ArrayList(entry.getValue()));
            }
            this.cookies = new LinkedHashMap<String, String>();
            this.cookies.putAll(copy.cookies);
        }

        @Override
        public URL url() {
            if (this.url == UnsetUrl) {
                throw new IllegalArgumentException("URL not set. Make sure to call #url(...) before executing the request.");
            }
            return this.url;
        }

        @Override
        public T url(URL url) {
            Validate.notNullParam(url, "url");
            this.url = new UrlBuilder(url).build();
            return (T)this;
        }

        @Override
        public Connection.Method method() {
            return this.method;
        }

        @Override
        public T method(Connection.Method method) {
            Validate.notNullParam((Object)method, "method");
            this.method = method;
            return (T)this;
        }

        @Override
        public @Nullable String header(String name) {
            Validate.notNullParam(name, "name");
            List<String> vals = this.getHeadersCaseInsensitive(name);
            if (!vals.isEmpty()) {
                return StringUtil.join(vals, ", ");
            }
            return null;
        }

        @Override
        public T addHeader(String name, @Nullable String value) {
            Validate.notEmptyParam(name, "name");
            value = value == null ? "" : value;
            List<String> values2 = this.headers(name);
            if (values2.isEmpty()) {
                values2 = new ArrayList<String>();
                this.headers.put(name, values2);
            }
            values2.add(value);
            return (T)this;
        }

        @Override
        public List<String> headers(String name) {
            Validate.notEmptyParam(name, "name");
            return this.getHeadersCaseInsensitive(name);
        }

        @Override
        public T header(String name, String value) {
            Validate.notEmptyParam(name, "name");
            this.removeHeader(name);
            this.addHeader(name, value);
            return (T)this;
        }

        @Override
        public boolean hasHeader(String name) {
            Validate.notEmptyParam(name, "name");
            return !this.getHeadersCaseInsensitive(name).isEmpty();
        }

        @Override
        public boolean hasHeaderWithValue(String name, String value) {
            Validate.notEmpty(name);
            Validate.notEmpty(value);
            List<String> values2 = this.headers(name);
            for (String candidate : values2) {
                if (!value.equalsIgnoreCase(candidate)) continue;
                return true;
            }
            return false;
        }

        @Override
        public T removeHeader(String name) {
            Validate.notEmptyParam(name, "name");
            Map.Entry<String, List<String>> entry = this.scanHeaders(name);
            if (entry != null) {
                this.headers.remove(entry.getKey());
            }
            return (T)this;
        }

        @Override
        public Map<String, String> headers() {
            LinkedHashMap<String, String> map = new LinkedHashMap<String, String>(this.headers.size());
            for (Map.Entry<String, List<String>> entry : this.headers.entrySet()) {
                String header = entry.getKey();
                List<String> values2 = entry.getValue();
                if (values2.isEmpty()) continue;
                map.put(header, values2.get(0));
            }
            return map;
        }

        @Override
        public Map<String, List<String>> multiHeaders() {
            return this.headers;
        }

        private List<String> getHeadersCaseInsensitive(String name) {
            Validate.notNull(name);
            for (Map.Entry<String, List<String>> entry : this.headers.entrySet()) {
                if (!name.equalsIgnoreCase(entry.getKey())) continue;
                return entry.getValue();
            }
            return Collections.emptyList();
        }

        private @Nullable Map.Entry<String, List<String>> scanHeaders(String name) {
            String lc = Normalizer.lowerCase(name);
            for (Map.Entry<String, List<String>> entry : this.headers.entrySet()) {
                if (!Normalizer.lowerCase(entry.getKey()).equals(lc)) continue;
                return entry;
            }
            return null;
        }

        @Override
        public String cookie(String name) {
            Validate.notEmptyParam(name, "name");
            return this.cookies.get(name);
        }

        @Override
        public T cookie(String name, String value) {
            Validate.notEmptyParam(name, "name");
            Validate.notNullParam(value, "value");
            this.cookies.put(name, value);
            return (T)this;
        }

        @Override
        public boolean hasCookie(String name) {
            Validate.notEmptyParam(name, "name");
            return this.cookies.containsKey(name);
        }

        @Override
        public T removeCookie(String name) {
            Validate.notEmptyParam(name, "name");
            this.cookies.remove(name);
            return (T)this;
        }

        @Override
        public Map<String, String> cookies() {
            return this.cookies;
        }

        static {
            try {
                UnsetUrl = new URL("http://undefined/");
            }
            catch (MalformedURLException e2) {
                throw new IllegalStateException(e2);
            }
        }
    }
}

