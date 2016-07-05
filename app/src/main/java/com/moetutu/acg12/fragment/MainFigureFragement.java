package com.moetutu.acg12.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moetutu.acg12.R;
import com.moetutu.acg12.activity.WenDangActivity;
import com.moetutu.acg12.app.AppContext;
import com.moetutu.acg12.entity.TestMode;
import com.moetutu.acg12.http.RetrofitService;
import com.moetutu.acg12.http.callback.SimpleCallBack;
import com.moetutu.acg12.util.Const;
import com.moetutu.acg12.util.LogUtils;
import com.moetutu.acg12.view.CardSlidePanel;
import com.moetutu.acg12.view.CardSlidePanel.CardSwitchListener;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Description
 * Company guokeyuzhou
 * Created by chengwanying on 16/7/4.
 * version
 */
public class MainFigureFragement extends LazyBaseFragment {

    View rootView;
    private CardSwitchListener cardSwitchListener;
    private int PageIndex = 1;
    @InjectView(R.id.image_slide_panel)
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
            ButterKnife.inject(this, rootView);
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
                WenDangActivity.launch(getContext(),slidePanel.dataList.get(index).getID()+"");
            }
        };

        slidePanel.setCardSwitchListener(cardSwitchListener);

    }

    public synchronized void initData(final boolean newdata) {
        RetrofitService
                .getInstance()
                .getApiCacheRetryService()
                .getList(appContext.getTuJiList(Const.TUZHANJINGXUANRANDOM, PageIndex))
                .enqueue(new SimpleCallBack<TestMode>() {
                    @Override
                    public void onSuccess(Call<TestMode> call, Response<TestMode> response) {
                        if (response.body().getPosts() == null) return;
//                            List<TestMode.PostsBean> dataList2 = new ArrayList<TestMode.PostsBean>();
//                            dataList2 = response.body().getPosts();
//                            dataList.addAll(dataList2);
                        PageIndex++;
                        if (newdata) {
                            slidePanel.appendData(response.body().getPosts());
                            LogUtils.d("---------->填充追加数据");
                        } else {
                            slidePanel.fillData(response.body().getPosts());
                            LogUtils.d("---------->加载");
                        }


                    }

                    @Override
                    public void onFailure(Call<TestMode> call, Throwable t) {
                        super.onFailure(call, t);
                        LogUtils.d("---------->call" + t.toString());
                    }
                });
    }

    @Override
    public void onLazyLoad() {
        super.onLazyLoad();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
