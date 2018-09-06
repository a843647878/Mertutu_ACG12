package com.moetutu.acg12.activity;

import android.Manifest;
import android.animation.LayoutTransition;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.rubensousa.floatingtoolbar.FloatingToolbar;
import com.moetutu.acg12.R;
import com.moetutu.acg12.adapter.ArticleAdapter;
import com.moetutu.acg12.dialog.ShareDialog;
import com.moetutu.acg12.entity.ArticleEntity;
import com.moetutu.acg12.entity.CommentDateEntity;
import com.moetutu.acg12.entity.CommentEntity;
import com.moetutu.acg12.entity.CommentsList;
import com.moetutu.acg12.entity.PostEntity;
import com.moetutu.acg12.entity.UserEntity;
import com.moetutu.acg12.http.RetrofitService;
import com.moetutu.acg12.http.callback.SimpleCallBack;
import com.moetutu.acg12.http.httpmodel.ResEntity;
import com.moetutu.acg12.presenter.UserDbPresenter;
import com.moetutu.acg12.util.ItemDecorationUtils;
import com.moetutu.acg12.util.LogUtils;
import com.moetutu.acg12.util.SharedPreferrenceHelper;
import com.moetutu.acg12.util.SystemUtils;
import com.moetutu.acg12.util.T;
import com.moetutu.acg12.util.ToastUtils;
import com.moetutu.acg12.view.widget.BaseRecyclerAdapter;
import com.moetutu.acg12.view.widget.HeaderFooterAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.sj.emoji.EmojiBean;
import com.zzhoujay.richtext.CacheType;
import com.zzhoujay.richtext.RichText;
import com.zzhoujay.richtext.callback.OnImageClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;
import sj.keyboard.SimpleUserdefEmoticonsKeyBoard;
import sj.keyboard.common.Constants;
import sj.keyboard.common.SimpleCommonUtils;
import sj.keyboard.common.widget.SimpleAppsGridView;
import sj.keyboard.data.EmoticonEntity;
import sj.keyboard.interfaces.EmoticonClickListener;
import sj.keyboard.utils.EmoticonsKeyboardUtils;
import sj.keyboard.widget.AudioRecorderButton;
import sj.keyboard.widget.EmoticonsEditText;
import sj.keyboard.widget.FuncLayout;

/**
 * Description
 * Company guokeyuzhou
 * Created by chengwanying on 16/7/5.
 * version.
 * 文章详情页
 */
