package com.moetutu.acg12.app;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.multidex.MultiDexApplication;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadHelper;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import com.moetutu.acg12.BuildConfig;
import com.moetutu.acg12.R;
import com.moetutu.acg12.presenter.UserDbPresenter;
import com.moetutu.acg12.util.AppManager;
import com.moetutu.acg12.util.LogUtils;
import com.moetutu.acg12.util.logger.AndroidLogAdapter;
import com.moetutu.acg12.util.logger.LogLevel;
import com.moetutu.acg12.util.logger.Logger;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import java.net.Proxy;
import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;
import okhttp3.OkHttpClient;


/**
 * 全局应用程序类：用于保存和调用全局应用配置及访问网络数据
 */
public class AppContext extends MultiDexApplication {//com.zztzt.android.simple.app.MainApplication {
	/**
	 * 全局Context，原理是因为Application类是应用最先运行的，所以在我们的代码调用时，该值已经被赋值过了
	 */
	private static AppContext mInstance;


	{// 友盟分享初始化
		PlatformConfig.setQQZone("1106093851", "xzMIm9suRXPC4vj2");
	}

	//static 代码段可以防止内存泄露
	static {
		//设置全局的Header构建器
		SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
			@Override
			public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
//				layout.setPrimaryColorsId(R.color.acg_fen2, android.R.color.white);//全局设置主题颜色
				return new MaterialHeader(context).setShowBezierWave(false);
			}
		});
		//设置全局的Footer构建器
		SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
			@Override
			public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
				//指定为经典Footer，默认是 BallPulseFooter
				return new ClassicsFooter(context).setDrawableSize(20);
			}
		});
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		UMShareAPI.get(this);
		JPushInterface.init(this);
		JPushInterface.setDebugMode(true);        // 设置开启日志,发布时请关闭日志
		mInstance = this;

		initLogger();


		this.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
			@Override
			public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
				AppManager.getAppManager().addActivity(activity);
				LogUtils.d("===========>onActivityCreated:" + activity + " savedInstanceState:" + savedInstanceState);
			}

			@Override
			public void onActivityStarted(Activity activity) {
				LogUtils.d("===========>onActivityStarted:" + activity);
			}

			@Override
			public void onActivityResumed(Activity activity) {
				LogUtils.d("===========>onActivityResumed:" + activity);
			}

			@Override
			public void onActivityPaused(Activity activity) {
				LogUtils.d("===========>onActivityPaused:" + activity);
			}

			@Override
			public void onActivityStopped(Activity activity) {
				LogUtils.d("===========>onActivityStopped:" + activity);
			}

			@Override
			public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
				LogUtils.d("===========>onActivitySaveInstanceState:" + activity);
			}

			@Override
			public void onActivityDestroyed(Activity activity) {
				AppManager.getAppManager().removeActivity(activity);
				LogUtils.d("===========>onActivityDestroyed:" + activity);
			}
		});
		initDownloader();
	}


	private void initDownloader() {

       /* //方式1
        FileDownloader.init(getApplicationContext());
        */


		//方式2
		FileDownloadLog.NEED_LOG = BuildConfig.IS_DEBUG;

		/**
		 * just for cache Application's Context, and ':filedownloader' progress will NOT be launched
		 * by below code, so please do not worry about performance.
		 * @see FileDownloader#init(Context)
		 */
		FileDownloader.init(getApplicationContext(),
				new FileDownloadHelper.OkHttpClientCustomMaker() { // is not has to provide.
					@Override
					public OkHttpClient customMake() {
// just for OkHttpClient customize.
						final OkHttpClient.Builder builder = new OkHttpClient.Builder();
						// you can set the connection timeout.
						builder.connectTimeout(15_000, TimeUnit.MILLISECONDS);
						// you can set the HTTP proxy.
						builder.proxy(Proxy.NO_PROXY);
						// etc.
						return builder.build();
					}
				});

	}

	public static AppContext getApplication() {
		return mInstance;
	}

	private void initLogger() {
		Logger.init("logger")                 // default PRETTYLOGGER or use just init()
				.methodCount(0)                 // default 2
				.hideThreadInfo()               // default shown
				.logLevel(BuildConfig.IS_DEBUG ? LogLevel.FULL : LogLevel.NONE)        // default LogLevel.FULL
				.methodOffset(0)                // default 0
				.logAdapter(new AndroidLogAdapter()); //default AndroidLogAdapter
	}



}
