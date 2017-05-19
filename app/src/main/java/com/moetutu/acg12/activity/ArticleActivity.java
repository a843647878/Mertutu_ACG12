package com.moetutu.acg12.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.moetutu.acg12.R;
import com.moetutu.acg12.adapter.WenDangAdapter;
import com.moetutu.acg12.app.AppContext;
import com.moetutu.acg12.entity.ArticleEntity;
import com.moetutu.acg12.entity.PostEntity;

import com.moetutu.acg12.http.RetrofitService;
import com.moetutu.acg12.http.callback.SimpleCallBack;
import com.moetutu.acg12.http.httpmodel.ResEntity;

import com.moetutu.acg12.util.GlideUtils;
import com.moetutu.acg12.util.HtmlAcgUtil;
import com.moetutu.acg12.util.ItemDecorationUtils;
import com.moetutu.acg12.util.T;
import com.moetutu.acg12.view.widget.BaseRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Description
 * Company guokeyuzhou
 * Created by chengwanying on 16/7/5.
 * version
 * 目前已经被抛弃  请用B
 */
@Deprecated
public class ArticleActivity extends BaseActivity implements AppBarLayout.OnOffsetChangedListener, BaseRecyclerAdapter.OnItemClickListener {

    public static String KEY_ARTICLEID;

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;
    @BindView(R.id.background_imageview)
    ImageView backgroundImageview;
    @BindView(R.id.framelayout_title)
    TextView framelayoutTitle;
    @BindView(R.id.linearlayout_title)
    LinearLayout mTitlelinearlayout;
    @BindView(R.id.main_appbar)
    AppBarLayout appbar;
    @BindView(R.id.picture_recycler)
    RecyclerView pictureRecycler;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.main_toolbar)
    Toolbar mainToolbar;
    @BindView(R.id.head_portrait)
    ImageView headPortrait;
    @BindView(R.id.framelayout_details)
    TextView framelayoutDetails;
    @BindView(R.id.activity_wendang)
    CoordinatorLayout activityWendang;

    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = true;

    private List<String> list = new ArrayList<String>();
    private WenDangAdapter adapter;
    private int wendangid;
    private String url;
    public AppContext appContext;


    ArticleEntity data;

    public static void launch(Context context, int wendangid) {
        if (context == null) return;
        if (wendangid == 0) return;
        Intent in = new Intent(context, ArticleActivity.class);
        in.putExtra(KEY_ARTICLEID, wendangid);
        context.startActivity(in);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
//        setImmerseLayout(findViewById(R.id.activity_wendang));
        ButterKnife.bind(this);
        appContext = AppContext.getApplication();
        wendangid = getIntent().getIntExtra(KEY_ARTICLEID,0);
        initView(this);
        initData();
    }

    @Override
    public void initView(Activity activity) {
        super.initView(activity);
        pictureRecycler.setLayoutManager(new LinearLayoutManager(this));
        pictureRecycler.addItemDecoration(ItemDecorationUtils.getCommTrans5Divider(ArticleActivity.this, true));

        adapter = new WenDangAdapter();
        adapter.setOnItemClickListener(this);
        pictureRecycler.setAdapter(adapter);

        appbar.addOnOffsetChangedListener(this);

        mainToolbar.inflateMenu(R.menu.menu_main);
        mainToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_share:
                        if (!TextUtils.isEmpty(url)) {
                            T.showShort("正在跳入异次元请求下载~~~");
                            Uri uri = Uri.parse(url);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                        }
                        break;
                }
                return true;
            }
        });
        startAlphaAnimation(toolbarTitle, 0, View.INVISIBLE);
    }

    @Override
    public void initData() {
        super.initData();
        RetrofitService.getInstance()
                .getApiCacheRetryService()
                .getPost(RetrofitService.getInstance().getToken(), wendangid,null)
                .enqueue(new SimpleCallBack<PostEntity>() {
                    @Override
                    public void onSuccess(Call<ResEntity<PostEntity>> call, Response<ResEntity<PostEntity>> response) {
                        if (response.body() == null) return;
                        data = response.body().data.post;
                        framelayoutTitle.setText(data.author.name);

                        toolbarTitle.setText(data.title);

                        if (HtmlAcgUtil.isHttps(data.author.avatarUrl)){
                            GlideUtils.loadUser(headPortrait.getContext(),data.author.avatarUrl,headPortrait);
                        }else {
                            GlideUtils.loadUser(headPortrait.getContext(),"https:"+data.author.avatarUrl,headPortrait);
                        }

                        if (HtmlAcgUtil.isHttps(data.thumbnail.url)){
                            GlideUtils.loadDetails(backgroundImageview.getContext(), data.thumbnail.url, backgroundImageview);
                        }else {
                            GlideUtils.loadDetails(backgroundImageview.getContext(), "https:"+data.thumbnail.url, backgroundImageview);
                        }

                        String s = data.excerpt;
                        framelayoutDetails.setText(s.replaceAll("[&hellip;]", ""));
//                        wendang_text.setText(stripHtml(data.getPost().getPost_content()));
                        url = data.url;
                        list.clear();

                        list = HtmlAcgUtil.quChu(HtmlAcgUtil.getImgStr(data.content));

                        adapter.bindData(true, list);
                    }
                });
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;

        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);
    }

    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if (!mIsTheTitleVisible) {
                startAlphaAnimation(toolbarTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(toolbarTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitlelinearlayout, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitlelinearlayout, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }

    public static void startAlphaAnimation(View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }

    @Override
    public void onItemClick(BaseRecyclerAdapter adapter, BaseRecyclerAdapter.ViewHolder holder, View view, int position) {
        ImagePagerActivity.launch(context,data.title ,(ArrayList<String>) list,position+1);
    }



}
