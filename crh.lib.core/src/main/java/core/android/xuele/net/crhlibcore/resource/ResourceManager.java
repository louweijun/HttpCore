package core.android.xuele.net.crhlibcore.resource;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import core.android.xuele.net.crhlibcore.http.Call;
import core.android.xuele.net.crhlibcore.http.HttpResponse;
import core.android.xuele.net.crhlibcore.http.callback.ApiProgressDoCallback;
import core.android.xuele.net.crhlibcore.http.callback.ReqCallBack;
import core.android.xuele.net.crhlibcore.resource.model.RemoteVersion;
import core.android.xuele.net.crhlibcore.uti.LogUtil;
import core.android.xuele.net.crhlibcore.uti.SPUtil;

/**
 * Created by louweijun on 2018-05-15.
 */

public class ResourceManager implements ApiProgressDoCallback<File> {
    public static final int CLIENT_ANDROID = 1;
    public static final String NEED_UPDATE = "1";
    public static final String URL_TAG = "url";

    private Context context;
    private LoadResourceListener resourceListener;
    private ResourceVersion localVersion;
    private RemoteVersion mRemoteVersion;
    //加载的JS文件的相对路径
    private Resource sdcardResource;
    private Resource assistResource;

    public Context getContext() {
        return context;
    }

    public ResourceManager(Context context, ResourceVersion localVersion, Resource sdcardResource, Resource assistResource) {
        this.context = context;
        ResourceFileUtil.getInstance().init(context);
        this.localVersion = localVersion;
        this.sdcardResource = sdcardResource;
        this.assistResource = assistResource;
    }


    public static ResourceManager createResourceManager(Context context, String url, String relativePath) {
        return new ResourceManager(context, new ResourceVersion(context, url), new SdcardResource(relativePath), new AssetsResource(relativePath));
    }

    public static ResourceManager createResourceManager(Context context, String url, String sdcardRelativePath, String assetsRelativePath) {
        return new ResourceManager(context, new ResourceVersion(context, url), new SdcardResource(sdcardRelativePath), new AssetsResource(assetsRelativePath));
    }


    public void setResourceListener(LoadResourceListener resourceListener) {
        this.resourceListener = resourceListener;
    }

    public void start() {
        //step 1 获取本地资源文件主页 并通知页面加载
        localResourceLoad();
        //step 2 检查资源文件版本
        checkResourceVersion();
    }

    public void localResourceLoad() {
        Resource resource;
        if (checkSDCardResource()) {
            //本地有文件
            resource = sdcardResource;
        } else {
            resource = assistResource;
        }
        load(localUrl(resource.getIndexPath()));
    }

    private String localUrl(String indexPath) {
        String fileUrl = "file:///" + indexPath + "?" + URL_TAG + "=" + getLoadJsUrl() + "&version=" + new Date().getTime();
        //todo
        if (resourceListener != null) {
            fileUrl = resourceListener.loadFileUrl(fileUrl);
        }
        return fileUrl;
    }

    private void checkResourceVersion() {
        //http
        ResourceApi.ready.checkVersion(
                localVersion.getUrl(),
                localVersion.getNowVersion(),
                CLIENT_ANDROID,
                localVersion.getAppVersionCode()).request(new ReqCallBack<RemoteVersion>() {
            @Override
            public void onReqSuccess(RemoteVersion remoteVersion) {
                mRemoteVersion = remoteVersion;
                //判断是否需要更新
                if (isNeedUpdate()) {
                    if (showAskDialog()) {
                        startDownload();
                    }
                }
            }

            @Override
            public void onReqFailed(String s) {

            }
        });
    }


    public void startDownload() {
        ResourceDownloadTask downloadTask = ResourceDownloadTask.createTask(mRemoteVersion, localVersion.getUrl());
        downloadTask.setApiProgressCallback(this);
        ResourceDownloader.getInstance().start(downloadTask);
    }

    private boolean showAskDialog() {
        return resourceListener == null || resourceListener.showDialog(this);
    }


    private boolean isNeedUpdate() {
        return TextUtils.equals(mRemoteVersion.getNeed_update_resource(), NEED_UPDATE);
    }


    private void load(String indexPath) {
        if (resourceListener != null) {
            resourceListener.load(indexPath);
        }
    }


    private boolean checkSDCardResource() {
        return sdcardResource.isExist();
    }


    @Override
    public void onProgress(long l, long l1, boolean b) {
        if (resourceListener != null) {
            resourceListener.onProgressChange((int) (l / l1 * 100));
        }
    }

    @Override
    public void onSuccess(Call<File> call, HttpResponse<File> httpResponse) {
        if (resourceListener != null) {
            resourceListener.downloadSuccess();
        }
        //保存版本号
        SPUtil.put(context, ResourceVersion.VERSION_KEY, mRemoteVersion.getLaster_resource_code());
        //下载资源成功操作,加载本地资源
        localResourceLoad();
    }

    @Override
    public void onFailure(Call<File> call, Throwable throwable) {
        Toast.makeText(context, "下载更新包错误，请退出重新进入！", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onSuccessDoInBackground(Call<File> call, File file) {
        try {
            upZipFile(file, ResourceFileUtil.getInstance().getJsDir());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解压缩文件到制定目录下
     *
     * @param zipFile
     * @param desFileDir
     * @author LPH
     */
    private static void upZipFile(File zipFile, String desFileDir) throws IOException {
        ZipFile zip = new ZipFile(zipFile);
        Enumeration entries = zip.entries();
        byte[] buf = new byte[1024];
        desFileDir = desFileDir + File.separator;
        while (entries.hasMoreElements()) {
            ZipEntry ze = (ZipEntry) entries.nextElement();
            if (ze.isDirectory()) {
                String dirstr = desFileDir + ze.getName();
                dirstr = new String(dirstr.getBytes("ISO-8859-1"), "utf-8");
                File f = new File(dirstr);
                f.mkdir();
            } else {
                OutputStream os = new BufferedOutputStream(new FileOutputStream(getRealFileName(desFileDir, ze.getName())));
                InputStream is = new BufferedInputStream(zip.getInputStream(ze));
                int readLen = 0;
                while ((readLen = is.read(buf, 0, 1024)) != -1) {
                    os.write(buf, 0, readLen);
                }
                is.close();
                os.close();
            }
        }
        zip.close();
    }


    /**
     * 给定根目录，返回一个相对路径所对应的实际文件名.
     *
     * @param desFilePath 指定根目录
     * @param absFileName 相对路径名，来自于ZipEntry中的name
     * @return
     * @author LPH
     */
    private static File getRealFileName(String desFilePath, String absFileName) {
        String[] dirs = absFileName.split("/");
        String lastDir = desFilePath;
        if (dirs.length > 1) {
            for (int i = 0; i < dirs.length - 1; i++) {
                lastDir += (dirs[i] + "/");
                File dir = new File(lastDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                    LogUtil.i("create dir = " + (lastDir + "/" + dirs[i]));
                }
            }
            File ret = new File(lastDir, dirs[dirs.length - 1]);
            LogUtil.i("ret = " + ret);
            return ret;
        } else {
            return new File(desFilePath, absFileName);
        }
    }

    private String getLoadJsUrl() {
        String url = localVersion.getUrl();
        try {
            URL u = new URL(url);
            if (u.getPort() == -1) {
                return u.getProtocol() + "://" + u.getHost() + "/";
            }
            return u.getProtocol() + "://" + u.getHost() + ":" + u.getPort() + "/";

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void release() {
        //todo
    }
}
