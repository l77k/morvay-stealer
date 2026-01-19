/*
 * Decompiled with CFR 0.152.
 */
package com.github.kevinsawicki.http;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.security.AccessController;
import java.security.GeneralSecurityException;
import java.security.PrivilegedAction;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.zip.GZIPInputStream;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class HttpRequest {
    public static final String CHARSET_UTF8 = "UTF-8";
    public static final String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded";
    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final String ENCODING_GZIP = "gzip";
    public static final String HEADER_ACCEPT = "Accept";
    public static final String HEADER_ACCEPT_CHARSET = "Accept-Charset";
    public static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String HEADER_CACHE_CONTROL = "Cache-Control";
    public static final String HEADER_CONTENT_ENCODING = "Content-Encoding";
    public static final String HEADER_CONTENT_LENGTH = "Content-Length";
    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String HEADER_DATE = "Date";
    public static final String HEADER_ETAG = "ETag";
    public static final String HEADER_EXPIRES = "Expires";
    public static final String HEADER_IF_NONE_MATCH = "If-None-Match";
    public static final String HEADER_LAST_MODIFIED = "Last-Modified";
    public static final String HEADER_LOCATION = "Location";
    public static final String HEADER_PROXY_AUTHORIZATION = "Proxy-Authorization";
    public static final String HEADER_REFERER = "Referer";
    public static final String HEADER_SERVER = "Server";
    public static final String HEADER_USER_AGENT = "User-Agent";
    public static final String METHOD_DELETE = "DELETE";
    public static final String METHOD_GET = "GET";
    public static final String METHOD_HEAD = "HEAD";
    public static final String METHOD_OPTIONS = "OPTIONS";
    public static final String METHOD_POST = "POST";
    public static final String METHOD_PUT = "PUT";
    public static final String METHOD_TRACE = "TRACE";
    public static final String PARAM_CHARSET = "charset";
    private static final String BOUNDARY = "00content0boundary00";
    private static final String CONTENT_TYPE_MULTIPART = "multipart/form-data; boundary=00content0boundary00";
    private static final String CRLF = "\r\n";
    private static final String[] EMPTY_STRINGS = new String[0];
    private static SSLSocketFactory TRUSTED_FACTORY;
    private static HostnameVerifier TRUSTED_VERIFIER;
    private static ConnectionFactory CONNECTION_FACTORY;
    private HttpURLConnection connection = null;
    private final URL url;
    private final String requestMethod;
    private RequestOutputStream output;
    private boolean multipart;
    private boolean form;
    private boolean ignoreCloseExceptions = true;
    private boolean uncompress = false;
    private int bufferSize = 8192;
    private long totalSize = -1L;
    private long totalWritten = 0L;
    private String httpProxyHost;
    private int httpProxyPort;
    private UploadProgress progress = UploadProgress.DEFAULT;

    private static String getValidCharset(String charset) {
        if (charset != null && charset.length() > 0) {
            return charset;
        }
        return CHARSET_UTF8;
    }

    private static SSLSocketFactory getTrustedFactory() throws HttpRequestException {
        if (TRUSTED_FACTORY == null) {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager(){

                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                public void checkClientTrusted(X509Certificate[] chain, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) {
                }
            }};
            try {
                SSLContext context = SSLContext.getInstance("TLS");
                context.init(null, trustAllCerts, new SecureRandom());
                TRUSTED_FACTORY = context.getSocketFactory();
            }
            catch (GeneralSecurityException e2) {
                IOException ioException = new IOException("Security exception configuring SSL context");
                ioException.initCause(e2);
                throw new HttpRequestException(ioException);
            }
        }
        return TRUSTED_FACTORY;
    }

    private static HostnameVerifier getTrustedVerifier() {
        if (TRUSTED_VERIFIER == null) {
            TRUSTED_VERIFIER = new HostnameVerifier(){

                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
        }
        return TRUSTED_VERIFIER;
    }

    private static StringBuilder addPathSeparator(String baseUrl, StringBuilder result) {
        if (baseUrl.indexOf(58) + 2 == baseUrl.lastIndexOf(47)) {
            result.append('/');
        }
        return result;
    }

    private static StringBuilder addParamPrefix(String baseUrl, StringBuilder result) {
        int queryStart = baseUrl.indexOf(63);
        int lastChar = result.length() - 1;
        if (queryStart == -1) {
            result.append('?');
        } else if (queryStart < lastChar && baseUrl.charAt(lastChar) != '&') {
            result.append('&');
        }
        return result;
    }

    private static StringBuilder addParam(Object key, Object value, StringBuilder result) {
        if (value != null && value.getClass().isArray()) {
            value = HttpRequest.arrayToList(value);
        }
        if (value instanceof Iterable) {
            Iterator iterator2 = ((Iterable)value).iterator();
            while (iterator2.hasNext()) {
                result.append(key);
                result.append("[]=");
                Object element = iterator2.next();
                if (element != null) {
                    result.append(element);
                }
                if (!iterator2.hasNext()) continue;
                result.append("&");
            }
        } else {
            result.append(key);
            result.append("=");
            if (value != null) {
                result.append(value);
            }
        }
        return result;
    }

    public static void setConnectionFactory(ConnectionFactory connectionFactory) {
        CONNECTION_FACTORY = connectionFactory == null ? ConnectionFactory.DEFAULT : connectionFactory;
    }

    private static List<Object> arrayToList(Object array) {
        ArrayList<Object> result;
        block10: {
            block16: {
                block15: {
                    block14: {
                        block13: {
                            block12: {
                                block11: {
                                    block9: {
                                        if (array instanceof Object[]) {
                                            return Arrays.asList((Object[])array);
                                        }
                                        result = new ArrayList<Object>();
                                        if (!(array instanceof int[])) break block9;
                                        for (int value : (int[])array) {
                                            result.add(value);
                                        }
                                        break block10;
                                    }
                                    if (!(array instanceof boolean[])) break block11;
                                    for (boolean value : (boolean[])array) {
                                        result.add(value);
                                    }
                                    break block10;
                                }
                                if (!(array instanceof long[])) break block12;
                                for (long value : (long[])array) {
                                    result.add(value);
                                }
                                break block10;
                            }
                            if (!(array instanceof float[])) break block13;
                            for (float value : (float[])array) {
                                result.add(Float.valueOf(value));
                            }
                            break block10;
                        }
                        if (!(array instanceof double[])) break block14;
                        for (double value : (double[])array) {
                            result.add(value);
                        }
                        break block10;
                    }
                    if (!(array instanceof short[])) break block15;
                    for (short value : (short[])array) {
                        result.add(value);
                    }
                    break block10;
                }
                if (!(array instanceof byte[])) break block16;
                for (byte value : (byte[])array) {
                    result.add(value);
                }
                break block10;
            }
            if (!(array instanceof char[])) break block10;
            for (char value : (char[])array) {
                result.add(Character.valueOf(value));
            }
        }
        return result;
    }

    public static String encode(CharSequence url) throws HttpRequestException {
        URL parsed;
        try {
            parsed = new URL(url.toString());
        }
        catch (IOException e2) {
            throw new HttpRequestException(e2);
        }
        String host = parsed.getHost();
        int port = parsed.getPort();
        if (port != -1) {
            host = host + ':' + Integer.toString(port);
        }
        try {
            String encoded = new URI(parsed.getProtocol(), host, parsed.getPath(), parsed.getQuery(), null).toASCIIString();
            int paramsStart = encoded.indexOf(63);
            if (paramsStart > 0 && paramsStart + 1 < encoded.length()) {
                encoded = encoded.substring(0, paramsStart + 1) + encoded.substring(paramsStart + 1).replace("+", "%2B");
            }
            return encoded;
        }
        catch (URISyntaxException e3) {
            IOException io = new IOException("Parsing URI failed");
            io.initCause(e3);
            throw new HttpRequestException(io);
        }
    }

    public static String append(CharSequence url, Map<?, ?> params) {
        String baseUrl = url.toString();
        if (params == null || params.isEmpty()) {
            return baseUrl;
        }
        StringBuilder result = new StringBuilder(baseUrl);
        HttpRequest.addPathSeparator(baseUrl, result);
        HttpRequest.addParamPrefix(baseUrl, result);
        Iterator<Map.Entry<?, ?>> iterator2 = params.entrySet().iterator();
        Map.Entry<?, ?> entry = iterator2.next();
        HttpRequest.addParam(entry.getKey().toString(), entry.getValue(), result);
        while (iterator2.hasNext()) {
            result.append('&');
            entry = iterator2.next();
            HttpRequest.addParam(entry.getKey().toString(), entry.getValue(), result);
        }
        return result.toString();
    }

    public static String append(CharSequence url, Object ... params) {
        String baseUrl = url.toString();
        if (params == null || params.length == 0) {
            return baseUrl;
        }
        if (params.length % 2 != 0) {
            throw new IllegalArgumentException("Must specify an even number of parameter names/values");
        }
        StringBuilder result = new StringBuilder(baseUrl);
        HttpRequest.addPathSeparator(baseUrl, result);
        HttpRequest.addParamPrefix(baseUrl, result);
        HttpRequest.addParam(params[0], params[1], result);
        for (int i2 = 2; i2 < params.length; i2 += 2) {
            result.append('&');
            HttpRequest.addParam(params[i2], params[i2 + 1], result);
        }
        return result.toString();
    }

    public static HttpRequest get(CharSequence url) throws HttpRequestException {
        return new HttpRequest(url, METHOD_GET);
    }

    public static HttpRequest get(URL url) throws HttpRequestException {
        return new HttpRequest(url, METHOD_GET);
    }

    public static HttpRequest get(CharSequence baseUrl, Map<?, ?> params, boolean encode) {
        String url = HttpRequest.append(baseUrl, params);
        return HttpRequest.get(encode ? HttpRequest.encode(url) : url);
    }

    public static HttpRequest get(CharSequence baseUrl, boolean encode, Object ... params) {
        String url = HttpRequest.append(baseUrl, params);
        return HttpRequest.get(encode ? HttpRequest.encode(url) : url);
    }

    public static HttpRequest post(CharSequence url) throws HttpRequestException {
        return new HttpRequest(url, METHOD_POST);
    }

    public static HttpRequest post(URL url) throws HttpRequestException {
        return new HttpRequest(url, METHOD_POST);
    }

    public static HttpRequest post(CharSequence baseUrl, Map<?, ?> params, boolean encode) {
        String url = HttpRequest.append(baseUrl, params);
        return HttpRequest.post(encode ? HttpRequest.encode(url) : url);
    }

    public static HttpRequest post(CharSequence baseUrl, boolean encode, Object ... params) {
        String url = HttpRequest.append(baseUrl, params);
        return HttpRequest.post(encode ? HttpRequest.encode(url) : url);
    }

    public static HttpRequest put(CharSequence url) throws HttpRequestException {
        return new HttpRequest(url, METHOD_PUT);
    }

    public static HttpRequest put(URL url) throws HttpRequestException {
        return new HttpRequest(url, METHOD_PUT);
    }

    public static HttpRequest put(CharSequence baseUrl, Map<?, ?> params, boolean encode) {
        String url = HttpRequest.append(baseUrl, params);
        return HttpRequest.put(encode ? HttpRequest.encode(url) : url);
    }

    public static HttpRequest put(CharSequence baseUrl, boolean encode, Object ... params) {
        String url = HttpRequest.append(baseUrl, params);
        return HttpRequest.put(encode ? HttpRequest.encode(url) : url);
    }

    public static HttpRequest delete(CharSequence url) throws HttpRequestException {
        return new HttpRequest(url, METHOD_DELETE);
    }

    public static HttpRequest delete(URL url) throws HttpRequestException {
        return new HttpRequest(url, METHOD_DELETE);
    }

    public static HttpRequest delete(CharSequence baseUrl, Map<?, ?> params, boolean encode) {
        String url = HttpRequest.append(baseUrl, params);
        return HttpRequest.delete(encode ? HttpRequest.encode(url) : url);
    }

    public static HttpRequest delete(CharSequence baseUrl, boolean encode, Object ... params) {
        String url = HttpRequest.append(baseUrl, params);
        return HttpRequest.delete(encode ? HttpRequest.encode(url) : url);
    }

    public static HttpRequest head(CharSequence url) throws HttpRequestException {
        return new HttpRequest(url, METHOD_HEAD);
    }

    public static HttpRequest head(URL url) throws HttpRequestException {
        return new HttpRequest(url, METHOD_HEAD);
    }

    public static HttpRequest head(CharSequence baseUrl, Map<?, ?> params, boolean encode) {
        String url = HttpRequest.append(baseUrl, params);
        return HttpRequest.head(encode ? HttpRequest.encode(url) : url);
    }

    public static HttpRequest head(CharSequence baseUrl, boolean encode, Object ... params) {
        String url = HttpRequest.append(baseUrl, params);
        return HttpRequest.head(encode ? HttpRequest.encode(url) : url);
    }

    public static HttpRequest options(CharSequence url) throws HttpRequestException {
        return new HttpRequest(url, METHOD_OPTIONS);
    }

    public static HttpRequest options(URL url) throws HttpRequestException {
        return new HttpRequest(url, METHOD_OPTIONS);
    }

    public static HttpRequest trace(CharSequence url) throws HttpRequestException {
        return new HttpRequest(url, METHOD_TRACE);
    }

    public static HttpRequest trace(URL url) throws HttpRequestException {
        return new HttpRequest(url, METHOD_TRACE);
    }

    public static void keepAlive(boolean keepAlive) {
        HttpRequest.setProperty("http.keepAlive", Boolean.toString(keepAlive));
    }

    public static void maxConnections(int maxConnections) {
        HttpRequest.setProperty("http.maxConnections", Integer.toString(maxConnections));
    }

    public static void proxyHost(String host) {
        HttpRequest.setProperty("http.proxyHost", host);
        HttpRequest.setProperty("https.proxyHost", host);
    }

    public static void proxyPort(int port) {
        String portValue = Integer.toString(port);
        HttpRequest.setProperty("http.proxyPort", portValue);
        HttpRequest.setProperty("https.proxyPort", portValue);
    }

    public static void nonProxyHosts(String ... hosts) {
        if (hosts != null && hosts.length > 0) {
            StringBuilder separated = new StringBuilder();
            int last = hosts.length - 1;
            for (int i2 = 0; i2 < last; ++i2) {
                separated.append(hosts[i2]).append('|');
            }
            separated.append(hosts[last]);
            HttpRequest.setProperty("http.nonProxyHosts", separated.toString());
        } else {
            HttpRequest.setProperty("http.nonProxyHosts", null);
        }
    }

    private static String setProperty(final String name, final String value) {
        PrivilegedAction<String> action = value != null ? new PrivilegedAction<String>(){

            @Override
            public String run() {
                return System.setProperty(name, value);
            }
        } : new PrivilegedAction<String>(){

            @Override
            public String run() {
                return System.clearProperty(name);
            }
        };
        return AccessController.doPrivileged(action);
    }

    public HttpRequest(CharSequence url, String method) throws HttpRequestException {
        try {
            this.url = new URL(url.toString());
        }
        catch (MalformedURLException e2) {
            throw new HttpRequestException(e2);
        }
        this.requestMethod = method;
    }

    public HttpRequest(URL url, String method) throws HttpRequestException {
        this.url = url;
        this.requestMethod = method;
    }

    private Proxy createProxy() {
        return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(this.httpProxyHost, this.httpProxyPort));
    }

    private HttpURLConnection createConnection() {
        try {
            HttpURLConnection connection = this.httpProxyHost != null ? CONNECTION_FACTORY.create(this.url, this.createProxy()) : CONNECTION_FACTORY.create(this.url);
            connection.setRequestMethod(this.requestMethod);
            return connection;
        }
        catch (IOException e2) {
            throw new HttpRequestException(e2);
        }
    }

    public String toString() {
        return this.method() + ' ' + this.url();
    }

    public HttpURLConnection getConnection() {
        if (this.connection == null) {
            this.connection = this.createConnection();
        }
        return this.connection;
    }

    public HttpRequest ignoreCloseExceptions(boolean ignore) {
        this.ignoreCloseExceptions = ignore;
        return this;
    }

    public boolean ignoreCloseExceptions() {
        return this.ignoreCloseExceptions;
    }

    public int code() throws HttpRequestException {
        try {
            this.closeOutput();
            return this.getConnection().getResponseCode();
        }
        catch (IOException e2) {
            throw new HttpRequestException(e2);
        }
    }

    public HttpRequest code(AtomicInteger output) throws HttpRequestException {
        output.set(this.code());
        return this;
    }

    public boolean ok() throws HttpRequestException {
        return 200 == this.code();
    }

    public boolean created() throws HttpRequestException {
        return 201 == this.code();
    }

    public boolean noContent() throws HttpRequestException {
        return 204 == this.code();
    }

    public boolean serverError() throws HttpRequestException {
        return 500 == this.code();
    }

    public boolean badRequest() throws HttpRequestException {
        return 400 == this.code();
    }

    public boolean notFound() throws HttpRequestException {
        return 404 == this.code();
    }

    public boolean notModified() throws HttpRequestException {
        return 304 == this.code();
    }

    public String message() throws HttpRequestException {
        try {
            this.closeOutput();
            return this.getConnection().getResponseMessage();
        }
        catch (IOException e2) {
            throw new HttpRequestException(e2);
        }
    }

    public HttpRequest disconnect() {
        this.getConnection().disconnect();
        return this;
    }

    public HttpRequest chunk(int size) {
        this.getConnection().setChunkedStreamingMode(size);
        return this;
    }

    public HttpRequest bufferSize(int size) {
        if (size < 1) {
            throw new IllegalArgumentException("Size must be greater than zero");
        }
        this.bufferSize = size;
        return this;
    }

    public int bufferSize() {
        return this.bufferSize;
    }

    public HttpRequest uncompress(boolean uncompress) {
        this.uncompress = uncompress;
        return this;
    }

    protected ByteArrayOutputStream byteStream() {
        int size = this.contentLength();
        if (size > 0) {
            return new ByteArrayOutputStream(size);
        }
        return new ByteArrayOutputStream();
    }

    public String body(String charset) throws HttpRequestException {
        ByteArrayOutputStream output = this.byteStream();
        try {
            this.copy(this.buffer(), output);
            return output.toString(HttpRequest.getValidCharset(charset));
        }
        catch (IOException e2) {
            throw new HttpRequestException(e2);
        }
    }

    public String body() throws HttpRequestException {
        return this.body(this.charset());
    }

    public HttpRequest body(AtomicReference<String> output) throws HttpRequestException {
        output.set(this.body());
        return this;
    }

    public HttpRequest body(AtomicReference<String> output, String charset) throws HttpRequestException {
        output.set(this.body(charset));
        return this;
    }

    public boolean isBodyEmpty() throws HttpRequestException {
        return this.contentLength() == 0;
    }

    public byte[] bytes() throws HttpRequestException {
        ByteArrayOutputStream output = this.byteStream();
        try {
            this.copy(this.buffer(), output);
        }
        catch (IOException e2) {
            throw new HttpRequestException(e2);
        }
        return output.toByteArray();
    }

    public BufferedInputStream buffer() throws HttpRequestException {
        return new BufferedInputStream(this.stream(), this.bufferSize);
    }

    public InputStream stream() throws HttpRequestException {
        InputStream stream;
        if (this.code() < 400) {
            try {
                stream = this.getConnection().getInputStream();
            }
            catch (IOException e2) {
                throw new HttpRequestException(e2);
            }
        }
        stream = this.getConnection().getErrorStream();
        if (stream == null) {
            try {
                stream = this.getConnection().getInputStream();
            }
            catch (IOException e3) {
                if (this.contentLength() > 0) {
                    throw new HttpRequestException(e3);
                }
                stream = new ByteArrayInputStream(new byte[0]);
            }
        }
        if (!this.uncompress || !ENCODING_GZIP.equals(this.contentEncoding())) {
            return stream;
        }
        try {
            return new GZIPInputStream(stream);
        }
        catch (IOException e4) {
            throw new HttpRequestException(e4);
        }
    }

    public InputStreamReader reader(String charset) throws HttpRequestException {
        try {
            return new InputStreamReader(this.stream(), HttpRequest.getValidCharset(charset));
        }
        catch (UnsupportedEncodingException e2) {
            throw new HttpRequestException(e2);
        }
    }

    public InputStreamReader reader() throws HttpRequestException {
        return this.reader(this.charset());
    }

    public BufferedReader bufferedReader(String charset) throws HttpRequestException {
        return new BufferedReader(this.reader(charset), this.bufferSize);
    }

    public BufferedReader bufferedReader() throws HttpRequestException {
        return this.bufferedReader(this.charset());
    }

    public HttpRequest receive(File file) throws HttpRequestException {
        BufferedOutputStream output;
        try {
            output = new BufferedOutputStream(new FileOutputStream(file), this.bufferSize);
        }
        catch (FileNotFoundException e2) {
            throw new HttpRequestException(e2);
        }
        return (HttpRequest)new CloseOperation<HttpRequest>((Closeable)output, this.ignoreCloseExceptions){

            @Override
            protected HttpRequest run() throws HttpRequestException, IOException {
                return HttpRequest.this.receive(output);
            }
        }.call();
    }

    public HttpRequest receive(OutputStream output) throws HttpRequestException {
        try {
            return this.copy(this.buffer(), output);
        }
        catch (IOException e2) {
            throw new HttpRequestException(e2);
        }
    }

    public HttpRequest receive(PrintStream output) throws HttpRequestException {
        return this.receive((OutputStream)output);
    }

    public HttpRequest receive(final Appendable appendable) throws HttpRequestException {
        final BufferedReader reader = this.bufferedReader();
        return (HttpRequest)new CloseOperation<HttpRequest>((Closeable)reader, this.ignoreCloseExceptions){

            @Override
            public HttpRequest run() throws IOException {
                int read;
                CharBuffer buffer = CharBuffer.allocate(HttpRequest.this.bufferSize);
                while ((read = reader.read(buffer)) != -1) {
                    buffer.rewind();
                    appendable.append(buffer, 0, read);
                    buffer.rewind();
                }
                return HttpRequest.this;
            }
        }.call();
    }

    public HttpRequest receive(final Writer writer) throws HttpRequestException {
        final BufferedReader reader = this.bufferedReader();
        return (HttpRequest)new CloseOperation<HttpRequest>((Closeable)reader, this.ignoreCloseExceptions){

            @Override
            public HttpRequest run() throws IOException {
                return HttpRequest.this.copy(reader, writer);
            }
        }.call();
    }

    public HttpRequest readTimeout(int timeout2) {
        this.getConnection().setReadTimeout(timeout2);
        return this;
    }

    public HttpRequest connectTimeout(int timeout2) {
        this.getConnection().setConnectTimeout(timeout2);
        return this;
    }

    public HttpRequest header(String name, String value) {
        this.getConnection().setRequestProperty(name, value);
        return this;
    }

    public HttpRequest header(String name, Number value) {
        return this.header(name, value != null ? value.toString() : null);
    }

    public HttpRequest headers(Map<String, String> headers) {
        if (!headers.isEmpty()) {
            for (Map.Entry<String, String> header : headers.entrySet()) {
                this.header(header);
            }
        }
        return this;
    }

    public HttpRequest header(Map.Entry<String, String> header) {
        return this.header(header.getKey(), header.getValue());
    }

    public String header(String name) throws HttpRequestException {
        this.closeOutputQuietly();
        return this.getConnection().getHeaderField(name);
    }

    public Map<String, List<String>> headers() throws HttpRequestException {
        this.closeOutputQuietly();
        return this.getConnection().getHeaderFields();
    }

    public long dateHeader(String name) throws HttpRequestException {
        return this.dateHeader(name, -1L);
    }

    public long dateHeader(String name, long defaultValue) throws HttpRequestException {
        this.closeOutputQuietly();
        return this.getConnection().getHeaderFieldDate(name, defaultValue);
    }

    public int intHeader(String name) throws HttpRequestException {
        return this.intHeader(name, -1);
    }

    public int intHeader(String name, int defaultValue) throws HttpRequestException {
        this.closeOutputQuietly();
        return this.getConnection().getHeaderFieldInt(name, defaultValue);
    }

    public String[] headers(String name) {
        Map<String, List<String>> headers = this.headers();
        if (headers == null || headers.isEmpty()) {
            return EMPTY_STRINGS;
        }
        List<String> values2 = headers.get(name);
        if (values2 != null && !values2.isEmpty()) {
            return values2.toArray(new String[values2.size()]);
        }
        return EMPTY_STRINGS;
    }

    public String parameter(String headerName, String paramName) {
        return this.getParam(this.header(headerName), paramName);
    }

    public Map<String, String> parameters(String headerName) {
        return this.getParams(this.header(headerName));
    }

    protected Map<String, String> getParams(String header) {
        if (header == null || header.length() == 0) {
            return Collections.emptyMap();
        }
        int headerLength = header.length();
        int start = header.indexOf(59) + 1;
        if (start == 0 || start == headerLength) {
            return Collections.emptyMap();
        }
        int end = header.indexOf(59, start);
        if (end == -1) {
            end = headerLength;
        }
        LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
        while (start < end) {
            String value;
            int length;
            String name;
            int nameEnd = header.indexOf(61, start);
            if (nameEnd != -1 && nameEnd < end && (name = header.substring(start, nameEnd).trim()).length() > 0 && (length = (value = header.substring(nameEnd + 1, end).trim()).length()) != 0) {
                if (length > 2 && '\"' == value.charAt(0) && '\"' == value.charAt(length - 1)) {
                    params.put(name, value.substring(1, length - 1));
                } else {
                    params.put(name, value);
                }
            }
            if ((end = header.indexOf(59, start = end + 1)) != -1) continue;
            end = headerLength;
        }
        return params;
    }

    protected String getParam(String value, String paramName) {
        if (value == null || value.length() == 0) {
            return null;
        }
        int length = value.length();
        int start = value.indexOf(59) + 1;
        if (start == 0 || start == length) {
            return null;
        }
        int end = value.indexOf(59, start);
        if (end == -1) {
            end = length;
        }
        while (start < end) {
            String paramValue;
            int valueLength;
            int nameEnd = value.indexOf(61, start);
            if (nameEnd != -1 && nameEnd < end && paramName.equals(value.substring(start, nameEnd).trim()) && (valueLength = (paramValue = value.substring(nameEnd + 1, end).trim()).length()) != 0) {
                if (valueLength > 2 && '\"' == paramValue.charAt(0) && '\"' == paramValue.charAt(valueLength - 1)) {
                    return paramValue.substring(1, valueLength - 1);
                }
                return paramValue;
            }
            start = end + 1;
            if ((end = value.indexOf(59, start)) != -1) continue;
            end = length;
        }
        return null;
    }

    public String charset() {
        return this.parameter(HEADER_CONTENT_TYPE, PARAM_CHARSET);
    }

    public HttpRequest userAgent(String userAgent) {
        return this.header(HEADER_USER_AGENT, userAgent);
    }

    public HttpRequest referer(String referer) {
        return this.header(HEADER_REFERER, referer);
    }

    public HttpRequest useCaches(boolean useCaches) {
        this.getConnection().setUseCaches(useCaches);
        return this;
    }

    public HttpRequest acceptEncoding(String acceptEncoding) {
        return this.header(HEADER_ACCEPT_ENCODING, acceptEncoding);
    }

    public HttpRequest acceptGzipEncoding() {
        return this.acceptEncoding(ENCODING_GZIP);
    }

    public HttpRequest acceptCharset(String acceptCharset) {
        return this.header(HEADER_ACCEPT_CHARSET, acceptCharset);
    }

    public String contentEncoding() {
        return this.header(HEADER_CONTENT_ENCODING);
    }

    public String server() {
        return this.header(HEADER_SERVER);
    }

    public long date() {
        return this.dateHeader(HEADER_DATE);
    }

    public String cacheControl() {
        return this.header(HEADER_CACHE_CONTROL);
    }

    public String eTag() {
        return this.header(HEADER_ETAG);
    }

    public long expires() {
        return this.dateHeader(HEADER_EXPIRES);
    }

    public long lastModified() {
        return this.dateHeader(HEADER_LAST_MODIFIED);
    }

    public String location() {
        return this.header(HEADER_LOCATION);
    }

    public HttpRequest authorization(String authorization) {
        return this.header(HEADER_AUTHORIZATION, authorization);
    }

    public HttpRequest proxyAuthorization(String proxyAuthorization) {
        return this.header(HEADER_PROXY_AUTHORIZATION, proxyAuthorization);
    }

    public HttpRequest basic(String name, String password) {
        return this.authorization("Basic " + Base64.encode(name + ':' + password));
    }

    public HttpRequest proxyBasic(String name, String password) {
        return this.proxyAuthorization("Basic " + Base64.encode(name + ':' + password));
    }

    public HttpRequest ifModifiedSince(long ifModifiedSince) {
        this.getConnection().setIfModifiedSince(ifModifiedSince);
        return this;
    }

    public HttpRequest ifNoneMatch(String ifNoneMatch) {
        return this.header(HEADER_IF_NONE_MATCH, ifNoneMatch);
    }

    public HttpRequest contentType(String contentType) {
        return this.contentType(contentType, null);
    }

    public HttpRequest contentType(String contentType, String charset) {
        if (charset != null && charset.length() > 0) {
            String separator = "; charset=";
            return this.header(HEADER_CONTENT_TYPE, contentType + "; charset=" + charset);
        }
        return this.header(HEADER_CONTENT_TYPE, contentType);
    }

    public String contentType() {
        return this.header(HEADER_CONTENT_TYPE);
    }

    public int contentLength() {
        return this.intHeader(HEADER_CONTENT_LENGTH);
    }

    public HttpRequest contentLength(String contentLength) {
        return this.contentLength(Integer.parseInt(contentLength));
    }

    public HttpRequest contentLength(int contentLength) {
        this.getConnection().setFixedLengthStreamingMode(contentLength);
        return this;
    }

    public HttpRequest accept(String accept) {
        return this.header(HEADER_ACCEPT, accept);
    }

    public HttpRequest acceptJson() {
        return this.accept(CONTENT_TYPE_JSON);
    }

    protected HttpRequest copy(final InputStream input, final OutputStream output) throws IOException {
        return (HttpRequest)new CloseOperation<HttpRequest>((Closeable)input, this.ignoreCloseExceptions){

            @Override
            public HttpRequest run() throws IOException {
                int read;
                byte[] buffer = new byte[HttpRequest.this.bufferSize];
                while ((read = input.read(buffer)) != -1) {
                    output.write(buffer, 0, read);
                    HttpRequest.this.totalWritten = HttpRequest.this.totalWritten + (long)read;
                    HttpRequest.this.progress.onUpload(HttpRequest.this.totalWritten, HttpRequest.this.totalSize);
                }
                return HttpRequest.this;
            }
        }.call();
    }

    protected HttpRequest copy(final Reader input, final Writer output) throws IOException {
        return (HttpRequest)new CloseOperation<HttpRequest>((Closeable)input, this.ignoreCloseExceptions){

            @Override
            public HttpRequest run() throws IOException {
                int read;
                char[] buffer = new char[HttpRequest.this.bufferSize];
                while ((read = input.read(buffer)) != -1) {
                    output.write(buffer, 0, read);
                    HttpRequest.this.totalWritten = HttpRequest.this.totalWritten + (long)read;
                    HttpRequest.this.progress.onUpload(HttpRequest.this.totalWritten, -1L);
                }
                return HttpRequest.this;
            }
        }.call();
    }

    public HttpRequest progress(UploadProgress callback) {
        this.progress = callback == null ? UploadProgress.DEFAULT : callback;
        return this;
    }

    private HttpRequest incrementTotalSize(long size) {
        if (this.totalSize == -1L) {
            this.totalSize = 0L;
        }
        this.totalSize += size;
        return this;
    }

    protected HttpRequest closeOutput() throws IOException {
        this.progress(null);
        if (this.output == null) {
            return this;
        }
        if (this.multipart) {
            this.output.write("\r\n--00content0boundary00--\r\n");
        }
        if (this.ignoreCloseExceptions) {
            try {
                this.output.close();
            }
            catch (IOException iOException) {}
        } else {
            this.output.close();
        }
        this.output = null;
        return this;
    }

    protected HttpRequest closeOutputQuietly() throws HttpRequestException {
        try {
            return this.closeOutput();
        }
        catch (IOException e2) {
            throw new HttpRequestException(e2);
        }
    }

    protected HttpRequest openOutput() throws IOException {
        if (this.output != null) {
            return this;
        }
        this.getConnection().setDoOutput(true);
        String charset = this.getParam(this.getConnection().getRequestProperty(HEADER_CONTENT_TYPE), PARAM_CHARSET);
        this.output = new RequestOutputStream(this.getConnection().getOutputStream(), charset, this.bufferSize);
        return this;
    }

    protected HttpRequest startPart() throws IOException {
        if (!this.multipart) {
            this.multipart = true;
            this.contentType(CONTENT_TYPE_MULTIPART).openOutput();
            this.output.write("--00content0boundary00\r\n");
        } else {
            this.output.write("\r\n--00content0boundary00\r\n");
        }
        return this;
    }

    protected HttpRequest writePartHeader(String name, String filename) throws IOException {
        return this.writePartHeader(name, filename, null);
    }

    protected HttpRequest writePartHeader(String name, String filename, String contentType) throws IOException {
        StringBuilder partBuffer = new StringBuilder();
        partBuffer.append("form-data; name=\"").append(name);
        if (filename != null) {
            partBuffer.append("\"; filename=\"").append(filename);
        }
        partBuffer.append('\"');
        this.partHeader("Content-Disposition", partBuffer.toString());
        if (contentType != null) {
            this.partHeader(HEADER_CONTENT_TYPE, contentType);
        }
        return this.send(CRLF);
    }

    public HttpRequest part(String name, String part) {
        return this.part(name, null, part);
    }

    public HttpRequest part(String name, String filename, String part) throws HttpRequestException {
        return this.part(name, filename, null, part);
    }

    public HttpRequest part(String name, String filename, String contentType, String part) throws HttpRequestException {
        try {
            this.startPart();
            this.writePartHeader(name, filename, contentType);
            this.output.write(part);
        }
        catch (IOException e2) {
            throw new HttpRequestException(e2);
        }
        return this;
    }

    public HttpRequest part(String name, Number part) throws HttpRequestException {
        return this.part(name, null, part);
    }

    public HttpRequest part(String name, String filename, Number part) throws HttpRequestException {
        return this.part(name, filename, part != null ? part.toString() : null);
    }

    public HttpRequest part(String name, File part) throws HttpRequestException {
        return this.part(name, null, part);
    }

    public HttpRequest part(String name, String filename, File part) throws HttpRequestException {
        return this.part(name, filename, null, part);
    }

    public HttpRequest part(String name, String filename, String contentType, File part) throws HttpRequestException {
        BufferedInputStream stream;
        try {
            stream = new BufferedInputStream(new FileInputStream(part));
            this.incrementTotalSize(part.length());
        }
        catch (IOException e2) {
            throw new HttpRequestException(e2);
        }
        return this.part(name, filename, contentType, stream);
    }

    public HttpRequest part(String name, InputStream part) throws HttpRequestException {
        return this.part(name, null, null, part);
    }

    public HttpRequest part(String name, String filename, String contentType, InputStream part) throws HttpRequestException {
        try {
            this.startPart();
            this.writePartHeader(name, filename, contentType);
            this.copy(part, this.output);
        }
        catch (IOException e2) {
            throw new HttpRequestException(e2);
        }
        return this;
    }

    public HttpRequest partHeader(String name, String value) throws HttpRequestException {
        return this.send(name).send(": ").send(value).send(CRLF);
    }

    public HttpRequest send(File input) throws HttpRequestException {
        BufferedInputStream stream;
        try {
            stream = new BufferedInputStream(new FileInputStream(input));
            this.incrementTotalSize(input.length());
        }
        catch (FileNotFoundException e2) {
            throw new HttpRequestException(e2);
        }
        return this.send(stream);
    }

    public HttpRequest send(byte[] input) throws HttpRequestException {
        if (input != null) {
            this.incrementTotalSize(input.length);
        }
        return this.send(new ByteArrayInputStream(input));
    }

    public HttpRequest send(InputStream input) throws HttpRequestException {
        try {
            this.openOutput();
            this.copy(input, this.output);
        }
        catch (IOException e2) {
            throw new HttpRequestException(e2);
        }
        return this;
    }

    public HttpRequest send(final Reader input) throws HttpRequestException {
        try {
            this.openOutput();
        }
        catch (IOException e2) {
            throw new HttpRequestException(e2);
        }
        final OutputStreamWriter writer = new OutputStreamWriter((OutputStream)this.output, this.output.encoder.charset());
        return (HttpRequest)new FlushOperation<HttpRequest>((Flushable)writer){

            @Override
            protected HttpRequest run() throws IOException {
                return HttpRequest.this.copy(input, writer);
            }
        }.call();
    }

    public HttpRequest send(CharSequence value) throws HttpRequestException {
        try {
            this.openOutput();
            this.output.write(value.toString());
        }
        catch (IOException e2) {
            throw new HttpRequestException(e2);
        }
        return this;
    }

    public OutputStreamWriter writer() throws HttpRequestException {
        try {
            this.openOutput();
            return new OutputStreamWriter((OutputStream)this.output, this.output.encoder.charset());
        }
        catch (IOException e2) {
            throw new HttpRequestException(e2);
        }
    }

    public HttpRequest form(Map<?, ?> values2) throws HttpRequestException {
        return this.form(values2, CHARSET_UTF8);
    }

    public HttpRequest form(Map.Entry<?, ?> entry) throws HttpRequestException {
        return this.form(entry, CHARSET_UTF8);
    }

    public HttpRequest form(Map.Entry<?, ?> entry, String charset) throws HttpRequestException {
        return this.form(entry.getKey(), entry.getValue(), charset);
    }

    public HttpRequest form(Object name, Object value) throws HttpRequestException {
        return this.form(name, value, CHARSET_UTF8);
    }

    public HttpRequest form(Object name, Object value, String charset) throws HttpRequestException {
        boolean first;
        boolean bl = first = !this.form;
        if (first) {
            this.contentType(CONTENT_TYPE_FORM, charset);
            this.form = true;
        }
        charset = HttpRequest.getValidCharset(charset);
        try {
            this.openOutput();
            if (!first) {
                this.output.write(38);
            }
            this.output.write(URLEncoder.encode(name.toString(), charset));
            this.output.write(61);
            if (value != null) {
                this.output.write(URLEncoder.encode(value.toString(), charset));
            }
        }
        catch (IOException e2) {
            throw new HttpRequestException(e2);
        }
        return this;
    }

    public HttpRequest form(Map<?, ?> values2, String charset) throws HttpRequestException {
        if (!values2.isEmpty()) {
            for (Map.Entry<?, ?> entry : values2.entrySet()) {
                this.form(entry, charset);
            }
        }
        return this;
    }

    public HttpRequest trustAllCerts() throws HttpRequestException {
        HttpURLConnection connection = this.getConnection();
        if (connection instanceof HttpsURLConnection) {
            ((HttpsURLConnection)connection).setSSLSocketFactory(HttpRequest.getTrustedFactory());
        }
        return this;
    }

    public HttpRequest trustAllHosts() {
        HttpURLConnection connection = this.getConnection();
        if (connection instanceof HttpsURLConnection) {
            ((HttpsURLConnection)connection).setHostnameVerifier(HttpRequest.getTrustedVerifier());
        }
        return this;
    }

    public URL url() {
        return this.getConnection().getURL();
    }

    public String method() {
        return this.getConnection().getRequestMethod();
    }

    public HttpRequest useProxy(String proxyHost, int proxyPort) {
        if (this.connection != null) {
            throw new IllegalStateException("The connection has already been created. This method must be called before reading or writing to the request.");
        }
        this.httpProxyHost = proxyHost;
        this.httpProxyPort = proxyPort;
        return this;
    }

    public HttpRequest followRedirects(boolean followRedirects) {
        this.getConnection().setInstanceFollowRedirects(followRedirects);
        return this;
    }

    static {
        CONNECTION_FACTORY = ConnectionFactory.DEFAULT;
    }

    public static class RequestOutputStream
    extends BufferedOutputStream {
        private final CharsetEncoder encoder;

        public RequestOutputStream(OutputStream stream, String charset, int bufferSize) {
            super(stream, bufferSize);
            this.encoder = Charset.forName(HttpRequest.getValidCharset(charset)).newEncoder();
        }

        public RequestOutputStream write(String value) throws IOException {
            ByteBuffer bytes = this.encoder.encode(CharBuffer.wrap(value));
            super.write(bytes.array(), 0, bytes.limit());
            return this;
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    protected static abstract class FlushOperation<V>
    extends Operation<V> {
        private final Flushable flushable;

        protected FlushOperation(Flushable flushable) {
            this.flushable = flushable;
        }

        @Override
        protected void done() throws IOException {
            this.flushable.flush();
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    protected static abstract class CloseOperation<V>
    extends Operation<V> {
        private final Closeable closeable;
        private final boolean ignoreCloseExceptions;

        protected CloseOperation(Closeable closeable, boolean ignoreCloseExceptions) {
            this.closeable = closeable;
            this.ignoreCloseExceptions = ignoreCloseExceptions;
        }

        @Override
        protected void done() throws IOException {
            if (this.closeable instanceof Flushable) {
                ((Flushable)((Object)this.closeable)).flush();
            }
            if (this.ignoreCloseExceptions) {
                try {
                    this.closeable.close();
                }
                catch (IOException iOException) {}
            } else {
                this.closeable.close();
            }
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    protected static abstract class Operation<V>
    implements Callable<V> {
        protected Operation() {
        }

        protected abstract V run() throws HttpRequestException, IOException;

        protected abstract void done() throws IOException;

        @Override
        public V call() throws HttpRequestException {
            boolean thrown = false;
            try {
                V v2 = this.run();
                return v2;
            }
            catch (HttpRequestException e2) {
                thrown = true;
                throw e2;
            }
            catch (IOException e3) {
                thrown = true;
                throw new HttpRequestException(e3);
            }
            finally {
                block10: {
                    try {
                        this.done();
                    }
                    catch (IOException e4) {
                        if (thrown) break block10;
                        throw new HttpRequestException(e4);
                    }
                }
            }
        }
    }

    public static class HttpRequestException
    extends RuntimeException {
        private static final long serialVersionUID = -1170466989781746231L;

        public HttpRequestException(IOException cause) {
            super(cause);
        }

        public IOException getCause() {
            return (IOException)super.getCause();
        }
    }

    public static class Base64 {
        private static final byte EQUALS_SIGN = 61;
        private static final String PREFERRED_ENCODING = "US-ASCII";
        private static final byte[] _STANDARD_ALPHABET = new byte[]{65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47};

        private Base64() {
        }

        private static byte[] encode3to4(byte[] source2, int srcOffset, int numSigBytes, byte[] destination, int destOffset) {
            byte[] ALPHABET = _STANDARD_ALPHABET;
            int inBuff = (numSigBytes > 0 ? source2[srcOffset] << 24 >>> 8 : 0) | (numSigBytes > 1 ? source2[srcOffset + 1] << 24 >>> 16 : 0) | (numSigBytes > 2 ? source2[srcOffset + 2] << 24 >>> 24 : 0);
            switch (numSigBytes) {
                case 3: {
                    destination[destOffset] = ALPHABET[inBuff >>> 18];
                    destination[destOffset + 1] = ALPHABET[inBuff >>> 12 & 0x3F];
                    destination[destOffset + 2] = ALPHABET[inBuff >>> 6 & 0x3F];
                    destination[destOffset + 3] = ALPHABET[inBuff & 0x3F];
                    return destination;
                }
                case 2: {
                    destination[destOffset] = ALPHABET[inBuff >>> 18];
                    destination[destOffset + 1] = ALPHABET[inBuff >>> 12 & 0x3F];
                    destination[destOffset + 2] = ALPHABET[inBuff >>> 6 & 0x3F];
                    destination[destOffset + 3] = 61;
                    return destination;
                }
                case 1: {
                    destination[destOffset] = ALPHABET[inBuff >>> 18];
                    destination[destOffset + 1] = ALPHABET[inBuff >>> 12 & 0x3F];
                    destination[destOffset + 2] = 61;
                    destination[destOffset + 3] = 61;
                    return destination;
                }
            }
            return destination;
        }

        public static String encode(String string) {
            byte[] bytes;
            try {
                bytes = string.getBytes(PREFERRED_ENCODING);
            }
            catch (UnsupportedEncodingException e2) {
                bytes = string.getBytes();
            }
            return Base64.encodeBytes(bytes);
        }

        public static String encodeBytes(byte[] source2) {
            return Base64.encodeBytes(source2, 0, source2.length);
        }

        public static String encodeBytes(byte[] source2, int off, int len) {
            byte[] encoded = Base64.encodeBytesToBytes(source2, off, len);
            try {
                return new String(encoded, PREFERRED_ENCODING);
            }
            catch (UnsupportedEncodingException uue) {
                return new String(encoded);
            }
        }

        public static byte[] encodeBytesToBytes(byte[] source2, int off, int len) {
            if (source2 == null) {
                throw new NullPointerException("Cannot serialize a null array.");
            }
            if (off < 0) {
                throw new IllegalArgumentException("Cannot have negative offset: " + off);
            }
            if (len < 0) {
                throw new IllegalArgumentException("Cannot have length offset: " + len);
            }
            if (off + len > source2.length) {
                throw new IllegalArgumentException(String.format("Cannot have offset of %d and length of %d with array of length %d", off, len, source2.length));
            }
            int encLen = len / 3 * 4 + (len % 3 > 0 ? 4 : 0);
            byte[] outBuff = new byte[encLen];
            int d2 = 0;
            int e2 = 0;
            int len2 = len - 2;
            while (d2 < len2) {
                Base64.encode3to4(source2, d2 + off, 3, outBuff, e2);
                d2 += 3;
                e2 += 4;
            }
            if (d2 < len) {
                Base64.encode3to4(source2, d2 + off, len - d2, outBuff, e2);
                e2 += 4;
            }
            if (e2 <= outBuff.length - 1) {
                byte[] finalOut = new byte[e2];
                System.arraycopy(outBuff, 0, finalOut, 0, e2);
                return finalOut;
            }
            return outBuff;
        }
    }

    public static interface UploadProgress {
        public static final UploadProgress DEFAULT = new UploadProgress(){

            public void onUpload(long uploaded, long total) {
            }
        };

        public void onUpload(long var1, long var3);
    }

    public static interface ConnectionFactory {
        public static final ConnectionFactory DEFAULT = new ConnectionFactory(){

            public HttpURLConnection create(URL url) throws IOException {
                return (HttpURLConnection)url.openConnection();
            }

            public HttpURLConnection create(URL url, Proxy proxy) throws IOException {
                return (HttpURLConnection)url.openConnection(proxy);
            }
        };

        public HttpURLConnection create(URL var1) throws IOException;

        public HttpURLConnection create(URL var1, Proxy var2) throws IOException;
    }
}

