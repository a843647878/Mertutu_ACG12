package com.moetutu.acg12.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.widget.TextView;

import com.moetutu.acg12.R;
import com.moetutu.acg12.entity.ArticleEntity;
import com.moetutu.acg12.entity.PostEntity;
import com.moetutu.acg12.http.RetrofitService;
import com.moetutu.acg12.http.callback.SimpleCallBack;
import com.moetutu.acg12.http.httpmodel.ResEntity;
import com.moetutu.acg12.util.LogUtils;
import com.moetutu.acg12.util.SystemUtils;
import com.moetutu.acg12.util.ToastUtils;
import com.moetutu.acg12.util.bilibili.BiliDanmukuParser;
import com.zzhoujay.richtext.CacheType;
import com.zzhoujay.richtext.RichText;
import com.zzhoujay.richtext.callback.OnImageClickListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import master.flame.danmaku.controller.IDanmakuView;
import master.flame.danmaku.danmaku.loader.ILoader;
import master.flame.danmaku.danmaku.loader.IllegalDataException;
import master.flame.danmaku.danmaku.loader.android.DanmakuLoaderFactory;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDanmakus;
import master.flame.danmaku.danmaku.model.IDisplayer;
import master.flame.danmaku.danmaku.model.android.BaseCacheStuffer;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.model.android.SpannedCacheStuffer;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.danmaku.parser.IDataSource;
import master.flame.danmaku.danmaku.util.IOUtils;
import master.flame.danmaku.danmaku.util.SystemClock;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Description
 * Company guokeyuzhou
 * Created by chengwanying on 16/7/5.
 * version
 */
public class ArticleDMActivity extends BaseActivity {

    public static String KEY_ARTICLEID = "key_articleid";
    @BindView(R.id.tv_html)
    TextView tvHtml;
    @BindView(R.id.download_button)
    FloatingActionButton downloadButton;
    @BindView(R.id.sv_danmaku)
    IDanmakuView svDanmaku;//我也不知道为什么会是IDanmakuView  控件使用的是的DanmakuView


    private BaseDanmakuParser mParser;  //数据源

    private DanmakuContext mContext;//弹幕配置上下文
    private BaseCacheStuffer.Proxy mCacheStufferAdapter = new BaseCacheStuffer.Proxy() {

        private Drawable mDrawable;

        @Override
        public void prepareDrawing(final BaseDanmaku danmaku, boolean fromWorkerThread) {
            if (danmaku.text instanceof Spanned) { // 根据你的条件检查是否需要需要更新弹幕
                // FIXME 这里只是简单启个线程来加载远程url图片，请使用你自己的异步线程池，最好加上你的缓存池
                new Thread() {

                    @Override
                    public void run() {
                        String url = "http://www.bilibili.com/favicon.ico";
                        InputStream inputStream = null;
                        Drawable drawable = mDrawable;
                        if (drawable == null) {
                            try {
                                URLConnection urlConnection = new URL(url).openConnection();
                                inputStream = urlConnection.getInputStream();
                                drawable = BitmapDrawable.createFromStream(inputStream, "bitmap");
                                mDrawable = drawable;
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                IOUtils.closeQuietly(inputStream);
                            }
                        }
                        if (drawable != null) {
                            drawable.setBounds(0, 0, 100, 100);
                            SpannableStringBuilder spannable = createSpannable(drawable);
                            danmaku.text = spannable;
                            if (svDanmaku != null) {
                                svDanmaku.invalidateDanmaku(danmaku, false);
                            }
                            return;
                        }
                    }
                }.start();
            }
        }

        @Override
        public void releaseResource(BaseDanmaku danmaku) {
            // TODO 重要:清理含有ImageSpan的text中的一些占用内存的资源 例如drawable
        }
    };


    private int wendangid;
    public ArticleEntity data;

