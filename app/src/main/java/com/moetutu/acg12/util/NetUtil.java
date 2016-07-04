package com.moetutu.acg12.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

/**
 * 网络连接帮助类
 */
public class NetUtil {
	/** 无网络连接 */
	public static final int NETWORN_NONE = 0;
	/** wifi连接 */
	public static final int NETWORN_WIFI = 1;
	/** 移动网络连接 */
	public static final int NETWORN_MOBILE = 2;

	/**
	 * 获取网络连接状态
	 * 
	 * @param context
	 *            上下文环境
	 * @return 状态参数值
	 */
	public static int getNetworkState(Context context) {
        // 监控网络（Wi-Fi连接，GPRS，UMTS，等）
        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connManager != null) {
            // Wifi
            State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                    .getState();
            if (state == State.CONNECTED || state == State.CONNECTING) {
                return NETWORN_WIFI;
            }

            // 3G
            NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (networkInfo!=null){
                state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                        .getState();
                if (state == State.CONNECTED || state == State.CONNECTING) {
                    return NETWORN_MOBILE;
                }
            }

            return NETWORN_NONE;
        } else {
            return NETWORN_NONE;
        }
    }
}
