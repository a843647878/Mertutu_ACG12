package com.moetutu.acg12.activity;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.exception.FileDownloadHttpException;
import com.liulishuo.filedownloader.exception.FileDownloadOutOfSpaceException;
import com.moetutu.acg12.R;
import com.moetutu.acg12.dialog.CommAlertDialog;
import com.moetutu.acg12.util.AppUtil;
import com.moetutu.acg12.util.GlideUtils;
import com.moetutu.acg12.util.SystemUtils;
import com.moetutu.acg12.util.T;
import com.moetutu.acg12.util.ToastUtils;
import com.moetutu.acg12.view.HackyViewPager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;


/**
 * Description   图片阅览界面（设为壁纸）
 * Company guokeyuzhou
 * Created by chengwanying on 16/6/15.
 * version
 */

public class ImagePagerBGActivity extends BaseActivity {

    // WallpaperManager类：系统壁纸管理。通过它可以获得当前壁纸以及设置指定图片作为系统壁纸。
    private WallpaperManager wallpaperManager;

    private static final String KEY_URLS = "key_urls";
    private static final String KEY_POS = "key_pos";
    private static final String KEY_NAME = "key_name";
    @BindView(R.id.tvsetbg)
    TextView tvsetbg;

    private int reqPermissionCode;

    SamplePagerAdapter pagerAdapter;
    String[] urls;
    Handler handler = new Handler();
    int realPos;
    @BindView(R.id.imagePager)
    HackyViewPager imagePager;
    @BindView(R.id.tvPagerTitle)
    TextView tvPagerTitle;
    @BindView(R.id.tvDownload)
    TextView tvDownload;

    private String apkFileName;
    private String ROOTPATH;


    public static void launch(Context context, String name, String[] urls, int pos) {
        if (context == null) return;
        if (urls == null) return;
        if (name == null) return;
        if (urls.length == 0) return;
        Intent intent = new Intent(context, ImagePagerBGActivity.class);
        intent.putExtra(KEY_NAME, name);
        intent.putExtra(KEY_URLS, urls);
        intent.putExtra(KEY_POS, pos);
        context.startActivity(intent);
    }

    public static void launch(Context context, String name, String[] urls) {
        launch(context, name, urls, 1);
    }

    public static void launch(Context context, String name, List<String> urls) {
        launch(context, name, urls, 1);
    }

    public static void launch(Context context, String name, List<String> urls, int pos) {
        if (context == null) return;
        if (urls == null) return;
        if (name == null) return;
        if (urls.size() == 0) return;
        String[] urlsArr = (String[]) urls.toArray(new String[urls.size()]);
        Intent intent = new Intent(context, ImagePagerBGActivity.class);
        intent.putExtra(KEY_NAME, name);
        intent.putExtra(KEY_URLS, urlsArr);
        intent.putExtra(KEY_POS, pos);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_pager_bg);
        ButterKnife.bind(this);
        wallpaperManager = WallpaperManager.getInstance(this);
        urls = getIntent().getStringArrayExtra(KEY_URLS);
        realPos = getIntent().getIntExtra(KEY_POS, 0);

        pagerAdapter = new SamplePagerAdapter(getIntent().getStringArrayExtra(KEY_URLS));
        imagePager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                realPos = position;
                tvPagerTitle.setText(String.format("%d/%d", position + 1, pagerAdapter.getCount()));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        imagePager.setAdapter(pagerAdapter);
        tvPagerTitle.setText(String.format("%d/%d", realPos+1, pagerAdapter.getCount()));
        if (realPos > 1) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    imagePager.setCurrentItem(realPos, false);
                }
            }, 50);
        }
    }

    private void startDownloadPhone() {
        if (Environment.isExternalStorageEmulated()) {
            apkFileName = String.format(getIntent().getStringExtra(KEY_NAME) + "_%s.jpg", realPos + "");
            String imageurl = urls[realPos];
            ROOTPATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "DCIM/";
            FileDownloader
                    .getImpl()
                    .create(imageurl)
                    .setPath(ROOTPATH + apkFileName)
                    .setForceReDownload(true)
                    .setListener(apkDownloadListener).start();
        } else {
            ToastUtils.showRoundRectToast("sd卡不可用!");
        }
    }

    private FileDownloadListener apkDownloadListener = new FileDownloadListener() {
        @Override
        protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {

        }

        @Override
        protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {

        }

        @Override
        protected void completed(BaseDownloadTask task) {
            // 其次把文件插入到系统图库
            try {
                MediaStore.Images.Media.insertImage(context.getContentResolver(),
                        ROOTPATH, apkFileName, null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            // 最后通知图库更新
//        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(ROOTPATH))));
            T.showShort("下载完成");
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
                new CommAlertDialog.Builder(context)
                        .setNotice("存储空间严重不足,去清理?")
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
            } else {
                ToastUtils.showRoundRectToast(String.format("下载异常!"));
            }
        }

        @Override
        protected void warn(BaseDownloadTask task) {

        }
    };


    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_STORAGE_WRITE_ACCESS_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startDownloadPhone();
                } else {
                    showLongSnackBar("文件写入权限被拒绝,请到设置页面打开app文件写入权限!");
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @OnClick({R.id.tvsetbg, R.id.tvDownload})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvsetbg:
                Observable.create(new Observable.OnSubscribe<Object>() {
                    @Override
                    public void call(Subscriber<? super Object> subscriber) {
                        try {
                            String imageurl = urls[realPos];
                            wallpaperManager.setBitmap(SystemUtils.getBitmap(imageurl));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).subscribeOn(Schedulers.newThread())
                        .subscribe();
                break;
            case R.id.tvDownload:
                if (SystemUtils.checkPermissions(context,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE})) {
                    startDownloadPhone();
                } else {
                    ToastUtils.showFillToast("请务必打开文件写入权限!");
                    SystemUtils.reqPermission(context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE_WRITE_ACCESS_PERMISSION);
                }
                break;
        }
    }

    private class SamplePagerAdapter extends PagerAdapter {

        public String[] imgs;


        public SamplePagerAdapter(String[] urls) {
            imgs = urls;
        }

        @Override
        public int getCount() {
            return imgs == null ? 0 : imgs.length;
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
//            PhotoView photoView = new PhotoView(container.getContext());
            View view = getLayoutInflater().inflate(R.layout.activity_image_pager_item, null);
            PhotoView photoView = (PhotoView) view.findViewById(R.id.pager_item_photo);


            photoView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            // Now just add PhotoView to ViewPager and return it
            container.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            final PhotoViewAttacher attacher = new PhotoViewAttacher(photoView);
            attacher.setMinimumScale(0.5f);
            attacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float x, float y) {
                    ImagePagerBGActivity.this.finish();
                }

                @Override
                public void onOutsidePhotoTap() {
                }
            });
            String url = imgs[position];
            if (GlideUtils.canLoadImage(context)) {
                Glide.with(context)
                        .load(url)
                        .placeholder(R.mipmap.cat_loging)
                        .error(R.mipmap.cat_loging)
                        .crossFade()
                        .into(photoView);
            }
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }
}
