package com.dawoo.lotterybox.mvp.model.user;


import rx.Subscriber;
import rx.Subscription;

/**
 * Created by benson on 17-12-21.
 */

public interface IUserModel {
    Subscription login(Subscriber subscriber, String name, String pwd, String appKey, String appSecret, String serialNo);

    Subscription register(Subscriber subscriber, String name, String pwd, String confirmPwd, String createChannel, String playerType, String mode);

    Subscription getUserInfo(Subscriber subscriber);

}
