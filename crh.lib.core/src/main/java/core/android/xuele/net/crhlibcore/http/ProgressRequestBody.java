package core.android.xuele.net.crhlibcore.http;


import java.io.IOException;

import core.android.xuele.net.crhlibcore.http.callback.ApiProgressCallback;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * 带进度的RequestBody
 * <p>
 * Created by KasoGG on 2017/1/18.
 */
class ProgressRequestBody extends RequestBody {
    private final RequestBody requestBody;
    private final ApiProgressCallback apiCallback;
    private final ApiManager apiManager;
    private BufferedSink bufferedSink;

    ProgressRequestBody(RequestBody requestBody, ApiProgressCallback apiCallback, ApiManager apiManager) {
        this.requestBody = requestBody;
        this.apiCallback = apiCallback;
        this.apiManager = apiManager;
    }

    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return requestBody.contentLength();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        if (bufferedSink == null) {
            bufferedSink = Okio.buffer(sink(sink));
        }
        try {
            requestBody.writeTo(bufferedSink);
            bufferedSink.flush();
        } catch (IllegalStateException e) {
            HttpUtils.log(e);
        }
    }

    private Sink sink(Sink sink) {
        return new ForwardingSink(sink) {
            long bytesWritten = 0L;

            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                bytesWritten += byteCount;
                if (apiCallback != null) {
                    apiManager.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                apiCallback.onProgress(bytesWritten, requestBody.contentLength(), bytesWritten == requestBody.contentLength());
                            } catch (IOException e) {
                                HttpUtils.log(e);
                            }
                        }
                    });
                }
            }
        };
    }
}