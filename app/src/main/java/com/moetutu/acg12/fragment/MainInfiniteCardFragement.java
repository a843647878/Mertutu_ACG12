package com.moetutu.acg12.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.moetutu.acg12.R;
import com.moetutu.acg12.adapter.InfiniteCardAdapter;
import com.moetutu.acg12.app.AppContext;
import com.moetutu.acg12.entity.ArticleEntity;
import com.moetutu.acg12.entity.PostEntity;
import com.moetutu.acg12.http.RetrofitService;
import com.moetutu.acg12.http.callback.SimpleCallBack;
import com.moetutu.acg12.http.httpmodel.ResEntity;
import com.moetutu.acg12.util.LogUtils;
import com.moetutu.acg12.view.card.InfiniteCardView;
import com.moetutu.acg12.view.card.transformer.DefaultTransformerToBack;
import com.moetutu.acg12.view.card.transformer.DefaultTransformerToFront;
import com.moetutu.acg12.view.card.transformer.DefaultZIndexTransformerCommon;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Description
 * Company guokeyuzhou
 * Created by chengwanying on 16/7/4.
 * version
 * 仿探探卡片式UI
 */
public class MainInfiniteCardFragement extends LazyBaseFragment {

    View rootView;
    private int PageIndex = 1;

    public AppContext appContext;
    @BindView(R.id.fake_status_bar)
    View fakeStatusBar;
    @BindView(R.id.acg_icon)
    ImageView acgIcon;
    @BindView(R.id.acg_name)
    TextView acgName;
    @BindView(R.id.view)
    InfiniteCardView mCardView;
    @BindView(R.id.activity_wendang)
    FrameLayout activityWendang;

    InfiniteCardAdapter infiniteCardAdapter;


    private List<ArticleEntity> posts=new ArrayList<>();


    public static MainInfiniteCardFragement newInstance() {
        MainInfiniteCardFragement fragment = new MainInfiniteCardFragement();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_card, container, false);
            ButterKnife.bind(this, rootView);
            appContext = AppContext.getApplication();

            initData(false);
        }
        if (rootView.getParent() != null) {
            ((ViewGroup) rootView.getParent()).removeView(rootView);
        }
        return rootView;
    }

    public void initView() {


        mCardView.setAnimInterpolator(new LinearInterpolator());
        mCardView.setTransformerToFront(new DefaultTransformerToFront());
        mCardView.setTransformerToBack(new DefaultTransformerToBack());
        mCardView.setZIndexTransformerToBack(new DefaultZIndexTransformerCommon());

    }

    public synchronized void initData(final boolean newdata) {
        RetrofitService
                .getInstance()
                .getApiCacheRetryService()
                .getRecommPosts(RetrofitService.getInstance().getToken(), "content", 10, PageIndex)
                .enqueue(new SimpleCallBack<PostEntity>() {
                    @Override
                    public void onSuccess(Call<ResEntity<PostEntity>> call, Response<ResEntity<PostEntity>> response) {
                        if (response.body().data.posts == null) return;
                        posts = response.body().data.posts;
//                            List<TestMode.PostsBean> dataList2 = new ArrayList<TestMode.PostsBean>();
//                            dataList2 = response.body().getPosts();
//                            dataList.addAll(dataList2);
                        PageIndex++;
                        infiniteCardAdapter = new InfiniteCardAdapter(posts);
                        mCardView.setAdapter(infiniteCardAdapter);
                        initView();
                    }
                });
    }

    @Override
    public void onLazyLoad() {
        super.onLazyLoad();
    }

}
