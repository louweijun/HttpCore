package core.android.xuele.net.crhlibcore.http.interceptor;

import java.io.IOException;

import core.android.xuele.net.crhlibcore.http.ApiManager;
import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * 请求前网络连接情况检查
 * <p>
 * Created by KasoGG on 2017/1/16.
 */
public class ConnectionCheckInterceptor implements Interceptor {
    private long mLastCallTime = 0;
    private static final long MIN_DURATION = 3000;

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (needToastNetError()) {
            ApiManager.ready().runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    Toast.makeText(XLApp.get(), "请检查网络连接~", Toast.LENGTH_SHORT).show();
                }
            });
        }
        return chain.proceed(chain.request());
    }

    private boolean needToastNetError() {
        long curTime = System.currentTimeMillis();
        if (curTime - mLastCallTime > MIN_DURATION) {
            mLastCallTime = curTime;
            return true;
        }
        return false;
    }
}

