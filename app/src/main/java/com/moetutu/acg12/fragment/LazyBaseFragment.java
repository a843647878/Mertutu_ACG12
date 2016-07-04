package com.moetutu.acg12.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * ClassName LazyBaseFragment
 * Description   懒加载fragment 适合viewPager网络数据请求，
 * Company
 * author  youxuan  E-mail:xuanyouwu@163.com
 * date createTime：2015/9/9 11:25
 * version
 */
public class LazyBaseFragment extends Fragment {
    private boolean mHasLoadedOnce = false;

    public void onLazyLoad() {
    }


    private boolean isLazy;

    /**
     * 请用viewPager第1个fragment 调用
     */
    public LazyBaseFragment startLazyMode() {
        isLazy = true;
        return this;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (this.isVisible()) {
            if (isVisibleToUser && !mHasLoadedOnce) {
                mHasLoadedOnce = true;
                onLazyLoad();
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (isLazy)
            setUserVisibleHint(isLazy);
        super.onActivityCreated(savedInstanceState);
    }
}


