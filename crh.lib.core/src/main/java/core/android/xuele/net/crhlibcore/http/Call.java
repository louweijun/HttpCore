package core.android.xuele.net.crhlibcore.http;


import core.android.xuele.net.crhlibcore.http.callback.ReqCallBack;
import core.android.xuele.net.crhlibcore.http.callback.ReqCallBackV2;
import core.android.xuele.net.crhlibcore.http.callback.ApiCallback;
import okhttp3.Request;

/**
 * Built request call
 * <p>
 * Created by KasoGG on 2017/1/11.
 */
public interface Call<T> {
    /**
     * 封装好业务处理的callback，兼容旧的接口
     * 使用{@link #requestV2(ReqCallBackV2)}
     */
    Call<T> request(final ReqCallBack<T> callback);

    /**
     * 封装好业务处理的callback，兼容旧的接口
     * 使用{@link #requestV2(ReqCallBackV2)}
     */
    Call<T> requestV2(final ReqCallBackV2<T> callback);



    /**
     * Synchronously send the request and return its response.
     */
    HttpResponse<T> execute();

    /**
     * Asynchronously send the request and notify {@code callback} of its response or if an error
     * occurred talking to the server, creating the request, or processing the response.
     */
    void enqueue(ApiCallback<T> callback);

    /**
     * Returns true if this call has been either {@linkplain #execute() executed} or {@linkplain
     * #enqueue(ApiCallback) enqueued}. It is an error to execute or enqueue a call more than once.
     */
    boolean isExecuted();


    /**
     * Cancel this call. An attempt will be made to cancel in-flight calls, and if the call has not
     * yet been executed it never will be.
     */
    void cancel();

    /**
     * True if {@link #cancel()} was called.
     */
    boolean isCanceled();

    /**
     * True if the call is finished
     */
    boolean isFinished();

    /**
     * The original HTTP request.
     */
    Request rawRequest();
}
