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
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description
 * Company guokeyuzhou
 * Created by chengwanying on 16/7/4.
 * version
 */
public class MainComicFragement extends LazyBaseFragment {
    View rootView;

    @BindView(R.id.viewpagertab)
    SmartTabLayout viewpagertab;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    BaseFragmentAdapter fragmentAdapter;

    public static MainComicFragement newInstance() {
        MainComicFragement fragment = new MainComicFragement();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_comic, container, false);
            ButterKnife.bind(this, rootView);
            initView();
        }
        if (rootView.getParent() != null) {
            ((ViewGroup) rootView.getParent()).removeView(rootView);
        }
        return rootView;
    }

    private void initView() {
        fragmentAdapter = new BaseFragmentAdapter(getChildFragmentManager());

        viewpager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Fragment fragment = fragmentAdapter.getItem(position);
                fragment.setUserVisibleHint(true);
            }
        });
        fragmentAdapter.bindTitle(true, Arrays.asList("漫画中心", "吾之宝藏", "动态"));
        fragmentAdapter.bindData(true,
                Arrays.asList(
                        FeagementComicOne.newInstance().startLazyMode(),
                        FeagementComicOne.newInstance(),
                        FeagementComicOne.newInstance()));
        viewpager.setAdapter(fragmentAdapter);
        viewpagertab.setViewPager(viewpager);
    }

    @Override
    public void onLazyLoad() {
        super.onLazyLoad();
    }


}
