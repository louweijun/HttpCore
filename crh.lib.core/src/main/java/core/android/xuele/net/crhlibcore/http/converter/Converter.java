package core.android.xuele.net.crhlibcore.http.converter;

/**
 * Convert request param and response
 * <p>
 * Created by KasoGG on 2017/1/12.
 */
public interface Converter<F, T> {
    T convert(F object);
}
