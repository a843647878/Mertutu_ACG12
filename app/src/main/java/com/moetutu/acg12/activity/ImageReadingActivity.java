package com.moetutu.acg12.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bm.library.PhotoView;
import com.moetutu.acg12.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Description   图片阅览界面
 * Company guokeyuzhou
 * Created by chengwanying on 16/6/15.
 * version
 */
public class ImageReadingActivity extends BaseActivity {

    @InjectView(R.id.pager)
    ViewPager pager;
    private List<String> lists = new ArrayList<String>();

    public static void launch(Context context,ArrayList<String> list) {
        if (context == null) return;
        Intent in = new Intent(context, ImageReadingActivity.class);
        in.putStringArrayListExtra("list", list);
        context.startActivity(in);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagereading);
        ButterKnife.inject(this);
        lists = getIntent().getStringArrayListExtra("list");
        initView(this);
    }

    @Override
    public void initView(Activity activity) {
        super.initView(activity);
        pager.setPageMargin((int) (getResources().getDisplayMetrics().density * 15));
        pager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return lists.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                PhotoView view = new PhotoView(ImageReadingActivity.this);
                view.enable();
                view.setScaleType(ImageView.ScaleType.FIT_CENTER);
                Picasso.with(view.getContext())
                        .load(lists.get(position))
                        .placeholder(R.drawable.gg)
                        .error(R.drawable.gg)
                        .into(view);
//                view.setImageResource(imgsId[position]);
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });
    }
}
