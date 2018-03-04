package com.dawoo.gamebox.mvp.presenter;

import android.content.Context;

import com.dawoo.gamebox.mvp.model.record.RecordModel;
import com.dawoo.gamebox.mvp.view.IBaseView;
import com.dawoo.gamebox.mvp.view.IMypromoListView;
import com.dawoo.gamebox.net.rx.ProgressSubscriber;

import rx.Subscription;

/**
 * Created by jack on 18-1-14.
 */

public class MyPromoPresenter<T extends IBaseView> extends BasePresenter {


    private final Context mContext;
    private T mView;
    private final RecordModel mModel;


    public MyPromoPresenter(Context Context, T mView) {
        super(Context, mView);

        mContext = Context;
        this.mView = mView;
        mModel = new RecordModel();
    }

    @Override
    public void onDestory() {
        super.onDestory();
    }

    /**
     * 加載普通數據
     *
     * @param pageNumber
     * @param pageSize
     */
    public void getCIMypromoListView(int pageNumber, int pageSize) {
        Subscription subscription = mModel.getMyPromo(new ProgressSubscriber(o ->
                        ((IMypromoListView) mView).onLoadResult(o), mContext),
                pageNumber,
                pageSize
        );
        subList.add(subscription);
    }


    /**
     * 加载更多数据
     *
     * @param pageNumber
     * @param pageSize
     */
    public void getCIMypromomoreListView(int pageNumber, int pageSize) {
        Subscription subscription = mModel.getMyPromo(new ProgressSubscriber(o ->
                        ((IMypromoListView) mView).loadMoreData(o), mContext),
                pageNumber,
                pageSize
        );
        subList.add(subscription);
    }
}
