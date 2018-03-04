package com.dawoo.lotterybox.mvp.presenter;

import android.content.Context;

import com.dawoo.lotterybox.mvp.model.record.IRecordModel;
import com.dawoo.lotterybox.mvp.model.record.RecordModel;
import com.dawoo.lotterybox.mvp.view.IBaseView;
import com.dawoo.lotterybox.mvp.view.INoteRecordDetailView;
import com.dawoo.lotterybox.mvp.view.INoteRecordHisView;
import com.dawoo.lotterybox.net.rx.ProgressSubscriber;


import rx.Subscription;


/**
 * 记录相关的presenter
 * Created by benson on 18-1-7.
 */

public class RecordPresenter<T extends IBaseView> extends BasePresenter {
    private final Context mContext;
    private T mView;
    private final IRecordModel mModel;

    public RecordPresenter(Context context, T mView) {
        super(context, mView);

        mContext = context;
        this.mView = mView;
        mModel = new RecordModel();
    }

    /**
     * 玩家投注历史报表
     */
    public void getOrders(String code, String expect,
                          String status, String queryStartDate, String queryEndDate,
                          String pageSize, String pageNumber, String playModel) {
        Subscription subscription = mModel.getOrders(new ProgressSubscriber(o ->
                        ((INoteRecordHisView) mView).onRefreshResult(o), mContext),
                code,
                expect,
                status,
                queryStartDate,
                queryEndDate,
                pageSize,
                pageNumber,
                playModel);
        subList.add(subscription);
    }

    /**
     * 玩家投注历史报表 加载更多
     */
    public void getMoreOrders(String code, String expect,
                              String status, String queryStartDate, String queryEndDate,
                              String pageSize, String pageNumber, String playModel) {
        Subscription subscription = mModel.getOrders(new ProgressSubscriber(o ->
                        ((INoteRecordHisView) mView).onLoadMoreResult(o), mContext),
                code,
                expect,
                status,
                queryStartDate,
                queryEndDate,
                pageSize,
                pageNumber,
                playModel);
        subList.add(subscription);
    }


    /**
     * 获取所有彩种
     */
    public void getLottery() {
        Subscription subscription = mModel.getLottery(new ProgressSubscriber(o ->
                ((INoteRecordHisView) mView).onLotteryDataResult(o), mContext));
        subList.add(subscription);
    }

    /**
     * 获取下注总金额，派彩总金额（注单合计）
     */
    public void getAssets(String queryStartDate, String queryEndDate, String status, String code) {
        Subscription subscription = mModel.getAssets(new ProgressSubscriber(o ->
                ((INoteRecordHisView) mView).onAssetsResult(o)
                , mContext), queryStartDate, queryEndDate, status, code);
        subList.add(subscription);
    }

    /**
     * 获取30天内的盈亏数据
     */
    public void getRecentProfit(String status, String code) {
        Subscription subscription = mModel.getRecentProfit(new ProgressSubscriber(o ->
                ((INoteRecordHisView) mView).onRecentProfit(o)
                , mContext), status, code);
        subList.add(subscription);
    }

    /**
     * 获取注单详细
     */
    public void getOrderDetail(String id) {
        Subscription subscription = mModel.getOrderDetail(new ProgressSubscriber(o ->
                ((INoteRecordDetailView) mView).onRefreshResult(o)
                , mContext), id);
        subList.add(subscription);
    }


    @Override
    public void onDestory() {
        super.onDestory();
    }
}
