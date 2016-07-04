package com.moetutu.acg12.adapter;

import android.net.Uri;

import com.facebook.drawee.view.SimpleDraweeView;
import com.moetutu.acg12.R;
import com.moetutu.acg12.view.widget.BaseArrayRecyclerAdapter;

/**
 * Created by chengwanying on 16/5/29.
 */
public class WenDangAdapter extends BaseArrayRecyclerAdapter<String>{


    @Override
    public void onBindHoder(ViewHolder holder, String s, int position) {
        if (s == null) return;
        SimpleDraweeView image = holder.obtainView(R.id.biaoqian_img);
        Uri uri = Uri.parse(s);
        image.setAspectRatio(1.33f);
        image.setImageURI(uri);
    }

    @Override
    public int bindView(int viewtype) {
        return R.layout.item_image;
    }

}
