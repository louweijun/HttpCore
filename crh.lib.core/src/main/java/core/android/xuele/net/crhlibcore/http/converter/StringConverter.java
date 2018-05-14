package core.android.xuele.net.crhlibcore.http.converter;

import com.alibaba.fastjson.JSON;

/**
 * Convert object to String or JSON String
 * <p>
 * Created by KasoGG on 2017/1/12.
 */
public class StringConverter implements Converter<Object, String> {
    @Override
    public String convert(Object object) {
        if (object instanceof String) {
            return (String) object;
        }
        return objectToJson(object);
    }

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
            return JSON.toJSONString(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
