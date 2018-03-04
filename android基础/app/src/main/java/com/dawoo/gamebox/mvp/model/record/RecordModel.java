package com.dawoo.gamebox.mvp.model.record;

import com.dawoo.gamebox.bean.BettingDetail;
import com.dawoo.gamebox.bean.CapitalRecord;
import com.dawoo.gamebox.bean.CapitalRecordDetail;
import com.dawoo.gamebox.bean.CapitalRecordType;
import com.dawoo.gamebox.bean.MyPromo;
import com.dawoo.gamebox.bean.NoteRecord;
import com.dawoo.gamebox.mvp.model.BaseModel;
import com.dawoo.gamebox.mvp.service.IPromoService;
import com.dawoo.gamebox.mvp.service.IRecordService;
import com.dawoo.gamebox.net.RetrofitHelper;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

/**
 * 记录相关
 * Created by benson on 18-1-07.
 */

public class RecordModel extends BaseModel implements IRecordModel {
    public Subscription getNoteRecord(Subscriber subscriber,
                                      String beginBetTime,
                                      String endBetTime,
                                      int pageSize,
                                      int pageNumber,
                                      boolean isShowStatistics) {
        Observable<NoteRecord> observable = RetrofitHelper.getService(IRecordService.class).getNoteRecord(beginBetTime, endBetTime, pageSize, pageNumber,isShowStatistics).map(new HttpResultFunc<NoteRecord>());
        return toSubscribe(observable, subscriber);
    }


    @Override
    public Subscription getBettingDetail(Subscriber subscriber, int id) {
        Observable<BettingDetail> observable = RetrofitHelper.getService(IRecordService.class).getBettingDetail(id).map(new HttpResultFunc<BettingDetail>());
        return toSubscribe(observable, subscriber);
    }


    @Override
    public Subscription getCapitalRecord(Subscriber subscriber, String beginBetTime, String endBetTime, String transactionType, int pageNumber, int pageSize) {
        Observable<CapitalRecord> observable = RetrofitHelper.getService(IRecordService.class)
                .getCapitalRecord(beginBetTime, endBetTime, transactionType, pageNumber, pageSize)
                .map(new HttpResultFunc<CapitalRecord>());
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription getCapitalRecordType(Subscriber subscriber) {
        Observable<CapitalRecordType> observable = RetrofitHelper.getService(IRecordService.class)
                .getCapitalRecordType()
                .map(new HttpResultFunc<CapitalRecordType>());
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription getCapitalRecordDetail(Subscriber subscriber, int id) {
        Observable<CapitalRecordDetail> observable = RetrofitHelper.getService(IRecordService.class)
                .getCapitalRecordDetail(id)
                .map(new HttpResultFunc<CapitalRecordDetail>());
        return toSubscribe(observable, subscriber);
    }


    @Override
    public Subscription getMyPromo(Subscriber subscriber, int pageNumber, int pageSize) {
        Observable<MyPromo> observable = RetrofitHelper
                .getService(IPromoService.class)
                .getMyPromo(pageNumber, pageSize)
                .map(new HttpResultFunc<MyPromo>());
        return toSubscribe(observable, subscriber);
    }


}
