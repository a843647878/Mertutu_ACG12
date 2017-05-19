package com.moetutu.acg12.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.moetutu.acg12.app.AppContext;

/**
 * Description
 * Company Beijing guokeyuzhou
 * author  youxuan  E-mail:xuanyouwu@163.com
 * date createTime：16/7/3
 * version
 * 缓存类
 */
public class SpUtils {
    public static final String DEFAULT_FILE = "acg12_cache_data";
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;


    private SpUtils() {
        sp = AppContext.getApplication().getSharedPreferences(DEFAULT_FILE, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    private SharedPreferences.Editor getEditor() {
        if (editor == null) {
            editor = sp.edit();
        }
        return editor;
    }

    static SpUtils instance;

    public static SpUtils getInstance() {
        if (instance == null) {
            instance = new SpUtils();
        }
        return instance;
    }


    public void putData(String key, String data) {
        getEditor().putString(key, data).commit();
    }

    public void putData(String key, boolean data) {
        getEditor().putBoolean(key, data).commit();
    }

    public void putData(String key, int data) {
        getEditor().putInt(key, data).commit();
    }

    public void putData(String key, long data) {
        getEditor().putLong(key, data).commit();
    }

    public void putData(String key, float data) {
        getEditor().putFloat(key, data).commit();
    }

    public String getStringData(String key, String defaultString) {
        if (sp.contains(key)) {
            return sp.getString(key, defaultString);
        }
        return defaultString;
    }

    public boolean getBooleanData(String key, boolean defaultBool) {
        if (sp.contains(key)) {
            return sp.getBoolean(key, defaultBool);
        }
        return defaultBool;
    }

    public int getIntData(String key, int defaultInt) {
        if (sp.contains(key)) {
            return sp.getInt(key, defaultInt);
        }
        return defaultInt;
    }

    public long getLongData(String key, long defaultLong) {
        if (sp.contains(key)) {
            return sp.getLong(key, defaultLong);
        }
        return defaultLong;
    }

    public float getFloatData(String key, float defaultFloat) {
        if (sp.contains(key)) {
            return sp.getFloat(key, defaultFloat);
        }
        return defaultFloat;
    }


}
