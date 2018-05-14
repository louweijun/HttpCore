package core.android.xuele.net.crhlibcore.http.callback;

/**
 * Api callback with progress on main thread
 * Created by KasoGG on 2017/1/18.
 */
public interface ApiProgressCallback<T> extends ApiCallback<T> {
    void onProgress(long bytesWritten, long totalBytes, boolean hasFinished);
}
