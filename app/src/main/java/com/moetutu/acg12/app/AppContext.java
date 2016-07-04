package com.moetutu.acg12.app;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.multidex.MultiDexApplication;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.moetutu.acg12.R;
import com.moetutu.acg12.view.CustomProgressDialog;

import cn.jpush.android.api.JPushInterface;


/**
 * 全局应用程序类：用于保存和调用全局应用配置及访问网络数据
 */
public class AppContext extends MultiDexApplication {//com.zztzt.android.simple.app.MainApplication {
	/**
	 * 全局Context，原理是因为Application类是应用最先运行的，所以在我们的代码调用时，该值已经被赋值过了
	 */
	private static AppContext mInstance;
	
	
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		JPushInterface.setDebugMode(false); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);

		mInstance = this;
		//fasebook初始化
		Fresco.initialize(this);
	}

	private int count = 0;// 用于计数
	private CustomProgressDialog progressDialog;// 加载进度条

	/**
	 * 获取App安装包 信息
	 *
	 * @return
	 */
	public PackageInfo getPackageInfo() {
		PackageInfo info = null;
		try {
			info = getPackageManager().getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace(System.err);
		}
		if (info == null)
			info = new PackageInfo();
		return info;
	}

	public static AppContext getApplication() {
		return mInstance;
	}

	/**
	 * 获取渠道名
	 *
	 * @param ctx
	 *            此处习惯性的设置为activity，实际上context就可以
	 * @return 如果没有获取成功，那么返回值为空
	 */
	public String getChannelName(Activity ctx) {
		if (ctx == null) {
			return null;
		}
		String channelName = null;
		try {
			PackageManager packageManager = ctx.getPackageManager();
			if (packageManager != null) {
				// 注意此处为ApplicationInfo 而不是
				// ActivityInfo,因为友盟设置的meta-data是在application标签中，而不是某activity标签中，所以用ApplicationInfo
				ApplicationInfo applicationInfo = packageManager
						.getApplicationInfo(ctx.getPackageName(),
								PackageManager.GET_META_DATA);
				if (applicationInfo != null) {
					if (applicationInfo.metaData != null) {
						// channelName =
						// applicationInfo.metaData.getString("UMENG_CHANNEL","android");
						channelName = applicationInfo.metaData
								.getString("UMENG_CHANNEL");
					}
				}

			}
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return channelName;
	}

	// IMEI（imei）
	public String getIMEI() {
		TelephonyManager tm = (TelephonyManager) getApplicationContext()
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = tm.getDeviceId();
		if (!TextUtils.isEmpty(imei)) {

			return imei;
		}
		return null;
	}

	/**
	 * 网络判断
	 *
	 * @return
	 */
	public boolean NetConnect() {
		boolean net = true;
		int code = com.moetutu.acg12.util.NetUtil.getNetworkState(this);
		if (com.moetutu.acg12.util.NetUtil.NETWORN_NONE == code) {
			com.moetutu.acg12.util.T.showShort(R.string.network_not_connected);
			net = false;
		}
		return net;
	}

	public boolean isWifi(Context mContext) {
		ConnectivityManager connectivityManager = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null
				&& activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
			return true;
		}
		return false;
	}

	
	/**
	 * 显示进度条
	 */
	public void showProgress(Activity activity) {
		showProgress(activity, R.string.common_loading, true);
	}

	public void showProgress(Activity activity, int id) {
		showProgress(activity, id, true);
	}

	public void showProgress(Activity activity, boolean canBack) {
		showProgress(activity, R.string.common_loading, canBack);
	}
	
	/**
	 * 显示进度条
	 *
	 * @param resID
	 * @param canBack
	 */
	public void showProgress(Activity activity, int resID, boolean canBack) {
		if (activity != null && !activity.isFinishing()) {
			if (progressDialog != null) {
				progressDialog.cancel();
			}
			progressDialog = new com.moetutu.acg12.view.CustomProgressDialog(activity, getResources()
					.getString(resID), canBack);
			progressDialog.show();
		}
	}

	/**
	 * 取消进度条
	 */
	public void cancelProgress() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog.cancel();
		}
	}




//	/**
//	 * 开启服务
//	 */
//	public void startService() {
//		Intent mServiceIntent = new Intent(getApplicationContext(),
//				MyService.class);
//		startService(mServiceIntent);
//	}



	public String getTuJiList(String url,int bookpage){

		String ss=String.format(url,bookpage);
		return ss;
	}

	public String getWenDang(String url,String wendangid){

		String ss=String.format(url,wendangid);
		return ss;
	}
	
	

	

	
}
