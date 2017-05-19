package sj.keyboard.common.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.keyboard.view.R;

import java.util.ArrayList;

import sj.keyboard.common.data.AppBean;
import sj.keyboard.widget.BaseArrayRecyclerAdapter;
import sj.keyboard.widget.BaseRecyclerAdapter;

public class AppsAdapter extends BaseArrayRecyclerAdapter<AppBean> {

    @Override
    public void onBindHoder(BaseRecyclerAdapter.ViewHolder holder, AppBean appBean, int position) {
        if (appBean == null) return;
        ImageView iv_icon = holder.obtainView(R.id.iv_icon);
        TextView tv_name = holder.obtainView(R.id.tv_name);
        iv_icon.setBackgroundResource(appBean.getIcon());
        tv_name.setText(appBean.getFuncName());
    }

    @Override
    public int bindView(int viewtype) {
        return R.layout.item_app;
    }

}