package core.android.xuele.net.crhlibcore.http.exception;

/**
 * 描述：Api请求异常
 * 功能：
 * Created by KasoGG on 2017/9/7.
 */
public class ApiNetworkException extends RuntimeException {
    private static final long serialVersionUID = 2260497961898961357L;

    public ApiNetworkException() {
    }

    public ApiNetworkException(String message) {
        super(message);
    }

    public ApiNetworkException(Throwable cause) {
        super(cause);
    }
}
