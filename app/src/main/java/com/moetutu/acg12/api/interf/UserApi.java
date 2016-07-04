package com.moetutu.acg12.api.interf;


import com.moetutu.acg12.asynctask.type.Acg12Obj;
import com.moetutu.acg12.entity.TestMode;
import com.moetutu.acg12.asynctask.type.WenDangMode;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface UserApi {
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
