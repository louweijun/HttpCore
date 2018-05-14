package core.android.xuele.net.crhlibcore.http.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 指定Base Url
 * <p>
 * Created by KasoGG on 2017/1/13.
 */
@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface BaseUrl {
    String value();
}
