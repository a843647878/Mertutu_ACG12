package com.moetutu.acg12.http.callback;

import android.app.Activity;
import android.widget.Toast;

import com.moetutu.acg12.activity.SplashActivity;
import com.moetutu.acg12.app.AppContext;
import com.moetutu.acg12.http.httpmodel.ResEntity;
import com.moetutu.acg12.interf.OnUpdateDialogNoticeListener;
import com.moetutu.acg12.presenter.UserDbPresenter;
import com.moetutu.acg12.util.AppManager;
import com.moetutu.acg12.util.LogUtils;
import com.google.gson.JsonParseException;
import com.moetutu.acg12.util.ToastUtils;

import java.net.SocketTimeoutException;
import java.util.NoSuchElementException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.adapter.rxjava.HttpException;

/**
 * @author xuanyouwu
 * @email xuanyouwu@163.com
 * @time 2016-04-20 18:38
 */
public abstract class SimpleCallBack<T> extends  BaseCallBack<ResEntity<T>> {

    public static class JSONResponseException extends RuntimeException {
        public final int code;
        public final String msg;


        public JSONResponseException(int code, String message) {
            this.code = code;
            this.msg = message;
        }
    }



    @Override
    protected void dispatchHttpSuccess(Call<ResEntity<T>> call, Response<ResEntity<T>> response) {
        if (response.body() != null && response.body().code == ResEntity.CODE_SUCESS) {
            onSuccess(call, response);
        } else {
            onFailure(call,
                    response.body() != null ?
                            new JSONResponseException(response.body().code, response.body().msg)
                            : new JSONResponseException(-1, "响应为null"));
        }
    }

    @Override
    public void onFailure(Call<ResEntity<T>> call, Throwable t) {
        super.onFailure(call, t);
        if (t instanceof JSONResponseException) {
            JSONResponseException jsonResponseException = (JSONResponseException) t;
            defNotify(jsonResponseException.msg);
            //defNotify(String.format("%s:%s", jsonResponseException.code, jsonResponseException.message));
            switch (jsonResponseException.code) {
                case ResEntity.CODE_TOKEN_INVALID:
                    try {
                        defNotify("token失效，请退出程序重新进入");
                        Activity currActivity = AppManager.getAppManager().currentActivity();
                        UserDbPresenter userDbPresenter = new UserDbPresenter(currActivity);
                        userDbPresenter.exitLoginUser();
                        userDbPresenter.close();
//                        System.exit(0);
//                        SplashActivity.launch(currActivity);
                    } catch (NoSuchElementException e) {
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case ResEntity.CODE_INVALIDAPPID:// 无效的APPID
                    try {
                        defNotify("无效的App id码,请和平胸猫联系");
                    } catch (NoSuchElementException e) {
                    }
//                    Activity topActivity = AppManager.getAppManager().currentActivity();
//                    if (topActivity instanceof OnUpdateDialogNoticeListener) {
//                        OnUpdateDialogNoticeListener updateDialogNoticeListener = (OnUpdateDialogNoticeListener) topActivity;
//                        updateDialogNoticeListener.shouldShutDown(jsonResponseException.msg);
//                    }
                    break;
                case ResEntity.CODE_INVALIDAPPSECURE://  无效的 App secure 码。
                    try {
                        defNotify("无效的App secure码,请和平胸猫联系");
                    } catch (NoSuchElementException e) {
                    }
//                    Activity currActivity = AppManager.getAppManager().currentActivity();
//                    if (currActivity instanceof OnUpdateDialogNoticeListener) {
//                        OnUpdateDialogNoticeListener updateDialogNoticeListener = (OnUpdateDialogNoticeListener) currActivity;
//                        updateDialogNoticeListener.shouldUpdate(true);
//                    }
                    break;
            }
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
        ToastUtils.showRoundRectToast(noticeStr);
    }

}
