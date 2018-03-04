package com.dawoo.gamebox.mvp.service;

import com.dawoo.gamebox.bean.GameNotice;
import com.dawoo.gamebox.bean.VerifyRealNameBean;
import com.dawoo.gamebox.bean.VerifyRealNameResponse;
import com.dawoo.gamebox.net.HttpResult;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by b on 18-1-25.
 */

public interface ILoginService {

    /**
     * 验证真实姓名
     * */

//    @FormUrlEncoded
//    @POST("passport/verify/verifyRealNameForApp.html")
//    Observable<Object> verifyRealName(
//            @Field("data") String jsonString);



    @FormUrlEncoded
    @POST("passport/verify/verifyRealNameForApp.html")
    Observable<VerifyRealNameResponse> verifyRealName(
            @Field("gb.token") String token,
            @Field("result.realName") String realName,
            @Field("needRealName") String yes,
            @Field("result.playerAccount") String playerAccount,
            @Field("search.playerAccount") String playeAccount,
            @Field("tempPass") String tempPass ,
            @Field("newPassword ") String newPassword ,
            @Field("passLevel") String passLevel);

}
