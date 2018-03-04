package com.dawoo.lotterybox.mvp.model.record;


import rx.Subscriber;
import rx.Subscription;

/**
 * Created by benson on 17-12-21.
 */

public interface IRecordModel {
    /**
     * 获取投注记录
     */

    Subscription getOrders(Subscriber subscriber, String code, String expect,
                           String status, String queryStartDate, String queryEndDate,
                           String pageSize, String pageNumber, String playModel);

    /**
     * 获取所有彩种
     */

    Subscription getLottery(Subscriber subscriber);

    /**
     * 获取下注总金额，派彩总金额（注单合计）
     */

    Subscription getAssets(Subscriber subscriber, String queryStartDate, String queryEndDate, String status, String code);

    /**
     * 获取30天的盈亏数据
     */
    Subscription getRecentProfit(Subscriber subscriber, String status, String code);

    /**
     * 获取注单详细
     */
    Subscription getOrderDetail(Subscriber subscriber, String id);

}
