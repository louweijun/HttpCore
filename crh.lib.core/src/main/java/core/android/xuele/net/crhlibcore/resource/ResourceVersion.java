package core.android.xuele.net.crhlibcore.resource;

import android.content.Context;
import android.text.TextUtils;

import core.android.xuele.net.crhlibcore.uti.DeviceUtil;
import core.android.xuele.net.crhlibcore.uti.SPUtil;
import core.android.xuele.net.crhlibcore.xml.XmlDocument;
import core.android.xuele.net.crhlibcore.xml.XmlResolve;

/**
 * Created by louweijun on 2018-05-06.
 * 本地资源版本信息
 */
public class ResourceVersion {
    public static final String VERSION_KEY = "VERSION_KEY";
    private String nowVersion;

    private String appVersionCode;

    private String url;

    public ResourceVersion(Context context, String nowVersion, String url) {
        this.nowVersion = nowVersion;
        this.url = url;
        this.appVersionCode = String.valueOf(DeviceUtil.getVersionCode(context));
    }

    public ResourceVersion(Context context, String url) {
        this.url = url;
        this.appVersionCode = String.valueOf(DeviceUtil.getVersionCode(context));
        //获取Sp中个版本信息
        nowVersion = SPUtil.get(context, VERSION_KEY, "");
        if (TextUtils.isEmpty(nowVersion)) {
            nowVersion = findFromXml(context);
        }
    }

    private String findFromXml(Context context) {
        XmlDocument document = XmlResolve.readXmlAssets(context, "kysec-statics/config.xml");
        if (document == null) return "0";

        return document.getFirstXmlTag("Version").getFisrtXmlTag("index_init_resource_version").getValue();
    }

    public String getCheckUrl() {
        String params = "resource_code=" + nowVersion + "&client_id=1&version_code=" + appVersionCode;
        String split = url.contains("?") ? "&" : "?";
        return url + split + params;
    }

    public String getNowVersion() {
        return nowVersion;
    }

    public String getAppVersionCode() {
        return appVersionCode;
    }

    public String getUrl() {
        return url;
    }
}
