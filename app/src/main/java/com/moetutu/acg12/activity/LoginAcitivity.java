package com.moetutu.acg12.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.moetutu.acg12.R;
import com.moetutu.acg12.entity.UserEntity;
import com.moetutu.acg12.entity.eventmodel.UserEvent;
import com.moetutu.acg12.http.RetrofitService;
import com.moetutu.acg12.http.callback.SimpleCallBack;
import com.moetutu.acg12.http.httpmodel.ResEntity;
import com.moetutu.acg12.presenter.UserDbPresenter;
import com.moetutu.acg12.util.SharedPreferrenceHelper;
import com.moetutu.acg12.view.ProgressButton;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class LoginAcitivity extends BaseActivity {


    @BindView(R.id.tv_settings)
    TextView tvSettings;

    public static void launch(Context context) {
        Intent intent = new Intent(context, LoginAcitivity.class);
        context.startActivity(intent);
    }

    public static final int COLOR_ACG_FEN = 0xFFFF4590;
    public static final int COLOR_JILAOZI = 0xFF9A27AE;
    public static final int COLOR_PANGCILAN = 0xFF2196F3;
    public static final int COLOR_SHAONVFEN = 0xFFFB7299;
    public static final int COLOR_YIMAHONG = 0xFFF44336;

    @BindView(R.id.login_password)
    EditText loginPassword;
    @BindView(R.id.login_button)
    ProgressButton loginButton;
    @BindView(R.id.login_email)
    EditText loginEmail;
    @BindView(R.id.textinput_email)
    TextInputLayout textinputEmail;
    @BindView(R.id.textinput_password)
    TextInputLayout textinputPassword;

    private UserDbPresenter presenter;
    private UserEntity user;

    Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        presenter = new UserDbPresenter(this);
        initView(this);
    }

    @Override
    public void initView(Activity activity) {
        // TODO Auto-generated method stub
        super.initView(activity);
        setZhuTitle("登录");
        setTitleLeftImageBack();

        switch (SharedPreferrenceHelper.gettheme(activity)) {
            case "1":
                loginButton.setBgColor(COLOR_ACG_FEN);
                break;
            case "2":
                loginButton.setBgColor(COLOR_JILAOZI);
                break;
            case "3":
                loginButton.setBgColor(COLOR_PANGCILAN);
                break;
            case "4":
                loginButton.setBgColor(COLOR_SHAONVFEN);
                break;
            case "5":
                loginButton.setBgColor(COLOR_YIMAHONG);
                break;

        }
        loginButton.setTextColor(Color.WHITE);
        loginButton.setProColor(Color.WHITE);
        loginButton.setButtonText("登录");
        textinputEmail.setHint("请输入邮箱");
        textinputPassword.setHint("请输入密码");
    }


    @Override
    public void initData() {
        // TODO Auto-generated method stub
        super.initData();
        if (presenter == null) return;
        if (TextUtils.isEmpty(loginEmail.getText().toString())) return;
        if (TextUtils.isEmpty(loginPassword.getText().toString())) return;
        RetrofitService
                .getInstance()
                .getApiCacheRetryService()
                .login(RetrofitService.getInstance().getToken(), loginEmail.getText().toString(), loginPassword.getText().toString())
                .enqueue(new SimpleCallBack<UserEvent>() {
                    @Override
                    public void onSuccess(Call<ResEntity<UserEvent>> call, Response<ResEntity<UserEvent>> response) {
                        if (response.body().data == null) return;

                        user = new UserEntity();
                        user.setToken(RetrofitService.getInstance().getToken());
                        user.setID((long) 7788);
                        user.setUid(response.body().data.user.uid);
                        user.setName(response.body().data.user.name);
                        user.setUrl(response.body().data.user.url);
                        user.setAvatarUrl(response.body().data.user.avatarUrl);
                        user.setPoint(response.body().data.user.point);
                        try {
                            presenter.update(user);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        EventBus.getDefault().post(new UserEvent(user));
                        loginButton.startAnim();
                        handler.removeCallbacksAndMessages(null);
                        handler.postDelayed(flashTask, 1500);
                    }
                });

    }


    private Runnable flashTask = new Runnable() {
        @Override
        public void run() {
            loginButton.stopAnim(new ProgressButton.OnStopAnim() {
                @Override
                public void Stop() {
                    finish();
                }
            });
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            try {
                presenter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    @OnClick({R.id.login_button, R.id.tv_settings})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_button:
                initData();
                break;
            case R.id.tv_settings:
                Uri uri = Uri.parse("https://acg12.com/account/?tab=settings");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
        }
    }
}
