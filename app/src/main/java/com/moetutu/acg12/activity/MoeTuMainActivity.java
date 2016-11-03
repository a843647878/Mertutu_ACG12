package com.moetutu.acg12.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;

import com.moetutu.acg12.R;
import com.moetutu.acg12.adapter.BaseFragmentAdapter;
import com.moetutu.acg12.app.ACache;
import com.moetutu.acg12.asynctask.type.Acg12Obj;
import com.moetutu.acg12.asynctask.type.AcgUserObj;
import com.moetutu.acg12.fragment.FragementTu;
import com.moetutu.acg12.http.RetrofitService;
import com.moetutu.acg12.http.callback.SimpleCallBack;
import com.moetutu.acg12.util.Const;
import com.moetutu.acg12.util.ExampleUtil;
import com.moetutu.acg12.util.T;
import com.moetutu.acg12.view.LogingPopupWindow;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.umeng.analytics.MobclickAgent;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.jpush.android.api.JPushInterface;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Description
 * Company guokeyuzhou
 * Created by chengwanying on 16/6/15.
 * version
 */
public class MoeTuMainActivity extends BaseActivity {
    private String userid;
    private String touxiang;
    private String name;
    public static boolean isForeground = false;
    BaseFragmentAdapter fragmentAdapter;

    @InjectView(R.id.viewpagertab)
    SmartTabLayout viewpagertab;
    @InjectView(R.id.viewpager)
    ViewPager viewpager;

    private MessageReceiver mMessageReceiver;


    public static void launch(Context context) {
        if (context == null) return;
        Intent in = new Intent(context, MoeTuMainActivity.class);
        context.startActivity(in);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);
        ButterKnife.inject(this);
        JPushInterface.resumePush(getApplicationContext());
        setImmerseLayout(findViewById(R.id.main_content));
        mMessageReceiver = new MessageReceiver();
        initView(this);
        //m_vp.setOffscreenPageLimit(2);
    }


    @Override
    public void initView(Activity activity) {
        super.initView(activity);
        setZhuTitleIcon();

        fragmentAdapter = new BaseFragmentAdapter(getSupportFragmentManager());

        viewpager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Fragment fragment = fragmentAdapter.getItem(position);
                fragment.setUserVisibleHint(true);
            }
        });
        fragmentAdapter.bindTitle(true, Arrays.asList("动漫图集", "图站精选", "绅士道", "PC通用萌化", "安卓萌化", "动漫游戏", "其他分类"));
        fragmentAdapter.bindData(true,
                Arrays.asList(
                        FragementTu.newInstance(Const.DongManTuJi).startLazyMode(),
                        FragementTu.newInstance(Const.TuZhanJingXuan),
                        FragementTu.newInstance(Const.ShenShi),
                        FragementTu.newInstance(Const.PCMengHua),
                        FragementTu.newInstance(Const.AndroidMengHua),
                        FragementTu.newInstance(Const.DongManGame),
                        FragementTu.newInstance(Const.QiTaFenLei)));
        viewpager.setAdapter(fragmentAdapter);
        viewpagertab.setViewPager(viewpager);
    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        super.onClick(v);
        ACache aCache = ACache.get(appContext);
        AcgUserObj data = (AcgUserObj) aCache.getAsObject("LOGIN");
        if (data != null) {
            userid = data.getId() + "";
        }
        switch (v.getId()) {
            case R.id.stock_remind:

                if (TextUtils.isEmpty(userid)) {
                    showLoginRegist2();
                } else {
                    T.showShort("你已经登录了喵！");
                }
                break;

            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("ACGmain_Activity");
        MobclickAgent.onResume(this); // 统计时长
        initData();

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("ACGmain_Activity");
        MobclickAgent.onPause(this);
        isForeground = false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        switch (keyCode) {
            case KeyEvent.KEYCODE_MENU:// 菜单键
                if (choosePop != null && choosePop.isShowing()) {
                    choosePop.dismiss();
                }
                break;
            case KeyEvent.KEYCODE_BACK:// 返回键
                if (choosePop != null && choosePop.isShowing()) {
                    choosePop.dismiss();
                } else {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        exitBy2Click(); // 调用双击退出函数
                    }
                    return false;
                }
                break;

            default:
                break;
        }
        return true;
    }

    /**
     * 双击退出函数
     */
    private static Boolean isExit = false;

    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            T.showShort("再按一次退出程序");
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            finish();
            System.exit(0);
        }
    }

    LogingPopupWindow menuWindow3;

    /**
     * 登录注册
     */
    private void showLoginRegist2() {
        menuWindow3 = new LogingPopupWindow(MoeTuMainActivity.this, itemsOnClick1);
        menuWindow3.setOutsideTouchable(true);
        // 显示窗口
        menuWindow3
                .showAtLocation(MoeTuMainActivity.this
                                .findViewById(R.id.main_content), Gravity.CENTER,
                        0, 0); // 设置layout在PopupWindow中显示的位置

    }

    View.OnClickListener itemsOnClick1 = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            menuWindow3.dismiss();
            switch (v.getId()) {
                case R.id.popup_back:
                    menuWindow3.dismiss();
                    break;
                case R.id.popup_regist:
                    T.showShort("暂时未开放注册功能喵，请前去ACG12小站注册");

                    break;
                case R.id.popup_login:
                    //登录预留
                    break;
            }

        }

    };

    //for receive customer msg from jpush server
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    public void registerMessageReceiver() {

        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(KEY_MESSAGE);
                String extras = intent.getStringExtra(KEY_EXTRAS);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                if (!ExampleUtil.isEmpty(extras)) {
                    showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                }
            }
        }
    }
}
