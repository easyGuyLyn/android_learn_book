package com.dawoo.lotterybox.mvp.model.hall;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by b on 18-2-16.
 */

public interface IHallModel {
    /**
     * 轮播图
     * @param subscriber
     * @return
     */
    Subscription getBanner(Subscriber subscriber);

    /**
     * 获取公告
     * @param subscriber
     * @return
     */
    Subscription getBulletin(Subscriber subscriber);

    /**
     * 获取彩种类型及其子彩种的代号和名称
     * @param subscriber
     * @return
     */
    Subscription getTypeAndLottery(Subscriber subscriber);


}
