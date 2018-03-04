package com.dawoo.lotterybox.mvp.presenter;

import android.content.Context;

import com.dawoo.lotterybox.bean.BannerBean;
import com.dawoo.lotterybox.bean.Bulletin;
import com.dawoo.lotterybox.bean.LoginBean;
import com.dawoo.lotterybox.bean.lottery.LotteryType;
import com.dawoo.lotterybox.bean.lottery.TypeAndLotteryBean;
import com.dawoo.lotterybox.mvp.model.Lottery.ILotteryModel;
import com.dawoo.lotterybox.mvp.model.Lottery.LotteryModel;
import com.dawoo.lotterybox.mvp.model.hall.HallModel;
import com.dawoo.lotterybox.mvp.model.hall.IHallModel;
import com.dawoo.lotterybox.mvp.view.IBaseView;
import com.dawoo.lotterybox.mvp.view.IHallView;
import com.dawoo.lotterybox.mvp.view.ILoginView;
import com.dawoo.lotterybox.net.rx.ProgressSubscriber;

import java.util.List;

import rx.Subscription;

/**
 * 购彩大厅相关
 * Created by b on 18-2-16.
 */

public class HallPresenter<T extends IBaseView> extends BasePresenter{


    private final Context mContext;
    private T mView;
    private final IHallModel mModel;

    public HallPresenter(Context context, T mView) {
        super(context, mView);

        mContext = context;
        this.mView = mView;
        mModel = new HallModel();
    }

    /**
     * 轮播图
     */
    public void getBanner() {
        Subscription subscription = mModel.getBanner(new ProgressSubscriber(o ->
                        ((IHallView) mView).onBanner((List<BannerBean>) o), mContext));
        subList.add(subscription);
    }

    /**
     * 公告
     */
    public void getBulletin() {
        Subscription subscription = mModel.getBulletin(new ProgressSubscriber(o ->
                ((IHallView) mView).onBulletin((List<Bulletin>) o), mContext));
        subList.add(subscription);
    }

    /**
     * 获取彩种类型及其子彩种的代号和名称
     */
    public void getTypeAndLottery() {
        Subscription subscription = mModel.getTypeAndLottery(new ProgressSubscriber(o ->
                ((IHallView) mView).onTypeAndLottery((List<TypeAndLotteryBean>) o), mContext));
        subList.add(subscription);
    }

    @Override
    public void ondetach() {
        super.ondetach();
    }
}
