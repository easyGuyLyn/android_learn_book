package com.dawoo.lotterybox.mvp.service;

import com.dawoo.lotterybox.bean.BannerBean;
import com.dawoo.lotterybox.bean.Bulletin;
import com.dawoo.lotterybox.bean.lottery.TypeAndLotteryBean;
import com.dawoo.lotterybox.net.HttpResult;

import java.util.List;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by b on 18-2-8.
 * 彩票大厅相关接口
 */

public interface IHallService {


    /**
     * 获取轮播图
     *
     * @return
     */
    @GET("hall/banner")
    Observable<HttpResult<List<BannerBean>>> getBanner();


    /**
     * 获取公告
     *
     * @return
     */
    @GET("bulletin")
    Observable<HttpResult<List<Bulletin>>> getBulletin();

    /**
     * 获取彩种类型及其子彩种的代号和名称
     *
     * @return
     */
    @GET("lottery/get-type-and-lottery")
    Observable<HttpResult<List<TypeAndLotteryBean>>> getTypeAndLottery();

}
