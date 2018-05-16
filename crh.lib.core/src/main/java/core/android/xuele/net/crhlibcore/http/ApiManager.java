package core.android.xuele.net.crhlibcore.http;

import android.os.Handler;
import android.os.Looper;
import android.webkit.URLUtil;

import java.io.File;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import core.android.xuele.net.crhlibcore.http.callback.ApiCallback;
import core.android.xuele.net.crhlibcore.http.converter.Converter;
import core.android.xuele.net.crhlibcore.http.converter.StringConverter;
import core.android.xuele.net.crhlibcore.http.interceptor.ConnectionCheckInterceptor;
import core.android.xuele.net.crhlibcore.http.interceptor.HeaderInterceptor;
import core.android.xuele.net.crhlibcore.http.interceptor.HttpLoggingInterceptor;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

/**
 * Api统一管理类
 * <p>
 * Created by KasoGG on 2017/1/10.
 */
public class ApiManager {
    private final Map<Class, Object> apiClassCache = new ConcurrentHashMap<>();
    private final Map<Method, ApiMethod> apiMethodCache = new ConcurrentHashMap<>();
    OkHttpClient okHttpClient;
    OkHttpClient downloadClient;
    String baseUrl;
    Converter<Object, String> paramConverter;
    ICommonParamProvider commonParamProvider;
    private HeaderInterceptor headerInterceptor;

    private Handler handler =new Handler(Looper.getMainLooper());

    private ApiManager() {
        headerInterceptor = new HeaderInterceptor();
//        @formatter:off
        this.downloadClient = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(600, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(new ConnectionCheckInterceptor())
                .addInterceptor(headerInterceptor)
//                .dns(new HttpDns())
                .build();
        this.okHttpClient = downloadClient.newBuilder()
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(600, TimeUnit.SECONDS)
                .addInterceptor(new HttpLoggingInterceptor())
                .build();
        //@formatter:on
        this.paramConverter = new StringConverter();
    }

    public static ApiManager ready() {
        return SingletonHolder.instance;
    }


    public ApiManager baseUrl(String baseUrl) {
        if (!URLUtil.isValidUrl(baseUrl)) {
            throw new IllegalArgumentException("Illegal URL: " + baseUrl);
        }
        this.baseUrl = baseUrl;
        return this;
    }

    public void addInterceptor(Interceptor interceptor) {
        if (okHttpClient != null) {
            okHttpClient = okHttpClient.newBuilder().addInterceptor(interceptor).build();
        }
    }

    public void addHeader(String key, String value) {
        if (headerInterceptor != null) {
            headerInterceptor.addHeader(key, value);
        }
    }

    public void addHeaders(Map<String, String> headers) {
        if (headerInterceptor != null) {
            headerInterceptor.addHeaders(headers);
        }
    }

    public Map<String, String> getHeaders() {
        if (headerInterceptor != null) {
            return headerInterceptor.getHeaders();
        }
        return null;
    }


    /**
     * 将DownloadCall于fragment或者activity的生命周期绑定
     *
     * @param url            下载地址
     * @param savePath       保存的路径
     * @param renameByUrl    是否根据url解析出文件名，如果为true则使用url解析出的文件名，会覆盖savePath
     * @param callback       回调
     * @return XLCall<File>
     */
    public Call<File> downloadFile(String url, final String savePath, final boolean renameByUrl, final ApiCallback<File> callback) {
        Call<File> downloadCall = new DownloadCall(this, url, savePath, renameByUrl);
        downloadCall.enqueue(callback);
        return downloadCall;
    }

    public void runOnUiThread(Runnable r) {
        handler.post(r);
    }

    @SuppressWarnings("unchecked")
    public <T> T getApi(Class<T> api) {
        T result = (T) apiClassCache.get(api);
        if (result != null) {
            return result;
        }
        synchronized (apiClassCache) {
            result = (T) apiClassCache.get(api);
            if (result == null) {
                result = create(api);
                apiClassCache.put(api, result);
            }
        }
        return result;
    }


    @SuppressWarnings("unchecked")
    private <T> T create(final Class<T> api) {
        HttpUtils.validateApiInterface(api);
        return (T) Proxy.newProxyInstance(api.getClassLoader(), new Class<?>[]{api}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object... args) throws Throwable {
                ApiMethod apiMethod = loadApiMethod(method);
                apiMethod.setArgs(args);

                return new ApiCallV2(ApiManager.this, apiMethod);
            }
        });
    }



    private ApiMethod loadApiMethod(Method method) {
        ApiMethod result = apiMethodCache.get(method);
        if (result != null) {
            return result;
        }

        synchronized (apiMethodCache) {
            result = apiMethodCache.get(method);
            if (result == null) {
                result = new ApiMethod.Builder(method).build();
                apiMethodCache.put(method, result);
            }
        }
        return result;
    }

    public void setCommonParamProvider(ICommonParamProvider commonParamProvider) {
        this.commonParamProvider = commonParamProvider;
    }


    /**
     * 公共请求参数提供者
     */
    public interface ICommonParamProvider {
        List<Parameter> getCommonParamList();
    }

    private static class SingletonHolder {
        private static ApiManager instance = new ApiManager();
    }

}
