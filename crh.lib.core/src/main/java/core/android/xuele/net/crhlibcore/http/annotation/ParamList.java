package core.android.xuele.net.crhlibcore.http.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Request Param{@link Param} list of which the params share a same key
 * 循环遍历List转成{@link Param}，共用同一个key，并且不会被签名
 * <p>
 * Created by KasoGG on 2017/1/20.
 */
@Documented
@Target(PARAMETER)
@Retention(RUNTIME)
public @interface ParamList {
    /**
     * The query parameter name(key).
     */
    String value();
}
