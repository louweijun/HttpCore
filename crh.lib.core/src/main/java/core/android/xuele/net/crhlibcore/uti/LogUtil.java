package core.android.xuele.net.crhlibcore.uti;

import android.text.TextUtils;
import android.util.Log;

/**
 * Created by LPH on 2016/11/17.
 */

public class LogUtil {
    public static boolean debugOn = true;
    private static final String TAG = "CRH.CORE";

    public static void v(String s) {
        if (debugOn) {
            Log.v(TAG, TextUtils.isEmpty(s) ? "" : s);
        }
    }

    public static void w(String s) {
        if (debugOn) {
            Log.w(TAG, TextUtils.isEmpty(s) ? "" : s);
        }
    }

    public static void d(String s) {
        if (debugOn) {
            Log.w(TAG, TextUtils.isEmpty(s) ? "" : s);
        }
    }

    public static void e(String s) {
        if (debugOn) {
            Log.e(TAG, TextUtils.isEmpty(s) ? "" : s);
        }
    }

    public static void i(String s) {
        if (debugOn) {
            Log.w(TAG, TextUtils.isEmpty(s) ? "" : s);
        }
    }
}
