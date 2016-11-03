package com.moetutu.acg12.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.moetutu.acg12.R;
import com.moetutu.acg12.app.AppContext;
import com.moetutu.acg12.util.Const;
import com.moetutu.acg12.util.PreferenceUtils;
import com.moetutu.acg12.util.ScreenUtil;
import com.umeng.analytics.MobclickAgent;


/**
 * 所有activity的基类
 * 
 */
public class BaseActivity extends BaseAppUpdateActivity implements OnClickListener{

	private Activity activity;// 子类
	public AppContext appContext;
	public SharedPreferences sharedPreferences;// 配置文件
	public PopupWindow choosePop;
	public PopupWindow mPopupShuoMing;
	
	public PopupWindow mPopupDialog;
	public PopupWindow mPopupgainame;
	public static int state = 3;// 主状态页标志�?：股票；2：动态；3：营业部�?：我�?
	public boolean showPW = true;// 是否显示CIS

	ImageView button_gupiao;
	ImageView button_hangqing;
	ImageView button_yingyebu;
	ImageView button_wo;
	ImageView button_fangdiebao;

	TextView text_gupiao;
	TextView text_hangqing;
	TextView text_yingyebu;
	TextView text_wo;
	TextView text_fangdiebao;

	LinearLayout dh_lay1;
	LinearLayout dh_lay_hangqing;
	LinearLayout dh_lay2;
	LinearLayout dh_lay3;
	LinearLayout dh_lay4;
	LinearLayout dh_lay_jia;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MobclickAgent.openActivityDurationTrack(false);
		appContext = (AppContext) getApplicationContext();
		String result = PreferenceUtils.getPrefString(BaseActivity.this, Const.PREFERENCES_NAME, Context.MODE_PRIVATE, "onlyIdqudao", null);
		if (TextUtils.isEmpty(result)) {
			//获取手机的唯�?���?
			String onlyId = appContext.getIMEI();
			//获取渠道名称
			ApplicationInfo appInfo = null;
			String qudao = null;
			try {
				appInfo = this.getPackageManager()
				        .getApplicationInfo(getPackageName(),
				PackageManager.GET_META_DATA);
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
//			if (appInfo!=null) {
//				qudao=appInfo.metaData.getString("UMENG_CHANNEL");
//			}
		}
	}
	
	protected void setImmerseLayout(View view) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			Window window = getWindow();
			/*window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
	                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);*/
			window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

			int statusBarHeight = ScreenUtil.getStatusBarHeight(this.getBaseContext());
			view.setPadding(0, statusBarHeight, 0, 0);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	

	/**
	 * 初始化View
	 * 
	 * @param activity
	 */
	public void initView(Activity activity) {
		this.activity = activity;
		sharedPreferences = activity.getSharedPreferences(
				Const.PREFERENCES_NAME, Context.MODE_PRIVATE);
		appContext = (AppContext) getApplicationContext();
	}

	/**
	 * 初始化数�?
	 */
	public void initData() {
	}

	/**
	 * 事件监听
	 */
	public void initListener() {
	}

	@Override
	public void onClick(View v) {
	}

	
	/**
	 * 设置主标题
	 * 
	 */
	public void setZhuTitle() {
		TextView mTextView = (TextView) activity
				.findViewById(R.id.acg_name);
		mTextView.setVisibility(View.VISIBLE);
	}
	/**
	 * 设置主标题
	 * 
	 */
	public void setZhuTitle(String title) {
		TextView mTextView = (TextView) activity
				.findViewById(R.id.acg_name);
		mTextView.setText(title);
		mTextView.setVisibility(View.VISIBLE);
	}
	
	/**
	 * 设置主标题icon
	 * 
	 */
	public void setZhuTitleIcon() {
		ImageView mTextView = (ImageView) activity
				.findViewById(R.id.acg_icon);
		mTextView.setVisibility(View.VISIBLE);
	}

	/**
	 * 设置登录按钮
	 * 
	 */
	public void setDengluIcon() {
		TextView mTextView = (TextView) activity
				.findViewById(R.id.stock_remind);
		mTextView.setVisibility(View.VISIBLE);
		mTextView.setOnClickListener(this);
	}
	
	/**
	 * 设置右标题
	 * 
	 */
	public void setTitleRight(String text) {
		TextView mTextView = (TextView) activity
				.findViewById(R.id.stock_remind);
		mTextView.setVisibility(View.VISIBLE);
		mTextView.setText(text);
	}
	
	/**
	 * 设置标题栏左侧返回按钮
	 */
	public void setTitleLeftImageBack() {
		TextView back = (TextView) activity
				.findViewById(R.id.view_title_left_image_btn_back);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(0, R.anim.push_right_out);
			}
		});
	}



	
	
	

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(0, R.anim.push_right_out);
	}

}
