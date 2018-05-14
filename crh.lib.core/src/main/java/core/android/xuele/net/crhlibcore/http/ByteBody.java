package core.android.xuele.net.crhlibcore.http;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

/**
 * Byte RequestBody
 * <p>
 * Created by KasoGG on 2017/2/7.
 */
class ByteBody extends RequestBody {
    private MediaType contentType;
    private byte[] content;

    ByteBody(MediaType contentType, byte[] content) {
        this.contentType = contentType;
        this.content = content;
    }

    @Override
    public MediaType contentType() {
        return contentType;
    }

    @Override
    public long contentLength() {
        return content.length;
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        Source source = null;
        try {
            source = Okio.source(new ByteArrayInputStream(content));
            sink.writeAll(source);
        } finally {
            Util.closeQuietly(source);
        }
    }
}
