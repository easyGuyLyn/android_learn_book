package com.dawoo.lotterybox.mvp.model.record;


import com.dawoo.lotterybox.bean.lottery.LotterySimpleBean;
import com.dawoo.lotterybox.bean.record.AssetsBean;
import com.dawoo.lotterybox.bean.record.NoTeRecordHisData;
import com.dawoo.lotterybox.bean.record.NoteRecordHis;
import com.dawoo.lotterybox.bean.record.ProfitBean;
import com.dawoo.lotterybox.mvp.model.BaseModel;
import com.dawoo.lotterybox.mvp.service.IRecordService;
import com.dawoo.lotterybox.net.HttpResult;
import com.dawoo.lotterybox.net.RetrofitHelper;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;


/**
 * Created by benson on 17-12-21.
 */

public class RecordModel extends BaseModel implements IRecordModel {
    @Override
    public Subscription getOrders(Subscriber subscriber, String code, String expect,
                                  String status, String queryStartDate, String queryEndDate,
                                  String pageSize, String pageNumber, String playModel) {
        Observable<NoTeRecordHisData> observable = RetrofitHelper
                .getService(IRecordService.class)
                .getOrders(code, expect, status, queryStartDate, queryEndDate, pageSize, pageNumber, playModel);
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription getLottery(Subscriber subscriber) {
        Observable<List<LotterySimpleBean>> observable = RetrofitHelper
                .getService(IRecordService.class)
                .getLottery()
                .map(new HttpResultFunc<>());
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription getAssets(Subscriber subscriber, String queryStartDate, String queryEndDate, String status, String code) {
        Observable<AssetsBean> observable = RetrofitHelper
                .getService(IRecordService.class)
                .getAssets(queryStartDate, queryEndDate, status, code)
                .map(new HttpResultFunc<>());
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription getRecentProfit(Subscriber subscriber, String status, String code) {
        Observable<List<ProfitBean>> observable = RetrofitHelper
                .getService(IRecordService.class)
                .getRecentProfit(status, code)
                .map(new HttpResultFunc<>());
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription getOrderDetail(Subscriber subscriber, String id) {
        Observable<NoteRecordHis> observable = RetrofitHelper
                .getService(IRecordService.class)
                .getOrderDetail(id)
                .map(new HttpResultFunc<>());
        return toSubscribe(observable, subscriber);
    }


}
