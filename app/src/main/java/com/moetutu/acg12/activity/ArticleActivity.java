package com.moetutu.acg12.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moetutu.acg12.R;
import com.moetutu.acg12.adapter.WenDangAdapter;
import com.moetutu.acg12.app.AppContext;
import com.moetutu.acg12.entity.ArticleEntity;
import com.moetutu.acg12.entity.PostEntity;
import com.moetutu.acg12.entity.WenDangMode;
import com.moetutu.acg12.http.RetrofitService;
import com.moetutu.acg12.http.callback.SimpleCallBack;
import com.moetutu.acg12.http.httpmodel.ResEntity;
import com.moetutu.acg12.util.Const;
import com.moetutu.acg12.util.GlideUtils;
import com.moetutu.acg12.util.ItemDecorationUtils;
import com.moetutu.acg12.util.T;
import com.moetutu.acg12.view.widget.BaseRecyclerAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Description
 * Company guokeyuzhou
 * Created by chengwanying on 16/7/5.
 * version
 */
public class ArticleActivity extends BaseActivity implements AppBarLayout.OnOffsetChangedListener, BaseRecyclerAdapter.OnItemClickListener {

    public static String KEY_ARTICLEID;

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;
    @BindView(R.id.background_imageview)
    ImageView backgroundImageview;
    @BindView(R.id.framelayout_title)
    TextView framelayoutTitle;
    @BindView(R.id.linearlayout_title)
    LinearLayout mTitlelinearlayout;
    @BindView(R.id.main_appbar)
    AppBarLayout appbar;
    @BindView(R.id.picture_recycler)
    RecyclerView pictureRecycler;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.main_toolbar)
    Toolbar mainToolbar;
    @BindView(R.id.head_portrait)
    CircleImageView headPortrait;
    @BindView(R.id.framelayout_details)
    TextView framelayoutDetails;
    @BindView(R.id.activity_wendang)
    CoordinatorLayout activityWendang;

    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = true;

    private List<String> list = new ArrayList<String>();
    private WenDangAdapter adapter;
    private int wendangid;
    private String url;
    public AppContext appContext;

    public static void launch(Context context, int wendangid) {
        if (context == null) return;
        if (wendangid == 0) return;
        Intent in = new Intent(context, ArticleActivity.class);
        in.putExtra(KEY_ARTICLEID, wendangid);
        context.startActivity(in);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
//        setImmerseLayout(findViewById(R.id.activity_wendang));
        ButterKnife.bind(this);
        appContext = AppContext.getApplication();
        wendangid = getIntent().getIntExtra(KEY_ARTICLEID,0);
        initView(this);
        initData();
    }

    @Override
    public void initView(Activity activity) {
        super.initView(activity);
        pictureRecycler.setLayoutManager(new LinearLayoutManager(this));
        pictureRecycler.addItemDecoration(ItemDecorationUtils.getCommTrans5Divider(ArticleActivity.this, true));

        adapter = new WenDangAdapter();
        adapter.setOnItemClickListener(this);
        pictureRecycler.setAdapter(adapter);

        appbar.addOnOffsetChangedListener(this);

        mainToolbar.inflateMenu(R.menu.menu_main);
        mainToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_share:
                        if (!TextUtils.isEmpty(url)) {
                            T.showShort("正在跳入异次元请求下载~~~");
                            Uri uri = Uri.parse(url);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                        }
                        break;
                }
                return true;
            }
        });
        startAlphaAnimation(toolbarTitle, 0, View.INVISIBLE);
    }

    @Override
    public void initData() {
        super.initData();
        RetrofitService.getInstance()
                .getApiCacheRetryService()
                .getPost(RetrofitService.getInstance().getToken(), wendangid,null)
                .enqueue(new SimpleCallBack<PostEntity>() {
                    @Override
                    public void onSuccess(Call<ResEntity<PostEntity>> call, Response<ResEntity<PostEntity>> response) {
                        if (response.body() == null) return;
                        ArticleEntity data = response.body().data.post;
                        framelayoutTitle.setText(data.author.name);

                        toolbarTitle.setText(data.title);

                        GlideUtils.loadUser(headPortrait.getContext(),data.author.avatarURL,headPortrait);

                        GlideUtils.loadDetails(backgroundImageview.getContext(), data.thumbnail.url, backgroundImageview);

                        String s = data.excerpt;
                        framelayoutDetails.setText(s.replaceAll("[&hellip;]", ""));
//                        wendang_text.setText(stripHtml(data.getPost().getPost_content()));
                        url = data.url;
                        list.clear();

                        list = quChu(getImgStr(data.content));

                        adapter.bindData(true, list);
                    }
                });
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;

        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);
    }

    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if (!mIsTheTitleVisible) {
                startAlphaAnimation(toolbarTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(toolbarTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitlelinearlayout, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitlelinearlayout, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }

    public static void startAlphaAnimation(View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }

    @Override
    public void onItemClick(BaseRecyclerAdapter adapter, BaseRecyclerAdapter.ViewHolder holder, View view, int position) {
        ImagePagerActivity.launch(context, (ArrayList<String>) list,position);
    }

    public static String stripHtml(String content) {
        // <p>段落替换为换行
        content = content.replaceAll("<p .*?>", "\r\n");
        // <br><br/>替换为换行
        content = content.replaceAll("<br\\s*/?>", "\r\n");
        // 去掉其它的<>之间的东西
        content = content.replaceAll("\\<.*?>", "");
        // 还原HTML
        // content = HTMLDecoder.decode(content);
        content = content.replaceAll("&nbsp;", "");
        content = content.replaceAll("&#8211;", "");
        content = content.replaceAll("&#8221;", "");
        content = content.replaceAll("&#amp;", "");
        return content;
    }


    public static List<String> getImgStr(String htmlStr) {
        String img = "";
        Pattern p_image;
        Matcher m_image;
        List<String> pics = new ArrayList<String>();

        //     String regEx_img = "<img.*src=(.*?)[^>]*?>"; //图片链接地址

        String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
        p_image = Pattern.compile
                (regEx_img, Pattern.CASE_INSENSITIVE);
        m_image = p_image.matcher(htmlStr);
        while (m_image.find()) {
            img = img + "," + m_image.group();
            // Matcher m  = Pattern.compile("src=\"?(.*?)(\"|>|\\s+)").matcher(img); //匹配src

            Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);

            while (m.find()) {
                pics.add(m.group(1));
            }
        }
        return pics;
    }


    public List<String> quChu(List<String> l) {
        for (int i = 0; i < l.size(); i++)  //外循环是循环的次数
        {
            for (int j = l.size() - 1; j > i; j--)  //内循环是 外循环一次比较的次数
            {

                if (l.get(i).equals(l.get(j))) {
                    l.remove(j);
                }

            }
        }
        return l;
    }
}
