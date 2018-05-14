package core.android.xuele.net.crhlibcore.http;

import android.text.TextUtils;
import android.webkit.URLUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import core.android.xuele.net.crhlibcore.http.annotation.BaseUrl;
import core.android.xuele.net.crhlibcore.http.annotation.BizType;
import core.android.xuele.net.crhlibcore.http.annotation.Cache;
import core.android.xuele.net.crhlibcore.http.annotation.GET;
import core.android.xuele.net.crhlibcore.http.annotation.Headers;
import core.android.xuele.net.crhlibcore.http.annotation.Host;
import core.android.xuele.net.crhlibcore.http.annotation.Multipart;
import core.android.xuele.net.crhlibcore.http.annotation.POST;
import core.android.xuele.net.crhlibcore.http.annotation.Param;
import core.android.xuele.net.crhlibcore.http.annotation.ParamList;
import core.android.xuele.net.crhlibcore.http.annotation.WebSocket;
import okhttp3.MediaType;

/**
 * Resolve Api interface
 */
final class ApiMethod {
    private final List<ParameterHandler> parameterHandlers;
    final String httpMethod;
    final String path;
    final okhttp3.Headers headers;
    final MediaType contentType;
    final Type responseType;
    final boolean hasBody;
    final boolean isMultipart;
    final int bizType;
    final boolean isWebSocket;
    List<Parameter> dealtParameters = new ArrayList<>(); // 保存处理过注解的实际请求的参数
    String baseUrl;
    String cacheKey;

    private ApiMethod(Builder builder) {
        this.httpMethod = builder.httpMethod;
        this.baseUrl = builder.baseUrl;
        this.path = builder.path;
        this.headers = builder.headers;
        this.parameterHandlers = builder.parameterHandlers;
        this.contentType = builder.contentType;
        this.hasBody = builder.hasBody;
        this.isMultipart = builder.isMultipart;
        this.responseType = builder.responseType;
        this.bizType = builder.bizType;
        this.isWebSocket = builder.isWebSocket;
    }

    /**
     * 由于ApiMethod会被缓存，因此不要修改原始的parameterHandlers
     */
    void setArgs(Object[] args) {
        int argumentCount = args != null ? args.length : 0;
        int parameterCount = parameterHandlers.size();
        if (argumentCount != parameterCount) {
            throw new IllegalArgumentException("Argument count (" + argumentCount + ") doesn't match expected count (" + parameterCount + ")");
        }

        dealtParameters = new ArrayList<>();
        for (int i = 0; i < parameterCount; i++) {
            parameterHandlers.get(i).apply(this, args[i]);
        }
    }

    static final class Builder {
        private final Method method;
        private final Annotation[] methodAnnotations;
        private final Annotation[][] parameterAnnotationsArray;
        private final Type[] parameterTypes;

        private String httpMethod;
        private String baseUrl;
        private String path;
        private okhttp3.Headers headers;
        private List<ParameterHandler> parameterHandlers;
        private MediaType contentType;
        private Type responseType;
        private boolean hasBody;
        private boolean isMultipart;
        private int bizType;
        private boolean isWebSocket;
        private boolean gotUrl;

        Builder(Method method) {
            this.method = method;
            this.methodAnnotations = method.getAnnotations();
            this.parameterAnnotationsArray = method.getParameterAnnotations();
            this.parameterTypes = method.getGenericParameterTypes();
            this.parameterHandlers = new ArrayList<>();
        }

        ApiMethod build() {
            //Parse method return Type
            resolveResponseType();

            // Parse method annotations
            for (Annotation annotation : methodAnnotations) {
                parseMethodAnnotation(annotation);
            }
            if (httpMethod == null && !isWebSocket) {
                throw methodError("HTTP method annotation is required (e.g., @GET, @POST, etc.).");
            }
            if (path == null) {
                throw methodError("Missing either @%s URL", httpMethod);
            }

            //Parse parameter annotations
            for (int i = 0; i < parameterAnnotationsArray.length; i++) {
                Type parameterType = parameterTypes[i];
                if (HttpUtils.hasUnresolvableType(parameterType)) {
                    throw parameterError(i, "Parameter type must not include a type variable or wildcard: %s", parameterType);
                }
                parseParameterAnnotation(i, parameterAnnotationsArray[i]);
            }

            return new ApiMethod(this);
        }

