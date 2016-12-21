package com.moetutu.acg12.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.moetutu.acg12.R;
import com.moetutu.acg12.util.HtmlAcgUtil;
import com.moetutu.acg12.view.HackyViewPager;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;


/**
 * Description   图片阅览界面
 * Company guokeyuzhou
 * Created by chengwanying on 16/6/15.
 * version
 */

public class ImagePagerActivity extends BaseActivity {

    private static final String KEY_URLS = "key_urls";
    private static final String KEY_POS = "key_pos";

    SamplePagerAdapter pagerAdapter;
    String[] urls;
    Handler handler = new Handler();
    int realPos;
    @BindView(R.id.imagePager)
    HackyViewPager imagePager;
    @BindView(R.id.tvPagerTitle)
    TextView tvPagerTitle;


    public static void launch(Context context, String[] urls, int pos) {
        if (context == null) return;
        if (urls == null) return;
        if (urls.length == 0) return;
        Intent intent = new Intent(context, ImagePagerActivity.class);
        intent.putExtra(KEY_URLS, urls);
        intent.putExtra(KEY_POS, pos);
        context.startActivity(intent);
    }

    public static void launch(Context context, String[] urls) {
        launch(context, urls, 1);
    }

    public static void launch(Context context, List<String> urls) {
        launch(context, urls, 1);
    }

    public static void launch(Context context, List<String> urls, int pos) {
        if (context == null) return;
        if (urls == null) return;
        if (urls.size() == 0) return;
        String[] urlsArr = (String[]) urls.toArray(new String[urls.size()]);
        Intent intent = new Intent(context, ImagePagerActivity.class);
        intent.putExtra(KEY_URLS, urlsArr);
        intent.putExtra(KEY_POS, pos);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_pager);
        ButterKnife.bind(this);

        urls = getIntent().getStringArrayExtra(KEY_URLS);
        realPos = getIntent().getIntExtra(KEY_POS, 1);
        if (realPos < 1) {
            realPos = 1;
        } else if (realPos > urls.length) {
            realPos = urls.length;
        }
        pagerAdapter = new SamplePagerAdapter(getIntent().getStringArrayExtra(KEY_URLS));
        imagePager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tvPagerTitle.setText(String.format("%d/%d", position + 1, pagerAdapter.getCount()));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        imagePager.setAdapter(pagerAdapter);
        tvPagerTitle.setText(String.format("%d/%d", realPos, pagerAdapter.getCount()));
        if (realPos > 1) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    imagePager.setCurrentItem(realPos - 1, false);
                }
            }, 50);
        }
    }


    private class SamplePagerAdapter extends PagerAdapter {

        public String[] imgs;

        public SamplePagerAdapter(String[] urls) {
            imgs = urls;
        }

        @Override
        public int getCount() {
            return imgs == null ? 0 : imgs.length;
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            photoView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            // Now just add PhotoView to ViewPager and return it
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            final PhotoViewAttacher attacher = new PhotoViewAttacher(photoView);
            attacher.setMinimumScale(0.5f);
            attacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float x, float y) {
                    ImagePagerActivity.this.finish();
                }

                @Override
                public void onOutsidePhotoTap() {
                }
            });

            String url = imgs[position];
            if (!TextUtils.isEmpty(url)) {
                if (HtmlAcgUtil.isHttps(url)){
                    Picasso.with(context)
                            .load(url)
                            .placeholder(R.mipmap.cat_loging)
                            .error(R.mipmap.cat_loging)
                            .into(photoView, new Callback() {
                                @Override
                                public void onSuccess() {
                                    if (attacher != null)
                                        attacher.update();
                                }

                                @Override
                                public void onError() {
                                }
                            });
                }else {
                    Picasso.with(context)
                            .load("https:" + url)
                            .placeholder(R.mipmap.cat_loging)
                            .error(R.mipmap.cat_loging)
                            .into(photoView, new Callback() {
                                @Override
                                public void onSuccess() {
                                    if (attacher != null)
                                        attacher.update();
                                }

                                @Override
                                public void onError() {
                                }
                            });
                }

            }

            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }
}
