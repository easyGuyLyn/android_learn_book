package com.dawoo.gamebox.mvp.model.mine;

import com.dawoo.gamebox.bean.MineLink;
import com.dawoo.gamebox.bean.UserPlayerRecommend;
import com.dawoo.gamebox.mvp.model.BaseModel;
import com.dawoo.gamebox.mvp.service.IMineService;
import com.dawoo.gamebox.net.RetrofitHelper;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by benson on 18-1-4.
 */

public class MineModel extends BaseModel implements IMineModel {
    @Override
    public Subscription getLink(Subscriber subscriber) {
        Observable<MineLink> observable = RetrofitHelper.getService(IMineService.class).getLink().map(new HttpResultFunc<MineLink>());
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription getUserPlayerRecommend(Subscriber subscriber) {
        Observable<UserPlayerRecommend> observable = RetrofitHelper.getService(IMineService.class).getUserPlayerRecommend().map(new HttpResultFunc<UserPlayerRecommend>());
        return toSubscribe(observable, subscriber);
    }

}
