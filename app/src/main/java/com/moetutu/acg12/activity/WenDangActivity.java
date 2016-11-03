package com.moetutu.acg12.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
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
import com.moetutu.acg12.util.ItemDecorationUtils;
import com.moetutu.acg12.util.T;
import com.moetutu.acg12.view.gamerefreshview.FunGameRefreshView;
import com.moetutu.acg12.view.widget.BaseRecyclerAdapter;
import com.moetutu.acg12.view.widget.HeaderFooterAdapter;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Response;


/**
 * Description  文档的activity
 * Company guokeyuzhou
 * Created by chengwanying on 16/6/15.
 * version
 */
public class WenDangActivity extends BaseActivity implements BaseRecyclerAdapter.OnItemClickListener {

    public static void launch(Context context, int wendangid) {
        Intent intent = new Intent(context, WenDangActivity.class);
        intent.putExtra("id", wendangid);
        context.startActivity(intent);
    }

    private int wendangid;
    private TextView title_text;
    private TextView excerpt_text;
    private TextView wendang_text;
    private AppContext appContext;
    private WenDangAdapter adapter;
    private HeaderFooterAdapter<WenDangAdapter> rootAdapter;
    private List<String> list = new ArrayList<String>();
    private View header;
    private String url;

    private RelativeLayout xiazai;

    private RecyclerView recyclerView;
    private FunGameRefreshView beautifulRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wendang);
        appContext = (AppContext) WenDangActivity.this.getApplication();
        setImmerseLayout(findViewById(R.id.activity_wendang));

        wendangid = getIntent().getIntExtra("id",0);

        initView(this);
    }

    @Override
    public void initView(Activity activity) {
        // TODO Auto-generated method stub
        super.initView(activity);
        setZhuTitle("摘要≡ω≡文档");
        setTitleLeftImageBack();
        recyclerView = (RecyclerView) activity.findViewById(R.id.wendang_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(ItemDecorationUtils.getCommTrans5Divider(WenDangActivity.this, true));
        beautifulRefreshLayout = (FunGameRefreshView) activity.findViewById(R.id.wendang_refresh);

        beautifulRefreshLayout.setOnRefreshListener(new FunGameRefreshView.FunGameRefreshListener() {
            @Override
            public void onRefreshing() {
                try {
                    // 模拟网络请求耗时动作
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                initData();
            }
        });

        header = LayoutInflater.from(activity)
                .inflate(R.layout.wendang_activity, recyclerView, false);


        adapter = new WenDangAdapter();
        adapter.setOnItemClickListener(this);
        rootAdapter = new HeaderFooterAdapter<WenDangAdapter>(adapter);


        xiazai = (RelativeLayout) activity.findViewById(R.id.xiazai);
        xiazai.setOnClickListener(this);
        title_text = (TextView) header.findViewById(R.id.title);
        excerpt_text = (TextView) header.findViewById(R.id.excerpt);
        wendang_text = (TextView) header.findViewById(R.id.wendang_text);


        rootAdapter.addHeader(header);
        recyclerView.setAdapter(rootAdapter);

        initData();

    }

    @Override
    public void initData() {
        // TODO Auto-generated method stub
        super.initData();

        RetrofitService.getInstance()
                .getApiCacheRetryService()
                .getPost(RetrofitService.getInstance().getToken(), wendangid,null)
                .enqueue(new SimpleCallBack<PostEntity>() {
                    @Override
                    public void onSuccess(Call<ResEntity<PostEntity>> call, Response<ResEntity<PostEntity>> response) {
                        if (response.body().data.post == null) return;
                        ArticleEntity data = response.body().data.post;
                        title_text.setText(data.title);
                        String s = "坐等KM返回简介";
                        excerpt_text.setText(s.replaceAll("[&hellip;]", ""));
                        wendang_text.setText(stripHtml(data.content));
                        url = data.url;
                        list.clear();

                        list = quChu(getImgStr(data.content));

                        adapter.bindData(true, list);
//						recyclerView.notifyMoreFinish(true);
                        rootAdapter.notifyDataSetChanged();
                        beautifulRefreshLayout.finishRefreshing();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        super.onClick(v);
        switch (v.getId()) {
            case R.id.xiazai:
                if (!TextUtils.isEmpty(url)) {
                    T.showShort("正在跳入异次元请求下载~~~");
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
                break;

            default:
                break;
        }


    }

    public static String delHTMLTag(String htmlStr) {
        String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; //定义script的正则表达式
        String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式
        String regEx_html = "<[^>]+>"; //定义HTML标签的正则表达式

        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll(""); //过滤script标签

        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll(""); //过滤style标签

        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); //过滤html标签

        return htmlStr.trim(); //返回文本字符串
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

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
        MobclickAgent.onPageStart("ACGwenDangActivity");
        MobclickAgent.onResume(this); // 统计时长
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        MobclickAgent.onPageEnd("ACGwenDangActivity");
        MobclickAgent.onPause(this);
    }

    @Override
    public void onItemClick(BaseRecyclerAdapter adapter, BaseRecyclerAdapter.ViewHolder holder, View view, int position) {
        ImageReadingActivity.launch(WenDangActivity.this, (ArrayList<String>) list);
    }

}
