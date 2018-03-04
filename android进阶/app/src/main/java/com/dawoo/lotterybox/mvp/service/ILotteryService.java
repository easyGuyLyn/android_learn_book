package com.dawoo.lotterybox.mvp.service;

import com.dawoo.lotterybox.bean.lottery.BaseHandicap;
import com.dawoo.lotterybox.bean.lottery.BaseLottery;
import com.dawoo.lotterybox.bean.lottery.Handicap;
import com.dawoo.lotterybox.bean.lottery.HandicpWithOpening;
import com.dawoo.lotterybox.bean.lottery.LotteryLastOpenAndOpening;
import com.dawoo.lotterybox.bean.lottery.LotteryType;
import com.dawoo.lotterybox.net.HttpResult;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 彩票相关接口
 * Created by benson on 18-2-8.
 */

public interface ILotteryService {


    /**
     * 获取所有彩种类型
     *
     * @return
     */
    @GET("lottery/get-type")
    Observable<HttpResult<List<LotteryType>>> getLotteryType();

    /**
     * 获取所有彩种
     *
     * @return
     */
    @GET("lottery/get-lottery")
    Observable<HttpResult<List<BaseLottery>>> getLottery();
    /**
     * 获取彩种类型及其子彩种的代号和名称
     *
     * @return
     */
    @GET("get-type-and-lottery")
    Observable<HttpResult<List<LotteryType>>> getTypeAndLottery();
    /**
     * 获取盘口数据
     *
     * @return
     */
    @GET("lottery/get-expect")
    Observable<HttpResult<BaseHandicap>> getLotteryExpect(@Query("code") String code);
    /**
     * 获取近期开奖结果
     *
     * @return
     */
    @GET("lottery/get-result-by-code")
    Observable<HttpResult<Handicap>> getResultByCode(
            @Query("search.code") String code,
            @Query("paging.pageSize") String pageSize,
            @Query("paging.pageNumber") String pageNumber);
    /**
     * 获取近期数据（包含未开奖期）
     *
     * @return
     */
    @GET("lottery/get-recent-records")
    Observable<HttpResult<List<HandicpWithOpening>>> getRecentRecords(
            @Query("code") String code,
            @Query("pageSize") String pageSize);
    /**
     * 获取每个彩种最后一期开奖结果和未开奖一期的数据
     *
     * @return
     */
    @GET("lottery/get-last-opened-and-opening-result")
    Observable<HttpResult<List<LotteryLastOpenAndOpening>>> getLastOpenedAndOpeningResult();

    /**
     * 获取彩种是否拥有传统玩法，官方玩法
     * 类型(1.全部2.官方玩法3.传统玩法)
     *
     * @return
     */
    @GET("lottery/get-lottery-genre")
    Observable<HttpResult<Integer>> getLotterygenre(
            @Query("code") String code);

    /**
     * 获取防重复下注token
     *
     * @return
     */
    @GET("get-lt-token")
    Observable<HttpResult<String>> getltToken();
}
