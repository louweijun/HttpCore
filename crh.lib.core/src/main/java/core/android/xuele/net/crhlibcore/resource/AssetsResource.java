package core.android.xuele.net.crhlibcore.resource;

import java.io.File;

/**
 * Created by louweijun on 2018-05-16.
 */

public class AssetsResource implements Resource {

    private String relativePath;

    public AssetsResource(String relativePath) {
        this.relativePath = relativePath;
    }

    @Override
    public String getIndexPath() {
        return "android_asset" + File.separator + relativePath + File.separator + "index.html";
    }

    @Override
    public boolean isExist() {
        return true;
    }

    @Override
    public String getRelativePath() {
        return relativePath;
    }

    @Override
    public String getAbsolutePath() {
        return relativePath;
    }
}
