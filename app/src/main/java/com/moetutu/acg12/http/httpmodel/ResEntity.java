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

    public static final String CODE_STATUS = "success";//完全成功
    public static final int CODE_SUCESS = 2000;//完全成功
    public static final int CODE_TOKEN_INVALID = 4000;//Token 失效

    private static final String FIELD_ERROR_STATUS = "status";
    private static final String FIELD_ERROR_CODE = "error_code";
    private static final String FIELD_ERROR_MESSAGE = "error_message";
    private static final String FIELD_DATA = "data";
    private static final String FIELD_POSTS = "posts";

    @SerializedName(FIELD_ERROR_STATUS)
    public String status;

    @SerializedName(FIELD_ERROR_CODE)
    public int error_code;

    @SerializedName(FIELD_ERROR_MESSAGE)
    public String error_message;

    @SerializedName(FIELD_DATA)
    public T data;

    @SerializedName(FIELD_POSTS)
    public T posts;
}
