package core.android.xuele.net.crhlibcore.resource;

/**
 * Created by louweijun on 2018-05-15.
 * 加载资源对外暴露接口
 * 一般不用实现此接口
 */
public interface LoadResourceListener {

    void load(String path);


    /**
     * @return true 马上开始下载，false 等待确认
     */
    boolean showDialog(ResourceManager resourceManager);

    void onProgressChange(int progress);

    void downloadSuccess();

    String loadFileUrl(String url);

}
