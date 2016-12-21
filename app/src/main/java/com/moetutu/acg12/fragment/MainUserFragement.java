package com.moetutu.acg12.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moetutu.acg12.R;

import butterknife.ButterKnife;

/**
 * Description
 * Company guokeyuzhou
 * Created by chengwanying on 16/7/4.
 * version
 */
public class MainUserFragement extends LazyBaseFragment{

    public static MainUserFragement newInstance() {
        MainUserFragement fragment = new MainUserFragement();
        return fragment;
    }
    View rootView;
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
