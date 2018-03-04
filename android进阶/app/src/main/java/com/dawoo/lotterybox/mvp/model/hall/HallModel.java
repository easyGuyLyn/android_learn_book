package com.dawoo.lotterybox.mvp.model.hall;

import com.dawoo.lotterybox.bean.BannerBean;
import com.dawoo.lotterybox.bean.Bulletin;
import com.dawoo.lotterybox.bean.lottery.LotteryLastOpenAndOpening;
import com.dawoo.lotterybox.bean.lottery.LotteryType;
import com.dawoo.lotterybox.bean.lottery.TypeAndLotteryBean;
import com.dawoo.lotterybox.mvp.model.BaseModel;
import com.dawoo.lotterybox.mvp.service.IHallService;
import com.dawoo.lotterybox.mvp.service.ILotteryService;
import com.dawoo.lotterybox.net.RetrofitHelper;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by b on 18-2-16.
 */

public class HallModel extends BaseModel implements IHallModel{
    @Override
    public Subscription getBanner(Subscriber subscriber) {
        Observable<List<BannerBean>> observable = RetrofitHelper
                .getService(IHallService.class)
                .getBanner()
                .map(new BaseModel.HttpResultFunc<List<BannerBean>>());
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription getBulletin(Subscriber subscriber) {
        Observable<List<Bulletin>> observable = RetrofitHelper
                .getService(IHallService.class)
                .getBulletin()
                .map(new BaseModel.HttpResultFunc<List<Bulletin>>());
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription getTypeAndLottery(Subscriber subscriber) {
        Observable<List<TypeAndLotteryBean>> observable = RetrofitHelper
                .getService(IHallService.class)
                .getTypeAndLottery()
                .map(new BaseModel.HttpResultFunc<List<TypeAndLotteryBean>>());
        return toSubscribe(observable, subscriber);
    }
}
