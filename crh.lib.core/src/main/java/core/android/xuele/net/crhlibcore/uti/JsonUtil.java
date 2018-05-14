package core.android.xuele.net.crhlibcore.uti;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static com.alibaba.fastjson.JSON.parseArray;
import static com.alibaba.fastjson.JSON.parseObject;

/**
 * json相关<br>
 * Author: 李超军<br>
 * Create 2014-5-28<br>
 * fixed by louweijun on 2016-8-29.<br>
 * Version 1.1.0
 */
public class JsonUtil {

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

    /**
     * 对象转化为json
     *
     * @param object        对象
     * @param excludeFields 排除的字段
     * @return Json字符串
     */
    public static String objectToJson(Object object, String... excludeFields) {
        if (object == null) {
            return "";
        }
        try {
            SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
            if (excludeFields != null) {
                for (String field : excludeFields) {
                    filter.getExcludes().add(field);
                }
            }
            return JSON.toJSONString(object, filter);
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
            return parseObject(json, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * json转化为对象
     *
     * @param json  字符串
     * @param clazz 需要转化的对象类型
     * @return 对象
     */
    public static <T> List jsonToArray(String json, Class<T> clazz) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        try {
            return parseArray(json, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * json转化为对象
     *
     * @param json 字符串
     * @param type LinkedHashMap泛型
     * @return 对象
     */
    public static LinkedHashMap jsonToLinkedHashMap(String json, Type type) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        try {
            return JSON.parseObject(json, type, Feature.OrderedField);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * json to map
     *
     * @param object jsonObject
     * @return map
     */
    public static HashMap<String, Object> objToHashMap(JSONObject object) {
        if (object == null)
            return null;
        String jsonData = object.toString();
        HashMap<String, Object> objectHashMap = null;
        try {
            if (!TextUtils.isEmpty(jsonData)) {
                objectHashMap = parseObject(jsonData, new TypeReference<HashMap<String, Object>>() {
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objectHashMap;
    }

    /**
     * json to list
     *
     * @return list
     */
    public static ArrayList jsonArrayToArrayList(org.json.JSONArray resources) {
        if (resources == null) {
            return null;
        }
        String jsonData = resources.toString();
        ArrayList arrayList = null;
        try {
            if (!TextUtils.isEmpty(jsonData)) {
                arrayList = parseObject(jsonData, new TypeReference<ArrayList>() {
                });

            }
        } catch (Exception e) {
        }
        return arrayList;
    }

}
