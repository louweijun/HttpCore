package core.android.xuele.net.crhlibcore.resource.model;

import core.android.xuele.net.crhlibcore.http.HttpResult;

/**
 * Created by louweijun on 2018-05-15.
 */

public class RemoteVersion extends HttpResult {

    private String need_update_resource;
    private String laster_resource_download_url;
    private String laster_resource_name;
    private String laster_resource_code;
    private String errorNo;


    @Override
    public String getError_no() {
        return errorNo;
    }

    public String getErrorNo() {
        return errorNo;
    }

    public void setErrorNo(String errorNo) {
        this.errorNo = errorNo;
    }

    public String getNeed_update_resource() {
        return need_update_resource;
    }

    public void setNeed_update_resource(String need_update_resource) {
        this.need_update_resource = need_update_resource;
    }

    public String getLaster_resource_download_url() {
        return laster_resource_download_url;
    }

    public void setLaster_resource_download_url(String laster_resource_download_url) {
        this.laster_resource_download_url = laster_resource_download_url;
    }

    public String getLaster_resource_name() {
        return laster_resource_name;
    }

    public void setLaster_resource_name(String laster_resource_name) {
        this.laster_resource_name = laster_resource_name;
    }

    public String getLaster_resource_code() {
        return laster_resource_code;
    }

    public void setLaster_resource_code(String laster_resource_code) {
        this.laster_resource_code = laster_resource_code;
    }
}
