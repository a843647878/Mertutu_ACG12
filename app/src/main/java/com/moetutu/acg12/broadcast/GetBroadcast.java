package com.moetutu.acg12.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;

/**
 * 更新完应用直接打开
 *
 * @author Andy
 */
public class GetBroadcast extends BroadcastReceiver {
    private static GetBroadcast mReceiver = new GetBroadcast();
    private static IntentFilter mIntentFilter;

    public static void registerReceiver(Context context) {
        mIntentFilter = new IntentFilter();
        mIntentFilter.addDataScheme("package");
        mIntentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        // mIntentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        mIntentFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        context.registerReceiver(mReceiver, mIntentFilter);
    }

    public static IntentFilter getmIntentFilter() {
        if (mIntentFilter == null) {
            mIntentFilter = new IntentFilter();
            mIntentFilter.addDataScheme("package");
            mIntentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
            // mIntentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
            mIntentFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        }
        return mIntentFilter;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        String packageName = intent.getData().getSchemeSpecificPart();
        if (Intent.ACTION_PACKAGE_REPLACED.equals(action)
                || Intent.ACTION_PACKAGE_ADDED.equals(context)) {
            PackageManager pm = context.getPackageManager();
            Intent intent1 = new Intent();
            try {
                intent1 = pm.getLaunchIntentForPackage(packageName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
        }
    }

}
