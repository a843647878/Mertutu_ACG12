package sj.keyboard.common.widget;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.RelativeLayout;


import com.keyboard.view.R;

import java.util.ArrayList;

import sj.keyboard.common.adapter.AppsAdapter;
import sj.keyboard.common.data.AppBean;
import sj.keyboard.widget.BaseRecyclerAdapter;

public class SimpleAppsGridView extends RelativeLayout {

    protected View view;

    public SimpleAppsGridView(Context context) {
        this(context, null);
    }

    public SimpleAppsGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.view_apps, this);
        init();
    }

    protected BaseRecyclerAdapter.OnItemClickListener onItemClickListener;

    public BaseRecyclerAdapter.OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(BaseRecyclerAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        if (adapter == null) return;
        adapter.setOnItemClickListener(this.onItemClickListener);
    }

    AppsAdapter adapter;
    RecyclerView rv_apps;

    protected void init(){
        rv_apps = (RecyclerView) view.findViewById(R.id.rv_apps);
        rv_apps.setLayoutManager(new GridLayoutManager(rv_apps.getContext(), 4));

        ArrayList<AppBean> mAppBeanList = new ArrayList<>();

        mAppBeanList.add(new AppBean(R.mipmap.chatting_photo, "图片"));
        mAppBeanList.add(new AppBean(R.mipmap.chatting_camera, "拍照"));

        if (adapter == null){
            adapter = new AppsAdapter();
            rv_apps.setAdapter(adapter);
        }
        adapter.bindData(true, mAppBeanList);

    }
}
