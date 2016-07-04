package com.moetutu.acg12.view;


import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.moetutu.acg12.R;
import com.moetutu.acg12.app.AppContext;
import com.moetutu.acg12.asynctask.type.Acg12Obj;
import com.moetutu.acg12.http.RetrofitService;
import com.moetutu.acg12.http.callback.SimpleCallBack;
import com.moetutu.acg12.util.Const;

import retrofit2.Call;
import retrofit2.Response;

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

        popup_password.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
                // TODO Auto-generated method stub

                if (arg1 == EditorInfo.IME_ACTION_SEND) {
                    // 在这里编写自己想要实现的功能
                    RetrofitService.getInstance()
                            .getApiCacheRetryService()
                            .login(Const.Login, popup_mobile.getText().toString(), popup_password.getText().toString(), "1")
                            .enqueue(new SimpleCallBack<Acg12Obj>() {
                                @Override
                                public void onSuccess(Call<Acg12Obj> call, Response<Acg12Obj> response) {
                                    if (response.body() == null) return;
                                    if (response.body().getStatus().equals("success")) {
                                        com.moetutu.acg12.util.T.showShort("登陆成功! name" + response.body().getUser().getDisplay_name() + " 猫爪：" + response.body().getUser().getPoints());
                                        com.moetutu.acg12.app.ACache aCache = com.moetutu.acg12.app.ACache.get(context);
                                        aCache.put("LOGIN", response.body().getUser());
                                    } else {
                                        com.moetutu.acg12.util.T.showShort("登录大失败，可能是你的账号密码错误了呢");
                                    }
                                }
                            });
                }
                return false;
            }
        });


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
