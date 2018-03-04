package com.dawoo.lotterybox.mvp.model.Lottery;

import com.dawoo.lotterybox.bean.lottery.HandicpWithOpening;
import com.dawoo.lotterybox.bean.lottery.LotteryLastOpenAndOpening;
import com.dawoo.lotterybox.mvp.model.BaseModel;
import com.dawoo.lotterybox.mvp.service.ILotteryService;
import com.dawoo.lotterybox.net.HttpResult;
import com.dawoo.lotterybox.net.RetrofitHelper;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by benson on 18-2-8.
 */

public class LotteryModel extends BaseModel implements ILotteryModel {


    @Override
    public Subscription getLastOpenedAndOpeningResult(Subscriber subscriber) {
        Observable<List<LotteryLastOpenAndOpening>> observable = RetrofitHelper
                .getService(ILotteryService.class)
                .getLastOpenedAndOpeningResult()
                .map(new HttpResultFunc<>());
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription getRecentRecords(Subscriber subscriber, String code, String pageSize) {
        Observable<List<HandicpWithOpening>> observable = RetrofitHelper
                .getService(ILotteryService.class)
                .getRecentRecords(code,pageSize)
                .map(new HttpResultFunc<>());
        return toSubscribe(observable, subscriber);
    }
}
