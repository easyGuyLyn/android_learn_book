package com.dawoo.gamebox.mvp.model.login;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by b on 18-1-25.
 */

public interface ILoginModel {


    Subscription verifyRealName(Subscriber subscriber,
                                String token,
                                String realName,
                                String playerAccount,
                                String playeAccount,
                                String tempPass ,
                                String newPassword);




}
