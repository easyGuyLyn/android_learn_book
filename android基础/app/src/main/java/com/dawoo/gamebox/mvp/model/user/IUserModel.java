package com.dawoo.gamebox.mvp.model.user;


import rx.Subscriber;
import rx.Subscription;

/**
 * Created by benson on 17-12-21.
 */

public interface IUserModel {
    Subscription modifyLoginPwd(Subscriber subscriber, String oldPwd, String newPwd);

    Subscription modifyLoginPwd(Subscriber subscriber, String oldPwd, String newPwd, String code);

    Subscription initSecurityPwd(Subscriber subscriber);

    Subscription setRealSafeName(Subscriber subscriber, String reaName);

    Subscription modifySecurityPwd(Subscriber subscriber, Boolean needCaptcha, String realNames,
                                   String oldPwd, String newPwd, String confrmPwd, String code);

    Subscription logOut(Subscriber subscriber);
}
