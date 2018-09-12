package com.jasonzou.retrofitdemo;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * host:    https://api.lcoce.com
 */
public interface API {

    String HOST = "https://api.lcoce.com/";

    /**
     * 登录接口
     *
     * @param account
     * @param password
     * @param loginType
     * @return
     */
    @POST("lawyer/login/index")
    @FormUrlEncoded
    Call<UserInfo> login(@Field("account") String account, @Field("password") String password, @Field("loginType") String loginType);
}
