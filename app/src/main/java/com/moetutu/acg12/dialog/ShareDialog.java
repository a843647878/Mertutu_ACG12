package com.moetutu.acg12.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.moetutu.acg12.R;
import com.moetutu.acg12.util.LogUtils;
import com.moetutu.acg12.util.ToastUtils;
import com.moetutu.acg12.view.widget.BaseArrayRecyclerAdapter;
import com.moetutu.acg12.view.widget.BaseRecyclerAdapter;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.util.Arrays;
import java.util.List;

/**
 * @author xuanyouwu
 * @email xuanyouwu@163.com
 * @time 2016-11-07 15:04
 * <p>
 * 分享的模板 [微信  朋友圈  QQ  QQ空间]
 * you need add code for your activity:
 * @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
 * super.onActivityResult(requestCode, resultCode, data);
 * UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
 * }
 */

public class ShareDialog extends Dialog implements BaseRecyclerAdapter.OnItemClickListener, UMShareListener {


    private String shareTitle;
    private String shareDescribe="图包、福利、动漫下载,尽在acg12调查小队";
    private String shareIcon;
    private String shareUrl;
    private Activity context;
    private int shareIconByte;

    /**
     * 分享三要素
     *
     * @param context
     * @param shareTitle
     * @param shareIcon
     * @param shareUrl
     */
    public ShareDialog(
            @NonNull Activity context,
            @NonNull String shareTitle,
            @NonNull String shareIcon,
            @Nullable String shareUrl) {
        super(context);
        this.context = context;
        this.shareIcon = shareIcon;
        this.shareTitle = shareTitle;
        this.shareUrl = shareUrl;
        LogUtils.d("----->sharetype1:context:" + context);
        LogUtils.d("----->shareIcon:" + shareIcon);
        LogUtils.d("----->shareTitle:" + shareTitle);
        LogUtils.d("----->shareUrl:" + shareUrl);
    }

    public ShareDialog(
            @NonNull Activity context,
            @NonNull String shareTitle,
            @NonNull int shareIconByte,
            @Nullable String shareUrl) {
        super(context);
        this.context = context;
        this.shareIconByte = shareIconByte;
        this.shareTitle = shareTitle;
        this.shareUrl = shareUrl;
        LogUtils.d("----->sharetype2:context:" + context);
        LogUtils.d("----->shareIconByte:" + shareIconByte);
        LogUtils.d("----->shareTitle:" + shareTitle);
        LogUtils.d("----->shareUrl:" + shareUrl);
    }

    public ShareDialog(
            @NonNull Activity context,
            @NonNull String shareTitle,
            @NonNull String shareDescribe,
            @NonNull int shareIconByte,
            @Nullable String shareUrl) {
        super(context);
        this.context = context;
        this.shareIconByte = shareIconByte;
        this.shareTitle = shareTitle;
        this.shareDescribe = shareDescribe;
        this.shareUrl = shareUrl;
        LogUtils.d("----->sharetype2:context:" + context);
        LogUtils.d("----->shareIconByte:" + shareIconByte);
        LogUtils.d("----->shareTitle:" + shareTitle);
        LogUtils.d("----->shareUrl:" + shareUrl);
    }


    private ShareDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    private ShareDialog(Context context) {
        super(context);
    }

    private ShareDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    TextView dialog_cancel;
    RecyclerView dialog_recyclerView;
    MenuShareAdapter menuShareAdapter;
    private List<ShareMenuEntity> shareMenuEntityList = Arrays.asList(
            new ShareMenuEntity("QQ好友", R.mipmap.ic_share_qq),
            new ShareMenuEntity("QQ空间", R.mipmap.ic_share_qqzone));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_bottom_share);
        Window win = getWindow();
        win.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        win.getDecorView().setPadding(0, 0, 0, 0);
        win.setWindowAnimations(R.style.AnimBottomDialog);
        win.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        win.setGravity(Gravity.BOTTOM);
        win.getAttributes().dimAmount = 0.5f;
        setCanceledOnTouchOutside(true);
        win.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        dialog_recyclerView = (RecyclerView) findViewById(R.id.dialog_recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 4);
        gridLayoutManager.setAutoMeasureEnabled(true);
        dialog_recyclerView.setLayoutManager(gridLayoutManager);
        dialog_recyclerView.setAdapter(menuShareAdapter = new MenuShareAdapter());
        menuShareAdapter.bindData(true, shareMenuEntityList);
        menuShareAdapter.setOnItemClickListener(this);
        dialog_cancel = (TextView) findViewById(R.id.dialog_cancel);
        dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ShareDialog.this.isShowing()) {
                    ShareDialog.this.dismiss();
                }
            }
        });
    }

    @Override
    public void onItemClick(BaseRecyclerAdapter adapter, BaseRecyclerAdapter.ViewHolder holder, View view, int position) {
        dismiss();
        UMWeb umWeb =  new UMWeb(shareUrl);
        //注意 传递null 会崩溃
        if (!TextUtils.isEmpty(shareIcon)) {
            umWeb.setThumb(new UMImage(context, shareIcon));
        } else if (shareIconByte != 0) {
            umWeb.setThumb(new UMImage(context, shareIconByte));
        }
        umWeb.setTitle(shareTitle);//标题
        umWeb.setDescription(shareDescribe);//描述

        ShareAction shareAction = new ShareAction(context)
                .withMedia(umWeb)
                .setCallback(ShareDialog.this);
        switch (position) {
            case 0: {
                shareAction.setPlatform(SHARE_MEDIA.QQ).share();
            }
            break;
            case 1: {
                shareAction.setPlatform(SHARE_MEDIA.QZONE).share();
            }
            break;
            case 2: {
                shareAction.setPlatform(SHARE_MEDIA.WEIXIN).share();

            }
            break;
            case 3: {
                shareAction.setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).share();
            }
            break;
            case 4: {
                shareAction.setPlatform(SHARE_MEDIA.SINA).share();
            }
            break;
            default:
                ToastUtils.showRoundRectToast("渠道没实现!");
                break;
        }
    }

    @Override
    public void onStart(SHARE_MEDIA share_media) {

    }

    /**
     * 分享成功
     *
     * @param share_media
     */
    @Override
    public void onResult(SHARE_MEDIA share_media) {

    }

    /**
     * 分享失败
     *
     * @param share_media
     * @param throwable
     */
    @Override
    public void onError(SHARE_MEDIA share_media, Throwable throwable) {

    }

    /**
     * 分享取消
     *
     * @param share_media
     */
    @Override
    public void onCancel(SHARE_MEDIA share_media) {

    }

    private static class ShareMenuEntity {
        public int icon;
        public String title;

        public ShareMenuEntity(String title, int icon) {
            this.title = title;
            this.icon = icon;
        }
    }

    private static class MenuShareAdapter extends BaseArrayRecyclerAdapter<ShareMenuEntity> {

        @Override
        public void onBindHoder(ViewHolder holder, ShareMenuEntity shareMenuEntity, int position) {
            ImageView iv_share_channel = holder.obtainView(R.id.iv_share_channel);
            TextView tv_share_channel = holder.obtainView(R.id.tv_share_channel);
            iv_share_channel.setImageResource(shareMenuEntity.icon);
            tv_share_channel.setText(shareMenuEntity.title);
        }

        @Override
        public int bindView(int viewtype) {
            return R.layout.item_share_menu;
        }
    }

}
