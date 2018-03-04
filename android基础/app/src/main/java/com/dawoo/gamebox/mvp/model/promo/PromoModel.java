package com.dawoo.gamebox.mvp.model.promo;

import com.dawoo.gamebox.bean.ActivityType;
import com.dawoo.gamebox.bean.ActivityTypeList;
import com.dawoo.gamebox.mvp.model.BaseModel;
import com.dawoo.gamebox.net.RetrofitHelper;
import com.dawoo.gamebox.mvp.service.IPromoService;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

/**
 * 优惠记录
 * Created by benson on 17-12-21.
 */

public class PromoModel extends BaseModel implements IPromoModel {

    @Override
    public Subscription getActivitysType(Subscriber subscriber) {
        Observable<List<ActivityType>> observable = RetrofitHelper.getService(IPromoService.class).getActivityType().map(new HttpResultFunc<List<ActivityType>>());
        return toSubscribe(observable, subscriber);
    }


    /**
     * 根据activityClassifyKey获取数据
     *
     * @param subscriber
     * @param pageNumber
     * @param pageSize
     * @param activityClassifyKey
     * @return
     */
    @Override
    public Subscription getActivityTypeList(Subscriber subscriber, int pageNumber, int pageSize, String activityClassifyKey) {
        Observable<ActivityTypeList> observable = RetrofitHelper
                .getService(IPromoService.class)
                .getActivityTypeList(activityClassifyKey)
                .map(new HttpResultFunc<ActivityTypeList>());
        return toSubscribe(observable, subscriber);
    }
//    /**
//     * 根据activityClassifyKey获取数据
//     *
//     * @param subscriber
//     * @param pageNumber
//     * @param pageSize
//     * @param activityClassifyKey
//     * @return
//     */
//    @Override
//    public Subscription getActivityTypeList(Subscriber subscriber, int pageNumber, int pageSize, String activityClassifyKey) {
//        Observable<ActivityTypeList> observable = RetrofitHelper
//                .getService(IPromoService.class)
//                .getActivityTypeList(pageNumber, pageSize, activityClassifyKey)
//                .map(new HttpResultFunc<ActivityTypeList>());
//        return toSubscribe(observable, subscriber);
//    }

}
