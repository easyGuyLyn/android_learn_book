package com.dawoo.gamebox.mvp.model.promo;


import rx.Subscriber;
import rx.Subscription;

/**
 * 优惠记录
 * 优惠分类
 * 优惠列表
 * Created by benson on 17-12-21.
 */

public interface IPromoModel {

    Subscription getActivitysType(Subscriber subscriber);


    /**
     * 获取数据列表
     *
     * @param subscriber
     * @param pageNumber
     * @param pageSize
     * @param activityClassifyKey
     * @return
     */
    Subscription getActivityTypeList(Subscriber subscriber, int pageNumber, int pageSize, String activityClassifyKey);
}
