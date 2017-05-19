package com.moetutu.acg12.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moetutu.acg12.R;
import com.moetutu.acg12.activity.ArticleActivity;
import com.moetutu.acg12.activity.ArticleBActivity;
import com.moetutu.acg12.adapter.TuJiAdapter;
import com.moetutu.acg12.entity.ArticleEntity;
import com.moetutu.acg12.entity.PostEntity;
import com.moetutu.acg12.http.RetrofitService;
import com.moetutu.acg12.http.callback.SimpleCallBack;
import com.moetutu.acg12.http.httpmodel.ResEntity;
import com.moetutu.acg12.util.ItemDecorationUtils;
import com.moetutu.acg12.util.LogUtils;
import com.moetutu.acg12.view.gamerefreshview.FunGameRefreshView;
import com.moetutu.acg12.view.refresh.MaterialRefreshLayout;
import com.moetutu.acg12.view.refresh.MaterialRefreshListener;
import com.moetutu.acg12.view.widget.BaseRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.moetutu.acg12.view.DesignViewUtils.isSlideToBottom;

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

    RecyclerView recyclerView;
    MaterialRefreshLayout myRefreshLayout;

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

        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView.setHasFixedSize(true);

        recyclerView.addItemDecoration(ItemDecorationUtils.getCommTrans5Divider(getActivity(), true));
        myRefreshLayout = (MaterialRefreshLayout) view.findViewById(R.id.myRefreshLayout);

        myRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                initData(true);
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                initData(false);
            }
        });
        tuadapter = new TuJiAdapter();
        tuadapter.setOnItemClickListener(this);
        recyclerView.setAdapter(tuadapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isSlideToBottom(recyclerView)) {
                    myRefreshLayout.autoRefreshLoadMore();
                }
            }
        });

    }




    @Override
    public void onLazyLoad() {
        super.onLazyLoad();
        myRefreshLayout.autoRefresh();
    }

    private synchronized void initData(final boolean isRefresh) {
        if (isRefresh) {
            PageIndex = 1;
        }
        endLastRefresh(isRefresh);
        RetrofitService
                .getInstance()
                .getApiCacheRetryService()
                .getPostsByCategory(RetrofitService.getInstance().getToken(),getArguments().getString(TYPE_API),"content",10,PageIndex)
                .enqueue(new SimpleCallBack<PostEntity>() {
                    @Override
                    public void onSuccess(Call<ResEntity<PostEntity>> call, Response<ResEntity<PostEntity>> response) {
                        if (response.body().data.posts != null) {
                            endCurrentRefresh(isRefresh);
                            tuadapter.bindData(isRefresh, response.body().data.posts);
                            PageIndex++;
                            myRefreshLayout.setLoadMore(response.body().data.posts.size() > 9);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResEntity<PostEntity>> call, Throwable t) {
                        super.onFailure(call, t);
                        endCurrentRefresh(isRefresh);
                    }
                });

    }

    @Override
    public void onItemClick(BaseRecyclerAdapter adapter, BaseRecyclerAdapter.ViewHolder holder, View view, int position) {
        if (adapter == tuadapter) {
            ArticleEntity obj = tuadapter.getItem(position);
            if (obj != null){
                ArticleBActivity.launch(getActivity(),obj.id);
            }
        }
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

}
