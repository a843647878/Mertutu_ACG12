package com.moetutu.acg12.adapter;

import android.net.Uri;
import android.widget.ImageView;

import com.moetutu.acg12.R;
import com.moetutu.acg12.util.GlideUtils;
import com.moetutu.acg12.util.HtmlAcgUtil;
import com.moetutu.acg12.util.LogUtils;
import com.moetutu.acg12.view.widget.BaseArrayRecyclerAdapter;

/**
 * Created by chengwanying on 16/5/29.
 */
public class WenDangAdapter extends BaseArrayRecyclerAdapter<String> {


    @Override
    public void onBindHoder(ViewHolder holder, String s, int position) {
        if (s == null) return;
        ImageView image = holder.obtainView(R.id.biaoqian_img);
        Uri uri = null;
        if (HtmlAcgUtil.isHttps(s)) {
            uri = Uri.parse(s);
        } else {
            uri = Uri.parse("https:" + s);
            GlideUtils.loadDetails(image.getContext(), s, image);
        }
        LogUtils.d("---------uri:"+uri.toString());
        image.setImageURI(uri);
    }

    @Override
    public int bindView(int viewtype) {
        return R.layout.item_image;
    }

}
