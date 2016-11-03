package com.moetutu.acg12.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

/**
 * @author xuanyouwu
 * @email xuanyouwu@163.com
 * @time 2016-07-20 16:12
 * <p>
 * 网络状态监听
 */

public abstract class NetBroadCastReceiver extends BroadcastReceiver {


    public static IntentFilter getDefaultFilter() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        return filter;
    }

    @Override
    public final void onReceive(Context context, Intent intent) {
        if (TextUtils.equals(intent.getAction(), ConnectivityManager.CONNECTIVITY_ACTION)) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = mConnectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isAvailable()) {

                /////////////网络连接
                String name = netInfo.getTypeName();
                if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    /////WiFi网络
                    isNetAvailable(true, name);
                    onNetChanged(ConnectivityManager.TYPE_WIFI, name);

                } else if (netInfo.getType() == ConnectivityManager.TYPE_ETHERNET) {
                    /////有线网络
                    isNetAvailable(true, name);
                    onNetChanged(ConnectivityManager.TYPE_ETHERNET, name);

                } else if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                    /////////3g网络
                    isNetAvailable(true, name);
                    onNetChanged(ConnectivityManager.TYPE_MOBILE, name);
                }
            } else {
                ////////网络断开
                isNetAvailable(false, "no connect");
                onNetChanged(-1, "no connect");
            }
        }
    }

    public abstract void isNetAvailable(boolean isNetAvailable, String typeName);

    public abstract void onNetChanged(int type, String typeName);

}
