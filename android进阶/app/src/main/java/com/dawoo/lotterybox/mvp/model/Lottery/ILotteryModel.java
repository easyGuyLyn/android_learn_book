package com.dawoo.lotterybox.mvp.model.Lottery;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by benson on 18-2-8.
 */

public interface ILotteryModel {

    /**
     * 最近一期的彩票开奖
     * @param subscriber
     * @return
     */
    Subscription getLastOpenedAndOpeningResult(Subscriber subscriber);

    /**
     * 获取近期数据
     * @param subscriber
     * @param code
     * @param pageSize
     * @return
     */
    Subscription getRecentRecords(Subscriber subscriber,String code,String pageSize);

}
