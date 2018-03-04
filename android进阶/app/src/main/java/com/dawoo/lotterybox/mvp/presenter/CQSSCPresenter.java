package com.dawoo.lotterybox.mvp.presenter;

import android.content.Context;

import com.dawoo.lotterybox.bean.AwardResultBean;
import com.dawoo.lotterybox.bean.CQSSCAwardResultBean;
import com.dawoo.lotterybox.bean.ExpectDataBean;
import com.dawoo.lotterybox.bean.LoginBean;
import com.dawoo.lotterybox.mvp.model.cqssc.CQSSCModel;
import com.dawoo.lotterybox.mvp.model.cqssc.ICQSSCModel;
import com.dawoo.lotterybox.mvp.model.hall.HallModel;
import com.dawoo.lotterybox.mvp.model.hall.IHallModel;
import com.dawoo.lotterybox.mvp.view.IBaseView;
import com.dawoo.lotterybox.mvp.view.ICQSSCView;
import com.dawoo.lotterybox.mvp.view.ILoginView;
import com.dawoo.lotterybox.net.rx.ProgressSubscriber;
import com.dawoo.lotterybox.net.rx.SubscriberOnNextListener;

import java.util.List;

import rx.Subscription;

/**
 * Created by b on 18-2-22.
 */

public class CQSSCPresenter<T extends IBaseView> extends BasePresenter {

    private static final String CQSSC = "cqssc";
    private static final String PAGE_SIZE = "120";
    private static final String PAGE_NUMBER = "1";

    private final Context mContext;
    private T mView;
    private final ICQSSCModel mModel;

    public CQSSCPresenter(Context context, T mView) {
        super(context, mView);

        mContext = context;
        this.mView = mView;
        mModel = new CQSSCModel();
    }

    /**
     * 获取近120期开奖数据
     * */

    public void getAwardResults(){
        Subscription subscription = mModel.getAwardResults(new ProgressSubscriber(o ->
                        ((ICQSSCView) mView).onAwardResults((List<CQSSCAwardResultBean>) o), mContext),
                CQSSC,
                PAGE_SIZE,
                PAGE_NUMBER);
        subList.add(subscription);
    }

    /**
     * 获取近120期开奖数据（包含未开奖数据）
     * */
    public void getAwardResultsAndNoOpen(){
        Subscription subscription = mModel.getAwardResultsAndNoOpen(new ProgressSubscriber(o ->
                        ((ICQSSCView) mView).onAwardResultsAndNoOpen((List<AwardResultBean>) o), mContext),
                CQSSC,
                PAGE_SIZE);
        subList.add(subscription);
    }

    /**
     * 获取盘口数据
     * */
    public void getExpectData(){
        Subscription subscription = mModel.getExpectData(new ProgressSubscriber<>(o ->
                ((ICQSSCView)mView).onExpectData((ExpectDataBean) o),mContext),
                CQSSC);
        subList.add(subscription);
    }

    /**
     * 获取赔率
     * */
    public void getLotteryOdd(String betCode){
        Subscription subscription = mModel.getLotteryOdd(new ProgressSubscriber<>(o ->
                        ((ICQSSCView)mView).onLotteryOdd(o),mContext),
                CQSSC,
                betCode
        );
        subList.add(subscription);
    }

    @Override
    public void onDestory() {
        super.onDestory();
    }
}
