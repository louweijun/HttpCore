package core.android.xuele.net.crhlibcore.http;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;

import core.android.xuele.net.crhlibcore.http.callback.ReqCallBack;
import core.android.xuele.net.crhlibcore.http.callback.ReqCallBackV2;
import core.android.xuele.net.crhlibcore.http.callback.ReqUploadCallBack;
import core.android.xuele.net.crhlibcore.http.callback.ApiCallback;
import core.android.xuele.net.crhlibcore.http.callback.ApiProgressCallback;
import core.android.xuele.net.crhlibcore.http.exception.ApiNetworkException;


/**
 * 对学乐云的通用返回处理做了一层包装
 * 增加ReqCallBackV2处理
 * Created by KasoGG on 2017/1/19.
 */
class ApiCallV2<T> extends OKHttpCall<T> {

    ApiCallV2(ApiManager apiManager, ApiMethod apiMethod) {
        super(apiManager, apiMethod);
    }

    @Override
    public Call<T> requestV2(final ReqCallBackV2<T> callback) {
        enqueueNormal(callback);
        return this;
    }

    public Call<T> request(final ReqCallBack<T> callback) {
        if (callback instanceof ReqUploadCallBack) {
            enqueueUpload((ReqUploadCallBack<T>) callback);
        } else {
            enqueueNormal(callback);
        }
        return this;
    }

    private void enqueueNormal(final Object callback) {
        enqueue(new ApiCallback<T>() {
            @Override
            public void onSuccess(Call<T> call, HttpResponse<T> response) {
                ApiCallV2.this.onSuccess(callback, response);
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                ApiCallV2.this.onFailure(callback, t);
            }
        });
    }

    private void enqueueUpload(final ReqUploadCallBack<T> callBack) {
        enqueue(new ApiProgressCallback<T>() {
            @Override
            public void onProgress(long bytesWritten, long totalBytes, boolean hasFinished) {
                callBack.updateProgress(totalBytes, bytesWritten);
            }

            @Override
            public void onSuccess(Call<T> call, HttpResponse<T> response) {
                ApiCallV2.this.onSuccess(callBack, response);
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                ApiCallV2.this.onFailure(callBack, t);
            }
        });
    }

    private void onSuccess(Object callback, HttpResponse<T> response) {

        if (callback == null) {
            return;
        }
        T finalResult = response.body();
        if (finalResult == null) {
            HttpResult result = jsonToObject(response.string(), HttpResult.class);
            onReqFailed(callback, "", ReqCallBackV2.CODE_NON_NETWORK_ERROR);
            return;
        }
        try {
            if (finalResult instanceof HttpResult) {
                HttpResult reResult = (HttpResult) finalResult;

                String state = reResult.getError_no();
                if (!TextUtils.isEmpty(state) && state.equals("0")) {
                    onReqSuccess(callback, finalResult);
                } else {
                    onReqFailed(callback, reResult.getError_info(), reResult.getError_no());
                }
            } else {
                onReqSuccess(callback, finalResult);
            }
        } catch (Exception e) {
            HttpUtils.log(e);
            onReqFailed(callback, "", ReqCallBackV2.CODE_NON_NETWORK_ERROR);
        }
    }

    private void onFailure(Object callback, Throwable t) {

        if (callback == null) {
            return;
        }
        onReqFailed(callback, "", t instanceof ApiNetworkException ? ReqCallBackV2.CODE_NETWORK_ERROR : ReqCallBackV2.CODE_NON_NETWORK_ERROR);
    }

    @SuppressWarnings("unchecked")
    private void onReqSuccess(Object callback, T result) {
        if (callback instanceof ReqCallBackV2) {
            ((ReqCallBackV2<T>) callback).onReqSuccess(result);
        } else if (callback instanceof ReqCallBack) {
            ((ReqCallBack<T>) callback).onReqSuccess(result);
        }
    }

    private void onReqFailed(Object callback, String errorMsg, String errorCode) {
        try {
            if (callback instanceof ReqCallBackV2) {
                ((ReqCallBackV2) callback).onReqFailed(errorMsg, errorCode);
            } else if (callback instanceof ReqCallBack) {
                ((ReqCallBack) callback).onReqFailed(errorMsg);
            }
        } catch (Exception e) {
            HttpUtils.log(e);
        }
    }


    /**
     * json转化为对象
     *
     * @param json  字符串
     * @param clazz 需要转化的对象类型
     * @return 对象
     */
    public static <T> T jsonToObject(String json, Class<T> clazz) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        try {
            return JSON.parseObject(json, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
