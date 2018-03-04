package com.dawoo.lotterybox.mvp.service;

import com.dawoo.lotterybox.bean.lottery.LotterySimpleBean;
import com.dawoo.lotterybox.bean.record.AssetsBean;
import com.dawoo.lotterybox.bean.record.NoTeRecordHisData;
import com.dawoo.lotterybox.bean.record.NoteRecordHis;
import com.dawoo.lotterybox.bean.record.ProfitBean;
import com.dawoo.lotterybox.net.HttpResult;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 报表和记录
 * Created by benson on 18-2-8.
 */

public interface IRecordService {

    /**
     * 玩家投注历史报表
     *
     * @return
     */
    @FormUrlEncoded
    @POST("bet/get-orders")
    Observable<NoTeRecordHisData> getOrders(
            @Field("search.code") String code,
            @Field("search.expect") String expect,
            @Field("search.status") String status,
            @Field("search.queryStartDate") String queryStartDate,
            @Field("search.queryEndDate") String queryEndDate,
            @Field("paging.pageSize") String pageSize,
            @Field("paging.pageNumber") String pageNumber,
            @Field("search.playModel") String playModel);

    /**
     * 获取所有彩种
     *
     * @return
     */
    @GET("lottery/get-lottery")
    Observable<HttpResult<List<LotterySimpleBean>>> getLottery();


    /**
     * 获取下注总金额，派彩总金额（注单合计）
     *
     * @return
     */
    @FormUrlEncoded
    @POST("bet/get-assets")
    Observable<HttpResult<AssetsBean>> getAssets(
            @Field("search.queryStartDate") String queryStartDate,
            @Field("search.queryEndDate") String queryEndDate,
            @Field("search.status") String status,
            @Field("code") String code);


    /**
     * 根据盈亏状态获取近30天盈亏金额
     *
     * @return
     */
    @FormUrlEncoded
    @POST("bet/get-recent-profit")
    Observable<HttpResult<List<ProfitBean>>> getRecentProfit(
            @Field("status") String status,
            @Field("code") String code);


    /**
     * 获取注单详细
     *
     * @return
     */
    @FormUrlEncoded
    @POST("bet/get-order-detail")
    Observable<HttpResult<NoteRecordHis>> getOrderDetail(@Field("search.id") String id);


}
