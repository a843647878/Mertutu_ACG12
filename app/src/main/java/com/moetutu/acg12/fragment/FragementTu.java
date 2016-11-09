package com.moetutu.acg12.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moetutu.acg12.R;
import com.moetutu.acg12.activity.ArticleActivity;
import com.moetutu.acg12.adapter.TuJiAdapter;
import com.moetutu.acg12.entity.ArticleEntity;
import com.moetutu.acg12.entity.PostEntity;
import com.moetutu.acg12.http.RetrofitService;
import com.moetutu.acg12.http.callback.SimpleCallBack;
import com.moetutu.acg12.http.httpmodel.ResEntity;
import com.moetutu.acg12.util.ItemDecorationUtils;
import com.moetutu.acg12.util.LogUtils;
import com.moetutu.acg12.view.gamerefreshview.FunGameRefreshView;
import com.moetutu.acg12.view.refreshview.ProgressStyle;
import com.moetutu.acg12.view.refreshview.XRecyclerView;
import com.moetutu.acg12.view.widget.BaseRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Description
 * Company guokeyuzhou
 * Created by chengwanying on 16/6/15.
 * version
 * 单个分类列表
 */
public class FragementTu extends LazyBaseFragment implements BaseRecyclerAdapter.OnItemClickListener {

    public static final String TYPE_API = "type";

    public static FragementTu newInstance(String type) {
        FragementTu fragementTu = new FragementTu();
        Bundle bundle = new Bundle();
        bundle.putString(TYPE_API, type);
        fragementTu.setArguments(bundle);
        return fragementTu;
    }

    View rootView;

    //图站Fragement
    private int PageIndex = 1;
    private List<ArticleEntity> matches = new ArrayList<ArticleEntity>();

    TuJiAdapter tuadapter;

    XRecyclerView recyclerView;
    FunGameRefreshView beautifulRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_one, container, false);
            initViews(rootView);
        }
        if (rootView.getParent() != null) {
            ((ViewGroup) rootView.getParent()).removeView(rootView);
        }
        return rootView;
    }

    public void initViews(View view) {

        recyclerView = (XRecyclerView) view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.addItemDecoration(ItemDecorationUtils.getCommTrans5Divider(getActivity(), true));
        beautifulRefreshLayout = (FunGameRefreshView) view.findViewById(R.id.refresh);
        beautifulRefreshLayout.setOnRefreshListener(new FunGameRefreshView.FunGameRefreshListener() {
            @Override
            public void onRefreshing() {
                try {
                    // 模拟网络请求耗时动作
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                initData(true);
            }
        });
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.Pacman);
        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                //已经无效了
                initData( true);
            }

            @Override
            public void onLoadMore() {
                initData(false);
            }
        });

        recyclerView.setPullRefreshEnabled(false);
        tuadapter = new TuJiAdapter();
        tuadapter.setOnItemClickListener(this);
        recyclerView.setAdapter(tuadapter);
        if (matches == null) {
            LogUtils.d("---------onViewCreated");
            initData(true);
        }
//        initData(PageIndex, true);
    }

    @Override
    public void onLazyLoad() {
        super.onLazyLoad();
        initData(true);
    }

    private synchronized void initData(final boolean mm) {
        if (mm) {
            PageIndex = 1;
        }
        RetrofitService
                .getInstance()
                .getApiCacheRetryService()
                .getPostsByCategory(RetrofitService.getInstance().getToken(),getArguments().getString(TYPE_API),null,10,PageIndex)
                .enqueue(new SimpleCallBack<PostEntity>() {
                    @Override
                    public void onSuccess(Call<ResEntity<PostEntity>> call, Response<ResEntity<PostEntity>> response) {
                        if (response.body().data.posts != null) {
                            tuadapter.bindData(mm, response.body().data.posts);
                            PageIndex++;
                            recyclerView.loadMoreComplete();
                            beautifulRefreshLayout.finishRefreshing();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResEntity<PostEntity>> call, Throwable t) {
                        super.onFailure(call, t);
                    }
                });

    }

    @Override
    public void onItemClick(BaseRecyclerAdapter adapter, BaseRecyclerAdapter.ViewHolder holder, View view, int position) {
        XRecyclerView.WrapAdapter wpAdapter = (XRecyclerView.WrapAdapter) recyclerView.getAdapter();
        ArticleEntity obj = tuadapter.getItem(position - wpAdapter.getHeadersCount());
        ArticleActivity.launch(getActivity(),obj.id);
    }

}
