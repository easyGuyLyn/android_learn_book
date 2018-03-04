package com.dawoo.lotterybox.mvp.model.user;


import com.dawoo.lotterybox.bean.LoginBean;
import com.dawoo.lotterybox.bean.User;
import com.dawoo.lotterybox.mvp.model.BaseModel;
import com.dawoo.lotterybox.mvp.service.IUserService;
import com.dawoo.lotterybox.net.RetrofitHelper;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;


/**
 * Created by benson on 17-12-21.
 */

public class UserModel extends BaseModel implements IUserModel {


    @Override
    public Subscription login(Subscriber subscriber, String name, String pwd, String appKey, String appSecret, String serialNo) {
        Observable<LoginBean> observable = RetrofitHelper
                .getService(IUserService.class)
                .login(name, pwd, appKey, appSecret, serialNo)
                .map(new HttpResultFunc<>());
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription register(Subscriber subscriber, String name, String pwd, String confirmPwd, String createChannel, String playerType, String mode) {
        Observable<Boolean> observable = RetrofitHelper
                .getService(IUserService.class)
                .register(name, pwd, confirmPwd, createChannel, playerType, mode)
                .map(new HttpResultFunc<>());
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription getUserInfo(Subscriber subscriber) {
        Observable<User> observable = RetrofitHelper.getService(IUserService.class).getUserInfo().map(new HttpResultFunc<>());
        return toSubscribe(observable, subscriber);
    }

}
