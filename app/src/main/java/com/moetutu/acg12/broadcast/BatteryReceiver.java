package com.moetutu.acg12.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * @author xuanyouwu
 * @email xuanyouwu@163.com
 * @time 2016-10-11 14:19
 * <p>
 * 监听电量的广播
 */

public abstract class BatteryReceiver extends BroadcastReceiver {

    /**
     * 获取电量意图过滤器
     *
     * @return
     */
    public static IntentFilter getDefaultIntentFilter() {
        return new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
    }

    @Override
    public final void onReceive(Context context, Intent intent) {
        int current = intent.getExtras().getInt("level");//获得当前电量
        int total = intent.getExtras().getInt("scale");//获得总电量

        onReceiveBattery(context, intent, current, total);
    }

    public abstract void onReceiveBattery(Context context, Intent intent, int current, int total);
}
