package core.android.xuele.net.crhlibcore.lifecycler;

import android.app.Application;
import android.content.Context;

/**
 * Created by louweijun on 2018-05-02.
 */

public class LifecycleManager {

    public void registerCRHLifecycle(AppLifecycle lifecycle) {
        this.lifecycle = lifecycle;
    }

    private AppLifecycle lifecycle;


    public AppLifecycle getLifecycle() {
        return lifecycle;
    }

    public void onAppCreate(Application application) {
        if (lifecycle != null)
            lifecycle.onAppCreate(application);
    }

    public void onMainActivityCreate(Context context) {
        if (lifecycle != null)
            lifecycle.onMainActivityCreate(context);
    }


    private LifecycleManager() {
    }

    private static LifecycleManager instance = new LifecycleManager();

    public static LifecycleManager getInstance() {
        return instance;
    }

}