public class ArticleBActivity extends BaseActivity implements FuncLayout.OnFuncKeyBoardListener,
        BaseRecyclerAdapter.OnItemClickListener, sj.keyboard.widget.BaseRecyclerAdapter.OnItemClickListener {


    public static String KEY_ARTICLEID = "key_articleid";

    protected static final int REQUEST_RECORD_AUDIO = 105;
    protected static final int READ_EXTERNAL_STORAGE = 106;

    SimpleAppsGridView simpleAppsGridView;

    TextView tvHtml;
    @BindView(R.id.download_button)
    FloatingActionButton downloadButton;


    View headerView;
    HeaderFooterAdapter<ArticleAdapter> headerFooterAdapter;
    ArticleAdapter articleAdapter;

    @BindView(R.id.rv_main_list)
    RecyclerView rvMainList;
    @BindView(R.id.myRefreshLayout)
    SmartRefreshLayout myRefreshLayout;
    @BindView(R.id.ek_bar)
    SimpleUserdefEmoticonsKeyBoard ekBar;
    @BindView(R.id.article_relative)
    RelativeLayout articleRelative;
    @BindView(R.id.floatingToolbar)
    FloatingToolbar floatingToolbar;
    @BindView(R.id.stock_remind)
    TextView stockRemind;

    private int wendangid;
    public ArticleEntity data;

    private UserDbPresenter presenter;
    private UserEntity user;

    private int PageIndex = 1;
    private List<CommentEntity> commentList = new ArrayList<CommentEntity>();


    LinearLayoutManager linearLayoutManager;

    public static void launch(Context context, int wendangid) {
        if (context == null) {
            return;
        }
        if (wendangid == 0) {
            return;
        }
        Intent in = new Intent(context, ArticleBActivity.class);
        in.putExtra(KEY_ARTICLEID, wendangid);
        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(in);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_b);
        ButterKnife.bind(this);
        wendangid = getIntent().getIntExtra(KEY_ARTICLEID, 0);
        initView(this);
        initEmoticonsKeyBoardBar();
    }


    @Override
    public void initView(Activity activity) {
        super.initView(activity);
        setZhuTitle();
        stockRemind.setText("分享");
        stockRemind.setVisibility(View.VISIBLE);
        setTitleLeftImageBack();
        presenter = new UserDbPresenter(this);
        try {
            user = presenter.getLoginUser();
        } catch (Exception e) {
            e.printStackTrace();
        }
        myRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getData(true);
                refreshlayout.finishRefresh(2000);
            }
        });
        myRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                getData(false);
                refreshlayout.finishLoadmore(2000);
            }
        });

        linearLayoutManager = new LinearLayoutManager(this);
        rvMainList.setLayoutManager(linearLayoutManager);
        rvMainList.addItemDecoration(ItemDecorationUtils.getCommFull05Divider(this, true));
        articleAdapter = new ArticleAdapter();
        headerFooterAdapter = new HeaderFooterAdapter<>(articleAdapter);
        headerView = LayoutInflater.from(context).inflate(R.layout.header_articlehtml, rvMainList, false);

        headerFooterAdapter.addHeader(headerView);
        rvMainList.setAdapter(headerFooterAdapter);
        floatingToolbar.attachFab(downloadButton);
        floatingToolbar.attachRecyclerView(rvMainList);

        articleRelative.setLayoutTransition(getLayoutTransition());//把动画加到按钮上
        rvMainList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //滑动关闭键盘
                if (ekBar != null) {// && ekBar.isSoftKeyboardPop()
                    SystemUtils.hideSoftKeyBoard(context);
                    ekBar.reset();
                }
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    showBottomBar();
                } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    if (downloadButton.getVisibility() == View.VISIBLE) {
                        downloadButton.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        tvHtml = (TextView) headerView.findViewById(R.id.tv_html);

        articleAdapter.setOnItemClickListener(this);


        floatingToolbar.setClickListener(new FloatingToolbar.ItemClickListener() {
            @Override
            public void onItemClick(MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (data.storage == null) {
                    ToastUtils.showRoundRectToast("此文章没有可以下载的东西喵!");
                } else {
                    switch (id) {
                        case R.id.menu_pass:
                            if (TextUtils.isEmpty(data.storage.items.get(0).extractPwd)) {
                                ToastUtils.showRoundRectToast("此文件没有解压密码!");
                            } else {
                                new AlertDialog.Builder(context)
                                        .setTitle("解压密码：")
                                        .setMessage(data.storage.items.get(0).extractPwd)
                                        .setNegativeButton("确认", null).show();
                                SystemUtils.copyToClipboard(context, "text", data.storage.items.get(0).extractPwd);
                                ToastUtils.showRoundRectToast("解压密码已经复制到剪切板啦!");
                            }

                            break;
                        case R.id.menu_download:
                            //跳转下载
                            intentDownload();
                            break;
                        default:
                            break;
                    }
                }
            }

            @Override
            public void onItemLongClick(MenuItem menuItem) {

            }
        });
        initData();
        myRefreshLayout.autoRefresh();
    }


    public void showBottomBar() {
        downloadButton.setVisibility(View.VISIBLE);
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
                        if (response.body() == null) {
                            return;
                        }
                        data = response.body().data.post;
                        RichText
                                .fromHtml(data.content) // 数据源
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
                                        ImagePagerActivity.launch(context, data.title, imageUrls, position + 1);
                                    }
                                }) // 设置图片点击回调
//                                .imageLongClick(onImageLongClickListener) // 设置图片长按回调
//                                .urlClick(onURLClickListener) // 设置链接点击回调
//                                .urlLongClick(onUrlLongClickListener) // 设置链接长按回调
                                .placeHolder(R.mipmap.cat_loging) // 设置加载中显示的占位图
                                .error(R.mipmap.cat_loging) // 设置加载失败的错误图
                                .cache(CacheType.ALL) // 缓存类型，默认为Cache.LAYOUT（不缓存图片，只缓存图片大小信息和文本样式信息）
