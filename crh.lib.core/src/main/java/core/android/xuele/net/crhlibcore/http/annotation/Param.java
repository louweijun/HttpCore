package core.android.xuele.net.crhlibcore.http.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Request Param
 * <p>
 * Created by KasoGG on 2017/1/10.
 */
@Documented
@Target(PARAMETER)
@Retention(RUNTIME)
public @interface Param {
    /**
     * The query parameter name(key).
     */
    String value();
}
