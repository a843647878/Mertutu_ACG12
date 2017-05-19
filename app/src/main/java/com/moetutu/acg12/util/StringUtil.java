package com.moetutu.acg12.util;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    public static final String NUM_PATTERN = "[0-9]+|\\+|\\%";

    /**
     * 设置字符串中数字的颜色 包含+ %
     *
     * @param numColor
     * @param data
     * @return
     */
    public static SpannableString setNumColor(int numColor, CharSequence data) {
        if (TextUtils.isEmpty(data)) return null;
        SpannableString sbr = new SpannableString(data);
        Pattern pattern = Pattern.compile(NUM_PATTERN);
        Matcher matcher = pattern.matcher(data);
        while (matcher.find()) {
            sbr.setSpan(
                    new ForegroundColorSpan(numColor),
                    matcher.start(),
                    matcher.end(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return sbr;
    }

    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return true 空，false非空
     */
    public static boolean isBlank(String str) {
        boolean b = false;
        if (str == null || "".equals(str) || "null".equals(str)
                || "NULL".equals(str)) {
            b = true;
        } else {
            b = false;
        }
        return b;
    }

    /**
     * 验证是否是邮箱
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9]*[-_.]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }


    /*
    判断是否是数字
     */
    public static boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 截取域中的前10为，为附近
     *
     * @param str
     * @return
     */
    public static String getNearBase(String str, int length) {
        String re = null;
        if (length > 7)
            length = 7;
        if (str.length() > 10) {
            re = str.substring(0, length);
        }
        return re;

    }

    /**
     * 替换字符
     *
     * @param text
     * @param value 要替换成的字符
     * @return
     */
    public static String replace(String text, String value) {
        String[] lines = text.split("&&P&&");
        Pattern pattern = Pattern.compile("\\*\\d+\\*");

        Map<String, String> maps = new HashMap<String, String>();
        for (String line : lines) {
            Matcher matcher = pattern.matcher(line);
            while (matcher.find()) {
                if (!maps.containsKey(matcher.group())) {
                    String key = matcher.group();
                    int len = Integer.parseInt(key.replace("*", ""));
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < len; i++) {
                        sb.append(value);
                    }
                    maps.put(key, sb.toString());
                }
            }
        }
        for (String key : maps.keySet()) {
            text = text.replace(key, maps.get(key));
        }
        return text;
    }

    /**
     * 处理空字符串
     *
     * @param str
     * @return String
     */
    public static String doEmpty(String str) {
        return doEmpty(str, "");
    }

    /**
     * 处理空字符串
     *
     * @param str
     * @param defaultValue
     * @return String
     */
    public static String doEmpty(String str, String defaultValue) {
        if (str == null || str.equalsIgnoreCase("null")
                || str.trim().equals("") || str.trim().equals("－请选择－")) {
            str = defaultValue;
        } else if (str.startsWith("null")) {
            str = str.substring(4, str.length());
        }
        return str.trim();
    }

    /**
     * 拼接List
     *
     * @return String
     */
    public static String listToString(List<String> stringList) {
        if (stringList == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        boolean flag = false;
        for (String string : stringList) {
            if (flag) {
                result.append(",");
            } else {
                flag = true;
            }
            result.append(string);
        }
        return result.toString();
    }


    /**
     * 将异常转换成字符串
     *
     * @param throwable
     * @return
     */
    public static String throwable2string(Throwable throwable) {
        String throwableString = null;
        try {
            StringWriter mStringWriter = new StringWriter();
            PrintWriter mPrintWriter = new PrintWriter(mStringWriter);
            throwable.printStackTrace(mPrintWriter);
            mPrintWriter.close();
            throwableString = mStringWriter.toString();
        } catch (Exception e) {
        }
        return throwableString;
    }

}
