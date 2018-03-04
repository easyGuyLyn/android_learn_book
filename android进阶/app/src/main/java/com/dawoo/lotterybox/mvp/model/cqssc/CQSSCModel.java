package com.dawoo.lotterybox.mvp.model.cqssc;

import com.dawoo.lotterybox.bean.AwardResultBean;
import com.dawoo.lotterybox.bean.CQSSCAwardResultBean;
import com.dawoo.lotterybox.bean.ExpectDataBean;
import com.dawoo.lotterybox.bean.LoginBean;
import com.dawoo.lotterybox.mvp.model.BaseModel;
import com.dawoo.lotterybox.mvp.service.ICQSSCService;
import com.dawoo.lotterybox.net.RetrofitHelper;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by b on 18-2-22.
 */

public class CQSSCModel extends BaseModel implements ICQSSCModel {
    @Override
    public Subscription getAwardResults(Subscriber subscriber, String code, String pageSize, String pageNumber) {
        Observable<List<CQSSCAwardResultBean>> observable = RetrofitHelper
                .getService(ICQSSCService.class)
                .getAwardResults(code, pageSize, pageNumber)
                .map(new BaseModel.HttpResultFunc<List<CQSSCAwardResultBean>>());
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription getAwardResultsAndNoOpen(Subscriber subscriber, String code, String pageSize) {
        Observable<List<AwardResultBean>> observable = RetrofitHelper
                .getService(ICQSSCService.class)
                .getAwardResultsAndNoOpen(code, pageSize)
                .map(new BaseModel.HttpResultFunc<List<AwardResultBean>>());
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription getExpectData(Subscriber subscriber, String code) {
        Observable<ExpectDataBean> observable = RetrofitHelper
                .getService(ICQSSCService.class)
                .getExpectData(code)
                .map(new BaseModel.HttpResultFunc<ExpectDataBean>());
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription getLotteryOdd(Subscriber subscriber, String code, String betCode) {
        Observable<Object> observable = RetrofitHelper
                .getService(ICQSSCService.class)
                .getLotteryOdd(code,betCode)
                .map(new BaseModel.HttpResultFunc<Object>());
        return toSubscribe(observable, subscriber);
    }
}
