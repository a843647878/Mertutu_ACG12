package com.moetutu.acg12.app;

import android.content.Context;
import android.support.multidex.MultiDexApplication;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.facebook.drawee.backends.pipeline.Fresco;

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

		JPushInterface.init(this);
		JPushInterface.setDebugMode(false);        // 设置开启日志,发布时请关闭日志
		mInstance = this;
		//fasebook初始化
		Fresco.initialize(this);
	}


	public static AppContext getApplication() {
		return mInstance;
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


	
}
