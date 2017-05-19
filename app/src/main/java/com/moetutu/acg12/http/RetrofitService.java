package com.moetutu.acg12.http;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.github.aurae.retrofit2.LoganSquareConverterFactory;
import com.moetutu.acg12.BuildConfig;
import com.moetutu.acg12.app.AppContext;
import com.moetutu.acg12.entity.UserEntity;
import com.moetutu.acg12.presenter.UserDbPresenter;
import com.moetutu.acg12.util.Const;
import com.moetutu.acg12.util.LogUtils;
import com.moetutu.acg12.util.SharedPreferrenceHelper;
import com.moetutu.acg12.util.logger.Logger;

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
    public long id;

    public static UserDbPresenter presenter;


    public static RetrofitService getInstance() {
        if (retrofitService == null) {
            context = AppContext.getApplication();
            presenter = new UserDbPresenter(context);
            retrofitService = new RetrofitService();
        }
        return retrofitService;
    }

    public void restLoginInfo(String token, long id) {
        this.token = token;
        this.id = id;
    }


    public RetrofitService refresh(boolean isRefresh) {
        this.isRefresh = isRefresh;
        return this;
    }


    public RetrofitService() {
        initRetrofit();
        UserEntity user = null;
        try {
            user = presenter.getLoginUser();
        } catch (Exception e) {
            e.printStackTrace();
        }
        restLoginInfo(user == null ? "" : user.getToken(), user == null ? (long)0 : user.getID());
    }

    public String getToken() {
        return token;
    }

    public long getId() {
        return id;
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
            File httpCacheDirectory = new File(context.getExternalCacheDir(), "responses");
            if (!httpCacheDirectory.exists())
                httpCacheDirectory.mkdirs();
            builder.cache(new Cache(httpCacheDirectory, HConst.CACHE_MAXSIZE));
        }
        builder.retryOnConnectionFailure(retry);
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                if (!TextUtils.isEmpty(message)) {
                    LogUtils.d("logger-http", message);
                    if (message.startsWith("{") && message.endsWith("}")) {
                        Logger.t("http-format").json(message);
                    } else if (message.startsWith("[") && message.endsWith("]")) {
                        Logger.t("http-format").json(message);
                    }
                }
            }
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(loggingInterceptor);
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Request requestBuilder = request.newBuilder()
                        .addHeader("Cookie", SharedPreferrenceHelper.getCookie(context))
                        .addHeader("token", getToken())
                        .addHeader("osVer", String.valueOf(Build.VERSION.SDK_INT))
                        .addHeader("osType", HConst.OS_TYPE)
                        .addHeader("appVer", BuildConfig.VERSION_NAME)
                        .build();
                Response response = chain.proceed(requestBuilder);
                String cookeHeader = response.header("Set-Cookie", "");
                if (!TextUtils.isEmpty(cookeHeader)) {
                    if (cookeHeader.length()>=SharedPreferrenceHelper.getCookie(context).length()){
                        SharedPreferrenceHelper.setCookie(context,cookeHeader);
                    }
                }
                return response;
            }
        });
        loggingInterceptor.setLevel(HConst.HTTP_LOG_ENABLE ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        builder.interceptors().add(NETWORKINTERCEPTOR);
        builder.addNetworkInterceptor(NETWORKINTERCEPTOR);
        builder.connectTimeout(HConst.SOCKET_TIME_OUT, TimeUnit.MILLISECONDS);
        builder.readTimeout(HConst.SOCKET_RESPONSE_TIME_OUT, TimeUnit.MILLISECONDS);
        return builder.build();
    }


    public Retrofit getRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(HConst.BASE_HTTP)
                //.addConverterFactory(ProtoConverterFactory.create())//适合数据同步
//                .addConverterFactory(GsonConverterFactory.create(JsonUtils.getGson()))
                .addConverterFactory(LoganSquareConverterFactory.create())
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
            if(!NetWorkUtils.checkNetState(context)){//如果网络不可用
                request=request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
                LogUtils.d("没有网络链接");
            }else{//网络可用
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_NETWORK)
                        .build();
                LogUtils.d("网络可用请求拦截");
            }
            Response response = chain.proceed(request);
            if(NetWorkUtils.checkNetState(context)){//如果网络可用
                LogUtils.d("网络可用请求拦截");
                response= response.newBuilder()
                        //覆盖服务器响应头的Cache-Control,用我们自己的,因为服务器响应回来的可能不支持缓存
                        .header("Cache-Control", "public,max-age=2")
                        .removeHeader("Pragma")
                        .build();
            }else{
//                    Log.d("OkHttp","网络不可用响应拦截");
//                    int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
//                    response= response.newBuilder()
//                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
//                            .removeHeader("Pragma")
//                            .build();
            }
            return response;
        }
    };


}

