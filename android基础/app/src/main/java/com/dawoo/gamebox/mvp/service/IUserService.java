package com.dawoo.gamebox.mvp.service;

import com.dawoo.gamebox.bean.Logout;
import com.dawoo.gamebox.bean.ResetSecurityPwd;
import com.dawoo.gamebox.bean.UpdateLoginPwd;
import com.dawoo.gamebox.bean.UserAssert;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 用户相关的接口
 * Created by benson on 17-12-21.
 */

public interface IUserService {

    /**
     * 修改登录密码
     *
     * @return
     */
    @FormUrlEncoded
    @POST("mobile-api/mineOrigin/updateLoginPassword.html")
    Observable<UpdateLoginPwd> updateLoginPwd(@Field("password") String password, @Field("newPassword") String newPassword);

    /**
     * 带验证码的修改登录密码
     *
     * @return
     */
    @FormUrlEncoded
    @POST("mobile-api/mineOrigin/updateLoginPassword.html")
    Observable<UpdateLoginPwd> updateLoginPwd(@Field("password") String password,
                                              @Field("newPassword") String newPassword,
                                              @Field("code") String code);


    /**
     * init安全密码
     *
     * @return
     */
    @POST("mobile-api/mineOrigin/initSafePassword.html")
    Observable<ResetSecurityPwd> initSafePassword();

    /**
     * 设置真名
     *
     * @return
     */
    @FormUrlEncoded
    @POST("mobile-api/mineOrigin/setRealName.html")
    Observable<ResetSecurityPwd> setRealName(@Field("realName") String realName);


    /**
     * 修改安全密码
     *
     * @param name
     * @param password
     * @param newPassword
     * @return
     */
    @FormUrlEncoded
    @POST("mobile-api/mineOrigin/updateSafePassword.html")
    Observable<ResetSecurityPwd> updateSafePassword(@Field("needCaptcha") Boolean needCaptcha,
                                                    @Field("realName") String name,
                                                    @Field("originPwd") String password,
                                                    @Field("pwd1") String newPassword,
                                                    @Field("pwd2") String confirmNewPassword,
                                                    @Field("code") String code);

    /**
     * 登出
     *
     * @return
     */
    @POST("mobile-api/mineOrigin/logout.html")
    Observable<Logout> logOut();


}
