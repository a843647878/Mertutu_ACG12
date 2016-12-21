package com.moetutu.acg12.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.moetutu.acg12.R;
import com.moetutu.acg12.util.HtmlAcgUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description
 * Created by chengwanying on 2016/12/5.
 * Company BeiJing guokeyuzhou
 */

public class SwitchBgActivity extends BaseActivity {
    @BindView(R.id.back_icon)
    ImageView backIcon;
    @BindView(R.id.acg_name)
    TextView acgName;
    @BindView(R.id.tv_acg)
    TextView tvAcg;
    @BindView(R.id.tv_jilao)
    TextView tvJilao;
    @BindView(R.id.tv_pangci)
    TextView tvPangci;
    @BindView(R.id.tv_shaonv)
    TextView tvShaonv;
    @BindView(R.id.tv_yima)
    TextView tvYima;

    public static void launch(Context context) {
        if (context == null) return;
        Intent intent = new Intent(context, SwitchBgActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switchbg);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.back_icon, R.id.tv_acg, R.id.tv_jilao, R.id.tv_pangci, R.id.tv_shaonv, R.id.tv_yima})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_icon:
                finish();
                break;
            case R.id.tv_acg:
                HtmlAcgUtil.switchAppTheme(context,"1");
                finish();
                break;
            case R.id.tv_jilao:
                HtmlAcgUtil.switchAppTheme(context,"2");
                finish();
                break;
            case R.id.tv_pangci:
                HtmlAcgUtil.switchAppTheme(context,"3");
                finish();
                break;
            case R.id.tv_shaonv:
                HtmlAcgUtil.switchAppTheme(context,"4");
                finish();
                break;
            case R.id.tv_yima:
                HtmlAcgUtil.switchAppTheme(context,"5");
                finish();
                break;
        }
    }
}