        private void resolveResponseType() {
            if (method.getReturnType() != Call.class && method.getReturnType() != XLWsCall.class) {
                throw methodError("Service methods must return XLCall<T> or XLWsCall<T>.");
            }
            Type returnType = method.getGenericReturnType();
            if (HttpUtils.hasUnresolvableType(returnType)) {
                throw methodError("Method return type must not include a type variable or wildcard: %s", returnType);
            }
            if (returnType instanceof ParameterizedType) {
                this.responseType = HttpUtils.getParameterUpperBound(0, (ParameterizedType) returnType);
            } else {
                // If not a generic type, return String
                this.responseType = String.class;
            }
            if (responseType == null) {
                throw methodError("Resolve response type error");
            }
        }

        private void parseMethodAnnotation(Annotation annotation) {
            if (annotation instanceof GET) {
                parseHttpMethodAndPath("GET", ((GET) annotation).value(), false);
            } else if (annotation instanceof POST) {
                parseHttpMethodAndPath("POST", ((POST) annotation).value(), true);
            } else if (annotation instanceof Headers) {
                String[] headersToParse = ((Headers) annotation).value();
                if (headersToParse.length == 0) {
                    throw methodError("@Headers annotation is empty.");
                }
                headers = parseHeaders(headersToParse);
            } else if (annotation instanceof Multipart) {
                isMultipart = true;
            } else if (annotation instanceof BaseUrl) {
                String baseUrl = ((BaseUrl) annotation).value();
                if (!URLUtil.isValidUrl(baseUrl)) {
                    throw new IllegalArgumentException("Illegal URL: " + baseUrl);
                }
                this.baseUrl = baseUrl;
                gotUrl = true;
            } else if (annotation instanceof BizType) {
                this.bizType = ((BizType) annotation).value();
            } else if (annotation instanceof WebSocket) {
                this.isWebSocket = true;
                this.path = ((WebSocket) annotation).value();
            }
        }

        private void parseHttpMethodAndPath(String httpMethod, String path, boolean hasBody) {
            if (this.httpMethod != null) {
                throw methodError("Only one HTTP method is allowed. Found: %s and %s.", this.httpMethod, httpMethod);
            }
            this.httpMethod = httpMethod;
            this.hasBody = hasBody;
            this.path = path;
        }

        private okhttp3.Headers parseHeaders(String[] headers) {
            okhttp3.Headers.Builder builder = new okhttp3.Headers.Builder();
            for (String header : headers) {
                int colon = header.indexOf(':');
                if (colon == -1 || colon == 0 || colon == header.length() - 1) {
                    throw methodError("@Headers value must be in the form \"Name: Value\". Found: \"%s\"", header);
                }
                String headerName = header.substring(0, colon);
                String headerValue = header.substring(colon + 1).trim();
                if ("Content-Type".equalsIgnoreCase(headerName)) {
                    MediaType type = MediaType.parse(headerValue);
                    if (type == null) {
                        throw methodError("Malformed content type: %s", headerValue);
                    }
                    contentType = type;
                } else {
                    builder.add(headerName, headerValue);
                }
            }
            return builder.build();
        }

        private void parseParameterAnnotation(int index, Annotation[] parameterAnnotations) {
            boolean added = false;
            for (Annotation annotation : parameterAnnotations) {
                if (annotation instanceof Param) {
                    Param paramAnnotation = (Param) annotation;
                    if (TextUtils.isEmpty(paramAnnotation.value())) {
                        throw parameterError(index, "@Param annotation must supply a name");
                    }
                    parameterHandlers.add(new ParameterHandler.Param(paramAnnotation.value()));
                    added = true;
                } else if (annotation instanceof ParamList) {
                    ParamList paramAnnotation = (ParamList) annotation;
                    if (TextUtils.isEmpty(paramAnnotation.value())) {
                        throw parameterError(index, "@ParamList annotation must supply a name");
                    }
                    parameterHandlers.add(new ParameterHandler.ParamList(paramAnnotation.value()));
                    added = true;
                } else if (annotation instanceof Host) {
                    if (gotUrl) {
                        throw new IllegalArgumentException("@Host parameters may not be used with @BaseUrl.");
                    }
                    parameterHandlers.add(new ParameterHandler.Host());
                    added = true;
                } else if (annotation instanceof Cache) {
                    parameterHandlers.add(new ParameterHandler.Cache());
                    added = true;
                }
            }
            // Deal non-annotation parameter
            if (!added) {
                throw parameterError(index, "Parameter must use Annotations.");
            }
        }

        private RuntimeException methodError(String message, Object... args) {
            message = String.format(message, args);
            return new IllegalArgumentException(message + "\n for method " + method.getDeclaringClass().getSimpleName() + "." + method.getName(), null);
        }

        private RuntimeException parameterError(int p, String message, Object... args) {
            return methodError(message + " (parameter #" + (p + 1) + ")", args);
        }
    }

}
