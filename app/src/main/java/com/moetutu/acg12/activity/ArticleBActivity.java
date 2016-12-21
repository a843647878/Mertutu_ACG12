package com.moetutu.acg12.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.widget.TextView;

import com.moetutu.acg12.R;
import com.moetutu.acg12.app.AppContext;
import com.moetutu.acg12.entity.ArticleEntity;
import com.moetutu.acg12.entity.PostEntity;
import com.moetutu.acg12.http.RetrofitService;
import com.moetutu.acg12.http.callback.SimpleCallBack;
import com.moetutu.acg12.http.httpmodel.ResEntity;
import com.moetutu.acg12.util.GlideUtils;
import com.moetutu.acg12.util.HtmlAcgUtil;
import com.moetutu.acg12.util.LogUtils;
import com.moetutu.acg12.util.SystemUtils;
import com.moetutu.acg12.util.ToastUtils;
import com.zzhoujay.richtext.CacheType;
import com.zzhoujay.richtext.RichText;
import com.zzhoujay.richtext.callback.OnImageClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Description
 * Company guokeyuzhou
 * Created by chengwanying on 16/7/5.
 * version
 */
public class ArticleBActivity extends BaseActivity {

    public static String KEY_ARTICLEID;
    @BindView(R.id.tv_html)
    TextView tvHtml;
    @BindView(R.id.download_button)
    FloatingActionButton downloadButton;


    private int wendangid;
    public AppContext appContext;
    public ArticleEntity data;

    public static void launch(Context context, int wendangid) {
        if (context == null) return;
        if (wendangid == 0) return;
        Intent in = new Intent(context, ArticleBActivity.class);
        in.putExtra(KEY_ARTICLEID, wendangid);
        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
        context.startActivity(in);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_b);
        ButterKnife.bind(this);
        appContext = AppContext.getApplication();
        wendangid = getIntent().getIntExtra(KEY_ARTICLEID, 0);
        initView(this);
        initData();
    }

    @Override
    public void initView(Activity activity) {
        super.initView(activity);
        setZhuTitle();
        setTitleLeftImageBack();

    }

    @Override
    public void initData() {
        super.initData();
        RetrofitService.getInstance()
                .getApiCacheRetryService()
                .getPost(RetrofitService.getInstance().getToken(), wendangid, null)
                .enqueue(new SimpleCallBack<PostEntity>() {
                    @Override
                    public void onSuccess(Call<ResEntity<PostEntity>> call, Response<ResEntity<PostEntity>> response) {
                        if (response.body() == null) return;
                        data = response.body().data.post;
                        String html = data.content.replace("//static", "https://static");
                        LogUtils.d("--------->" + html);
                        RichText
                                .fromHtml(html) // 数据源
//                                .type(RichText.TYPE_MARKDOWN) // 数据格式,不设置默认是Html,使用fromMarkdown的默认是Markdown格式
                                .autoFix(true) // 是否自动修复，默认true
                                //.async(true)  已取消异步的接口，交给调用者自己处理
//                                .fix(imageFixCallback) // 设置自定义修复图片宽高
//                                .fixLink(linkFixCallback) // 设置链接自定义回调
//                                .noImage(true) // 不显示并且不加载图片
                                .resetSize(false) // 默认false，是否忽略img标签中的宽高尺寸（只在img标签中存在宽高时才有效），true：忽略标签中的尺寸并触发SIZE_READY回调，false：使用img标签中的宽高尺寸，不触发SIZE_READY回调
                                .clickable(true) // 是否可点击，默认只有设置了点击监听才可点击
                                .imageClick(new OnImageClickListener() {
                                    @Override
                                    public void imageClicked(List<String> imageUrls, int position) {
                                        ImagePagerActivity.launch(context, imageUrls, position + 1);
                                    }
                                }) // 设置图片点击回调
//                                .imageLongClick(onImageLongClickListener) // 设置图片长按回调
//                                .urlClick(onURLClickListener) // 设置链接点击回调
//                                .urlLongClick(onUrlLongClickListener) // 设置链接长按回调
                                .placeHolder(R.mipmap.cat_loging) // 设置加载中显示的占位图
                                .error(R.mipmap.cat_loging) // 设置加载失败的错误图
                                .cache(CacheType.NONE) // 缓存类型，默认为Cache.LAYOUT（不缓存图片，只缓存图片大小信息和文本样式信息）
//                                .imageGetter() // 设置图片加载器，默认为DefaultImageGetter，使用okhttp实现
//                                .bind(tag) // 绑定richText对象到某个object上，方便后面的清理
                                .into(tvHtml); // 设置目标TextView
                    }
                });
    }


    @OnClick(R.id.download_button)
    public void onClick() {
        if (data.storage.items.size() > 0) {
            SystemUtils.copyToClipboard(context, "text", data.storage.items.get(0).downloadPwd);
            ToastUtils.showRoundRectToast("提取密码已经复制到剪切板啦!");
            Uri uri = Uri.parse(data.storage.items.get(0).url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } else {
            ToastUtils.showRoundRectToast("没发现下载地址");
        }
    }
}
