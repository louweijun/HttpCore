package core.android.xuele.net.crhlibcore.http;

import android.webkit.URLUtil;

import java.util.List;

/**
 * 保存ApiMethod处理前的参数
 */
abstract class ParameterHandler {
    protected String key;

    abstract void apply(ApiMethod apiMethod, Object arg);

    static final class Param extends ParameterHandler {
        public Param(String key) {
            this.key = key;
        }

        @Override
        void apply(ApiMethod apiMethod, Object arg) {
            if (arg == null) {
                return;
            }
            //File和byte[]类型的参数不需要签名
            Parameter parameter = new Parameter(key, arg, false);
            apiMethod.dealtParameters.add(parameter);
        }
    }

    static final class ParamList extends ParameterHandler {
        ParamList(String key) {
            this.key = key;
        }

        @Override
        void apply(ApiMethod apiMethod, Object arg) {
            if (arg == null) {
                return;
            }
            if (!(arg instanceof List)) {
                throw new IllegalArgumentException("@ParamList parameter type must be List");
            }
            List list = (List) arg;
            if (list.size() == 0) {
                return;
            }
            for (Object object : list) {
                Parameter param = new Parameter(key, object, false);
                apiMethod.dealtParameters.add(param);
            }
        }
    }

    static final class Host extends ParameterHandler {

        @Override
        void apply(ApiMethod apiMethod, Object arg) {
            if (arg == null || !(arg instanceof String) || !URLUtil.isValidUrl((String) arg)) {
                throw new IllegalArgumentException("Illegal URL: " + arg);
            }
            apiMethod.baseUrl = (String) arg;
        }
    }

    static final class Cache extends ParameterHandler {

        @Override
        void apply(ApiMethod apiMethod, Object arg) {
            if (arg == null) {
                apiMethod.cacheKey = null;
            } else if (!(arg instanceof String)) {
                throw new IllegalArgumentException("@Cache parameter must be a String： " + arg);
            } else {
                apiMethod.cacheKey = (String) arg;
            }
        }
    }
}
