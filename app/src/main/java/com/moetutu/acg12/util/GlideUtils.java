package com.moetutu.acg12.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.moetutu.acg12.R;
import com.moetutu.acg12.util.transformations.BlurTransformation;
import com.moetutu.acg12.util.transformations.GlideCircleTransform;

/**
 * Description
 * Glide 工具类
 * Company Beijing guokeyuzhou
 * author  youxuan  E-mail:xuanyouwu@163.com
 * date createTime：16/7/11
 * version
 */

public class GlideUtils {


    /**
     * fragment 中 glide是否可以加载图片
     * 否则 glide会引发崩溃
     *
     * @param fragment
     * @return
     */
    public static boolean canLoadImage(Fragment fragment) {
        if (fragment == null) {
            return false;
        }
        FragmentActivity parentActivity = fragment.getActivity();
        return canLoadImage(parentActivity);
    }

    /**
     * context 中 glide是否可以加载图片
     * 否则 glide会引发崩溃
     *
     * @param context
     * @return
     */
    public static boolean canLoadImage(Context context) {
        if (context == null) {
            return false;
        }
        if (!(context instanceof Activity)) {
            return true;
        }
        Activity activity = (Activity) context;
        return canLoadImage(activity);
    }

    /**
     * activity 中 glide是否可以加载图片
     * 否则 glide会引发崩溃
     *
     * @param activity
     * @return
     */
    public static boolean canLoadImage(Activity activity) {
        if (activity == null) {
            return false;
        }
        boolean destroyed = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 &&
                activity.isDestroyed();
        if (destroyed || activity.isFinishing()) {
            return false;
        }
        return true;
    }


    private GlideUtils() {
    }

    /**
     * 加载用户 头像 等 圆角
     *
     * @param context
     * @param path
     * @param imageView
     */
    public static void loadUser(Context context, String path, ImageView imageView) {
        if (context == null) return;
        if (imageView == null) return;
        if (context instanceof Activity) {
            if (((Activity) context).isFinishing())
                return;
        }
        Glide.with(context)
                .load(path)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(new GlideCircleTransform(context))
                .placeholder(R.mipmap.icon_defaulthead)
                .error(R.mipmap.icon_defaulthead)
                .crossFade()
                .into(imageView);
    }

    /**
     * 加载用户 头像 等 圆角
     *
     * @param context
     * @param path
     * @param imageView
     */
    public static void loadUser(Context context, int path, ImageView imageView) {
        if (context == null) return;
        if (imageView == null) return;
        if (context instanceof Activity) {
            if (((Activity) context).isFinishing())
                return;
        }
        Glide.with(context)
                .load(path)
                .asGif()
                .placeholder(R.mipmap.icon_defaulthead)
                .error(R.mipmap.icon_defaulthead)
                .crossFade()
                .into(imageView);
    }


    /**
     * 加载用户 头像 等 圆角 有边框有 图标
     *
     * @param context
     * @param path
     * @param imageView
     */
    public static void loadUser2(Context context, String path, ImageView imageView, int color, int width, int angle, int img ) {
        if (context == null) return;
        if (imageView == null) return;
        if (context instanceof Activity) {
            if (((Activity) context).isFinishing())
                return;
        }
        Glide.with(context)
                .load(path)
                .transform(new GlideHollowCircleTransform(context,color,width,angle,img))
                .placeholder(R.mipmap.icon_defaulthead)
                .error(R.mipmap.icon_defaulthead)
                .crossFade()
                .into(imageView);
    }

    /*
    只有边框
     */
    public static void loadUser2(Context context, String path, ImageView imageView, int color, int width ) {
        if (context == null) return;
        if (imageView == null) return;
        if (context instanceof Activity) {
            if (((Activity) context).isFinishing())
                return;
        }
        Glide.with(context)
                .load(path)
                .transform(new GlideHollowCircleTransform(context,color,width))
                .placeholder(R.mipmap.home_pressed)
                .error(R.mipmap.home_pressed)
                .crossFade()
                .into(imageView);
    }


    /*
   只有图标
    */
    public static void loadUser2(Context context, String path, ImageView imageView, int angle, int img , boolean isImg) {
        if (context == null) return;
        if (imageView == null) return;
        if (context instanceof Activity) {
            if (((Activity) context).isFinishing())
                return;
        }
        Glide.with(context)
                .load(path)
                .transform(new GlideHollowCircleTransform(context,angle,img,isImg))
                .placeholder(R.mipmap.home_pressed)
                .error(R.mipmap.home_pressed)
                .crossFade()
                .into(imageView);
    }

    /**
     * 某处理
     * @param context
     * @param path
     * @param imageView
     */
    public static void blurImg(Context context, String path, ImageView imageView, int blur){
        Glide.with(context)
                .load(path)
                .bitmapTransform(new BlurTransformation(context,blur))
                .placeholder(R.mipmap.home_pressed)
                .error(R.mipmap.home_pressed)
                .crossFade()
                .into(imageView);
    }


    /**
     * 加载单张图片
     *
     * @param context
     * @param path
     * @param imageView
     */
    public static void loadDetails(Context context, String path, ImageView imageView) {
        if (context == null) return;
        if (imageView == null) return;
        if (context instanceof Activity) {
            if (((Activity) context).isFinishing())
                return;
        }
        Glide.with(context)
                .load(path)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .animate( android.R.anim.fade_in )
                .placeholder(R.mipmap.cat_loging)
                .error(R.mipmap.cat_loging)
                .into(imageView);
    }


    /**
     * 加载背景图
     *
     * @param context
     * @param path
     * @param imageView
     */
    public static void loadUserBG(Context context, Uri path, ImageView imageView) {
        if (context == null) return;
        if (imageView == null) return;
        if (context instanceof Activity) {
            if (((Activity) context).isFinishing())
                return;
        }
        Glide.with(context)
                .load(path)
                .animate(android.R.anim.fade_in )
                .placeholder(R.drawable.gg)
                .error(R.drawable.gg)
                .into(imageView);
    }

    /**
     * 加载慕课图标
     *
     * @param context
     * @param path
     * @param imageView
     */
    public static void loadTcmIcom(Context context, String path, ImageView imageView) {
        if (context == null) return;
        if (imageView == null) return;
        if (context instanceof Activity) {
            if (((Activity) context).isFinishing())
                return;
        }
        Glide.with(context)
                .load(path)
                .transform(new GlideCircleTransform(context))
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .crossFade()  //淡入淡出效果
                .into(imageView);
    }


    /**
     * 加载慕课图标
     *
     * @param context
     * @param path
     * @param imageView
     */
    public static void loadTcmSquareIcom(Context context, String path, ImageView imageView) {
        if (context == null) return;
        if (imageView == null) return;
        if (context instanceof Activity) {
            if (((Activity) context).isFinishing())
                return;
        }
        Glide.with(context)
                .load(path)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .crossFade()
                .into(imageView);
    }


}

