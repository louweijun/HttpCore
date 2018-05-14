package core.android.xuele.net.crhlibcore.http.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 动态指定BaseUrl，通过参数传入，不能与{@link BaseUrl}同时用
 * <p>
 * Created by KasoGG on 2017/1/19.
 */
@Documented
@Target({PARAMETER})
@Retention(RUNTIME)
public @interface Host {
}
