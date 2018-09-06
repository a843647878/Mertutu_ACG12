package com.moetutu.acg12.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

import com.moetutu.acg12.R;
import com.moetutu.acg12.entity.LoginInfo;
import com.moetutu.acg12.entity.UserEntity;
import com.moetutu.acg12.http.RetrofitService;
import com.moetutu.acg12.http.callback.SimpleCallBack;
import com.moetutu.acg12.http.httpmodel.ResEntity;
import com.moetutu.acg12.presenter.UserDbPresenter;
import com.moetutu.acg12.util.LogUtils;
import com.moetutu.acg12.util.SpUtils;
import com.moetutu.acg12.util.T;
import com.moetutu.acg12.view.SplashView;

import cn.jpush.android.api.JPushInterface;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 启动页
 * <p>
 * 有问题  直接加载图片的  可能会出现oom
 */
public class SplashActivity extends BaseActivity {

    private UserEntity user;
    private UserEntity data;
    private static final int DELAY_TIME = 2_500;
    private Handler handler = new Handler();

    public static void launch(Context context) {
        Intent intent = new Intent(context, SplashActivity.class);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    private UserDbPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        presenter = new UserDbPresenter(this);
        try {
            user = presenter.getLoginUser();
            LogUtils.d("---------->list"+presenter.loadAll().size());
        } catch (Exception e) {
        }
        if (user == null) {
            dispatchPage(null);
        } else {
            if (TextUtils.isEmpty(user.token)){
                dispatchPage(null);
            }else {
                dispatchPage(user);
            }

        }
    }

    @Override
    public void initView(Activity activity) {
        super.initView(activity);

    }



    @Override
    public void initData() {
        super.initData();
        RetrofitService
                .getInstance()
                .getApiCacheRetryService()
                .getToken("4q6wnmmdzd", "3wk9khscjfk")
                .enqueue(new SimpleCallBack<UserEntity>() {
                    @Override
                    public void onSuccess(Call<ResEntity<UserEntity>> call, Response<ResEntity<UserEntity>> response) {
                        data = response.body().data;
                        if (!TextUtils.isEmpty(data.token)) {
                            if (presenter != null) {
                                try {
                                    user = new UserEntity();
                                    user.setToken(data.token);
                                    user.setID((long)7788);
                                    RetrofitService.getInstance().restLoginInfo(response.body().data.token, 7788);
                                    presenter.inertOrReplace(user);
                                    LogUtils.d("------------------>写入数据库");
                                    LogUtils.d("------------------>RetrofitService.token=" + RetrofitService.getInstance()
                                            .getToken());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    LogUtils.d("-------->exe:" + e);
                                }
                            }
                        }
                        dispatchPage(user);
                    }
                });
    }


    private Runnable flashTask = new Runnable() {
        @Override
        public void run() {
            MainActivity.launch(SplashActivity.this);
            finish();
        }
    };

    /**
     * @param user 为null 表示未登陆
     */
    private void dispatchPage(@Nullable UserEntity user) {
        if (user == null) {//未登录
            initData();
        } else {//已经登陆
            if (TextUtils.isEmpty(user.token)){
                initData();
            }else {
                // call after setContentView(R.layout.activity_sample);
//                SplashView.showSplashView(this, 3, R.mipmap.splash, new SplashView.OnSplashViewActionListener() {
//                    @Override
//                    public void onSplashImageClick(final String actionUrl) {
//                        handler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                LogUtils.d("----------------->广告");
//                                T.showShort("点击广告进入的位置"+actionUrl);
//                            }
//                        });
//
//                    }
//
//                    @Override
//                    public void onSplashViewDismiss(final boolean initiativeDismiss) {
//                        handler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                LogUtils.d("----------------->关闭");
//                                T.showShort("关闭"+initiativeDismiss);
//                                if (initiativeDismiss){
//
//                                }
//                                MainActivity.launch(SplashActivity.this);
//                                finish();
//                            }
//                        });
//
//                    }
//
//                });

                // call this method anywhere to update splash view data
//                SplashView.updateSplashData(this, "https://static.acg12.com/uploads/2017/04/d24a509a57e82b556eaa7de868f8f38c.jpg", "https://static.acg12.com");
                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(flashTask, DELAY_TIME);
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            try {
                presenter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
