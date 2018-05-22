package core.android.xuele.net.crhlibcore.resource;

/**
 * Created by louweijun on 2018-05-15.
 */

public interface ResourceDownListener {

    void onProgressChange(int percent);

    void onStart();

    void onPause();

    void onSuccess();

    void onError();
}
