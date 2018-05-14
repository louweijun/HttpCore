package core.android.xuele.net.crhlibcore.http.interceptor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 添加公共Header
 * <p>
 * Created by KasoGG on 2017/1/13.
 */
public class HeaderInterceptor implements Interceptor {
    private Map<String, String> headers;

    public HeaderInterceptor() {
        this.headers = defaultHeaders();
    }

    public void addHeader(String key, String value) {
        if (headers != null) {
            this.headers.put(key, value);
        }
    }

    public void addHeaders(Map<String, String> headers) {
        if (headers != null) {
            this.headers.putAll(headers);
        }
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    private Map<String, String> defaultHeaders() {
        Map<String, String> map = new HashMap<>();
        map.put("Connection", "keep-alive");
//        map.put("platform", XLLibCoreUtils.getDeviceModel());
//        map.put("phoneModel", XLHttpUtils.filterChineseHeader(Build.MODEL));
//        map.put("systemVersion", Build.VERSION.RELEASE);
//        map.put("appVersion", XLLibCoreUtils.getVersionName());
//        map.put("networkType", XLLibCoreUtils.getNetTypeName());
        return map;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (headers == null || headers.size() == 0) {
            return chain.proceed(request);
        }

        Request.Builder requestBuilder = request.newBuilder();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            requestBuilder.addHeader(entry.getKey(), entry.getValue());
        }
        return chain.proceed(requestBuilder.build());
    }
}