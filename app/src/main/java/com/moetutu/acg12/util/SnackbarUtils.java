package com.moetutu.acg12.util;

import android.app.Activity;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;


import com.moetutu.acg12.R;


/**
 * Description
 * Company Beijing guokeyuzhou
 * author  youxuan  E-mail:xuanyouwu@163.com
 * date createTime：16/6/21
 * version
 */
public class SnackbarUtils {

    private static Snackbar makeSnackBar(View view, CharSequence snack, int duration) {
        Snackbar snackbar = Snackbar.make(view, snack, Snackbar.LENGTH_SHORT);
        snackbar.getView().setBackgroundColor(view.getContext().getResources().getColor(com.moetutu.acg12.R.color.gray));
        ((TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text)).setTextColor(Color.WHITE);
        return snackbar;
    }

    public static void showSnack(View view, CharSequence snack) {
        if (view == null) return;
        if (TextUtils.isEmpty(snack)) return;
        makeSnackBar(view, snack, Snackbar.LENGTH_SHORT).show();
    }


    public static void showSnack(View view, int resId) {
        if (view == null) return;
        String resStr = view.getContext().getResources().getString(resId);
        if (TextUtils.isEmpty(resStr)) return;
        showSnack(view, resStr);
    }

    public static void showSnack(Activity activity, String msg) {
        if (activity == null) return;
        if (TextUtils.isEmpty(msg)) return;
        showSnack(activity.getWindow().getDecorView(), msg);
    }


    public static void showLongSnack(Activity activity, String msg) {
        if (activity == null) return;
        if (TextUtils.isEmpty(msg)) return;
        View view = activity.getWindow().getDecorView();
        if (view == null) return;
        makeSnackBar(view, msg, Snackbar.LENGTH_LONG).show();
    }

}
