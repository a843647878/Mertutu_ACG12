package com.moetutu.acg12.activity;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.moetutu.acg12.R;

public class LoginAcitivity extends BaseActivity {
	private EditText youxiang;
	private EditText pass;
	private Button login_button;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initView(this);
	}
	@Override
	public void initView(Activity activity) {
		// TODO Auto-generated method stub
		super.initView(activity);
		setZhuTitle("登录");
		setTitleLeftImageBack();
		youxiang=(EditText) activity.findViewById(R.id.login_mobile);
		pass=(EditText) activity.findViewById(R.id.login_password);
		login_button=(Button) activity.findViewById(R.id.login_button);
		login_button.setOnClickListener(this);
	}
	
	@Override
	public void initData() {
		// TODO Auto-generated method stub
		super.initData();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.login_button:

			
			break;

		default:
			break;
		}
	}

}
