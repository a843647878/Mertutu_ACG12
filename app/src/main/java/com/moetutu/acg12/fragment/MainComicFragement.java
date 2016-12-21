package com.moetutu.acg12.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
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
 * 之前的漫画页
 */
public class MainComicFragement extends LazyBaseFragment {
    View rootView;



    public static MainComicFragement newInstance() {
        MainComicFragement fragment = new MainComicFragement();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_user, container, false);
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


}