    public static void launch(Context context, int wendangid) {
        if (context == null) return;
        if (wendangid == 0) return;
        Intent in = new Intent(context, ArticleDMActivity.class);
        in.putExtra(KEY_ARTICLEID, wendangid);
        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(in);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_dm);
        ButterKnife.bind(this);
        wendangid = getIntent().getIntExtra(KEY_ARTICLEID, 0);
        initView(this);
        initData();
    }

    @Override
    public void initView(Activity activity) {
        super.initView(activity);
        setZhuTitle();
        setTitleLeftImageBack();

        // 设置最大显示行数
        HashMap<Integer, Integer> maxLinesPair = new HashMap<Integer, Integer>();
        maxLinesPair.put(BaseDanmaku.TYPE_SCROLL_RL, 5); // 滚动弹幕最大显示5行
        // 设置是否禁止重叠
        HashMap<Integer, Boolean> overlappingEnablePair = new HashMap<Integer, Boolean>();
        overlappingEnablePair.put(BaseDanmaku.TYPE_SCROLL_RL, true);
        overlappingEnablePair.put(BaseDanmaku.TYPE_FIX_TOP, true);

        mContext = DanmakuContext.create();
        mContext.setDanmakuStyle(IDisplayer.DANMAKU_STYLE_STROKEN, 3).setDuplicateMergingEnabled(false).setScrollSpeedFactor(1.2f).setScaleTextSize(1.2f)
                .setCacheStuffer(new SpannedCacheStuffer(), mCacheStufferAdapter) // 图文混排使用SpannedCacheStuffer
//        .setCacheStuffer(new BackgroundCacheStuffer())  // 绘制背景使用BackgroundCacheStuffer
                .setMaximumLines(maxLinesPair)
                .preventOverlapping(overlappingEnablePair);
        if (svDanmaku != null) {
            mParser = createParser(this.getResources().openRawResource(R.raw.comments));
            svDanmaku.setCallback(new master.flame.danmaku.controller.DrawHandler.Callback() {
                @Override
                public void updateTimer(DanmakuTimer timer) {
                }

                @Override
                public void drawingFinished() {

                }

                @Override
                public void danmakuShown(BaseDanmaku danmaku) {
//                    Log.d("DFM", "danmakuShown(): text=" + danmaku.text);
                }

                @Override
                public void prepared() {
                    svDanmaku.start();
                }
            });
            svDanmaku.setOnDanmakuClickListener(new IDanmakuView.OnDanmakuClickListener() {

                @Override
                public boolean onDanmakuClick(IDanmakus danmakus) {
                    Log.d("DFM", "onDanmakuClick: danmakus size:" + danmakus.size());
                    BaseDanmaku latest = danmakus.last();
                    if (null != latest) {
                        Log.d("DFM", "onDanmakuClick: text of latest danmaku:" + latest.text);
                        return true;
                    }
                    return false;
                }

                @Override
                public boolean onViewClick(IDanmakuView view) {
//                    mMediaController.setVisibility(View.VISIBLE);
                    return false;
                }
            });
            svDanmaku.prepare(mParser, mContext);
            svDanmaku.showFPS(true);
            svDanmaku.enableDanmakuDrawingCache(true);
        }

    }

    /*
    * 填充弹幕数据
    * */
    private BaseDanmakuParser createParser(InputStream stream) {

        if (stream == null) {
            return new BaseDanmakuParser() {

                @Override
                protected Danmakus parse() {
                    return new Danmakus();
                }
            };
        }

        ILoader loader = DanmakuLoaderFactory.create(DanmakuLoaderFactory.TAG_BILI);

        try {
            loader.load(stream);
        } catch (IllegalDataException e) {
            e.printStackTrace();
        }
        BaseDanmakuParser parser = new BiliDanmukuParser();
        IDataSource<?> dataSource = loader.getDataSource();
        parser.load(dataSource);
        return parser;

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
                                        ImagePagerActivity.launch(context,data.title, imageUrls, position + 1);
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


    Timer timer = new Timer();

    class AsyncAddTask extends TimerTask {

        @Override
        public void run() {
            for (int i = 0; i < 20; i++) {
                addDanmaku(true);
                SystemClock.sleep(20);
            }
        }
    }

    ;

    private void addDanmaku(boolean islive) {
        BaseDanmaku danmaku = mContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        if (danmaku == null || svDanmaku == null) {
            return;
        }
        // for(int i=0;i<100;i++){
        // }
        danmaku.text = "这是一条弹幕" + System.nanoTime();
        danmaku.padding = 5;
        danmaku.priority = 0;  // 可能会被各种过滤器过滤并隐藏显示
        danmaku.isLive = islive;
        danmaku.setTime(svDanmaku.getCurrentTime() + 1200);
        danmaku.textSize = 25f * (mParser.getDisplayer().getDensity() - 0.6f);
        danmaku.textColor = Color.RED;
        danmaku.textShadowColor = Color.WHITE;
        // danmaku.underlineColor = Color.GREEN;
        danmaku.borderColor = Color.GREEN;
        svDanmaku.addDanmaku(danmaku);

    }

    private void addDanmaKuShowTextAndImage(boolean islive) {
        BaseDanmaku danmaku = mContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        Drawable drawable = getResources().getDrawable(R.mipmap.ic_launcher);
        drawable.setBounds(0, 0, 100, 100);
        SpannableStringBuilder spannable = createSpannable(drawable);
        danmaku.text = spannable;
        danmaku.padding = 5;
        danmaku.priority = 1;  // 一定会显示, 一般用于本机发送的弹幕
        danmaku.isLive = islive;
        danmaku.setTime(svDanmaku.getCurrentTime() + 1200);
        danmaku.textSize = 25f * (mParser.getDisplayer().getDensity() - 0.6f);
        danmaku.textColor = Color.RED;
        danmaku.textShadowColor = 0; // 重要：如果有图文混排，最好不要设置描边(设textShadowColor=0)，否则会进行两次复杂的绘制导致运行效率降低
        danmaku.underlineColor = Color.GREEN;
        svDanmaku.addDanmaku(danmaku);
    }

    private SpannableStringBuilder createSpannable(Drawable drawable) {
        String text = "bitmap";
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
        ImageSpan span = new ImageSpan(drawable);//ImageSpan.ALIGN_BOTTOM);
        spannableStringBuilder.setSpan(span, 0, text.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.append("图文混排");
        spannableStringBuilder.setSpan(new BackgroundColorSpan(Color.parseColor("#8A2233B1")), 0, spannableStringBuilder.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return spannableStringBuilder;
    }




    @Override
    protected void onPause() {
        super.onPause();
        if (svDanmaku != null && svDanmaku.isPrepared()) {
            svDanmaku.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (svDanmaku != null && svDanmaku.isPrepared() && svDanmaku.isPaused()) {
            svDanmaku.resume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (svDanmaku != null) {
            // dont forget release!
            svDanmaku.release();
            svDanmaku = null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (svDanmaku != null) {
            // dont forget release!
            svDanmaku.release();
            svDanmaku = null;
        }
    }



}
