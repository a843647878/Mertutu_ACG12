package com.moetutu.acg12.util.transformations;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.module.GlideModule;

/**
 * @author xuanyouwu
 * @email xuanyouwu@163.com
 * @time 2016-09-21 19:43
 * 增加清晰度  避免浅绿色背景
 */

public class GlideConfiguration implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
