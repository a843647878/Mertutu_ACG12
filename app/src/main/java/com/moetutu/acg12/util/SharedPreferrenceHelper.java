package com.moetutu.acg12.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * Description
 * Created by chengwanying on 2016/12/5.
 * Company BeiJing guokeyuzhou
 */

public class SharedPreferrenceHelper {
    private static final String THEME = "theme";

    private static final String COOKIE = "Set-Cookie";

    public static void settheme(Context context, String theme) {
        SharedPreferences sp = context.getSharedPreferences("acg", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(THEME, theme);
        editor.apply();
    }

    public static String gettheme(Context context) {
        SharedPreferences sp = context.getSharedPreferences("acg", Context.MODE_PRIVATE);
        return sp.getString(THEME, "1");
    }


    public static void setCookie(Context context, String theme) {
        SharedPreferences sp = context.getSharedPreferences("acg", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(COOKIE, theme);
        editor.apply();
    }

    public static String getCookie(Context context) {
        SharedPreferences sp = context.getSharedPreferences("acg", Context.MODE_PRIVATE);
        String cookie = sp.getString(COOKIE, null);
        return TextUtils.isEmpty(cookie)?"":cookie;
    }
}
