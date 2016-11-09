package com.moetutu.acg12.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

import com.dao.userinfo.User;
import com.moetutu.acg12.R;
import com.moetutu.acg12.asynctask.type.Acg12Obj;
import com.moetutu.acg12.entity.LoginInfo;
import com.moetutu.acg12.entity.UserEntity;
import com.moetutu.acg12.http.RetrofitService;
import com.moetutu.acg12.http.callback.SimpleCallBack;
import com.moetutu.acg12.http.httpmodel.ResEntity;
import com.moetutu.acg12.presenter.UserDbPresenter;
import com.moetutu.acg12.util.LogUtils;

import cn.jpush.android.api.JPushInterface;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 启动页
 *
 * 有问题  直接加载图片的  可能会出现oom
 */
public class SplashActivity extends BaseActivity {
	private boolean kaishi = true;
	private ImageView welcomeImageView;
	private Handler handler = new Handler() {
		@SuppressWarnings("deprecation")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 101:
				welcomeImageView.setBackgroundDrawable(getResources()
						.getDrawable(R.mipmap.splash));
				animation.setDuration(2000);
				welcomeImageView.startAnimation(animation);
				break;
			}
		}
	};


	Bitmap bm;
	AlphaAnimation animation;
	private UserDbPresenter presenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_splash);
		presenter = new UserDbPresenter(this);
		JPushInterface.stopPush(getApplicationContext());
		initView(this);
	}

	@Override
	public void initView(Activity activity) {
		super.initView(activity);

		welcomeImageView = (ImageView) findViewById(R.id.welcome);
		animation = new AlphaAnimation(1.0f, 1.0f);


		initData();// 调用token登录
		
		handler.sendEmptyMessage(101);
		animation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation animation) {

				// 判断用户是否是第一次使用
				SharedPreferences sharedPreferences = SplashActivity.this
						.getSharedPreferences("shar", MODE_PRIVATE);

				boolean isFirstRun = sharedPreferences.getBoolean(
						"isFirstRun7", true);
				Editor editor = sharedPreferences.edit();
				if (kaishi) {
					if (isFirstRun) {
//						MoeTuMainActivity.launch(SplashActivity.this);
						MainActivity.launch(SplashActivity.this);
						editor.putBoolean("isFirstRun7", false);
						editor.commit();
					} else {
						MainActivity.launch(SplashActivity.this);
//						MoeTuMainActivity.launch(SplashActivity.this);
					}
				}
				finish();

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationStart(Animation animation) {
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		 JPushInterface.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
	}

	

	/**
	 * 获取版本号
	 *
	 * @return 当前应用的版本号
	 */
	private String getVersionName() throws Exception {
		// 获取packagemanager的实例
		PackageManager packageManager = getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),
				0);
		String version = packInfo.versionName;
		return version;
	}

	@Override
	public void onBackPressed() {

	}


	@Override
	public void initData() {
		super.initData();
		RetrofitService
				.getInstance()
				.getApiCacheRetryService()
				.getToken("4q6wnmmdzd","3wk9khscjfk")
				.enqueue(new SimpleCallBack<LoginInfo>() {
					@Override
					public void onSuccess(Call<ResEntity<LoginInfo>> call, Response<ResEntity<LoginInfo>> response) {
						final LoginInfo data = response.body().data;
						LogUtils.d("------------------>token="+data.token);
						if (!TextUtils.isEmpty(data.token)){
							if (presenter != null) {
								try {
									LogUtils.d("------------------>写入数据库");
									RetrofitService.getInstance().restLoginInfo(response.body().data.token,"7788");
									User user = new User();
									user.setToken(data.token);
									user.setUid("7788");
									presenter.inertOrReplace(user);
									LogUtils.d("------------------>RetrofitService.token="+RetrofitService.getInstance()
									.getToken());
								} catch (Exception e) {
									e.printStackTrace();
									LogUtils.d("-------->exe:" + e);
								}
							}
						}

					}
				});
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
