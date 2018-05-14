package core.android.xuele.net.crhlibcore.http.interceptor;


import java.io.IOException;

import core.android.xuele.net.crhlibcore.BuildConfig;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * HTTP请求Log
 * <p>
 * Created by KasoGG on 2017/1/14.
 */
public class HttpLoggingInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (!BuildConfig.DEBUG) {
            return chain.proceed(request);
        }

        long startNs = System.nanoTime();
        Response response = chain.proceed(request);
        return response;
    }
}
