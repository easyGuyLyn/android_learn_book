package com.dawoo.gamebox.mvp.service;


import com.dawoo.gamebox.bean.ActivityType;
import com.dawoo.gamebox.bean.ActivityTypeList;
import com.dawoo.gamebox.bean.MyPromo;
import com.dawoo.gamebox.net.HttpResult;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 优惠相关的接口
 * Created by benson on 17-12-21.
 */

public interface IPromoService {

    /**
     * 获取优惠分类和列表
     *
     * @return
     */
    @POST("mobile-api/discountsOrigin/getActivityType.html")
    Observable<HttpResult<List<ActivityType>>> getActivityType();

//    /**
//     * 获取优惠活动列表
//     *
//     * @param pageNumber
//     * @param pageSize
//     * @param activityClassifyKey
//     * @return
//     */
//    @FormUrlEncoded
//    @POST("mobile-api/discountsOrigin/getActivityTypeList.html")
//    Observable<HttpResult<ActivityTypeList>> getActivityTypeList(
//            @Field("paging.pageNumber") int pageNumber,
//            @Field("paging.pageSize") int pageSize,
//            @Field("search.activityClassifyKey") String activityClassifyKey);

    /**
     * 获取优惠活动列表
     *
     * @param activityClassifyKey
     * @return
     */
    @FormUrlEncoded
    @POST("mobile-api/discountsOrigin/getActivityTypeList.html")
    Observable<HttpResult<ActivityTypeList>> getActivityTypeList(
            @Field("search.activityClassifyKey") String activityClassifyKey);

    /**
     * @param pageNumber
     * @param pageSize
     * @return port/mobile-api/mineOrigin/getMyPromo.html
     */
    @FormUrlEncoded
    @POST("mobile-api/mineOrigin/getMyPromo.html")
    Observable<HttpResult<MyPromo>> getMyPromo(@Field("paging.pageNumber") int pageNumber, @Field("paging.pageSize") int pageSize);
}
