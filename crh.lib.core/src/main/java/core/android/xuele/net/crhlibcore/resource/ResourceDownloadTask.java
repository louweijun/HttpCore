package core.android.xuele.net.crhlibcore.resource;


import java.io.File;

import core.android.xuele.net.crhlibcore.http.ApiManager;
import core.android.xuele.net.crhlibcore.http.callback.ApiProgressCallback;
import core.android.xuele.net.crhlibcore.resource.model.RemoteVersion;

/**
 * Created by louweijun on 2018-05-16.
 */

public class ResourceDownloadTask {

    private String url;
    private String path;
    private ApiProgressCallback<File> apiProgressCallback;

    public void setApiProgressCallback(ApiProgressCallback<File> apiProgressCallback) {
        this.apiProgressCallback = apiProgressCallback;
    }

    public ResourceDownloadTask(String url, String path) {
        this.url = url;
        this.path = path;
    }

    public static ResourceDownloadTask createTask(RemoteVersion remoteVersion, String checkVersionUrl) {
        String downloadBaseUrl = checkVersionUrl.substring(0, checkVersionUrl.lastIndexOf("/"));
        String downloadLastUrl = remoteVersion.getLaster_resource_download_url().startsWith("/") ? remoteVersion.getLaster_resource_download_url() : ("/" + remoteVersion.getLaster_resource_download_url());
        String downloadUrl = downloadLastUrl.contains("?") ? (downloadLastUrl + "&resource_type=index") : (downloadLastUrl + "?resource_type=index");
        return new ResourceDownloadTask(downloadBaseUrl + downloadUrl, ResourceFileUtil.getInstance().getZipDir());
    }

    public String getUrl() {
        return url;
    }

    public void start() {
        ApiManager.ready().downloadFile(url, path, true, apiProgressCallback);
    }

}
