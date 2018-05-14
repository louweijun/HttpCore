package core.android.xuele.net.crhlibcore.http.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 指定业务类型，OPEN_API和文件系统加密方式不同
 * <p>
 * Created by KasoGG on 2017/1/19.
 */
@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface BizType {
    int OPEN_API = 0;
    int FILE_SYSTEM = 1;

    int value() default OPEN_API;
}
