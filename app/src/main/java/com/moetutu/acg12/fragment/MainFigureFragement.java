package com.moetutu.acg12.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moetutu.acg12.R;
import com.moetutu.acg12.activity.ArticleActivity;
import com.moetutu.acg12.activity.ArticleBActivity;
import com.moetutu.acg12.app.AppContext;
import com.moetutu.acg12.entity.PostEntity;
import com.moetutu.acg12.entity.TestMode;
import com.moetutu.acg12.http.RetrofitService;
import com.moetutu.acg12.http.callback.SimpleCallBack;
import com.moetutu.acg12.http.httpmodel.ResEntity;
import com.moetutu.acg12.util.Const;
import com.moetutu.acg12.util.LogUtils;
import com.moetutu.acg12.view.CardSlidePanel;
import com.moetutu.acg12.view.CardSlidePanel.CardSwitchListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Description
 * Company guokeyuzhou
 * Created by chengwanying on 16/7/4.
 * version
 * 仿探探卡片式UI
 */
public class MainFigureFragement extends LazyBaseFragment {

    View rootView;
    private CardSwitchListener cardSwitchListener;
    private int PageIndex = 1;
    @BindView(R.id.image_slide_panel)
    CardSlidePanel slidePanel;

    public AppContext appContext;


    public static MainFigureFragement newInstance() {
        MainFigureFragement fragment = new MainFigureFragement();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_figure, container, false);
            ButterKnife.bind(this, rootView);
            appContext = AppContext.getApplication();
            initView();
            initData(false);
        }
        if (rootView.getParent() != null) {
            ((ViewGroup) rootView.getParent()).removeView(rootView);
        }
        return rootView;
    }

    public void initView() {

        cardSwitchListener = new CardSlidePanel.CardSwitchListener() {

            @Override
            public void onShow(int index) {
                LogUtils.d("---------->正在显示-" + index);

            }

            @Override
            public void onCardVanish(int index, int type) {
                LogUtils.d("---------->正在消失-" +index);
                if (index == slidePanel.dataList.size()-3) {
                    initData(true);
                }
            }

            @Override
            public void onItemClick(View cardView, int index) {
                LogUtils.d("---------->卡片点击-" + index);
                if (slidePanel.dataList == null) return;
                ArticleBActivity.launch(getContext(),slidePanel.dataList.get(index).id);
            }
        };

        slidePanel.setCardSwitchListener(cardSwitchListener);

    }

    public synchronized void initData(final boolean newdata) {
        RetrofitService
                .getInstance()
                .getApiCacheRetryService()
                .getRecommPosts(RetrofitService.getInstance().getToken(),"content",10,PageIndex)
                .enqueue(new SimpleCallBack<PostEntity>() {
                    @Override
                    public void onSuccess(Call<ResEntity<PostEntity>> call, Response<ResEntity<PostEntity>> response) {
                        if (response.body().data.posts == null) return;
//                            List<TestMode.PostsBean> dataList2 = new ArrayList<TestMode.PostsBean>();
//                            dataList2 = response.body().getPosts();
//                            dataList.addAll(dataList2);
                        PageIndex++;
                        if (newdata) {
                            slidePanel.appendData(response.body().data.posts);
                            LogUtils.d("---------->填充追加数据");
                        } else {
                            slidePanel.fillData(response.body().data.posts);
                            LogUtils.d("---------->加载");
                        }
                    }
                });
    }

    @Override
    public void onLazyLoad() {
        super.onLazyLoad();
    }

}
