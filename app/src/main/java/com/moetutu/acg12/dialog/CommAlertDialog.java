package com.moetutu.acg12.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.moetutu.acg12.R;


/**
 * @author xuanyouwu
 * @email xuanyouwu@163.com
 * @time 2016-07-27 11:00
 * 大部分通用对话框
 * 可以扩展
 */

public class CommAlertDialog extends AlertDialog {
    protected CommAlertDialog(@NonNull Context context) {
        super(context);
    }

    protected CommAlertDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    protected CommAlertDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }


    public static class Builder implements View.OnClickListener {
        private Context context;
        private CharSequence title;
        private CharSequence notice;
        private CharSequence positiveButtonText;
        private int positiveButtonBgColor = 0x00000000;//透明
        private int positiveButtonTextColor = 0xff000000;//纯黑色
        private CharSequence negativeButtonText;
        private int negativeButtonBgColor = 0x00000000;//透明
        private int negativeButtonTextColor = 0xff000000;//纯黑色
        private OnClickListener positiveClickListener;
        private OnClickListener negativeClickListener;
        private CommAlertDialog alertDialog;
        private View rootView;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setTitle(CharSequence title) {
            this.title = title;
            return this;
        }

        public Builder setNotice(CharSequence notice) {
            this.notice = notice;
            return this;
        }

        public Builder setPositiveButton(CharSequence positiveButtonText, OnClickListener positiveClickListener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveClickListener = positiveClickListener;
            return this;
        }


        public Builder setNegativeButton(CharSequence negativeButtonText, OnClickListener negativeClickListener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeClickListener = negativeClickListener;
            return this;
        }

        public Builder setPositiveButtonTextColor(int positiveButtonTextColor) {
            this.positiveButtonTextColor = positiveButtonTextColor;
            return this;
        }

        public Builder setNegativeButtonTextColor(int negativeButtonTextColor) {
            this.negativeButtonTextColor = negativeButtonTextColor;
            return this;
        }

        public Builder setPositiveButtonBgColor(int positiveButtonBgColor) {
            this.positiveButtonBgColor = positiveButtonBgColor;
            return this;
        }

        public Builder setNegativeButtonBgColor(int negativeButtonBgColor) {
            this.negativeButtonBgColor = negativeButtonBgColor;
            return this;
        }

        public CommAlertDialog create() {
            alertDialog = new CommAlertDialog(context);
            rootView = View.inflate(context, R.layout.dialog_comm_dialog, null);
            TextView dialog_title = (TextView) rootView.findViewById(R.id.dialog_title);
            if (TextUtils.isEmpty(title)) {
                dialog_title.setVisibility(View.GONE);
            } else {
                dialog_title.setVisibility(View.VISIBLE);
                dialog_title.setText(title);
            }
            TextView dialog_notice = (TextView) rootView.findViewById(R.id.dialog_notice);
            dialog_notice.setText(notice);

            Button bt_positive = (Button) rootView.findViewById(R.id.bt_positive);
            bt_positive.setBackgroundColor(positiveButtonBgColor);
            bt_positive.setTextColor(positiveButtonTextColor);
            bt_positive.setText(positiveButtonText);
            bt_positive.setOnClickListener(this);

            Button bt_negative = (Button) rootView.findViewById(R.id.bt_negative);
            bt_negative.setBackgroundColor(negativeButtonBgColor);
            bt_negative.setTextColor(negativeButtonTextColor);
            bt_negative.setOnClickListener(this);
            bt_negative.setText(negativeButtonText);

            return alertDialog;
        }

        public void show() {
            create().show();
            alertDialog.setContentView(rootView);
            /**
             *   Caused by: android.util.AndroidRuntimeException: requestFeature() must be called before adding content
             *   相等于窗体构建之前
             */

            Window win = alertDialog.getWindow();
            win.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            win.getDecorView().setPadding(0, 0, 0, 0);
        }

        @Override
        public void onClick(View view) {
            if (view == null) return;
            if (alertDialog != null)
                alertDialog.dismiss();
            switch (view.getId()) {
                case R.id.bt_positive:
                    if (positiveClickListener != null) {
                        positiveClickListener.onClick(alertDialog, DialogInterface.BUTTON_POSITIVE);
                    }
                    break;
                case R.id.bt_negative:
                    if (negativeClickListener != null) {
                        negativeClickListener.onClick(alertDialog, DialogInterface.BUTTON_NEGATIVE);
                    }
                    break;
            }
        }
    }
}
