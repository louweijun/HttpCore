package core.android.xuele.net.crhlibcore.resource;


import core.android.xuele.net.crhlibcore.http.ApiManager;
import core.android.xuele.net.crhlibcore.http.Call;
import core.android.xuele.net.crhlibcore.http.annotation.Host;
import core.android.xuele.net.crhlibcore.http.annotation.POST;
import core.android.xuele.net.crhlibcore.http.annotation.Param;
import core.android.xuele.net.crhlibcore.resource.model.RemoteVersion;

/**
 * Created by louweijun on 2018-05-15.
 */

public interface ResourceApi {

    ResourceApi ready = ApiManager.ready().getApi(ResourceApi.class);


    @POST
    Call<RemoteVersion> checkVersion(@Host String url,
                                     @Param("resource_code") String currentVersion,
                                     @Param("client_id") int clientId,
                                     @Param("version_code") String versionCode);


}
