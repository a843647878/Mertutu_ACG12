package com.moetutu.acg12.http.callback;

import android.widget.Toast;

import com.moetutu.acg12.app.AppContext;
import com.moetutu.acg12.util.LogUtils;
import com.google.gson.JsonParseException;

import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.adapter.rxjava.HttpException;

/**
 * @author xuanyouwu
 * @email xuanyouwu@163.com
 * @time 2016-04-20 18:38
 */
public abstract class SimpleCallBack<T> extends BaseCallBack<T> {

    public static class JSONResponseException extends RuntimeException {
        public final int code;
        public final String message;


        public JSONResponseException(int code, String message) {
            this.code = code;
            this.message = message;
        }
    }



    @Override
    protected void dispatchHttpSuccess(Call<T> call, Response<T> response) {
        if (response.body() != null) {
            onSuccess(call, response);
        } else {
            onFailure(call,new JSONResponseException(-1, "响应为null"));
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        super.onFailure(call, t);
        if (t instanceof JSONResponseException) {
            JSONResponseException jsonResponseException = (JSONResponseException) t;
            defNotify(String.format("%s:%s", jsonResponseException.code, jsonResponseException.message));
            //验证失效 跳转到登录页
//            if (jsonResponseException.code == ResEntity.CODE_TOKEN_INVALID) {
//                LoginActivity.launchLogin(AppManager.getAppManager().currentActivity());
//            }
        } else if (t instanceof HttpException) {
            defNotify(String.format("%s:%s", ((HttpException) t).code(), ((HttpException) t).message()));
        } else if (t instanceof JsonParseException) {
            defNotify("解析异常");
        } else if (t instanceof java.net.UnknownHostException) {
            defNotify("网络未连接");
        } else if (t instanceof SocketTimeoutException) {
            defNotify("服务器响应超时");
        } else {
            defNotify("未知异常");
        }
        LogUtils.d("http", "------->throwable:" + t);
    }


    public void defNotify(String noticeStr) {
        Toast.makeText(AppContext.getApplication(), noticeStr, Toast.LENGTH_SHORT).show();
    }

}
