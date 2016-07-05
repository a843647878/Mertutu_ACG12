package com.moetutu.acg12.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.moetutu.acg12.R;
import com.moetutu.acg12.entity.ChMedicCircleRecEntity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Description
 * Company guokeyuzhou
 * Created by chengwanying on 16/7/5.
 * version
 */
public class ComicShufflingAdapter extends PagerAdapter {
    private final ArrayList<ChMedicCircleRecEntity> subjectsInfos = new ArrayList<>();
    private Context context;

    public ComicShufflingAdapter(Context context) {
        this.context = context;
    }


    public void bindData(List<ChMedicCircleRecEntity> datas) {
        subjectsInfos.clear();
        subjectsInfos.addAll(datas);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return subjectsInfos.size();
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comic_shuffling_circ, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_movie);
        TextView title = (TextView) view.findViewById(R.id.tv_title);
        TextView desc = (TextView) view.findViewById(R.id.tv_desc);
        ChMedicCircleRecEntity subjectsInfo = subjectsInfos.get(position);
        if (subjectsInfo != null) {
            title.setText(subjectsInfo.title);
            desc.setText(subjectsInfo.desc);
            Picasso.with(context)
                    .load(subjectsInfo.img)
                    .fit()
                    .placeholder(R.mipmap.loginicon)
                    .config(Bitmap.Config.RGB_565)
                    .error(R.mipmap.loginicon)
                    .into(imageView);
        }
        container.addView(view);
        return view;
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}
