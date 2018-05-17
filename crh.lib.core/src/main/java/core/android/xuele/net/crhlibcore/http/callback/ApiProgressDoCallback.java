package core.android.xuele.net.crhlibcore.http.callback;

import java.io.File;

import core.android.xuele.net.crhlibcore.http.Call;

/**
 * Created by louweijun on 2018-05-16.
 */

public interface ApiProgressDoCallback<T> extends ApiProgressCallback<T> {

    void onSuccessDoInBackground(Call<T> call, File file);
}
