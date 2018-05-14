package core.android.xuele.net.crhlibcore.http;


import java.io.IOException;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

import core.android.xuele.net.crhlibcore.uti.LogManager;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Http工具类
 */
public class HttpUtils {
    private static final String LOG_TAG = "API";
    final static Charset UTF_8 = Charset.forName("UTF-8");

    static <T> void validateApiInterface(Class<T> api) {
        if (!api.isInterface()) {
            throw new IllegalArgumentException("API declarations must be interfaces.");
        }
        if (api.getInterfaces().length > 0) {
            throw new IllegalArgumentException("API interfaces must not extend other interfaces.");
        }
    }

    static <T> T checkNotNull(T object, String message) {
        if (object == null) {
            throw new NullPointerException(message);
        }
        return object;
    }

    static boolean checkParameter(Parameter parameter) {
        return parameter != null && parameter.getValue() != null;
    }

    static Type getParameterUpperBound(int index, ParameterizedType type) {
        Type[] types = type.getActualTypeArguments();
        if (index < 0 || index >= types.length) {
            throw new IllegalArgumentException("Index " + index + " not in range [0," + types.length + ") for " + type);
        }
        Type paramType = types[index];
        if (paramType instanceof WildcardType) {
            return ((WildcardType) paramType).getUpperBounds()[0];
        }
        return paramType;
    }

    static boolean hasUnresolvableType(Type type) {
        if (type instanceof Class<?>) {
            return false;
        }
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            for (Type typeArgument : parameterizedType.getActualTypeArguments()) {
                if (hasUnresolvableType(typeArgument)) {
                    return true;
                }
            }
            return false;
        }
        if (type instanceof GenericArrayType) {
            return hasUnresolvableType(((GenericArrayType) type).getGenericComponentType());
        }
        if (type instanceof TypeVariable) {
            return true;
        }
        if (type instanceof WildcardType) {
            return true;
        }
        String className = type == null ? "null" : type.getClass().getName();
        throw new IllegalArgumentException("Expected a Class, ParameterizedType, or " + "GenericArrayType, but <" + type + "> is of type " + className);
    }

    public static String getResponseString(ResponseBody responseBody) {
        BufferedSource source = responseBody.source();
        try {
            source.request(Long.MAX_VALUE); // Buffer the entire body.
        } catch (IOException e) {
            log(e);
            return "";
        }
        Buffer buffer = source.buffer();
        Charset charset = UTF_8;
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
            try {
                charset = contentType.charset(UTF_8);
            } catch (UnsupportedCharsetException e) {
                HttpUtils.log("Couldn't decode the response body; charset is likely malformed.", e);
                return "";
            }
        }
        String result = "";
        long contentLength = responseBody.contentLength();
        if (contentLength != 0) {
            result = buffer.clone().readString(charset);
        }
        return result;
    }


    /**
     * OKHTTP不支持中文Header，过滤中文Header
     */
    public static String filterChineseHeader(String header) {
        StringBuilder sb = new StringBuilder();
        try {
            for (int i = 0, length = header.length(); i < length; i++) {
                char c = header.charAt(i);
                if (c <= '\u001f' || c >= '\u007f') {
                    sb.append(String.format("\\u%04x", (int) c));
                } else {
                    sb.append(c);
                }
            }
            return sb.toString();
        } catch (Exception e) {
            log(e);
            return "";
        }
    }

    public static void log(String msg) {
        LogManager.e(LOG_TAG, msg);
    }

    public static void log(Throwable t) {
        LogManager.e(LOG_TAG, t);
    }

    public static void log(String msg, Throwable t) {
        LogManager.e(LOG_TAG, msg, t);
    }
}
