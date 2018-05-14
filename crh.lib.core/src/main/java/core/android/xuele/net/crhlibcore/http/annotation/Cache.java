package core.android.xuele.net.crhlibcore.http.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 网络请求缓存注解
 * 默认缓存的key为{@link net.xuele.android.core.http.ApiMethod#path} + CacheKey, 传null不缓存
 * Created by KasoGG on 2017/3/17.
 */
@Documented
@Target(PARAMETER)
@Retention(RUNTIME)
public @interface Cache {
}