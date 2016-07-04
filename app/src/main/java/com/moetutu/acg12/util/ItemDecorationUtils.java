package com.moetutu.acg12.util;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import com.moetutu.acg12.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import com.yqritc.recyclerviewflexibledivider.VerticalDividerItemDecoration;


/**
 * ClassName ItemDecorationUtils
 * Description  RecyclerView divider工具类
 * 参考: https://github.com/yqritc/RecyclerView-FlexibleDivider
 * Company Beijing keke
 * author  youxuan  E-mail:xuanyouwu@163.com
 * date createTime：2015/9/11 15:29
 * version
 */
public class ItemDecorationUtils {
    public static int color_comm_divider = 0xFFD9D9D9;

    public static int getStyleDividerColor(Context context) {
        /*if (!(context instanceof BaseActivity)) return color_comm_divider;
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.divide_1, typedValue, true);
        try {
            color_comm_divider = Color.parseColor("" + typedValue.coerceToString());
        } catch (Exception e) {
        }*/
        return color_comm_divider;
    }


    /**
     * 宽度与RecyclerView一致
     */
    public static RecyclerView.ItemDecoration getCommFullDivider(Context context, boolean isShowLastDivider) {
        getStyleDividerColor(context);
        HorizontalDividerItemDecoration.Builder builder = new HorizontalDividerItemDecoration.Builder(context).color(color_comm_divider).sizeResId(R.dimen.dp0_5);
        if (isShowLastDivider) builder.showLastDivider();
        return builder.build();
    }

    /**
     * 高度与RecyclerView一致
     * 垂直方向
     *
     * @param context
     * @param isShowLastDivider
     * @return
     */
    public static RecyclerView.ItemDecoration getCommFullDividerVertical(Context context, boolean isShowLastDivider) {
        getStyleDividerColor(context);
        VerticalDividerItemDecoration.Builder builder = new VerticalDividerItemDecoration.Builder(context).color(color_comm_divider).sizeResId(R.dimen.dp0_5);
        if (isShowLastDivider) builder.showLastDivider();
        return builder.build();
    }

    /**
     * 高度 5 dp 分割
     *
     * @param context
     * @return
     */
    public static RecyclerView.ItemDecoration getCommFull5Divider(Context context, boolean isShowLastDivider) {
        getStyleDividerColor(context);
        HorizontalDividerItemDecoration.Builder builder = new HorizontalDividerItemDecoration.Builder(context).color(color_comm_divider).sizeResId(R.dimen.dp5);
        if (isShowLastDivider) builder.showLastDivider();
        return builder.build();
    }
    /**
     * 高度 10 dp 分割
     *
     * @param context
     * @return
     */
    public static RecyclerView.ItemDecoration getCommFull10Divider(Context context, boolean isShowLastDivider) {
        getStyleDividerColor(context);
        HorizontalDividerItemDecoration.Builder builder = new HorizontalDividerItemDecoration.Builder(context).color(color_comm_divider).sizeResId(R.dimen.dp10);
        if (isShowLastDivider) builder.showLastDivider();
        return builder.build();
    }

    /**
     * 高度 5 dp 的透明
     */
    public static RecyclerView.ItemDecoration getCommTrans5Divider(Context context, boolean isShowLastDivider) {
        HorizontalDividerItemDecoration.Builder builder = new HorizontalDividerItemDecoration.Builder(context).color(Color.TRANSPARENT).sizeResId(R.dimen.dp5);
        if (isShowLastDivider) builder.showLastDivider();
        return builder.build();
    }


    /**
     * 高度 10 dp 的透明
     */
    public static RecyclerView.ItemDecoration getCommTrans10Divider(Context context, boolean isShowLastDivider) {
        HorizontalDividerItemDecoration.Builder builder = new HorizontalDividerItemDecoration.Builder(context).color(Color.TRANSPARENT).sizeResId(R.dimen.dp10);
        if (isShowLastDivider) builder.showLastDivider();
        return builder.build();
    }

    /**
     * 外边距10 dp
     */
    public static RecyclerView.ItemDecoration getCommMagin10Divider(Context context, boolean isShowLastDivider) {
        getStyleDividerColor(context);
        HorizontalDividerItemDecoration.Builder builder = new HorizontalDividerItemDecoration.Builder(context).color(color_comm_divider).sizeResId(R.dimen.dp0_5).marginResId(R.dimen.dp10, R.dimen.dp10);
        if (isShowLastDivider) builder.showLastDivider();
        return builder.build();
    }

    /**
     * 外边距15 dp
     */
    public static RecyclerView.ItemDecoration getCommMagin15Divider(Context context, boolean isShowLastDivider) {
        getStyleDividerColor(context);
        HorizontalDividerItemDecoration.Builder builder = new HorizontalDividerItemDecoration.Builder(context).color(color_comm_divider).sizeResId(R.dimen.dp0_5).marginResId(R.dimen.dp15, R.dimen.dp15);
        if (isShowLastDivider) builder.showLastDivider();
        return builder.build();
    }


    /**
     * 外边距5 dp
     */
    public static RecyclerView.ItemDecoration getCommMagin5Divider(Context context, boolean isShowLastDivider) {
        getStyleDividerColor(context);
        HorizontalDividerItemDecoration.Builder builder = new HorizontalDividerItemDecoration.Builder(context).color(color_comm_divider).sizeResId(R.dimen.dp0_5).marginResId(R.dimen.dp5, R.dimen.dp5);
        if (isShowLastDivider) builder.showLastDivider();
        return builder.build();
    }


}

