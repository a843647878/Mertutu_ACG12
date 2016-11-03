package com.moetutu.acg12.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moetutu.acg12.R;
import com.moetutu.acg12.activity.ArticleActivity;
import com.moetutu.acg12.adapter.ComicShufflingAdapter;
import com.moetutu.acg12.adapter.TuJiAdapter;
import com.moetutu.acg12.app.AppContext;
import com.moetutu.acg12.entity.ArticleEntity;
import com.moetutu.acg12.entity.ChMedicCircleRecEntity;
import com.moetutu.acg12.entity.PostEntity;
import com.moetutu.acg12.entity.TestMode;
import com.moetutu.acg12.http.RetrofitService;
import com.moetutu.acg12.http.callback.SimpleCallBack;
import com.moetutu.acg12.http.httpmodel.ResEntity;
import com.moetutu.acg12.util.Const;
import com.moetutu.acg12.util.LogUtils;
import com.moetutu.acg12.view.ZoomOutPageTransformer;
import com.moetutu.acg12.view.refresh.MaterialRefreshLayout;
import com.moetutu.acg12.view.refresh.MaterialRefreshListener;
import com.moetutu.acg12.view.widget.BaseRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit2.Call;
import retrofit2.Response;


/**
 * Description
 * Company guokeyuzhou
 * Created by chengwanying on 16/6/15.
 * version
 * 原本是漫画界面  可能会做成视频
 */
public class FeagementComicOne extends LazyBaseFragment implements BaseRecyclerAdapter.OnItemClickListener {
    View rootView;


    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
    @InjectView(R.id.myRefreshLayout)
    MaterialRefreshLayout myRefreshLayout;
    @InjectView(R.id.header_viewPager)
    ViewPager headerViewPager;

    @InjectView(R.id.appbar)
    AppBarLayout appbar;


    private ComicShufflingAdapter chMedCircImgAdapter;
    public AppContext appContext;
    private int PageIndex = 1;

    TuJiAdapter tuadapter;

    public static FeagementComicOne newInstance() {
        FeagementComicOne fragment = new FeagementComicOne();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_comic_main, container, false);
            ButterKnife.inject(this, rootView);
            appContext = AppContext.getApplication();
            initView();
        }
        if (rootView.getParent() != null) {
            ((ViewGroup) rootView.getParent()).removeView(rootView);
        }
        return rootView;
    }

    private void initView() {


//        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        chMedCircImgAdapter = new ComicShufflingAdapter(getActivity());
        headerViewPager.setOffscreenPageLimit(5);
        headerViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        headerViewPager.setAdapter(chMedCircImgAdapter);

        myRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                initData(PageIndex, true);
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                initData(PageIndex, false);
            }
        });
        recyclerView.setAdapter(tuadapter = new TuJiAdapter());
    }

    @Override
    public void onLazyLoad() {
        super.onLazyLoad();
        getData();
    }


    private synchronized void initData(final int bookpage, final boolean isRefresh) {
        if (isRefresh) {
            PageIndex = 1;
            LogUtils.d("-------------->" + isRefresh);
        }
        LogUtils.d("-------------->" + PageIndex);
        endLastRefresh(isRefresh);
        RetrofitService
                .getInstance()
                .getApiCacheRetryService()
                .getPostsByCategory(RetrofitService.getInstance().getToken(),"236",null,10,PageIndex)
                .enqueue(new SimpleCallBack<PostEntity>() {
                    @Override
                    public void onSuccess(Call<ResEntity<PostEntity>> call, Response<ResEntity<PostEntity>> response) {
                        if (response.body().data.posts == null) return;
                        endCurrentRefresh(isRefresh);
                        tuadapter.bindData(isRefresh, response.body().data.posts);
                        PageIndex++;
                        myRefreshLayout.setLoadMore(isRefresh);
                    }

                    @Override
                    public void onFailure(Call<ResEntity<PostEntity>> call, Throwable t) {
                        super.onFailure(call, t);
                        endCurrentRefresh(isRefresh);
                    }
                });

    }

    private void getData() {

        List<ChMedicCircleRecEntity> datas = new ArrayList<>();
        ChMedicCircleRecEntity en = new ChMedicCircleRecEntity();
        en.id = 0;
        en.title = "惊天魔盗团2";
        en.img = "http://img3.doubanio.com/view/movie_poster_cover/lpst/public/p2355440566.jpg";
        en.desc = "描述:惊天魔盗团2";
        datas.add(en);


        en = new ChMedicCircleRecEntity();
        en.id = 1;
        en.title = "赏金猎人";
        en.img = "http://img3.douban.com/view/movie_poster_cover/lpst/public/p2358403173.jpg";
        en.desc = "描述:赏金猎人";
        datas.add(en);


        en = new ChMedicCircleRecEntity();
        en.id = 1;
        en.title = "魔轮";
        en.img = "http://img3.douban.com/view/movie_poster_cover/lpst/public/p2358333141.jpg";
        en.desc = "描述:魔轮";
        datas.add(en);

        en = new ChMedicCircleRecEntity();
        en.id = 1;
        en.title = "终极胜利";
        en.img = "http://img3.douban.com/view/movie_poster_cover/lpst/public/p2363435535.jpg";
        en.desc = "描述:终极胜利";
        datas.add(en);


        en = new ChMedicCircleRecEntity();
        en.id = 1;
        en.title = "忍者神龟2：破影而出";
        en.img = "http://img3.doubanio.com/view/movie_poster_cover/lpst/public/p2357111716.jpg";
        en.desc = "描述:忍者神龟2：破影而出";
        datas.add(en);

        chMedCircImgAdapter.bindData(datas);
        if (chMedCircImgAdapter.getCount() >= 2) {
            headerViewPager.setCurrentItem(1, false);
        }

        myRefreshLayout.autoRefresh();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    public void endLastRefresh(boolean isRefresh) {
        if (myRefreshLayout == null) return;
        if (isRefresh)
            myRefreshLayout.finishRefreshLoadMore();
        else
            myRefreshLayout.finishRefresh();
    }

    public void endCurrentRefresh(boolean isRefresh) {
        if (myRefreshLayout == null) return;
        if (isRefresh)
            myRefreshLayout.finishRefresh();
        else
            myRefreshLayout.finishRefreshLoadMore();
    }


    @Override
    public void onItemClick(BaseRecyclerAdapter adapter, BaseRecyclerAdapter.ViewHolder holder, View view, int position) {
        if (adapter == tuadapter) {
            ArticleEntity obj = tuadapter.getItem(position);
            ArticleActivity.launch(getActivity(), obj.id);
        }

    }
}
