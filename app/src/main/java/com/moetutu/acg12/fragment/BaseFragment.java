package com.moetutu.acg12.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author xuanyouwu
 * @email xuanyouwu@163.com
 * @time 2016-09-03 11:23
 */

public class BaseFragment extends Fragment {

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // LogUtils.d("===========>fragmentOnAttach:" + this + "  context:" + context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // LogUtils.d("===========>fragmentOnCreate:" + this + "  savedInstanceState:" + savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // LogUtils.d("===========>fragmentOnCreateView:" + this + "    container:" + container + "  savedInstanceState:" + savedInstanceState);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // LogUtils.d("===========>fragmentActivityCreated:" + this + "  savedInstanceState:" + savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        // LogUtils.d("===========>fragmentOnStart:" + this);
    }

    @Override
    public void onResume() {
        super.onResume();
        //LogUtils.d("===========>fragmentOnResume:" + this);
    }

    @Override
    public void onPause() {
        super.onPause();
        // LogUtils.d("===========>fragmentOnPause:" + this);
    }

    @Override
    public void onStop() {
        super.onStop();
        // LogUtils.d("===========>fragmentOnStop:" + this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // LogUtils.d("===========>fragmentOnDestroyView:" + this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // LogUtils.d("===========>fragmentOnDestroy:" + this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //LogUtils.d("===========>fragmentOnDetach:" + this);
    }


}
