package com.moetutu.acg12.http;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.dao.userinfo.User;
import com.moetutu.acg12.BuildConfig;
import com.moetutu.acg12.app.ACache;
import com.moetutu.acg12.app.AppContext;
import com.moetutu.acg12.presenter.UserDbPresenter;
import com.moetutu.acg12.util.JsonUtils;
import com.moetutu.acg12.util.LogUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * ClassName RetrofitService
 * Description
 * Company
 * author  youxuan  E-mail:xuanyouwu@163.com
 * date createTime：2015/12/24 10:42
 * version
 */
public class RetrofitService {


    private static RetrofitService retrofitService;
    private ApiService apiService;//不缓存不重连  适合做交互请求
    private ApiService apiCacheRetryService;//http协议缓存 重连 适合做数据获取
    public static Context context;
    private boolean isRefresh = false;
    public String token;
    public String uid;

    public static UserDbPresenter presenter;


    public static RetrofitService getInstance() {
        if (retrofitService == null) {
            context = AppContext.getApplication();
            presenter = new UserDbPresenter(context);
            retrofitService = new RetrofitService();
        }
        return retrofitService;
    }

    public void restLoginInfo(String token, String uid) {
        this.token = token;
        this.uid = uid;
    }


    public RetrofitService refresh(boolean isRefresh) {
        this.isRefresh = isRefresh;
        return this;
    }


    public RetrofitService() {
        initRetrofit();
        User user = null;
        try {
            user = presenter.getLoginUser();
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogUtils.d("------------------>运行RetrofitService"+user == null ? "null" : "不空");
        restLoginInfo(user == null ? "" : user.getToken(), user == null ? "" : user.getUid());
    }

    public String getToken() {
        return token;
    }

    public String getUid() {
        return uid;
    }

    public ApiService getApiService() {
        return apiService;
    }

    public ApiService getApiCacheRetryService() {
        return apiCacheRetryService;
    }

    private void initRetrofit() {
        apiService = getRetrofit(getOkHttpClient(false, false)).create(ApiService.class);
        apiCacheRetryService = getRetrofit(getOkHttpClient(true, true)).create(ApiService.class);
    }

    public OkHttpClient getOkHttpClient(boolean cache, boolean retry) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (cache) {
            File httpCacheDirectory = new File(context.getCacheDir(), "responses");
            if (!httpCacheDirectory.exists())
                httpCacheDirectory.mkdirs();
            builder.cache(new Cache(httpCacheDirectory, HConst.CACHE_MAXSIZE));
        }
        builder.retryOnConnectionFailure(retry);
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                LogUtils.d("http", message);
            }
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(loggingInterceptor);
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Request requestBuilder = request.newBuilder()
                        .addHeader("token", getToken())
                        .addHeader("osVer", String.valueOf(Build.VERSION.SDK_INT))
                        .addHeader("osType", HConst.OS_TYPE)
                        .addHeader("appVer", BuildConfig.VERSION_NAME)
                        .build();
                return chain.proceed(requestBuilder);
            }
        });
        builder.addNetworkInterceptor(NETWORKINTERCEPTOR);
        builder.connectTimeout(HConst.SOCKET_TIME_OUT, TimeUnit.MILLISECONDS);
        builder.readTimeout(HConst.SOCKET_RESPONSE_TIME_OUT, TimeUnit.MILLISECONDS);
        return builder.build();
    }

    public Retrofit getRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(HConst.BASE_HTTP)
                //.addConverterFactory(ProtoConverterFactory.create())//适合数据同步
                .addConverterFactory(GsonConverterFactory.create(JsonUtils.getGson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    /**
     * 网络缓存拦截器
     * 网络连接时请求服务器，否则从本地缓存中获取
     */
    private final Interceptor NETWORKINTERCEPTOR = new Interceptor() {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!NetWorkUtils.checkNetState(context)) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
                LogUtils.d("没有网络链接");
            }
            if (isRefresh) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_NETWORK)
                        .build();
                refresh(!isRefresh);
                LogUtils.d("刷新操作");
            }
            Response response = chain.proceed(request);
            if (NetWorkUtils.checkNetState(context)) {
                int maxAge = 0; // 有网络时 设置缓存超时时间0个小时
                LogUtils.d("网络已连接，缓存时间为：" + maxAge);
                response = response.newBuilder()
                        .addHeader("Cache-Control", "public, max-age=" + maxAge)
                        .removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                        .build();
            } else {
                int maxStale = HConst.TIME_CACHE;
                LogUtils.d("网络未连接，缓存时间为：" + maxStale);
                response = response.newBuilder()
                        .addHeader("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .removeHeader("Pragma")
                        .build();
            }
            return response;
        }
    };


}

