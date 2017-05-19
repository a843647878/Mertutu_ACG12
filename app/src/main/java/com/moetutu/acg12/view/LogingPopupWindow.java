package com.moetutu.acg12.view;


import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.moetutu.acg12.R;
import com.moetutu.acg12.app.AppContext;

public class LogingPopupWindow extends PopupWindow {

    public ImageView popup_back;
    public Button popup_login, popup_regist;
    public EditText popup_mobile;
    public EditText popup_password;
    public View mMenuView;
    public LinearLayout popup_bg;
    private Activity context;
    private AppContext appContext;

    public LogingPopupWindow(final Activity context, OnClickListener itemsOnClick) {
        super(context);
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.alert_login_loging, null);
        appContext = (AppContext) context.getApplication();
        popup_back = (ImageView) mMenuView.findViewById(R.id.popup_back);
        popup_back.setOnClickListener(itemsOnClick);
        popup_login = (Button) mMenuView.findViewById(R.id.popup_login);
        popup_login.setOnClickListener(itemsOnClick);
        popup_regist = (Button) mMenuView.findViewById(R.id.popup_regist);
        popup_regist.setOnClickListener(itemsOnClick);
        popup_mobile = (EditText) mMenuView.findViewById(R.id.popup_mobile);
        popup_password = (EditText) mMenuView.findViewById(R.id.popup_password);
        popup_bg = (LinearLayout) mMenuView.findViewById(R.id.popup_bg);




        // 设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimBottom);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);

        popup_bg.getBackground().setAlpha(70);

        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
//		mMenuView.setOnTouchListener(new OnTouchListener() {
//
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//
//				int height = mMenuView.findViewById(R.id.pop_layout).getTop();
//				int y = (int) event.getY();
//				if (event.getAction() == MotionEvent.ACTION_UP) {
//					if (y < height) {
//						dismiss();
//					}
//				}
//				return false;
//			}
//		});

    }

    @Override
    public void setOutsideTouchable(boolean touchable) {
        // TODO Auto-generated method stub
        super.setOutsideTouchable(touchable);
    }


}
