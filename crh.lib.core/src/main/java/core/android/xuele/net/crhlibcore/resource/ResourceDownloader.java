package core.android.xuele.net.crhlibcore.resource;

import android.util.Log;

import java.util.LinkedList;

/**
 * Created by louweijun on 2018-05-15.
 * 下载服务器资源
 */
public class ResourceDownloader {

    private LinkedList<ResourceDownloadTask> tasks = new LinkedList<>();
    private ResourceDownloadTask mCurrentTask;

    public void start(ResourceDownloadTask downloadTask) {
        if (downloadTask==null){
            Log.d("Resource", "start download error because task is null");
            return;
        }
        if (checkUrl(downloadTask)) {
            Log.d("Resource", "start download error because url is exist : url---" + downloadTask.getUrl());
        }
        tasks.addLast(downloadTask);
        realStart();
    }

    private boolean checkUrl(ResourceDownloadTask downloadTask) {
        String url = downloadTask.getUrl();

        for (ResourceDownloadTask task : tasks) {
            if (url.equals(task.getUrl())) {
                return false;
            }
        }
        return true;
    }

    private void realStart() {
        if (mCurrentTask == null && tasks.size() > 0) {
            mCurrentTask = tasks.getFirst();
            tasks.removeFirst();
            mCurrentTask.start();
        }
    }


    private ResourceDownloader() {
    }

    private static ResourceDownloader instance = new ResourceDownloader();

    public static ResourceDownloader getInstance() {
        return instance;
    }
}
