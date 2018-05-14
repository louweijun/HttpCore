package core.android.xuele.net.crhlibcore.http;

import android.text.TextUtils;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import core.android.xuele.net.crhlibcore.http.callback.ApiCallback;
import core.android.xuele.net.crhlibcore.http.callback.ApiProgressCallback;
import core.android.xuele.net.crhlibcore.uti.JsonUtil;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.BufferedSink;

/**
 * Build an OKHttp request
 * <p>
 * Created by KasoGG on 2017/1/11.
 */
final class OKHttpRequestBuilder {
    private final ApiManager apiManager;
    private final ApiMethod apiMethod;
    private final ApiCallback callback;
    private final String baseUrl;

    private final Request.Builder requestBuilder;
    private HttpUrl.Builder urlBuilder;
    private RequestBody body;

    OKHttpRequestBuilder(ApiManager apiManager, ApiMethod apiMethod, ApiCallback callback) {
        this.apiManager = apiManager;
        this.baseUrl = TextUtils.isEmpty(apiMethod.baseUrl) ? apiManager.baseUrl : apiMethod.baseUrl;
        this.apiMethod = apiMethod;
        this.callback = callback;

        this.requestBuilder = new Request.Builder();

        if (apiMethod.headers != null) {
            requestBuilder.headers(apiMethod.headers);
        }
    }

    Request build() {
        urlBuilder = getUrlBuilder();
        if (urlBuilder == null) {
            throw new IllegalArgumentException("Malformed URL. Base: " + baseUrl + ", Path: " + apiMethod.path);
        }

        if ("GET".equals(apiMethod.httpMethod)) {
            for (Parameter parameter : apiMethod.dealtParameters) {
                if (HttpUtils.checkParameter(parameter)) {
                    addGetParam(parameter.getKey(), apiManager.paramConverter.convert(parameter.getValue()));
                }
            }
        }

        HttpUrl url = urlBuilder.build();
        HttpUtils.log("REQUEST_URL-----> " + url);

        if ("POST".equals(apiMethod.httpMethod)) {
            buildRequestBody(apiMethod.dealtParameters);
        }

        MediaType contentType = apiMethod.contentType;
        if (contentType != null) {
            if (body != null) {
                body = new ContentTypeOverridingRequestBody(body, contentType);
            } else {
                requestBuilder.addHeader("Content-Type", contentType.toString());
            }
        }

        return requestBuilder.url(url).method(apiMethod.httpMethod, body).build();
    }

    private HttpUrl.Builder getUrlBuilder() {
        HttpUtils.checkNotNull(baseUrl, "Base url is null. You must set a base url first.");
        HttpUrl httpUrl = HttpUrl.parse(baseUrl);
        if (httpUrl == null) {
            throw new IllegalArgumentException("Malformed URL: " + baseUrl);
        }

        if (apiMethod.path != null) {
            return httpUrl.newBuilder(apiMethod.path);
        } else {
            return httpUrl.newBuilder();
        }
    }

    private void addGetParam(String name, String value) {
        urlBuilder.addQueryParameter(name, value);
    }

    private void buildRequestBody(List<Parameter> parameters) {
        if (apiMethod.isMultipart) {
            body = buildMultipartBody(parameters);
        } else {
            body = buildFormBody(parameters);
        }
    }

    private RequestBody buildFormBody(List<Parameter> parameters) {
        FormBody.Builder formBuilder = new FormBody.Builder();
        Map<String, Object> paramMap = new HashMap<>();
        for (Parameter parameter : parameters) {
            if (!HttpUtils.checkParameter(parameter)) {
                continue;
            }
            Object value = parameter.getValue();
            String rawValue =apiManager.paramConverter.convert(value);
            paramMap.put(parameter.getKey(),rawValue);
            formBuilder.add(parameter.getKey(), rawValue);
        }
        String paramJson = "";
        if (paramMap.size() != 0) {
            paramJson = JsonUtil.objectToJson(paramMap);
        }

        HttpUtils.log("REQUEST_PARAM-----> " + paramJson);
        return formBuilder.build();
    }

    private RequestBody buildMultipartBody(List<Parameter> parameters) {
        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (Parameter parameter : parameters) {
            if (!HttpUtils.checkParameter(parameter)) {
                continue;
            }
            Object value = parameter.getValue();
            if (value instanceof File && ((File) value).exists()) {
                File file = (File) value;
                RequestBody fileBody = RequestBody.create(MediaType.parse(guessMimeType(file.getName())), file);
                if (callback != null && callback instanceof ApiProgressCallback) {
                    fileBody = new ProgressRequestBody(fileBody, (ApiProgressCallback) callback, apiManager);
                }
                multipartBuilder.addFormDataPart(parameter.getKey(), file.getName(), fileBody);
            } else if (value instanceof byte[]) {
                RequestBody fileBody = new ByteBody(MediaType.parse(guessMimeType(null)), (byte[]) value);
                if (callback != null && callback instanceof ApiProgressCallback) {
                    fileBody = new ProgressRequestBody(fileBody, (ApiProgressCallback) callback, apiManager);
                }
                multipartBuilder.addFormDataPart(parameter.getKey(), System.currentTimeMillis() + ".data", fileBody);
            } else {
                multipartBuilder.addFormDataPart(parameter.getKey(), apiManager.paramConverter.convert(value));
            }
        }
        return multipartBuilder.build();
    }

    private static String guessMimeType(String path) {
        String contentTypeFor = null;
        if (!TextUtils.isEmpty(path)) {
            FileNameMap fileNameMap = URLConnection.getFileNameMap();
            contentTypeFor = fileNameMap.getContentTypeFor(path);
        }
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    private static class ContentTypeOverridingRequestBody extends RequestBody {
        private final RequestBody delegate;
        private final MediaType contentType;

        ContentTypeOverridingRequestBody(RequestBody delegate, MediaType contentType) {
            this.delegate = delegate;
            this.contentType = contentType;
        }

        @Override
        public MediaType contentType() {
            return contentType;
        }

        @Override
        public long contentLength() throws IOException {
            return delegate.contentLength();
        }

        @Override
        public void writeTo(BufferedSink sink) throws IOException {
            delegate.writeTo(sink);
        }
    }
}
