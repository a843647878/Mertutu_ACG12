package com.moetutu.acg12.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.moetutu.acg12.R;
import com.moetutu.acg12.entity.TestMode;
import com.moetutu.acg12.http.RetrofitService;
import com.moetutu.acg12.http.callback.SimpleCallBack;
import com.moetutu.acg12.util.Const;
import com.moetutu.acg12.util.LogUtils;
import com.moetutu.acg12.view.CardSlidePanel;
import com.moetutu.acg12.view.CardSlidePanel.CardSwitchListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by chengwanying on 16/6/7.
 */
public class TestMain extends BaseActivity{

    public static void launch(Context context) {
        if (context == null) return;
        Intent in = new Intent(context, TestMain.class);
        context.startActivity(in);
    }

    private CardSwitchListener cardSwitchListener;
    private int PageIndex = 0;

    CardSlidePanel slidePanel;

    private List<TestMode.PostsBean> dataList = new ArrayList<TestMode.PostsBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_madie);
        setImmerseLayout(findViewById(R.id.activity_wendang));
        initData(PageIndex,false);
        initView(this);
    }

    @Override
    public void initView(Activity activity) {
        super.initView(activity);

        slidePanel = (CardSlidePanel) activity
                .findViewById(R.id.image_slide_panel);
        cardSwitchListener = new CardSwitchListener() {

            @Override
            public void onShow(int index) {
                LogUtils.d("----------正在显示-" +index+"------" +dataList.get(index).getPost_title());
                if (index == 3){
                    initData(PageIndex,true);
                }
            }

            @Override
            public void onCardVanish(int index, int type) {
                LogUtils.d("----------正在消失-" + dataList.get(index).getPost_title());

            }

            @Override
            public void onItemClick(View cardView, int index) {
                LogUtils.d("----------卡片点击-" + dataList.get(index).getPost_title());
            }
        };
        slidePanel.setCardSwitchListener(cardSwitchListener);

    }

    public void initData(final int bookpage,final boolean newdata) {
        RetrofitService
                .getInstance()
                .getApiCacheRetryService()
                .getList(appContext.getTuJiList(Const.TUZHANJINGXUANRANDOM, bookpage))
                .enqueue(new SimpleCallBack<TestMode>() {
                    @Override
                    public void onSuccess(Call<TestMode> call, Response<TestMode> response) {
                        if (response.body() != null) {
                            List<TestMode.PostsBean> dataList2 = new ArrayList<TestMode.PostsBean>();
                            dataList2 = response.body().getPosts();
                            dataList.addAll(dataList2);
                            PageIndex++;
                            if (newdata){
                                slidePanel.appendData(dataList);
                            }else {
                                slidePanel.fillData(dataList);
                            }

                        }
                    }
                    @Override
                    public void onFailure(Call<TestMode> call, Throwable t) {
                        super.onFailure(call, t);
                        LogUtils.d("----------call"+t.toString());
                    }
                });
    }
}
