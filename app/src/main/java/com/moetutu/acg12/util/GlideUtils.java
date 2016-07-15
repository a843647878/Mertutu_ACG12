package com.moetutu.acg12.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.moetutu.acg12.R;

/**
 * Description
 * Glide 工具类
 * Company Beijing guokeyuzhou
 * author  youxuan  E-mail:xuanyouwu@163.com
 * date createTime：16/7/11
 * version
 */

public class GlideUtils {

    private GlideUtils() {
    }

    /**
     * 加载用户 头像 等
     *
     * @param context
     * @param path
     * @param imageView
     */
    public static void loadUser(Context context, String path, ImageView imageView) {
        if (context == null) return;
        if (imageView == null) return;
        Glide.with(context)
                .load(path)
                .fitCenter()
                .placeholder(R.mipmap.home_pressed)
                .error(R.mipmap.home_pressed)
                .into(imageView);
    }

    /**
     * 加载图片
     *
     * @param context
     * @param path
     * @param imageView
     */
    public static void loadCourse(Context context, String path, ImageView imageView) {
        if (context == null) return;
        if (imageView == null) return;
        Glide.with(context)
                .load(path)
                .fitCenter()
                .placeholder(R.mipmap.loginicon)
                .error(R.mipmap.loginicon)
                .into(imageView);
    }

}

