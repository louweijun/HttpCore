package core.android.xuele.net.crhlibcore.resource;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by louweijun on 2018-05-16.
 */

public class ResourceFileUtil {

    private String rootPath;

    public void init(Context context) {
        rootPath = getDiskFileDir(context);
    }

    private ResourceFileUtil() {
    }

    private static ResourceFileUtil instance = new ResourceFileUtil();

    public static ResourceFileUtil getInstance() {
        return instance;
    }

    public String getZipDir() {
        String zipPath = rootPath + File.separator + "zip";
        File file = new File(zipPath);
        file.mkdirs();
        return zipPath;
    }


    public String getJsDir() {
        String jsPath = rootPath + File.separator + "js";
        File file = new File(jsPath);
        file.mkdirs();
        return jsPath;
    }


    public String getJsDir(String path) {
        return getJsDir() + File.separator + path;
    }


    /**
     * 缓存路径设置 用户存储长时间缓存的数据<br/>
     * 应用在被用户卸载后，SDCard/Android/data/你的应用的包名/ 这个目录下的所有文件都会被删除，不会留下垃圾信息。<br/>
     * 而且上面二个目录分别对应 设置->应用->应用详情里面的”清除数据“与”清除缓存“选项
     *
     * @param context
     * @return 若sdcard未被移除则返回android/data/包名/files<br/>
     * 移除的话则返回:data/data/包名/files
     * @author LPH
     */
    static String getDiskFileDir(Context context) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||
                !Environment.isExternalStorageRemovable()) {
            //getExternalFilesDir(null) = /mnt/sdcard/Android/data/com.my.app/files
            cachePath = context.getExternalFilesDir(null).getPath();
        } else {
            //getFilesDir() = /data/data/com.my.app/files
            cachePath = context.getFilesDir().getPath();
        }
        return cachePath;
    }
}
