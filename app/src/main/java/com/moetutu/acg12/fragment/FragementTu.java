package com.moetutu.acg12.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.moetutu.acg12.view.widget.BaseRecyclerAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;


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
    SmartRefreshLayout myRefreshLayout;

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
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(ItemDecorationUtils.getCommTrans5Divider(getActivity(), true));
        myRefreshLayout = (SmartRefreshLayout) view.findViewById(R.id.myRefreshLayout);

        myRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                initData(true);
                refreshlayout.finishRefresh(2000);
            }
        });
        myRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                initData(false);
                refreshlayout.finishLoadmore(2000);
            }
        });

        tuadapter = new TuJiAdapter();
        tuadapter.setOnItemClickListener(this);
        recyclerView.setAdapter(tuadapter);


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
        RetrofitService
                .getInstance()
                .getApiCacheRetryService()
                .getPostsByCategory(RetrofitService.getInstance().getToken(), getArguments().getString(TYPE_API), "content", 10, PageIndex)
                .enqueue(new SimpleCallBack<PostEntity>() {
                    @Override
                    public void onSuccess(Call<ResEntity<PostEntity>> call, Response<ResEntity<PostEntity>> response) {
                        if (response.body().data.posts != null) {
                            tuadapter.bindData(isRefresh, response.body().data.posts);
                            PageIndex++;
                            myRefreshLayout.setLoadmoreFinished(!(response.body().data.posts.size() > 9));
                        }
                    }
                });

    }

    @Override
    public void onItemClick(BaseRecyclerAdapter adapter, BaseRecyclerAdapter.ViewHolder holder, View view, int position) {
        if (adapter == tuadapter) {
            ArticleEntity obj = tuadapter.getItem(position);
            if (obj != null) {
                ArticleBActivity.launch(getActivity(), obj.id);
            }
        }
    }




}
