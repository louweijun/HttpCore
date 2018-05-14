package core.android.xuele.net.crhlibcore.http.callback;

/**
 * Created by Administrator on 2016/5/11 0011.
 */
public interface ReqUploadCallBack<T> extends ReqCallBack<T> {


    /**
     * 响应进度更新
     */
    void updateProgress(long total, long current);
}