//                                .imageGetter() // 设置图片加载器，默认为DefaultImageGetter，使用okhttp实现
                                .bind(rvMainList) // 绑定richText对象到某个object上，方便后面的清理
                                .into(tvHtml); // 设置目标TextView
                    }
                });
    }


    @Override
    public synchronized void getData(final boolean isRefresh) {
        super.getData(isRefresh);
        if (isRefresh) {
            PageIndex = 1;
        }
//        endLastRefresh(isRefresh);
        RetrofitService
                .getInstance()
                .getApiCacheRetryService()
                .getComments(RetrofitService.getInstance().getToken(), wendangid, 10, PageIndex + "", null)
                .enqueue(new SimpleCallBack<CommentsList>() {
                    @Override
                    public void onSuccess(Call<ResEntity<CommentsList>> call, Response<ResEntity<CommentsList>> response) {
//                        endCurrentRefresh(isRefresh);
                        if (response.body().data.comments != null) {
                            articleAdapter.bindData(isRefresh, response.body().data.comments);
                            PageIndex++;
                            myRefreshLayout.setLoadmoreFinished(!(response.body().data.comments.size() > 9));
//                            myRefreshLayout.setLoadMore(response.body().data.comments.size() > 9);
                            headerFooterAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResEntity<CommentsList>> call, Throwable t) {
                        super.onFailure(call, t);
//                        endCurrentRefresh(isRefresh);
                    }
                });
    }


    //跳转到下载页面
    public void intentDownload() {
        if (data.storage.items.size() > 0) {
            if (TextUtils.isEmpty(data.storage.items.get(0).downloadPwd)){
                ToastUtils.showRoundRectToast("没发现下载地址");
                return;
            }
            SystemUtils.copyToClipboard(context, "text", data.storage.items.get(0).downloadPwd);
            ToastUtils.showRoundRectToast("提取密码已经复制到剪切板啦!");
            Uri uri = Uri.parse(data.storage.items.get(0).url.replace(" ", ""));
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } else {
            ToastUtils.showRoundRectToast("没发现下载地址");
        }
    }

    //初始化底部键盘
    private void initEmoticonsKeyBoardBar() {
        SimpleCommonUtils.initEmoticonsEditText(ekBar.getEtChat());
        ekBar.setAdapter(SimpleCommonUtils.getCommonAdapter(this, emoticonClickListener));
        ekBar.addOnFuncKeyBoardListener(this);
        simpleAppsGridView = new SimpleAppsGridView(this);
        simpleAppsGridView.setOnItemClickListener(this);
        ekBar.addFuncView(simpleAppsGridView);

        ekBar.getEtChat().setOnSizeChangedListener(new EmoticonsEditText.OnSizeChangedListener() {
            @Override
            public void onSizeChanged(int w, int h, int oldw, int oldh) {
                scrollToBottom();
            }
        });
        ekBar.getBtnSend().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnSendBtnClick(ekBar.getEtChat().getText().toString());
                ekBar.getEtChat().setText("");
            }
        });

        ekBar.getBtnVoice().setAudioFinishRecorderListener(new AudioRecorderButton.AudioFinishRecorderListener() {
            @Override
            public void onPermission() {
                audioPermision();
            }

            @Override
            public void onFinish(float seconds, String filePath) {
                T.showShort("这个想想  以后再做");
            }
        });
