package core.android.xuele.net.crhlibcore.lifecycler;

import android.app.Application;
import android.content.Context;

/**
 * Created by louweijun on 2018-05-02.
 */

public interface AppLifecycle {

    void onAppCreate(Application application);

    void onMainActivityCreate(Context context);
}
