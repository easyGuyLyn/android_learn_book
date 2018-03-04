package com.dawoo.gamebox.mvp.model.user;

import com.dawoo.gamebox.bean.Logout;
import com.dawoo.gamebox.bean.ResetSecurityPwd;
import com.dawoo.gamebox.bean.UpdateLoginPwd;
import com.dawoo.gamebox.mvp.model.BaseModel;
import com.dawoo.gamebox.mvp.service.IUserService;
import com.dawoo.gamebox.net.RetrofitHelper;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;


/**
 * Created by benson on 17-12-21.
 */

public class UserModel extends BaseModel implements IUserModel {


    @Override
    public Subscription modifyLoginPwd(Subscriber subscriber, String oldPwd, String newPwd) {
        Observable<UpdateLoginPwd> observable = RetrofitHelper.getService(IUserService.class)
                .updateLoginPwd(oldPwd, newPwd);
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription modifyLoginPwd(Subscriber subscriber, String oldPwd, String newPwd, String code) {
        Observable<UpdateLoginPwd> observable = RetrofitHelper.getService(IUserService.class)
                .updateLoginPwd(oldPwd, newPwd, code);
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription initSecurityPwd(Subscriber subscriber) {
        Observable<ResetSecurityPwd> observable = RetrofitHelper.getService(IUserService.class)
                .initSafePassword();
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription setRealSafeName(Subscriber subscriber, String reaName) {
        Observable<ResetSecurityPwd> observable = RetrofitHelper.getService(IUserService.class)
                .setRealName(reaName);
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription modifySecurityPwd(Subscriber subscriber, Boolean needCaptcha, String realNames, String oldPwd, String newPwd, String confrmPwd, String code) {
        Observable<ResetSecurityPwd> observable = RetrofitHelper.getService(IUserService.class)
                .updateSafePassword(needCaptcha, realNames, oldPwd, newPwd, confrmPwd, code);
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription logOut(Subscriber subscriber) {
        Observable<Logout> observable = RetrofitHelper.getService(IUserService.class).logOut();
        return toSubscribe(observable, subscriber);
    }

}
