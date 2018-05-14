package core.android.xuele.net.crhlibcore.http;

/**
 * Request parameter
 * Created by KasoGG on 2017/1/11.
 */
public class Parameter {
    private String key;
    private Object value;
    private boolean needSign;

    public Parameter(String key, Object value) {
        this.key = key;
        this.value = value;
        this.needSign = true;
    }

    public Parameter(String key, Object value, boolean needSign) {
        this.key = key;
        this.value = value;
        this.needSign = needSign;
    }

    String getKey() {
        return key;
    }

    Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    boolean isNeedSign() {
        return needSign;
    }
}
