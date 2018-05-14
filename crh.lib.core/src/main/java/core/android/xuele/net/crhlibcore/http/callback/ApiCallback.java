package core.android.xuele.net.crhlibcore.http.callback;


import core.android.xuele.net.crhlibcore.http.Call;
import core.android.xuele.net.crhlibcore.http.HttpResponse;

/**
 * Api callback on main thread
 * Created by KasoGG on 2017/1/11.
 */
public interface ApiCallback<T> {
    void onSuccess(Call<T> call, HttpResponse<T> response);

    void onFailure(Call<T> call, Throwable t);
}
