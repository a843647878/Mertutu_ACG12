package com.moetutu.acg12.dialog;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.exception.FileDownloadHttpException;
import com.liulishuo.filedownloader.exception.FileDownloadOutOfSpaceException;
import com.moetutu.acg12.R;
import com.moetutu.acg12.util.SpUtils;
import com.moetutu.acg12.util.SystemUtils;
import com.moetutu.acg12.util.ToastUtils;
import com.moetutu.acg12.view.ProgressLayout;

import java.io.File;

/**
 * @author xuanyouwu
 * @email xuanyouwu@163.com
 * @time 2016-09-21 15:45
 * 升级提示对话框+下载
 */
public class AppUpdateDialog extends AlertDialog implements View.OnClickListener {


    protected AppUpdateDialog(Context context) {
        super(context);
    }

    protected AppUpdateDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    private String newAppDescInfo;
    private Activity activity;
    private boolean isForeceUpdate;
    private int reqPermissionCode;
    private String apkUrl;
    private String apkVersion;//php端返回的是"V3.3.0"这样的字符串
    private String apkFileName;

    public static final String KEY_VERSION_DESC = "ACG12_app_version_%s_desc";

    public AppUpdateDialog(Activity context, int reqPermissionCode, boolean isForeceUpdate, String apkUrl, String apkVersion, String newAppDescInfo) {
        this(context, R.style.AnimBottomDialog);
        this.activity = context;
        this.reqPermissionCode = reqPermissionCode;
        this.isForeceUpdate = isForeceUpdate;
        this.apkUrl = apkUrl;
        this.apkVersion = apkVersion;
        this.apkFileName = String.format("ACG12_%s.apk", this.apkVersion);
        this.newAppDescInfo = newAppDescInfo;
    }

    protected AppUpdateDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    private ImageView dialog_close;
    private TextView tv_version_info;
    private CheckedTextView bt_ok;
    private ProgressLayout progressLayout;
    private TextView tv_version;
    private FileDownloadListener apkDownloadListener = new FileDownloadListener() {
        @Override
        protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            if (bt_ok != null) {
                bt_ok.setBackgroundColor(Color.TRANSPARENT);
            }
        }

        @Override
        protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            if (progressLayout != null) {
                progressLayout.setCurrentProgress((int) (soFarBytes * 1.0f / totalBytes * 100));
            }
            if (bt_ok != null) {
                bt_ok.setText(((int) (soFarBytes * 1.0f / totalBytes * 100)) + "%");
            }
        }

        @Override
        protected void completed(BaseDownloadTask task) {
            dismiss();
            installApk();
        }

        @Override
        protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
        }

        @Override
        protected void error(BaseDownloadTask task, Throwable e) {
            if (e instanceof FileDownloadHttpException) {
                int code = ((FileDownloadHttpException) e).getCode();
                ToastUtils.showRoundRectToast(String.format("%s:%s", code, "下载异常!"));
            } else if (e instanceof FileDownloadOutOfSpaceException) {
                new CommAlertDialog.Builder(activity)
                        .setNotice("存储空间严重不足,去清理?")
                        .setPositiveButtonBgColor(0xFF02b980)
                        .setPositiveButtonTextColor(Color.WHITE)
                        .setPositiveButton("确认", new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SystemUtils.launchInterStorageSettings(activity);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            } else {
                ToastUtils.showRoundRectToast(String.format("下载异常!"));
            }
            dismiss();
        }

        @Override
        protected void warn(BaseDownloadTask task) {

        }
    };

    @Override
    public void dismiss() {
        super.dismiss();
        pauseDownloadApk();
    }

    /**
     * 安装apk
     */
    private void installApk() {
        if (Environment.isExternalStorageEmulated()) {
            String ROOTPATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
            File apkFile = new File(ROOTPATH + apkFileName);
            if (!apkFile.exists()) {
                return;
            }
            Intent intent = new Intent(Intent.ACTION_VIEW);
            if(Build.VERSION.SDK_INT>=24) { //判读版本是否在7.0以上
                Uri apkUri =
                        FileProvider.getUriForFile(getContext(), "com.moetutu.acg12.fileprovider", apkFile);
                //添加这一句表示对目标应用临时授权该Uri所代表的文件
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            }else {
                intent.setDataAndType(Uri.fromFile(apkFile),
                        "application/vnd.android.package-archive");
            }
            getContext().startActivity(intent);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_update_app);
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
        dialog_close.setVisibility(isForeceUpdate ? View.GONE : View.VISIBLE);
        tv_version_info = (TextView) findViewById(R.id.tv_version_info);
        tv_version_info.setText(newAppDescInfo);
        bt_ok = (CheckedTextView) findViewById(R.id.bt_ok);
        bt_ok.setOnClickListener(this);
        progressLayout = (ProgressLayout) findViewById(R.id.progressLayout);
        progressLayout.setMaxProgress(100);
        tv_version = (TextView) findViewById(R.id.tv_version);
        tv_version.setText(apkVersion);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_close:
                dismiss();
                break;
            case R.id.bt_ok:
                if (TextUtils.equals(bt_ok.getText(), "立即更新")) {
                    if (SystemUtils.checkPermissions(activity,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE})) {
                        startDownloadApk();
                    } else {
                        ToastUtils.showFillToast("请务必打开文件写入权限!");
                        SystemUtils.reqPermission(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, reqPermissionCode);
                    }
                } else {

                }
                break;
        }
    }

    /**
     * //php端返回的是"V3.3.0"这样的字符串
     * 处理成BuildConfig VERSION_NAME 方便对应
     */
    private void saveNewAppDescInfo() {
        try {
            if (!TextUtils.isEmpty(apkVersion) && !TextUtils.isEmpty(newAppDescInfo)) {
                String formatApkVersion = apkVersion.replace("V", "");
                formatApkVersion = formatApkVersion.replace("v", "");
                SpUtils.getInstance().putData(
                        String.format(KEY_VERSION_DESC, formatApkVersion),
                        newAppDescInfo);
            }
        } catch (Exception e) {
        }
    }

    private void startDownloadApk() {
        saveNewAppDescInfo();
        if (Environment.isExternalStorageEmulated()) {
            String ROOTPATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
            FileDownloader
                    .getImpl()
                    .create(apkUrl)
                    .setPath(ROOTPATH + apkFileName)
                    .setForceReDownload(true)
                    .setListener(apkDownloadListener).start();
        } else {
            ToastUtils.showRoundRectToast("sd卡不可用!");
        }
    }

    private void pauseDownloadApk() {
        if (apkDownloadListener != null) {
            try {
                FileDownloader
                        .getImpl()
                        .pause(apkDownloadListener);
            } catch (Exception e) {
            }
        }
    }
}
