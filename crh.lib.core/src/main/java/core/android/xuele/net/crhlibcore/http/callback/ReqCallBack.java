package core.android.xuele.net.crhlibcore.http.callback;

/**
 * @Description:请求响应
 * @Copyright:Copyright (c) 2014
 * @Company: 杭州博世数据网络科技有限公司
 * @File: ReqCallBack.java
 * @Author: 李超军
 * @Create ：2015-6-7
 * @Version: 1.0.0
 * @Others:
 */

public interface ReqCallBack<T> {
	/**
	 * 响应成功
	 */
	 void onReqSuccess(T result);

	/**
	 * 响应失败
	 */
	 void onReqFailed(String errorMsg);
}
