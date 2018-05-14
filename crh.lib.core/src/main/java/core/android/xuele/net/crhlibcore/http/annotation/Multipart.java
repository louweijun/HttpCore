package core.android.xuele.net.crhlibcore.http.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Multipart类型，文件上传时需要声明此类型，暂时只支持byte[]和File
 */
@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface Multipart {
}
