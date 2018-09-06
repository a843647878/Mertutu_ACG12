package com.moetutu.acg12.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moetutu.acg12.R;
import com.moetutu.acg12.activity.ArticleBActivity;
import com.moetutu.acg12.activity.ArticleBGActivity;
import com.moetutu.acg12.adapter.TuJiAdapter;
import com.moetutu.acg12.entity.ArticleEntity;
import com.moetutu.acg12.entity.PostEntity;
import com.moetutu.acg12.http.RetrofitService;
import com.moetutu.acg12.http.callback.SimpleCallBack;
import com.moetutu.acg12.http.httpmodel.ResEntity;
import com.moetutu.acg12.util.Const;
import com.moetutu.acg12.util.ItemDecorationUtils;
import com.moetutu.acg12.view.widget.BaseRecyclerAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;


/**
 * Description
 * Company guokeyuzhou
 * Created by chengwanying on 16/7/4.
 * version
 * 壁纸页
 */
public class MainComicFragement extends LazyBaseFragment implements BaseRecyclerAdapter.OnItemClickListener{
    View rootView;
    @BindView(R.id.acg_name)
    TextView acgName;
    @BindView(R.id.list)
    RecyclerView recyclerView;
    @BindView(R.id.myRefreshLayout)
    SmartRefreshLayout myRefreshLayout;


    //图站Fragement
    private int PageIndex = 1;
    private List<ArticleEntity> matches = new ArrayList<ArticleEntity>();

    TuJiAdapter tuadapter;


    public static MainComicFragement newInstance() {
        MainComicFragement fragment = new MainComicFragement();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_wallpaper, container, false);
            ButterKnife.bind(this, rootView);
            initView();
        }
        if (rootView.getParent() != null) {
            ((ViewGroup) rootView.getParent()).removeView(rootView);
        }
        return rootView;
    }


    @Override
    public void onLazyLoad() {
        super.onLazyLoad();
    }

    private void initView() {
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.addItemDecoration(ItemDecorationUtils.getCommTrans5Divider(getActivity(), true));

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
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                if (isSlideToBottom(recyclerView)) {
//                    myRefreshLayout.autoRefreshLoadMore();
//                }
//            }
//        });
        myRefreshLayout.autoRefresh();
    }

    private synchronized void initData(final boolean isRefresh) {
        if (isRefresh) {
            PageIndex = 1;
        }
        RetrofitService
                .getInstance()
                .getApiCacheRetryService()
                .getPostsByCategory(RetrofitService.getInstance().getToken(), Const.BiZhi,"content",10,PageIndex)
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
            if (obj != null){
                ArticleBGActivity.launch(getActivity(),obj.id);
            }
        }
    }


}
