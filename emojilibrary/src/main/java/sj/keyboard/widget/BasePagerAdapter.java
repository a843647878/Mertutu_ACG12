package sj.keyboard.widget;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Description 封装 PagerAdapter 简化开发
 * Company Beijing guokeyuzhou
 * author  youxuan  E-mail:xuanyouwu@163.com
 * date createTime：16/12/9
 * version
 * 现在viewpager逐渐被取代 替代recyclerView[横向]
 */

public abstract class BasePagerAdapter<T> extends PagerAdapter {

    public interface OnPagerItemClickListener<T> {

        /**
         * @param adapter
         * @param v       点击的控件
         * @param pos     点击的位置[在adapter中]
         */
        void OnItemClick(BasePagerAdapter<T> adapter, View v, int pos);
    }

    public interface OnPagerItemChildClickListener<T> {

        /**
         * @param adapter
         * @param v       点击的控件[布局中的子控件]
         * @param pos
         */
        void OnPagerItemChild(BasePagerAdapter<T> adapter, View v, int pos);
    }

    private final List<T> datas = new ArrayList<>();

    public void bindData(boolean isRefresh, List<T> data) {
        if (isRefresh) {
            datas.clear();
        }
        datas.addAll(data);
        notifyDataSetChanged();
    }

    private boolean isCanupdateItem;

    private OnPagerItemClickListener<T> onPagerItemClickListener;
    private OnPagerItemChildClickListener<T> onPagerItemChildClickListener;

    public OnPagerItemClickListener<T> getOnPagerItemClickListener() {
        return onPagerItemClickListener;
    }

    public void setOnPagerItemClickListener(OnPagerItemClickListener<T> onPagerItemClickListener) {
        this.onPagerItemClickListener = onPagerItemClickListener;
    }

    public OnPagerItemChildClickListener<T> getOnPagerItemChildClickListener() {
        return onPagerItemChildClickListener;
    }

    public void setOnPagerItemChildClickListener(OnPagerItemChildClickListener<T> onPagerItemChildClickListener) {
        this.onPagerItemChildClickListener = onPagerItemChildClickListener;
    }

    public boolean isCanupdateItem() {
        return isCanupdateItem;
    }

    public void setCanupdateItem(boolean canupdateItem) {
        this.isCanupdateItem = canupdateItem;
    }

    public BasePagerAdapter(boolean isCanupdateItem) {
        this.isCanupdateItem = isCanupdateItem;
    }

    public BasePagerAdapter() {
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    public T getItem(int pos) {
        if (pos >= 0 && pos < getCount()) {
            return datas.get(pos);
        }
        return null;
    }

    /**
     * 绑定布局 优先于 public int bindView(int pos)
     *
     * @param context
     * @param pos
     * @return
     */
    public View bindView(Context context, int pos) {
        return null;
    }

    /**
     * 绑定布局id
     *
     * @param pos
     * @return
     */
    public abstract int bindView(int pos);

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = bindView(container.getContext(), position);
        if (itemView == null) {
            itemView = View.inflate(container.getContext(), bindView(position), null);
        }
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onPagerItemClickListener != null) {
                    onPagerItemClickListener.OnItemClick(BasePagerAdapter.this, v, position);
                }
            }
        });
        container.addView(itemView);
        bindDataToItem(getItem(position), container, itemView, position);
        return itemView;
    }

    public abstract void bindDataToItem(T t, ViewGroup container, View itemView, int pos);

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return isCanupdateItem ? POSITION_NONE : super.getItemPosition(object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
