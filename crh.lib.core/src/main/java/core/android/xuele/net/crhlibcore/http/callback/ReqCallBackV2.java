package core.android.xuele.net.crhlibcore.http.callback;

/**
 * 描述：Api callback V2
 * 功能：
 * Created by KasoGG on 2017/9/7.
 */
public interface ReqCallBackV2<T> {
    String CODE_NON_NETWORK_ERROR = String.valueOf(Integer.MAX_VALUE); //网络请求成功，业务异常，拿不到具体的errorCode
    String CODE_NETWORK_ERROR = String.valueOf(Integer.MAX_VALUE - 1); // 网络异常，可能是网络异常或状态码不等于200


    /**
     * 响应成功
     */
    void onReqSuccess(T result);

    /**
     * 响应失败
     */
    void onReqFailed(String errorMsg, String errorCode);

}
