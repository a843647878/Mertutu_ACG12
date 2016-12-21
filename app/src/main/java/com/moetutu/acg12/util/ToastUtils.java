package com.moetutu.acg12.util;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;

import com.moetutu.acg12.R;
import com.moetutu.acg12.app.AppContext;
import com.moetutu.acg12.view.refresh.DensityUtil;


/**
 * Description
 * Toast 工具类
 * Company Beijing guokeyuzhou
 * author  youxuan  E-mail:xuanyouwu@163.com
 * date createTime：16/7/7
 * version
 */
public class ToastUtils {

    private ToastUtils() {
    }

    /**
     * 顶部 全屏宽度
     *
     * @param notice
     */
    public static void showFillToast(CharSequence notice) {
        if (TextUtils.isEmpty(notice)) return;
        Context context = AppContext.getApplication();
        if (context == null) return;
        new Builder(context)
                .setDuration(Toast.LENGTH_SHORT)
                .setFill(true)
                .setGravity(Gravity.TOP)
                .setyOffset(DensityUtil.dip2px(context, 52))
                .setTitle(notice)
                .setTextColor(Color.WHITE)
                .setBackgroundColor(0XFFFF4590)
                .setRadius(0)
                .setElevation(0)
                .build()
                .show();
    }


    /**
     * 圆角
     * 屏幕中间
     *
     * @param notice
     */
    public static void showRoundRectToast(CharSequence notice) {
        if (TextUtils.isEmpty(notice)) return;
        Context context = AppContext.getApplication();
        if (context == null) return;
        new Builder(context)
                .setDuration(Toast.LENGTH_SHORT)
                .setFill(false)
                .setGravity(Gravity.CENTER)
                .setyOffset(0)
                .setTitle(notice)
                .setTextColor(Color.WHITE)
                .setBackgroundColor(0XFFFF4081)
                .setRadius(DensityUtil.dip2px(context, 4))
                .setElevation(DensityUtil.dip2px(context, 4))
                .build()
                .show();
    }


    public static class Builder {
        private Context context;

        public Builder(Context context) {
            this.context = context;
        }

        private CharSequence title;

        public Builder setTitle(CharSequence title) {
            this.title = title;
            return this;
        }

        public Builder setGravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        public Builder setFill(boolean fill) {
            isFill = fill;
            return this;
        }

        public Builder setyOffset(int yOffset) {
            this.yOffset = yOffset;
            return this;
        }

        public Builder setDuration(int duration) {
            this.duration = duration;
            return this;
        }

        public Builder setTextColor(int textColor) {
            this.textColor = textColor;
            return this;
        }

        public Builder setBackgroundColor(int backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        public Builder setRadius(float radius) {
            this.radius = radius;
            return this;
        }

        public Builder setElevation(int elevation) {
            this.elevation = elevation;
            return this;
        }

        private int gravity = Gravity.TOP;
        private boolean isFill;
        private int yOffset;
        private int duration = Toast.LENGTH_SHORT;
        private int textColor = Color.WHITE;
        private int backgroundColor = Color.BLACK;
        private float radius;
        private int elevation;

        public Toast build() {
            Toast toast = new Toast(context);
            if (isFill) {
                toast.setGravity(gravity | Gravity.FILL_HORIZONTAL, 0, yOffset);
            } else {
                toast.setGravity(gravity, 0, yOffset);
            }
            toast.setDuration(duration);
            toast.setMargin(0, 0);
            CardView rootView = (CardView) LayoutInflater.from(context).inflate(R.layout.view_toast, null);
            TextView textView = (TextView) rootView.findViewById(R.id.toastTextView);
            rootView.setCardElevation(elevation);
            rootView.setRadius(radius);
            rootView.setCardBackgroundColor(backgroundColor);
            textView.setTextColor(textColor);
            toast.setView(rootView);
            textView.setText(title);
            return toast;
        }
    }

}
