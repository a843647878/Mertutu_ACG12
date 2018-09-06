package com.moetutu.acg12.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.widget.Toast;


import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.moetutu.acg12.BuildConfig;
import com.moetutu.acg12.broadcast.GetBroadcast;
import com.moetutu.acg12.dialog.AppUpdateDialog;
import com.moetutu.acg12.entity.UpAppEntity;
import com.moetutu.acg12.entity.UpdateApkInfo;
import com.moetutu.acg12.http.RetrofitService;
import com.moetutu.acg12.http.callback.SimpleCallBack;
import com.moetutu.acg12.http.httpmodel.ResEntity;
import com.moetutu.acg12.interf.OnUpdateDialogNoticeListener;
import com.moetutu.acg12.util.SnackbarUtils;
import com.moetutu.acg12.util.T;
import com.moetutu.acg12.util.ToastUtils;

import java.io.File;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Description
 * Company Beijing guokeyuzhou
 * author  youxuan  E-mail:xuanyouwu@163.com
 * date createTime：16/6/16
 * version
 */
public class BaseAppUpdateActivity extends PermisionActivity implements OnUpdateDialogNoticeListener {


    // 更新提示  强制对话框 -确定 ->  再一个对话框(进度条的对话框)
    private AlertDialog updateNoticeDialog;
    private ProgressDialog updateProgressDialog;
    private GetBroadcast getBroadcast;
    private UpdateApkInfo apkInfo;
    private FileDownloadListener apkDownloadListener = new FileDownloadListener() {
        @Override
        protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {

        }

        @Override
        protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            if (updateProgressDialog != null) {
                updateProgressDialog.setMax(totalBytes);
                updateProgressDialog.setProgress(soFarBytes);
            }
        }

        @Override
        protected void completed(BaseDownloadTask task) {
            if (updateProgressDialog != null) {
                updateProgressDialog.dismiss();
            }
            if (getBroadcast == null) {
                getBroadcast = new GetBroadcast();
                registerReceiver(getBroadcast, GetBroadcast.getmIntentFilter());
            }
            installApk();
        }

        @Override
        protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
        }

        @Override
        protected void error(BaseDownloadTask task, Throwable e) {
            Toast.makeText(BaseAppUpdateActivity.this, "下载异常!", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void warn(BaseDownloadTask task) {

        }
    };


    @Override
    public synchronized void shouldUpdate(final boolean isForce) {
            RetrofitService
                    .getInstance("http://m.acg12.cn/and-update/")
                    .getApiCacheRetryService()
                    .GetVersion()
                    .enqueue(new SimpleCallBack<UpAppEntity>() {
                        @Override
                        public void onSuccess(Call<ResEntity<UpAppEntity>> call, Response<ResEntity<UpAppEntity>> response) {
                            if (response.body().data != null) {
                            if (response.body().data.versionCode > BuildConfig.VERSION_CODE) {
                                if (!isDestroyOrFinishing()) {
                                    new AppUpdateDialog(
                                            context,
                                            REQUEST_STORAGE_WRITE_ACCESS_PERMISSION,
                                            false,
                                            response.body().data.url,
                                            BuildConfig.VERSION_NAME,
                                            response.body().data.instructions).show();
                                }
                            }else {
                                if (isForce){
                                    T.showShort("已是最新版本");
                                }
                            }
                            }
                        }
                    });
    }


    private synchronized void showUpdateNoticeDialog(final boolean isForce) {
        if (apkInfo == null) return;
        if (TextUtils.isEmpty(apkInfo.updateUrl)) return;
        if (updateNoticeDialog == null) {
            String noticeMsg = apkInfo.updateInfo;
            if (!TextUtils.isEmpty(noticeMsg)) {
                noticeMsg = noticeMsg.replaceAll("\\\\n", "\n");
                noticeMsg = noticeMsg.replaceAll("\\\\r\\\\n", "\n");
            }
            updateNoticeDialog = new AlertDialog.Builder(this)
                    .setTitle("软件版本更新")
                    .setMessage(noticeMsg)
                    .setCancelable(!isForce)
                    .setPositiveButton("下载", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            checkPermision(isForce);
                        }
                    })
                    .create();
        }
        if (!isDestroyOrFinishing()) {//activity 未销毁
            if (!updateNoticeDialog.isShowing()) {
                updateNoticeDialog.show();
            }
        }
    }

    private void showUpdateProgessDialog(boolean isForce) {
        if (apkInfo == null) return;
        if (updateProgressDialog == null) {
            updateProgressDialog = new ProgressDialog(this);
            updateProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            updateProgressDialog.setTitle("正在下载");
            updateProgressDialog.setCanceledOnTouchOutside(!isForce);
            updateProgressDialog.setCancelable(!isForce);
        }
        if (!updateProgressDialog.isShowing()) {
            updateProgressDialog.show();
            downloadNewApk(apkInfo.updateUrl);
        }
    }

    private void downloadNewApk(String apkUrl) {
        if (TextUtils.isEmpty(apkUrl)) {
            if (updateProgressDialog != null && updateProgressDialog.isShowing()) {
                updateProgressDialog.dismiss();
            }
        } else {
            if (Environment.isExternalStorageEmulated()) {
                String ROOTPATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
                String fileName = "tcmmooc.apk";
                FileDownloader
                        .getImpl()
                        .create(apkUrl)
                        .setPath(ROOTPATH + fileName)
                        .setForceReDownload(true)
                        .setListener(apkDownloadListener).start();
            } else {
                Toast.makeText(this, "sd卡不可用!", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void shouldShutDown(String msg) {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        pauseDownloadApk();
        if (getBroadcast != null) {
            unregisterReceiver(getBroadcast);
        }
    }

    private void checkPermision(final boolean isForce) {
        if (apkInfo == null) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN // Permission was added in API Level 16
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, "我们需要文件读写权限",
                    REQUEST_STORAGE_WRITE_ACCESS_PERMISSION);
        } else {
            if (updateNoticeDialog != null)
                updateNoticeDialog.dismiss();
            showUpdateProgessDialog(isForce);
        }
    }


    /**
     * 安装apk
     */
    private void installApk() {
        if (Environment.isExternalStorageEmulated()) {
            String ROOTPATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
            String fileName = "tcmmooc.apk";
            File apkFile = new File(ROOTPATH + fileName);
            if (!apkFile.exists()) {
                return;
            }
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(apkFile),
                    "application/vnd.android.package-archive");
            startActivity(intent);
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

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_STORAGE_WRITE_ACCESS_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (apkInfo != null) {
                        showUpdateProgessDialog(apkInfo.force == 1);
                    }
                } else {
                    showLongSnackBar("文件写入权限被拒绝,请到设置页面打开app文件写入权限!");
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void showSnackBar(String msg) {
        SnackbarUtils.showSnack(this, msg);
    }

    public void showLongSnackBar(String msg) {
        SnackbarUtils.showLongSnack(this, msg);
    }

    public void showFillToast(CharSequence msg) {
        ToastUtils.showFillToast(msg);
    }

    public void showRoundRectToast(CharSequence msg) {
        ToastUtils.showRoundRectToast(msg);
    }

}
