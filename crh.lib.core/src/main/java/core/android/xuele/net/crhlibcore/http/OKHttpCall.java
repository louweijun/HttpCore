package core.android.xuele.net.crhlibcore.http;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import core.android.xuele.net.crhlibcore.http.callback.ApiCallback;
import core.android.xuele.net.crhlibcore.http.converter.ResponseConverter;
import core.android.xuele.net.crhlibcore.http.exception.ApiNetworkException;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static okhttp3.internal.Util.UTF_8;

/**
 * Generate OKHttpCall
 * <p>
 * Created by KasoGG on 2017/1/11.
 */
abstract class OKHttpCall<T> implements Call<T> {
    private final ApiMethod apiMethod;
    private final ApiManager apiManager;
    private final String cacheKey;
    protected ApiCallback<T> callback;
    private okhttp3.Call rawCall;
    private boolean executed;
    private boolean canceled;
    private boolean finished;

    OKHttpCall(ApiManager apiManager, ApiMethod apiMethod) {
        this.apiManager = apiManager;
        this.apiMethod = apiMethod;
        if (apiMethod != null && apiMethod.cacheKey != null) {
            this.cacheKey = String.format("%s%s", apiMethod.path, apiMethod.cacheKey);
        } else {
            this.cacheKey = null;
        }
    }

    @Override
    public HttpResponse<T> execute() {
        okhttp3.Call call;

        synchronized (this) {
            if (executed) {
                throw new IllegalStateException("Already executed.");
            }
            executed = true;

            call = rawCall;
            if (call == null) {
                try {
                    call = rawCall = createRawCall();
                } catch (Exception e) {
                    finished = true;
                    HttpUtils.log(e);
                    return new HttpResponse<>(null, 0, null, null, null, null);
                }
            }
        }

        if (canceled) {
            call.cancel();
        }

        Response rawResponse = null;
        try {
            rawResponse = call.execute();
            HttpResponse<T> response = parseResponse(rawResponse);
            putCache(response);
            return response;
        } catch (Exception e) {
            HttpUtils.log(e);
            // 失败时如果设置了缓存则获取网络缓存
            HttpResponse<T> cacheResponse = getCacheResponse();
            if (cacheResponse != null) {
                return cacheResponse;
            }
            if (rawResponse != null) {
                return new HttpResponse<>(rawResponse, rawResponse.code(), null, null, getResponseHeaders(rawResponse), null);
            } else {
                return new HttpResponse<>(null, 0, null, null, null, null);
            }
        } finally {
            finished = true;
        }
    }

    @Override
    public void enqueue(final ApiCallback<T> callback) {
        this.callback = callback;
        okhttp3.Call call;

        synchronized (this) {
            if (executed) {
                throw new IllegalStateException("Already executed.");
            }
            executed = true;

            call = rawCall;
            if (call == null) {
                try {
                    call = rawCall = createRawCall();
                } catch (final Exception e) {
                    finished = true;
                    handleFailure(e);
                    return;
                }
            }
        }

        if (canceled) {
            call.cancel();
        }

        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response rawResponse) throws IOException {
                if (isCanceled()) {
                    return;
                }
                if (rawResponse.isSuccessful()) {
                    final HttpResponse<T> response;
                    try {
                        response = parseResponse(rawResponse);
                        finished = true;
                        putCache(response);
                        if (callback != null) {
                            apiManager.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onSuccess(OKHttpCall.this, response);
                                }
                            });
                        }
                    } catch (Throwable t) {
                        handleFailure(t);
                    }
                } else {
                    handleFailure(new ApiNetworkException(rawResponse.toString()));
                }
            }

            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull final IOException e) {
                finished = true;
                handleFailure(new ApiNetworkException(e));
            }
        });
    }

    private void handleFailure(final Throwable t) {
        if (isCanceled()) {
            return;
        }
        HttpUtils.log(t);
        if (callback != null) {
            final HttpResponse<T> cacheResponse = getCacheResponse();
            if (cacheResponse != null) {
                apiManager.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSuccess(OKHttpCall.this, cacheResponse);
                    }
                });
            } else {
                apiManager.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFailure(OKHttpCall.this, t);
                    }
                });
            }
        }
    }

    private void putCache(HttpResponse response) {
    }

    @Override
    public boolean isExecuted() {
        return executed;
    }

    public void cancel() {
        canceled = true;

        okhttp3.Call call;
        synchronized (this) {
            call = rawCall;
        }
        if (call != null) {
            call.cancel();
        }
    }

    @Override
    public boolean isCanceled() {
        if (canceled) {
            return true;
        }
        synchronized (this) {
            return rawCall != null && rawCall.isCanceled();
        }
    }

    @Override
    public boolean isFinished() {
        return finished || canceled;
    }

    @Override
    public Request rawRequest() {
        okhttp3.Call call = rawCall;
        if (call != null) {
            return call.request();
        }
        try {
            return (rawCall = createRawCall()).request();
        } catch (RuntimeException e) {
            HttpUtils.log(e);
            throw e;
        }
    }

    protected okhttp3.Call createRawCall() {
        Request request = new OKHttpRequestBuilder(apiManager, apiMethod, callback).build();
        okhttp3.Call call = apiManager.okHttpClient.newCall(request);
        if (call == null) {
            throw new NullPointerException("Call returned null.");
        }
        return call;
    }

    protected HttpResponse<T> parseResponse(Response rawResponse) throws IOException {
        Map<String, String> headers = getResponseHeaders(rawResponse);
        int code = rawResponse.code();
        byte[] bodyBytes = rawResponse.body().bytes();
        String bodyString = new String(bodyBytes, getCharset(rawResponse.body()));
        T bean = new ResponseConverter<T>(apiMethod.responseType).convert(bodyString);
        return new HttpResponse<>(rawResponse, code, bodyBytes, bodyString, headers, bean);
    }

    private HttpResponse<T> getCacheResponse() {
//        try {
//            String cacheValue;
//            if (apiMethod == null || apiMethod.cacheKey == null || (cacheValue == null)) {
//                return null;
//            }
//            byte[] bodyBytes = cacheValue.getBytes();
//            T bean = new ResponseConverter<T>(apiMethod.responseType).convert(cacheValue);
//            return new XLResponse<>(null, 200, bodyBytes, cacheValue, null, bean);
//        } catch (Exception e) {
//            XLHttpUtils.log(e);
//            return null;
//        }
        return null;
    }

    Map<String, String> getResponseHeaders(Response response) {
        Map<String, String> headers = new HashMap<>();
        if (response.headers() != null && response.headers().size() > 0) {
            for (String key : response.headers().names()) {
                headers.put(key, response.headers().get(key));
            }
        }
        return headers;
    }

    private Charset getCharset(ResponseBody body) {
        MediaType contentType = body.contentType();
        return contentType != null ? contentType.charset(UTF_8) : UTF_8;
    }
}
