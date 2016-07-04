package com.moetutu.acg12.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/**
 * 配置文件辅助类 SharedPreferences
 */
public class PreferenceUtils {
	// ------get-------------------------------------------
	/**
	 * 默认SharedPreferences 从配置文件获取一个Int类型数字
	 * 
	 * @param context
	 *            上下文环境
	 * @param key
	 *            对应键名
	 * @param defaultValue
	 *            默认值
	 * @return int
	 */
	public static int getPrefInt(Context context, final String key,
			final int defaultValue) {
		final SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		return settings.getInt(key, defaultValue);
	}

	/**
	 * 从配置文件获取一个Int类型数字
	 * 
	 * @param context
	 *            上下文环境
	 * @param fileName
	 *            文件名称
	 * @param mode
	 *            操作模式
	 * @param key
	 *            对应键名
	 * @param defaultValue
	 *            默认值
	 * @return int
	 */
	public static int getPrefInt(Context context, String fileName, int mode,
			final String key, final int defaultValue) {
		final SharedPreferences settings = context.getSharedPreferences(
				fileName, mode);
		return settings.getInt(key, defaultValue);
	}

	/**
	 * 默认SharedPreferences 从配置文件获取一个long类型数字
	 * 
	 * @param context
	 *            上下文环境
	 * @param key
	 *            对应键名
	 * @param defaultValue
	 *            默认值
	 * @return long
	 */
	public static long getPrefLong(Context context, final String key,
			final long defaultValue) {
		final SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		return settings.getLong(key, defaultValue);
	}

	/**
	 * 从配置文件获取一个long类型数字
	 * 
	 * @param context
	 *            上下文环境
	 * @param fileName
	 *            文件名称
	 * @param mode
	 *            操作模式
	 * @param key
	 *            对应键名
	 * @param defaultValue
	 *            默认值
	 * @return long
	 */
	public static long getPrefLong(Context context, String fileName, int mode,
			final String key, final long defaultValue) {
		final SharedPreferences settings = context.getSharedPreferences(
				fileName, mode);
		return settings.getLong(key, defaultValue);
	}

	/**
	 * 默认SharedPreferences 从配置文件获取一个Float类型数字
	 * 
	 * @param context
	 *            上下文环境
	 * @param key
	 *            对应键名
	 * @param defaultValue
	 *            默认值
	 * @return float
	 */
	public static float getPrefFloat(Context context, final String key,
			final float defaultValue) {
		final SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		return settings.getFloat(key, defaultValue);
	}

	/**
	 * 从配置文件获取一个Float类型数字
	 * 
	 * @param context
	 *            上下文环境
	 * @param fileName
	 *            文件名称
	 * @param mode
	 *            操作模式
	 * @param key
	 *            对应键名
	 * @param defaultValue
	 *            默认值
	 * @return float
	 */
	public static float getPrefFloat(Context context, String fileName,
			int mode, final String key, final float defaultValue) {
		final SharedPreferences settings = context.getSharedPreferences(
				fileName, mode);
		return settings.getFloat(key, defaultValue);
	}

	/**
	 * 默认SharedPreferences 从配置文件获取一个Boolean类型数字
	 * 
	 * @param context
	 *            上下文环境
	 * @param key
	 *            对应键名
	 * @param defaultValue
	 *            默认值
	 * @return boolean
	 */
	public static boolean getPrefBoolean(Context context, final String key,
			final boolean defaultValue) {
		final SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		return settings.getBoolean(key, defaultValue);
	}

	/**
	 * 从配置文件获取一个Boolean类型数字
	 * 
	 * @param context
	 *            上下文环境
	 * @param fileName
	 *            文件名称
	 * @param mode
	 *            操作模式
	 * @param key
	 *            对应键名
	 * @param defaultValue
	 *            默认值
	 * @return boolean
	 */
	public static boolean getPrefBoolean(Context context, String fileName,
			int mode, final String key, final boolean defaultValue) {
		final SharedPreferences settings = context.getSharedPreferences(
				fileName, mode);
		return settings.getBoolean(key, defaultValue);
	}

	/**
	 * 默认SharedPreferences 从配置文件获取一个String类型数字
	 * 
	 * @param context
	 *            上下文环境
	 * @param key
	 *            对应键名
	 * @param defaultValue
	 *            默认值
	 * @return string
	 */
	public static String getPrefString(Context context, String key,
			final String defaultValue) {
		final SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		return settings.getString(key, defaultValue);
	}

	/**
	 * 从配置文件获取一个String类型数字
	 * 
	 * @param context
	 *            上下文环境
	 * @param fileName
	 *            文件名称
	 * @param mode
	 *            操作模式
	 * @param key
	 *            对应键名
	 * @param defaultValue
	 *            默认值
	 * @return string
	 */
	public static String getPrefString(Context context, String fileName,
			int mode, String key, final String defaultValue) {
		final SharedPreferences settings = context.getSharedPreferences(
				fileName, mode);
		return settings.getString(key, defaultValue);
	}

	// ------set--------------------------------------
	/**
	 * 默认SharedPreferences 在配置文件保存一个Int类型数字
	 * 
	 * @param context
	 *            上下文环境
	 * @param key
	 *            对应键名
	 * @param value
	 *            对应值
	 */
	public static void setPrefInt(Context context, final String key,
			final int value) {
		final SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		settings.edit().putInt(key, value).commit();
	}

