package core.android.xuele.net.crhlibcore.http;

import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import core.android.xuele.net.crhlibcore.http.callback.ApiProgressCallback;
import core.android.xuele.net.crhlibcore.http.callback.ApiProgressDoCallback;
import core.android.xuele.net.crhlibcore.http.callback.ReqCallBack;
import core.android.xuele.net.crhlibcore.http.callback.ReqCallBackV2;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Generate OKHttp Download Call
 * <p>
 * Created by KasoGG on 2017/2/8.
 */
class DownloadCall extends OKHttpCall<File> {
    private final ApiManager apiManager;
    private final String url;
    private final String savePath;
    private final boolean renameByUrl; //是否根据url重新命名下载后的文件

    DownloadCall(ApiManager apiManager, String url, String savePath, boolean renameByUrl) {
        super(apiManager, null);
        this.apiManager = apiManager;
        this.url = url;
        this.savePath = savePath;
        this.renameByUrl = renameByUrl;
    }

    @Override
    public Call<File> request(ReqCallBack<File> callback) {
        throw new UnsupportedOperationException("Use enqueue() instead.");
    }

    @Override
    public Call<File> requestV2(ReqCallBackV2<File> callback) {
        throw new UnsupportedOperationException("Use enqueue() instead.");
    }


    protected okhttp3.Call createRawCall() {
        final Request request = new Request.Builder().url(url).build();
        final okhttp3.Call call = apiManager.downloadClient.newCall(request);
        if (call == null) {
            throw new NullPointerException("Call returned null.");
        }
        HttpUtils.log("DOWNLOAD_START-----> " + url);
        return call;
    }

    @Override
    protected HttpResponse<File> parseResponse(Response rawResponse) throws IOException {
        Map<String, String> headers = getResponseHeaders(rawResponse);
        int code = rawResponse.code();
        File file = saveFile(rawResponse, this.savePath);
        if (file == null) {
            throw new NullPointerException("File returned null");
        }
        if (callback instanceof ApiProgressDoCallback) {
            ((ApiProgressDoCallback<File>) callback).onSuccessDoInBackground(this, file);
        }
        HttpUtils.log("DOWNLOAD_FINISH----> " + file.getPath());
        return new HttpResponse<>(rawResponse, code, null, null, headers, file);
    }

    private File saveFile(Response response, String filePath) throws IOException {
        final long totalBytes = response.body().contentLength();

        File file = new File(filePath);

        if (renameByUrl) {
            file = getRenamedFile(file, response.request().url().toString());
        }

        if (file.exists() && file.length() == totalBytes) {
            response.body().close();
            return file;
        }

        InputStream is = null;
        FileOutputStream fos = null;
        byte[] buffer = new byte[4096];
        long bytesWritten = 0;
        int len;
        try {
            is = response.body().byteStream();
            fos = new FileOutputStream(file);
            while ((len = is.read(buffer)) != -1) {
                bytesWritten += len;
                fos.write(buffer, 0, len);
                final long totalBytesWritten = bytesWritten;
                if (callback != null && callback instanceof ApiProgressCallback) {
                    apiManager.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((ApiProgressCallback) callback).onProgress(totalBytesWritten, totalBytes, totalBytesWritten == totalBytes);
                        }
                    });
                }
            }
            fos.flush();
            return file;
        } finally {
            try {
                response.body().close();
                if (is != null) {
                    is.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e) {
                HttpUtils.log(e);
            }
        }
    }

    private File getRenamedFile(File oldFile, String url) {
        String newFileName = getFileFullNameByUrl(url);
        if (oldFile != null && !TextUtils.isEmpty(newFileName)) {
            return new File(oldFile.getParent(), newFileName);
        } else {
            return oldFile;
        }
    }

    private String getFileFullNameByUrl(String url) {
        String name = null;
        if (url != null) {
            int start = url.lastIndexOf("/");
            int end = url.lastIndexOf("?");
            name = url.substring(start == -1 ? 0 : start + 1, end == -1 ? url.length() : end);
        }
        return name;
    }

}
