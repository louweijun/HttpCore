package core.android.xuele.net.crhlibcore.uti;

import android.text.TextUtils;

import com.google.gson.Gson;

/**
 * json相关<br>
 * Author: 李超军<br>
 * Create 2014-5-28<br>
 * fixed by louweijun on 2016-8-29.<br>
 * Version 1.1.0
 */
public class JsonUtil {

    public static Gson mGson = new Gson();

    /**
     * 对象转化为json
     *
     * @param object 对象
     * @return Json字符串
     */
    public static String objectToJson(Object object) {
        if (object == null) {
            return "";
        }
        try {
            return mGson.toJson(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * json转化为对象
     *
     * @param json  字符串
     * @param clazz 需要转化的对象类型
     * @return 对象
     */
    public static <T> T jsonToObject(String json, Class<T> clazz) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        try {
            return mGson.fromJson(json, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }




}
