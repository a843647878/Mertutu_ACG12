package com.moetutu.acg12.http;


import com.moetutu.acg12.BuildConfig;

/**
 * @author xuanyouwu
 * @email xuanyouwu@163.com
 * @time 2016-04-15 14:14
 */
public class HConst {

    //测试服务器
    public static final String BASE_DEBUG_URL = "http://acg12.com/wp-admin/";

    //正式服务器
    public static final String BASE_RELEASE_URL = "http://acg12.com/wp-admin/";


    public static String BASE_HTTP = BASE_DEBUG_URL;

    static {
        if (BuildConfig.IS_DEBUG) {
            BASE_HTTP = BASE_DEBUG_URL;
        } else {
            BASE_HTTP = BASE_RELEASE_URL;
        }
    }


    /**
     * 网络缓存最大值
     */
    public static final int CACHE_MAXSIZE = 1024 * 1024 * 30;

    /**
     * 网络缓存保存时间
     */
    public static final int TIME_CACHE = 60 * 60; // 一小时

    /**
     * 接口请求超时时间
     */
    public static final int SOCKET_TIME_OUT = 20_000;

    /**
     * 接口响应超时时间  目前服务器压力大
     */
    public static final int SOCKET_RESPONSE_TIME_OUT = 20_000;


    /**
     * 允许http日志
     */
    public static boolean HTTP_LOG_ENABLE = BuildConfig.IS_DEBUG;

    // Android 渠道
    public static final String OS_TYPE="1";

}
