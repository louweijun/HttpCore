package core.android.xuele.net.crhlibcore.resource;

import java.io.File;

/**
 * Created by louweijun on 2018-05-16.
 */

public class SdcardResource implements Resource {

    private String relativePath;

    public SdcardResource(String relativePath) {
        this.relativePath = relativePath;
    }

    @Override
    public String getIndexPath() {
        return ResourceFileUtil.getInstance().getJsDir(relativePath + File.separator + "index.html");
    }

    @Override
    public boolean isExist() {
        return new File(getAbsolutePath()).exists();
    }

    @Override
    public String getRelativePath() {
        return relativePath;
    }

    @Override
    public String getAbsolutePath() {
        return ResourceFileUtil.getInstance().getJsDir(relativePath);
    }
}
