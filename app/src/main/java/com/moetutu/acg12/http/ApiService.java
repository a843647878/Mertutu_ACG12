package com.moetutu.acg12.http;

import com.moetutu.acg12.asynctask.type.Acg12Obj;
import com.moetutu.acg12.entity.WenDangMode;
import com.moetutu.acg12.entity.TestMode;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * @author xuanyouwu
 * @email xuanyouwu@163.com
 * @time 2016-06-02 14:26
 */

public interface ApiService {

    //登陆接口
    @POST()
    @FormUrlEncoded
    Call<Acg12Obj> login(@Url String Url, @Field("user_email") String user_email, @Field("user_pwd")String user_pwd, @Field("remember") String remember);

    //文档接口
    @GET()
    Call<WenDangMode> getDetail(@Url String Url);


    @GET()
    Call<TestMode> getList(@Url String Url);


}
