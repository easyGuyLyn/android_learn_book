package com.dawoo.lotterybox.mvp.service;

import com.dawoo.lotterybox.bean.LoginBean;
import com.dawoo.lotterybox.bean.User;
import com.dawoo.lotterybox.net.HttpResult;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 用户相关的接口
 * Created by benson on 17-12-21.
 */

public interface IUserService {

    /**
     * 登录
     *
     * @return
     */
    @FormUrlEncoded
    @POST("passport/login.html")
    Observable<HttpResult<LoginBean>> login(
            @Field("username") String username,
            @Field("password") String password,
            @Field("appKey") String appKey,
            @Field("appSecret") String appSecret,
            @Field("serialNo") String serialNo);

    /**
     * 注册
     *
     * @return
     */
    @FormUrlEncoded
    @POST("register")
    Observable<HttpResult<Boolean>> register(
            @Field("username") String username,
            @Field("password") String password,
            @Field("confirmPassword") String confirmPassword,
            @Field("createChannel") String createChannel,
            @Field("playerType") String playerType,
            @Field("mode") String mode);

    /**
     * 获取玩家信息
     *
     * @return
     */
    @GET("account/get-user-info")
    Observable<HttpResult<User>> getUserInfo();

}