//        ekBar.getEmoticonsToolBarView().addFixedToolItemView(false, R.mipmap.icon_add, null, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                T.showShort("add");
//            }
//        });
//        ekBar.getEmoticonsToolBarView().addToolItemView(R.mipmap.icon_setting, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                T.showShort("SETTING");
//            }
//        });
    }

    EmoticonClickListener emoticonClickListener = new EmoticonClickListener() {
        @Override
        public void onEmoticonClick(Object o, int actionType, boolean isDelBtn) {

            if (isDelBtn) {
                SimpleCommonUtils.delClick(ekBar.getEtChat());
            } else {
                if (o == null) {
                    return;
                }
                if (actionType == Constants.EMOTICON_CLICK_BIGIMAGE) {
                    if (o instanceof EmoticonEntity) {
                        OnSendImage(((EmoticonEntity) o).getIconUri());
                    }
                } else {
                    String content = null;
                    if (o instanceof EmojiBean) {
                        content = ((EmojiBean) o).emoji;
                    } else if (o instanceof EmoticonEntity) {
                        content = ((EmoticonEntity) o).getContent();
                    }

                    if (TextUtils.isEmpty(content)) {
                        return;
                    }
                    int index = ekBar.getEtChat().getSelectionStart();
                    Editable editable = ekBar.getEtChat().getText();
                    editable.insert(index, content);
                }
            }
        }
    };

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (EmoticonsKeyboardUtils.isFullScreen(this)) {
            boolean isConsum = ekBar.dispatchKeyEventInFullScreen(event);
            return isConsum ? isConsum : super.dispatchKeyEvent(event);
        }
        return super.dispatchKeyEvent(event);
    }

    /*
    * 发送评论
    * */
    private void OnSendBtnClick(String msg) {
        if (user == null) {
            ToastUtils.showRoundRectToast("未登录用户暂不支持评论哦");
        } else {
            if (TextUtils.isEmpty(msg)) return;
            RetrofitService.getInstance()
                    .getApiCacheRetryService()
                    .newComment(RetrofitService.getInstance().getToken(), wendangid, 0, msg)
                    .enqueue(new SimpleCallBack<CommentDateEntity>() {
                        @Override
                        public void onSuccess(Call<ResEntity<CommentDateEntity>> call, Response<ResEntity<CommentDateEntity>> response) {
                            if (response.body().data.comment != null) {
                                articleAdapter.addItem(0, response.body().data.comment);
                                scrollToBottomSend();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResEntity<CommentDateEntity>> call, Throwable t) {
                            super.onFailure(call, t);
                            LogUtils.d("------------->Cookie:" + SharedPreferrenceHelper.getCookie(context));
                        }
                    });
        }
    }


    private LayoutTransition getLayoutTransition() {
        LayoutTransition transition = new LayoutTransition();
        transition.getDuration(2000);
        transition.setStartDelay(LayoutTransition.CHANGE_APPEARING, 1000);
        transition.setStartDelay(LayoutTransition.CHANGE_DISAPPEARING, 1000);
        transition.setAnimator(LayoutTransition.APPEARING, transition.getAnimator(LayoutTransition.APPEARING));
        transition.setAnimator(LayoutTransition.CHANGE_APPEARING, transition.getAnimator(LayoutTransition.CHANGE_APPEARING));//CHANGE_APPEARING消失动画
        transition.setAnimator(LayoutTransition.DISAPPEARING, transition.getAnimator(LayoutTransition.DISAPPEARING));//DISAPPEARING移除view的动画
        transition.setAnimator(LayoutTransition.CHANGE_DISAPPEARING, transition.getAnimator(LayoutTransition.CHANGE_DISAPPEARING));
        return transition;
    }

    /*
    * 发个图片
    * */
    private void OnSendImage(String image) {
        if (!TextUtils.isEmpty(image)) {
            OnSendBtnClick("[img]" + image);
        }
    }


    private void scrollToBottom() {
        if (rvMainList == null) {return;}
        int lastChildViewPosition = linearLayoutManager.findLastVisibleItemPosition();
        LogUtils.d("------------->lastChildViewPosition:" + lastChildViewPosition + "--------->" + articleAdapter.getItemCount());
        if (articleAdapter.getItemCount() - lastChildViewPosition < 3) {
            rvMainList.scrollToPosition(articleAdapter.getItemCount() - 1);
        }
//        chatRecyclerview.scrollToPosition(adapter.getItemCount() - 1);
    }

    private void scrollToBottomSend() {
        if (rvMainList == null) {return;}
        rvMainList.scrollToPosition(articleAdapter.getItemCount() - 1);
    }


    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    public void onItemClick(BaseRecyclerAdapter adapter, BaseRecyclerAdapter.ViewHolder holder, View view, int position) {
        T.showShort("回复评论这个没时间做");
    }

    @Override
    public void OnFuncPop(int height) {
        scrollToBottom();
    }

    @Override
    public void OnFuncClose() {

    }


    @Override
    public void onItemClick(sj.keyboard.widget.BaseRecyclerAdapter adapter, sj.keyboard.widget.BaseRecyclerAdapter.ViewHolder holder, View view, int position) {
        T.showShort("这个想想  以后再做");
    }


    //获取权限录音权限
    private void audioPermision() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN // Permission was added in API Level 16
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.RECORD_AUDIO, "我们需要录音权限",
                    REQUEST_RECORD_AUDIO);
        }
    }

    @OnClick(R.id.stock_remind)
    public void onViewClicked() {
        String[] permisions = {Manifest.permission.READ_EXTERNAL_STORAGE};
        if (checkPermissions(permisions))//有权限
        {
            new ShareDialog(context, data.title, R.mipmap.ic_launcher, data.url).show();
        } else {//没权限
            reqPermission(permisions, READ_EXTERNAL_STORAGE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    new ShareDialog(context, data.title, R.mipmap.ic_launcher, data.url).show();
                } else {
                    showFillToast("权限被拒绝,请到设置中心打开读取权限!");
                }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
