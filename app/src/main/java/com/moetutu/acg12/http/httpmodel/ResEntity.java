package com.moetutu.acg12.http.httpmodel;

import com.google.gson.annotations.SerializedName;

/**
 * @author xuanyouwu
 * @email xuanyouwu@163.com
 * @time 2016-06-01 16:59
 * <p>
 * 不要轻易修改
 * 不要轻易修改
 */

public class ResEntity<T> {

    public static final int CODE_SUCESS = 0;//完全成功
    public static final int CODE_TOKEN_INVALID = 20001;//Token 失效
    public static final int CODE_INVALIDAPPID = 20002;//无效的 App id 码。
    public static final int CODE_INVALIDAPPSECURE = 20003;//无效的 App secure 码。


    private static final String FIELD_ERROR_CODE = "code";
    private static final String FIELD_ERROR_MESSAGE = "msg";
    private static final String FIELD_DATA = "data";

    @SerializedName(FIELD_ERROR_CODE)
    public int code;

    @SerializedName(FIELD_ERROR_MESSAGE)
    public String msg;

    @SerializedName(FIELD_DATA)
    public T data;
}
