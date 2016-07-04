package com.moetutu.acg12.util;

import android.util.Log;

import com.moetutu.acg12.BuildConfig;


/**
 * @author xuanyouwu
 * @email xuanyouwu@163.com
 * @time 2016-05-05 10:38
 */
public class LogUtils {
    public static boolean isDebug = true;
    public static final String TAG = LogUtils.class.getSimpleName();

    static {
        isDebug = BuildConfig.IS_DEBUG;
    }


    public static void d(String msg) {
        if (isDebug)
            Log.d(TAG, msg);
    }

    public static void d(String tag, String msg) {
        if (isDebug)
            Log.d(tag, msg);
    }

    public static void e(String msg) {
        if (isDebug)
            Log.e(TAG, msg);
    }

    public static void w(String msg) {
        if (isDebug)
            Log.w(TAG, msg);
    }

    public static void i(String msg) {
        if (isDebug)
            Log.i(TAG, msg);
    }
}
