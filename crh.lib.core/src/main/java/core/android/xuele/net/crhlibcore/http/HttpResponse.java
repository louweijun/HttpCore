package core.android.xuele.net.crhlibcore.http;

import java.io.File;
import java.util.Map;

import okhttp3.Response;

/**
 * Wrapped Response
 * <p>
 */
public class HttpResponse<T> {
    private final Response rawResponse;
    private final int code;
    private final byte[] bodyBytes;
    private final String bodyString;
    private final Map<String, String> headers;
    private final T body;

    public HttpResponse(Response rawResponse, int code, byte[] bodyBytes, String bodyString, Map<String, String> headers, T bean) {
        this.rawResponse = rawResponse;
        this.code = code;
        this.bodyBytes = bodyBytes;
        this.bodyString = bodyString;
        this.headers = headers;
        this.body = bean;
    }

    public Response rawResponse() {
        return rawResponse;
    }

    public int code() {
        return code;
    }

    public String string() {
        if (body != null && body instanceof File) {
            throw new UnsupportedOperationException("File response doesn't support string() method");
        }
        return bodyString;
    }

    public byte[] bytes() {
        if (body != null && body instanceof File) {
            throw new UnsupportedOperationException("File response doesn't support bytes() method");
        }
        return bodyBytes;
    }

    public Map<String, String> headers() {
        return headers;
    }

    public T body() {
        return body;
    }
}
