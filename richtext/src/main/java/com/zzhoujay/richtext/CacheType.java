package com.zzhoujay.richtext;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by zhou on 2016/12/5.
 * CacheType
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({CacheType.NONE, CacheType.LAYOUT, CacheType.ALL})
public @interface CacheType {
    int NONE = 0; // 不进行缓存
    int LAYOUT = 1; // 只缓存文字样式和图片大小信息
    int ALL = 2; // 在LAYOUT的基础上还缓存图片，使用前需先设置缓存目录
}
