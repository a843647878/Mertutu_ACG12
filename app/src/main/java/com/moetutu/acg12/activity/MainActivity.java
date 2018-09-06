package com.moetutu.acg12.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.jaeger.library.StatusBarUtil;
import com.moetutu.acg12.BuildConfig;
import com.moetutu.acg12.R;
import com.moetutu.acg12.adapter.BaseFragmentAdapter;
import com.moetutu.acg12.dialog.AppNewVersionInfoDialog;
import com.moetutu.acg12.dialog.AppUpdateDialog;
import com.moetutu.acg12.dialog.CommAlertDialog;
import com.moetutu.acg12.entity.eventmodel.ImageEvtivity;
import com.moetutu.acg12.fragment.MainComicFragement;
import com.moetutu.acg12.fragment.MainFigureFragement;
import com.moetutu.acg12.fragment.MainInfiniteCardFragement;
import com.moetutu.acg12.fragment.MainMerTuFragement;
import com.moetutu.acg12.fragment.MainUserFragement;
import com.moetutu.acg12.util.ExampleUtil;
import com.moetutu.acg12.util.FileUtils;
import com.moetutu.acg12.util.LogUtils;
import com.moetutu.acg12.util.PicCropUtil;
import com.moetutu.acg12.util.SpUtils;
import com.moetutu.acg12.util.SystemUtils;
import com.moetutu.acg12.util.T;
import com.moetutu.acg12.view.NoScrollViewPager;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;

/**
 * Description
 * Company guokeyuzhou
 * Created by chengwanying on 16/7/1.
 * version
 * 首页
 */
public class MainActivity extends BaseActivity {

    private static final int ACTION_UPDATE = 100;//显示更新提示对话框
    private static final int ACTION_SHOW_NEW_VERSION_APP = 101;//显示更新后的对话框
    private static final int ACTION_SHOW_INNER_SPACE_NOT_AVAIABLE = 102;//显示内置存储空间不足

    public static boolean isForeground = false;

    BaseFragmentAdapter fragmentAdapter;
    String KEY_TAB_INDEX = "tab_index";
    @BindView(R.id.viewpager_main)
   ViewPager viewpagerMain;
    @BindView(R.id.bottom_view)
    View bottomView;
    @BindView(R.id.rb_mertu)
    RadioButton rbMertu;
    @BindView(R.id.rb_figure)
    RadioButton rbFigure;
    @BindView(R.id.rb_comic)
    RadioButton rbComic;
    @BindView(R.id.rb_user)
    RadioButton rbUser;
    @BindView(R.id.main_tab)
    RadioGroup mainTab;


    private MessageReceiver mMessageReceiver;

    public static void launch(Context context) {
        if (context == null) return;
        Intent in = new Intent(context, MainActivity.class);
        in.addCategory(Intent.CATEGORY_DEFAULT);
        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(in);
    }

    public static void launchPush(Context context) {
        if (context == null) return;
        Intent in = new Intent(context, MainActivity.class);
        in.addCategory(Intent.CATEGORY_DEFAULT);
        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(in);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mMessageReceiver = new MessageReceiver();
        JPushInterface.resumePush(this);
        initView(this);
    }

    @Override
    public void initView(Activity activity) {
        super.initView(activity);
        fragmentAdapter = new BaseFragmentAdapter(getSupportFragmentManager());
        viewpagerMain.setAdapter(fragmentAdapter);
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(MainMerTuFragement.newInstance().startLazyMode());
        fragmentList.add(MainFigureFragement.newInstance());
        fragmentList.add(MainComicFragement.newInstance());
        fragmentList.add(MainUserFragement.newInstance());
        viewpagerMain.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Fragment fragment = fragmentAdapter.getItem(position);
                if (fragment != null)
                    fragment.setUserVisibleHint(true);
            }
        });
        fragmentAdapter.bindData(true, fragmentList);
        mainTab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_mertu:
                        viewpagerMain.setCurrentItem(0, false);
                        break;
                    case R.id.rb_figure:
                        viewpagerMain.setCurrentItem(1, false);
                        break;
                    case R.id.rb_comic:
                        viewpagerMain.setCurrentItem(2, false);
                        break;
                    case R.id.rb_user:
                        viewpagerMain.setCurrentItem(3, false);
                        break;
                }
            }
        });

        //展示新版本新功能或者检查版本更新
        String appNewVersionDesc = SpUtils.getInstance().getStringData(String.format(AppUpdateDialog.KEY_VERSION_DESC, BuildConfig.VERSION_NAME), "");
        if (TextUtils.isEmpty(appNewVersionDesc)) {
            checkAppUpdate();
        } else {
            showNewVersionDesc();
        }
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState != null && viewpagerMain != null) {
            outState.putInt(KEY_TAB_INDEX, viewpagerMain.getCurrentItem());
        }
    }

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

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("ACGmain_Activity");
        MobclickAgent.onResume(this); // 统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("ACGmain_Activity");
        MobclickAgent.onPause(this);
        isForeground = false;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageViewInFragment(context, null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PicCropUtil.onActivityResult(requestCode, resultCode, data, context, new PicCropUtil.CropHandler() {
            @Override
            public void handleCropResult(Uri uri, int tag) {
                LogUtils.d("---------->Uri"+uri);
                EventBus.getDefault().post(new ImageEvtivity(uri));
            }

            @Override
            public void handleCropError(Intent data) {

            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ACTION_UPDATE:
                    shouldUpdate(false);
                    break;
                case ACTION_SHOW_NEW_VERSION_APP:
                    if (!isDestroyOrFinishing()) {
                        String appNewVersionDesc = SpUtils.getInstance().getStringData(String.format(AppUpdateDialog.KEY_VERSION_DESC, BuildConfig.VERSION_NAME), "");
                        if (!TextUtils.isEmpty(appNewVersionDesc)) {
                            new AppNewVersionInfoDialog(MainActivity.this, appNewVersionDesc).show();
                        }
                    }
                    break;
                case ACTION_SHOW_INNER_SPACE_NOT_AVAIABLE:
                    if (!isDestroyOrFinishing()) {
                        if (FileUtils.isAvaiableSpace(100)) {
                            new CommAlertDialog.Builder(context)
                                    .setNotice("存储空间小于100M啦,去清理?")
                                    .setPositiveButtonBgColor(0xFF02b980)
                                    .setPositiveButtonTextColor(Color.WHITE)
                                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            SystemUtils.launchInterStorageSettings(context);
                                        }
                                    })
                                    .setNegativeButton("取消", null)
                                    .show();
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 提示新版本功能介绍
     */
    private void showNewVersionDesc() {
        handler.removeMessages(ACTION_SHOW_NEW_VERSION_APP);
        handler.sendEmptyMessageDelayed(ACTION_SHOW_NEW_VERSION_APP, 1_000);
    }

    /**
     * 检查更新
     */
    private void checkAppUpdate() {
        handler.removeMessages(ACTION_UPDATE);
        handler.sendEmptyMessageDelayed(ACTION_UPDATE, 1_000);
    }

    /**
     * 检查内置存储空间
     */
    private void checkAvaiableSpace() {
        handler.removeMessages(ACTION_SHOW_INNER_SPACE_NOT_AVAIABLE);
        handler.sendEmptyMessageDelayed(ACTION_SHOW_INNER_SPACE_NOT_AVAIABLE, 2 * 1_000);
    }
}
