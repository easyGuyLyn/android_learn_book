package com.dawoo.gamebox.mvp.service;


import com.dawoo.gamebox.bean.MineLink;
import com.dawoo.gamebox.bean.UserPlayerRecommend;
import com.dawoo.gamebox.net.HttpResult;

import retrofit2.http.POST;
import rx.Observable;

/**
 * 获取我的页面相关接口
 * Created by benson on 18-01-03.
 */

public interface IMineService {

    /**
     * 获取我的
     *
     * @return
     */
    @POST("mobile-api/mineOrigin/getLink.html")
    Observable<HttpResult<MineLink>> getLink();


    @POST("mobile-api/mineOrigin/getUserPlayerRecommend.html")
    Observable<HttpResult<UserPlayerRecommend>> getUserPlayerRecommend();



}
