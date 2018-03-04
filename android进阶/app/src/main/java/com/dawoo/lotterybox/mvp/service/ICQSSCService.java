package com.dawoo.lotterybox.mvp.service;

import com.dawoo.lotterybox.bean.AwardResultBean;
import com.dawoo.lotterybox.bean.BannerBean;
import com.dawoo.lotterybox.bean.CQSSCAwardResultBean;
import com.dawoo.lotterybox.bean.ExpectDataBean;
import com.dawoo.lotterybox.net.HttpResult;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by b on 18-2-22.
 * 重庆时时彩A盘
 */

public interface ICQSSCService {



    /**
     * 获取近期开奖
     *
     * @return
     */
    @FormUrlEncoded
    @POST("lottery/get-result-by-code")
    Observable<HttpResult<List<CQSSCAwardResultBean>>> getAwardResults(
            @Field("search.code") String code,
            @Field("paging.pageSize") String pageSize,
            @Field("paging.pageNumber") String pageNumber
    );

    /**
     * 获取近期数据（包含未开奖期）
     *
     * @return
     */
    @FormUrlEncoded
    @POST("lottery/get-recent-records")
    Observable<HttpResult<List<AwardResultBean>>> getAwardResultsAndNoOpen(
            @Field("code") String code,
            @Field("pageSize") String pageSize
    );


    /**
     * 盘口数据
     *
     * @return
     */
    @FormUrlEncoded
    @POST("lottery/get-expect")
    Observable<HttpResult<ExpectDataBean>> getExpectData(
            @Field("code") String code
    );

    /**
     * 获取赔率
     *
     * @return
     */
    @FormUrlEncoded
    @POST("lottery/get-lottery-odd")
    Observable<HttpResult<Object>> getLotteryOdd(
            @Field("code") String code,
            @Field("betCode") String betCode
    );

}
