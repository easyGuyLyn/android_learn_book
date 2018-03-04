package com.dawoo.gamebox.mvp.presenter;

import android.content.Context;

import com.dawoo.gamebox.mvp.model.mine.MineModel;
import com.dawoo.gamebox.mvp.model.promo.PromoModel;
import com.dawoo.gamebox.mvp.view.IBaseView;
import com.dawoo.gamebox.mvp.view.IGetUserPlayerRecommendView;
import com.dawoo.gamebox.mvp.view.IMineFragmentView;
import com.dawoo.gamebox.mvp.view.IShareRuleRecordView;
import com.dawoo.gamebox.net.rx.ProgressSubscriber;
import com.dawoo.gamebox.net.rx.SubscriberOnNextListener;

import rx.Subscriber;
import rx.Subscription;


/**
 * 我的页面presenter
 */

public class MineFramentPresenter<T extends IBaseView> extends BasePresenter {

    private final Context mContext;
    private T mView;
    private final MineModel mModel;

    public MineFramentPresenter(Context context, T mView) {
        super(context, mView);

        mContext = context;
        this.mView = mView;
        mModel = new MineModel();
    }


    /**
     * 我的页面获取相关数据
     */
    public void getLink() {
        Subscription subscription = mModel.getLink(new ProgressSubscriber<>(o -> ((IMineFragmentView) mView).onLinkResult(o), mContext));
        subList.add(subscription);
    }


    public void getUserPlayerRecommend() {
        Subscription subscription = mModel.getUserPlayerRecommend(new ProgressSubscriber<>(o -> ((IGetUserPlayerRecommendView) mView).onResult(o), mContext));
        subList.add(subscription);
    }

    public void getShareUserPlayerRecommend() {
        Subscription subscription = mModel.getUserPlayerRecommend(new ProgressSubscriber<>(o -> ((IShareRuleRecordView) mView).onResult(o), mContext));
        subList.add(subscription);
    }


    @Override
    public void onDestory() {
        super.onDestory();
    }
}
