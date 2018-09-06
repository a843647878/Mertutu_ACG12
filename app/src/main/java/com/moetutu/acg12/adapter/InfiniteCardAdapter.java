package com.moetutu.acg12.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.moetutu.acg12.R;
import com.moetutu.acg12.entity.ArticleEntity;
import com.moetutu.acg12.util.GlideUtils;
import com.moetutu.acg12.view.widget.BaseArrayRecyclerAdapter;
import com.moetutu.acg12.view.widget.BaseRecyclerAdapter;

import java.util.List;

/**
 * Description
 * Created by chengwanying on 2017/8/4.
 * Company BeiJing kaimijiaoyu
 */

public class InfiniteCardAdapter extends BaseAdapter {


    private List<ArticleEntity> posts;



    public InfiniteCardAdapter(List<ArticleEntity> posts) {
        this.posts = posts;
    }

    @Override
    public int getCount() {
        return posts.size();
    }

    @Override
    public ArticleEntity getItem(int position) {
        return posts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, null);
            holder.img = (ImageView) convertView.findViewById(R.id.biaoqian_img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        GlideUtils.loadDetails(holder.img.getContext(),getItem(position).thumbnail.url,holder.img);
        return convertView;
    }


}

//在外面先定义，ViewHolder静态类
class ViewHolder {
    public ImageView img;
}
