package core.android.xuele.net.crhlibcore.http.converter;


import java.lang.reflect.Type;

import core.android.xuele.net.crhlibcore.http.HttpUtils;
import core.android.xuele.net.crhlibcore.uti.JsonUtil;

/**
 * Convert response String to bean, only support String and JSON for now
 * <p>
 * Created by KasoGG on 2017/1/12.
 */
public class ResponseConverter<T> implements Converter<String, T> {
    private final Type responseType;

    public ResponseConverter(Type responseType) {
        this.responseType = responseType;
    }

    @Override
    public T convert(String result) {
        Class<?> clazz = (Class) responseType;
        HttpUtils.log(result);
        if (clazz == String.class.getClass()) {
            //noinspection unchecked
            return (T) result;
        }
        return (T) JsonUtil.jsonToObject(result, clazz);
    }
}
