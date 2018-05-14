package core.android.xuele.net.crhlibcore.http;


import core.android.xuele.net.crhlibcore.http.callback.ReqCallBack;

/**
 * WebSocket Call
 * <p>
 */
public interface XLWsCall<T> {
    XLWsCall<T> request(final ReqCallBack<T> callback);

    /**
     * Close this call.
     */
    void close();

    /**
     * True if {@link #close()} was called.
     */
    boolean isClosed();
}
