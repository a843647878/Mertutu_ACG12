package com.moetutu.acg12.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moetutu.acg12.R;
import com.moetutu.acg12.adapter.BaseFragmentAdapter;
import com.moetutu.acg12.util.Const;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.Arrays;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Description
 * Company guokeyuzhou
 * Created by chengwanying on 16/7/4.
 * version
 */
public class MainMerTuFragement extends LazyBaseFragment {

    View rootView;
    @InjectView(R.id.viewpagertab)
    SmartTabLayout viewpagertab;
    @InjectView(R.id.viewpager)
    ViewPager viewpager;


    private String userid;
    private String touxiang;
    private String name;
    public static boolean isForeground = false;
    BaseFragmentAdapter fragmentAdapter;

    public static MainMerTuFragement newInstance() {
        MainMerTuFragement fragment = new MainMerTuFragement();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_main, container, false);
            ButterKnife.inject(this, rootView);
            initView();
        }
        if (rootView.getParent() != null) {
            ((ViewGroup) rootView.getParent()).removeView(rootView);
        }
        ButterKnife.inject(this, rootView);
        return rootView;
    }

    private synchronized void initView() {

        fragmentAdapter = new BaseFragmentAdapter(getChildFragmentManager());

        viewpager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Fragment fragment = fragmentAdapter.getItem(position);
                fragment.setUserVisibleHint(true);
            }
        });
        fragmentAdapter.bindTitle(true, Arrays.asList("动漫图集", "图站精选", "绅士道", "PC通用萌化", "安卓萌化", "动漫游戏", "其他分类"));
        fragmentAdapter.bindData(true,
                Arrays.asList(
                        FragementTu.newInstance(Const.DongManTuJi).startLazyMode(),
                        FragementTu.newInstance(Const.TuZhanJingXuan),
                        FragementTu.newInstance(Const.ShenShi),
                        FragementTu.newInstance(Const.PCMengHua),
                        FragementTu.newInstance(Const.AndroidMengHua),
                        FragementTu.newInstance(Const.DongManGame),
                        FragementTu.newInstance(Const.QiTaFenLei)));
        viewpager.setAdapter(fragmentAdapter);
        viewpagertab.setViewPager(viewpager);


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
