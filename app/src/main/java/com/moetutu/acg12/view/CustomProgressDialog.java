package com.moetutu.acg12.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.TextView;

import com.moetutu.acg12.R;


public class CustomProgressDialog extends Dialog {
	private Context context;
	private boolean mCanceledOnTouchOutside = true;
	protected boolean mCancelable = true;

	public CustomProgressDialog(Context context, String strMessage,boolean canback) {
		this(context, R.style.CustomProgressDialog, strMessage);
		this.mCancelable = canback;
	}

	public CustomProgressDialog(Context context, int theme, String strMessage) {
		super(context, theme);
		this.context = context;
		this.setContentView(R.layout.customprogressdialog);
		this.getWindow().getAttributes().gravity = Gravity.CENTER;
		TextView tvMsg = (TextView) this.findViewById(R.id.id_tv_loadingmsg);
		if (tvMsg != null) {
			tvMsg.setText(strMessage);
		}
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {

		if (!hasFocus) {
			dismiss();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mCancelable && mCanceledOnTouchOutside
				&& event.getAction() == MotionEvent.ACTION_DOWN
				&& isOutOfBounds(event)) {
			cancel();
			return true;
		}

		return false;
	}

	private boolean isOutOfBounds(MotionEvent event) {
		final int x = (int) event.getX();
		final int y = (int) event.getY();
		final int slop = ViewConfiguration.get(context)
				.getScaledWindowTouchSlop();
		final View decorView = getWindow().getDecorView();
		return (x < -slop) || (y < -slop)
				|| (x > (decorView.getWidth() + slop))
				|| (y > (decorView.getHeight() + slop));
	}

}