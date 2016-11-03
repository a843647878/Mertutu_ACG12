package com.moetutu.acg12.http;

import com.dao.userinfo.User;
import com.moetutu.acg12.asynctask.type.Acg12Obj;
import com.moetutu.acg12.entity.ArticleEntity;
import com.moetutu.acg12.entity.PostEntity;
import com.moetutu.acg12.entity.UserEntity;
import com.moetutu.acg12.entity.WenDangMode;
import com.moetutu.acg12.entity.TestMode;
import com.moetutu.acg12.http.httpmodel.ResEntity;

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


    //获取令牌
    @POST("admin-ajax.php?action=53c421&type=getToken")
    @FormUrlEncoded
    Call<ResEntity<User>> getToken(@Field("appID") String appID, @Field("appSecure")String appSecure);

    // 按分类获取多篇文章
    @POST("admin-ajax.php?action=53c421&type=getPostsByCategory")
    @FormUrlEncoded
    Call<ResEntity<PostEntity>> getPostsByCategory(@Field("token") String token,
                                      @Field("catID")String catID,
                                      @Field("unsets")String unsets,
                                      @Field("number")int number,
                                      @Field("page")int page
                                      );


    // 获取多篇推荐文章
    @POST("admin-ajax.php?action=53c421&type=getRecommPosts")
    @FormUrlEncoded
    Call<ResEntity<PostEntity>> getRecommPosts(@Field("token") String token,
                                                   @Field("unsets")String unsets,
                                                   @Field("number")int number,
                                                   @Field("page")int page
    );


    //按 ID 获取单篇文章
    @POST("admin-ajax.php?action=53c421&type=getPost")
    @FormUrlEncoded
    Call<ResEntity<PostEntity>> getPost (@Field("token") String token, @Field("postID")int postID, @Field("unsets")String unsets);


}
