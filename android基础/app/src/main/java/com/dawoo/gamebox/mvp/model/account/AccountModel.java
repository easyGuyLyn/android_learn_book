package com.dawoo.gamebox.mvp.model.account;

import com.dawoo.gamebox.bean.UserAssert;
import com.dawoo.gamebox.bean.VideoGame;
import com.dawoo.gamebox.mvp.model.BaseModel;
import com.dawoo.gamebox.mvp.service.IAccountService;
import com.dawoo.gamebox.mvp.service.IGameService;
import com.dawoo.gamebox.net.RetrofitHelper;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by benson on 18-2-25.
 */

public class AccountModel extends BaseModel implements IAccountModel{
    @Override
    public Subscription getUserAssert(Subscriber subscriber) {
        Observable<UserAssert> observable = RetrofitHelper
                .getService(IAccountService.class)
                .getUserAssert()
                .map(new HttpResultFunc<>());
        return toSubscribe(observable, subscriber);
    }
}
