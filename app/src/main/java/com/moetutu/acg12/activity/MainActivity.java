package com.moetutu.acg12.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Description
 * Company guokeyuzhou
 * Created by chengwanying on 16/7/1.
 * version
 */
public class MainActivity extends BaseActivity{

    public static void lunch(Context context){
        if (context == null) return;
        Intent in = new Intent(context , MainActivity.class);
        context.startActivity(in);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