	/**
	 * 在配置文件保存一个Int类型数字
	 * 
	 * @param context
	 *            上下文环境
	 * @param fileName
	 *            文件名称
	 * @param mode
	 *            操作模式
	 * @param key
	 *            对应键名
	 * @param value
	 *            对应值
	 */
	public static void setPrefInt(Context context, String fileName, int mode,
			final String key, final int value) {
		final SharedPreferences settings = context.getSharedPreferences(
				fileName, mode);
		;
		settings.edit().putInt(key, value).commit();
	}

	/**
	 * 默认SharedPreferences 在配置文件保存一个long类型数字
	 * 
	 * @param context
	 *            上下文环境
	 * @param key
	 *            对应键名
	 * @param value
	 *            对应值
	 */
	public static void setSettingLong(Context context, final String key,
			final long value) {
		final SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		settings.edit().putLong(key, value).commit();
	}

	/**
	 * 在配置文件保存一个long类型数字
	 * 
	 * @param context
	 *            上下文环境
	 * @param fileName
	 *            文件名称
	 * @param mode
	 *            操作模式
	 * @param key
	 *            对应键名
	 * @param value
	 *            对应值
	 */
	public static void setSettingLong(Context context, String fileName,
			int mode, final String key, final long value) {
		final SharedPreferences settings = context.getSharedPreferences(
				fileName, mode);
		settings.edit().putLong(key, value).commit();
	}

	/**
	 * 默认SharedPreferences 在配置文件保存一个Float类型数字
	 * 
	 * @param context
	 *            上下文环境
	 * @param key
	 *            对应键名
	 * @param value
	 *            对应值
	 */
	public static void setPrefFloat(Context context, final String key,
			final float value) {
		final SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		settings.edit().putFloat(key, value).commit();
	}

	/**
	 * 在配置文件保存一个Float类型数字
	 * 
	 * @param context
	 *            上下文环境
	 * @param fileName
	 *            文件名称
	 * @param mode
	 *            操作模式
	 * @param key
	 *            对应键名
	 * @param value
	 *            对应值
	 */
	public static void setPrefFloat(Context context, String fileName, int mode,
			final String key, final float value) {
		final SharedPreferences settings = context.getSharedPreferences(
				fileName, mode);
		settings.edit().putFloat(key, value).commit();
	}

	/**
	 * 默认SharedPreferences 在配置文件保存一个Boolean类型数字
	 * 
	 * @param context
	 *            上下文环境
	 * @param key
	 *            对应键名
	 * @param value
	 *            对应值
	 */
	public static void setPrefBoolean(Context context, final String key,
			final boolean value) {
		final SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		settings.edit().putBoolean(key, value).commit();
	}

	/**
	 * 在配置文件保存一个Boolean类型数字
	 * 
	 * @param context
	 *            上下文环境
	 * @param fileName
	 *            文件名称
	 * @param mode
	 *            操作模式
	 * @param key
	 *            对应键名
	 * @param value
	 *            对应值
	 */
	public static void setPrefBoolean(Context context, String fileName,
			int mode, final String key, final boolean value) {
		final SharedPreferences settings = context.getSharedPreferences(
				fileName, mode);
		settings.edit().putBoolean(key, value).commit();
	}

	/**
	 * 默认SharedPreferences 在配置文件保存一个String类型数字
	 * 
	 * @param context
	 *            上下文环境
	 * @param key
	 *            对应键名
	 * @param value
	 *            对应值
	 */
	public static void setPrefString(Context context, final String key,
			final String value) {
		final SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		settings.edit().putString(key, value).commit();
	}

	/**
	 * 在配置文件保存一个String类型数字
	 * 
	 * @param context
	 *            上下文环境
	 * @param fileName
	 *            文件名称
	 * @param mode
	 *            操作模式
	 * @param key
	 *            对应键名
	 * @param value
	 *            对应值
	 */
	public static void setPrefString(Context context, String fileName,
			int mode, final String key, final String value) {
		final SharedPreferences settings = context.getSharedPreferences(
				fileName, mode);
		settings.edit().putString(key, value).commit();
	}

	// -----hasKey--------------------------
	/**
	 * 默认SharedPreferences 判断键名是否重复
	 * 
	 * @param context
	 * @param key
	 * @return
	 */
	public static boolean hasKey(Context context, final String key) {
		return PreferenceManager.getDefaultSharedPreferences(context).contains(
				key);
	}

	/**
	 * 判断键名是否重复
	 * 
	 * @param context
	 * @param fileName
	 *            文件名称
	 * @param mode
	 *            操作模式
	 * @param key
	 * @return boolean
	 */
	public static boolean hasKey(Context context, String fileName, int mode,
			final String key) {
		return context.getSharedPreferences(fileName, mode).contains(key);
	}

	// -----clear--------------------------
	/**
	 * 默认SharedPreferences 清除配置信息
	 * 
	 * @param context
	 *            上下文环境
	 */
	public static void clearPreference(Context context) {
		final Editor editor = PreferenceManager.getDefaultSharedPreferences(
				context).edit();
		editor.clear();
		editor.commit();
	}

	/**
	 * 清除配置信息
	 * 
	 * @param context
	 *            上下文环境
	 * @param fileName
	 *            文件名称
	 * @param mode
	 *            操作模式
	 */
	public static void clearPreference(Context context, String fileName,
			int mode) {
		final Editor editor = context.getSharedPreferences(fileName, mode)
				.edit();
		editor.clear();
		editor.commit();
	}
}