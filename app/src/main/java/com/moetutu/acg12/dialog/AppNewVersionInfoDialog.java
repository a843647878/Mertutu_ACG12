package com.moetutu.acg12.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.moetutu.acg12.BuildConfig;
import com.moetutu.acg12.R;
import com.moetutu.acg12.util.SpUtils;


/**
 * @author xuanyouwu
 * @email xuanyouwu@163.com
 * @time 2016-09-21 15:45
 * 新版本升级后提示新功能的对话框
 */
public class AppNewVersionInfoDialog extends AlertDialog implements View.OnClickListener {


    protected AppNewVersionInfoDialog(Context context) {
        super(context);
    }

    protected AppNewVersionInfoDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    private String newAppDescInfo;

    public AppNewVersionInfoDialog(Context context, String newAppDescInfo) {
        this(context, R.style.AnimBottomDialog);
        this.newAppDescInfo = newAppDescInfo;
    }

    protected AppNewVersionInfoDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    private ImageView dialog_close;
    private TextView tv_version_info;
    private Button bt_ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_new_version_app_info);
        Window win = getWindow();
        win.setWindowAnimations(R.style.AnimBottomDialog);
        win.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        win.getDecorView().setPadding(0, 0, 0, 0);
        win.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        win.setGravity(Gravity.CENTER);
        win.getAttributes().dimAmount = 0.5f;
        win.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        setCanceledOnTouchOutside(false);
        dialog_close = (ImageView) findViewById(R.id.dialog_close);
        dialog_close.setOnClickListener(this);
        tv_version_info = (TextView) findViewById(R.id.tv_version_info);
        tv_version_info.setText(newAppDescInfo);
        bt_ok = (Button) findViewById(R.id.bt_ok);
        bt_ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_close:
            case R.id.bt_ok:
                SpUtils.getInstance().putData(String.format(AppUpdateDialog.KEY_VERSION_DESC, BuildConfig.VERSION_NAME), "");
                dismiss();
                break;
        }
    }
}
