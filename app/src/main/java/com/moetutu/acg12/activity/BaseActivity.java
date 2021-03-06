package com.moetutu.acg12.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.moetutu.acg12.R;
import com.moetutu.acg12.app.AppContext;
import com.moetutu.acg12.util.Const;
import com.moetutu.acg12.util.HtmlAcgUtil;
import com.moetutu.acg12.util.PreferenceUtils;
import com.moetutu.acg12.util.ScreenUtil;
import com.moetutu.acg12.util.SharedPreferrenceHelper;
import com.umeng.analytics.MobclickAgent;


/**
 * 所有activity的基类
 */
public class BaseActivity extends BaseAppUpdateActivity implements OnClickListener {

    private Activity activity;// 子类
    public AppContext appContext;
    public SharedPreferences sharedPreferences;// 配置文件
    public PopupWindow choosePop;

    private int theme = 1;//主题

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            theme = HtmlAcgUtil.getAppTheme(this);
        } else {
            theme = savedInstanceState.getInt("theme");
        }
        setTheme(theme);
        super.onCreate(savedInstanceState);
        MobclickAgent.openActivityDurationTrack(false);
        appContext = (AppContext) getApplicationContext();
        setStatusBar();
    }

    protected void setStatusBar() {
        switch (SharedPreferrenceHelper.gettheme(context)){
            case "1":
                StatusBarUtil.setColor(this, getResources().getColor(R.color.acg_fen2),0);
                break;
            case "2":
                StatusBarUtil.setColor(this, getResources().getColor(R.color.jilaozi),0);
                break;
            case "3":
                StatusBarUtil.setColor(this, getResources().getColor(R.color.pangcilan),0);
                break;
            case "4":
                StatusBarUtil.setColor(this, getResources().getColor(R.color.shaonvfen),0);
                break;
            case "5":
                StatusBarUtil.setColor(this, getResources().getColor(R.color.yimahong),0);
                break;

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
        if (theme == HtmlAcgUtil.getAppTheme(this)) {
        } else {
            reload();
        }
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


    //设置完主题后重新加载
    public void reload() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);//不设置进入退出动画
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
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
     * 获取数据数据
     */
    public synchronized void getData(boolean isRefresh) {
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
     */
    public void setZhuTitle() {
        TextView mTextView = (TextView) activity
                .findViewById(R.id.acg_name);
        mTextView.setVisibility(View.VISIBLE);
    }

    /**
     * 设置主标题
     */
    public void setZhuTitle(String title) {
        TextView mTextView = (TextView) activity
                .findViewById(R.id.acg_name);
        mTextView.setText(title);
        mTextView.setVisibility(View.VISIBLE);
    }

    /**
     * 设置主标题icon
     */
    public void setZhuTitleIcon() {
        ImageView mTextView = (ImageView) activity
                .findViewById(R.id.acg_icon);
        mTextView.setVisibility(View.VISIBLE);
    }

    /**
     * 设置登录按钮
     */
    public void setDengluIcon() {
        TextView mTextView = (TextView) activity
                .findViewById(R.id.stock_remind);
        mTextView.setVisibility(View.VISIBLE);
        mTextView.setOnClickListener(this);
    }

    /**
     * 设置右标题
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
