package com.moetutu.acg12.http;


import com.moetutu.acg12.entity.CommentDateEntity;
import com.moetutu.acg12.entity.CommentEntity;
import com.moetutu.acg12.entity.CommentsList;
import com.moetutu.acg12.entity.LoginInfo;
import com.moetutu.acg12.entity.PostEntity;

import com.moetutu.acg12.entity.UserEntity;
import com.moetutu.acg12.entity.eventmodel.UserEvent;
import com.moetutu.acg12.http.httpmodel.ResEntity;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * @author xuanyouwu
 * @email xuanyouwu@163.com
 * @time 2016-06-02 14:26
 */

public interface ApiService {

    //登陆接口
    @POST("admin-ajax.php?action=3a83abb58190771625479890b3035831&type=userLogin")
    @FormUrlEncoded
    Call<ResEntity<UserEvent>> login(@Field("token") String token, @Field("userEmail") String userEmail, @Field("userPwd")String userPwd);


    //获取令牌
    @POST("admin-ajax.php?action=3a83abb58190771625479890b3035831&type=getToken")
    @FormUrlEncoded
    Call<ResEntity<UserEntity>> getToken(@Field("appID") String appID, @Field("appSecure")String appSecure);

    // 按分类获取多篇文章
    @POST("admin-ajax.php?action=3a83abb58190771625479890b3035831&type=getPostsByCategory")
    @FormUrlEncoded
    Call<ResEntity<PostEntity>> getPostsByCategory(@Field("token") String token,
                                      @Field("catId")String catId,
                                      @Field("unsets")String unsets,
                                      @Field("number")int number,
                                      @Field("page")int page
                                      );

    // 获取多篇推荐文章
    @POST("admin-ajax.php?action=3a83abb58190771625479890b3035831&type=getRecommPosts")
    @FormUrlEncoded
    Call<ResEntity<PostEntity>> getRecommPosts(@Field("token") String token,
                                                   @Field("unsets")String unsets,
                                                   @Field("number")int number,
                                                   @Field("page")int page
    );


    //按 ID 获取单篇文章
    @POST("admin-ajax.php?action=3a83abb58190771625479890b3035831&type=getPost")
    @FormUrlEncoded
    Call<ResEntity<PostEntity>> getPost (@Field("token") String token,
                                         @Field("postId")int postId,
                                         @Field("unsets")String unsets);


    //获取多条评论
    @POST("admin-ajax.php?action=3a83abb58190771625479890b3035831&type=getComments")
    @FormUrlEncoded
    Call<ResEntity<CommentsList>> getComments (@Field("token") String token,
                                               @Field("postId")int postId,
                                               @Field("number")int number,
                                               @Field("page")String page,
                                               @Field("unsets")String unsets);


    //新建一条评论评论
    @POST("admin-ajax.php?action=3a83abb58190771625479890b3035831&type=newComment ")
    @FormUrlEncoded
    Call<ResEntity<CommentDateEntity>> newComment  (@Field("token") String token,
                                                    @Field("postId")int postId,
                                                    @Field("parentId")int parentId,
                                                    @Field("content")String content);


}
